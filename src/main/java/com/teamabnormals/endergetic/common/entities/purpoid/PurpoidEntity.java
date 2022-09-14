package com.teamabnormals.endergetic.common.entities.purpoid;

import com.teamabnormals.endergetic.api.entity.pathfinding.EndergeticFlyingPathNavigator;
import com.teamabnormals.endergetic.api.util.TemporaryMathUtil;
import com.teamabnormals.endergetic.client.particles.EEParticles;
import com.teamabnormals.endergetic.client.particles.data.CorrockCrownParticleData;
import com.teamabnormals.endergetic.common.entities.purpoid.ai.*;
import com.teamabnormals.endergetic.core.registry.other.EEDataSerializers;
import com.teamabnormals.endergetic.core.registry.other.EEPlayableEndimations;
import com.teamabnormals.blueprint.core.endimator.Endimatable;
import com.teamabnormals.blueprint.core.endimator.PlayableEndimation;
import com.teamabnormals.blueprint.core.util.MathUtil;
import com.teamabnormals.blueprint.core.util.NetworkUtil;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class PurpoidEntity extends PathfinderMob implements Endimatable {
	private static final EntityDataAccessor<PurpoidSize> SIZE = SynchedEntityData.defineId(PurpoidEntity.class, EEDataSerializers.PURPOID_SIZE);
	private static final EntityDataAccessor<Integer> BOOSTING_TICKS = SynchedEntityData.defineId(PurpoidEntity.class, EntityDataSerializers.INT);
	private final TeleportController teleportController = new TeleportController();
	private int growingAge;
	private int teleportCooldown;
	private int restCooldown;
	private Vec3 prevPull = Vec3.ZERO, pull = Vec3.ZERO;
	@Nullable
	private BlockPos flowerPos;
	private PurpoidTelefragGoal telefragGoal;
	private PurpoidMoveNearTargetGoal moveNearTargetGoal;
	private PurpoidAttackGoal attackGoal;
	private PurpoidRestOnFlowerGoal restOnFlowerGoal;
	private PurpoidTeleportToFlowerGoal teleportToFlowerGoal;

	public PurpoidEntity(EntityType<? extends PathfinderMob> type, Level world) {
		super(type, world);
		this.resetTeleportCooldown();
		this.resetRestCooldown();
		this.moveControl = new PurpoidMoveController(this);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(SIZE, PurpoidSize.NORMAL);
		this.entityData.define(BOOSTING_TICKS, 0);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, this.telefragGoal = new PurpoidTelefragGoal(this));
		this.goalSelector.addGoal(2, this.moveNearTargetGoal = new PurpoidMoveNearTargetGoal(this));
		this.goalSelector.addGoal(2, this.attackGoal = new PurpoidAttackGoal(this));
		this.goalSelector.addGoal(3, this.restOnFlowerGoal = new PurpoidRestOnFlowerGoal(this));
		this.goalSelector.addGoal(4, this.teleportToFlowerGoal = new PurpoidTeleportToFlowerGoal(this));
		this.goalSelector.addGoal(4, new PurpoidRandomTeleportGoal(this));
		this.goalSelector.addGoal(5, new PurpoidMoveRandomGoal(this));
		this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
		if (SIZE.equals(key)) {
			this.refreshDimensions();
		}
		super.onSyncedDataUpdated(key);
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.ATTACK_DAMAGE, 4.0F)
				.add(Attributes.MOVEMENT_SPEED, 0.2F)
				.add(Attributes.MAX_HEALTH, 25.0F)
				.add(Attributes.FOLLOW_RANGE, 32.0F)
				.add(Attributes.KNOCKBACK_RESISTANCE, 0.2F);
	}

	@Override
	public void tick() {
		super.tick();
		Level world = this.level;
		if (world.isClientSide) {
			this.prevPull = this.pull;
			Vec3 pos = this.position();
			this.pull = pos.add(this.pull.subtract(pos).normalize().scale(0.1F));

			if (this.isBoosting() && world.getGameTime() % 4 == 0) {
				double dy = this.pull.y() - pos.y();
				CorrockCrownParticleData particleData = this.createParticleData();
				RandomSource random = this.getRandom();
				for (int i = 0; i < 2; i++) {
					world.addParticle(particleData, this.getRandomX(0.5D), this.getY() + this.getEyeHeight(), this.getRandomZ(0.5D), TemporaryMathUtil.makeNegativeRandomly(random.nextDouble() * 0.05F, random), dy * random.nextDouble(), TemporaryMathUtil.makeNegativeRandomly(random.nextDouble() * 0.05F, random));
				}
			}

			if (!this.isDeadOrDying() && (this.isEndimationPlaying(EEPlayableEndimations.PURPOID_TELEPORT_TO) || this.isEndimationPlaying(EEPlayableEndimations.PURPOID_FAST_TELEPORT_TO) || this.isEndimationPlaying(EEPlayableEndimations.PURPOID_TELEPORT_FROM) || this.isPassenger())) {
				pos = pos.subtract(0.0F, 1.0F, 0.0F);
				this.pull = pos.add(this.pull.subtract(pos).normalize().scale(0.1F));
			}

			int animationTick = this.getAnimationTick();
			if ((this.isEndimationPlaying(EEPlayableEndimations.PURPOID_TELEPORT_TO) || this.isEndimationPlaying(EEPlayableEndimations.PURPOID_FAST_TELEPORT_TO)) && animationTick == 7 || this.isEndimationPlaying(EEPlayableEndimations.PURPOID_TELEPORT_FROM) && animationTick == 4 || this.isEndimationPlaying(EEPlayableEndimations.PURPOID_TELEFRAG) && animationTick == 2) {
				CorrockCrownParticleData particleData = this.createParticleData();
				RandomSource random = this.getRandom();
				for (int i = 0; i < 12; i++) {
					world.addParticle(particleData, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), TemporaryMathUtil.makeNegativeRandomly(random.nextFloat() * 0.25F, random), (random.nextFloat() - random.nextFloat()) * 0.3F + 0.1F, TemporaryMathUtil.makeNegativeRandomly(random.nextFloat() * 0.25F, random));
				}
			}
		} else {
			if (this.isAlive() && this.getSize() != PurpoidSize.PURPAZOID) {
				int age = this.growingAge;
				if (age < 0) {
					this.updateAge(++age);
				} else if (age > 0) {
					this.updateAge(--age);
				}
			}

			if (this.hasTeleportCooldown()) {
				this.teleportCooldown--;
			}
			if (this.hasRestCooldown()) {
				this.restCooldown--;
			}
			if (this.isBoosting()) {
				this.setBoostingTicks(this.getBoostingTicks() - 1);
			} else if (!this.isPassenger() && this.hasTeleportCooldown() && !this.isResting() && this.random.nextFloat() <= 0.001F) {
				this.setBoostingTicks(this.random.nextInt(81) + 80);
			}
			this.teleportController.tick(this);
		}

		if (this.isDeadOrDying()) {
			if (!this.isEndimationPlaying(EEPlayableEndimations.PURPOID_DEATH) && !world.isClientSide) {
				NetworkUtil.setPlayingAnimation(this, EEPlayableEndimations.PURPOID_DEATH);
			}
			if (++this.deathTime >= 10) {
				if (!world.isClientSide) {
					this.discard();
				} else {
					CorrockCrownParticleData particleData = this.createParticleData();
					RandomSource random = this.getRandom();
					for (int i = 0; i < 12; ++i) {
						world.addParticle(particleData, this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D), TemporaryMathUtil.makeNegativeRandomly(random.nextFloat() * 0.25F, random), (random.nextFloat() - random.nextFloat()) * 0.3F + 0.1F, TemporaryMathUtil.makeNegativeRandomly(random.nextFloat() * 0.25F, random));
					}
					for (int i = 0; i < 20; ++i) {
						world.addParticle(ParticleTypes.POOF, this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D), this.random.nextGaussian() * 0.02D, this.random.nextGaussian() * 0.02D, this.random.nextGaussian() * 0.02D);
					}
				}
			}
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt("Size", this.getSize().ordinal());
		compound.putInt("Age", this.growingAge);
		compound.putInt("BoostingTicks", this.getBoostingTicks());
		compound.putInt("TeleportCooldown", this.teleportCooldown);
		compound.putInt("RestCooldown", this.restCooldown);
		if (this.isResting()) {
			compound.put("FlowerPos", NbtUtils.writeBlockPos(this.flowerPos));
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.setSize(PurpoidSize.values()[Mth.clamp(compound.getInt("Size"), 0, 2)], false);
		this.updateAge(compound.getInt("Age"));
		this.setBoostingTicks(Math.max(0, compound.getInt("BoostingTicks")));
		if (compound.contains("TeleportCooldown", 3)) {
			this.teleportCooldown = Math.max(0, compound.getInt("TeleportCooldown"));
		}
		if (compound.contains("RestCooldown", 3)) {
			this.restCooldown = Math.max(0, compound.getInt("RestCooldown"));
		}
		if (compound.contains("FlowerPos", 10)) {
			this.flowerPos = NbtUtils.readBlockPos(compound.getCompound("FlowerPos"));
		}
	}

	public void setSize(PurpoidSize size, boolean updateHealth) {
		this.entityData.set(SIZE, size);
		float scale = size.getScale();
		this.getAttribute(Attributes.MAX_HEALTH).setBaseValue((int) (scale * 25.0F));
		this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue((int) (scale * 4.0F));
		if (updateHealth) {
			this.setHealth(this.getMaxHealth());
		}
		GoalSelector goalSelector = this.goalSelector;
		if (size == PurpoidSize.PURPAZOID) {
			goalSelector.removeGoal(this.moveNearTargetGoal);
			goalSelector.removeGoal(this.attackGoal);
			goalSelector.removeGoal(this.restOnFlowerGoal);
			goalSelector.removeGoal(this.teleportToFlowerGoal);
			goalSelector.removeGoal(this.telefragGoal);
		} else {
			if (size == PurpoidSize.NORMAL) {
				goalSelector.addGoal(1, this.telefragGoal);
			} else {
				goalSelector.removeGoal(this.telefragGoal);
			}
			goalSelector.addGoal(2, this.moveNearTargetGoal);
			goalSelector.addGoal(2, this.attackGoal);
			goalSelector.addGoal(3, this.restOnFlowerGoal);
			goalSelector.addGoal(4, this.teleportToFlowerGoal);
		}
		this.xpReward = (int) (2 * scale);
	}

	public PurpoidSize getSize() {
		return this.entityData.get(SIZE);
	}

	public void setBoostingTicks(int boostingTicks) {
		this.entityData.set(BOOSTING_TICKS, boostingTicks);
	}

	public int getBoostingTicks() {
		return this.entityData.get(BOOSTING_TICKS);
	}

	public void updateAge(int growingAge) {
		int prevAge = this.growingAge;
		this.growingAge = growingAge;
		if (prevAge < 0 && growingAge >= 0 || prevAge >= 0 && growingAge < 0) {
			this.setSize(growingAge < 0 ? PurpoidSize.PURP : PurpoidSize.NORMAL, true);
		}
	}

	public boolean isBoosting() {
		return this.getBoostingTicks() > 0;
	}

	public void resetTeleportCooldown() {
		this.teleportCooldown = this.getRandom().nextInt(2801) + 200;
	}

	public boolean hasTeleportCooldown() {
		return this.teleportCooldown > 0;
	}

	public void resetRestCooldown() {
		this.restCooldown = this.getRandom().nextInt(2001) + 600;
	}

	public boolean hasRestCooldown() {
		return this.restCooldown > 0;
	}

	public void updatePull(Vec3 pos) {
		this.prevPull = this.pull = pos.subtract(0.0F, 1.0F, 0.0F);
	}

	public Vec3 getPull(float partialTicks) {
		return MathUtil.lerp(this.prevPull, this.pull, partialTicks);
	}

	public TeleportController getTeleportController() {
		return this.teleportController;
	}

	public void setFlowerPos(@Nullable BlockPos flowerPos) {
		this.flowerPos = flowerPos;
	}

	@Nullable
	public BlockPos getFlowerPos() {
		return this.flowerPos;
	}

	public boolean isResting() {
		return this.flowerPos != null;
	}

	@Override
	public boolean isBaby() {
		return this.getSize() == PurpoidSize.PURP;
	}

	@Override
	public float getScale() {
		return this.getSize().getScale();
	}

	private CorrockCrownParticleData createParticleData() {
		return new CorrockCrownParticleData(EEParticles.END_CROWN.get(), false, 0.2F * this.getSize().getScale());
	}

	@Override
	public void travel(Vec3 travelVector) {
		if (this.isEffectiveAi()) {
			this.moveRelative(0.1F, travelVector);
			this.move(MoverType.SELF, this.getDeltaMovement());
			this.setDeltaMovement(this.getDeltaMovement().scale(0.8D));
		} else {
			super.travel(travelVector);
		}
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
		if (spawnData == null) {
			spawnData = new AgeableMob.AgeableMobGroupData(true);
		}

		RandomSource random = this.random;
		if (spawnData instanceof AgeableMob.AgeableMobGroupData) {
			AgeableMob.AgeableMobGroupData ageableData = (AgeableMob.AgeableMobGroupData) spawnData;
			if (ageableData.isShouldSpawnBaby() && ageableData.getGroupSize() > 0 && random.nextFloat() <= ageableData.getBabySpawnChance()) {
				this.updateAge(-24000);
			} else if (random.nextFloat() <= 0.005F) {
				this.setSize(PurpoidSize.PURPAZOID, true);
			}
			ageableData.increaseGroupSizeByOne();
		}
		return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnData, dataTag);
	}

	@Override
	public void calculateEntityAnimation(LivingEntity entity, boolean p_233629_2_) {
		super.calculateEntityAnimation(entity, true);
	}

	@Override
	public double getMyRidingOffset() {
		Entity ridingEntity = this.getVehicle();
		if (ridingEntity != null) {
			return ridingEntity.getBoundingBox().maxY - (ridingEntity.getY() + ridingEntity.getPassengersRidingOffset());
		}
		return super.getMyRidingOffset();
	}

	@Override
	public boolean startRiding(Entity entity, boolean force) {
		boolean riding = super.startRiding(entity, force);
		if (entity instanceof ServerPlayer) {
			((ServerPlayer) entity).connection.send(new ClientboundSetPassengersPacket(entity));
		}
		return riding;
	}

	@Override
	public void stopRiding() {
		Entity entity = this.getVehicle();
		super.stopRiding();
		if (entity instanceof ServerPlayer) {
			((ServerPlayer) entity).connection.send(new ClientboundSetPassengersPacket(entity));
		}
	}

	@Override
	protected InteractionResult mobInteract(Player player, InteractionHand hand) {
		if (player.getMainHandItem().isEmpty() && !this.isPassenger() && this.getSize() == PurpoidSize.NORMAL && this.isAlive()) {
			this.startRiding(player);
			this.setTarget(player);
			return InteractionResult.SUCCESS;
		}
		return super.mobInteract(player, hand);
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (!this.level.isClientSide) {
			Entity ridingEntity = this.getVehicle();
			if (ridingEntity != null && source.getEntity() == ridingEntity) {
				this.stopRiding();
				return super.hurt(source, amount);
			}
			if (this.isNoEndimationPlaying() && !this.getTeleportController().isTeleporting()) {
				if (source instanceof IndirectEntityDamageSource) {
					if (this.tryToTeleportRandomly(12)) {
						return true;
					}
				} else if (!(source.getEntity() instanceof LivingEntity) && this.random.nextInt(10) != 0) {
					this.tryToTeleportRandomly(1);
				}
			}
		}
		return super.hurt(source, amount);
	}

	@Override
	public PathNavigation createNavigation(Level world) {
		return new EndergeticFlyingPathNavigator(this, world);
	}

	@Override
	public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
		return false;
	}

	@Override
	protected void tickDeath() {
	}

	@Override
	protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
	}

	@Override
	public boolean onClimbable() {
		return false;
	}

	@Override
	protected float getStandingEyeHeight(Pose poseIn, EntityDimensions size) {
		return size.height * 0.5F;
	}

	@Override
	public boolean isSensitiveToWater() {
		return true;
	}

	@Override
	public boolean canRiderInteract() {
		return true;
	}

	@Override
	public void onEndimationStart(PlayableEndimation endimation, PlayableEndimation oldEndimation) {
		if (endimation == EEPlayableEndimations.PURPOID_DEATH) {
			this.deathTime = 0;
		}
	}

	@Override
	public void onEndimationEnd(PlayableEndimation endimation, PlayableEndimation newEndimation) {
		if (!this.level.isClientSide && newEndimation != EEPlayableEndimations.PURPOID_TELEPORT_FROM && (endimation == EEPlayableEndimations.PURPOID_TELEPORT_TO || endimation == EEPlayableEndimations.PURPOID_FAST_TELEPORT_TO)) {
			NetworkUtil.setPlayingAnimation(this, EEPlayableEndimations.PURPOID_TELEPORT_FROM);
		}
	}

	private boolean tryToTeleportRandomly(int attempts) {
		BlockPos pos = this.blockPosition();
		RandomSource random = this.getRandom();
		EntityDimensions size = this.getDimensions(this.getPose());
		Level world = this.level;
		for (int i = 0; i < attempts; i++) {
			BlockPos randomPos = pos.offset(random.nextInt(17) - random.nextInt(17), random.nextInt(17) - random.nextInt(17), random.nextInt(17) - random.nextInt(17));
			AABB collisionBox = size.makeBoundingBox(randomPos.getX() + 0.5F, randomPos.getY(), randomPos.getZ() + 0.5F);
			if (world.noCollision(collisionBox) && !world.containsAnyLiquid(collisionBox)) {
				this.teleportController.beginTeleportation(this, randomPos, this.getVehicle() != null);
				return true;
			}
		}
		return false;
	}

	static class PurpoidMoveController extends MoveControl {
		private Vec3 prevPos;
		private int stuckTicks;

		public PurpoidMoveController(PurpoidEntity mob) {
			super(mob);
			this.prevPos = mob.position();
		}

		@Override
		public void setWantedPosition(double x, double y, double z, double speedIn) {
			super.setWantedPosition(x, y, z, speedIn);
			this.stuckTicks = 0;
		}

		@Override
		public void tick() {
			PurpoidEntity purpoid = (PurpoidEntity) this.mob;
			boolean boosting = purpoid.isBoosting();
			if (this.operation == Operation.MOVE_TO) {
				Vec3 pos = purpoid.position();
				double x = pos.x();
				double z = pos.z();
				Vec3 vector3d = new Vec3(this.wantedX - x, this.wantedY - pos.y(), this.wantedZ - z);
				double distance = vector3d.length();
				if (distance <= 0.2F * purpoid.getSize().getScale()) {
					this.operation = Operation.WAIT;
				} else {
					double dx = vector3d.x;
					double dz = vector3d.z;
					purpoid.setYRot(purpoid.yBodyRot = this.rotlerp(purpoid.getYRot(), (float)(Mth.atan2(dz, dx) * (double)(180F / (float)Math.PI)) - 90.0F, 90.0F));
					float newMoveSpeed = Mth.lerp(0.125F, purpoid.getSpeed(), (boosting ? 1.25F : 1.0F) * (float)(this.speedModifier * purpoid.getAttributeValue(Attributes.MOVEMENT_SPEED)));
					purpoid.setSpeed(newMoveSpeed);
					double normalizedY = vector3d.y / distance;
					purpoid.setDeltaMovement(purpoid.getDeltaMovement().add(0.0F, newMoveSpeed * normalizedY * 0.1D, 0.0F));
					LookControl lookControl = purpoid.getLookControl();
					double d11 = lookControl.getWantedX();
					double d12 = lookControl.getWantedY();
					double d13 = lookControl.getWantedZ();
					double d8 = x + (dx / distance) * 2.0D;
					double d9 = purpoid.getEyeY() + normalizedY / distance;
					double d10 = z + (dz / distance) * 2.0D;
					if (!lookControl.isLookingAtTarget()) {
						d11 = d8;
						d12 = d9;
						d13 = d10;
					}

					lookControl.setLookAt(Mth.lerp(0.125D, d11, d8), Mth.lerp(0.125D, d12, d9), Mth.lerp(0.125D, d13, d10), 10.0F, 40.0F);

					if (this.prevPos.distanceToSqr(pos) <= 0.005F) {
						if (++this.stuckTicks >= 60) {
							this.operation = Operation.WAIT;
						}
					} else {
						this.stuckTicks = 0;
					}
				}
			} else {
				purpoid.setSpeed(0.0F);
				if (purpoid.isNoEndimationPlaying() && !purpoid.isResting()) {
					purpoid.setDeltaMovement(purpoid.getDeltaMovement().add(0.0F, boosting ? 0.025F : 0.01F, 0.0F));
				}
			}
			this.prevPos = purpoid.position();
		}

	}

	public static class TeleportController {
		@Nullable
		private BlockPos destination;

		private void tick(PurpoidEntity purpoid) {
			if ((purpoid.isEndimationPlaying(EEPlayableEndimations.PURPOID_TELEPORT_TO) || purpoid.isEndimationPlaying(EEPlayableEndimations.PURPOID_FAST_TELEPORT_TO)) && purpoid.getAnimationTick() == 10) {
				this.teleportToDestination(purpoid);
			} else if (purpoid.isEndimationPlaying(EEPlayableEndimations.PURPOID_TELEPORT_FROM)) {
				purpoid.setDeltaMovement(Vec3.ZERO);
			}
		}

		private void teleportToDestination(PurpoidEntity purpoid) {
			if (this.isTeleporting()) {
				BlockPos destination = this.destination;
				Entity ridingEntity = purpoid.getVehicle();
				if (ridingEntity != null) {
					ridingEntity.teleportToWithTicket(destination.getX() + 0.5F, destination.getY(), destination.getZ() + 0.5F);
				} else {
					NetworkUtil.teleportEntity(purpoid, destination.getX() + 0.5F, destination.getY(), destination.getZ() + 0.5F);
				}
				this.destination = null;
			}
		}

		public void beginTeleportation(PurpoidEntity purpoid, BlockPos destination, boolean fast) {
			NetworkUtil.setPlayingAnimation(purpoid, fast ? EEPlayableEndimations.PURPOID_FAST_TELEPORT_TO : EEPlayableEndimations.PURPOID_TELEPORT_TO);
			this.destination = destination;
		}

		public boolean isTeleporting() {
			return this.destination != null;
		}
	}
}
