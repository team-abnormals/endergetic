package endergeticexpansion.common.entities.booflo.ai;

import javax.annotation.Nullable;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.pathfinding.PathType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class BoofloSwimGoal extends RandomWalkingGoal {
	
	public BoofloSwimGoal(CreatureEntity booflo, double p_i48937_2_, int p_i48937_4_) {
		super(booflo, p_i48937_2_, p_i48937_4_);
	}

	@Nullable
	protected Vec3d getPosition() {
		Vec3d vec3d = RandomPositionGenerator.findRandomTarget(this.creature, 8, 2);

		for(int i = 0; vec3d != null && !this.creature.world.getBlockState(new BlockPos(vec3d)).allowsMovement(this.creature.world, new BlockPos(vec3d), PathType.AIR) && i++ < 8; vec3d = RandomPositionGenerator.findRandomTarget(this.creature, 10, 2)) {
			;
		}
		
		return vec3d;
	}
	
	@Override
	public boolean shouldExecute() {
		if(this.creature.isBeingRidden()) {
			return false;
		} else {
			if(this.creature.getRNG().nextInt(this.executionChance) != 0) {
				return false;
			}
		}

		Vec3d vec3d = this.getPosition();
		if(vec3d == null) {
			return false;
		} else {
			this.x = vec3d.x;
			this.y = vec3d.y;
			this.z = vec3d.z;
			this.mustUpdate = false;
			return !this.creature.isInWater();
		}
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		return super.shouldContinueExecuting() && !this.creature.isInWater();
	}
	
}