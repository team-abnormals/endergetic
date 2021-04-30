package com.minecraftabnormals.endergetic.common.entities.eetle.ai.brood;

import com.minecraftabnormals.endergetic.common.entities.eetle.AbstractEetleEntity;
import com.minecraftabnormals.endergetic.common.entities.eetle.BroodEetleEntity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.EnumSet;
import java.util.List;

public class BroodEetleHurtByTargetGoal extends Goal {
	private static final EntityPredicate PREDICATE = (new EntityPredicate()).setLineOfSiteRequired().setUseInvisibilityCheck();
	private final BroodEetleEntity broodEetle;
	private int revengeTimerOld;

	public BroodEetleHurtByTargetGoal(BroodEetleEntity broodEetle) {
		this.broodEetle = broodEetle;
		this.setMutexFlags(EnumSet.of(Goal.Flag.TARGET));
	}

	@Override
	public boolean shouldExecute() {
		BroodEetleEntity broodEetle = this.broodEetle;
		int revengeTimer = broodEetle.getRevengeTimer();
		LivingEntity revengeTarget = broodEetle.getRevengeTarget();
		if (revengeTimer != this.revengeTimerOld && revengeTarget != null) {
			return isSuitableTarget(broodEetle, revengeTarget);
		}
		return false;
	}

	public void startExecuting() {
		BroodEetleEntity broodEetle = this.broodEetle;
		LivingEntity revengeTarget = broodEetle.getRevengeTarget();
		if (revengeTarget != null && !(revengeTarget instanceof BroodEetleEntity) && !broodEetle.isOnSameTeam(revengeTarget)) {
			broodEetle.addRevengeTarget(revengeTarget);
			this.revengeTimerOld = broodEetle.getRevengeTimer();

			double targetDistance = broodEetle.getAttributeValue(Attributes.FOLLOW_RANGE);
			AxisAlignedBB axisalignedbb = AxisAlignedBB.fromVector(broodEetle.getPositionVec()).grow(targetDistance, 10.0D, targetDistance);
			List<AbstractEetleEntity> list = broodEetle.world.getLoadedEntitiesWithinAABB(AbstractEetleEntity.class, axisalignedbb);
			for (AbstractEetleEntity eetle : list) {
				if (!eetle.isChild() && (eetle.getAttackTarget() == null || !eetle.getAttackTarget().isAlive()) && !eetle.isOnSameTeam(revengeTarget)) {
					eetle.setAttackTarget(revengeTarget);
				}
			}
		}
	}

	@Override
	public boolean shouldContinueExecuting() {
		return false;
	}

	private static boolean isSuitableTarget(BroodEetleEntity broodEetle, LivingEntity target) {
		return PREDICATE.canTarget(broodEetle, target) && broodEetle.isWithinHomeDistanceFromPosition(target.getPosition());
	}
}
