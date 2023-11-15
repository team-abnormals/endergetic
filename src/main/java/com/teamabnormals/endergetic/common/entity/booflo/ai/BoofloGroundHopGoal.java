package com.teamabnormals.endergetic.common.entity.booflo.ai;

import com.teamabnormals.blueprint.core.endimator.entity.EndimatedGoal;
import com.teamabnormals.endergetic.api.entity.util.RayTraceHelper;
import com.teamabnormals.endergetic.common.entity.booflo.Booflo;
import com.teamabnormals.endergetic.common.entity.booflo.Booflo.GroundMoveHelperController;
import com.teamabnormals.endergetic.core.registry.other.EEPlayableEndimations;
import net.minecraft.world.phys.HitResult.Type;

import java.util.EnumSet;

public class BoofloGroundHopGoal extends EndimatedGoal<Booflo> {
	private int ticksPassed;

	public BoofloGroundHopGoal(Booflo booflo) {
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