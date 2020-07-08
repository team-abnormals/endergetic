package endergeticexpansion.common.entities.puffbug.ai;

import java.util.EnumSet;

import javax.annotation.Nullable;

import endergeticexpansion.common.blocks.poise.BolloomBudBlock;
import endergeticexpansion.common.entities.puffbug.PuffBugEntity;
import endergeticexpansion.common.tileentities.BolloomBudTileEntity;
import endergeticexpansion.core.registry.EEBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class PuffBugTeleportToBudGoal extends Goal {
	private static final int AREA_CHECK_SIZE = 26;
	private PuffBugEntity puffbug;
	private World world;
	
	public PuffBugTeleportToBudGoal(PuffBugEntity puffbug) {
		this.puffbug = puffbug;
		this.world = puffbug.world;
		this.setMutexFlags(EnumSet.of(Flag.MOVE));
	}

	@Override
	public boolean shouldExecute() {
		if(!this.puffbug.isPassenger() && this.puffbug.getAttackTarget() == null && this.puffbug.isNoEndimationPlaying() && this.puffbug.getRNG().nextInt(100) == 0 && !this.puffbug.hasLevitation() && !this.puffbug.isInLove() && !this.puffbug.wantsToRest() && this.puffbug.getTeleportController().canTeleport()) {
			BolloomBudTileEntity bud = this.findNearbyBud();
			if(bud != null) {
				BlockPos pos = this.createUpperPosition(bud.getPos());
				if(pos != null && this.puffbug.getTeleportController().tryToCreateDesinationTo(pos, null)) {
					bud.setTeleportingBug(this.puffbug);
					this.puffbug.setBudPos(bud.getPos());
					return true;
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
	
	@Nullable
	private BolloomBudTileEntity findNearbyBud() {
		BlockPos pos = this.puffbug.func_233580_cy_();
		for(BlockPos blockpos : BlockPos.getAllInBoxMutable(pos.add(-AREA_CHECK_SIZE, -AREA_CHECK_SIZE / 2, -AREA_CHECK_SIZE), pos.add(AREA_CHECK_SIZE, AREA_CHECK_SIZE / 2, AREA_CHECK_SIZE))) {
			if(blockpos.withinDistance(this.puffbug.getPositionVec(), AREA_CHECK_SIZE)) {
				if(this.world.getBlockState(blockpos).getBlock() == EEBlocks.BOLLOOM_BUD.get() && this.world.getTileEntity(blockpos) instanceof BolloomBudTileEntity) {
					BolloomBudTileEntity bud = (BolloomBudTileEntity) this.world.getTileEntity(blockpos);
					if(!bud.getBlockState().get(BolloomBudBlock.OPENED) && this.isPathNotBlockedByEntity(bud) && !bud.hasTeleportingBug() && bud.canBeOpened()) {
						return bud;
					}
				}
			}
		}
		return null;
	}
	
	@Nullable
	private BlockPos createUpperPosition(BlockPos pos) {
		BlockPos foundPos = null;
		for(int y = 1; y < 3; y++) {
			if(this.world.getBlockState(pos.up(y)).getCollisionShape(this.world, pos).isEmpty() && this.world.getFluidState(pos.up(y)).isEmpty()) {
				foundPos = pos.up(y);
			} else {
				foundPos = null;
				break;
			}
		}
		return foundPos;
	}
	
	private boolean isPathNotBlockedByEntity(BolloomBudTileEntity bud) {
		return this.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(bud.getPos()).expand(0.0F, 3.0F, 0.0F)).isEmpty();
	}
}