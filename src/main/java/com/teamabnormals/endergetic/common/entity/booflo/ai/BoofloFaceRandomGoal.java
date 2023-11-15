package com.teamabnormals.endergetic.common.entity.booflo.ai;

import com.teamabnormals.endergetic.common.entity.booflo.Booflo;
import com.teamabnormals.endergetic.common.entity.booflo.Booflo.GroundMoveHelperController;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class BoofloFaceRandomGoal extends Goal {
	private final Booflo booflo;
	private float chosenDegrees;
	private int nextRandomizeTime;

	public BoofloFaceRandomGoal(Booflo booflo) {
		this.booflo = booflo;
		this.setFlags(EnumSet.of(Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		return !this.booflo.isBoofed() && this.booflo.getTarget() == null && (this.booflo.isOnGround() || this.booflo.hasEffect(MobEffects.LEVITATION)) && this.booflo.getMoveControl() instanceof Booflo.GroundMoveHelperController;
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

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}
}