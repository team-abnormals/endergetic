package com.minecraftabnormals.endergetic.common.entities.eetle.ai.brood;

import com.minecraftabnormals.abnormals_core.core.endimator.entity.EndimatedGoal;
import com.minecraftabnormals.abnormals_core.core.util.NetworkUtil;
import com.minecraftabnormals.endergetic.api.entity.util.DetectionHelper;
import com.minecraftabnormals.endergetic.common.entities.eetle.BroodEetleEntity;
import com.minecraftabnormals.endergetic.common.entities.eetle.flying.TargetFlyingRotations;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;

import java.util.List;

public class BroodEetleAirSlamGoal extends EndimatedGoal<BroodEetleEntity> {

	public BroodEetleAirSlamGoal(BroodEetleEntity entity) {
		super(entity, BroodEetleEntity.AIR_CHARGE);
	}

	@Override
	public boolean canUse() {
		BroodEetleEntity broodEetle = this.entity;
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
		BroodEetleEntity broodEetle = this.entity;
		broodEetle.getNavigation().stop();
		broodEetle.resetSlamCooldown();
		this.playEndimation();
	}

	@Override
	public void tick() {
		BroodEetleEntity broodEetle = this.entity;
		broodEetle.setTargetFlyingRotations(new TargetFlyingRotations(-30.0F, 0.0F));
		broodEetle.push(0.0F, -0.15F, 0.0F);

		if (broodEetle.isOnGround()) {
			int animationTick = broodEetle.getAnimationTick();
			if (animationTick >= 10 && animationTick <= 60) {
				NetworkUtil.setPlayingAnimationMessage(broodEetle, BroodEetleEntity.AIR_SLAM);
			}
		}
	}

	@Override
	public boolean canContinueToUse() {
		return this.entity.isFlying() && this.isEndimationPlaying();
	}

	private static List<LivingEntity> searchForAggressorsUnder(BroodEetleEntity broodEetle, AABB otherBounds) {
		return broodEetle.level.getEntitiesOfClass(LivingEntity.class, DetectionHelper.expandDownwards(broodEetle.getBoundingBox(), 10.0F), livingEntity -> {
			if (broodEetle.getY() - livingEntity.getY() < 4 || !otherBounds.contains(livingEntity.position())) {
				return false;
			}
			if (livingEntity instanceof Player) {
				return livingEntity.isAlive() && !livingEntity.isInvisible() && broodEetle.canSee(livingEntity) && !((Player) livingEntity).isCreative();
			}
			return livingEntity.isAlive() && livingEntity.isOnGround() && !livingEntity.isInvisible() && broodEetle.canSee(livingEntity) && (livingEntity instanceof Mob && ((Mob) livingEntity).getTarget() == broodEetle || broodEetle.isAnAggressor(livingEntity));
		});
	}

}
