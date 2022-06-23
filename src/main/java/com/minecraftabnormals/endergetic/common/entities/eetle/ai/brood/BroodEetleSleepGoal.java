package com.minecraftabnormals.endergetic.common.entities.eetle.ai.brood;

import com.minecraftabnormals.endergetic.common.entities.eetle.BroodEetleEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;

import net.minecraft.world.entity.ai.goal.Goal.Flag;

public class BroodEetleSleepGoal extends Goal {
	private final BroodEetleEntity broodEetle;
	private float prevHealth;
	private int offGroundTicks;

	public BroodEetleSleepGoal(BroodEetleEntity broodEetle) {
		this.broodEetle = broodEetle;
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.TARGET));
	}

	@Override
	public boolean canUse() {
		BroodEetleEntity broodEetle = this.broodEetle;
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
		BroodEetleEntity broodEetle = this.broodEetle;
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
		BroodEetleEntity broodEetle = this.broodEetle;
		return broodEetle.isAlive() && broodEetle.isSleeping();
	}

	private static boolean areAnyPlayersClose(BroodEetleEntity broodEetle) {
		return !broodEetle.level.getEntitiesOfClass(Player.class, broodEetle.getBoundingBox().inflate(2.0F, 0.1F, 2.0F), player -> {
			return player.isAlive() && !player.isInvisible() && !player.isCreative();
		}).isEmpty();
	}
}
