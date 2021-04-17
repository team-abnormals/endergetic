package com.minecraftabnormals.endergetic.common.entities.eetle.flying;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

public final class FlyingEetleMoveController<F extends MobEntity & IFlyingEetle> extends MovementController {
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
		if (this.action == Action.MOVE_TO) {
			eetle.setMoving(true);
			this.action = Action.WAIT;
			double distanceX = this.posX - eetle.getPosX();
			double distanceY = this.posY - eetle.getPosY();
			double distanceZ = this.posZ - eetle.getPosZ();
			double magnitude = distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ;
			if (magnitude < 0.00000025F) {
				eetle.setMoveVertical(0.0F);
				eetle.setMoveForward(0.0F);
				return;
			}

			float yaw = (float) (MathHelper.atan2(distanceZ, distanceX) * 57.3D) - 90.0F;
			eetle.rotationYaw = this.limitAngle(eetle.rotationYaw, yaw, this.deltaYaw);
			float f1 = (float) (this.speed * eetle.getAttributeValue(Attributes.FLYING_SPEED));
			eetle.setAIMoveSpeed(f1);
			double horizontalMag = MathHelper.sqrt(distanceX * distanceX + distanceZ * distanceZ);
			float pitch = (float) (-(MathHelper.atan2(distanceY, horizontalMag) * 57.3D));
			float limitedPitch = this.limitAngle(eetle.rotationPitch, pitch, 40.0F);
			eetle.rotationPitch = limitedPitch;

			float targetRoll = 0.0F;
			Vector3d lookVec = eetle.getLook(1.0F);
			Vector3d motion = eetle.getMotion();
			double motionHMag = Entity.horizontalMag(motion);
			double lookHMag = Entity.horizontalMag(lookVec);
			if (motionHMag > 0.0D && lookHMag > 0.0D) {
				double rollRatio = MathHelper.clamp((motion.x * lookVec.x + motion.z * lookVec.z) / Math.sqrt(motionHMag * lookHMag), -1.0F, 1.0F);
				double horizontalDifference = motion.x * lookVec.z - motion.z * lookVec.x;
				float maxRoll = this.maxRoll;
				targetRoll = MathHelper.clamp((float) ((Math.signum(horizontalDifference) * Math.acos(rollRatio)) * 57.3D), -maxRoll, maxRoll);
			}
			eetle.setTargetFlyingRotations(new TargetFlyingRotations(limitedPitch, targetRoll));
			eetle.setMoveVertical(distanceY > 0.0D ? f1 : -f1);
		} else {
			eetle.setMoving(false);
			eetle.setMoveVertical(0.0F);
			eetle.setMoveForward(0.0F);
		}
	}
}
