package endergeticexpansion.common.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.FireBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockEnderFire extends FireBlock {

	public BlockEnderFire(Properties builder) {
		super(builder);
	}

	@Override
	public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {}
	
}
