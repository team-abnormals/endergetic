package com.minecraftabnormals.endergetic.api.entity.pathfinding;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.entity.MobEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.pathfinding.FlaggedPathPoint;
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.pathfinding.PathType;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockReader;

public class EndergeticFlyingNodeProcessor extends NodeProcessor {

	@Override
	public PathPoint getStart() {
		return super.getNode(MathHelper.floor(this.mob.getBoundingBox().minX), MathHelper.floor(this.mob.getBoundingBox().minY + 0.5D), MathHelper.floor(this.mob.getBoundingBox().minZ));
	}

	@Override
	public FlaggedPathPoint getGoal(double p_224768_1_, double p_224768_3_, double p_224768_5_) {
		return new FlaggedPathPoint(super.getNode(MathHelper.floor(p_224768_1_ - (double) (this.mob.getBbWidth() / 2.0F)), MathHelper.floor(p_224768_3_ + 0.5D), MathHelper.floor(p_224768_5_ - (double) (this.mob.getBbWidth() / 2.0F))));
	}

	@Override
	public int getNeighbors(PathPoint[] pathPoints, PathPoint pathNode) {
		int i = 0;

		for (Direction direction : Direction.values()) {
			PathPoint pathpoint = this.getPoint(pathNode.x + direction.getStepX(), pathNode.y + direction.getStepY(), pathNode.z + direction.getStepZ());
			if (pathpoint != null && !pathpoint.closed) {
				pathPoints[i++] = pathpoint;
			}
		}

		return i;
	}

	@Override
	public PathNodeType getBlockPathType(IBlockReader blockaccessIn, int x, int y, int z, MobEntity entitylivingIn, int xSize, int ySize, int zSize, boolean canBreakDoorsIn, boolean canEnterDoorsIn) {
		return this.getBlockPathType(blockaccessIn, x, y, z);
	}

	@Override
	public PathNodeType getBlockPathType(IBlockReader blockaccessIn, int x, int y, int z) {
		BlockPos blockpos = new BlockPos(x, y, z);
		BlockState blockstate = blockaccessIn.getBlockState(blockpos);
		return blockstate.isPathfindable(blockaccessIn, blockpos, PathType.AIR) ? PathNodeType.WALKABLE : PathNodeType.BLOCKED;
	}

	@Nullable
	private PathPoint getPoint(int x, int y, int z) {
		PathNodeType pathnodetype = this.isFree(x, y, z);
		return (pathnodetype != PathNodeType.BREACH) && pathnodetype != PathNodeType.WALKABLE ? null : this.getNode(x, y, z);
	}

	@Override
	protected PathPoint getNode(int x, int y, int z) {
		PathPoint pathpoint = null;
		PathNodeType pathnodetype = this.getBlockPathType(this.mob.level, x, y, z);
		float malus = this.mob.getPathfindingMalus(pathnodetype);
		if (malus >= 0.0F) {
			pathpoint = super.getNode(x, y, z);
			pathpoint.type = pathnodetype;
			pathpoint.costMalus = Math.max(pathpoint.costMalus, malus);
			if (this.level.getFluidState(new BlockPos(x, y, z)).isEmpty()) {
				pathpoint.costMalus += 8.0F;
			}
		}

		return pathpoint;
	}

	@SuppressWarnings("deprecation")
	private PathNodeType isFree(int x, int y, int z) {
		BlockPos.Mutable blockpos$mutableblockpos = new BlockPos.Mutable();

		for (int i = x; i < x + this.entityWidth; ++i) {
			for (int j = y; j < y + this.entityHeight; ++j) {
				for (int k = z; k < z + this.entityDepth; ++k) {
					FluidState ifluidstate = this.level.getFluidState(blockpos$mutableblockpos.set(i, j, k));
					BlockState blockstate = this.level.getBlockState(blockpos$mutableblockpos.set(i, j, k));
					if (ifluidstate.isEmpty() && blockstate.isPathfindable(this.level, blockpos$mutableblockpos.below(), PathType.AIR) && blockstate.isAir()) {
						return PathNodeType.WALKABLE;
					}

					if (ifluidstate.is(FluidTags.WATER)) {
						return PathNodeType.BLOCKED;
					}
				}
			}
		}

		BlockState blockstate1 = this.level.getBlockState(blockpos$mutableblockpos);
		if (blockstate1.isPathfindable(this.level, blockpos$mutableblockpos, PathType.AIR)) {
			return PathNodeType.WALKABLE;
		} else {
			return PathNodeType.BLOCKED;
		}
	}

}
