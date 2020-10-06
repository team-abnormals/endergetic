package com.minecraftabnormals.endergetic.common.entities.puffbug.ai;

import com.minecraftabnormals.endergetic.common.entities.puffbug.PuffBugEntity;

import net.minecraft.entity.ai.goal.HurtByTargetGoal;

public class PuffBugTargetAggressorGoal extends HurtByTargetGoal {

	public PuffBugTargetAggressorGoal(PuffBugEntity puffbug) {
		super(puffbug);
		this.setCallsForHelp(new Class[]{PuffBugEntity.class});
	}

	@Override
	public void startExecuting() {
		super.startExecuting();

		for (PuffBugEntity bugs : this.goalOwner.world.getEntitiesWithinAABB(PuffBugEntity.class, this.goalOwner.getBoundingBox().grow(16.0D), (puffbug) -> puffbug.getAttackTarget() == null)) {
			bugs.setAttackTarget(this.goalOwner.getAttackTarget());
		}
	}

}
