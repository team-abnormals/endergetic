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

import net.minecraft.block.AbstractBlock.Properties;

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
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
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
	public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (this.shouldConvert(worldIn)) {
			worldIn.getBlockTicks().scheduleTick(currentPos, this, 40 + worldIn.getRandom().nextInt(40));
		}

		if (CorrockBlock.isSubmerged(worldIn, currentPos)) {
			return EntityEvents.convertCorrockBlock(state);
		}

		return state;
	}

	protected boolean shouldConvert(IWorld world) {
		return CONVERSIONS.getOrDefault(world.dimensionType(), EEBlocks.CORROCK_OVERWORLD_BLOCK).get() != this;
	}
}
