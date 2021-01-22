package com.minecraftabnormals.endergetic.common.blocks;

import com.minecraftabnormals.endergetic.common.tileentities.EetleEggsTileEntity;
import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;

import javax.annotation.Nullable;
import java.util.Random;

public class EetleEggsBlock extends ContainerBlock implements IWaterLoggable {
	public static final IntegerProperty SIZE = IntegerProperty.create("size", 0, 2);
	public static final DirectionProperty FACING = DirectionalBlock.FACING;
	private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final BooleanProperty PETRIFIED = BooleanProperty.create("petrified");
	private static final VoxelShape[][] SHAPES = new VoxelShape[][] {
			new VoxelShape[] {
					makeCuboidShape(1.0F, 11.0F, 1.0F, 15.0F, 16.0F, 15.0F),
					makeCuboidShape(1.0F, 0.0F, 1.0F, 15.0F, 5.0F, 15.0F),
					makeCuboidShape(15.0F, 1.0F, 16.0F, 1.0F, 15.0F, 11.0F),
					makeCuboidShape(1.0F, 1.0F, 0.0F, 15.0F, 15.0F, 5.0F),
					makeCuboidShape(16.0F, 1.0F, 1.0F, 11.0F, 15.0F, 15.0F),
					makeCuboidShape(0.0F, 1.0F, 1.0F, 5.0F, 15.0F, 15.0F),
			},
			new VoxelShape[] {
					makeCuboidShape(1.0F, 8.0F, 1.0F, 15.0F, 16.0F, 15.0F),
					makeCuboidShape(1.0F, 0.0F, 1.0F, 15.0F, 8.0F, 15.0F),
					makeCuboidShape(15.0F, 1.0F, 16.0F, 1.0F, 15.0F, 8.0F),
					makeCuboidShape(1.0F, 1.0F, 0.0F, 15.0F, 15.0F, 8.0F),
					makeCuboidShape(16.0F, 1.0F, 1.0F, 8.0F, 15.0F, 15.0F),
					makeCuboidShape(0.0F, 1.0F, 1.0F, 8.0F, 15.0F, 15.0F),
			},
			new VoxelShape[] {
					makeCuboidShape(1.0F, 4.0F, 1.0F, 15.0F, 16.0F, 15.0F),
					makeCuboidShape(1.0F, 0.0F, 1.0F, 15.0F, 12.0F, 15.0F),
					makeCuboidShape(15.0F, 1.0F, 16.0F, 1.0F, 15.0F, 4.0F),
					makeCuboidShape(1.0F, 1.0F, 0.0F, 15.0F, 15.0F, 12.0F),
					makeCuboidShape(16.0F, 1.0F, 1.0F, 4.0F, 15.0F, 15.0F),
					makeCuboidShape(0.0F, 1.0F, 1.0F, 12.0F, 15.0F, 15.0F),
			}
	};

	public EetleEggsBlock(Properties properties) {
		super(properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(SIZE, 0).with(FACING, Direction.UP).with(WATERLOGGED, false).with(PETRIFIED, false));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(SIZE, FACING, WATERLOGGED, PETRIFIED);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPES[state.get(SIZE)][state.get(FACING).getIndex()];
	}

	@Nullable
	@Override
	public TileEntity createNewTileEntity(IBlockReader world) {
		return new EetleEggsTileEntity();
	}

	@Override
	public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos) {
		if (!this.isValidPosition(state, world, currentPos)) {
			return Blocks.AIR.getDefaultState();
		} else if (state.get(WATERLOGGED)) {
			world.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(world));
			if (!state.get(PETRIFIED)) {
				return state.with(PETRIFIED, true);
			}
		}
		return state;
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		BlockPos pos = context.getPos();
		BlockState state = context.getWorld().getBlockState(pos);
		if (state.getBlock() == this) {
			return state.with(SIZE, Math.min(2, state.get(SIZE) + 1));
		}
		Direction[] nearestDirections = context.getNearestLookingDirections();
		IWorldReader world = context.getWorld();
		BlockState wallState = this.getWallState(nearestDirections, world, pos);
		for (Direction direction : nearestDirections) {
			BlockState directionState = direction == Direction.UP || direction == Direction.DOWN ? this.getDefaultState().with(SIZE, 0).with(FACING, direction.getOpposite()) : wallState;
			if (directionState != null && directionState.isValidPosition(world, pos)) {
				FluidState fluidState = world.getFluidState(pos);
				boolean waterlogged = fluidState.isTagged(FluidTags.WATER) && fluidState.getLevel() == 8;
				return directionState.with(WATERLOGGED, waterlogged).with(PETRIFIED, waterlogged);
			}
		}
		return null;
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		Direction facing = state.get(FACING);
		BlockPos blockpos = pos.offset(facing.getOpposite());
		return isOnValidState(worldIn, worldIn.getBlockState(blockpos), blockpos, facing);
	}

	@Nullable
	private BlockState getWallState(Direction[] nearestDirections, IWorldReader world, BlockPos pos) {
		for (Direction direction : nearestDirections) {
			if (direction.getAxis().isHorizontal()) {
				BlockState state = this.getDefaultState().with(FACING, direction.getOpposite());
				if (state.isValidPosition(world, pos)) {
					return state;
				}
			}
		}
		return null;
	}

	@Override
	public boolean isReplaceable(BlockState state, BlockItemUseContext useContext) {
		return useContext.getItem().getItem() == this.asItem() && state.get(SIZE) < 2;
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : Fluids.EMPTY.getDefaultState();
	}

	@Override
	public boolean receiveFluid(IWorld world, BlockPos pos, BlockState state, FluidState fluidStateIn) {
		if (!state.get(BlockStateProperties.WATERLOGGED) && fluidStateIn.getFluid() == Fluids.WATER) {
			if (!world.isRemote()) {
				world.setBlockState(pos, state.with(BlockStateProperties.WATERLOGGED, true).with(PETRIFIED, true), 3);
				world.getPendingFluidTicks().scheduleTick(pos, fluidStateIn.getFluid(), fluidStateIn.getFluid().getTickRate(world));
			}
			return true;
		}
		return false;
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	private static boolean isOnValidState(IWorldReader world, BlockState state, BlockPos pos, Direction direction) {
		return !state.getCollisionShape(world, pos).project(direction).isEmpty() || state.isSolidSide(world, pos, direction);
	}

	public static void shuffleDirections(Direction[] directions, Random random) {
		int length = directions.length;
		for (int i = length; i > 1; i--) {
			int offset1 = Math.max(0, i - 1);
			int offset2 = Math.max(0, random.nextInt(i));
			if (offset1 >= length || offset2 >= length) {
				return;
			}
			int swaps = Math.min(Math.min(1, length - offset1), length - offset2);
			for (int j = 0; j < swaps; j++, offset1++, offset2++) {
				Direction direction = directions[offset1];
				directions[offset1] = directions[offset2];
				directions[offset2] = direction;
			}
		}
	}
}
