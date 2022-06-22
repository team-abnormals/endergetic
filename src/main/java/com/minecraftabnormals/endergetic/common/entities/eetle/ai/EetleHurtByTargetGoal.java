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
		this.setAlertOthers(AbstractEetleEntity.class);
	}

	@Override
	protected boolean canAttack(@Nullable LivingEntity potentialTarget, EntityPredicate targetPredicate) {
		return !(potentialTarget instanceof BroodEetleEntity || potentialTarget instanceof AbstractEetleEntity) && super.canAttack(potentialTarget, targetPredicate);
	}

	@Override
	protected void alertOthers() {
		double targetDistance = this.getFollowDistance();
		AxisAlignedBB axisalignedbb = AxisAlignedBB.unitCubeFromLowerCorner(this.mob.position()).inflate(targetDistance, 10.0D, targetDistance);
		List<AbstractEetleEntity> list = this.mob.level.getLoadedEntitiesOfClass(AbstractEetleEntity.class, axisalignedbb);
		Iterator<AbstractEetleEntity> iterator = list.iterator();
		LivingEntity revengeTarget = this.mob.getLastHurtByMob();
		while (iterator.hasNext()) {
			AbstractEetleEntity eetle = iterator.next();
			if (eetle != this.mob && !eetle.isBaby() && (eetle.getTarget() == null || !eetle.getTarget().isAlive()) && !eetle.isAlliedTo(revengeTarget)) {
				this.alertOther(eetle, revengeTarget);
			}
		}
	}

}
