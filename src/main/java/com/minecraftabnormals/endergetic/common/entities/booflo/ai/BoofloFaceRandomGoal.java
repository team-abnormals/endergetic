package com.minecraftabnormals.endergetic.common.entities.booflo.ai;

import java.util.EnumSet;

import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity;
import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity.GroundMoveHelperController;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.potion.Effects;

public class BoofloFaceRandomGoal extends Goal {
	private final BoofloEntity booflo;
	private float chosenDegrees;
	private int nextRandomizeTime;

	public BoofloFaceRandomGoal(BoofloEntity booflo) {
		this.booflo = booflo;
		this.setMutexFlags(EnumSet.of(Flag.LOOK));
	}

	@Override
	public boolean shouldExecute() {
		return !this.booflo.isBoofed() && this.booflo.getAttackTarget() == null && (this.booflo.isOnGround() || this.booflo.isPotionActive(Effects.LEVITATION)) && this.booflo.getMoveHelper() instanceof BoofloEntity.GroundMoveHelperController;
	}

	@Override
	public void tick() {
		if (this.nextRandomizeTime-- <= 0) {
			this.nextRandomizeTime = 30 + this.booflo.getRNG().nextInt(60);
			this.chosenDegrees = this.booflo.getRNG().nextInt(360);
		}

		if (this.booflo.getMoveHelper() instanceof GroundMoveHelperController) {
			((GroundMoveHelperController) this.booflo.getMoveHelper()).setDirection(this.chosenDegrees, false);
		}
	}
}