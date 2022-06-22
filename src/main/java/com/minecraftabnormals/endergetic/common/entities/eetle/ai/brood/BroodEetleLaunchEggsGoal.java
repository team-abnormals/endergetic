package com.minecraftabnormals.endergetic.common.entities.eetle.ai.brood;

import com.minecraftabnormals.abnormals_core.core.endimator.entity.EndimatedGoal;
import com.minecraftabnormals.endergetic.common.entities.eetle.AbstractEetleEntity;
import com.minecraftabnormals.endergetic.common.entities.eetle.BroodEetleEntity;
import com.minecraftabnormals.endergetic.common.entities.eetle.EetleEggEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.EntitySenses;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.util.math.vector.Vector3d;

import java.util.EnumSet;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.ai.goal.Goal.Flag;

public class BroodEetleLaunchEggsGoal extends EndimatedGoal<BroodEetleEntity> {
	private int ticksOffGround;
	private int shotsToFire;
	private int ticksPassed;

	public BroodEetleLaunchEggsGoal(BroodEetleEntity entity) {
		super(entity, BroodEetleEntity.LAUNCH);
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		BroodEetleEntity broodEetle = this.entity;
		if ((broodEetle.canFireEggCannon() || broodEetle.getEggCannonCooldown() <= 200 && this.random.nextFloat() < 0.05F && BroodEetleDropEggsGoal.areFewEetlesNearby(broodEetle)) && broodEetle.hasWokenUp() && broodEetle.isOnGround() && broodEetle.isNoEndimationPlaying() && !broodEetle.isFlying()) {
			List<LivingEntity> aggressors = BroodEetleFlingGoal.searchForNearbyAggressors(broodEetle, broodEetle.getAttributeValue(Attributes.FOLLOW_RANGE) * 0.5D);
			return computeAverageAggressorDistanceSq(broodEetle.position(), aggressors) >= 42.25F || !aggressors.isEmpty() && this.random.nextFloat() < 0.01F;
		}
		return false;
	}

	@Override
	public boolean canContinueToUse() {
		if (this.entity.isOnGround()) {
			this.ticksOffGround = 0;
		}
		return this.shotsToFire > 0 && ++this.ticksOffGround < 10;
	}

	@Override
	public void start() {
		BroodEetleEntity broodEetle = this.entity;
		broodEetle.setFiringCannon(true);
		this.shotsToFire = this.random.nextInt(3) + 5 - (int) Math.min(5.0F, (float) getNearbyEetleCount(broodEetle) * 0.625F);
	}

	@Override
	public void tick() {
		this.ticksPassed++;

		BroodEetleEntity broodEetle = this.entity;
		if (broodEetle.isEggMouthOpen() && this.ticksPassed % 20 == 0) {
			this.playEndimation();
			Vector3d firingPos = new Vector3d(-1.0D, 3.0D, 0.0D).yRot(-broodEetle.yBodyRot * ((float)Math.PI / 180F) - ((float) Math.PI / 2F));
			EetleEggEntity eetleEgg = new EetleEggEntity(broodEetle.level, broodEetle.position().add(firingPos));
			Random random = this.random;
			eetleEgg.setEggSize(EetleEggEntity.EggSize.random(random, true));
			eetleEgg.setDeltaMovement(new Vector3d((random.nextFloat() - random.nextFloat()) * 0.35F, 0.8F + random.nextFloat() * 0.1F, (random.nextFloat() - random.nextFloat()) * 0.35F));
			broodEetle.level.addFreshEntity(eetleEgg);
			this.shotsToFire--;
		}
	}

	@Override
	public void stop() {
		BroodEetleEntity broodEetle = this.entity;
		broodEetle.setFiringCannon(false);
		broodEetle.resetEggCannonCooldown();
		this.ticksOffGround = 0;
		this.shotsToFire = 0;
	}

	private static double computeAverageAggressorDistanceSq(Vector3d pos, List<LivingEntity> aggressors) {
		double total = 0.0F;
		for (LivingEntity livingEntity : aggressors) {
			total += pos.distanceToSqr(livingEntity.position());
		}
		return total / aggressors.size();
	}

	public static int getNearbyEetleCount(BroodEetleEntity broodEetle) {
		double followRange = broodEetle.getAttributeValue(Attributes.FOLLOW_RANGE);
		double posY = broodEetle.getY();
		EntitySenses senses = broodEetle.getSensing();
		return broodEetle.level.getEntitiesOfClass(AbstractEetleEntity.class, broodEetle.getBoundingBox().inflate(followRange, followRange * 0.75D, followRange), eetle -> {
			if (eetle.getY() - posY >= 0.5F && !senses.canSee(eetle)) {
				return false;
			}
			return eetle.isAlive() && (!eetle.isBaby() || eetle.getGrowingAge() >= -240);
		}).size();
	}
}
