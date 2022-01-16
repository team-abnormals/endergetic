package com.minecraftabnormals.endergetic.common.entities.bolloom;

import com.minecraftabnormals.endergetic.core.registry.EEEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BolloomKnotEntity extends Entity {
	private BlockPos hangingPosition;
	private static final DataParameter<Integer> BALLOONS_TIED = EntityDataManager.defineId(BolloomKnotEntity.class, DataSerializers.INT);

	public BolloomKnotEntity(EntityType<? extends BolloomKnotEntity> entityType, World world) {
		super(entityType, world);
	}

	public BolloomKnotEntity(World world, BlockPos pos) {
		this(EEEntities.BOLLOOM_KNOT.get(), world);
		this.setPos(pos.getX() + 0.5F, pos.getY() + 0.9F, pos.getZ() + 0.5F);
		this.hangingPosition = pos;
		this.setDeltaMovement(Vector3d.ZERO);
		this.forcedLoading = true;
	}

	public BolloomKnotEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		this(EEEntities.BOLLOOM_KNOT.get(), world);
	}

	@Override
	public void tick() {
		this.xo = this.getX();
		this.yo = this.getY();
		this.zo = this.getZ();
		if (!this.level.isClientSide && this.isAlive() && this.level.isAreaLoaded(this.getHangingPos(), 1) && !this.onValidBlock()) {
			this.remove();
		} else if (this.getBalloonsTied() <= 0) {
			this.remove();
		}
	}

	@Nullable
	public static BolloomKnotEntity getKnotForPosition(World worldIn, BlockPos pos) {
		for (BolloomKnotEntity entity : worldIn.getEntitiesOfClass(BolloomKnotEntity.class, new AxisAlignedBB(pos))) {
			if (entity.getHangingPos().equals(pos)) {
				return entity;
			}
		}
		return null;
	}

	public static void createStartingKnot(World world, BlockPos pos, BalloonColor balloonColor) {
		BolloomKnotEntity knot = new BolloomKnotEntity(world, pos);
		BolloomBalloonEntity balloon = new BolloomBalloonEntity(world, knot.getUUID(), pos, 0);
		knot.setBalloonsTied(1);
		balloon.setColor(balloonColor);
		world.addFreshEntity(knot);
		world.addFreshEntity(balloon);
	}

	public void addBalloon(BalloonColor balloonColor) {
		BolloomBalloonEntity balloon = new BolloomBalloonEntity(this.level, this.getUUID(), this.getHangingPos(), 0.1F);
		balloon.setColor(balloonColor);
		this.level.addFreshEntity(balloon);
		this.setBalloonsTied(this.getBalloonsTied() + 1);
	}

	private boolean onValidBlock() {
		return this.level.getBlockState(this.hangingPosition).getBlock().is(BlockTags.FENCES);
	}

	public boolean isPickable() {
		return true;
	}

	@Override
	protected boolean repositionEntityAfterLoad() {
		return false;
	}

	@Override
	public void setPos(double x, double y, double z) {
		this.hangingPosition = new BlockPos(x, y, z);
		super.setPos(x, y, z);
	}

	@OnlyIn(Dist.CLIENT)
	public boolean shouldRenderAtSqrDistance(double distance) {
		return distance < 1024.0D;
	}

	@Override
	protected void defineSynchedData() {
		this.entityData.define(BALLOONS_TIED, 0);
	}

	@Override
	protected void readAdditionalSaveData(CompoundNBT nbt) {
		this.hangingPosition = new BlockPos(nbt.getInt("TileX"), nbt.getInt("TileY"), nbt.getInt("TileZ"));
		this.setBalloonsTied(nbt.getInt("Ballons_Tied"));
	}

	@Override
	protected void addAdditionalSaveData(CompoundNBT nbt) {
		BlockPos blockpos = this.getHangingPos();
		nbt.putInt("TileX", blockpos.getX());
		nbt.putInt("TileY", blockpos.getY());
		nbt.putInt("TileZ", blockpos.getZ());
		//Cursed NBT name, rename in 1.16.2
		nbt.putInt("Ballons_Tied", this.getBalloonsTied());
	}

	public BlockPos getHangingPos() {
		return this.hangingPosition;
	}

	public void setBalloonsTied(int amount) {
		this.getEntityData().set(BALLOONS_TIED, amount);
	}

	public int getBalloonsTied() {
		return this.getEntityData().get(BALLOONS_TIED);
	}

	public boolean hasMaxBalloons() {
		return this.entityData.get(BALLOONS_TIED) > 3;
	}

	@Nonnull
	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
