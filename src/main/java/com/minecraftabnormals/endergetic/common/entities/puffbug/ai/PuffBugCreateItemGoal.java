package com.minecraftabnormals.endergetic.common.entities.puffbug.ai;

import java.util.EnumSet;
import java.util.Random;

import com.teamabnormals.abnormals_core.core.library.endimator.EndimatedGoal;
import com.teamabnormals.abnormals_core.core.library.endimator.Endimation;
import com.teamabnormals.abnormals_core.core.utils.MathUtils;
import com.teamabnormals.abnormals_core.core.utils.NetworkUtil;
import com.minecraftabnormals.endergetic.common.entities.puffbug.PuffBugEntity;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.potion.Effects;

public class PuffBugCreateItemGoal extends EndimatedGoal<PuffBugEntity> {
	private int ticksPassed;
	private float originalHealth;

	public PuffBugCreateItemGoal(PuffBugEntity puffbug) {
		super(puffbug);
		this.setMutexFlags(EnumSet.of(Flag.MOVE));
	}

	@Override
	public boolean shouldExecute() {
		return !this.entity.isPassenger() && this.entity.isInflated() && !this.entity.isAggressive() && this.entity.hasStackToCreate() && this.entity.isNoEndimationPlaying();
	}

	@Override
	public boolean shouldContinueExecuting() {
		return this.entity.getAttackTarget() == null && this.entity.isInflated() && this.entity.hasLevitation() && this.entity.hasStackToCreate() && this.entity.getHealth() >= this.originalHealth;
	}

	@Override
	public void startExecuting() {
		this.originalHealth = this.entity.getHealth();
	}

	@Override
	public void tick() {
		this.ticksPassed++;

		this.entity.getNavigator().clearPath();
		this.entity.setAIMoveSpeed(0.0F);

		if (this.ticksPassed >= 25 && this.entity.isNoEndimationPlaying()) {
			this.playEndimation();
		}

		this.entity.getRotationController().rotate(0.0F, 180.0F, 0.0F, 20);

		if (this.isEndimationPlaying() && this.entity.hasStackToCreate()) {
			Random rand = this.entity.getRNG();

			if (this.isEndimationAtTick(90)) {
				ItemEntity itementity = new ItemEntity(this.entity.world, this.entity.getPosX(), this.entity.getPosY() - 0.5D, this.entity.getPosZ(), this.entity.getStackToCreate());
				itementity.setPickupDelay(40);
				this.entity.world.addEntity(itementity);
				this.entity.setStackToCreate(null);

				this.entity.removePotionEffect(Effects.LEVITATION);

				double posX = this.entity.getPosX();
				double posY = this.entity.getPosY();
				double posZ = this.entity.getPosZ();
				for (int i = 0; i < 6; i++) {
					double offsetX = MathUtils.makeNegativeRandomly(rand.nextFloat() * 0.1F, rand);
					double offsetZ = MathUtils.makeNegativeRandomly(rand.nextFloat() * 0.1F, rand);

					double x = posX + offsetX;
					double y = posY + (rand.nextFloat() * 0.05F) + 0.5F;
					double z = posZ + offsetZ;

					NetworkUtil.spawnParticle("endergetic:short_poise_bubble", x, y, z, MathUtils.makeNegativeRandomly((rand.nextFloat() * 0.15F), rand) + 0.025F, (rand.nextFloat() * 0.025F) + 0.025F, MathUtils.makeNegativeRandomly((rand.nextFloat() * 0.15F), rand) + 0.025F);
				}
			} else if (this.isEndimationAtTick(85)) {
				this.entity.playSound(this.entity.getItemCreationSound(), 0.1F, this.entity.isChild() ? (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.5F : (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
			}
		}
	}

	@Override
	public void resetTask() {
		this.ticksPassed = 0;
		this.originalHealth = 0.0F;
	}

	@Override
	protected Endimation getEndimation() {
		return PuffBugEntity.MAKE_ITEM_ANIMATION;
	}
}