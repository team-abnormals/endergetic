package com.minecraftabnormals.endergetic.common.entities.booflo.ai;

import java.util.EnumSet;

import com.teamabnormals.abnormals_core.core.utils.NetworkUtil;
import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity;
import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity.GroundMoveHelperController;
import com.minecraftabnormals.endergetic.core.registry.EEItems;

import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class BoofloTemptGoal extends Goal {
	private static final EntityPredicate SHOULD_FOLLOW = (new EntityPredicate()).setDistance(10.0D).allowFriendlyFire().allowInvulnerable();
	private BoofloEntity booflo;
	private PlayerEntity tempter;
	
	public BoofloTemptGoal(BoofloEntity booflo) {
		this.booflo = booflo;
		this.setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	@Override
	public boolean shouldExecute() {
		this.tempter = this.booflo.world.getClosestPlayer(SHOULD_FOLLOW, this.booflo);
		if (this.tempter == null) {
			return false;
		} else {
			return !this.booflo.isTamed() && !this.booflo.hasCaughtFruit() && !this.booflo.isInLove() && this.booflo.getMoveHelper() instanceof GroundMoveHelperController && !this.booflo.isBoofed() && this.booflo.isOnGround() && this.isTemptedBy(this.tempter.getHeldItemMainhand()) || this.isTemptedBy(this.tempter.getHeldItemOffhand());
		}
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		this.tempter = this.booflo.world.getClosestPlayer(SHOULD_FOLLOW, this.booflo);
		if (this.tempter == null) {
			return false;
		} else {
			return this.booflo.getMoveHelper() instanceof GroundMoveHelperController && !this.booflo.isTamed() && !this.booflo.isInLove() && !this.booflo.isBoofed();
		}
	}
	
	@Override
	public void tick() {
		if (this.booflo.hopDelay == 0 && this.booflo.isNoEndimationPlaying()) {
			NetworkUtil.setPlayingAnimationMessage(this.booflo, BoofloEntity.HOP);
		}
		
		if (this.booflo.getMoveHelper() instanceof GroundMoveHelperController) {
			((GroundMoveHelperController) this.booflo.getMoveHelper()).setSpeed(1.0D);
		}
		
		double dx = this.tempter.getPosX() - this.booflo.getPosX();
		double dz = this.tempter.getPosZ() - this.booflo.getPosZ();
		
		float angle = (float) (MathHelper.atan2(dz, dx) * (double) (180F / Math.PI)) - 90.0F;
		
		if (this.booflo.getMoveHelper() instanceof GroundMoveHelperController) {
			((GroundMoveHelperController) this.booflo.getMoveHelper()).setDirection(angle, false);
		}
		
		this.booflo.getNavigator().tryMoveToEntityLiving(this.booflo, 1.0D);
	}
	
	@Override
	public void resetTask() {
		this.tempter = null;
	}
	
	private boolean isTemptedBy(ItemStack stack) {
		return stack.getItem() == EEItems.BOLLOOM_FRUIT.get();
	}
}