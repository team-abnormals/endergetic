package com.teamabnormals.endergetic.common.entity.bolloom;

import com.teamabnormals.endergetic.core.registry.EEEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BolloomKnot extends Entity {
	private BlockPos hangingPosition;
	private static final EntityDataAccessor<Integer> BALLOONS_TIED = SynchedEntityData.defineId(BolloomKnot.class, EntityDataSerializers.INT);

	public BolloomKnot(EntityType<? extends BolloomKnot> entityType, Level world) {
		super(entityType, world);
	}

	public BolloomKnot(Level world, BlockPos pos) {
		this(EEEntityTypes.BOLLOOM_KNOT.get(), world);
		this.setPos(pos.getX() + 0.5F, pos.getY() + 0.9F, pos.getZ() + 0.5F);
		this.hangingPosition = pos;
		this.setDeltaMovement(Vec3.ZERO);
	}

	public BolloomKnot(PlayMessages.SpawnEntity spawnEntity, Level world) {
		this(EEEntityTypes.BOLLOOM_KNOT.get(), world);
	}

	@Override
	public void tick() {
		this.xo = this.getX();
		this.yo = this.getY();
		this.zo = this.getZ();
		if (!this.level.isClientSide && this.isAlive() && this.level.isAreaLoaded(this.getHangingPos(), 1) && !this.onValidBlock()) {
			this.discard();
		} else if (this.getBalloonsTied() <= 0) {
			this.discard();
		}
	}

	@Nullable
	public static BolloomKnot getKnotForPosition(Level worldIn, BlockPos pos) {
		for (BolloomKnot entity : worldIn.getEntitiesOfClass(BolloomKnot.class, new AABB(pos))) {
			if (entity.getHangingPos().equals(pos)) {
				return entity;
			}
		}
		return null;
	}

	public static void createStartingKnot(Level world, BlockPos pos, BalloonColor balloonColor) {
		BolloomKnot knot = new BolloomKnot(world, pos);
		BolloomBalloon balloon = new BolloomBalloon(world, knot.getUUID(), pos, 0);
		knot.setBalloonsTied(1);
		balloon.setColor(balloonColor);
		world.addFreshEntity(knot);
		world.addFreshEntity(balloon);
	}

	public void addBalloon(BalloonColor balloonColor) {
		BolloomBalloon balloon = new BolloomBalloon(this.level, this.getUUID(), this.getHangingPos(), 0.1F);
		balloon.setColor(balloonColor);
		this.level.addFreshEntity(balloon);
		this.setBalloonsTied(this.getBalloonsTied() + 1);
	}

	private boolean onValidBlock() {
		return this.level.getBlockState(this.hangingPosition).is(BlockTags.FENCES);
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
		this.setBalloonsTied(nbt.getInt("BalloonsTied"));
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag nbt) {
		BlockPos blockpos = this.getHangingPos();
		nbt.putInt("TileX", blockpos.getX());
		nbt.putInt("TileY", blockpos.getY());
		nbt.putInt("TileZ", blockpos.getZ());
		nbt.putInt("BalloonsTied", this.getBalloonsTied());
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
