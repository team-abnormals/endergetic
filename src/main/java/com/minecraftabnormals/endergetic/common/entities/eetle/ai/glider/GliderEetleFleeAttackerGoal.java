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
		this.setFlags(EnumSet.of(Goal.Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		GliderEetleEntity glider = this.glider;
		if (glider.isGrounded() && !glider.isFlying()) {
			LivingEntity attacker = glider.groundedAttacker;
			if (attacker != null) {
				Vector3d pointAway = RandomPositionGenerator.getPosAvoid(glider, 16, 7, attacker.position());
				if (pointAway == null) {
					return false;
				} else if (attacker.distanceToSqr(pointAway.x, pointAway.y, pointAway.z) < attacker.distanceToSqr(glider)) {
					return false;
				} else {
					this.path = glider.getNavigation().createPath(pointAway.x, pointAway.y, pointAway.z, 0);
					return this.path != null;
				}
			}
		}
		return false;
	}

	@Override
	public boolean canContinueToUse() {
		GliderEetleEntity glider = this.glider;
		return glider.isGrounded() && !glider.isFlying() && glider.groundedAttacker != null && glider.getNavigation().isInProgress();
	}

	@Override
	public void start() {
		this.glider.getNavigation().moveTo(this.path, 1.25F);
	}

	@Override
	public void stop() {
		this.path = null;
	}
}
