package com.teamabnormals.endergetic.common.blocks;

import com.google.common.collect.Maps;
import com.teamabnormals.endergetic.core.events.EntityEvents;
import com.teamabnormals.endergetic.core.registry.EEBlocks;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.Direction;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.server.level.ServerLevel;

import java.util.Map;
import java.util.function.Supplier;

public class SpeckledCorrockBlock extends Block {
	private static final Map<ResourceLocation, Supplier<Block>> CONVERSIONS = Util.make(Maps.newHashMap(), (conversions) -> {
		conversions.put(BuiltinDimensionTypes.OVERWORLD.location(), EEBlocks.SPECKLED_OVERWORLD_CORROCK);
		conversions.put(BuiltinDimensionTypes.NETHER.location(), EEBlocks.SPECKLED_NETHER_CORROCK);
		conversions.put(BuiltinDimensionTypes.END.location(), EEBlocks.SPECKLED_END_CORROCK);
	});

	public SpeckledCorrockBlock(Properties properties) {
		super(properties);
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		Block conversion = this.getConversionBlock(level);
		if (conversion != this) {
			level.setBlockAndUpdate(pos, conversion.defaultBlockState());
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (this.shouldConvert(worldIn)) {
			worldIn.scheduleTick(currentPos, this, 60 + worldIn.getRandom().nextInt(40));
		}

		if (CorrockBlock.isSubmerged(worldIn, currentPos)) {
			return EntityEvents.convertCorrockBlock(stateIn);
		}

		return stateIn;
	}

	protected Block getConversionBlock(LevelAccessor level) {
		return CONVERSIONS.getOrDefault(level.registryAccess().registry(Registry.DIMENSION_TYPE_REGISTRY).get().getKey(level.dimensionType()), EEBlocks.SPECKLED_OVERWORLD_CORROCK).get();
	}

	protected boolean shouldConvert(LevelAccessor level) {
		return this.getConversionBlock(level) != this;
	}
}
