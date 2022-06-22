package com.minecraftabnormals.endergetic.common.entities.eetle.ai.brood;

import com.minecraftabnormals.abnormals_core.core.endimator.entity.EndimatedGoal;
import com.minecraftabnormals.endergetic.client.particles.EEParticles;
import com.minecraftabnormals.endergetic.common.entities.eetle.AbstractEetleEntity;
import com.minecraftabnormals.endergetic.common.entities.eetle.BroodEetleEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.ai.goal.Goal.Flag;

public class BroodEetleSlamGoal extends EndimatedGoal<BroodEetleEntity> {

	public BroodEetleSlamGoal(BroodEetleEntity entity) {
		super(entity, BroodEetleEntity.SLAM);
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		BroodEetleEntity broodEetle = this.entity;
		if (broodEetle.isFiringCannon()) {
			return false;
		}
		return broodEetle.canSlam() && broodEetle.isOnGround() && (broodEetle.isNoEndimationPlaying() && BroodEetleFlingGoal.searchForNearbyAggressors(broodEetle, 3.0D).size() > 3) || broodEetle.shouldSlamWhenWakingUp();
	}

	@Override
	public void start() {
		this.playEndimation();
		BroodEetleEntity broodEetle = this.entity;
		broodEetle.wokenUpAggressively = false;
		broodEetle.resetSlamCooldown();
	}

	@Override
	public boolean canContinueToUse() {
		return this.isEndimationPlaying();
	}

	@Override
	public void tick() {
		if (this.isEndimationAtTick(14)) {
			slam(this.entity, this.random, 1.0F);
		}
	}

	public static void slam(BroodEetleEntity broodEetle, Random random, float power) {
		ServerWorld world = (ServerWorld) broodEetle.level;
		double posX = broodEetle.getX();
		double posY = broodEetle.getY();
		double posZ = broodEetle.getZ();
		for (BlockState state : sampleGround(world, broodEetle.blockPosition().below(), random)) {
			world.sendParticles(new BlockParticleData(EEParticles.FAST_BLOCK.get(), state), posX, posY, posZ, 8, 0.0D, 0.0D, 0.0D, 0.225F);
		}
		float attackDamage = (float) broodEetle.getAttributeValue(Attributes.ATTACK_DAMAGE) * power;
		double knockback = broodEetle.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
		for (LivingEntity livingEntity : world.getEntitiesOfClass(LivingEntity.class, broodEetle.getBoundingBox().inflate(4.5D), entity1 -> entity1 != broodEetle)) {
			float damage;
			if ((int) attackDamage > 0.0F) {
				damage = attackDamage / 2.0F + random.nextInt((int) attackDamage);
			} else {
				damage = attackDamage;
			}

			if (livingEntity instanceof AbstractEetleEntity) {
				damage = 0.0F;
			}

			if (livingEntity.hurt(DamageSource.mobAttack(broodEetle), damage)) {
				broodEetle.doEnchantDamageEffects(broodEetle, livingEntity);
				double knockbackForce = knockback - livingEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
				float inAirFactor = livingEntity.isOnGround() ? 1.0F : 0.75F;
				Vector3d horizontalVelocity = new Vector3d(livingEntity.getX() - posX, 0.0D, livingEntity.getZ() - posZ).normalize().scale((knockbackForce * (random.nextFloat() * 0.75F + 0.5F)) * inAirFactor * power);
				livingEntity.push(horizontalVelocity.x, knockbackForce * 0.5F * random.nextFloat() * 0.5F * inAirFactor, horizontalVelocity.z);
				livingEntity.hurtMarked = true;
			}
		}
	}

	@SuppressWarnings("deprecation")
	private static List<BlockState> sampleGround(World world, BlockPos groundPos, Random random) {
		List<BlockState> list = new ArrayList<>();
		int originX = groundPos.getX();
		int originY = groundPos.getY();
		int originZ = groundPos.getZ();
		BlockPos.Mutable mutable = groundPos.mutable();
		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z <= 1; z++) {
				BlockState state = world.getBlockState(mutable.set(originX + x, originY, originZ + z));
				if (!state.isAir()) {
					list.add(state);
				}
			}
		}
		if (!list.isEmpty()) {
			while (list.size() < 9) {
				list.add(list.get(random.nextInt(list.size())));
			}
		}
		return list;
	}

}
