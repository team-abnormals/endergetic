package com.minecraftabnormals.endergetic.common.entities.booflo.ai;

import java.util.Random;

import com.minecraftabnormals.abnormals_core.core.endimator.entity.EndimatedGoal;
import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloBabyEntity;
import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity;
import com.minecraftabnormals.endergetic.core.registry.EEEntities;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;

public class BoofloGiveBirthGoal extends EndimatedGoal<BoofloEntity> {
	private int birthTicks;
	private float originalYaw;

	public BoofloGiveBirthGoal(BoofloEntity mother) {
		super(mother, BoofloEntity.BIRTH);
	}

	@Override
	public boolean canUse() {
		return !this.entity.hasCaughtFruit() && !this.entity.isBoofed() && this.entity.isNoEndimationPlaying() && this.entity.isPregnant() && (this.entity.isOnGround() || this.entity.isPassenger());
	}

	@Override
	public boolean canContinueToUse() {
		return this.entity.isPregnant() && this.isEndimationPlaying();
	}

	@Override
	public void start() {
		this.originalYaw = this.entity.yRot;
		this.entity.setLockedYaw(this.originalYaw);
		this.playEndimation();
	}

	@Override
	public void stop() {
		if (this.birthTicks >= 70) {
			this.entity.setPregnant(false);
		}
		this.entity.hopDelay = this.entity.getDefaultGroundHopDelay();
		this.birthTicks = 0;
	}

	@Override
	public void tick() {
		this.birthTicks++;

		this.entity.yRot = this.originalYaw;

		if (this.birthTicks % 20 == 0 && this.birthTicks > 0 && this.birthTicks < 70) {
			Level world = this.entity.level;
			Random rand = this.entity.getRandom();

			double dx = Math.cos((this.entity.yRot + 90) * Math.PI / 180.0D) * -0.2F;
			double dz = Math.sin((this.entity.yRot + 90) * Math.PI / 180.0D) * -0.2F;

			this.entity.playSound(SoundEvents.ITEM_PICKUP, 0.3F, 0.6F);

			BoofloBabyEntity baby = EEEntities.BOOFLO_BABY.get().create(world);
			if (baby != null) {
				baby.setBeingBorn(true);
				baby.setGrowingAge(-24000);
				baby.setPos(this.entity.getX() + dx, this.entity.getY() + 0.9F + (rand.nextFloat() * 0.05F), this.entity.getZ() + dz);
				baby.startRiding(this.entity, true);
				baby.wasBred = this.entity.tickCount > 200;
				world.addFreshEntity(baby);
			}
		}
	}
}