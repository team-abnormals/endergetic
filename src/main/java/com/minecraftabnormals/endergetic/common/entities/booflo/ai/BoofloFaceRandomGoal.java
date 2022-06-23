package com.minecraftabnormals.endergetic.common.entities.booflo.ai;

import java.util.EnumSet;

import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity;
import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity.GroundMoveHelperController;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.effect.MobEffects;

import net.minecraft.world.entity.ai.goal.Goal.Flag;

public class BoofloFaceRandomGoal extends Goal {
	private final BoofloEntity booflo;
	private float chosenDegrees;
	private int nextRandomizeTime;

	public BoofloFaceRandomGoal(BoofloEntity booflo) {
		this.booflo = booflo;
		this.setFlags(EnumSet.of(Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		return !this.booflo.isBoofed() && this.booflo.getTarget() == null && (this.booflo.isOnGround() || this.booflo.hasEffect(MobEffects.LEVITATION)) && this.booflo.getMoveControl() instanceof BoofloEntity.GroundMoveHelperController;
	}

	@Override
	public void tick() {
		if (this.nextRandomizeTime-- <= 0) {
			this.nextRandomizeTime = 30 + this.booflo.getRandom().nextInt(60);
			this.chosenDegrees = this.booflo.getRandom().nextInt(360);
		}

		if (this.booflo.getMoveControl() instanceof GroundMoveHelperController) {
			((GroundMoveHelperController) this.booflo.getMoveControl()).setDirection(this.chosenDegrees, false);
		}
	}
}