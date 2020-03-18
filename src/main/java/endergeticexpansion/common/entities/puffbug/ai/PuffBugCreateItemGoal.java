package endergeticexpansion.common.entities.puffbug.ai;

import java.util.EnumSet;
import java.util.Random;

import endergeticexpansion.api.util.MathUtils;
import endergeticexpansion.api.util.NetworkUtil;
import endergeticexpansion.common.entities.puffbug.EntityPuffBug;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.potion.Effects;

public class PuffBugCreateItemGoal extends Goal {
	private EntityPuffBug puffbug;
	private int ticksPassed;
	private float originalHealth;
	
	public PuffBugCreateItemGoal(EntityPuffBug puffbug) {
		this.puffbug = puffbug;
		this.setMutexFlags(EnumSet.of(Flag.MOVE));
	}

	@Override
	public boolean shouldExecute() {
		return this.puffbug.isInflated() && !this.puffbug.isAggressive() && this.puffbug.hasStackToCreate() && this.puffbug.isNoEndimationPlaying();
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		return this.puffbug.getAttackTarget() == null && this.puffbug.isInflated() && this.puffbug.hasLevitation() && this.puffbug.hasStackToCreate() && this.puffbug.getHealth() >= this.originalHealth;
	}
	
	@Override
	public void startExecuting() {
		this.originalHealth = this.puffbug.getHealth();
	}
	
	@Override
	public void tick() {
		this.ticksPassed++;
		
		this.puffbug.getNavigator().clearPath();
		this.puffbug.setAIMoveSpeed(0.0F);
		
		if(this.ticksPassed >= 25 && this.puffbug.isNoEndimationPlaying()) {
			NetworkUtil.setPlayingAnimationMessage(this.puffbug, EntityPuffBug.MAKE_ITEM_ANIMATION);
		}
		
		this.puffbug.getRotationController().rotate(0.0F, 180.0F, 0.0F, 20);
		
		if(this.puffbug.isEndimationPlaying(EntityPuffBug.MAKE_ITEM_ANIMATION) && this.puffbug.hasStackToCreate()) {
			Random rand = this.puffbug.getRNG();
			
			if(this.puffbug.getAnimationTick() == 90) {
				ItemEntity itementity = new ItemEntity(this.puffbug.world, this.puffbug.posX, this.puffbug.posY - 0.5D, this.puffbug.posZ, this.puffbug.getStackToCreate());
				itementity.setPickupDelay(40);
				this.puffbug.world.addEntity(itementity);
				this.puffbug.setStackToCreate(null);
				
				this.puffbug.removePotionEffect(Effects.LEVITATION);
				
				for(int i = 0; i < 6; i++) {
					double offsetX = MathUtils.makeNegativeRandomly(rand.nextFloat() * 0.1F, rand);
					double offsetZ = MathUtils.makeNegativeRandomly(rand.nextFloat() * 0.1F, rand);
				
					double x = this.puffbug.posX + offsetX;
					double y = this.puffbug.posY + (rand.nextFloat() * 0.05F) + 0.5F;
					double z = this.puffbug.posZ + offsetZ;
					
					NetworkUtil.spawnParticle("endergetic:short_poise_bubble", x, y, z, MathUtils.makeNegativeRandomly((rand.nextFloat() * 0.15F), rand) + 0.025F, (rand.nextFloat() * 0.025F) + 0.025F, MathUtils.makeNegativeRandomly((rand.nextFloat() * 0.15F), rand) + 0.025F);
				}
			} else if(this.puffbug.getAnimationTick() == 85) {
				float pitch = this.puffbug.isChild() ? (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.5F : (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F;
				this.puffbug.playSound(this.puffbug.getItemCreationSound(), 0.1F, pitch);
			}
		}
		
		if(this.puffbug.getAnimationTick() == 90 && this.puffbug.isEndimationPlaying(EntityPuffBug.MAKE_ITEM_ANIMATION) && this.puffbug.hasStackToCreate()) {
			
		}
	}
	
	@Override
	public void resetTask() {
		this.ticksPassed = 0;
		this.originalHealth = 0.0F;
	}
}