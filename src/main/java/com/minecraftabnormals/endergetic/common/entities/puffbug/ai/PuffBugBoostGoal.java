package com.minecraftabnormals.endergetic.common.entities.puffbug.ai;

import javax.annotation.Nullable;

import com.minecraftabnormals.endergetic.common.entities.puffbug.PuffBugEntity;

import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

public class PuffBugBoostGoal extends RandomStrollGoal {

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

			Vec3 destination = this.getPosition();
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
	protected Vec3 getPosition() {
		Vec3 vec3d = RandomPos.getPos(this.mob, 8, 5);

		for (int i = 0; vec3d != null && !this.mob.level.getBlockState(new BlockPos(vec3d)).isPathfindable(this.mob.level, new BlockPos(vec3d), PathComputationType.AIR) && i++ < 10; vec3d = RandomPos.getPos(this.mob, 8, 5)) {
			;
		}

		return vec3d;
	}

}