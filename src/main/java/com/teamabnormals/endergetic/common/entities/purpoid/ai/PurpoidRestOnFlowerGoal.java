package com.teamabnormals.endergetic.common.entities.purpoid.ai;

import com.teamabnormals.endergetic.common.entities.purpoid.PurpoidEntity;
import net.minecraft.util.Mth;
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
			return isAlignedWithFlowerAndFlowerIsNotBlocked(purpoid);
		}
		return false;
	}

	@Override
	public void start() {
		PurpoidEntity purpoid = this.purpoid;
		purpoid.setSpeed(0.0F);
		purpoid.getNavigation().stop();
		this.startingHealth = purpoid.getHealth();
		this.duration = purpoid.getRandom().nextInt(1501) + 300;
	}

	@Override
	public void tick() {
		PurpoidEntity purpoid = this.purpoid;
		BlockPos flowerPos = purpoid.getRestingPos();
		if (flowerPos == null) return;
		if (purpoid.getRestOntoFlowerProgress() < 0.95F) {
			purpoid.setDeltaMovement(purpoid.getDeltaMovement().subtract(0.0F, 0.01F, 0.0F));
		} else {
			purpoid.setDeltaMovement(purpoid.getDeltaMovement().multiply(0.5F, 0.0F, 0.5F));
		}
	}

	@Override
	public boolean canContinueToUse() {
		PurpoidEntity purpoid = this.purpoid;
		return isAlignedWithFlowerAndFlowerIsNotBlocked(purpoid) && purpoid.getHealth() >= this.startingHealth && --this.duration >= 0;
	}

	@Override
	public void stop() {
		PurpoidEntity purpoid = this.purpoid;
		purpoid.setRestingPos(null);
		purpoid.resetRestCooldown();
	}

	private static boolean isAlignedWithFlowerAndFlowerIsNotBlocked(PurpoidEntity purpoid) {
		BlockPos flowerPos = purpoid.getRestingPos();
		if (flowerPos != null) {
			if (purpoid.level.getBlockState(flowerPos).getBlock() != Blocks.CHORUS_FLOWER || !purpoid.level.getBlockState(flowerPos.above()).isAir()) {
				purpoid.setRestingPos(null);
				return false;
			}
			double purpoidY = purpoid.getY();
			int flowerPosY = flowerPos.getY();
			double verticalDisplacementFromFlower = purpoidY - flowerPosY;
			if (verticalDisplacementFromFlower >= -0.0625D && verticalDisplacementFromFlower <= 3.0625D) {
				Vec3 position = purpoid.position();
				int flowerPosX = flowerPos.getX();
				int flowerPosZ = flowerPos.getZ();
				double horizontalDistanceFromFlowerSqr = Mth.square(position.x - flowerPosX - 0.5D) + Mth.square(position.z - flowerPosZ - 0.5D);
				return horizontalDistanceFromFlowerSqr <= 0.00390625D && PurpoidTeleportToFlowerGoal.isRangeNotBlockedByEntities(purpoid, flowerPosX, flowerPosY, flowerPosZ, (float) purpoidY);
			}
		}
		return false;
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}
}
