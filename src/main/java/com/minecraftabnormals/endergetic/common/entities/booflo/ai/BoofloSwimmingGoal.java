package com.minecraftabnormals.endergetic.common.entities.booflo.ai;

import java.util.EnumSet;

import com.minecraftabnormals.abnormals_core.core.utils.NetworkUtil;
import com.minecraftabnormals.endergetic.api.entity.util.RayTraceHelper;
import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.RayTraceResult.Type;

public class BoofloSwimmingGoal extends Goal {
	private BoofloEntity booflo;
	
	public BoofloSwimmingGoal(BoofloEntity booflo) {
		this.booflo = booflo;
		this.setMutexFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
	}

	@Override
	public boolean shouldExecute() {
		if(RayTraceHelper.rayTrace(this.booflo, 1.5D, 1.0F).getType() == Type.BLOCK) {
			return false;
		}
		return this.booflo.hasPath() && this.booflo.isNoEndimationPlaying() && this.booflo.isMovingInAir() && this.booflo.isBoofed();
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		return this.booflo.isEndimationPlaying(BoofloEntity.SWIM) && this.booflo.isBoofed();
	}
	
	@Override
	public void startExecuting() {
		NetworkUtil.setPlayingAnimationMessage(this.booflo, BoofloEntity.SWIM);
	}
}