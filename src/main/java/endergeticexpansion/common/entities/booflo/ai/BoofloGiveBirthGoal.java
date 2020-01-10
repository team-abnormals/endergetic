package endergeticexpansion.common.entities.booflo.ai;

import java.util.Random;

import endergeticexpansion.api.util.NetworkUtil;
import endergeticexpansion.common.entities.booflo.EntityBooflo;
import endergeticexpansion.common.entities.booflo.EntityBoofloBaby;
import endergeticexpansion.core.registry.EEEntities;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class BoofloGiveBirthGoal extends Goal {
	private EntityBooflo mother;
	private int birthTicks;
	private float originalYaw;
	
	public BoofloGiveBirthGoal(EntityBooflo mother) {
		this.mother = mother;
	}
	
	@Override
	public boolean shouldExecute() {
		return !this.mother.hasCaughtFruit() && !this.mother.isBoofed() && this.mother.isAnimationPlaying(EntityBooflo.BLANK_ANIMATION) && this.mother.isPregnant() && this.mother.onGround;
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		return this.mother.isPregnant() && this.mother.isAnimationPlaying(EntityBooflo.BIRTH);
	}
	
	@Override
	public void startExecuting() {
		this.originalYaw = this.mother.rotationYaw;
		this.mother.setBirthYaw(this.originalYaw);
		NetworkUtil.setPlayingAnimationMessage(this.mother, EntityBooflo.BIRTH);
	}
	
	@Override
	public void resetTask() {
		if(this.birthTicks >= 70) {
			this.mother.setPregnant(false);
		}
		this.mother.hopDelay = this.mother.getDefaultGroundHopDelay();
		this.birthTicks = 0;
	}
	
	@Override
	public void tick() {
		this.birthTicks++;
		
		this.mother.rotationYaw = this.originalYaw;
	
		if(this.birthTicks % 20 == 0 && this.birthTicks > 0 && this.birthTicks < 70) {
			World world = this.mother.world;
			Random rand = this.mother.getRNG();
			
			double dx = Math.cos((this.mother.rotationYaw + 90) * Math.PI / 180.0D) * -0.2F;
			double dz = Math.sin((this.mother.rotationYaw + 90) * Math.PI / 180.0D) * -0.2F;
			
			this.mother.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 0.3F, 0.6F);
			
			EntityBoofloBaby baby = EEEntities.BOOFLO_BABY.get().create(world);
			baby.setBeingBorn(true);
			baby.setGrowingAge(-24000);
			baby.setPosition(this.mother.posX + dx, this.mother.posY + 0.9F + (rand.nextFloat() * 0.05F), this.mother.posZ + dz);
			baby.startRiding(this.mother, true);
			
			world.addEntity(baby);
		}
	}
}