package com.minecraftabnormals.endergetic.common.entities.eetle.ai.glider;

import com.minecraftabnormals.abnormals_core.common.world.storage.tracking.IDataManager;
import com.minecraftabnormals.endergetic.common.entities.eetle.GliderEetleEntity;
import com.minecraftabnormals.endergetic.core.registry.other.EEDataProcessors;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.EnumSet;

public class GliderEetleGrabGoal extends Goal {
	private final GliderEetleEntity glider;
	private Path path;
	private int delayCounter;

	public GliderEetleGrabGoal(GliderEetleEntity glider) {
		this.glider = glider;
		this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	@Override
	public boolean shouldExecute() {
		if (this.glider.isGrounded()) return false;
		LivingEntity target = this.glider.getAttackTarget();
		if (target == null || !target.isAlive() || GliderEetleEntity.isEntityLarge(target) || target.getRidingEntity() instanceof GliderEetleEntity || !this.glider.getPassengers().isEmpty()) {
			return false;
		}
		this.path = this.glider.getNavigator().getPathToEntity(target, 0);
		return this.path != null;
	}

	@Override
	public void startExecuting() {
		GliderEetleEntity glider = this.glider;
		if (!glider.isFlying()) {
			glider.setFlying(true);
		}
		glider.getNavigator().setPath(this.path, 1.375F);
		glider.setAggroed(true);
		this.delayCounter = 0;
	}

	@Override
	public boolean shouldContinueExecuting() {
		if (this.glider.isGrounded()) return false;
		LivingEntity target = this.glider.getAttackTarget();
		if (target == null || !target.isAlive() || GliderEetleEntity.isEntityLarge(target) || target.getRidingEntity() instanceof GliderEetleEntity || !this.glider.getPassengers().isEmpty()) {
			return false;
		}
		return !this.glider.getNavigator().noPath();
	}

	@Override
	public void tick() {
		this.delayCounter = Math.max(this.delayCounter - 1, 0);
		GliderEetleEntity glider = this.glider;
		LivingEntity target = glider.getAttackTarget();
		glider.getLookController().setLookPositionWithEntity(target, 30.0F, 30.0F);
		double distanceToTargetSq = glider.getDistanceSq(target);
		boolean canSeeTarget = glider.getEntitySenses().canSee(target);
		if (canSeeTarget && this.delayCounter <= 0 && glider.getRNG().nextFloat() < 0.05F) {
			this.delayCounter = 4 + glider.getRNG().nextInt(9);
			PathNavigator pathNavigator = glider.getNavigator();
			if (distanceToTargetSq > 1024.0D) {
				this.delayCounter += 10;
			} else if (distanceToTargetSq > 256.0D) {
				this.delayCounter += 5;
			}

			float blocksFromTarget = MathHelper.sqrt(distanceToTargetSq);
			if (blocksFromTarget >= 3.0F) {
				Path path = pathNavigator.getPathToPos(getAirPosAboveTarget(glider.world, target), 0);
				if (path == null || !pathNavigator.setPath(path, 1.25F)) {
					this.delayCounter += 15;
				}
			} else {
				if (!glider.getNavigator().tryMoveToEntityLiving(target, 1.25F)) {
					this.delayCounter += 15;
				}
			}
		}

		if (canSeeTarget && ((IDataManager) target).getValue(EEDataProcessors.CATCHING_COOLDOWN) <= 0) {
			double reachRange = glider.getWidth() * 2.0F * glider.getWidth() * 2.0F + target.getWidth();
			if (distanceToTargetSq <= reachRange) {
				target.startRiding(glider, true);
			}
		}
	}

	@Override
	public void resetTask() {
		GliderEetleEntity glider = this.glider;
		LivingEntity livingentity = glider.getAttackTarget();
		if (!EntityPredicates.CAN_AI_TARGET.test(livingentity)) {
			glider.setAttackTarget(null);
		}
		glider.setAggroed(false);
		glider.getNavigator().clearPath();
	}

	public static BlockPos getAirPosAboveTarget(World world, LivingEntity target) {
		BlockPos.Mutable mutable = target.getPosition().toMutable();
		for (int y = 0; y < 4; y++) {
			mutable.move(0, 1, 0);
			if (!world.isAirBlock(mutable)) {
				mutable.move(0, -1, 0);
				break;
			}
		}
		return mutable;
	}
}
