package com.minecraftabnormals.endergetic.common.entities.bolloom;

import java.util.*;

import javax.annotation.Nullable;

import com.minecraftabnormals.endergetic.api.entity.util.EntityItemStackHelper;
import com.minecraftabnormals.endergetic.common.network.entity.S2CDetachCustomPositionBalloon;
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
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.PacketDistributor;

/**
 * @author - SmellyModder (Luke Tonon)
 */
public class BolloomBalloonEntity extends AbstractBolloomEntity {
	private static final DataParameter<Optional<UUID>> KNOT_UNIQUE_ID = EntityDataManager.createKey(BolloomBalloonEntity.class, DataSerializers.OPTIONAL_UNIQUE_ID);
	private static final DataParameter<BlockPos> FENCE_POS = EntityDataManager.createKey(BolloomBalloonEntity.class, DataSerializers.BLOCK_POS);
	private static final DataParameter<BalloonColor> COLOR = EntityDataManager.createKey(BolloomBalloonEntity.class, EEDataSerializers.BALLOON_COLOR);

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
		float posX = pos.getX() + 0.5F + (this.rand.nextBoolean() ? -offset : offset);
		float posY = pos.getY() + 3;
		float posZ = pos.getZ() + 0.5F + (this.rand.nextBoolean() ? -offset : offset);
		this.setPosition(posX, posY, posZ);
		this.setOrigin(posX, posY, posZ);
		this.prevPosX = posX;
		this.prevPosY = posY;
		this.prevPosZ = posZ;
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
		this.setPosition(posX, posY, posZ);
		this.setOrigin(posX, posY, posZ);
		this.prevPosX = posX;
		this.prevPosY = posY;
		this.prevPosZ = posZ;
		this.setUntied(true);
		this.setDesiredAngle((float) (this.rand.nextDouble() * 2 * Math.PI));
		this.setAngle((float) (this.rand.nextDouble() * 2 * Math.PI));
	}

	@Override
	public void tick() {
		if (this.isAttachedToEntity() && (!this.attachedEntity.isAlive() || this.attachedEntity.isSpectator())) {
			this.detachFromEntity();
		} else if (!this.world.isRemote && this.attachedEntityUUID != null) {
			Entity entity = ((ServerWorld) this.world).getEntityByUuid(this.attachedEntityUUID);
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
	protected void registerData() {
		super.registerData();
		this.dataManager.register(KNOT_UNIQUE_ID, Optional.empty());
		this.dataManager.register(FENCE_POS, BlockPos.ZERO);
		this.dataManager.register(COLOR, BalloonColor.DEFAULT);
	}

	@Override
	protected void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		if (this.getKnotId() != null) {
			compound.putUniqueId("KnotUUID", this.getKnotId());
		}
		compound.putLong("FENCE_POS", this.getFencePos().toLong());
		compound.putByte("Color", (byte) this.getColor().ordinal());
		if (this.isAttachedToEntity() && !(this.attachedEntity instanceof PlayerEntity)) {
			compound.put("Pos", this.newDoubleNBTList(this.attachedEntity.getPosX(), this.attachedEntity.getPosY(), this.attachedEntity.getPosZ()));
			compound.putUniqueId("AttachedUUID", this.attachedEntity.getUniqueID());
		}
	}
	
	@Override
	protected void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		this.setKnotId(compound.contains("KnotUUID") ? compound.getUniqueId("KnotUUID") : null);
		this.setFencePos(BlockPos.fromLong(compound.getLong("FENCE_POS")));
		this.setColor(BalloonColor.byOrdinal(compound.getByte("Color")));
		if (compound.hasUniqueId("AttachedUUID")) {
			this.attachedEntityUUID = compound.getUniqueId("AttachedUUID");
		}
	}

	public void setKnotId(@Nullable UUID knotUUID) {
		this.dataManager.set(KNOT_UNIQUE_ID, Optional.ofNullable(knotUUID));
	}

	@Nullable
	public UUID getKnotId() {
		return this.dataManager.get(KNOT_UNIQUE_ID).orElse(null);
	}

	@Nullable
	public Entity getKnot() {
		return ((ServerWorld) this.world).getEntityByUuid(this.getKnotId());
	}

	private void setFencePos(BlockPos fencePos) {
		this.dataManager.set(FENCE_POS, fencePos);
	}

	private BlockPos getFencePos() {
		return this.dataManager.get(FENCE_POS);
	}

	public BalloonColor getColor() {
		return this.dataManager.get(COLOR);
	}

	public void setColor(BalloonColor color) {
		this.dataManager.set(COLOR, color);
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
			if (!this.world.isRemote && this.attachedEntity instanceof CustomBalloonPositioner) {
				((CustomBalloonPositioner) this.attachedEntity).onBalloonDetachedServer(this);
				EndergeticExpansion.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> this.attachedEntity), new S2CDetachCustomPositionBalloon(this.attachedEntity.getEntityId(), this.getEntityId()));
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
		this.setMotion(Vector3d.ZERO);
		if (this.canUpdate()) {
			this.tick();
			this.incrementTicksExisted();
			if (this.attachedEntity instanceof CustomBalloonPositioner) {
				((CustomBalloonPositioner) this.attachedEntity).updateAttachedPosition(this);
			} else if (this.attachedEntity != null) {
				this.setPosition(this.attachedEntity.getPosX() + this.getSway() * Math.sin(-this.getAngle()), this.attachedEntity.getPosY() + this.getMountedYOffset() + this.attachedEntity.getEyeHeight(), this.attachedEntity.getPosZ() + this.getSway() * Math.cos(-this.getAngle()));
			}
		}
	}

	@Override
	public void updatePositionAndMotion(double angleX, double angleZ) {
		if (this.world.isAreaLoaded(this.func_233580_cy_(), 1)) {
			if (!this.isUntied()) {
				this.setPosition(
						this.getOriginX() + this.getSway() * angleX,
						this.getOriginY(),
						this.getOriginZ() + this.getSway() * angleZ
				);
			} else if (!this.isAttachedToEntity()) {
				this.move(MoverType.SELF, this.getMotion());
				this.setMotion(angleX * angleZ * 0.05F, 0.07F, angleZ * angleZ * 0.05F);
			}
		}
	}

	@Override
	public void updateUntied() {
		if (this.world.isAreaLoaded(this.getFencePos(), 1) && !this.isUntied()) {
			if (!this.world.getBlockState(this.getFencePos()).getBlock().isIn(BlockTags.FENCES)) {
				if (!this.world.isRemote && this.getKnot() != null) {
					BolloomKnotEntity knotEntity = ((BolloomKnotEntity) this.getKnot());
					knotEntity.setBalloonsTied(knotEntity.getBalloonsTied() - 1);
				}
				this.setUntied(true);
			}
		}
		if (this.isAttachmentBlocked()) {
			if (!this.world.isRemote && this.getKnot() != null && !this.isUntied()) {
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
		BlockPos.Mutable mutable = this.getFencePos().up(3).func_239590_i_();
		for (int i = 0; i < 3; i++) {
			BlockPos pos = mutable.down(i);
			if (this.world.isAreaLoaded(pos, 1)) {
				if (!this.world.getBlockState(pos).getMaterial().isReplaceable() || this.world.getBlockState(pos).getBlock() == Blocks.LAVA) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void onKillCommand() {
		if (!this.world.isRemote) {
			Entity knot = this.getKnot();
			if (knot instanceof BolloomKnotEntity) {
				BolloomKnotEntity bolloomKnot = (BolloomKnotEntity) knot;
				bolloomKnot.setBalloonsTied(bolloomKnot.getBalloonsTied() - 1);
			}
		}
		super.onKillCommand();
	}
	
	@Override
	public ActionResultType applyPlayerInteraction(PlayerEntity player, Vector3d vec, Hand hand) {
		ItemStack itemstack = player.getHeldItem(hand);
		if (itemstack.getItem() instanceof DyeItem && this.getColor().color != ((DyeItem) itemstack.getItem()).getDyeColor()) {
			if (!this.world.isRemote) {
				this.setColor(BalloonColor.byDyeColor(((DyeItem) itemstack.getItem()).getDyeColor()));
				EntityItemStackHelper.consumeItemFromStack(player, itemstack);
			}
			return ActionResultType.CONSUME;
		}
		return super.applyPlayerInteraction(player, vec, hand);
	}

	@Override
	public double getMountedYOffset() {
		return 1.75F;
	}
	
	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(this.getColor().balloonItem.get());
	}
    
    @Override
    public boolean isInRangeToRenderDist(double distance) {
    	if (this.isAttachedToEntity()) {
    		return this.attachedEntity.isInRangeToRenderDist(distance);
    	}
    	return super.isInRangeToRenderDist(distance);
    }
}
