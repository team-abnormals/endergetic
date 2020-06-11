package endergeticexpansion.common.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.FireBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class EnderFireBlock extends FireBlock {

	public EnderFireBlock(Properties builder) {
		super(builder);
	}
	
	@Override
	public boolean isBurning(BlockState state, IBlockReader world, BlockPos pos) {
		return true;
	}

}
