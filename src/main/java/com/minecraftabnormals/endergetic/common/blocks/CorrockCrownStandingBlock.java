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
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.DimensionType;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class CorrockCrownStandingBlock extends CorrockCrownBlock {
	private static final Map<DimensionType, Supplier<CorrockCrownBlock>> CONVERSIONS = Util.make(Maps.newHashMap(), (conversions) -> {
		conversions.put(CorrockBlock.DimensionTypeAccessor.OVERWORLD, () -> EEBlocks.CORROCK_CROWN_OVERWORLD_STANDING.get());
		conversions.put(CorrockBlock.DimensionTypeAccessor.THE_NETHER, () -> EEBlocks.CORROCK_CROWN_NETHER_STANDING.get());
		conversions.put(CorrockBlock.DimensionTypeAccessor.THE_END, () -> EEBlocks.CORROCK_CROWN_END_STANDING.get());
	});
	public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_0_15;
	public static final BooleanProperty UPSIDE_DOWN = BooleanProperty.create("upside_down");

	public CorrockCrownStandingBlock(Properties properties, boolean petrified) {
		super(properties, petrified);
		this.setDefaultState(this.stateContainer.getBaseState().with(ROTATION, 0).with(WATERLOGGED, false).with(UPSIDE_DOWN, false));
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return state.get(UPSIDE_DOWN) ? Block.makeCuboidShape(2.0D, 2.0D, 2.0D, 14.0D, 16.0D, 14.0D) : Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 15.0D, 14.0D);
	}

	@Override
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (!this.petrified && !this.isInProperDimension(world)) {
			world.setBlockState(pos, CONVERSIONS.getOrDefault(world.func_230315_m_(), EEBlocks.CORROCK_CROWN_OVERWORLD_STANDING).get().getDefaultState()
					.with(ROTATION, world.getBlockState(pos).get(ROTATION))
					.with(UPSIDE_DOWN, world.getBlockState(pos).get(UPSIDE_DOWN))
			);
		}
	}

	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		return state.get(UPSIDE_DOWN) ? worldIn.getBlockState(pos.up()).isSolidSide(worldIn, pos.up(), Direction.DOWN) : worldIn.getBlockState(pos.down()).isSolidSide(worldIn, pos.down(), Direction.UP);
	}

	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (stateIn.get(WATERLOGGED)) {
			worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
			if (!this.petrified) {
				return EntityEvents.convertCorrockBlock(stateIn);
			}
		}
		if (!this.petrified && !this.isInProperDimension(worldIn.getWorld())) {
			worldIn.getPendingBlockTicks().scheduleTick(currentPos, this, 60 + worldIn.getRandom().nextInt(40));
		}

		return this.isValidPosition(stateIn, worldIn, currentPos) ? stateIn : Blocks.AIR.getDefaultState();
	}

	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		FluidState ifluidstate = context.getWorld().getFluidState(context.getPos());
		Direction direction = context.getFace();
		if (!this.isInProperDimension(context.getWorld())) {
			context.getWorld().getPendingBlockTicks().scheduleTick(context.getPos(), this, 60 + context.getWorld().getRandom().nextInt(40));
		}
		if (context.getWorld().getBlockState(context.getPos().down()).getBlock() instanceof CorrockCrownStandingBlock) {
			return null;
		}
		return direction == Direction.UP ?
				this.getDefaultState().with(ROTATION, Integer.valueOf(MathHelper.floor((double) ((180.0F + context.getPlacementYaw()) * 16.0F / 360.0F) + 0.5D) & 15)).with(WATERLOGGED, ifluidstate.isTagged(FluidTags.WATER) && ifluidstate.getLevel() >= 8)
				: this.getDefaultState().with(ROTATION, Integer.valueOf(MathHelper.floor((double) ((180.0F + context.getPlacementYaw()) * 16.0F / 360.0F) + 0.5D) & 15)).with(UPSIDE_DOWN, true).with(WATERLOGGED, ifluidstate.isTagged(FluidTags.WATER) && ifluidstate.getLevel() >= 8);
	}

	public boolean isInProperDimension(World world) {
		return !this.petrified && CONVERSIONS.getOrDefault(world.func_230315_m_(), EEBlocks.CORROCK_CROWN_OVERWORLD_STANDING).get() == this;
	}

	public BlockState rotate(BlockState state, Rotation rot) {
		return state.with(ROTATION, Integer.valueOf(rot.rotate(state.get(ROTATION), 16)));
	}

	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.with(ROTATION, Integer.valueOf(mirrorIn.mirrorRotation(state.get(ROTATION), 16)));
	}

	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(ROTATION, WATERLOGGED, UPSIDE_DOWN);
	}
}