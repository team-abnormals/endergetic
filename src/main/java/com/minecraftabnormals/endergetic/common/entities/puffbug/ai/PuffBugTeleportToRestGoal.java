package com.minecraftabnormals.endergetic.common.entities.puffbug.ai;

import java.util.EnumSet;

import com.minecraftabnormals.endergetic.common.entities.puffbug.PuffBugEntity;
import com.minecraftabnormals.endergetic.common.tileentities.PuffBugHiveTileEntity;
import com.minecraftabnormals.endergetic.common.tileentities.PuffBugHiveTileEntity.HiveOccupantData;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

public class PuffBugTeleportToRestGoal extends Goal {
	private PuffBugEntity puffbug;
	
	public PuffBugTeleportToRestGoal(PuffBugEntity puffbug) {
		this.puffbug = puffbug;
		this.setMutexFlags(EnumSet.of(Flag.MOVE));
	}

	@Override
	public boolean shouldExecute() {
		if(this.puffbug.getAttackTarget() == null && this.puffbug.wantsToRest() && this.puffbug.getRNG().nextInt(50) == 0 && !this.puffbug.isInLove() && !this.puffbug.hasLevitation() && this.puffbug.getAttachedHiveSide() == Direction.UP && this.puffbug.getHive() != null && this.puffbug.getPollinationPos() == null && this.puffbug.getTeleportController().canTeleport()) {
			PuffBugHiveTileEntity hive = this.puffbug.getHive();
			for(Direction directions : Direction.values()) {
				if(HiveOccupantData.isHiveSideEmpty(hive, directions)) {
					BlockPos offset = hive.getPos().offset(directions);
					if(!hive.isSideBeingTeleportedTo(directions) && this.puffbug.getTeleportController().tryToCreateDesinationTo(offset, directions)) {
						this.puffbug.setTeleportHiveSide(directions);
						hive.setBeingTeleportedToBy(this.puffbug, directions);
						return true;
					}
				}
			}
		}
		return false;
	}
	
	@Override
	public void startExecuting() {
		this.puffbug.getTeleportController().processTeleportation();
		this.puffbug.setMotion(Vector3d.ZERO);
	}
	
	@Override
	public void tick() {
		this.puffbug.setMotion(Vector3d.ZERO);
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		return !this.puffbug.isInLove() && this.puffbug.isEndimationPlaying(PuffBugEntity.TELEPORT_TO_ANIMATION);
	}
}