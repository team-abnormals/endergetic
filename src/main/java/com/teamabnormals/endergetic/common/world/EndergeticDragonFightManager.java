package com.teamabnormals.endergetic.common.world;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.teamabnormals.endergetic.common.world.features.EndergeticEndPodiumFeature;
import com.teamabnormals.endergetic.core.config.EEConfig;
import com.teamabnormals.endergetic.core.registry.EEBlocks;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.pattern.BlockPattern.BlockPatternMatch;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.TheEndPortalBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.util.Unit;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;

public final class EndergeticDragonFightManager extends EndDragonFight {
	private static final Logger LOGGER = LogManager.getLogger();
	private static final Predicate<Entity> VALID_PLAYER = EntitySelector.ENTITY_STILL_ALIVE.and(EntitySelector.withinDistance(0.0D, 128.0D, 0.0D, 192.0D));

	public EndergeticDragonFightManager(ServerLevel worldIn, long seed, CompoundTag compound) {
		super(worldIn, seed, compound);
	}

	@Override
	public void tick() {
		this.dragonEvent.setVisible(!this.dragonKilled);
		if (this.ticksSinceLastPlayerScan++ >= 20) {
			this.updatePlayers();
			this.ticksSinceLastPlayerScan = 0;
		}

		if (!this.dragonEvent.getPlayers().isEmpty()) {
			this.level.getChunkSource().addRegionTicket(TicketType.DRAGON, new ChunkPos(0, 0), 9, Unit.INSTANCE);
			boolean flag = this.isArenaLoaded();
			if (this.needsStateScanning && flag) {
				this.scanForLegacyFight();
				this.needsStateScanning = false;
			}

			if (this.respawnStage != null) {
				if (this.respawnCrystals == null && flag) {
					this.respawnStage = null;
					this.tryRespawn();
				}

				this.respawnStage.tick(this.level, this, this.respawnCrystals, this.respawnTime++, this.portalLocation);
			}

			if (!this.dragonKilled) {
				if ((this.dragonUUID == null || ++this.ticksSinceDragonSeen >= 1200) && flag) {
					this.findOrCreateDragon();
					this.ticksSinceDragonSeen = 0;
				}

				if (++this.ticksSinceCrystalsScanned >= 100 && flag) {
					this.updateCrystalCount();
					this.ticksSinceCrystalsScanned = 0;
				}
			}
		} else {
			this.level.getChunkSource().removeRegionTicket(TicketType.DRAGON, new ChunkPos(0, 0), 9, Unit.INSTANCE);
		}

		if (EEConfig.ValuesHolder.shouldDebugDragonFightManager()) {
			LOGGER.debug("Found exit portal: " + this.findEndergeticExitPortal(true));
			LOGGER.debug(this.portalLocation != null ? this.portalLocation.toString() : "null");
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
				this.spawnExitPortal(false);
			}
		}

		List<? extends EnderDragon> list = this.level.getDragons();
		if (list.isEmpty()) {
			this.dragonKilled = true;
		} else {
			EnderDragon enderdragonentity = list.get(0);
			this.dragonUUID = enderdragonentity.getUUID();
			LOGGER.info("Found that there's a dragon still alive ({})", enderdragonentity);
			this.dragonKilled = false;
			if (!flag) {
				LOGGER.info("But we didn't have a portal, let's remove it.");
				enderdragonentity.discard();
				this.dragonUUID = null;
			}
		}

