package com.minecraftabnormals.endergetic.common.entities.booflo.ai;

import java.util.Random;

import com.teamabnormals.abnormals_core.core.utils.NetworkUtil;
import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloBabyEntity;
import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity;
import com.minecraftabnormals.endergetic.core.registry.EEEntities;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class BoofloGiveBirthGoal extends Goal {
	private BoofloEntity mother;
	private int birthTicks;
	private float originalYaw;
	
	public BoofloGiveBirthGoal(BoofloEntity mother) {
		this.mother = mother;
	}
	
	@Override
	public boolean shouldExecute() {
		return !this.mother.hasCaughtFruit() && !this.mother.isBoofed() && this.mother.isNoEndimationPlaying() && this.mother.isPregnant() && (this.mother.func_233570_aj_() || this.mother.isPassenger());
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		return this.mother.isPregnant() && this.mother.isEndimationPlaying(BoofloEntity.BIRTH);
	}
	
	@Override
	public void startExecuting() {
		this.originalYaw = this.mother.rotationYaw;
		this.mother.setLockedYaw(this.originalYaw);
		NetworkUtil.setPlayingAnimationMessage(this.mother, BoofloEntity.BIRTH);
	}
	
	@Override
	public void resetTask() {
		if (this.birthTicks >= 70) {
			this.mother.setPregnant(false);
		}
		this.mother.hopDelay = this.mother.getDefaultGroundHopDelay();
		this.birthTicks = 0;
	}
	
	@Override
	public void tick() {
		this.birthTicks++;
		
		this.mother.rotationYaw = this.originalYaw;
	
		if (this.birthTicks % 20 == 0 && this.birthTicks > 0 && this.birthTicks < 70) {
			World world = this.mother.world;
			Random rand = this.mother.getRNG();
			
			double dx = Math.cos((this.mother.rotationYaw + 90) * Math.PI / 180.0D) * -0.2F;
			double dz = Math.sin((this.mother.rotationYaw + 90) * Math.PI / 180.0D) * -0.2F;
			
			this.mother.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 0.3F, 0.6F);
			
			BoofloBabyEntity baby = EEEntities.BOOFLO_BABY.get().create(world);
			baby.setBeingBorn(true);
			baby.setGrowingAge(-24000);
			baby.setPosition(this.mother.getPosX() + dx, this.mother.getPosY() + 0.9F + (rand.nextFloat() * 0.05F), this.mother.getPosZ() + dz);
			baby.startRiding(this.mother, true);
			baby.wasBred = this.mother.ticksExisted > 200;
			
			world.addEntity(baby);
		}
	}
}