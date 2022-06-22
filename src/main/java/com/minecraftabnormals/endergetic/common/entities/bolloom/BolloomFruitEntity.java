package com.minecraftabnormals.endergetic.common.entities.bolloom;

import com.minecraftabnormals.endergetic.common.blocks.poise.BolloomBudBlock;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.minecraftabnormals.endergetic.core.registry.EEEntities;
import com.minecraftabnormals.endergetic.core.registry.EEItems;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraft.world.World;

/**
 * @author - SmellyModder (Luke Tonon)
 */
public class BolloomFruitEntity extends AbstractBolloomEntity {
	private static final DataParameter<BlockPos> BUD_POS = EntityDataManager.defineId(BolloomFruitEntity.class, DataSerializers.BLOCK_POS);
	private static final DataParameter<Integer> VINE_HEIGHT = EntityDataManager.defineId(BolloomFruitEntity.class, DataSerializers.INT);

	public BolloomFruitEntity(EntityType<? extends BolloomFruitEntity> type, World world) {
		super(EEEntities.BOLLOOM_FRUIT.get(), world);
		this.setNoGravity(true);
	}

	public BolloomFruitEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		this(EEEntities.BOLLOOM_FRUIT.get(), world);
	}

	public BolloomFruitEntity(World world, BlockPos budPos, BlockPos origin, int height, Direction direction) {
		this(EEEntities.BOLLOOM_FRUIT.get(), world);
		float xPos = origin.getX() + 0.5F + (direction.getAxis() == Axis.Z ? 0.0F : -0.2F * direction.getAxisDirection().getStep());
		float zPos = origin.getZ() + 0.5F + (direction.getAxis() == Axis.X ? 0.0F : -0.2F * direction.getAxisDirection().getStep());
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
	public void addAdditionalSaveData(CompoundNBT compound) {
		super.addAdditionalSaveData(compound);
		compound.putLong("BudPosition", this.getBudPos().asLong());
		compound.putInt("VineHeight", this.getVineHeight());
	}

	@Override
	public void readAdditionalSaveData(CompoundNBT compound) {
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
	public float getVineAngle() {
		return (float) Math.atan(this.getSway() / (this.getVineHeight()));
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
			this.setPos(
					this.getOriginX() + this.getSway() * angleX,
					this.getOriginY(),
					this.getOriginZ() + this.getSway() * angleZ
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
		BlockPos.Mutable mutable = this.blockPosition().mutable();
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
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(EEItems.BOLLOOM_FRUIT.get());
	}

	@Override
	public void push(Entity entity) {
		if (entity instanceof BolloomFruitEntity) {
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