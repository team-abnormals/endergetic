package com.minecraftabnormals.endergetic.common.entities.eetle.ai.brood;

import com.minecraftabnormals.endergetic.api.entity.util.DetectionHelper;
import com.minecraftabnormals.endergetic.common.entities.eetle.AbstractEetleEntity;
import com.minecraftabnormals.endergetic.common.entities.eetle.BroodEetleEntity;
import com.minecraftabnormals.endergetic.common.entities.eetle.BroodEggSackEntity;
import com.minecraftabnormals.endergetic.common.entities.eetle.EetleEggEntity;
import com.minecraftabnormals.endergetic.core.registry.other.EEPlayableEndimations;
import com.teamabnormals.blueprint.core.endimator.entity.EndimatedGoal;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.sensing.Sensing;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;

public class BroodEetleDropEggsGoal extends EndimatedGoal<BroodEetleEntity> {
	private int eggsToDrop;
	private int ticksPassed;

	public BroodEetleDropEggsGoal(BroodEetleEntity entity) {
		super(entity, EEPlayableEndimations.BROOD_EETLE_LAUNCH);
	}

	@Override
	public boolean canUse() {
		BroodEetleEntity broodEetle = this.entity;
		return broodEetle.getTicksFlying() >= 100 && broodEetle.canDropOffEggs() && broodEetle.isFlying() && !broodEetle.isOnGround() && broodEetle.isNoEndimationPlaying() && notNearGround(broodEetle) && areFewEetlesNearby(broodEetle) && !BroodEetleFlingGoal.searchForNearbyAggressors(broodEetle, broodEetle.getAttributeValue(Attributes.FOLLOW_RANGE)).isEmpty() && this.random.nextFloat() < 0.025F;
	}

	@Override
	public void start() {
		BroodEetleEntity broodEetle = this.entity;
		broodEetle.setDroppingEggs(true);
		broodEetle.resetEggDropOffCooldown();
		this.eggsToDrop = this.random.nextInt(3) + 5;
	}

	@Override
	public void tick() {
		this.ticksPassed++;

		BroodEetleEntity broodEetle = this.entity;
		if (broodEetle.isEggMouthOpen() && this.ticksPassed % 20 == 0) {
			this.playEndimation();
			Level world = broodEetle.level;
			BroodEggSackEntity eggSack = broodEetle.getEggSack(world);
			if (eggSack != null) {
				EetleEggEntity eetleEgg = new EetleEggEntity(world, eggSack.position());
				RandomSource random = broodEetle.getRandom();
				eetleEgg.setEggSize(EetleEggEntity.EggSize.random(random, true));
				eetleEgg.setDeltaMovement(new Vec3((random.nextFloat() - random.nextFloat()) * 0.3F, -0.1F, (random.nextFloat() - random.nextFloat()) * 0.3F).add(broodEetle.getDeltaMovement()));
				world.addFreshEntity(eetleEgg);
				this.eggsToDrop--;
			}
		}
	}

	@Override
	public boolean canContinueToUse() {
		return this.entity.isFlying() && this.eggsToDrop > 0;
	}

	@Override
	public void stop() {
		this.entity.setDroppingEggs(false);
	}

	public static boolean areFewEetlesNearby(BroodEetleEntity broodEetle) {
		double followRange = broodEetle.getAttributeValue(Attributes.FOLLOW_RANGE);
		Sensing senses = broodEetle.getSensing();
		return broodEetle.level.getEntitiesOfClass(AbstractEetleEntity.class, broodEetle.getBoundingBox().inflate(followRange, followRange * 0.5D, followRange), eetle -> {
			return eetle.isAlive() && senses.hasLineOfSight(eetle) && (!eetle.isBaby() || eetle.getGrowingAge() >= -240);
		}).size() <= 2;
	}

	private static boolean notNearGround(BroodEetleEntity broodEetle) {
		return broodEetle.level.noCollision(DetectionHelper.checkOnGround(broodEetle.getBoundingBox(), -4.0F));
	}
}
