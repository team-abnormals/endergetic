package com.minecraftabnormals.endergetic.common.entities.booflo.ai;

import java.util.EnumSet;

import com.minecraftabnormals.abnormals_core.core.endimator.entity.EndimatedGoal;
import com.minecraftabnormals.endergetic.api.entity.util.RayTraceHelper;
import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity;

import net.minecraft.util.math.RayTraceResult.Type;

public class BoofloSwimmingGoal extends EndimatedGoal<BoofloEntity> {

	public BoofloSwimmingGoal(BoofloEntity booflo) {
		super(booflo, BoofloEntity.SWIM);
		this.setMutexFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
	}

	@Override
	public boolean shouldExecute() {
		return this.entity.hasPath() && this.isNoEndimationPlaying() && this.entity.isMovingInAir() && this.entity.isBoofed() && RayTraceHelper.rayTrace(this.entity, 1.5D, 1.0F).getType() != Type.BLOCK;
	}

	@Override
	public boolean shouldContinueExecuting() {
		return this.isEndimationPlaying() && this.entity.isBoofed();
	}

	@Override
	public void startExecuting() {
		this.playEndimation();
	}

}