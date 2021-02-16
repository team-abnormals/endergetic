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
		this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
	}

	@Override
	public boolean shouldExecute() {
		GliderEetleEntity glider = this.glider;
		if (glider.isGrounded()) return false;
		LivingEntity attackTarget = glider.getAttackTarget();
		if (attackTarget != null && attackTarget.isAlive() && attackTarget.getRidingEntity() instanceof GliderEetleEntity && glider.getPassengers().isEmpty()) {
			this.path = this.glider.getNavigator().getPathToEntity(attackTarget, 5);
			return this.path != null;
		}
		return false;
	}

	@Override
	public void startExecuting() {
		GliderEetleEntity glider = this.glider;
		if (!glider.isFlying()) {
			glider.setFlying(true);
		}
		glider.getNavigator().setPath(this.path, 1.25F);
		glider.setAggroed(true);
	}

	@Override
	public boolean shouldContinueExecuting() {
		if (this.glider.isGrounded()) return false;
		LivingEntity target = this.glider.getAttackTarget();
		return target != null && target.getRidingEntity() instanceof GliderEetleEntity && this.glider.getPassengers().isEmpty() && this.glider.getNavigator().hasPath();
	}

	@Override
	public void tick() {
		this.delayCounter = Math.max(this.delayCounter - 1, 0);
		GliderEetleEntity glider = this.glider;
		LivingEntity target = glider.getAttackTarget();
		double distanceToTargetSq = glider.getDistanceSq(target);
		boolean canSeeTarget = glider.getEntitySenses().canSee(target);
		if (canSeeTarget && this.delayCounter <= 0 && glider.getRNG().nextFloat() < 0.05F) {
			this.delayCounter = 4 + glider.getRNG().nextInt(9);
			PathNavigator pathNavigator = glider.getNavigator();
			if (distanceToTargetSq > 1024.0D) {
				this.delayCounter += 10;
			} else if (distanceToTargetSq > 256.0D) {
				this.delayCounter += 5;
			}

			Path path = pathNavigator.getPathToPos(GliderEetleGrabGoal.getAirPosAboveTarget(glider.world, target), 5);
			if (path == null || !pathNavigator.setPath(path, 1.25F)) {
				this.delayCounter += 15;
			}
		}
	}

	@Override
	public void resetTask() {
		GliderEetleEntity glider = this.glider;
		LivingEntity livingentity = glider.getAttackTarget();
		if (!EntityPredicates.CAN_AI_TARGET.test(livingentity)) {
			glider.setAttackTarget(null);
		}
		glider.setAggroed(false);
		glider.getNavigator().clearPath();
	}
}
