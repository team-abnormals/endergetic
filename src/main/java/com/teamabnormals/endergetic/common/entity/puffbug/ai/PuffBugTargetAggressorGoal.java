package com.teamabnormals.endergetic.common.entity.puffbug.ai;

import com.teamabnormals.endergetic.common.entity.puffbug.PuffBug;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;

public class PuffBugTargetAggressorGoal extends HurtByTargetGoal {

	public PuffBugTargetAggressorGoal(PuffBug puffbug) {
		super(puffbug);
		this.setAlertOthers(PuffBug.class);
	}

	@Override
	public void start() {
		super.start();

		for (PuffBug bugs : this.mob.level.getEntitiesOfClass(PuffBug.class, this.mob.getBoundingBox().inflate(16.0D), (puffbug) -> puffbug.getTarget() == null)) {
			bugs.setTarget(this.mob.getTarget());
		}
	}

}
