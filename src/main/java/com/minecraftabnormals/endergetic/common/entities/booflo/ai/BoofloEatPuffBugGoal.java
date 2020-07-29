package com.minecraftabnormals.endergetic.common.entities.booflo.ai;

import com.teamabnormals.abnormals_core.core.utils.NetworkUtil;
import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;

public class BoofloEatPuffBugGoal extends Goal {
	private BoofloEntity booflo;
	private float originalYaw;
	private int soundDelay = 0;

	public BoofloEatPuffBugGoal(BoofloEntity booflo) {
		this.booflo = booflo;
	}

	@Override
	public boolean shouldExecute() {
		if(this.booflo.isPlayerNear(1.0F)) {
			if(!this.booflo.isTamed()) {
				return false;
			}
		}
		return this.booflo.isNoEndimationPlaying() && this.booflo.hasCaughtPuffBug() && !this.booflo.isBoofed() && this.booflo.isOnGround() && !this.booflo.isInLove();
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		boolean flag = true;
		if(!this.booflo.hasCaughtPuffBug()) {
			if(this.booflo.getAnimationTick() < 140) {
				flag = false;
			}
		}
		
		if(this.booflo.isPlayerNear(0.6F)) {
			if(!this.booflo.isTamed()) {
				this.booflo.hopDelay = 0;
				for(PlayerEntity players : this.booflo.getNearbyPlayers(0.6F)) {
					if(!this.booflo.hasAggressiveAttackTarget()) {
						this.booflo.setBoofloAttackTargetId(players.getEntityId());
					}
				}
				return false;
			}
		}
		return this.booflo.isEndimationPlaying(BoofloEntity.EAT) && flag && !this.booflo.isBoofed() && this.booflo.isOnGround();
	}
	
	@Override
	public void startExecuting() {
		NetworkUtil.setPlayingAnimationMessage(this.booflo, BoofloEntity.EAT);
		this.originalYaw = this.booflo.rotationYaw;
		this.booflo.setLockedYaw(this.originalYaw);
	}
	
	@Override
	public void resetTask() {
		this.originalYaw = 0;
		NetworkUtil.setPlayingAnimationMessage(this.booflo, BoofloEntity.BLANK_ANIMATION);
	}

	@Override
	public void tick() {
		if(this.soundDelay > 0) this.soundDelay--;
		
		this.booflo.rotationYaw = this.originalYaw;
		this.booflo.prevRotationYaw = this.originalYaw;
		
		if(this.booflo.isPlayerNear(1.0F) && this.soundDelay == 0) {
			if(!this.booflo.isTamed()) {
				this.booflo.playSound(this.booflo.getGrowlSound(), 0.75F, (float) MathHelper.clamp(this.booflo.getRNG().nextFloat() * 1.0, 0.95F, 1.0F));
				this.soundDelay = 50;
			}
		}
	}
}