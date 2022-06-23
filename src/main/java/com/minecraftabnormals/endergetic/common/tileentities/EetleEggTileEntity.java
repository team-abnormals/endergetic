package com.minecraftabnormals.endergetic.common.tileentities;

import com.minecraftabnormals.endergetic.client.particles.EEParticles;
import com.minecraftabnormals.endergetic.client.particles.data.CorrockCrownParticleData;
import com.minecraftabnormals.endergetic.common.blocks.EetleEggBlock;
import com.minecraftabnormals.endergetic.common.entities.eetle.AbstractEetleEntity;
import com.minecraftabnormals.endergetic.core.registry.EEEntities;
import com.minecraftabnormals.endergetic.core.registry.EESounds;
import com.minecraftabnormals.endergetic.core.registry.EETileEntities;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

import javax.annotation.Nullable;
import java.util.Random;

public class EetleEggTileEntity extends BlockEntity {
	private static final Random RANDOM = new Random();
	private final SackGrowth[] sackGrowths;
	private int hatchDelay = -30000 - RANDOM.nextInt(12001);
	private int hatchProgress;
	private boolean bypassesSpawningGameRule;
	public boolean fromBroodEetle;

	public EetleEggTileEntity(BlockPos pos, BlockState state) {
		super(EETileEntities.EETLE_EGG.get(), pos, state);
		this.sackGrowths = new SackGrowth[]{
				new SackGrowth(),
				new SackGrowth(),
				new SackGrowth(),
				new SackGrowth()
		};
	}

	public static void tick(Level level, BlockPos pos, BlockState state, EetleEggTileEntity eetleEgg) {
		if (level != null) {
			if (level.isClientSide) {
				for (SackGrowth growth : eetleEgg.sackGrowths) {
					growth.tick();
				}
			} else if ((level.getGameRules().getRule(GameRules.RULE_DOMOBSPAWNING).get() || eetleEgg.bypassesSpawningGameRule) && !level.isRainingAt(pos) && level.getDifficulty() != Difficulty.PEACEFUL && !eetleEgg.getBlockState().getValue(EetleEggBlock.PETRIFIED)) {
				if (RANDOM.nextFloat() < 0.05F && eetleEgg.hatchDelay < -60) {
					if (!level.getEntitiesOfClass(Player.class, new AABB(pos).inflate(1.0D), player -> player.isAlive() && !player.isShiftKeyDown() && !player.isInvisible() && !player.isCreative() && !player.isSpectator()).isEmpty()) {
						eetleEgg.hatchDelay = -60;
						level.sendBlockUpdated(eetleEgg.getBlockPos(), eetleEgg.getBlockState(), eetleEgg.getBlockState(), 3);
					}
				}

				int delay = eetleEgg.hatchDelay;
				if (delay < 0) {
					if (!eetleEgg.bypassesSpawningGameRule && delay > -300 && delay % 5 == 0 && level.getEntitiesOfClass(AbstractEetleEntity.class, new AABB(eetleEgg.getBlockPos()).inflate(14.0D)).size() >= 7) {
						delay = -600 - RANDOM.nextInt(201);
					}

					eetleEgg.updateHatchDelay(level, ++delay);
				} else if (delay > 0) {
					eetleEgg.updateHatchDelay(level, --delay);
				} else {
					if (eetleEgg.hatchProgress < 20 && RANDOM.nextFloat() < 0.9F) {
						level.sendBlockUpdated(eetleEgg.getBlockPos(), eetleEgg.getBlockState(), eetleEgg.getBlockState(), 3);
						if (++eetleEgg.hatchProgress >= 20) {
							level.destroyBlock(pos, false);
							int x = pos.getX();
							int y = pos.getY();
							int z = pos.getZ();
							Direction facing = state.getValue(EetleEggBlock.FACING);
							float xOffset = facing.getStepX();
							float yOffset = facing == Direction.DOWN ? 0.25F : 0.1F;
							float zOffset = facing.getStepZ();
							int size = state.getValue(EetleEggBlock.SIZE);
							boolean fromBroodEetle = eetleEgg.fromBroodEetle;
							for (int i = 0; i <= size; i++) {
								AbstractEetleEntity eetle = RANDOM.nextFloat() < 0.6F ? EEEntities.CHARGER_EETLE.get().create(level) : EEEntities.GLIDER_EETLE.get().create(level);
								if (eetle != null) {
									eetle.markFromEgg();
									eetle.updateAge(-(RANDOM.nextInt(41) + 120));
									eetle.absMoveTo(x + RANDOM.nextFloat() * 0.5F + xOffset * 0.5F * RANDOM.nextFloat(), y + yOffset, z + RANDOM.nextFloat() * 0.5F + zOffset * 0.5F * RANDOM.nextFloat(), RANDOM.nextFloat() * 360.0F, 0.0F);
									if (fromBroodEetle) {
										eetle.applyDespawnTimer();
									}
									level.addFreshEntity(eetle);
								}
							}
							if (level instanceof ServerLevel) {
								((ServerLevel) level).sendParticles(new CorrockCrownParticleData(EEParticles.END_CROWN.get(), true), x + 0.5F, y + 0.25F * (size + 1.0F), z + 0.5F, 5 + size, 0.3F, 0.1F, 0.3F, 0.1D);
							}
						}
					}
				}
			} else if (eetleEgg.hatchDelay > -80 || eetleEgg.hatchProgress > 0) {
				eetleEgg.hatchProgress = 0;
				eetleEgg.hatchDelay = -80;
				level.sendBlockUpdated(pos, eetleEgg.getBlockState(), eetleEgg.getBlockState(), 3);
			}
			if (!level.isClientSide && RANDOM.nextFloat() <= 0.0025F) {
				level.playSound(null, pos, EESounds.EETLE_EGG_AMBIENT.get(), SoundSource.BLOCKS, 0.25F + RANDOM.nextFloat() * 0.25F, (float) (0.9D + RANDOM.nextDouble() * 0.1D));
			}
		}
	}

