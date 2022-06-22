package com.minecraftabnormals.endergetic.common.entities.booflo.ai;

import java.util.EnumSet;

import com.minecraftabnormals.abnormals_core.core.endimator.entity.EndimatedGoal;
import com.minecraftabnormals.endergetic.api.entity.util.RayTraceHelper;
import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity;

import net.minecraft.util.math.RayTraceResult.Type;

import net.minecraft.entity.ai.goal.Goal.Flag;

public class BoofloSwimmingGoal extends EndimatedGoal<BoofloEntity> {

	public BoofloSwimmingGoal(BoofloEntity booflo) {
		super(booflo, BoofloEntity.SWIM);
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