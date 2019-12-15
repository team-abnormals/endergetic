package endergeticexpansion.common.entities;

import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.Sets;

import endergeticexpansion.common.tileentities.TileEntityPuffBugHive;
import endergeticexpansion.core.registry.EEBlocks;
import endergeticexpansion.core.registry.EEEntities;
import endergeticexpansion.core.registry.EEItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingEntity;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class EntityPuffBug extends FlyingEntity {
	private static final DataParameter<BlockPos> HIVE_POS = EntityDataManager.createKey(EntityPuffBug.class, DataSerializers.BLOCK_POS);
	private static final DataParameter<Integer> COLOR = EntityDataManager.createKey(EntityPuffBug.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> PUFF_STATE = EntityDataManager.createKey(EntityPuffBug.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> POTION_TIMER = EntityDataManager.createKey(EntityPuffBug.class, DataSerializers.VARINT);
	private static final DataParameter<Boolean> FROM_BOTTLE = EntityDataManager.createKey(EntityPuffBug.class, DataSerializers.BOOLEAN);
	private Potion potion = Potions.EMPTY;
	private final Set<EffectInstance> customPotionEffects = Sets.newHashSet();
	private boolean fixedColor;
	
	public EntityPuffBug(EntityType<? extends EntityPuffBug> type, World worldIn) {
		super(type, worldIn);
		this.experienceValue = 5;
	}
	
	public EntityPuffBug(World worldIn, BlockPos pos) {
		this(EEEntities.PUFF_BUG, worldIn);
		this.setHivePos(pos);
	}
	
	public EntityPuffBug(World worldIn, double x, double y, double z) {
		this(EEEntities.PUFF_BUG, worldIn);
		this.setPosition(x, y, z);
	}
	
	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0D);
	}
	
	@Override
	protected void registerData() {
		this.getDataManager().register(HIVE_POS, BlockPos.ZERO);
		this.getDataManager().register(COLOR, -1);
		this.getDataManager().register(PUFF_STATE, 1);
		this.getDataManager().register(POTION_TIMER, 0);
		this.getDataManager().register(FROM_BOTTLE, false);
		super.registerData();
	}
	
	@Override
	public void readAdditional(CompoundNBT nbt) {
		super.readAdditional(nbt);
		this.setHivePos(new BlockPos(nbt.getInt("HiveX"), nbt.getInt("HiveX"), nbt.getInt("HiveX")));
		this.setPuffState(nbt.getInt("PuffState"));
		this.setPotionTimer(nbt.getInt("PotionTimer"));
		this.setFromBottle(nbt.getBoolean("FromBottle"));
		
		if(nbt.contains("Potion", 8)) {
			this.potion = PotionUtils.getPotionTypeFromNBT(nbt);
		}

		for(EffectInstance effectinstance : PotionUtils.getFullEffectsFromTag(nbt)) {
			this.addEffect(effectinstance);
		}

		if(nbt.contains("Color", 99)) {
			this.setFixedColor(nbt.getInt("Color"));
		} else {
			this.refreshColor();
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void writeAdditional(CompoundNBT nbt) {
		super.writeAdditional(nbt);
		BlockPos blockpos = this.getHivePos();
		nbt.putInt("HiveX", blockpos.getX());
		nbt.putInt("HiveY", blockpos.getY());
		nbt.putInt("HiveZ", blockpos.getZ());
		nbt.putInt("PuffState", this.getPuffState());
		nbt.putInt("PotionTimer", this.getPotionTimer());
		nbt.putBoolean("FromBottle", this.isFromBottle());
		
		if(this.potion != Potions.EMPTY && this.potion != null) {
			nbt.putString("Potion", Registry.POTION.getKey(this.potion).toString());
		}
		
		if(this.fixedColor) {
			nbt.putInt("Color", this.getColor());
		}
		
		if(!this.customPotionEffects.isEmpty()) {
			ListNBT listnbt = new ListNBT();

			for(EffectInstance effectinstance : this.customPotionEffects) {
				listnbt.add(effectinstance.write(new CompoundNBT()));
			}

			nbt.put("CustomPotionEffects", listnbt);
		}
	}
	
	public void setHivePos(BlockPos pos) {
		this.getDataManager().set(HIVE_POS, pos);
	}
	
	public BlockPos getHivePos() {
		return this.getDataManager().get(HIVE_POS);
	}
	
	/*
	 * ids - 0: deflated, 1: medium inflated, 2: inflated
	 */
	public void setPuffState(int stateId) {
		this.getDataManager().set(PUFF_STATE, stateId);
	}
	
	public int getPuffState() {
		return this.getDataManager().get(PUFF_STATE);
	}
	
	public void setPotionTimer(int ticks) {
		this.getDataManager().set(POTION_TIMER, ticks);
	}
	
	public int getPotionTimer() {
		return this.getDataManager().get(POTION_TIMER);
	}
	
	@SuppressWarnings("deprecation")
	protected void setBottleData(ItemStack bottle) {
		if (this.hasCustomName()) {
			bottle.setDisplayName(this.getCustomName());
		}
		CompoundNBT nbt = bottle.getOrCreateTag();
		nbt.putInt("PotionTimerTag", this.getPotionTimer());
		if(this.potion != Potions.EMPTY && this.potion != null) {
			nbt.putString("PotionTag", Registry.POTION.getKey(this.potion).toString());
		}
		if(this.fixedColor) {
			nbt.putInt("ColorTag", this.getColor());
		}
		if(!this.customPotionEffects.isEmpty()) {
			ListNBT listnbt = new ListNBT();

			for(EffectInstance effectinstance : this.customPotionEffects) {
				listnbt.add(effectinstance.write(new CompoundNBT()));
			}

			nbt.put("CustomPotionEffects", listnbt);
		}
    }
	
	public boolean isFromBottle() {
        return this.getDataManager().get(FROM_BOTTLE);
    }

    public void setFromBottle(boolean value) {
        this.getDataManager().set(FROM_BOTTLE, value);
    }
	
	private void refreshColor() {
		this.fixedColor = false;
		this.getDataManager().set(COLOR, PotionUtils.getPotionColorFromEffectList(PotionUtils.mergeEffects(this.potion, this.customPotionEffects)));
	}
	
	public void addEffect(EffectInstance effect) {
		this.customPotionEffects.add(effect);
		this.getDataManager().set(COLOR, PotionUtils.getPotionColorFromEffectList(PotionUtils.mergeEffects(this.potion, this.customPotionEffects)));
	}
	
	public int getColor() {
		return this.dataManager.get(COLOR);
	}
	
	private void setFixedColor(int color) {
		this.fixedColor = true;
		this.dataManager.set(COLOR, color);
	}
	
	public Set<EffectInstance> getCustomPotionEffects(){
		return this.customPotionEffects;
	}
	
	private void spawnPotionParticles(int particleCount) {
		int i = this.getColor();
		if (i != -1 && particleCount > 0) {
			double d0 = (double)(i >> 16 & 255) / 255.0D;
			double d1 = (double)(i >> 8 & 255) / 255.0D;
			double d2 = (double)(i >> 0 & 255) / 255.0D;

			for(int j = 0; j < particleCount; ++j) {
				this.world.addParticle(ParticleTypes.ENTITY_EFFECT, this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.getWidth(), this.posY + this.rand.nextDouble() * (double)this.getHeight(), this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.getWidth(), d0, d1, d2);
			}
		}
	}
	
	private boolean isHiveAtPos(BlockPos pos) {
		return this.world.getTileEntity(pos).getTileEntity() instanceof TileEntityPuffBugHive;
	}
	
	@Nullable
	private TileEntity getHive() {
		return this.world.getTileEntity(this.getHivePos()).getTileEntity();
	}
	
	@Override
	public void tick() {
		super.tick();
		if(!this.getActivePotionEffects().isEmpty()) {
			this.setPotionTimer(200);
			for(EffectInstance effectinstance : this.getActivePotionEffects()) {
				this.potion = new Potion(effectinstance);
				this.addEffect(effectinstance);
			}
			this.fixedColor = true;
			this.clearActivePotions();
		}
		if(this.world.isRemote && world.getGameTime() % 5 == 0) {
			this.spawnPotionParticles(1);
		}
		if(this.getPotionTimer() > 0) {
			this.setPotionTimer(this.getPotionTimer() - 1);
		} else {
			if(!this.customPotionEffects.isEmpty()) {
				this.setFixedColor(-1);
				this.customPotionEffects.clear();
			}
		}
		if(this.getColor() == 3694022) {
			this.setFixedColor(-1);
		}
	}
	
	@Override
	protected boolean processInteract(PlayerEntity player, Hand hand) {
		ItemStack itemstack = player.getHeldItem(hand);
        if(itemstack.getItem() == Items.GLASS_BOTTLE && this.isAlive()) {
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
		if(dataTag != null && dataTag.contains("PotionTimerTag", 3)) {
			this.setPotionTimer(dataTag.getInt("PotionTimerTag"));
			if(dataTag.contains("PotionTag")) {
				this.potion = PotionUtils.getPotionTypeFromNBT(dataTag);
			}
			if(dataTag.contains("ColorTag")) {
				this.setFixedColor(dataTag.getInt("ColorTag"));
			}
			if(dataTag.contains("CustomPotionEffects")) {
				for(EffectInstance effectinstance : PotionUtils.getFullEffectsFromTag(dataTag)) {
					this.addEffect(effectinstance);
				}
			}
		}
		return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
	}
	
	@Override
	public boolean isInvulnerableTo(DamageSource source) {
		//Used to make puff bugs immune to potion damage
		if(source.isMagicDamage()) {
			return true;
		}
		return super.isInvulnerableTo(source);
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
}
