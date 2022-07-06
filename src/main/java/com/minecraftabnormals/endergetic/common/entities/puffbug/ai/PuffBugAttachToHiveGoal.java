package com.minecraftabnormals.endergetic.common.entities.puffbug.ai;

import com.minecraftabnormals.endergetic.common.entities.puffbug.PuffBugEntity;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

public class PuffBugAttachToHiveGoal extends Goal {
	private PuffBugEntity puffbug;
	private int ticksPassed;

	public PuffBugAttachToHiveGoal(PuffBugEntity puffbug) {
		this.puffbug = puffbug;
	}

	@Override
	public boolean canUse() {
		Direction side = this.puffbug.getDesiredHiveSide();
		if (side != null && this.puffbug.getHive() != null) {
			return this.puffbug.isAtCorrectRestLocation(side);
		}
		return false;
	}

	@Override
	public void start() {
		this.puffbug.getNavigation().stop();
		this.puffbug.setSpeed(0.0F);

		this.puffbug.setDeltaMovement(Vec3.ZERO);
	}

	@Override
	public void tick() {
		this.ticksPassed++;

		this.puffbug.getNavigation().stop();
		this.puffbug.setSpeed(0.0F);

		this.puffbug.setDeltaMovement(this.puffbug.getDeltaMovement().multiply(1.0F, 0.0F, 1.0F));

		if (this.ticksPassed > 25) {
			this.puffbug.setAttachedHiveSide(this.puffbug.getDesiredHiveSide());
			this.puffbug.setDesiredHiveSide(null);
		}
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