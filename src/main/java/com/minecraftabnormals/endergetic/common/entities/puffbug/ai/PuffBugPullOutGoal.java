package com.minecraftabnormals.endergetic.common.entities.puffbug.ai;

import java.util.EnumSet;

import com.teamabnormals.abnormals_core.core.utils.NetworkUtil;
import com.minecraftabnormals.endergetic.common.entities.puffbug.PuffBugEntity;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

public class PuffBugPullOutGoal extends Goal {
	private PuffBugEntity puffbug;
	private int pulls;
	
	public PuffBugPullOutGoal(PuffBugEntity puffbug) {
		this.puffbug = puffbug;
		this.setMutexFlags(EnumSet.of(Flag.MOVE));
	}

	@Override
	public boolean shouldExecute() {
		return !this.puffbug.isInflated() && this.puffbug.isNoEndimationPlaying() && this.puffbug.stuckInBlock;
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		return !this.puffbug.isInflated() && this.puffbug.stuckInBlock;
	}
	
	@Override
	public void startExecuting() {
		this.pulls = this.puffbug.getRNG().nextInt(4) + 2;
	}
	
	@Override
	public void tick() {
		if (this.puffbug.isNoEndimationPlaying()) {
			if (this.pulls > 0) {
				NetworkUtil.setPlayingAnimationMessage(this.puffbug, PuffBugEntity.PULL_ANIMATION);
				this.pulls--;
			}
		} else if (this.puffbug.isEndimationPlaying(PuffBugEntity.PULL_ANIMATION)) {
			if (this.pulls <= 0 && this.puffbug.getAnimationTick() == 5) {
				this.puffbug.disableProjectile();
				this.puffbug.stuckInBlockState = null;
				
				float[] rotations = this.puffbug.getRotationController().getRotations(1.0F);
				
				float motionX = MathHelper.sin(rotations[1] * ((float) Math.PI / 180F)) * MathHelper.cos(rotations[0] * ((float) Math.PI / 180F));
				float motionY = -MathHelper.sin(rotations[0] * ((float) Math.PI / 180F));
				float motionZ = -MathHelper.cos(rotations[1] * ((float) Math.PI / 180F)) * MathHelper.cos(rotations[0] * ((float) Math.PI / 180F));
				
				Vector3d popOutMotion = new Vector3d(motionX, motionY, motionZ).normalize().scale(0.25F);
				
				this.puffbug.setMotion(popOutMotion);
			}
		}
	}
	
	@Override
	public void resetTask() {
		NetworkUtil.setPlayingAnimationMessage(this.puffbug, PuffBugEntity.BLANK_ANIMATION);
	}
}