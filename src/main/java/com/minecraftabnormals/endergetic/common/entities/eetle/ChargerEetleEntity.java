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
import net.minecraft.entity.monster.IFlinging;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

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

	public static AttributeModifierMap.MutableAttribute getAttributes() {
		return MobEntity.func_233666_p_()
				.createMutableAttribute(Attributes.ATTACK_DAMAGE, 6.0F)
				.createMutableAttribute(Attributes.ATTACK_KNOCKBACK, 1.0F)
				.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.2F)
				.createMutableAttribute(Attributes.ARMOR, 4.0F)
				.createMutableAttribute(Attributes.MAX_HEALTH, 30.0F)
				.createMutableAttribute(Attributes.FOLLOW_RANGE, 32.0F)
				.createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 0.2F);
	}

	@Override
	public void tick() {
		super.tick();

		if (!this.world.isRemote) {
			if (!this.isChild()) {
				if (this.isCatapultProjectile()) {
					LivingEntity attackTarget = this.getAttackTarget();
					if (attackTarget != null) {
						this.getNavigator().clearPath();
						this.getLookController().setLookPositionWithEntity(attackTarget, 30.0F, 30.0F);
					}
					if (this.isOnGround()) {
						this.catapultTimer = 0;
					} else this.catapultTimer--;
				}

				if (this.rand.nextFloat() < 0.005F && this.idleDelay <= 0 && this.getAttackTarget() == null && this.isNoEndimationPlaying()) {
					NetworkUtil.setPlayingAnimationMessage(this, FLAP);
					this.resetIdleFlapDelay();
				}
			}
		}
	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		compound.putInt("CatapultCooldown", this.catapultGoal.cooldown);
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		this.catapultGoal.cooldown = compound.getInt("CatapultCooldown");
	}

	@Override
	public boolean attackEntityAsMob(Entity target) {
		if (!(target instanceof LivingEntity)) {
			return false;
		} else {
			if (!this.world.isRemote) {
				NetworkUtil.setPlayingAnimationMessage(this, ATTACK);
			}
			return IFlinging.func_234403_a_(this, (LivingEntity) target);
		}
	}

	@Override
	protected void constructKnockBackVector(LivingEntity target) {
		if (!this.isChild()) {
			IFlinging.func_234404_b_(this, target);
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
		return new Endimation[]{
				ATTACK,
				CATAPULT,
				FLAP
		};
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
		double xDifference = target.getPosX() - this.getPosX();
		double zDifference = target.getPosZ() - this.getPosZ();
		double verticalOffset = MathHelper.sqrt(xDifference * xDifference + zDifference * zDifference) * 0.475F;
		Vector3d launchMotion = new Vector3d(xDifference, Math.max(0.0F, target.getPosYHeight(0.25F) - this.getPosY() + verticalOffset), zDifference).normalize().scale(1.325F);
		if (launchMotion.y > 0.9F) {
			launchMotion = new Vector3d(launchMotion.getX(), 0.9F, launchMotion.getZ());
		}
		this.setMotion(launchMotion);
	}
}
