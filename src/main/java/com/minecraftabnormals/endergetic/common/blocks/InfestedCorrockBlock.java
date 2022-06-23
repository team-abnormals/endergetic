package com.minecraftabnormals.endergetic.common.blocks;

import com.google.common.collect.Maps;
import com.minecraftabnormals.endergetic.core.events.EntityEvents;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.Direction;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.server.level.ServerLevel;

import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class InfestedCorrockBlock extends Block {
	private static final Direction[] POSSIBLE_DIRECTIONS = Direction.values();
	private static final Map<DimensionType, Supplier<Block>> CONVERSIONS = Util.make(Maps.newHashMap(), (conversions) -> {
		conversions.put(CorrockBlock.DimensionTypeAccessor.OVERWORLD, EEBlocks.CORROCK_OVERWORLD_BLOCK);
		conversions.put(CorrockBlock.DimensionTypeAccessor.THE_NETHER, EEBlocks.CORROCK_NETHER_BLOCK);
		conversions.put(CorrockBlock.DimensionTypeAccessor.THE_END, EEBlocks.INFESTED_CORROCK);
	});

	public InfestedCorrockBlock(Properties properties) {
		super(properties);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void tick(BlockState state, ServerLevel world, BlockPos pos, Random rand) {
		if (this.shouldConvert(world)) {
			world.setBlockAndUpdate(pos, CONVERSIONS.getOrDefault(world.dimensionType(), EEBlocks.CORROCK_OVERWORLD_BLOCK).get().defaultBlockState());
			return;
		}

		if (world.isAreaLoaded(pos, 1) && rand.nextFloat() <= 0.02F) {
			EetleEggBlock.shuffleDirections(POSSIBLE_DIRECTIONS, rand);
			boolean grewNewEgg = false;
			Block eetleEggs = EEBlocks.EETLE_EGG.get();
			for (Direction direction : POSSIBLE_DIRECTIONS) {
				BlockPos offset = pos.relative(direction);
				if (!grewNewEgg && world.isEmptyBlock(offset)) {
					grewNewEgg = true;
					world.setBlockAndUpdate(offset, eetleEggs.defaultBlockState().setValue(EetleEggBlock.FACING, direction));
				} else {
					BlockState offsetState = world.getBlockState(offset);
					if (offsetState.getBlock() == eetleEggs && rand.nextFloat() <= 0.25F) {
						int size = offsetState.getValue(EetleEggBlock.SIZE);
						if (size < 2) {
							world.setBlockAndUpdate(offset, offsetState.setValue(EetleEggBlock.SIZE, size + 1));
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
			worldIn.getBlockTicks().scheduleTick(currentPos, this, 40 + worldIn.getRandom().nextInt(40));
		}

		if (CorrockBlock.isSubmerged(worldIn, currentPos)) {
			return EntityEvents.convertCorrockBlock(state);
		}

		return state;
	}

	protected boolean shouldConvert(LevelAccessor world) {
		return CONVERSIONS.getOrDefault(world.dimensionType(), EEBlocks.CORROCK_OVERWORLD_BLOCK).get() != this;
	}
}
