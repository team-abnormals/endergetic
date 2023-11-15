package com.teamabnormals.endergetic.common.entity.bolloom;

import com.teamabnormals.endergetic.common.block.poise.BolloomBudBlock;
import com.teamabnormals.endergetic.core.registry.EEBlocks;
import com.teamabnormals.endergetic.core.registry.EEEntityTypes;
import com.teamabnormals.endergetic.core.registry.EEItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.PlayMessages;

/**
 * @author - SmellyModder (Luke Tonon)
 */
public class BolloomFruit extends AbstractBolloom {
	private static final EntityDataAccessor<BlockPos> BUD_POS = SynchedEntityData.defineId(BolloomFruit.class, EntityDataSerializers.BLOCK_POS);
	private static final EntityDataAccessor<Integer> VINE_HEIGHT = SynchedEntityData.defineId(BolloomFruit.class, EntityDataSerializers.INT);

	public BolloomFruit(EntityType<? extends BolloomFruit> type, Level world) {
		super(EEEntityTypes.BOLLOOM_FRUIT.get(), world);
		this.setNoGravity(true);
	}

	public BolloomFruit(PlayMessages.SpawnEntity spawnEntity, Level world) {
		this(EEEntityTypes.BOLLOOM_FRUIT.get(), world);
	}

	public BolloomFruit(Level world, BlockPos budPos, BlockPos origin, int height, Direction direction) {
		this(EEEntityTypes.BOLLOOM_FRUIT.get(), world);
		float xPos = origin.getX() + 0.5F + 0.8125F * direction.getStepX();
		float zPos = origin.getZ() + 0.5F + 0.8125F * direction.getStepZ();
		float yPos = origin.getY() + 1.15F;

		this.setPos(xPos, yPos, zPos);
		this.setOrigin(xPos, yPos, zPos);
		this.setBudPos(budPos);
		this.setVineHeight(height);

		this.xo = this.getX();
		this.yo = this.getY();
		this.zo = this.getZ();
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(VINE_HEIGHT, 1);
		this.entityData.define(BUD_POS, BlockPos.ZERO);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putLong("BudPosition", this.getBudPos().asLong());
		compound.putInt("VineHeight", this.getVineHeight());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.setBudPos(BlockPos.of(compound.getLong("BudPosition")));
		if (compound.contains("VineHeight", 3)) {
			this.setVineHeight(compound.getInt("VineHeight"));
		} else {
			this.setVineHeight(1);
		}
	}

	private void setBudPos(BlockPos budPos) {
		this.entityData.set(BUD_POS, budPos);
	}

	private BlockPos getBudPos() {
		return this.entityData.get(BUD_POS);
	}

	@Override
	public float getVineXRot() {
		return (float) Math.atan(this.getSway() / this.getVineHeight());
	}

	public void setVineHeight(int height) {
		this.entityData.set(VINE_HEIGHT, height);
	}

	public int getVineHeight() {
		return this.entityData.get(VINE_HEIGHT);
	}

	@Override
	public void updatePositionAndMotion(double angleX, double angleZ) {
		if (!this.isUntied()) {
			float sway = this.getSway();
			this.setPos(
					this.getOriginX() + sway * angleX,
					this.getOriginY(),
					this.getOriginZ() + sway * angleZ
			);
		} else {
			this.move(MoverType.SELF, this.getDeltaMovement());
			this.setDeltaMovement(angleX * 0.05F, 0.07F, angleZ * 0.05F);
		}
	}

	@Override
	public void updateUntied() {
		BlockPos budPos = this.getBudPos();
		if (this.level.isAreaLoaded(budPos, 1)) {
			if (this.level.getBlockState(budPos).getBlock() != EEBlocks.BOLLOOM_BUD.get() || !this.level.getBlockState(budPos).getValue(BolloomBudBlock.OPENED)) {
				this.setUntied(true);
			}
		}

		if (!this.isOpenPathBelowFruit()) {
			this.setUntied(true);
		}
	}

	@Override
	public void onBroken(boolean dropFruit) {
		super.onBroken(dropFruit);
		if (dropFruit) {
			Block.popResource(this.level, this.blockPosition(), new ItemStack(EEItems.BOLLOOM_FRUIT.get()));
		}
	}

	@SuppressWarnings("deprecation")
	public boolean isOpenPathBelowFruit() {
		BlockPos.MutableBlockPos mutable = this.blockPosition().mutable();
		for (int i = 0; i < this.getVineHeight(); i++) {
			BlockPos pos = mutable.below(i);
			if (this.level.isAreaLoaded(pos, 1)) {
				if (!this.level.getBlockState(pos).isAir()) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public ItemStack getPickedResult(HitResult target) {
		return new ItemStack(EEItems.BOLLOOM_FRUIT.get());
	}

	@Override
	public void push(Entity entity) {
		if (entity instanceof BolloomFruit) {
			if (entity.getBoundingBox().minY < this.getBoundingBox().maxY) {
				super.push(entity);
			}
		} else if (entity.getY() >= this.getBoundingBox().minY) {
			super.push(entity);
		}
	}

	@Override
	public boolean shouldRenderAtSqrDistance(double distance) {
		return true;
	}

	@Override
	public boolean shouldRender(double x, double y, double z) {
		return true;
	}
}