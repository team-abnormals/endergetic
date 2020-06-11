package endergeticexpansion.common.blocks;

import endergeticexpansion.common.tileentities.EndStoneCoverTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.HorizontalBlock;

import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;

public class EndStoneCoverBlock extends ContainerBlock {
	public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
	public static final VoxelShape[] SHAPES = new VoxelShape[] {
		Block.makeCuboidShape(0, 0, 14, 16, 16, 16),
		Block.makeCuboidShape(0, 0, 0, 2, 16, 16),
		Block.makeCuboidShape(0, 0, 0, 16, 16, 2),
		Block.makeCuboidShape(14, 0, 0, 16, 16, 16),
	};

	public EndStoneCoverBlock(Properties builder) {
		super(builder);
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH));
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPES[state.get(FACING).getHorizontalIndex()];
	}
	
	@Override
	public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
		return world.getBlockState(pos.offset(state.get(FACING))).getBlock() == Blocks.END_STONE;
	}
	
	@Override
	public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos) {
		return this.isValidPosition(state, world, currentPos) ? state : Blocks.AIR.getDefaultState();
	}
	
	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		return new EndStoneCoverTileEntity();
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
}