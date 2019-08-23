package endergeticexpansion.common.blocks.poise;

import java.util.Random;

import endergeticexpansion.core.registry.EEBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IGrowable;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.lighting.LightEngine;
import net.minecraftforge.common.ToolType;

@SuppressWarnings("deprecation")
public class BlockPoiseEumusGrass extends Block implements IGrowable {

	public BlockPoiseEumusGrass(Properties properties) {
		super(properties);
	}
	
	@Override
	public ToolType getHarvestTool(BlockState state) {
		return ToolType.SHOVEL;
	}

	@Override
	public void tick(BlockState state, World worldIn, BlockPos pos, Random random) {
		if (!worldIn.isRemote) {
            if (!worldIn.isAreaLoaded(pos, 3)) return;
            if (!func_220257_b(state, worldIn, pos)) {
                worldIn.setBlockState(pos, EEBlocks.EUMUS.getDefaultState());
            } else {
            	for (int i = 0; i < 4; ++i) {
            		BlockPos blockpos = pos.add(worldIn.rand.nextInt(3) - 1, worldIn.rand.nextInt(5) - 3, worldIn.rand.nextInt(3) - 1);

                    if (blockpos.getY() >= 0 && blockpos.getY() < 256 && !worldIn.isBlockLoaded(blockpos)) {
                    	return;
                    }

                    BlockState iblockstate = worldIn.getBlockState(blockpos.up());
                    BlockState iblockstate1 = worldIn.getBlockState(blockpos);

                    if (iblockstate1.getBlock() == EEBlocks.EUMUS && func_220257_b(iblockstate, worldIn, blockpos)) {
                        worldIn.setBlockState(blockpos, this.getDefaultState());
                    }
                }
            }
        }
		super.tick(state, worldIn, pos, random);
	}
	
	private static boolean func_220257_b(BlockState p_220257_0_, IWorldReader p_220257_1_, BlockPos p_220257_2_) {
		BlockPos blockpos = p_220257_2_.up();
		BlockState blockstate = p_220257_1_.getBlockState(blockpos);
		int i = LightEngine.func_215613_a(p_220257_1_, p_220257_0_, p_220257_2_, blockstate, blockpos, Direction.UP, blockstate.getOpacity(p_220257_1_, blockpos));
		return i < p_220257_1_.getMaxLightLevel();
	}
	
	public boolean canGrow(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient) {
		return worldIn.getBlockState(pos.up()).isAir();
	}
	
	@Override
	public void grow(World worldIn, Random rand, BlockPos pos, BlockState state) {
		BlockPos blockpos = pos.up();
		BlockState blockstate = EEBlocks.POISE_GRASS.getDefaultState();

		for(int i = 0; i < 128; ++i) {
			BlockPos blockpos1 = blockpos;
			int j = 0;

			while(true) {
				if (j >= i / 16) {
					BlockState blockstate2 = worldIn.getBlockState(blockpos1);
					if (blockstate2.getBlock() == blockstate.getBlock() && rand.nextInt(10) == 0) {
						((IGrowable)blockstate.getBlock()).grow(worldIn, rand, blockpos1, blockstate2);
					}

					if (!blockstate2.isAir()) {
						break;
					}

					BlockState blockstate1;
					blockstate1 = blockstate;

				if (blockstate1.isValidPosition(worldIn, blockpos1)) {
					worldIn.setBlockState(blockpos1, blockstate1, 3);
				}
				break;
			}

			blockpos1 = blockpos1.add(rand.nextInt(3) - 1, (rand.nextInt(3) - 1) * rand.nextInt(3) / 2, rand.nextInt(3) - 1);
			if (worldIn.getBlockState(blockpos1.down()).getBlock() != this || worldIn.getBlockState(blockpos1).func_224756_o(worldIn, blockpos1)) {
				break;
			}

			++j;
			}
		}
	}

	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, BlockState state) {
		return true;
	}
	
	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
	
}
