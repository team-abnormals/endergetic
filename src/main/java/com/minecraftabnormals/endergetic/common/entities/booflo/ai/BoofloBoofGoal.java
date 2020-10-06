package com.minecraftabnormals.endergetic.common.entities.booflo.ai;

import java.util.EnumSet;

import com.teamabnormals.abnormals_core.core.library.endimator.EndimatedGoal;
import com.teamabnormals.abnormals_core.core.library.endimator.Endimation;
import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity;

import net.minecraft.block.Block;
import net.minecraft.fluid.FluidState;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BoofloBoofGoal extends EndimatedGoal<BoofloEntity> {
	private final World world;

	public BoofloBoofGoal(BoofloEntity booflo) {
		super(booflo);
		this.world = booflo.world;
		this.setMutexFlags(EnumSet.of(Flag.MOVE));
	}

	@Override
	public boolean shouldExecute() {
		boolean onGround = this.entity.isOnGround();
		boolean flagChance = !this.entity.hasAggressiveAttackTarget() ? this.entity.getRNG().nextFloat() < 0.25F && this.isEndimationAtTick(20) : this.isEndimationPastOrAtTick(20);

		if (this.entity.hasAggressiveAttackTarget() && !this.entity.isBoofed()) {
			return this.entity.isNoEndimationPlaying();
		}

		if (!onGround) {
			if (this.shouldJumpForFall() && !this.entity.isBoofed() && this.entity.getRideControlDelay() <= 0) {
				if (this.entity.isBeingRidden()) {
					this.entity.setDelayExpanding(true);
					this.entity.setDelayDecrementing(false);
				}
				return true;
			}
		}
		return (this.entity.hasCaughtPuffBug() || this.entity.getPassengers().isEmpty()) && !onGround && !this.entity.isTempted() && flagChance && this.entity.isEndimationPlaying(BoofloEntity.HOP);
	}

	@Override
	public boolean shouldContinueExecuting() {
		return this.isEndimationPlaying();
	}

	@Override
	public void startExecuting() {
		this.entity.setBoofed(true);
		this.playEndimation();
	}

	private boolean shouldJumpForFall() {
		BlockPos pos = this.entity.getPosition();
		for (int i = 0; i < 12; i++) {
			pos = pos.down(i);
			FluidState fluidState = this.world.getFluidState(pos);
			if (!Block.hasSolidSide(this.world.getBlockState(pos), this.world, pos, Direction.UP) && i > 6 || fluidState.isTagged(FluidTags.LAVA)) {
				return true;
			} else if (Block.hasSolidSide(this.world.getBlockState(pos), this.world, pos, Direction.UP)) {
				return false;
			}
		}
		return false;
	}

	@Override
	protected Endimation getEndimation() {
		return BoofloEntity.INFLATE;
	}
}