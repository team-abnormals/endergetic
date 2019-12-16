package endergeticexpansion.common.blocks;

import java.util.Map;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import endergeticexpansion.core.registry.EEBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;

public class BlockCorrockCrownWall extends BlockCorrockCrown {
	public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
	private static final Map<Direction, VoxelShape> SHAPES = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, Block.makeCuboidShape(0.0D, 4.5D, 14.0D, 16.0D, 12.5D, 16.0D), Direction.SOUTH, Block.makeCuboidShape(0.0D, 4.5D, 0.0D, 16.0D, 12.5D, 2.0D), Direction.EAST, Block.makeCuboidShape(0.0D, 4.5D, 0.0D, 2.0D, 12.5D, 16.0D), Direction.WEST, Block.makeCuboidShape(14.0D, 4.5D, 0.0D, 16.0D, 12.5D, 16.0D)));
	   
	public BlockCorrockCrownWall(Properties builder) {
		super(builder);
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(WATERLOGGED, Boolean.valueOf(false)));
	}
	
	@Override
	public void tick(BlockState state, World worldIn, BlockPos pos, Random random) {
		if(!this.isInProperDimension(worldIn) && !this.isSubmerged(worldIn, pos)) {
			worldIn.setBlockState(pos, this.getCorrockBlockForDimension(worldIn.getDimension())
				.with(FACING, worldIn.getBlockState(pos).get(FACING)));
		}
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
		return SHAPES.get(state.get(FACING));
	}
	
	public BlockState getCorrockBlockForDimension(Dimension dimension) {
		switch(dimension.getType().getId()) {
			case 0:
				return EEBlocks.CORROCK_CROWN_OVERWORLD_WALL.get().getDefaultState();
			case 1:
				return EEBlocks.CORROCK_CROWN_END_WALL.get().getDefaultState();
			case -1:
				return EEBlocks.CORROCK_CROWN_NETHER_WALL.get().getDefaultState();
		}
		return null;
	}
	
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		return worldIn.getBlockState(pos.offset(state.get(FACING).getOpposite())).getMaterial().isSolid();
	}
	
	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		BlockState BlockState = this.getDefaultState();
		IFluidState ifluidstate = context.getWorld().getFluidState(context.getPos());
		IWorldReader iworldreaderbase = context.getWorld();
		BlockPos blockpos = context.getPos();
		Direction[] aDirection = context.getNearestLookingDirections();
		
		if (!this.isInProperDimension(context.getWorld())) {
			context.getWorld().getPendingBlockTicks().scheduleTick(context.getPos(), this, 60 + context.getWorld().getRandom().nextInt(40));
		}

		for(Direction Direction : aDirection) {
			if (Direction.getAxis().isHorizontal()) {
				Direction Direction1 = Direction.getOpposite();
	            BlockState = BlockState.with(FACING, Direction1);
	            if (BlockState.isValidPosition(iworldreaderbase, blockpos) && !(iworldreaderbase.getBlockState(blockpos.offset(BlockState.get(FACING).getOpposite())).getBlock() instanceof BlockCorrockCrownWall) && !(iworldreaderbase.getBlockState(blockpos.offset(BlockState.get(FACING).getOpposite())).getBlock() instanceof BlockCorrockCrownStanding)) {
	            	return BlockState.with(WATERLOGGED, Boolean.valueOf(ifluidstate.getFluid() == Fluids.WATER));
	            }
			}
		}
	    return null;
	}

	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (!this.isInProperDimension(worldIn.getWorld())) {
			worldIn.getPendingBlockTicks().scheduleTick(currentPos, this, 60 + worldIn.getRandom().nextInt(40));
		}
		if (facing == Direction.DOWN && !stateIn.isValidPosition(worldIn, currentPos)) {
	         return Blocks.AIR.getDefaultState();
		}
		return facing.getOpposite() == stateIn.get(FACING) && !stateIn.isValidPosition(worldIn, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	public BlockState rotate(BlockState state, Rotation rot) {
		return state.with(FACING, rot.rotate(state.get(FACING)));
	}
	
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.toRotation(state.get(FACING)));
	}
	
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING, WATERLOGGED);
	}
}
