package com.minecraftabnormals.endergetic.common.entities.puffbug.ai;

import javax.annotation.Nullable;

import com.minecraftabnormals.endergetic.common.entities.puffbug.PuffBugEntity;
import com.minecraftabnormals.endergetic.common.tileentities.BolloomBudTileEntity;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class PuffBugDescentGoal extends Goal {
	private final PuffBugEntity puffbug;
	private final Level world;
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
		BlockEntity te = this.world.getBlockEntity(this.budPos);
		if (!(te instanceof BolloomBudTileEntity && ((BolloomBudTileEntity) te).canBeOpened())) {
			return false;
		}
		return this.puffbug.getTarget() == null && this.ticksPassed < 160 && !this.puffbug.hasLevitation() && !this.puffbug.isOnGround() && this.puffbug.getX() == this.originalPosX && this.puffbug.getZ() == this.originalPosZ;
	}

	@Override
	public void start() {
		BlockEntity te = this.world.getBlockEntity(this.puffbug.getBudPos());
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
		BlockEntity te = this.world.getBlockEntity(this.budPos);
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

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}
}