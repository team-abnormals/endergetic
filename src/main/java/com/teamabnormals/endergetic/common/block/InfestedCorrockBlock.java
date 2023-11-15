package com.teamabnormals.endergetic.common.block;

import com.google.common.collect.Maps;
import com.teamabnormals.endergetic.core.registry.EEBlocks;
import com.teamabnormals.endergetic.core.registry.other.EEEvents;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;

import java.util.Map;
import java.util.function.Supplier;

public class InfestedCorrockBlock extends Block {
	private static final Direction[] POSSIBLE_DIRECTIONS = Direction.values();
	private static final Map<ResourceLocation, Supplier<Block>> CONVERSIONS = Util.make(Maps.newHashMap(), (conversions) -> {
		conversions.put(BuiltinDimensionTypes.OVERWORLD.location(), EEBlocks.CORROCK_OVERWORLD_BLOCK);
		conversions.put(BuiltinDimensionTypes.NETHER.location(), EEBlocks.CORROCK_NETHER_BLOCK);
		conversions.put(BuiltinDimensionTypes.END.location(), EEBlocks.INFESTED_CORROCK);
	});

	public InfestedCorrockBlock(Properties properties) {
		super(properties);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
		Block conversion = this.getConversionBlock(level);
		if (conversion != this) {
			level.setBlockAndUpdate(pos, conversion.defaultBlockState());
			return;
		}

		if (level.isAreaLoaded(pos, 1) && rand.nextFloat() <= 0.02F) {
			EetleEggBlock.shuffleDirections(POSSIBLE_DIRECTIONS, rand);
			boolean grewNewEgg = false;
			Block eetleEggs = EEBlocks.EETLE_EGG.get();
			for (Direction direction : POSSIBLE_DIRECTIONS) {
				BlockPos offset = pos.relative(direction);
				if (!grewNewEgg && level.isEmptyBlock(offset)) {
					grewNewEgg = true;
					level.setBlockAndUpdate(offset, eetleEggs.defaultBlockState().setValue(EetleEggBlock.FACING, direction));
				} else {
					BlockState offsetState = level.getBlockState(offset);
					if (offsetState.getBlock() == eetleEggs && rand.nextFloat() <= 0.25F) {
						int size = offsetState.getValue(EetleEggBlock.SIZE);
						if (size < 2) {
							level.setBlockAndUpdate(offset, offsetState.setValue(EetleEggBlock.SIZE, size + 1));
						}
					}
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (this.shouldConvert(worldIn)) {
			worldIn.scheduleTick(currentPos, this, 40 + worldIn.getRandom().nextInt(40));
		}

		if (CorrockBlock.isSubmerged(worldIn, currentPos)) {
			return EEEvents.convertCorrockBlock(state);
		}

		return state;
	}

	protected Block getConversionBlock(LevelAccessor level) {
		return CONVERSIONS.getOrDefault(level.registryAccess().registry(Registry.DIMENSION_TYPE_REGISTRY).get().getKey(level.dimensionType()), EEBlocks.CORROCK_OVERWORLD_BLOCK).get();
	}

	protected boolean shouldConvert(LevelAccessor level) {
		return this.getConversionBlock(level) != this;
	}
}
