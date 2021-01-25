package com.minecraftabnormals.endergetic.common.entities.eetle.ai.glider;

import com.minecraftabnormals.endergetic.common.entities.eetle.GliderEetleEntity;
import net.minecraft.entity.ai.goal.Goal;

public class GliderEetleTakeoffGoal extends Goal {
	private final GliderEetleEntity glider;
	private int ticksPassed;

	public GliderEetleTakeoffGoal(GliderEetleEntity glider) {
		this.glider = glider;
	}

	@Override
	public boolean shouldExecute() {
		return this.glider.isNoEndimationPlaying() && !this.glider.isFlying() && this.glider.canFly() && this.glider.getRNG().nextFloat() < 0.05F;
	}

	@Override
	public boolean shouldContinueExecuting() {
		return this.ticksPassed < 5 && this.glider.canFly();
	}

	@Override
	public void startExecuting() {
		this.glider.setFlying(true);
	}

	@Override
	public void resetTask() {
		this.ticksPassed = 0;
	}

	@Override
	public void tick() {
		this.ticksPassed++;
	}
}
