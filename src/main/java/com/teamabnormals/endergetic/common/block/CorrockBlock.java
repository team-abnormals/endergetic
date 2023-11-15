package com.teamabnormals.endergetic.common.block;

import com.google.common.collect.Maps;
import com.teamabnormals.endergetic.core.registry.EEBlocks;
import com.teamabnormals.endergetic.core.registry.EEEntityTypes;
import com.teamabnormals.endergetic.core.registry.other.EEEvents;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.material.FluidState;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Supplier;

public class CorrockBlock extends Block implements BonemealableBlock {
	private static final Map<ResourceLocation, Supplier<Block>> CONVERSIONS = Util.make(Maps.newHashMap(), (conversions) -> {
		conversions.put(BuiltinDimensionTypes.OVERWORLD.location(), EEBlocks.CORROCK_OVERWORLD_BLOCK);
		conversions.put(BuiltinDimensionTypes.NETHER.location(), EEBlocks.CORROCK_NETHER_BLOCK);
		conversions.put(BuiltinDimensionTypes.END.location(), EEBlocks.CORROCK_END_BLOCK);
	});
	private final Supplier<Block> speckledBlock;
	private final Supplier<Block> plantBlock;

	public CorrockBlock(Properties properties, Supplier<Block> speckledBlock, Supplier<Block> plantBlock) {
		super(properties);
		this.speckledBlock = speckledBlock;
		this.plantBlock = plantBlock;
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		Block conversion = this.getConversionBlock(level);
		if (conversion != this) {
			level.setBlockAndUpdate(pos, conversion.defaultBlockState());
		}
	}

	@Override
	public SoundType getSoundType(BlockState state, LevelReader world, BlockPos pos, Entity entity) {
		return SoundType.CORAL_BLOCK;
	}

	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (this.shouldConvert(worldIn)) {
			worldIn.scheduleTick(currentPos, this, 60 + worldIn.getRandom().nextInt(40));
		}

		if (isSubmerged(worldIn, currentPos)) {
			return EEEvents.convertCorrockBlock(stateIn);
		}

		return stateIn;
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		if (this.shouldConvert(context.getLevel())) {
			context.getLevel().scheduleTick(context.getClickedPos(), this, 60 + context.getLevel().getRandom().nextInt(40));
		}
		return this.defaultBlockState();
	}

	protected Block getConversionBlock(LevelAccessor level) {
		return CONVERSIONS.getOrDefault(level.registryAccess().registry(Registry.DIMENSION_TYPE_REGISTRY).get().getKey(level.dimensionType()), EEBlocks.CORROCK_OVERWORLD_BLOCK).get();
	}

	protected boolean shouldConvert(LevelAccessor level) {
		return this.getConversionBlock(level) != this;
	}

	public static boolean isSubmerged(LevelAccessor world, BlockPos pos) {
		for (Direction offsets : Direction.values()) {
			FluidState fluidState = world.getFluidState(pos.relative(offsets));
			if (!fluidState.isEmpty() && fluidState.is(FluidTags.WATER)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isValidSpawn(BlockState state, BlockGetter level, BlockPos pos, SpawnPlacements.Type type, EntityType<?> entityType) {
		return entityType == EEEntityTypes.CHARGER_EETLE.get() || super.isValidSpawn(state, level, pos, type, entityType);
	}

	@Override
	public boolean isValidBonemealTarget(BlockGetter worldIn, BlockPos pos, BlockState state, boolean isClient) {
		return true;
	}

	@Override
	public boolean isBonemealSuccess(Level p_220878_, RandomSource p_220879_, BlockPos p_220880_, BlockState p_220881_) {
		return true;
	}

	@Override
	public void performBonemeal(ServerLevel level, RandomSource rand, BlockPos pos, BlockState state) {
		int radius = 2;
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
		Block speckledBlock = this.speckledBlock.get();
		BlockState speckledState = speckledBlock.defaultBlockState();
		BlockState plantState = this.plantBlock.get().defaultBlockState();
		for (int x = -radius; x <= radius; x++) {
			for (int z = -radius; z <= radius; z++) {
				mutable.setWithOffset(pos, x, 0, z);
				Block block = this.findHighestSpreadableBlock(level, mutable, speckledBlock);
				boolean thisBlock = block == this;
				boolean isSpeckled = block == speckledBlock;
				if (block == Blocks.END_STONE || thisBlock || isSpeckled) {
					int distanceSq = x * x + z * z - rand.nextInt(2);
					if (distanceSq <= 1) {
						this.placeSpreadBlock(level, mutable, this.defaultBlockState(), plantState, rand, false);
					} else if (distanceSq <= 4) {
						boolean notSpeckled = isSpeckled || thisBlock;
						this.placeSpreadBlock(level, mutable, notSpeckled ? this.defaultBlockState() : speckledState, plantState, rand, !notSpeckled);
					}
				}
			}
		}
	}

	private void placeSpreadBlock(ServerLevel world, BlockPos pos, BlockState state, BlockState plantState, RandomSource random, boolean speckled) {
		world.setBlockAndUpdate(pos, state);
		if (random.nextFloat() < (speckled ? 0.1F : 0.2F)) {
			BlockPos up = pos.above();
			if (world.isEmptyBlock(up)) {
				world.setBlockAndUpdate(up, plantState);
			}
		}
	}

	@Nullable
	private Block findHighestSpreadableBlock(ServerLevel world, BlockPos.MutableBlockPos mutable, Block speckledBlock) {
		int originY = mutable.getY();
		for (int y = 1; y > -2; y--) {
			mutable.setY(originY + y);
			Block block = world.getBlockState(mutable).getBlock();
			if (block == Blocks.END_STONE || block == this || block == speckledBlock) {
				return block;
			}
		}
		return null;
	}
}
