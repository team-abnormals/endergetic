package com.minecraftabnormals.endergetic.common.entities.bolloom;

import java.util.*;

import javax.annotation.Nullable;

import com.minecraftabnormals.endergetic.api.entity.util.EntityItemStackHelper;
import com.minecraftabnormals.endergetic.common.network.entity.S2CDetachCustomPositionBalloon;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import com.minecraftabnormals.endergetic.core.interfaces.BalloonHolder;
import com.minecraftabnormals.endergetic.core.interfaces.CustomBalloonPositioner;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
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
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.PacketDistributor;

//TODO: Merge shared data in the bolloom entities into an abstract bolloom entity class.
public class BolloomBalloonEntity extends Entity {
	private static final DataParameter<Float> ORIGIN_X = EntityDataManager.createKey(BolloomBalloonEntity.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> ORIGIN_Z = EntityDataManager.createKey(BolloomBalloonEntity.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> ORIGIN_Y = EntityDataManager.createKey(BolloomBalloonEntity.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> ANGLE = EntityDataManager.createKey(BolloomBalloonEntity.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> DESIRED_ANGLE = EntityDataManager.createKey(BolloomBalloonEntity.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> SWAY = EntityDataManager.createKey(BolloomBalloonEntity.class, DataSerializers.FLOAT);
	private static final DataParameter<Boolean> UNTIED = EntityDataManager.createKey(BolloomBalloonEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Optional<UUID>> KNOT_UNIQUE_ID = EntityDataManager.createKey(BolloomBalloonEntity.class, DataSerializers.OPTIONAL_UNIQUE_ID);
	private static final DataParameter<BlockPos> FENCE_POS = EntityDataManager.createKey(BolloomBalloonEntity.class, DataSerializers.BLOCK_POS);
	private static final DataParameter<Integer> TICKS_EXISTED = EntityDataManager.createKey(BolloomBalloonEntity.class, DataSerializers.VARINT); //Vanilla's ticksExisted isn't synced between server and client
	private static final DataParameter<BalloonColor> COLOR = EntityDataManager.createKey(BolloomBalloonEntity.class, EEDataSerializers.BALLOON_COLOR);

	private float prevVineAngle;
	private float prevAngle;
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
		this.setOriginPos(posX, posY, posZ);
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
		this.setOriginPos(posX, posY, posZ);
		this.prevPosX = posX;
		this.prevPosY = posY;
		this.prevPosZ = posZ;
		this.setUntied(true);
		this.setDesiredAngle((float) (this.rand.nextDouble() * 2 * Math.PI));
		this.setAngle((float) (this.rand.nextDouble() * 2 * Math.PI));
	}

	@Override
	public void tick() {
		this.prevPosX = this.getPosX();
		this.prevPosY = this.getPosY();
		this.prevPosZ = this.getPosZ();
		this.prevVineAngle = this.getVineAngle();
		this.prevAngle = this.getAngle();

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
		
		if (this.world.isAreaLoaded(getFencePos(), 1) && !this.isUntied()) {
			if (!this.world.getBlockState(this.getFencePos()).getBlock().isIn(BlockTags.FENCES)) {
				if (!this.world.isRemote && this.getKnot() != null) {
					((BolloomKnotEntity) this.getKnot()).setBalloonsTied(((BolloomKnotEntity) this.getKnot()).getBalloonsTied() - 1);
				}
				this.setUntied(true);
			}
		}
		
		this.setSway((float) Math.sin((2 * Math.PI / 100 * this.getTicksExisted())) * 0.5F);
		
		if (!this.world.isRemote) {
			if (this.getTicksExisted() % 45 == 0) {
			    this.setDesiredAngle((float) (this.rand.nextDouble() * 2 * Math.PI));
			}
			
			if (this.getPosY() > 254 && this.isUntied()) {
				this.onBroken();
				this.remove();
			}
			
			float dangle = this.getDesiredAngle() - this.getAngle();
			
			while (dangle > Math.PI) {
			    dangle -= 2 * Math.PI;
			}
			
			while (dangle <= -Math.PI) {
			    dangle += 2 * Math.PI;
			}
			
			if (Math.abs(dangle) <= 0.1F) {
			    this.setAngle(this.getAngle() + dangle);
			} else if (dangle > 0) {
			    this.setAngle(this.getAngle() + 0.03F);
			} else {
			    this.setAngle(this.getAngle() - 0.03F);
			}
		}

		if (this.world.isAreaLoaded(this.func_233580_cy_(), 1)) {
			if (!this.isUntied()) {
				this.setPosition(
					this.getOriginX() + this.getSway() * Math.sin(-this.getAngle()),
					this.getOriginY(),
					this.getOriginZ() + this.getSway() * Math.cos(-this.getAngle())
				);
			} else if (!this.isAttachedToEntity()) {
				this.move(MoverType.SELF, this.getMotion());
				this.setMotion(Math.sin(this.getAngle()) * Math.cos(this.getAngle()) * 0.05F, Math.toRadians(4), Math.cos(this.getVineAngle()) * Math.cos(-this.getAngle()) * 0.05F);
			}
		}

		if (this.checkForBlocksDown()) {
			if (!this.getEntityWorld().isRemote && this.getKnot() != null && !this.isUntied()) {
				((BolloomKnotEntity) this.getKnot()).setBalloonsTied(((BolloomKnotEntity)this.getKnot()).getBalloonsTied() - 1);
			}
			this.setUntied(true);
		}

		if (!this.isAttachedToEntity()) {
			this.incrementTicksExisted();
		}
	}
	
	@Override
	protected void registerData() {
		this.dataManager.register(KNOT_UNIQUE_ID, Optional.empty());
		this.dataManager.register(ORIGIN_X, 0.0F);
		this.dataManager.register(ORIGIN_Z, 0.0F);
		this.dataManager.register(ORIGIN_Y, 0.0F);
		this.dataManager.register(FENCE_POS, BlockPos.ZERO);
		this.dataManager.register(UNTIED, false);
		this.dataManager.register(ANGLE, 0.0F);
		this.dataManager.register(SWAY, 0.0F);
		this.dataManager.register(DESIRED_ANGLE, 0.0F);
		this.dataManager.register(TICKS_EXISTED, 0);
		this.dataManager.register(COLOR, BalloonColor.DEFAULT);
	}

	@Override
	protected void writeAdditional(CompoundNBT compound) {
		if (this.getKnotId() != null) {
			compound.putUniqueId("KnotUUID", this.getKnotId());
		}
		compound.putBoolean("UNTIED", this.isUntied());
		compound.putFloat("ORIGIN_X", this.getOriginX());
		compound.putFloat("ORIGIN_Y", this.getOriginY());
		compound.putFloat("ORIGIN_Z", this.getOriginZ());
		compound.putLong("FENCE_POS", this.getFencePos().toLong());
		compound.putByte("Color", (byte) this.dataManager.get(COLOR).ordinal());
		if (this.isAttachedToEntity() && !(this.attachedEntity instanceof PlayerEntity)) {
			compound.put("Pos", this.newDoubleNBTList(this.attachedEntity.getPosX(), this.attachedEntity.getPosY(), this.attachedEntity.getPosZ()));
			compound.putUniqueId("AttachedUUID", this.attachedEntity.getUniqueID());
		}
	}
	
	@Override
	protected void readAdditional(CompoundNBT nbt) {
		this.setKnotId(nbt.contains("KnotUUID") ? nbt.getUniqueId("KnotUUID") : null);
		this.setUntied(nbt.getBoolean("UNTIED"));
		this.setOriginPos(nbt.getFloat("ORIGIN_X"), nbt.getFloat("ORIGIN_Y"), nbt.getFloat("ORIGIN_Z"));
		this.setFencePos(BlockPos.fromLong(nbt.getLong("FENCE_POS")));
		this.dataManager.set(COLOR, BalloonColor.byOrdinal(nbt.getByte("Color")));
		if (nbt.hasUniqueId("AttachedUUID")) {
			this.attachedEntityUUID = nbt.getUniqueId("AttachedUUID");
		}
	}

	public void setOriginPos(float x, float y, float z) {
		this.dataManager.set(ORIGIN_X, x);
		this.dataManager.set(ORIGIN_Y, y);
		this.dataManager.set(ORIGIN_Z, z);
	}

	public float getOriginX() {
		return this.dataManager.get(ORIGIN_X);
	}

	public float getOriginY() {
		return this.dataManager.get(ORIGIN_Y);
	}

	public float getOriginZ() {
		return this.dataManager.get(ORIGIN_Z);
	}

	public void setAngle(float degree) {
		this.dataManager.set(ANGLE, degree);
	}

	public float getAngle() {
		return this.dataManager.get(ANGLE);
	}

	public void setDesiredAngle(float angle) {
		this.dataManager.set(DESIRED_ANGLE, angle);
	}

	public float getDesiredAngle() {
		return this.dataManager.get(DESIRED_ANGLE);
	}

	public float getVineAngle() {
		return (float) Math.atan(this.getSway() / 2F);
	}

	public void setSway(float sway) {
		this.dataManager.set(SWAY, sway);
	}

	public float getSway() {
		return this.dataManager.get(SWAY);
	}

	public void setUntied(boolean untied) {
		this.dataManager.set(UNTIED, untied);
	}

	public boolean isUntied() {
		return this.dataManager.get(UNTIED);
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

	public void setFencePos(BlockPos fencePos) {
		this.dataManager.set(FENCE_POS, fencePos);
	}

	public BlockPos getFencePos() {
		return this.dataManager.get(FENCE_POS);
	}

	public void incrementTicksExisted() {
		this.dataManager.set(TICKS_EXISTED, this.getTicksExisted() + 1);
	}

	public int getTicksExisted() {
		return this.dataManager.get(TICKS_EXISTED);
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

	@Nullable
	public Entity getAttachedEntity() {
		return this.attachedEntity;
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

	public boolean isAttachedToEntity() {
		return this.attachedEntity != null;
	}
	
	@OnlyIn(Dist.CLIENT)
	public float[] getVineAnimation(float partialTicks) {
		return new float[] {
			MathHelper.lerp(partialTicks, this.prevVineAngle, this.getVineAngle()),
			MathHelper.lerp(partialTicks, this.prevAngle, this.getAngle()),
		};
	}
	
	public boolean checkForBlocksDown() {
		for (int i = 0; i < 3; i++) {
			BlockPos pos = this.getFencePos().up(3).down(i);
			if (this.getEntityWorld().isAreaLoaded(pos, 1)) {
				if (!this.getEntityWorld().getBlockState(pos).getMaterial().isReplaceable() || this.getEntityWorld().getBlockState(pos).getBlock() == Blocks.LAVA) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	public void onBroken() {
		this.playSound(SoundEvents.BLOCK_WET_GRASS_BREAK, 1.0F, 1.0F);
		Entity knot = this.getKnot();
		if (knot instanceof BolloomKnotEntity) {
			BolloomKnotEntity bolloomKnot = (BolloomKnotEntity) knot;
			bolloomKnot.setBalloonsTied(bolloomKnot.getBalloonsTied() - 1);
		}
		if (this.world instanceof ServerWorld) {
			((ServerWorld) this.world).spawnParticle(new BlockParticleData(ParticleTypes.BLOCK, EEBlocks.BOLLOOM_PARTICLE.get().getDefaultState()), this.getPosX(), this.getPosY() + (double)this.getHeight() / 1.5D, this.getPosZ(), 10, this.getWidth() / 4.0F, this.getHeight() / 4.0F, this.getWidth() / 4.0F, 0.05D);
		}
	}

	@Override
	public void onKillCommand() {
		if (!this.getEntityWorld().isRemote) {
			Entity knot = this.getKnot();
			if (knot instanceof BolloomKnotEntity) {
				BolloomKnotEntity bolloomKnot = (BolloomKnotEntity) knot;
				bolloomKnot.setBalloonsTied(bolloomKnot.getBalloonsTied() - 1);
			}
		}
		super.onKillCommand();
	}

	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (this.isInvulnerableTo(source)) {
			return false;
		} else {
			if (!this.world.isRemote && this.isAlive()) {
				this.remove();
				this.markVelocityChanged();
				this.onBroken();
			}
			return true;
		}
	}

	public boolean isInvulnerableTo(DamageSource source) {
		return this.isInvulnerable() && source != DamageSource.OUT_OF_WORLD && source != DamageSource.CRAMMING;
	}

	@Override
	public boolean hitByEntity(Entity entityIn) {
		return entityIn instanceof PlayerEntity && this.attackEntityFrom(DamageSource.causePlayerDamage((PlayerEntity) entityIn), 0.0F);
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
	public boolean canRiderInteract() {
		return true;
	}

	public void updateAttachedPosition() {
		this.setMotion(Vector3d.ZERO);
		if (canUpdate()) {
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
	public void addVelocity(double x, double y, double z) {
		if (!this.isUntied()) return;
		super.addVelocity(x, y, z);
	}
	
	@Override
	protected Vector3d handlePistonMovement(Vector3d pos) {
		return Vector3d.ZERO;
	}
	
	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(this.getColor().balloonItem.get());
	}
    
    @Override
    protected boolean canTriggerWalking() {
    	return false;
    }
    
	@Override
	public boolean canBePushed() {
		return this.isAlive();
	}
	
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox() {
		return this.getBoundingBox();
	}
	
	@Nullable
	public AxisAlignedBB getCollisionBox(Entity entityIn) {
		return entityIn.canBePushed() ? entityIn.getBoundingBox() : null;
	}
    
    @Override
	public AxisAlignedBB getRenderBoundingBox() {
    	return super.getRenderBoundingBox().grow(5);
	}
    
    @Override
    public boolean isInRangeToRenderDist(double distance) {
    	if (this.isAttachedToEntity()) {
    		return this.attachedEntity.isInRangeToRenderDist(distance);
    	}
    	return super.isInRangeToRenderDist(distance);
    }

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}