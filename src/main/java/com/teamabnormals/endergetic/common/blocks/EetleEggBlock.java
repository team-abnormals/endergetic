package com.teamabnormals.endergetic.common.blocks;

import com.teamabnormals.endergetic.common.tileentities.EetleEggTileEntity;
import com.teamabnormals.endergetic.core.registry.EETileEntities;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;

public class EetleEggBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
	public static final IntegerProperty SIZE = IntegerProperty.create("size", 0, 2);
	public static final DirectionProperty FACING = DirectionalBlock.FACING;
	private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final BooleanProperty PETRIFIED = BooleanProperty.create("petrified");
	private static final VoxelShape[][] SHAPES = new VoxelShape[][] {
			new VoxelShape[] {
					box(1.0F, 11.0F, 1.0F, 15.0F, 16.0F, 15.0F),
					box(1.0F, 0.0F, 1.0F, 15.0F, 5.0F, 15.0F),
					box(1.0F, 1.0F, 11.0F, 15.0F, 15.0F, 16.0F),
					box(1.0F, 1.0F, 0.0F, 15.0F, 15.0F, 5.0F),
					box(11.0F, 1.0F, 1.0F, 16.0F, 15.0F, 15.0F),
					box(0.0F, 1.0F, 1.0F, 5.0F, 15.0F, 15.0F),
			},
			new VoxelShape[] {
					box(1.0F, 8.0F, 1.0F, 15.0F, 16.0F, 15.0F),
					box(1.0F, 0.0F, 1.0F, 15.0F, 8.0F, 15.0F),
					box(1.0F, 1.0F, 8.0F, 15.0F, 15.0F, 16.0F),
					box(1.0F, 1.0F, 0.0F, 15.0F, 15.0F, 8.0F),
					box(8.0F, 1.0F, 1.0F, 16.0F, 15.0F, 15.0F),
					box(0.0F, 1.0F, 1.0F, 8.0F, 15.0F, 15.0F),
			},
			new VoxelShape[] {
					box(1.0F, 4.0F, 1.0F, 15.0F, 16.0F, 15.0F),
					box(1.0F, 0.0F, 1.0F, 15.0F, 12.0F, 15.0F),
					box(1.0F, 1.0F, 4.0F, 15.0F, 15.0F, 16.0F),
					box(1.0F, 1.0F, 0.0F, 15.0F, 15.0F, 12.0F),
					box(4.0F, 1.0F, 1.0F, 16.0F, 15.0F, 15.0F),
					box(0.0F, 1.0F, 1.0F, 12.0F, 15.0F, 15.0F),
			}
	};

	public EetleEggBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(SIZE, 0).setValue(FACING, Direction.UP).setValue(WATERLOGGED, false).setValue(PETRIFIED, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(SIZE, FACING, WATERLOGGED, PETRIFIED);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return SHAPES[state.getValue(SIZE)][state.getValue(FACING).get3DDataValue()];
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new EetleEggTileEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, EETileEntities.EETLE_EGG.get(), EetleEggTileEntity::tick);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos) {
		if (!this.canSurvive(state, world, currentPos)) {
			return Blocks.AIR.defaultBlockState();
		} else if (state.getValue(WATERLOGGED)) {
			world.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
			if (!state.getValue(PETRIFIED)) {
				return state.setValue(PETRIFIED, true);
			}
		}
		return state;
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockPos pos = context.getClickedPos();
		BlockState state = context.getLevel().getBlockState(pos);
		if (state.getBlock() == this) {
			return state.setValue(SIZE, Math.min(2, state.getValue(SIZE) + 1));
		}
		Direction[] nearestDirections = context.getNearestLookingDirections();
		LevelReader world = context.getLevel();
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
	public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
		Direction facing = state.getValue(FACING);
		BlockPos blockpos = pos.relative(facing.getOpposite());
		return isOnValidState(worldIn, worldIn.getBlockState(blockpos), blockpos, facing);
	}

	@Nullable
	private BlockState getWallState(Direction[] nearestDirections, LevelReader world, BlockPos pos) {
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
	public boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
		return useContext.getItemInHand().getItem() == this.asItem() && state.getValue(SIZE) < 2;
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
	}

	@Override
	public boolean placeLiquid(LevelAccessor world, BlockPos pos, BlockState state, FluidState fluidStateIn) {
		if (!state.getValue(BlockStateProperties.WATERLOGGED) && fluidStateIn.getType() == Fluids.WATER) {
			if (!world.isClientSide()) {
				world.setBlock(pos, state.setValue(BlockStateProperties.WATERLOGGED, true).setValue(PETRIFIED, true), 3);
				world.scheduleTick(pos, fluidStateIn.getType(), fluidStateIn.getType().getTickDelay(world));
			}
			return true;
		}
		return false;
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}

	private static boolean isOnValidState(LevelReader world, BlockState state, BlockPos pos, Direction direction) {
		return !state.getCollisionShape(world, pos).getFaceShape(direction).isEmpty() || state.isFaceSturdy(world, pos, direction);
	}

	public static void shuffleDirections(Direction[] directions, RandomSource random) {
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
