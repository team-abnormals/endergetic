package com.minecraftabnormals.endergetic.common.entities.puffbug.ai;

import javax.annotation.Nullable;

import com.minecraftabnormals.endergetic.common.entities.puffbug.PuffBugEntity;

import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.pathfinding.PathType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

public class PuffBugBoostGoal extends RandomWalkingGoal {

	public PuffBugBoostGoal(PuffBugEntity puffbug) {
		super(puffbug, 1.0F, 15);
	}

	public boolean shouldExecute() {
		if (this.creature.isBeingRidden()) {
			return false;
		} else {
			if (!this.mustUpdate) {
				if (this.creature.getRNG().nextInt(this.executionChance) != 0) {
					return false;
				}
			}

			Vector3d destination = this.getPosition();
			if (destination == null) {
				return false;
			} else {
				this.x = destination.x;
				this.y = destination.y;
				this.z = destination.z;
				this.mustUpdate = false;
				return true;
			}
		}
	}

	@Nullable
	protected Vector3d getPosition() {
		Vector3d vec3d = RandomPositionGenerator.findRandomTarget(this.creature, 8, 5);

		for (int i = 0; vec3d != null && !this.creature.world.getBlockState(new BlockPos(vec3d)).allowsMovement(this.creature.world, new BlockPos(vec3d), PathType.AIR) && i++ < 10; vec3d = RandomPositionGenerator.findRandomTarget(this.creature, 8, 5)) {
			;
		}

		return vec3d;
	}

}