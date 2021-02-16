package com.minecraftabnormals.endergetic.common.entities.eetle.ai.glider;

import com.minecraftabnormals.endergetic.common.entities.eetle.GliderEetleEntity;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;

public class GliderEetleLookRandomlyGoal extends Goal {
	private final GliderEetleEntity glider;
	private double lookX;
	private double lookZ;
	private int idleTime;

	public GliderEetleLookRandomlyGoal(GliderEetleEntity glider) {
		this.glider = glider;
		this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	public boolean shouldExecute() {
		return this.glider.getPassengers().isEmpty() && this.glider.getRNG().nextFloat() < 0.02F;
	}

	public boolean shouldContinueExecuting() {
		return this.glider.getPassengers().isEmpty() && this.idleTime >= 0;
	}

	public void startExecuting() {
		double randomRot = (Math.PI * 2.0D) * this.glider.getRNG().nextDouble();
		this.lookX = Math.cos(randomRot);
		this.lookZ = Math.sin(randomRot);
		this.idleTime = 20 + this.glider.getRNG().nextInt(20);
	}

	public void tick() {
		--this.idleTime;
		this.glider.getLookController().setLookPosition(this.glider.getPosX() + this.lookX, this.glider.getPosYEye(), this.glider.getPosZ() + this.lookZ);
	}
}
