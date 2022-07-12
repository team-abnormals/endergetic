package com.teamabnormals.endergetic.common.entities.eetle.ai.brood;

import com.teamabnormals.endergetic.common.entities.eetle.AbstractEetleEntity;
import com.teamabnormals.endergetic.common.entities.eetle.BroodEetleEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.AABB;

import java.util.EnumSet;
import java.util.List;

public class BroodEetleHurtByTargetGoal extends Goal {
	private static final TargetingConditions PREDICATE = TargetingConditions.forCombat().ignoreLineOfSight();
	private final BroodEetleEntity broodEetle;
	private int revengeTimerOld;

	public BroodEetleHurtByTargetGoal(BroodEetleEntity broodEetle) {
		this.broodEetle = broodEetle;
		this.setFlags(EnumSet.of(Goal.Flag.TARGET));
	}

	@Override
	public boolean canUse() {
		BroodEetleEntity broodEetle = this.broodEetle;
		int revengeTimer = broodEetle.getLastHurtByMobTimestamp();
		LivingEntity revengeTarget = broodEetle.getLastHurtByMob();
		if (revengeTimer != this.revengeTimerOld && revengeTarget != null) {
			return isSuitableTarget(broodEetle, revengeTarget);
		}
		return false;
	}

	public void start() {
		BroodEetleEntity broodEetle = this.broodEetle;
		LivingEntity revengeTarget = broodEetle.getLastHurtByMob();
		if (revengeTarget != null && !(revengeTarget instanceof BroodEetleEntity) && !broodEetle.isAlliedTo(revengeTarget)) {
			broodEetle.addRevengeTarget(revengeTarget);
			this.revengeTimerOld = broodEetle.getLastHurtByMobTimestamp();

			double targetDistance = broodEetle.getAttributeValue(Attributes.FOLLOW_RANGE);
			AABB axisalignedbb = AABB.unitCubeFromLowerCorner(broodEetle.position()).inflate(targetDistance, 10.0D, targetDistance);
			List<AbstractEetleEntity> list = broodEetle.level.getEntitiesOfClass(AbstractEetleEntity.class, axisalignedbb);
			for (AbstractEetleEntity eetle : list) {
				if (!eetle.isBaby() && (eetle.getTarget() == null || !eetle.getTarget().isAlive()) && !eetle.isAlliedTo(revengeTarget)) {
					eetle.setTarget(revengeTarget);
				}
			}
		}
	}

	@Override
	public boolean canContinueToUse() {
		return false;
	}

	private static boolean isSuitableTarget(BroodEetleEntity broodEetle, LivingEntity target) {
		return PREDICATE.test(broodEetle, target) && broodEetle.isWithinRestriction(target.blockPosition());
	}
}
