package com.minecraftabnormals.endergetic.common.entities.eetle.ai.charger;

import com.minecraftabnormals.endergetic.common.entities.eetle.ChargerEetleEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.EntityPredicates;

import java.util.EnumSet;

public class EetleMeleeAttackGoal extends Goal {
	private final ChargerEetleEntity attacker;
	private Path path;
	private double targetX;
	private double targetY;
	private double targetZ;
	private int delayCounter;

	public EetleMeleeAttackGoal(ChargerEetleEntity charger) {
		this.attacker = charger;
		this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	@Override
	public boolean shouldExecute() {
		LivingEntity livingentity = this.attacker.getAttackTarget();
		if (livingentity == null) {
			return false;
		} else if (!livingentity.isAlive()) {
			return false;
		} else {
			this.path = this.attacker.getNavigator().getPathToEntity(livingentity, 0);
			if (this.path != null) {
				return true;
			} else {
				return this.getAttackReachSqr(livingentity) >= this.attacker.getDistanceSq(livingentity.getPosX(), livingentity.getPosY(), livingentity.getPosZ());
			}
		}
	}

	@Override
	public void startExecuting() {
		this.attacker.getNavigator().setPath(this.path, 1.25F);
		this.attacker.setAggroed(true);
		this.delayCounter = 0;
	}

	@Override
	public boolean shouldContinueExecuting() {
		LivingEntity livingentity = this.attacker.getAttackTarget();
		if (livingentity == null) {
			return false;
		} else if (!livingentity.isAlive()) {
			return false;
		}
		return !this.attacker.getNavigator().noPath();
	}

	@Override
	public void tick() {
		LivingEntity livingentity = this.attacker.getAttackTarget();
		this.attacker.getLookController().setLookPositionWithEntity(livingentity, 30.0F, 30.0F);
		double d0 = this.attacker.getDistanceSq(livingentity.getPosX(), livingentity.getPosY(), livingentity.getPosZ());
		this.delayCounter = Math.max(this.delayCounter - 1, 0);
		if (this.attacker.getEntitySenses().canSee(livingentity) && this.delayCounter <= 0 && (this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D || livingentity.getDistanceSq(this.targetX, this.targetY, this.targetZ) >= 1.0D || this.attacker.getRNG().nextFloat() < 0.05F)) {
			this.targetX = livingentity.getPosX();
			this.targetY = livingentity.getPosY();
			this.targetZ = livingentity.getPosZ();
			this.delayCounter = 4 + this.attacker.getRNG().nextInt(7);
			if (d0 > 1024.0D) {
				this.delayCounter += 10;
			} else if (d0 > 256.0D) {
				this.delayCounter += 5;
			}

			if (!this.attacker.getNavigator().tryMoveToEntityLiving(livingentity, 1.25F)) {
				this.delayCounter += 15;
			}
		}

		this.checkAndPerformAttack(livingentity, d0);
	}

	@Override
	public void resetTask() {
		LivingEntity livingentity = this.attacker.getAttackTarget();
		if (!EntityPredicates.CAN_AI_TARGET.test(livingentity)) {
			this.attacker.setAttackTarget(null);
		}

		this.attacker.setAggroed(false);
		this.attacker.getNavigator().clearPath();
	}

	private void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
		if (this.attacker.isNoEndimationPlaying() && distToEnemySqr <= this.getAttackReachSqr(enemy)) {
			this.attacker.attackEntityAsMob(enemy);
		}
	}

	private double getAttackReachSqr(LivingEntity attackTarget) {
		return this.attacker.getWidth() * 2.0F * this.attacker.getWidth() * 2.0F + attackTarget.getWidth();
	}
}
