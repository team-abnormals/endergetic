package endergeticexpansion.common.entities.booflo;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.controller.LookController;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EntityBoofloBaby extends CreatureEntity {
	private static final DataParameter<Boolean> MOVING = EntityDataManager.createKey(EntityBoofloBaby.class, DataSerializers.BOOLEAN);
	private float prevTailAnimation;
	private float tailAnimation;
	private float tailSpeed;
	
	public EntityBoofloBaby(EntityType<? extends EntityBoofloBaby> type, World worldIn) {
		super(type, worldIn);
		this.moveController = new EntityBoofloBaby.BoofloBabyMoveContoller(this);
		this.lookController = new EntityBoofloBaby.BoofloBabyLookController(this, 10);
		this.tailAnimation = this.rand.nextFloat();
		this.prevTailAnimation = this.tailAnimation;
	}
	
	@Override
	protected void registerData() {
		super.registerData();
		this.getDataManager().register(MOVING, false);
	}
	
	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.85D);
		this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(18.0D);
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(5.0D);
	}
	
	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(5, new EntityBoofloBaby.RandomFlyingGoal(this, 1.1D, 20));
	}
	
	@Override
	protected PathNavigator createNavigator(World worldIn) {
		return new FlyingPathNavigator(this, worldIn) { 
			
			@SuppressWarnings("deprecation")
			@Override
			public boolean canEntityStandOnPos(BlockPos pos) {
				return this.world.getBlockState(pos).isAir();
			}
			
		};
	}
	
	@Override
	public void travel(Vec3d vec3d) {
		if (this.isServerWorld() && !this.isInWater()) {
			this.moveRelative(0.01F, vec3d);
			this.move(MoverType.SELF, this.getMotion());
			this.setMotion(this.getMotion().scale(0.9D));
			if(!this.isMoving()) {
				this.setMotion(this.getMotion().subtract(0, 0.0025D, 0));
			}
		} else {
			super.travel(vec3d);
		}
	}
	
	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		compound.putBoolean("Moving", this.isMoving());
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		this.setMoving(compound.getBoolean("Moving"));
	}
	
	public boolean isMoving() {
		return this.getDataManager().get(MOVING);
	}

	public void setMoving(boolean moving) {
		this.getDataManager().set(MOVING, moving);
	}
	
	@OnlyIn(Dist.CLIENT)	
	public float getTailAnimation(float ptc) {
		return MathHelper.lerp(ptc, this.prevTailAnimation, this.tailAnimation);
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}
	
	public int getVerticalFaceSpeed() {
		return 1;
	}

	public int getHorizontalFaceSpeed() {
		return 1;
	}
	
	@Override
	public void livingTick() {
		if (this.world.isRemote) {
			this.prevTailAnimation = this.tailAnimation;
			if(this.isInWater()) {
				this.tailSpeed = 1.0F;
			} else if(this.isMoving()) {
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
		super.livingTick();
	}

	@Override
	public void fall(float distance, float damageMultiplier) {}

	@Override
	protected void updateFallState(double y, boolean onGroundIn, BlockState state, BlockPos pos) {}
	
	static class RandomFlyingGoal extends RandomWalkingGoal {
		public RandomFlyingGoal(CreatureEntity p_i48937_1_, double p_i48937_2_, int p_i48937_4_) {
			super(p_i48937_1_, p_i48937_2_, p_i48937_4_);
		}

		@Nullable
		protected Vec3d getPosition() {
			Vec3d vec3d = RandomPositionGenerator.findRandomTarget(this.creature, 7, 4);

			for(int i = 0; vec3d != null && !this.creature.world.getBlockState(new BlockPos(vec3d)).allowsMovement(this.creature.world, new BlockPos(vec3d), PathType.WATER) && i++ < 10; vec3d = RandomPositionGenerator.findRandomTarget(this.creature, 7, 4)) {
				;
			}
			
			return vec3d;
		}
	}
	
	static class BoofloBabyMoveContoller extends MovementController {
		private final EntityBoofloBaby booflo;

		public BoofloBabyMoveContoller(EntityBoofloBaby booflo) {
			super(booflo);
			this.booflo = booflo;
		}

		public void tick() {
			if (this.action == MovementController.Action.MOVE_TO && !this.booflo.getNavigator().noPath()) {
				Vec3d vec3d = new Vec3d(this.posX - this.booflo.posX, this.posY - this.booflo.posY, this.posZ - this.booflo.posZ);
				double d0 = vec3d.length();
				double d1 = vec3d.y / d0;
				float f = (float) (MathHelper.atan2(vec3d.z, vec3d.x) * (double) (180F / (float) Math.PI)) - 90F;
				
				this.booflo.rotationYaw = this.limitAngle(this.booflo.rotationYaw, f, 10.0F);
				this.booflo.renderYawOffset = this.booflo.rotationYaw;
				this.booflo.rotationYawHead = this.booflo.rotationYaw;
				
				float f1 = (float)(this.speed * this.booflo.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue());
				float f2 = MathHelper.lerp(0.125F, this.booflo.getAIMoveSpeed(), f1);
				
				this.booflo.setAIMoveSpeed(f2);
				
				double d3 = Math.cos((double)(this.booflo.rotationYaw * ((float)Math.PI / 180F)));
				double d4 = Math.sin((double)(this.booflo.rotationYaw * ((float)Math.PI / 180F)));
				double d5 = Math.sin((double)(this.booflo.ticksExisted + this.booflo.getEntityId()) * 0.75D) * 0.05D;
				
				if (!this.booflo.isInWater()) {
					float f3 = -((float)(MathHelper.atan2(vec3d.y, (double)MathHelper.sqrt(vec3d.x * vec3d.x + vec3d.z * vec3d.z)) * (double)(180F / (float)Math.PI)));
					f3 = MathHelper.clamp(MathHelper.wrapDegrees(f3), -85.0F, 85.0F);
					this.booflo.rotationPitch = this.limitAngle(this.booflo.rotationPitch, f3, 5.0F);
				}
				
				this.booflo.setMotion(this.booflo.getMotion().add(0, d5 * (d4 + d3) * 0.25D + (double)f2 * d1 * 0.02D, 0));
				
				this.booflo.setMoving(true);
			} else {
				this.booflo.setAIMoveSpeed(0F);
				this.booflo.setMoving(false);
			}
		}
	}
	
	class BoofloBabyLookController extends LookController {
		private final int angleLimit;

		public BoofloBabyLookController(EntityBoofloBaby baby, int angleLimit) {
			super(baby);
			this.angleLimit = angleLimit;
		}

		public void tick() {
			if (this.isLooking) {
				this.isLooking = false;
				this.mob.rotationYawHead = this.func_220675_a(this.mob.rotationYawHead, this.func_220678_h() + 20.0F, this.deltaLookYaw);
				this.mob.rotationPitch = this.func_220675_a(this.mob.rotationPitch, this.func_220677_g() + 10.0F, this.deltaLookPitch);
			} else {
				if (this.mob.getNavigator().noPath()) {
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
