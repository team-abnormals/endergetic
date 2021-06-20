package com.minecraftabnormals.endergetic.common.entities.eetle.ai.brood;

import com.minecraftabnormals.abnormals_core.core.util.NetworkUtil;
import com.minecraftabnormals.endergetic.common.entities.eetle.BroodEetleEntity;
import com.minecraftabnormals.endergetic.common.entities.eetle.EetleEggEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.vector.Vector3d;

import java.util.EnumSet;
import java.util.Random;

public class BroodEetleLastStageGoal extends Goal {
	private final BroodEetleEntity broodEetle;
	private int cannonTicks;

	public BroodEetleLastStageGoal(BroodEetleEntity broodEetle) {
		this.broodEetle = broodEetle;
		this.setMutexFlags(EnumSet.allOf(Goal.Flag.class));
	}

	@Override
	public boolean shouldExecute() {
		BroodEetleEntity broodEetle = this.broodEetle;
		return broodEetle.isOnLastHealthStage() && broodEetle.isAlive() && !broodEetle.isFlying();
	}

	@Override
	public void startExecuting() {
		BroodEetleEntity broodEetle = this.broodEetle;
		broodEetle.getNavigator().clearPath();
		NetworkUtil.setPlayingAnimationMessage(broodEetle, BroodEetleEntity.SLAM);
		broodEetle.setFiringCannon(true);
	}

	@Override
	public void tick() {
		BroodEetleEntity broodEetle = this.broodEetle;
		if (broodEetle.isFiringCannon()) {
			this.cannonTicks++;

			if (broodEetle.isEndimationPlaying(BroodEetleEntity.SLAM)) {
				if (broodEetle.getAnimationTick() == 14) {
					BroodEetleSlamGoal.slam(broodEetle, broodEetle.getRNG(), 1.25F);
				}
			} else if (broodEetle.isEggMouthOpen() && this.cannonTicks % 20 == 0) {
				Random random = broodEetle.getRNG();
				if (BroodEetleLaunchEggsGoal.getNearbyEetleCount(broodEetle) <= 9 || this.cannonTicks <= 75 || random.nextFloat() <= 0.05F) {
					NetworkUtil.setPlayingAnimationMessage(broodEetle, BroodEetleEntity.LAUNCH);
					Vector3d firingPos = new Vector3d(-1.0D, 3.0D, 0.0D).rotateYaw(-broodEetle.renderYawOffset * ((float)Math.PI / 180F) - ((float) Math.PI / 2F));
					EetleEggEntity eetleEgg = new EetleEggEntity(broodEetle.world, broodEetle.getPositionVec().add(firingPos));
					eetleEgg.setEggSize(EetleEggEntity.EggSize.random(random, false));
					eetleEgg.setMotion(new Vector3d((random.nextFloat() - random.nextFloat()) * 0.35F, 0.8F + random.nextFloat() * 0.1F, (random.nextFloat() - random.nextFloat()) * 0.35F));
					broodEetle.world.addEntity(eetleEgg);
				} else {
					broodEetle.heal(5.0F);
					broodEetle.world.setEntityState(broodEetle, (byte) 60);
				}
			}
		}
	}

	@Override
	public boolean shouldContinueExecuting() {
		BroodEetleEntity broodEetle = this.broodEetle;
		return broodEetle.isAlive() && broodEetle.isOnLastHealthStage();
	}

	@Override
	public void resetTask() {
		BroodEetleEntity broodEetle = this.broodEetle;
		broodEetle.setFiringCannon(false);
	}
}
