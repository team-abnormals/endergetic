package com.minecraftabnormals.endergetic.common.entities.booflo.ai;

import java.util.EnumSet;

import com.minecraftabnormals.abnormals_core.core.endimator.entity.EndimatedGoal;
import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.tags.FluidTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import net.minecraft.world.entity.ai.goal.Goal.Flag;

public class BoofloBoofGoal extends EndimatedGoal<BoofloEntity> {
	private final Level world;

	public BoofloBoofGoal(BoofloEntity booflo) {
		super(booflo, BoofloEntity.INFLATE);
		this.world = booflo.level;
		this.setFlags(EnumSet.of(Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		boolean onGround = this.entity.isOnGround();
		boolean flagChance = !this.entity.hasAggressiveAttackTarget() ? this.entity.getRandom().nextFloat() < 0.25F && this.isEndimationAtTick(20) : this.isEndimationPastOrAtTick(20);

		if (this.entity.hasAggressiveAttackTarget() && !this.entity.isBoofed()) {
			return this.entity.isNoEndimationPlaying();
		}

		if (!onGround) {
			if (this.shouldJumpForFall() && !this.entity.isBoofed() && this.entity.getBoostPower() <= 0) {
				if (this.entity.isVehicle()) {
					this.entity.setBoostExpanding(true);
					this.entity.setBoostLocked(true);
				}
				return true;
			}
		}
		return (this.entity.hasCaughtPuffBug() || this.entity.getPassengers().isEmpty()) && !onGround && !this.entity.isTempted() && flagChance && this.entity.isEndimationPlaying(BoofloEntity.HOP);
	}

	@Override
	public boolean canContinueToUse() {
		return this.isEndimationPlaying();
	}

	@Override
	public void start() {
		this.entity.setBoofed(true);
		this.playEndimation();
	}

	private boolean shouldJumpForFall() {
		BlockPos pos = this.entity.blockPosition();
		for (int i = 0; i < 12; i++) {
			pos = pos.below(i);
			FluidState fluidState = this.world.getFluidState(pos);
			boolean hasSolidTop = Block.canSupportRigidBlock(this.world, pos);
			if (!hasSolidTop && i > 6 || fluidState.is(FluidTags.LAVA)) {
				return true;
			} else if (hasSolidTop) {
				return false;
			}
		}
		return false;
	}
}