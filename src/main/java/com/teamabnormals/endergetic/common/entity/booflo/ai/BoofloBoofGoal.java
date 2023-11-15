package com.teamabnormals.endergetic.common.entity.booflo.ai;

import com.teamabnormals.blueprint.core.endimator.entity.EndimatedGoal;
import com.teamabnormals.endergetic.common.entity.booflo.Booflo;
import com.teamabnormals.endergetic.core.registry.other.EEPlayableEndimations;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.FluidState;

import java.util.EnumSet;

public class BoofloBoofGoal extends EndimatedGoal<Booflo> {
	private final Level world;

	public BoofloBoofGoal(Booflo booflo) {
		super(booflo, EEPlayableEndimations.BOOFLO_INFLATE);
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
		return (this.entity.hasCaughtPuffBug() || this.entity.getPassengers().isEmpty()) && !onGround && !this.entity.isTempted() && flagChance && this.entity.isEndimationPlaying(EEPlayableEndimations.BOOFLO_HOP);
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