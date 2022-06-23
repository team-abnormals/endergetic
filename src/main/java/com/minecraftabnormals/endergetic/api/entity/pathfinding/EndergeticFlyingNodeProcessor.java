package com.minecraftabnormals.endergetic.api.entity.pathfinding;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.Target;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.tags.FluidTags;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockGetter;

public class EndergeticFlyingNodeProcessor extends NodeEvaluator {

	@Override
	public Node getStart() {
		return super.getNode(Mth.floor(this.mob.getBoundingBox().minX), Mth.floor(this.mob.getBoundingBox().minY + 0.5D), Mth.floor(this.mob.getBoundingBox().minZ));
	}

	@Override
	public Target getGoal(double p_224768_1_, double p_224768_3_, double p_224768_5_) {
		return new Target(super.getNode(Mth.floor(p_224768_1_ - (double) (this.mob.getBbWidth() / 2.0F)), Mth.floor(p_224768_3_ + 0.5D), Mth.floor(p_224768_5_ - (double) (this.mob.getBbWidth() / 2.0F))));
	}

	@Override
	public int getNeighbors(Node[] pathPoints, Node pathNode) {
		int i = 0;

		for (Direction direction : Direction.values()) {
			Node pathpoint = this.getPoint(pathNode.x + direction.getStepX(), pathNode.y + direction.getStepY(), pathNode.z + direction.getStepZ());
			if (pathpoint != null && !pathpoint.closed) {
				pathPoints[i++] = pathpoint;
			}
		}

		return i;
	}

	@Override
	public BlockPathTypes getBlockPathType(BlockGetter blockaccessIn, int x, int y, int z, Mob entitylivingIn, int xSize, int ySize, int zSize, boolean canBreakDoorsIn, boolean canEnterDoorsIn) {
		return this.getBlockPathType(blockaccessIn, x, y, z);
	}

	@Override
	public BlockPathTypes getBlockPathType(BlockGetter blockaccessIn, int x, int y, int z) {
		BlockPos blockpos = new BlockPos(x, y, z);
		BlockState blockstate = blockaccessIn.getBlockState(blockpos);
		return blockstate.isPathfindable(blockaccessIn, blockpos, PathComputationType.AIR) ? BlockPathTypes.WALKABLE : BlockPathTypes.BLOCKED;
	}

	@Nullable
	private Node getPoint(int x, int y, int z) {
		BlockPathTypes pathnodetype = this.isFree(x, y, z);
		return (pathnodetype != BlockPathTypes.BREACH) && pathnodetype != BlockPathTypes.WALKABLE ? null : this.getNode(x, y, z);
	}

	@Override
	protected Node getNode(int x, int y, int z) {
		Node pathpoint = null;
		BlockPathTypes pathnodetype = this.getBlockPathType(this.mob.level, x, y, z);
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

	private BlockPathTypes isFree(int x, int y, int z) {
		BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

		for (int i = x; i < x + this.entityWidth; ++i) {
			for (int j = y; j < y + this.entityHeight; ++j) {
				for (int k = z; k < z + this.entityDepth; ++k) {
					FluidState ifluidstate = this.level.getFluidState(blockpos$mutableblockpos.set(i, j, k));
					BlockState blockstate = this.level.getBlockState(blockpos$mutableblockpos.set(i, j, k));
					if (ifluidstate.isEmpty() && blockstate.isPathfindable(this.level, blockpos$mutableblockpos.below(), PathComputationType.AIR) && blockstate.isAir()) {
						return BlockPathTypes.WALKABLE;
					}

					if (ifluidstate.is(FluidTags.WATER)) {
						return BlockPathTypes.BLOCKED;
					}
				}
			}
		}

		BlockState blockstate1 = this.level.getBlockState(blockpos$mutableblockpos);
		if (blockstate1.isPathfindable(this.level, blockpos$mutableblockpos, PathComputationType.AIR)) {
			return BlockPathTypes.WALKABLE;
		} else {
			return BlockPathTypes.BLOCKED;
		}
	}

}
