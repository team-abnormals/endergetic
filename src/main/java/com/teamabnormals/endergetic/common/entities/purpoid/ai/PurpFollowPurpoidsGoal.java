package com.teamabnormals.endergetic.common.entities.purpoid.ai;

import com.teamabnormals.endergetic.common.entities.purpoid.PurpoidEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;

public class PurpFollowPurpoidsGoal extends Goal {
	private final PurpoidEntity purp;
	@Nullable
	public PurpoidEntity leader;
	private int timeToRecalcPath;

	public PurpFollowPurpoidsGoal(PurpoidEntity purp) {
		this.purp = purp;
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		PurpoidEntity purp = this.purp;
		if (!purp.isBaby()) return false;
		List<? extends PurpoidEntity> list = purp.level.getEntitiesOfClass(PurpoidEntity.class, purp.getBoundingBox().inflate(12.0D, 12.0D, 12.0D));
		PurpoidEntity newLeader = null;
		double smallestDistance = Double.MAX_VALUE;
		for (PurpoidEntity purpoidEntity : list) {
			if (!purpoidEntity.isBaby() && !purpoidEntity.isResting()) {
				double distanceToPurpoid = purp.distanceToSqr(purpoidEntity);
				if (distanceToPurpoid < smallestDistance) {
					smallestDistance = distanceToPurpoid;
					newLeader = purpoidEntity;
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
		PurpoidEntity purp = this.purp;
		PurpoidEntity leader = this.leader;
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
