package endergeticexpansion.common.entities.booflo;

import java.util.function.Predicate;

import javax.annotation.Nullable;

import endergeticexpansion.api.endimator.Endimation;
import endergeticexpansion.api.entity.util.EndergeticFlyingPathNavigator;
import endergeticexpansion.api.endimator.EndimatedEntity;
import endergeticexpansion.api.util.NetworkUtil;
import endergeticexpansion.common.entities.bolloom.EntityBolloomFruit;
import endergeticexpansion.common.entities.booflo.ai.AdolescentAttackGoal;
import endergeticexpansion.common.entities.booflo.ai.AdolescentEatGoal;
import endergeticexpansion.common.entities.booflo.ai.BoofloNearestAttackableTargetGoal;
import endergeticexpansion.core.registry.EEItems;
import net.minecraft.block.BlockState;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathType;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author - SmellyModder(Luke Tonon)
 */
public class EntityBoofloAdolescent extends EndimatedEntity {
	private static final DataParameter<Boolean> MOVING = EntityDataManager.createKey(EntityBoofloAdolescent.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> HAS_FRUIT = EntityDataManager.createKey(EntityBoofloAdolescent.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> DESCENTING = EntityDataManager.createKey(EntityBoofloAdolescent.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> EATING = EntityDataManager.createKey(EntityBoofloAdolescent.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> HUNGRY = EntityDataManager.createKey(EntityBoofloAdolescent.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Float> FALL_SPEED = EntityDataManager.createKey(EntityBoofloAdolescent.class, DataSerializers.FLOAT);
	private static final DataParameter<Integer> BOOF_BOOST_COOLDOWN = EntityDataManager.createKey(EntityBoofloAdolescent.class, DataSerializers.VARINT);
	public static final Predicate<Entity> IS_SCARED_BY = (entity) -> {
		return !(entity instanceof PlayerEntity) || !entity.isSpectator() && !((PlayerEntity)entity).isCreative();
	};
	public static final Endimation BOOF_ANIMATION = new Endimation(10);
	public static final Endimation EATING_ANIMATION = new Endimation(10);
	private Entity boofloAttackTarget;
	private float prevTailAnimation;
	private float tailAnimation;
	private float tailSpeed;
	private float prevSwimmingAnimation;
	private float swimmingAnimation;
	private float swimmingAnimationSpeed;
	
	public EntityBoofloAdolescent(EntityType<? extends EntityBoofloAdolescent> type, World worldIn) {
		super(type, worldIn);
		this.moveController = new EntityBoofloAdolescent.BoofloAdolescentMoveController(this);
		this.tailAnimation = this.rand.nextFloat();
		this.prevTailAnimation = this.tailAnimation;
	}
	
	@Override
	protected void registerData() {
		super.registerData();
		this.getDataManager().register(MOVING, false);
		this.getDataManager().register(HAS_FRUIT, false);
		this.getDataManager().register(DESCENTING, false);
		this.getDataManager().register(EATING, false);
		this.getDataManager().register(HUNGRY, true);
		this.getDataManager().register(FALL_SPEED, 0.0F);
		this.getDataManager().register(BOOF_BOOST_COOLDOWN, 0);
	}
	
	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(1.7D);
		this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(25.0D);
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(9.0D);
	}
	
	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new AdolescentEatGoal(this));
		this.goalSelector.addGoal(4, new AdolescentAttackGoal(this, 1.1D, true));
		this.goalSelector.addGoal(6, new EntityBoofloAdolescent.RandomFlyingGoal(this, 1.1D, 5));
		
		this.targetSelector.addGoal(4, new BoofloNearestAttackableTargetGoal<>(this, EntityBolloomFruit.class, true));
	}
	
	@Override
	public void travel(Vec3d vec3d) {
		if(this.isServerWorld() && !this.isInWater()) {
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
				return !this.entity.onGround;
			}
			
		};
	}
	
	protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
		return sizeIn.height * 0.65F;
	}
	
	@Override
	public void tick() {
		super.tick();
		
		if(this.getPlayingAnimation() == EATING_ANIMATION && this.getAnimationTick() == 9) {
			if(this.world instanceof ServerWorld) {
				((ServerWorld)this.world).spawnParticle(new ItemParticleData(ParticleTypes.ITEM, new ItemStack(EEItems.BOLLOOM_FRUIT)), this.posX, this.posY + (double)this.getHeight() / 1.5D, this.posZ, 10, (double)(this.getWidth() / 4.0F), (double)(this.getHeight() / 4.0F), (double)(this.getWidth() / 4.0F), 0.05D);
			}
			this.playSound(SoundEvents.ENTITY_GENERIC_EAT, 1.0F, 1.0F);
		}
	}
	
