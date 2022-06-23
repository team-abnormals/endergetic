package com.minecraftabnormals.endergetic.common.blocks;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;
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
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.tags.FluidTags;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.server.level.ServerLevel;

import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;

public class CorrockCrownWallBlock extends CorrockCrownBlock {
	private static final Map<ResourceLocation, Supplier<CorrockCrownWallBlock>> CONVERSIONS = Util.make(Maps.newHashMap(), (conversions) -> {
		conversions.put(BuiltinDimensionTypes.OVERWORLD.location(), EEBlocks.CORROCK_CROWN_OVERWORLD_WALL);
		conversions.put(BuiltinDimensionTypes.NETHER.location(), EEBlocks.CORROCK_CROWN_NETHER_WALL);
		conversions.put(BuiltinDimensionTypes.END.location(), EEBlocks.CORROCK_CROWN_END_WALL);
	});
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	private static final Map<Direction, VoxelShape> SHAPES = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, Block.box(0.0D, 4.5D, 14.0D, 16.0D, 12.5D, 16.0D), Direction.SOUTH, Block.box(0.0D, 4.5D, 0.0D, 16.0D, 12.5D, 2.0D), Direction.EAST, Block.box(0.0D, 4.5D, 0.0D, 2.0D, 12.5D, 16.0D), Direction.WEST, Block.box(14.0D, 4.5D, 0.0D, 16.0D, 12.5D, 16.0D)));

	public CorrockCrownWallBlock(Properties builder, DimensionalType dimensionalType, boolean petrified) {
		super(builder, dimensionalType, petrified);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false));
	}

	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource rand) {
		Direction facing = state.getValue(FACING).getOpposite();
		int xFacingOffset = facing.getStepX();
		int zFacingOffset = facing.getStepZ();
		double xOffset = xFacingOffset * 0.2F + makeNegativeRandomly(rand.nextFloat() * 0.25F, rand);
		double yOffset = makeNegativeRandomly(rand.nextFloat() * 0.25F, rand);
		double zOffset = zFacingOffset * 0.2F + makeNegativeRandomly(rand.nextFloat() * 0.25F, rand);
		double posX = (double) pos.getX() + 0.5F + xOffset;
		double posY = (double) pos.getY() + 0.5D + yOffset;
		double posZ = (double) pos.getZ() + 0.5F + zOffset;
		world.addParticle(new CorrockCrownParticleData(this.dimensionalType.particle.get(), false), posX, posY, posZ, xFacingOffset * -0.01F + rand.nextFloat() * 0.04F - rand.nextFloat() * 0.04F, -0.005F, zFacingOffset * -0.01F + rand.nextFloat() * 0.04F - rand.nextFloat() * 0.04F);
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (!this.petrified) {
			Block conversion = this.getConversionBlock(level);
			if (conversion == this) return;
			level.setBlockAndUpdate(pos, conversion.defaultBlockState().setValue(FACING, state.getValue(FACING)));
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
		return SHAPES.get(state.getValue(FACING));
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		Direction direction = state.getValue(FACING);
		BlockPos blockpos = pos.relative(direction.getOpposite());
		return Block.canSupportCenter(world, blockpos, direction);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockState state = this.defaultBlockState();
		FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
		LevelReader iworldreaderbase = context.getLevel();
		BlockPos blockpos = context.getClickedPos();
		Direction[] aDirection = context.getNearestLookingDirections();

		if (this.shouldConvert(context.getLevel())) {
			context.getLevel().scheduleTick(context.getClickedPos(), this, 60 + context.getLevel().getRandom().nextInt(40));
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

	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (stateIn.getValue(WATERLOGGED)) {
			worldIn.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
			if (!this.petrified) {
				return EntityEvents.convertCorrockBlock(stateIn);
			}
		} else if (this.shouldConvert(worldIn)) {
			worldIn.scheduleTick(currentPos, this, 60 + worldIn.getRandom().nextInt(40));
		}

		if (facing == Direction.DOWN && !stateIn.canSurvive(worldIn, currentPos)) {
			return Blocks.AIR.defaultBlockState();
		}
		return facing.getOpposite() == stateIn.getValue(FACING) && !stateIn.canSurvive(worldIn, currentPos) ? Blocks.AIR.defaultBlockState() : stateIn;
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}

	@SuppressWarnings("deprecation")
	@Override
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, WATERLOGGED);
	}

	protected Block getConversionBlock(LevelAccessor level) {
		return CONVERSIONS.getOrDefault(level.registryAccess().registry(Registry.DIMENSION_TYPE_REGISTRY).get().getKey(level.dimensionType()), EEBlocks.CORROCK_CROWN_OVERWORLD_WALL).get();
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
