package com.teamabnormals.endergetic.common.entity.purpoid.ai;

import com.teamabnormals.endergetic.common.entity.purpoid.Purpoid;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.pathfinder.Path;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class PurpoidMoveNearTargetGoal extends Goal {
	private final Purpoid purpoid;
	@Nullable
	private Path path;
	private int delayCounter;

	public PurpoidMoveNearTargetGoal(Purpoid purpoid) {
		this.purpoid = purpoid;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		Purpoid purpoid = this.purpoid;
		if (PurpoidAttackGoal.shouldFollowTarget(purpoid, true)) {
			this.path = purpoid.getNavigation().createPath(purpoid.getTarget(), 5);
			return this.path != null;
		}
		return false;
	}

	@Override
	public void start() {
		Purpoid purpoid = this.purpoid;
		purpoid.getNavigation().moveTo(this.path, 2.0F);
		purpoid.setAggressive(true);
		this.delayCounter = 0;
	}

	@Override
	public boolean canContinueToUse() {
		Purpoid purpoid = this.purpoid;
		return PurpoidAttackGoal.shouldFollowTarget(purpoid, true) && purpoid.getNavigation().isInProgress();
	}

	@Override
	public void tick() {
		this.delayCounter = Math.max(this.delayCounter - 1, 0);
		Purpoid purpoid = this.purpoid;
		LivingEntity target = purpoid.getTarget();
		purpoid.getLookControl().setLookAt(target, 30.0F, 30.0F);
		RandomSource random = purpoid.getRandom();
		if (this.delayCounter <= 0 && random.nextFloat() < 0.05F) {
			this.delayCounter = 4 + random.nextInt(9);
			double distanceToTargetSq = purpoid.distanceToSqr(target);
			if (distanceToTargetSq > 1024.0D) {
				this.delayCounter += 10;
			} else if (distanceToTargetSq > 256.0D) {
				this.delayCounter += 5;
			}

			PathNavigation pathNavigator = purpoid.getNavigation();
			Path path = pathNavigator.createPath(PurpoidAttackGoal.findAirPosAboveTarget(purpoid.level, target), random.nextInt(3) + 3);
			if (path == null || !pathNavigator.moveTo(path, 2.0F)) {
				this.delayCounter += 15;
			}
		}
	}

	@Override
	public void stop() {
		Purpoid purpoid = this.purpoid;
		LivingEntity target = purpoid.getTarget();
		if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(target)) {
			purpoid.setTarget(null);
		}

		purpoid.setAggressive(false);
		purpoid.getNavigation().stop();
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}
}
