package com.minecraftabnormals.endergetic.common.entities.eetle.ai.glider;

import com.minecraftabnormals.endergetic.common.entities.eetle.GliderEetleEntity;
import com.minecraftabnormals.endergetic.common.entities.eetle.flying.TargetFlyingRotations;
import com.minecraftabnormals.endergetic.core.registry.other.EEDataProcessors;
import com.teamabnormals.blueprint.common.world.storage.tracking.IDataManager;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;

import java.util.EnumSet;

public class GliderEetleGrabGoal extends Goal {
	private final GliderEetleEntity glider;
	private Path path;
	private int delayCounter;
	private int swoopTimer;

	public GliderEetleGrabGoal(GliderEetleEntity glider) {
		this.glider = glider;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		if (this.glider.isGrounded()) return false;
		LivingEntity target = this.glider.getTarget();
		if (target == null || !target.isAlive() || GliderEetleEntity.isEntityLarge(target) || ((IDataManager) target).getValue(EEDataProcessors.CATCHING_COOLDOWN) > 0 || target.getVehicle() instanceof GliderEetleEntity || !this.glider.getPassengers().isEmpty()) {
			return false;
		}
		this.path = this.glider.getNavigation().createPath(target, 0);
		return this.path != null;
	}

	@Override
	public void start() {
		GliderEetleEntity glider = this.glider;
		if (!glider.isFlying()) {
			glider.setFlying(true);
		}
		glider.getNavigation().moveTo(this.path, 1.375F);
		glider.setAggressive(true);
		this.delayCounter = 0;
	}

	@Override
	public boolean canContinueToUse() {
		if (this.glider.isGrounded()) return false;
		if (this.swoopTimer > 0 && !this.glider.getPassengers().isEmpty()) {
			return true;
		}
		LivingEntity target = this.glider.getTarget();
		if (target == null || !target.isAlive() || GliderEetleEntity.isEntityLarge(target) || ((IDataManager) target).getValue(EEDataProcessors.CATCHING_COOLDOWN) > 0 || target.getVehicle() instanceof GliderEetleEntity || !this.glider.getPassengers().isEmpty()) {
			return false;
		}
		return !this.glider.getNavigation().isDone();
	}

	@Override
	public void tick() {
		GliderEetleEntity glider = this.glider;
		if (this.swoopTimer > 0) {
			this.swoopTimer--;
			glider.setDeltaMovement(glider.getDeltaMovement().scale(1.0625F));
			glider.getNavigation().stop();
			glider.setTargetFlyingRotations(new TargetFlyingRotations(-30.0F, glider.getTargetFlyingRotations().getTargetFlyRoll()));
			return;
		}

		this.delayCounter = Math.max(this.delayCounter - 1, 0);
		LivingEntity target = glider.getTarget();
		glider.getLookControl().setLookAt(target, 30.0F, 30.0F);
		double distanceToTargetSq = glider.distanceToSqr(target);
		if (glider.getSensing().hasLineOfSight(target) && this.delayCounter <= 0 && glider.getRandom().nextFloat() < 0.05F) {
			this.delayCounter = 4 + glider.getRandom().nextInt(9);
			PathNavigation pathNavigator = glider.getNavigation();
			if (distanceToTargetSq > 1024.0D) {
				this.delayCounter += 10;
			} else if (distanceToTargetSq > 256.0D) {
				this.delayCounter += 5;
			}

			float blocksFromTarget = Mth.sqrt((float) distanceToTargetSq);
			if (blocksFromTarget >= 3.0F) {
				Path path = pathNavigator.createPath(getAirPosAboveTarget(glider.level, target), 0);
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
		if (distanceToTargetSq <= reachRange && glider.hasLineOfSight(target)) {
			if (target.startRiding(glider, true)) {
				RandomSource random = glider.getRandom();
				float yaw = glider.getYRot() + (random.nextFloat() * 15.0F - random.nextFloat() * 15.0F);
				float xMotion = -Mth.sin(yaw * ((float) Math.PI / 180F));
				float zMotion = Mth.cos(yaw * ((float) Math.PI / 180F));
				glider.setDeltaMovement(glider.getDeltaMovement().add(xMotion * 0.3F, 0.45F, zMotion * 0.3F));
				this.swoopTimer = 10;
			}
		}
	}

	@Override
	public void stop() {
		GliderEetleEntity glider = this.glider;
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

	public static BlockPos getAirPosAboveTarget(Level world, LivingEntity target) {
		BlockPos.MutableBlockPos mutable = target.blockPosition().mutable();
		for (int y = 0; y < 4; y++) {
			mutable.move(0, 1, 0);
			if (!world.isEmptyBlock(mutable)) {
				mutable.move(0, -1, 0);
				break;
			}
		}
		return mutable;
	}
}
