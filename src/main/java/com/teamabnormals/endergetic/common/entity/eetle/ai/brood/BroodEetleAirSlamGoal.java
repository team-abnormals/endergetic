package com.teamabnormals.endergetic.common.entity.eetle.ai.brood;

import com.teamabnormals.blueprint.core.endimator.entity.EndimatedGoal;
import com.teamabnormals.blueprint.core.util.NetworkUtil;
import com.teamabnormals.endergetic.api.entity.util.DetectionHelper;
import com.teamabnormals.endergetic.common.entity.eetle.BroodEetle;
import com.teamabnormals.endergetic.common.entity.eetle.flying.TargetFlyingRotations;
import com.teamabnormals.endergetic.core.registry.other.EEPlayableEndimations;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class BroodEetleAirSlamGoal extends EndimatedGoal<BroodEetle> {

	public BroodEetleAirSlamGoal(BroodEetle entity) {
		super(entity, EEPlayableEndimations.BROOD_EETLE_AIR_CHARGE);
	}

	@Override
	public boolean canUse() {
		BroodEetle broodEetle = this.entity;
		if (broodEetle.isNotDroppingEggs() && broodEetle.canSlam() && broodEetle.isFlying() && !broodEetle.isOnGround() && broodEetle.isNoEndimationPlaying()) {
			BlockPos takeoffPos = broodEetle.takeoffPos;
			if (takeoffPos != null) {
				return !searchForAggressorsUnder(broodEetle, new AABB(takeoffPos).inflate(8.0D, 7.0D, 8.0D)).isEmpty();
			}
		}
		return false;
	}

	@Override
	public void start() {
		BroodEetle broodEetle = this.entity;
		broodEetle.getNavigation().stop();
		broodEetle.resetSlamCooldown();
		this.playEndimation();
	}

	@Override
	public void tick() {
		BroodEetle broodEetle = this.entity;
		broodEetle.setTargetFlyingRotations(new TargetFlyingRotations(-30.0F, 0.0F));
		broodEetle.push(0.0F, -0.15F, 0.0F);

		if (broodEetle.isOnGround()) {
			int animationTick = broodEetle.getAnimationTick();
			if (animationTick >= 10 && animationTick <= 60) {
				NetworkUtil.setPlayingAnimation(broodEetle, EEPlayableEndimations.BROOD_EETLE_AIR_SLAM);
			}
		}
	}

	@Override
	public boolean canContinueToUse() {
		return this.entity.isFlying() && this.isEndimationPlaying();
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}

	private static List<LivingEntity> searchForAggressorsUnder(BroodEetle broodEetle, AABB otherBounds) {
		return broodEetle.level.getEntitiesOfClass(LivingEntity.class, DetectionHelper.expandDownwards(broodEetle.getBoundingBox(), 10.0F), livingEntity -> {
			if (broodEetle.getY() - livingEntity.getY() < 4 || !otherBounds.contains(livingEntity.position())) {
				return false;
			}
			if (livingEntity instanceof Player) {
				return livingEntity.isAlive() && !livingEntity.isInvisible() && broodEetle.hasLineOfSight(livingEntity) && !((Player) livingEntity).isCreative();
			}
			return livingEntity.isAlive() && livingEntity.isOnGround() && !livingEntity.isInvisible() && broodEetle.hasLineOfSight(livingEntity) && (livingEntity instanceof Mob && ((Mob) livingEntity).getTarget() == broodEetle || broodEetle.isAnAggressor(livingEntity));
		});
	}

}
