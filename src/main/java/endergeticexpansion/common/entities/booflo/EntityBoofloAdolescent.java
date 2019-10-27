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
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author - SmellyModder(Luke Tonon)
 */
public class EntityBoofloAdolescent extends EndimatedEntity {
	private static final DataParameter<Boolean> MOVING = EntityDataManager.createKey(EntityBoofloAdolescent.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Float> FALL_SPEED = EntityDataManager.createKey(EntityBoofloAdolescent.class, DataSerializers.FLOAT);
	private static final DataParameter<Boolean> HAS_FRUIT = EntityDataManager.createKey(EntityBoofloAdolescent.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> BOOF_BOOST_COOLDOWN = EntityDataManager.createKey(EntityBoofloAdolescent.class, DataSerializers.VARINT);
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
		this.tailAnimation = this.rand.nextFloat();
		this.prevTailAnimation = this.tailAnimation;
	}
	
	@Override
	protected void registerData() {
		super.registerData();
		this.getDataManager().register(MOVING, false);
		this.getDataManager().register(HAS_FRUIT, false);
		this.getDataManager().register(FALL_SPEED, 0.0F);
		this.getDataManager().register(BOOF_BOOST_COOLDOWN, 0);
	}
	
	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(1.7D);
		this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(25.0D);
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(9.0D);
		this.getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
	}
	
	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(6, new EntityBoofloAdolescent.RandomFlyingGoal(this, 1.1D, 5));
		this.goalSelector.addGoal(4, new AdolescentAttackGoal(this, 1.1D, true));
		
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
				return ((EntityBoofloAdolescent) this.entity).isSafePos(pos, 1) && !this.entity.onGround;
			}
			
		};
	}
	
	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		compound.putBoolean("Moving", this.isMoving());
		compound.putFloat("FallSpeed", this.getFallSpeed());
		compound.putInt("BoofBoostCooldown", this.getBoofBoostCooldown());
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		this.setMoving(compound.getBoolean("Moving"));
		this.setFallSpeed(compound.getFloat("FallSpeed"));
		this.setBoofBoostCooldown(compound.getInt("BoofBoostCooldown"));
	}
	
	public boolean isMoving() {
		return this.getDataManager().get(MOVING);
	}

	public void setMoving(boolean moving) {
		this.getDataManager().set(MOVING, moving);
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
	
	private boolean isSafePos(BlockPos pos, int muliplier) {
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
	
	protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
		return sizeIn.height * 0.65F;
	}
	
	@Override
	public void livingTick() {
		if(this.getRidingEntity() == null) this.setFallSpeed(this.getFallSpeed() + 0.1F);
		
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
		
		if(this.getBoofBoostCooldown() <= 0 && (this.onGround || this.areEyesInFluid(FluidTags.WATER)) && this.isAnimationPlaying(BLANK_ANIMATION) && !this.isWorldRemote()) {
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
		
		if(this.getBoofloAttackTarget() != null) {
			this.rotationYaw = this.rotationYawHead;
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
			Vec3d vec3d = RandomPositionGenerator.findRandomTarget(this.creature, 10, 0);
			
			for(int i = 0; vec3d != null && !this.creature.world.getBlockState(new BlockPos(vec3d)).allowsMovement(this.creature.world, new BlockPos(vec3d), PathType.AIR) && i++ < 10; vec3d = RandomPositionGenerator.findRandomTarget(this.creature, 10, 0)) {
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
}