package com.minecraftabnormals.endergetic.common.entities.booflo.ai;

import com.minecraftabnormals.endergetic.api.entity.util.DetectionHelper;
import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity;

import com.minecraftabnormals.endergetic.core.registry.other.EEPlayableEndimations;
import com.teamabnormals.blueprint.core.endimator.entity.EndimatedGoal;
import com.teamabnormals.blueprint.core.util.NetworkUtil;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class BoofloSlamGoal extends EndimatedGoal<BoofloEntity> {
	private final Level world;

	public BoofloSlamGoal(BoofloEntity booflo) {
		super(booflo, EEPlayableEndimations.BOOFLO_CHARGE);
		this.world = booflo.level;
	}

	@Override
	public boolean canUse() {
		return this.entity.hasAggressiveAttackTarget() && this.entity.getPassengers().isEmpty() && this.entity.isEndimationPlaying(EEPlayableEndimations.BOOFLO_SWIM) && !this.entity.isOnGround() && this.isEntityUnder() && this.isSolidUnderTarget();
	}

	@Override
	public boolean canContinueToUse() {
		if (!this.entity.hasAggressiveAttackTarget()) return false;

		if (!this.isSolidUnderTarget()) {
			NetworkUtil.setPlayingAnimation(this.entity, EEPlayableEndimations.BOOFLO_INFLATE);
			return false;
		}
		return !this.entity.isOnGround() && this.isEndimationPlaying();
	}

	@Override
	public void start() {
		this.playEndimation();
	}

	@Override
	public void tick() {
		this.entity.getNavigation().stop();
		this.entity.setSpeed(0.0F);

		this.entity.setXRot(0.0F);
	}

	private boolean isEntityUnder() {
		for (LivingEntity entity : this.world.getEntitiesOfClass(LivingEntity.class, DetectionHelper.expandDownwards(this.entity.getBoundingBox().inflate(1.0F), 12.0F))) {
			if (entity == this.entity.getBoofloAttackTarget()) {
				return true;
			}
		}
		return false;
	}

	private boolean isSolidUnderTarget() {
		boolean isSomewhatSolidUnder = false;
		BlockPos.MutableBlockPos mutable = this.entity.getBoofloAttackTarget().blockPosition().mutable();
		for (int y = 1; y < 4; y++) {
			isSomewhatSolidUnder = isSomewhatSolidUnder || this.entity.getBoofloAttackTarget() != null && Block.canSupportRigidBlock(this.world, mutable.below(y));
		}
		return isSomewhatSolidUnder;
	}
}