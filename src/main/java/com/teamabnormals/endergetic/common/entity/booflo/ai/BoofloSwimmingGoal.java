package com.teamabnormals.endergetic.common.entity.booflo.ai;

import com.teamabnormals.blueprint.core.endimator.entity.EndimatedGoal;
import com.teamabnormals.endergetic.api.entity.util.RayTraceHelper;
import com.teamabnormals.endergetic.common.entity.booflo.Booflo;
import com.teamabnormals.endergetic.core.registry.other.EEPlayableEndimations;
import net.minecraft.world.phys.HitResult.Type;

import java.util.EnumSet;

public class BoofloSwimmingGoal extends EndimatedGoal<Booflo> {

	public BoofloSwimmingGoal(Booflo booflo) {
		super(booflo, EEPlayableEndimations.BOOFLO_SWIM);
		this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		return this.entity.isPathFinding() && this.isNoEndimationPlaying() && this.entity.isMovingInAir() && this.entity.isBoofed() && RayTraceHelper.rayTrace(this.entity, 1.5D, 1.0F).getType() != Type.BLOCK;
	}

	@Override
	public boolean canContinueToUse() {
		return this.isEndimationPlaying() && this.entity.isBoofed();
	}

	@Override
	public void start() {
		this.playEndimation();
	}

}