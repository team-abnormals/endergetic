package com.minecraftabnormals.endergetic.common.entities.puffbug.ai;

import java.util.EnumSet;

import javax.annotation.Nullable;

import com.minecraftabnormals.endergetic.common.blocks.poise.BolloomBudBlock;
import com.minecraftabnormals.endergetic.common.entities.puffbug.PuffBugEntity;
import com.minecraftabnormals.endergetic.common.tileentities.BolloomBudTileEntity;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;

import net.minecraft.world.entity.ai.goal.Goal.Flag;

public class PuffBugTeleportToBudGoal extends Goal {
	private static final int AREA_CHECK_SIZE = 26;
	private PuffBugEntity puffbug;
	private Level world;

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
		return this.world.getEntitiesOfClass(Entity.class, new AABB(bud.getBlockPos()).expandTowards(0.0F, 3.0F, 0.0F)).isEmpty();
	}
}