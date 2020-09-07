package com.minecraftabnormals.endergetic.common.entities.booflo;

import javax.annotation.Nullable;

import com.teamabnormals.abnormals_core.core.library.endimator.Endimation;
import com.teamabnormals.abnormals_core.core.library.endimator.entity.EndimatedEntity;
import com.teamabnormals.abnormals_core.core.utils.NetworkUtil;
import com.minecraftabnormals.endergetic.api.entity.pathfinding.EndergeticFlyingPathNavigator;
import com.minecraftabnormals.endergetic.api.entity.util.EntityItemStackHelper;
import com.minecraftabnormals.endergetic.common.blocks.CorrockBlock.DimensionTypeAccessor;
import com.minecraftabnormals.endergetic.common.entities.bolloom.BolloomFruitEntity;
import com.minecraftabnormals.endergetic.common.entities.booflo.ai.AdolescentAttackGoal;
import com.minecraftabnormals.endergetic.common.entities.booflo.ai.AdolescentEatGoal;
import com.minecraftabnormals.endergetic.common.entities.booflo.ai.BoofloNearestAttackableTargetGoal;
import com.minecraftabnormals.endergetic.core.registry.EEEntities;
import com.minecraftabnormals.endergetic.core.registry.EEItems;

import net.minecraft.block.BlockState;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathType;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.GameRules;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author - SmellyModder(Luke Tonon)
 */
