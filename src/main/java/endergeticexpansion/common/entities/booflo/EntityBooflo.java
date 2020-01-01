package endergeticexpansion.common.entities.booflo;

import endergeticexpansion.api.endimator.EndimatedEntity;
import endergeticexpansion.api.endimator.Endimation;
import endergeticexpansion.api.entity.util.EntityItemStackHelper;
import endergeticexpansion.api.util.NetworkUtil;
import endergeticexpansion.common.entities.booflo.ai.BoofloFaceRandomGoal;
import endergeticexpansion.common.entities.booflo.ai.BoofloGroundHopGoal;
import endergeticexpansion.core.registry.EEEntities;
import endergeticexpansion.core.registry.EEItems;
import endergeticexpansion.core.registry.EESounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityBooflo extends EndimatedEntity {
	private static final DataParameter<Boolean> BOOFED = EntityDataManager.createKey(EntityBooflo.class, DataSerializers.BOOLEAN);
	public static final Endimation CROAK = new Endimation(55);
	public static final Endimation HOP = new Endimation(25);
	public static final Endimation HURT = new Endimation(15);
	public int hopDelay;

	public EntityBooflo(EntityType<? extends EntityBooflo> type, World worldIn) {
		super(type, worldIn);
		this.moveController = new GroundMoveHelperController(this);
		this.hopDelay = this.getDefaultGroundHopDelay();
	}
	
	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(1.05D);
		this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2.0D);
		this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(22.0D);
	}
	
	@Override
	protected void registerData() {
		super.registerData();
		this.getDataManager().register(BOOFED, false);
	}
	
	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(4, new BoofloFaceRandomGoal(this));
		this.goalSelector.addGoal(5, new BoofloGroundHopGoal(this));
	}
	
	@Override
	public void livingTick() {
		super.livingTick();
		
		if(this.hopDelay > 0) {
			this.hopDelay--;
		}
		
		if(!this.isWorldRemote() && this.isAlive() && this.onGround && this.rand.nextInt(1000) < this.livingSoundTime++ && this.isAnimationPlaying(BLANK_ANIMATION)) {
			this.livingSoundTime = -this.getTalkInterval();
			this.setPlayingAnimation(CROAK);
			NetworkUtil.setPlayingAnimationMessage(this, CROAK);
		}
		
		if(this.isAnimationPlaying(CROAK) && this.getAnimationTick() == 5 && !this.isWorldRemote()) {
			this.playSound(this.getAmbientSound(), this.getSoundVolume(), this.getSoundPitch());
		}
	}
	
	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		compound.putBoolean("IsBoofed", this.isBoofed());
	}
	
	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		this.setBoofed(compound.getBoolean("IsBoofed"));
	}
	
	public boolean isBoofed() {
		return this.dataManager.get(BOOFED);
	}
	
	public void setBoofed(boolean boofed) {
		this.dataManager.set(BOOFED, boofed);
	}
	
	public int getDefaultGroundHopDelay() {
		return this.rand.nextInt(40) + 20;
	}
	
	@Override
	public Endimation getHurtAnimation() {
		return HURT;
	}
	
	@Override
	public PathNavigator getNavigator() {
		return new GroundPathNavigator(this, world);
	}
	
	@Override
	protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
		return 0.9F;
	}
	
	@Override
	public int getVerticalFaceSpeed() {
		return 20;
	}
	
	@Override
	public int getHorizontalFaceSpeed() {
		return 20;
	}
	
	@Override
	public int getTalkInterval() {
		return 120;
	}
	
	@Override
	public Endimation[] getAnimations() {
		return new Endimation[] {
			CROAK,
			HOP,
			HURT
		};
	}
	
	@Override
	protected void jump() {
		Vec3d vec3d = this.getMotion();
		this.setMotion(vec3d.x, 0.55D, vec3d.z);
		this.isAirBorne = true;
	}
	
	@Override
	protected void collideWithEntity(Entity entity) {
		if(entity instanceof EntityBoofloBaby && ((EntityBoofloBaby) (entity)).isBeingBorn) return;
		
		super.collideWithEntity(entity);
	}
	
	@Override
	protected boolean processInteract(PlayerEntity player, Hand hand) {
		ItemStack itemstack = player.getHeldItem(hand);
		Item item = itemstack.getItem();
		
		if(item instanceof SpawnEggItem && ((SpawnEggItem)item).hasType(itemstack.getTag(), this.getType())) {
			if(!this.world.isRemote) {
				EntityBoofloBaby baby = EEEntities.BOOFLO_BABY.get().create(this.world);
				baby.setGrowingAge(-24000);
				baby.setLocationAndAngles(this.posX, this.posY, this.posZ, 0.0F, 0.0F);
				this.world.addEntity(baby);
				if(itemstack.hasDisplayName()) {
					baby.setCustomName(itemstack.getDisplayName());
				}
				
				EntityItemStackHelper.consumeItemFromStack(player, itemstack);
			}
			return true;
		}
		return false;
	}
	
	@Override
	public void fall(float distance, float damageMultiplier) {}
	
	/*
	 * Overridden to do nothing; gets remade in this class 
	 * @see EntityBooflo#livingTick
	 */
	@Override
	public void playAmbientSound() {}
	
	@Override
	protected SoundEvent getAmbientSound() {
		return EESounds.BOOFLO_CROAK.get();
	}
	
	@Override
	protected SoundEvent getDeathSound() {
		return EESounds.BOOFLO_DEATH.get();
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return EESounds.BOOFLO_HURT.get();
	}
	
	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(EEItems.BOOFLO_SPAWN_EGG.get());
	}
	
	public static class GroundMoveHelperController extends MovementController {
		private final EntityBooflo booflo;
		private float yRot;
		public int hopDelay;
		public boolean isAggressive;

		public GroundMoveHelperController(EntityBooflo booflo) {
			super(booflo);
			this.booflo = booflo;
			this.yRot = (float) (180.0F * booflo.rotationYaw / Math.PI);
		}

		public void setDirection(float yRot, boolean aggressive) {
			this.yRot = yRot;
			this.isAggressive = aggressive;
		}

		public void setSpeed(double speed) {
			this.speed = speed;
			this.action = MovementController.Action.MOVE_TO;
		}

		public void tick() {
			this.mob.rotationYaw = this.limitAngle(this.mob.rotationYaw, this.yRot, 90.0F);
			this.mob.rotationYawHead = this.mob.rotationYaw;
			this.mob.renderYawOffset = this.mob.rotationYaw;
			
			if(this.action != MovementController.Action.MOVE_TO) {
				this.mob.setMoveForward(0.0F);
			} else {
				this.action = MovementController.Action.WAIT;
				if(this.mob.onGround) {
					this.mob.setAIMoveSpeed((float) (this.speed * this.mob.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue()));
					if(this.booflo.hopDelay == 0) {
						this.booflo.getJumpController().setJumping();
						
						this.booflo.hopDelay = this.booflo.getDefaultGroundHopDelay();
					} else {
						this.booflo.moveStrafing = 0.0F;
						this.booflo.moveForward = 0.0F;
						this.mob.setAIMoveSpeed(0.0F);
					}
				} else {
					this.mob.setAIMoveSpeed((float)(this.speed * this.mob.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue()));
				}
	         }
		}
	}
}