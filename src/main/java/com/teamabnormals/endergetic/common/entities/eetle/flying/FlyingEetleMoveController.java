package com.teamabnormals.endergetic.common.entities.eetle.flying;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public final class FlyingEetleMoveController<F extends Mob & IFlyingEetle> extends MoveControl {
	private final F eetle;
	private final float maxRoll;
	private final float deltaYaw;

	public FlyingEetleMoveController(F flyingEetle, float maxRoll, float deltaYaw) {
		super(flyingEetle);
		this.eetle = flyingEetle;
		this.maxRoll = maxRoll;
		this.deltaYaw = deltaYaw;
	}

	@Override
	public void tick() {
		F eetle = this.eetle;
		if (this.operation == Operation.MOVE_TO) {
			eetle.setMoving(true);
			this.operation = Operation.WAIT;
			double distanceX = this.wantedX - eetle.getX();
			double distanceY = this.wantedY - eetle.getY();
			double distanceZ = this.wantedZ - eetle.getZ();
			double magnitude = distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ;
			if (magnitude < 0.00000025F) {
				eetle.setYya(0.0F);
				eetle.setZza(0.0F);
				return;
			}

			float yaw = (float) (Mth.atan2(distanceZ, distanceX) * 57.3D) - 90.0F;
			eetle.setYRot(this.rotlerp(eetle.getYRot(), yaw, this.deltaYaw));
			float f1 = (float) (this.speedModifier * eetle.getAttributeValue(Attributes.FLYING_SPEED));
			eetle.setSpeed(f1);
			double horizontalMag = Mth.sqrt((float) (distanceX * distanceX + distanceZ * distanceZ));
			float pitch = (float) (-(Mth.atan2(distanceY, horizontalMag) * 57.3D));
			float limitedPitch = this.rotlerp(eetle.getXRot(), pitch, 40.0F);
			eetle.setXRot(limitedPitch);

			float targetRoll = 0.0F;
			Vec3 lookVec = eetle.getViewVector(1.0F);
			Vec3 motion = eetle.getDeltaMovement();
			double motionHMag = motion.horizontalDistanceSqr();
			double lookHMag = lookVec.horizontalDistanceSqr();
			if (motionHMag > 0.0D && lookHMag > 0.0D) {
				double rollRatio = Mth.clamp((motion.x * lookVec.x + motion.z * lookVec.z) / Math.sqrt(motionHMag * lookHMag), -1.0F, 1.0F);
				double horizontalDifference = motion.x * lookVec.z - motion.z * lookVec.x;
				float maxRoll = this.maxRoll;
				targetRoll = Mth.clamp((float) ((Math.signum(horizontalDifference) * Math.acos(rollRatio)) * 57.3D), -maxRoll, maxRoll);
			}
			eetle.setTargetFlyingRotations(new TargetFlyingRotations(limitedPitch, targetRoll));
			eetle.setYya(distanceY > 0.0D ? f1 : -f1);
		} else {
			eetle.setMoving(false);
			eetle.setYya(0.0F);
			eetle.setZza(0.0F);
		}
	}
}
