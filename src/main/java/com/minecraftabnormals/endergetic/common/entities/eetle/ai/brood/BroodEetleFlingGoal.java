package com.minecraftabnormals.endergetic.common.entities.eetle.ai.brood;

import com.minecraftabnormals.endergetic.common.entities.eetle.AbstractEetleEntity;
import com.minecraftabnormals.endergetic.common.entities.eetle.BroodEetleEntity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;

public class BroodEetleFlingGoal extends Goal {
	private static final EntityPredicate PREDICATE = new EntityPredicate().setCustomPredicate(livingEntity -> !(livingEntity instanceof AbstractEetleEntity));
	private final BroodEetleEntity broodEetle;
	@Nullable
	private LivingEntity target;
	private int ticksPassed;

	public BroodEetleFlingGoal(BroodEetleEntity broodEetle) {
		this.broodEetle = broodEetle;
		this.setMutexFlags(EnumSet.of(Flag.LOOK));
	}

	@Override
	public boolean shouldExecute() {
		BroodEetleEntity broodEetle = this.broodEetle;
		if (broodEetle.isFiringCannon() || !broodEetle.hasWokenUp()) {
			return false;
		}
		List<LivingEntity> targets = searchForNearbyAggressors(broodEetle, 3.0D);
		if (!targets.isEmpty() && (targets.size() <= 3 || !broodEetle.canSlam())) {
			this.target = broodEetle.world.getClosestEntity(targets, PREDICATE, broodEetle, broodEetle.getPosX(), broodEetle.getPosY(), broodEetle.getPosZ());
			return this.target != null;
		}
		return false;
	}

	@Override
	public void startExecuting() {
		this.broodEetle.setAggroed(true);
	}

	@Override
	public boolean shouldContinueExecuting() {
		if (this.ticksPassed >= 80) {
			return false;
		}
		LivingEntity target = this.target;
		if (target != null && target.isAlive() && !target.isInvisible()) {
			return target.getDistanceSq(this.broodEetle) <= 20.25F;
		}
		return false;
	}

	@Override
	public void tick() {
		this.ticksPassed++;

		BroodEetleEntity broodEetle = this.broodEetle;
		LivingEntity target = this.target;
		if (target != null) {
			broodEetle.getLookController().setLookPositionWithEntity(target, 30.0F, 30.0F);
			if (this.ticksPassed >= 10) {
				if (broodEetle.isNoEndimationPlaying() && broodEetle.getDistanceSq(target) <= getAttackReachSqr(broodEetle.getWidth() * 1.8F, target)) {
					broodEetle.attackEntityAsMob(target);
				}
			}
		}
	}

	@Override
	public void resetTask() {
		this.target = null;
		this.ticksPassed = 0;
	}

	private static double getAttackReachSqr(float width, LivingEntity attackTarget) {
		return width * 2.0F * width * 2.0F + attackTarget.getWidth();
	}

	public static List<LivingEntity> searchForNearbyAggressors(BroodEetleEntity broodEetle, double size) {
		return broodEetle.world.getEntitiesWithinAABB(LivingEntity.class, broodEetle.getBoundingBox().grow(size), livingEntity -> {
			if (livingEntity instanceof PlayerEntity) {
				return livingEntity.isAlive() && !livingEntity.isInvisible() && !((PlayerEntity) livingEntity).isCreative();
			}
			return livingEntity.isAlive() && !livingEntity.isInvisible() && broodEetle.getEntitySenses().canSee(livingEntity) && (livingEntity instanceof MobEntity && ((MobEntity) livingEntity).getAttackTarget() == broodEetle || broodEetle.isAnAggressor(livingEntity));
		});
	}
}
