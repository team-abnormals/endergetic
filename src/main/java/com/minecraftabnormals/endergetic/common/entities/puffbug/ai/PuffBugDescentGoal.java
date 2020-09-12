package com.minecraftabnormals.endergetic.common.entities.puffbug.ai;

import javax.annotation.Nullable;

import com.minecraftabnormals.endergetic.common.entities.puffbug.PuffBugEntity;
import com.minecraftabnormals.endergetic.common.tileentities.BolloomBudTileEntity;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PuffBugDescentGoal extends Goal {
	private PuffBugEntity puffbug;
	private World world;
	@Nullable
	private BlockPos budPos;
	private float originalPosX, originalPosZ;
	private int ticksPassed;
	
	public PuffBugDescentGoal(PuffBugEntity puffbug) {
		this.puffbug = puffbug;
		this.world = puffbug.world;
	}
	
	@Override
	public boolean shouldExecute() {
		if (this.puffbug.getAttackTarget() == null && !this.puffbug.hasLevitation() && this.isBolloomBudUnder() && this.puffbug.getBudPos() != null) {
			this.budPos = new BlockPos(this.puffbug.getPosX() - 0.5F, this.puffbug.getPosY() - 0.5F, this.puffbug.getPosZ() - 0.5F).down(2);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		TileEntity te = this.world.getTileEntity(this.budPos);
		if (!(te instanceof BolloomBudTileEntity && ((BolloomBudTileEntity) te).canBeOpened())) {
			return false;
		}
		return this.puffbug.getAttackTarget() == null && this.ticksPassed < 160 && !this.puffbug.hasLevitation() && !this.puffbug.isOnGround() && this.puffbug.getPosX() == this.originalPosX && this.puffbug.getPosZ() == this.originalPosZ;
	}
	
	@Override
	public void startExecuting() {
		TileEntity te = this.world.getTileEntity(this.puffbug.getBudPos());
		if (te instanceof BolloomBudTileEntity) {
			((BolloomBudTileEntity) te).setTeleportingBug(null);
		}
		
		this.puffbug.setBudPos(null);
		this.puffbug.setBoosting(false);
		this.puffbug.setAIMoveSpeed(0.0F);
		this.puffbug.getNavigator().clearPath();
		
		this.ticksPassed = 0;
		this.originalPosX = (float) this.puffbug.getPosX();
		this.originalPosZ = (float) this.puffbug.getPosZ();
	}

	@Override
	public void tick() {
		this.ticksPassed++;
		
		if (this.ticksPassed > 20) {
			this.puffbug.getRotationController().rotate(0.0F, 180.0F, 0.0F, 20);
			this.puffbug.puffCooldown = 25;
		}
		
		this.puffbug.setBoosting(false);
		this.puffbug.setAIMoveSpeed(0.0F);
		this.puffbug.getNavigator().clearPath();
	}
	
	@Override
	public void resetTask() {
		TileEntity te = this.world.getTileEntity(this.budPos);
		if ((te instanceof BolloomBudTileEntity && ((BolloomBudTileEntity) te).canBeOpened()) && this.puffbug.getPosX() == this.originalPosX && this.puffbug.getPosZ() == this.originalPosZ && this.puffbug.isOnGround()) {
			this.puffbug.setPollinationPos(this.budPos);
		}
		
		this.budPos = null;
		this.originalPosX = 0.0F;
		this.originalPosZ = 0.0F;
		this.puffbug.setBoosting(false);
	}
	
	private boolean isBolloomBudUnder() {
		BlockPos pos = new BlockPos(this.puffbug.getPosX() - 0.5F, this.puffbug.getPosY() - 0.5F, this.puffbug.getPosZ() - 0.5F);
		
		if (this.world.getBlockState(pos.down()).getCollisionShape(this.world, pos.down()).isEmpty() && this.world.getBlockState(pos.down(2)).getBlock() == EEBlocks.BOLLOOM_BUD.get()) {
			this.budPos = pos.down(2);
			return true;
		}
		
		return false;
	}
}