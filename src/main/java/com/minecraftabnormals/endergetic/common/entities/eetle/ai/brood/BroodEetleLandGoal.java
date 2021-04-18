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

public class BroodEetleLandGoal extends Goal {
	private final BroodEetleEntity broodEetle;
	private int failedPaths;
	@Nullable
	private Path path;
	private int ticksNoPath;
	private int ticksNearTakeoffPos;

	public BroodEetleLandGoal(BroodEetleEntity broodEetle) {
		this.broodEetle = broodEetle;
		this.setMutexFlags(EnumSet.of(Flag.MOVE));
	}

	@Override
	public boolean shouldExecute() {
		BroodEetleEntity broodEetle = this.broodEetle;
		if (broodEetle.isFlying() && broodEetle.getTicksFlying() >= 700 && broodEetle.getRNG().nextFloat() < 0.1F) {
			BlockPos takeoffPos = broodEetle.takeoffPos;
			World world = broodEetle.world;
			boolean canSitOnTakeoffPos = takeoffPos != null && world.isTopSolid(takeoffPos.down(), broodEetle);
			if (canSitOnTakeoffPos) {
				this.path = broodEetle.getNavigator().getPathToPos(takeoffPos, 0);
				if (this.path != null) {
					return true;
				}
				this.failedPaths++;
			}
			if (!canSitOnTakeoffPos || this.failedPaths >= 6) {
				Vector3d foundGroundPos = RandomPositionGenerator.findGroundTarget(broodEetle, 8, 4, -3, broodEetle.getLook(0.0F), (float)Math.PI / 2F);
				if (foundGroundPos != null) {
					this.path = broodEetle.getNavigator().getPathToPos(foundGroundPos.x, foundGroundPos.y, foundGroundPos.z, 0);
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
	public void startExecuting() {
		this.failedPaths = 0;
		this.broodEetle.getNavigator().setPath(this.path, 1.0F);
	}

	@Override
	public boolean shouldContinueExecuting() {
		BroodEetleEntity broodEetle = this.broodEetle;
		if (broodEetle.getNavigator().noPath()) {
			this.ticksNoPath++;
		}
		return broodEetle.takeoffPos != null && broodEetle.isFlying() && this.ticksNoPath < 10;
	}

	@Override
	public void tick() {
		BroodEetleEntity broodEetle = this.broodEetle;
		BlockPos takeoffPos = broodEetle.takeoffPos;
		if (broodEetle.isFlying() && broodEetle.getPositionVec().squareDistanceTo(Vector3d.copyCentered(takeoffPos)) <= 3.0F) {
			broodEetle.setMotion(broodEetle.getMotion().scale(0.95F));
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
	public void resetTask() {
		this.path = null;
		this.ticksNearTakeoffPos = 0;
		this.ticksNoPath = 0;
	}
}
