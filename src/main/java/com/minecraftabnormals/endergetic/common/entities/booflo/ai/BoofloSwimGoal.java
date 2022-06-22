package com.minecraftabnormals.endergetic.common.entities.booflo.ai;

import javax.annotation.Nullable;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.pathfinding.PathType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

public class BoofloSwimGoal extends RandomWalkingGoal {

	public BoofloSwimGoal(CreatureEntity booflo, double p_i48937_2_, int p_i48937_4_) {
		super(booflo, p_i48937_2_, p_i48937_4_);
	}

	@Nullable
	protected Vector3d getPosition() {
		Vector3d vec3d = RandomPositionGenerator.getPos(this.mob, 8, 2);

		for (int i = 0; vec3d != null && !this.mob.level.getBlockState(new BlockPos(vec3d)).isPathfindable(this.mob.level, new BlockPos(vec3d), PathType.AIR) && i++ < 8; vec3d = RandomPositionGenerator.getPos(this.mob, 10, 2)) {
			;
		}

		return vec3d;
	}

	@Override
	public boolean canUse() {
		if (this.mob.isVehicle()) {
			return false;
		} else {
			if (this.mob.getRandom().nextInt(this.interval) != 0) {
				return false;
			}
		}

		Vector3d vec3d = this.getPosition();
		if (vec3d == null) {
			return false;
		} else {
			this.wantedX = vec3d.x;
			this.wantedY = vec3d.y;
			this.wantedZ = vec3d.z;
			this.forceTrigger = false;
			return !this.mob.isInWater();
		}
	}

	@Override
	public boolean canContinueToUse() {
		return super.canContinueToUse() && !this.mob.isInWater();
	}

}