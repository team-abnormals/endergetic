package com.teamabnormals.endergetic.common.entity.purpoid.ai;

import com.teamabnormals.endergetic.common.entity.purpoid.Purpoid;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class PurpoidRestGoal extends Goal {
	private final Purpoid purpoid;
	private float startingHealth;
	private int duration;

	public PurpoidRestGoal(Purpoid purpoid) {
		this.purpoid = purpoid;
		this.setFlags(EnumSet.of(Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		Purpoid purpoid = this.purpoid;
		if (purpoid.isResting() && !purpoid.getTeleportController().isTeleporting()) {
			if (isAlignedAndNotBlocked(purpoid)) return true;
			purpoid.setRestingPos(null);
			purpoid.setRestingSide(Direction.DOWN);
		}
		return false;
	}

	@Override
	public void start() {
		Purpoid purpoid = this.purpoid;
		purpoid.setSpeed(0.0F);
		purpoid.setDeltaMovement(Vec3.ZERO);
		purpoid.getNavigation().stop();
		this.startingHealth = purpoid.getHealth();
		this.duration = purpoid.getRandom().nextInt(1501) + 300;
	}

	@Override
	public void tick() {
		Purpoid purpoid = this.purpoid;
		purpoid.setSpeed(0.0F);
		purpoid.getNavigation().stop();
		Direction side = purpoid.getRestingSide();
		if (purpoid.getRestOntoProgress() < 0.95F) {
			purpoid.setDeltaMovement(purpoid.getDeltaMovement().subtract(side.getStepX() * 0.01F, side.getStepY() * 0.01F, side.getStepZ() * 0.01F));
		} else {
			purpoid.setDeltaMovement(purpoid.getDeltaMovement().multiply(0.5F - 0.5F * Mth.abs(side.getStepX()), 0.5F - 0.5F * Mth.abs(side.getStepY()), 0.5F - 0.5F * Mth.abs(side.getStepZ())));
		}
	}

	@Override
	public boolean canContinueToUse() {
		Purpoid purpoid = this.purpoid;
		return purpoid.getHealth() >= this.startingHealth && --this.duration >= 0 && isAlignedAndNotBlocked(purpoid);
	}

	@Override
	public void stop() {
		Purpoid purpoid = this.purpoid;
		purpoid.setRestingPos(null);
		purpoid.setRestingSide(Direction.DOWN);
		purpoid.resetRestCooldown();
	}

	private static boolean isAlignedAndNotBlocked(Purpoid purpoid) {
		BlockPos restingPos = purpoid.getRestingPos();
		if (restingPos != null) {
			Direction side = purpoid.getRestingSide();
			if (purpoid.level.getBlockState(restingPos).getBlock() != (purpoid.isBaby() ? Blocks.CHORUS_PLANT : Blocks.CHORUS_FLOWER) || !purpoid.level.getBlockState(restingPos.relative(side)).isAir()) return false;
			Direction.Axis axis = side.getAxis();
			Vec3 position = purpoid.position();
			double purpoidX = position.x;
			double purpoidY = position.y;
			double purpoidZ = position.z;
			double mainAxisPurpoidCoordinate = axis.choose(purpoidX, purpoidY, purpoidZ);
			Vec3 centeredRestingPos = purpoid.isBaby() ? new Vec3(restingPos.getX() + 0.5F, restingPos.getY() + 0.25F, restingPos.getZ() + 0.5F) : Vec3.atBottomCenterOf(restingPos);
			double centeredRestingPosX = centeredRestingPos.x();
			double centeredRestingPosY = centeredRestingPos.y();
			double centeredRestingPosZ = centeredRestingPos.z();
			double mainAxisRestingPosCoordinate = axis.choose(centeredRestingPosX, centeredRestingPosY, centeredRestingPosZ);
			double sideDisplacementFromRestingPos = mainAxisPurpoidCoordinate - mainAxisRestingPosCoordinate;
			//Checks if the Purpoid is in the proper displacement range on the axis that it's traveling on to arrive at its rest pos
			if (side.getAxisDirection() == Direction.AxisDirection.POSITIVE ? sideDisplacementFromRestingPos >= -0.0625D && sideDisplacementFromRestingPos <= 3.0625D : sideDisplacementFromRestingPos <= 0.0625D && sideDisplacementFromRestingPos >= -3.0625D) {
				//We now measure how far away the Purpoid is on the other two axes from its rest pos to make sure the Purpoid is aligned enough
				int mainAxisIndex = axis.ordinal();
				Direction.Axis nextAxis = Direction.Axis.VALUES[(mainAxisIndex + 1) % 3];
				Direction.Axis precedingAxis = Direction.Axis.VALUES[(mainAxisIndex + 2) % 3];
				double nextAxisPurpoidCoordinate = nextAxis.choose(purpoidX, purpoidY, purpoidZ);
				double precedingAxisPurpoidCoordinate = precedingAxis.choose(purpoidX, purpoidY, purpoidZ);
				double nextAxisRestingPosCoordinate = nextAxis.choose(centeredRestingPosX, centeredRestingPosY, centeredRestingPosZ);
				double precedingAxisRestingPosCoordinate = precedingAxis.choose(centeredRestingPosX, centeredRestingPosY, centeredRestingPosZ);
				double otherAxesDistanceFromRestingPosSqr = Mth.square(nextAxisPurpoidCoordinate - nextAxisRestingPosCoordinate) + Mth.square(precedingAxisPurpoidCoordinate - precedingAxisRestingPosCoordinate);
				//Additionally, we check if there are no colliders in the path of the Purpoid
				return otherAxesDistanceFromRestingPosSqr <= 0.0625F && (purpoid.getRestOntoProgress() >= 0.6F || PurpoidTeleportToFlowerGoal.isRestingPosNotBlocked(purpoid, position, restingPos, side));
			}
		}
		purpoid.setRestingPos(null);
		purpoid.setRestingSide(Direction.DOWN);
		return false;
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}
}
