package com.minecraftabnormals.endergetic.common.entities.eetle;

import com.minecraftabnormals.abnormals_core.client.ClientInfo;
import com.minecraftabnormals.abnormals_core.core.endimator.ControlledEndimation;
import com.minecraftabnormals.abnormals_core.core.endimator.Endimation;
import com.minecraftabnormals.abnormals_core.core.endimator.entity.IEndimatedEntity;
import com.minecraftabnormals.abnormals_core.core.util.NetworkUtil;
import com.minecraftabnormals.endergetic.api.entity.pathfinding.EndergeticFlyingPathNavigator;
import com.minecraftabnormals.endergetic.api.entity.util.DetectionHelper;
import com.minecraftabnormals.endergetic.client.particles.EEParticles;
import com.minecraftabnormals.endergetic.client.particles.data.CorrockCrownParticleData;
import com.minecraftabnormals.endergetic.common.entities.eetle.ai.brood.*;
import com.minecraftabnormals.endergetic.common.entities.eetle.flying.*;
import com.minecraftabnormals.endergetic.common.tileentities.EetleEggTileEntity;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.minecraftabnormals.endergetic.core.registry.other.EEDataSerializers;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class BroodEetleEntity extends MonsterEntity implements IEndimatedEntity, IFlyingEetle {
	private static final Field SIZE_FIELD = ObfuscationReflectionHelper.findField(Entity.class, "field_213325_aI");
	private static final Field EYE_HEIGHT_FIELD = ObfuscationReflectionHelper.findField(Entity.class, "field_213326_aJ");
	private static final DataParameter<Boolean> FIRING_CANNON = EntityDataManager.defineId(BroodEetleEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> FLYING = EntityDataManager.defineId(BroodEetleEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> MOVING = EntityDataManager.defineId(BroodEetleEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> DROPPING_EGGS = EntityDataManager.defineId(BroodEetleEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> SLEEPING = EntityDataManager.defineId(BroodEetleEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<TargetFlyingRotations> TARGET_FLYING_ROTATIONS = EntityDataManager.defineId(BroodEetleEntity.class, EEDataSerializers.TARGET_FLYING_ROTATIONS);
	private static final DataParameter<Integer> EGG_SACK_ID = EntityDataManager.defineId(BroodEetleEntity.class, DataSerializers.INT);
	private static final DataParameter<HealthStage> HEALTH_STAGE = EntityDataManager.defineId(BroodEetleEntity.class, EEDataSerializers.BROOD_HEALTH_STAGE);
	public static final Endimation FLAP = new Endimation(22);
	public static final Endimation MUNCH = new Endimation(25);
	public static final Endimation ATTACK = new Endimation(12);
	public static final Endimation SLAM = new Endimation(20);
	public static final Endimation LAUNCH = new Endimation(18);
	public static final Endimation AIR_CHARGE = new Endimation(80);
	public static final Endimation AIR_SLAM = new Endimation(11);
	public static final Endimation DEATH = new Endimation(115);
	private static final EntitySize FLYING_SIZE = EntitySize.fixed(1.875F, 2.125F);
	private static final EntitySize FINAL_STAGE_SIZE = EntitySize.fixed(2.1875F, 2.125F);
	private final ControlledEndimation eggCannonEndimation = new ControlledEndimation(20, 0);
	private final ControlledEndimation eggMouthEndimation = new ControlledEndimation(15, 0);
	private final ControlledEndimation takeoffEndimation = new ControlledEndimation(15, 0);
	private final ControlledEndimation eggCannonFlyingEndimation = new ControlledEndimation(20, 0);
	private final ControlledEndimation flyingEndimation = new ControlledEndimation(20, 0);
	private final ControlledEndimation sleepingEndimation = new ControlledEndimation(20, 0);
	private final ControlledEndimation healPulseEndimation = new ControlledEndimation(10, 0);
	private final ServerBossInfo bossInfo = new ServerBossInfo(this.getDisplayName(), BossInfo.Color.PURPLE, BossInfo.Overlay.NOTCHED_6);
	private final Set<ServerPlayerEntity> trackedPlayers = new HashSet<>();
	private final FlyingRotations flyingRotations = new FlyingRotations();
	private final Set<LivingEntity> revengeTargets = new HashSet<>();
	private Endimation endimation = BLANK_ANIMATION;
	public final HeadTiltDirection headTiltDirection;
	@Nullable
	public BlockPos takeoffPos;
	private int animationTick;
	private int idleDelay;
	private int slamCooldown;
	private int eggCannonCooldown;
	private int ticksFlying;
	private int flyCooldown;
	private int eggDropOffCooldown;
	private boolean takeoffMoving;
	private float prevWingFlap, wingFlap;
	private float wingFlapSpeed;
	private float prevHealthPercentage;
	public boolean wokenUpAggressively;

	public BroodEetleEntity(EntityType<? extends BroodEetleEntity> type, World world) {
		super(type, world);
		this.headTiltDirection = this.getRandom().nextBoolean() ? HeadTiltDirection.LEFT : HeadTiltDirection.RIGHT;
		this.xpReward = 50;
		this.prevWingFlap = this.wingFlap = this.random.nextFloat();
		this.prevHealthPercentage = 1.0F;
		this.takeoffEndimation.setDecrementing(true);
		this.flyingEndimation.setDecrementing(true);
		this.healPulseEndimation.setDecrementing(true);
		this.resetIdleFlapDelay();
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new BroodEetleLastStageGoal(this));
		this.goalSelector.addGoal(0, new BroodEetleSleepGoal(this));
		this.goalSelector.addGoal(1, new BroodEetleDropEggsGoal(this));
		this.goalSelector.addGoal(1, new BroodEetleAirSlamGoal(this));
		this.goalSelector.addGoal(2, new BroodEetleLandGoal(this));
		this.goalSelector.addGoal(3, new BroodEetleFlyNearPosGoal(this));
		this.goalSelector.addGoal(3, new BroodEetleLaunchEggsGoal(this));
		this.goalSelector.addGoal(4, new BroodEetleTakeoffGoal(this));
		this.goalSelector.addGoal(5, new BroodEetleSlamGoal(this));
		this.goalSelector.addGoal(6, new BroodEetleFlingGoal(this));
		this.goalSelector.addGoal(7, new LookRandomlyGoal(this));
		this.goalSelector.addGoal(8, new LookAtGoal(this, AbstractEetleEntity.class, 8.0F));
		this.targetSelector.addGoal(1, new BroodEetleHurtByTargetGoal(this));
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(FIRING_CANNON, false);
		this.entityData.define(FLYING, false);
		this.entityData.define(MOVING, false);
		this.entityData.define(DROPPING_EGGS, false);
		this.entityData.define(SLEEPING, false);
		this.entityData.define(TARGET_FLYING_ROTATIONS, TargetFlyingRotations.ZERO);
		this.entityData.define(EGG_SACK_ID, -1);
		this.entityData.define(HEALTH_STAGE, HealthStage.ZERO);
	}

	@Override
	public void onSyncedDataUpdated(DataParameter<?> key) {
		super.onSyncedDataUpdated(key);
		if (FLYING.equals(key)) {
			if (this.isFlying()) {
				this.moveControl = new FlyingEetleMoveController<>(this, 10.0F, 30.0F);
				this.navigation = new EndergeticFlyingPathNavigator(this, this.level);
			} else {
				this.moveControl = new MovementController(this);
				this.navigation = this.createNavigation(this.level);
			}
			this.resetIdleFlapDelay();
			this.refreshDimensions();
			BroodEggSackEntity eggSackEntity = this.getEggSack(this.level);
			if (eggSackEntity != null) {
				eggSackEntity.refreshDimensions();
			}
		} else if (TARGET_FLYING_ROTATIONS.equals(key)) {
			this.flyingRotations.setLooking(true);
		} else if (SLEEPING.equals(key) && !this.isSleeping()) {
			for (ServerPlayerEntity playerEntity : this.trackedPlayers) {
				this.bossInfo.addPlayer(playerEntity);
			}
		} else if (HEALTH_STAGE.equals(key)) {
			this.refreshDimensions();
			BroodEggSackEntity eggSackEntity = this.getEggSack(this.level);
			if (eggSackEntity != null) {
				eggSackEntity.refreshDimensions();
			}
		}
	}

	//TODO: Possibly tweak these values
	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return MobEntity.createMobAttributes()
				.add(Attributes.ATTACK_DAMAGE, 11.0F)
				.add(Attributes.FLYING_SPEED, 0.35F)
				.add(Attributes.MOVEMENT_SPEED, 0.2F)
				.add(Attributes.ARMOR, 6.0F)
				.add(Attributes.MAX_HEALTH, 300.0F)
				.add(Attributes.FOLLOW_RANGE, 32.0F)
				.add(Attributes.KNOCKBACK_RESISTANCE, 1.0F)
				.add(Attributes.ATTACK_KNOCKBACK, 2.0D);
	}

	@Override
	public void tick() {
		super.tick();
		this.endimateTick();

		World world = this.level;
		if (this.isDeadOrDying()) {
			if (!this.isEndimationPlaying(DEATH) && !world.isClientSide) {
				NetworkUtil.setPlayingAnimationMessage(this, DEATH);
			}
			if (++this.deathTime >= 105) {
				if (!world.isClientSide) {
					ItemEntity elytra = this.spawnAtLocation(Items.ELYTRA);
					if (elytra != null) {
						elytra.setExtendedLifetime();
					}
					this.remove();
					if (world instanceof ServerWorld) {
						Vector3d eggSackPos = BroodEggSackEntity.getEggPos(this.position(), this.yBodyRot, this.getEggCannonProgressServer(), this.getEggCannonFlyingProgressServer(), this.getFlyingRotations().getFlyPitch(), this.isOnLastHealthStage());
						((ServerWorld) world).sendParticles(new BlockParticleData(ParticleTypes.BLOCK, EEBlocks.EETLE_EGG.get().defaultBlockState()), eggSackPos.x(), eggSackPos.y() + 0.83F, eggSackPos.z(), 20, 0.3125F, 0.3125F, 0.3125F, 0.2D);
						((ServerWorld) world).sendParticles(new CorrockCrownParticleData(EEParticles.END_CROWN.get(), true), eggSackPos.x(), eggSackPos.y() + 0.83F, eggSackPos.z(), 30, 0.3125F, 0.3125F, 0.3125F, 0.2D);
					}
				} else {
					for (int i = 0; i < 20; ++i) {
						world.addParticle(ParticleTypes.POOF, this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D), this.random.nextGaussian() * 0.02D, this.random.nextGaussian() * 0.02D, this.random.nextGaussian() * 0.02D);
					}
				}
			}
		}

		if (!world.isClientSide) {
			if (this.idleDelay > 0) this.idleDelay--;
			if (this.slamCooldown > 0) this.slamCooldown--;
			if (this.eggCannonCooldown > 0) this.eggCannonCooldown--;
			if (this.flyCooldown > 0) this.flyCooldown--;
			if (this.eggDropOffCooldown > 0) this.eggDropOffCooldown--;

			if (this.random.nextFloat() < 0.005F && this.idleDelay <= 0 && this.isOnGround() && !this.isFiringCannon() && this.isNoEndimationPlaying()) {
				NetworkUtil.setPlayingAnimationMessage(this, this.random.nextFloat() < 0.6F && !this.isFlying() ? FLAP : MUNCH);
				this.resetIdleFlapDelay();
			}

			BroodEggSackEntity eggSackEntity = this.getEggSack(world);
			if (eggSackEntity != null && eggSackEntity.isAlive()) {
				eggSackEntity.xo = eggSackEntity.xOld = eggSackEntity.getX();
				eggSackEntity.yo = eggSackEntity.yOld = eggSackEntity.getY();
				eggSackEntity.zo = eggSackEntity.zOld = eggSackEntity.getZ();
				eggSackEntity.updatePosition(this);
			} else {
				eggSackEntity = new BroodEggSackEntity(world);
				eggSackEntity.setBroodID(this.getId());
				eggSackEntity.updatePosition(this);
				world.addFreshEntity(eggSackEntity);
				this.setEggSackID(eggSackEntity.getId());
			}

			if (this.isFlying()) {
				this.ticksFlying++;

				if (this.isEndimationPlaying(AIR_SLAM) && this.getAnimationTick() == 5 && (this.onGround || !this.level.noCollision(DetectionHelper.checkOnGround(this.getBoundingBox(), 0.25F)))) {
					BroodEetleSlamGoal.slam(this, this.random, 1.0F);
				}
			} else {
				this.ticksFlying = 0;
			}

			if (this.level.getGameTime() % 5 == 0) {
				BlockPos takeoffPos = this.takeoffPos;
				if (takeoffPos != null && this.position().distanceToSqr(Vector3d.atLowerCornerOf(takeoffPos)) > 256.0F) {
					this.takeoffPos = null;
				}
			}

			float percentage = this.getHealth() / this.getMaxHealth();
			this.bossInfo.setPercent(percentage);
			if (percentage != this.prevHealthPercentage) {
				HealthStage newStage = HealthStage.getStage(percentage);
				if (newStage.eggGrowthChance > 0.0F && newStage.ordinal() > this.getHealthStage().ordinal()) {
					newStage.awakeNearbyEggs(this);
				}
				this.setHealthStage(newStage);
			}
			this.prevHealthPercentage = percentage;
		} else {
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

			this.wingFlapSpeed += this.isMoving() ? (0.5F - this.wingFlapSpeed) * 0.1F : (0.125F - this.wingFlapSpeed) * 0.2F;
			this.prevWingFlap = this.wingFlap;
			this.wingFlap += this.wingFlapSpeed;
		}

		ControlledEndimation eggCannonEndimation = this.eggCannonEndimation;
		eggCannonEndimation.setDecrementing(!this.isFiringCannon() && !(this.isEndimationPlaying(DEATH) && this.getAnimationTick() >= 15));
		eggCannonEndimation.update();
		eggCannonEndimation.tick();

		ControlledEndimation eggMouthEndimation = this.eggMouthEndimation;
		eggMouthEndimation.setDecrementing(!eggCannonEndimation.isAtMax() && this.isNotDroppingEggs());
		eggMouthEndimation.update();
		eggMouthEndimation.tick();

		ControlledEndimation eggCannonFlyingEndimation = this.eggCannonFlyingEndimation;
		eggCannonFlyingEndimation.setDecrementing(!this.isFlying());
		eggCannonFlyingEndimation.update();
		eggCannonFlyingEndimation.tick();

		ControlledEndimation flying = this.flyingEndimation;
		flying.setDecrementing(!this.isMoving());
		flying.update();
		flying.tick();

		ControlledEndimation sleepingEndimation = this.sleepingEndimation;
		sleepingEndimation.setDecrementing(!this.isSleeping());
		sleepingEndimation.update();
		sleepingEndimation.tick();

		ControlledEndimation healPulseEndimation = this.healPulseEndimation;
		if (healPulseEndimation.isAtMax()) {
			healPulseEndimation.setDecrementing(true);
		}
		healPulseEndimation.update();
		healPulseEndimation.tick();

		this.flyingRotations.tick(this.getTargetFlyingRotations());
	}

	@Override
	public void travel(Vector3d travelVector) {
		if (this.isEffectiveAi() && !this.isBaby() && this.isFlying()) {
			this.moveRelative(0.1F, travelVector);
			this.move(MoverType.SELF, this.getDeltaMovement());
			this.setDeltaMovement(this.getDeltaMovement().scale(0.8F));
			this.setDeltaMovement(this.getDeltaMovement().subtract(0, 0.01D, 0));
		} else {
			super.travel(travelVector);
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundNBT compound) {
		super.addAdditionalSaveData(compound);
		compound.putBoolean("IsSleeping", this.isSleeping());
		if (this.takeoffPos != null) {
			compound.put("TakeoffPos", NBTUtil.writeBlockPos(this.takeoffPos));
		}
		compound.putInt("EggCannonCooldown", this.eggCannonCooldown);
		compound.putInt("FlyCooldown", this.flyCooldown);
		compound.putInt("EggDropOffCooldown", this.eggDropOffCooldown);
		compound.putBoolean("IsFlying", this.isFlying());
	}

	@Override
	public void readAdditionalSaveData(CompoundNBT compound) {
		super.readAdditionalSaveData(compound);
		if (this.hasCustomName()) {
			this.bossInfo.setName(this.getDisplayName());
		}
		this.setSleeping(compound.getBoolean("IsSleeping"));
		if (compound.contains("TakeoffPos", Constants.NBT.TAG_COMPOUND)) {
			this.takeoffPos = NBTUtil.readBlockPos(compound.getCompound("TakeoffPos"));
		}
		this.eggCannonCooldown = compound.getInt("EggCannonCooldown");
		this.flyCooldown = compound.getInt("FlyCooldown");
		this.eggDropOffCooldown = compound.getInt("EggDropOffCooldown");
		this.setFlying(compound.getBoolean("IsFlying"));
		float progress = this.getHealth() / this.getMaxHealth();
		this.prevHealthPercentage = progress;
		this.setHealthStage(HealthStage.getStage(progress));
	}

	@Override
	protected void doPush(Entity collider) {
		if (!this.isPassengerOfSameVehicle(collider)) {
			if (!collider.noPhysics && !this.noPhysics) {
				double d0 = collider.getX() - this.getX();
				double d1 = collider.getZ() - this.getZ();
				double d2 = MathHelper.absMax(d0, d1);
				if (d2 >= 0.01D) {
					d2 = MathHelper.sqrt(d2);
					d0 = d0 / d2;
					d1 = d1 / d2;
					double d3 = 1.0D / d2;
					if (d3 > 1.0D) {
						d3 = 1.0D;
					}

					d0 = d0 * d3;
					d1 = d1 * d3;
					d0 = d0 * 0.05D;
					d1 = d1 * 0.05D;
					d0 = d0 * (1.0D - this.pushthrough);
					d1 = d1 * (1.0D - this.pushthrough);
					if (!this.isVehicle()) {
						this.push(-d0 * 0.2F, 0.0D, -d1 * 0.2F);
					}

					if (!collider.isVehicle()) {
						collider.push(d0 * 2.0F, 0.0D, d1 * 2.0F);
					}
				}
			}
		}
	}

	@Override
	public void checkDespawn() {
		if (this.level.getDifficulty() == Difficulty.PEACEFUL && this.shouldDespawnInPeaceful()) {
			this.remove();
		} else {
			this.noActionTime = 0;
		}
	}

	@Override
	public boolean causeFallDamage(float distance, float damageMultiplier) {
		return false;
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	@Override
	public boolean isSensitiveToWater() {
		return true;
	}

	@Override
	public boolean canChangeDimensions() {
		return false;
	}

	@Override
	public CreatureAttribute getMobType() {
		return CreatureAttribute.ARTHROPOD;
	}

	@Override
	public boolean doHurtTarget(Entity target) {
		if (!(target instanceof LivingEntity)) {
			return false;
		} else {
			if (!this.level.isClientSide) {
				NetworkUtil.setPlayingAnimationMessage(this, ATTACK);
			}
			float attackDamage = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
			float damage;
			if ((int) attackDamage > 0.0F) {
				damage = attackDamage / 2.0F + this.random.nextInt((int) attackDamage);
			} else {
				damage = attackDamage;
			}

			boolean attacked = target.hurt(DamageSource.mobAttack(this), damage);
			if (attacked) {
				this.doEnchantDamageEffects(this, target);
				this.blockedByShield((LivingEntity) target);
			}
			return attacked;
		}
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (source.getDirectEntity() instanceof AbstractArrowEntity) {
			return false;
		}
		return super.hurt(source, amount * (source.isProjectile() ? 0.05F : 0.1F));
	}

	public boolean attackEntityFromEggSack(DamageSource source, float amount) {
		if (super.hurt(source, amount)) {
			this.playSound(SoundEvents.WET_GRASS_PLACE, 1.0F, 0.6F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
			return true;
		}
		return false;
	}

	@Override
	protected void tickDeath() {
	}

	@Override
	protected void blockedByShield(LivingEntity target) {
		double knockbackForce = this.getAttributeValue(Attributes.ATTACK_KNOCKBACK) - target.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
		if (knockbackForce > 0.0D) {
			Random random = this.level.random;
			double scale = knockbackForce * (random.nextFloat() * 0.5F + 0.5F);
			Vector3d horizontalVelocity = new Vector3d(target.getX() - this.getX(), 0.0D, target.getZ() - this.getZ()).normalize().scale(scale);
			target.push(horizontalVelocity.x, knockbackForce * 0.5F * random.nextFloat() * 0.5F, horizontalVelocity.z);
			target.hurtMarked = true;
		}
	}

	@Override
	public void setCustomName(@Nullable ITextComponent name) {
		super.setCustomName(name);
		this.bossInfo.setName(this.getDisplayName());
	}

	@Override
	public void startSeenByPlayer(ServerPlayerEntity player) {
		if (this.isSleeping()) {
			this.trackedPlayers.add(player);
		} else {
			this.bossInfo.addPlayer(player);
		}
	}

	@Override
	public void stopSeenByPlayer(ServerPlayerEntity player) {
		if (this.isSleeping()) {
			this.trackedPlayers.remove(player);
		} else {
			this.bossInfo.removePlayer(player);
		}
	}

	@Override
	public EntitySize getDimensions(Pose pose) {
		return this.isFlying() ? FLYING_SIZE : this.isOnLastHealthStage() ? FINAL_STAGE_SIZE : super.getDimensions(pose);
	}

	@Override
	public void handleEntityEvent(byte id) {
		if (id == 60) {
			this.healPulseEndimation.setDecrementing(false);
		} else {
			super.handleEntityEvent(id);
		}
	}

	public void setFiringCannon(boolean firingCannon) {
		this.entityData.set(FIRING_CANNON, firingCannon);
	}

	public boolean isFiringCannon() {
		return this.entityData.get(FIRING_CANNON);
	}

	public float getEggCannonProgress() {
		return this.eggCannonEndimation.getAnimationProgress();
	}

	public float getEggCannonProgressServer() {
		return this.eggCannonEndimation.getAnimationProgressServer();
	}

	public float getEggMouthProgress() {
		return this.eggMouthEndimation.getAnimationProgress();
	}

	public boolean isEggMouthOpen() {
		return this.eggMouthEndimation.isAtMax();
	}

	public float getTakeoffProgress() {
		return this.takeoffEndimation.getAnimationProgress();
	}

	public boolean isEggCannonFlyingAtMax() {
		return this.eggCannonFlyingEndimation.isAtMax();
	}

	public float getEggCannonFlyingProgress() {
		return this.eggCannonFlyingEndimation.getAnimationProgress();
	}

	public float getEggCannonFlyingProgressServer() {
		return this.eggCannonFlyingEndimation.getAnimationProgressServer();
	}

	public boolean hasWokenUp() {
		return this.sleepingEndimation.getTick() == 0;
	}

	public boolean shouldSlamWhenWakingUp() {
		return this.sleepingEndimation.getTick() == 2 && this.wokenUpAggressively;
	}

	public float getSleepingProgress() {
		return MathHelper.sin(1.5708F * this.sleepingEndimation.getAnimationProgress());
	}

	public float getFlyingProgress() {
		return this.flyingEndimation.getAnimationProgress();
	}

	public float getHealPulseProgress() {
		return this.healPulseEndimation.getAnimationProgress();
	}

	public void resetIdleFlapDelay() {
		this.idleDelay = this.random.nextInt(41) + 25;
	}

	public void resetSlamCooldown() {
		this.slamCooldown = this.random.nextInt(21) + 70;
	}

	public void resetEggCannonCooldown() {
		this.eggCannonCooldown = this.random.nextInt(201) + 1200;
	}

	public boolean canSlam() {
		return this.slamCooldown <= 0;
	}

	public boolean canFireEggCannon() {
		return this.eggCannonCooldown <= 0;
	}

	public int getEggCannonCooldown() {
		return this.eggCannonCooldown;
	}

	public int getTicksFlying() {
		return this.ticksFlying;
	}

	public void resetFlyCooldown() {
		this.flyCooldown = this.random.nextInt(301) + 500;
	}

	public boolean canFly() {
		return this.flyCooldown <= 0;
	}

	public void resetEggDropOffCooldown() {
		this.eggDropOffCooldown = this.random.nextInt(201) + 800;
	}

	public boolean canDropOffEggs() {
		return this.eggDropOffCooldown <= 0;
	}

	private void setHealthStage(HealthStage stage) {
		this.entityData.set(HEALTH_STAGE, stage);
	}

	public HealthStage getHealthStage() {
		return this.entityData.get(HEALTH_STAGE);
	}

	public boolean isOnLastHealthStage() {
		return this.getHealthStage() == HealthStage.FIVE;
	}

	public void setFlying(boolean flying) {
		this.entityData.set(FLYING, flying);
		if (!flying) {
			this.setMoving(false);
		}
	}

	public boolean isFlying() {
		return this.entityData.get(FLYING);
	}

	public void setDroppingEggs(boolean droppingEggs) {
		this.entityData.set(DROPPING_EGGS, droppingEggs);
	}

	public boolean isNotDroppingEggs() {
		return !this.entityData.get(DROPPING_EGGS);
	}

	@Override
	public void setMoving(boolean moving) {
		this.entityData.set(MOVING, moving);
	}

	public boolean isMoving() {
		return this.entityData.get(MOVING);
	}

	public void setSleeping(boolean sleeping) {
		this.entityData.set(SLEEPING, sleeping);
	}

	@Override
	public boolean isSleeping() {
		return this.entityData.get(SLEEPING);
	}

	@Override
	public void setTargetFlyingRotations(TargetFlyingRotations flyingRotations) {
		this.entityData.set(TARGET_FLYING_ROTATIONS, flyingRotations);
	}

	private TargetFlyingRotations getTargetFlyingRotations() {
		return this.entityData.get(TARGET_FLYING_ROTATIONS);
	}

	public void setEggSackID(int id) {
		this.entityData.set(EGG_SACK_ID, Math.max(-1, id));
	}

	private int getEggSackID() {
		return this.entityData.get(EGG_SACK_ID);
	}

	@Nullable
	public BroodEggSackEntity getEggSack(World world) {
		int eggSackID = this.getEggSackID();
		if (eggSackID >= 0) {
			Entity entity = world.getEntity(eggSackID);
			if (entity instanceof BroodEggSackEntity) {
				return (BroodEggSackEntity) entity;
			}
		}
		return null;
	}

	public FlyingRotations getFlyingRotations() {
		return this.flyingRotations;
	}

	public void addRevengeTarget(LivingEntity target) {
		this.revengeTargets.add(target);
	}

	public boolean isAnAggressor(LivingEntity entity) {
		return this.revengeTargets.contains(entity);
	}

	public float getWingFlap() {
		return MathHelper.lerp(ClientInfo.getPartialTicks(), this.prevWingFlap, this.wingFlap);
	}

	@Override
	public Endimation[] getEndimations() {
		return new Endimation[] {
				FLAP, MUNCH, ATTACK, SLAM, LAUNCH, AIR_CHARGE, AIR_SLAM, DEATH
		};
	}

	@Override
	public Endimation getPlayingEndimation() {
		return this.endimation;
	}

	@Override
	public int getAnimationTick() {
		return this.animationTick;
	}

	@Override
	public void setAnimationTick(int animationTick) {
		this.animationTick = animationTick;
	}

	@Override
	public void setPlayingEndimation(Endimation endimation) {
		this.onEndimationEnd(this.endimation);
		this.endimation = endimation;
		this.setAnimationTick(0);
	}

	@Override
	public void onEndimationStart(Endimation endimation) {
		if (endimation == DEATH) {
			this.deathTime = 0;
			this.setFlying(false);
			this.setDroppingEggs(false);
			this.setFiringCannon(false);
		} else if (endimation == LAUNCH) {
			World world = this.level;
			if (world instanceof ServerWorld) {
				Vector3d eggSackPos = BroodEggSackEntity.getEggPos(this.position(), this.yBodyRot, this.getEggCannonProgressServer(), this.getEggCannonFlyingProgressServer(), this.getFlyingRotations().getFlyPitch(), this.isOnLastHealthStage());
				((ServerWorld) world).sendParticles(new CorrockCrownParticleData(EEParticles.END_CROWN.get(), true), eggSackPos.x(), eggSackPos.y() + (this.isFlying() ? 0.0F : 1.0F), eggSackPos.z(), 20, 0.3125F, 0.3125F, 0.3125F, 0.15D);
			}
		}
	}

	//The Brood Eetle's size changes greatly in size, causing too much motion when landing. To fix this, resizing motion is removed.
	@Override
	public void refreshDimensions() {
		try {
			EntitySize currentSize = (EntitySize) SIZE_FIELD.get(this);
			Pose pose = this.getPose();
			EntitySize newSize = this.getDimensions(pose);
			EntityEvent.Size sizeEvent = ForgeEventFactory.getEntitySizeForge(this, pose, currentSize, newSize, this.getEyeHeight(pose, newSize));
			newSize = sizeEvent.getNewSize();
			SIZE_FIELD.set(this, newSize);
			EYE_HEIGHT_FIELD.set(this, sizeEvent.getNewEyeHeight());
			if (newSize.width < currentSize.width) {
				double d0 = newSize.width / 2.0D;
				this.setBoundingBox(new AxisAlignedBB(this.getX() - d0, this.getY(), this.getZ() - d0, this.getX() + d0, this.getY() + newSize.height, this.getZ() + d0));
			} else {
				AxisAlignedBB axisalignedbb = this.getBoundingBox();
				this.setBoundingBox(new AxisAlignedBB(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ, axisalignedbb.minX + newSize.width, axisalignedbb.minY + newSize.height, axisalignedbb.minZ + newSize.width));
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public enum HeadTiltDirection {
		LEFT(-0.17F),
		RIGHT(0.17F);

		public final float angle;

		HeadTiltDirection(float angle) {
			this.angle = angle;
		}
	}

	public enum HealthStage {
		ZERO(1.0F, 0.0F),
		ONE(5.0F / 6.0F, 0.1F),
		TWO(2.0F / 3.0F, 0.1F),
		THREE(0.5F, 0.2F),
		FOUR(1.0F / 3.0F, 0.3F),
		FIVE(1.0F / 6.0F, 0.0F);

		private static final HealthStage[] VALUES = {FIVE, FOUR, THREE, TWO, ONE, ZERO};
		private final float percentage;
		private final float eggGrowthChance;

		HealthStage(float percentage, float eggGrowthChance) {
			this.percentage = percentage;
			this.eggGrowthChance = eggGrowthChance;
		}

		private void awakeNearbyEggs(BroodEetleEntity broodEetle) {
			BlockPos.Mutable mutable = broodEetle.blockPosition().mutable();
			int originX = mutable.getX();
			int originY = mutable.getY();
			int originZ = mutable.getZ();
			World world = broodEetle.level;
			Random random = broodEetle.random;
			for (int x = -10; x <= 10; x++) {
				for (int y = -6; y <= 14; y++) {
					for (int z = -10; z <= 10; z++) {
						mutable.set(originX + x, originY + y, originZ + z);
						TileEntity tileEntity = world.getBlockEntity(mutable);
						if (tileEntity instanceof EetleEggTileEntity && random.nextFloat() < this.eggGrowthChance) {
							EetleEggTileEntity eetleEggs = (EetleEggTileEntity) tileEntity;
							if (eetleEggs.getHatchDelay() < -60) {
								eetleEggs.bypassSpawningGameRule();
								eetleEggs.fromBroodEetle = true;
								eetleEggs.updateHatchDelay(world, -60 - random.nextInt(10));
							}
						}
					}
				}
			}
		}

		private static HealthStage getStage(float progress) {
			for (HealthStage stage : VALUES) {
				if (progress <= stage.percentage) {
					return stage;
				}
			}
			return ZERO;
		}
	}
}
