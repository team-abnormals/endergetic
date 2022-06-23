package com.minecraftabnormals.endergetic.common.entities.booflo.ai;

import java.util.EnumSet;

import com.minecraftabnormals.abnormals_core.core.endimator.entity.EndimatedGoal;
import com.minecraftabnormals.abnormals_core.core.util.NetworkUtil;
import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity;
import com.minecraftabnormals.endergetic.core.registry.EEItems;

import net.minecraft.world.entity.player.Player;
import net.minecraft.util.Mth;

import net.minecraft.world.entity.ai.goal.Goal.Flag;

public class BoofloEatFruitGoal extends EndimatedGoal<BoofloEntity> {
	protected float originalYaw;
	private int soundDelay = 0;

	public BoofloEatFruitGoal(BoofloEntity booflo) {
		super(booflo, BoofloEntity.EAT);
		this.setFlags(EnumSet.of(Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		if (this.entity.isPlayerNear(1.0F)) {
			if (!this.entity.isTamed()) {
				return false;
			}
		}
		return this.entity.isNoEndimationPlaying() && this.entity.hasCaughtFruit() && !this.entity.isBoofed() && this.entity.isOnGround() && !this.entity.isInLove();
	}

	@Override
	public boolean canContinueToUse() {
		boolean flag = true;
		if (!this.entity.hasCaughtFruit()) {
			if (this.entity.getAnimationTick() < 140) {
				flag = false;
			}
		}

		if (this.entity.isPlayerNear(0.6F)) {
			if (!this.entity.isTamed()) {
				this.entity.hopDelay = 0;
				for (Player players : this.entity.getNearbyPlayers(0.6F)) {
					if (!this.entity.hasAggressiveAttackTarget()) {
						this.entity.setBoofloAttackTargetId(players.getId());
					}
				}
				return false;
			}
		}
		return this.isEndimationPlaying() && flag && !this.entity.isBoofed() && this.entity.isOnGround();
	}

	@Override
	public void start() {
		this.playEndimation();
		this.originalYaw = this.entity.yRot;
	}

	@Override
	public void stop() {
		this.originalYaw = 0;
		if (this.entity.hasCaughtFruit()) {
			this.entity.setCaughtFruit(false);
			this.entity.spawnAtLocation(EEItems.BOLLOOM_FRUIT.get());
		}
		NetworkUtil.setPlayingAnimationMessage(this.entity, BoofloEntity.BLANK_ANIMATION);
	}

	@Override
	public void tick() {
		if (this.soundDelay > 0) this.soundDelay--;

		this.entity.yRot = this.originalYaw;
		this.entity.yRotO = this.originalYaw;

		if (this.entity.isPlayerNear(1.0F) && this.soundDelay == 0) {
			if (!this.entity.isTamed()) {
				this.entity.playSound(this.entity.getGrowlSound(), 0.75F, (float) Mth.clamp(this.entity.getRandom().nextFloat() * 1.0, 0.95F, 1.0F));
				this.soundDelay = 50;
			}
		}
	}
}