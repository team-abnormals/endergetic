package com.minecraftabnormals.endergetic.api.entity.pathfinding;

import net.minecraft.world.entity.Mob;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;

public class EndergeticFlyingPathNavigator extends PathNavigation {

	public EndergeticFlyingPathNavigator(Mob entitylivingIn, Level worldIn) {
		super(entitylivingIn, worldIn);
	}

	protected PathFinder createPathFinder(int idleTime) {
		this.nodeEvaluator = new EndergeticFlyingNodeProcessor();
		return new PathFinder(this.nodeEvaluator, idleTime);
	}

	@Override
	protected boolean canUpdatePath() {
		return !this.isInLiquid();
	}

	@Override
	protected Vec3 getTempMobPos() {
		return new Vec3(this.mob.getX(), this.mob.getY() + (double) this.mob.getBbHeight() * 0.5D, this.mob.getZ());
	}

	@Override
	public void tick() {
		this.tick++;
		if (this.hasDelayedRecomputation) {
			this.recomputePath();
		}

		if (!this.isDone()) {
			if (this.canUpdatePath()) {
				this.followThePath();
			} else if (this.path != null && this.path.getNextNodeIndex() < this.path.getNodeCount()) {
				Vec3 Vector3d = this.path.getEntityPosAtNode(this.mob, this.path.getNextNodeIndex());
				if (Mth.floor(this.mob.getX()) == Mth.floor(Vector3d.x) && Mth.floor(this.mob.getY()) == Mth.floor(Vector3d.y) && Mth.floor(this.mob.getZ()) == Mth.floor(Vector3d.z)) {
					this.path.setNextNodeIndex(this.path.getNextNodeIndex() + 1);
				}
			}

			DebugPackets.sendPathFindingPacket(this.level, this.mob, this.path, this.maxDistanceToWaypoint);

			if (!this.isDone()) {
				Vec3 Vector3d1 = this.path.getNextEntityPos(this.mob);
				this.mob.getMoveControl().setWantedPosition(Vector3d1.x, Vector3d1.y, Vector3d1.z, this.speedModifier);
			}
		}
	}

	@Override
	protected void followThePath() {
		if (this.path != null) {
			Vec3 entityPos = this.getTempMobPos();
			float f = this.mob.getBbWidth();
			float f1 = f > 0.75F ? f / 2.0F : 0.75F - f / 2.0F;
			Vec3 Vector3d1 = this.mob.getDeltaMovement();
			if (Math.abs(Vector3d1.x) > 0.2D || Math.abs(Vector3d1.z) > 0.2D) {
				f1 = (float) ((double) f1 * Vector3d1.length() * 6.0D);
			}

			Vec3 Vector3d2 = Vec3.atBottomCenterOf(this.path.getNextNodePos());
			if (Math.abs(this.mob.getX() - (Vector3d2.x + 0.5D)) < (double) f1 && Math.abs(this.mob.getZ() - (Vector3d2.z + 0.5D)) < (double) f1 && Math.abs(this.mob.getY() - Vector3d2.y) < (double) (f1 * 2.0F)) {
				this.path.advance();
			}

			for (int j = Math.min(this.path.getNextNodeIndex() + 6, this.path.getNodeCount() - 1); j > this.path.getNextNodeIndex(); --j) {
				Vector3d2 = this.path.getEntityPosAtNode(this.mob, j);
				if (!(Vector3d2.distanceToSqr(entityPos) > 36.0D) && this.canMoveDirectly(entityPos, Vector3d2)) {
					this.path.setNextNodeIndex(j);
					break;
				}
			}

			this.doStuckDetection(entityPos);
		}
	}

	/**
	 * Checks if entity haven't been moved when last checked and if so, clears current
	 */
	@Override
	protected void doStuckDetection(Vec3 positionVec3) {
		if (this.tick - this.lastStuckCheck > 100) {
			if (positionVec3.distanceToSqr(this.lastStuckCheckPos) < 2.25D) {
				this.stop();
			}
			this.lastStuckCheck = this.tick;
			this.lastStuckCheckPos = positionVec3;
		}

		if (this.path != null && !this.path.isDone()) {
			Vec3i vector3i = this.path.getNextNodePos();
			if (vector3i.equals(this.timeoutCachedNode)) {
				this.timeoutTimer += Util.getMillis() - this.lastTimeoutCheck;
			} else {
				this.timeoutCachedNode = vector3i;
				double d0 = positionVec3.distanceTo(Vec3.atCenterOf(this.timeoutCachedNode));
				this.timeoutLimit = this.mob.getSpeed() > 0.0F ? d0 / this.mob.getSpeed() * 100.0D : 0.0D;
			}

			if (this.timeoutLimit > 0.0D && this.timeoutTimer > this.timeoutLimit * 2.0D) {
				this.timeoutCachedNode = Vec3i.ZERO;
				this.timeoutTimer = 0L;
				this.timeoutLimit = 0.0D;
				this.stop();
			}

			this.lastTimeoutCheck = Util.getMillis();
		}
	}

	@Override
	protected boolean canMoveDirectly(Vec3 posVec31, Vec3 posVec32) {
		Vec3 Vector3d = new Vec3(posVec32.x, posVec32.y + (double) this.mob.getBbHeight() * 0.5D, posVec32.z);
		return this.level.clip(new ClipContext(posVec31, Vector3d, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.mob)).getType() == HitResult.Type.MISS;
	}

	@Override
	public boolean isStableDestination(BlockPos pos) {
		return this.level.isEmptyBlock(pos);
	}
}