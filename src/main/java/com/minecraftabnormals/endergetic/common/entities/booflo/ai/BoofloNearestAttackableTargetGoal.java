package com.minecraftabnormals.endergetic.common.entities.booflo.ai;

import java.util.EnumSet;
import java.util.List;

import javax.annotation.Nullable;

import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloAdolescentEntity;
import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity;
import com.minecraftabnormals.endergetic.common.entities.puffbug.PuffBugEntity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.util.math.AxisAlignedBB;

import net.minecraft.entity.ai.goal.Goal.Flag;

public class BoofloNearestAttackableTargetGoal<E extends Entity> extends TargetGoal {
	protected final Class<E> targetClass;
	protected final int targetChance;
	protected Entity nearestTarget;
	protected EntityPredicate targetEntitySelector;

	public BoofloNearestAttackableTargetGoal(MobEntity attacker, Class<E> targetClass, boolean p_i50313_3_) {
		this(attacker, targetClass, p_i50313_3_, false);
	}

	public BoofloNearestAttackableTargetGoal(MobEntity attacker, Class<E> targetClass, boolean p_i50314_3_, boolean p_i50314_4_) {
		this(attacker, targetClass, 5, p_i50314_3_, p_i50314_4_);
	}

	public BoofloNearestAttackableTargetGoal(MobEntity p_i50315_1_, Class<E> p_i50315_2_, int p_i50315_3_, boolean p_i50315_4_, boolean p_i50315_5_) {
		super(p_i50315_1_, p_i50315_4_, p_i50315_5_);
		this.targetClass = p_i50315_2_;
		this.targetChance = p_i50315_3_;
		this.setFlags(EnumSet.of(Flag.TARGET));
		this.targetEntitySelector = EntityPredicate.DEFAULT.range(this.getFollowDistance());
	}

	public boolean canUse() {
		if (this.targetChance > 0 && this.mob.getRandom().nextInt(this.targetChance) != 0) {
			return false;
		} else if (this.mob instanceof BoofloAdolescentEntity && !((BoofloAdolescentEntity) this.mob).isHungry()) {
			return false;
		} else if (this.mob instanceof BoofloEntity && (((BoofloEntity) this.mob).getBoofloAttackTarget() != null || !((BoofloEntity) this.mob).isBoofed() || !((BoofloEntity) this.mob).isHungry())) {
			return false;
		} else {
			if (this.mob instanceof BoofloEntity && (((BoofloEntity) this.mob).isTamed() && this.targetClass == PuffBugEntity.class)) {
				return false;
			}

			this.findNearestTarget();
			return this.nearestTarget != null;
		}
	}

	protected AxisAlignedBB getTargetableArea(double targetDistance) {
		return this.mob.getBoundingBox().inflate(targetDistance, 4.0D, targetDistance);
	}

	protected void findNearestTarget() {
		this.nearestTarget = this.findEntity(this.targetClass, this.targetEntitySelector, this.mob, this.mob.getX(), this.mob.getY() + (double) this.mob.getEyeHeight(), this.mob.getZ(), this.getTargetableArea(this.getFollowDistance()));
	}

	public void start() {
		if (this.mob instanceof BoofloEntity) {
			((BoofloEntity) this.mob).setBoofloAttackTargetId(this.nearestTarget.getId());
		} else {
			((BoofloAdolescentEntity) this.mob).setBoofloAttackTarget(this.nearestTarget);
		}
		super.start();
	}

	@Nullable
	public E findEntity(Class<? extends E> target, EntityPredicate predicate, @Nullable LivingEntity attacker, double p_225318_4_, double p_225318_6_, double p_225318_8_, AxisAlignedBB bb) {
		return this.getClosestEntity(attacker.level.getEntitiesOfClass(target, bb, null), predicate, attacker, p_225318_4_, p_225318_6_, p_225318_8_);
	}

	@Nullable
	private E getClosestEntity(List<? extends E> p_217361_1_, EntityPredicate p_217361_2_, @Nullable LivingEntity attacker, double p_217361_4_, double p_217361_6_, double p_217361_8_) {
		double d0 = -1.0D;
		E e = null;

		for (E e1 : p_217361_1_) {
			if (this.canTarget(attacker, e1)) {
				double d1 = e1.distanceToSqr(p_217361_4_, p_217361_6_, p_217361_8_);
				if (d0 == -1.0D || d1 < d0) {
					d0 = d1;
					e = e1;
				}
			}
		}

		return e;
	}

	public boolean canTarget(@Nullable LivingEntity attacker, Entity target) {
		if (attacker == target) {
			return false;
		} else if (target.isSpectator()) {
			return false;
		} else if (!target.isAlive()) {
			return false;
		} else if (target.isInvulnerable()) {
			return false;
		} else {
			if (attacker != null) {
				if (!attacker.canAttackType(target.getType())) {
					return false;
				}
				if (attacker.isAlliedTo(target)) {
					return false;
				}

				if (this.getFollowDistance() > 0.0D) {
					double d1 = this.getFollowDistance();
					double d2 = attacker.distanceToSqr(target.getX(), target.getY(), target.getZ());
					if (d2 > d1 * d1) {
						return false;
					}
				}

				if (attacker instanceof MobEntity && !((MobEntity) attacker).getSensing().canSee(target)) {
					return false;
				}
			}
			return true;
		}
	}
}