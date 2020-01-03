package endergeticexpansion.common.entities.booflo;

import java.util.UUID;

import javax.annotation.Nullable;

import endergeticexpansion.api.endimator.EndimatedEntity;
import endergeticexpansion.api.endimator.Endimation;
import endergeticexpansion.api.entity.util.EntityItemStackHelper;
import endergeticexpansion.api.util.NetworkUtil;
import endergeticexpansion.common.entities.booflo.ai.BoofloBreedGoal;
import endergeticexpansion.common.entities.booflo.ai.BoofloFaceRandomGoal;
import endergeticexpansion.common.entities.booflo.ai.BoofloGiveBirthGoal;
import endergeticexpansion.common.entities.booflo.ai.BoofloGroundHopGoal;
import endergeticexpansion.core.registry.EEBlocks;
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
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EntityBooflo extends EndimatedEntity {
	private static final DataParameter<Boolean> BOOFED = EntityDataManager.createKey(EntityBooflo.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> PREGNANT = EntityDataManager.createKey(EntityBooflo.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> LOVE_TICKS = EntityDataManager.createKey(EntityBooflo.class, DataSerializers.VARINT);
	private static final DataParameter<Float> BIRTH_YAW = EntityDataManager.createKey(EntityBooflo.class, DataSerializers.FLOAT);
	public static final Endimation CROAK = new Endimation(55);
	public static final Endimation HOP = new Endimation(25);
	public static final Endimation HURT = new Endimation(15);
	public static final Endimation BIRTH = new Endimation(140);
	public int hopDelay;
	private UUID playerInLove;

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
		this.getDataManager().register(PREGNANT, false);
		this.getDataManager().register(LOVE_TICKS, 0);
		this.getDataManager().register(BIRTH_YAW, 0.0F);
	}
	
	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new BoofloGiveBirthGoal(this));
		this.goalSelector.addGoal(1, new BoofloBreedGoal(this));
		this.goalSelector.addGoal(4, new BoofloFaceRandomGoal(this));
		this.goalSelector.addGoal(5, new BoofloGroundHopGoal(this));
	}
	
	@Override
	public void livingTick() {
		super.livingTick();
		
		if(this.hopDelay > 0) {
			this.hopDelay--;
		}
		
		if(this.isPregnant()) {
			this.resetInLove();
		}
		
		if(this.getInLoveTicks() > 0) {
			this.setInLove(this.getInLoveTicks() - 1);
			if(this.getInLoveTicks() % 10 == 0) {
				double d0 = this.rand.nextGaussian() * 0.02D;
				double d1 = this.rand.nextGaussian() * 0.02D;
				double d2 = this.rand.nextGaussian() * 0.02D;
				this.world.addParticle(ParticleTypes.HEART, this.posX + (this.rand.nextFloat() * this.getWidth() * 2.0F) - this.getWidth(), this.posY + 0.5D + (this.rand.nextFloat() * this.getHeight()), this.posZ + (this.rand.nextFloat() * this.getWidth() * 2.0F) - this.getWidth(), d0, d1, d2);
			}
		}
		
		if(!this.isWorldRemote() && this.isAlive() && this.onGround && this.rand.nextInt(1000) < this.livingSoundTime++ && this.isAnimationPlaying(BLANK_ANIMATION)) {
			this.livingSoundTime = -this.getTalkInterval();
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
		compound.putBoolean("IsPregnant", this.isPregnant());
		compound.putInt("InLove", this.getInLoveTicks());
		compound.putFloat("BirthYaw", this.getBirthYaw());
		if(this.playerInLove != null) {
			compound.putUniqueId("LoveCause", this.playerInLove);
		}
	}
	
	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		this.setBoofed(compound.getBoolean("IsBoofed"));
		this.setPregnant(compound.getBoolean("IsPregnant"));
		this.setInLove(compound.getInt("InLove"));
		this.setBirthYaw(compound.getFloat("BirthYaw"));
		this.playerInLove = compound.hasUniqueId("LoveCause") ? compound.getUniqueId("LoveCause") : null;
	}
	
	public boolean isBoofed() {
		return this.dataManager.get(BOOFED);
	}
	
	public void setBoofed(boolean boofed) {
		this.dataManager.set(BOOFED, boofed);
	}
	
	public boolean isPregnant() {
		return this.dataManager.get(PREGNANT);
	}
	
	public void setPregnant(boolean pregnant) {
		this.dataManager.set(PREGNANT, pregnant);
	}
	
	public float getBirthYaw() {
		return this.dataManager.get(BIRTH_YAW);
	}
	
	public void setBirthYaw(float yaw) {
		this.dataManager.set(BIRTH_YAW, yaw);
	}
	
	public void setInLove(@Nullable PlayerEntity player) {
		this.setInLove(600);
		if(player != null) {
			this.playerInLove = player.getUniqueID();
		}

		this.world.setEntityState(this, (byte) 18);
	}

	public void setInLove(int ticks) {
		this.dataManager.set(LOVE_TICKS, ticks);
	}
	
	public int getInLoveTicks() {
		return this.dataManager.get(LOVE_TICKS);
	}
	
	public boolean canBreed() {
		return this.getInLoveTicks() <= 0 && !this.isPregnant();
	}
	
	public boolean isInLove() {
		if(this.isPregnant()) {
			return false;
		}
		return this.getInLoveTicks() > 0;
	}
	
	public void resetInLove() {
		this.setInLove(0);
	}
	
	@Nullable
	public ServerPlayerEntity getLoveCause() {
		if(this.playerInLove == null) {
			return null;
		} else {
			PlayerEntity playerentity = this.world.getPlayerByUuid(this.playerInLove);
			return playerentity instanceof ServerPlayerEntity ? (ServerPlayerEntity) playerentity : null;
		}
	}
	
	public boolean canMateWith(EntityBooflo possibleMate) {
		if(possibleMate == this) {
			return false;
		} else {
			return this.isInLove() && possibleMate.isInLove();
		}
	}
	
	public int getDefaultGroundHopDelay() {
		return this.isInLove() ? this.rand.nextInt(10) + 25 : this.rand.nextInt(40) + 80;
	}
	
	@Override
	protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
		return 0.9F;
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
			HURT,
			BIRTH
		};
	}
	
	@Override
	public Endimation getHurtAnimation() {
		return HURT;
	}
	
	@Override
	protected void jump() {
		Vec3d vec3d = this.getMotion();
		this.setMotion(vec3d.x, 0.55D, vec3d.z);
		this.isAirBorne = true;
	}
	
	public void setMovementSpeed(double newSpeed) {
		this.getNavigator().setSpeed(newSpeed);
		this.moveController.setMoveTo(this.moveController.getX(), this.moveController.getY(), this.moveController.getZ(), newSpeed);
	}
	
	@Override
	protected void collideWithEntity(Entity entity) {
		if(entity instanceof EntityBoofloBaby && (((EntityBoofloBaby) (entity)).isBeingBorn() || ((EntityBoofloBaby) (entity)).getMotherNoClipTicks() > 0)) return;
		
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
		} else if(item == EEBlocks.POISE_CLUSTER.asItem() && this.canBreed()) {
			EntityItemStackHelper.consumeItemFromStack(player, itemstack);
			this.setInLove(player);
		}
		return false;
	}
	
	@Override
	public void updatePassenger(Entity passenger) {
		if(this.isPassenger(passenger)) {
			int passengerIndex = this.getPassengers().indexOf(passenger);
			
			double xOffset = passengerIndex == 0 ? 0.25F : -0.25F;
			double zOffset = passengerIndex == 0 ? 0.0F : passengerIndex == 1 ? -0.25F : 0.25F;
			Vec3d ridingOffset = (new Vec3d(xOffset, 0.0D, zOffset)).rotateYaw(-this.getBirthYaw() * ((float) Math.PI / 180F) - ((float) Math.PI / 2F));
			
			passenger.setPosition(this.posX + ridingOffset.x, this.posY + 0.9F, this.posZ + ridingOffset.z);
		}
	}
	
	@Override
	public int getHorizontalFaceSpeed() {
		return 1;
	}
	
	@Override
	protected boolean canFitPassenger(Entity passenger) {
		int limit = this.isPregnant() ? 3 : 1;
		return this.getPassengers().size() < limit;
	}
	
	@Override
	public void fall(float distance, float damageMultiplier) {}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public void handleStatusUpdate(byte id) {
		if(id == 18) {
			for(int i = 0; i < 7; ++i) {
				double d0 = this.rand.nextGaussian() * 0.02D;
				double d1 = this.rand.nextGaussian() * 0.02D;
				double d2 = this.rand.nextGaussian() * 0.02D;
				this.world.addParticle(ParticleTypes.HEART, this.posX + (double)(this.rand.nextFloat() * this.getWidth() * 2.0F) - (double)this.getWidth(), this.posY + 0.5D + (double)(this.rand.nextFloat() * this.getHeight()), this.posZ + (double)(this.rand.nextFloat() * this.getWidth() * 2.0F) - (double)this.getWidth(), d0, d1, d2);
			}
		} else {
			super.handleStatusUpdate(id);
		}
	}
	
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
					if(this.booflo.hopDelay == 0 && this.booflo.isAnimationPlaying(HOP) && this.booflo.getAnimationTick() == 10) {
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