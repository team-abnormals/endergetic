package com.minecraftabnormals.endergetic.common.entities.booflo.ai;

import com.teamabnormals.abnormals_core.core.library.endimator.EndimatedGoal;
import com.teamabnormals.abnormals_core.core.library.endimator.Endimation;
import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloAdolescentEntity;

import net.minecraft.block.Block;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class AdolescentEatGoal extends EndimatedGoal<BoofloAdolescentEntity> {
	private int eatingTicks;

	public AdolescentEatGoal(BoofloAdolescentEntity adolescent) {
		super(adolescent);
	}

	@Override
	public boolean shouldExecute() {
		if (this.entity.isPlayerNear()) {
			return false;
		}
		return this.entity.getRNG().nextInt(40) == 0 && this.entity.hasFruit() && this.isSafePos();
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		if (this.entity.isPlayerNear()) {
			return false;
		}
		
		if (this.entity.isDescenting()) {
			return this.isSafePos() && this.entity.hasFruit();
		} else if (this.entity.isEating()) {
			return this.entity.isOnGround() && this.entity.hasFruit() && this.eatingTicks < 61;
		}
		
		return false;
	}
	
	@Override
	public void startExecuting() {
		this.entity.setDescenting(true);
		this.entity.setAIMoveSpeed(0.0F);
		this.entity.getNavigator().clearPath();
	}
	
	@Override
	public void resetTask() {
		if (this.entity.isDescenting()) {
			this.entity.setDescenting(false);
		}

		if (this.entity.isEating()) {
			this.entity.setEating(false);
			this.entity.dropFruit();
			this.entity.resetEndimation();
		}

		this.eatingTicks = 0;
	}
	
	@Override
	public void tick() {
		this.entity.setAIMoveSpeed(0.0F);
		this.entity.getNavigator().clearPath();
		
		if (this.entity.isDescenting()) {
			if (this.entity.isOnGround()) {
				this.entity.setEating(true);
				this.entity.setDescenting(false);
			}
		} else if (this.entity.isEating()) {
			this.eatingTicks++;
			
			if (this.eatingTicks % 10 == 0) {
				this.playEndimation();
				if (this.eatingTicks < 60) {
					this.playEndimation();
				}
			}
			
			if (this.eatingTicks == 60) {
				this.entity.resetEndimation();
				this.entity.setHungry(false);
				this.entity.setHasFruit(false);
				this.entity.setEating(false);
				this.entity.setEaten(true);
			}
		}
	}

	private boolean isSafePos() {
		BlockPos pos = this.entity.getPosition();
		for (int i = 0; i < 10; i++) {
			pos = pos.down(i);
			if (Block.hasSolidSide(this.entity.world.getBlockState(pos), this.entity.world, pos, Direction.UP) && i >= 4) {
				if (this.entity.world.getBlockState(pos).getFluidState().isEmpty() && !this.entity.world.getBlockState(pos).isBurning(this.entity.world, pos)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	protected Endimation getEndimation() {
		return BoofloAdolescentEntity.EATING_ANIMATION;
	}
}