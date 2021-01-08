package com.minecraftabnormals.endergetic.common.entities.eetle;

import com.minecraftabnormals.abnormals_core.core.endimator.Endimation;
import com.minecraftabnormals.abnormals_core.core.util.NetworkUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.IFlinging;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class ChargerEetleEntity extends AbstractEetleEntity {
	public static final Endimation ATTACK = new Endimation(10);
	public static final Endimation FLAP = new Endimation(20);
	private int flapDelay;

	public ChargerEetleEntity(EntityType<? extends AbstractEetleEntity> type, World worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.25F, false));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 0.8D));
		this.goalSelector.addGoal(6, new LookRandomlyGoal(this));
		this.goalSelector.addGoal(8, new LookAtGoal(this, MobEntity.class, 8.0F));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
	}

	@Override
	public void tick() {
		super.tick();

		if (!this.world.isRemote) {
			if (!this.isChild()) {
				if (this.flapDelay > 0) this.flapDelay--;

				if (this.rand.nextFloat() < 0.005F && this.flapDelay <= 0 && this.getAttackTarget() == null && this.isNoEndimationPlaying()) {
					NetworkUtil.setPlayingAnimationMessage(this, FLAP);
					this.flapDelay = this.rand.nextInt(41) + 25;
				}
			}
		}
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
	public Endimation[] getEndimations() {
		return new Endimation[]{
				ATTACK,
				FLAP
		};
	}
}
