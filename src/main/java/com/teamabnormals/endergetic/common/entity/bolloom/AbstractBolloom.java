package com.teamabnormals.endergetic.common.entity.bolloom;

import com.teamabnormals.endergetic.core.registry.EEBlocks;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import org.apache.commons.lang3.ObjectUtils;

/**
 * @author - SmellyModder (Luke Tonon)
 */
public abstract class AbstractBolloom extends Entity {
	private static final EntityDataAccessor<Float> ORIGIN_X = SynchedEntityData.defineId(AbstractBolloom.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> ORIGIN_Y = SynchedEntityData.defineId(AbstractBolloom.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> ORIGIN_Z = SynchedEntityData.defineId(AbstractBolloom.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> VINE_Y_ROT = SynchedEntityData.defineId(AbstractBolloom.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> DESIRED_VINE_Y_ROT = SynchedEntityData.defineId(AbstractBolloom.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Integer> TICKS_EXISTED = SynchedEntityData.defineId(AbstractBolloom.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Boolean> UNTIED = SynchedEntityData.defineId(AbstractBolloom.class, EntityDataSerializers.BOOLEAN);

	private float sway;
	private float prevVineXRot;
	private float prevVineYRot;

	protected AbstractBolloom(EntityType<?> entityType, Level world) {
		super(entityType, world);
	}

	@Override
	protected void defineSynchedData() {
		this.entityData.define(ORIGIN_X, 0.0F);
		this.entityData.define(ORIGIN_Y, 0.0F);
		this.entityData.define(ORIGIN_Z, 0.0F);
		this.entityData.define(VINE_Y_ROT, 0.0F);
		this.entityData.define(DESIRED_VINE_Y_ROT, 0.0F);
		this.entityData.define(UNTIED, false);
		this.entityData.define(TICKS_EXISTED, 0);
	}

	@Override
	public void tick() {
		this.xOld = this.xo = this.getX();
		this.yOld = this.yo = this.getY();
		this.zOld = this.zo = this.getZ();
		this.prevVineXRot = this.getVineXRot();
		float vineYRot = this.getVineYRot();
		this.prevVineYRot = vineYRot;

		this.sway = Mth.sin((float) (2 * Math.PI / 100 * this.getTicksExisted())) * 0.5F;
		this.updatePositionAndMotion(Mth.sin(-vineYRot), Mth.cos(-vineYRot));

		float seekingVineYRot = this.getDesiredVineYRot() - vineYRot;

		while (seekingVineYRot > Math.PI) {
			seekingVineYRot -= 2 * Math.PI;
		}

		while (seekingVineYRot <= -Math.PI) {
			seekingVineYRot += 2 * Math.PI;
		}

		boolean isOnServerSide = !this.level.isClientSide;

		if (Math.abs(seekingVineYRot) <= 0.1F) {
			this.setVineYRot(vineYRot + seekingVineYRot, isOnServerSide);
		} else if (seekingVineYRot > 0) {
			this.setVineYRot(vineYRot + 0.03F, isOnServerSide);
		} else {
			this.setVineYRot(vineYRot - 0.03F, isOnServerSide);
		}

		if (isOnServerSide) {
			if (this.getY() >= 254 && this.isUntied()) {
				this.onBroken(true);
				this.discard();
			}

			if (this.getTicksExisted() % 50 == 0) {
				this.setDesiredVineYRot((float) (this.random.nextDouble() * 2 * Math.PI));
			}

			this.updateUntied();
		}

		if (this.shouldIncrementTicksExisted()) {
			this.incrementTicksExisted(isOnServerSide);
		}

		this.clearFire();
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
		compound.putBoolean("Untied", this.isUntied());
		compound.putFloat("OriginX", this.getOriginX());
		compound.putFloat("OriginY", this.getOriginY());
		compound.putFloat("OriginZ", this.getOriginZ());
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {
		this.setUntied(compound.getBoolean("Untied"));
		if (compound.contains("OriginX", 5) && compound.contains("OriginY", 5) && compound.contains("OriginZ", 5)) {
			this.setOrigin(compound.getFloat("OriginX"), compound.getFloat("OriginY"), compound.getFloat("OriginZ"));
		} else {
			this.setUntied(true);
		}
	}

	public void setOrigin(float x, float y, float z) {
		this.entityData.set(ORIGIN_X, x);
		this.entityData.set(ORIGIN_Y, y);
		this.entityData.set(ORIGIN_Z, z);
	}

	public float getOriginX() {
		return this.entityData.get(ORIGIN_X);
	}

	public float getOriginY() {
		return this.entityData.get(ORIGIN_Y);
	}

	public float getOriginZ() {
		return this.entityData.get(ORIGIN_Z);
	}

	public void setVineYRot(float radians, boolean updateClient) {
		SynchedEntityData.DataItem<Float> dataItem = this.entityData.getItem(VINE_Y_ROT);
		if (ObjectUtils.notEqual(radians, dataItem.getValue())) {
			dataItem.setValue(radians);
			this.onSyncedDataUpdated(VINE_Y_ROT);
			//We can only mark the item as dirty because marking entityData as dirty could cause a positional update and possible jitter
			//Our setDesiredVineYRot() method will trigger the routine positional updates. Entity networking is wacky!
			if (updateClient) dataItem.setDirty(true);
		}
	}

	public float getVineYRot() {
		return this.entityData.get(VINE_Y_ROT);
	}

	public void setDesiredVineYRot(float radians) {
		this.entityData.set(DESIRED_VINE_Y_ROT, radians);
	}

	public float getDesiredVineYRot() {
		return this.entityData.get(DESIRED_VINE_Y_ROT);
	}

	public float getVineXRot() {
		return (float) Math.atan(this.getSway() / 2F);
	}

	public float getSway() {
		return this.sway;
	}

	public void setUntied(boolean untied) {
		this.entityData.set(UNTIED, untied);
	}

	public boolean isUntied() {
		return this.entityData.get(UNTIED);
	}

	public void incrementTicksExisted(boolean updateClient) {
		int ticksExisted = this.entityData.get(TICKS_EXISTED) + 1;
		SynchedEntityData.DataItem<Integer> dataItem = this.entityData.getItem(TICKS_EXISTED);
		if (ObjectUtils.notEqual(ticksExisted, dataItem.getValue())) {
			dataItem.setValue(ticksExisted);
			this.onSyncedDataUpdated(TICKS_EXISTED);
			//We can only mark the item as dirty because marking entityData as dirty could cause a positional update and possible jitter
			//Our setDesiredVineYRot() method will trigger the routine positional updates. Entity networking is wacky!
			if (updateClient) dataItem.setDirty(true);
		}
	}

	public int getTicksExisted() {
		return this.entityData.get(TICKS_EXISTED);
	}

	@OnlyIn(Dist.CLIENT)
	public float[] getVineAnimation(float partialTicks) {
		return new float[]{
				Mth.lerp(partialTicks, this.prevVineXRot, this.getVineXRot()),
				Mth.lerp(partialTicks, this.prevVineYRot, this.getVineYRot()),
		};
	}

	public abstract void updatePositionAndMotion(double angleX, double angleZ);

	public abstract void updateUntied();

	public boolean shouldIncrementTicksExisted() {
		return true;
	}

	public void onBroken(boolean dropFruit) {
		this.playSound(SoundEvents.WET_GRASS_BREAK, 1.0F, 1.0F);
		this.doParticles();
	}

	protected void doParticles() {
		if (this.level instanceof ServerLevel) {
			((ServerLevel) this.level).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, EEBlocks.BOLLOOM_PARTICLE.get().defaultBlockState()), this.getX(), this.getY() + (double) this.getBbHeight() / 1.5D, this.getZ(), 10, this.getBbWidth() / 4.0F, this.getBbHeight() / 4.0F, this.getBbWidth() / 4.0F, 0.05D);
		}
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public boolean isPickable() {
		return true;
	}

	@Override
	public boolean isPushable() {
		return this.isAlive();
	}

	@Override
	public boolean displayFireAnimation() {
		return false;
	}

	@Override
	public boolean isInvulnerableTo(DamageSource source) {
		return this.isInvulnerable() && source != DamageSource.OUT_OF_WORLD && source != DamageSource.CRAMMING;
	}

	@Override
	protected Vec3 limitPistonMovement(Vec3 pos) {
		return Vec3.ZERO;
	}

	@Override
	public boolean skipAttackInteraction(Entity entityIn) {
		return entityIn instanceof Player && this.hurt(DamageSource.playerAttack((Player) entityIn), 0.0F);
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (this.isInvulnerableTo(source)) {
			return false;
		} else {
			if (this.isAlive() && !this.level.isClientSide) {
				this.discard();
				this.markHurt();
				this.onBroken(true);
			}
			return true;
		}
	}

	@Override
	public void push(double x, double y, double z) {
		if (!this.isUntied()) return;
		super.push(x, y, z);
	}

	@Override
	public AABB getBoundingBoxForCulling() {
		return super.getBoundingBoxForCulling().inflate(5);
	}

	@Override
	protected MovementEmission getMovementEmission() {
		return MovementEmission.NONE;
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
