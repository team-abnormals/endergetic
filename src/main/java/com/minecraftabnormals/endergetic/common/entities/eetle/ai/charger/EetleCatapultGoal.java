package com.minecraftabnormals.endergetic.common.entities.eetle.ai.charger;

import com.minecraftabnormals.abnormals_core.core.endimator.entity.EndimatedGoal;
import com.minecraftabnormals.endergetic.common.entities.eetle.ChargerEetleEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.level.Level;

import java.util.EnumSet;

import net.minecraft.world.entity.ai.goal.Goal.Flag;

public class EetleCatapultGoal extends EndimatedGoal<ChargerEetleEntity> {
	private static final TargetingConditions PREDICATE = new TargetingConditions().allowSameTeam();
	private static final float MIN_DISTANCE = 2.0F;
	public int cooldown;

	public EetleCatapultGoal(ChargerEetleEntity entity) {
		super(entity, ChargerEetleEntity.CATAPULT);
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		if (this.cooldown > 0) {
			this.cooldown--;
		} else if (this.entity.getRandom().nextFloat() < 0.1F) {
			ChargerEetleEntity chargerEetle = this.entity;
			LivingEntity attackTarget = chargerEetle.getTarget();
			if (attackTarget != null && attackTarget.isAlive() && !chargerEetle.isCatapultProjectile() && chargerEetle.isOnGround()) {
				Level world = chargerEetle.level;
				ChargerEetleEntity closestCharger = world.getNearestEntity(world.getEntitiesOfClass(ChargerEetleEntity.class, chargerEetle.getBoundingBox().inflate(2.5F), eetle -> {
					return eetle != chargerEetle && eetle.isOnGround() && !eetle.isBaby() && eetle.getTarget() == attackTarget && !eetle.isCatapulting() && attackTarget.distanceTo(eetle) >= MIN_DISTANCE;
				}), PREDICATE, null, chargerEetle.getX(), chargerEetle.getY(), chargerEetle.getZ());
				if (closestCharger != null) {
					chargerEetle.setCatapultingTarget(closestCharger);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean canContinueToUse() {
		ChargerEetleEntity target = this.entity.getCatapultingTarget();
		if (target != null && target.isAlive()) {
			ChargerEetleEntity charger = this.entity;
			LivingEntity attackTarget = charger.getTarget();
			return attackTarget != null && attackTarget.isAlive() && target.getTarget() == attackTarget && target.distanceTo(charger) <= charger.getAttributeValue(Attributes.FOLLOW_RANGE) && attackTarget.distanceTo(target) >= MIN_DISTANCE && charger.canSee(target) && charger.isOnGround() && PREDICATE.test(charger, target) && PREDICATE.test(charger, attackTarget);
		}
		return false;
	}

	@Override
	public void tick() {
		ChargerEetleEntity target = this.entity.getCatapultingTarget();
		if (target != null) {
			ChargerEetleEntity charger = this.entity;
			charger.getLookControl().setLookAt(target, 30.0F, 30.0F);
			charger.getNavigation().moveTo(target, 1.5F);
			double distanceSq = target.distanceToSqr(charger.getX(), charger.getY(), charger.getZ());
			if (distanceSq <= charger.getBbWidth() * 2.0F * charger.getBbWidth() * 2.0F + target.getBbWidth()) {
				this.launchTarget();
				this.stop();
			}
		}
	}

	@Override
	public void stop() {
		this.resetCooldown();
		this.entity.setCatapultingTarget(null);
		this.entity.getNavigation().stop();
		this.entity.setAggressive(false);
		LivingEntity attackTarget = this.entity.getTarget();
		if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(attackTarget)) {
			this.entity.setTarget(null);
		}
	}

	public void resetCooldown() {
		this.cooldown = 200 + this.random.nextInt(61);
	}

	private void launchTarget() {
		ChargerEetleEntity target = this.entity.getCatapultingTarget();
		if (target != null) {
			LivingEntity launchTo = target.getTarget();
			if (launchTo != null) {
				this.playEndimation();
				target.launchFromCatapult(launchTo);
			}
		}
	}
}