	public void updateHatchDelay(Level world, int hatchDelay) {
		int prevDelay = this.hatchDelay;
		this.hatchDelay = hatchDelay;
		if (prevDelay < 0 && hatchDelay >= 0 || prevDelay >= 0 && hatchDelay < 0) {
			world.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
		}
	}

	public int getHatchDelay() {
		return this.hatchDelay;
	}

	public void bypassSpawningGameRule() {
		this.bypassesSpawningGameRule = true;
	}

	public SackGrowth[] getSackGrowths() {
		return this.sackGrowths;
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet) {
		if (this.level != null) {
			this.load(packet.getTag());
			if (this.hatchProgress > 0) {
				for (SackGrowth growth : this.sackGrowths) {
					growth.stage = SackGrowth.Stage.BURSTING;
					growth.cooldown = Math.max(0, growth.cooldown - 15);
				}
			} else {
				SackGrowth.Stage growthStage = this.hatchDelay >= -60 ? SackGrowth.Stage.HATCHING : SackGrowth.Stage.IDLE;
				for (SackGrowth growth : this.sackGrowths) {
					growth.stage = growthStage;
				}
			}
		}
	}

	@Override
	protected void saveAdditional(CompoundTag compound) {
		super.saveAdditional(compound);

		compound.putInt("HatchDelay", this.hatchDelay);
		compound.putInt("HatchProgress", this.hatchProgress);
		compound.putBoolean("BypassSpawningGameRule", this.bypassesSpawningGameRule);
		compound.putBoolean("FromBroodEetle", this.fromBroodEetle);
	}

	@Override
	public void load(CompoundTag compound) {
		super.load(compound);

		if (compound.contains("HatchDelay", 3)) {
			this.hatchDelay = compound.getInt("HatchDelay");
		}

		this.hatchProgress = Mth.clamp(compound.getInt("HatchProgress"), 0, 2);
		this.bypassesSpawningGameRule = compound.getBoolean("BypassSpawningGameRule");
		this.fromBroodEetle = compound.getBoolean("FromBroodEetle");
	}

	@Override
	public CompoundTag getUpdateTag() {
		return this.saveWithoutMetadata();
	}

	@Nullable
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	public static class SackGrowth {
		private Stage stage = Stage.IDLE;
		private int cooldown;
		private float prevGrowth;
		private float growth;

		public SackGrowth() {
			this.cooldown += RANDOM.nextInt(21) + 5;
		}

		public void tick() {
			this.prevGrowth = this.growth;
			Stage stage = this.stage;
			if (this.cooldown > 0) {
				this.cooldown--;
				this.growth = Math.max(0.0F, this.growth - stage.growthSpeed);
			} else {
				float maxGrowth = stage.maxGrowth;
				this.growth = Math.min(maxGrowth, this.growth + stage.growthSpeed);
				if (this.growth == maxGrowth) {
					this.cooldown += ((float) RANDOM.nextInt(36) + 25) * stage.cooldownMultiplier;
				}
			}
		}

		public float getGrowth(float partialTicks) {
			return 1.0F + Mth.lerp(partialTicks, this.prevGrowth, this.growth);
		}

		public float getGrowthMultiplied(float partialTicks, float multiplier) {
			return 1.0F + multiplier * Mth.lerp(partialTicks, this.prevGrowth, this.growth);
		}

		enum Stage {
			IDLE(0.0075F, 0.15F, 1.0F),
			HATCHING(0.01875F, 0.225F, 0.35F),
			BURSTING(0.0275F, 0.45F, 0.0F);

			private final float growthSpeed;
			private final float maxGrowth;
			private final float cooldownMultiplier;

			Stage(float growthSpeed, float maxGrowth, float cooldownMultiplier) {
				this.growthSpeed = growthSpeed;
				this.maxGrowth = maxGrowth;
				this.cooldownMultiplier = cooldownMultiplier;
			}
		}
	}
}
