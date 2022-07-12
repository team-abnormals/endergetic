package com.teamabnormals.endergetic.common.entities.purpoid.ai;

import com.teamabnormals.endergetic.common.entities.purpoid.PurpoidEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class PurpoidRestOnFlowerGoal extends Goal {
	private final PurpoidEntity purpoid;
	private float startingHealth;
	private int duration;

	public PurpoidRestOnFlowerGoal(PurpoidEntity purpoid) {
		this.purpoid = purpoid;
		this.setFlags(EnumSet.of(Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		PurpoidEntity purpoid = this.purpoid;
		if (!purpoid.getTeleportController().isTeleporting()) {
			return isNearFlower(purpoid);
		}
		return false;
	}

	@Override
	public void start() {
		PurpoidEntity purpoid = this.purpoid;
		this.startingHealth = purpoid.getHealth();
		this.duration = purpoid.getRandom().nextInt(1501) + 300;
	}

	@Override
	public void tick() {
		PurpoidEntity purpoid = this.purpoid;
		purpoid.setDeltaMovement(purpoid.getDeltaMovement().subtract(0.0F, 0.02F, 0.0F));
	}

	@Override
	public boolean canContinueToUse() {
		PurpoidEntity purpoid = this.purpoid;
		return isNearFlower(purpoid) && purpoid.getHealth() >= this.startingHealth && --this.duration >= 0;
	}

	@Override
	public void stop() {
		PurpoidEntity purpoid = this.purpoid;
		purpoid.setFlowerPos(null);
		purpoid.resetRestCooldown();
	}

	private static boolean isNearFlower(PurpoidEntity purpoid) {
		BlockPos flowerPos = purpoid.getFlowerPos();
		return flowerPos != null && purpoid.level.getBlockState(flowerPos).getBlock() == Blocks.CHORUS_FLOWER && Vec3.upFromBottomCenterOf(flowerPos, 1.0F).distanceToSqr(purpoid.position()) <= 0.02F;
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}
}
