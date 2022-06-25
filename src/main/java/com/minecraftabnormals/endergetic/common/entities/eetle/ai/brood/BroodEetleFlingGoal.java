package com.minecraftabnormals.endergetic.common.entities.eetle.ai.brood;

import com.minecraftabnormals.endergetic.common.entities.eetle.AbstractEetleEntity;
import com.minecraftabnormals.endergetic.common.entities.eetle.BroodEetleEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;

public class BroodEetleFlingGoal extends Goal {
	private static final TargetingConditions PREDICATE = TargetingConditions.forCombat().selector(livingEntity -> !(livingEntity instanceof AbstractEetleEntity));
	private final BroodEetleEntity broodEetle;
	@Nullable
	private LivingEntity target;
	private int ticksPassed;

	public BroodEetleFlingGoal(BroodEetleEntity broodEetle) {
		this.broodEetle = broodEetle;
		this.setFlags(EnumSet.of(Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		BroodEetleEntity broodEetle = this.broodEetle;
		if (broodEetle.isFiringCannon() || !broodEetle.hasWokenUp()) {
			return false;
		}
		List<LivingEntity> targets = searchForNearbyAggressors(broodEetle, 3.0D);
		if (!targets.isEmpty() && (targets.size() <= 3 || !broodEetle.canSlam())) {
			this.target = broodEetle.level.getNearestEntity(targets, PREDICATE, broodEetle, broodEetle.getX(), broodEetle.getY(), broodEetle.getZ());
			return this.target != null;
		}
		return false;
	}

	@Override
	public void start() {
		this.broodEetle.setAggressive(true);
	}

	@Override
	public boolean canContinueToUse() {
		if (this.ticksPassed >= 80) {
			return false;
		}
		LivingEntity target = this.target;
		if (target != null && target.isAlive() && !target.isInvisible()) {
			return target.distanceToSqr(this.broodEetle) <= 20.25F;
		}
		return false;
	}

	@Override
	public void tick() {
		this.ticksPassed++;

		BroodEetleEntity broodEetle = this.broodEetle;
		LivingEntity target = this.target;
		if (target != null) {
			broodEetle.getLookControl().setLookAt(target, 30.0F, 30.0F);
			if (this.ticksPassed >= 10) {
				if (broodEetle.isNoEndimationPlaying() && broodEetle.distanceToSqr(target) <= getAttackReachSqr(broodEetle.getBbWidth() * 1.8F, target)) {
					broodEetle.doHurtTarget(target);
				}
			}
		}
	}

	@Override
	public void stop() {
		this.target = null;
		this.ticksPassed = 0;
	}

	private static double getAttackReachSqr(float width, LivingEntity attackTarget) {
		return width * 2.0F * width * 2.0F + attackTarget.getBbWidth();
	}

	public static List<LivingEntity> searchForNearbyAggressors(BroodEetleEntity broodEetle, double size) {
		return broodEetle.level.getEntitiesOfClass(LivingEntity.class, broodEetle.getBoundingBox().inflate(size), livingEntity -> {
			if (livingEntity instanceof Player) {
				return livingEntity.isAlive() && !livingEntity.isInvisible() && !((Player) livingEntity).isCreative();
			}
			return livingEntity.isAlive() && !livingEntity.isInvisible() && broodEetle.getSensing().hasLineOfSight(livingEntity) && (livingEntity instanceof Mob && ((Mob) livingEntity).getTarget() == broodEetle || broodEetle.isAnAggressor(livingEntity));
		});
	}
}
