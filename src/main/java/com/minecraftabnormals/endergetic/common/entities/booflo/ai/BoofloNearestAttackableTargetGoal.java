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
		this.setMutexFlags(EnumSet.of(Flag.TARGET));
		this.targetEntitySelector = EntityPredicate.DEFAULT.setDistance(this.getTargetDistance());
	}

	public boolean shouldExecute() {
		if (this.targetChance > 0 && this.goalOwner.getRNG().nextInt(this.targetChance) != 0) {
			return false;
		} else if (this.goalOwner instanceof BoofloAdolescentEntity && !((BoofloAdolescentEntity) this.goalOwner).isHungry()) {
			return false;
		} else if (this.goalOwner instanceof BoofloEntity && (((BoofloEntity) this.goalOwner).getBoofloAttackTarget() != null || !((BoofloEntity) this.goalOwner).isBoofed() || !((BoofloEntity) this.goalOwner).isHungry())) {
			return false;
		} else {
			if (this.goalOwner instanceof BoofloEntity && (((BoofloEntity) this.goalOwner).isTamed() && this.targetClass == PuffBugEntity.class)) {
				return false;
			}

			this.findNearestTarget();
			return this.nearestTarget != null;
		}
	}

	protected AxisAlignedBB getTargetableArea(double targetDistance) {
		return this.goalOwner.getBoundingBox().grow(targetDistance, 4.0D, targetDistance);
	}

	protected void findNearestTarget() {
		this.nearestTarget = this.findEntity(this.targetClass, this.targetEntitySelector, this.goalOwner, this.goalOwner.getPosX(), this.goalOwner.getPosY() + (double) this.goalOwner.getEyeHeight(), this.goalOwner.getPosZ(), this.getTargetableArea(this.getTargetDistance()));
	}

	public void startExecuting() {
		if (this.goalOwner instanceof BoofloEntity) {
			((BoofloEntity) this.goalOwner).setBoofloAttackTargetId(this.nearestTarget.getEntityId());
		} else {
			((BoofloAdolescentEntity) this.goalOwner).setBoofloAttackTarget(this.nearestTarget);
		}
		super.startExecuting();
	}

	@Nullable
	public E findEntity(Class<? extends E> target, EntityPredicate predicate, @Nullable LivingEntity attacker, double p_225318_4_, double p_225318_6_, double p_225318_8_, AxisAlignedBB bb) {
		return this.getClosestEntity(attacker.world.getEntitiesWithinAABB(target, bb, null), predicate, attacker, p_225318_4_, p_225318_6_, p_225318_8_);
	}

	@Nullable
	private E getClosestEntity(List<? extends E> p_217361_1_, EntityPredicate p_217361_2_, @Nullable LivingEntity attacker, double p_217361_4_, double p_217361_6_, double p_217361_8_) {
		double d0 = -1.0D;
		E e = null;

		for (E e1 : p_217361_1_) {
			if (this.canTarget(attacker, e1)) {
				double d1 = e1.getDistanceSq(p_217361_4_, p_217361_6_, p_217361_8_);
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
				if (!attacker.canAttack(target.getType())) {
					return false;
				}
				if (attacker.isOnSameTeam(target)) {
					return false;
				}

				if (this.getTargetDistance() > 0.0D) {
					double d1 = this.getTargetDistance();
					double d2 = attacker.getDistanceSq(target.getPosX(), target.getPosY(), target.getPosZ());
					if (d2 > d1 * d1) {
						return false;
					}
				}

				if (attacker instanceof MobEntity && !((MobEntity) attacker).getEntitySenses().canSee(target)) {
					return false;
				}
			}
			return true;
		}
	}
}