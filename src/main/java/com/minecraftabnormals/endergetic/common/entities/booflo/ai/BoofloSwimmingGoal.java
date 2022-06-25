package com.minecraftabnormals.endergetic.common.entities.booflo.ai;

import java.util.EnumSet;

import com.minecraftabnormals.endergetic.api.entity.util.RayTraceHelper;
import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity;

import com.minecraftabnormals.endergetic.core.registry.other.EEPlayableEndimations;
import com.teamabnormals.blueprint.core.endimator.entity.EndimatedGoal;
import net.minecraft.world.phys.HitResult.Type;

public class BoofloSwimmingGoal extends EndimatedGoal<BoofloEntity> {

	public BoofloSwimmingGoal(BoofloEntity booflo) {
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