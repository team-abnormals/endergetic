package com.minecraftabnormals.endergetic.common.entities.eetle;

import com.minecraftabnormals.abnormals_core.core.endimator.Endimation;
import com.minecraftabnormals.abnormals_core.core.endimator.entity.IEndimatedEntity;
import com.minecraftabnormals.abnormals_core.core.util.NetworkUtil;
import com.minecraftabnormals.endergetic.common.entities.eetle.ai.brood.*;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class BroodEetleEntity extends MonsterEntity implements IEndimatedEntity {
	public static final Endimation FLAP = new Endimation(22);
	public static final Endimation MUNCH = new Endimation(25);
	public static final Endimation ATTACK = new Endimation(12);
	public static final Endimation SLAM = new Endimation(20);
	private final Set<LivingEntity> revengeTargets = new HashSet<>();
	private Endimation endimation = BLANK_ANIMATION;
	private int animationTick;
	private int idleDelay;
	private int slamCooldown;

	public BroodEetleEntity(EntityType<? extends BroodEetleEntity> type, World world) {
		super(type, world);
		this.experienceValue = 50;
		this.resetIdleFlapDelay();
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(4, new BroodEetleSlamGoal(this));
		this.goalSelector.addGoal(5, new BroodEetleFlingGoal(this));
		this.goalSelector.addGoal(6, new LookRandomlyGoal(this));
		this.goalSelector.addGoal(8, new LookAtGoal(this, AbstractEetleEntity.class, 8.0F));
		this.goalSelector.addGoal(1, new BroodEetleHurtByTargetGoal(this));
	}

	@Override
	public void tick() {
		super.tick();
		this.endimateTick();

		if (!this.world.isRemote) {
			if (this.idleDelay > 0) this.idleDelay--;
			if (this.slamCooldown > 0) this.slamCooldown--;

			if (this.rand.nextFloat() < 0.005F && this.idleDelay <= 0 && this.isOnGround() && this.getAttackTarget() == null && this.isNoEndimationPlaying()) {
				NetworkUtil.setPlayingAnimationMessage(this, this.rand.nextFloat() < 0.6F ? FLAP : MUNCH);
				this.resetIdleFlapDelay();
			}
		}
	}

	@Override
	public boolean canBePushed() {
		return false;
	}

	@Override
	protected void collideWithEntity(Entity collider) {
		if (!this.isRidingSameEntity(collider)) {
			if (!collider.noClip && !this.noClip) {
				double d0 = collider.getPosX() - this.getPosX();
				double d1 = collider.getPosZ() - this.getPosZ();
				double d2 = MathHelper.absMax(d0, d1);
				if (d2 >= 0.01D) {
					d2 = MathHelper.sqrt(d2);
					d0 = d0 / d2;
					d1 = d1 / d2;
					double d3 = 1.0D / d2;
					if (d3 > 1.0D) {
						d3 = 1.0D;
					}

					d0 = d0 * d3;
					d1 = d1 * d3;
					d0 = d0 * 0.05D;
					d1 = d1 * 0.05D;
					d0 = d0 * (1.0D - this.entityCollisionReduction);
					d1 = d1 * (1.0D - this.entityCollisionReduction);
					if (!this.isBeingRidden()) {
						this.addVelocity(-d0 * 0.25F, 0.0D, -d1 * 0.25F);
					}

					if (!collider.isBeingRidden()) {
						collider.addVelocity(d0 * 2.0F, 0.0D, d1 * 2.0F);
					}
				}
			}
		}
	}

	@Override
	public void checkDespawn() {
		if (this.world.getDifficulty() == Difficulty.PEACEFUL && this.isDespawnPeaceful()) {
			this.remove();
		} else {
			this.idleTime = 0;
		}
	}

	@Override
	public boolean onLivingFall(float distance, float damageMultiplier) {
		return false;
	}

	@Override
	public boolean isWaterSensitive() {
		return true;
	}

	@Override
	public CreatureAttribute getCreatureAttribute() {
		return CreatureAttribute.ARTHROPOD;
	}

	@Override
	public boolean attackEntityAsMob(Entity target) {
		if (!(target instanceof LivingEntity)) {
			return false;
		} else {
			if (!this.world.isRemote) {
				NetworkUtil.setPlayingAnimationMessage(this, ATTACK);
			}
			float attackDamage = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
			float damage;
			if ((int) attackDamage > 0.0F) {
				damage = attackDamage / 2.0F + this.rand.nextInt((int) attackDamage);
			} else {
				damage = attackDamage;
			}

			boolean attacked = target.attackEntityFrom(DamageSource.causeMobDamage(this), damage);
			if (attacked) {
				this.applyEnchantments(this, target);
				this.constructKnockBackVector((LivingEntity) target);
			}
			return attacked;
		}
	}

	@Override
	protected void constructKnockBackVector(LivingEntity target) {
		double knockbackForce = this.getAttributeValue(Attributes.ATTACK_KNOCKBACK) - target.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
		if (knockbackForce > 0.0D) {
			Random random = this.world.rand;
			double scale = knockbackForce * (random.nextFloat() * 0.5F + 0.5F);
			Vector3d horizontalVelocity = new Vector3d(target.getPosX() - this.getPosX(), 0.0D, target.getPosZ() - this.getPosZ()).normalize().scale(scale);
			target.addVelocity(horizontalVelocity.x, knockbackForce * 0.5F * random.nextFloat() * 0.5F, horizontalVelocity.z);
			target.velocityChanged = true;
		}
	}

	public void resetIdleFlapDelay() {
		this.idleDelay = this.rand.nextInt(41) + 25;
	}

	public void resetSlamCooldown() {
		this.slamCooldown = this.rand.nextInt(21) + 60;
	}

	public boolean canSlam() {
		return this.slamCooldown <= 0;
	}

	public void addRevengeTarget(LivingEntity target) {
		this.revengeTargets.add(target);
	}

	public boolean isAnAggressor(LivingEntity entity) {
		return this.revengeTargets.contains(entity);
	}

	@Override
	public Endimation[] getEndimations() {
		return new Endimation[] {
				FLAP, MUNCH, ATTACK, SLAM
		};
	}

	@Override
	public Endimation getPlayingEndimation() {
		return this.endimation;
	}

	@Override
	public int getAnimationTick() {
		return this.animationTick;
	}

	@Override
	public void setAnimationTick(int animationTick) {
		this.animationTick = animationTick;
	}

	@Override
	public void setPlayingEndimation(Endimation endimation) {
		this.onEndimationEnd(this.endimation);
		this.endimation = endimation;
		this.setAnimationTick(0);
	}
}
