package endergeticexpansion.common.entities.booflo;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import endergeticexpansion.api.endimator.ControlledEndimation;
import endergeticexpansion.api.endimator.EndimatedEntity;
import endergeticexpansion.api.endimator.Endimation;
import endergeticexpansion.api.entity.util.EndergeticFlyingPathNavigator;
import endergeticexpansion.api.entity.util.EntityItemStackHelper;
import endergeticexpansion.api.entity.util.RayTraceHelper;
import endergeticexpansion.api.util.MathUtils;
import endergeticexpansion.api.util.NetworkUtil;
import endergeticexpansion.common.entities.bolloom.EntityBolloomFruit;
import endergeticexpansion.common.entities.booflo.ai.BoofloAttackGoal;
import endergeticexpansion.common.entities.booflo.ai.BoofloBoofGoal;
import endergeticexpansion.common.entities.booflo.ai.BoofloBreedGoal;
import endergeticexpansion.common.entities.booflo.ai.BoofloEatGoal;
import endergeticexpansion.common.entities.booflo.ai.BoofloFaceRandomGoal;
import endergeticexpansion.common.entities.booflo.ai.BoofloGiveBirthGoal;
import endergeticexpansion.common.entities.booflo.ai.BoofloGroundHopGoal;
import endergeticexpansion.common.entities.booflo.ai.BoofloHuntGoal;
import endergeticexpansion.common.entities.booflo.ai.BoofloNearestAttackableTargetGoal;
import endergeticexpansion.common.entities.booflo.ai.BoofloSinkGoal;
import endergeticexpansion.common.entities.booflo.ai.BoofloSlamGoal;
import endergeticexpansion.common.entities.booflo.ai.BoofloSwimGoal;
import endergeticexpansion.common.entities.booflo.ai.BoofloTemptGoal;
import endergeticexpansion.core.registry.EEBlocks;
import endergeticexpansion.core.registry.EEEntities;
import endergeticexpansion.core.registry.EEItems;
import endergeticexpansion.core.registry.EESounds;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.controller.LookController;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EntityBooflo extends EndimatedEntity {
	public static final Predicate<Entity> IS_SCARED_BY = (entity) -> {
		if(entity instanceof PlayerEntity) {
			return !entity.isSpectator() && !((PlayerEntity)entity).isCreative();
		}
		return false;
	};
	private static final DataParameter<Optional<UUID>> OWNER_UNIQUE_ID = EntityDataManager.createKey(EntityBooflo.class, DataSerializers.OPTIONAL_UNIQUE_ID);
	private static final DataParameter<Optional<UUID>> LAST_FED_UNIQUE_ID = EntityDataManager.createKey(EntityBooflo.class, DataSerializers.OPTIONAL_UNIQUE_ID);
	private static final DataParameter<Boolean> TAMED = EntityDataManager.createKey(EntityBooflo.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> MOVING_IN_AIR = EntityDataManager.createKey(EntityBooflo.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> BOOFED = EntityDataManager.createKey(EntityBooflo.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> PREGNANT = EntityDataManager.createKey(EntityBooflo.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> HUNGRY = EntityDataManager.createKey(EntityBooflo.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> HAS_FRUIT = EntityDataManager.createKey(EntityBooflo.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> FRUITS_NEEDED = EntityDataManager.createKey(EntityBooflo.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> RIDE_CONTROL_DELAY = EntityDataManager.createKey(EntityBooflo.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> LOVE_TICKS = EntityDataManager.createKey(EntityBooflo.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> ATTACK_TARGET = EntityDataManager.createKey(EntityBooflo.class, DataSerializers.VARINT);
	private static final DataParameter<Float> BIRTH_YAW = EntityDataManager.createKey(EntityBooflo.class, DataSerializers.FLOAT);
	public static final Endimation CROAK = new Endimation(55);
	public static final Endimation HOP = new Endimation(25);
	public static final Endimation HURT = new Endimation(15);
	public static final Endimation BIRTH = new Endimation(140);
	public static final Endimation INFLATE = new Endimation(10);
	public static final Endimation SWIM = new Endimation(20);
	public static final Endimation EAT = new Endimation(160);
	public static final Endimation CHARGE = new Endimation(75);
	public static final Endimation SLAM = new Endimation(10);
	public static final Endimation GROWL = new Endimation(60);
	private static final EntitySize BOOFED_SIZE = EntitySize.fixed(2.0F, 1.5F);
	public final ControlledEndimation OPEN_JAW = new ControlledEndimation(25, 0);
	public final ControlledEndimation FRUIT_HOVER = new ControlledEndimation(8, 0);
	private final EndergeticFlyingPathNavigator attackingNavigator;
	private UUID playerInLove;
	public int hopDelay;
	private int croakDelay;
	private boolean shouldPlayLandSound;
	private boolean wasOnGround;

	public EntityBooflo(EntityType<? extends EntityBooflo> type, World world) {
		super(type, world);
		this.attackingNavigator = new EndergeticFlyingPathNavigator(this, this.world);
		this.moveController = new GroundMoveHelperController(this);
		this.hopDelay = this.getDefaultGroundHopDelay();
	}
	
	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		this.getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(7.0D);
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0D);
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(1.05D);
		this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(4.0D);
		this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(22.0D);
	}
	
	@Override
	protected void registerData() {
		super.registerData();
		this.getDataManager().register(OWNER_UNIQUE_ID, Optional.empty());
		this.getDataManager().register(LAST_FED_UNIQUE_ID, Optional.empty());
		this.getDataManager().register(TAMED, false);
		this.getDataManager().register(MOVING_IN_AIR, false);
		this.getDataManager().register(BOOFED, false);
		this.getDataManager().register(PREGNANT, false);
		this.getDataManager().register(HUNGRY, true);
		this.getDataManager().register(HAS_FRUIT, false);
		this.getDataManager().register(FRUITS_NEEDED, this.getRNG().nextInt(3) + 2);
		this.getDataManager().register(RIDE_CONTROL_DELAY, 0);
		this.getDataManager().register(LOVE_TICKS, 0);
		this.getDataManager().register(ATTACK_TARGET, 0);
		this.getDataManager().register(BIRTH_YAW, 0.0F);
	}
	
	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new BoofloEatGoal(this));
		this.goalSelector.addGoal(0, new BoofloGiveBirthGoal(this));
		this.goalSelector.addGoal(1, new BoofloSlamGoal(this));
		this.goalSelector.addGoal(1, new BoofloBreedGoal(this));
		this.goalSelector.addGoal(3, new BoofloSinkGoal(this));
		this.goalSelector.addGoal(4, new BoofloBoofGoal(this));
		this.goalSelector.addGoal(5, new BoofloTemptGoal(this));
		this.goalSelector.addGoal(6, new BoofloAttackGoal(this, true));
		this.goalSelector.addGoal(7, new BoofloHuntGoal(this, 1.0F, true));
		this.goalSelector.addGoal(8, new BoofloSwimGoal(this, 1.0F, 15));
		this.goalSelector.addGoal(9, new BoofloFaceRandomGoal(this));
		this.goalSelector.addGoal(10, new BoofloGroundHopGoal(this));
		
		this.targetSelector.addGoal(2, new BoofloNearestAttackableTargetGoal<>(this, EntityBolloomFruit.class, true));
	}
	
	@Override
	public void tick() {
		super.tick();
		
		if(this.getRideControlDelay() > 0) this.setRideControlDelay(this.getRideControlDelay() - 1);
		
		if(this.croakDelay > 0) this.croakDelay--;
		
		if(this.isBoofed()) {
			if(this.hasAggressiveAttackTarget()) {
				this.navigator = this.attackingNavigator;
			} else {
				if(this.navigator instanceof EndergeticFlyingPathNavigator) {
					this.navigator = new FlyingPathNavigator(this, this.world) {
						
						@Override
						public boolean canEntityStandOnPos(BlockPos pos) {
							return this.world.isAirBlock(pos);
						}
						
					};
				}
			}
		}
		
		if(!this.isWorldRemote()) {
			if(this.isBoofed() && this.isAnimationPlaying(EntityBooflo.BLANK_ANIMATION) && this.isMovingInAir()) {
				if(RayTraceHelper.rayTrace(this, 2.0D, 1.0F).getType() != Type.BLOCK) {
					NetworkUtil.setPlayingAnimationMessage(this, EntityBooflo.SWIM);
				}
			}
			
			if(this.isAnimationPlaying(EAT)) {
				if((this.getAnimationTick() > 20 && this.getAnimationTick() <= 140)) {
					if(this.getAnimationTick() % 18 == 0) {
						if(this.world instanceof ServerWorld && this.hasCaughtFruit()) {
							((ServerWorld)this.world).spawnParticle(new ItemParticleData(ParticleTypes.ITEM, new ItemStack(EEItems.BOLLOOM_FRUIT.get())), this.posX, this.posY + (double)this.getHeight() / 1.5D, this.posZ, 10, (double)(this.getWidth() / 4.0F), (double)(this.getHeight() / 4.0F), (double)(this.getWidth() / 4.0F), 0.05D);
						}
						this.playSound(SoundEvents.ENTITY_GENERIC_EAT, 1.0F, 1.0F);
					}
					if(this.getAnimationTick() == 140) {
						this.setCaughtFruit(false);
						this.heal(5.0F);
					}
				}
			}
			
			if(this.isAnimationPlaying(HOP)) {
				if(this.getAnimationTick() == 10) {
					this.playSound(this.getHopSound(false), 0.95F, this.getSoundPitch());
					this.shouldPlayLandSound = true;
				}
			}
			
			if(this.shouldPlayLandSound && this.onGround && !this.wasOnGround) {
				this.playSound(this.getHopSound(true), 0.95F, this.getSoundPitch());
				this.shouldPlayLandSound = false;
			}
		}
		
		if(this.isAnimationPlaying(SWIM)) {
			if(this.getAnimationTick() == 2) {
				float xMotion = -MathHelper.sin(this.rotationYaw * ((float) Math.PI / 180F)) * MathHelper.cos(this.rotationPitch * ((float) Math.PI / 180F));
				float yMotion = -MathHelper.sin(this.rotationPitch * ((float) Math.PI / 180F));
				float zMotion = MathHelper.cos(this.rotationYaw * ((float) Math.PI / 180F)) * MathHelper.cos(this.rotationPitch * ((float) Math.PI / 180F));
			
				double motionScale;
				
				if(this.hasAggressiveAttackTarget()) {
					motionScale = 0.85F;
				} else {
					motionScale = this.getPassengers().isEmpty() ? 0.5 : 0.8F;
				}
			
				Vec3d motion = new Vec3d(xMotion, yMotion, zMotion).normalize().mul(motionScale, 0.5D, motionScale);
			
				this.addVelocity(motion.x, motion.y, motion.z);
			}
			
			if(!this.isWorldRemote() && this.getAnimationTick() <= 15) {
				this.setMovingInAir(true);
			}
		}
		
		if(this.isAnimationPlaying(INFLATE) && this.getAnimationTick() == 2) {
			this.boof(1.0F, 1.0F);
		}
		
		if(!this.isWorldRemote() && this.isAnimationPlaying(GROWL)) {
			if(this.getAnimationTick() == 10) {
				this.playSound(this.getGrowlSound(), 0.75F, this.getSoundPitch());
			}
			
			if(this.getAnimationTick() >= 20) {
				for(PlayerEntity players : this.getNearbyPlayers(0.4F)) {
					if(!this.hasAggressiveAttackTarget()) {
						this.setBoofloAttackTargetId(players.getEntityId());
					}
				}
			}
		}
		
		if(this.isAnimationPlaying(SLAM) && this.getAnimationTick() == 3) {
			this.boof(1.2F, 2.2F);
			this.playSound(this.getSlamSound(), 0.75F, 1.0F);
		}
		
		if(this.isInWater()) {
			if(!this.isBoofed()) {
				this.setBoofed(true);
			} else if(this.isBoofed() && this.getRNG().nextFloat() < 0.7F) {
				this.addVelocity(0.0F, 0.05F, 0.0F);
			}
		}
		
		if(this.onGround && this.isBoofed()) {
			if(this.hasAggressiveAttackTarget()) {
				if(!this.isWorldRemote()) {
					if(this.isAnimationPlaying(BLANK_ANIMATION)) {
						NetworkUtil.setPlayingAnimationMessage(this, INFLATE);
					} else if(this.isAnimationPlaying(CHARGE)) {
						NetworkUtil.setPlayingAnimationMessage(this, SLAM);
					}
				}
			} else {
				this.setBoofed(false);
			}
		}
		
		if(this.getRNG().nextInt(40000) < 10 && !this.hasCaughtFruit()) {
			this.setHungry(true);
		}
		
		if(this.isWorldRemote()) {
			this.FRUIT_HOVER.update();
			
			if(this.isBoofed()) {
				this.OPEN_JAW.setDecrementing(this.getBoofloAttackTarget() == null || this.hasAggressiveAttackTarget());
				
				this.OPEN_JAW.update();
					
				this.OPEN_JAW.tick();
			}
			
			if(this.isAnimationPlaying(EAT)) {
				if((this.getAnimationTick() >= 20) && this.getAnimationTick() < 140) {
					if(this.getAnimationTick() % 10 == 0) {
						if(this.getAnimationTick() == 20) {
							this.FRUIT_HOVER.setDecrementing(false);
							this.FRUIT_HOVER.setValue(0);
						}
						this.FRUIT_HOVER.setDecrementing(!this.FRUIT_HOVER.isDescrementing());
					}
				} else if(this.getAnimationTick() >= 140) {
					this.FRUIT_HOVER.setDecrementing(false);
				}
			}
			
			this.FRUIT_HOVER.tick();
		}
		
		/**
		 * Fix Boof
		 */
		if(!this.isWorldRemote() && this.moveController instanceof FlyingMoveController && this.isBoofed()) {
			this.setBoofed(true);
		}
		
		this.wasOnGround = this.onGround;
	}
	
	@Override
	public void livingTick() {
		super.livingTick();
		
		if(this.hopDelay > 0) this.hopDelay--;
		
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
		
		if(!this.isWorldRemote() && this.croakDelay == 0 && !this.isTempted() && this.isAlive() && this.onGround && !this.isBoofed() && this.rand.nextInt(1000) < this.livingSoundTime++ && this.isAnimationPlaying(BLANK_ANIMATION) && this.getPassengers().isEmpty()) {
			this.livingSoundTime = -this.getTalkInterval();
			NetworkUtil.setPlayingAnimationMessage(this, CROAK);
		}
		
		if(this.isAnimationPlaying(CROAK) && this.getAnimationTick() == 5 && !this.isWorldRemote()) {
			this.playSound(this.getAmbientSound(), this.getSoundVolume(), this.getSoundPitch());
		}
		
		if(this.hasAggressiveAttackTarget() && this.canEntityBeSeen(this.getBoofloAttackTarget())) {
			this.rotationYaw = this.rotationYawHead;
		}
	}
	
	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		compound.putBoolean("IsMovingInAir", this.isMovingInAir());
		compound.putBoolean("IsBoofed", this.isBoofed());
		compound.putBoolean("IsPregnant", this.isPregnant());
		compound.putBoolean("IsHungry", this.isHungry());
		compound.putBoolean("HasFruit", this.hasCaughtFruit());
		compound.putInt("FruitsNeededTillTamed", this.getFruitsNeededTillTamed());
		compound.putInt("RideControlDelay", this.getRideControlDelay());
		compound.putInt("InLove", this.getInLoveTicks());
		compound.putInt("BoofloTargetId", this.getBoofloAttackTargetId());
		compound.putFloat("BirthYaw", this.getBirthYaw());
		
		if(this.playerInLove != null) {
			compound.putUniqueId("LoveCause", this.playerInLove);
		}
		
		if(this.getOwnerId() == null) {
			compound.putString("OwnerUUID", "");
		} else {
			compound.putString("OwnerUUID", this.getOwnerId().toString());
		}
		
		if(this.getLastFedId() == null) {
			compound.putString("LastFedUUID", "");
		} else {
			compound.putString("LastFedUUID", this.getLastFedId().toString());
		}
	}
	
	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		this.setMovingInAir(compound.getBoolean("IsMovingInAir"));
		this.setBoofed(compound.getBoolean("IsBoofed"));
		this.setPregnant(compound.getBoolean("IsPregnant"));
		this.setHungry(compound.getBoolean("IsHungry"));
		this.setCaughtFruit(compound.getBoolean("HasFruit"));
		this.setFruitsNeeded(compound.getInt("FruitsNeededTillTamed"));
		this.setRideControlDelay(compound.getInt("RideControlDelay"));
		this.setInLove(compound.getInt("InLove"));
		this.setBoofloAttackTargetId(compound.getInt("BoofloTargetId"));
		this.setBirthYaw(compound.getFloat("BirthYaw"));
		this.playerInLove = compound.hasUniqueId("LoveCause") ? compound.getUniqueId("LoveCause") : null;
		
		String ownerUUID = compound.contains("OwnerUUID", 8) ? compound.getString("OwnerUUID") : PreYggdrasilConverter.convertMobOwnerIfNeeded(this.getServer(), compound.getString("Owner"));
		String lastFedUUID = compound.contains("LastFedUUID") ? compound.getString("LastFedUUID") : PreYggdrasilConverter.convertMobOwnerIfNeeded(this.getServer(), compound.getString("Owner"));
		
		if(!ownerUUID.isEmpty()) {
			try {
				this.setOwnerId(UUID.fromString(ownerUUID));
				this.setTamed(true);
			} catch (Throwable exception) {
				this.setTamed(false);
			}
		}
		
		if(!lastFedUUID.isEmpty()) {
			try {
				this.setLastFedId(UUID.fromString(lastFedUUID));
			} catch (Throwable exception) {}
		}
	}
	
	@Override
	public void notifyDataManagerChange(DataParameter<?> key) {
		if(BOOFED.equals(key)) {
			this.recalculateSize();
			if(this.isBoofed()) {
				this.navigator = new FlyingPathNavigator(this, this.world) {
					
					@Override
					public boolean canEntityStandOnPos(BlockPos pos) {
						return this.world.isAirBlock(pos);
					}
					
				};
				this.moveController = new FlyingMoveController(this);
				this.lookController = new FlyingLookController(this, 10);
				
				if(!this.isWorldRemote() && this.ticksExisted > 5) {
					this.playSound(this.getInflateSound(), this.getSoundVolume(), this.getSoundPitch());
				}
			} else {
				this.navigator = this.createNavigator(this.world);
				this.moveController = new GroundMoveHelperController(this);
				this.lookController = new LookController(this);
				
				if(!this.isWorldRemote() && this.ticksExisted > 5) {
					this.playSound(this.getDeflateSound(), this.getSoundVolume(), this.getSoundPitch());
				}
				
				if(this.isWorldRemote()) {
					this.OPEN_JAW.setValue(0);
					this.setBoofloAttackTargetId(0);
				}
			}
		}
	}
	
	@Override
	public void travel(Vec3d vec3d) {
		if(this.isBeingRidden() && this.canBeSteered()) {
			PlayerEntity player = (PlayerEntity) this.getControllingPassenger();
			this.rotationYaw = player.rotationYaw;
			this.prevRotationYaw = this.rotationYaw;
			this.rotationPitch = 0.0F;
			this.renderYawOffset = this.rotationYaw;
			this.rotationYawHead = this.renderYawOffset;
			
			float playerMoveFoward = player.moveForward;
			
			if(!this.isWorldRemote() && playerMoveFoward > 0.0F) {
				if(this.onGround && this.isAnimationPlaying(BLANK_ANIMATION) && !this.isBoofed()) {
					NetworkUtil.setPlayingAnimationMessage(this, HOP);
					this.setRideControlDelay(25);
				} else if(!this.onGround && this.isAnimationPlaying(BLANK_ANIMATION) && this.isBoofed()) {
					NetworkUtil.setPlayingAnimationMessage(this, SWIM);
				}
			}
			
			if(this.isBoofed()) {
				if(this.hasPath()) {
					this.getNavigator().clearPath();
				}
				
				if(this.getBoofloAttackTarget() != null) {
					this.setBoofloAttackTargetId(0);
				}
				
				this.moveRelative(0.0F, vec3d);
				this.move(MoverType.SELF, this.getMotion());
				this.setMotion(this.getMotion().scale(0.9D));
				if(playerMoveFoward <= 0.0F) {
					this.setMotion(this.getMotion().subtract(0, 0.01D, 0));
				}
			} else {
				if(this.isAnimationPlaying(HOP) && this.getAnimationTick() == 10) {
					Vec3d motion = this.getMotion();
					this.setMotion(motion.x, 0.55F, motion.z);
					this.isAirBorne = true;
					
					float xMotion = -MathHelper.sin(this.rotationYaw * ((float) Math.PI / 180F)) * MathHelper.cos(this.rotationPitch * ((float) Math.PI / 180F));
					float zMotion = MathHelper.cos(this.rotationYaw * ((float) Math.PI / 180F)) * MathHelper.cos(this.rotationPitch * ((float) Math.PI / 180F));
				
					Vec3d jumpFowardForce = new Vec3d(xMotion, 1.0F, zMotion).normalize().scale(1.4F);
			
					this.setMotion(this.getMotion().add(jumpFowardForce.getX(), 0.0F, jumpFowardForce.getZ()));
				}
				super.travel(vec3d);
			}
		} else {
			if(this.isServerWorld() && this.isBoofed()) {
				this.moveRelative(0.0F, vec3d);
				this.move(MoverType.SELF, this.getMotion());
				this.setMotion(this.getMotion().scale(0.9D));
				if(!this.isMovingInAir()) {
					this.setMotion(this.getMotion().subtract(0, 0.01D, 0));
				}
			} else {
				super.travel(vec3d);
			}
		}
	}
	
	@Override
	public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, ILivingEntityData spawnDataIn, CompoundNBT dataTag) {
		if(reason == SpawnReason.NATURAL) {
			Random rand = new Random();
			if(rand.nextFloat() < 0.2F) {
				this.setPregnant(true);
			}
			
			this.setFruitsNeeded(rand.nextInt(3) + 2);
		}
		return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
	}
	
	@Nullable
	public UUID getOwnerId() {
		return this.dataManager.get(OWNER_UNIQUE_ID).orElse(null);
	}  

	public void setOwnerId(@Nullable UUID ownerId) {
		this.dataManager.set(OWNER_UNIQUE_ID, Optional.ofNullable(ownerId));
	}
	
	@Nullable
	public UUID getLastFedId() {
		return this.dataManager.get(LAST_FED_UNIQUE_ID).orElse(null);
	}  

	public void setLastFedId(@Nullable UUID ownerId) {
		this.dataManager.set(LAST_FED_UNIQUE_ID, Optional.ofNullable(ownerId));
	}
	
	public boolean isTamed() {
		return this.dataManager.get(TAMED);
	}

	public void setTamed(boolean tamed) {
		this.dataManager.set(TAMED, tamed);
	}
	
	public boolean isMovingInAir() {
		return this.getDataManager().get(MOVING_IN_AIR);
	}

	public void setMovingInAir(boolean moving) {
		this.getDataManager().set(MOVING_IN_AIR, moving);
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
	
	public boolean isHungry() {
		return this.dataManager.get(HUNGRY);
	}
	
	public void setHungry(boolean hungry) {
		this.dataManager.set(HUNGRY, hungry);
	}
	
	public boolean hasCaughtFruit() {
		return this.dataManager.get(HAS_FRUIT);
	}
	
	public void setCaughtFruit(boolean hasCaughtFruit) {
		this.dataManager.set(HAS_FRUIT, hasCaughtFruit);
	}
	
	public float getBirthYaw() {
		return this.dataManager.get(BIRTH_YAW);
	}
	
	public void setBirthYaw(float yaw) {
		this.dataManager.set(BIRTH_YAW, yaw);
	}
	
	public int getBoofloAttackTargetId() {
		return this.dataManager.get(ATTACK_TARGET);
	}
	
	@Nullable
	public Entity getBoofloAttackTarget() {
		Entity entity = this.world.getEntityByID(this.getBoofloAttackTargetId());
		if(entity == null || entity != null && !entity.isAlive() || entity instanceof EntityBooflo) {
			this.setBoofloAttackTargetId(0);
		}
		
		if(this.getOwner() != null && this.getOwner() == entity) {
			this.setBoofloAttackTargetId(0);
		}
		return this.getBoofloAttackTargetId() > 0 ? entity : null;
	}
	
	public boolean hasAggressiveAttackTarget() {
		return this.getBoofloAttackTarget() instanceof LivingEntity;
	}
	
	public void setBoofloAttackTargetId(int id) {
		this.dataManager.set(ATTACK_TARGET, id);
	}
	
	public void setInLove(@Nullable PlayerEntity player) {
		this.setInLove(600);
		if(player != null) {
			this.playerInLove = player.getUniqueID();
		}

		this.world.setEntityState(this, (byte) 18);
	}
	
	public void setFruitsNeeded(int fruitsNeeded) {
		this.dataManager.set(FRUITS_NEEDED, fruitsNeeded);
	}
	
	public int getFruitsNeededTillTamed() {
		return this.dataManager.get(FRUITS_NEEDED);
	}
	
	public void setRideControlDelay(int ticks) {
		this.dataManager.set(RIDE_CONTROL_DELAY, ticks);
	}
	
	public int getRideControlDelay() {
		return this.dataManager.get(RIDE_CONTROL_DELAY);
	}

	public void setInLove(int ticks) {
		this.dataManager.set(LOVE_TICKS, ticks);
	}
	
	public int getInLoveTicks() {
		return this.dataManager.get(LOVE_TICKS);
	}
	
	public boolean canBreed() {
		return this.isTamed() && this.getInLoveTicks() <= 0 && !this.isPregnant();
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
	
	public void setTamedBy(PlayerEntity player) {
		this.setTamed(true);
		this.setOwnerId(player.getUniqueID());
		if(player instanceof ServerPlayerEntity) {
			//Creates wolf to still trigger tamed - as booflo isn't an AnimalEntity
			CriteriaTriggers.TAME_ANIMAL.trigger((ServerPlayerEntity)player, EntityType.WOLF.create(this.world));
		}
	}
	
	@Nullable
	public LivingEntity getOwner() {
		try {
			UUID uuid = this.getOwnerId();
			return uuid == null ? null : this.world.getPlayerByUuid(uuid);
		} catch (IllegalArgumentException exception) {
			return null;
		}
	}
	
	@Nullable
	public LivingEntity getLastFedPlayer() {
		try {
			UUID uuid = this.getLastFedId();
			return uuid == null ? null : this.world.getPlayerByUuid(uuid);
		} catch (IllegalArgumentException exception) {
			return null;
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
	
	public void boof(float internalStrength, float offensiveStrength) {
		if(!this.isWorldRemote()) {
			this.addVelocity(-MathHelper.sin((float) (this.rotationYaw * Math.PI / 180.0F)) * ((4F * internalStrength) * (rand.nextFloat() + 0.1F)) * 0.1F, 1.3F, MathHelper.cos((float) (this.rotationYaw * Math.PI / 180.0F)) * ((4F * internalStrength) * (rand.nextFloat() + 0.1F)) * 0.1F);
			
			if(offensiveStrength > 2.0F) {
				for(int i = 0; i < 12; i++) {
					double offsetX = MathUtils.makeNegativeRandomly(rand.nextFloat() * 0.25F, rand);
					double offsetZ = MathUtils.makeNegativeRandomly(rand.nextFloat() * 0.25F, rand);
			
					double x = this.posX + 0.5D + offsetX;
					double y = this.posY + 0.5D + (rand.nextFloat() * 0.05F);
					double z = this.posZ + 0.5D + offsetZ;
			
					if(this.isServerWorld()) {
						NetworkUtil.spawnParticle("endergetic:poise_bubble", x, y, z, MathUtils.makeNegativeRandomly((rand.nextFloat() * 0.3F), rand) + 0.025F, (rand.nextFloat() * 0.15F) + 0.1F, MathUtils.makeNegativeRandomly((rand.nextFloat() * 0.3F), rand) + 0.025F);
					}
				}
			}
		}
		for(Entity entity : this.world.getEntitiesWithinAABB(Entity.class, this.getBoundingBox().grow(3.5F * MathHelper.clamp(offensiveStrength / 2, 1.0F, offensiveStrength / 2)))) {
			boolean flyingPlayerFlag = !(entity instanceof PlayerEntity && ((PlayerEntity) entity).isCreative() && ((PlayerEntity) entity).abilities.isFlying);
			if(entity != this && (entity instanceof ItemEntity || entity instanceof LivingEntity) && flyingPlayerFlag) {
				float resistancy = this.isResistantToBoof(entity) ? 0.15F : 1.0F;
				float amount = (0.2F * offensiveStrength) * resistancy;
				if(offensiveStrength > 2.0F && resistancy > 0.15F) {
					entity.attackEntityFrom(DamageSource.causeMobDamage(this), (float) this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue());
					entity.velocityChanged = false;
				}
				Vec3d result = entity.getPositionVec().subtract(this.getPositionVec());
				entity.addVelocity(result.x * amount, (this.rand.nextFloat() * 0.75D + 0.25D) * offensiveStrength, result.z * amount);
			}
		}
	}
	
	@Override
	public boolean canDespawn(double distanceToClosestPlayer) {
		return !this.isTamed();
	}
	
	public boolean isTempted() {
		for(Object goals : this.goalSelector.getRunningGoals().toArray()) {
			if(goals instanceof PrioritizedGoal) {
				return ((PrioritizedGoal) goals).getGoal() instanceof BoofloTemptGoal;
			}
		}
		return false;
	}
	
	public List<PlayerEntity> getNearbyPlayers(float multiplier) {
		return this.world.getEntitiesWithinAABB(PlayerEntity.class, this.getBoundingBox().grow(8.0F * multiplier, 4.0F, 8.0F * multiplier), IS_SCARED_BY);
	}
	
	public boolean isPlayerNear(float multiplier) {
		return !this.getNearbyPlayers(multiplier).isEmpty();
	}
	
	@Override
	protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
		return this.isBoofed() ? 1.2F : 0.9F;
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
			BIRTH,
			INFLATE,
			SWIM,
			EAT,
			CHARGE,
			SLAM,
			GROWL
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
			if(!this.isWorldRemote()) {
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
			
			return true;
		} else if(item == EEItems.BOLLOOM_FRUIT.get() && !this.isAggressive() && !this.hasCaughtFruit() && this.onGround) {
			IParticleData particle = ParticleTypes.HEART;
			EntityItemStackHelper.consumeItemFromStack(player, itemstack);
			this.setCaughtFruit(true);
			this.setHungry(false);
			
			if(!this.isTamed()) {
				if(this.getFruitsNeededTillTamed() >= 1) {
					this.setFruitsNeeded(this.getFruitsNeededTillTamed() - 1);
					this.setLastFedId(player.getUniqueID());
					particle = ParticleTypes.SMOKE;
				
					if(!this.isWorldRemote()) {
						NetworkUtil.setPlayingAnimationMessage(this, GROWL);
					}
				} else {
					if(player == this.getLastFedPlayer()) {
						this.setFruitsNeeded(0);
						this.setTamedBy(player);
						this.croakDelay = 40;
					}
				}
			}
			
			if(this.isWorldRemote()) {
				for(int i = 0; i < 7; ++i) {
					double d0 = this.rand.nextGaussian() * 0.02D;
					double d1 = this.rand.nextGaussian() * 0.02D;
					double d2 = this.rand.nextGaussian() * 0.02D;
					this.world.addParticle(particle, this.posX + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double)this.getWidth(), this.posY + 0.5D + (double)(this.rand.nextFloat() * this.getHeight()), this.posZ + (double)(this.rand.nextFloat() * this.getWidth() * 2.0F) - (double)this.getWidth(), d0, d1, d2);
				}
			}
			return true;
		} else {
			if(this.isTamed() && !this.isBeingRidden() && !this.isPregnant()) {
				if(!this.world.isRemote) {
					player.startRiding(this);
				}
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void updatePassenger(Entity passenger) {
		if(this.isPassenger(passenger)) {
			if(passenger instanceof EntityBoofloBaby) {
				int passengerIndex = this.getPassengers().indexOf(passenger);
				
				double xOffset = passengerIndex == 0 ? 0.25F : -0.25F;
				double zOffset = passengerIndex == 0 ? 0.0F : passengerIndex == 1 ? -0.25F : 0.25F;
				Vec3d ridingOffset = (new Vec3d(xOffset, 0.0D, zOffset)).rotateYaw(-this.getBirthYaw() * ((float) Math.PI / 180F) - ((float) Math.PI / 2F));
				
				passenger.setPosition(this.posX + ridingOffset.x, this.posY + 0.9F, this.posZ + ridingOffset.z);
			} else {
				super.updatePassenger(passenger);
			}
		}
	}
	
	@Override
	public double getMountedYOffset() {
		double original = super.getMountedYOffset();
		return this.isBoofed() ? original + 0.15F : original;
	}
	
	@Override
	public boolean canBeSteered() {
		return this.getControllingPassenger() instanceof PlayerEntity;
	}
	
	@Override
	protected boolean canFitPassenger(Entity passenger) {
		int limit = this.isPregnant() ? 3 : 1;
		return this.getPassengers().size() < limit;
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		Entity entitySource = source.getTrueSource();
		if(entitySource instanceof LivingEntity) {
			if(entitySource instanceof PlayerEntity) {
				if(!entitySource.isSpectator() && !((PlayerEntity) entitySource).isCreative()) {
					this.setBoofloAttackTargetId(entitySource.getEntityId());
				}
			} else {
				this.setBoofloAttackTargetId(entitySource.getEntityId());
			}
		}
		return super.attackEntityFrom(source, amount);
	}
	
	@Override
	protected void damageEntity(DamageSource damageSrc, float damageAmount) {
		Entity entitySource = damageSrc.getTrueSource();
		if(entitySource instanceof LivingEntity) {
			if(entitySource instanceof PlayerEntity) {
				if(!entitySource.isSpectator() && !((PlayerEntity) entitySource).isCreative()) {
					this.setBoofloAttackTargetId(entitySource.getEntityId());
				}
			} else {
				this.setBoofloAttackTargetId(entitySource.getEntityId());
			}
		}
		super.damageEntity(damageSrc, damageAmount);
	}
	
	@Override
	public int getHorizontalFaceSpeed() {
		return 1;
	}
	
	@Override
	public int getMaxSpawnedInChunk() {
		return 4;
	}
	
	@Override
	public EntitySize getSize(Pose poseIn) {
		return this.isBoofed() ? BOOFED_SIZE : super.getSize(poseIn);
	}
	
	@Nullable
	public Entity getControllingPassenger() {
		return this.getPassengers().isEmpty() ? null : !(this.getPassengers().get(0) instanceof PlayerEntity) ? null : this.getPassengers().get(0);
	}
	
	protected boolean isResistantToBoof(Entity entity) {
		return entity instanceof EntityBooflo || entity instanceof EntityBoofloAdolescent || entity instanceof EntityBoofloBaby;
	}
	
	@Override
	public boolean shouldRiderFaceForward(PlayerEntity player) {
		return true;
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
	
	public SoundEvent getHopSound(boolean landing) {
		return landing ? EESounds.BOOFLO_HOP_LAND.get() : EESounds.BOOFLO_HOP.get();
	}
	
	public SoundEvent getGrowlSound() {
		return EESounds.BOOFLO_GROWL.get();
	}
	
	public SoundEvent getSlamSound() {
		return EESounds.BOOFLO_SLAM.get();
	}
	
	protected SoundEvent getInflateSound() {
		return EESounds.BOOFLO_INFLATE.get();
	}
	
	protected SoundEvent getDeflateSound() {
		return EESounds.BOOFLO_DEFLATE.get();
	}
	
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
					if(!this.isPlayerRiding()) {
						this.mob.setAIMoveSpeed((float) (this.speed * this.mob.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue()));
					}
					if(this.booflo.hopDelay == 0 && this.booflo.isAnimationPlaying(HOP) && this.booflo.getAnimationTick() == 10) {
						this.booflo.getJumpController().setJumping();
						
						this.booflo.hopDelay = this.booflo.getDefaultGroundHopDelay();
					} else {
						this.booflo.moveStrafing = 0.0F;
						this.booflo.moveForward = 0.0F;
						this.mob.setAIMoveSpeed(0.0F);
					}
				} else {
					if(!this.isPlayerRiding()) {
						this.mob.setAIMoveSpeed((float) (this.speed * this.mob.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue()));
					}
				}
			}
		}
		
		private boolean isPlayerRiding() {
			return !this.booflo.getPassengers().isEmpty() && this.booflo.getPassengers().get(0) instanceof PlayerEntity;
		}
	}
	
	public class FlyingMoveController extends MovementController {
		private final EntityBooflo booflo;

		public FlyingMoveController(EntityBooflo booflo) {
			super(booflo);
			this.booflo = booflo;
		}

		public void tick() {
			if(this.action == MovementController.Action.MOVE_TO && !this.booflo.getNavigator().noPath()) {
				if(this.booflo.hasAggressiveAttackTarget()) {
					Vec3d vec3d = this.booflo.getMoveControllerPathDistance(this.posX, this.posY, this.posZ);
					
					this.booflo.rotationYaw = this.limitAngle(this.booflo.rotationYaw, this.booflo.getTargetAngleForPathDistance(vec3d), 10.0F);
					this.booflo.renderYawOffset = this.booflo.rotationYaw;
					this.booflo.rotationYawHead = this.booflo.rotationYaw;
					
					float f1 = (float)(2 * this.booflo.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue());
					float f2 = MathHelper.lerp(0.125F, this.booflo.getAIMoveSpeed(), f1);
					
					this.booflo.setAIMoveSpeed(f2);
				} else {
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
				}
				this.booflo.setMovingInAir(true);
			} else {
				this.booflo.setAIMoveSpeed(0F);
				this.booflo.setMovingInAir(false);
			}
		}
	}
	
	class FlyingLookController extends LookController {
		private final int angleLimit;

		public FlyingLookController(EntityBooflo booflo, int angleLimit) {
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
			if(wrappedDegrees < (float)(-this.angleLimit)) {
				this.mob.renderYawOffset -= 4.0F;
			} else if (wrappedDegrees > (float)this.angleLimit) {
				this.mob.renderYawOffset += 4.0F;
			}
			
			if(((EntityBooflo) this.mob).isAnimationPlaying(CHARGE)) {
				this.mob.rotationPitch = this.func_220675_a(this.mob.rotationPitch, 0.0F, 10.0F);
			}
		}
	}
}