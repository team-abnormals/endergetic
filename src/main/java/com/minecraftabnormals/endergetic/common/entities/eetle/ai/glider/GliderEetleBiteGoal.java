package com.minecraftabnormals.endergetic.common.entities.eetle.ai.glider;

import com.minecraftabnormals.abnormals_core.core.endimator.entity.EndimatedGoal;
import com.minecraftabnormals.endergetic.common.entities.eetle.GliderEetleEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.EntityPredicates;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Random;

public class GliderEetleBiteGoal extends EndimatedGoal<GliderEetleEntity> {
	@Nullable
	private Path path;
	private int delayCounter;

	public GliderEetleBiteGoal(GliderEetleEntity entity) {
		super(entity, GliderEetleEntity.MUNCH);
		this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	@Override
	public boolean shouldExecute() {
		if (this.entity.isGrounded()) return false;
		LivingEntity target = this.entity.getAttackTarget();
		if (target != null && target.isAlive() && GliderEetleEntity.isEntityLarge(target) && !(target.getRidingEntity() instanceof GliderEetleEntity) && this.entity.getPassengers().isEmpty()) {
			this.path = this.entity.getNavigator().getPathToEntity(target, 0);
			return this.path != null;
		}
		return false;
	}

	@Override
	public void startExecuting() {
		GliderEetleEntity glider = this.entity;
		if (!glider.isFlying()) {
			glider.setFlying(true);
		}
		glider.getNavigator().setPath(this.path, 1.25F);
		glider.setAggroed(true);
		this.delayCounter = 0;
	}

	@Override
	public boolean shouldContinueExecuting() {
		if (this.entity.isGrounded()) return false;
		LivingEntity target = this.entity.getAttackTarget();
		return target != null && target.isAlive() && GliderEetleEntity.isEntityLarge(target) && !(target.getRidingEntity() instanceof GliderEetleEntity) && this.entity.getPassengers().isEmpty() && !this.entity.getNavigator().noPath();
	}

	@Override
	public void tick() {
		this.delayCounter = Math.max(this.delayCounter - 1, 0);
		GliderEetleEntity glider = this.entity;
		LivingEntity target = glider.getAttackTarget();
		glider.getLookController().setLookPositionWithEntity(target, 30.0F, 30.0F);
		double distanceToTargetSq = glider.getDistanceSq(target);
		boolean canSeeTarget = glider.getEntitySenses().canSee(target);
		Random random = this.random;
		if (canSeeTarget && this.delayCounter <= 0 && random.nextFloat() < 0.05F) {
			this.delayCounter = 4 + random.nextInt(9);
			PathNavigator pathNavigator = glider.getNavigator();
			if (distanceToTargetSq >= 9.0F) {
				Path path = pathNavigator.getPathToPos(GliderEetleGrabGoal.getAirPosAboveTarget(glider.world, target), 0);
				if (path == null || !pathNavigator.setPath(path, 1.25F)) {
					this.delayCounter += 15;
				}
			} else {
				if (!glider.getNavigator().tryMoveToEntityLiving(target, 1.25F)) {
					this.delayCounter += 15;
				}
			}
		}

		double reachRange = glider.getWidth() * 2.0F * glider.getWidth() * 2.0F + target.getWidth();
		if (distanceToTargetSq <= reachRange) {
			if (!this.isEndimationPlaying()) {
				this.playEndimation();
			} else if (this.isEndimationAtTick(8) || this.isEndimationAtTick(18)){
				target.attackEntityFrom(GliderEetleMunchGoal.causeMunchDamage(glider), (float) glider.getAttributeValue(Attributes.ATTACK_DAMAGE) + random.nextInt(2));
			}
		}
	}

	@Override
	public void resetTask() {
		GliderEetleEntity glider = this.entity;
		LivingEntity livingentity = glider.getAttackTarget();
		if (!EntityPredicates.CAN_AI_TARGET.test(livingentity)) {
			glider.setAttackTarget(null);
		}
		glider.setAggroed(false);
		glider.getNavigator().clearPath();
	}
}
