package com.minecraftabnormals.endergetic.common.entities.purpoid.ai;

import com.minecraftabnormals.endergetic.common.entities.purpoid.PurpoidEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.EntityPredicates;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Random;

public class PurpoidMoveNearTargetGoal extends Goal {
	private final PurpoidEntity purpoid;
	@Nullable
	private Path path;
	private int delayCounter;

	public PurpoidMoveNearTargetGoal(PurpoidEntity purpoid) {
		this.purpoid = purpoid;
		this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	@Override
	public boolean shouldExecute() {
		PurpoidEntity purpoid = this.purpoid;
		if (PurpoidAttackGoal.shouldFollowTarget(purpoid, true)) {
			this.path = purpoid.getNavigator().getPathToEntity(purpoid.getAttackTarget(), 5);
			return this.path != null;
		}
		return false;
	}

	@Override
	public void startExecuting() {
		PurpoidEntity purpoid = this.purpoid;
		purpoid.getNavigator().setPath(this.path, 2.0F);
		purpoid.setAggroed(true);
		this.delayCounter = 0;
	}

	@Override
	public boolean shouldContinueExecuting() {
		PurpoidEntity purpoid = this.purpoid;
		return PurpoidAttackGoal.shouldFollowTarget(purpoid, true) && purpoid.getNavigator().hasPath();
	}

	@Override
	public void tick() {
		this.delayCounter = Math.max(this.delayCounter - 1, 0);
		PurpoidEntity purpoid = this.purpoid;
		LivingEntity target = purpoid.getAttackTarget();
		purpoid.getLookController().setLookPositionWithEntity(target, 30.0F, 30.0F);
		Random random = purpoid.getRNG();
		if (this.delayCounter <= 0 && random.nextFloat() < 0.05F) {
			this.delayCounter = 4 + random.nextInt(9);
			double distanceToTargetSq = purpoid.getDistanceSq(target);
			if (distanceToTargetSq > 1024.0D) {
				this.delayCounter += 10;
			} else if (distanceToTargetSq > 256.0D) {
				this.delayCounter += 5;
			}

			PathNavigator pathNavigator = purpoid.getNavigator();
			Path path = pathNavigator.getPathToPos(PurpoidAttackGoal.findAirPosAboveTarget(purpoid.world, target), random.nextInt(3) + 3);
			if (path == null || !pathNavigator.setPath(path, 2.0F)) {
				this.delayCounter += 15;
			}
		}
	}

	@Override
	public void resetTask() {
		PurpoidEntity purpoid = this.purpoid;
		LivingEntity target = purpoid.getAttackTarget();
		if (!EntityPredicates.CAN_AI_TARGET.test(target)) {
			purpoid.setAttackTarget(null);
		}

		purpoid.setAggroed(false);
		purpoid.getNavigator().clearPath();
	}
}
