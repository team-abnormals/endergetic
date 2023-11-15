package com.teamabnormals.endergetic.common.entity.eetle.ai.glider;

import com.teamabnormals.endergetic.common.entity.eetle.GliderEetle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;

public class GliderEetleTakeoffGoal extends Goal {
	private final GliderEetle glider;
	private int ticksPassed;

	public GliderEetleTakeoffGoal(GliderEetle glider) {
		this.glider = glider;
	}

	@Override
	public boolean canUse() {
		GliderEetle glider = this.glider;
		if (!glider.isGrounded() && !glider.isFlying() && glider.isNoEndimationPlaying()) {
			return glider.canFly() && glider.getRandom().nextFloat() < 0.05F || !glider.isOnGround() && willFallFar(glider);
		}
		return false;
	}

	@Override
	public boolean canContinueToUse() {
		return !this.glider.isGrounded() && this.ticksPassed < 5 && this.glider.canFly();
	}

	@Override
	public void start() {
		this.glider.setFlying(true);
	}

	@Override
	public void stop() {
		this.ticksPassed = 0;
	}

	@Override
	public void tick() {
		this.ticksPassed++;
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}

	private static boolean willFallFar(GliderEetle gliderEetleEntity) {
		Level world = gliderEetleEntity.level;
		BlockPos.MutableBlockPos mutable = gliderEetleEntity.blockPosition().mutable();
		int startY = mutable.getY();
		for (int i = 0; i < 8; i++) {
			mutable.setY(startY - i);
			if (world.loadedAndEntityCanStandOn(mutable, gliderEetleEntity)) {
				return false;
			}
		}
		return true;
	}
}
