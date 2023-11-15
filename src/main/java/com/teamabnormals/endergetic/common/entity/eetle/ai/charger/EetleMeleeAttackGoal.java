package com.teamabnormals.endergetic.common.entity.eetle.ai.charger;

import com.teamabnormals.endergetic.common.entity.eetle.ChargerEetle;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.pathfinder.Path;

import java.util.EnumSet;

public class EetleMeleeAttackGoal extends Goal {
	private final ChargerEetle attacker;
	private Path path;
	private double targetX;
	private double targetY;
	private double targetZ;
	private int delayCounter;
	private int meleeCooldown;

	public EetleMeleeAttackGoal(ChargerEetle charger) {
		this.attacker = charger;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		LivingEntity livingentity = this.attacker.getTarget();
		if (livingentity == null) {
			return false;
		} else if (!livingentity.isAlive()) {
			return false;
		} else {
			this.path = this.attacker.getNavigation().createPath(livingentity, 0);
			if (this.path != null) {
				return true;
			} else {
				return this.getAttackReachSqr(livingentity) >= this.attacker.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
			}
		}
	}

	@Override
	public void start() {
		this.attacker.getNavigation().moveTo(this.path, 1.25F);
		this.attacker.setAggressive(true);
		this.delayCounter = 0;
	}

	@Override
	public boolean canContinueToUse() {
		LivingEntity livingentity = this.attacker.getTarget();
		if (livingentity == null) {
			return false;
		} else if (!livingentity.isAlive()) {
			return false;
		}
		return !this.attacker.getNavigation().isDone();
	}

	@Override
	public void tick() {
		LivingEntity livingentity = this.attacker.getTarget();
		this.attacker.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
		double d0 = this.attacker.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
		this.delayCounter = Math.max(this.delayCounter - 1, 0);
		if (this.attacker.getSensing().hasLineOfSight(livingentity) && this.delayCounter <= 0 && (this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D || livingentity.distanceToSqr(this.targetX, this.targetY, this.targetZ) >= 1.0D || this.attacker.getRandom().nextFloat() < 0.05F)) {
			this.targetX = livingentity.getX();
			this.targetY = livingentity.getY();
			this.targetZ = livingentity.getZ();
			this.delayCounter = 4 + this.attacker.getRandom().nextInt(7);
			if (d0 > 1024.0D) {
				this.delayCounter += 10;
			} else if (d0 > 256.0D) {
				this.delayCounter += 5;
			}

			if (!this.attacker.getNavigation().moveTo(livingentity, 1.25F)) {
				this.delayCounter += 15;
			}
		}

		if (this.meleeCooldown > 0) {
			this.meleeCooldown--;
		} else {
			this.checkAndPerformAttack(livingentity, d0);
		}
	}

	@Override
	public void stop() {
		LivingEntity livingentity = this.attacker.getTarget();
		if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingentity)) {
			this.attacker.setTarget(null);
		}

		this.attacker.setAggressive(false);
		this.attacker.getNavigation().stop();
	}

	private void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
		ChargerEetle attacker = this.attacker;
		if (attacker.isNoEndimationPlaying() && distToEnemySqr <= this.getAttackReachSqr(enemy) && attacker.hasLineOfSight(enemy)) {
			attacker.doHurtTarget(enemy);
			this.meleeCooldown += 10 + attacker.getRandom().nextInt(11);
		}
	}

	private double getAttackReachSqr(LivingEntity attackTarget) {
		float widthDoubled = this.attacker.getBbWidth() * 2.0F;
		return widthDoubled * widthDoubled + attackTarget.getBbWidth();
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}
}
