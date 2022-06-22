package com.minecraftabnormals.endergetic.common.blocks;

import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;
import com.minecraftabnormals.endergetic.core.events.EntityEvents;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.SoundType;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.DimensionType;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.server.ServerWorld;

import net.minecraft.block.AbstractBlock.OffsetType;
import net.minecraft.block.AbstractBlock.Properties;

public class CorrockPlantBlock extends Block implements IWaterLoggable {
	private static final Map<DimensionType, Supplier<Block>> CONVERSIONS = Util.make(Maps.newHashMap(), (conversions) -> {
		conversions.put(CorrockBlock.DimensionTypeAccessor.OVERWORLD, EEBlocks.CORROCK_OVERWORLD);
		conversions.put(CorrockBlock.DimensionTypeAccessor.THE_NETHER, EEBlocks.CORROCK_NETHER);
		conversions.put(CorrockBlock.DimensionTypeAccessor.THE_END, EEBlocks.CORROCK_END);
	});
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 15.0D, 14.0D);
	public final boolean petrified;

	public CorrockPlantBlock(Properties properties, boolean petrified) {
		super(properties);
		this.petrified = petrified;
		this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, false));
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(WATERLOGGED);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}

	@Override
	public SoundType getSoundType(BlockState state, IWorldReader world, BlockPos pos, Entity entity) {
		return SoundType.CORAL_BLOCK;
	}

	@Override
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
		if (this.shouldConvert(world)) {
			world.setBlockAndUpdate(pos, CONVERSIONS.getOrDefault(world.dimensionType(), EEBlocks.CORROCK_OVERWORLD).get().defaultBlockState());
		}
	}

	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (facing == Direction.DOWN && !stateIn.canSurvive(worldIn, currentPos)) {
			return Blocks.AIR.defaultBlockState();
		} else {
			if (stateIn.getValue(WATERLOGGED)) {
				worldIn.getLiquidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
				if (!this.petrified) {
					return EntityEvents.convertCorrockBlock(stateIn);
				}
			}

			if (this.shouldConvert(worldIn)) {
				worldIn.getBlockTicks().scheduleTick(currentPos, this, 60 + worldIn.getRandom().nextInt(40));
			}
			return stateIn;
		}
	}

	@Override
	public boolean canSurvive(BlockState state, IWorldReader worldIn, BlockPos pos) {
		BlockPos blockpos = pos.below();
		return worldIn.getBlockState(blockpos).isFaceSturdy(worldIn, blockpos, Direction.UP);
	}

	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		if (this.shouldConvert(context.getLevel())) {
			context.getLevel().getBlockTicks().scheduleTick(context.getClickedPos(), this, 60 + context.getLevel().getRandom().nextInt(40));
		}
		FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
		return this.defaultBlockState().setValue(WATERLOGGED, fluidState.is(FluidTags.WATER) && fluidState.getAmount() >= 8);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
	}

	@Override
	public OffsetType getOffsetType() {
		return OffsetType.NONE;
	}

	private boolean shouldConvert(IWorld world) {
		return !this.petrified && CONVERSIONS.getOrDefault(world.dimensionType(), EEBlocks.CORROCK_OVERWORLD).get() != this;
	}
}