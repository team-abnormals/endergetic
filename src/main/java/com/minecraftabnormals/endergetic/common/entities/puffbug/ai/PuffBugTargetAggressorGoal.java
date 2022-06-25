package com.minecraftabnormals.endergetic.common.entities.puffbug.ai;

import com.minecraftabnormals.endergetic.common.entities.puffbug.PuffBugEntity;

import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;

public class PuffBugTargetAggressorGoal extends HurtByTargetGoal {

	public PuffBugTargetAggressorGoal(PuffBugEntity puffbug) {
		super(puffbug);
		this.setAlertOthers(PuffBugEntity.class);
	}

	@Override
	public void start() {
		super.start();

		for (PuffBugEntity bugs : this.mob.level.getEntitiesOfClass(PuffBugEntity.class, this.mob.getBoundingBox().inflate(16.0D), (puffbug) -> puffbug.getTarget() == null)) {
			bugs.setTarget(this.mob.getTarget());
		}
	}

}
