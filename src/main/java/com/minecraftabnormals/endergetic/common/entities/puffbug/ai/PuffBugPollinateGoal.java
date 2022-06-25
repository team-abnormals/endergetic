package com.minecraftabnormals.endergetic.common.entities.puffbug.ai;

import com.minecraftabnormals.endergetic.common.entities.puffbug.PuffBugEntity;
import com.minecraftabnormals.endergetic.common.tileentities.BolloomBudTileEntity;

import com.minecraftabnormals.endergetic.core.registry.other.EEPlayableEndimations;
import com.teamabnormals.blueprint.core.endimator.PlayableEndimation;
import com.teamabnormals.blueprint.core.endimator.entity.EndimatedGoal;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.Level;

public class PuffBugPollinateGoal extends EndimatedGoal<PuffBugEntity> {
	private Level world;
	private float originalPosX, originalPosY, originalPosZ;

	public PuffBugPollinateGoal(PuffBugEntity puffbug) {
		super(puffbug, EEPlayableEndimations.PUFF_BUG_POLLINATE);
		this.world = puffbug.level;
	}

	@Override
	public boolean canUse() {
		if (this.entity.getPollinationPos() != null) {
			BlockEntity te = this.world.getBlockEntity(this.entity.getPollinationPos());
			return te instanceof BolloomBudTileEntity && ((BolloomBudTileEntity) te).canBeOpened();
		}
		return false;
	}

	@Override
	public boolean canContinueToUse() {
		if (this.entity.getPollinationPos() != null) {
			BlockEntity te = this.world.getBlockEntity(this.entity.getPollinationPos());
			if (!(te instanceof BolloomBudTileEntity && ((BolloomBudTileEntity) te).canBeOpened())) {
				return false;
			}
		} else {
			return false;
		}
		return
				!this.entity.hasLevitation() &&
						this.entity.isEndimationPlaying(EEPlayableEndimations.PUFF_BUG_POLLINATE) &&
						this.entity.getX() == this.originalPosX &&
						this.entity.getZ() == this.originalPosZ &&
						Math.abs(this.originalPosY - this.entity.getY()) < 0.5F
				;
	}

	@Override
	public void tick() {
		this.entity.getRotationController().rotate(0.0F, 180.0F, 0.0F, 20);
		this.entity.puffCooldown = 10;

		this.entity.setBoosting(false);
		this.entity.setSpeed(0.0F);
		this.entity.getNavigation().stop();
	}

	@Override
	public void start() {
		this.entity.setBoosting(false);
		this.entity.setSpeed(0.0F);
		this.entity.getNavigation().stop();

		this.originalPosX = (float) this.entity.getX();
		this.originalPosY = (float) this.entity.getY();
		this.originalPosZ = (float) this.entity.getZ();

		this.playEndimation();
	}

	@Override
	public void stop() {
		this.entity.setPollinationPos(null);
		this.originalPosX = this.originalPosY = this.originalPosZ = 0.0F;
		this.playEndimation(PlayableEndimation.BLANK);
	}
}