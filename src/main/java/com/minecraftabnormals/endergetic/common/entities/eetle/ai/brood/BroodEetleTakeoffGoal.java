package com.minecraftabnormals.endergetic.common.entities.eetle.ai.brood;

import com.minecraftabnormals.endergetic.common.entities.eetle.BroodEetleEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BroodEetleTakeoffGoal extends Goal {
	private final BroodEetleEntity broodEetle;
	private int ticksPassed;

	public BroodEetleTakeoffGoal(BroodEetleEntity broodEetle) {
		this.broodEetle = broodEetle;
	}

	@Override
	public boolean shouldExecute() {
		BroodEetleEntity broodEetle = this.broodEetle;
		if (!broodEetle.isFlying()) {
			return !broodEetle.canFireEggCannon() && broodEetle.isOnGround() && broodEetle.canFly() && !BroodEetleFlingGoal.searchForNearbyAggressors(broodEetle, broodEetle.getAttributeValue(Attributes.FOLLOW_RANGE)).isEmpty() && broodEetle.getRNG().nextFloat() < 0.025F || willFallFar(broodEetle);
		}
		return false;
	}

	@Override
	public void startExecuting() {
		BroodEetleEntity broodEetle = this.broodEetle;
		broodEetle.takeoffPos = broodEetle.getPosition();
		broodEetle.setFlying(true);
		broodEetle.setFiringCannon(false);
	}

	@Override
	public boolean shouldContinueExecuting() {
		return this.ticksPassed < 10;
	}

	@Override
	public void resetTask() {
		this.ticksPassed = 0;
	}

	@Override
	public void tick() {
		this.ticksPassed++;
	}

	private static boolean willFallFar(BroodEetleEntity broodEetle) {
		World world = broodEetle.world;
		BlockPos.Mutable mutable = broodEetle.getPosition().toMutable();
		int startY = mutable.getY();
		for (int i = 0; i < 10; i++) {
			mutable.setY(startY - i);
			if (world.isTopSolid(mutable, broodEetle)) {
				return false;
			}
		}
		return true;
	}
}
