package com.minecraftabnormals.endergetic.common.blocks;

import com.google.common.collect.Maps;
import com.minecraftabnormals.endergetic.core.events.EntityEvents;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;

import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

public class SpeckledCorrockBlock extends Block {
	private static final Map<DimensionType, Supplier<Block>> CONVERSIONS = Util.make(Maps.newHashMap(), (conversions) -> {
		conversions.put(CorrockBlock.DimensionTypeAccessor.OVERWORLD, EEBlocks.SPECKLED_OVERWORLD_CORROCK);
		conversions.put(CorrockBlock.DimensionTypeAccessor.THE_NETHER, EEBlocks.SPECKLED_NETHER_CORROCK);
		conversions.put(CorrockBlock.DimensionTypeAccessor.THE_END, EEBlocks.SPECKLED_END_CORROCK);
	});

	public SpeckledCorrockBlock(Properties properties) {
		super(properties);
	}

	@Override
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (this.shouldConvert(world)) {
			world.setBlockState(pos, CONVERSIONS.getOrDefault(world.getDimensionType(), EEBlocks.SPECKLED_OVERWORLD_CORROCK).get().getDefaultState());
		}
	}

	@SuppressWarnings("deprecation")
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (this.shouldConvert(worldIn)) {
			worldIn.getPendingBlockTicks().scheduleTick(currentPos, this, 60 + worldIn.getRandom().nextInt(40));
		}

		if (CorrockBlock.isSubmerged(worldIn, currentPos)) {
			return EntityEvents.convertCorrockBlock(stateIn);
		}

		return stateIn;
	}

	private boolean shouldConvert(IWorld world) {
		return CONVERSIONS.getOrDefault(world.getDimensionType(), EEBlocks.SPECKLED_OVERWORLD_CORROCK).get() != this;
	}
}
