package com.minecraftabnormals.endergetic.common.entities.purpoid.ai;

import com.minecraftabnormals.abnormals_core.core.util.NetworkUtil;
import com.minecraftabnormals.endergetic.common.entities.purpoid.PurpoidEntity;
import com.minecraftabnormals.endergetic.common.entities.purpoid.PurpoidSize;
import com.minecraftabnormals.endergetic.common.network.entity.S2CEnablePurpoidFlash;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Random;

public class PurpoidAttackGoal extends Goal {
	private final PurpoidEntity purpoid;
	@Nullable
	private Path path;
	private int delayCounter;

	public PurpoidAttackGoal(PurpoidEntity purpoid) {
		this.purpoid = purpoid;
		this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	@Override
	public boolean shouldExecute() {
		PurpoidEntity purpoid = this.purpoid;
		if (shouldFollowTarget(purpoid, false)) {
			this.path = purpoid.getNavigator().getPathToEntity(purpoid.getAttackTarget(), 0);
			return this.path != null;
		}
		return false;
	}

	@Override
	public void startExecuting() {
		PurpoidEntity purpoid = this.purpoid;
		purpoid.getNavigator().setPath(this.path, 2.25F);
		purpoid.setAggroed(true);
		this.delayCounter = 0;
	}

	@Override
	public boolean shouldContinueExecuting() {
		PurpoidEntity purpoid = this.purpoid;
		return shouldFollowTarget(purpoid, false) && purpoid.getNavigator().hasPath();
	}

	@Override
	public void tick() {
		PurpoidEntity purpoid = this.purpoid;
		if (!purpoid.isBoosting()) {
			purpoid.setBoostingTicks(5);
		}
		this.delayCounter = Math.max(this.delayCounter - 1, 0);
		LivingEntity target = purpoid.getAttackTarget();
		double distanceToTargetSq = purpoid.getDistanceSq(target);
		Random random = purpoid.getRNG();
		if (this.delayCounter <= 0 && random.nextFloat() < 0.05F) {
			this.delayCounter = 4 + random.nextInt(9);
			PathNavigator pathNavigator = purpoid.getNavigator();
			if (distanceToTargetSq >= 9.0F) {
				Path path = pathNavigator.getPathToPos(findAirPosAboveTarget(purpoid.world, target), 0);
				if (path == null || !pathNavigator.setPath(path, 2.25F)) {
					this.delayCounter += 15;
				}
			} else {
				purpoid.getMoveHelper().setMoveTo(target.getPosX(), target.getPosY(), target.getPosZ(), 2.25F);
			}
		}

		boolean small = purpoid.getSize() == PurpoidSize.SMALL;
		float width = purpoid.getWidth() * (small ? 2.85F : 2.0F);
		double reachRange = width * width + target.getWidth();
		if (distanceToTargetSq <= reachRange) {
			if (small) {
				if (purpoid.isEndimationPlaying(PurpoidEntity.TELEFRAG_ANIMATION) && purpoid.getAnimationTick() == 5) {
					double targetX = target.getPosX();
					double targetY = target.getPosY();
					double targetZ = target.getPosZ();
					for (int i = 0; i < 16; i++) {
						double randomX = targetX + (random.nextDouble() - 0.5D) * 32.0D;
						double randomY = targetY + (random.nextInt(33) - 16);
						double randomZ = targetZ + (random.nextDouble() - 0.5D) * 32.0D;
						if (target.attemptTeleport(randomX, randomY, randomZ, false)) {
							target.attackEntityFrom(DamageSource.causeMobDamage(purpoid), (float) purpoid.getAttributeValue(Attributes.ATTACK_DAMAGE));
							if (target instanceof ServerPlayerEntity) {
								EndergeticExpansion.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) target), new S2CEnablePurpoidFlash());
							}
							break;
						}
					}
				} else if (purpoid.isNoEndimationPlaying()) {
					NetworkUtil.setPlayingAnimationMessage(purpoid, PurpoidEntity.TELEFRAG_ANIMATION);
				}
			} else {
				purpoid.startRiding(target);
			}
		}
	}

	@Override
	public void resetTask() {
		PurpoidEntity purpoid = this.purpoid;
		LivingEntity livingentity = purpoid.getAttackTarget();
		if (!EntityPredicates.CAN_AI_TARGET.test(livingentity)) {
			purpoid.setAttackTarget(null);
		}

		purpoid.setAggroed(false);
		purpoid.getNavigator().clearPath();
	}

	public static boolean shouldFollowTarget(PurpoidEntity purpoid, boolean near) {
		LivingEntity attackTarget = purpoid.getAttackTarget();
		return attackTarget != null && attackTarget.isAlive() && attackTarget.isPassenger(PurpoidEntity.class) == near && purpoid.getSize() != PurpoidSize.GIANT && !purpoid.isPassenger() && (!(attackTarget instanceof PlayerEntity) || !attackTarget.isSpectator() && !((PlayerEntity) attackTarget).isCreative());
	}

	public static BlockPos findAirPosAboveTarget(World world, LivingEntity target) {
		BlockPos.Mutable mutable = target.getPosition().toMutable();
		int maxHeight = target.getRNG().nextInt(3) + 4;
		for (int y = 0; y < maxHeight; y++) {
			mutable.move(0, 1, 0);
			if (!world.isAirBlock(mutable)) {
				mutable.move(0, -1, 0);
				break;
			}
		}
		return mutable;
	}
}
