package com.minecraftabnormals.endergetic.api.entity.pathfinding;

import net.minecraft.entity.MobEntity;
import net.minecraft.network.DebugPacketSender;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;

public class EndergeticFlyingPathNavigator extends PathNavigator {

	public EndergeticFlyingPathNavigator(MobEntity entitylivingIn, World worldIn) {
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
	protected Vector3d getTempMobPos() {
		return new Vector3d(this.mob.getX(), this.mob.getY() + (double) this.mob.getBbHeight() * 0.5D, this.mob.getZ());
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
				Vector3d Vector3d = this.path.getEntityPosAtNode(this.mob, this.path.getNextNodeIndex());
				if (MathHelper.floor(this.mob.getX()) == MathHelper.floor(Vector3d.x) && MathHelper.floor(this.mob.getY()) == MathHelper.floor(Vector3d.y) && MathHelper.floor(this.mob.getZ()) == MathHelper.floor(Vector3d.z)) {
					this.path.setNextNodeIndex(this.path.getNextNodeIndex() + 1);
				}
			}

			DebugPacketSender.sendPathFindingPacket(this.level, this.mob, this.path, this.maxDistanceToWaypoint);

			if (!this.isDone()) {
				Vector3d Vector3d1 = this.path.getNextEntityPos(this.mob);
				this.mob.getMoveControl().setWantedPosition(Vector3d1.x, Vector3d1.y, Vector3d1.z, this.speedModifier);
			}
		}
	}

	@Override
	protected void followThePath() {
		if (this.path != null) {
			Vector3d entityPos = this.getTempMobPos();
			float f = this.mob.getBbWidth();
			float f1 = f > 0.75F ? f / 2.0F : 0.75F - f / 2.0F;
			Vector3d Vector3d1 = this.mob.getDeltaMovement();
			if (Math.abs(Vector3d1.x) > 0.2D || Math.abs(Vector3d1.z) > 0.2D) {
				f1 = (float) ((double) f1 * Vector3d1.length() * 6.0D);
			}

			Vector3d Vector3d2 = Vector3d.atBottomCenterOf(this.path.getNextNodePos());
			if (Math.abs(this.mob.getX() - (Vector3d2.x + 0.5D)) < (double) f1 && Math.abs(this.mob.getZ() - (Vector3d2.z + 0.5D)) < (double) f1 && Math.abs(this.mob.getY() - Vector3d2.y) < (double) (f1 * 2.0F)) {
				this.path.advance();
			}

			for (int j = Math.min(this.path.getNextNodeIndex() + 6, this.path.getNodeCount() - 1); j > this.path.getNextNodeIndex(); --j) {
				Vector3d2 = this.path.getEntityPosAtNode(this.mob, j);
				if (!(Vector3d2.distanceToSqr(entityPos) > 36.0D) && this.canMoveDirectly(entityPos, Vector3d2, 0, 0, 0)) {
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
	protected void doStuckDetection(Vector3d positionVec3) {
		if (this.tick - this.lastStuckCheck > 100) {
			if (positionVec3.distanceToSqr(this.lastStuckCheckPos) < 2.25D) {
				this.stop();
			}
			this.lastStuckCheck = this.tick;
			this.lastStuckCheckPos = positionVec3;
		}

		if (this.path != null && !this.path.isDone()) {
			Vector3i vector3i = this.path.getNextNodePos();
			if (vector3i.equals(this.timeoutCachedNode)) {
				this.timeoutTimer += Util.getMillis() - this.lastTimeoutCheck;
			} else {
				this.timeoutCachedNode = vector3i;
				double d0 = positionVec3.distanceTo(Vector3d.atCenterOf(this.timeoutCachedNode));
				this.timeoutLimit = this.mob.getSpeed() > 0.0F ? d0 / this.mob.getSpeed() * 100.0D : 0.0D;
			}

			if (this.timeoutLimit > 0.0D && this.timeoutTimer > this.timeoutLimit * 2.0D) {
				this.timeoutCachedNode = Vector3i.ZERO;
				this.timeoutTimer = 0L;
				this.timeoutLimit = 0.0D;
				this.stop();
			}

			this.lastTimeoutCheck = Util.getMillis();
		}
	}

	@Override
	protected boolean canMoveDirectly(Vector3d posVec31, Vector3d posVec32, int sizeX, int sizeY, int sizeZ) {
		Vector3d Vector3d = new Vector3d(posVec32.x, posVec32.y + (double) this.mob.getBbHeight() * 0.5D, posVec32.z);
		return this.level.clip(new RayTraceContext(posVec31, Vector3d, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this.mob)).getType() == RayTraceResult.Type.MISS;
	}

	@Override
	public boolean isStableDestination(BlockPos pos) {
		return this.level.isEmptyBlock(pos);
	}
}