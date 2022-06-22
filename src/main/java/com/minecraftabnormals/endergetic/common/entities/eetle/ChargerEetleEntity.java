package com.minecraftabnormals.endergetic.common.entities.eetle;

import com.minecraftabnormals.abnormals_core.core.endimator.Endimation;
import com.minecraftabnormals.abnormals_core.core.util.NetworkUtil;
import com.minecraftabnormals.endergetic.common.entities.eetle.ai.charger.EetleCatapultGoal;
import com.minecraftabnormals.endergetic.common.entities.eetle.ai.charger.EetleMeleeAttackGoal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class ChargerEetleEntity extends AbstractEetleEntity {
	public static final Endimation ATTACK = new Endimation(10);
	public static final Endimation CATAPULT = new Endimation(16);
	public static final Endimation FLAP = new Endimation(20);
	private EetleMeleeAttackGoal meleeAttackGoal;
	private EetleCatapultGoal catapultGoal;
	@Nullable
	private ChargerEetleEntity catapultingTarget;
	private int catapultTimer;

	public ChargerEetleEntity(EntityType<? extends AbstractEetleEntity> type, World worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.catapultGoal = new EetleCatapultGoal(this);
		this.meleeAttackGoal = new EetleMeleeAttackGoal(this);
		this.goalSelector.addGoal(1, this.catapultGoal);
		this.goalSelector.addGoal(2, this.meleeAttackGoal);
		this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 0.8D));
		this.goalSelector.addGoal(6, new LookRandomlyGoal(this));
		this.goalSelector.addGoal(8, new LookAtGoal(this, MobEntity.class, 8.0F));
	}

	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return MobEntity.createMobAttributes()
				.add(Attributes.ATTACK_DAMAGE, 6.0F)
				.add(Attributes.ATTACK_KNOCKBACK, 1.0F)
				.add(Attributes.MOVEMENT_SPEED, 0.2F)
				.add(Attributes.ARMOR, 4.0F)
				.add(Attributes.MAX_HEALTH, 30.0F)
				.add(Attributes.FOLLOW_RANGE, 32.0F)
				.add(Attributes.KNOCKBACK_RESISTANCE, 0.2F);
	}

	@Override
	public void tick() {
		super.tick();

		if (!this.level.isClientSide) {
			if (!this.isBaby()) {
				if (this.isCatapultProjectile()) {
					LivingEntity attackTarget = this.getTarget();
					if (attackTarget != null) {
						this.getNavigation().stop();
						this.getLookControl().setLookAt(attackTarget, 30.0F, 30.0F);
					}
					if (this.isOnGround()) {
						this.catapultTimer = 0;
					} else this.catapultTimer--;
				}

				if (this.random.nextFloat() < 0.005F && this.idleDelay <= 0 && this.getTarget() == null && this.isNoEndimationPlaying()) {
					NetworkUtil.setPlayingAnimationMessage(this, FLAP);
					this.resetIdleFlapDelay();
				}
			}
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundNBT compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt("CatapultCooldown", this.catapultGoal.cooldown);
	}

	@Override
	public void readAdditionalSaveData(CompoundNBT compound) {
		super.readAdditionalSaveData(compound);
		this.catapultGoal.cooldown = compound.getInt("CatapultCooldown");
	}

	@Override
	public boolean doHurtTarget(Entity target) {
		if (!(target instanceof LivingEntity)) {
			return false;
		} else {
			if (!this.level.isClientSide) {
				NetworkUtil.setPlayingAnimationMessage(this, ATTACK);
			}
			float attackDamage = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
			float damage;
			if ((int) attackDamage > 0.0F) {
				damage = attackDamage / 2.0F + this.random.nextInt((int) attackDamage);
			} else {
				damage = attackDamage;
			}

			boolean attacked = target.hurt(DamageSource.mobAttack(this), damage);
			if (attacked) {
				this.doEnchantDamageEffects(this, target);
				this.blockedByShield((LivingEntity) target);
			}
			return attacked;
		}
	}

	@Override
	protected void blockedByShield(LivingEntity target) {
		if (!this.isBaby()) {
			double knockbackForce = this.getAttributeValue(Attributes.ATTACK_KNOCKBACK) - target.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
			if (knockbackForce > 0.0D) {
				Random random = this.level.random;
				double scale = knockbackForce * (random.nextFloat() * 1.0F + 0.5F);
				Vector3d horizontalVelocity = new Vector3d(target.getX() - this.getX(), 0.0D, target.getZ() - this.getZ()).normalize().scale(scale);
				target.push(horizontalVelocity.x, knockbackForce * (random.nextFloat() * 0.05F), horizontalVelocity.z);
				target.hurtMarked = true;
			}
		}
	}

	@Override
	protected void updateGoals(GoalSelector goalSelector, GoalSelector targetSelector, boolean child) {
		super.updateGoals(goalSelector, targetSelector, child);
		if (child) {
			goalSelector.removeGoal(this.catapultGoal);
			goalSelector.removeGoal(this.meleeAttackGoal);
		} else {
			goalSelector.addGoal(1, this.catapultGoal);
			goalSelector.addGoal(2, this.meleeAttackGoal);
		}
	}

	@Override
	public Endimation[] getEndimations() {
		return new Endimation[]{ATTACK, CATAPULT, FLAP, GROW_UP};
	}

	@Nullable
	public ChargerEetleEntity getCatapultingTarget() {
		return this.catapultingTarget;
	}

	public void setCatapultingTarget(@Nullable ChargerEetleEntity catapultingTarget) {
		this.catapultingTarget = catapultingTarget;
	}

	public boolean isCatapulting() {
		return this.catapultingTarget != null;
	}

	public boolean isCatapultProjectile() {
		return this.catapultTimer > 0;
	}

	public void launchFromCatapult(LivingEntity target) {
		this.catapultGoal.resetCooldown();
		this.catapultTimer = 25;
		double xDifference = target.getX() - this.getX();
		double zDifference = target.getZ() - this.getZ();
		double verticalOffset = MathHelper.sqrt(xDifference * xDifference + zDifference * zDifference) * 0.475F;
		Vector3d launchMotion = new Vector3d(xDifference, Math.max(0.0F, target.getY(0.25F) - this.getY() + verticalOffset), zDifference).normalize().scale(1.325F);
		if (launchMotion.y > 0.9F) {
			launchMotion = new Vector3d(launchMotion.x(), 0.9F, launchMotion.z());
		}
		this.setDeltaMovement(launchMotion);
	}
}
