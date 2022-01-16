package com.minecraftabnormals.endergetic.common.entities.puffbug.ai;

import com.minecraftabnormals.endergetic.common.entities.puffbug.PuffBugEntity;
import com.minecraftabnormals.endergetic.common.tileentities.BolloomBudTileEntity;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class PuffBugDescentGoal extends Goal {
	private PuffBugEntity puffbug;
	private World world;
	@Nullable
	private BlockPos budPos;
	private float originalPosX, originalPosZ;
	private int ticksPassed;

	public PuffBugDescentGoal(PuffBugEntity puffbug) {
		this.puffbug = puffbug;
		this.world = puffbug.level;
	}

	@Override
	public boolean canUse() {
		if (this.puffbug.getTarget() == null && !this.puffbug.hasLevitation() && this.isBolloomBudUnder() && this.puffbug.getBudPos() != null) {
			this.budPos = new BlockPos(this.puffbug.getX() - 0.5F, this.puffbug.getY() - 0.5F, this.puffbug.getZ() - 0.5F).below(2);
			return true;
		}
		return false;
	}

	@Override
	public boolean canContinueToUse() {
		TileEntity te = this.world.getBlockEntity(this.budPos);
		if (!(te instanceof BolloomBudTileEntity && ((BolloomBudTileEntity) te).canBeOpened())) {
			return false;
		}
		return this.puffbug.getTarget() == null && this.ticksPassed < 160 && !this.puffbug.hasLevitation() && !this.puffbug.isOnGround() && this.puffbug.getX() == this.originalPosX && this.puffbug.getZ() == this.originalPosZ;
	}

	@Override
	public void start() {
		TileEntity te = this.world.getBlockEntity(this.puffbug.getBudPos());
		if (te instanceof BolloomBudTileEntity) {
			((BolloomBudTileEntity) te).setTeleportingBug(null);
		}

		this.puffbug.setBudPos(null);
		this.puffbug.setBoosting(false);
		this.puffbug.setSpeed(0.0F);
		this.puffbug.getNavigation().stop();

		this.ticksPassed = 0;
		this.originalPosX = (float) this.puffbug.getX();
		this.originalPosZ = (float) this.puffbug.getZ();
	}

	@Override
	public void tick() {
		this.ticksPassed++;

		if (this.ticksPassed > 20) {
			this.puffbug.getRotationController().rotate(0.0F, 180.0F, 0.0F, 20);
			this.puffbug.puffCooldown = 25;
		}

		this.puffbug.setBoosting(false);
		this.puffbug.setSpeed(0.0F);
		this.puffbug.getNavigation().stop();
	}

	@Override
	public void stop() {
		TileEntity te = this.world.getBlockEntity(this.budPos);
		if ((te instanceof BolloomBudTileEntity && ((BolloomBudTileEntity) te).canBeOpened()) && this.puffbug.getX() == this.originalPosX && this.puffbug.getZ() == this.originalPosZ && this.puffbug.isOnGround()) {
			this.puffbug.setPollinationPos(this.budPos);
		}

		this.budPos = null;
		this.originalPosX = 0.0F;
		this.originalPosZ = 0.0F;
		this.puffbug.setBoosting(false);
	}

	private boolean isBolloomBudUnder() {
		BlockPos pos = new BlockPos(this.puffbug.getX() - 0.5F, this.puffbug.getY() - 0.5F, this.puffbug.getZ() - 0.5F);

		if (this.world.getBlockState(pos.below()).getCollisionShape(this.world, pos.below()).isEmpty() && this.world.getBlockState(pos.below(2)).getBlock() == EEBlocks.BOLLOOM_BUD.get()) {
			this.budPos = pos.below(2);
			return true;
		}

		return false;
	}
}