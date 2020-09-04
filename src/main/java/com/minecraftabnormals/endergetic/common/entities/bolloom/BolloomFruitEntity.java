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
	private static final DataParameter<BlockPos> BUD_POS = EntityDataManager.createKey(BolloomFruitEntity.class, DataSerializers.BLOCK_POS);
	private static final DataParameter<Integer> VINE_HEIGHT = EntityDataManager.createKey(BolloomFruitEntity.class, DataSerializers.VARINT);
	
	public BolloomFruitEntity(EntityType<? extends BolloomFruitEntity> type, World world) {
		super(EEEntities.BOLLOOM_FRUIT.get(), world);
		this.setNoGravity(true);
	}
	
	public BolloomFruitEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		this(EEEntities.BOLLOOM_FRUIT.get(), world);
	}
	
	public BolloomFruitEntity(World world, BlockPos budPos, BlockPos origin, int height, Direction direction) {
		this(EEEntities.BOLLOOM_FRUIT.get(), world);
		float xPos = origin.getX() + 0.5F + (direction.getAxis() == Axis.Z ? 0.0F : -0.2F * direction.getAxisDirection().getOffset());
		float zPos = origin.getZ() + 0.5F + (direction.getAxis() == Axis.X ? 0.0F : -0.2F * direction.getAxisDirection().getOffset());
		float yPos = origin.getY() + 1.15F;

		this.setPosition(xPos, yPos, zPos);
		this.setOrigin(xPos, yPos, zPos);
		this.setBudPos(budPos);
		this.setVineHeight(height);
		
		this.prevPosX = this.getPosX();
		this.prevPosY = this.getPosY();
		this.prevPosZ = this.getPosZ();
	}
	
	@Override
	protected void registerData() {
		super.registerData();
		this.dataManager.register(VINE_HEIGHT, 1);
		this.dataManager.register(BUD_POS, BlockPos.ZERO);
	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		compound.putLong("BudPosition", this.getBudPos().toLong());
		compound.putInt("VineHeight", this.getVineHeight());
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		this.setBudPos(BlockPos.fromLong(compound.getLong("BudPosition")));
		if (compound.contains("VineHeight", 3)) {
			this.setVineHeight(compound.getInt("VineHeight"));
		} else {
			this.setVineHeight(1);
		}
	}

	private void setBudPos(BlockPos budPos) {
		this.dataManager.set(BUD_POS, budPos);
	}

	private BlockPos getBudPos() {
		return this.dataManager.get(BUD_POS);
	}

	@Override
	public float getVineAngle() {
		return (float) Math.atan(this.getSway() / (this.getVineHeight()));
	}

	public void setVineHeight(int height) {
		this.dataManager.set(VINE_HEIGHT, height);
	}
	
	public int getVineHeight() {
		return this.dataManager.get(VINE_HEIGHT);
	}

	@Override
	public void updatePositionAndMotion(double angleX, double angleZ) {
		if (!this.isUntied()) {
			this.setPosition(
					this.getOriginX() + this.getSway() * angleX,
					this.getOriginY(),
					this.getOriginZ() + this.getSway() * angleZ
			);
		} else {
			this.move(MoverType.SELF, this.getMotion());
			this.setMotion(angleX * angleZ * 0.05F, 0.07F, angleZ * angleZ * 0.05F);
		}
	}

	@Override
	public void updateUntied() {
		BlockPos budPos = this.getBudPos();
		if (this.world.isAreaLoaded(budPos, 1)) {
			if (this.world.getBlockState(budPos).getBlock() != EEBlocks.BOLLOOM_BUD.get() || !this.world.getBlockState(budPos).get(BolloomBudBlock.OPENED)) {
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
			Block.spawnAsEntity(this.world, this.func_233580_cy_(), new ItemStack(EEItems.BOLLOOM_FRUIT.get()));
		}
	}

	@SuppressWarnings("deprecation")
	public boolean isOpenPathBelowFruit() {
		BlockPos.Mutable mutable = this.func_233580_cy_().func_239590_i_();
		for (int i = 0; i < this.getVineHeight(); i++) {
			BlockPos pos = mutable.down(i);
			if (this.world.isAreaLoaded(pos, 1)) {
				if (!this.world.getBlockState(pos).isAir()) {
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
	public void applyEntityCollision(Entity entity) {
		if (entity instanceof BolloomFruitEntity) {
			if (entity.getBoundingBox().minY < this.getBoundingBox().maxY) {
				super.applyEntityCollision(entity);
			}
		} else if (entity.getPosY() >= this.getBoundingBox().minY) {
			super.applyEntityCollision(entity);
		}
	}

	@Override
	public boolean isInRangeToRenderDist(double distance) {
		return true;
	}
	
	@Override
	public boolean isInRangeToRender3d(double x, double y, double z) {
		return true;
	}
}