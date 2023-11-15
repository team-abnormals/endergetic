package com.teamabnormals.endergetic.common.entity.booflo;

import com.teamabnormals.blueprint.core.endimator.Endimatable;
import com.teamabnormals.endergetic.api.entity.util.EntityItemStackHelper;
import com.teamabnormals.endergetic.common.entity.booflo.ai.BabyFollowParentGoal;
import com.teamabnormals.endergetic.core.registry.EEEntityTypes;
import com.teamabnormals.endergetic.core.registry.EEItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class BoofloBaby extends PathfinderMob implements Endimatable {
	private static final EntityDataAccessor<Boolean> MOVING = SynchedEntityData.defineId(BoofloBaby.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Integer> BIRTH_TIMER = SynchedEntityData.defineId(BoofloBaby.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Integer> MOTHER_IMMUNITY_TICKS = SynchedEntityData.defineId(BoofloBaby.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Integer> BIRTH_POSITION_ID = SynchedEntityData.defineId(BoofloBaby.class, EntityDataSerializers.INT);
	public boolean wasBred;
	public int growingAge;
	public int forcedAge;
	public int forcedAgeTimer;
	private float prevTailAnimation;
	private float tailAnimation;
	private float tailSpeed;

	public BoofloBaby(EntityType<? extends BoofloBaby> type, Level worldIn) {
		super(type, worldIn);
		this.moveControl = new BoofloBaby.BoofloBabyMoveContoller(this);
		this.lookControl = new SmoothSwimmingLookControl(this, 10);
		this.tailAnimation = this.random.nextFloat();
		this.prevTailAnimation = this.tailAnimation;
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(MOVING, false);
		this.entityData.define(BIRTH_TIMER, 0);
		this.entityData.define(MOTHER_IMMUNITY_TICKS, 0);
		this.entityData.define(BIRTH_POSITION_ID, 0);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this)); //Makes Booflo when in water at surface to stay and swim like a cow in water
		this.goalSelector.addGoal(3, new BabyFollowParentGoal(this, 1.2F));
		this.goalSelector.addGoal(5, new BoofloBaby.RandomFlyingGoal(this, 1.1D, 20));
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 5.0F)
				.add(Attributes.MOVEMENT_SPEED, 0.85F)
				.add(Attributes.FOLLOW_RANGE, 18.0F);
	}

	@Override
	protected PathNavigation createNavigation(Level worldIn) {
		return new FlyingPathNavigation(this, worldIn) {

			@Override
			public boolean isStableDestination(BlockPos pos) {
				return this.level.getBlockState(pos).isAir();
			}

		};
	}

	@Override
	public void travel(Vec3 vec3d) {
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
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putBoolean("Moving", this.isMoving());
		compound.putInt("BirthTime", this.getBirthTimer());
		compound.putInt("Age", this.getGrowingAge());
		compound.putInt("MotherImmunityTicks", this.getMotherNoClipTicks());
		compound.putInt("BirthPosition", this.getBirthPositionID());
		compound.putInt("ForcedAge", this.forcedAge);
		compound.putBoolean("WasBred", this.wasBred);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.setMoving(compound.getBoolean("Moving"));
		this.setBirthTimer(compound.getInt("BirthTime"));
		this.setGrowingAge(compound.getInt("Age"));
		this.setMotherNoClipTicks(compound.getInt("MotherImmunityTicks"));
		this.setBirthPosition(BirthPosition.get(Math.min(compound.getInt("BirthPosition"), 2)));
		this.forcedAge = compound.getInt("ForcedAge");
		this.wasBred = compound.getBoolean("WasBred");
	}

	public boolean isMoving() {
		return this.entityData.get(MOVING);
	}

	public void setMoving(boolean moving) {
		this.entityData.set(MOVING, moving);
	}

	public int getBirthTimer() {
		return this.entityData.get(BIRTH_TIMER);
	}

	public boolean isBeingBorn() {
		return this.getBirthTimer() > 0;
	}

	public void setBirthTimer(int birthTimer) {
		this.entityData.set(BIRTH_TIMER, birthTimer);
	}

	public int getMotherNoClipTicks() {
		return this.entityData.get(MOTHER_IMMUNITY_TICKS);
	}

	public void setMotherNoClipTicks(int ticks) {
		this.entityData.set(MOTHER_IMMUNITY_TICKS, ticks);
	}

	public Vec3 getBirthPositionOffset() {
		return BirthPosition.get(this.getBirthPositionID()).offset;
	}

	public int getBirthPositionID() {
		return this.entityData.get(BIRTH_POSITION_ID);
	}

	public void setBirthPosition(BirthPosition birthPosition) {
		this.entityData.set(BIRTH_POSITION_ID, birthPosition.ordinal());
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

	@OnlyIn(Dist.CLIENT)
	public float getTailAnimation(float ptc) {
		return Mth.lerp(ptc, this.prevTailAnimation, this.tailAnimation);
	}

	@Override
	public boolean isEffectiveAi() {
		return !this.isBeingBorn() && super.isEffectiveAi();
	}

	@Override
	public boolean removeWhenFarAway(double distanceToClosestPlayer) {
		return !this.wasBred;
	}

	@Override
	protected MovementEmission getMovementEmission() {
		return MovementEmission.SOUNDS;
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
		} else {
			int birthTimer = this.getBirthTimer();
			if (birthTimer > 0) {
				if (--birthTimer == 0) {
					Vec3 posBeforeDismount = this.position();
					this.stopRiding();
					this.setPos(posBeforeDismount);
					this.setXRot(180.0F);
					this.setMotherNoClipTicks(50);
				}
				this.setBirthTimer(birthTimer);
			}
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
	}

	public LivingEntity growUp() {
		if (this.isAlive()) {
			this.spawnAtLocation(EEItems.BOOFLO_HIDE.get(), 1);

			BoofloAdolescent booflo = EEEntityTypes.BOOFLO_ADOLESCENT.get().create(this.level);
			booflo.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot());

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
			this.discard();

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

	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		if (itemstack.getItem() == EEItems.BOLLOOM_FRUIT.get()) {
			EntityItemStackHelper.consumeItemFromStack(player, itemstack);
			this.ageUp((int) ((-this.getGrowingAge() / 20) * 0.1F), true);
			return InteractionResult.sidedSuccess(this.level.isClientSide);
		}
		return super.mobInteract(player, hand);
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
		this.setGrowingAge(-24000);
		return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
	}

	@Override
	public ItemStack getPickedResult(HitResult target) {
		return new ItemStack(EEItems.BOOFLO_SPAWN_EGG.get());
	}

	@Override
	protected void doPush(Entity entityIn) {
		if (!(entityIn instanceof Booflo)) {
			super.doPush(entityIn);
		}
	}

	@Override
	public boolean isInvulnerableTo(DamageSource source) {
		return source == DamageSource.IN_WALL || source == DamageSource.FLY_INTO_WALL || super.isInvulnerableTo(source);
	}

	@Override
	public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
		return false;
	}

	@Override
	protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
	}

	static class RandomFlyingGoal extends RandomStrollGoal {
		public RandomFlyingGoal(PathfinderMob p_i48937_1_, double p_i48937_2_, int p_i48937_4_) {
			super(p_i48937_1_, p_i48937_2_, p_i48937_4_);
		}

		@Nullable
		protected Vec3 getPosition() {
			Vec3 view = this.mob.getViewVector(0.0F);
			double viewX = view.x;
			double viewZ = view.z;
			Vec3 vec3d = HoverRandomPos.getPos(this.mob, 7, 4, viewX, viewZ, ((float) Math.PI / 2F), 3, 1);

			for (int i = 0; vec3d != null && !this.mob.level.getBlockState(new BlockPos(vec3d)).isPathfindable(this.mob.level, new BlockPos(vec3d), PathComputationType.WATER) && i++ < 10; vec3d = HoverRandomPos.getPos(this.mob, 7, 4, viewX, viewZ, ((float) Math.PI / 2F), 3, 1)) {
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

	static class BoofloBabyMoveContoller extends MoveControl {
		private final BoofloBaby booflo;

		public BoofloBabyMoveContoller(BoofloBaby booflo) {
			super(booflo);
			this.booflo = booflo;
		}

		public void tick() {
			if (this.operation == MoveControl.Operation.MOVE_TO && !this.booflo.getNavigation().isDone()) {
				Vec3 vec3d = new Vec3(this.wantedX - this.booflo.getX(), this.wantedY - this.booflo.getY(), this.wantedZ - this.booflo.getZ());
				double d0 = vec3d.length();
				double d1 = vec3d.y / d0;
				float f = (float) (Mth.atan2(vec3d.z, vec3d.x) * (double) (180F / (float) Math.PI)) - 90F;

				this.booflo.setYRot(this.rotlerp(this.booflo.getYRot(), f, 10.0F));
				this.booflo.yBodyRot = this.booflo.getYRot();
				this.booflo.yHeadRot = this.booflo.getYRot();

				float f1 = (float) (this.speedModifier * this.booflo.getAttribute(Attributes.MOVEMENT_SPEED).getValue());
				float f2 = Mth.lerp(0.125F, this.booflo.getSpeed(), f1);

				this.booflo.setSpeed(f2);

				double d3 = Math.cos(this.booflo.getYRot() * ((float) Math.PI / 180F));
				double d4 = Math.sin(this.booflo.getYRot() * ((float) Math.PI / 180F));
				double d5 = Math.sin((double) (this.booflo.tickCount + this.booflo.getId()) * 0.75D) * 0.05D;

				if (!this.booflo.isInWater()) {
					float f3 = -((float) (Mth.atan2(vec3d.y, Mth.sqrt((float) (vec3d.x * vec3d.x + vec3d.z * vec3d.z))) * (double) (180F / (float) Math.PI)));
					f3 = Mth.clamp(Mth.wrapDegrees(f3), -85.0F, 85.0F);
					this.booflo.setXRot(this.rotlerp(this.booflo.getXRot(), f3, 5.0F));
				}

				this.booflo.setDeltaMovement(this.booflo.getDeltaMovement().add(0, d5 * (d4 + d3) * 0.25D + (double) f2 * d1 * 0.02D, 0));

				this.booflo.setMoving(true);
			} else {
				this.booflo.setSpeed(0F);
				this.booflo.setMoving(false);
			}
		}
	}

	public enum BirthPosition {
		BACK(-0.25F, 0.0F),
		LEFT(0.2F, -0.3F),
		RIGHT(0.2F, 0.3F);

		private static final BirthPosition[] VALUES = values();
		private final Vec3 offset;

		BirthPosition(float xOffset, float zOffset) {
			this.offset = new Vec3(xOffset, 0.0F, zOffset);
		}

		public static BirthPosition get(int id) {
			return VALUES[id];
		}
	}
}
