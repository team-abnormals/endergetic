package com.minecraftabnormals.endergetic.common.entities.booflo.ai;

import java.util.EnumSet;

import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.core.BlockPos;

import net.minecraft.world.entity.ai.goal.Goal.Flag;

public class BoofloSinkGoal extends Goal {
	private BoofloEntity booflo;

	public BoofloSinkGoal(BoofloEntity booflo) {
		this.booflo = booflo;
		this.setFlags(EnumSet.of(Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		return (this.booflo.hasCaughtFruit() || this.booflo.hasCaughtPuffBug()) && this.booflo.isBoofed() && !this.booflo.isOnGround() && this.booflo.getRandom().nextInt(70) == 0 && this.isSafePos();
	}

	@Override
	public boolean canContinueToUse() {
		if (!this.isSafePos()) {
			return false;
		}
		return !this.booflo.isOnGround() && this.booflo.isBoofed() && (this.booflo.hasCaughtFruit() || this.booflo.hasCaughtPuffBug());
	}

	@Override
	public void tick() {
		this.booflo.getNavigation().stop();
	}

	private boolean isSafePos() {
		BlockPos pos = this.booflo.blockPosition();
		for (int i = 0; i < 10; i++) {
			BlockPos newPos = pos.below(i);
			if (Block.canSupportRigidBlock(this.booflo.level, newPos)) {
				if (this.booflo.level.getBlockState(newPos).getFluidState().isEmpty() && !this.booflo.level.getBlockState(newPos).isBurning(this.booflo.level, newPos)) {
					return true;
				}
			}
		}
		return false;
	}
}