package com.minecraftabnormals.endergetic.common.entities.eetle.ai.brood;

import com.minecraftabnormals.endergetic.common.entities.eetle.BroodEetleEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;

import java.util.EnumSet;

public class BroodEetleSleepGoal extends Goal {
	private final BroodEetleEntity broodEetle;
	private float prevHealth;
	private int offGroundTicks;

	public BroodEetleSleepGoal(BroodEetleEntity broodEetle) {
		this.broodEetle = broodEetle;
		this.setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.TARGET));
	}

	@Override
	public boolean shouldExecute() {
		BroodEetleEntity broodEetle = this.broodEetle;
		return broodEetle.isAlive() && broodEetle.isSleeping();
	}

	@Override
	public void startExecuting() {
		this.prevHealth = this.broodEetle.getHealth();
		this.offGroundTicks = 0;
	}

	@Override
	@SuppressWarnings("deprecation")
	public void tick() {
		BroodEetleEntity broodEetle = this.broodEetle;
		if (!broodEetle.isOnGround() && broodEetle.world.isBlockLoaded(broodEetle.getPosition())) {
			this.offGroundTicks++;
		} else {
			this.offGroundTicks = 0;
		}
		boolean wokenUpByPlayer = areAnyPlayersClose(broodEetle);
		if (wokenUpByPlayer || this.prevHealth > broodEetle.getHealth() || this.offGroundTicks >= 10) {
			broodEetle.setSleeping(false);
			broodEetle.wokenUpByPlayer = wokenUpByPlayer;
		}
		this.prevHealth = broodEetle.getHealth();
	}

	@Override
	public boolean shouldContinueExecuting() {
		BroodEetleEntity broodEetle = this.broodEetle;
		return broodEetle.isAlive() && broodEetle.isSleeping();
	}

	private static boolean areAnyPlayersClose(BroodEetleEntity broodEetle) {
		return !broodEetle.world.getEntitiesWithinAABB(PlayerEntity.class, broodEetle.getBoundingBox().grow(2.0F, 0.1F, 2.0F), player -> {
			return player.isAlive() && !player.isInvisible() && !player.isCreative();
		}).isEmpty();
	}
}