public class BoofloAdolescentEntity extends EndimatedEntity {
	private static final DataParameter<Boolean> MOVING = EntityDataManager.createKey(BoofloAdolescentEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> HAS_FRUIT = EntityDataManager.createKey(BoofloAdolescentEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> DESCENTING = EntityDataManager.createKey(BoofloAdolescentEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> EATING = EntityDataManager.createKey(BoofloAdolescentEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> HUNGRY = EntityDataManager.createKey(BoofloAdolescentEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> EATEN = EntityDataManager.createKey(BoofloAdolescentEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> WANTS_TO_GROW = EntityDataManager.createKey(BoofloAdolescentEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Float> FALL_SPEED = EntityDataManager.createKey(BoofloAdolescentEntity.class, DataSerializers.FLOAT);
	private static final DataParameter<Integer> BOOF_BOOST_COOLDOWN = EntityDataManager.createKey(BoofloAdolescentEntity.class, DataSerializers.VARINT);
	public static final Endimation BOOF_ANIMATION = new Endimation(10);
	public static final Endimation EATING_ANIMATION = new Endimation(10);
	private Entity boofloAttackTarget;
	public int growingAge;
	public int forcedAge;
	public int forcedAgeTimer;
	private float prevTailAnimation;
	private float tailAnimation;
	private float tailSpeed;
	private float prevSwimmingAnimation;
	private float swimmingAnimation;
	private float swimmingAnimationSpeed;
	public boolean wasBred;
	
	public BoofloAdolescentEntity(EntityType<? extends BoofloAdolescentEntity> type, World worldIn) {
		super(type, worldIn);
		this.moveController = new BoofloAdolescentEntity.BoofloAdolescentMoveController(this);
		this.tailAnimation = this.rand.nextFloat();
		this.prevTailAnimation = this.tailAnimation;
	}
	
	@Override
	protected void registerData() {
		super.registerData();
		this.dataManager.register(MOVING, false);
		this.dataManager.register(HAS_FRUIT, false);
		this.dataManager.register(DESCENTING, false);
		this.dataManager.register(EATING, false);
		this.dataManager.register(HUNGRY, true);
		this.dataManager.register(EATEN, false);
		this.dataManager.register(WANTS_TO_GROW, false);
		this.dataManager.register(FALL_SPEED, 0.0F);
		this.dataManager.register(BOOF_BOOST_COOLDOWN, 0);
	}
	
	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new AdolescentEatGoal(this));
		this.goalSelector.addGoal(4, new AdolescentAttackGoal(this, 1.1D, true));
		this.goalSelector.addGoal(6, new BoofloAdolescentEntity.RandomFlyingGoal(this, 1.1D, 5));
		
		this.targetSelector.addGoal(4, new BoofloNearestAttackableTargetGoal<>(this, BolloomFruitEntity.class, true));
	}
	
	@Override
	public void travel(Vector3d vec3d) {
		if (this.isServerWorld() && !this.isInWater()) {
			this.moveRelative(0.015F, vec3d);
			this.move(MoverType.SELF, this.getMotion());
			this.setMotion(this.getMotion().scale(0.9D));
			this.setMotion(this.getMotion().subtract(0.0D, 0.005D * this.getFallSpeed(), 0.0D));
		} else {
			super.travel(vec3d);
		}
	}
	
	@Override
	protected PathNavigator createNavigator(World worldIn) {
		return new EndergeticFlyingPathNavigator(this, worldIn) { 
			
			@Override
			public boolean canEntityStandOnPos(BlockPos pos) {
				return !this.entity.func_233570_aj_();
			}
			
		};
	}
	
	protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
		return sizeIn.height * 0.65F;
	}
	
	@Override
	public void tick() {
		super.tick();
		
		if (this.isEndimationPlaying(BoofloAdolescentEntity.EATING_ANIMATION) && this.getAnimationTick() == 9) {
			if (this.world instanceof ServerWorld) {
				((ServerWorld)this.world).spawnParticle(new ItemParticleData(ParticleTypes.ITEM, new ItemStack(EEItems.BOLLOOM_FRUIT.get())), this.getPosX(), this.getPosY() + (double)this.getHeight() / 1.5D, this.getPosZ(), 10, (double)(this.getWidth() / 4.0F), (double)(this.getHeight() / 4.0F), (double)(this.getWidth() / 4.0F), 0.05D);
			}
			this.playSound(SoundEvents.ENTITY_GENERIC_EAT, 1.0F, 1.0F);
		}
	}
	
	@Override
	public void livingTick() {
		super.livingTick();
		
		if (this.getRidingEntity() == null) this.setFallSpeed(this.getFallSpeed() + 0.1F);
		
		if (this.getRNG().nextInt(50000) < 10 && !this.isHungry() && !this.hasFruit()) {
			this.setHungry(true);
		}
		
		if (this.world.isRemote) {	
			this.prevTailAnimation = this.tailAnimation;
			this.prevSwimmingAnimation = this.swimmingAnimation;
			if (!this.isInWater()) {
				this.tailSpeed = 1.0F;
				this.swimmingAnimationSpeed = 1.0F;
			} else if (this.isMoving()) {
				if (this.tailSpeed < 0.5F) {
					this.tailSpeed = 1.0F;
				} else {
					this.tailSpeed += (0.25F - this.tailSpeed) * 0.1F;
				}
			} else {
				this.tailSpeed += (0.1875F - this.tailSpeed) * 0.1F;
			}
			if (this.getFallSpeed() > 0.0F) {
				if (this.swimmingAnimationSpeed < 0.5F) {
					this.swimmingAnimationSpeed = 1.0F;
				} else {
					this.swimmingAnimationSpeed += ((this.swimmingAnimationSpeed * 2.5F) - this.getFallSpeed()) * 0.1F;
				}
			} else {
				this.swimmingAnimationSpeed = 1.0F;
			}
			this.tailAnimation += this.tailSpeed;
			this.swimmingAnimation += this.swimmingAnimationSpeed;
		}
		
		if (this.getBoofBoostCooldown() > 0) {
			this.setBoofBoostCooldown(this.getBoofBoostCooldown() - 1);
		}
		
		if ((this.onGround || this.isPassenger()) && this.doesWantToGrow() && this.world.hasNoCollisions(this.getBoundingBox().grow(2.0F, 0.0F, 2.0F))) {
			this.growUp();
		}
		
		if (!this.isWorldRemote() && ((!this.isDescenting() && !this.isEating()) && this.getBoofBoostCooldown() <= 0 && (this.onGround || this.areEyesInFluid(FluidTags.WATER)))) {
			this.addVelocity(-MathHelper.sin((float) (this.rotationYaw * Math.PI / 180.0F)) * (5 * (rand.nextFloat() + 0.1F)) * 0.1F, (rand.nextFloat() * 0.45F) + 0.65F, MathHelper.cos((float) (this.rotationYaw * Math.PI / 180.0F)) * (5 * (rand.nextFloat() + 0.1F)) * 0.1F);
			this.setPlayingEndimation(BOOF_ANIMATION);
			NetworkUtil.setPlayingAnimationMessage(this, BOOF_ANIMATION);
			this.setFallSpeed(0.0F);
			//Fixes super boosting underwater
			if (this.eyesInWater) {
				this.setBoofBoostCooldown(20);
			}
		}
		
		//Helps them not fall off the edge
		if ((this.getBoofBoostCooldown() <= 0 && !this.onGround) && this.world.func_230315_m_() == DimensionTypeAccessor.THE_END && !this.isSafePos(this.func_233580_cy_(), 3)) {
			this.setBoofBoostCooldown(20);
			this.setFallSpeed(0.0F);
			
			if (this.getPosY() <= 50) {
				this.addVelocity(-MathHelper.sin((float) (this.rotationYaw * Math.PI / 180.0F)) * (5 * (rand.nextFloat() + 0.1F)) * 0.1F, (rand.nextFloat() * 0.45F) + 0.65F, MathHelper.cos((float) (this.rotationYaw * Math.PI / 180.0F)) * (5 * (rand.nextFloat() + 0.1F)) * 0.1F);
				this.setPlayingEndimation(BOOF_ANIMATION);
			}
		}
		
		if (!this.onGround && this.world.func_230315_m_() == DimensionTypeAccessor.THE_END && !this.isSafePos(this.func_233580_cy_(), 3) && !this.isWorldRemote()) {
			this.addVelocity(-MathHelper.sin((float) (this.rotationYaw * Math.PI / 180.0F)) * 0.01F, 0, MathHelper.cos((float) (this.rotationYaw * Math.PI / 180.0F)) * 0.01F);
		}
		
		if (this.getBoofloAttackTarget() != null && this.canEntityBeSeen(this.getBoofloAttackTarget())) {
			this.rotationYaw = this.rotationYawHead;
		}
		
		if (this.world.isRemote) {
			if (this.forcedAgeTimer > 0) {
				if (this.forcedAgeTimer % 4 == 0) {
					this.world.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getPosX() + (this.rand.nextFloat() * this.getWidth() * 2.0F) - this.getWidth(), this.getPosY() + 0.5D + (this.rand.nextFloat() * this.getHeight()), this.getPosZ() + (this.rand.nextFloat() * this.getWidth() * 2.0F) - this.getWidth(), 0.0D, 0.0D, 0.0D);
				}

				this.forcedAgeTimer--;
			}
		} else if (this.isAlive() && this.hasEaten()) {
			int growingAge = this.getGrowingAge();
			if (growingAge < 0) {
				growingAge++;
	            this.setGrowingAge(growingAge);
			} else if (growingAge > 0) {
				growingAge--;
				this.setGrowingAge(growingAge);
			}
		}
	}
	
	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(EEItems.BOOFLO_SPAWN_EGG.get());
	}
	
	@Override
	public boolean onLivingFall(float distance, float damageMultiplier) {
		return false;
	}

	@Override
	protected void updateFallState(double y, boolean onGroundIn, BlockState state, BlockPos pos) {}
	
	@Override
	public void onDeath(DamageSource cause) {
		if (this.hasFruit() && this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
			this.dropFruit();
		}
		super.onDeath(cause);
	}
	
	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		compound.putBoolean("Moving", this.isMoving());
		compound.putBoolean("HasFruit", this.hasFruit());
		compound.putBoolean("Descenting", this.isDescenting());
		compound.putBoolean("IsEating", this.isEating());
		compound.putBoolean("IsHungry", this.isHungry());
		compound.putBoolean("HasEaten", this.hasEaten());
		compound.putBoolean("WantsToGrow", this.doesWantToGrow());
		compound.putFloat("FallSpeed", this.getFallSpeed());
		compound.putInt("BoofBoostCooldown", this.getBoofBoostCooldown());
		compound.putInt("Age", this.getGrowingAge());
		compound.putInt("ForcedAge", this.forcedAge);
		compound.putBoolean("WasBred", this.wasBred);
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		this.setMoving(compound.getBoolean("Moving"));
		this.setHasFruit(compound.getBoolean("HasFruit"));
		this.setDescenting(compound.getBoolean("Descenting"));
		this.setEating(compound.getBoolean("IsEating"));
		this.setHungry(compound.getBoolean("IsHungry"));
		this.setEaten(compound.getBoolean("HasEaten"));
		this.setWantsToGrow(compound.getBoolean("WantsToGrow"));
		this.setFallSpeed(compound.getFloat("FallSpeed"));
		this.setBoofBoostCooldown(compound.getInt("BoofBoostCooldown"));
		this.setGrowingAge(compound.getInt("Age"));
		this.forcedAge = compound.getInt("ForcedAge");
		this.wasBred = compound.getBoolean("WasBred");
	}
	
	public boolean isMoving() {
		return this.dataManager.get(MOVING);
	}

	public void setMoving(boolean moving) {
		this.dataManager.set(MOVING, moving);
	}
	
	public boolean hasFruit() {
		return this.dataManager.get(HAS_FRUIT);
	}

	public void setHasFruit(boolean hasFruit) {
		this.dataManager.set(HAS_FRUIT, hasFruit);
	}
	
	public boolean isDescenting() {
		return this.dataManager.get(DESCENTING);
	}

	public void setDescenting(boolean descenting) {
		this.dataManager.set(DESCENTING, descenting);
	}
	
	public boolean isEating() {
		return this.dataManager.get(EATING);
	}

	public void setEating(boolean eating) {
		this.dataManager.set(EATING, eating);
	}
	
	public boolean isHungry() {
		return this.dataManager.get(HUNGRY);
	}

	public void setHungry(boolean hungry) {
		this.dataManager.set(HUNGRY, hungry);
	}
	
	public boolean hasEaten() {
		return this.dataManager.get(EATEN);
	}

	public void setEaten(boolean eaten) {
		this.dataManager.set(EATEN, eaten);
	}
	
	public boolean doesWantToGrow() {
		return this.dataManager.get(WANTS_TO_GROW);
	}

	public void setWantsToGrow(boolean wantsToGrow) {
		this.dataManager.set(WANTS_TO_GROW, wantsToGrow);
	}
	
	public float getFallSpeed() {
		return this.dataManager.get(FALL_SPEED);
	}

	public void setFallSpeed(float speed) {
		this.dataManager.set(FALL_SPEED, speed);
	}
	
	public int getBoofBoostCooldown() {
		return this.dataManager.get(BOOF_BOOST_COOLDOWN);
	}

	public void setBoofBoostCooldown(int ticks) {
		this.dataManager.set(BOOF_BOOST_COOLDOWN, ticks);
	}
	
	@Nullable
	public Entity getBoofloAttackTarget() {
		return this.boofloAttackTarget;
	}

	public void setBoofloAttackTarget(@Nullable Entity entity) {
		this.boofloAttackTarget = entity;
	}
	
	public int getGrowingAge() {
		if (this.world.isRemote) {
			return -1;
		} else {
			return this.growingAge;
		}
	}
	
	public void setGrowingAge(int age) {
		int oldAge = this.growingAge;
		this.growingAge = age;
		if ((oldAge < 0 && age >= 0 || oldAge > 0 && age < 0) && this.hasEaten()) {
			this.setWantsToGrow(true);
		}
	}
	
	public void addGrowth(int growth) {
		this.ageUp(growth, false);
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

		if  (this.getGrowingAge() == 0) {
			this.setGrowingAge(this.forcedAge);
		}
	}
	
	public void growUp() {
		if (!this.world.isRemote && this.isAlive()) {
			this.entityDropItem(EEItems.BOOFLO_HIDE.get(), 1);
			
			BoofloEntity booflo = EEEntities.BOOFLO.get().create(this.world);
			booflo.setLocationAndAngles(this.getPosX(), this.getPosY(), this.getPosZ(), this.rotationYaw, this.rotationPitch);
			
			if (this.hasCustomName()) {
    			booflo.setCustomName(this.getCustomName());
    			booflo.setCustomNameVisible(this.isCustomNameVisible());
    		}
			
			if (this.getLeashed()) {
				booflo.setLeashHolder(this.getLeashHolder(), true);
				this.clearLeashed(true, false);
			}
			
			if (this.getRidingEntity() != null) {
				booflo.startRiding(this.getRidingEntity());
			}
			
			booflo.wasBred = this.wasBred;
			booflo.setHealth(booflo.getMaxHealth());
			this.world.addEntity(booflo);
			
			this.remove();
		}
	}

	public void growDown() {
		if (!this.world.isRemote && this.isAlive()) {
			BoofloBabyEntity boofloBaby = EEEntities.BOOFLO_BABY.get().create(this.world);
			boofloBaby.setLocationAndAngles(this.getPosX(), this.getPosY(), this.getPosZ(), this.rotationYaw, this.rotationPitch);

			if (this.hasCustomName()) {
				boofloBaby.setCustomName(this.getCustomName());
				boofloBaby.setCustomNameVisible(this.isCustomNameVisible());
			}

			if (this.getLeashed()) {
				boofloBaby.setLeashHolder(this.getLeashHolder(), true);
				this.clearLeashed(true, false);
			}

			if (this.getRidingEntity() != null) {
				boofloBaby.startRiding(this.getRidingEntity());
			}

			boofloBaby.wasBred = this.wasBred;
			boofloBaby.setHealth(boofloBaby.getMaxHealth());
			this.world.addEntity(boofloBaby);

			this.remove();
		}
	}
	
	@OnlyIn(Dist.CLIENT)	
	public float getTailAnimation(float ptc) {
		return MathHelper.lerp(ptc, this.prevTailAnimation, this.tailAnimation);
	}
	
	@OnlyIn(Dist.CLIENT)	
	public float getSwimmingAnimation(float ptc) {
		return MathHelper.lerp(ptc, this.prevSwimmingAnimation, this.swimmingAnimation);
	}
	
	public boolean isSafePos(BlockPos pos, int muliplier) {
		BlockPos newPos = pos;
		for (int y = 0; y < 10 * muliplier; y++) {
			newPos = newPos.down(y);
			BlockState state = this.world.getBlockState(newPos);
			if (state.isSolid() || (!state.getFluidState().isEmpty() && !state.getFluidState().isTagged(FluidTags.LAVA))) {
				return true;
			}
		}
		return false;
	}
	
	public void dropFruit() {
		if (!this.isWorldRemote()) {
			this.entityDropItem(EEItems.BOLLOOM_FRUIT.get());
			this.setHasFruit(false);
		}
	}
	
	public boolean isPlayerNear() {
		return this.world.getEntitiesWithinAABB(PlayerEntity.class, this.getBoundingBox().grow(2.0F), BoofloEntity.IS_SCARED_BY).size() > 0;
	}
	
	@Override
	public boolean isInvulnerableTo(DamageSource source) {
		return source == DamageSource.IN_WALL || source == DamageSource.FLY_INTO_WALL || super.isInvulnerableTo(source);
	}
	
	@Override
	protected ActionResultType func_230254_b_(PlayerEntity player, Hand hand) {
		ItemStack itemstack = player.getHeldItem(hand);
		Item item = itemstack.getItem();
		
		if (item instanceof SpawnEggItem && ((SpawnEggItem)item).hasType(itemstack.getTag(), EEEntities.BOOFLO.get())) {
			if (!this.world.isRemote) {
				BoofloBabyEntity baby = EEEntities.BOOFLO_BABY.get().create(this.world);
				baby.setGrowingAge(-24000);
				baby.setLocationAndAngles(this.getPosX(), this.getPosY(), this.getPosZ(), 0.0F, 0.0F);
				this.world.addEntity(baby);
				if (itemstack.hasDisplayName()) {
					baby.setCustomName(itemstack.getDisplayName());
				}
				
				EntityItemStackHelper.consumeItemFromStack(player, itemstack);
			}
			return ActionResultType.PASS;
		} else if (item == EEItems.BOLLOOM_FRUIT.get()) {
			EntityItemStackHelper.consumeItemFromStack(player, itemstack);
            this.ageUp((int) ((-this.getGrowingAge() / 20) * 0.1F), true);
            this.setEaten(true);
            return ActionResultType.CONSUME;
		}
		return ActionResultType.PASS;
	}
	
	@Override
	public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, ILivingEntityData spawnDataIn, CompoundNBT dataTag) {
		this.setGrowingAge(-24000);
		return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
	}
	
