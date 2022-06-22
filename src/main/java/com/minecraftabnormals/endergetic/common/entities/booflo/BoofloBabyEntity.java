package com.minecraftabnormals.endergetic.common.entities.booflo;

import javax.annotation.Nullable;

import com.minecraftabnormals.abnormals_core.core.api.IAgeableEntity;
import com.minecraftabnormals.abnormals_core.core.endimator.Endimation;
import com.minecraftabnormals.abnormals_core.core.endimator.entity.EndimatedEntity;
import com.minecraftabnormals.endergetic.api.entity.util.EntityItemStackHelper;
import com.minecraftabnormals.endergetic.common.entities.booflo.ai.BabyFollowParentGoal;
import com.minecraftabnormals.endergetic.core.registry.EEEntities;
import com.minecraftabnormals.endergetic.core.registry.EEItems;

import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.LookController;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BoofloBabyEntity extends EndimatedEntity implements IAgeableEntity {
	private static final DataParameter<Boolean> MOVING = EntityDataManager.defineId(BoofloBabyEntity.class, DataSerializers.BOOLEAN);
	public static final DataParameter<Boolean> BEING_BORN = EntityDataManager.defineId(BoofloBabyEntity.class, DataSerializers.BOOLEAN);
	public static final DataParameter<Integer> MOTHER_IMMUNITY_TICKS = EntityDataManager.defineId(BoofloBabyEntity.class, DataSerializers.INT);
	public static final Endimation BIRTH = new Endimation(60);
	public boolean wasBred;
	public int growingAge;
	public int forcedAge;
	public int forcedAgeTimer;
	private float prevTailAnimation;
	private float tailAnimation;
	private float tailSpeed;

	public BoofloBabyEntity(EntityType<? extends BoofloBabyEntity> type, World worldIn) {
		super(type, worldIn);
		this.moveControl = new BoofloBabyEntity.BoofloBabyMoveContoller(this);
		this.lookControl = new BoofloBabyLookController(this, 10);
		this.tailAnimation = this.random.nextFloat();
		this.prevTailAnimation = this.tailAnimation;
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(MOVING, false);
		this.entityData.define(BEING_BORN, false);
		this.entityData.define(MOTHER_IMMUNITY_TICKS, 0);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new SwimGoal(this)); //Makes Booflo when in water at surface to stay and swim like a cow in water
		this.goalSelector.addGoal(3, new BabyFollowParentGoal(this, 1.2F));
		this.goalSelector.addGoal(5, new BoofloBabyEntity.RandomFlyingGoal(this, 1.1D, 20));
	}

	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return MobEntity.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 5.0F)
				.add(Attributes.MOVEMENT_SPEED, 0.85F)
				.add(Attributes.FOLLOW_RANGE, 18.0F);
	}

	@Override
	protected PathNavigator createNavigation(World worldIn) {
		return new FlyingPathNavigator(this, worldIn) {

			@SuppressWarnings("deprecation")
			@Override
			public boolean isStableDestination(BlockPos pos) {
				return this.level.getBlockState(pos).isAir();
			}

		};
	}

	@Override
	public void travel(Vector3d vec3d) {
		if (this.isEffectiveAi() && !this.isInWater()) {
			this.moveRelative(0.012F, vec3d);
			this.move(MoverType.SELF, this.getDeltaMovement());
			this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
			if (!this.isMoving()) {
				this.setDeltaMovement(this.getDeltaMovement().subtract(0, 0.0025D, 0));
			}
		} else {
			super.travel(vec3d);
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundNBT compound) {
		super.addAdditionalSaveData(compound);
		compound.putBoolean("Moving", this.isMoving());
		compound.putBoolean("IsBeingBorn", this.isBeingBorn());
		compound.putInt("Age", this.getGrowingAge());
		compound.putInt("MotherImmunityTicks", this.getMotherNoClipTicks());
		compound.putInt("ForcedAge", this.forcedAge);
		compound.putBoolean("WasBred", this.wasBred);
	}

	@Override
	public void readAdditionalSaveData(CompoundNBT compound) {
		super.readAdditionalSaveData(compound);
		this.setMoving(compound.getBoolean("Moving"));
		this.setBeingBorn(compound.getBoolean("IsBeingBorn"));
		this.setGrowingAge(compound.getInt("Age"));
		this.setMotherNoClipTicks(compound.getInt("MotherImmunityTicks"));
		this.forcedAge = compound.getInt("ForcedAge");
		this.wasBred = compound.getBoolean("WasBred");
	}

	public boolean isMoving() {
		return this.entityData.get(MOVING);
	}

	public void setMoving(boolean moving) {
		this.entityData.set(MOVING, moving);
	}

	public boolean isBeingBorn() {
		return this.entityData.get(BEING_BORN);
	}

	public void setBeingBorn(boolean beingBorn) {
		this.entityData.set(BEING_BORN, beingBorn);
	}

	public int getMotherNoClipTicks() {
		return this.entityData.get(MOTHER_IMMUNITY_TICKS);
	}

	public void setMotherNoClipTicks(int ticks) {
		this.entityData.set(MOTHER_IMMUNITY_TICKS, ticks);
	}

	public int getGrowingAge() {
		if (this.level.isClientSide) {
			return -1;
		} else {
			return this.growingAge;
		}
	}

	public void setGrowingAge(int age) {
		int oldAge = this.growingAge;
		this.growingAge = age;
		if (oldAge < 0 && age >= 0 || oldAge > 0 && age < 0) {
			this.growUp();
		}
	}

	public void addGrowth(int growth) {
		this.ageUp(growth, false);
	}

	@OnlyIn(Dist.CLIENT)
	public float getTailAnimation(float ptc) {
		return MathHelper.lerp(ptc, this.prevTailAnimation, this.tailAnimation);
	}

	@Override
	public boolean isNoAi() {
		return this.isBeingBorn() || super.isNoAi();
	}

	@Override
	public boolean removeWhenFarAway(double distanceToClosestPlayer) {
		return !this.wasBred;
	}

	@Override
	protected boolean isMovementNoisy() {
		return false;
	}

	public int getMaxHeadXRot() {
		return 1;
	}

	public int getMaxHeadYRot() {
		return 1;
	}

	@Override
	public void aiStep() {
		super.aiStep();

		if (this.level.isClientSide) {
			this.prevTailAnimation = this.tailAnimation;
			if (this.isInWater()) {
				this.tailSpeed = 1.0F;
			} else if (this.isMoving()) {
				if (this.tailSpeed < 0.5F) {
					this.tailSpeed = 1.0F;
				} else {
					this.tailSpeed += (0.25F - this.tailSpeed) * 0.1F;
				}
			} else {
				this.tailSpeed += (0.1875F - this.tailSpeed) * 0.1F;
			}
			this.tailAnimation += this.tailSpeed;
		}

		if (this.level.isClientSide) {
			if (this.forcedAgeTimer > 0) {
				if (this.forcedAgeTimer % 4 == 0) {
					this.level.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getX() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), this.getY() + 0.5D + (double) (this.random.nextFloat() * this.getBbHeight()), this.getZ() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), 0.0D, 0.0D, 0.0D);
				}

				this.forcedAgeTimer--;
			}
		} else if (this.isAlive()) {
			int growingAge = this.getGrowingAge();
			if (growingAge < 0) {
				growingAge++;
				this.setGrowingAge(growingAge);
			} else if (growingAge > 0) {
				growingAge--;
				this.setGrowingAge(growingAge);
			}
		}

		if (this.getMotherNoClipTicks() > 0) {
			this.setMotherNoClipTicks(this.getMotherNoClipTicks() - 1);
		}

		if (this.isBeingBorn() && this.isNoEndimationPlaying()) {
			this.setPlayingEndimation(BIRTH);
		}

		if (this.isEndimationPlaying(BIRTH)) {
			if (this.getAnimationTick() == 59) {
				double[] oldPosition = {this.getX(), this.getY(), this.getZ()};
				this.stopRiding();
				this.setBeingBorn(false);
				this.setPos(oldPosition[0], oldPosition[1], oldPosition[2]);
				this.xRot = 180;
				this.setMotherNoClipTicks(50);
			}
		} else if (this.isNoEndimationPlaying() && this.getVehicle() instanceof BoofloEntity) {
			if (this.tickCount > 260) {
				this.stopRiding();
				this.setBeingBorn(false);
			}
		}
	}

	public LivingEntity growUp() {
		if (this.isAlive()) {
			this.spawnAtLocation(EEItems.BOOFLO_HIDE.get(), 1);

			BoofloAdolescentEntity booflo = EEEntities.BOOFLO_ADOLESCENT.get().create(this.level);
			booflo.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, this.xRot);

			if (this.hasCustomName()) {
				booflo.setCustomName(this.getCustomName());
				booflo.setCustomNameVisible(this.isCustomNameVisible());
			}

			if (this.isLeashed()) {
				booflo.setLeashedTo(this.getLeashHolder(), true);
				this.dropLeash(true, false);
			}

			if (this.getVehicle() != null) {
				booflo.startRiding(this.getVehicle());
			}

			booflo.setHealth(booflo.getMaxHealth());
			booflo.setGrowingAge(-24000);
			booflo.wasBred = this.wasBred;
			this.level.addFreshEntity(booflo);
			this.remove();

			return booflo;
		}
		return this;
	}

	public void ageUp(int growthSeconds, boolean updateForcedAge) {
		int growingAge = this.getGrowingAge();
		int j = growingAge;
		growingAge += growthSeconds * 20;
		if (growingAge > 0) {
			growingAge = 0;
		}

		int k = growingAge - j;

		this.setGrowingAge(growingAge);
		if (updateForcedAge) {
			this.forcedAge += k;
			if (this.forcedAgeTimer == 0) {
				this.forcedAgeTimer = 40;
			}
		}

		if (this.getGrowingAge() == 0) {
			this.setGrowingAge(this.forcedAge);
		}
	}

	public ActionResultType mobInteract(PlayerEntity player, Hand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		if (itemstack.getItem() == EEItems.BOLLOOM_FRUIT.get()) {
			EntityItemStackHelper.consumeItemFromStack(player, itemstack);
			this.ageUp((int) ((-this.getGrowingAge() / 20) * 0.1F), true);
			return ActionResultType.sidedSuccess(this.level.isClientSide);
		}
		return super.mobInteract(player, hand);
	}

	@Nullable
	@Override
	public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
		this.setGrowingAge(-24000);
		return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(EEItems.BOOFLO_SPAWN_EGG.get());
	}

	@Override
	protected void doPush(Entity entityIn) {
		if (!(entityIn instanceof BoofloEntity)) {
			super.doPush(entityIn);
		}
	}

	@Override
	public boolean isInvulnerableTo(DamageSource source) {
		return source == DamageSource.IN_WALL || source == DamageSource.FLY_INTO_WALL || super.isInvulnerableTo(source);
	}

	@Override
	public Endimation[] getEndimations() {
		return new Endimation[]{
				BIRTH
		};
	}

	@Override
	public boolean causeFallDamage(float distance, float damageMultiplier) {
		return false;
	}

	@Override
	protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
	}

	@Override
	public boolean hasGrowthProgress() {
		return true;
	}

	@Override
	public void resetGrowthProgress() {
		this.setGrowingAge(-24000);
	}

	@Override
	public boolean canAge(boolean isGrowing) {
		return isGrowing;
	}

	@Override
	public LivingEntity attemptAging(boolean isGrowing) {
		return isGrowing ? this.growUp() : this;
	}

	static class RandomFlyingGoal extends RandomWalkingGoal {
		public RandomFlyingGoal(CreatureEntity p_i48937_1_, double p_i48937_2_, int p_i48937_4_) {
			super(p_i48937_1_, p_i48937_2_, p_i48937_4_);
		}

		@Nullable
		protected Vector3d getPosition() {
			Vector3d vec3d = RandomPositionGenerator.getPos(this.mob, 7, 4);

			for (int i = 0; vec3d != null && !this.mob.level.getBlockState(new BlockPos(vec3d)).isPathfindable(this.mob.level, new BlockPos(vec3d), PathType.WATER) && i++ < 10; vec3d = RandomPositionGenerator.getPos(this.mob, 7, 4)) {
				;
			}

			return vec3d;
		}

		@Override
		public boolean canUse() {
			return super.canUse() && !this.mob.isInWater();
		}

		@Override
		public boolean canContinueToUse() {
			return super.canContinueToUse() && !this.mob.isInWater();
		}
	}

	static class BoofloBabyMoveContoller extends MovementController {
		private final BoofloBabyEntity booflo;

		public BoofloBabyMoveContoller(BoofloBabyEntity booflo) {
			super(booflo);
			this.booflo = booflo;
		}

		public void tick() {
			if (this.operation == MovementController.Action.MOVE_TO && !this.booflo.getNavigation().isDone()) {
				Vector3d vec3d = new Vector3d(this.wantedX - this.booflo.getX(), this.wantedY - this.booflo.getY(), this.wantedZ - this.booflo.getZ());
				double d0 = vec3d.length();
				double d1 = vec3d.y / d0;
				float f = (float) (MathHelper.atan2(vec3d.z, vec3d.x) * (double) (180F / (float) Math.PI)) - 90F;

				this.booflo.yRot = this.rotlerp(this.booflo.yRot, f, 10.0F);
				this.booflo.yBodyRot = this.booflo.yRot;
				this.booflo.yHeadRot = this.booflo.yRot;

				float f1 = (float) (this.speedModifier * this.booflo.getAttribute(Attributes.MOVEMENT_SPEED).getValue());
				float f2 = MathHelper.lerp(0.125F, this.booflo.getSpeed(), f1);

				this.booflo.setSpeed(f2);

				double d3 = Math.cos(this.booflo.yRot * ((float) Math.PI / 180F));
				double d4 = Math.sin(this.booflo.yRot * ((float) Math.PI / 180F));
				double d5 = Math.sin((double) (this.booflo.tickCount + this.booflo.getId()) * 0.75D) * 0.05D;

				if (!this.booflo.isInWater()) {
					float f3 = -((float) (MathHelper.atan2(vec3d.y, (double) MathHelper.sqrt(vec3d.x * vec3d.x + vec3d.z * vec3d.z)) * (double) (180F / (float) Math.PI)));
					f3 = MathHelper.clamp(MathHelper.wrapDegrees(f3), -85.0F, 85.0F);
					this.booflo.xRot = this.rotlerp(this.booflo.xRot, f3, 5.0F);
				}

				this.booflo.setDeltaMovement(this.booflo.getDeltaMovement().add(0, d5 * (d4 + d3) * 0.25D + (double) f2 * d1 * 0.02D, 0));

				this.booflo.setMoving(true);
			} else {
				this.booflo.setSpeed(0F);
				this.booflo.setMoving(false);
			}
		}
	}

	static class BoofloBabyLookController extends LookController {
		private final int angleLimit;

		public BoofloBabyLookController(BoofloBabyEntity baby, int angleLimit) {
			super(baby);
			this.angleLimit = angleLimit;
		}

		public void tick() {
			if (this.hasWanted) {
				this.hasWanted = false;
				this.mob.yHeadRot = this.rotateTowards(this.mob.yHeadRot, this.getYRotD() + 20.0F, this.yMaxRotSpeed);
				this.mob.xRot = this.rotateTowards(this.mob.xRot, this.getXRotD() + 10.0F, this.xMaxRotAngle);
			} else {
				if (this.mob.getNavigation().isDone()) {
					this.mob.xRot = this.rotateTowards(this.mob.xRot, 0.0F, 5.0F);
				}
				this.mob.yHeadRot = this.rotateTowards(this.mob.yHeadRot, this.mob.yBodyRot, this.yMaxRotSpeed);
			}

			float wrappedDegrees = MathHelper.wrapDegrees(this.mob.yHeadRot - this.mob.yBodyRot);
			if (wrappedDegrees < (float) (-this.angleLimit)) {
				this.mob.yBodyRot -= 4.0F;
			} else if (wrappedDegrees > (float) this.angleLimit) {
				this.mob.yBodyRot += 4.0F;
			}
		}
	}

}
