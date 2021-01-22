package com.minecraftabnormals.endergetic.common.entities.eetle.ai.charger;

import com.minecraftabnormals.abnormals_core.core.endimator.entity.EndimatedGoal;
import com.minecraftabnormals.endergetic.common.entities.eetle.ChargerEetleEntity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.util.EntityPredicates;
import net.minecraft.world.World;

import java.util.EnumSet;

public class EetleCatapultGoal extends EndimatedGoal<ChargerEetleEntity> {
	private static final EntityPredicate PREDICATE = new EntityPredicate().allowFriendlyFire();
	private static final float MIN_DISTANCE = 2.0F;
	public int cooldown;

	public EetleCatapultGoal(ChargerEetleEntity entity) {
		super(entity, ChargerEetleEntity.CATAPULT);
		this.setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	@Override
	public boolean shouldExecute() {
		if (this.cooldown > 0) {
			this.cooldown--;
		} else if (this.entity.getRNG().nextFloat() < 0.1F) {
			ChargerEetleEntity chargerEetle = this.entity;
			LivingEntity attackTarget = chargerEetle.getAttackTarget();
			if (attackTarget != null && attackTarget.isAlive() && !chargerEetle.isCatapultProjectile() && chargerEetle.isOnGround()) {
				World world = chargerEetle.world;
				ChargerEetleEntity closestCharger = world.getClosestEntity(world.getEntitiesWithinAABB(ChargerEetleEntity.class, chargerEetle.getBoundingBox().grow(2.5F), eetle -> {
					return eetle != chargerEetle && eetle.isOnGround() && !eetle.isChild() && eetle.getAttackTarget() == attackTarget && !eetle.isCatapulting() && attackTarget.getDistance(eetle) >= MIN_DISTANCE;
				}), PREDICATE, null, chargerEetle.getPosX(), chargerEetle.getPosY(), chargerEetle.getPosZ());
				if (closestCharger != null) {
					chargerEetle.setCatapultingTarget(closestCharger);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean shouldContinueExecuting() {
		ChargerEetleEntity target = this.entity.getCatapultingTarget();
		if (target != null && target.isAlive()) {
			ChargerEetleEntity charger = this.entity;
			LivingEntity attackTarget = charger.getAttackTarget();
			return attackTarget != null && attackTarget.isAlive() && target.getAttackTarget() == attackTarget && target.getDistance(charger) <= charger.getAttributeValue(Attributes.FOLLOW_RANGE) && attackTarget.getDistance(target) >= MIN_DISTANCE && charger.canEntityBeSeen(target) && charger.isOnGround() && PREDICATE.canTarget(charger, target) && PREDICATE.canTarget(charger, attackTarget);
		}
		return false;
	}

	@Override
	public void tick() {
		ChargerEetleEntity target = this.entity.getCatapultingTarget();
		if (target != null) {
			ChargerEetleEntity charger = this.entity;
			charger.getLookController().setLookPositionWithEntity(target, 30.0F, 30.0F);
			charger.getNavigator().tryMoveToEntityLiving(target, 1.5F);
			double distanceSq = target.getDistanceSq(charger.getPosX(), charger.getPosY(), charger.getPosZ());
			if (distanceSq <= charger.getWidth() * 2.0F * charger.getWidth() * 2.0F + target.getWidth()) {
				this.launchTarget();
				this.resetTask();
			}
		}
	}

	@Override
	public void resetTask() {
		this.resetCooldown();
		this.entity.setCatapultingTarget(null);
		this.entity.getNavigator().clearPath();
		this.entity.setAggroed(false);
		LivingEntity attackTarget = this.entity.getAttackTarget();
		if (!EntityPredicates.CAN_AI_TARGET.test(attackTarget)) {
			this.entity.setAttackTarget(null);
		}
	}

	public void resetCooldown() {
		this.cooldown = 200 + this.random.nextInt(61);
	}

	private void launchTarget() {
		ChargerEetleEntity target = this.entity.getCatapultingTarget();
		if (target != null) {
			LivingEntity launchTo = target.getAttackTarget();
			if (launchTo != null) {
				this.playEndimation();
				target.launchFromCatapult(launchTo);
			}
		}
	}
}
