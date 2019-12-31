package endergeticexpansion.common.entities.booflo;

import endergeticexpansion.api.endimator.EndimatedEntity;
import endergeticexpansion.api.endimator.Endimation;
import endergeticexpansion.api.entity.util.EntityItemStackHelper;
import endergeticexpansion.api.util.NetworkUtil;
import endergeticexpansion.core.registry.EEEntities;
import endergeticexpansion.core.registry.EEItems;
import endergeticexpansion.core.registry.EESounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityBooflo extends EndimatedEntity {
	public static final Endimation CROAK = new Endimation(55);

	public EntityBooflo(EntityType<? extends EntityBooflo> type, World worldIn) {
		super(type, worldIn);
	}
	
	@Override
	protected void registerAttributes() {
		super.registerAttributes();
	}
	
	@Override
	public void livingTick() {
		super.livingTick();
		
		if(this.isAlive() && this.rand.nextInt(1000) < this.livingSoundTime++ && this.isAnimationPlaying(BLANK_ANIMATION) && !this.isWorldRemote()) {
			this.livingSoundTime = -this.getTalkInterval();
			this.setPlayingAnimation(CROAK);
			NetworkUtil.setPlayingAnimationMessage(this, CROAK);
		}
		
		if(this.isAnimationPlaying(CROAK) && this.getAnimationTick() == 5 && !this.isWorldRemote()) {
			this.playSound(this.getAmbientSound(), this.getSoundVolume(), this.getSoundPitch());
		}
	}
	
	@Override
	protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
		return 0.85F;
	}
	
	@Override
	public int getVerticalFaceSpeed() {
		return 10;
	}
	
	@Override
	public int getHorizontalFaceSpeed() {
		return 10;
	}
	
	@Override
	public int getTalkInterval() {
		return 120;
	}
	
	@Override
	public Endimation[] getAnimations() {
		return new Endimation[] {
			CROAK
		};
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
}