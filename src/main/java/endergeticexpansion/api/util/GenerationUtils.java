package endergeticexpansion.api.util;

import java.util.Arrays;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.FourWayBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.IWorldGenerationBaseReader;

public class GenerationUtils {
	
	public static <B extends BlockState> BlockState getWallPlaceState(B state, IWorld world, BlockPos pos) {
		BlockPos blockpos1 = pos.north();
		BlockPos blockpos2 = pos.east();
		BlockPos blockpos3 = pos.south();
		BlockPos blockpos4 = pos.west();
		BlockState blockstate = world.getBlockState(blockpos1);
		BlockState blockstate1 = world.getBlockState(blockpos2);
		BlockState blockstate2 = world.getBlockState(blockpos3);
		BlockState blockstate3 = world.getBlockState(blockpos4);
		boolean flag = getWallFlag(blockstate, blockstate.func_224755_d(world, blockpos1, Direction.SOUTH), Direction.SOUTH);
		boolean flag1 = getWallFlag(blockstate1, blockstate1.func_224755_d(world, blockpos2, Direction.WEST), Direction.WEST);
		boolean flag2 = getWallFlag(blockstate2, blockstate2.func_224755_d(world, blockpos3, Direction.NORTH), Direction.NORTH);
		boolean flag3 = getWallFlag(blockstate3, blockstate3.func_224755_d(world, blockpos4, Direction.EAST), Direction.EAST);
		boolean flag4 = (!flag || flag1 || !flag2 || flag3) && (flag || !flag1 || flag2 || !flag3);
		return state.with(WallBlock.UP, Boolean.valueOf(flag4 || !world.isAirBlock(pos.up()))).with(FourWayBlock.NORTH, flag).with(FourWayBlock.EAST, flag1).with(FourWayBlock.SOUTH, flag2).with(FourWayBlock.WEST, flag3);
	}
	
	public static boolean getWallFlag(BlockState p_220113_1_, boolean p_220113_2_, Direction p_220113_3_) {
		Block block = p_220113_1_.getBlock();
		boolean flag = block.isIn(BlockTags.WALLS) || block instanceof FenceGateBlock && FenceGateBlock.isParallel(p_220113_1_, p_220113_3_);
		return !Block.cannotAttach(block) && p_220113_2_ || flag;
	}

	@SuppressWarnings("deprecation")
	public static boolean isAir(IWorldGenerationBaseReader worldIn, BlockPos pos) {
		if (!(worldIn instanceof net.minecraft.world.IBlockReader)) return worldIn.hasBlockState(pos, BlockState::isAir);
		else return worldIn.hasBlockState(pos, state -> state.isAir((net.minecraft.world.IBlockReader)worldIn, pos));
	}
	
	public static <B extends BlockState> boolean isProperBlock(B blockState, Block[] validBlocks, boolean orSolid) {
		if(!Arrays.asList(validBlocks).contains(blockState.getBlock()) && orSolid) {
			return blockState.isSolid();
		}
    	return Arrays.asList(validBlocks).contains(blockState.getBlock());
    }
	
	public static void fillAreaWithBlockCube(IWorld world, int x1, int y1, int z1, int x2, int y2, int z2, BlockState block) {
		for(int yy = y1; yy <= y2; yy++) {
			for (int xx = x1; xx <= x2; xx++) {
				for (int zz = z1; zz <= z2; zz++) {
					if(world.getBlockState(new BlockPos(xx, yy, zz)).getMaterial().isReplaceable()) {
						world.setBlockState(new BlockPos(xx, yy, zz), block, 2);
					}
				}
			}
		}
	}
	
	public static void fillAreaWithBlockCubeEdged(IWorld world, int x1, int y1, int z1, int x2, int y2, int z2, BlockState block) {
		for(int yy = y1; yy <= y2; yy++) {
			for (int xx = x1; xx <= x2; xx++) {
				for (int zz = z1; zz <= z2; zz++) {
					if(world.getBlockState(new BlockPos(xx, yy, zz)).getMaterial().isReplaceable() && (xx == x2 || zz == z2)) {
						world.setBlockState(new BlockPos(xx, yy, zz), block, 2);
					}
				}
			}
		}
	}
	
	public static void fillAreaWithBlockCubeUnsafe(IWorld world, int x1, int y1, int z1, int x2, int y2, int z2, BlockState block) {
		for(int yy = y1; yy <= y2; yy++) {
			for (int xx = x1; xx <= x2; xx++) {
				for (int zz = z1; zz <= z2; zz++) {
					world.setBlockState(new BlockPos(xx, yy, zz), block, 2);
				}
			}
		}
	}
	
	public static void fillAreaWithBlockCubeUnsafeReverse(IWorld world, int x1, int y1, int z1, int x2, int y2, int z2, BlockState block) {
		for(int yy = y1; yy <= y2; yy++) {
			for (int xx = x1; xx >= x2; xx--) {
				for (int zz = z1; zz >= z2; zz--) {
					world.setBlockState(new BlockPos(xx, yy, zz), block, 2);
				}
			}
		}
	}
	
	public static void fillWithRandomTwoBlocksCube(IWorld world, int x1, int y1, int z1, int x2, int y2, int z2, Random rand, BlockState block, BlockState block2, float chance) {
		for(int yy = y1; yy <= y2; yy++) {
			for(int xx = x1; xx <= x2; xx++) {
				for(int zz = z1; zz <= z2; zz++) {
					if(world.getBlockState(new BlockPos(xx, yy, zz)).getMaterial().isReplaceable()) {
						if(rand.nextFloat() <= chance) {
							world.setBlockState(new BlockPos(xx, yy, zz), block2, 2);
						} else {
							world.setBlockState(new BlockPos(xx, yy, zz), block, 2);
						}
					}
				}
			}
		}
	}
	
	public static boolean isAreaReplacable(IWorld world, int x1, int y1, int z1, int x2, int y2, int z2) {
		for(int yy = y1; yy <= y2; yy++) {
			for(int xx = x1; xx <= x2; xx++) {
				for(int zz = z1; zz <= z2; zz++) {
					if(!world.getBlockState(new BlockPos(xx, yy, zz)).getMaterial().isReplaceable()) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	public static boolean isAreaCompletelySolid(IWorld world, int x1, int y1, int z1, int x2, int y2, int z2) {
		for(int yy = y1; yy <= y2; yy++) {
			for(int xx = x1; xx <= x2; xx++) {
				for(int zz = z1; zz <= z2; zz++) {
					if(world.getBlockState(new BlockPos(xx, yy, zz)).getMaterial().isReplaceable()) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
}
