package com.teamabnormals.endergetic.common.entity.eetle;

import com.teamabnormals.blueprint.core.endimator.Endimatable;
import com.teamabnormals.blueprint.core.endimator.PlayableEndimation;
import com.teamabnormals.blueprint.core.util.NetworkUtil;
import com.teamabnormals.endergetic.client.particle.data.CorrockCrownParticleData;
import com.teamabnormals.endergetic.common.block.EetleEggBlock;
import com.teamabnormals.endergetic.common.block.entity.EetleEggTileEntity;
import com.teamabnormals.endergetic.common.entity.eetle.ai.EetleHurtByTargetGoal;
import com.teamabnormals.endergetic.core.registry.EEBlocks;
import com.teamabnormals.endergetic.core.registry.EEParticleTypes;
import com.teamabnormals.endergetic.core.registry.EESoundEvents;
import com.teamabnormals.endergetic.core.other.EEPlayableEndimations;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.UUID;

public abstract class AbstractEetle extends Monster implements Endimatable {
	private static final EntityDataAccessor<Boolean> CHILD = SynchedEntityData.defineId(AbstractEetle.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDimensions LEETLE_SIZE = EntityDimensions.fixed(0.6F, 0.4375F);
	private static final AttributeModifier LEETLE_HEALTH = new AttributeModifier(UUID.fromString("8a1ea466-4b2d-11eb-ae93-0242ac130002"), "Leetle health decrease", -0.8F, AttributeModifier.Operation.MULTIPLY_BASE);
	private static final Direction[] EGG_DIRECTIONS = Direction.values();
	private final AvoidEntityGoal<Player> avoidEntityGoal = new AvoidEntityGoal<>(this, Player.class, 12.0F, 1.0F, 1.0F);
	private NearestAttackableTargetGoal<Player> attackableTargetGoal;
	private int growingAge;
	private int despawnTimer;
	private boolean fromEgg;
	protected int idleDelay;

	protected AbstractEetle(EntityType<? extends AbstractEetle> type, Level world) {
		super(type, world);
		this.moveControl = new GroundEetleMoveController(this);
		this.setMaxUpStep(0.5F);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(CHILD, false);
	}

	@Override
	protected void registerGoals() {
		this.attackableTargetGoal = new NearestAttackableTargetGoal<>(this, Player.class, true);
		this.targetSelector.addGoal(2, this.attackableTargetGoal);
		this.targetSelector.addGoal(1, new EetleHurtByTargetGoal(this));
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
		if (CHILD.equals(key)) {
			this.refreshDimensions();
		}
		super.onSyncedDataUpdated(key);
	}

	@Override
	public void tick() {
		super.tick();

		if (!this.level().isClientSide && this.isAlive()) {
			int age = this.growingAge;
			if (age < 0) {
				this.updateAge(++age);
			} else if (age > 0) {
				this.updateAge(--age);
			}
			if (!this.isBaby()) {
				if (this.idleDelay > 0) this.idleDelay--;
				if (this.despawnTimer > 0) {
					int newTime = --this.despawnTimer;
					if (newTime == 0) {
						this.level().broadcastEntityEvent(this, (byte) 20);
						this.discard();
					} else if (newTime <= 100 && newTime % 10 == 0) {
						LivingEntity attackTarget = this.getTarget();
						if (attackTarget != null && attackTarget.isAlive() && this.distanceToSqr(attackTarget) <= 256.0F && this.hasLineOfSight(attackTarget)) {
							this.despawnTimer += 105 + this.random.nextInt(11);
						}
					}
				}
			}
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt("Age", this.growingAge);
		compound.putInt("DespawnTimer", this.despawnTimer);
		compound.putBoolean("FromEgg", this.fromEgg);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.updateAge(compound.getInt("Age"));
		this.despawnTimer = Math.max(0, compound.getInt("DespawnTimer"));
		this.fromEgg = compound.getBoolean("FromEgg");
	}

	@Override
	public boolean isBaby() {
		return this.entityData.get(CHILD);
	}

	@Override
	public void setBaby(boolean child) {
		boolean wasChild = this.isBaby();
		this.entityData.set(CHILD, child);
		this.updateGoals(this.goalSelector, this.targetSelector, child);
		if (child) {
			this.xpReward = 2;
			if (this.level() != null && !this.level().isClientSide) {
				AttributeInstance maxHealth = this.getAttribute(Attributes.MAX_HEALTH);
				if (maxHealth != null) {
					maxHealth.addTransientModifier(LEETLE_HEALTH);
					this.setHealth(Math.max(this.getHealth(), this.getMaxHealth()));
				}
			}
		} else {
			this.xpReward = 6;
			if (wasChild) {
				AttributeInstance maxHealth = this.getAttribute(Attributes.MAX_HEALTH);
				if (maxHealth != null) {
					maxHealth.removeModifier(LEETLE_HEALTH);
				}
				this.setHealth(Math.min(this.getMaxHealth(), this.getHealth() * 4.3F));
			}
		}
	}

	public void updateAge(int growingAge) {
		int prevAge = this.growingAge;
		this.growingAge = growingAge;
		if (prevAge < 0 && growingAge >= 0 || prevAge >= 0 && growingAge < 0) {
			boolean willBeAdult = growingAge >= 0;
			if (willBeAdult && this.isBaby()) {
				NetworkUtil.setPlayingAnimation(this, EEPlayableEndimations.EETLE_GROW_UP);
				return;
			} else if (this.isEndimationPlaying(EEPlayableEndimations.EETLE_GROW_UP) && !willBeAdult) {
				NetworkUtil.setPlayingAnimation(this, PlayableEndimation.BLANK);
			}
			this.setBaby(!willBeAdult);
		}
	}

	public int getGrowingAge() {
		return this.growingAge;
	}

	public void markFromEgg() {
		this.fromEgg = true;
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
		//Patches of baby eetles will spawn 40% of the time
		if (reason == MobSpawnType.NATURAL && this.random.nextFloat() < 0.25F) {
			this.updateAge(-(20000 + this.random.nextInt(4001)));
			BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
			int startX = (int) this.getX();
			int startZ = (int) this.getZ();
			for (int x = -2; x <= 2; x++) {
				for (int z = -2; z <= 2; z++) {
					if (this.random.nextFloat() < 0.1F) {
						int currentX = startX + x;
						int currentZ = startZ + z;
						mutable.set(currentX, world.getHeight(Heightmap.Types.MOTION_BLOCKING, currentX, currentZ), currentZ);
						if (world.isEmptyBlock(mutable) && Block.canSupportRigidBlock(world, mutable.below())) {
							EntityType<?> type = this.getType();
							Entity entity = type.create(this.level());
							if (entity instanceof AbstractEetle) {
								AbstractEetle eetle = (AbstractEetle) entity;
								eetle.updateAge(-(20000 + this.random.nextInt(4001)));
								if (this.level().addFreshEntity(eetle)) {
									entity.absMoveTo(currentX + 0.5F, mutable.getY(), currentZ + 0.5F, this.random.nextFloat() * 360.0F, 0.0F);
								}
							}
						}
					}
				}
			}
		}
		return super.finalizeSpawn(world, difficultyIn, reason, spawnData, dataTag);
	}

	@Override
	public void die(DamageSource cause) {
		Level world = this.level();
		if (!this.isBaby() && this.random.nextFloat() < calculateEggChance(world, this.getBoundingBox().inflate(this.getAttributeValue(Attributes.FOLLOW_RANGE) * 1.25F)) && !this.isRemoved() && !this.dead) {
			if (!world.isClientSide) {
				BlockPos pos = this.blockPosition();
				if (world.getFluidState(pos).isEmpty() && world.getBlockState(pos).canBeReplaced()) {
					RandomSource random = this.random;
					EetleEggBlock.shuffleDirections(EGG_DIRECTIONS, random);
					BlockState defaultState = EEBlocks.EETLE_EGG.get().defaultBlockState();
					for (Direction direction : EGG_DIRECTIONS) {
						BlockState state = defaultState.setValue(EetleEggBlock.FACING, direction);
						if (state.canSurvive(world, pos)) {
							world.setBlockAndUpdate(pos, state.setValue(EetleEggBlock.SIZE, random.nextInt(2)));
							world.playSound(null, pos, EESoundEvents.EETLE_EGG_PLACE.get(), SoundSource.BLOCKS, 1.0F - random.nextFloat() * 0.1F, 0.8F + random.nextFloat() * 0.2F);
							BlockEntity tileEntity = world.getBlockEntity(pos);
							if (tileEntity instanceof EetleEggTileEntity) {
								EetleEggTileEntity eetleEggTileEntity = (EetleEggTileEntity) tileEntity;
								eetleEggTileEntity.updateHatchDelay(world, random.nextInt(11) + 5);
								eetleEggTileEntity.bypassSpawningGameRule();
							}
							if (world instanceof ServerLevel) {
								((ServerLevel) world).sendParticles(new CorrockCrownParticleData(EEParticleTypes.END_CROWN.get(), true), this.getX(), this.getY() + this.getBbHeight(), this.getZ(), 5, this.getBbWidth() / 4.0F, this.getBbHeight() / 4.0F, this.getBbWidth() / 4.0F, 0.1D);
							}
						}
					}
				}
			}
		}
		super.die(cause);
	}

	protected void updateGoals(GoalSelector goalSelector, GoalSelector targetSelector, boolean child) {
		if (child) {
			goalSelector.addGoal(1, this.avoidEntityGoal);
			targetSelector.removeGoal(this.attackableTargetGoal);
		} else {
			goalSelector.removeGoal(this.avoidEntityGoal);
			targetSelector.addGoal(2, this.attackableTargetGoal);
		}
	}

	public void resetIdleFlapDelay() {
		this.idleDelay = this.random.nextInt(41) + 30;
	}

	public void applyDespawnTimer() {
		this.despawnTimer = this.random.nextInt(101) + 600;
	}

	@Override
	public void onEndimationEnd(PlayableEndimation endimation, PlayableEndimation newEndimation) {
		Level world = this.level();
		if (endimation == EEPlayableEndimations.EETLE_GROW_UP && world instanceof ServerLevel) {
			this.setBaby(false);
			this.playSound(EESoundEvents.LEETLE_TRANSFORM.get(), this.getSoundVolume(), this.getVoicePitch());
			((ServerLevel) world).sendParticles(new CorrockCrownParticleData(EEParticleTypes.END_CROWN.get(), true), this.getX(), this.getY() + this.getBbHeight(), this.getZ(), 5, this.getBbWidth() / 4.0F, this.getBbHeight() / 4.0F, this.getBbWidth() / 4.0F, 0.1D);
		}
	}

	@Override
	public EntityDimensions getDimensions(Pose poseIn) {
		return this.isBaby() ? LEETLE_SIZE : super.getDimensions(poseIn);
	}

	@Override
	public int getAmbientSoundInterval() {
		return 160;
	}

	@Nullable
	@Override
	protected SoundEvent getAmbientSound() {
		return this.isBaby() ? EESoundEvents.LEETLE_AMBIENT.get() : super.getAmbientSound();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return this.isBaby() ? EESoundEvents.LEETLE_HURT.get() : super.getHurtSound(damageSourceIn);
	}

	@Override
	protected SoundEvent getDeathSound() {
		return this.isBaby() ? EESoundEvents.LEETLE_DEATH.get() : super.getDeathSound();
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState blockIn) {
		if (this.isBaby()) {
			this.playSound(EESoundEvents.LEETLE_STEP.get(), 0.15F, 1.0F);
		} else {
			super.playStepSound(pos, blockIn);
		}
	}

	@Override
	protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
		return 0.65F;
	}

	@Override
	public boolean isSensitiveToWater() {
		return true;
	}

	@Override
	public MobType getMobType() {
		return MobType.ARTHROPOD;
	}

	@Override
	public int getMaxSpawnClusterSize() {
		return 3;
	}

	@Override
	protected boolean shouldDropLoot() {
		return !this.isBaby();
	}

	@Override
	public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
		return false;
	}

