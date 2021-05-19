package com.minecraftabnormals.endergetic.common.entities.eetle.ai.brood;

import com.minecraftabnormals.abnormals_core.core.endimator.entity.EndimatedGoal;
import com.minecraftabnormals.abnormals_core.core.util.NetworkUtil;
import com.minecraftabnormals.endergetic.api.entity.util.DetectionHelper;
import com.minecraftabnormals.endergetic.common.entities.eetle.BroodEetleEntity;
import com.minecraftabnormals.endergetic.common.entities.eetle.flying.TargetFlyingRotations;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class BroodEetleAirSlamGoal extends EndimatedGoal<BroodEetleEntity> {

	public BroodEetleAirSlamGoal(BroodEetleEntity entity) {
		super(entity, BroodEetleEntity.AIR_CHARGE);
	}

	@Override
	public boolean shouldExecute() {
		BroodEetleEntity broodEetle = this.entity;
		if (broodEetle.isNotDroppingEggs() && broodEetle.canSlam() && broodEetle.isFlying() && !broodEetle.isOnGround() && broodEetle.isNoEndimationPlaying()) {
			BlockPos takeoffPos = broodEetle.takeoffPos;
			if (takeoffPos != null) {
				return !searchForAggressorsUnder(broodEetle, new AxisAlignedBB(takeoffPos).grow(8.0D, 7.0D, 8.0D)).isEmpty();
			}
		}
		return false;
	}

	@Override
	public void startExecuting() {
		BroodEetleEntity broodEetle = this.entity;
		broodEetle.getNavigator().clearPath();
		broodEetle.resetSlamCooldown();
		this.playEndimation();
	}

	@Override
	public void tick() {
		BroodEetleEntity broodEetle = this.entity;
		broodEetle.setTargetFlyingRotations(new TargetFlyingRotations(-30.0F, 0.0F));
		broodEetle.addVelocity(0.0F, -0.15F, 0.0F);

		if (broodEetle.isOnGround()) {
			int animationTick = broodEetle.getAnimationTick();
			if (animationTick >= 10 && animationTick <= 60) {
				NetworkUtil.setPlayingAnimationMessage(broodEetle, BroodEetleEntity.AIR_SLAM);
			}
		}
	}

	@Override
	public boolean shouldContinueExecuting() {
		return this.entity.isFlying() && this.isEndimationPlaying();
	}

	private static List<LivingEntity> searchForAggressorsUnder(BroodEetleEntity broodEetle, AxisAlignedBB otherBounds) {
		return broodEetle.world.getEntitiesWithinAABB(LivingEntity.class, DetectionHelper.expandDownwards(broodEetle.getBoundingBox(), 10.0F), livingEntity -> {
			if (broodEetle.getPosY() - livingEntity.getPosY() < 4 || !otherBounds.contains(livingEntity.getPositionVec())) {
				return false;
			}
			if (livingEntity instanceof PlayerEntity) {
				return livingEntity.isAlive() && !livingEntity.isInvisible() && broodEetle.canEntityBeSeen(livingEntity) && !((PlayerEntity) livingEntity).isCreative();
			}
			return livingEntity.isAlive() && livingEntity.isOnGround() && !livingEntity.isInvisible() && broodEetle.canEntityBeSeen(livingEntity) && (livingEntity instanceof MobEntity && ((MobEntity) livingEntity).getAttackTarget() == broodEetle || broodEetle.isAnAggressor(livingEntity));
		});
	}

}