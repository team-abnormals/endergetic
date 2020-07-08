package endergeticexpansion.common.blocks;

import endergeticexpansion.core.registry.other.EETags;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;

public class EnderFireBlock extends AbstractFireBlock {

	public EnderFireBlock(Properties builder) {
		super(builder, 3.0F);
	}

	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		return this.isValidPosition(stateIn, worldIn, currentPos) ? this.getDefaultState() : Blocks.AIR.getDefaultState();
	}

	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		return isEnderFireBase(worldIn.getBlockState(pos.down()).getBlock());
	}

	public static boolean isEnderFireBase(Block block) {
		return block.isIn(EETags.Blocks.ENDER_FIRE_BASE_BLOCKS);
	}

	protected boolean canBurn(BlockState stateIn) {
		return true;
	}
	
}