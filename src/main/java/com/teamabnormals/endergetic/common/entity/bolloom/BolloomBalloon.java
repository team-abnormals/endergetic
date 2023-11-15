package com.teamabnormals.endergetic.common.entity.bolloom;

import com.teamabnormals.endergetic.api.entity.util.EntityItemStackHelper;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import com.teamabnormals.endergetic.core.interfaces.BalloonHolder;
import com.teamabnormals.endergetic.core.interfaces.CustomBalloonPositioner;
import com.teamabnormals.endergetic.core.registry.EEEntityTypes;
import com.teamabnormals.endergetic.core.registry.other.EEDataSerializers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PlayMessages;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

/**
 * @author - SmellyModder (Luke Tonon)
 */
public class BolloomBalloon extends AbstractBolloom {
	private static final EntityDataAccessor<Optional<UUID>> KNOT_UNIQUE_ID = SynchedEntityData.defineId(BolloomBalloon.class, EntityDataSerializers.OPTIONAL_UUID);
	private static final EntityDataAccessor<BlockPos> FENCE_POS = SynchedEntityData.defineId(BolloomBalloon.class, EntityDataSerializers.BLOCK_POS);
	private static final EntityDataAccessor<BalloonColor> COLOR = SynchedEntityData.defineId(BolloomBalloon.class, EEDataSerializers.BALLOON_COLOR);

	public boolean hasModifiedBoatOrder;
	@Nullable
	private Entity attachedEntity;
	@Nullable
	private UUID attachedEntityUUID;

	public BolloomBalloon(EntityType<? extends BolloomBalloon> entityType, Level world) {
		super(entityType, world);
	}

	public BolloomBalloon(PlayMessages.SpawnEntity spawnEntity, Level world) {
		this(EEEntityTypes.BOLLOOM_BALLOON.get(), world);
	}

	/*
	 * Used for Adding onto a fence
	 */
	public BolloomBalloon(Level world, UUID ownerKnot, BlockPos pos, float offset) {
		this(EEEntityTypes.BOLLOOM_BALLOON.get(), world);
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
	public BolloomBalloon(Level world, BlockPos pos) {
		this(EEEntityTypes.BOLLOOM_BALLOON.get(), world);
		float posX = pos.getX() + 0.5F;
		float posY = pos.getY();
		float posZ = pos.getZ() + 0.5F;
		this.setPos(posX, posY, posZ);
		this.setOrigin(posX, posY, posZ);
		this.xo = posX;
		this.yo = posY;
		this.zo = posZ;
		this.setUntied(true);
		this.setDesiredVineYRot((float) (this.random.nextDouble() * 2 * Math.PI));
		this.setVineYRot((float) (this.random.nextDouble() * 2 * Math.PI), true);
	}

	@Override
	public void tick() {
		if (this.isAttachedToEntity() && (!this.attachedEntity.isAlive() || this.attachedEntity.isSpectator())) {
			this.detachFromEntity();
		} else if (!this.level.isClientSide && this.attachedEntityUUID != null) {
			Entity entity = ((ServerLevel) this.level).getEntity(this.attachedEntityUUID);
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
	protected void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		if (this.getKnotId() != null) {
			compound.putUUID("KnotUUID", this.getKnotId());
		}
		compound.putLong("FENCE_POS", this.getFencePos().asLong());
		compound.putByte("Color", (byte) this.getColor().ordinal());
		if (this.isAttachedToEntity() && !(this.attachedEntity instanceof Player)) {
			compound.put("Pos", this.newDoubleList(this.attachedEntity.getX(), this.attachedEntity.getY(), this.attachedEntity.getZ()));
			compound.putUUID("AttachedUUID", this.attachedEntity.getUUID());
		}
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {
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
		return ((ServerLevel) this.level).getEntity(this.getKnotId());
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
		this.setDeltaMovement(Vec3.ZERO);
		if (this.canUpdate()) {
			this.tick();
			this.incrementTicksExisted(!this.level.isClientSide);
			if (this.attachedEntity instanceof CustomBalloonPositioner) {
				((CustomBalloonPositioner) this.attachedEntity).updateAttachedPosition(this);
			} else if (this.attachedEntity != null) {
				this.setPos(this.attachedEntity.getX() + this.getSway() * Mth.sin(-this.getVineYRot()), this.attachedEntity.getY() + this.getPassengersRidingOffset() + this.attachedEntity.getEyeHeight(), this.attachedEntity.getZ() + this.getSway() * Mth.cos(-this.getVineYRot()));
			}
		}
	}

	@Override
	public void updatePositionAndMotion(double angleX, double angleZ) {
		if (this.level.isAreaLoaded(this.blockPosition(), 1)) {
			if (!this.isUntied()) {
				float sway = this.getSway();
				this.setPos(
						this.getOriginX() + sway * angleX,
						this.getOriginY(),
						this.getOriginZ() + sway * angleZ
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
			if (!this.level.getBlockState(this.getFencePos()).is(BlockTags.FENCES)) {
				if (this.getKnot() instanceof BolloomKnot bolloomKnot) {
					bolloomKnot.setBalloonsTied(bolloomKnot.getBalloonsTied() - 1);
				}
				this.setUntied(true);
			}
		}
		if (this.isAttachmentBlocked()) {
			if (!this.isUntied() && this.getKnot() instanceof BolloomKnot bolloomKnot) {
				bolloomKnot.setBalloonsTied(bolloomKnot.getBalloonsTied() - 1);
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
		if (knot instanceof BolloomKnot) {
			BolloomKnot bolloomKnot = (BolloomKnot) knot;
			bolloomKnot.setBalloonsTied(bolloomKnot.getBalloonsTied() - 1);
		}
	}

	public boolean isAttachmentBlocked() {
		BlockPos.MutableBlockPos mutable = this.getFencePos().above(3).mutable();
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
			if (knot instanceof BolloomKnot) {
				BolloomKnot bolloomKnot = (BolloomKnot) knot;
				bolloomKnot.setBalloonsTied(bolloomKnot.getBalloonsTied() - 1);
			}
		}
		super.kill();
	}

	@Override
	public InteractionResult interactAt(Player player, Vec3 vec, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		if (itemstack.getItem() instanceof DyeItem && this.getColor().color != ((DyeItem) itemstack.getItem()).getDyeColor()) {
			if (!this.level.isClientSide) {
				this.setColor(BalloonColor.byDyeColor(((DyeItem) itemstack.getItem()).getDyeColor()));
				EntityItemStackHelper.consumeItemFromStack(player, itemstack);
			}
			return InteractionResult.CONSUME;
		}
		return super.interactAt(player, vec, hand);
	}

	@Override
	public double getPassengersRidingOffset() {
		return 1.75F;
	}

	@Override
	public ItemStack getPickedResult(HitResult target) {
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
