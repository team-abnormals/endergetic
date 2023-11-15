package com.teamabnormals.endergetic.common.entity.booflo.ai;

import com.teamabnormals.blueprint.core.endimator.entity.EndimatedGoal;
import com.teamabnormals.endergetic.common.entity.booflo.Booflo;
import com.teamabnormals.endergetic.common.entity.booflo.BoofloBaby;
import com.teamabnormals.endergetic.core.registry.EEEntityTypes;
import com.teamabnormals.endergetic.core.registry.other.EEPlayableEndimations;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class BoofloGiveBirthGoal extends EndimatedGoal<Booflo> {
	private int birthTicks;
	private float originalYaw;

	public BoofloGiveBirthGoal(Booflo mother) {
		super(mother, EEPlayableEndimations.BOOFLO_BIRTH);
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
		this.originalYaw = this.entity.getYRot();
		this.entity.setLockedYaw(this.originalYaw);
		this.playEndimation();
	}

	@Override
	public void stop() {
		this.entity.hopDelay = this.entity.getDefaultGroundHopDelay();
		this.birthTicks = 0;
	}

	@Override
	public void tick() {
		Booflo booflo = this.entity;
		booflo.setYRot(this.originalYaw);
		if (++this.birthTicks % 20 == 0 && booflo.babiesToBirth > 0) {
			Level level = booflo.level;
			BoofloBaby baby = EEEntityTypes.BOOFLO_BABY.get().create(level);
			if (baby != null) {
				booflo.playSound(SoundEvents.ITEM_PICKUP, 0.3F, 0.6F);

				baby.setBirthTimer(60);
				baby.setBirthPosition(BoofloBaby.BirthPosition.get(3 - booflo.babiesToBirth));
				baby.setGrowingAge(-24000);

				Vec3 ridingOffset = baby.getBirthPositionOffset().yRot(-booflo.getYRot() * ((float) Math.PI / 180F) - ((float) Math.PI / 2F));
				baby.setPos(booflo.getX() + ridingOffset.x, booflo.getY() + 0.9F, booflo.getZ() + ridingOffset.z);
				baby.startRiding(booflo, true);
				baby.wasBred = booflo.tickCount > 200;
				level.addFreshEntity(baby);
			}
			booflo.babiesToBirth--;
		}
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}
}