	@Override
	public Endimation[] getEndimations() {
		return new Endimation[] {
			BOOF_ANIMATION,
			EATING_ANIMATION
		};
	}
	
	@Override
	public boolean canDespawn(double distanceToClosestPlayer) {
		return !this.wasBred;
	}
	
	static class RandomFlyingGoal extends RandomWalkingGoal {
		
		public RandomFlyingGoal(CreatureEntity booflo, double speed, int chance) {
			super(booflo, speed, chance);
		}

		@Nullable
		protected Vector3d getPosition() {
			Vector3d vec3d = RandomPositionGenerator.findRandomTarget(this.creature, 10, 0);
			
			for(int i = 0; vec3d != null && !this.creature.world.getBlockState(new BlockPos(vec3d)).allowsMovement(this.creature.world, new BlockPos(vec3d), PathType.AIR) && i++ < 10; vec3d = RandomPositionGenerator.findRandomTarget(this.creature, 10, 0)) {
				;
			}
			
			return vec3d;
		}
		
		@Override
		public boolean shouldExecute() {
			return super.shouldExecute() && !this.creature.isInWater();
		}
		
		@Override
		public boolean shouldContinueExecuting() {
			return super.shouldContinueExecuting() && !this.creature.isInWater();
		}
		
	}
	
	static class BoofloAdolescentMoveController extends MovementController {
		private final BoofloAdolescentEntity booflo;

		BoofloAdolescentMoveController(BoofloAdolescentEntity booflo) {
			super(booflo);
			this.booflo = booflo;
		}

		public void tick() {
			if (this.action == MovementController.Action.MOVE_TO && !this.booflo.getNavigator().noPath()) {
				Vector3d vec3d = this.booflo.getMoveControllerPathDistance(this.posX, this.posY, this.posZ);
				
				this.booflo.rotationYaw = this.limitAngle(this.booflo.rotationYaw, this.booflo.getTargetAngleForPathDistance(vec3d), 10.0F);
				this.booflo.renderYawOffset = this.booflo.rotationYaw;
				this.booflo.rotationYawHead = this.booflo.rotationYaw;
				
				float f1 = (float) (2 * this.booflo.getAttribute(Attributes.MOVEMENT_SPEED).getValue());
				float f2 = MathHelper.lerp(0.125F, this.booflo.getAIMoveSpeed(), f1);
				
				this.booflo.setAIMoveSpeed(f2);
				
				this.booflo.setMoving(true);
			} else {
				this.booflo.setMoving(false);
			}
		}
	}
}