	@Override
	public void livingTick() {
		super.livingTick();
		
		if(this.getRidingEntity() == null) this.setFallSpeed(this.getFallSpeed() + 0.1F);
		
		if(!this.isHungry() && this.getRNG().nextInt(300) == 0) {
			this.setHungry(true);
		}
		
		if(this.world.isRemote) {	
			this.prevTailAnimation = this.tailAnimation;
			this.prevSwimmingAnimation = this.swimmingAnimation;
			if(!this.isInWater()) {
				this.tailSpeed = 1.0F;
				this.swimmingAnimationSpeed = 1.0F;
			} else if(this.isMoving()) {
				if(this.tailSpeed < 0.5F) {
					this.tailSpeed = 1.0F;
				} else {
					this.tailSpeed += (0.25F - this.tailSpeed) * 0.1F;
				}
			} else {
				this.tailSpeed += (0.1875F - this.tailSpeed) * 0.1F;
			}
			if(this.getFallSpeed() > 0.0F) {
				if(this.swimmingAnimationSpeed < 0.5F) {
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
		
		if(this.getBoofBoostCooldown() > 0) {
			this.setBoofBoostCooldown(this.getBoofBoostCooldown() - 1);
		}
		
		if((!this.isDescenting() && !this.isEating()) && this.getBoofBoostCooldown() <= 0 && (this.onGround || this.areEyesInFluid(FluidTags.WATER)) && !this.isWorldRemote()) {
			this.addVelocity(-MathHelper.sin((float) (this.rotationYaw * Math.PI / 180.0F)) * (5 * (rand.nextFloat() + 0.1F)) * 0.1F, (rand.nextFloat() * 0.45F) + 0.65F, MathHelper.cos((float) (this.rotationYaw * Math.PI / 180.0F)) * (5 * (rand.nextFloat() + 0.1F)) * 0.1F);
			NetworkUtil.setPlayingAnimationMessage(this, BOOF_ANIMATION);
			this.setFallSpeed(0.0F);
			//Fixes super boosting underwater
			if(this.eyesInWater) {
				this.setBoofBoostCooldown(20);
			}
		}
		
		//Helps them not fall off the edge
		if((this.getBoofBoostCooldown() <= 0 && !this.onGround) && this.dimension == DimensionType.THE_END && !this.isSafePos(getPosition(), 3) && !this.isWorldRemote()) {
			this.setBoofBoostCooldown(20);
			this.setFallSpeed(0.0F);
			
			if(this.posY <= 50) {
				this.addVelocity(-MathHelper.sin((float) (this.rotationYaw * Math.PI / 180.0F)) * (5 * (rand.nextFloat() + 0.1F)) * 0.1F, (rand.nextFloat() * 0.45F) + 0.65F, MathHelper.cos((float) (this.rotationYaw * Math.PI / 180.0F)) * (5 * (rand.nextFloat() + 0.1F)) * 0.1F);
				NetworkUtil.setPlayingAnimationMessage(this, BOOF_ANIMATION);
			}
		}
		
		if(!this.onGround && this.dimension == DimensionType.THE_END && !this.isSafePos(getPosition(), 3) && !this.isWorldRemote()) {
			this.addVelocity(-MathHelper.sin((float) (this.rotationYaw * Math.PI / 180.0F)) * 0.01F, 0, MathHelper.cos((float) (this.rotationYaw * Math.PI / 180.0F)) * 0.01F);
		}
		
		if(this.getBoofloAttackTarget() != null && this.canEntityBeSeen(this.getBoofloAttackTarget())) {
			this.rotationYaw = this.rotationYawHead;
		}
	}
	
	@Override
	public void fall(float distance, float damageMultiplier) {}

	@Override
	protected void updateFallState(double y, boolean onGroundIn, BlockState state, BlockPos pos) {}
	
	@Override
	public void onDeath(DamageSource cause) {
		if(this.hasFruit() && this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
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
		compound.putFloat("FallSpeed", this.getFallSpeed());
		compound.putInt("BoofBoostCooldown", this.getBoofBoostCooldown());
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		this.setMoving(compound.getBoolean("Moving"));
		this.setHasFruit(compound.getBoolean("HasFruit"));
		this.setDescenting(compound.getBoolean("Descenting"));
		this.setEating(compound.getBoolean("IsEating"));
		this.setHungry(compound.getBoolean("IsHungry"));
		this.setFallSpeed(compound.getFloat("FallSpeed"));
		this.setBoofBoostCooldown(compound.getInt("BoofBoostCooldown"));
	}
	
	public boolean isMoving() {
		return this.getDataManager().get(MOVING);
	}

	public void setMoving(boolean moving) {
		this.getDataManager().set(MOVING, moving);
	}
	
	public boolean hasFruit() {
		return this.getDataManager().get(HAS_FRUIT);
	}

	public void setHasFruit(boolean hasFruit) {
		this.getDataManager().set(HAS_FRUIT, hasFruit);
	}
	
	public boolean isDescenting() {
		return this.getDataManager().get(DESCENTING);
	}

	public void setDescenting(boolean descenting) {
		this.getDataManager().set(DESCENTING, descenting);
	}
	
	public boolean isEating() {
		return this.getDataManager().get(EATING);
	}

	public void setEating(boolean eating) {
		this.getDataManager().set(EATING, eating);
	}
	
	public boolean isHungry() {
		return this.getDataManager().get(HUNGRY);
	}

	public void setHungry(boolean hungry) {
		this.getDataManager().set(HUNGRY, hungry);
	}
	
	public float getFallSpeed() {
		return this.getDataManager().get(FALL_SPEED);
	}

	public void setFallSpeed(float speed) {
		this.getDataManager().set(FALL_SPEED, speed);
	}
	
	public int getBoofBoostCooldown() {
		return this.getDataManager().get(BOOF_BOOST_COOLDOWN);
	}

	public void setBoofBoostCooldown(int ticks) {
		this.getDataManager().set(BOOF_BOOST_COOLDOWN, ticks);
	}
	
	@Nullable
	public Entity getBoofloAttackTarget() {
		return this.boofloAttackTarget;
	}

	public void setBoofloAttackTarget(@Nullable Entity entity) {
		this.boofloAttackTarget = entity;
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
		for(int y = 0; y < 10 * muliplier; y++) {
			newPos = newPos.down(y);
			BlockState state = this.world.getBlockState(newPos);
			if(state.isSolid() || (!state.getFluidState().isEmpty() && !state.getFluidState().isTagged(FluidTags.LAVA))) {
				return true;
			}
		}
		return false;
	}
	
	public void dropFruit() {
		if(!this.world.isRemote) {
			this.entityDropItem(EEItems.BOLLOOM_FRUIT);
			this.setHasFruit(false);
		}
	}
	
	public boolean isPlayerNear() {
		return this.world.getEntitiesWithinAABB(PlayerEntity.class, this.getBoundingBox().grow(2.0F), IS_SCARED_BY).size() > 0;
	}
	
	@Override
	public Endimation[] getAnimations() {
		return new Endimation[] {
			BOOF_ANIMATION,
			EATING_ANIMATION
		};
	}
	
	static class RandomFlyingGoal extends RandomWalkingGoal {
		
		public RandomFlyingGoal(CreatureEntity booflo, double speed, int chance) {
			super(booflo, speed, chance);
		}

		@Nullable
		protected Vec3d getPosition() {
			Vec3d vec3d = RandomPositionGenerator.findRandomTarget(this.creature, 10, 0);
			
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
		private final EntityBoofloAdolescent booflo;

		BoofloAdolescentMoveController(EntityBoofloAdolescent booflo) {
			super(booflo);
			this.booflo = booflo;
		}

		public void tick() {
			if(this.action == MovementController.Action.MOVE_TO && !this.booflo.getNavigator().noPath()) {
				Vec3d vec3d = this.booflo.getMoveControllerPathDistance(this.posX, this.posY, this.posZ);
				
				this.booflo.rotationYaw = this.limitAngle(this.booflo.rotationYaw, this.booflo.getTargetAngleForPathDistance(vec3d), 10.0F);
				this.booflo.renderYawOffset = this.booflo.rotationYaw;
				this.booflo.rotationYawHead = this.booflo.rotationYaw;
				
				float f1 = (float)(2 * this.booflo.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue());
				float f2 = MathHelper.lerp(0.125F, this.booflo.getAIMoveSpeed(), f1);
				
				this.booflo.setAIMoveSpeed(f2);
				
				this.booflo.setMoving(true);
			} else {
				this.booflo.setMoving(false);
			}
		}
	}
}