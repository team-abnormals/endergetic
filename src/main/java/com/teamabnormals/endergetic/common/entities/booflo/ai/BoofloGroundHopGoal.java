package com.teamabnormals.endergetic.common.entities.booflo.ai;

import java.util.EnumSet;

import com.teamabnormals.endergetic.api.entity.util.RayTraceHelper;
import com.teamabnormals.endergetic.common.entities.booflo.BoofloEntity;
import com.teamabnormals.endergetic.common.entities.booflo.BoofloEntity.GroundMoveHelperController;

import com.teamabnormals.endergetic.core.registry.other.EEPlayableEndimations;
import com.teamabnormals.blueprint.core.endimator.entity.EndimatedGoal;
import net.minecraft.world.phys.HitResult.Type;

public class BoofloGroundHopGoal extends EndimatedGoal<BoofloEntity> {
	private int ticksPassed;

	public BoofloGroundHopGoal(BoofloEntity booflo) {
		super(booflo, EEPlayableEndimations.BOOFLO_HOP);
		this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		if (RayTraceHelper.rayTrace(this.entity, 1.5D, 1.0F).getType() == Type.BLOCK) {
			return false;
		}
		return this.entity.getMoveControl() instanceof GroundMoveHelperController && this.entity.isOnGround() && !this.entity.isBoofed() && this.entity.hopDelay == 0 && this.entity.isNoEndimationPlaying() && !this.entity.isPassenger() && this.entity.getPassengers().isEmpty();
	}

	@Override
	public boolean canContinueToUse() {
		return this.entity.getMoveControl() instanceof GroundMoveHelperController && this.ticksPassed <= 10;
	}

	@Override
	public void start() {
		this.playEndimation();
	}

	@Override
	public void stop() {
		this.ticksPassed = 0;
	}

	@Override
	public void tick() {
		this.ticksPassed++;

		if (this.entity.getMoveControl() instanceof GroundMoveHelperController) {
			((GroundMoveHelperController) this.entity.getMoveControl()).setSpeed(1.25D);
		}
	}
	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}
}