package com.minecraftabnormals.endergetic.common.entities.eetle;

import com.minecraftabnormals.abnormals_core.core.endimator.Endimation;
import com.minecraftabnormals.abnormals_core.core.endimator.entity.IEndimatedEntity;
import com.minecraftabnormals.abnormals_core.core.util.NetworkUtil;
import com.minecraftabnormals.endergetic.client.particles.EEParticles;
import com.minecraftabnormals.endergetic.client.particles.data.CorrockCrownParticleData;
import com.minecraftabnormals.endergetic.common.blocks.EetleEggBlock;
import com.minecraftabnormals.endergetic.common.entities.eetle.ai.EetleHurtByTargetGoal;
import com.minecraftabnormals.endergetic.common.tileentities.EetleEggTileEntity;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.minecraftabnormals.endergetic.core.registry.EEItems;
import com.minecraftabnormals.endergetic.core.registry.EESounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.UUID;

public abstract class AbstractEetleEntity extends MonsterEntity implements IEndimatedEntity {
	private static final DataParameter<Boolean> CHILD = EntityDataManager.defineId(AbstractEetleEntity.class, DataSerializers.BOOLEAN);
	private static final EntitySize LEETLE_SIZE = EntitySize.fixed(0.6F, 0.4375F);
	private static final AttributeModifier LEETLE_HEALTH = new AttributeModifier(UUID.fromString("8a1ea466-4b2d-11eb-ae93-0242ac130002"), "Leetle health decrease", -0.8F, AttributeModifier.Operation.MULTIPLY_BASE);
	private static final Direction[] EGG_DIRECTIONS = Direction.values();
	public static final Endimation GROW_UP = new Endimation(30);
	private final AvoidEntityGoal<PlayerEntity> avoidEntityGoal = new AvoidEntityGoal<>(this, PlayerEntity.class, 12.0F, 1.0F, 1.0F);
	private NearestAttackableTargetGoal<PlayerEntity> attackableTargetGoal;
	private Endimation endimation = BLANK_ANIMATION;
	private int animationTick;
	private int growingAge;
	private int despawnTimer;
	private boolean fromEgg;
	protected int idleDelay;

	protected AbstractEetleEntity(EntityType<? extends AbstractEetleEntity> type, World world) {
		super(type, world);
		this.moveControl = new GroundEetleMoveController(this);
		this.maxUpStep = 0.5F;
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(CHILD, false);
	}

