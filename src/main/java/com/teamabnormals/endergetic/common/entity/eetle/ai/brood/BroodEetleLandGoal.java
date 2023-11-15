package com.teamabnormals.endergetic.common.entity.eetle.ai.brood;

import com.teamabnormals.endergetic.common.entity.eetle.BroodEetle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class BroodEetleLandGoal extends Goal {
	private final BroodEetle broodEetle;
	private int failedPaths;
	@Nullable
	private Path path;
	private int ticksNoPath;
	private int ticksNearTakeoffPos;

	public BroodEetleLandGoal(BroodEetle broodEetle) {
		this.broodEetle = broodEetle;
		this.setFlags(EnumSet.of(Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		BroodEetle broodEetle = this.broodEetle;
		if (broodEetle.isFlying() && ((broodEetle.getTicksFlying() >= 700 && broodEetle.getRandom().nextFloat() < 0.1F) || broodEetle.isOnLastHealthStage())) {
			BlockPos takeoffPos = broodEetle.takeoffPos;
			Level world = broodEetle.level;
			boolean canSitOnTakeoffPos = takeoffPos != null && world.loadedAndEntityCanStandOn(takeoffPos.below(), broodEetle);
			if (canSitOnTakeoffPos) {
				this.path = broodEetle.getNavigation().createPath(takeoffPos, 0);
				if (this.path != null) {
					return true;
				}
				this.failedPaths++;
			}
			if (!canSitOnTakeoffPos || this.failedPaths >= 6) {
				Vec3 view = broodEetle.getViewVector(0.0F);
				Vec3 foundGroundPos = AirAndWaterRandomPos.getPos(broodEetle, 8, 4, -3, view.x, view.z, (float) Math.PI / 2F);
				if (foundGroundPos != null) {
					this.path = broodEetle.getNavigation().createPath(foundGroundPos.x, foundGroundPos.y, foundGroundPos.z, 0);
					if (this.path != null) {
						broodEetle.takeoffPos = new BlockPos(foundGroundPos);
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public void start() {
		this.failedPaths = 0;
		this.broodEetle.getNavigation().moveTo(this.path, 1.0F);
	}

	@Override
	public boolean canContinueToUse() {
		BroodEetle broodEetle = this.broodEetle;
		if (broodEetle.getNavigation().isDone()) {
			this.ticksNoPath++;
		}
		return broodEetle.takeoffPos != null && broodEetle.isFlying() && this.ticksNoPath < 10;
	}

	@Override
	public void tick() {
		BroodEetle broodEetle = this.broodEetle;
		BlockPos takeoffPos = broodEetle.takeoffPos;
		if (broodEetle.isFlying() && broodEetle.position().distanceToSqr(Vec3.atCenterOf(takeoffPos)) <= 3.0F) {
			broodEetle.setDeltaMovement(broodEetle.getDeltaMovement().scale(0.95F));
			if (++this.ticksNearTakeoffPos >= 5 && broodEetle.isOnGround()) {
				broodEetle.resetFlyCooldown();
				broodEetle.setFlying(false);
				broodEetle.takeoffPos = null;
			}
		} else {
			this.ticksNearTakeoffPos = 0;
		}
	}

	@Override
	public void stop() {
		this.path = null;
		this.ticksNearTakeoffPos = 0;
		this.ticksNoPath = 0;
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}
}
