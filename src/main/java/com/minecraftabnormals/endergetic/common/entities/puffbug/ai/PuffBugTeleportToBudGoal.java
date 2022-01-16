package com.minecraftabnormals.endergetic.common.entities.puffbug.ai;

import com.minecraftabnormals.endergetic.common.blocks.poise.BolloomBudBlock;
import com.minecraftabnormals.endergetic.common.entities.puffbug.PuffBugEntity;
import com.minecraftabnormals.endergetic.common.tileentities.BolloomBudTileEntity;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class PuffBugTeleportToBudGoal extends Goal {
	private static final int AREA_CHECK_SIZE = 26;
	private PuffBugEntity puffbug;
	private World world;

	public PuffBugTeleportToBudGoal(PuffBugEntity puffbug) {
		this.puffbug = puffbug;
		this.world = puffbug.level;
		this.setFlags(EnumSet.of(Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		if (!this.puffbug.isPassenger() && this.puffbug.getTarget() == null && this.puffbug.isNoEndimationPlaying() && this.puffbug.getRandom().nextInt(100) == 0 && !this.puffbug.hasLevitation() && !this.puffbug.isInLove() && !this.puffbug.wantsToRest() && this.puffbug.getTeleportController().canTeleport()) {
			BolloomBudTileEntity bud = this.findNearbyBud();
			if (bud != null) {
				BlockPos pos = this.createUpperPosition(bud.getBlockPos());
				if (pos != null && this.puffbug.getTeleportController().tryToCreateDesinationTo(pos, null)) {
					bud.setTeleportingBug(this.puffbug);
					this.puffbug.setBudPos(bud.getBlockPos());
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void start() {
		this.puffbug.getTeleportController().processTeleportation();
		this.puffbug.setDeltaMovement(Vector3d.ZERO);
	}

	@Override
	public void tick() {
		this.puffbug.setDeltaMovement(Vector3d.ZERO);
	}

	@Override
	public boolean canContinueToUse() {
		return !this.puffbug.isInLove() && this.puffbug.isEndimationPlaying(PuffBugEntity.TELEPORT_TO_ANIMATION);
	}

	@Nullable
	private BolloomBudTileEntity findNearbyBud() {
		BlockPos pos = this.puffbug.blockPosition();
		for (BlockPos blockpos : BlockPos.betweenClosed(pos.offset(-AREA_CHECK_SIZE, -AREA_CHECK_SIZE / 2, -AREA_CHECK_SIZE), pos.offset(AREA_CHECK_SIZE, AREA_CHECK_SIZE / 2, AREA_CHECK_SIZE))) {
			if (blockpos.closerThan(this.puffbug.position(), AREA_CHECK_SIZE)) {
				if (this.world.getBlockState(blockpos).getBlock() == EEBlocks.BOLLOOM_BUD.get() && this.world.getBlockEntity(blockpos) instanceof BolloomBudTileEntity) {
					BolloomBudTileEntity bud = (BolloomBudTileEntity) this.world.getBlockEntity(blockpos);
					if (!bud.getBlockState().getValue(BolloomBudBlock.OPENED) && this.isPathNotBlockedByEntity(bud) && !bud.hasTeleportingBug() && bud.canBeOpened()) {
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
		for (int y = 1; y < 3; y++) {
			if (this.world.getBlockState(pos.above(y)).getCollisionShape(this.world, pos).isEmpty() && this.world.getFluidState(pos.above(y)).isEmpty()) {
				foundPos = pos.above(y);
			} else {
				foundPos = null;
				break;
			}
		}
		return foundPos;
	}

	private boolean isPathNotBlockedByEntity(BolloomBudTileEntity bud) {
		return this.world.getEntitiesOfClass(Entity.class, new AxisAlignedBB(bud.getBlockPos()).expandTowards(0.0F, 3.0F, 0.0F)).isEmpty();
	}
}