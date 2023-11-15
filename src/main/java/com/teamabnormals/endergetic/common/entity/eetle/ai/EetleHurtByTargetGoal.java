package com.teamabnormals.endergetic.common.entity.eetle.ai;

import com.teamabnormals.endergetic.common.entity.eetle.AbstractEetle;
import com.teamabnormals.endergetic.common.entity.eetle.BroodEetle;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;

public class EetleHurtByTargetGoal extends HurtByTargetGoal {

	public EetleHurtByTargetGoal(AbstractEetle eetle) {
		super(eetle);
		this.setAlertOthers(AbstractEetle.class);
	}

	@Override
	protected boolean canAttack(@Nullable LivingEntity potentialTarget, TargetingConditions targetPredicate) {
		return !(potentialTarget instanceof BroodEetle || potentialTarget instanceof AbstractEetle) && super.canAttack(potentialTarget, targetPredicate);
	}

	@Override
	protected void alertOthers() {
		double targetDistance = this.getFollowDistance();
		AABB axisalignedbb = AABB.unitCubeFromLowerCorner(this.mob.position()).inflate(targetDistance, 10.0D, targetDistance);
		List<AbstractEetle> list = this.mob.level.getEntitiesOfClass(AbstractEetle.class, axisalignedbb);
		Iterator<AbstractEetle> iterator = list.iterator();
		LivingEntity revengeTarget = this.mob.getLastHurtByMob();
		while (iterator.hasNext()) {
			AbstractEetle eetle = iterator.next();
			if (eetle != this.mob && !eetle.isBaby() && (eetle.getTarget() == null || !eetle.getTarget().isAlive()) && !eetle.isAlliedTo(revengeTarget)) {
				this.alertOther(eetle, revengeTarget);
			}
		}
	}

}
