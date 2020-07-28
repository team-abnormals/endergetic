package com.minecraftabnormals.endergetic.common.entities.booflo.ai;

import java.util.EnumSet;

import com.minecraftabnormals.abnormals_core.core.utils.NetworkUtil;
import com.minecraftabnormals.endergetic.api.entity.util.RayTraceHelper;
import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity;
import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity.GroundMoveHelperController;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.RayTraceResult.Type;

public class BoofloGroundHopGoal extends Goal {
	private final BoofloEntity booflo;
	private int ticksPassed;

	public BoofloGroundHopGoal(BoofloEntity booflo) {
		this.booflo = booflo;
		this.setMutexFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
	}

	@Override
	public boolean shouldExecute() {
		if(RayTraceHelper.rayTrace(this.booflo, 1.5D, 1.0F).getType() == Type.BLOCK) {
			return false;
		}
		return this.booflo.getMoveHelper() instanceof GroundMoveHelperController && this.booflo.isOnGround() && !this.booflo.isBoofed() && this.booflo.hopDelay == 0 && this.booflo.isNoEndimationPlaying() && !this.booflo.isPassenger() && this.booflo.getPassengers().isEmpty();
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		return this.booflo.getMoveHelper() instanceof GroundMoveHelperController && this.ticksPassed <= 10;
	}
	
	@Override
	public void startExecuting() {
		NetworkUtil.setPlayingAnimationMessage(this.booflo, BoofloEntity.HOP);
	}
	
	@Override
	public void resetTask() {
		this.ticksPassed = 0;
	}

	@Override
	public void tick() {
		this.ticksPassed++;
		
		if(this.booflo.getMoveHelper() instanceof GroundMoveHelperController) {
			((GroundMoveHelperController) this.booflo.getMoveHelper()).setSpeed(1.25D);
		}
	}
}