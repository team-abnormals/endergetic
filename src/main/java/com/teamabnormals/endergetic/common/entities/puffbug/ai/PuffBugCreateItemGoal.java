package com.teamabnormals.endergetic.common.entities.puffbug.ai;

import java.util.EnumSet;

import com.teamabnormals.endergetic.api.util.TemporaryMathUtil;
import com.teamabnormals.endergetic.common.entities.puffbug.PuffBugEntity;

import com.teamabnormals.endergetic.core.registry.other.EEPlayableEndimations;
import com.teamabnormals.blueprint.core.endimator.entity.EndimatedGoal;
import com.teamabnormals.blueprint.core.util.NetworkUtil;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.effect.MobEffects;

public class PuffBugCreateItemGoal extends EndimatedGoal<PuffBugEntity> {
	private int ticksPassed;
	private float originalHealth;

	public PuffBugCreateItemGoal(PuffBugEntity puffbug) {
		super(puffbug, EEPlayableEndimations.PUFF_BUG_MAKE_ITEM);
		this.setFlags(EnumSet.of(Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		return !this.entity.isPassenger() && this.entity.isInflated() && !this.entity.isAggressive() && this.entity.hasStackToCreate() && this.entity.isNoEndimationPlaying();
	}

	@Override
	public boolean canContinueToUse() {
		return this.entity.getTarget() == null && this.entity.isInflated() && this.entity.hasLevitation() && this.entity.hasStackToCreate() && this.entity.getHealth() >= this.originalHealth;
	}

	@Override
	public void start() {
		this.originalHealth = this.entity.getHealth();
	}

	@Override
	public void tick() {
		this.ticksPassed++;

		this.entity.getNavigation().stop();
		this.entity.setSpeed(0.0F);

		if (this.ticksPassed >= 25 && this.entity.isNoEndimationPlaying()) {
			this.playEndimation();
		}

		this.entity.getRotationController().rotate(0.0F, 180.0F, 0.0F, 20);

		if (this.isEndimationPlaying() && this.entity.hasStackToCreate()) {
			RandomSource rand = this.entity.getRandom();

			if (this.isEndimationAtTick(90)) {
				ItemEntity itementity = new ItemEntity(this.entity.level, this.entity.getX(), this.entity.getY() - 0.5D, this.entity.getZ(), this.entity.getStackToCreate());
				itementity.setPickUpDelay(40);
				this.entity.level.addFreshEntity(itementity);
				this.entity.setStackToCreate(null);

				this.entity.removeEffect(MobEffects.LEVITATION);

				double posX = this.entity.getX();
				double posY = this.entity.getY();
				double posZ = this.entity.getZ();
				for (int i = 0; i < 6; i++) {
					double offsetX = TemporaryMathUtil.makeNegativeRandomly(rand.nextFloat() * 0.1F, rand);
					double offsetZ = TemporaryMathUtil.makeNegativeRandomly(rand.nextFloat() * 0.1F, rand);

					double x = posX + offsetX;
					double y = posY + (rand.nextFloat() * 0.05F) + 0.5F;
					double z = posZ + offsetZ;

					NetworkUtil.spawnParticle("endergetic:short_poise_bubble", x, y, z, TemporaryMathUtil.makeNegativeRandomly((rand.nextFloat() * 0.15F), rand) + 0.025F, (rand.nextFloat() * 0.025F) + 0.025F, TemporaryMathUtil.makeNegativeRandomly((rand.nextFloat() * 0.15F), rand) + 0.025F);
				}
			} else if (this.isEndimationAtTick(85)) {
				this.entity.playSound(this.entity.getItemCreationSound(), 0.1F, this.entity.isBaby() ? (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.5F : (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
			}
		}
	}

	@Override
	public void stop() {
		this.ticksPassed = 0;
		this.originalHealth = 0.0F;
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}
}