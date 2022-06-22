package com.minecraftabnormals.endergetic.common.entities.eetle;

import com.minecraftabnormals.abnormals_core.client.ClientInfo;
import com.minecraftabnormals.abnormals_core.common.world.storage.tracking.IDataManager;
import com.minecraftabnormals.abnormals_core.core.endimator.ControlledEndimation;
import com.minecraftabnormals.abnormals_core.core.endimator.Endimation;
import com.minecraftabnormals.abnormals_core.core.util.NetworkUtil;
import com.minecraftabnormals.endergetic.api.entity.pathfinding.EndergeticFlyingPathNavigator;
import com.minecraftabnormals.endergetic.common.entities.eetle.ai.glider.*;
import com.minecraftabnormals.endergetic.common.entities.eetle.flying.*;
import com.minecraftabnormals.endergetic.core.registry.other.EEDataProcessors;
import com.minecraftabnormals.endergetic.core.registry.other.EEDataSerializers;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.UUID;

public class GliderEetleEntity extends AbstractEetleEntity implements IFlyingEetle {
	private static final DataParameter<Boolean> FLYING = EntityDataManager.createKey(GliderEetleEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> MOVING = EntityDataManager.createKey(GliderEetleEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> DIVING = EntityDataManager.createKey(GliderEetleEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<TargetFlyingRotations> TARGET_FLYING_ROTATIONS = EntityDataManager.createKey(GliderEetleEntity.class, EEDataSerializers.TARGET_FLYING_ROTATIONS);
	private static final DataParameter<EntitySize> CAUGHT_SIZE = EntityDataManager.createKey(GliderEetleEntity.class, EEDataSerializers.ENTITY_SIZE);
	public static final EntitySize DEFAULT_SIZE = EntitySize.fixed(1.0F, 0.85F);
	public static final AttributeModifier CAUGHT_KNOCKBACK_RESISTANCE = new AttributeModifier(UUID.fromString("17da0b48-6e5f-11eb-9439-0242ac130002"), "Caught target knockback resistance", 0.8F, AttributeModifier.Operation.ADDITION);
	public static final Endimation FLAP = new Endimation(22);
	public static final Endimation MUNCH = new Endimation(25);
	private final ControlledEndimation takeoffEndimation = new ControlledEndimation(15, 0);
	private final ControlledEndimation flyingEndimation = new ControlledEndimation(20, 0);
	private final FlyingRotations flyingRotations = new FlyingRotations();
	private GliderEetleLandGoal landGoal;
	private GliderEetleTakeoffGoal takeoffGoal;
	private GliderEetleFlyGoal flyGoal;
	private GliderEetleGrabGoal grabGoal;
	private GliderEetleFleeAttackerGoal fleeAttackerGoal;
	private GliderEetleHoverNearTargetGoal hoverNearTargetGoal;
	private GliderEetleMunchGoal munchGoal;
	private GliderEetleDropOffGoal dropOffGoal;
	private GliderEetleBiteGoal biteGoal;
	private GliderEetleDiveGoal diveGoal;
	@Nullable
	public LivingEntity groundedAttacker;
	private boolean takeoffMoving;
	private int flyCooldown;
	private int ticksFlown;
	private int groundedTimer;
	private float prevWingFlap, wingFlap;
	private float wingFlapSpeed;
	private float damageTakenWhileFlying;

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
		this.dataManager.register(DIVING, false);
		this.dataManager.register(TARGET_FLYING_ROTATIONS, TargetFlyingRotations.ZERO);
		this.dataManager.register(CAUGHT_SIZE, DEFAULT_SIZE);
	}

	@Override
	public void notifyDataManagerChange(DataParameter<?> key) {
		super.notifyDataManagerChange(key);
		if (FLYING.equals(key)) {
			if (!this.isChild() && this.isFlying()) {
				this.moveController = new FlyingEetleMoveController<>(this, 16.0F, 50.0F);
				this.navigator = new EndergeticFlyingPathNavigator(this, this.world);
			} else {
				if (!this.isChild()) {
					for (Entity passenger : this.getPassengers()) {
						passenger.dismount();
						World world = this.world;
						if (passenger instanceof LivingEntity && passenger.getRidingEntity() != this && !world.isRemote) {
							AxisAlignedBB passengerBoundingBox = passenger.getBoundingBox();
							double xSize = passengerBoundingBox.getXSize();
							double zSize = passengerBoundingBox.getZSize();
							AxisAlignedBB detectionBox = AxisAlignedBB.withSizeAtOrigin(xSize * 0.8F, 0.2F, zSize * 0.8F).offset(passenger.getPosX(), passenger.getPosY(), passenger.getPosZ());
							if (world.func_241457_a_(passenger, detectionBox, (state, pos) -> !state.getCollisionShape(world, pos).isEmpty()).findAny().isPresent()) {
								BlockPos pos = passenger.getPosition();
								if (!hasCollisionsAbove(world, pos.toMutable(), getEntitySizeBlocksCeil(passenger))) {
									passenger.setPosition(passenger.getPosX(), pos.getY() + 1.0F, passenger.getPosZ());
								}
							}
						}
					}
				}
				this.idleDelay = this.rand.nextInt(41) + 30;
				this.moveController = new GroundEetleMoveController(this);
				this.navigator = this.createNavigator(this.world);
			}
		} else if (TARGET_FLYING_ROTATIONS.equals(key)) {
			this.flyingRotations.setLooking(true);
		} else if (CAUGHT_SIZE.equals(key)) {
			this.recalculateSize();
		}
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(1, this.diveGoal = new GliderEetleDiveGoal(this));
		this.goalSelector.addGoal(1, this.dropOffGoal = new GliderEetleDropOffGoal(this));
		this.goalSelector.addGoal(2, this.biteGoal = new GliderEetleBiteGoal(this));
		this.goalSelector.addGoal(2, this.grabGoal = new GliderEetleGrabGoal(this));
		this.goalSelector.addGoal(2, this.munchGoal = new GliderEetleMunchGoal(this));
		this.goalSelector.addGoal(2, this.hoverNearTargetGoal = new GliderEetleHoverNearTargetGoal(this));
		this.goalSelector.addGoal(3, this.fleeAttackerGoal = new GliderEetleFleeAttackerGoal(this));
		this.goalSelector.addGoal(3, this.landGoal = new GliderEetleLandGoal(this));
		this.goalSelector.addGoal(4, this.takeoffGoal = new GliderEetleTakeoffGoal(this));
		this.goalSelector.addGoal(5, this.flyGoal = new GliderEetleFlyGoal(this));
		this.goalSelector.addGoal(6, new WaterAvoidingRandomWalkingGoal(this, 0.8D));
		this.goalSelector.addGoal(7, new GliderEetleLookRandomlyGoal(this));
		this.goalSelector.addGoal(8, new LookAtGoal(this, MobEntity.class, 8.0F) {
			@Override
			public boolean shouldExecute() {
				return this.entity.getPassengers().isEmpty() && super.shouldExecute();
			}

			@Override
			public boolean shouldContinueExecuting() {
				return this.entity.getPassengers().isEmpty() && super.shouldContinueExecuting();
			}
		});
	}

	public static AttributeModifierMap.MutableAttribute getAttributes() {
		return MobEntity.func_233666_p_()
				.createMutableAttribute(Attributes.ATTACK_DAMAGE, 4.0F)
				.createMutableAttribute(Attributes.FLYING_SPEED, 0.35F)
				.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.2F)
				.createMutableAttribute(Attributes.ARMOR, 4.0F)
				.createMutableAttribute(Attributes.MAX_HEALTH, 25.0F)
				.createMutableAttribute(Attributes.FOLLOW_RANGE, 28.0F)
				.createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 0.2F);
	}

