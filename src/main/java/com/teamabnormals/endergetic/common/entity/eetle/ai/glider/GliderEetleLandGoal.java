package com.teamabnormals.endergetic.common.entity.eetle.ai.glider;

import com.teamabnormals.endergetic.common.entity.eetle.GliderEetle;
import net.minecraft.world.entity.ai.goal.Goal;

public class GliderEetleLandGoal extends Goal {
	private final GliderEetle glider;
	private int ticksPassed;

	public GliderEetleLandGoal(GliderEetle glider) {
		this.glider = glider;
	}

	@Override
	public boolean canUse() {
		return this.glider.shouldLand() && !this.glider.isMoving() && this.glider.isOnGround() && !this.glider.isAggressive() && this.glider.getRandom().nextFloat() < 0.05F;
	}

	@Override
	public boolean canContinueToUse() {
		if (this.ticksPassed < 15 && this.glider.isOnGround() && !this.glider.isAggressive()) {
			return true;
		}
		this.glider.setFlying(this.ticksPassed < 10);
		return false;
	}

	@Override
	public void start() {
		this.glider.getNavigation().stop();
		this.glider.resetFlyCooldown();
		this.glider.setFlying(false);
		this.glider.resetIdleFlapDelay();
	}

	@Override
	public void tick() {
		this.ticksPassed++;
		this.glider.getNavigation().stop();
	}

	@Override
	public void stop() {
		this.ticksPassed = 0;
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}
}