	@Override
	protected void registerGoals() {
		this.attackableTargetGoal = new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true);
		this.targetSelector.addGoal(2, this.attackableTargetGoal);
		this.targetSelector.addGoal(1, new EetleHurtByTargetGoal(this));
	}

	@Override
	public void onSyncedDataUpdated(DataParameter<?> key) {
		if (CHILD.equals(key)) {
			this.refreshDimensions();
		}
		super.onSyncedDataUpdated(key);
	}

	@Override
	public void tick() {
		super.tick();
		this.endimateTick();

		if (!this.level.isClientSide && this.isAlive()) {
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
						this.level.broadcastEntityEvent(this, (byte) 20);
						this.remove();
					} else if (newTime <= 100 && newTime % 10 == 0) {
						LivingEntity attackTarget = this.getTarget();
						if (attackTarget != null && attackTarget.isAlive() && this.distanceToSqr(attackTarget) <= 256.0F && this.canSee(attackTarget)) {
							this.despawnTimer += 105 + this.random.nextInt(11);
						}
					}
				}
			}
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundNBT compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt("Age", this.growingAge);
		compound.putInt("DespawnTimer", this.despawnTimer);
		compound.putBoolean("FromEgg", this.fromEgg);
	}

	@Override
	public void readAdditionalSaveData(CompoundNBT compound) {
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
			if (this.level != null && !this.level.isClientSide) {
				ModifiableAttributeInstance maxHealth = this.getAttribute(Attributes.MAX_HEALTH);
				if (maxHealth != null) {
					maxHealth.addTransientModifier(LEETLE_HEALTH);
					this.setHealth(Math.max(this.getHealth(), this.getMaxHealth()));
				}
			}
		} else {
			this.xpReward = 6;
			if (wasChild) {
				ModifiableAttributeInstance maxHealth = this.getAttribute(Attributes.MAX_HEALTH);
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
				NetworkUtil.setPlayingAnimationMessage(this, GROW_UP);
				return;
			} else if (this.isEndimationPlaying(GROW_UP) && !willBeAdult) {
				NetworkUtil.setPlayingAnimationMessage(this, BLANK_ANIMATION);
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
	public ILivingEntityData finalizeSpawn(IServerWorld world, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnData, @Nullable CompoundNBT dataTag) {
		//Patches of baby eetles will spawn 40% of the time
		if (reason == SpawnReason.NATURAL && this.random.nextFloat() < 0.4F) {
			this.updateAge(-(20000 + this.random.nextInt(4001)));
			BlockPos.Mutable mutable = new BlockPos.Mutable();
			int startX = (int) this.getX();
			int startZ = (int) this.getZ();
			for (int x = -2; x <= 2; x++) {
				for (int z = -2; z <= 2; z++) {
					if (this.random.nextFloat() < 0.1F) {
						int currentX = startX + x;
						int currentZ = startZ + z;
						mutable.set(currentX, world.getHeight(Heightmap.Type.MOTION_BLOCKING, currentX, currentZ), currentZ);
						if (world.isEmptyBlock(mutable) && Block.canSupportRigidBlock(world, mutable.below())) {
							EntityType<?> type = this.getType();
							Entity entity = type.create(this.level);
							if (entity instanceof AbstractEetleEntity) {
								AbstractEetleEntity eetle = (AbstractEetleEntity) entity;
								eetle.updateAge(-(20000 + this.random.nextInt(4001)));
								if (this.level.addFreshEntity(eetle)) {
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

	@SuppressWarnings("deprecation")
	@Override
	public void die(DamageSource cause) {
		World world = this.level;
		if (!this.isBaby() && this.random.nextFloat() < calculateEggChance(world, this.getBoundingBox().inflate(this.getAttributeValue(Attributes.FOLLOW_RANGE) * 1.25F)) && !this.removed && !this.dead) {
			if (!world.isClientSide) {
				BlockPos pos = this.blockPosition();
				if (world.getFluidState(pos).isEmpty() && world.getBlockState(pos).getMaterial().isReplaceable()) {
					Random random = this.random;
					EetleEggBlock.shuffleDirections(EGG_DIRECTIONS, random);
					BlockState defaultState = EEBlocks.EETLE_EGG.get().defaultBlockState();
					for (Direction direction : EGG_DIRECTIONS) {
						BlockState state = defaultState.setValue(EetleEggBlock.FACING, direction);
						if (state.canSurvive(world, pos)) {
							world.setBlockAndUpdate(pos, state.setValue(EetleEggBlock.SIZE, random.nextInt(2)));
							world.playSound(null, pos, EESounds.EETLE_EGG_PLACE.get(), SoundCategory.BLOCKS, 1.0F - random.nextFloat() * 0.1F, 0.8F + random.nextFloat() * 0.2F);
							TileEntity tileEntity = world.getBlockEntity(pos);
							if (tileEntity instanceof EetleEggTileEntity) {
								EetleEggTileEntity eetleEggTileEntity = (EetleEggTileEntity) tileEntity;
								eetleEggTileEntity.updateHatchDelay(world, random.nextInt(11) + 5);
								eetleEggTileEntity.bypassSpawningGameRule();
							}
							if (world instanceof ServerWorld) {
								((ServerWorld) world).sendParticles(new CorrockCrownParticleData(EEParticles.END_CROWN.get(), true), this.getX(), this.getY() + this.getBbHeight(), this.getZ(), 5, this.getBbWidth() / 4.0F, this.getBbHeight() / 4.0F, this.getBbWidth() / 4.0F, 0.1D);
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
	public Endimation getPlayingEndimation() {
		return this.endimation;
	}

	@Override
	public void setPlayingEndimation(Endimation endimation) {
		this.onEndimationEnd(this.endimation);
		this.endimation = endimation;
		this.setAnimationTick(0);
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
	public void onEndimationEnd(Endimation endimation) {
		World world = this.level;
		if (endimation == GROW_UP && world instanceof ServerWorld) {
			this.setBaby(false);
			this.playSound(EESounds.LEETLE_TRANSFORM.get(), this.getSoundVolume(), this.getVoicePitch());
			((ServerWorld) world).sendParticles(new CorrockCrownParticleData(EEParticles.END_CROWN.get(), true), this.getX(), this.getY() + this.getBbHeight(), this.getZ(), 5, this.getBbWidth() / 4.0F, this.getBbHeight() / 4.0F, this.getBbWidth() / 4.0F, 0.1D);
		}
	}

	@Override
	public EntitySize getDimensions(Pose poseIn) {
		return this.isBaby() ? LEETLE_SIZE : super.getDimensions(poseIn);
	}

	@Override
	public int getAmbientSoundInterval() {
		return 160;
	}

	@Nullable
	@Override
	protected SoundEvent getAmbientSound() {
		return this.isBaby() ? EESounds.LEETLE_AMBIENT.get() : super.getAmbientSound();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return this.isBaby() ? EESounds.LEETLE_HURT.get() : super.getHurtSound(damageSourceIn);
	}

	@Override
	protected SoundEvent getDeathSound() {
		return this.isBaby() ? EESounds.LEETLE_DEATH.get() : super.getDeathSound();
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState blockIn) {
		if (this.isBaby()) {
			this.playSound(EESounds.LEETLE_STEP.get(), 0.15F, 1.0F);
		} else {
			super.playStepSound(pos, blockIn);
		}
	}

	@Override
	protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
		return 0.65F;
	}

	@Override
	public boolean isSensitiveToWater() {
		return true;
	}

	@Override
	public CreatureAttribute getMobType() {
		return CreatureAttribute.ARTHROPOD;
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
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(EEItems.EETLE_SPAWN_EGG.get());
	}

	@Override
	public boolean causeFallDamage(float distance, float damageMultiplier) {
		return false;
	}

	private static float calculateEggChance(World world, AxisAlignedBB boundingBox) {
		return 0.6F - 0.075F * world.getEntitiesOfClass(AbstractEetleEntity.class, boundingBox, eetle -> {
			return eetle.isAlive() && !eetle.isBaby();
		}).size();
	}

	protected static class GroundEetleMoveController extends MovementController {

		protected GroundEetleMoveController(MobEntity mob) {
			super(mob);
		}

		@Override
		public void tick() {
			if (this.operation == MovementController.Action.MOVE_TO) {
				MobEntity mob = this.mob;
				this.operation = MovementController.Action.WAIT;
				double d0 = this.wantedX - mob.getX();
				double d1 = this.wantedZ - mob.getZ();
				double d2 = this.wantedY - mob.getY();
				double d3 = d0 * d0 + d2 * d2 + d1 * d1;
				if (d3 < 2.5000003E-7D) {
					mob.setZza(0.0F);
					return;
				}

				float f9 = (float)(MathHelper.atan2(d1, d0) * (double)(180F / (float)Math.PI)) - 90.0F;
				mob.yRot = this.rotlerp(mob.yRot, f9, 90.0F);
				float moveSpeed = (float)(this.speedModifier * mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
				if (mob instanceof AbstractEetleEntity) {
					AbstractEetleEntity abstractEetleEntity = (AbstractEetleEntity) mob;
					if (abstractEetleEntity.isBaby() && abstractEetleEntity.fromEgg) {
						int growingAge = Math.abs(abstractEetleEntity.growingAge);
						moveSpeed *= Math.min(2.0F, 7.0F * growingAge / (growingAge + 300.0F));
					}
				}
				mob.setSpeed(moveSpeed);
				BlockPos blockpos = mob.blockPosition();
				BlockState blockstate = mob.level.getBlockState(blockpos);
				Block block = blockstate.getBlock();
				VoxelShape voxelshape = blockstate.getCollisionShape(mob.level, blockpos);
				if (d2 > mob.maxUpStep && d0 * d0 + d1 * d1 < (double)Math.max(1.0F, mob.getBbWidth()) || !voxelshape.isEmpty() && mob.getY() < voxelshape.max(Direction.Axis.Y) + (double)blockpos.getY() && !block.is(BlockTags.DOORS) && !block.is(BlockTags.FENCES)) {
					mob.getJumpControl().jump();
					this.operation = MovementController.Action.JUMPING;
				}
				return;
			}
			super.tick();
		}

	}
}
