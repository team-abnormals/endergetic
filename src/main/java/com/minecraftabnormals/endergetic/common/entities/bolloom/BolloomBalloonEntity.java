package com.minecraftabnormals.endergetic.common.entities.bolloom;

import java.util.*;

import javax.annotation.Nullable;

import com.minecraftabnormals.endergetic.api.entity.util.EntityItemStackHelper;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import com.minecraftabnormals.endergetic.core.interfaces.BalloonHolder;
import com.minecraftabnormals.endergetic.core.interfaces.CustomBalloonPositioner;
import com.minecraftabnormals.endergetic.core.registry.EEEntities;

import com.minecraftabnormals.endergetic.core.registry.other.EEDataSerializers;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.FMLPlayMessages;

/**
 * @author - SmellyModder (Luke Tonon)
 */
public class BolloomBalloonEntity extends AbstractBolloomEntity {
	private static final DataParameter<Optional<UUID>> KNOT_UNIQUE_ID = EntityDataManager.defineId(BolloomBalloonEntity.class, DataSerializers.OPTIONAL_UUID);
	private static final DataParameter<BlockPos> FENCE_POS = EntityDataManager.defineId(BolloomBalloonEntity.class, DataSerializers.BLOCK_POS);
	private static final DataParameter<BalloonColor> COLOR = EntityDataManager.defineId(BolloomBalloonEntity.class, EEDataSerializers.BALLOON_COLOR);

	public boolean hasModifiedBoatOrder;
	@Nullable
	private Entity attachedEntity;
	@Nullable
	private UUID attachedEntityUUID;

	public BolloomBalloonEntity(EntityType<? extends BolloomBalloonEntity> entityType, World world) {
		super(entityType, world);
	}

