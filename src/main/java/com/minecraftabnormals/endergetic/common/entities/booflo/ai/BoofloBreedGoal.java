package com.minecraftabnormals.endergetic.common.entities.booflo.ai;

import java.util.EnumSet;
import java.util.List;

import javax.annotation.Nullable;

import com.minecraftabnormals.abnormals_core.core.util.NetworkUtil;
import com.minecraftabnormals.endergetic.common.advancement.EECriteriaTriggers;
import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity;
import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity.GroundMoveHelperController;

import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.stats.Stats;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameRules;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;

import net.minecraft.entity.ai.goal.Goal.Flag;

public class BoofloBreedGoal extends Goal {
	private static final EntityPredicate MATE_CHECKER = (new EntityPredicate()).range(16.0D).allowInvulnerable().allowSameTeam().allowUnseeable();
	protected final BoofloEntity booflo;
	protected BoofloEntity mate;
	private int impregnateDelay;

	public BoofloBreedGoal(BoofloEntity booflo) {
		this.booflo = booflo;
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		if (this.booflo.isBoofed() || (!this.booflo.isOnGround() && this.booflo.getVehicle() == null) || !this.booflo.isInLove() || this.booflo.isPregnant()) {
			return false;
		} else {
			this.mate = this.getNearbyMate();
			return this.mate != null && !this.mate.isPregnant();
		}
	}

	@Override
	public boolean canContinueToUse() {
		return !this.booflo.isBoofed() && this.mate.isAlive() && this.mate.isInLove() && this.impregnateDelay < 100;
	}

	public void stop() {
		this.mate = null;
		this.impregnateDelay = 0;
	}

	public void tick() {
		if (this.booflo.hopDelay == 0 && this.booflo.isNoEndimationPlaying() && !this.isBeingRidenOrRiding()) {
			NetworkUtil.setPlayingAnimationMessage(this.booflo, BoofloEntity.HOP);
		}

		if (this.booflo.getMoveControl() instanceof GroundMoveHelperController && !this.isBeingRidenOrRiding()) {
			((GroundMoveHelperController) this.booflo.getMoveControl()).setSpeed(0.1D);
		}

		double dx = this.mate.getX() - this.booflo.getX();
		double dz = this.mate.getZ() - this.booflo.getZ();

		float angle = (float) (MathHelper.atan2(dz, dx) * (double) (180F / Math.PI)) - 90.0F;

		if (this.booflo.getMoveControl() instanceof GroundMoveHelperController && !this.isBeingRidenOrRiding()) {
			((GroundMoveHelperController) this.booflo.getMoveControl()).setDirection(angle, false);
		}

		this.booflo.getNavigation().moveTo(this.mate, 1.0D);

		this.impregnateDelay++;
		if (this.impregnateDelay >= 60 && this.booflo.distanceToSqr(this.mate) < 10.0D) {
			this.impregnateBooflo();
		}
	}

	protected void impregnateBooflo() {
		final BabyEntitySpawnEvent event = new net.minecraftforge.event.entity.living.BabyEntitySpawnEvent(this.booflo, this.mate, null);
		final boolean cancelled = MinecraftForge.EVENT_BUS.post(event);
		if (cancelled) {
			this.booflo.resetInLove();
			this.mate.resetInLove();
			return;
		}

		ServerPlayerEntity serverplayerentity = this.booflo.getLoveCause();
		if (serverplayerentity == null && this.mate.getLoveCause() != null) {
			serverplayerentity = this.mate.getLoveCause();
		}

		if (serverplayerentity != null) {
			serverplayerentity.awardStat(Stats.ANIMALS_BRED);
			EECriteriaTriggers.BRED_BOOFLO.trigger(serverplayerentity);
		}

		if (!this.mate.isPregnant()) {
			this.booflo.setPregnant(true);
		}

		this.booflo.resetInLove();
		this.mate.resetInLove();
		this.booflo.breedDelay = 1400;
		this.mate.breedDelay = 1400;

		this.booflo.level.broadcastEntityEvent(this.booflo, (byte) 18);
		if (this.booflo.level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
			this.booflo.level.addFreshEntity(new ExperienceOrbEntity(this.booflo.level, this.booflo.getX(), this.booflo.getY(), this.booflo.getZ(), this.booflo.getRandom().nextInt(7) + 1));
		}
	}

	@Nullable
	private BoofloEntity getNearbyMate() {
		List<BoofloEntity> list = this.booflo.level.getNearbyEntities(BoofloEntity.class, MATE_CHECKER, this.booflo, this.booflo.getBoundingBox().inflate(16.0D));
		double d0 = Double.MAX_VALUE;
		BoofloEntity booflo = null;

		for (BoofloEntity booflos : list) {
			if (this.booflo.canMateWith(booflos) && this.booflo.distanceToSqr(booflos) < d0) {
				booflo = booflos;
				d0 = this.booflo.distanceToSqr(booflos);
			}
		}

		return booflo;
	}

	private boolean isBeingRidenOrRiding() {
		return this.booflo.isPassenger() || this.booflo.isVehicle();
	}
}