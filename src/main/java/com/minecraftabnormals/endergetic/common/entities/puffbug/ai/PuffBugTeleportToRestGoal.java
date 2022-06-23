package com.minecraftabnormals.endergetic.common.entities.puffbug.ai;

import java.util.EnumSet;

import com.minecraftabnormals.endergetic.common.entities.puffbug.PuffBugEntity;
import com.minecraftabnormals.endergetic.common.tileentities.PuffBugHiveTileEntity;
import com.minecraftabnormals.endergetic.common.tileentities.PuffBugHiveTileEntity.HiveOccupantData;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

import net.minecraft.world.entity.ai.goal.Goal.Flag;

public class PuffBugTeleportToRestGoal extends Goal {
	private PuffBugEntity puffbug;

	public PuffBugTeleportToRestGoal(PuffBugEntity puffbug) {
		this.puffbug = puffbug;
		this.setFlags(EnumSet.of(Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		if (this.puffbug.getTarget() == null && this.puffbug.wantsToRest() && this.puffbug.getRandom().nextInt(50) == 0 && !this.puffbug.isInLove() && !this.puffbug.hasLevitation() && this.puffbug.getAttachedHiveSide() == Direction.UP && this.puffbug.getHive() != null && this.puffbug.getPollinationPos() == null && this.puffbug.getTeleportController().canTeleport()) {
			PuffBugHiveTileEntity hive = this.puffbug.getHive();
			for (Direction directions : Direction.values()) {
				if (HiveOccupantData.isHiveSideEmpty(hive, directions)) {
					BlockPos offset = hive.getBlockPos().relative(directions);
					if (!hive.isSideBeingTeleportedTo(directions) && this.puffbug.getTeleportController().tryToCreateDesinationTo(offset, directions)) {
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
	public void start() {
		this.puffbug.getTeleportController().processTeleportation();
		this.puffbug.setDeltaMovement(Vec3.ZERO);
	}

	@Override
	public void tick() {
		this.puffbug.setDeltaMovement(Vec3.ZERO);
	}

	@Override
	public boolean canContinueToUse() {
		return !this.puffbug.isInLove() && this.puffbug.isEndimationPlaying(PuffBugEntity.TELEPORT_TO_ANIMATION);
	}
}