package com.minecraftabnormals.endergetic.common.entities.eetle.ai.brood;

import com.minecraftabnormals.endergetic.common.entities.eetle.BroodEetleEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.EnumSet;

import net.minecraft.entity.ai.goal.Goal.Flag;

public class BroodEetleLandGoal extends Goal {
	private final BroodEetleEntity broodEetle;
	private int failedPaths;
	@Nullable
	private Path path;
	private int ticksNoPath;
	private int ticksNearTakeoffPos;

	public BroodEetleLandGoal(BroodEetleEntity broodEetle) {
		this.broodEetle = broodEetle;
		this.setFlags(EnumSet.of(Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		BroodEetleEntity broodEetle = this.broodEetle;
		if (broodEetle.isFlying() && ((broodEetle.getTicksFlying() >= 700 && broodEetle.getRandom().nextFloat() < 0.1F) || broodEetle.isOnLastHealthStage())) {
			BlockPos takeoffPos = broodEetle.takeoffPos;
			World world = broodEetle.level;
			boolean canSitOnTakeoffPos = takeoffPos != null && world.loadedAndEntityCanStandOn(takeoffPos.below(), broodEetle);
			if (canSitOnTakeoffPos) {
				this.path = broodEetle.getNavigation().createPath(takeoffPos, 0);
				if (this.path != null) {
					return true;
				}
				this.failedPaths++;
			}
			if (!canSitOnTakeoffPos || this.failedPaths >= 6) {
				Vector3d foundGroundPos = RandomPositionGenerator.getAirPos(broodEetle, 8, 4, -3, broodEetle.getViewVector(0.0F), (float)Math.PI / 2F);
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
		BroodEetleEntity broodEetle = this.broodEetle;
		if (broodEetle.getNavigation().isDone()) {
			this.ticksNoPath++;
		}
		return broodEetle.takeoffPos != null && broodEetle.isFlying() && this.ticksNoPath < 10;
	}

	@Override
	public void tick() {
		BroodEetleEntity broodEetle = this.broodEetle;
		BlockPos takeoffPos = broodEetle.takeoffPos;
		if (broodEetle.isFlying() && broodEetle.position().distanceToSqr(Vector3d.atCenterOf(takeoffPos)) <= 3.0F) {
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
}
