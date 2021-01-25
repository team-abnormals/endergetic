package com.minecraftabnormals.endergetic.common.entities.eetle.ai.glider;

import com.minecraftabnormals.endergetic.common.entities.eetle.GliderEetleEntity;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;

public class GliderEetleFlyGoal extends RandomWalkingGoal {
	private final GliderEetleEntity glider;

	public GliderEetleFlyGoal(GliderEetleEntity glider) {
		super(glider, 1.0F);
		this.glider = glider;
	}

	@Override
	public boolean shouldExecute() {
		return this.glider.isFlying() && super.shouldExecute();
	}
}
