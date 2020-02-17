package endergeticexpansion.common.entities.puffbug;

import java.util.Collection;
import java.util.Optional;
import java.util.Random;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import endergeticexpansion.api.endimator.Endimation;
import endergeticexpansion.api.endimator.entity.IEndimatedEntity;
import endergeticexpansion.api.entity.util.EndergeticFlyingPathNavigator;
import endergeticexpansion.api.entity.util.RayTraceHelper;
import endergeticexpansion.api.util.MathUtils;
import endergeticexpansion.api.util.NetworkUtil;
import endergeticexpansion.client.particle.EEParticles;
import endergeticexpansion.common.entities.puffbug.ai.*;
import endergeticexpansion.common.network.entity.puffbug.MessageRotate;
import endergeticexpansion.common.tileentities.TileEntityPuffBugHive;
import endergeticexpansion.core.EndergeticExpansion;
import endergeticexpansion.core.registry.EEBlocks;
import endergeticexpansion.core.registry.EEEntities;
import endergeticexpansion.core.registry.EEItems;
import endergeticexpansion.core.registry.EESounds;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
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
import net.minecraftforge.fml.network.PacketDistributor;

public class EntityPuffBug extends AnimalEntity implements IEndimatedEntity {
	public static final Predicate<LivingEntity> CAN_ANGER = (entity) -> {
		if(entity instanceof PlayerEntity) {
			return !entity.isSpectator() && !((PlayerEntity) entity).isCreative();
		}
		return !entity.isSpectator() && !entity.isInvisible();
	};
	private static final DataParameter<Optional<BlockPos>> HIVE_POS = EntityDataManager.createKey(EntityPuffBug.class, DataSerializers.OPTIONAL_BLOCK_POS);
	private static final DataParameter<Optional<BlockPos>> BUD_POS = EntityDataManager.createKey(EntityPuffBug.class, DataSerializers.OPTIONAL_BLOCK_POS);
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
	private TeleportController teleportController;
	private RotationController rotationController;
	private Endimation endimation = BLANK_ANIMATION;
	private int animationTick;
	private int teleportCooldown;
	
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
		this.getDataManager().register(BUD_POS, Optional.empty());
		this.getDataManager().register(ATTACHED_HIVE_SIDE, Direction.UP);
		this.getDataManager().register(COLOR, -1);
		this.getDataManager().register(FROM_BOTTLE, false);
		this.getDataManager().register(INFLATED, true);
		this.getDataManager().register(BOOSTING, false);
	}	
	
	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new PuffBugTeleportToBudGoal(this));
		this.goalSelector.addGoal(3, new PuffBugDescentGoal(this));
		this.goalSelector.addGoal(5, new PuffBugBoostGoal(this));
	}
	
	@Override
	public void tick() {
		super.tick();
		this.endimateTick();
		this.getRotationController().tick();
		
		this.fallDistance = 0;
		
		if(!this.world.isRemote) {
			if(this.teleportCooldown > 0) {
				this.teleportCooldown--;
			}
			
			if(this.isInflated() && !this.getRotationController().rotating && this.isNoEndimationPlaying()) {
				if(this.isBoosting() && RayTraceHelper.rayTrace(this, 2.0D, 1.0F).getType() != Type.BLOCK || this.onGround) {
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
			} else {
				if(this.getHive() == null) {
					this.setHivePos(null);
				} else {
					if(this.getAttachedHiveSide() == null){
						
					}
				}
			}
		} else {
			Random rand = this.getRNG();
			
			if(this.isEndimationPlaying(PUFF_ANIMATION) && this.getAnimationTick() == 5) {
				for(int i = 0; i < 4; i++) {
					double offsetX = MathUtils.makeNegativeRandomly(rand.nextFloat() * 0.1F, rand);
    				double offsetZ = MathUtils.makeNegativeRandomly(rand.nextFloat() * 0.1F, rand);
    			
    				double x = this.posX + offsetX;
    				double y = this.posY + (rand.nextFloat() * 0.05F) + 0.7F;
    				double z = this.posZ + offsetZ;
    				
					this.world.addParticle(EEParticles.SHORT_POISE_BUBBLE.get(), x, y, z, MathUtils.makeNegativeRandomly((rand.nextFloat() * 0.1F), rand) + 0.05F, (rand.nextFloat() * 0.05F) + 0.025F, MathUtils.makeNegativeRandomly((rand.nextFloat() * 0.1F), rand) + 0.05F);
				}
			} else if(this.isEndimationPlaying(TELEPORT_TO_ANIMATION) && this.getAnimationTick() == 8) {
				for(int i = 0; i < 10; i++) {
					double offsetX = MathUtils.makeNegativeRandomly(rand.nextFloat() * 0.1F, rand);
					double offsetZ = MathUtils.makeNegativeRandomly(rand.nextFloat() * 0.1F, rand);
				
					double x = this.posX + offsetX;
					double y = this.posY + (rand.nextFloat() * 0.05F) + 0.7F;
					double z = this.posZ + offsetZ;
					
					this.world.addParticle(EEParticles.SHORT_POISE_BUBBLE.get(), x, y, z, MathUtils.makeNegativeRandomly((rand.nextFloat() * 0.15F), rand) + 0.025F, (rand.nextFloat() * 0.025F) + 0.025F, MathUtils.makeNegativeRandomly((rand.nextFloat() * 0.15F), rand) + 0.025F);
				}
			} else if(this.isEndimationPlaying(TELEPORT_FROM_ANIMATION) && this.getAnimationTick() == 5) {
				for(int i = 0; i < 10; i++) {
					double offsetX = MathUtils.makeNegativeRandomly(rand.nextFloat() * 0.1F, rand);
					double offsetZ = MathUtils.makeNegativeRandomly(rand.nextFloat() * 0.1F, rand);
			
					double x = this.posX + offsetX;
					double y = this.posY + (rand.nextFloat() * 0.05F) + 0.7F;
					double z = this.posZ + offsetZ;
				
					this.world.addParticle(EEParticles.SHORT_POISE_BUBBLE.get(), x, y, z, MathUtils.makeNegativeRandomly((rand.nextFloat() * 0.15F), rand) + 0.025F, (rand.nextFloat() * 0.025F) + 0.025F, MathUtils.makeNegativeRandomly((rand.nextFloat() * 0.15F), rand) + 0.025F);
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
		
		if(compound.contains("IsInflated")) {
			this.setInflated(compound.getBoolean("IsInflated"));
		}
		
		if(compound.contains("HivePos", 10)) {
			this.setHivePos(NBTUtil.readBlockPos(compound.getCompound("HivePos")));
		}
	}
	
	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		
		compound.putByte("AttachedHiveSide", (byte) this.getAttachedHiveSide().getIndex());
		compound.putBoolean("FromBottle", this.isFromBottle());
		compound.putBoolean("IsInflated", this.isInflated());
		
		compound.putInt("TeleportCooldown", this.teleportCooldown);
		
		if(this.getHivePos() != null) {
			compound.put("HivePos", NBTUtil.writeBlockPos(this.getHivePos()));
		}
	}
	
	@Nullable
	public BlockPos getHivePos() {
		return this.getDataManager().get(HIVE_POS).orElse(null);
	}
	
	public void setHivePos(@Nullable BlockPos pos) {
		this.getDataManager().set(HIVE_POS, Optional.ofNullable(pos));
	}
	
	@Nullable
	private TileEntityPuffBugHive getHive() {
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
		return this.getDataManager().get(BUD_POS).orElse(null);
	}
	
	public void setBudPos(@Nullable BlockPos pos) {
		this.getDataManager().set(BUD_POS, Optional.ofNullable(pos));
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
			ROTATE_ANIMATION
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
		}
	}
	
	@Override
	public void onEndimationEnd(Endimation endimation) {
		if(endimation == TELEPORT_TO_ANIMATION) {
			if(!this.world.isRemote) {
				NetworkUtil.setPlayingAnimationMessage(this, TELEPORT_FROM_ANIMATION);
			}
		}
	}
	
	@Override
	public void travel(Vec3d moveDirection) {
		if(this.isServerWorld() && this.isInflated()) {
			double gravity = this.getActivePotionEffect(Effects.LEVITATION) != null ? -0.005D : 0.005D;
			float speed = this.onGround ? 0.01F : 0.025F;
			
			this.moveRelative(speed, moveDirection);
			this.move(MoverType.SELF, this.getMotion());
			this.setMotion(this.getMotion().scale(0.75D));
			this.setMotion(this.getMotion().subtract(0, gravity, 0));
		} else {
			super.travel(moveDirection);
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
		
		nbt.putBoolean("IsChild", this.isChild());
	}
	
	@Override
	protected PathNavigator createNavigator(World worldIn) {
		return new EndergeticFlyingPathNavigator(this, worldIn);
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
		if(itemstack.getItem() == Items.GLASS_BOTTLE && this.isAlive() && !this.isAggressive()) {
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
		} else {
			return super.processInteract(player, hand);
		}
	}
	
	@Override
	public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, ILivingEntityData spawnDataIn, CompoundNBT dataTag) {
		if(dataTag != null) {
			int age = dataTag.getBoolean("IsChild") ? -24000 : 0;
			
			this.setGrowingAge(age);
			this.teleportCooldown = dataTag.getInt("TeleportCooldown");
			
			if(dataTag.contains("ColorTag", 3)) {
				this.setColor(dataTag.getInt("ColorTag"));
			}
			
			if(dataTag.contains("CustomPotionEffects")) {
				for(EffectInstance effectinstance : PotionUtils.getFullEffectsFromTag(dataTag)) {
					this.addPotionEffect(effectinstance);
				}
			}
		}
		return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
	}
	
	@Override
	public boolean canDespawn(double distanceToClosestPlayer) {
		return this.getHive() != null && !this.isFromBottle() && !this.hasCustomName();
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
		return EESounds.PUFBUG_PUFF.get();
	}
	
	public SoundEvent getTeleportSound(boolean from) {
		return from ? EESounds.PUFBUG_TELEPORT_FROM.get() : EESounds.PUFBUG_TELEPORT_TO.get();
	}
	
	public SoundEvent getPollinateSound() {
		return EESounds.PUFBUG_POLLINATE.get();
	}
	
	public SoundEvent getHiveCreationSound() {
		return EESounds.PUFBUG_CREATE_HIVE.get();
	}
	
	public SoundEvent getLaunchSound() {
		return EESounds.PUFBUG_LAUNCH.get();
	}
	
	public SoundEvent getLandSound() {
		return EESounds.PUFBUG_LAND.get();
	}
	
	@Override
	protected SoundEvent getDeathSound() {
		return EESounds.PUFBUG_DEATH.get();
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource damageSource) {
		return EESounds.PUFBUG_HURT.get();
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
				NetworkUtil.teleportEntity(this.puffbug, this.destination.getX() + 0.5F, this.destination.getY() + 0.5F, this.destination.getZ() + 0.5F);
				this.destination = null;
			}
		}
		
		@Nullable
		public boolean tryToCreateDesinationTo(BlockPos pos) {
			if(this.puffbug.ticksExisted > 5 && this.world.getBlockState(pos).getCollisionShape(this.world, pos).isEmpty() && this.hasNoDestination()) {
				this.destination = pos;
				return true;
			}
			return false;
		}
		
		public boolean hasNoDestination() {
			return this.destination == null;
		}
		
		public boolean canTeleport() {
			return this.destination == null && this.puffbug.teleportCooldown <= 0;
		}
	}
	
	public static class RotationController {
		private EntityPuffBug puffbug;
		private float prevYaw, yaw, startingYaw;
		private float prevPitch, pitch, startingPitch;
		private float setYaw, setPitch;
		private int tickLength;
		private int ticksSinceNotRotating;
		private boolean rotating;
		
		RotationController(EntityPuffBug puffbug) {
			this.puffbug = puffbug;
		}
		
		protected void tick() {
			this.prevYaw = this.yaw;
			this.prevPitch = this.pitch;
			
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
				
					this.setYaw = 0.0F;
					this.setPitch = 0.0F;
					this.tickLength = 20;	
				}
				this.ticksSinceNotRotating++;
			}
			
			this.yaw = this.clamp((this.setYaw - this.startingYaw) <= 0, this.yaw + ((this.setYaw - this.startingYaw) / this.tickLength), this.startingYaw, this.setYaw);
			this.pitch = this.clamp((this.setPitch - this.startingPitch) <= 0, this.pitch + ((this.setPitch - this.startingPitch) / this.tickLength), this.startingPitch, this.setPitch);
			
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
		
		public void rotate(float yaw, float pitch, int tickLength) {
			if(this.setYaw != yaw) {
				this.startingYaw = this.yaw;
			}
			
			if(this.setPitch != pitch) {
				this.startingPitch = this.pitch;
				NetworkUtil.setPlayingAnimationMessage(this.puffbug, EntityPuffBug.ROTATE_ANIMATION);
			}
			
			this.setYaw = yaw;
			this.setPitch = pitch;
			this.tickLength = tickLength;
			this.rotating = true;
			this.ticksSinceNotRotating = 0;
			
			if(!this.puffbug.world.isRemote) {
				EndergeticExpansion.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> this.puffbug), new MessageRotate(this.puffbug.getEntityId(), tickLength, yaw, pitch));
			}
		}
		
		public float[] getRotations(float ptc) {
			return new float[] {MathHelper.lerp(ptc, this.prevYaw, this.yaw), MathHelper.lerp(ptc, this.prevPitch, this.pitch)}; 
		}
	}
}