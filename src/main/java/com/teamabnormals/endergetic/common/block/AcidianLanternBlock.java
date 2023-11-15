package com.teamabnormals.endergetic.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EndRodBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class AcidianLanternBlock extends EndRodBlock implements SimpleWaterloggedBlock {
	//DOWN, UP, NORTH, SOUTH, WEST, EAST
	protected static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	private static final VoxelShape[] SHAPES = new VoxelShape[]{
			Shapes.or(Block.box(6.0D, 8.0D, 6.0D, 10.0D, 16.0D, 10.0D), Block.box(4.0D, 0.0D, 4.0D, 12.0D, 8.0D, 12.0D)),
			Shapes.or(Block.box(6.0D, 0.0D, 6.0D, 10.0D, 8.0D, 10.0D), Block.box(4.0D, 8.0D, 4.0D, 12.0D, 16.0D, 12.0D)),
			Shapes.or(Block.box(6.0D, 6.0D, 8.0D, 10.0D, 10.0D, 16.0D), Block.box(4.0D, 4.0D, 0.0D, 12.0D, 12.0D, 8.0D)),
			Shapes.or(Block.box(6.0D, 6.0D, 0.0D, 10.0D, 10.0D, 8.0D), Block.box(4.0D, 4.0D, 8.0D, 12.0D, 12.0D, 16.0D)),
			Shapes.or(Block.box(8.0D, 6.0D, 6.0D, 16.0D, 10.0D, 10.0D), Block.box(0.0D, 4.0D, 4.0D, 8.0D, 12.0D, 12.0D)),
			Shapes.or(Block.box(0.0D, 6.0D, 6.0D, 8.0D, 10.0D, 10.0D), Block.box(8.0D, 4.0D, 4.0D, 16.0D, 12.0D, 12.0D)),
	};

	public AcidianLanternBlock(Properties builder) {
		super(builder);
		this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, false).setValue(FACING, Direction.UP));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, WATERLOGGED);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return SHAPES[state.getValue(FACING).get3DDataValue()];
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
		return super.getStateForPlacement(context).setValue(WATERLOGGED, fluidState.is(FluidTags.WATER) && fluidState.getAmount() >= 8);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos) {
		if (state.getValue(WATERLOGGED)) {
			world.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
		}
		return state;
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, RandomSource rand) {
		Direction direction = stateIn.getValue(FACING);
		double offset = direction == Direction.UP ? 0.1D : direction == Direction.DOWN ? 1D : 0.5D;
		double xOffset;
		double zOffset;
		switch (direction) {
			default:
			case NORTH:
				xOffset = 0.5D;
				zOffset = -0.1D;
				break;
			case SOUTH:
				xOffset = 0.5D;
				zOffset = 1.1D;
				break;
			case WEST:
				xOffset = -0.1D;
				zOffset = 0.5D;
				break;
			case EAST:
				xOffset = 1.1D;
				zOffset = 0.5D;
				break;
		}
		if (direction == Direction.UP || direction == Direction.DOWN) {
			xOffset = 0.55D;
			zOffset = 0.55D;
		}
		double d0 = pos.getX() + xOffset - (double) (rand.nextFloat() * 0.1F);
		double d1 = pos.getY() + offset - (double) (rand.nextFloat() * 0.1F);
		double d2 = pos.getZ() + zOffset - (double) (rand.nextFloat() * 0.1F);
		if (rand.nextFloat() <= 0.65F) {
			worldIn.addParticle(ParticleTypes.DRAGON_BREATH, d0, d1 + direction.getStepY(), d2, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D);
		}
	}
}