	private static float calculateEggChance(Level world, AABB boundingBox) {
		return 0.6F - 0.075F * world.getEntitiesOfClass(AbstractEetle.class, boundingBox, eetle -> {
			return eetle.isAlive() && !eetle.isBaby();
		}).size();
	}

	protected static class GroundEetleMoveController extends MoveControl {

		protected GroundEetleMoveController(Mob mob) {
			super(mob);
		}

		@Override
		public void tick() {
			if (this.operation == MoveControl.Operation.MOVE_TO) {
				Mob mob = this.mob;
				this.operation = MoveControl.Operation.WAIT;
				double d0 = this.wantedX - mob.getX();
				double d1 = this.wantedZ - mob.getZ();
				double d2 = this.wantedY - mob.getY();
				double d3 = d0 * d0 + d2 * d2 + d1 * d1;
				if (d3 < 2.5000003E-7D) {
					mob.setZza(0.0F);
					return;
				}

				float f9 = (float) (Mth.atan2(d1, d0) * (double) (180F / (float) Math.PI)) - 90.0F;
				mob.setYRot(this.rotlerp(mob.getYRot(), f9, 90.0F));
				float moveSpeed = (float) (this.speedModifier * mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
				if (mob instanceof AbstractEetle abstractEetle) {
					if (abstractEetle.isBaby() && abstractEetle.fromEgg) {
						int growingAge = Math.abs(abstractEetle.growingAge);
						moveSpeed *= Math.min(2.0F, 7.0F * growingAge / (growingAge + 300.0F));
					}
				}
				mob.setSpeed(moveSpeed);
				BlockPos blockpos = mob.blockPosition();
				BlockState blockstate = mob.level().getBlockState(blockpos);
				VoxelShape voxelshape = blockstate.getCollisionShape(mob.level(), blockpos);
				if (d2 > mob.maxUpStep() && d0 * d0 + d1 * d1 < (double) Math.max(1.0F, mob.getBbWidth()) || !voxelshape.isEmpty() && mob.getY() < voxelshape.max(Direction.Axis.Y) + (double) blockpos.getY() && !blockstate.is(BlockTags.DOORS) && !blockstate.is(BlockTags.FENCES)) {
					mob.getJumpControl().jump();
					this.operation = MoveControl.Operation.JUMPING;
				}
				return;
			}
			super.tick();
		}

	}
}
