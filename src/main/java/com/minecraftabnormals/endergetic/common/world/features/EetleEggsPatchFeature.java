package com.minecraftabnormals.endergetic.common.world.features;

import com.minecraftabnormals.endergetic.common.blocks.EetleEggsBlock;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.ProbabilityConfig;

import java.util.Random;

public class EetleEggsPatchFeature extends Feature<ProbabilityConfig> {
	private static final Direction[] DIRECTIONS = Direction.values();
	private static final BlockState BASE_STATE = EEBlocks.EETLE_EGGS.get().getDefaultState();

	public EetleEggsPatchFeature(Codec<ProbabilityConfig> codec) {
		super(codec);
	}

	@Override
	public boolean generate(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, ProbabilityConfig config) {
		int i = 0;
		pos = world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, pos);
		EetleEggsBlock.shuffleDirections(DIRECTIONS, rand);
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		float chance = config.probability;
		Block corrockBlock = EEBlocks.CORROCK_END_BLOCK.get();
		Block eumus = EEBlocks.EUMUS.get();
		for (int j = 0; j < 45; j++) {
			mutable.setAndOffset(pos, rand.nextInt(9) - rand.nextInt(9), rand.nextInt(9) - rand.nextInt(9), rand.nextInt(9) - rand.nextInt(9));
			if (world.isAirBlock(mutable) && rand.nextFloat() < chance) {
				for (Direction direction : DIRECTIONS) {
					Block offsetBlock = world.getBlockState(mutable.offset(direction)).getBlock();
					if (offsetBlock == corrockBlock || offsetBlock == eumus) {
						BlockState state = BASE_STATE.with(EetleEggsBlock.FACING, direction.getOpposite());
						if (state.isValidPosition(world, mutable)) {
							world.setBlockState(mutable, state.with(EetleEggsBlock.SIZE, rand.nextFloat() < 0.75F ? 0 : rand.nextFloat() < 0.6F ? 1 : 2), 2);
							i++;
							break;
						}
					}
				}
			}
		}
		return i > 0;
	}
}
