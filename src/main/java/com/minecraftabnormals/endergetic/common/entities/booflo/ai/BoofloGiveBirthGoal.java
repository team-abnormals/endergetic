package com.minecraftabnormals.endergetic.common.entities.booflo.ai;

import java.util.Random;

import com.teamabnormals.abnormals_core.core.library.endimator.EndimatedGoal;
import com.teamabnormals.abnormals_core.core.library.endimator.Endimation;
import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloBabyEntity;
import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity;
import com.minecraftabnormals.endergetic.core.registry.EEEntities;

import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class BoofloGiveBirthGoal extends EndimatedGoal<BoofloEntity> {
	private int birthTicks;
	private float originalYaw;
	
	public BoofloGiveBirthGoal(BoofloEntity mother) {
		super(mother);
	}

	@Override
	public boolean shouldExecute() {
		return !this.entity.hasCaughtFruit() && !this.entity.isBoofed() && this.entity.isNoEndimationPlaying() && this.entity.isPregnant() && (this.entity.func_233570_aj_() || this.entity.isPassenger());
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		return this.entity.isPregnant() && this.isEndimationPlaying();
	}
	
	@Override
	public void startExecuting() {
		this.originalYaw = this.entity.rotationYaw;
		this.entity.setLockedYaw(this.originalYaw);
		this.playEndimation();
	}
	
	@Override
	public void resetTask() {
		if (this.birthTicks >= 70) {
			this.entity.setPregnant(false);
		}
		this.entity.hopDelay = this.entity.getDefaultGroundHopDelay();
		this.birthTicks = 0;
	}
	
	@Override
	public void tick() {
		this.birthTicks++;
		
		this.entity.rotationYaw = this.originalYaw;
	
		if (this.birthTicks % 20 == 0 && this.birthTicks > 0 && this.birthTicks < 70) {
			World world = this.entity.world;
			Random rand = this.entity.getRNG();
			
			double dx = Math.cos((this.entity.rotationYaw + 90) * Math.PI / 180.0D) * -0.2F;
			double dz = Math.sin((this.entity.rotationYaw + 90) * Math.PI / 180.0D) * -0.2F;
			
			this.entity.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 0.3F, 0.6F);
			
			BoofloBabyEntity baby = EEEntities.BOOFLO_BABY.get().create(world);
			baby.setBeingBorn(true);
			baby.setGrowingAge(-24000);
			baby.setPosition(this.entity.getPosX() + dx, this.entity.getPosY() + 0.9F + (rand.nextFloat() * 0.05F), this.entity.getPosZ() + dz);
			baby.startRiding(this.entity, true);
			baby.wasBred = this.entity.ticksExisted > 200;
			
			world.addEntity(baby);
		}
	}
	
	@Override
	protected Endimation getEndimation() {
		return BoofloEntity.BIRTH;
	}
}