package endergeticexpansion.common.entities.puffbug;

import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.Random;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import endergeticexpansion.api.endimator.ControlledEndimation;
import endergeticexpansion.api.endimator.Endimation;
import endergeticexpansion.api.endimator.entity.IEndimatedEntity;
import endergeticexpansion.api.entity.util.EndergeticFlyingPathNavigator;
import endergeticexpansion.api.entity.util.RayTraceHelper;
import endergeticexpansion.api.util.MathUtils;
import endergeticexpansion.api.util.NetworkUtil;
import endergeticexpansion.client.particle.EEParticles;
import endergeticexpansion.common.blocks.poise.BlockBolloomBud;
import endergeticexpansion.common.entities.puffbug.ai.*;
import endergeticexpansion.common.network.entity.puffbug.MessageRotate;
import endergeticexpansion.common.tileentities.TileEntityBolloomBud;
import endergeticexpansion.common.tileentities.TileEntityPuffBugHive;
import endergeticexpansion.common.tileentities.TileEntityBolloomBud.BudSide;
import endergeticexpansion.core.EndergeticExpansion;
import endergeticexpansion.core.registry.EEBlocks;
import endergeticexpansion.core.registry.EEEntities;
import endergeticexpansion.core.registry.EEItems;
import endergeticexpansion.core.registry.EESounds;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.BreedGoal;
import net.minecraft.entity.ai.goal.FollowParentGoal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.PacketDistributor;

