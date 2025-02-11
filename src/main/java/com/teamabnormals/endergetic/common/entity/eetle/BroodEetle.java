package com.teamabnormals.endergetic.common.entity.eetle;

import com.teamabnormals.blueprint.client.ClientInfo;
import com.teamabnormals.blueprint.core.endimator.Endimatable;
import com.teamabnormals.blueprint.core.endimator.PlayableEndimation;
import com.teamabnormals.blueprint.core.endimator.TimedEndimation;
import com.teamabnormals.blueprint.core.util.NetworkUtil;
import com.teamabnormals.endergetic.api.entity.pathfinding.EndergeticFlyingPathNavigator;
import com.teamabnormals.endergetic.api.entity.util.DetectionHelper;
import com.teamabnormals.endergetic.client.particle.data.CorrockCrownParticleData;
import com.teamabnormals.endergetic.common.block.entity.EetleEggTileEntity;
import com.teamabnormals.endergetic.common.entity.eetle.ai.brood.*;
import com.teamabnormals.endergetic.common.entity.eetle.flying.FlyingEetleMoveController;
import com.teamabnormals.endergetic.common.entity.eetle.flying.FlyingRotations;
import com.teamabnormals.endergetic.common.entity.eetle.flying.IFlyingEetle;
import com.teamabnormals.endergetic.common.entity.eetle.flying.TargetFlyingRotations;
import com.teamabnormals.endergetic.core.registry.EEBlocks;
import com.teamabnormals.endergetic.core.registry.EEParticleTypes;
import com.teamabnormals.endergetic.core.other.EEDataSerializers;
import com.teamabnormals.endergetic.core.other.EEPlayableEndimations;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class BroodEetle extends Monster implements Endimatable, IFlyingEetle {
	private static final Field SIZE_FIELD = ObfuscationReflectionHelper.findField(Entity.class, "f_19815_");
	private static final Field EYE_HEIGHT_FIELD = ObfuscationReflectionHelper.findField(Entity.class, "f_19816_");
	private static final EntityDataAccessor<Boolean> FIRING_CANNON = SynchedEntityData.defineId(BroodEetle.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> FLYING = SynchedEntityData.defineId(BroodEetle.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> MOVING = SynchedEntityData.defineId(BroodEetle.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> DROPPING_EGGS = SynchedEntityData.defineId(BroodEetle.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> SLEEPING = SynchedEntityData.defineId(BroodEetle.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<TargetFlyingRotations> TARGET_FLYING_ROTATIONS = SynchedEntityData.defineId(BroodEetle.class, EEDataSerializers.TARGET_FLYING_ROTATIONS);
	private static final EntityDataAccessor<Integer> EGG_SACK_ID = SynchedEntityData.defineId(BroodEetle.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<HealthStage> HEALTH_STAGE = SynchedEntityData.defineId(BroodEetle.class, EEDataSerializers.BROOD_HEALTH_STAGE);
	private static final EntityDimensions FLYING_SIZE = EntityDimensions.fixed(1.875F, 2.125F);
	private static final EntityDimensions FINAL_STAGE_SIZE = EntityDimensions.fixed(2.1875F, 2.125F);
	private final TimedEndimation eggCannonEndimation = new TimedEndimation(20, 0);
	private final TimedEndimation eggMouthEndimation = new TimedEndimation(15, 0);
	private final TimedEndimation takeoffEndimation = new TimedEndimation(15, 0);
	private final TimedEndimation eggCannonFlyingEndimation = new TimedEndimation(20, 0);
	private final TimedEndimation flyingEndimation = new TimedEndimation(20, 0);
	private final TimedEndimation sleepingEndimation = new TimedEndimation(20, 0);
	private final TimedEndimation healPulseEndimation = new TimedEndimation(10, 0);
	private final ServerBossEvent bossInfo = new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.NOTCHED_6);
	private final Set<ServerPlayer> trackedPlayers = new HashSet<>();
	private final FlyingRotations flyingRotations = new FlyingRotations();
	private final Set<LivingEntity> revengeTargets = new HashSet<>();
	@Nullable
	public BlockPos takeoffPos;
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

	public BroodEetle(EntityType<? extends BroodEetle> type, Level world) {
		super(type, world);
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
		this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
		this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, AbstractEetle.class, 8.0F));
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
	public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
		super.onSyncedDataUpdated(key);
		if (FLYING.equals(key)) {
			if (this.isFlying()) {
				this.moveControl = new FlyingEetleMoveController<>(this, 10.0F, 30.0F);
				this.navigation = new EndergeticFlyingPathNavigator(this, this.level());
			} else {
				this.moveControl = new MoveControl(this);
				this.navigation = this.createNavigation(this.level());
			}
			this.resetIdleFlapDelay();
			this.refreshDimensions();
			BroodEggSack eggSackEntity = this.getEggSack(this.level());
			if (eggSackEntity != null) {
				eggSackEntity.refreshDimensions();
			}
		} else if (TARGET_FLYING_ROTATIONS.equals(key)) {
			this.flyingRotations.setLooking(true);
		} else if (SLEEPING.equals(key) && !this.isSleeping()) {
			for (ServerPlayer playerEntity : this.trackedPlayers) {
				this.bossInfo.addPlayer(playerEntity);
			}
		} else if (HEALTH_STAGE.equals(key)) {
			this.refreshDimensions();
			BroodEggSack eggSackEntity = this.getEggSack(this.level());
			if (eggSackEntity != null) {
				eggSackEntity.refreshDimensions();
			}
		}
	}

	//TODO: Possibly tweak these values
	public static AttributeSupplier.Builder registerAttributes() {
		return Mob.createMobAttributes()
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

		Level world = this.level();
		if (this.isDeadOrDying()) {
			if (!this.isEndimationPlaying(EEPlayableEndimations.BROOD_EETLE_DEATH_LEFT) && !this.isEndimationPlaying(EEPlayableEndimations.BROOD_EETLE_DEATH_RIGHT) && !world.isClientSide) {
				NetworkUtil.setPlayingAnimation(this, this.random.nextBoolean() ? EEPlayableEndimations.BROOD_EETLE_DEATH_LEFT : EEPlayableEndimations.BROOD_EETLE_DEATH_RIGHT);
			}
			if (++this.deathTime >= 105) {
				if (!world.isClientSide) {
					ItemEntity elytra = this.spawnAtLocation(Items.ELYTRA);
					if (elytra != null) {
						elytra.setExtendedLifetime();
					}
					this.discard();
					if (world instanceof ServerLevel) {
						Vec3 eggSackPos = BroodEggSack.getEggPos(this.position(), this.yBodyRot, this.getEggCannonProgressServer(), this.getEggCannonFlyingProgressServer(), this.getFlyingRotations().getFlyPitch(), this.isOnLastHealthStage());
						((ServerLevel) world).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, EEBlocks.EETLE_EGG.get().defaultBlockState()), eggSackPos.x(), eggSackPos.y() + 0.83F, eggSackPos.z(), 20, 0.3125F, 0.3125F, 0.3125F, 0.2D);
						((ServerLevel) world).sendParticles(new CorrockCrownParticleData(EEParticleTypes.END_CROWN.get(), true), eggSackPos.x(), eggSackPos.y() + 0.83F, eggSackPos.z(), 30, 0.3125F, 0.3125F, 0.3125F, 0.2D);
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

			if (this.random.nextFloat() < 0.005F && this.idleDelay <= 0 && this.onGround() && !this.isFiringCannon() && this.isNoEndimationPlaying()) {
				NetworkUtil.setPlayingAnimation(this, this.random.nextFloat() < 0.6F && !this.isFlying() ? EEPlayableEndimations.BROOD_EETLE_FLAP : EEPlayableEndimations.BROOD_EETLE_MUNCH);
				this.resetIdleFlapDelay();
			}

			BroodEggSack eggSackEntity = this.getEggSack(world);
			if (eggSackEntity != null && eggSackEntity.isAlive()) {
				eggSackEntity.xo = eggSackEntity.xOld = eggSackEntity.getX();
				eggSackEntity.yo = eggSackEntity.yOld = eggSackEntity.getY();
				eggSackEntity.zo = eggSackEntity.zOld = eggSackEntity.getZ();
				eggSackEntity.updatePosition(this);
			} else {
				eggSackEntity = new BroodEggSack(world);
				eggSackEntity.setBroodID(this.getId());
				eggSackEntity.updatePosition(this);
				world.addFreshEntity(eggSackEntity);
				this.setEggSackID(eggSackEntity.getId());
			}

			if (this.isFlying()) {
				this.ticksFlying++;

				if (this.isEndimationPlaying(EEPlayableEndimations.BROOD_EETLE_AIR_SLAM) && this.getAnimationTick() == 5 && (this.onGround() || !this.level().noCollision(DetectionHelper.checkOnGround(this.getBoundingBox(), 0.25F)))) {
					BroodEetleSlamGoal.slam(this, this.random, 1.0F);
				}
			} else {
				this.ticksFlying = 0;
			}

			if (this.level().getGameTime() % 5 == 0) {
				BlockPos takeoffPos = this.takeoffPos;
				if (takeoffPos != null && this.position().distanceToSqr(Vec3.atLowerCornerOf(takeoffPos)) > 256.0F) {
					this.takeoffPos = null;
				}
			}

			float percentage = this.getHealth() / this.getMaxHealth();
			this.bossInfo.setProgress(percentage);
			if (percentage != this.prevHealthPercentage) {
				HealthStage newStage = HealthStage.getStage(percentage);
				if (newStage.eggGrowthChance > 0.0F && newStage.ordinal() > this.getHealthStage().ordinal()) {
					newStage.awakeNearbyEggs(this);
				}
				this.setHealthStage(newStage);
			}
			this.prevHealthPercentage = percentage;
		} else {
			TimedEndimation takeoff = this.takeoffEndimation;
			takeoff.tick();
			if (takeoff.isMaxed()) {
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

		TimedEndimation eggCannonEndimation = this.eggCannonEndimation;
		eggCannonEndimation.setDecrementing(!this.isFiringCannon() && !((this.isEndimationPlaying(EEPlayableEndimations.BROOD_EETLE_DEATH_LEFT) || this.isEndimationPlaying(EEPlayableEndimations.BROOD_EETLE_DEATH_RIGHT)) && this.getAnimationTick() >= 15));
		eggCannonEndimation.tick();

		TimedEndimation eggMouthEndimation = this.eggMouthEndimation;
		eggMouthEndimation.setDecrementing(!eggCannonEndimation.isMaxed() && this.isNotDroppingEggs());
		eggMouthEndimation.tick();

		TimedEndimation eggCannonFlyingEndimation = this.eggCannonFlyingEndimation;
		eggCannonFlyingEndimation.setDecrementing(!this.isFlying());
		eggCannonFlyingEndimation.tick();

		TimedEndimation flying = this.flyingEndimation;
		flying.setDecrementing(!this.isMoving());
		flying.tick();

		TimedEndimation sleepingEndimation = this.sleepingEndimation;
		sleepingEndimation.setDecrementing(!this.isSleeping());
		sleepingEndimation.tick();

		TimedEndimation healPulseEndimation = this.healPulseEndimation;
		if (healPulseEndimation.isMaxed()) {
			healPulseEndimation.setDecrementing(true);
		}
		healPulseEndimation.tick();

		this.flyingRotations.tick(this.getTargetFlyingRotations());
	}

	@Override
	public void travel(Vec3 travelVector) {
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
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putBoolean("IsSleeping", this.isSleeping());
		if (this.takeoffPos != null) {
			compound.put("TakeoffPos", NbtUtils.writeBlockPos(this.takeoffPos));
		}
		compound.putInt("EggCannonCooldown", this.eggCannonCooldown);
		compound.putInt("FlyCooldown", this.flyCooldown);
		compound.putInt("EggDropOffCooldown", this.eggDropOffCooldown);
		compound.putBoolean("IsFlying", this.isFlying());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		if (this.hasCustomName()) {
			this.bossInfo.setName(this.getDisplayName());
		}
		this.setSleeping(compound.getBoolean("IsSleeping"));
		if (compound.contains("TakeoffPos", 10)) {
			this.takeoffPos = NbtUtils.readBlockPos(compound.getCompound("TakeoffPos"));
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
				double d2 = Mth.absMax(d0, d1);
				if (d2 >= 0.01D) {
					d2 = Mth.sqrt((float) d2);
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
					if (!this.isVehicle()) {
						this.push(-d0 * 0.05F, 0.0D, -d1 * 0.05F);
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
		if (this.level().getDifficulty() == Difficulty.PEACEFUL && this.shouldDespawnInPeaceful()) {
			this.discard();
		} else {
			this.noActionTime = 0;
		}
	}

	@Override
	public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
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
	public MobType getMobType() {
		return MobType.ARTHROPOD;
	}

	@Override
	public boolean doHurtTarget(Entity target) {
		if (!(target instanceof LivingEntity)) {
			return false;
		} else {
			if (!this.level().isClientSide) {
				NetworkUtil.setPlayingAnimation(this, EEPlayableEndimations.BROOD_EETLE_ATTACK);
			}
			float attackDamage = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
			float damage;
			if ((int) attackDamage > 0.0F) {
				damage = attackDamage / 2.0F + this.random.nextInt((int) attackDamage);
			} else {
				damage = attackDamage;
			}

			boolean attacked = target.hurt(this.damageSources().mobAttack(this), damage);
			if (attacked) {
				this.doEnchantDamageEffects(this, target);
				this.blockedByShield((LivingEntity) target);
			}
			return attacked;
		}
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (source.getDirectEntity() instanceof AbstractArrow) {
			return false;
		}
		// TODO: Make sure this works properly, perhaps a magic damage tag in blueprint
		return super.hurt(source, source.is(DamageTypeTags.WITCH_RESISTANT_TO) && source.is(DamageTypeTags.BYPASSES_ARMOR) ? amount : amount * (source.is(DamageTypeTags.IS_PROJECTILE) ? 0.05F : 0.1F));
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
			RandomSource random = this.level().random;
			double scale = knockbackForce * (random.nextFloat() * 0.5F + 0.5F);
			Vec3 horizontalVelocity = new Vec3(target.getX() - this.getX(), 0.0D, target.getZ() - this.getZ()).normalize().scale(scale);
			target.push(horizontalVelocity.x, knockbackForce * 0.5F * random.nextFloat() * 0.5F, horizontalVelocity.z);
			target.hurtMarked = true;
		}
	}

	@Override
	public void setCustomName(@Nullable Component name) {
		super.setCustomName(name);
		this.bossInfo.setName(this.getDisplayName());
	}

	@Override
	public void startSeenByPlayer(ServerPlayer player) {
		if (this.isSleeping()) {
			this.trackedPlayers.add(player);
		} else {
			this.bossInfo.addPlayer(player);
		}
	}

	@Override
	public void stopSeenByPlayer(ServerPlayer player) {
		if (this.isSleeping()) {
			this.trackedPlayers.remove(player);
		} else {
			this.bossInfo.removePlayer(player);
		}
	}

	@Override
	public EntityDimensions getDimensions(Pose pose) {
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
		return this.eggCannonEndimation.getProgress(ClientInfo.getPartialTicks());
	}

	public float getEggCannonProgressServer() {
		return this.eggCannonEndimation.getServerProgress();
	}

	public float getEggMouthProgress() {
		return this.eggMouthEndimation.getProgress(ClientInfo.getPartialTicks());
	}

	public boolean isEggMouthOpen() {
		return this.eggMouthEndimation.isMaxed();
	}

	public float getTakeoffProgress() {
		return this.takeoffEndimation.getProgress(ClientInfo.getPartialTicks());
	}

	public boolean isEggCannonFlyingAtMax() {
		return this.eggCannonFlyingEndimation.isMaxed();
	}

	public float getEggCannonFlyingProgress() {
		return this.eggCannonFlyingEndimation.getProgress(ClientInfo.getPartialTicks());
	}

	public float getEggCannonFlyingProgressServer() {
		return this.eggCannonFlyingEndimation.getServerProgress();
	}

	public boolean hasWokenUp() {
		return this.sleepingEndimation.getTick() == 0;
	}

	public boolean shouldSlamWhenWakingUp() {
		return this.sleepingEndimation.getTick() == 2 && this.wokenUpAggressively;
	}

	public float getSleepingProgress() {
		return Mth.sin(1.5708F * this.sleepingEndimation.getProgress(ClientInfo.getPartialTicks()));
	}

	public float getFlyingProgress() {
		return this.flyingEndimation.getProgress(ClientInfo.getPartialTicks());
	}

	public float getHealPulseProgress() {
		return this.healPulseEndimation.getProgress(ClientInfo.getPartialTicks());
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
	public BroodEggSack getEggSack(Level world) {
		int eggSackID = this.getEggSackID();
		if (eggSackID >= 0) {
			Entity entity = world.getEntity(eggSackID);
			if (entity instanceof BroodEggSack) {
				return (BroodEggSack) entity;
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
		return Mth.lerp(ClientInfo.getPartialTicks(), this.prevWingFlap, this.wingFlap);
	}

	@Override
	public void onEndimationStart(PlayableEndimation endimation, PlayableEndimation oldEndimation) {
		if (endimation == EEPlayableEndimations.BROOD_EETLE_DEATH_LEFT || endimation == EEPlayableEndimations.BROOD_EETLE_DEATH_RIGHT) {
			this.deathTime = 0;
			this.setFlying(false);
			this.setDroppingEggs(false);
			this.setFiringCannon(false);
		} else if (endimation == EEPlayableEndimations.BROOD_EETLE_LAUNCH || endimation == EEPlayableEndimations.BROOD_EETLE_DROP_EGGS) {
			Level world = this.level();
			if (world instanceof ServerLevel) {
				Vec3 eggSackPos = BroodEggSack.getEggPos(this.position(), this.yBodyRot, this.getEggCannonProgressServer(), this.getEggCannonFlyingProgressServer(), this.getFlyingRotations().getFlyPitch(), this.isOnLastHealthStage());
				((ServerLevel) world).sendParticles(new CorrockCrownParticleData(EEParticleTypes.END_CROWN.get(), true), eggSackPos.x(), eggSackPos.y() + (this.isFlying() ? 0.0F : 1.0F), eggSackPos.z(), 20, 0.3125F, 0.3125F, 0.3125F, 0.15D);
			}
		}
	}

	//The Brood Eetle's size changes greatly in size, causing too much motion when landing. To fix this, resizing motion is removed.
	@Override
	public void refreshDimensions() {
		try {
			EntityDimensions currentSize = (EntityDimensions) SIZE_FIELD.get(this);
			Pose pose = this.getPose();
			EntityDimensions newSize = this.getDimensions(pose);
			EntityEvent.Size sizeEvent = ForgeEventFactory.getEntitySizeForge(this, pose, currentSize, newSize, this.getEyeHeight(pose, newSize));
			newSize = sizeEvent.getNewSize();
			SIZE_FIELD.set(this, newSize);
			EYE_HEIGHT_FIELD.set(this, sizeEvent.getNewEyeHeight());
			if (newSize.width < currentSize.width) {
				double d0 = newSize.width / 2.0D;
				this.setBoundingBox(new AABB(this.getX() - d0, this.getY(), this.getZ() - d0, this.getX() + d0, this.getY() + newSize.height, this.getZ() + d0));
			} else {
				AABB axisalignedbb = this.getBoundingBox();
				this.setBoundingBox(new AABB(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ, axisalignedbb.minX + newSize.width, axisalignedbb.minY + newSize.height, axisalignedbb.minZ + newSize.width));
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

		private void awakeNearbyEggs(BroodEetle broodEetle) {
			BlockPos.MutableBlockPos mutable = broodEetle.blockPosition().mutable();
			int originX = mutable.getX();
			int originY = mutable.getY();
			int originZ = mutable.getZ();
			Level world = broodEetle.level();
			RandomSource random = broodEetle.random;
			for (int x = -10; x <= 10; x++) {
				for (int y = -6; y <= 14; y++) {
					for (int z = -10; z <= 10; z++) {
						mutable.set(originX + x, originY + y, originZ + z);
						BlockEntity tileEntity = world.getBlockEntity(mutable);
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
