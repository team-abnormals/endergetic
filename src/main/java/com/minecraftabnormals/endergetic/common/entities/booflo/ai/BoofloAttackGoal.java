package com.minecraftabnormals.endergetic.common.entities.booflo.ai;

import java.util.EnumSet;
import java.util.Random;

import javax.annotation.Nullable;

import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity;
import com.minecraftabnormals.endergetic.common.entities.puffbug.PuffBugEntity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.core.BlockPos;

import net.minecraft.world.entity.ai.goal.Goal.Flag;

public class BoofloAttackGoal extends Goal {
	private final int UPPER_DISTANCE = 16;
	private final BoofloEntity booflo;
	private Path path;
	private BlockPos upperAirPos;
	private int delayCounter;

	public BoofloAttackGoal(BoofloEntity booflo) {
		this.booflo = booflo;
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	public boolean canUse() {
		Entity target = this.booflo.getBoofloAttackTarget();
		if (target == null) {
			return false;
		} else if (!target.isAlive()) {
			return false;
		} else if (!this.booflo.hasAggressiveAttackTarget()) {
			return false;
		} else if (!this.booflo.isBoofed()) {
			return false;
		} else {
			if (this.booflo.getBoofloAttackTarget() instanceof PuffBugEntity) {
				return false;
			}

			this.upperAirPos = this.getUpperPosToTarget(target, this.booflo.getRandom());
			if (this.upperAirPos == null) {
				Path newPath = this.booflo.getNavigation().createPath(target, 0);
				this.upperAirPos = newPath != null ? newPath.getTarget() : null;
				return upperAirPos != null;
			}

			this.path = this.booflo.getNavigation().createPath(this.upperAirPos, 0);

			if (this.path != null && this.booflo.hasAggressiveAttackTarget()) {
				return true;
			}
		}
		return false;
	}

	public boolean canContinueToUse() {
		Entity target = this.booflo.getBoofloAttackTarget();
		if (target == null) {
			return false;
		} else if (!target.isAlive()) {
			return false;
		} else if (!this.booflo.isBoofed()) {
			return false;
		} else if (this.booflo.blockPosition().distSqr(this.upperAirPos) > UPPER_DISTANCE) {
			return false;
		} else {
			return !(target instanceof Player) || !target.isSpectator() && !((Player) target).isCreative();
		}
	}

	public void start() {
		this.booflo.getNavigation().moveTo(this.path, 1.35F);
		this.booflo.setAggressive(true);
		this.delayCounter = 0;
	}

	public void stop() {
		Entity target = this.booflo.getBoofloAttackTarget();
		if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(target)) {
			this.booflo.setBoofloAttackTargetId(0);
		}
		this.booflo.setAggressive(false);
		this.booflo.getNavigation().stop();
	}

	public void tick() {
		this.delayCounter--;

		Entity target = this.booflo.getBoofloAttackTarget();

		if (target != null && this.upperAirPos != null) {
			this.booflo.getLookControl().setLookAt(this.upperAirPos.getX(), this.upperAirPos.getY(), this.upperAirPos.getZ(), 10.0F, 10.0F);
			double distToEnemySqr = this.booflo.distanceToSqr(target.getX(), target.getBoundingBox().minY, target.getZ());

			if (this.delayCounter <= 0 && !target.isInvisible()) {
				this.delayCounter = 4 + this.booflo.getRandom().nextInt(7);

				if (distToEnemySqr > 256.0D) {
					this.delayCounter += 5;
				}

				if (this.path != null && !this.booflo.getNavigation().moveTo(this.path.getTarget().getX(), this.path.getTarget().getY(), this.path.getTarget().getZ(), 1.35F)) {
					this.delayCounter += 5;
				}
			}
		}
	}

	@Nullable
	private BlockPos getUpperPosToTarget(Entity target, Random rand) {
		BlockPos startingPos = target.blockPosition();
		BlockPos targetPos = BlockPos.ZERO;
		boolean isOpenBelow = true;
		for (int y = 0; y < UPPER_DISTANCE; y++) {
			if (!target.level.getBlockState(startingPos.above(y)).getCollisionShape(target.level, startingPos.above(y)).isEmpty()) {
				isOpenBelow = false;
			}

			if (target.level.getBlockState(startingPos.above(y)).getCollisionShape(target.level, startingPos.above(y)).isEmpty() && y > 9) {
				targetPos = startingPos.above(y);
			}
		}
		return isOpenBelow ? targetPos : null;
	}
}