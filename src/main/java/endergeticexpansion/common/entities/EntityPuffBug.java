package endergeticexpansion.common.entities;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import endergeticexpansion.api.endimator.Endimation;
import endergeticexpansion.api.endimator.entity.IEndimatedEntity;
import endergeticexpansion.api.util.NetworkUtil;
import endergeticexpansion.common.tileentities.TileEntityPuffBugHive;
import endergeticexpansion.core.registry.EEBlocks;
import endergeticexpansion.core.registry.EEEntities;
import endergeticexpansion.core.registry.EEItems;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
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
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

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
	private static final DataParameter<Integer> COLOR = EntityDataManager.createKey(EntityPuffBug.class, DataSerializers.VARINT);
	public static final Endimation CLAIM_HIVE_ANIMATION = new Endimation(20);
	private Endimation endimation = BLANK_ANIMATION;
	private int animationTick;
	
	public EntityPuffBug(EntityType<? extends EntityPuffBug> type, World worldIn) {
		super(type, worldIn);
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
		this.getDataManager().register(INFLATED, true);
		this.getDataManager().register(FROM_BOTTLE, false);
	}
	
	@Override
	public void tick() {
		super.tick();
		this.endimateTick();
		
		if(!this.world.isRemote) {
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
				}
			}
		}
	}
	
	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		
		this.setAttachedHiveSide(Direction.byIndex(compound.getByte("AttachedHiveSide")));
		this.setFromBottle(compound.getBoolean("FromBottle"));
		this.setInflated(compound.getBoolean("IsInflated"));
		
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
			} catch(Exception e) {
				return null;
			}
		}
		return null;
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
	
	public int getColor() {
		return this.dataManager.get(COLOR);
	}
	
	public void setColor(int color) {
		this.dataManager.set(COLOR, color);
	}
	
	@Override
	public Endimation[] getEndimations() {
		return new Endimation[] {
			CLAIM_HIVE_ANIMATION
		};
	}
	
	@Override
	public void onEndimationEnd(Endimation endimation) {
		
	}
	
	@Override
	public void travel(Vec3d moveDirection) {
		if(this.isServerWorld() && this.isInflated()) {
			double gravity = this.getActivePotionEffect(Effects.LEVITATION) != null ? -0.005D : 0.005D;
			
			this.moveRelative(0.0F, moveDirection);
			this.move(MoverType.SELF, this.getMotion());
			this.setMotion(this.getMotion().scale(0.75D));
			this.setMotion(this.getMotion().subtract(0, gravity, 0));
		} else {
			super.travel(moveDirection);
		}
	}
	
	@Override
	public void fall(float distance, float damageMultiplier) {}
	
	@Override
	protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
		return sizeIn.height * 0.275F;
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
		return !this.isFromBottle() && !this.hasCustomName();
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
	public AgeableEntity createChild(AgeableEntity ageable) {
		return EEEntities.PUFF_BUG.get().create(this.world);
	}

	@Override
	public Endimation getPlayingEndimation() {
		return this.endimation;
	}

	@Override
	public void setPlayingEndimation(Endimation endimationToPlay) {
		this.onEndimationEnd(this.endimation);
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
}