	public BolloomBalloonEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		this(EEEntities.BOLLOOM_BALLOON.get(), world);
	}

	/*
	 * Used for Adding onto a fence
	 */
	public BolloomBalloonEntity(World world, UUID ownerKnot, BlockPos pos, float offset) {
		this(EEEntities.BOLLOOM_BALLOON.get(), world);
		float posX = pos.getX() + 0.5F + (this.random.nextBoolean() ? -offset : offset);
		float posY = pos.getY() + 3;
		float posZ = pos.getZ() + 0.5F + (this.random.nextBoolean() ? -offset : offset);
		this.setPos(posX, posY, posZ);
		this.setOrigin(posX, posY, posZ);
		this.xo = posX;
		this.yo = posY;
		this.zo = posZ;
		this.setFencePos(pos);
		this.setKnotId(ownerKnot);
	}

	/*
	 * Used for Dispensers
	 */
	public BolloomBalloonEntity(World world, BlockPos pos) {
		this(EEEntities.BOLLOOM_BALLOON.get(), world);
		float posX = pos.getX() + 0.5F;
		float posY = pos.getY();
		float posZ = pos.getZ() + 0.5F;
		this.setPos(posX, posY, posZ);
		this.setOrigin(posX, posY, posZ);
		this.xo = posX;
		this.yo = posY;
		this.zo = posZ;
		this.setUntied(true);
		this.setDesiredAngle((float) (this.random.nextDouble() * 2 * Math.PI));
		this.setAngle((float) (this.random.nextDouble() * 2 * Math.PI));
	}

	@Override
	public void tick() {
		if (this.isAttachedToEntity() && (!this.attachedEntity.isAlive() || this.attachedEntity.isSpectator())) {
			this.detachFromEntity();
		} else if (!this.level.isClientSide && this.attachedEntityUUID != null) {
			Entity entity = ((ServerWorld) this.level).getEntity(this.attachedEntityUUID);
			if (entity != null) {
				this.attachToEntity(entity);
			} else {
				EndergeticExpansion.LOGGER.warn("Could not find entity the balloon was attached to with UUID: {}", this.attachedEntityUUID);
			}
			this.attachedEntityUUID = null;
		}
		super.tick();
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(KNOT_UNIQUE_ID, Optional.empty());
		this.entityData.define(FENCE_POS, BlockPos.ZERO);
		this.entityData.define(COLOR, BalloonColor.DEFAULT);
	}

	@Override
	protected void addAdditionalSaveData(CompoundNBT compound) {
		super.addAdditionalSaveData(compound);
		if (this.getKnotId() != null) {
			compound.putUUID("KnotUUID", this.getKnotId());
		}
		compound.putLong("FENCE_POS", this.getFencePos().asLong());
		compound.putByte("Color", (byte) this.getColor().ordinal());
		if (this.isAttachedToEntity() && !(this.attachedEntity instanceof PlayerEntity)) {
			compound.put("Pos", this.newDoubleList(this.attachedEntity.getX(), this.attachedEntity.getY(), this.attachedEntity.getZ()));
			compound.putUUID("AttachedUUID", this.attachedEntity.getUUID());
		}
	}

	@Override
	protected void readAdditionalSaveData(CompoundNBT compound) {
		super.readAdditionalSaveData(compound);
		this.setKnotId(compound.contains("KnotUUID") ? compound.getUUID("KnotUUID") : null);
		this.setFencePos(BlockPos.of(compound.getLong("FENCE_POS")));
		this.setColor(BalloonColor.byOrdinal(compound.getByte("Color")));
		if (compound.hasUUID("AttachedUUID")) {
			this.attachedEntityUUID = compound.getUUID("AttachedUUID");
		}
	}

	public void setKnotId(@Nullable UUID knotUUID) {
		this.entityData.set(KNOT_UNIQUE_ID, Optional.ofNullable(knotUUID));
	}

	@Nullable
	public UUID getKnotId() {
		return this.entityData.get(KNOT_UNIQUE_ID).orElse(null);
	}

	@Nullable
	public Entity getKnot() {
		return ((ServerWorld) this.level).getEntity(this.getKnotId());
	}

	private void setFencePos(BlockPos fencePos) {
		this.entityData.set(FENCE_POS, fencePos);
	}

	private BlockPos getFencePos() {
		return this.entityData.get(FENCE_POS);
	}

	public BalloonColor getColor() {
		return this.entityData.get(COLOR);
	}

	public void setColor(BalloonColor color) {
		this.entityData.set(COLOR, color);
	}

	public void attachToEntity(Entity entity) {
		this.attachedEntity = entity;
		if (entity instanceof CustomBalloonPositioner) {
			((CustomBalloonPositioner) entity).onBalloonAttached(this);
		}
		((BalloonHolder) entity).attachBalloon(this);
	}

	public void detachFromEntity() {
		if (this.attachedEntity != null) {
			((BalloonHolder) this.attachedEntity).detachBalloon(this);
			if (this.attachedEntity instanceof CustomBalloonPositioner) {
				((CustomBalloonPositioner) this.attachedEntity).onBalloonDetached(this);
			}
			this.attachedEntity = null;
		}
	}

	@Nullable
	public Entity getAttachedEntity() {
		return this.attachedEntity;
	}

	public boolean isAttachedToEntity() {
		return this.attachedEntity != null;
	}

	public void updateAttachedPosition() {
		this.setDeltaMovement(Vector3d.ZERO);
		if (this.canUpdate()) {
			this.tick();
			this.incrementTicksExisted();
			if (this.attachedEntity instanceof CustomBalloonPositioner) {
				((CustomBalloonPositioner) this.attachedEntity).updateAttachedPosition(this);
			} else if (this.attachedEntity != null) {
				this.setPos(this.attachedEntity.getX() + this.getSway() * MathHelper.sin(-this.getAngle()), this.attachedEntity.getY() + this.getPassengersRidingOffset() + this.attachedEntity.getEyeHeight(), this.attachedEntity.getZ() + this.getSway() * MathHelper.cos(-this.getAngle()));
			}
		}
	}

	@Override
	public void updatePositionAndMotion(double angleX, double angleZ) {
		if (this.level.isAreaLoaded(this.blockPosition(), 1)) {
			if (!this.isUntied()) {
				this.setPos(
						this.getOriginX() + this.getSway() * angleX,
						this.getOriginY(),
						this.getOriginZ() + this.getSway() * angleZ
				);
			} else if (!this.isAttachedToEntity()) {
				this.move(MoverType.SELF, this.getDeltaMovement());
				this.setDeltaMovement(angleX * 0.05F, 0.07F, angleZ * 0.05F);
			}
		}
	}

	@Override
	public void updateUntied() {
		if (this.level.isAreaLoaded(this.getFencePos(), 1) && !this.isUntied()) {
			if (!this.level.getBlockState(this.getFencePos()).getBlock().is(BlockTags.FENCES)) {
				if (!this.level.isClientSide && this.getKnot() != null) {
					BolloomKnotEntity knotEntity = ((BolloomKnotEntity) this.getKnot());
					knotEntity.setBalloonsTied(knotEntity.getBalloonsTied() - 1);
				}
				this.setUntied(true);
			}
		}
		if (this.isAttachmentBlocked()) {
			if (!this.level.isClientSide && this.getKnot() != null && !this.isUntied()) {
				BolloomKnotEntity knotEntity = ((BolloomKnotEntity) this.getKnot());
				knotEntity.setBalloonsTied(knotEntity.getBalloonsTied() - 1);
			}
			this.setUntied(true);
		}
	}

	@Override
	public boolean shouldIncrementTicksExisted() {
		return !this.isAttachedToEntity();
	}

	@Override
	public void onBroken(boolean dropFruit) {
		super.onBroken(dropFruit);
		Entity knot = this.getKnot();
		if (knot instanceof BolloomKnotEntity) {
			BolloomKnotEntity bolloomKnot = (BolloomKnotEntity) knot;
			bolloomKnot.setBalloonsTied(bolloomKnot.getBalloonsTied() - 1);
		}
	}

	public boolean isAttachmentBlocked() {
		BlockPos.Mutable mutable = this.getFencePos().above(3).mutable();
		for (int i = 0; i < 3; i++) {
			BlockPos pos = mutable.below(i);
			if (this.level.isAreaLoaded(pos, 1)) {
				if (!this.level.getBlockState(pos).getMaterial().isReplaceable() || this.level.getBlockState(pos).getBlock() == Blocks.LAVA) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void kill() {
		if (!this.level.isClientSide) {
			Entity knot = this.getKnot();
			if (knot instanceof BolloomKnotEntity) {
				BolloomKnotEntity bolloomKnot = (BolloomKnotEntity) knot;
				bolloomKnot.setBalloonsTied(bolloomKnot.getBalloonsTied() - 1);
			}
		}
		super.kill();
	}

	@Override
	public ActionResultType interactAt(PlayerEntity player, Vector3d vec, Hand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		if (itemstack.getItem() instanceof DyeItem && this.getColor().color != ((DyeItem) itemstack.getItem()).getDyeColor()) {
			if (!this.level.isClientSide) {
				this.setColor(BalloonColor.byDyeColor(((DyeItem) itemstack.getItem()).getDyeColor()));
				EntityItemStackHelper.consumeItemFromStack(player, itemstack);
			}
			return ActionResultType.CONSUME;
		}
		return super.interactAt(player, vec, hand);
	}

	@Override
	public double getPassengersRidingOffset() {
		return 1.75F;
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(this.getColor().balloonItem.get());
	}

	@Override
	public boolean shouldRenderAtSqrDistance(double distance) {
		if (this.isAttachedToEntity()) {
			return this.attachedEntity.shouldRenderAtSqrDistance(distance);
		}
		return super.shouldRenderAtSqrDistance(distance);
	}
}
