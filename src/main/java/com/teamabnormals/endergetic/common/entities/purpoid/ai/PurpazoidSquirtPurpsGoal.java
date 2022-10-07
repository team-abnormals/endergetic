package com.teamabnormals.endergetic.common.entities.purpoid.ai;

import com.teamabnormals.blueprint.core.util.NetworkUtil;
import com.teamabnormals.endergetic.common.entities.purpoid.PurpoidEntity;
import com.teamabnormals.endergetic.core.registry.EEEntities;
import com.teamabnormals.endergetic.core.registry.other.EEPlayableEndimations;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class PurpazoidSquirtPurpsGoal extends Goal {
	private final PurpoidEntity purpoid;
	private int ticksWhileWaiting;
	private Vec3 chosenDirection = Vec3.ZERO;
	private List<LivingEntity> nearbyRevengeTargets = new ArrayList<>();

	public PurpazoidSquirtPurpsGoal(PurpoidEntity purpoid) {
		this.purpoid = purpoid;
		this.setFlags(EnumSet.allOf(Goal.Flag.class));
	}

	@Override
	public boolean canUse() {
		PurpoidEntity purpoid = this.purpoid;
		if (!purpoid.wantsToFlee() && purpoid.getStunTimer() <= 0 && purpoid.isNoEndimationPlaying() && !purpoid.getTeleportController().isTeleporting()) {
			if (++this.ticksWhileWaiting < 10) return false;
			var revengeTargets = purpoid.revengeTargets;
			if (!revengeTargets.isEmpty()) {
				var nearbyRevengeTargets = purpoid.level.getEntitiesOfClass(LivingEntity.class, purpoid.getBoundingBox().inflate(32.0F), entity -> TargetingConditions.DEFAULT.test(purpoid, entity) && revengeTargets.contains(entity.getUUID()));
				if (!nearbyRevengeTargets.isEmpty()) {
					Vec3 positionOfRandomNearbyRevengeTarget = nearbyRevengeTargets.get(purpoid.getRandom().nextInt(nearbyRevengeTargets.size())).position();
					Vec3 direction = purpoid.position().subtract(positionOfRandomNearbyRevengeTarget).normalize();
					double y = direction.y();
					this.chosenDirection = y < 0.0D ? new Vec3(direction.x(), -y, direction.z()) : direction;
					this.nearbyRevengeTargets = nearbyRevengeTargets;
					return true;
				}
			}
		}
		this.ticksWhileWaiting = 0;
		return false;
	}

	@Override
	public void start() {
		PurpoidEntity purpoid = this.purpoid;
		NetworkUtil.setPlayingAnimation(purpoid, EEPlayableEndimations.PURPOID_SQUIRT_ATTACK);
		purpoid.setSpeed(0.0F);
		purpoid.getNavigation().stop();
	}

	@Override
	public boolean canContinueToUse() {
		if (!this.purpoid.isEndimationPlaying(EEPlayableEndimations.PURPOID_SQUIRT_ATTACK)) {
			this.purpoid.setStunTimer(600 + this.purpoid.getRandom().nextInt(101));
			return false;
		}
		return true;
	}

	@Override
	public void tick() {
		PurpoidEntity purpoid = this.purpoid;
		purpoid.setSpeed(0.0F);
		purpoid.getNavigation().stop();
		int animationTick = purpoid.getAnimationTick();
		float velocityScale = 0.0333F;
		if (animationTick >= 40) {
			velocityScale = 0.2F;
			if (animationTick % 4 == 0) {
				PurpoidEntity purp = EEEntities.PURPOID.get().create(purpoid.level);
				if (purp != null) {
					purp.updateAge(-24000);
					Vec3 deltaMovement = purpoid.getDeltaMovement();
					purp.moveTo(purpoid.getX() - 4.0F * deltaMovement.x(), purpoid.getY() + 0.75F - 4.0F * deltaMovement.y(), purpoid.getZ() - 4.0F * deltaMovement.z(), 0.0F, 0.0F);
					RandomSource random = purpoid.getRandom();
					purp.setDeltaMovement(deltaMovement.add(random.triangle(0.0D, 1.0F), 0.0F, random.triangle(0.0D, 1.0F)).scale(-2.0F));
					purp.setTarget(this.nearbyRevengeTargets.get(random.nextInt(this.nearbyRevengeTargets.size())));
					purpoid.level.addFreshEntity(purp);
				}
			}
		}
		purpoid.setDeltaMovement(purpoid.getDeltaMovement().add(this.chosenDirection.scale(velocityScale)));
	}

	@Override
	public void stop() {
		this.chosenDirection = Vec3.ZERO;
		this.ticksWhileWaiting = 0;
		this.nearbyRevengeTargets.clear();
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}
}
