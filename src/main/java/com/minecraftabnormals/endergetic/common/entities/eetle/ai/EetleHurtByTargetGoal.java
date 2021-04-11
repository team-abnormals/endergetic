package com.minecraftabnormals.endergetic.common.entities.eetle.ai;

import com.minecraftabnormals.endergetic.common.entities.eetle.AbstractEetleEntity;
import com.minecraftabnormals.endergetic.common.entities.eetle.BroodEetleEntity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;

public class EetleHurtByTargetGoal extends HurtByTargetGoal {

	public EetleHurtByTargetGoal(AbstractEetleEntity eetle) {
		super(eetle);
		this.setCallsForHelp(AbstractEetleEntity.class);
	}

	@Override
	protected boolean isSuitableTarget(@Nullable LivingEntity potentialTarget, EntityPredicate targetPredicate) {
		return !(potentialTarget instanceof BroodEetleEntity || potentialTarget instanceof AbstractEetleEntity) && super.isSuitableTarget(potentialTarget, targetPredicate);
	}

	@Override
	protected void alertOthers() {
		double targetDistance = this.getTargetDistance();
		AxisAlignedBB axisalignedbb = AxisAlignedBB.fromVector(this.goalOwner.getPositionVec()).grow(targetDistance, 10.0D, targetDistance);
		List<AbstractEetleEntity> list = this.goalOwner.world.getLoadedEntitiesWithinAABB(AbstractEetleEntity.class, axisalignedbb);
		Iterator<AbstractEetleEntity> iterator = list.iterator();
		LivingEntity revengeTarget = this.goalOwner.getRevengeTarget();
		while (iterator.hasNext()) {
			AbstractEetleEntity eetle = iterator.next();
			if (eetle != this.goalOwner && !eetle.isChild() && (eetle.getAttackTarget() == null || !eetle.getAttackTarget().isAlive()) && !eetle.isOnSameTeam(revengeTarget)) {
				this.setAttackTarget(eetle, revengeTarget);
			}
		}
	}

}
