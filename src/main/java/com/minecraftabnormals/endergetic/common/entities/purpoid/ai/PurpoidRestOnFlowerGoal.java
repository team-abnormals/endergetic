package com.minecraftabnormals.endergetic.common.entities.purpoid.ai;

import com.minecraftabnormals.endergetic.common.entities.purpoid.PurpoidEntity;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

import java.util.EnumSet;

public class PurpoidRestOnFlowerGoal extends Goal {
	private final PurpoidEntity purpoid;
	private float startingHealth;
	private int duration;

	public PurpoidRestOnFlowerGoal(PurpoidEntity purpoid) {
		this.purpoid = purpoid;
		this.setMutexFlags(EnumSet.of(Flag.MOVE));
	}

	@Override
	public boolean shouldExecute() {
		PurpoidEntity purpoid = this.purpoid;
		if (!purpoid.getTeleportController().isTeleporting()) {
			return isNearFlower(purpoid);
		}
		return false;
	}

	@Override
	public void startExecuting() {
		PurpoidEntity purpoid = this.purpoid;
		this.startingHealth = purpoid.getHealth();
		this.duration = purpoid.getRNG().nextInt(1501) + 300;
	}

	@Override
	public void tick() {
		PurpoidEntity purpoid = this.purpoid;
		purpoid.setMotion(purpoid.getMotion().subtract(0.0F, 0.02F, 0.0F));
	}

	@Override
	public boolean shouldContinueExecuting() {
		PurpoidEntity purpoid = this.purpoid;
		return isNearFlower(purpoid) && purpoid.getHealth() >= this.startingHealth && --this.duration >= 0;
	}

	@Override
	public void resetTask() {
		PurpoidEntity purpoid = this.purpoid;
		purpoid.setFlowerPos(null);
		purpoid.resetRestCooldown();
	}

	private static boolean isNearFlower(PurpoidEntity purpoid) {
		BlockPos flowerPos = purpoid.getFlowerPos();
		return flowerPos != null && purpoid.world.getBlockState(flowerPos).getBlock() == Blocks.CHORUS_FLOWER && Vector3d.copyCenteredWithVerticalOffset(flowerPos, 1.0F).squareDistanceTo(purpoid.getPositionVec()) <= 0.02F;
	}
}
