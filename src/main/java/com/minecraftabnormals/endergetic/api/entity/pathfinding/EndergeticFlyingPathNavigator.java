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

	protected PathFinder getPathFinder(int idleTime) {
		this.nodeProcessor = new EndergeticFlyingNodeProcessor();
		return new PathFinder(this.nodeProcessor, idleTime);
	}

	@Override
	protected boolean canNavigate() {
		return !this.isInLiquid();
	}

	@Override
	protected Vector3d getEntityPosition() {
		return new Vector3d(this.entity.getPosX(), this.entity.getPosY() + (double) this.entity.getHeight() * 0.5D, this.entity.getPosZ());
	}

	@Override
	public void tick() {
		this.totalTicks++;
		if (this.tryUpdatePath) {
			this.updatePath();
		}

		if (!this.noPath()) {
			if (this.canNavigate()) {
				this.pathFollow();
			} else if (this.currentPath != null && this.currentPath.getCurrentPathIndex() < this.currentPath.getCurrentPathLength()) {
				Vector3d Vector3d = this.currentPath.getVectorFromIndex(this.entity, this.currentPath.getCurrentPathIndex());
				if (MathHelper.floor(this.entity.getPosX()) == MathHelper.floor(Vector3d.x) && MathHelper.floor(this.entity.getPosY()) == MathHelper.floor(Vector3d.y) && MathHelper.floor(this.entity.getPosZ()) == MathHelper.floor(Vector3d.z)) {
					this.currentPath.setCurrentPathIndex(this.currentPath.getCurrentPathIndex() + 1);
				}
			}

			DebugPacketSender.sendPath(this.world, this.entity, this.currentPath, this.maxDistanceToWaypoint);

			if (!this.noPath()) {
				Vector3d Vector3d1 = this.currentPath.getPosition(this.entity);
				this.entity.getMoveHelper().setMoveTo(Vector3d1.x, Vector3d1.y, Vector3d1.z, this.speed);
			}
		}
	}

	@Override
	protected void pathFollow() {
		if (this.currentPath != null) {
			Vector3d entityPos = this.getEntityPosition();
			float f = this.entity.getWidth();
			float f1 = f > 0.75F ? f / 2.0F : 0.75F - f / 2.0F;
			Vector3d Vector3d1 = this.entity.getMotion();
			if (Math.abs(Vector3d1.x) > 0.2D || Math.abs(Vector3d1.z) > 0.2D) {
				f1 = (float) ((double) f1 * Vector3d1.length() * 6.0D);
			}

			Vector3d Vector3d2 = Vector3d.copyCenteredHorizontally(this.currentPath.func_242948_g());
			if (Math.abs(this.entity.getPosX() - (Vector3d2.x + 0.5D)) < (double) f1 && Math.abs(this.entity.getPosZ() - (Vector3d2.z + 0.5D)) < (double) f1 && Math.abs(this.entity.getPosY() - Vector3d2.y) < (double) (f1 * 2.0F)) {
				this.currentPath.incrementPathIndex();
			}

			for (int j = Math.min(this.currentPath.getCurrentPathIndex() + 6, this.currentPath.getCurrentPathLength() - 1); j > this.currentPath.getCurrentPathIndex(); --j) {
				Vector3d2 = this.currentPath.getVectorFromIndex(this.entity, j);
				if (!(Vector3d2.squareDistanceTo(entityPos) > 36.0D) && this.isDirectPathBetweenPoints(entityPos, Vector3d2, 0, 0, 0)) {
					this.currentPath.setCurrentPathIndex(j);
					break;
				}
			}

			this.checkForStuck(entityPos);
		}
	}

	/**
	 * Checks if entity haven't been moved when last checked and if so, clears current
	 */
	@Override
	protected void checkForStuck(Vector3d positionVec3) {
		if (this.totalTicks - this.ticksAtLastPos > 100) {
			if (positionVec3.squareDistanceTo(this.lastPosCheck) < 2.25D) {
				this.clearPath();
			}
			this.ticksAtLastPos = this.totalTicks;
			this.lastPosCheck = positionVec3;
		}

		if (this.currentPath != null && !this.currentPath.isFinished()) {
			Vector3i vector3i = this.currentPath.func_242948_g();
			if (vector3i.equals(this.timeoutCachedNode)) {
				this.timeoutTimer += Util.milliTime() - this.lastTimeoutCheck;
			} else {
				this.timeoutCachedNode = vector3i;
				double d0 = positionVec3.distanceTo(Vector3d.copyCentered(this.timeoutCachedNode));
				this.timeoutLimit = this.entity.getAIMoveSpeed() > 0.0F ? d0 / this.entity.getAIMoveSpeed() * 100.0D : 0.0D;
			}

			if (this.timeoutLimit > 0.0D && this.timeoutTimer > this.timeoutLimit * 2.0D) {
				this.timeoutCachedNode = Vector3i.NULL_VECTOR;
				this.timeoutTimer = 0L;
				this.timeoutLimit = 0.0D;
				this.clearPath();
			}

			this.lastTimeoutCheck = Util.milliTime();
		}
	}

	@Override
	protected boolean isDirectPathBetweenPoints(Vector3d posVec31, Vector3d posVec32, int sizeX, int sizeY, int sizeZ) {
		Vector3d Vector3d = new Vector3d(posVec32.x, posVec32.y + (double) this.entity.getHeight() * 0.5D, posVec32.z);
		return this.world.rayTraceBlocks(new RayTraceContext(posVec31, Vector3d, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this.entity)).getType() == RayTraceResult.Type.MISS;
	}

	@Override
	public boolean canEntityStandOnPos(BlockPos pos) {
		return this.world.isAirBlock(pos);
	}
}