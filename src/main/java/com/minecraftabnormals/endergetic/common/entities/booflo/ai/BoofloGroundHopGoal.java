package com.minecraftabnormals.endergetic.common.entities.booflo.ai;

import java.util.EnumSet;

import com.minecraftabnormals.abnormals_core.core.endimator.entity.EndimatedGoal;
import com.minecraftabnormals.endergetic.api.entity.util.RayTraceHelper;
import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity;
import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity.GroundMoveHelperController;

import net.minecraft.util.math.RayTraceResult.Type;

public class BoofloGroundHopGoal extends EndimatedGoal<BoofloEntity> {
	private int ticksPassed;

	public BoofloGroundHopGoal(BoofloEntity booflo) {
		super(booflo, BoofloEntity.HOP);
		this.setMutexFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
	}

	@Override
	public boolean shouldExecute() {
		if (RayTraceHelper.rayTrace(this.entity, 1.5D, 1.0F).getType() == Type.BLOCK) {
			return false;
		}
		return this.entity.getMoveHelper() instanceof GroundMoveHelperController && this.entity.isOnGround() && !this.entity.isBoofed() && this.entity.hopDelay == 0 && this.entity.isNoEndimationPlaying() && !this.entity.isPassenger() && this.entity.getPassengers().isEmpty();
	}

	@Override
	public boolean shouldContinueExecuting() {
		return this.entity.getMoveHelper() instanceof GroundMoveHelperController && this.ticksPassed <= 10;
	}

	@Override
	public void startExecuting() {
		this.playEndimation();
	}

	@Override
	public void resetTask() {
		this.ticksPassed = 0;
	}

	@Override
	public void tick() {
		this.ticksPassed++;

		if (this.entity.getMoveHelper() instanceof GroundMoveHelperController) {
			((GroundMoveHelperController) this.entity.getMoveHelper()).setSpeed(1.25D);
		}
	}
}