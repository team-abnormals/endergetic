package com.minecraftabnormals.endergetic.common.entities.puffbug.ai;

import com.minecraftabnormals.endergetic.common.entities.puffbug.PuffBugEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.Direction;

import java.util.Random;

public class PuffBugRestOnHiveGoal extends Goal {
	private PuffBugEntity puffbug;
	private final Random random;
	private int ticksRested;

	public PuffBugRestOnHiveGoal(PuffBugEntity puffbug) {
		this.puffbug = puffbug;
		this.random = puffbug.getRandom();
	}

	@Override
	public boolean canUse() {
		return this.puffbug.getTarget() == null && !this.puffbug.isInflated() && this.puffbug.getAttachedHiveSide() != Direction.UP;
	}

	@Override
	public void tick() {
		this.ticksRested++;

		if (this.ticksRested > 1000 && this.random.nextFloat() < 0.25F) {
			this.puffbug.setAttachedHiveSide(Direction.UP);
			this.puffbug.setInflated(true);
		}

		if (this.puffbug.getAttachedHiveSide() != Direction.UP && this.puffbug.getAttachedHiveSide() != Direction.DOWN) {
			int ticks = this.puffbug.tickCount > 10 ? 20 : 5;
			this.puffbug.getRotationController().rotate(0.0F, -115.0F, 0.0F, ticks);
		}

		if (this.puffbug.level.getGameTime() % 60 == 0) {
			this.puffbug.heal(2.0F);
		}

		/*
		 * snore...
		 */
		if (this.random.nextInt(200) == 0) {
			float pitch = this.puffbug.isBaby() ? (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.5F : (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F;
			this.puffbug.playSound(this.puffbug.getSleepSound(), 0.1F, pitch);
		}
	}

	@Override
	public void stop() {
		this.ticksRested = 0;
	}
}