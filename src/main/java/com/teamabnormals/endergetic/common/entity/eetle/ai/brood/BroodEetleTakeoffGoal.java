package com.teamabnormals.endergetic.common.entity.eetle.ai.brood;

import com.teamabnormals.endergetic.common.entity.eetle.BroodEetle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;

public class BroodEetleTakeoffGoal extends Goal {
	private final BroodEetle broodEetle;
	private int ticksPassed;

	public BroodEetleTakeoffGoal(BroodEetle broodEetle) {
		this.broodEetle = broodEetle;
	}

	@Override
	public boolean canUse() {
		BroodEetle broodEetle = this.broodEetle;
		if (!broodEetle.isFlying() && broodEetle.hasWokenUp() && !broodEetle.isFiringCannon()) {
			return !broodEetle.canFireEggCannon() && broodEetle.isOnGround() && (broodEetle.canFly() || BroodEetleDropEggsGoal.areFewEetlesNearby(broodEetle) && broodEetle.getRandom().nextFloat() < 0.025F) && !BroodEetleFlingGoal.searchForNearbyAggressors(broodEetle, broodEetle.getAttributeValue(Attributes.FOLLOW_RANGE)).isEmpty() && broodEetle.getRandom().nextFloat() < 0.025F || willFallFar(broodEetle);
		}
		return false;
	}

	@Override
	public void start() {
		BroodEetle broodEetle = this.broodEetle;
		broodEetle.takeoffPos = broodEetle.blockPosition();
		broodEetle.resetSlamCooldown();
		broodEetle.setFlying(true);
		broodEetle.setFiringCannon(false);
	}

	@Override
	public boolean canContinueToUse() {
		return this.ticksPassed < 10;
	}

	@Override
	public void stop() {
		this.ticksPassed = 0;
	}

	@Override
	public void tick() {
		this.ticksPassed++;
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}

	private static boolean willFallFar(BroodEetle broodEetle) {
		Level world = broodEetle.level;
		BlockPos.MutableBlockPos mutable = broodEetle.blockPosition().mutable();
		int startY = mutable.getY();
		for (int i = 0; i < 10; i++) {
			mutable.setY(startY - i);
			if (world.loadedAndEntityCanStandOn(mutable, broodEetle)) {
				return false;
			}
		}
		return true;
	}
}
