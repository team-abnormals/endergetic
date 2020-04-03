package endergeticexpansion.common.blocks;

import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import endergeticexpansion.core.events.PlayerEvents;
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
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;

public class BlockCorrockCrownWall extends BlockCorrockCrown {
	private static final Map<DimensionType, Supplier<Block>> CONVERSIONS = Util.make(Maps.newHashMap(), (conversions) -> {
		conversions.put(DimensionType.OVERWORLD, () -> EEBlocks.CORROCK_CROWN_OVERWORLD_WALL.get());
		conversions.put(DimensionType.THE_NETHER, () -> EEBlocks.CORROCK_CROWN_NETHER_WALL.get());
		conversions.put(DimensionType.THE_END, () -> EEBlocks.CORROCK_CROWN_END_WALL.get());
	});
	public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
	private static final Map<Direction, VoxelShape> SHAPES = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, Block.makeCuboidShape(0.0D, 4.5D, 14.0D, 16.0D, 12.5D, 16.0D), Direction.SOUTH, Block.makeCuboidShape(0.0D, 4.5D, 0.0D, 16.0D, 12.5D, 2.0D), Direction.EAST, Block.makeCuboidShape(0.0D, 4.5D, 0.0D, 2.0D, 12.5D, 16.0D), Direction.WEST, Block.makeCuboidShape(14.0D, 4.5D, 0.0D, 16.0D, 12.5D, 16.0D)));
	   
	public BlockCorrockCrownWall(Properties builder, boolean petrified) {
		super(builder, petrified);
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(WATERLOGGED, Boolean.valueOf(false)));
	}
	
	@Override
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if(!this.isInProperDimension(world)) {
			world.setBlockState(pos, CONVERSIONS.get(world.getDimension().getType()).get().getDefaultState().with(FACING, world.getBlockState(pos).get(FACING)));
		}
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
		return SHAPES.get(state.get(FACING));
	}
	
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		return worldIn.getBlockState(pos.offset(state.get(FACING).getOpposite())).getMaterial().isSolid();
	}
	
	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		BlockState state = this.getDefaultState();
		IFluidState fluidState = context.getWorld().getFluidState(context.getPos());
		IWorldReader iworldreaderbase = context.getWorld();
		BlockPos blockpos = context.getPos();
		Direction[] aDirection = context.getNearestLookingDirections();
		
		if(!this.petrified && !this.isInProperDimension(context.getWorld())) {
			context.getWorld().getPendingBlockTicks().scheduleTick(context.getPos(), this, 60 + context.getWorld().getRandom().nextInt(40));
		}

		for(Direction Direction : aDirection) {
			if (Direction.getAxis().isHorizontal()) {
				Direction Direction1 = Direction.getOpposite();
				state = state.with(FACING, Direction1);
	            if (state.isValidPosition(iworldreaderbase, blockpos) && !(iworldreaderbase.getBlockState(blockpos.offset(state.get(FACING).getOpposite())).getBlock() instanceof BlockCorrockCrownWall) && !(iworldreaderbase.getBlockState(blockpos.offset(state.get(FACING).getOpposite())).getBlock() instanceof BlockCorrockCrownStanding)) {
	            	return state.with(WATERLOGGED, fluidState.isTagged(FluidTags.WATER) && fluidState.getLevel() >= 8);
	            }
			}
		}
	    return null;
	}

	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		if(stateIn.get(WATERLOGGED)) {
			worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
			if(!this.petrified) {
				return PlayerEvents.convertCorrockBlock(stateIn);
			}
		}
		if(!stateIn.get(WATERLOGGED) && !this.petrified && !this.isInProperDimension(worldIn.getWorld())) {
			worldIn.getPendingBlockTicks().scheduleTick(currentPos, this, 60 + worldIn.getRandom().nextInt(40));
		}
		if(facing == Direction.DOWN && !stateIn.isValidPosition(worldIn, currentPos)) {
	         return Blocks.AIR.getDefaultState();
		}
		return facing.getOpposite() == stateIn.get(FACING) && !stateIn.isValidPosition(worldIn, currentPos) ? Blocks.AIR.getDefaultState() : stateIn;
	}
	
	public boolean isInProperDimension(World world) {
		return !this.petrified && CONVERSIONS.get(world.getDimension().getType()).get() == this;
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