public class EntityPuffBug extends AnimalEntity implements IEndimatedEntity {
	public static final Predicate<LivingEntity> CAN_ANGER = (entity) -> {
		if(entity instanceof PlayerEntity) {
			return !entity.isSpectator() && !((PlayerEntity) entity).isCreative();
		}
		return !entity.isSpectator() && !entity.isInvisible();
	};
	private static final DataParameter<Optional<BlockPos>> HIVE_POS = EntityDataManager.createKey(EntityPuffBug.class, DataSerializers.OPTIONAL_BLOCK_POS);
	private static final DataParameter<Direction> ATTACHED_HIVE_SIDE = EntityDataManager.createKey(EntityPuffBug.class, DataSerializers.DIRECTION);
	private static final DataParameter<Boolean> FROM_BOTTLE = EntityDataManager.createKey(EntityPuffBug.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> INFLATED = EntityDataManager.createKey(EntityPuffBug.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> BOOSTING = EntityDataManager.createKey(EntityPuffBug.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> COLOR = EntityDataManager.createKey(EntityPuffBug.class, DataSerializers.VARINT);
	
	public static final Endimation CLAIM_HIVE_ANIMATION = new Endimation(20);
	public static final Endimation PUFF_ANIMATION = new Endimation(20);
	public static final Endimation TELEPORT_TO_ANIMATION = new Endimation(15);
	public static final Endimation TELEPORT_FROM_ANIMATION = new Endimation(10);
	public static final Endimation ROTATE_ANIMATION = new Endimation(20);
	public static final Endimation POLLINATE_ANIMATION = new Endimation(120);
	public static final Endimation MAKE_ITEM_ANIMATION = new Endimation(100);
	
	public final ControlledEndimation HIVE_LANDING = new ControlledEndimation(20, 0);
	public final ControlledEndimation HIVE_SLEEP = new ControlledEndimation(25, 0);
	
	private TeleportController teleportController;
	private RotationController rotationController;
	private Endimation endimation = BLANK_ANIMATION;
	
	@Nullable
	private BlockPos budPos, pollinationPos;
	@Nullable
	private Direction teleportHiveSide, desiredHiveSide;
	
	@Nullable
	private ItemStack stackToCreate; 
	
	private int animationTick;
	private int teleportCooldown;
	public int ticksAwayFromHive;
	public int puffCooldown;
	
	public EntityPuffBug(EntityType<? extends EntityPuffBug> type, World worldIn) {
		super(type, worldIn);
		this.moveController = new PuffBugMoveController(this);
		this.teleportController = new TeleportController(this);
		this.rotationController = new RotationController(this);
		this.experienceValue = 2;
	}
	
	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		this.getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
		this.getAttributes().registerAttribute(SharedMonsterAttributes.FLYING_SPEED).setBaseValue(0.75F);
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0D);
	}
	
	@Override
	protected void registerData() {
		super.registerData();
		this.getDataManager().register(HIVE_POS, Optional.empty());
		this.getDataManager().register(ATTACHED_HIVE_SIDE, Direction.UP);
		this.getDataManager().register(COLOR, -1);
		this.getDataManager().register(FROM_BOTTLE, false);
		this.getDataManager().register(INFLATED, true);
		this.getDataManager().register(BOOSTING, false);
	}
	
	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new PuffBugRestOnHiveGoal(this));
		this.goalSelector.addGoal(1, new PuffBugAttachToHiveGoal(this));
		this.goalSelector.addGoal(2, new PuffBugCreateItemGoal(this));
		this.goalSelector.addGoal(3, new PuffBugTeleportToRestGoal(this));
		this.goalSelector.addGoal(4, new PuffBugPollinateGoal(this));
		this.goalSelector.addGoal(5, new PuffBugDescentGoal(this));
		this.goalSelector.addGoal(6, new PuffBugTeleportToBudGoal(this));
		this.goalSelector.addGoal(7, new BreedGoal(this, 1.0F));
		this.goalSelector.addGoal(8, new FollowParentGoal(this, 1.5F));
		this.goalSelector.addGoal(9, new PuffBugBoostGoal(this));
	}
	
	@Override
	public void tick() {
		super.tick();
		this.endimateTick();
		this.getRotationController().tick();
		this.keepEffectsAbsorbed();
		
		this.fallDistance = 0;
		
		if(!this.world.isRemote) {
			if(this.teleportCooldown > 0) {
				this.teleportCooldown--;
			}
			
			if(this.puffCooldown > 0) {
				this.puffCooldown--;
			}
			
			if(this.isInflated() && !this.getRotationController().rotating && this.isNoEndimationPlaying()) {
				if(this.isBoosting() && RayTraceHelper.rayTrace(this, 2.0D, 1.0F).getType() != Type.BLOCK || (this.onGround && this.puffCooldown <= 0 && this.getPollinationPos() == null)) {
					NetworkUtil.setPlayingAnimationMessage(this, PUFF_ANIMATION);
					this.playSound(this.getPuffSound(), 0.15F, this.getSoundPitch());
				}
			}
			
			if(this.isEndimationPlaying(TELEPORT_TO_ANIMATION) && this.getAnimationTick() == 10) {
				this.getTeleportController().bringToDestination();
			} else if(this.isEndimationPlaying(TELEPORT_FROM_ANIMATION)) {
				this.setMotion(Vec3d.ZERO);
			}
			
			if(this.getHivePos() == null) {
				if(this.getRNG().nextFloat() <= 0.05F) {
					TileEntityPuffBugHive hive = this.findNewNearbyHive();
					if(hive != null) {
						this.addToHive(hive);
						if(this.isNoEndimationPlaying()) {
							NetworkUtil.setPlayingAnimationMessage(this, CLAIM_HIVE_ANIMATION);
						}
					}
				}
				this.ticksAwayFromHive = 0;
			} else {
				if(this.getHive() == null) {
					this.setHivePos(null);
				} else {
					if(this.getAttachedHiveSide() == Direction.UP) {
						this.ticksAwayFromHive++;
						
						if(this.ticksAwayFromHive < 1500) {
							if(this.getRNG().nextInt(7500) == 0 && !this.isInLove() && !this.getHive().isHiveFull()) {
								for(EntityPuffBug nearbyHiveMembers : this.world.getEntitiesWithinAABB(EntityPuffBug.class, this.getBoundingBox().grow(9.0F), puffbug -> (puffbug.getHive() != null && puffbug.getHive() == this.getHive()) && this.getDistance(puffbug) < 12.0F)) {
									
									this.setInLove(1000);
									this.world.setEntityState(this, (byte) 18);
									
									nearbyHiveMembers.setInLove(1000);
									this.world.setEntityState(nearbyHiveMembers, (byte) 18);
									
									if(this.isInLove() || nearbyHiveMembers.isInLove()) break;
								}
							}
						}
					} else {
						this.ticksAwayFromHive = 0;
					}
				}
			}
			
			if(this.getAttachedHiveSide() != Direction.UP) {
				this.setInflated(false);
			} else if(!this.isInflated()) {
				this.setInflated(true);
			}
		} else {
			Random rand = this.getRNG();
			
			if(this.isEndimationPlaying(PUFF_ANIMATION) && this.getAnimationTick() == 5) {
				for(int i = 0; i < 3; i++) {
					double offsetX = MathUtils.makeNegativeRandomly(rand.nextFloat() * 0.1F, rand);
					double offsetZ = MathUtils.makeNegativeRandomly(rand.nextFloat() * 0.1F, rand);
    			
    				double x = this.posX + offsetX;
    				double y = this.posY + (rand.nextFloat() * 0.05F) + 0.7F;
    				double z = this.posZ + offsetZ;
    				
					this.world.addParticle(EEParticles.SHORT_POISE_BUBBLE.get(), x, y, z, MathUtils.makeNegativeRandomly((rand.nextFloat() * 0.1F), rand) + 0.05F, (rand.nextFloat() * 0.05F) + 0.025F, MathUtils.makeNegativeRandomly((rand.nextFloat() * 0.1F), rand) + 0.05F);
				}
			} else if(this.isEndimationPlaying(TELEPORT_TO_ANIMATION) && this.getAnimationTick() == 8) {
				for(int i = 0; i < 6; i++) {
					double offsetX = MathUtils.makeNegativeRandomly(rand.nextFloat() * 0.1F, rand);
					double offsetZ = MathUtils.makeNegativeRandomly(rand.nextFloat() * 0.1F, rand);
				
					double x = this.posX + offsetX;
					double y = this.posY + (rand.nextFloat() * 0.05F) + 0.7F;
					double z = this.posZ + offsetZ;
					
					this.world.addParticle(EEParticles.SHORT_POISE_BUBBLE.get(), x, y, z, MathUtils.makeNegativeRandomly((rand.nextFloat() * 0.15F), rand) + 0.025F, (rand.nextFloat() * 0.025F) + 0.025F, MathUtils.makeNegativeRandomly((rand.nextFloat() * 0.15F), rand) + 0.025F);
				}
			} else if(this.isEndimationPlaying(TELEPORT_FROM_ANIMATION) && this.getAnimationTick() == 5) {
				for(int i = 0; i < 6; i++) {
					double offsetX = MathUtils.makeNegativeRandomly(rand.nextFloat() * 0.1F, rand);
					double offsetZ = MathUtils.makeNegativeRandomly(rand.nextFloat() * 0.1F, rand);
			
					double x = this.posX + offsetX;
					double y = this.posY + (rand.nextFloat() * 0.05F) + 0.7F;
					double z = this.posZ + offsetZ;
				
					this.world.addParticle(EEParticles.SHORT_POISE_BUBBLE.get(), x, y, z, MathUtils.makeNegativeRandomly((rand.nextFloat() * 0.15F), rand) + 0.025F, (rand.nextFloat() * 0.025F) + 0.025F, MathUtils.makeNegativeRandomly((rand.nextFloat() * 0.15F), rand) + 0.025F);
				}
			}
			
			this.HIVE_LANDING.update();
			this.HIVE_SLEEP.update();
			
			this.HIVE_LANDING.tick();
			this.HIVE_SLEEP.tick();
			
			this.HIVE_LANDING.setDecrementing(this.getAttachedHiveSide() == Direction.UP);
			
			if(this.HIVE_LANDING.isAtMax()) {
				if(this.HIVE_SLEEP.isDescrementing() && this.HIVE_SLEEP.getTick() == 0) {
					this.HIVE_SLEEP.setDecrementing(false);
				} else if(this.HIVE_SLEEP.isAtMax()) {
					this.HIVE_SLEEP.setDecrementing(true);
				}
			} else {
				this.HIVE_SLEEP.setDecrementing(true);
			}
		}
		
		if(this.getDesiredHiveSide() != null) {
			if(this.getHive() != null && this.getDesiredHiveSide() != Direction.DOWN) {
				float degrees = (this.getDesiredHiveSide() == Direction.SOUTH ? 0 : this.getDesiredHiveSide().getAxisDirection().getOffset()) * (this.getDesiredHiveSide().getAxis() == Axis.Z ? 180.0F : -90.0F);
				this.rotationYaw = this.prevRotationYaw = degrees;
				this.rotationYawHead = this.prevRotationYawHead = degrees;
				this.renderYawOffset = this.prevRenderYawOffset = degrees;
			}
		}
		
		if(this.getAttachedHiveSide() != Direction.UP) {
			if(this.getHive() != null) {
				this.setMotion(this.getMotion().mul(1.0F, 0.0F, 1.0F));
				this.getNavigator().clearPath();
				this.setAIMoveSpeed(0.0F);
				
				this.addVelocity(0.0F, 0.0025F, 0.0F);
				
				this.rotationYaw = this.prevRotationYaw;
				this.rotationYawHead = this.prevRotationYawHead;
				this.renderYawOffset = this.prevRenderYawOffset;
				
				if(!this.world.isRemote && !this.isAtCorrectRestLocation(this.getAttachedHiveSide())) {
					this.setAttachedHiveSide(Direction.UP);
				}
			} else {
				if(!this.world.isRemote) {
					this.setAttachedHiveSide(Direction.UP);
				}
			}
		}
	}
	
	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		
		this.setAttachedHiveSide(Direction.byIndex(compound.getByte("AttachedHiveSide")));
		this.setFromBottle(compound.getBoolean("FromBottle"));
		
		this.teleportCooldown = compound.getInt("TeleportCooldown");
		this.ticksAwayFromHive = compound.getInt("TicksAwayFromHive");
		
		if(compound.contains("IsInflated")) {
			this.setInflated(compound.getBoolean("IsInflated"));
		}
		
		if(compound.contains("HivePos", 10)) {
			this.setHivePos(NBTUtil.readBlockPos(compound.getCompound("HivePos")));
		}
		
		CompoundNBT stackToCreate = compound.getCompound("ItemStackToCreate");
		
		if(stackToCreate != null) {
			ItemStack newStackToCreate = ItemStack.read(stackToCreate);
			if(!newStackToCreate.isEmpty()) {
				this.setStackToCreate(newStackToCreate);
			}
		}
		
		this.rotationController = this.getRotationController().read(this, compound.getCompound("Orientation"));
	}
	
	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		
		compound.putByte("AttachedHiveSide", (byte) this.getAttachedHiveSide().getIndex());
		compound.putBoolean("FromBottle", this.isFromBottle());
		compound.putBoolean("IsInflated", this.isInflated());
		
		compound.putInt("TeleportCooldown", this.teleportCooldown);
		compound.putInt("TicksAwayFromHive", this.ticksAwayFromHive);
		
		if(this.getHivePos() != null) {
			compound.put("HivePos", NBTUtil.writeBlockPos(this.getHivePos()));
		}
		
		if(this.hasStackToCreate()) {
			compound.put("ItemStackToCreate", this.getStackToCreate().write(new CompoundNBT()));
		}
		
		compound.put("Orientation", this.getRotationController().write(new CompoundNBT()));
	}
	
	@Nullable
	public BlockPos getHivePos() {
		return this.getDataManager().get(HIVE_POS).orElse(null);
	}
	
	public void setHivePos(@Nullable BlockPos pos) {
		this.getDataManager().set(HIVE_POS, Optional.ofNullable(pos));
	}
	
	@Nullable
	public TileEntityPuffBugHive getHive() {
		BlockPos hivePos = this.getHivePos();
		if(hivePos != null) {
			try {
				TileEntity tileEntity = this.world.getTileEntity(hivePos).getTileEntity();
				if(tileEntity instanceof TileEntityPuffBugHive) {
					return (TileEntityPuffBugHive) tileEntity;
				}
			} catch(NullPointerException e) {
				return null;
			}
		}
		return null;
	}
	
	@Nullable
	public BlockPos getBudPos() {
		return this.budPos;
	}
	
	public void setBudPos(@Nullable BlockPos pos) {
		this.budPos = pos;
	}
	
	@Nullable
	public BlockPos getPollinationPos() {
		return this.pollinationPos;
	}
	
	public void setPollinationPos(@Nullable BlockPos pos) {
		this.pollinationPos = pos;
	}
	
	public boolean canAttachToSide(Direction direction) {
		if(direction == Direction.UP) {
			return false;
		}
		return this.getHive() != null && TileEntityPuffBugHive.HiveOccupantData.isHiveSideEmpty(this.getHive(), direction);
	}
	
	/*
	 * Up is considered null
	 */
	public Direction getAttachedHiveSide() {
		return this.dataManager.get(ATTACHED_HIVE_SIDE);
	}
	
	public void setAttachedHiveSide(Direction side) {
		this.dataManager.set(ATTACHED_HIVE_SIDE, side);
	}
	
	@Nullable
	public Direction getTeleportHiveSide() {
		return this.teleportHiveSide;
	}
	
	public void setTeleportHiveSide(@Nullable Direction side) {
		this.teleportHiveSide = side;
	}
	
	@Nullable
	public Direction getDesiredHiveSide() {
		return this.desiredHiveSide;
	}
	
	public void setDesiredHiveSide(@Nullable Direction side) {
		this.desiredHiveSide = side;
	}
	
	public boolean isFromBottle() {
		return this.dataManager.get(FROM_BOTTLE);
	}

	public void setFromBottle(boolean value) {
		this.dataManager.set(FROM_BOTTLE, value);
	}
	
	public boolean isInflated() {
		return this.dataManager.get(INFLATED);
	}

	public void setInflated(boolean inflated) {
		this.dataManager.set(INFLATED, inflated);
	}
	
	public boolean isBoosting() {
		return this.dataManager.get(BOOSTING);
	}

	public void setBoosting(boolean boosting) {
		this.dataManager.set(BOOSTING, boosting);
	}
	
	public int getColor() {
		return this.dataManager.get(COLOR);
	}
	
	public void setColor(int color) {
		this.dataManager.set(COLOR, color);
	}
	
	public void setStackToCreate(@Nullable ItemStack stack) {
		this.stackToCreate = stack;
	}
	
	@Nullable
	public ItemStack getStackToCreate() {
		return this.stackToCreate;
	}
	
	public boolean hasStackToCreate() {
		return this.getStackToCreate() != null;
	}
	
	public boolean hasLevitation() {
		return this.isPotionActive(Effects.LEVITATION);
	}
	
	@Override
	public boolean hasNoGravity() {
		return super.hasNoGravity() || this.getAttachedHiveSide() != Direction.UP;
	}
	
	public TeleportController getTeleportController() {
		return this.teleportController;
	}
	
	public RotationController getRotationController() {
		return this.rotationController;
	}
	
	@Override
	public Endimation[] getEndimations() {
		return new Endimation[] {
			CLAIM_HIVE_ANIMATION,
			PUFF_ANIMATION,
			TELEPORT_TO_ANIMATION,
			TELEPORT_FROM_ANIMATION,
			ROTATE_ANIMATION,
			POLLINATE_ANIMATION,
			MAKE_ITEM_ANIMATION
		};
	}
	
	@Override
	public void onEndimationStart(Endimation endimation) {
		if(endimation == PUFF_ANIMATION) {
			float pitch = this.isBeingRidden() ? 1.0F : this.rotationPitch;
			float xMotion = -MathHelper.sin(this.rotationYaw * ((float) Math.PI / 180F)) * MathHelper.cos(pitch * ((float) Math.PI / 180F));
			float zMotion = MathHelper.cos(this.rotationYaw * ((float) Math.PI / 180F)) * MathHelper.cos(pitch * ((float) Math.PI / 180F));
			
			Vec3d motion = new Vec3d(xMotion, 0.65F, zMotion).normalize();
			
			this.addVelocity(motion.getX() * (this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue() - 0.1F), motion.getY(), motion.getZ() * (this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue() - 0.1F));
		} else if(endimation == TELEPORT_TO_ANIMATION) {
			if(!this.world.isRemote) {
				this.playSound(this.getTeleportSound(false), 0.65F, this.getSoundPitch());
			}
		}
	}
	
	@Override
	public void onEndimationEnd(Endimation endimation) {
		if(endimation == TELEPORT_TO_ANIMATION) {
			if(!this.world.isRemote) {
				NetworkUtil.setPlayingAnimationMessage(this, TELEPORT_FROM_ANIMATION);
				this.playSound(this.getTeleportSound(true), 0.65F, this.getSoundPitch());
			}
		} else if(endimation == POLLINATE_ANIMATION) {
			this.addPotionEffect(new EffectInstance(Effects.LEVITATION, 3000));
			if(this.getPollinationPos() != null) {
				TileEntity te = this.world.getTileEntity(this.getPollinationPos());
				if(te instanceof TileEntityBolloomBud) {
					TileEntityBolloomBud bud = (TileEntityBolloomBud) te;
					if(bud.canBeOpened()) {
						for(BudSide side : BudSide.values()) {
							BlockPos sidePos = side.offsetPosition(this.getPollinationPos());
							if(this.world.getBlockState(sidePos).getCollisionShape(world, this.getPollinationPos()).isEmpty()) {
								this.world.destroyBlock(sidePos, true);
							}
						}
					
						this.world.setBlockState(this.getPollinationPos(), world.getBlockState(this.getPollinationPos()).with(BlockBolloomBud.OPENED, true));
						bud.startGrowing(this.getRNG(), bud.calculateFruitMaxHeight(), false);
					}
				}
			}
		}
	}
	
	@Override
	public void travel(Vec3d moveDirection) {
		if(this.isServerWorld() && this.isInflated()) {
			double gravity = this.hasLevitation() ? -0.005D : 0.005D;
			float speed = this.onGround ? 0.01F : 0.025F;
			
			this.moveRelative(speed, moveDirection);
			this.move(MoverType.SELF, this.getMotion());
			this.setMotion(this.getMotion().scale(0.75D));
			this.setMotion(this.getMotion().subtract(0, gravity, 0));
		} else {
			if(!this.isInflated()) {
				this.setMotion(this.getMotion().mul(1.0F, 0.0F, 1.0F));
			}
			super.travel(Vec3d.ZERO);
			if(!this.isInflated()) {
				this.setMotion(this.getMotion().mul(1.0F, 0.0F, 1.0F));
			}
		}
	}
	
	public void addToHive(TileEntityPuffBugHive hive) {
		hive.addBugToHive(this);
		this.setHivePos(hive.getPos());
	}
	
	@Nullable
	public TileEntityPuffBugHive findNewNearbyHive() {
		BlockPos pos = this.getPosition();
		double xyDistance = 16.0D;
		for(BlockPos blockpos : BlockPos.getAllInBoxMutable(pos.add(-xyDistance, -6.0D, -xyDistance), pos.add(xyDistance, 6.0D, xyDistance))) {
			if(blockpos.withinDistance(this.getPositionVec(), xyDistance)) {
				if(this.world.getBlockState(blockpos).getBlock() == EEBlocks.PUFFBUG_HIVE.get() && this.world.getTileEntity(blockpos) instanceof TileEntityPuffBugHive) {
					TileEntityPuffBugHive hive = (TileEntityPuffBugHive) this.world.getTileEntity(blockpos);
					if(!hive.isHiveFull() && this.getHive() == null) {
						return hive;
					}
				}
			}
		}
		return null;
	}
	
	protected void setBottleData(ItemStack bottle) {
		if(this.hasCustomName()) {
			bottle.setDisplayName(this.getCustomName());
		}
		
		CompoundNBT nbt = bottle.getOrCreateTag();
		
		if(this.getColor() != -1) {
			nbt.putInt("ColorTag", this.getColor());
		}
		
		nbt.putInt("TeleportCooldown", this.teleportCooldown);
		
		if(!this.getActivePotionEffects().isEmpty()) {
			ListNBT listnbt = new ListNBT();

			for(EffectInstance effectinstance : this.getActivePotionEffects()) {
				listnbt.add(effectinstance.write(new CompoundNBT()));
			}

			nbt.put("CustomPotionEffects", listnbt);
		}
		
		nbt.putBoolean("IsFromBottle", true);
		nbt.putBoolean("IsChild", this.isChild());
	}
	
	private void keepEffectsAbsorbed() {
		Iterator<Effect> iterator = this.getActivePotionMap().keySet().iterator();
		while(iterator.hasNext()) {
            Effect effect = iterator.next();
            if(effect != Effects.LEVITATION) {
            	EffectInstance effectInstance = this.getActivePotionMap().get(effect);
            	this.getActivePotionMap().put(effect, new EffectInstance(effect, 100, effectInstance.getAmplifier(), effectInstance.isAmbient(), effectInstance.doesShowParticles()));
            }
		}
	}
	
	public boolean wantsToRest() {
		return this.ticksAwayFromHive >= 3000;
	}
	
	public boolean isAtCorrectRestLocation(Direction side) {
		TileEntity te = side == Direction.DOWN ? this.isChild() ? this.world.getTileEntity(this.getPosition().up(1)) : this.world.getTileEntity(this.getPosition().up(2)) : this.isChild() ? this.world.getTileEntity(this.getPosition().offset(side.getOpposite())) : this.world.getTileEntity(this.getPosition().up(1).offset(side.getOpposite()));
		if(te != this.getHive()) {
			return false;
		}
		
		switch(side) {
			case UP:
				return false;
			case DOWN:
				float yOffsetDown = this.isChild() ? 0.45F : -0.15F;
				return new Vec3d(this.getHivePos().down()).add(0.5F, yOffsetDown, 0.5F).distanceTo(this.getPositionVec()) < 0.25F;
			default:
				float yOffset = this.isChild() ? 0.2F : -0.2F;
				return this.world.isAirBlock(this.getHivePos().offset(side).up()) && this.world.isAirBlock(this.getHivePos().offset(side).down())
					&& new Vec3d(this.getHivePos().offset(side)).add(this.getTeleportController().getOffsetForDirection(side)[0], yOffset, this.getTeleportController().getOffsetForDirection(side)[1]).distanceTo(this.getPositionVec()) < (this.isChild() ? 0.1F : 0.25F);
		}
	}
	
	@Override
	protected PathNavigator createNavigator(World worldIn) {
		return new EndergeticFlyingPathNavigator(this, worldIn);
	}
	
	@Override
	public boolean canBreed() {
		return super.canBreed() && this.isInflated();
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public boolean isInRangeToRenderDist(double distance) {
		double boundingBoxLength = this.getBoundingBox().getAverageEdgeLength() * 2.5D;
		if(Double.isNaN(boundingBoxLength)) {
			boundingBoxLength = 1.0D;
		}

		boundingBoxLength = boundingBoxLength * 64.0D * getRenderDistanceWeight();
		return distance < boundingBoxLength * boundingBoxLength;
	}
	
	@Override
	public void fall(float distance, float damageMultiplier) {}
	
	@Override
	protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
		return sizeIn.height * 0.275F;
	}
	
	@Override
	protected void updatePotionMetadata() {
		super.updatePotionMetadata();
		Collection<EffectInstance> effects = this.getActivePotionEffects();
		
		if(!effects.isEmpty()) {
			this.setColor(PotionUtils.getPotionColorFromEffectList(effects));
		} else {
			this.setColor(-1);
		}
	}
	
	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(EEItems.PUFF_BUG_SPAWN_EGG.get());
	}
	
	@Override
	public boolean isBreedingItem(ItemStack stack) {
		return stack.getItem() == EEBlocks.POISE_GRASS_TALL.get().asItem();
	}
	
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return super.getRenderBoundingBox().grow(12);
	}
	
	@Override
	public boolean processInteract(PlayerEntity player, Hand hand) {
		ItemStack itemstack = player.getHeldItem(hand);
		Item item = itemstack.getItem();
		
		if(!this.isAlive() || this.isAggressive()) return false;
	
		if(item == Items.GLASS_BOTTLE) {
			this.playSound(SoundEvents.ITEM_BOTTLE_FILL_DRAGONBREATH, 1.0F, 1.0F);
			itemstack.shrink(1);
			ItemStack bottle = new ItemStack(EEItems.PUFFBUG_BOTTLE.get());
			this.setBottleData(bottle);
        	
			if(itemstack.isEmpty()) {
				player.setHeldItem(hand, bottle);
			} else if(!player.inventory.addItemStackToInventory(bottle)) {
				player.dropItem(bottle, false);
			}

			this.remove();
			return true;
		} else if(!this.hasStackToCreate() && this.hasLevitation()) {
			ItemStack newStackToCreate = item == EEItems.BOLLOOM_FRUIT.get() ? new ItemStack(EEBlocks.BOLLOOM_BUD.get()) : this.isBreedingItem(itemstack) ? new ItemStack(EEBlocks.PUFFBUG_HIVE.get()) : null;
			if(newStackToCreate != null) {
				this.setStackToCreate(newStackToCreate);
				this.consumeItemFromStack(player, itemstack);
				return true;
			}
			return false;
		} else {
			return super.processInteract(player, hand);
		}
	}
	
	@Override
	public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, ILivingEntityData spawnDataIn, CompoundNBT dataTag) {
		Random rng = this.getRNG();
		
		if(dataTag != null) {
			int age = dataTag.getBoolean("IsChild") ? -24000 : 0;
			
			this.setGrowingAge(age);
			this.teleportCooldown = dataTag.getInt("TeleportCooldown");
			this.setFromBottle(dataTag.getBoolean("IsFromBottle"));
			
			if(dataTag.contains("ColorTag", 3)) {
				this.setColor(dataTag.getInt("ColorTag"));
			}
			
			if(dataTag.contains("CustomPotionEffects")) {
				for(EffectInstance effectinstance : PotionUtils.getFullEffectsFromTag(dataTag)) {
					this.addPotionEffect(effectinstance);
				}
			}
		}
		
		if(reason == SpawnReason.STRUCTURE) {
			this.ticksAwayFromHive = rng.nextInt(1500) + 1500;
			
			if(rng.nextFloat() < 0.1F) {
				this.growingAge = -24000;
			}
		} else if(reason == SpawnReason.NATURAL || reason == SpawnReason.SPAWNER) {
			if(rng.nextFloat() < 0.2F && this.world.areCollisionShapesEmpty(this.getBoundingBox().grow(6.0D))) {
				int swarmSize = rng.nextInt(11) + 8;
				for(int i = 0; i < swarmSize; i++) {
					Vec3d spawnPos = new Vec3d(this.getPosition()).add(MathUtils.makeNegativeRandomly(rng.nextFloat() * 5.5F, rng), MathUtils.makeNegativeRandomly(rng.nextFloat() * 2.0F, rng), MathUtils.makeNegativeRandomly(rng.nextFloat() * 5.5F, rng));
					
					EntityPuffBug swarmChild = EEEntities.PUFF_BUG.get().create(this.world);
					swarmChild.setLocationAndAngles(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), 0.0F, 0.0F);
					swarmChild.onInitialSpawn(this.world, this.world.getDifficultyForLocation(new BlockPos(spawnPos)), SpawnReason.EVENT, null, null);
					swarmChild.setGrowingAge(-24000);
					
					this.world.addEntity(swarmChild);
				}
			}
		}
		
		return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
	}
	
	@Override
	public boolean canDespawn(double distanceToClosestPlayer) {
		return this.getHive() == null && !this.isFromBottle() && !this.hasCustomName();
	}

	@Override
	public boolean preventDespawn() {
    	return this.isFromBottle();
	}
	
	@Override
	public boolean isOnLadder() {
		return false;
	}
	
	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	public AgeableEntity createChild(AgeableEntity ageable) {
		return EEEntities.PUFF_BUG.get().create(this.world);
	}
	
	public SoundEvent getPuffSound() {
		return EESounds.PUFFBUG_PUFF.get();
	}
	
	public SoundEvent getTeleportSound(boolean from) {
		return from ? EESounds.PUFFBUG_TELEPORT_FROM.get() : EESounds.PUFFBUG_TELEPORT_TO.get();
	}
	
	public SoundEvent getPollinateSound() {
		return EESounds.PUFFBUG_POLLINATE.get();
	}
	
	public SoundEvent getSleepSound() {
		return EESounds.PUFFBUG_SLEEP.get();
	}
	
	public SoundEvent getHiveCreationSound() {
		return EESounds.PUFFBUG_CREATE_HIVE.get();
	}
	
	public SoundEvent getLaunchSound() {
		return EESounds.PUFFBUG_LAUNCH.get();
	}
	
	public SoundEvent getLandSound() {
		return EESounds.PUFFBUG_LAND.get();
	}
	
	@Override
	protected SoundEvent getDeathSound() {
		return EESounds.PUFFBUG_DEATH.get();
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource damageSource) {
		return EESounds.PUFFBUG_HURT.get();
	}
	
	@Override
	public Endimation getPlayingEndimation() {
		return this.endimation;
	}

	@Override
	public void setPlayingEndimation(Endimation endimationToPlay) {
		this.endimation = endimationToPlay;
		this.setAnimationTick(0);
	}

	@Override
	public int getAnimationTick() {
		return this.animationTick;
	}

	@Override
	public void setAnimationTick(int animationTick) {
		this.animationTick = animationTick;
	}
	
	static class PuffBugMoveController extends MovementController {
		private final EntityPuffBug puffbug;

		PuffBugMoveController(EntityPuffBug puffbug) {
			super(puffbug);
			this.puffbug = puffbug;
		}

		public void tick() {
			if(this.action == MovementController.Action.MOVE_TO && !this.puffbug.getNavigator().noPath()) {
				double xDistance = this.posX - this.puffbug.posX;
				double yDistance = this.posY - this.puffbug.posY;
				double zDistance = this.posZ - this.puffbug.posZ;
				double totalDistance = (double) MathHelper.sqrt(xDistance * xDistance + yDistance * yDistance + zDistance * zDistance);
				
				double verticalVelocity = yDistance / totalDistance;
				
				float angle = (float) (MathHelper.atan2(zDistance, xDistance) * (180F / Math.PI)) - 90.0F;
				
				this.puffbug.rotationYaw = this.limitAngle(this.puffbug.rotationYaw, angle, 20.0F);
				this.puffbug.renderYawOffset = this.puffbug.rotationYaw;
				
				float speed = (float) (this.speed * this.puffbug.getAttribute(SharedMonsterAttributes.FLYING_SPEED).getValue());
				
				if(verticalVelocity < 0.0F) {
					this.puffbug.setAIMoveSpeed(MathHelper.lerp(0.125F, this.puffbug.getAIMoveSpeed(), speed));
					this.puffbug.setMotion(this.puffbug.getMotion().add(0.0D, (double) this.puffbug.getAIMoveSpeed() * verticalVelocity * 0.05D, 0.0D));
					this.puffbug.setBoosting(false);
				} else {
					this.puffbug.setBoosting(true);
				}
			} else { 
				this.puffbug.setAIMoveSpeed(0.0F);
				this.puffbug.setBoosting(false);
			}
		}
	}
	
	public static class TeleportController {
		private EntityPuffBug puffbug;
		private World world;
		@Nullable
		private BlockPos destination;
		
		TeleportController(EntityPuffBug puffbug) {
			this.puffbug = puffbug;
			this.world = puffbug.world;
		}
		
		public void processTeleportation() {
			if(!this.hasNoDestination() && !this.world.isRemote) {
				this.puffbug.getNavigator().clearPath();
				NetworkUtil.setPlayingAnimationMessage(this.puffbug, EntityPuffBug.TELEPORT_TO_ANIMATION);
				
				this.puffbug.teleportCooldown = this.puffbug.getRNG().nextInt(300) + 1200;
			}
		}
		
		protected void bringToDestination() {
			if(!this.world.isRemote) {
				Direction side = this.puffbug.getTeleportHiveSide();
				
				float xOffset = side == null || side == Direction.DOWN ? 0.5F : this.getOffsetForDirection(side)[0];
				float yOffset = side == Direction.DOWN ? this.puffbug.isChild() ? 0.45F : -0.15F : this.puffbug.isChild() ? 0.2F : -0.2F;
				float zOffset = side == null || side == Direction.DOWN ? 0.5F : this.getOffsetForDirection(side)[1];
				
				if(side == null) {
					NetworkUtil.teleportEntity(this.puffbug, this.destination.getX() + 0.5F, this.destination.getY() + 0.5F, this.destination.getZ() + 0.5F);
				} else {
					NetworkUtil.teleportEntity(this.puffbug, this.destination.getX() + xOffset, this.destination.getY() + yOffset, this.destination.getZ() + zOffset);
				}
				
				this.destination = null;
			
				this.puffbug.getNavigator().clearPath();
				this.puffbug.setMotion(Vec3d.ZERO);
				
				if(side != null) {
					this.puffbug.setDesiredHiveSide(side);
					this.puffbug.setTeleportHiveSide(null);
				}
			}
		}
		
		@Nullable
		public boolean tryToCreateDesinationTo(BlockPos pos, @Nullable Direction direction) {
			boolean directionFlag = direction != null ? this.world.isAirBlock(pos.offset(direction)) : true;
			
			if(direction != null && direction != Direction.DOWN) {
				if(!this.world.isAirBlock(pos.up()) || !this.world.isAirBlock(pos.down())) {
					directionFlag = false;
				}
			}
			
			if(this.puffbug.ticksExisted > 5 && this.world.isAirBlock(pos) && directionFlag && this.hasNoDestination()) {
				if(this.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos)).isEmpty()) {
					this.destination = pos;
					return true;
				}
			}
			return false;
		}
		
		public boolean hasNoDestination() {
			return this.destination == null;
		}
		
		public boolean canTeleport() {
			return this.destination == null && this.puffbug.teleportCooldown <= 0;
		}
		
		protected float[] getOffsetForDirection(Direction side) {
			switch(side) {
				default:
				case NORTH:
					return this.puffbug.isChild() ? new float[] {0.5F, 0.85F} : new float[] {0.5F, 0.75F};
				case SOUTH:
					return this.puffbug.isChild() ? new float[] {0.5F, 0.15F} : new float[] {0.5F, 0.25F};
				case EAST:
					return this.puffbug.isChild() ? new float[] {0.15F, 0.5F} : new float[] {0.25F, 0.5F};
				case WEST:
					return this.puffbug.isChild() ? new float[] {0.85F, 0.5F} : new float[] {0.75F, 0.5F};
			}
		}
	}
	
	public static class RotationController {
		private EntityPuffBug puffbug;
		private float prevYaw, yaw, startingYaw;
		private float prevPitch, pitch, startingPitch;
		private float prevRoll, roll, startingRoll;
		private float setYaw, setPitch, setRoll;
		private int tickLength;
		private int ticksSinceNotRotating;
		private boolean rotating;
		
		RotationController(EntityPuffBug puffbug) {
			this.puffbug = puffbug;
		}
		
		protected void tick() {
			this.prevYaw = this.yaw;
			this.prevPitch = this.pitch;
			this.prevRoll = this.roll;
			
			if(!this.rotating) {
				if(this.ticksSinceNotRotating > 5) {
					if(this.setYaw != 0.0F) {
						this.startingYaw = this.yaw;
					}
				
					if(this.setPitch != 0.0F) {
						this.startingPitch = this.pitch;
						if(this.puffbug.isNoEndimationPlaying()) {
							NetworkUtil.setPlayingAnimationMessage(this.puffbug, EntityPuffBug.ROTATE_ANIMATION);
						}
					}
					
					if(this.setRoll != 0.0F) {
						this.startingRoll = this.roll;
					}
				
					this.setYaw = 0.0F;
					this.setPitch = 0.0F;
					this.setRoll = 0.0F;
					this.tickLength = 20;	
				}
				this.ticksSinceNotRotating++;
			}
			
			this.yaw = this.clamp((this.setYaw - this.startingYaw) <= 0, this.yaw + ((this.setYaw - this.startingYaw) / this.tickLength), this.startingYaw, this.setYaw);
			this.pitch = this.clamp((this.setPitch - this.startingPitch) <= 0, this.pitch + ((this.setPitch - this.startingPitch) / this.tickLength), this.startingPitch, this.setPitch);
			this.roll = this.clamp((this.setRoll - this.startingRoll) <= 0, this.roll + ((this.setRoll - this.startingRoll) / this.tickLength), this.startingRoll, this.setRoll);
			
			this.rotating = false;
		}
		
		private float clamp(boolean invert, float num, float min, float max) {
			if(invert) {
				return num > max ? num : max;
			} else {
				if(num < min) {
					return min;
				} else {
					return num > max ? max : num;
				}
			}
		}
		
		public void rotate(float yaw, float pitch, float roll, int tickLength) {
			if(this.setYaw != yaw) {
				this.startingYaw = this.yaw;
			}
			
			if(this.setPitch != pitch) {
				this.startingPitch = this.pitch;
				if(tickLength >= 20) {
					NetworkUtil.setPlayingAnimationMessage(this.puffbug, EntityPuffBug.ROTATE_ANIMATION);
				}
			}
			
			if(this.setRoll != roll) {
				this.startingRoll = this.roll;
			}
			
			this.setYaw = yaw;
			this.setPitch = pitch;
			this.setRoll = roll;
			this.tickLength = tickLength;
			this.rotating = true;
			this.ticksSinceNotRotating = 0;
			
			if(!this.puffbug.world.isRemote) {
				EndergeticExpansion.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> this.puffbug), new MessageRotate(this.puffbug.getEntityId(), tickLength, yaw, pitch, roll));
			}
		}
		
		protected CompoundNBT write(CompoundNBT compound) {
			compound.putFloat("Yaw", this.yaw);
			compound.putFloat("Pitch", this.pitch);
			compound.putFloat("Roll", this.roll);
			compound.putFloat("SetYaw", this.setYaw);
			compound.putFloat("SetPitch", this.setPitch);
			compound.putFloat("SetRoll", this.roll);
			compound.putFloat("StartingYaw", this.startingYaw);
			compound.putFloat("StartingPitch", this.startingPitch);
			compound.putFloat("StartingRoll", this.startingRoll);
			compound.putInt("TickLength", this.tickLength);
			compound.putBoolean("Rotating", this.rotating);
			return compound;
		}
		
		protected RotationController read(EntityPuffBug puffbug, CompoundNBT compound) {
			RotationController rotationController = new RotationController(puffbug);
			
			rotationController.yaw = rotationController.prevYaw = compound.getFloat("Yaw");
			rotationController.pitch = rotationController.prevPitch = compound.getFloat("Pitch");
			rotationController.roll = rotationController.prevRoll = compound.getFloat("Roll");
			rotationController.setYaw = compound.getFloat("SetYaw");
			rotationController.setPitch = compound.getFloat("SetPitch");
			rotationController.setRoll = compound.getFloat("SetRoll");
			rotationController.startingYaw = compound.getFloat("StartingYaw");
			rotationController.startingPitch = compound.getFloat("StartingPitch");
			rotationController.startingRoll = compound.getFloat("StartingRoll");
			rotationController.tickLength = compound.getInt("TickLength");
			rotationController.rotating = compound.getBoolean("Rotating");
			
			return rotationController;
		}
		
		public float[] getRotations(float ptc) {
			return new float[] {MathHelper.lerp(ptc, this.prevYaw, this.yaw), MathHelper.lerp(ptc, this.prevPitch, this.pitch), MathHelper.lerp(ptc, this.prevRoll, this.roll)}; 
		}
	}
}