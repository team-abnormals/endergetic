package com.minecraftabnormals.endergetic.common.entities.puffbug.ai;

import java.util.EnumSet;
import java.util.Random;

import com.minecraftabnormals.abnormals_core.core.util.MathUtil;
import com.minecraftabnormals.abnormals_core.core.util.NetworkUtil;
import com.minecraftabnormals.endergetic.common.entities.puffbug.PuffBugEntity;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

public class PuffBugRotateToFireGoal extends Goal {
	private final PuffBugEntity puffbug;
	private final Random random;
	private int ticksPassed;
	private int ticksToRotate;

	public PuffBugRotateToFireGoal(PuffBugEntity puffbug) {
		this.puffbug = puffbug;
		this.random = puffbug.getRNG();
		this.setMutexFlags(EnumSet.of(Flag.LOOK));
	}

	@Override
	public boolean shouldExecute() {
		return !this.puffbug.isPassenger() && !this.puffbug.isEndimationPlaying(PuffBugEntity.TELEPORT_FROM_ANIMATION) && this.puffbug.isInflated() && this.puffbug.getLaunchDirection() != null;
	}

	@Override
	public boolean shouldContinueExecuting() {
		return this.shouldExecute() && this.ticksPassed <= this.ticksToRotate;
	}

	@Override
	public void startExecuting() {
		this.ticksToRotate = this.random.nextInt(5) + 12;
	}

	@Override
	public void resetTask() {
		if (this.puffbug.getLaunchDirection() != null && this.ticksPassed >= this.ticksToRotate) {
			Vector3d launch = this.puffbug.getLaunchDirection();

			float yaw = (float) launch.getY() - this.puffbug.rotationYaw;
			float pitch = MathHelper.wrapSubtractDegrees((float) launch.getX(), 0.0F);

			float x = -MathHelper.sin(yaw * ((float) Math.PI / 180F)) * MathHelper.cos(pitch * ((float) Math.PI / 180F));
			float y = -MathHelper.sin(pitch * ((float) Math.PI / 180F));
			float z = MathHelper.cos(yaw * ((float) Math.PI / 180F)) * MathHelper.cos(pitch * ((float) Math.PI / 180F));

			Vector3d motion = new Vector3d(x, -y, z).normalize();

			this.puffbug.setMotion(this.puffbug.getMotion().add(motion.scale(1.0F)));

			this.puffbug.setFireDirection((float) launch.getX(), (float) launch.getY());
			this.puffbug.removeLaunchDirection();
			this.puffbug.setInflated(false);
			NetworkUtil.setPlayingAnimationMessage(this.puffbug, PuffBugEntity.FLY_ANIMATION);

			Vector3d pos = this.puffbug.getPositionVec();
			double posX = pos.getX();
			double posY = pos.getY();
			double posZ = pos.getZ();
			for (int i = 0; i < 3; i++) {
				float particleX = MathHelper.sin(yaw * ((float) Math.PI / 180F)) * MathHelper.cos(pitch * ((float) Math.PI / 180F));
				float particleY = -MathHelper.sin(pitch * ((float) Math.PI / 180F));
				float particleZ = -MathHelper.cos(yaw * ((float) Math.PI / 180F)) * MathHelper.cos(pitch * ((float) Math.PI / 180F));

				Vector3d particleMotion = new Vector3d(particleX, particleY, particleZ).normalize().scale(0.5F);

				NetworkUtil.spawnParticle("endergetic:short_poise_bubble", posX, posY, posZ, particleMotion.getX() + MathUtil.makeNegativeRandomly((this.random.nextFloat() * 0.25F), this.random), particleMotion.getY() + (this.random.nextFloat() * 0.05F), MathUtil.makeNegativeRandomly(particleMotion.getZ() + (this.random.nextFloat() * 0.25F), this.random));
			}
		}
		this.ticksPassed = 0;
		this.ticksToRotate = 0;
	}

	@Override
	public void tick() {
		this.ticksPassed++;

		this.puffbug.getNavigator().clearPath();

		Vector3d launchDirection = this.puffbug.getLaunchDirection();

		this.puffbug.getRotationController().rotate((float) MathHelper.wrapDegrees(launchDirection.getY() - this.puffbug.rotationYaw), (float) launchDirection.getX() + 90.0F, 0.0F, 10);
	}
}