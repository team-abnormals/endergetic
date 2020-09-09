package com.minecraftabnormals.endergetic.common.entities.puffbug.ai;

import com.teamabnormals.abnormals_core.core.library.endimator.EndimatedGoal;
import com.teamabnormals.abnormals_core.core.library.endimator.Endimation;
import com.teamabnormals.abnormals_core.core.utils.NetworkUtil;
import com.minecraftabnormals.endergetic.common.entities.puffbug.PuffBugEntity;
import com.minecraftabnormals.endergetic.common.tileentities.BolloomBudTileEntity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PuffBugPollinateGoal extends EndimatedGoal<PuffBugEntity> {
	private World world;
	private float originalPosX, originalPosY, originalPosZ;
	
	public PuffBugPollinateGoal(PuffBugEntity puffbug) {
		super(puffbug);
		this.world = puffbug.world;
	}

	@Override
	public boolean shouldExecute() {
		if (this.entity.getPollinationPos() != null) {
			TileEntity te = this.world.getTileEntity(this.entity.getPollinationPos());
			if (te instanceof BolloomBudTileEntity && ((BolloomBudTileEntity) te).canBeOpened()) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		if (this.entity.getPollinationPos() != null) {
			TileEntity te = this.world.getTileEntity(this.entity.getPollinationPos());
			if (!(te instanceof BolloomBudTileEntity && ((BolloomBudTileEntity) te).canBeOpened())) {
				return false;
			}
		} else {
			return false;
		}
		return
			!this.entity.hasLevitation() &&
			this.entity.isEndimationPlaying(PuffBugEntity.POLLINATE_ANIMATION) &&
			this.entity.getPosX() == this.originalPosX &&
			this.entity.getPosZ() == this.originalPosZ &&
			Math.abs(this.originalPosY - this.entity.getPosY()) < 0.5F
		;
	}
	
	@Override
	public void tick() {
		this.entity.getRotationController().rotate(0.0F, 180.0F, 0.0F, 20);
		this.entity.puffCooldown = 10;
		
		this.entity.setBoosting(false);
		this.entity.setAIMoveSpeed(0.0F);
		this.entity.getNavigator().clearPath();
	}
	
	@Override
	public void startExecuting() {
		this.entity.setBoosting(false);
		this.entity.setAIMoveSpeed(0.0F);
		this.entity.getNavigator().clearPath();
		
		this.originalPosX = (float) this.entity.getPosX();
		this.originalPosY = (float) this.entity.getPosY();
		this.originalPosZ = (float) this.entity.getPosZ();

		this.playEndimation();
	}
	
	@Override
	public void resetTask() {
		this.entity.setPollinationPos(null);
		this.originalPosX = this.originalPosY = this.originalPosZ = 0.0F;
		
		NetworkUtil.setPlayingAnimationMessage(this.entity, PuffBugEntity.BLANK_ANIMATION);
	}

	@Override
	protected Endimation getEndimation() {
		return PuffBugEntity.POLLINATE_ANIMATION;
	}
}