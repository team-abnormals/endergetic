package com.teamabnormals.endergetic.common.entity.purpoid.ai;

import com.teamabnormals.blueprint.core.util.NetworkUtil;
import com.teamabnormals.endergetic.common.entity.purpoid.Purpoid;
import com.teamabnormals.endergetic.common.entity.purpoid.PurpoidSize;
import com.teamabnormals.endergetic.common.network.entity.S2CEnablePurpoidFlash;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import com.teamabnormals.endergetic.core.other.EEPlayableEndimations;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class PurpoidAttackGoal extends Goal {
	private final Purpoid purpoid;
	@Nullable
	private Path path;
	private int delayCounter;
	private int teleportToTargetTimer;

	public PurpoidAttackGoal(Purpoid purpoid) {
		this.purpoid = purpoid;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		Purpoid purpoid = this.purpoid;
		if (purpoid.isNoEndimationPlaying() && !purpoid.getTeleportController().isTeleporting() && shouldFollowTarget(purpoid, false)) {
			this.path = purpoid.getNavigation().createPath(purpoid.getTarget(), 0);
			return this.path != null;
		}
		return false;
	}

	@Override
	public void start() {
		Purpoid purpoid = this.purpoid;
		purpoid.getNavigation().moveTo(this.path, 2.25F);
		purpoid.setAggressive(true);
		this.delayCounter = 0;
		this.teleportToTargetTimer = 0;
	}

	@Override
	public boolean canContinueToUse() {
		Purpoid purpoid = this.purpoid;
		return shouldFollowTarget(purpoid, false) && purpoid.getNavigation().isInProgress();
	}

	@Override
	public void tick() {
		Purpoid purpoid = this.purpoid;
		if (!purpoid.isBoosting()) {
			purpoid.setBoostingTicks(5);
		}
		this.delayCounter = Math.max(this.delayCounter - 1, 0);
		LivingEntity target = purpoid.getTarget();
		double distanceToTargetSq = purpoid.distanceToSqr(target);
		RandomSource random = purpoid.getRandom();
		if (this.delayCounter <= 0 && random.nextFloat() < 0.05F) {
			this.delayCounter = 15 + random.nextInt(11);
			PathNavigation pathNavigator = purpoid.getNavigation();
			if (distanceToTargetSq >= 9.0F) {
				Path path = pathNavigator.createPath(purpoid.getSize() == PurpoidSize.NORMAL ? findAirPosAboveTarget(purpoid.level(), target) : target.blockPosition().above(random.nextInt(3)), 0);
				if (path == null || !pathNavigator.moveTo(path, 2.25F)) {
					this.delayCounter += 20;
				}
			} else {
				purpoid.getMoveControl().setWantedPosition(target.getX(), target.getY(), target.getZ(), 2.25F);
			}
		}

		boolean small = purpoid.getSize() == PurpoidSize.PURP;
		float width = purpoid.getBbWidth() * (small ? 3.25F : 2.0F);
		double reachRange = width * width + target.getBbWidth();
		if (distanceToTargetSq <= reachRange) {
			if (small) {
				if (purpoid.isEndimationPlaying(EEPlayableEndimations.PURPOID_TELEFRAG) && purpoid.getAnimationTick() == 5) {
					target.hurt(purpoid.level().damageSources().mobAttack(purpoid), (float) purpoid.getAttributeValue(Attributes.ATTACK_DAMAGE));
					double targetX = target.getX();
					double targetY = target.getY();
					double targetZ = target.getZ();
					for (int i = 0; i < 16; i++) {
						double randomX = targetX + (random.nextDouble() - 0.5D) * 32.0D;
						double randomY = targetY + (random.nextInt(33) - 16);
						double randomZ = targetZ + (random.nextDouble() - 0.5D) * 32.0D;
						if (target.randomTeleport(randomX, randomY, randomZ, false)) {
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
			this.teleportToTargetTimer = 0;
		} else if (small && distanceToTargetSq >= 2.0D && distanceToTargetSq <= 1024.0D && random.nextFloat() < 0.1F && ++this.teleportToTargetTimer >= 10) {
			double targetX = target.getX();
			double targetY = target.getY();
			double targetZ = target.getZ();
			Level level = purpoid.level();
			EntityDimensions dimensions = purpoid.getDimensions(purpoid.getPose());
			double predictedXDisplacement = Mth.clamp(target.getDeltaMovement().x() * 57.0F, -7.0D, 7.0D);
			double predictedZDisplacement = Mth.clamp(target.getDeltaMovement().z() * 57.0F, -7.0D, 7.0D);
			for (int i = 0; i < 16; i++) {
				double randomX = targetX + predictedXDisplacement;
				double randomY = targetY + random.triangle(0.0D, 2.0D);
				double randomZ = targetZ + predictedZDisplacement;
				if (level.noCollision(dimensions.makeBoundingBox(randomX, randomY, randomZ))) {
					purpoid.getTeleportController().beginTeleportation(purpoid, new Vec3(randomX, randomY, randomZ), false);
					purpoid.getNavigation().stop();
					break;
				}
			}
			this.teleportToTargetTimer = 0;
		}
	}

	@Override
	public void stop() {
		Purpoid purpoid = this.purpoid;
		LivingEntity livingentity = purpoid.getTarget();
		if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingentity)) {
			purpoid.setTarget(null);
		}

		purpoid.setAggressive(false);
		purpoid.getNavigation().stop();
	}

	public static boolean shouldFollowTarget(Purpoid purpoid, boolean near) {
		LivingEntity attackTarget = purpoid.getTarget();
		return attackTarget != null && attackTarget.isAlive() && attackTarget.hasPassenger(e -> e instanceof Purpoid) == near && !purpoid.isPassenger() && (!(attackTarget instanceof Player) || !attackTarget.isSpectator() && !((Player) attackTarget).isCreative());
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
