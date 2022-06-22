package com.minecraftabnormals.endergetic.common.entities.puffbug.ai;

import com.minecraftabnormals.abnormals_core.core.endimator.entity.EndimatedGoal;
import com.minecraftabnormals.endergetic.common.entities.puffbug.PuffBugEntity;
import com.minecraftabnormals.endergetic.common.tileentities.BolloomBudTileEntity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PuffBugPollinateGoal extends EndimatedGoal<PuffBugEntity> {
	private World world;
	private float originalPosX, originalPosY, originalPosZ;

	public PuffBugPollinateGoal(PuffBugEntity puffbug) {
		super(puffbug, PuffBugEntity.POLLINATE_ANIMATION);
		this.world = puffbug.level;
	}

	@Override
	public boolean canUse() {
		if (this.entity.getPollinationPos() != null) {
			TileEntity te = this.world.getBlockEntity(this.entity.getPollinationPos());
			if (te instanceof BolloomBudTileEntity && ((BolloomBudTileEntity) te).canBeOpened()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean canContinueToUse() {
		if (this.entity.getPollinationPos() != null) {
			TileEntity te = this.world.getBlockEntity(this.entity.getPollinationPos());
			if (!(te instanceof BolloomBudTileEntity && ((BolloomBudTileEntity) te).canBeOpened())) {
				return false;
			}
		} else {
			return false;
		}
		return
				!this.entity.hasLevitation() &&
						this.entity.isEndimationPlaying(PuffBugEntity.POLLINATE_ANIMATION) &&
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
		this.playEndimation(PuffBugEntity.BLANK_ANIMATION);
	}
}