package com.minecraftabnormals.endergetic.common.entities.puffbug.ai;

import com.minecraftabnormals.endergetic.common.entities.puffbug.PuffBugEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.pathfinding.PathType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nullable;

public class PuffBugBoostGoal extends RandomWalkingGoal {

	public PuffBugBoostGoal(PuffBugEntity puffbug) {
		super(puffbug, 1.0F, 15);
	}

	public boolean canUse() {
		if (this.mob.isVehicle()) {
			return false;
		} else {
			if (!this.forceTrigger) {
				if (this.mob.getRandom().nextInt(this.interval) != 0) {
					return false;
				}
			}

			Vector3d destination = this.getPosition();
			if (destination == null) {
				return false;
			} else {
				this.wantedX = destination.x;
				this.wantedY = destination.y;
				this.wantedZ = destination.z;
				this.forceTrigger = false;
				return true;
			}
		}
	}

	@Nullable
	protected Vector3d getPosition() {
		Vector3d vec3d = RandomPositionGenerator.getPos(this.mob, 8, 5);

		for (int i = 0; vec3d != null && !this.mob.level.getBlockState(new BlockPos(vec3d)).isPathfindable(this.mob.level, new BlockPos(vec3d), PathType.AIR) && i++ < 10; vec3d = RandomPositionGenerator.getPos(this.mob, 8, 5)) {
			;
		}

		return vec3d;
	}

}