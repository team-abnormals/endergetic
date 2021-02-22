package com.minecraftabnormals.endergetic.common.entities.eetle.ai.glider;

import com.minecraftabnormals.endergetic.common.entities.eetle.GliderEetleEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.vector.Vector3d;

import java.util.EnumSet;

public class GliderEetleFleeAttackerGoal extends Goal {
	private final GliderEetleEntity glider;
	private Path path;

	public GliderEetleFleeAttackerGoal(GliderEetleEntity glider) {
		this.glider = glider;
		this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
	}

	@Override
	public boolean shouldExecute() {
		GliderEetleEntity glider = this.glider;
		if (glider.isGrounded() && !glider.isFlying()) {
			LivingEntity attacker = glider.groundedAttacker;
			if (attacker != null) {
				Vector3d pointAway = RandomPositionGenerator.findRandomTargetBlockAwayFrom(glider, 16, 7, attacker.getPositionVec());
				if (pointAway == null) {
					return false;
				} else if (attacker.getDistanceSq(pointAway.x, pointAway.y, pointAway.z) < attacker.getDistanceSq(glider)) {
					return false;
				} else {
					this.path = glider.getNavigator().getPathToPos(pointAway.x, pointAway.y, pointAway.z, 0);
					return this.path != null;
				}
			}
		}
		return false;
	}

	@Override
	public boolean shouldContinueExecuting() {
		GliderEetleEntity glider = this.glider;
		return glider.isGrounded() && !glider.isFlying() && glider.groundedAttacker != null && glider.getNavigator().hasPath();
	}

	@Override
	public void startExecuting() {
		this.glider.getNavigator().setPath(this.path, 1.25F);
	}

	@Override
	public void resetTask() {
		this.path = null;
	}
}
