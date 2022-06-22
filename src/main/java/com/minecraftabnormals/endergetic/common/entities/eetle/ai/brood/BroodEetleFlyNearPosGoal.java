package com.minecraftabnormals.endergetic.common.entities.eetle.ai.brood;

import com.minecraftabnormals.endergetic.common.entities.eetle.BroodEetleEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nullable;
import java.util.EnumSet;

import net.minecraft.entity.ai.goal.Goal.Flag;

public class BroodEetleFlyNearPosGoal extends Goal {
	private final BroodEetleEntity broodEetle;
	private double x;
	private double y;
	private double z;

	public BroodEetleFlyNearPosGoal(BroodEetleEntity broodEetle) {
		this.broodEetle = broodEetle;
		this.setFlags(EnumSet.of(Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		BroodEetleEntity broodEetle = this.broodEetle;
		if (broodEetle.isFlying() && broodEetle.hasWokenUp() && broodEetle.getRandom().nextFloat() < 0.1F) {
			BlockPos takeoffPos = broodEetle.takeoffPos;
			if (takeoffPos != null) {
				Vector3d vector3d = findPos(broodEetle, Vector3d.atCenterOf(broodEetle.takeoffPos));
				if (vector3d == null) {
					return false;
				} else {
					this.x = vector3d.x;
					this.y = vector3d.y;
					this.z = vector3d.z;
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void start() {
		this.broodEetle.getNavigation().moveTo(this.x, this.y, this.z, 1.0F);
	}

	public boolean canContinueToUse() {
		return !this.broodEetle.getNavigation().isDone();
	}

	@Override
	public void stop() {
		this.broodEetle.getNavigation().stop();
	}

	@Nullable
	private static Vector3d findPos(BroodEetleEntity broodEetle, Vector3d takeoffPos) {
		Vector3d differenceNormalized = takeoffPos.subtract(broodEetle.position()).normalize();
		boolean verticalFarAway = Math.abs(broodEetle.getY() - takeoffPos.y()) >= 6;
		return RandomPositionGenerator.generateRandomPos(broodEetle, 8, verticalFarAway ? 2 : 1, verticalFarAway ? -1 : 6, differenceNormalized, false, ((float)Math.PI / 2.0F), broodEetle::getWalkTargetValue, true, 2, 1, true);
	}
}
