package com.minecraftabnormals.endergetic.common.blocks;

import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.minecraftabnormals.endergetic.core.events.EntityEvents;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
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
import net.minecraft.world.DimensionType;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.server.ServerWorld;

public class CorrockCrownWallBlock extends CorrockCrownBlock {
	private static final Map<DimensionType, Supplier<CorrockCrownWallBlock>> CONVERSIONS = Util.make(Maps.newHashMap(), (conversions) -> {
		conversions.put(CorrockBlock.DimensionTypeAccessor.OVERWORLD, EEBlocks.CORROCK_CROWN_OVERWORLD_WALL);
		conversions.put(CorrockBlock.DimensionTypeAccessor.THE_NETHER, EEBlocks.CORROCK_CROWN_NETHER_WALL);
		conversions.put(CorrockBlock.DimensionTypeAccessor.THE_END, EEBlocks.CORROCK_CROWN_END_WALL);
	});
	public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
	private static final Map<Direction, VoxelShape> SHAPES = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, Block.makeCuboidShape(0.0D, 4.5D, 14.0D, 16.0D, 12.5D, 16.0D), Direction.SOUTH, Block.makeCuboidShape(0.0D, 4.5D, 0.0D, 16.0D, 12.5D, 2.0D), Direction.EAST, Block.makeCuboidShape(0.0D, 4.5D, 0.0D, 2.0D, 12.5D, 16.0D), Direction.WEST, Block.makeCuboidShape(14.0D, 4.5D, 0.0D, 16.0D, 12.5D, 16.0D)));

	public CorrockCrownWallBlock(Properties builder, boolean petrified) {
		super(builder, petrified);
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(WATERLOGGED, false));
	}

	@Override
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (this.shouldConvert(world)) {
			world.setBlockState(pos, CONVERSIONS.getOrDefault(world.getDimensionType(), EEBlocks.CORROCK_CROWN_OVERWORLD_WALL).get().getDefaultState().with(FACING, world.getBlockState(pos).get(FACING)));
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
		return SHAPES.get(state.get(FACING));
	}

	public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
		Direction direction = state.get(FACING);
		BlockPos blockpos = pos.offset(direction.getOpposite());
		return Block.hasEnoughSolidSide(world, blockpos, direction);
	}

	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		BlockState state = this.getDefaultState();
		FluidState fluidState = context.getWorld().getFluidState(context.getPos());
		IWorldReader iworldreaderbase = context.getWorld();
		BlockPos blockpos = context.getPos();
		Direction[] aDirection = context.getNearestLookingDirections();

		if (this.shouldConvert(context.getWorld())) {
			context.getWorld().getPendingBlockTicks().scheduleTick(context.getPos(), this, 60 + context.getWorld().getRandom().nextInt(40));
		}

		for (Direction Direction : aDirection) {
			if (Direction.getAxis().isHorizontal()) {
				Direction Direction1 = Direction.getOpposite();
				state = state.with(FACING, Direction1);
				if (state.isValidPosition(iworldreaderbase, blockpos) && !(iworldreaderbase.getBlockState(blockpos.offset(state.get(FACING).getOpposite())).getBlock() instanceof CorrockCrownWallBlock) && !(iworldreaderbase.getBlockState(blockpos.offset(state.get(FACING).getOpposite())).getBlock() instanceof CorrockCrownStandingBlock)) {
					return state.with(WATERLOGGED, fluidState.isTagged(FluidTags.WATER) && fluidState.getLevel() >= 8);
				}
			}
		}
		return null;
	}

	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (stateIn.get(WATERLOGGED)) {
			worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
			if (!this.petrified) {
				return EntityEvents.convertCorrockBlock(stateIn);
			}
		} else if (this.shouldConvert(worldIn)) {
			worldIn.getPendingBlockTicks().scheduleTick(currentPos, this, 60 + worldIn.getRandom().nextInt(40));
		}

		if (facing == Direction.DOWN && !stateIn.isValidPosition(worldIn, currentPos)) {
			return Blocks.AIR.getDefaultState();
		}
		return facing.getOpposite() == stateIn.get(FACING) && !stateIn.isValidPosition(worldIn, currentPos) ? Blocks.AIR.getDefaultState() : stateIn;
	}

	public BlockState rotate(BlockState state, Rotation rot) {
		return state.with(FACING, rot.rotate(state.get(FACING)));
	}

	@SuppressWarnings("deprecation")
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.toRotation(state.get(FACING)));
	}

	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING, WATERLOGGED);
	}

	private boolean shouldConvert(IWorld world) {
		return !this.petrified && CONVERSIONS.getOrDefault(world.getDimensionType(), EEBlocks.CORROCK_CROWN_OVERWORLD_WALL).get() != this;
	}
}
