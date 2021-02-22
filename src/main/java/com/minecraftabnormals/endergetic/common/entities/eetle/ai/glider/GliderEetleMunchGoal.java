package com.minecraftabnormals.endergetic.common.entities.eetle.ai.glider;

import com.minecraftabnormals.abnormals_core.core.endimator.entity.EndimatedGoal;
import com.minecraftabnormals.endergetic.common.entities.eetle.GliderEetleEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;

public class GliderEetleMunchGoal extends EndimatedGoal<GliderEetleEntity> {
	private int munchCooldown;

	public GliderEetleMunchGoal(GliderEetleEntity glider) {
		super(glider, GliderEetleEntity.MUNCH);
	}

	@Override
	public boolean shouldExecute() {
		if (this.munchCooldown > 0) {
			this.munchCooldown--;
		} else if (this.entity.getRNG().nextFloat() < 0.05F) {
			LivingEntity attackTarget = this.entity.getAttackTarget();
			if (attackTarget != null) {
				return this.entity.getPassengers().contains(attackTarget) && this.entity.isFlying() && this.isNoEndimationPlaying();
			}
		}
		return false;
	}

	@Override
	public void startExecuting() {
		this.playEndimation();
	}

	@Override
	public boolean shouldContinueExecuting() {
		return this.isEndimationPlaying() && this.entity.isFlying();
	}

	@Override
	public void tick() {
		LivingEntity attackTarget = this.entity.getAttackTarget();
		if (attackTarget != null && this.entity.getPassengers().contains(attackTarget)) {
			if (this.isEndimationAtTick(8) || this.isEndimationAtTick(18)) {
				attackTarget.attackEntityFrom(causeMunchDamage(this.entity), (float) this.entity.getAttributeValue(Attributes.ATTACK_DAMAGE) + attackTarget.getRNG().nextInt(2));
			}
		}
	}

	@Override
	public void resetTask() {
		this.munchCooldown = this.entity.getRNG().nextInt(41) + 20;
	}

	public static DamageSource causeMunchDamage(GliderEetleEntity glider) {
		return new EntityDamageSource("endergetic.munch", glider);
	}
}
