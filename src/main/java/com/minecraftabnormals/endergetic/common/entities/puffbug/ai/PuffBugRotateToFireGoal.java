package com.minecraftabnormals.endergetic.common.entities.puffbug.ai;

import com.minecraftabnormals.abnormals_core.core.util.MathUtil;
import com.minecraftabnormals.abnormals_core.core.util.NetworkUtil;
import com.minecraftabnormals.endergetic.common.entities.puffbug.PuffBugEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

import java.util.EnumSet;
import java.util.Random;

public class PuffBugRotateToFireGoal extends Goal {
	private final PuffBugEntity puffbug;
	private final Random random;
	private int ticksPassed;
	private int ticksToRotate;

	public PuffBugRotateToFireGoal(PuffBugEntity puffbug) {
		this.puffbug = puffbug;
		this.random = puffbug.getRandom();
		this.setFlags(EnumSet.of(Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		return !this.puffbug.isPassenger() && !this.puffbug.isEndimationPlaying(PuffBugEntity.TELEPORT_FROM_ANIMATION) && this.puffbug.isInflated() && this.puffbug.getLaunchDirection() != null;
	}

	@Override
	public boolean canContinueToUse() {
		return this.canUse() && this.ticksPassed <= this.ticksToRotate;
	}

	@Override
	public void start() {
		this.ticksToRotate = this.random.nextInt(5) + 12;
	}

	@Override
	public void stop() {
		if (this.puffbug.getLaunchDirection() != null && this.ticksPassed >= this.ticksToRotate) {
			Vector3d launch = this.puffbug.getLaunchDirection();

			float yaw = (float) launch.y() - this.puffbug.yRot;
			float pitch = MathHelper.degreesDifference((float) launch.x(), 0.0F);

			float x = -MathHelper.sin(yaw * ((float) Math.PI / 180F)) * MathHelper.cos(pitch * ((float) Math.PI / 180F));
			float y = -MathHelper.sin(pitch * ((float) Math.PI / 180F));
			float z = MathHelper.cos(yaw * ((float) Math.PI / 180F)) * MathHelper.cos(pitch * ((float) Math.PI / 180F));

			Vector3d motion = new Vector3d(x, -y, z).normalize();

			this.puffbug.setDeltaMovement(this.puffbug.getDeltaMovement().add(motion.scale(1.0F)));

			this.puffbug.setFireDirection((float) launch.x(), (float) launch.y());
			this.puffbug.removeLaunchDirection();
			this.puffbug.setInflated(false);
			NetworkUtil.setPlayingAnimationMessage(this.puffbug, PuffBugEntity.FLY_ANIMATION);

			Vector3d pos = this.puffbug.position();
			double posX = pos.x();
			double posY = pos.y();
			double posZ = pos.z();
			for (int i = 0; i < 3; i++) {
				float particleX = MathHelper.sin(yaw * ((float) Math.PI / 180F)) * MathHelper.cos(pitch * ((float) Math.PI / 180F));
				float particleY = -MathHelper.sin(pitch * ((float) Math.PI / 180F));
				float particleZ = -MathHelper.cos(yaw * ((float) Math.PI / 180F)) * MathHelper.cos(pitch * ((float) Math.PI / 180F));

				Vector3d particleMotion = new Vector3d(particleX, particleY, particleZ).normalize().scale(0.5F);

				NetworkUtil.spawnParticle("endergetic:short_poise_bubble", posX, posY, posZ, particleMotion.x() + MathUtil.makeNegativeRandomly((this.random.nextFloat() * 0.25F), this.random), particleMotion.y() + (this.random.nextFloat() * 0.05F), MathUtil.makeNegativeRandomly(particleMotion.z() + (this.random.nextFloat() * 0.25F), this.random));
			}
		}
		this.ticksPassed = 0;
		this.ticksToRotate = 0;
	}

	@Override
	public void tick() {
		this.ticksPassed++;

		this.puffbug.getNavigation().stop();

		Vector3d launchDirection = this.puffbug.getLaunchDirection();

		this.puffbug.getRotationController().rotate((float) MathHelper.wrapDegrees(launchDirection.y() - this.puffbug.yRot), (float) launchDirection.x() + 90.0F, 0.0F, 10);
	}
}