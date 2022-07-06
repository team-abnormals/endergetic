package com.minecraftabnormals.endergetic.common.entities.purpoid.ai;

import com.minecraftabnormals.endergetic.common.entities.purpoid.PurpoidEntity;
import com.minecraftabnormals.endergetic.common.entities.purpoid.PurpoidSize;
import com.minecraftabnormals.endergetic.common.network.entity.S2CEnablePurpoidFlash;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import com.minecraftabnormals.endergetic.core.registry.other.EEPlayableEndimations;
import com.teamabnormals.blueprint.core.util.NetworkUtil;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class PurpoidAttackGoal extends Goal {
	private final PurpoidEntity purpoid;
	@Nullable
	private Path path;
	private int delayCounter;

	public PurpoidAttackGoal(PurpoidEntity purpoid) {
		this.purpoid = purpoid;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		PurpoidEntity purpoid = this.purpoid;
		if (shouldFollowTarget(purpoid, false)) {
			this.path = purpoid.getNavigation().createPath(purpoid.getTarget(), 0);
			return this.path != null;
		}
		return false;
	}

	@Override
	public void start() {
		PurpoidEntity purpoid = this.purpoid;
		purpoid.getNavigation().moveTo(this.path, 2.25F);
		purpoid.setAggressive(true);
		this.delayCounter = 0;
	}

	@Override
	public boolean canContinueToUse() {
		PurpoidEntity purpoid = this.purpoid;
		return shouldFollowTarget(purpoid, false) && purpoid.getNavigation().isInProgress();
	}

	@Override
	public void tick() {
		PurpoidEntity purpoid = this.purpoid;
		if (!purpoid.isBoosting()) {
			purpoid.setBoostingTicks(5);
		}
		this.delayCounter = Math.max(this.delayCounter - 1, 0);
		LivingEntity target = purpoid.getTarget();
		double distanceToTargetSq = purpoid.distanceToSqr(target);
		RandomSource random = purpoid.getRandom();
		if (this.delayCounter <= 0 && random.nextFloat() < 0.05F) {
			this.delayCounter = 4 + random.nextInt(9);
			PathNavigation pathNavigator = purpoid.getNavigation();
			if (distanceToTargetSq >= 9.0F) {
				Path path = pathNavigator.createPath(findAirPosAboveTarget(purpoid.level, target), 0);
				if (path == null || !pathNavigator.moveTo(path, 2.25F)) {
					this.delayCounter += 15;
				}
			} else {
				purpoid.getMoveControl().setWantedPosition(target.getX(), target.getY(), target.getZ(), 2.25F);
			}
		}

		boolean small = purpoid.getSize() == PurpoidSize.PURP;
		float width = purpoid.getBbWidth() * (small ? 2.85F : 2.0F);
		double reachRange = width * width + target.getBbWidth();
		if (distanceToTargetSq <= reachRange) {
			if (small) {
				if (purpoid.isEndimationPlaying(EEPlayableEndimations.PURPOID_TELEFRAG) && purpoid.getAnimationTick() == 5) {
					double targetX = target.getX();
					double targetY = target.getY();
					double targetZ = target.getZ();
					for (int i = 0; i < 16; i++) {
						double randomX = targetX + (random.nextDouble() - 0.5D) * 32.0D;
						double randomY = targetY + (random.nextInt(33) - 16);
						double randomZ = targetZ + (random.nextDouble() - 0.5D) * 32.0D;
						if (target.randomTeleport(randomX, randomY, randomZ, false)) {
							target.hurt(DamageSource.mobAttack(purpoid), (float) purpoid.getAttributeValue(Attributes.ATTACK_DAMAGE));
							if (target instanceof ServerPlayer) {
								EndergeticExpansion.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) target), new S2CEnablePurpoidFlash());
							}
							break;
						}
					}
				} else if (purpoid.isNoEndimationPlaying()) {
					NetworkUtil.setPlayingAnimation(purpoid, EEPlayableEndimations.PURPOID_TELEFRAG);
				}
			} else {
				purpoid.startRiding(target);
			}
		}
	}

	@Override
	public void stop() {
		PurpoidEntity purpoid = this.purpoid;
		LivingEntity livingentity = purpoid.getTarget();
		if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingentity)) {
			purpoid.setTarget(null);
		}

		purpoid.setAggressive(false);
		purpoid.getNavigation().stop();
	}

	public static boolean shouldFollowTarget(PurpoidEntity purpoid, boolean near) {
		LivingEntity attackTarget = purpoid.getTarget();
		return attackTarget != null && attackTarget.isAlive() && attackTarget.hasPassenger(e -> e instanceof PurpoidEntity) == near && !purpoid.isPassenger() && (!(attackTarget instanceof Player) || !attackTarget.isSpectator() && !((Player) attackTarget).isCreative());
	}

	public static BlockPos findAirPosAboveTarget(Level world, LivingEntity target) {
		BlockPos.MutableBlockPos mutable = target.blockPosition().mutable();
		int maxHeight = target.getRandom().nextInt(3) + 4;
		for (int y = 0; y < maxHeight; y++) {
			mutable.move(0, 1, 0);
			if (!world.isEmptyBlock(mutable)) {
				mutable.move(0, -1, 0);
				break;
			}
		}
		return mutable;
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}
}
