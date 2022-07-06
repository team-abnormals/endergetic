package com.minecraftabnormals.endergetic.common.entities.eetle.ai.brood;

import com.minecraftabnormals.endergetic.client.particles.EEParticles;
import com.minecraftabnormals.endergetic.common.entities.eetle.AbstractEetleEntity;
import com.minecraftabnormals.endergetic.common.entities.eetle.BroodEetleEntity;
import com.minecraftabnormals.endergetic.core.registry.other.EEPlayableEndimations;
import com.teamabnormals.blueprint.core.endimator.entity.EndimatedGoal;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class BroodEetleSlamGoal extends EndimatedGoal<BroodEetleEntity> {

	public BroodEetleSlamGoal(BroodEetleEntity entity) {
		super(entity, EEPlayableEndimations.BROOD_EETLE_SLAM);
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
			slam(this.entity, this.entity.getRandom(), 1.0F);
		}
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}

	public static void slam(BroodEetleEntity broodEetle, RandomSource random, float power) {
		ServerLevel world = (ServerLevel) broodEetle.level;
		double posX = broodEetle.getX();
		double posY = broodEetle.getY();
		double posZ = broodEetle.getZ();
		for (BlockState state : sampleGround(world, broodEetle.blockPosition().below(), random)) {
			world.sendParticles(new BlockParticleOption(EEParticles.FAST_BLOCK.get(), state), posX, posY, posZ, 8, 0.0D, 0.0D, 0.0D, 0.225F);
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
				Vec3 horizontalVelocity = new Vec3(livingEntity.getX() - posX, 0.0D, livingEntity.getZ() - posZ).normalize().scale((knockbackForce * (random.nextFloat() * 0.75F + 0.5F)) * inAirFactor * power);
				livingEntity.push(horizontalVelocity.x, knockbackForce * 0.5F * random.nextFloat() * 0.5F * inAirFactor, horizontalVelocity.z);
				livingEntity.hurtMarked = true;
			}
		}
	}

	private static List<BlockState> sampleGround(Level world, BlockPos groundPos, RandomSource random) {
		List<BlockState> list = new ArrayList<>();
		int originX = groundPos.getX();
		int originY = groundPos.getY();
		int originZ = groundPos.getZ();
		BlockPos.MutableBlockPos mutable = groundPos.mutable();
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
