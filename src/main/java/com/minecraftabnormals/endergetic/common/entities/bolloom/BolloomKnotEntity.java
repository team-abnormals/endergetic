package com.minecraftabnormals.endergetic.common.entities.bolloom;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.minecraftabnormals.endergetic.core.registry.EEEntities;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

public class BolloomKnotEntity extends Entity {
	private BlockPos hangingPosition;
	private static final EntityDataAccessor<Integer> BALLOONS_TIED = SynchedEntityData.defineId(BolloomKnotEntity.class, EntityDataSerializers.INT);

	public BolloomKnotEntity(EntityType<? extends BolloomKnotEntity> entityType, Level world) {
		super(entityType, world);
	}

	public BolloomKnotEntity(Level world, BlockPos pos) {
		this(EEEntities.BOLLOOM_KNOT.get(), world);
		this.setPos(pos.getX() + 0.5F, pos.getY() + 0.9F, pos.getZ() + 0.5F);
		this.hangingPosition = pos;
		this.setDeltaMovement(Vec3.ZERO);
		this.forcedLoading = true;
	}

	public BolloomKnotEntity(FMLPlayMessages.SpawnEntity spawnEntity, Level world) {
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
	public static BolloomKnotEntity getKnotForPosition(Level worldIn, BlockPos pos) {
		for (BolloomKnotEntity entity : worldIn.getEntitiesOfClass(BolloomKnotEntity.class, new AABB(pos))) {
			if (entity.getHangingPos().equals(pos)) {
				return entity;
			}
		}
		return null;
	}

	public static void createStartingKnot(Level world, BlockPos pos, BalloonColor balloonColor) {
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
	protected void readAdditionalSaveData(CompoundTag nbt) {
		this.hangingPosition = new BlockPos(nbt.getInt("TileX"), nbt.getInt("TileY"), nbt.getInt("TileZ"));
		this.setBalloonsTied(nbt.getInt("Ballons_Tied"));
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag nbt) {
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
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
