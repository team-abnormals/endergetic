package com.teamabnormals.endergetic.common.entities.eetle.ai.glider;

import com.teamabnormals.endergetic.common.entities.eetle.GliderEetleEntity;
import com.teamabnormals.endergetic.core.registry.other.EEDataProcessors;
import com.teamabnormals.endergetic.core.registry.other.EEPlayableEndimations;
import com.teamabnormals.blueprint.common.world.storage.tracking.IDataManager;
import com.teamabnormals.blueprint.core.endimator.entity.EndimatedGoal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.EntitySelector;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Random;

public class GliderEetleBiteGoal extends EndimatedGoal<GliderEetleEntity> {
	@Nullable
	private Path path;
	private int delayCounter;

	public GliderEetleBiteGoal(GliderEetleEntity entity) {
		super(entity, EEPlayableEndimations.GLIDER_EETLE_MUNCH);
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		if (this.entity.isGrounded()) return false;
		LivingEntity target = this.entity.getTarget();
		if (target != null && target.isAlive() && (GliderEetleEntity.isEntityLarge(target) || ((IDataManager) target).getValue(EEDataProcessors.CATCHING_COOLDOWN) > 0) && !(target.getVehicle() instanceof GliderEetleEntity) && this.entity.getPassengers().isEmpty()) {
			this.path = this.entity.getNavigation().createPath(target, 0);
			return this.path != null;
		}
		return false;
	}

	@Override
	public void start() {
		GliderEetleEntity glider = this.entity;
		if (!glider.isFlying()) {
			glider.setFlying(true);
		}
		glider.getNavigation().moveTo(this.path, 1.25F);
		glider.setAggressive(true);
		this.delayCounter = 0;
	}

	@Override
	public boolean canContinueToUse() {
		if (this.entity.isGrounded()) return false;
		LivingEntity target = this.entity.getTarget();
		return target != null && target.isAlive() && (GliderEetleEntity.isEntityLarge(target) || ((IDataManager) target).getValue(EEDataProcessors.CATCHING_COOLDOWN) > 0) && !(target.getVehicle() instanceof GliderEetleEntity) && this.entity.getPassengers().isEmpty() && !this.entity.getNavigation().isDone();
	}

	@Override
	public void tick() {
		this.delayCounter = Math.max(this.delayCounter - 1, 0);
		GliderEetleEntity glider = this.entity;
		LivingEntity target = glider.getTarget();
		glider.getLookControl().setLookAt(target, 30.0F, 30.0F);
		double distanceToTargetSq = glider.distanceToSqr(target);
		boolean canSeeTarget = glider.getSensing().hasLineOfSight(target);
		Random random = this.random;
		if (canSeeTarget && this.delayCounter <= 0 && random.nextFloat() < 0.05F) {
			this.delayCounter = 4 + random.nextInt(9);
			PathNavigation pathNavigator = glider.getNavigation();
			if (distanceToTargetSq >= 9.0F) {
				Path path = pathNavigator.createPath(GliderEetleGrabGoal.getAirPosAboveTarget(glider.level, target), 0);
				if (path == null || !pathNavigator.moveTo(path, 1.25F)) {
					this.delayCounter += 15;
				}
			} else {
				if (!glider.getNavigation().moveTo(target, 1.25F)) {
					this.delayCounter += 15;
				}
			}
		}

		double reachRange = glider.getBbWidth() * 2.0F * glider.getBbWidth() * 2.0F + target.getBbWidth();
		if (distanceToTargetSq <= reachRange) {
			if (!this.isEndimationPlaying()) {
				this.playEndimation();
			} else if ((this.isEndimationAtTick(8) || this.isEndimationAtTick(18)) && glider.hasLineOfSight(target)) {
				target.hurt(GliderEetleMunchGoal.causeMunchDamage(glider), (float) glider.getAttributeValue(Attributes.ATTACK_DAMAGE) + random.nextInt(3));
			}
		}
	}

	@Override
	public void stop() {
		GliderEetleEntity glider = this.entity;
		LivingEntity livingentity = glider.getTarget();
		if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingentity)) {
			glider.setTarget(null);
		}
		glider.setAggressive(false);
		glider.getNavigation().stop();
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}
}
