package com.teamabnormals.endergetic.common.entities.eetle.ai.brood;

import com.teamabnormals.endergetic.common.entities.eetle.BroodEetleEntity;
import com.teamabnormals.endergetic.common.entities.eetle.EetleEggEntity;
import com.teamabnormals.endergetic.core.registry.other.EEPlayableEndimations;
import com.teamabnormals.blueprint.core.util.NetworkUtil;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class BroodEetleLastStageGoal extends Goal {
	private final BroodEetleEntity broodEetle;
	private int cannonTicks;

	public BroodEetleLastStageGoal(BroodEetleEntity broodEetle) {
		this.broodEetle = broodEetle;
		this.setFlags(EnumSet.allOf(Goal.Flag.class));
	}

	@Override
	public boolean canUse() {
		BroodEetleEntity broodEetle = this.broodEetle;
		return broodEetle.isOnLastHealthStage() && broodEetle.isAlive() && !broodEetle.isFlying();
	}

	@Override
	public void start() {
		BroodEetleEntity broodEetle = this.broodEetle;
		broodEetle.getNavigation().stop();
		NetworkUtil.setPlayingAnimation(broodEetle, EEPlayableEndimations.BROOD_EETLE_SLAM);
		broodEetle.setFiringCannon(true);
	}

	@Override
	public void tick() {
		BroodEetleEntity broodEetle = this.broodEetle;
		if (broodEetle.isFiringCannon()) {
			this.cannonTicks++;

			if (broodEetle.isEndimationPlaying(EEPlayableEndimations.BROOD_EETLE_SLAM)) {
				if (broodEetle.getAnimationTick() == 14) {
					BroodEetleSlamGoal.slam(broodEetle, broodEetle.getRandom(), 1.25F);
				}
			} else if (broodEetle.isEggMouthOpen() && this.cannonTicks % 20 == 0) {
				RandomSource random = broodEetle.getRandom();
				if (BroodEetleLaunchEggsGoal.getNearbyEetleCount(broodEetle) <= 9 || this.cannonTicks <= 75 || random.nextFloat() <= 0.05F) {
					NetworkUtil.setPlayingAnimation(broodEetle, EEPlayableEndimations.BROOD_EETLE_LAUNCH);
					Vec3 firingPos = new Vec3(-1.0D, 3.0D, 0.0D).yRot(-broodEetle.yBodyRot * ((float)Math.PI / 180F) - ((float) Math.PI / 2F));
					EetleEggEntity eetleEgg = new EetleEggEntity(broodEetle.level, broodEetle.position().add(firingPos));
					eetleEgg.setEggSize(EetleEggEntity.EggSize.random(random, false));
					eetleEgg.setDeltaMovement(new Vec3((random.nextFloat() - random.nextFloat()) * 0.35F, 0.8F + random.nextFloat() * 0.1F, (random.nextFloat() - random.nextFloat()) * 0.35F));
					broodEetle.level.addFreshEntity(eetleEgg);
				} else {
					broodEetle.heal(5.0F);
					broodEetle.level.broadcastEntityEvent(broodEetle, (byte) 60);
				}
			}
		}
	}

	@Override
	public boolean canContinueToUse() {
		BroodEetleEntity broodEetle = this.broodEetle;
		return broodEetle.isAlive() && broodEetle.isOnLastHealthStage();
	}

	@Override
	public void stop() {
		BroodEetleEntity broodEetle = this.broodEetle;
		broodEetle.setFiringCannon(false);
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}
}