	@Override
	public void tick() {
		super.tick();

		if (!this.world.isRemote) {
			ModifiableAttributeInstance knockbackResistance = this.getAttribute(Attributes.KNOCKBACK_RESISTANCE);
			if (knockbackResistance != null) {
				boolean noPassengers = this.getPassengers().isEmpty();
				boolean hasModifier = knockbackResistance.hasModifier(CAUGHT_KNOCKBACK_RESISTANCE);
				if (noPassengers && hasModifier || this.isChild()) {
					knockbackResistance.removeModifier(CAUGHT_KNOCKBACK_RESISTANCE);
				} else if (!noPassengers && !hasModifier) {
					knockbackResistance.applyNonPersistentModifier(CAUGHT_KNOCKBACK_RESISTANCE);
				}
			}

			if (!this.isChild()) {
				if (this.flyCooldown > 0) this.flyCooldown--;

				if (this.isFlying()) {
					this.ticksFlown++;

					if (this.isGrounded()) {
						this.setFlying(false);
					}
				} else {
					this.ticksFlown = 0;
				}

				if (this.rand.nextFloat() < 0.005F && this.idleDelay <= 0 && !this.isFlying() && this.getAttackTarget() == null && this.isNoEndimationPlaying()) {
					NetworkUtil.setPlayingAnimationMessage(this, this.rand.nextFloat() < 0.6F ? FLAP : MUNCH);
					this.resetIdleFlapDelay();
				}
			}

			if (this.isGrounded()) {
				this.groundedTimer--;
				if (this.groundedTimer <= 0) {
					this.groundedAttacker = null;
				}
			}
			if (this.isDiving()) {
				this.setDiving(false);
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
		if (this.isServerWorld() && !this.isChild() && this.isFlying()) {
			this.moveRelative(0.1F, travelVector);
			this.move(MoverType.SELF, this.getMotion());
			this.setMotion(this.getMotion().scale(this.isDiving() ? 0.95F : 0.8F));
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

	@Override
	protected void updateGoals(GoalSelector goalSelector, GoalSelector targetSelector, boolean child) {
		super.updateGoals(goalSelector, targetSelector, child);
		if (child) {
			goalSelector.removeGoal(this.landGoal);
			goalSelector.removeGoal(this.takeoffGoal);
			goalSelector.removeGoal(this.flyGoal);
			goalSelector.removeGoal(this.grabGoal);
			goalSelector.removeGoal(this.fleeAttackerGoal);
			goalSelector.removeGoal(this.hoverNearTargetGoal);
			goalSelector.removeGoal(this.munchGoal);
			goalSelector.removeGoal(this.dropOffGoal);
			goalSelector.removeGoal(this.biteGoal);
			goalSelector.removeGoal(this.diveGoal);
		} else {
			goalSelector.addGoal(3, this.landGoal);
			goalSelector.addGoal(4, this.takeoffGoal);
			goalSelector.addGoal(5, this.flyGoal);
			goalSelector.addGoal(2, this.grabGoal);
			goalSelector.addGoal(3, this.fleeAttackerGoal);
			goalSelector.addGoal(2, this.hoverNearTargetGoal);
			goalSelector.addGoal(2, this.munchGoal);
			goalSelector.addGoal(1, this.dropOffGoal);
			goalSelector.addGoal(2, this.biteGoal);
			goalSelector.addGoal(1, this.diveGoal);
		}
	}

	@Override
	public void updatePassenger(Entity passenger) {
		this.setRenderYawOffset(this.rotationYaw);
		if (this.isPassenger(passenger)) {
			if (passenger instanceof LivingEntity && !isEntityLarge(passenger)) {
				AxisAlignedBB boundingBox = passenger.getBoundingBox();
				float y = (float) (boundingBox.maxY - boundingBox.minY) * -0.25F;
				float pitch = this.flyingRotations.getFlyPitch();
				Vector3d riderPos = (new Vector3d(0.8D + Math.abs(pitch * 0.002F), y, 0.0D)).rotateYaw(-this.rotationYaw * ((float)Math.PI / 180F) - ((float) Math.PI / 2F)).rotatePitch(pitch * ((float)Math.PI / 180F));
				passenger.setPosition(this.getPosX() + riderPos.x, this.getPosY() + 0.25F + riderPos.y, this.getPosZ() + riderPos.z);
			} else {
				super.updatePassenger(passenger);
			}
		}
	}

	@Override
	protected void addPassenger(Entity passenger) {
		super.addPassenger(passenger);
		if (!this.world.isRemote && passenger instanceof LivingEntity && passenger.getRidingEntity() == this && this.getPassengers().indexOf(passenger) == 0) {
			this.setCaughtSize(EntitySize.fixed(1.0F + passenger.getSize(passenger.getPose()).width, 0.85F));
		}
	}

	@Override
	protected void removePassenger(Entity passenger) {
		super.removePassenger(passenger);
		if (passenger.getRidingEntity() != this && passenger instanceof IDataManager) {
			IDataManager dataManager = (IDataManager) passenger;
			dataManager.setValue(EEDataProcessors.CATCHING_COOLDOWN, dataManager.getValue(EEDataProcessors.CATCHING_COOLDOWN) + 40 + this.rand.nextInt(11));
		}
		if (!this.world.isRemote) {
			if (!this.getPassengers().isEmpty()) {
				Entity indexZeroPassenger = this.getPassengers().get(0);
				if (indexZeroPassenger instanceof LivingEntity && passenger.getRidingEntity() == this) {
					this.setCaughtSize(EntitySize.fixed(1.0F + passenger.getSize(passenger.getPose()).width, 0.85F));
				} else {
					this.setCaughtSize(DEFAULT_SIZE);
				}
			} else {
				this.setCaughtSize(DEFAULT_SIZE);
			}
		}
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		float prevHealth = this.getHealth();
		if (super.attackEntityFrom(source, amount)) {
			Entity attacker = source.getTrueSource();
			if (attacker instanceof LivingEntity && this.isFlying() && !this.getPassengers().isEmpty()) {
				this.damageTakenWhileFlying += Math.max((prevHealth - this.getHealth()) - this.rand.nextInt(3), 0.0F);
				if (this.damageTakenWhileFlying >= 16.0F) {
					this.damageTakenWhileFlying = 0.0F;
					this.groundedAttacker = (LivingEntity) attacker;
					this.makeGrounded();
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public EntitySize getSize(Pose poseIn) {
		if (!this.isChild() && !this.getPassengers().isEmpty()) {
			return this.getCaughtSize();
		}
		return super.getSize(poseIn);
	}

	@Override
	public boolean canRiderInteract() {
		return true;
	}

	@Override
	public boolean shouldRiderSit() {
		return false;
	}

	public void makeGrounded() {
		this.groundedTimer = this.rand.nextInt(41) + 80;
		this.setFlying(false);
	}

	public void setFlying(boolean flying) {
		this.dataManager.set(FLYING, flying);
		if (!flying) {
			this.setMoving(false);
		}
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

	public void setDiving(boolean diving) {
		this.dataManager.set(DIVING, diving);
	}

	public boolean isDiving() {
		return this.dataManager.get(DIVING);
	}

	public void setTargetFlyingRotations(TargetFlyingRotations flyingRotations) {
		this.dataManager.set(TARGET_FLYING_ROTATIONS, flyingRotations);
	}

	public TargetFlyingRotations getTargetFlyingRotations() {
		return this.dataManager.get(TARGET_FLYING_ROTATIONS);
	}

	public void setCaughtSize(EntitySize size) {
		this.dataManager.set(CAUGHT_SIZE, size);
	}

	public EntitySize getCaughtSize() {
		return this.dataManager.get(CAUGHT_SIZE);
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

	public boolean isGrounded() {
		return this.groundedTimer > 0;
	}

	public boolean shouldLand() {
		return this.getPassengers().isEmpty() && this.ticksFlown >= 30;
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
		return new Endimation[] {FLAP, MUNCH, GROW_UP};
	}

	public static boolean isEntityLarge(Entity entity) {
		return entity.getBoundingBox().getAverageEdgeLength() >= 1.3F;
	}

	private static int getEntitySizeBlocksCeil(Entity entity) {
		return (int) Math.ceil(entity.getBoundingBox().getYSize());
	}

	private static boolean hasCollisionsAbove(World world, BlockPos.Mutable mutable, int blocksAbove) {
		int y = mutable.getY();
		for (int i = 1; i <= blocksAbove; i++) {
			mutable.setY(y + i);
			if (!world.getBlockState(mutable).getCollisionShape(world, mutable).isEmpty()) {
				return true;
			}
		}
		return false;
	}
}