package com.minecraftabnormals.endergetic.common.entities.booflo.ai;

import com.minecraftabnormals.abnormals_core.core.endimator.entity.EndimatedGoal;
import com.minecraftabnormals.abnormals_core.core.util.NetworkUtil;
import com.minecraftabnormals.endergetic.api.entity.util.DetectionHelper;
import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity;

import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BoofloSlamGoal extends EndimatedGoal<BoofloEntity> {
	private World world;

	public BoofloSlamGoal(BoofloEntity booflo) {
		super(booflo, BoofloEntity.CHARGE);
		this.world = booflo.world;
	}

	@Override
	public boolean shouldExecute() {
		return this.entity.hasAggressiveAttackTarget() && this.entity.getPassengers().isEmpty() && this.entity.isEndimationPlaying(BoofloEntity.SWIM) && !this.entity.isOnGround() && this.isEntityUnder() && this.isSolidUnderTarget();
	}

	@Override
	public boolean shouldContinueExecuting() {
		if (!this.entity.hasAggressiveAttackTarget()) return false;

		if (!this.isSolidUnderTarget()) {
			NetworkUtil.setPlayingAnimationMessage(this.entity, BoofloEntity.INFLATE);
			return false;
		}
		return !this.entity.isOnGround() && this.isEndimationPlaying();
	}

	@Override
	public void startExecuting() {
		this.playEndimation();
	}

	@Override
	public void tick() {
		this.entity.getNavigator().clearPath();
		this.entity.setAIMoveSpeed(0.0F);

		this.entity.rotationPitch = 0.0F;
	}

	private boolean isEntityUnder() {
		for (LivingEntity entity : this.world.getEntitiesWithinAABB(LivingEntity.class, DetectionHelper.expandDownwards(this.entity.getBoundingBox().grow(1.0F), 12.0F))) {
			if (entity == this.entity.getBoofloAttackTarget()) {
				return true;
			}
		}
		return false;
	}

	private boolean isSolidUnderTarget() {
		boolean isSomewhatSolidUnder = false;
		BlockPos.Mutable mutable = this.entity.getBoofloAttackTarget().getPosition().toMutable();
		for (int y = 1; y < 4; y++) {
			isSomewhatSolidUnder = isSomewhatSolidUnder || this.entity.getBoofloAttackTarget() != null && Block.hasSolidSideOnTop(this.world, mutable.down(y));
		}
		return isSomewhatSolidUnder;
	}
}