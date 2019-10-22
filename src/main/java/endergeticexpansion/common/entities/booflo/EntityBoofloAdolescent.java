package endergeticexpansion.common.entities.booflo;

import javax.annotation.Nullable;

import endergeticexpansion.api.endimator.Endimation;
import endergeticexpansion.api.entity.util.EndergeticFlyingPathNavigator;
import endergeticexpansion.api.endimator.EndimatedEntity;
import endergeticexpansion.api.util.NetworkUtil;
import endergeticexpansion.common.entities.bolloom.EntityBolloomFruit;
import endergeticexpansion.common.entities.booflo.ai.AdolescentAttackGoal;
import endergeticexpansion.common.entities.booflo.ai.BoofloNearestAttackableTargetGoal;
import net.minecraft.block.BlockState;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.controller.LookController;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathType;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EntityBoofloAdolescent extends EndimatedEntity {
	private static final DataParameter<Boolean> MOVING = EntityDataManager.createKey(EntityBoofloAdolescent.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> WAS_ON_GROUND = EntityDataManager.createKey(EntityBoofloAdolescent.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Float> FALL_SPEED = EntityDataManager.createKey(EntityBoofloAdolescent.class, DataSerializers.FLOAT);
	private static final DataParameter<Integer> ATTACK_COOLDOWN = EntityDataManager.createKey(EntityBoofloAdolescent.class, DataSerializers.VARINT);
	public static final Endimation BOOF_ANIMATION = new Endimation(10);
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
		this.lookController = new EntityBoofloAdolescent.BoofloAdolescentLookController(this, 10);
		this.tailAnimation = this.rand.nextFloat();
		this.prevTailAnimation = this.tailAnimation;
	}
	
	@Override
	protected void registerData() {
		super.registerData();
		this.getDataManager().register(MOVING, false);
		this.getDataManager().register(WAS_ON_GROUND, true);
		this.getDataManager().register(FALL_SPEED, 0.0F);
		this.getDataManager().register(ATTACK_COOLDOWN, 0);
	}
	
	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(1.7D);
		this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(16.0D);
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(9.0D);
		this.getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
	}
	
	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(5, new EntityBoofloAdolescent.RandomFlyingGoal(this, 1.1D, 5));
		this.targetSelector.addGoal(5, new BoofloNearestAttackableTargetGoal<>(this, EntityBolloomFruit.class, true));
		this.goalSelector.addGoal(4, new AdolescentAttackGoal(this, 1.1D, true));
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
			
			@SuppressWarnings("deprecation")
			@Override
			public boolean canEntityStandOnPos(BlockPos pos) {
				return this.world.getBlockState(pos).isAir() && !this.entity.onGround;
			}
			
		};
	}
	
	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		compound.putBoolean("Moving", this.isMoving());
		compound.putBoolean("WasOnGround", this.wasOnGround());
		compound.putFloat("FallSpeed", this.getFallSpeed());
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		this.setMoving(compound.getBoolean("Moving"));
		this.setWasOnGround(compound.getBoolean("WasOnGround"));
		this.setFallSpeed(compound.getFloat("FallSpeed"));
	}
	
	public boolean isMoving() {
		return this.getDataManager().get(MOVING);
	}

	public void setMoving(boolean moving) {
		this.getDataManager().set(MOVING, moving);
	}
	
	public boolean wasOnGround() {
		return this.getDataManager().get(WAS_ON_GROUND);
	}

	public void setWasOnGround(boolean wasOnGround) {
		this.getDataManager().set(WAS_ON_GROUND, wasOnGround);
	}
	
	public float getFallSpeed() {
		return this.getDataManager().get(FALL_SPEED);
	}

	public void setFallSpeed(float speed) {
		this.getDataManager().set(FALL_SPEED, speed);
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
	
	protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
		return sizeIn.height * 0.65F;
	}
	
	@Override
	public void livingTick() {
		this.setFallSpeed(this.getFallSpeed() + 0.1F);
		
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
		
		if((this.onGround || this.areEyesInFluid(FluidTags.WATER)) && this.isAnimationPlaying(BLANK_ANIMATION) && !this.isWorldRemote()) {
			this.addVelocity(-MathHelper.sin((float) (this.rotationYaw * Math.PI / 180.0F)) * (5 * (rand.nextFloat() + 0.1F)) * 0.1F, (rand.nextFloat() * 0.45F) + 0.65F, MathHelper.cos((float) (this.rotationYaw * Math.PI / 180.0F)) * (5 * (rand.nextFloat() + 0.1F)) * 0.1F);
			NetworkUtil.setPlayingAnimationMessage(this, BOOF_ANIMATION);
			this.setFallSpeed(0.0F);
		}
		
		super.livingTick();
	}
	
	@Override
	public void fall(float distance, float damageMultiplier) {}

	@Override
	protected void updateFallState(double y, boolean onGroundIn, BlockState state, BlockPos pos) {}
	
	@Override
	public Endimation[] getAnimations() {
		return new Endimation[] {
			BOOF_ANIMATION 
		};
	}
	
	static class RandomFlyingGoal extends RandomWalkingGoal {
		
		public RandomFlyingGoal(CreatureEntity booflo, double speed, int chance) {
			super(booflo, speed, chance);
		}

		@Nullable
		protected Vec3d getPosition() {
			Vec3d vec3d = RandomPositionGenerator.findRandomTarget(this.creature, 10, 2);
			
			for(int i = 0; vec3d != null && !this.creature.world.getBlockState(new BlockPos(vec3d)).allowsMovement(this.creature.world, new BlockPos(vec3d), PathType.AIR) && i++ < 10; vec3d = RandomPositionGenerator.findRandomTarget(this.creature, 10, 2)) {
				;
			}
			
			return vec3d;
		}
		
		@Override
		public boolean shouldExecute() {
			return super.shouldExecute() && this.creature.getNavigator().noPath() && !this.creature.isInWater();
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
	
	class BoofloAdolescentLookController extends LookController {
		private final int angleLimit;

		public BoofloAdolescentLookController(EntityBoofloAdolescent booflo, int angleLimit) {
			super(booflo);
			this.angleLimit = angleLimit;
		}

		public void tick() {
			if(this.isLooking) {
				this.isLooking = false;
				this.mob.rotationYawHead = this.func_220675_a(this.mob.rotationYawHead, this.func_220678_h() + 20.0F, this.deltaLookYaw);
				this.mob.rotationPitch = this.func_220675_a(this.mob.rotationPitch, this.func_220677_g() + 10.0F, this.deltaLookPitch);
			} else {
				if(this.mob.getNavigator().noPath()) {
					this.mob.rotationPitch = this.func_220675_a(this.mob.rotationPitch, 0.0F, 5.0F);
				}
				this.mob.rotationYawHead = this.func_220675_a(this.mob.rotationYawHead, this.mob.renderYawOffset, this.deltaLookYaw);
			}

			float wrappedDegrees = MathHelper.wrapDegrees(this.mob.rotationYawHead - this.mob.renderYawOffset);
			if (wrappedDegrees < (float)(-this.angleLimit)) {
				this.mob.renderYawOffset -= 4.0F;
			} else if (wrappedDegrees > (float)this.angleLimit) {
				this.mob.renderYawOffset += 4.0F;
			}
		}
	}
}