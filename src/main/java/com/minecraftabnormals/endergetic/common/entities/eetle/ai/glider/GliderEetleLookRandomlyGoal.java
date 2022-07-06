package com.minecraftabnormals.endergetic.common.entities.eetle.ai.glider;

import com.minecraftabnormals.endergetic.common.entities.eetle.GliderEetleEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class GliderEetleLookRandomlyGoal extends Goal {
	private final GliderEetleEntity glider;
	private double lookX;
	private double lookZ;
	private int idleTime;

	public GliderEetleLookRandomlyGoal(GliderEetleEntity glider) {
		this.glider = glider;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	public boolean canUse() {
		return this.glider.getPassengers().isEmpty() && this.glider.getRandom().nextFloat() < 0.02F;
	}

	public boolean canContinueToUse() {
		return this.glider.getPassengers().isEmpty() && this.idleTime >= 0;
	}

	public void start() {
		double randomRot = (Math.PI * 2.0D) * this.glider.getRandom().nextDouble();
		this.lookX = Math.cos(randomRot);
		this.lookZ = Math.sin(randomRot);
		this.idleTime = 20 + this.glider.getRandom().nextInt(20);
	}

	public void tick() {
		--this.idleTime;
		this.glider.getLookControl().setLookAt(this.glider.getX() + this.lookX, this.glider.getEyeY(), this.glider.getZ() + this.lookZ);
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}
}
