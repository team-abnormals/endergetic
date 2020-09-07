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
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class CorrockPlantBlock extends Block implements IWaterLoggable {
	private static final Map<DimensionType, Supplier<Block>> CONVERSIONS = Util.make(Maps.newHashMap(), (conversions) -> {
		conversions.put(CorrockBlock.DimensionTypeAccessor.OVERWORLD, () -> EEBlocks.CORROCK_OVERWORLD.get());
		conversions.put(CorrockBlock.DimensionTypeAccessor.THE_NETHER, () -> EEBlocks.CORROCK_NETHER.get());
		conversions.put(CorrockBlock.DimensionTypeAccessor.THE_END, () -> EEBlocks.CORROCK_END.get());
	});
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 15.0D, 14.0D);
	public final boolean petrified;
	
	public CorrockPlantBlock(Properties properties, boolean petrified) {
		super(properties);
		this.petrified = petrified;
		this.setDefaultState(this.stateContainer.getBaseState().with(WATERLOGGED, false));
	}
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(WATERLOGGED);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}
	
	@Override
	public SoundType getSoundType(BlockState state, IWorldReader world, BlockPos pos, Entity entity) {
		return SoundType.CORAL;
	}
	
	@Override
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
		if (!this.petrified && !this.isInProperDimension(world)) {
			world.setBlockState(pos, CONVERSIONS.getOrDefault(world.func_230315_m_(), EEBlocks.CORROCK_OVERWORLD).get().getDefaultState());
		}
	}
	
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (facing == Direction.DOWN && !stateIn.isValidPosition(worldIn, currentPos)) {
			return Blocks.AIR.getDefaultState();
		} else {
			if (stateIn.get(WATERLOGGED)) {
				worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
				if (!this.petrified) {
					return EntityEvents.convertCorrockBlock(stateIn);
				}
			}
			
			if (!this.isInProperDimension(worldIn.getWorld())) {
				worldIn.getPendingBlockTicks().scheduleTick(currentPos, this, 60 + worldIn.getRandom().nextInt(40));
			}
			return stateIn;
		}
	}
	
	@Override
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		BlockPos blockpos = pos.down();
		return worldIn.getBlockState(blockpos).isSolidSide(worldIn, blockpos, Direction.UP);
	}
	
	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		if (!this.isInProperDimension(context.getWorld())) {
			context.getWorld().getPendingBlockTicks().scheduleTick(context.getPos(), this, 60 + context.getWorld().getRandom().nextInt(40));
		}
		FluidState fluidState = context.getWorld().getFluidState(context.getPos());
		return super.getStateForPlacement(context).with(WATERLOGGED, fluidState.isTagged(FluidTags.WATER) && fluidState.getLevel() >= 8);
	}

	public boolean isInProperDimension(World world) {
		return !this.petrified && CONVERSIONS.getOrDefault(world.func_230315_m_(), EEBlocks.CORROCK_OVERWORLD).get() == this;
	}
	
	@Override
	public FluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : Fluids.EMPTY.getDefaultState();
	}
	
	@Override
	public OffsetType getOffsetType() {
		return OffsetType.NONE;
	}
}