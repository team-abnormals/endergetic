package com.teamabnormals.endergetic.common.entities.purpoid.ai;

import com.teamabnormals.endergetic.common.entities.purpoid.PurpoidEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class PurpoidStunnedGoal extends Goal {
	private final PurpoidEntity purpoid;
	private int ticksPassed;

	public PurpoidStunnedGoal(PurpoidEntity purpoid) {
		this.purpoid = purpoid;
		this.setFlags(EnumSet.allOf(Flag.class));
	}

	@Override
	public boolean canUse() {
		return this.purpoid.getStunTimer() > 0;
	}

	@Override
	public void start() {
		this.purpoid.setSpeed(0.0F);
		this.purpoid.setBoostingTicks(0);
		this.ticksPassed = 0;
	}

	@Override
	public void tick() {
		PurpoidEntity purpoid = this.purpoid;
		purpoid.setSpeed(0.0F);
		purpoid.getNavigation().stop();
		if (++this.ticksPassed <= 15) {
			purpoid.setDeltaMovement(purpoid.getDeltaMovement().multiply(0.5F, 1.0F, 0.5F).add(0.0F, 0.025F, 0.0F));
			if (purpoid.shouldApplyRotationSnaps()) purpoid.setShouldApplyRotationSnaps(false);
		} else {
			purpoid.setDeltaMovement(purpoid.getDeltaMovement().multiply(0.5F, 1.0F, 0.5F).subtract(0.0F, 0.05F, 0.0F));
			if (!purpoid.shouldApplyRotationSnaps()) purpoid.setShouldApplyRotationSnaps(true);
		}
	}

	@Override
	public void stop() {
		this.purpoid.setShouldApplyRotationSnaps(true);
	}

	@Override
	public boolean canContinueToUse() {
		return this.purpoid.getStunTimer() > 0;
	}
}
