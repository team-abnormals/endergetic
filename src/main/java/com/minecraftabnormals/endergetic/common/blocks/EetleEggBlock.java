package com.minecraftabnormals.endergetic.common.blocks;

import com.minecraftabnormals.endergetic.common.tileentities.EetleEggTileEntity;
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

import net.minecraft.block.AbstractBlock.Properties;

public class EetleEggBlock extends ContainerBlock implements IWaterLoggable {
	public static final IntegerProperty SIZE = IntegerProperty.create("size", 0, 2);
	public static final DirectionProperty FACING = DirectionalBlock.FACING;
	private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final BooleanProperty PETRIFIED = BooleanProperty.create("petrified");
	private static final VoxelShape[][] SHAPES = new VoxelShape[][] {
			new VoxelShape[] {
					box(1.0F, 11.0F, 1.0F, 15.0F, 16.0F, 15.0F),
					box(1.0F, 0.0F, 1.0F, 15.0F, 5.0F, 15.0F),
					box(15.0F, 1.0F, 16.0F, 1.0F, 15.0F, 11.0F),
					box(1.0F, 1.0F, 0.0F, 15.0F, 15.0F, 5.0F),
					box(16.0F, 1.0F, 1.0F, 11.0F, 15.0F, 15.0F),
					box(0.0F, 1.0F, 1.0F, 5.0F, 15.0F, 15.0F),
			},
			new VoxelShape[] {
					box(1.0F, 8.0F, 1.0F, 15.0F, 16.0F, 15.0F),
					box(1.0F, 0.0F, 1.0F, 15.0F, 8.0F, 15.0F),
					box(15.0F, 1.0F, 16.0F, 1.0F, 15.0F, 8.0F),
					box(1.0F, 1.0F, 0.0F, 15.0F, 15.0F, 8.0F),
					box(16.0F, 1.0F, 1.0F, 8.0F, 15.0F, 15.0F),
					box(0.0F, 1.0F, 1.0F, 8.0F, 15.0F, 15.0F),
			},
			new VoxelShape[] {
					box(1.0F, 4.0F, 1.0F, 15.0F, 16.0F, 15.0F),
					box(1.0F, 0.0F, 1.0F, 15.0F, 12.0F, 15.0F),
					box(15.0F, 1.0F, 16.0F, 1.0F, 15.0F, 4.0F),
					box(1.0F, 1.0F, 0.0F, 15.0F, 15.0F, 12.0F),
					box(16.0F, 1.0F, 1.0F, 4.0F, 15.0F, 15.0F),
					box(0.0F, 1.0F, 1.0F, 12.0F, 15.0F, 15.0F),
			}
	};

	public EetleEggBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(SIZE, 0).setValue(FACING, Direction.UP).setValue(WATERLOGGED, false).setValue(PETRIFIED, false));
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(SIZE, FACING, WATERLOGGED, PETRIFIED);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPES[state.getValue(SIZE)][state.getValue(FACING).get3DDataValue()];
	}

	@Nullable
	@Override
	public TileEntity newBlockEntity(IBlockReader world) {
		return new EetleEggTileEntity();
	}

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos) {
		if (!this.canSurvive(state, world, currentPos)) {
			return Blocks.AIR.defaultBlockState();
		} else if (state.getValue(WATERLOGGED)) {
			world.getLiquidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
			if (!state.getValue(PETRIFIED)) {
				return state.setValue(PETRIFIED, true);
			}
		}
		return state;
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		BlockPos pos = context.getClickedPos();
		BlockState state = context.getLevel().getBlockState(pos);
		if (state.getBlock() == this) {
			return state.setValue(SIZE, Math.min(2, state.getValue(SIZE) + 1));
		}
		Direction[] nearestDirections = context.getNearestLookingDirections();
		IWorldReader world = context.getLevel();
		BlockState wallState = this.getWallState(nearestDirections, world, pos);
		for (Direction direction : nearestDirections) {
			BlockState directionState = direction == Direction.UP || direction == Direction.DOWN ? this.defaultBlockState().setValue(SIZE, 0).setValue(FACING, direction.getOpposite()) : wallState;
			if (directionState != null && directionState.canSurvive(world, pos)) {
				FluidState fluidState = world.getFluidState(pos);
				boolean waterlogged = fluidState.is(FluidTags.WATER) && fluidState.getAmount() == 8;
				return directionState.setValue(WATERLOGGED, waterlogged).setValue(PETRIFIED, waterlogged);
			}
		}
		return null;
	}

	@Override
	public boolean canSurvive(BlockState state, IWorldReader worldIn, BlockPos pos) {
		Direction facing = state.getValue(FACING);
		BlockPos blockpos = pos.relative(facing.getOpposite());
		return isOnValidState(worldIn, worldIn.getBlockState(blockpos), blockpos, facing);
	}

	@Nullable
	private BlockState getWallState(Direction[] nearestDirections, IWorldReader world, BlockPos pos) {
		for (Direction direction : nearestDirections) {
			if (direction.getAxis().isHorizontal()) {
				BlockState state = this.defaultBlockState().setValue(FACING, direction.getOpposite());
				if (state.canSurvive(world, pos)) {
					return state;
				}
			}
		}
		return null;
	}

	@Override
	public boolean canBeReplaced(BlockState state, BlockItemUseContext useContext) {
		return useContext.getItemInHand().getItem() == this.asItem() && state.getValue(SIZE) < 2;
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
	}

	@Override
	public boolean placeLiquid(IWorld world, BlockPos pos, BlockState state, FluidState fluidStateIn) {
		if (!state.getValue(BlockStateProperties.WATERLOGGED) && fluidStateIn.getType() == Fluids.WATER) {
			if (!world.isClientSide()) {
				world.setBlock(pos, state.setValue(BlockStateProperties.WATERLOGGED, true).setValue(PETRIFIED, true), 3);
				world.getLiquidTicks().scheduleTick(pos, fluidStateIn.getType(), fluidStateIn.getType().getTickDelay(world));
			}
			return true;
		}
		return false;
	}

	@Override
	public BlockRenderType getRenderShape(BlockState state) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	private static boolean isOnValidState(IWorldReader world, BlockState state, BlockPos pos, Direction direction) {
		return !state.getCollisionShape(world, pos).getFaceShape(direction).isEmpty() || state.isFaceSturdy(world, pos, direction);
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
