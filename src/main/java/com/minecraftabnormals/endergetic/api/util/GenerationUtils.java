package com.minecraftabnormals.endergetic.api.util;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelSimulatedReader;

public final class GenerationUtils {

	@SuppressWarnings("deprecation")
	public static boolean isAir(LevelSimulatedReader worldIn, BlockPos pos) {
		if (!(worldIn instanceof net.minecraft.world.level.BlockGetter))
			return worldIn.isStateAtPosition(pos, BlockState::isAir);
		else return worldIn.isStateAtPosition(pos, state -> state.isAir((net.minecraft.world.level.BlockGetter) worldIn, pos));
	}

	public static <B extends BlockState> boolean isProperBlock(B blockState, Block[] validBlocks, boolean orSolid) {
		if (!Arrays.asList(validBlocks).contains(blockState.getBlock()) && orSolid) {
			return blockState.canOcclude();
		}
		return Arrays.asList(validBlocks).contains(blockState.getBlock());
	}

	public static void fillAreaWithBlockCube(LevelAccessor world, int x1, int y1, int z1, int x2, int y2, int z2, BlockState block) {
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
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

	public static void fillAreaWithBlockCubeEdged(LevelAccessor world, int x1, int y1, int z1, int x2, int y2, int z2, BlockState block) {
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
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

	public static void forceFillAreaWithBlockCube(LevelAccessor world, int x1, int y1, int z1, int x2, int y2, int z2, BlockState block) {
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
		for (int yy = y1; yy <= y2; yy++) {
			for (int xx = x1; xx <= x2; xx++) {
				for (int zz = z1; zz <= z2; zz++) {
					world.setBlock(mutable.set(xx, yy, zz), block, 2);
				}
			}
		}
	}

	public static void forceFillAreaReversedWithBlockCube(LevelAccessor world, int x1, int y1, int z1, int x2, int y2, int z2, BlockState block) {
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
		for (int yy = y1; yy <= y2; yy++) {
			for (int xx = x1; xx >= x2; xx--) {
				for (int zz = z1; zz >= z2; zz--) {
					world.setBlock(mutable.set(xx, yy, zz), block, 2);
				}
			}
		}
	}

	public static void fillWithRandomTwoBlocksCube(LevelAccessor world, int x1, int y1, int z1, int x2, int y2, int z2, Random rand, BlockState block, BlockState block2, float chance) {
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
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

	public static boolean isAreaReplacable(LevelAccessor world, int x1, int y1, int z1, int x2, int y2, int z2) {
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
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

	public static boolean isAreaAir(LevelAccessor world, int x1, int y1, int z1, int x2, int y2, int z2) {
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
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

	public static boolean isAreaCompletelySolid(LevelAccessor world, int x1, int y1, int z1, int x2, int y2, int z2) {
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
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
			double newDistance = Vec3.atLowerCornerOf(pos).distanceToSqr(Vec3.atLowerCornerOf((listOfPositions)));
			if (distance == -1.0D || newDistance < distance) {
				distance = newDistance;
				currentPos = listOfPositions;
			}
		}

		return currentPos;
	}

}
