package com.minecraftabnormals.endergetic.common.entities.eetle.ai.glider;

import com.minecraftabnormals.endergetic.common.entities.eetle.GliderEetleEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nullable;

public class GliderEetleFlyGoal extends WaterAvoidingRandomWalkingGoal {
	private final GliderEetleEntity glider;

	public GliderEetleFlyGoal(GliderEetleEntity glider) {
		super(glider, 1.0F);
		this.glider = glider;
	}

	@Override
	public boolean shouldExecute() {
		if (this.glider.isFlying()) {
			if (!this.mustUpdate) {
				if (this.creature.getIdleTime() >= 100) {
					return false;
				}

				if (this.creature.getRNG().nextInt(this.glider.getPassengers().isEmpty() ? this.executionChance : 40) != 0) {
					return false;
				}
			}

			Vector3d vector3d = this.getPosition();
			if (vector3d == null) {
				return false;
			} else {
				this.x = vector3d.x;
				this.y = vector3d.y;
				this.z = vector3d.z;
				this.mustUpdate = false;
				return true;
			}
		}
		return false;
	}

	public boolean shouldContinueExecuting() {
		return !this.creature.getNavigator().noPath();
	}

	@Nullable
	@Override
	protected Vector3d getPosition() {
		GliderEetleEntity glider = this.glider;
		if (glider.isFlying() && !glider.getPassengers().isEmpty()) {
			return RandomPositionGenerator.findAirTarget(glider, 8, 8, glider.getLook(0.0F), ((float)Math.PI / 2.0F), 2, 1);
		}
		return super.getPosition();
	}
}
