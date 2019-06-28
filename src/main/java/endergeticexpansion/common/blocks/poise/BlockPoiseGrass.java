package endergeticexpansion.common.blocks.poise;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.lighting.LightEngine;
import net.minecraftforge.common.ToolType;

public class BlockPoiseGrass extends Block {

	public BlockPoiseGrass(Properties properties) {
		super(properties);
	}
	
	@Override
	public ToolType getHarvestTool(BlockState state) {
		return ToolType.PICKAXE;
	}
	
	@Override
	public void tick(BlockState state, World worldIn, BlockPos pos, Random random) {
		if (!worldIn.isRemote) {
			if (!worldIn.isAreaLoaded(pos, 3)) return;
			if (!func_220257_b(state, worldIn, pos)) {
				worldIn.setBlockState(pos, Blocks.END_STONE.getDefaultState());
			} else {
				
			}
		}
	}
	
	private static boolean func_220257_b(BlockState p_220257_0_, IWorldReader p_220257_1_, BlockPos p_220257_2_) {
		BlockPos blockpos = p_220257_2_.up();
		BlockState blockstate = p_220257_1_.getBlockState(blockpos);
		int i = LightEngine.func_215613_a(p_220257_1_, p_220257_0_, p_220257_2_, blockstate, blockpos, Direction.UP, blockstate.getOpacity(p_220257_1_, blockpos));
		return i < p_220257_1_.getMaxLightLevel();
	}
	
	@Override
	public int getHarvestLevel(BlockState state) {
		return 2;
	}
	
	@Override
	public SoundType getSoundType(BlockState p_220072_1_) {
		return SoundType.STONE;
	}

	public boolean isSolid(BlockState state) {
		return true;
	}

	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
	
}
