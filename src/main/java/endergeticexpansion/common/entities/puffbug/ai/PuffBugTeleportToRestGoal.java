package endergeticexpansion.common.entities.puffbug.ai;

import java.util.EnumSet;

import endergeticexpansion.common.entities.puffbug.EntityPuffBug;
import endergeticexpansion.common.tileentities.TileEntityPuffBugHive;
import endergeticexpansion.common.tileentities.TileEntityPuffBugHive.HiveOccupantData;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class PuffBugTeleportToRestGoal extends Goal {
	private EntityPuffBug puffbug;
	
	public PuffBugTeleportToRestGoal(EntityPuffBug puffbug) {
		this.puffbug = puffbug;
		this.setMutexFlags(EnumSet.of(Flag.MOVE));
	}

	@Override
	public boolean shouldExecute() {
		if(this.puffbug.wantsToRest() && this.puffbug.getRNG().nextInt(50) == 0 && !this.puffbug.isInLove() && !this.puffbug.hasLevitation() && this.puffbug.getAttachedHiveSide() == Direction.UP && this.puffbug.getHive() != null && this.puffbug.getPollinationPos() == null && this.puffbug.getTeleportController().canTeleport()) {
			TileEntityPuffBugHive hive = this.puffbug.getHive();
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
		this.puffbug.setMotion(Vec3d.ZERO);
	}
	
	@Override
	public void tick() {
		this.puffbug.setMotion(Vec3d.ZERO);
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		return !this.puffbug.isInLove() && this.puffbug.isEndimationPlaying(EntityPuffBug.TELEPORT_TO_ANIMATION);
	}
}