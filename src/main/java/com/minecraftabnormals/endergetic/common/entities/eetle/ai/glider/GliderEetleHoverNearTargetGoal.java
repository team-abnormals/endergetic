package com.minecraftabnormals.endergetic.common.entities.eetle.ai.glider;

import com.minecraftabnormals.endergetic.common.entities.eetle.GliderEetleEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.EntityPredicates;

import java.util.EnumSet;

/**
 * Makes the Glider Eetle hover nearby the target when the target is caught by another Glider Eetle.
 */
public class GliderEetleHoverNearTargetGoal extends Goal {
	private final GliderEetleEntity glider;
	private Path path;
	private int delayCounter;

	public GliderEetleHoverNearTargetGoal(GliderEetleEntity glider) {
		this.glider = glider;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		GliderEetleEntity glider = this.glider;
		if (glider.isGrounded()) return false;
		LivingEntity attackTarget = glider.getTarget();
		if (attackTarget != null && attackTarget.isAlive() && attackTarget.getVehicle() instanceof GliderEetleEntity && glider.getPassengers().isEmpty()) {
			this.path = this.glider.getNavigation().createPath(attackTarget, 5);
			return this.path != null;
		}
		return false;
	}

	@Override
	public void start() {
		GliderEetleEntity glider = this.glider;
		if (!glider.isFlying()) {
			glider.setFlying(true);
		}
		glider.getNavigation().moveTo(this.path, 1.25F);
		glider.setAggressive(true);
	}

	@Override
	public boolean canContinueToUse() {
		if (this.glider.isGrounded()) return false;
		LivingEntity target = this.glider.getTarget();
		return target != null && target.getVehicle() instanceof GliderEetleEntity && this.glider.getPassengers().isEmpty() && this.glider.getNavigation().isInProgress();
	}

	@Override
	public void tick() {
		this.delayCounter = Math.max(this.delayCounter - 1, 0);
		GliderEetleEntity glider = this.glider;
		LivingEntity target = glider.getTarget();
		double distanceToTargetSq = glider.distanceToSqr(target);
		boolean canSeeTarget = glider.getSensing().canSee(target);
		if (canSeeTarget && this.delayCounter <= 0 && glider.getRandom().nextFloat() < 0.05F) {
			this.delayCounter = 4 + glider.getRandom().nextInt(9);
			PathNavigator pathNavigator = glider.getNavigation();
			if (distanceToTargetSq > 1024.0D) {
				this.delayCounter += 10;
			} else if (distanceToTargetSq > 256.0D) {
				this.delayCounter += 5;
			}

			Path path = pathNavigator.createPath(GliderEetleGrabGoal.getAirPosAboveTarget(glider.level, target), 5);
			if (path == null || !pathNavigator.moveTo(path, 1.25F)) {
				this.delayCounter += 15;
			}
		}
	}

	@Override
	public void stop() {
		GliderEetleEntity glider = this.glider;
		LivingEntity livingentity = glider.getTarget();
		if (!EntityPredicates.NO_CREATIVE_OR_SPECTATOR.test(livingentity)) {
			glider.setTarget(null);
		}
		glider.setAggressive(false);
		glider.getNavigation().stop();
	}
}
