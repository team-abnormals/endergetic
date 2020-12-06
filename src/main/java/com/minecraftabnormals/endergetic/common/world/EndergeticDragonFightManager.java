package com.minecraftabnormals.endergetic.common.world;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.minecraftabnormals.endergetic.common.world.features.EEFeatures;
import com.minecraftabnormals.endergetic.common.world.features.EndergeticEndPodiumFeature;
import com.minecraftabnormals.endergetic.core.config.EEConfig;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;

import net.minecraft.block.Blocks;
import net.minecraft.block.pattern.BlockPattern.PatternHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.item.EnderCrystalEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.EndPortalTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.Unit;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.end.DragonFightManager;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.EndGatewayConfig;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.server.TicketType;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public final class EndergeticDragonFightManager extends DragonFightManager {
	private static final Logger LOGGER = LogManager.getLogger();
	private static final Predicate<Entity> VALID_PLAYER = EntityPredicates.IS_ALIVE.and(EntityPredicates.withinRange(0.0D, 128.0D, 0.0D, 192.0D));

	public EndergeticDragonFightManager(ServerWorld worldIn, long seed, CompoundNBT compound) {
		super(worldIn, seed, compound);
	}

	@Override
	public void tick() {
		this.bossInfo.setVisible(!this.dragonKilled);
		if (this.ticksSinceLastPlayerScan++ >= 20) {
			this.updatePlayers();
			this.ticksSinceLastPlayerScan = 0;
		}

		if (!this.bossInfo.getPlayers().isEmpty()) {
			this.world.getChunkProvider().registerTicket(TicketType.DRAGON, new ChunkPos(0, 0), 9, Unit.INSTANCE);
			boolean flag = this.isFightAreaLoaded();
			if (this.scanForLegacyFight && flag) {
				this.scanForLegacyFight();
				this.scanForLegacyFight = false;
			}

			if (this.respawnState != null) {
				if (this.crystals == null && flag) {
					this.respawnState = null;
					this.tryRespawnDragon();
				}

				this.respawnState.process(this.world, this, this.crystals, this.respawnStateTicks++, this.exitPortalLocation);
			}

			if (!this.dragonKilled) {
				if ((this.dragonUniqueId == null || ++this.ticksSinceDragonSeen >= 1200) && flag) {
					this.findOrCreateDragon();
					this.ticksSinceDragonSeen = 0;
				}

				if (++this.ticksSinceCrystalsScanned >= 100 && flag) {
					this.findAliveCrystals();
					this.ticksSinceCrystalsScanned = 0;
				}
			}
		} else {
			this.world.getChunkProvider().releaseTicket(TicketType.DRAGON, new ChunkPos(0, 0), 9, Unit.INSTANCE);
		}

		if (EEConfig.ValuesHolder.shouldDebugDragonFightManager()) {
			LOGGER.debug("Found exit portal: " + this.findEndergeticExitPortal(true));
			LOGGER.debug(this.exitPortalLocation != null ? this.exitPortalLocation.toString() : "null");
		}
	}

	private void scanForLegacyFight() {
		LOGGER.info("Scanning for legacy world dragon fight...");
		boolean flag = this.exitPortalExists();
		if (flag) {
			LOGGER.info("Found that the dragon has been killed in this world already.");
			this.previouslyKilled = true;
		} else {
			LOGGER.info("Found that the dragon has not yet been killed in this world.");
			this.previouslyKilled = false;
			if (!this.findEndergeticExitPortal(false)) {
				this.generatePortal(false);
			}
		}

		List<EnderDragonEntity> list = this.world.getDragons();
		if (list.isEmpty()) {
			this.dragonKilled = true;
		} else {
			EnderDragonEntity enderdragonentity = list.get(0);
			this.dragonUniqueId = enderdragonentity.getUniqueID();
			LOGGER.info("Found that there's a dragon still alive ({})", (Object) enderdragonentity);
			this.dragonKilled = false;
			if (!flag) {
				LOGGER.info("But we didn't have a portal, let's remove it.");
				enderdragonentity.remove();
				this.dragonUniqueId = null;
			}
		}

		if (!this.previouslyKilled && this.dragonKilled) {
			this.dragonKilled = false;
		}
	}

	private void updatePlayers() {
		Set<ServerPlayerEntity> set = Sets.newHashSet();

		for (ServerPlayerEntity serverplayerentity : this.world.getPlayers(VALID_PLAYER)) {
			this.bossInfo.addPlayer(serverplayerentity);
			set.add(serverplayerentity);
		}

		Set<ServerPlayerEntity> set1 = Sets.newHashSet(this.bossInfo.getPlayers());
		set1.removeAll(set);

		for (ServerPlayerEntity serverplayerentity1 : set1) {
			this.bossInfo.removePlayer(serverplayerentity1);
		}
	}

	private boolean exitPortalExists() {
		for (int i = -8; i <= 8; ++i) {
			for (int j = -8; j <= 8; ++j) {
				Chunk chunk = this.world.getChunk(i, j);

				for (TileEntity tileentity : chunk.getTileEntityMap().values()) {
					if (tileentity instanceof EndPortalTileEntity) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public void tryRespawnDragon() {
		if (this.dragonKilled && this.respawnState == null) {
			BlockPos portalPos = this.exitPortalLocation;
			if (portalPos == null) {
				LOGGER.debug("Tried to respawn, but need to find the portal first.");
				if (!this.findEndergeticExitPortal(false)) {
					LOGGER.debug("Couldn't find a portal, so we made one.");
					this.generatePortal(true);
				} else {
					LOGGER.debug("Found the exit portal & temporarily using it.");
				}

				portalPos = this.exitPortalLocation;
			}

			List<EnderCrystalEntity> list1 = Lists.newArrayList();
			BlockPos centerPos = portalPos.up(2);

			for (Direction direction : Direction.Plane.HORIZONTAL) {
				List<EnderCrystalEntity> list = this.world.getEntitiesWithinAABB(EnderCrystalEntity.class, new AxisAlignedBB(centerPos.offset(direction, 2)));
				if (list.isEmpty()) return;

				list1.addAll(list);
			}

			LOGGER.debug("Found all crystals, respawning dragon.");
			this.respawnDragon(list1);
		}
	}

	//Not used anymore
	@Override
	@Nullable
	public PatternHelper findExitPortal() {
		return null;
	}

	/*
	 * New way of finding the portal
	 */
	public boolean findEndergeticExitPortal(boolean debug) {
		for (int i = -8; i <= 8; ++i) {
			for (int j = -8; j <= 8; ++j) {
				Chunk chunk = this.world.getChunk(i, j);

				for (TileEntity tileentity : chunk.getTileEntityMap().values()) {
					if (tileentity instanceof EndPortalTileEntity) {
						BlockPos tilePos = tileentity.getPos();
						for (Direction directions : Direction.Plane.HORIZONTAL) {
							if (this.world.getBlockState(tilePos.offset(directions)).getBlock() == EEBlocks.MYSTICAL_OBSIDIAN.get()) {
								BlockPos possiblePortalPos = tilePos.offset(directions).down();
								if (this.isPortalAtPos(possiblePortalPos)) {
									if (!debug) {
										this.exitPortalLocation = possiblePortalPos;
									}
									return true;
								}
							}
						}
					}
				}
			}
		}

		int height = this.world.getHeight(Heightmap.Type.MOTION_BLOCKING, EndergeticEndPodiumFeature.END_PODIUM_LOCATION.up()).getY();

		for (int y = height; y >= 0; y--) {
			BlockPos pos = new BlockPos(EndergeticEndPodiumFeature.END_PODIUM_LOCATION.getX(), y, EndergeticEndPodiumFeature.END_PODIUM_LOCATION.getZ());

			if (this.isPortalAtPos(pos)) {
				if (!debug) {
					this.exitPortalLocation = pos;
				}
				return true;
			}
		}
		return false;
	}

	private boolean isPortalAtPos(BlockPos pos) {
		BlockPos up = pos.up();
		ServerWorld world = this.world;
		boolean[] flag = new boolean[4];

		for (Direction directions : Direction.Plane.HORIZONTAL) {
			BlockPos side = up.offset(directions, 3);
			if (world.getBlockState(side).getBlock() == EEBlocks.MYSTICAL_OBSIDIAN_ACTIVATION_RUNE.get() || world.getBlockState(side).getBlock() == EEBlocks.ACTIVATED_MYSTICAL_OBSIDIAN_ACTIVATION_RUNE.get()) {
				if (world.getBlockState(side.offset(directions.rotateY())).getBlock() == EEBlocks.MYSTICAL_OBSIDIAN_RUNE.get() && world.getBlockState(side.offset(directions.rotateYCCW())).getBlock() == EEBlocks.MYSTICAL_OBSIDIAN_RUNE.get()) {
					if (world.getBlockState(up).getBlock() == EEBlocks.MYSTICAL_OBSIDIAN.get()) {
						if (world.getBlockState(up.north(2).east(2)).getBlock() == EEBlocks.MYSTICAL_OBSIDIAN.get() && world.getBlockState(up.north(2).west(2)).getBlock() == EEBlocks.MYSTICAL_OBSIDIAN.get() && world.getBlockState(up.south(2).east(2)).getBlock() == EEBlocks.MYSTICAL_OBSIDIAN.get() && world.getBlockState(up.south(2).west(2)).getBlock() == EEBlocks.MYSTICAL_OBSIDIAN.get()) {
							flag[directions.getIndex() - 2] = true;
						}
					}
				}
			}
		}
		return flag[0] && flag[1] && flag[2] && flag[3];
	}

	@Override
	public void processDragonDeath(EnderDragonEntity dragon) {
		if (dragon.getUniqueID().equals(this.dragonUniqueId)) {
			this.bossInfo.setPercent(0.0F);
			this.bossInfo.setVisible(false);
			this.generatePortal(true);
			this.spawnNewGateway();
			if (!this.previouslyKilled) {
				this.world.setBlockState(this.world.getHeight(Heightmap.Type.MOTION_BLOCKING, EndergeticEndPodiumFeature.END_PODIUM_LOCATION), Blocks.DRAGON_EGG.getDefaultState());
			}

			this.previouslyKilled = true;
			this.dragonKilled = true;
		}
	}

	@Override
	public void generatePortal(boolean active) {
		EndergeticEndPodiumFeature endpodium = new EndergeticEndPodiumFeature(active);
		if (this.exitPortalLocation == null) {
			for (this.exitPortalLocation = this.world.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EndergeticEndPodiumFeature.END_PODIUM_LOCATION).down(); this.world.getBlockState(this.exitPortalLocation).getBlock() == Blocks.BEDROCK && this.exitPortalLocation.getY() > this.world.getSeaLevel(); this.exitPortalLocation = this.exitPortalLocation.down()) {
				;
			}
		}
		endpodium.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).generate(this.world, this.world.getChunkProvider().getChunkGenerator(), new Random(), this.exitPortalLocation);
	}

	@Override
	protected void spawnNewGateway() {
		List<Integer> gateways = ObfuscationReflectionHelper.getPrivateValue(DragonFightManager.class, this, "field_186111_e");
		if (!gateways.isEmpty()) {
			int removed = gateways.remove(gateways.size() - 1);
			BlockPos pos = new BlockPos(MathHelper.floor(96.0D * Math.cos(2.0D * (-Math.PI + 0.15707963267948966D * (double) removed))), 75, MathHelper.floor(96.0D * Math.sin(2.0D * (-Math.PI + 0.15707963267948966D * (double) removed))));
			this.world.playEvent(3000, pos, 0);
			EEFeatures.ENDERGETIC_GATEWAY.get().withConfiguration(EndGatewayConfig.func_214698_a()).generate(this.world, this.world.getChunkProvider().getChunkGenerator(), new Random(), pos);
		}
	}
}