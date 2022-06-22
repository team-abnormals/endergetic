package com.minecraftabnormals.endergetic.common.tileentities;

import com.minecraftabnormals.endergetic.client.particles.EEParticles;
import com.minecraftabnormals.endergetic.client.particles.data.CorrockCrownParticleData;
import com.minecraftabnormals.endergetic.common.blocks.EetleEggBlock;
import com.minecraftabnormals.endergetic.common.entities.eetle.AbstractEetleEntity;
import com.minecraftabnormals.endergetic.core.registry.EEEntities;
import com.minecraftabnormals.endergetic.core.registry.EESounds;
import com.minecraftabnormals.endergetic.core.registry.EETileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.Random;

public class EetleEggTileEntity extends TileEntity implements ITickableTileEntity {
	private static final Random RANDOM = new Random();
	private final SackGrowth[] sackGrowths;
	private int hatchDelay = -30000 - RANDOM.nextInt(12001);
	private int hatchProgress;
	private boolean bypassesSpawningGameRule;
	public boolean fromBroodEetle;

	public EetleEggTileEntity() {
		super(EETileEntities.EETLE_EGG.get());
		this.sackGrowths = new SackGrowth[]{
				new SackGrowth(),
				new SackGrowth(),
				new SackGrowth(),
				new SackGrowth()
		};
	}

	@Override
	public void tick() {
		World world = this.getLevel();
		if (world != null) {
			BlockPos pos = this.worldPosition;
			if (world.isClientSide) {
				for (SackGrowth growth : this.sackGrowths) {
					growth.tick();
				}
			} else if ((world.getGameRules().getRule(GameRules.RULE_DOMOBSPAWNING).get() || this.bypassesSpawningGameRule) && !world.isRainingAt(pos) && world.getDifficulty() != Difficulty.PEACEFUL && !this.getBlockState().getValue(EetleEggBlock.PETRIFIED)) {
				if (RANDOM.nextFloat() < 0.05F && this.hatchDelay < -60) {
					if (!world.getEntitiesOfClass(PlayerEntity.class, new AxisAlignedBB(pos).inflate(1.0D), player -> player.isAlive() && !player.isShiftKeyDown() && !player.isInvisible() && !player.isCreative() && !player.isSpectator()).isEmpty()) {
						this.hatchDelay = -60;
						world.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
					}
				}

				int delay = this.hatchDelay;
				if (delay < 0) {
					if (!this.bypassesSpawningGameRule && delay > -300 && delay % 5 == 0 && world.getEntitiesOfClass(AbstractEetleEntity.class, new AxisAlignedBB(this.getBlockPos()).inflate(14.0D)).size() >= 7) {
						delay = -600 - RANDOM.nextInt(201);
					}

					this.updateHatchDelay(world, ++delay);
				} else if (delay > 0) {
					this.updateHatchDelay(world, --delay);
				} else {
					if (this.hatchProgress < 20 && RANDOM.nextFloat() < 0.9F) {
						world.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
						if (++this.hatchProgress >= 20) {
							world.destroyBlock(pos, false);
							int x = pos.getX();
							int y = pos.getY();
							int z = pos.getZ();
							BlockState state = this.getBlockState();
							Direction facing = state.getValue(EetleEggBlock.FACING);
							float xOffset = facing.getStepX();
							float yOffset = facing == Direction.DOWN ? 0.25F : 0.1F;
							float zOffset = facing.getStepZ();
							int size = state.getValue(EetleEggBlock.SIZE);
							boolean fromBroodEetle = this.fromBroodEetle;
							for (int i = 0; i <= size; i++) {
								AbstractEetleEntity eetle = RANDOM.nextFloat() < 0.6F ? EEEntities.CHARGER_EETLE.get().create(world) : EEEntities.GLIDER_EETLE.get().create(world);
								if (eetle != null) {
									eetle.markFromEgg();
									eetle.updateAge(-(RANDOM.nextInt(41) + 120));
									eetle.absMoveTo(x + RANDOM.nextFloat() * 0.5F + xOffset * 0.5F * RANDOM.nextFloat(), y + yOffset, z + RANDOM.nextFloat() * 0.5F + zOffset * 0.5F * RANDOM.nextFloat(), RANDOM.nextFloat() * 360.0F, 0.0F);
									if (fromBroodEetle) {
										eetle.applyDespawnTimer();
									}
									world.addFreshEntity(eetle);
								}
							}
							if (world instanceof ServerWorld) {
								((ServerWorld) world).sendParticles(new CorrockCrownParticleData(EEParticles.END_CROWN.get(), true), x + 0.5F, y + 0.25F * (size + 1.0F), z + 0.5F, 5 + size, 0.3F, 0.1F, 0.3F, 0.1D);
							}
						}
					}
				}
			} else if (this.hatchDelay > -80 || this.hatchProgress > 0) {
				this.hatchProgress = 0;
				this.hatchDelay = -80;
				world.sendBlockUpdated(pos, this.getBlockState(), this.getBlockState(), 3);
			}
			if (!world.isClientSide && RANDOM.nextFloat() <= 0.0025F) {
				world.playSound(null, pos, EESounds.EETLE_EGG_AMBIENT.get(), SoundCategory.BLOCKS, 0.25F + RANDOM.nextFloat() * 0.25F, (float) (0.9D + RANDOM.nextDouble() * 0.1D));
			}
		}
	}

	public void updateHatchDelay(World world, int hatchDelay) {
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
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
		if (this.level != null) {
			this.load(this.level.getBlockState(packet.getPos()), packet.getTag());
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
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);
		compound.putInt("HatchDelay", this.hatchDelay);
		compound.putInt("HatchProgress", this.hatchProgress);
		compound.putBoolean("BypassSpawningGameRule", this.bypassesSpawningGameRule);
		compound.putBoolean("FromBroodEetle", this.fromBroodEetle);
		return compound;
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		if (compound.contains("HatchDelay", Constants.NBT.TAG_INT)) {
			this.hatchDelay = compound.getInt("HatchDelay");
		}
		this.hatchProgress = MathHelper.clamp(compound.getInt("HatchProgress"), 0, 2);
		this.bypassesSpawningGameRule = compound.getBoolean("BypassSpawningGameRule");
		this.fromBroodEetle = compound.getBoolean("FromBroodEetle");
	}

	@Override
	public CompoundNBT getUpdateTag() {
		return this.save(new CompoundNBT());
	}

	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(this.worldPosition, 100, this.getUpdateTag());
	}

	@Override
	public double getViewDistance() {
		return 128.0D;
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
			return 1.0F + MathHelper.lerp(partialTicks, this.prevGrowth, this.growth);
		}

		public float getGrowthMultiplied(float partialTicks, float multiplier) {
			return 1.0F + multiplier * MathHelper.lerp(partialTicks, this.prevGrowth, this.growth);
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
