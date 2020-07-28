package com.minecraftabnormals.endergetic.common.entities.puffbug.ai;

import com.minecraftabnormals.endergetic.common.entities.puffbug.PuffBugEntity;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3d;

public class PuffBugAttachToHiveGoal extends Goal {
	private PuffBugEntity puffbug;
	private int ticksPassed;
	
	public PuffBugAttachToHiveGoal(PuffBugEntity puffbug) {
		this.puffbug = puffbug;
	}

	@Override
	public boolean shouldExecute() {
		Direction side = this.puffbug.getDesiredHiveSide();
		if(side != null && this.puffbug.getHive() != null) {
			if(this.puffbug.isAtCorrectRestLocation(side)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void startExecuting() {
		this.puffbug.getNavigator().clearPath();
		this.puffbug.setAIMoveSpeed(0.0F);
		
		this.puffbug.setMotion(Vector3d.ZERO);
	}
	
	@Override
	public void tick() {
		this.ticksPassed++;
		
		this.puffbug.getNavigator().clearPath();
		this.puffbug.setAIMoveSpeed(0.0F);
		
		this.puffbug.setMotion(this.puffbug.getMotion().mul(1.0F, 0.0F, 1.0F));
		
		if(this.ticksPassed > 25) {
			this.puffbug.setAttachedHiveSide(this.puffbug.getDesiredHiveSide());
			this.puffbug.setDesiredHiveSide(null);
		}
	}
	
	@Override
	public void resetTask() {
		this.ticksPassed = 0;
	}
}