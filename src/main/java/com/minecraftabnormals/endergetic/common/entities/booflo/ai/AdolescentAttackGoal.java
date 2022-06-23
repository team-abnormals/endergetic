package com.minecraftabnormals.endergetic.common.entities.booflo.ai;

import java.util.EnumSet;

import javax.annotation.Nullable;

import com.minecraftabnormals.endergetic.common.entities.bolloom.BolloomFruitEntity;
import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloAdolescentEntity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.core.BlockPos;

public class AdolescentAttackGoal extends Goal {
	protected final BoofloAdolescentEntity attacker;
	protected int attackTick;
	private final double speedTowardsTarget;
	private final boolean longMemory;
	private Path path;
	private int delayCounter;
	private double targetX;
	private double targetY;
	private double targetZ;
	protected final int attackInterval = 20;
	private long lastCanUseCheck;
	private boolean canPenalize = false;

	public AdolescentAttackGoal(BoofloAdolescentEntity attacker, double speedIn, boolean useLongMemory) {
		this.attacker = attacker;
		this.speedTowardsTarget = speedIn;
		this.longMemory = useLongMemory;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	public boolean canUse() {
		long i = this.attacker.level.getGameTime();
		if (i - this.lastCanUseCheck < 20L) {
			return false;
		} else if (!this.attacker.isHungry()) {
			return false;
		} else {
			this.lastCanUseCheck = i;
			Entity target = this.attacker.getBoofloAttackTarget();
			if (target == null) {
				return false;
			} else if (!target.isAlive()) {
				return false;
			} else if (this.attacker.hasFruit()) {
				return false;
			} else {
				if (this.canPenalize) {
					if (--this.delayCounter <= 0) {
						this.path = this.attacker.getNavigation().createPath(target, 0);
						this.delayCounter = 4 + this.attacker.getRandom().nextInt(7);
						return this.path != null;
					} else {
						return true;
					}
				}
				this.path = this.attacker.getNavigation().createPath(target, 0);
				if (this.path != null) {
					return true;
				} else {
					return this.getAttackReachSqr(target) >= this.attacker.distanceToSqr(target.getX(), target.getBoundingBox().minY, target.getZ());
				}
			}
		}
	}

	public boolean canContinueToUse() {
		Entity target = this.attacker.getBoofloAttackTarget();
		if (target == null) {
			return false;
		} else if (!target.isAlive()) {
			return false;
		} else if (this.attacker.hasFruit()) {
			return false;
		} else if (!this.attacker.isHungry()) {
			return false;
		} else if (!this.longMemory) {
			return !this.attacker.getNavigation().isDone();
		} else if (!this.attacker.isWithinRestriction(target.blockPosition())) {
			return false;
		} else {
			return !(target instanceof Player) || !target.isSpectator() && !((Player) target).isCreative();
		}
	}

	public void start() {
		this.attacker.getNavigation().moveTo(this.path, this.speedTowardsTarget);
		this.attacker.setAggressive(true);
		this.delayCounter = 0;
	}

	public void stop() {
		Entity target = this.attacker.getBoofloAttackTarget();
		if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(target)) {
			this.attacker.setBoofloAttackTarget(null);
		}
		this.attacker.setAggressive(false);
		this.attacker.getNavigation().stop();
	}

	public void tick() {
		Entity target = this.attacker.getBoofloAttackTarget();
		this.attacker.getLookControl().setLookAt(target, 10.0F, 10.0F);

		double distToEnemySqr = this.attacker.distanceToSqr(target.getX(), target.getBoundingBox().minY, target.getZ());

		this.delayCounter--;

		if ((this.longMemory || this.attacker.getSensing().canSee(target)) && this.delayCounter <= 0 && (this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D || target.distanceToSqr(this.targetX, this.targetY, this.targetZ) >= 1.0D || this.attacker.getRandom().nextFloat() < 0.05F)) {
			this.targetX = target.getX();
			this.targetY = target.getBoundingBox().minY;
			this.targetZ = target.getZ();

			this.delayCounter = 4 + this.attacker.getRandom().nextInt(7);

			if (distToEnemySqr > 1024.0D) {
				this.delayCounter += 10;
			} else if (distToEnemySqr > 256.0D) {
				this.delayCounter += 5;
			}

			if (!this.attacker.getNavigation().moveTo(target, this.speedTowardsTarget)) {
				this.delayCounter += 15;
			}
		}

		this.attackTick = Math.max(this.attackTick - 1, 0);
		this.tryToCapturePrey(target, distToEnemySqr);
	}

	protected void tryToCapturePrey(Entity prey, double distToEnemySqr) {
		double attackReach = this.getAttackReachSqr(prey);
		if (distToEnemySqr <= attackReach && this.attackTick <= 0) {
			this.attackTick = 20;
			this.attacker.setHasFruit(true);
			if (prey instanceof BolloomFruitEntity) {
				((BolloomFruitEntity) prey).onBroken(false);
				prey.remove();
			}
		}
	}

	protected double getAttackReachSqr(Entity attackTarget) {
		return (this.attacker.getBbWidth() * 2.0F * this.attacker.getBbWidth() * 2.0F + attackTarget.getBbWidth());
	}

	@Nullable
	public Path getPathToEntity(Entity entity) {
		BlockPos pos = entity.blockPosition();
		for (int y = 0; y < 8; y++) {
			pos = pos.below(y);
			if (!entity.getCommandSenderWorld().isClientSide) {
				if (entity.getCommandSenderWorld().getBlockState(pos).canOcclude() || !entity.getCommandSenderWorld().getBlockState(pos).getFluidState().isEmpty()) {
					return this.attacker.getNavigation().createPath(pos, 0);
				}
			}
		}
		return this.attacker.getNavigation().createPath(entity.blockPosition(), 0);
	}
}