package com.minecraftabnormals.endergetic.common.entities.puffbug.ai;

import com.teamabnormals.abnormals_core.core.utils.NetworkUtil;
import com.minecraftabnormals.endergetic.common.entities.puffbug.PuffBugEntity;
import com.minecraftabnormals.endergetic.common.tileentities.BolloomBudTileEntity;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PuffBugPollinateGoal extends Goal {
	private PuffBugEntity puffbug;
	private World world;
	private float originalPosX, originalPosY, originalPosZ;
	
	public PuffBugPollinateGoal(PuffBugEntity puffbug) {
		this.puffbug = puffbug;
		this.world = this.puffbug.world;
	}

	@Override
	public boolean shouldExecute() {
		if(this.puffbug.getPollinationPos() != null) {
			TileEntity te = this.world.getTileEntity(this.puffbug.getPollinationPos());
			if(te instanceof BolloomBudTileEntity && ((BolloomBudTileEntity) te).canBeOpened()) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		if(this.puffbug.getPollinationPos() != null) {
			TileEntity te = this.world.getTileEntity(this.puffbug.getPollinationPos());
			if(!(te instanceof BolloomBudTileEntity && ((BolloomBudTileEntity) te).canBeOpened())) {
				return false;
			}
		} else {
			return false;
		}
		return
			!this.puffbug.hasLevitation() &&
			this.puffbug.isEndimationPlaying(PuffBugEntity.POLLINATE_ANIMATION) &&
			this.puffbug.getPosX() == this.originalPosX &&
			this.puffbug.getPosZ() == this.originalPosZ &&
			Math.abs(this.originalPosY - this.puffbug.getPosY()) < 0.5F
		;
	}
	
	@Override
	public void tick() {
		this.puffbug.getRotationController().rotate(0.0F, 180.0F, 0.0F, 20);
		this.puffbug.puffCooldown = 10;
		
		this.puffbug.setBoosting(false);
		this.puffbug.setAIMoveSpeed(0.0F);
		this.puffbug.getNavigator().clearPath();
	}
	
	@Override
	public void startExecuting() {
		this.puffbug.setBoosting(false);
		this.puffbug.setAIMoveSpeed(0.0F);
		this.puffbug.getNavigator().clearPath();
		
		this.originalPosX = (float) this.puffbug.getPosX();
		this.originalPosY = (float) this.puffbug.getPosY();
		this.originalPosZ = (float) this.puffbug.getPosZ();
		
		NetworkUtil.setPlayingAnimationMessage(this.puffbug, PuffBugEntity.POLLINATE_ANIMATION);
	}
	
	@Override
	public void resetTask() {
		this.puffbug.setPollinationPos(null);
		this.originalPosX = this.originalPosY = this.originalPosZ = 0.0F;
		
		NetworkUtil.setPlayingAnimationMessage(this.puffbug, PuffBugEntity.BLANK_ANIMATION);
	}
}