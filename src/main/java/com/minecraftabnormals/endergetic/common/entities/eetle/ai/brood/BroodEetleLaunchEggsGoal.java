package com.minecraftabnormals.endergetic.common.entities.eetle.ai.brood;

import com.minecraftabnormals.abnormals_core.core.endimator.entity.EndimatedGoal;
import com.minecraftabnormals.endergetic.common.entities.eetle.BroodEetleEntity;
import com.minecraftabnormals.endergetic.common.entities.eetle.EetleEggsEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.util.math.vector.Vector3d;

import java.util.EnumSet;
import java.util.List;
import java.util.Random;

public class BroodEetleLaunchEggsGoal extends EndimatedGoal<BroodEetleEntity> {
	private int ticksOffGround;
	private int shotsToFire;
	private int ticksPassed;

	public BroodEetleLaunchEggsGoal(BroodEetleEntity entity) {
		super(entity, BroodEetleEntity.LAUNCH);
		this.setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	@Override
	public boolean shouldExecute() {
		BroodEetleEntity broodEetle = this.entity;
		if (broodEetle.canFireEggCannon() && broodEetle.isOnGround() && broodEetle.isNoEndimationPlaying()) {
			List<LivingEntity> aggressors = BroodEetleFlingGoal.searchForNearbyAggressors(broodEetle, broodEetle.getAttributeValue(Attributes.FOLLOW_RANGE) * 0.5D);
			return computeAverageAggressorDistanceSq(broodEetle.getPositionVec(), aggressors) >= 42.25F || !aggressors.isEmpty() && this.random.nextFloat() < 0.0005F;
		}
		return false;
	}

	@Override
	public boolean shouldContinueExecuting() {
		if (this.entity.isOnGround()) {
			this.ticksOffGround = 0;
		}
		return this.shotsToFire > 0 && ++this.ticksOffGround < 10;
	}

	@Override
	public void startExecuting() {
		this.entity.setFiringCannon(true);
		this.shotsToFire = this.random.nextInt(4) + 4;
	}

	@Override
	public void tick() {
		this.ticksPassed++;

		BroodEetleEntity broodEetle = this.entity;
		if (broodEetle.isEggMouthOpen() && this.ticksPassed % 20 == 0) {
			this.playEndimation();
			Vector3d firingPos = new Vector3d(-1.0D, 3.0D, 0.0D).rotateYaw(-broodEetle.renderYawOffset * ((float)Math.PI / 180F) - ((float) Math.PI / 2F));
			EetleEggsEntity eetleEgg = new EetleEggsEntity(broodEetle.world, broodEetle.getPositionVec().add(firingPos));
			Random random = this.random;
			eetleEgg.setEggSize(EetleEggsEntity.EggSize.random(random));
			eetleEgg.setMotion(new Vector3d((random.nextFloat() - random.nextFloat()) * 0.35F, 0.8F + random.nextFloat() * 0.1F, (random.nextFloat() - random.nextFloat()) * 0.35F));
			broodEetle.world.addEntity(eetleEgg);
			this.shotsToFire--;
		}
	}

	@Override
	public void resetTask() {
		BroodEetleEntity broodEetle = this.entity;
		broodEetle.setFiringCannon(false);
		broodEetle.resetEggCannonCooldown();
		this.ticksOffGround = 0;
		this.shotsToFire = 0;
	}

	private static double computeAverageAggressorDistanceSq(Vector3d pos, List<LivingEntity> aggressors) {
		double total = 0.0F;
		for (LivingEntity livingEntity : aggressors) {
			total += pos.squareDistanceTo(livingEntity.getPositionVec());
		}
		return total / aggressors.size();
	}
}
