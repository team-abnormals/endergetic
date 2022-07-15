package com.teamabnormals.endergetic.common.entities.bolloom;

import com.teamabnormals.endergetic.core.registry.EEBlocks;
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
	private static final EntityDataAccessor<Float> DESIRED_VINE_Y_ROT = SynchedEntityData.defineId(AbstractBolloomEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Integer> CLIENT_TICKS_EXISTED = SynchedEntityData.defineId(AbstractBolloomEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Boolean> UNTIED = SynchedEntityData.defineId(AbstractBolloomEntity.class, EntityDataSerializers.BOOLEAN);

	private float sway;
	private float prevVineXRot;
	private float prevVineYRot, vineYRot;
	private int ticksExisted;

	protected AbstractBolloomEntity(EntityType<?> entityType, Level world) {
		super(entityType, world);
	}

	@Override
	protected void defineSynchedData() {
		this.entityData.define(ORIGIN_X, 0.0F);
		this.entityData.define(ORIGIN_Y, 0.0F);
		this.entityData.define(ORIGIN_Z, 0.0F);
		this.entityData.define(DESIRED_VINE_Y_ROT, 0.0F);
		this.entityData.define(UNTIED, false);
		this.entityData.define(CLIENT_TICKS_EXISTED, 0);
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

		if (Math.abs(seekingVineYRot) <= 0.1F) {
			this.vineYRot = vineYRot + seekingVineYRot;
		} else if (seekingVineYRot > 0) {
			this.vineYRot = vineYRot + 0.03F;
		} else {
			this.vineYRot = vineYRot - 0.03F;
		}

		if (!this.level.isClientSide) {
			if (this.getY() >= 254 && this.isUntied()) {
				this.onBroken(true);
				this.discard();
			}

			if (this.ticksExisted % 50 == 0) {
				this.setDesiredVineYRot((float) (this.random.nextDouble() * 2 * Math.PI));
			}
		}

		if (this.shouldIncrementTicksExisted()) {
			this.incrementTicksExisted();
		}

		this.updateUntied();
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

	public void setVineYRot(float angle) {
		this.vineYRot = angle;
	}

	public float getVineYRot() {
		return this.vineYRot;
	}

	public void setDesiredVineYRot(float angle) {
		this.entityData.set(DESIRED_VINE_Y_ROT, angle);
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

	public void incrementTicksExisted() {
		if (this.level.isClientSide) {
			this.entityData.set(CLIENT_TICKS_EXISTED, this.entityData.get(CLIENT_TICKS_EXISTED) + 1);
		} else if (++this.ticksExisted % 20 == 0) {
			this.entityData.set(CLIENT_TICKS_EXISTED, this.ticksExisted);
		}
	}

	public int getTicksExisted() {
		return this.level.isClientSide ? this.entityData.get(CLIENT_TICKS_EXISTED) : this.ticksExisted;
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
