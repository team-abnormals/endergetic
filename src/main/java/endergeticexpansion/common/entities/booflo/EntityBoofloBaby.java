package endergeticexpansion.common.entities.booflo;

import javax.annotation.Nullable;

import endergeticexpansion.api.endimator.EndimatedEntity;
import endergeticexpansion.api.endimator.Endimation;
import endergeticexpansion.api.entity.util.EntityItemStackHelper;
import endergeticexpansion.common.entities.booflo.ai.BabyFollowParentGoal;
import endergeticexpansion.core.registry.EEEntities;
import endergeticexpansion.core.registry.EEItems;
import net.minecraft.block.BlockState;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.RandomPositionGenerator;
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
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EntityBoofloBaby extends EndimatedEntity {
	private static final DataParameter<Boolean> MOVING = EntityDataManager.createKey(EntityBoofloBaby.class, DataSerializers.BOOLEAN);
	public static final DataParameter<Boolean> BEING_BORN = EntityDataManager.createKey(EntityBoofloBaby.class, DataSerializers.BOOLEAN);
	public static final DataParameter<Integer> MOTHER_IMMUNITY_TICKS = EntityDataManager.createKey(EntityBoofloBaby.class, DataSerializers.VARINT);
	public static final Endimation BIRTH = new Endimation(60);
	public int growingAge;
	public int forcedAge;
	public int forcedAgeTimer;
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
		this.getDataManager().register(BEING_BORN, false);
		this.getDataManager().register(MOTHER_IMMUNITY_TICKS, 0);
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
		this.goalSelector.addGoal(0, new SwimGoal(this)); //Makes Booflo when in water at surface to stay and swim like a cow in water
		this.goalSelector.addGoal(3, new BabyFollowParentGoal(this, 1.2F));
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
		compound.putBoolean("IsBeingBorn", this.isBeingBorn());
		compound.putInt("Age", this.getGrowingAge());
		compound.putInt("MotherImmunityTicks", this.getMotherNoClipTicks());
		compound.putInt("ForcedAge", this.forcedAge);
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		this.setMoving(compound.getBoolean("Moving"));
		this.setBeingBorn(compound.getBoolean("IsBeingBorn"));
		this.setGrowingAge(compound.getInt("Age"));
		this.setMotherNoClipTicks(compound.getInt("MotherImmunityTicks"));
		this.forcedAge = compound.getInt("ForcedAge");
	}
	
	public boolean isMoving() {
		return this.getDataManager().get(MOVING);
	}

	public void setMoving(boolean moving) {
		this.getDataManager().set(MOVING, moving);
	}
	
	public boolean isBeingBorn() {
		return this.getDataManager().get(BEING_BORN);
	}

	public void setBeingBorn(boolean beingBorn) {
		this.getDataManager().set(BEING_BORN, beingBorn);
	}
	
	public int getMotherNoClipTicks() {
		return this.getDataManager().get(MOTHER_IMMUNITY_TICKS);
	}

	public void setMotherNoClipTicks(int ticks) {
		this.getDataManager().set(MOTHER_IMMUNITY_TICKS, ticks);
	}
	
	public int getGrowingAge() {
		if(this.world.isRemote) {
			return -1;
		} else {
			return this.growingAge;
		}
	}
	
	public void setGrowingAge(int age) {
		int oldAge = this.growingAge;
		this.growingAge = age;
		if(oldAge < 0 && age >= 0 || oldAge > 0 && age < 0) {
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
	public boolean isAIDisabled() {
		return this.isBeingBorn() || super.isAIDisabled();
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
		super.livingTick();
		
		if(this.world.isRemote) {
			this.prevTailAnimation = this.tailAnimation;
			if(this.isInWater()) {
				this.tailSpeed = 1.0F;
			} else if(this.isMoving()) {
				if(this.tailSpeed < 0.5F) {
					this.tailSpeed = 1.0F;
				} else {
					this.tailSpeed += (0.25F - this.tailSpeed) * 0.1F;
				}
			} else {
				this.tailSpeed += (0.1875F - this.tailSpeed) * 0.1F;
			}
			this.tailAnimation += this.tailSpeed;
		}
		
		if(this.world.isRemote) {
			if(this.forcedAgeTimer > 0) {
				if(this.forcedAgeTimer % 4 == 0) {
					this.world.addParticle(ParticleTypes.HAPPY_VILLAGER, this.posX + (double)(this.rand.nextFloat() * this.getWidth() * 2.0F) - (double)this.getWidth(), this.posY + 0.5D + (double)(this.rand.nextFloat() * this.getHeight()), this.posZ + (double)(this.rand.nextFloat() * this.getWidth() * 2.0F) - (double)this.getWidth(), 0.0D, 0.0D, 0.0D);
				}

				this.forcedAgeTimer--;
			}
		} else if(this.isAlive()) {
			int growingAge = this.getGrowingAge();
			if(growingAge < 0) {
				growingAge++;
	            this.setGrowingAge(growingAge);
			} else if(growingAge > 0) {
				growingAge--;
				this.setGrowingAge(growingAge);
			}
		}
		
		if(this.getMotherNoClipTicks() > 0) {
			this.setMotherNoClipTicks(this.getMotherNoClipTicks() - 1);
		}
		
		if(this.isBeingBorn() && this.isAnimationPlaying(BLANK_ANIMATION)) {
			this.setPlayingAnimation(BIRTH);
		}
		
		if(this.isAnimationPlaying(BIRTH) && this.getAnimationTick() == 59) {
			double[] oldPosition = { this.posX, this.posY, this.posZ };
			this.stopRiding();
			this.setBeingBorn(false);
			this.setPosition(oldPosition[0], oldPosition[1], oldPosition[2]);
			this.rotationPitch = 180;
			this.setMotherNoClipTicks(50);
		}
	}
	
	public void growUp() {
		if(!this.world.isRemote && this.isAlive()) {
			EntityBoofloAdolescent booflo = EEEntities.BOOFLO_ADOLESCENT.get().create(this.world);
			booflo.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
			
			if(booflo.hasCustomName()) {
    			booflo.setCustomName(booflo.getCustomName());
    			booflo.setCustomNameVisible(booflo.isCustomNameVisible());
    		}
			
			booflo.setHealth(booflo.getMaxHealth());
			booflo.setGrowingAge(-24000);
			this.world.addEntity(booflo);
			
			this.remove();
		}
	}
	
	public void ageUp(int growthSeconds, boolean updateForcedAge) {
		int growingAge = this.getGrowingAge();
		int j = growingAge;
		growingAge += growthSeconds * 20;
		if(growingAge > 0) {
			growingAge = 0;
		}

		int k = growingAge - j;
		
		this.setGrowingAge(growingAge);
		if(updateForcedAge) {
			this.forcedAge += k;
			if(this.forcedAgeTimer == 0) {
				this.forcedAgeTimer = 40;
			}
		}

		if (this.getGrowingAge() == 0) {
			this.setGrowingAge(this.forcedAge);
		}
	}
	
	public boolean processInteract(PlayerEntity player, Hand hand) {
		ItemStack itemstack = player.getHeldItem(hand);
		if(itemstack.getItem() == EEItems.BOLLOOM_FRUIT.get()) {
			EntityItemStackHelper.consumeItemFromStack(player, itemstack);
			this.ageUp((int) ((-this.getGrowingAge() / 20) * 0.1F), true);
			return true;
		}
		return super.processInteract(player, hand);
	}
	
	@Override
	public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, ILivingEntityData spawnDataIn, CompoundNBT dataTag) {
		this.setGrowingAge(-24000);
		return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
	}
	
	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(EEItems.BOOFLO_SPAWN_EGG.get());
	}
	
	@Override
	protected void collideWithEntity(Entity entityIn) {
		if(!(entityIn instanceof EntityBooflo)) {
			super.collideWithEntity(entityIn);
		}
	}
	
	@Override
	public Endimation[] getAnimations() {
		return new Endimation[] {
			BIRTH
		};
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
		
		@Override
		public boolean shouldExecute() {
			return super.shouldExecute() && !this.creature.isInWater();
		}
		
		@Override
		public boolean shouldContinueExecuting() {
			return super.shouldContinueExecuting() && !this.creature.isInWater();
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