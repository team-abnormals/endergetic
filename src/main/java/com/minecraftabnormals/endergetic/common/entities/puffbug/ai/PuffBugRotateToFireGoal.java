package com.minecraftabnormals.endergetic.common.entities.puffbug.ai;

import java.util.EnumSet;

import com.teamabnormals.abnormals_core.core.utils.MathUtils;
import com.teamabnormals.abnormals_core.core.utils.NetworkUtil;
import com.minecraftabnormals.endergetic.common.entities.puffbug.PuffBugEntity;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

public class PuffBugRotateToFireGoal extends Goal {
	private PuffBugEntity puffbug;
	private int ticksPassed;
	private int ticksToRotate;
	
	public PuffBugRotateToFireGoal(PuffBugEntity puffbug) {
		this.puffbug = puffbug;
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
		this.ticksToRotate = this.puffbug.getRNG().nextInt(5) + 12;
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
			
			for (int i = 0; i < 3; i++) {
				Vector3d pos = this.puffbug.getPositionVec();
				
				float particleX = MathHelper.sin(yaw * ((float) Math.PI / 180F)) * MathHelper.cos(pitch * ((float) Math.PI / 180F));
				float particleY = -MathHelper.sin(pitch * ((float) Math.PI / 180F));
				float particleZ = -MathHelper.cos(yaw * ((float) Math.PI / 180F)) * MathHelper.cos(pitch * ((float) Math.PI / 180F));
				
				Vector3d particleMotion = new Vector3d(particleX, particleY, particleZ).normalize().scale(0.5F);
				
				NetworkUtil.spawnParticle("endergetic:short_poise_bubble", pos.getX(), pos.getY(), pos.getZ(), particleMotion.getX() + MathUtils.makeNegativeRandomly((this.puffbug.getRNG().nextFloat() * 0.25F), this.puffbug.getRNG()), particleMotion.getY() + (this.puffbug.getRNG().nextFloat() * 0.05F), MathUtils.makeNegativeRandomly(particleMotion.getZ() + (this.puffbug.getRNG().nextFloat() * 0.25F), this.puffbug.getRNG()));
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