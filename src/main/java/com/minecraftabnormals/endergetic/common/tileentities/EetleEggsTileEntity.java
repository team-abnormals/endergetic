package com.minecraftabnormals.endergetic.common.tileentities;

import com.minecraftabnormals.endergetic.common.blocks.EetleEggsBlock;
import com.minecraftabnormals.endergetic.common.entities.eetle.ChargerEetleEntity;
import com.minecraftabnormals.endergetic.core.registry.EEEntities;
import com.minecraftabnormals.endergetic.core.registry.EETileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.Random;

public class EetleEggsTileEntity extends TileEntity implements ITickableTileEntity {
	private static final Random RANDOM = new Random();
	private final SackGrowth[] sackGrowths;
	private int hatchDelay = -18000;
	private int hatchProgress;

	public EetleEggsTileEntity() {
		super(EETileEntities.EETLE_EGGS.get());
		this.sackGrowths = new SackGrowth[]{
				new SackGrowth(),
				new SackGrowth(),
				new SackGrowth(),
				new SackGrowth()
		};
	}

	@Override
	public void tick() {
		World world = this.getWorld();
		if (world != null) {
			BlockPos pos = this.pos;
			if (world.isRemote) {
				for (SackGrowth growth : this.sackGrowths) {
					growth.tick();
				}
			} else if (world.getGameRules().get(GameRules.DO_MOB_SPAWNING).get() && !world.isRainingAt(pos) && world.getDifficulty() != Difficulty.PEACEFUL && !this.getBlockState().get(EetleEggsBlock.PETRIFIED)) {
				if (RANDOM.nextFloat() < 0.05F && this.hatchDelay < -60) {
					if (!world.getEntitiesWithinAABB(PlayerEntity.class, new AxisAlignedBB(pos).grow(1.0D), player -> player.isAlive() && !player.isSneaking() && !player.isInvisible() && !player.isCreative() && !player.isSpectator()).isEmpty()) {
						this.hatchDelay = -60;
						world.notifyBlockUpdate(this.getPos(), this.getBlockState(), this.getBlockState(), 3);
					}
				}

				int delay = this.hatchDelay;
				if (delay < 0) {
					this.updateHatchDelay(world, ++delay);
				} else if (delay > 0) {
					this.updateHatchDelay(world, --delay);
				} else {
					if (this.hatchProgress < 20 && RANDOM.nextFloat() < 0.9F) {
						world.notifyBlockUpdate(this.getPos(), this.getBlockState(), this.getBlockState(), 3);
						if (++this.hatchProgress >= 20) {
							world.destroyBlock(pos, false);
							int x = pos.getX();
							int y = pos.getY();
							int z = pos.getZ();
							BlockState state = this.getBlockState();
							Direction facing = state.get(EetleEggsBlock.FACING);
							float xOffset = facing.getXOffset();
							float yOffset = facing == Direction.DOWN ? 0.25F : 0.1F;
							float zOffset = facing.getZOffset();
							int size = state.get(EetleEggsBlock.SIZE);
							for (int i = 0; i <= size; i++) {
								ChargerEetleEntity chargerEetle = EEEntities.CHARGER_EETLE.get().create(world);
								if (chargerEetle != null) {
									chargerEetle.updateAge(-(RANDOM.nextInt(121) + 120));
									chargerEetle.setPositionAndRotation(x + RANDOM.nextFloat() * 0.5F + xOffset * 0.5F * RANDOM.nextFloat(), y + yOffset, z + RANDOM.nextFloat() * 0.5F + zOffset * 0.5F * RANDOM.nextFloat(), RANDOM.nextFloat() * 360.0F, 0.0F);
									world.addEntity(chargerEetle);
								}
							}
						}
					}
				}
			} else if (this.hatchDelay > -80 || this.hatchProgress > 0) {
				this.hatchProgress = 0;
				this.hatchDelay = -80;
				world.notifyBlockUpdate(pos, this.getBlockState(), this.getBlockState(), 3);
			}
		}
	}

	private void updateHatchDelay(World world, int hatchDelay) {
		int prevDelay = this.hatchDelay;
		this.hatchDelay = hatchDelay;
		if (prevDelay < 0 && hatchDelay >= 0 || prevDelay >= 0 && hatchDelay < 0) {
			world.notifyBlockUpdate(this.getPos(), this.getBlockState(), this.getBlockState(), 3);
		}
	}

	public SackGrowth[] getSackGrowths() {
		return this.sackGrowths;
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
		if (this.world != null) {
			this.read(this.world.getBlockState(packet.getPos()), packet.getNbtCompound());
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
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.putInt("HatchDelay", this.hatchDelay);
		compound.putInt("HatchProgress", this.hatchProgress);
		return compound;
	}

	@Override
	public void read(BlockState state, CompoundNBT compound) {
		super.read(state, compound);
		if (compound.contains("HatchDelay", Constants.NBT.TAG_INT)) {
			this.hatchDelay = compound.getInt("HatchDelay");
		}
		this.hatchProgress = MathHelper.clamp(compound.getInt("HatchProgress"), 0, 2);
	}

	@Override
	public CompoundNBT getUpdateTag() {
		return this.write(new CompoundNBT());
	}

	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(this.pos, 100, this.getUpdateTag());
	}

	@Override
	public double getMaxRenderDistanceSquared() {
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

		private void tick() {
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
