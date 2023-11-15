package com.teamabnormals.endergetic.common.entity.purpoid.ai;

import com.teamabnormals.endergetic.common.entity.purpoid.Purpoid;
import net.minecraft.world.entity.ai.goal.Goal;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;

public class PurpFollowPurpoidsGoal extends Goal {
	private final Purpoid purp;
	@Nullable
	public Purpoid leader;
	private int timeToRecalcPath;

	public PurpFollowPurpoidsGoal(Purpoid purp) {
		this.purp = purp;
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		Purpoid purp = this.purp;
		if (!purp.isBaby()) return false;
		List<? extends Purpoid> list = purp.level.getEntitiesOfClass(Purpoid.class, purp.getBoundingBox().inflate(12.0D, 12.0D, 12.0D));
		Purpoid newLeader = null;
		double smallestDistance = Double.MAX_VALUE;
		for (Purpoid purpoid : list) {
			if (!purpoid.isBaby() && !purpoid.isResting()) {
				double distanceToPurpoid = purp.distanceToSqr(purpoid);
				if (distanceToPurpoid < smallestDistance) {
					smallestDistance = distanceToPurpoid;
					newLeader = purpoid;
				}
			}
		}

		if (newLeader == null || smallestDistance < 9.0D) return false;
		this.leader = newLeader;
		return true;
	}

	@Override
	public void start() {
		this.timeToRecalcPath = 0;
	}

	@Override
	public void tick() {
		if (--this.timeToRecalcPath <= 0) {
			this.timeToRecalcPath = this.adjustedTickDelay(10);
			this.purp.getNavigation().moveTo(this.leader, 1.0F);
		}
	}

	@Override
	public boolean canContinueToUse() {
		Purpoid purp = this.purp;
		Purpoid leader = this.leader;
		if (!purp.isBaby() || leader == null || !leader.isAlive()) return false;
		double distanceToLeader = purp.distanceToSqr(leader);
		return distanceToLeader >= 9.0D && distanceToLeader <= 256.0D;
	}

	@Override
	public void stop() {
		this.leader = null;
		this.purp.getNavigation().stop();
	}
}
