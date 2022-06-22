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
	public boolean canUse() {
		if (this.glider.isFlying()) {
			if (!this.forceTrigger) {
				if (this.mob.getNoActionTime() >= 100) {
					return false;
				}

				if (this.mob.getRandom().nextInt(this.glider.getPassengers().isEmpty() ? this.interval : 40) != 0) {
					return false;
				}
			}

			Vector3d vector3d = this.getPosition();
			if (vector3d == null) {
				return false;
			} else {
				this.wantedX = vector3d.x;
				this.wantedY = vector3d.y;
				this.wantedZ = vector3d.z;
				this.forceTrigger = false;
				return true;
			}
		}
		return false;
	}

	public boolean canContinueToUse() {
		return !this.mob.getNavigation().isDone();
	}

	@Nullable
	@Override
	protected Vector3d getPosition() {
		GliderEetleEntity glider = this.glider;
		if (glider.isFlying() && !glider.getPassengers().isEmpty()) {
			return RandomPositionGenerator.getAboveLandPos(glider, 8, 8, glider.getViewVector(0.0F), ((float)Math.PI / 2.0F), 2, 1);
		}
		return super.getPosition();
	}
}
