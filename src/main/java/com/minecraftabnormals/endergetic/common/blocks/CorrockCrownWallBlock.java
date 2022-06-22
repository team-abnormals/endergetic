package com.minecraftabnormals.endergetic.common.blocks;

import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.minecraftabnormals.abnormals_core.core.util.MathUtil;
import com.minecraftabnormals.endergetic.client.particles.data.CorrockCrownParticleData;
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
import net.minecraft.world.*;
import net.minecraft.world.server.ServerWorld;

import net.minecraft.block.AbstractBlock.Properties;

public class CorrockCrownWallBlock extends CorrockCrownBlock {
	private static final Map<DimensionType, Supplier<CorrockCrownWallBlock>> CONVERSIONS = Util.make(Maps.newHashMap(), (conversions) -> {
		conversions.put(CorrockBlock.DimensionTypeAccessor.OVERWORLD, EEBlocks.CORROCK_CROWN_OVERWORLD_WALL);
		conversions.put(CorrockBlock.DimensionTypeAccessor.THE_NETHER, EEBlocks.CORROCK_CROWN_NETHER_WALL);
		conversions.put(CorrockBlock.DimensionTypeAccessor.THE_END, EEBlocks.CORROCK_CROWN_END_WALL);
	});
	public static final DirectionProperty FACING = HorizontalBlock.FACING;
	private static final Map<Direction, VoxelShape> SHAPES = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, Block.box(0.0D, 4.5D, 14.0D, 16.0D, 12.5D, 16.0D), Direction.SOUTH, Block.box(0.0D, 4.5D, 0.0D, 16.0D, 12.5D, 2.0D), Direction.EAST, Block.box(0.0D, 4.5D, 0.0D, 2.0D, 12.5D, 16.0D), Direction.WEST, Block.box(14.0D, 4.5D, 0.0D, 16.0D, 12.5D, 16.0D)));

	public CorrockCrownWallBlock(Properties builder, DimensionalType dimensionalType, boolean petrified) {
		super(builder, dimensionalType, petrified);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false));
	}

	@Override
	public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
		Direction facing = state.getValue(FACING).getOpposite();
		int xFacingOffset = facing.getStepX();
		int zFacingOffset = facing.getStepZ();
		double xOffset = xFacingOffset * 0.2F + MathUtil.makeNegativeRandomly(rand.nextFloat() * 0.25F, rand);
		double yOffset = MathUtil.makeNegativeRandomly(rand.nextFloat() * 0.25F, rand);
		double zOffset = zFacingOffset * 0.2F + MathUtil.makeNegativeRandomly(rand.nextFloat() * 0.25F, rand);
		double posX = (double) pos.getX() + 0.5F + xOffset;
		double posY = (double) pos.getY() + 0.5D + yOffset;
		double posZ = (double) pos.getZ() + 0.5F + zOffset;
		world.addParticle(new CorrockCrownParticleData(this.dimensionalType.particle.get(), false), posX, posY, posZ, xFacingOffset * -0.01F + rand.nextFloat() * 0.04F - rand.nextFloat() * 0.04F, -0.005F, zFacingOffset * -0.01F + rand.nextFloat() * 0.04F - rand.nextFloat() * 0.04F);
	}

	@Override
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (this.shouldConvert(world)) {
			world.setBlockAndUpdate(pos, CONVERSIONS.getOrDefault(world.dimensionType(), EEBlocks.CORROCK_CROWN_OVERWORLD_WALL).get().defaultBlockState().setValue(FACING, world.getBlockState(pos).getValue(FACING)));
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
		return SHAPES.get(state.getValue(FACING));
	}

	public boolean canSurvive(BlockState state, IWorldReader world, BlockPos pos) {
		Direction direction = state.getValue(FACING);
		BlockPos blockpos = pos.relative(direction.getOpposite());
		return Block.canSupportCenter(world, blockpos, direction);
	}

	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		BlockState state = this.defaultBlockState();
		FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
		IWorldReader iworldreaderbase = context.getLevel();
		BlockPos blockpos = context.getClickedPos();
		Direction[] aDirection = context.getNearestLookingDirections();

		if (this.shouldConvert(context.getLevel())) {
			context.getLevel().getBlockTicks().scheduleTick(context.getClickedPos(), this, 60 + context.getLevel().getRandom().nextInt(40));
		}

		for (Direction Direction : aDirection) {
			if (Direction.getAxis().isHorizontal()) {
				Direction Direction1 = Direction.getOpposite();
				state = state.setValue(FACING, Direction1);
				if (state.canSurvive(iworldreaderbase, blockpos) && !(iworldreaderbase.getBlockState(blockpos.relative(state.getValue(FACING).getOpposite())).getBlock() instanceof CorrockCrownWallBlock) && !(iworldreaderbase.getBlockState(blockpos.relative(state.getValue(FACING).getOpposite())).getBlock() instanceof CorrockCrownStandingBlock)) {
					return state.setValue(WATERLOGGED, fluidState.is(FluidTags.WATER) && fluidState.getAmount() >= 8);
				}
			}
		}
		return null;
	}

	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (stateIn.getValue(WATERLOGGED)) {
			worldIn.getLiquidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
			if (!this.petrified) {
				return EntityEvents.convertCorrockBlock(stateIn);
			}
		} else if (this.shouldConvert(worldIn)) {
			worldIn.getBlockTicks().scheduleTick(currentPos, this, 60 + worldIn.getRandom().nextInt(40));
		}

		if (facing == Direction.DOWN && !stateIn.canSurvive(worldIn, currentPos)) {
			return Blocks.AIR.defaultBlockState();
		}
		return facing.getOpposite() == stateIn.getValue(FACING) && !stateIn.canSurvive(worldIn, currentPos) ? Blocks.AIR.defaultBlockState() : stateIn;
	}

	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}

	@SuppressWarnings("deprecation")
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
	}

	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING, WATERLOGGED);
	}

	private boolean shouldConvert(IWorld world) {
		return !this.petrified && CONVERSIONS.getOrDefault(world.dimensionType(), EEBlocks.CORROCK_CROWN_OVERWORLD_WALL).get() != this;
	}
}
