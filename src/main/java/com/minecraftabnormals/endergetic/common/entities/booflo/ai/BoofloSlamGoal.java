package com.minecraftabnormals.endergetic.common.entities.booflo.ai;

import com.teamabnormals.abnormals_core.core.library.endimator.EndimatedGoal;
import com.teamabnormals.abnormals_core.core.library.endimator.Endimation;
import com.teamabnormals.abnormals_core.core.utils.NetworkUtil;
import com.minecraftabnormals.endergetic.api.entity.util.DetectionHelper;
import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity;

import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Direction;
import net.minecraft.world.World;

public class BoofloSlamGoal extends EndimatedGoal<BoofloEntity> {
	private World world;
	
	public BoofloSlamGoal(BoofloEntity booflo) {
		super(booflo);
		this.world = booflo.world;
	}

	@Override
	public boolean shouldExecute() {
		return this.entity.getPassengers().isEmpty() && this.entity.isEndimationPlaying(BoofloEntity.SWIM) && !this.entity.func_233570_aj_() && (this.entity.hasAggressiveAttackTarget()) && this.isEntityUnder() && this.isSolidUnderTarget();
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		if (!this.isSolidUnderTarget()) {
			NetworkUtil.setPlayingAnimationMessage(this.entity, BoofloEntity.INFLATE);
			return false;
		}
		return !this.entity.func_233570_aj_() && this.entity.hasAggressiveAttackTarget() && this.isEndimationPlaying();
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
		for (int y = 1; y < 4; y++) {
			isSomewhatSolidUnder = !isSomewhatSolidUnder ? this.entity.getBoofloAttackTarget() != null && Block.hasSolidSide(this.world.getBlockState(this.entity.getBoofloAttackTarget().func_233580_cy_().down(y)), this.world, this.entity.getBoofloAttackTarget().func_233580_cy_().down(y), Direction.UP) : true;
		}
		return isSomewhatSolidUnder;
	}

	@Override
	protected Endimation getEndimation() {
		return BoofloEntity.CHARGE;
	}
}