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
	private static final DataParameter<Float> ORIGIN_X = EntityDataManager.createKey(AbstractBolloomEntity.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> ORIGIN_Y = EntityDataManager.createKey(AbstractBolloomEntity.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> ORIGIN_Z = EntityDataManager.createKey(AbstractBolloomEntity.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> ANGLE = EntityDataManager.createKey(AbstractBolloomEntity.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> DESIRED_ANGLE = EntityDataManager.createKey(AbstractBolloomEntity.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> SWAY = EntityDataManager.createKey(AbstractBolloomEntity.class, DataSerializers.FLOAT);
	private static final DataParameter<Integer> TICKS_EXISTED = EntityDataManager.createKey(AbstractBolloomEntity.class, DataSerializers.VARINT); //Vanilla's ticksExisted isn't synced between server and client
	private static final DataParameter<Boolean> UNTIED = EntityDataManager.createKey(AbstractBolloomEntity.class, DataSerializers.BOOLEAN);

	private float prevVineAngle;
	private float prevAngle;

	protected AbstractBolloomEntity(EntityType<?> entityType, World world) {
		super(entityType, world);
	}

	@Override
	protected void registerData() {
		this.dataManager.register(ORIGIN_X, 0.0F);
		this.dataManager.register(ORIGIN_Y, 0.0F);
		this.dataManager.register(ORIGIN_Z, 0.0F);
		this.dataManager.register(ANGLE, 0.0F);
		this.dataManager.register(DESIRED_ANGLE, 0.0F);
		this.dataManager.register(SWAY, 0.0F);
		this.dataManager.register(UNTIED, false);
		this.dataManager.register(TICKS_EXISTED, 0);
	}

	@Override
	public void tick() {
		this.prevPosX = this.getPosX();
		this.prevPosY = this.getPosY();
		this.prevPosZ = this.getPosZ();
		this.prevVineAngle = this.getVineAngle();
		this.prevAngle = this.getAngle();

		this.setSway(MathHelper.sin((float) (2 * Math.PI / 100 * this.getTicksExisted())) * 0.5F);
		this.updatePositionAndMotion(Math.sin(-this.getAngle()), Math.cos(-this.getAngle()));

		if (!this.world.isRemote) {
			if (this.getTicksExisted() % 45 == 0) {
				this.setDesiredAngle((float) (this.rand.nextDouble() * 2 * Math.PI));
			}

			if (this.getPosY() >= 254 && this.isUntied()) {
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
		this.extinguish();
		if (this.shouldIncrementTicksExisted()) {
			this.incrementTicksExisted();
		}
	}

	@Override
	protected void writeAdditional(CompoundNBT compound) {
		compound.putBoolean("UNTIED", this.isUntied());
		compound.putFloat("ORIGIN_X", this.getOriginX());
		compound.putFloat("ORIGIN_Y", this.getOriginY());
		compound.putFloat("ORIGIN_Z", this.getOriginZ());
	}

	@Override
	protected void readAdditional(CompoundNBT compound) {
		this.setUntied(compound.getBoolean("UNTIED"));
		if (compound.contains("ORIGIN_X", 5) && compound.contains("ORIGIN_Y", 5) && compound.contains("ORIGIN_Z", 5)) {
			this.setOrigin(compound.getFloat("ORIGIN_X"), compound.getFloat("ORIGIN_Y"), compound.getFloat("ORIGIN_Z"));
		} else {
			this.setUntied(true);
		}
	}

	public void setOrigin(float x, float y, float z) {
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

	public void incrementTicksExisted() {
		this.dataManager.set(TICKS_EXISTED, this.getTicksExisted() + 1);
	}

	public int getTicksExisted() {
		return this.dataManager.get(TICKS_EXISTED);
	}

	@OnlyIn(Dist.CLIENT)
	public float[] getVineAnimation(float partialTicks) {
		return new float[] {
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
		this.playSound(SoundEvents.BLOCK_WET_GRASS_BREAK, 1.0F, 1.0F);
		this.doParticles();
	}

	protected void doParticles() {
		if (this.world instanceof ServerWorld) {
			((ServerWorld) this.world).spawnParticle(new BlockParticleData(ParticleTypes.BLOCK, EEBlocks.BOLLOOM_PARTICLE.get().getDefaultState()), this.getPosX(), this.getPosY() + (double)this.getHeight() / 1.5D, this.getPosZ(), 10, this.getWidth() / 4.0F, this.getHeight() / 4.0F, this.getWidth() / 4.0F, 0.05D);
		}
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public boolean canBePushed() {
		return this.isAlive();
	}

	@Override
	public boolean canRenderOnFire() {
		return false;
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	public boolean isInvulnerableTo(DamageSource source) {
		return this.isInvulnerable() && source != DamageSource.OUT_OF_WORLD && source != DamageSource.CRAMMING;
	}

	@Override
	protected Vector3d handlePistonMovement(Vector3d pos) {
		return Vector3d.ZERO;
	}

	@Override
	public boolean hitByEntity(Entity entityIn) {
		return entityIn instanceof PlayerEntity && this.attackEntityFrom(DamageSource.causePlayerDamage((PlayerEntity) entityIn), 0.0F);
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (this.isInvulnerableTo(source)) {
			return false;
		} else {
			if (this.isAlive() && !this.world.isRemote) {
				this.remove();
				this.markVelocityChanged();
				this.onBroken(true);
			}
			return true;
		}
	}

	@Override
	public void addVelocity(double x, double y, double z) {
		if(!this.isUntied()) return;
		super.addVelocity(x, y, z);
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return super.getRenderBoundingBox().grow(5);
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
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
