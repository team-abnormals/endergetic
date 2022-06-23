package com.minecraftabnormals.endergetic.common.entities.puffbug.ai;

import java.util.EnumSet;

import com.minecraftabnormals.abnormals_core.core.endimator.entity.EndimatedGoal;
import com.minecraftabnormals.abnormals_core.core.util.NetworkUtil;
import com.minecraftabnormals.endergetic.common.entities.puffbug.PuffBugEntity;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import net.minecraft.world.entity.ai.goal.Goal.Flag;

public class PuffBugPullOutGoal extends EndimatedGoal<PuffBugEntity> {
	private int pulls;

	public PuffBugPullOutGoal(PuffBugEntity puffbug) {
		super(puffbug, PuffBugEntity.PULL_ANIMATION);
		this.setFlags(EnumSet.of(Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		return !this.entity.isInflated() && this.entity.isNoEndimationPlaying() && this.entity.stuckInBlock;
	}

	@Override
	public boolean canContinueToUse() {
		return !this.entity.isInflated() && this.entity.stuckInBlock;
	}

	@Override
	public void start() {
		this.pulls = this.entity.getRandom().nextInt(4) + 2;
	}

	@Override
	public void tick() {
		if (this.entity.isNoEndimationPlaying()) {
			if (this.pulls > 0) {
				this.playEndimation();
				this.pulls--;
			}
		} else if (this.isEndimationPlaying() && this.isEndimationAtTick(5) && this.pulls <= 0) {
			this.entity.disableProjectile();
			this.entity.stuckInBlockState = null;

			float[] rotations = this.entity.getRotationController().getRotations(1.0F);

			float motionX = Mth.sin(rotations[1] * ((float) Math.PI / 180F)) * Mth.cos(rotations[0] * ((float) Math.PI / 180F));
			float motionY = -Mth.sin(rotations[0] * ((float) Math.PI / 180F));
			float motionZ = -Mth.cos(rotations[1] * ((float) Math.PI / 180F)) * Mth.cos(rotations[0] * ((float) Math.PI / 180F));

			Vec3 popOutMotion = new Vec3(motionX, motionY, motionZ).normalize().scale(0.25F);
			this.entity.setDeltaMovement(popOutMotion);
		}
	}

	@Override
	public void stop() {
		NetworkUtil.setPlayingAnimationMessage(this.entity, PuffBugEntity.BLANK_ANIMATION);
	}
}