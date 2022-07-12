package com.teamabnormals.endergetic.common.entities.booflo.ai;

import java.util.EnumSet;

import com.teamabnormals.endergetic.common.entities.booflo.BoofloEntity;
import com.teamabnormals.endergetic.common.entities.booflo.BoofloEntity.GroundMoveHelperController;
import com.teamabnormals.endergetic.core.registry.EEItems;

import com.teamabnormals.endergetic.core.registry.other.EEPlayableEndimations;
import com.teamabnormals.blueprint.core.util.NetworkUtil;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.Mth;

public class BoofloTemptGoal extends Goal {
	private static final TargetingConditions SHOULD_FOLLOW = TargetingConditions.forNonCombat().range(10.0D).ignoreLineOfSight();
	private final BoofloEntity booflo;
	private Player tempter;

	public BoofloTemptGoal(BoofloEntity booflo) {
		this.booflo = booflo;
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		this.tempter = this.booflo.level.getNearestPlayer(SHOULD_FOLLOW, this.booflo);
		if (this.tempter == null) {
			return false;
		} else {
			return !this.booflo.isTamed() && !this.booflo.hasCaughtFruit() && !this.booflo.isInLove() && this.booflo.getMoveControl() instanceof GroundMoveHelperController && !this.booflo.isBoofed() && this.booflo.isOnGround() && this.isTemptedBy(this.tempter.getMainHandItem()) || this.isTemptedBy(this.tempter.getOffhandItem());
		}
	}

	@Override
	public boolean canContinueToUse() {
		Player tempter = this.tempter = this.booflo.level.getNearestPlayer(SHOULD_FOLLOW, this.booflo);
		if (tempter == null) {
			return false;
		} else {
			return (this.isTemptedBy(tempter.getMainHandItem()) || this.isTemptedBy(tempter.getOffhandItem())) && this.booflo.getMoveControl() instanceof GroundMoveHelperController && !this.booflo.isTamed() && !this.booflo.isInLove() && !this.booflo.isBoofed();
		}
	}

	@Override
	public void tick() {
		if (this.booflo.hopDelay == 0 && this.booflo.isNoEndimationPlaying()) {
			NetworkUtil.setPlayingAnimation(this.booflo, EEPlayableEndimations.BOOFLO_HOP);
		}

		if (this.booflo.getMoveControl() instanceof GroundMoveHelperController) {
			((GroundMoveHelperController) this.booflo.getMoveControl()).setSpeed(1.0D);
		}

		double dx = this.tempter.getX() - this.booflo.getX();
		double dz = this.tempter.getZ() - this.booflo.getZ();

		float angle = (float) (Mth.atan2(dz, dx) * (180F / Math.PI)) - 90.0F;

		if (this.booflo.getMoveControl() instanceof GroundMoveHelperController) {
			((GroundMoveHelperController) this.booflo.getMoveControl()).setDirection(angle, false);
		}

		this.booflo.getNavigation().moveTo(this.booflo, 1.0D);
	}

	@Override
	public void stop() {
		this.tempter = null;
	}

	private boolean isTemptedBy(ItemStack stack) {
		return stack.getItem() == EEItems.BOLLOOM_FRUIT.get();
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}
}