package com.minecraftabnormals.endergetic.common.entities.eetle.ai.glider;

import com.minecraftabnormals.endergetic.common.entities.eetle.GliderEetleEntity;
import net.minecraft.entity.ai.goal.Goal;

public class GliderEetleLandGoal extends Goal {
	private final GliderEetleEntity glider;
	private int ticksPassed;

	public GliderEetleLandGoal(GliderEetleEntity glider) {
		this.glider = glider;
	}

	@Override
	public boolean shouldExecute() {
		return this.glider.shouldLand() && !this.glider.isMoving() && this.glider.isOnGround() && !this.glider.isAggressive() && this.glider.getRNG().nextFloat() < 0.05F;
	}

	@Override
	public boolean shouldContinueExecuting() {
		if (this.ticksPassed < 15 && this.glider.isOnGround() && !this.glider.isAggressive()) {
			return true;
		}
		this.glider.setFlying(this.ticksPassed < 10);
		return false;
	}

	@Override
	public void startExecuting() {
		this.glider.getNavigator().clearPath();
		this.glider.resetFlyCooldown();
		this.glider.setFlying(false);
		this.glider.resetIdleFlapDelay();
	}

	@Override
	public void tick() {
		this.ticksPassed++;
		this.glider.getNavigator().clearPath();
	}

	@Override
	public void resetTask() {
		this.ticksPassed = 0;
	}
}
