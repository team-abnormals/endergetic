package endergeticexpansion.api.util;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.IWorldGenerationBaseReader;

public class GenerationUtils {

	@SuppressWarnings("deprecation")
	public static boolean isAir(IWorldGenerationBaseReader worldIn, BlockPos pos) {
		if (!(worldIn instanceof net.minecraft.world.IBlockReader))
		return worldIn.hasBlockState(pos, BlockState::isAir);
		else return worldIn.hasBlockState(pos, state -> state.isAir((net.minecraft.world.IBlockReader)worldIn, pos));
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
	
	public static void fillWithRandomTwoBlocksCube(IWorld world, int x1, int y1, int z1, int x2, int y2, int z2, Random rand, BlockState block, BlockState block2, float chance) {
		for(int yy = y1; yy <= y2; yy++) {
			for (int xx = x1; xx <= x2; xx++) {
				for (int zz = z1; zz <= z2; zz++) {
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
	
}
