package com.teamabnormals.endergetic.common.entities.eetle;

import com.teamabnormals.endergetic.common.entities.eetle.ai.charger.EetleCatapultGoal;
import com.teamabnormals.endergetic.common.entities.eetle.ai.charger.EetleMeleeAttackGoal;
import com.teamabnormals.endergetic.core.registry.other.EEPlayableEndimations;
import com.teamabnormals.blueprint.core.util.NetworkUtil;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class ChargerEetleEntity extends AbstractEetleEntity {
	private EetleMeleeAttackGoal meleeAttackGoal;
	private EetleCatapultGoal catapultGoal;
	@Nullable
	private ChargerEetleEntity catapultingTarget;
	private int catapultTimer;

	public ChargerEetleEntity(EntityType<? extends AbstractEetleEntity> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.catapultGoal = new EetleCatapultGoal(this);
		this.meleeAttackGoal = new EetleMeleeAttackGoal(this);
		this.goalSelector.addGoal(1, this.catapultGoal);
		this.goalSelector.addGoal(2, this.meleeAttackGoal);
		this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8D));
		this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
		this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Mob.class, 8.0F));
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Mob.createMobAttributes()
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
					NetworkUtil.setPlayingAnimation(this, EEPlayableEndimations.CHARGER_EETLE_FLAP);
					this.resetIdleFlapDelay();
				}
			}
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt("CatapultCooldown", this.catapultGoal.cooldown);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.catapultGoal.cooldown = compound.getInt("CatapultCooldown");
	}

	@Override
	public boolean doHurtTarget(Entity target) {
		if (!(target instanceof LivingEntity)) {
			return false;
		} else {
			if (!this.level.isClientSide) {
				NetworkUtil.setPlayingAnimation(this, EEPlayableEndimations.CHARGER_EETLE_ATTACK);
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
				RandomSource random = this.level.random;
				double scale = knockbackForce * (random.nextFloat() * 1.0F + 0.5F);
				Vec3 horizontalVelocity = new Vec3(target.getX() - this.getX(), 0.0D, target.getZ() - this.getZ()).normalize().scale(scale);
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
		double verticalOffset = Mth.sqrt((float) (xDifference * xDifference + zDifference * zDifference)) * 0.475F;
		Vec3 launchMotion = new Vec3(xDifference, Math.max(0.0F, target.getY(0.25F) - this.getY() + verticalOffset), zDifference).normalize().scale(1.325F);
		if (launchMotion.y > 0.9F) {
			launchMotion = new Vec3(launchMotion.x(), 0.9F, launchMotion.z());
		}
		this.setDeltaMovement(launchMotion);
	}
}