		if (!this.previouslyKilled && this.dragonKilled) {
			this.dragonKilled = false;
		}
	}

	private void updatePlayers() {
		Set<ServerPlayer> set = Sets.newHashSet();

		for (ServerPlayer serverplayerentity : this.level.getPlayers(VALID_PLAYER)) {
			this.dragonEvent.addPlayer(serverplayerentity);
			set.add(serverplayerentity);
		}

		Set<ServerPlayer> set1 = Sets.newHashSet(this.dragonEvent.getPlayers());
		set1.removeAll(set);

		for (ServerPlayer serverplayerentity1 : set1) {
			this.dragonEvent.removePlayer(serverplayerentity1);
		}
	}

	private boolean exitPortalExists() {
		for (int i = -8; i <= 8; ++i) {
			for (int j = -8; j <= 8; ++j) {
				LevelChunk chunk = this.level.getChunk(i, j);

				for (BlockEntity tileentity : chunk.getBlockEntities().values()) {
					if (tileentity instanceof TheEndPortalBlockEntity) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public void tryRespawn() {
		if (this.dragonKilled && this.respawnStage == null) {
			BlockPos portalPos = this.portalLocation;
			if (portalPos == null) {
				LOGGER.debug("Tried to respawn, but need to find the portal first.");
				if (!this.findEndergeticExitPortal(false)) {
					LOGGER.debug("Couldn't find a portal, so we made one.");
					this.spawnExitPortal(true);
				} else {
					LOGGER.debug("Found the exit portal & temporarily using it.");
				}

				portalPos = this.portalLocation;
			}

			List<EndCrystal> list1 = Lists.newArrayList();
			BlockPos centerPos = portalPos.above(2);

			for (Direction direction : Direction.Plane.HORIZONTAL) {
				List<EndCrystal> list = this.level.getEntitiesOfClass(EndCrystal.class, new AABB(centerPos.relative(direction, 2)));
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
	public BlockPatternMatch findExitPortal() {
		return null;
	}

	/*
	 * New way of finding the portal
	 */
	public boolean findEndergeticExitPortal(boolean debug) {
		for (int i = -8; i <= 8; ++i) {
			for (int j = -8; j <= 8; ++j) {
				LevelChunk chunk = this.level.getChunk(i, j);

				for (BlockEntity tileentity : chunk.getBlockEntities().values()) {
					if (tileentity instanceof TheEndPortalBlockEntity) {
						BlockPos tilePos = tileentity.getBlockPos();
						for (Direction directions : Direction.Plane.HORIZONTAL) {
							if (this.level.getBlockState(tilePos.relative(directions)).getBlock() == EEBlocks.MYSTICAL_OBSIDIAN.get()) {
								BlockPos possiblePortalPos = tilePos.relative(directions).below();
								if (this.isPortalAtPos(possiblePortalPos)) {
									if (!debug) {
										this.portalLocation = possiblePortalPos;
									}
									return true;
								}
							}
						}
					}
				}
			}
		}

		int height = this.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, EndergeticEndPodiumFeature.END_PODIUM_LOCATION.above()).getY();

		for (int y = height; y >= 0; y--) {
			BlockPos pos = new BlockPos(EndergeticEndPodiumFeature.END_PODIUM_LOCATION.getX(), y, EndergeticEndPodiumFeature.END_PODIUM_LOCATION.getZ());

			if (this.isPortalAtPos(pos)) {
				if (!debug) {
					this.portalLocation = pos;
				}
				return true;
			}
		}
		return false;
	}

	private boolean isPortalAtPos(BlockPos pos) {
		BlockPos up = pos.above();
		ServerLevel world = this.level;
		boolean[] flag = new boolean[4];

		for (Direction directions : Direction.Plane.HORIZONTAL) {
			BlockPos side = up.relative(directions, 3);
			if (world.getBlockState(side).getBlock() == EEBlocks.MYSTICAL_OBSIDIAN_ACTIVATION_RUNE.get() || world.getBlockState(side).getBlock() == EEBlocks.ACTIVATED_MYSTICAL_OBSIDIAN_ACTIVATION_RUNE.get()) {
				if (world.getBlockState(side.relative(directions.getClockWise())).getBlock() == EEBlocks.MYSTICAL_OBSIDIAN_RUNE.get() && world.getBlockState(side.relative(directions.getCounterClockWise())).getBlock() == EEBlocks.MYSTICAL_OBSIDIAN_RUNE.get()) {
					if (world.getBlockState(up).getBlock() == EEBlocks.MYSTICAL_OBSIDIAN.get()) {
						if (world.getBlockState(up.north(2).east(2)).getBlock() == EEBlocks.MYSTICAL_OBSIDIAN.get() && world.getBlockState(up.north(2).west(2)).getBlock() == EEBlocks.MYSTICAL_OBSIDIAN.get() && world.getBlockState(up.south(2).east(2)).getBlock() == EEBlocks.MYSTICAL_OBSIDIAN.get() && world.getBlockState(up.south(2).west(2)).getBlock() == EEBlocks.MYSTICAL_OBSIDIAN.get()) {
							flag[directions.get3DDataValue() - 2] = true;
						}
					}
				}
			}
		}
		return flag[0] && flag[1] && flag[2] && flag[3];
	}

	@Override
	public void setDragonKilled(EnderDragon dragon) {
		if (dragon.getUUID().equals(this.dragonUUID)) {
			this.dragonEvent.setProgress(0.0F);
			this.dragonEvent.setVisible(false);
			this.spawnExitPortal(true);
			this.spawnNewGateway();
			if (!this.previouslyKilled) {
				this.level.setBlockAndUpdate(this.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, EndergeticEndPodiumFeature.END_PODIUM_LOCATION), Blocks.DRAGON_EGG.defaultBlockState());
			}

			this.previouslyKilled = true;
			this.dragonKilled = true;
		}
	}

	@Override
	public void spawnExitPortal(boolean active) {
		EndergeticEndPodiumFeature endpodium = new EndergeticEndPodiumFeature(active);
		if (this.portalLocation == null) {
			for (this.portalLocation = this.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EndergeticEndPodiumFeature.END_PODIUM_LOCATION).below(); this.level.getBlockState(this.portalLocation).getBlock() == Blocks.BEDROCK && this.portalLocation.getY() > this.level.getSeaLevel(); this.portalLocation = this.portalLocation.below()) {
				;
			}
		}
		endpodium.place(FeatureConfiguration.NONE, this.level, this.level.getChunkSource().getGenerator(), RandomSource.create(), this.portalLocation);
	}

	@Override
	protected void spawnNewGateway() {
		List<Integer> gateways = ObfuscationReflectionHelper.getPrivateValue(EndDragonFight.class, this, "f_64062_");
		if (!gateways.isEmpty()) {
			int removed = gateways.remove(gateways.size() - 1);
			BlockPos pos = new BlockPos(Mth.floor(96.0D * Math.cos(2.0D * (-Math.PI + 0.15707963267948966D * (double) removed))), 75, Mth.floor(96.0D * Math.sin(2.0D * (-Math.PI + 0.15707963267948966D * (double) removed))));
			ServerLevel level = this.level;
			level.levelEvent(3000, pos, 0);
			//Turns out datapacks can't replace the delayed end gateway. Silly Mojang!
			var endGateway = level.registryAccess().registryOrThrow(Registry.CONFIGURED_FEATURE_REGISTRY).get(new ResourceLocation("end_gateway_delayed"));
			if (endGateway != null) endGateway.place(this.level, this.level.getChunkSource().getGenerator(), RandomSource.create(), pos);
		}
	}
}