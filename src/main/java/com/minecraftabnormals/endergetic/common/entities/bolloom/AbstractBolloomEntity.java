package com.minecraftabnormals.endergetic.common.entities.bolloom;

import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

/**
 * @author - SmellyModder (Luke Tonon)
 */
public abstract class AbstractBolloomEntity extends Entity {
	private static final DataParameter<Float> ORIGIN_X = EntityDataManager.defineId(AbstractBolloomEntity.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> ORIGIN_Y = EntityDataManager.defineId(AbstractBolloomEntity.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> ORIGIN_Z = EntityDataManager.defineId(AbstractBolloomEntity.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> ANGLE = EntityDataManager.defineId(AbstractBolloomEntity.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> DESIRED_ANGLE = EntityDataManager.defineId(AbstractBolloomEntity.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> SWAY = EntityDataManager.defineId(AbstractBolloomEntity.class, DataSerializers.FLOAT);
	private static final DataParameter<Integer> TICKS_EXISTED = EntityDataManager.defineId(AbstractBolloomEntity.class, DataSerializers.INT); //Vanilla's ticksExisted isn't synced between server and client
	private static final DataParameter<Boolean> UNTIED = EntityDataManager.defineId(AbstractBolloomEntity.class, DataSerializers.BOOLEAN);

	private float prevVineAngle;
	private float prevAngle;

	protected AbstractBolloomEntity(EntityType<?> entityType, World world) {
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

		this.setSway(MathHelper.sin((float) (2 * Math.PI / 100 * this.getTicksExisted())) * 0.5F);
		this.updatePositionAndMotion(MathHelper.sin(-this.getAngle()), MathHelper.cos(-this.getAngle()));

		if (!this.level.isClientSide) {
			if (this.getTicksExisted() % 45 == 0) {
				this.setDesiredAngle((float) (this.random.nextDouble() * 2 * Math.PI));
			}

			if (this.getY() >= 254 && this.isUntied()) {
				this.onBroken(true);
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

		this.updateUntied();
		this.clearFire();
		if (this.shouldIncrementTicksExisted()) {
			this.incrementTicksExisted();
		}
	}

	@Override
	protected void addAdditionalSaveData(CompoundNBT compound) {
		compound.putBoolean("UNTIED", this.isUntied());
		compound.putFloat("ORIGIN_X", this.getOriginX());
		compound.putFloat("ORIGIN_Y", this.getOriginY());
		compound.putFloat("ORIGIN_Z", this.getOriginZ());
	}

	@Override
	protected void readAdditionalSaveData(CompoundNBT compound) {
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
				MathHelper.lerp(partialTicks, this.prevVineAngle, this.getVineAngle()),
				MathHelper.lerp(partialTicks, this.prevAngle, this.getAngle()),
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
		if (this.level instanceof ServerWorld) {
			((ServerWorld) this.level).sendParticles(new BlockParticleData(ParticleTypes.BLOCK, EEBlocks.BOLLOOM_PARTICLE.get().defaultBlockState()), this.getX(), this.getY() + (double) this.getBbHeight() / 1.5D, this.getZ(), 10, this.getBbWidth() / 4.0F, this.getBbHeight() / 4.0F, this.getBbWidth() / 4.0F, 0.05D);
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
	protected boolean isMovementNoisy() {
		return false;
	}

	@Override
	public boolean isInvulnerableTo(DamageSource source) {
		return this.isInvulnerable() && source != DamageSource.OUT_OF_WORLD && source != DamageSource.CRAMMING;
	}

	@Override
	protected Vector3d limitPistonMovement(Vector3d pos) {
		return Vector3d.ZERO;
	}

	@Override
	public boolean skipAttackInteraction(Entity entityIn) {
		return entityIn instanceof PlayerEntity && this.hurt(DamageSource.playerAttack((PlayerEntity) entityIn), 0.0F);
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (this.isInvulnerableTo(source)) {
			return false;
		} else {
			if (this.isAlive() && !this.level.isClientSide) {
				this.remove();
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
	public AxisAlignedBB getBoundingBoxForCulling() {
		return super.getBoundingBoxForCulling().inflate(5);
	}

	@Nullable
	public AxisAlignedBB getCollisionBoundingBox() {
		return this.getBoundingBox();
	}

	@Nullable
	public AxisAlignedBB getCollisionBox(Entity entityIn) {
		return entityIn.isPushable() ? entityIn.getBoundingBox() : null;
	}

	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
