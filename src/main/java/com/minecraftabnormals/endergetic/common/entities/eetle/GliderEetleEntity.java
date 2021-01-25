package com.minecraftabnormals.endergetic.common.entities.eetle;

import com.minecraftabnormals.abnormals_core.client.ClientInfo;
import com.minecraftabnormals.abnormals_core.core.endimator.ControlledEndimation;
import com.minecraftabnormals.abnormals_core.core.endimator.Endimation;
import com.minecraftabnormals.abnormals_core.core.util.NetworkUtil;
import com.minecraftabnormals.endergetic.api.entity.pathfinding.EndergeticFlyingPathNavigator;
import com.minecraftabnormals.endergetic.common.entities.eetle.ai.glider.GliderEetleFlyGoal;
import com.minecraftabnormals.endergetic.common.entities.eetle.ai.glider.GliderEetleLandGoal;
import com.minecraftabnormals.endergetic.common.entities.eetle.ai.glider.GliderEetleTakeoffGoal;
import com.minecraftabnormals.endergetic.core.registry.other.EEDataSerializers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class GliderEetleEntity extends AbstractEetleEntity {
	private static final DataParameter<Boolean> FLYING = EntityDataManager.createKey(GliderEetleEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> MOVING = EntityDataManager.createKey(GliderEetleEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<TargetFlyingRotations> TARGET_FLYING_ROTATIONS = EntityDataManager.createKey(GliderEetleEntity.class, EEDataSerializers.TARGET_FLYING_ROTATIONS);
	public static final Endimation FLAP = new Endimation(22);
	public static final Endimation MUNCH = new Endimation(20);
	private final ControlledEndimation takeoffEndimation = new ControlledEndimation(15, 0);
	private final ControlledEndimation flyingEndimation = new ControlledEndimation(20, 0);
	private final FlyingRotations flyingRotations = new FlyingRotations();
	private boolean takeoffMoving;
	private int flyCooldown;
	private int ticksFlown;
	private float prevWingFlap, wingFlap;
	private float wingFlapSpeed;

	public GliderEetleEntity(EntityType<? extends AbstractEetleEntity> type, World world) {
		super(type, world);
		this.prevWingFlap = this.wingFlap = this.rand.nextFloat();
		this.takeoffEndimation.setDecrementing(true);
		this.flyingEndimation.setDecrementing(true);
		this.resetFlyCooldown();
	}

	@Override
	protected void registerData() {
		super.registerData();
		this.dataManager.register(FLYING, false);
		this.dataManager.register(MOVING, false);
		this.dataManager.register(TARGET_FLYING_ROTATIONS, new TargetFlyingRotations(0.0F, 0.0F));
	}

	@Override
	public void notifyDataManagerChange(DataParameter<?> key) {
		super.notifyDataManagerChange(key);
		if (FLYING.equals(key)) {
			if (this.isFlying()) {
				this.moveController = new GliderMoveController(this);
				this.navigator = new EndergeticFlyingPathNavigator(this, this.world);
			} else {
				this.idleDelay = this.rand.nextInt(41) + 30;
				this.moveController = new MovementController(this);
				this.navigator = this.createNavigator(this.world);
			}
		} else if (TARGET_FLYING_ROTATIONS.equals(key)) {
			this.flyingRotations.looking = true;
		}
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(3, new GliderEetleLandGoal(this));
		this.goalSelector.addGoal(4, new GliderEetleTakeoffGoal(this));
		this.goalSelector.addGoal(5, new GliderEetleFlyGoal(this));
		this.goalSelector.addGoal(6, new WaterAvoidingRandomWalkingGoal(this, 0.8D));
		this.goalSelector.addGoal(7, new LookRandomlyGoal(this));
		this.goalSelector.addGoal(8, new LookAtGoal(this, MobEntity.class, 8.0F));
	}

	@Override
	public void tick() {
		super.tick();

		if (!this.world.isRemote) {
			if (!this.isChild()) {
				if (this.flyCooldown > 0) this.flyCooldown--;

				if (this.isFlying()) {
					this.ticksFlown++;
				} else {
					this.ticksFlown = 0;
				}

				if (this.rand.nextFloat() < 0.005F && this.idleDelay <= 0 && !this.isFlying() && this.getAttackTarget() == null && this.isNoEndimationPlaying()) {
					NetworkUtil.setPlayingAnimationMessage(this, this.rand.nextFloat() < 0.6F ? FLAP : MUNCH);
					this.resetIdleFlapDelay();
				}
			}
		} else {
			if (!this.isChild()) {
				ControlledEndimation takeoff = this.takeoffEndimation;
				takeoff.update();
				takeoff.tick();
				if (takeoff.isAtMax()) {
					this.takeoffMoving = true;
					takeoff.setDecrementing(true);
				} else {
					if (this.takeoffMoving) {
						this.takeoffMoving = takeoff.getTick() > 15;
					} else {
						takeoff.setDecrementing(!this.isFlying());
					}
				}
				ControlledEndimation flying = this.flyingEndimation;
				flying.update();
				flying.tick();
				boolean moving = this.isMoving();
				flying.setDecrementing(!moving);
				this.wingFlapSpeed += moving ? (0.5F - this.wingFlapSpeed) * 0.1F : (0.125F - this.wingFlapSpeed) * 0.2F;
				this.prevWingFlap = this.wingFlap;
				this.wingFlap += this.wingFlapSpeed;
			}
			this.flyingRotations.tick(this.getTargetFlyingRotations());
		}
	}

	@Override
	public void travel(Vector3d travelVector) {
		if (this.isServerWorld() && this.isFlying()) {
			this.moveRelative(0.1F, travelVector);
			this.move(MoverType.SELF, this.getMotion());
			this.setMotion(this.getMotion().scale(0.8F));
			this.setMotion(this.getMotion().subtract(0, 0.01D, 0));
		} else {
			super.travel(travelVector);
		}
	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		compound.putInt("FlyCooldown", this.flyCooldown);
		compound.putBoolean("IsFlying", this.isFlying());
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		this.flyCooldown = compound.getInt("FlyCooldown");
		this.setFlying(compound.getBoolean("IsFlying"));
	}

	@Override
	public void setChild(boolean child) {
		super.setChild(child);
		if (child) {
			this.setFlying(false);
		}
	}

	public void setFlying(boolean flying) {
		this.dataManager.set(FLYING, flying);
	}

	public boolean isFlying() {
		return this.dataManager.get(FLYING);
	}

	public void setMoving(boolean moving) {
		this.dataManager.set(MOVING, moving);
	}

	public boolean isMoving() {
		return this.dataManager.get(MOVING);
	}

	public void setTargetFlyingRotations(TargetFlyingRotations flyingRotations) {
		this.dataManager.set(TARGET_FLYING_ROTATIONS, flyingRotations);
	}

	public TargetFlyingRotations getTargetFlyingRotations() {
		return this.dataManager.get(TARGET_FLYING_ROTATIONS);
	}

	public FlyingRotations getFlyingRotations() {
		return this.flyingRotations;
	}

	public void resetFlyCooldown() {
		this.flyCooldown = this.rand.nextInt(301) + 100;
	}

	public boolean canFly() {
		return this.flyCooldown <= 0;
	}

	public boolean shouldLand() {
		return this.ticksFlown >= 30;
	}

	public float getTakeoffProgress() {
		return this.takeoffEndimation.getAnimationProgress();
	}

	public float getFlyingProgress() {
		return this.flyingEndimation.getAnimationProgress();
	}

	public float getWingFlap() {
		return MathHelper.lerp(ClientInfo.getPartialTicks(), this.prevWingFlap, this.wingFlap);
	}

	@Override
	public Endimation[] getEndimations() {
		return new Endimation[] {
				FLAP,
				MUNCH
		};
	}

	static class GliderMoveController extends MovementController {

		public GliderMoveController(MobEntity mob) {
			super(mob);
		}

		public void tick() {
			MobEntity mob = this.mob;
			if (this.action == Action.MOVE_TO) {
				if (mob instanceof GliderEetleEntity) {
					GliderEetleEntity glider = (GliderEetleEntity) mob;
					glider.setMoving(true);
					this.action = Action.WAIT;
					double distanceX = this.posX - mob.getPosX();
					double distanceY = this.posY - mob.getPosY();
					double distanceZ = this.posZ - mob.getPosZ();
					double magnitude = distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ;
					if (magnitude < 0.00000025F) {
						mob.setMoveVertical(0.0F);
						mob.setMoveForward(0.0F);
						return;
					}

					float yaw = (float) (MathHelper.atan2(distanceZ, distanceX) * 57.3D) - 90.0F;
					mob.rotationYaw = this.limitAngle(mob.rotationYaw, yaw, 75.0F);
					float f1 = (float) (this.speed * mob.getAttributeValue(Attributes.FLYING_SPEED));
					mob.setAIMoveSpeed(f1);
					double horizontalMag = MathHelper.sqrt(distanceX * distanceX + distanceZ * distanceZ);
					float pitch = (float) (-(MathHelper.atan2(distanceY, horizontalMag) * 57.3D));
					float limitedPitch = this.limitAngle(mob.rotationPitch, pitch, 45.0F);
					mob.rotationPitch = limitedPitch;

					float targetRoll = 0.0F;
					Vector3d vector3d = mob.getLook(1.0F);
					Vector3d vector3d1 = mob.getMotion();
					double d0 = Entity.horizontalMag(vector3d1);
					double d1 = Entity.horizontalMag(vector3d);
					if (d0 > 0.0D && d1 > 0.0D) {
						double d2 = (vector3d1.x * vector3d.x + vector3d1.z * vector3d.z) / Math.sqrt(d0 * d1);
						double d3 = vector3d1.x * vector3d.z - vector3d1.z * vector3d.x;
						targetRoll = MathHelper.clamp((float) ((Math.signum(d3) * Math.acos(d2)) * 57.3D), -16.0F, 16.0F);
					}
					glider.setTargetFlyingRotations(new TargetFlyingRotations(limitedPitch, targetRoll));

					mob.setMoveVertical(distanceY > 0.0D ? f1 : -f1);
				}
			} else {
				if (mob instanceof GliderEetleEntity) {
					((GliderEetleEntity) mob).setMoving(false);
				}
				mob.setMoveVertical(0.0F);
				mob.setMoveForward(0.0F);
			}
		}

	}

	public static class FlyingRotations {
		private float prevFlyPitch, flyPitch;
		private float prevFlyRoll, flyRoll;
		private boolean looking;

		public void tick(TargetFlyingRotations targetRotations) {
			this.prevFlyPitch = this.flyPitch;
			this.prevFlyRoll = this.flyRoll;

			while (this.flyPitch - this.prevFlyPitch < -180.0F) {
				this.prevFlyPitch -= 360.0F;
			}

			while (this.flyPitch - this.prevFlyPitch >= 180.0F) {
				this.prevFlyPitch += 360.0F;
			}

			while (this.flyRoll - this.prevFlyRoll < -180.0F) {
				this.prevFlyRoll -= 360.0F;
			}

			while (this.flyRoll - this.prevFlyRoll >= 180.0F) {
				this.prevFlyRoll += 360.0F;
			}

			if (this.looking) {
				this.flyPitch = clampedRotate(this.flyPitch, targetRotations.targetFlyPitch);
				this.flyRoll = clampedRotate(this.flyRoll, targetRotations.targetFlyRoll);
				this.looking = false;
			} else {
				this.flyPitch = clampedRotate(this.flyPitch, 0.0F);
				this.flyRoll = clampedRotate(this.flyRoll, 0.0F);
			}
		}

		public float getRenderFlyPitch() {
			return MathHelper.lerp(ClientInfo.getPartialTicks(), this.prevFlyPitch, this.flyPitch);
		}

		public float getRenderFlyRoll() {
			return MathHelper.lerp(ClientInfo.getPartialTicks(), this.prevFlyRoll, this.flyRoll);
		}

		private static float clampedRotate(float from, float to) {
			float wrapSubtractDegrees = MathHelper.wrapSubtractDegrees(from, to);
			float clampedDelta = MathHelper.clamp(wrapSubtractDegrees, -5.0F, 5.0F);
			return from + clampedDelta;
		}
	}

	public static class TargetFlyingRotations {
		private final float targetFlyPitch;
		private final float targetFlyRoll;

		public TargetFlyingRotations(float targetFlyPitch, float targetFlyRoll) {
			this.targetFlyPitch = targetFlyPitch;
			this.targetFlyRoll = targetFlyRoll;
		}

		public float getTargetFlyPitch() {
			return this.targetFlyPitch;
		}

		public float getTargetFlyRoll() {
			return this.targetFlyRoll;
		}
	}
}
