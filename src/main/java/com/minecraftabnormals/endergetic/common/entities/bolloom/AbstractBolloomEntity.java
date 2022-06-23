package com.minecraftabnormals.endergetic.common.entities.bolloom;

import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.phys.AABB;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;

/**
 * @author - SmellyModder (Luke Tonon)
 */
public abstract class AbstractBolloomEntity extends Entity {
	private static final EntityDataAccessor<Float> ORIGIN_X = SynchedEntityData.defineId(AbstractBolloomEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> ORIGIN_Y = SynchedEntityData.defineId(AbstractBolloomEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> ORIGIN_Z = SynchedEntityData.defineId(AbstractBolloomEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> ANGLE = SynchedEntityData.defineId(AbstractBolloomEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> DESIRED_ANGLE = SynchedEntityData.defineId(AbstractBolloomEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> SWAY = SynchedEntityData.defineId(AbstractBolloomEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Integer> TICKS_EXISTED = SynchedEntityData.defineId(AbstractBolloomEntity.class, EntityDataSerializers.INT); //Vanilla's ticksExisted isn't synced between server and client
	private static final EntityDataAccessor<Boolean> UNTIED = SynchedEntityData.defineId(AbstractBolloomEntity.class, EntityDataSerializers.BOOLEAN);

	private float prevVineAngle;
	private float prevAngle;

	protected AbstractBolloomEntity(EntityType<?> entityType, Level world) {
		super(entityType, world);
	}

	@Override
	protected void defineSynchedData() {
		this.entityData.define(ORIGIN_X, 0.0F);
		this.entityData.define(ORIGIN_Y, 0.0F);
		this.entityData.define(ORIGIN_Z, 0.0F);
		this.entityData.define(ANGLE, 0.0F);
		this.entityData.define(DESIRED_ANGLE, 0.0F);
		this.entityData.define(SWAY, 0.0F);
		this.entityData.define(UNTIED, false);
		this.entityData.define(TICKS_EXISTED, 0);
	}

	@Override
	public void tick() {
		this.xo = this.getX();
		this.yo = this.getY();
		this.zo = this.getZ();
		this.prevVineAngle = this.getVineAngle();
		this.prevAngle = this.getAngle();

		this.setSway(Mth.sin((float) (2 * Math.PI / 100 * this.getTicksExisted())) * 0.5F);
		this.updatePositionAndMotion(Mth.sin(-this.getAngle()), Mth.cos(-this.getAngle()));

		if (!this.level.isClientSide) {
			if (this.getTicksExisted() % 45 == 0) {
				this.setDesiredAngle((float) (this.random.nextDouble() * 2 * Math.PI));
			}

			if (this.getY() >= 254 && this.isUntied()) {
				this.onBroken(true);
				this.discard();
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

		this.updateUntied();
		this.clearFire();
		if (this.shouldIncrementTicksExisted()) {
			this.incrementTicksExisted();
		}
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
		compound.putBoolean("UNTIED", this.isUntied());
		compound.putFloat("ORIGIN_X", this.getOriginX());
		compound.putFloat("ORIGIN_Y", this.getOriginY());
		compound.putFloat("ORIGIN_Z", this.getOriginZ());
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {
		this.setUntied(compound.getBoolean("UNTIED"));
		if (compound.contains("ORIGIN_X", 5) && compound.contains("ORIGIN_Y", 5) && compound.contains("ORIGIN_Z", 5)) {
			this.setOrigin(compound.getFloat("ORIGIN_X"), compound.getFloat("ORIGIN_Y"), compound.getFloat("ORIGIN_Z"));
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

	public void setAngle(float degree) {
		this.entityData.set(ANGLE, degree);
	}

	public float getAngle() {
		return this.entityData.get(ANGLE);
	}

	public void setDesiredAngle(float angle) {
		this.entityData.set(DESIRED_ANGLE, angle);
	}

	public float getDesiredAngle() {
		return this.entityData.get(DESIRED_ANGLE);
	}

	public float getVineAngle() {
		return (float) Math.atan(this.getSway() / 2F);
	}

	public void setSway(float sway) {
		this.entityData.set(SWAY, sway);
	}

	public float getSway() {
		return this.entityData.get(SWAY);
	}

	public void setUntied(boolean untied) {
		this.entityData.set(UNTIED, untied);
	}

	public boolean isUntied() {
		return this.entityData.get(UNTIED);
	}

	public void incrementTicksExisted() {
		this.entityData.set(TICKS_EXISTED, this.getTicksExisted() + 1);
	}

	public int getTicksExisted() {
		return this.entityData.get(TICKS_EXISTED);
	}

	@OnlyIn(Dist.CLIENT)
	public float[] getVineAnimation(float partialTicks) {
		return new float[]{
				Mth.lerp(partialTicks, this.prevVineAngle, this.getVineAngle()),
				Mth.lerp(partialTicks, this.prevAngle, this.getAngle()),
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
