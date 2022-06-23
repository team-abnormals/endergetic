package com.minecraftabnormals.endergetic.common.blocks;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;
import com.minecraftabnormals.endergetic.client.particles.data.CorrockCrownParticleData;
import com.minecraftabnormals.endergetic.core.events.EntityEvents;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.server.level.ServerLevel;

import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;

public class CorrockCrownStandingBlock extends CorrockCrownBlock {
	private static final Map<ResourceLocation, Supplier<CorrockCrownBlock>> CONVERSIONS = Util.make(Maps.newHashMap(), (conversions) -> {
		conversions.put(BuiltinDimensionTypes.OVERWORLD.location(), EEBlocks.CORROCK_CROWN_OVERWORLD_STANDING);
		conversions.put(BuiltinDimensionTypes.NETHER.location(), EEBlocks.CORROCK_CROWN_NETHER_STANDING);
		conversions.put(BuiltinDimensionTypes.END.location(), EEBlocks.CORROCK_CROWN_END_STANDING);
	});
	public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;
	public static final BooleanProperty UPSIDE_DOWN = BooleanProperty.create("upside_down");

	public CorrockCrownStandingBlock(Properties properties, DimensionalType dimensionalType, boolean petrified) {
		super(properties, dimensionalType, petrified);
		this.registerDefaultState(this.stateDefinition.any().setValue(ROTATION, 0).setValue(WATERLOGGED, false).setValue(UPSIDE_DOWN, false));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return state.getValue(UPSIDE_DOWN) ? Block.box(2.0D, 2.0D, 2.0D, 14.0D, 16.0D, 14.0D) : Block.box(2.0D, 0.0D, 2.0D, 14.0D, 15.0D, 14.0D);
	}

	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource rand) {
		if (state.getValue(UPSIDE_DOWN)) {
			double xOffset = makeNegativeRandomly(rand.nextFloat() * 0.35F, rand);
			double yOffset = makeNegativeRandomly(rand.nextFloat() * 0.35F, rand);
			double zOffset = makeNegativeRandomly(rand.nextFloat() * 0.35F, rand);
			double posX = (double) pos.getX() + 0.5D + xOffset;
			double posY = (double) pos.getY() + 0.5D + yOffset;
			double posZ = (double) pos.getZ() + 0.5D + zOffset;
			world.addParticle(new CorrockCrownParticleData(this.dimensionalType.particle.get(), false), posX, posY, posZ, rand.nextFloat() * 0.05F - rand.nextFloat() * 0.05F, -0.005F, rand.nextFloat() * 0.05F - rand.nextFloat() * 0.05F);
		} else if (rand.nextFloat() < 0.6F) {
			double xOffset = makeNegativeRandomly(rand.nextFloat() * 0.25F, rand);
			double yOffset = makeNegativeRandomly(rand.nextFloat() * 0.25F, rand);
			double zOffset = makeNegativeRandomly(rand.nextFloat() * 0.25F, rand);
			double posX = (double) pos.getX() + 0.5D + xOffset;
			double posY = (double) pos.getY() + 0.5D + yOffset;
			double posZ = (double) pos.getZ() + 0.5D + zOffset;
			world.addParticle(new CorrockCrownParticleData(this.dimensionalType.particle.get(), false), posX, posY, posZ, rand.nextFloat() * 0.03F - rand.nextFloat() * 0.03F, 0.01F + rand.nextFloat() * 0.02F, rand.nextFloat() * 0.03F - rand.nextFloat() * 0.03F);
		}
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (this.shouldConvert(level)) {
			level.setBlockAndUpdate(pos, this.getConversionBlock(level).defaultBlockState()
					.setValue(ROTATION, level.getBlockState(pos).getValue(ROTATION))
					.setValue(UPSIDE_DOWN, level.getBlockState(pos).getValue(UPSIDE_DOWN))
			);
		}
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
		return state.getValue(UPSIDE_DOWN) ? worldIn.getBlockState(pos.above()).isFaceSturdy(worldIn, pos.above(), Direction.DOWN) : worldIn.getBlockState(pos.below()).isFaceSturdy(worldIn, pos.below(), Direction.UP);
	}

	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (stateIn.getValue(WATERLOGGED)) {
			worldIn.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
			if (!this.petrified) {
				return EntityEvents.convertCorrockBlock(stateIn);
			}
		}
		if (this.shouldConvert(worldIn)) {
			worldIn.scheduleTick(currentPos, this, 60 + worldIn.getRandom().nextInt(40));
		}

		return this.canSurvive(stateIn, worldIn, currentPos) ? stateIn : Blocks.AIR.defaultBlockState();
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		FluidState ifluidstate = context.getLevel().getFluidState(context.getClickedPos());
		Direction direction = context.getClickedFace();
		if (this.shouldConvert(context.getLevel())) {
			context.getLevel().scheduleTick(context.getClickedPos(), this, 60 + context.getLevel().getRandom().nextInt(40));
		}
		if (context.getLevel().getBlockState(context.getClickedPos().below()).getBlock() instanceof CorrockCrownStandingBlock) {
			return null;
		}
		return direction == Direction.UP ?
				this.defaultBlockState().setValue(ROTATION, Mth.floor((double) ((180.0F + context.getRotation()) * 16.0F / 360.0F) + 0.5D) & 15).setValue(WATERLOGGED, ifluidstate.is(FluidTags.WATER) && ifluidstate.getAmount() >= 8)
				: this.defaultBlockState().setValue(ROTATION, Mth.floor((double) ((180.0F + context.getRotation()) * 16.0F / 360.0F) + 0.5D) & 15).setValue(UPSIDE_DOWN, true).setValue(WATERLOGGED, ifluidstate.is(FluidTags.WATER) && ifluidstate.getAmount() >= 8);
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(ROTATION, rot.rotate(state.getValue(ROTATION), 16));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.setValue(ROTATION, mirrorIn.mirror(state.getValue(ROTATION), 16));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(ROTATION, WATERLOGGED, UPSIDE_DOWN);
	}

	protected Block getConversionBlock(LevelAccessor level) {
		return CONVERSIONS.getOrDefault(level.registryAccess().registry(Registry.DIMENSION_TYPE_REGISTRY).get().getKey(level.dimensionType()), EEBlocks.CORROCK_CROWN_OVERWORLD_STANDING).get();
	}

	private boolean shouldConvert(LevelAccessor level) {
		return !this.petrified && this.getConversionBlock(level) != this;
	}

	private static double makeNegativeRandomly(double value, RandomSource rand) {
		return rand.nextBoolean() ? -value : value;
	}

	@Override
	public ItemStack pickupBlock(LevelAccessor p_152719_, BlockPos p_152720_, BlockState p_152721_) {
		return new ItemStack(this);
	}

	@Override
	public Optional<SoundEvent> getPickupSound() {
		return Optional.empty();
	}
}