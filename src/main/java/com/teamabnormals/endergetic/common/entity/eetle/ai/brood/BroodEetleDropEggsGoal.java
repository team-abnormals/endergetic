package com.teamabnormals.endergetic.common.entity.eetle.ai.brood;

import com.teamabnormals.blueprint.core.endimator.entity.EndimatedGoal;
import com.teamabnormals.endergetic.api.entity.util.DetectionHelper;
import com.teamabnormals.endergetic.common.entity.eetle.AbstractEetle;
import com.teamabnormals.endergetic.common.entity.eetle.BroodEetle;
import com.teamabnormals.endergetic.common.entity.eetle.BroodEggSack;
import com.teamabnormals.endergetic.common.entity.eetle.EetleEgg;
import com.teamabnormals.endergetic.core.registry.other.EEPlayableEndimations;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.sensing.Sensing;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class BroodEetleDropEggsGoal extends EndimatedGoal<BroodEetle> {
	private int eggsToDrop;
	private int ticksPassed;

	public BroodEetleDropEggsGoal(BroodEetle entity) {
		super(entity, EEPlayableEndimations.BROOD_EETLE_DROP_EGGS);
	}

	@Override
	public boolean canUse() {
		BroodEetle broodEetle = this.entity;
		return broodEetle.getTicksFlying() >= 100 && broodEetle.canDropOffEggs() && broodEetle.isFlying() && !broodEetle.isOnGround() && broodEetle.isNoEndimationPlaying() && notNearGround(broodEetle) && areFewEetlesNearby(broodEetle) && !BroodEetleFlingGoal.searchForNearbyAggressors(broodEetle, broodEetle.getAttributeValue(Attributes.FOLLOW_RANGE)).isEmpty() && this.random.nextFloat() < 0.025F;
	}

	@Override
	public void start() {
		BroodEetle broodEetle = this.entity;
		broodEetle.setDroppingEggs(true);
		broodEetle.resetEggDropOffCooldown();
		this.eggsToDrop = this.random.nextInt(3) + 5;
	}

	@Override
	public void tick() {
		this.ticksPassed++;

		BroodEetle broodEetle = this.entity;
		if (broodEetle.isEggMouthOpen() && this.ticksPassed % 20 == 0) {
			this.playEndimation();
			Level world = broodEetle.level;
			BroodEggSack eggSack = broodEetle.getEggSack(world);
			if (eggSack != null) {
				EetleEgg eetleEgg = new EetleEgg(world, eggSack.position());
				RandomSource random = broodEetle.getRandom();
				eetleEgg.setEggSize(EetleEgg.EggSize.random(random, true));
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

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}

	public static boolean areFewEetlesNearby(BroodEetle broodEetle) {
		double followRange = broodEetle.getAttributeValue(Attributes.FOLLOW_RANGE);
		Sensing senses = broodEetle.getSensing();
		return broodEetle.level.getEntitiesOfClass(AbstractEetle.class, broodEetle.getBoundingBox().inflate(followRange, followRange * 0.5D, followRange), eetle -> {
			return eetle.isAlive() && senses.hasLineOfSight(eetle) && (!eetle.isBaby() || eetle.getGrowingAge() >= -240);
		}).size() <= 2;
	}

	private static boolean notNearGround(BroodEetle broodEetle) {
		return broodEetle.level.noCollision(DetectionHelper.checkOnGround(broodEetle.getBoundingBox(), -4.0F));
	}
}
