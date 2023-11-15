package com.teamabnormals.endergetic.common.entity.eetle.ai.brood;

import com.teamabnormals.endergetic.common.entity.eetle.BroodEetle;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;

public class BroodEetleSleepGoal extends Goal {
	private final BroodEetle broodEetle;
	private float prevHealth;
	private int offGroundTicks;

	public BroodEetleSleepGoal(BroodEetle broodEetle) {
		this.broodEetle = broodEetle;
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.TARGET));
	}

	@Override
	public boolean canUse() {
		BroodEetle broodEetle = this.broodEetle;
		return broodEetle.isAlive() && broodEetle.isSleeping();
	}

	@Override
	public void start() {
		this.prevHealth = this.broodEetle.getHealth();
		this.offGroundTicks = 0;
	}

	@Override
	@SuppressWarnings("deprecation")
	public void tick() {
		BroodEetle broodEetle = this.broodEetle;
		if (!broodEetle.isOnGround() && broodEetle.level.hasChunkAt(broodEetle.blockPosition())) {
			this.offGroundTicks++;
		} else {
			this.offGroundTicks = 0;
		}
		boolean wokenUpAggressively = areAnyPlayersClose(broodEetle) || this.prevHealth > broodEetle.getHealth();
		if (wokenUpAggressively || this.offGroundTicks >= 10) {
			broodEetle.setSleeping(false);
			broodEetle.wokenUpAggressively = wokenUpAggressively;
		}
		this.prevHealth = broodEetle.getHealth();
	}

	@Override
	public boolean canContinueToUse() {
		BroodEetle broodEetle = this.broodEetle;
		return broodEetle.isAlive() && broodEetle.isSleeping();
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}

	private static boolean areAnyPlayersClose(BroodEetle broodEetle) {
		return !broodEetle.level.getEntitiesOfClass(Player.class, broodEetle.getBoundingBox().inflate(2.0F, 0.1F, 2.0F), player -> {
			return player.isAlive() && !player.isInvisible() && !player.isCreative();
		}).isEmpty();
	}
}
