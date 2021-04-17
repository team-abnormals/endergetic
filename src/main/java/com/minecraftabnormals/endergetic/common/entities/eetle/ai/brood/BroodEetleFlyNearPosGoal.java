package com.minecraftabnormals.endergetic.common.entities.eetle.ai.brood;

import com.minecraftabnormals.endergetic.common.entities.eetle.BroodEetleEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class BroodEetleFlyNearPosGoal extends Goal {
	private final BroodEetleEntity broodEetle;
	private double x;
	private double y;
	private double z;

	public BroodEetleFlyNearPosGoal(BroodEetleEntity broodEetle) {
		this.broodEetle = broodEetle;
		this.setMutexFlags(EnumSet.of(Flag.MOVE));
	}

	@Override
	public boolean shouldExecute() {
		BroodEetleEntity broodEetle = this.broodEetle;
		if (broodEetle.isFlying() && broodEetle.getRNG().nextFloat() < 0.1F) {
			BlockPos takeoffPos = broodEetle.takeoffPos;
			if (takeoffPos != null) {
				Vector3d vector3d = findPos(broodEetle, Vector3d.copyCentered(broodEetle.takeoffPos));
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
	public void startExecuting() {
		this.broodEetle.getNavigator().tryMoveToXYZ(this.x, this.y, this.z, 1.0F);
	}

	public boolean shouldContinueExecuting() {
		return !this.broodEetle.getNavigator().noPath();
	}

	@Override
	public void resetTask() {
		this.broodEetle.getNavigator().clearPath();
	}

	@Nullable
	private static Vector3d findPos(BroodEetleEntity broodEetle, Vector3d takeoffPos) {
		Vector3d differenceNormalized = takeoffPos.subtract(broodEetle.getPositionVec()).normalize();
		boolean verticalFarAway = Math.abs(broodEetle.getPosY() - takeoffPos.getY()) >= 6;
		return RandomPositionGenerator.func_226339_a_(broodEetle, 8, verticalFarAway ? 2 : 1, verticalFarAway ? -1 : 6, differenceNormalized, false, ((float)Math.PI / 2.0F), broodEetle::getBlockPathWeight, true, 2, 1, true);
	}
}
