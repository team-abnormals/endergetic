package com.minecraftabnormals.endergetic.api.util;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.IWorldGenerationBaseReader;

public final class GenerationUtils {

	@SuppressWarnings("deprecation")
	public static boolean isAir(IWorldGenerationBaseReader worldIn, BlockPos pos) {
		if (!(worldIn instanceof net.minecraft.world.IBlockReader))
			return worldIn.isStateAtPosition(pos, BlockState::isAir);
		else return worldIn.isStateAtPosition(pos, state -> state.isAir((net.minecraft.world.IBlockReader) worldIn, pos));
	}

	public static <B extends BlockState> boolean isProperBlock(B blockState, Block[] validBlocks, boolean orSolid) {
		if (!Arrays.asList(validBlocks).contains(blockState.getBlock()) && orSolid) {
			return blockState.canOcclude();
		}
		return Arrays.asList(validBlocks).contains(blockState.getBlock());
	}

	public static void fillAreaWithBlockCube(IWorld world, int x1, int y1, int z1, int x2, int y2, int z2, BlockState block) {
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		for (int yy = y1; yy <= y2; yy++) {
			for (int xx = x1; xx <= x2; xx++) {
				for (int zz = z1; zz <= z2; zz++) {
					mutable.set(xx, yy, zz);
					if (world.getBlockState(mutable).getMaterial().isReplaceable()) {
						world.setBlock(mutable, block, 2);
					}
				}
			}
		}
	}

	public static void fillAreaWithBlockCubeEdged(IWorld world, int x1, int y1, int z1, int x2, int y2, int z2, BlockState block) {
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		for (int yy = y1; yy <= y2; yy++) {
			for (int xx = x1; xx <= x2; xx++) {
				for (int zz = z1; zz <= z2; zz++) {
					mutable.set(xx, yy, zz);
					if (world.getBlockState(mutable).getMaterial().isReplaceable() && (xx == x2 || zz == z2)) {
						world.setBlock(mutable, block, 2);
					}
				}
			}
		}
	}

	public static void forceFillAreaWithBlockCube(IWorld world, int x1, int y1, int z1, int x2, int y2, int z2, BlockState block) {
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		for (int yy = y1; yy <= y2; yy++) {
			for (int xx = x1; xx <= x2; xx++) {
				for (int zz = z1; zz <= z2; zz++) {
					world.setBlock(mutable.set(xx, yy, zz), block, 2);
				}
			}
		}
	}

	public static void forceFillAreaReversedWithBlockCube(IWorld world, int x1, int y1, int z1, int x2, int y2, int z2, BlockState block) {
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		for (int yy = y1; yy <= y2; yy++) {
			for (int xx = x1; xx >= x2; xx--) {
				for (int zz = z1; zz >= z2; zz--) {
					world.setBlock(mutable.set(xx, yy, zz), block, 2);
				}
			}
		}
	}

	public static void fillWithRandomTwoBlocksCube(IWorld world, int x1, int y1, int z1, int x2, int y2, int z2, Random rand, BlockState block, BlockState block2, float chance) {
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		for (int yy = y1; yy <= y2; yy++) {
			for (int xx = x1; xx <= x2; xx++) {
				for (int zz = z1; zz <= z2; zz++) {
					mutable.set(xx, yy, zz);
					if (world.getBlockState(mutable).getMaterial().isReplaceable()) {
						if (rand.nextFloat() <= chance) {
							world.setBlock(mutable, block2, 2);
						} else {
							world.setBlock(mutable, block, 2);
						}
					}
				}
			}
		}
	}

	public static boolean isAreaReplacable(IWorld world, int x1, int y1, int z1, int x2, int y2, int z2) {
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		for (int yy = y1; yy <= y2; yy++) {
			for (int xx = x1; xx <= x2; xx++) {
				for (int zz = z1; zz <= z2; zz++) {
					if (!world.getBlockState(mutable.set(xx, yy, zz)).getMaterial().isReplaceable()) {
						return false;
					}
				}
			}
		}
		return true;
	}

	public static boolean isAreaAir(IWorld world, int x1, int y1, int z1, int x2, int y2, int z2) {
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		for (int yy = y1; yy <= y2; yy++) {
			for (int xx = x1; xx <= x2; xx++) {
				for (int zz = z1; zz <= z2; zz++) {
					if (!world.isEmptyBlock(mutable.set(xx, yy, zz))) {
						return false;
					}
				}
			}
		}
		return true;
	}

	public static boolean isAreaCompletelySolid(IWorld world, int x1, int y1, int z1, int x2, int y2, int z2) {
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		for (int yy = y1; yy <= y2; yy++) {
			for (int xx = x1; xx <= x2; xx++) {
				for (int zz = z1; zz <= z2; zz++) {
					if (world.getBlockState(mutable.set(xx, yy, zz)).getMaterial().isReplaceable()) {
						return false;
					}
				}
			}
		}
		return true;
	}

	public static BlockPos getClosestPositionToPos(List<BlockPos> positions, BlockPos pos) {
		double distance = -1.0D;
		BlockPos currentPos = null;

		for (BlockPos listOfPositions : positions) {
			double newDistance = Vector3d.atLowerCornerOf(pos).distanceToSqr(Vector3d.atLowerCornerOf((listOfPositions)));
			if (distance == -1.0D || newDistance < distance) {
				distance = newDistance;
				currentPos = listOfPositions;
			}
		}

		return currentPos;
	}

}
