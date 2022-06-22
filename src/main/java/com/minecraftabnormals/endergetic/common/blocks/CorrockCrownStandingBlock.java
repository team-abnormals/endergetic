package com.minecraftabnormals.endergetic.common.blocks;

import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;
import com.minecraftabnormals.abnormals_core.core.util.MathUtil;
import com.minecraftabnormals.endergetic.client.particles.data.CorrockCrownParticleData;
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
import net.minecraft.world.*;
import net.minecraft.world.server.ServerWorld;

import net.minecraft.block.AbstractBlock.Properties;

public class CorrockCrownStandingBlock extends CorrockCrownBlock {
	private static final Map<DimensionType, Supplier<CorrockCrownBlock>> CONVERSIONS = Util.make(Maps.newHashMap(), (conversions) -> {
		conversions.put(CorrockBlock.DimensionTypeAccessor.OVERWORLD, EEBlocks.CORROCK_CROWN_OVERWORLD_STANDING);
		conversions.put(CorrockBlock.DimensionTypeAccessor.THE_NETHER, EEBlocks.CORROCK_CROWN_NETHER_STANDING);
		conversions.put(CorrockBlock.DimensionTypeAccessor.THE_END, EEBlocks.CORROCK_CROWN_END_STANDING);
	});
	public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;
	public static final BooleanProperty UPSIDE_DOWN = BooleanProperty.create("upside_down");

	public CorrockCrownStandingBlock(Properties properties, DimensionalType dimensionalType, boolean petrified) {
		super(properties, dimensionalType, petrified);
		this.registerDefaultState(this.stateDefinition.any().setValue(ROTATION, 0).setValue(WATERLOGGED, false).setValue(UPSIDE_DOWN, false));
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return state.getValue(UPSIDE_DOWN) ? Block.box(2.0D, 2.0D, 2.0D, 14.0D, 16.0D, 14.0D) : Block.box(2.0D, 0.0D, 2.0D, 14.0D, 15.0D, 14.0D);
	}

	@Override
	public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
		if (state.getValue(UPSIDE_DOWN)) {
			double xOffset = MathUtil.makeNegativeRandomly(rand.nextFloat() * 0.35F, rand);
			double yOffset = MathUtil.makeNegativeRandomly(rand.nextFloat() * 0.35F, rand);
			double zOffset = MathUtil.makeNegativeRandomly(rand.nextFloat() * 0.35F, rand);
			double posX = (double) pos.getX() + 0.5D + xOffset;
			double posY = (double) pos.getY() + 0.5D + yOffset;
			double posZ = (double) pos.getZ() + 0.5D + zOffset;
			world.addParticle(new CorrockCrownParticleData(this.dimensionalType.particle.get(), false), posX, posY, posZ, rand.nextFloat() * 0.05F - rand.nextFloat() * 0.05F, -0.005F, rand.nextFloat() * 0.05F - rand.nextFloat() * 0.05F);
		} else if (rand.nextFloat() < 0.6F) {
			double xOffset = MathUtil.makeNegativeRandomly(rand.nextFloat() * 0.25F, rand);
			double yOffset = MathUtil.makeNegativeRandomly(rand.nextFloat() * 0.25F, rand);
			double zOffset = MathUtil.makeNegativeRandomly(rand.nextFloat() * 0.25F, rand);
			double posX = (double) pos.getX() + 0.5D + xOffset;
			double posY = (double) pos.getY() + 0.5D + yOffset;
			double posZ = (double) pos.getZ() + 0.5D + zOffset;
			world.addParticle(new CorrockCrownParticleData(this.dimensionalType.particle.get(), false), posX, posY, posZ, rand.nextFloat() * 0.03F - rand.nextFloat() * 0.03F, 0.01F + rand.nextFloat() * 0.02F, rand.nextFloat() * 0.03F - rand.nextFloat() * 0.03F);
		}
	}

	@Override
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (this.shouldConvert(world)) {
			world.setBlockAndUpdate(pos, CONVERSIONS.getOrDefault(world.dimensionType(), EEBlocks.CORROCK_CROWN_OVERWORLD_STANDING).get().defaultBlockState()
					.setValue(ROTATION, world.getBlockState(pos).getValue(ROTATION))
					.setValue(UPSIDE_DOWN, world.getBlockState(pos).getValue(UPSIDE_DOWN))
			);
		}
	}

	public boolean canSurvive(BlockState state, IWorldReader worldIn, BlockPos pos) {
		return state.getValue(UPSIDE_DOWN) ? worldIn.getBlockState(pos.above()).isFaceSturdy(worldIn, pos.above(), Direction.DOWN) : worldIn.getBlockState(pos.below()).isFaceSturdy(worldIn, pos.below(), Direction.UP);
	}

	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (stateIn.getValue(WATERLOGGED)) {
			worldIn.getLiquidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
			if (!this.petrified) {
				return EntityEvents.convertCorrockBlock(stateIn);
			}
		}
		if (this.shouldConvert(worldIn)) {
			worldIn.getBlockTicks().scheduleTick(currentPos, this, 60 + worldIn.getRandom().nextInt(40));
		}

		return this.canSurvive(stateIn, worldIn, currentPos) ? stateIn : Blocks.AIR.defaultBlockState();
	}

	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		FluidState ifluidstate = context.getLevel().getFluidState(context.getClickedPos());
		Direction direction = context.getClickedFace();
		if (this.shouldConvert(context.getLevel())) {
			context.getLevel().getBlockTicks().scheduleTick(context.getClickedPos(), this, 60 + context.getLevel().getRandom().nextInt(40));
		}
		if (context.getLevel().getBlockState(context.getClickedPos().below()).getBlock() instanceof CorrockCrownStandingBlock) {
			return null;
		}
		return direction == Direction.UP ?
				this.defaultBlockState().setValue(ROTATION, MathHelper.floor((double) ((180.0F + context.getRotation()) * 16.0F / 360.0F) + 0.5D) & 15).setValue(WATERLOGGED, ifluidstate.is(FluidTags.WATER) && ifluidstate.getAmount() >= 8)
				: this.defaultBlockState().setValue(ROTATION, MathHelper.floor((double) ((180.0F + context.getRotation()) * 16.0F / 360.0F) + 0.5D) & 15).setValue(UPSIDE_DOWN, true).setValue(WATERLOGGED, ifluidstate.is(FluidTags.WATER) && ifluidstate.getAmount() >= 8);
	}

	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(ROTATION, rot.rotate(state.getValue(ROTATION), 16));
	}

	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.setValue(ROTATION, mirrorIn.mirror(state.getValue(ROTATION), 16));
	}

	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(ROTATION, WATERLOGGED, UPSIDE_DOWN);
	}

	private boolean shouldConvert(IWorld world) {
		return !this.petrified && CONVERSIONS.getOrDefault(world.dimensionType(), EEBlocks.CORROCK_CROWN_OVERWORLD_STANDING).get() != this;
	}
}