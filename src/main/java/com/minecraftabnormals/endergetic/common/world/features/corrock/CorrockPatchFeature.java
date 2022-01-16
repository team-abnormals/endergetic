package com.minecraftabnormals.endergetic.common.world.features.corrock;

import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.ProbabilityConfig;

import java.util.Random;

public class CorrockPatchFeature extends AbstractCorrockFeature<ProbabilityConfig> {

	public CorrockPatchFeature(Codec<ProbabilityConfig> config) {
		super(config);
	}

	public boolean place(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, ProbabilityConfig config) {
		BlockPos blockpos = world.getHeightmapPos(Heightmap.Type.WORLD_SURFACE_WG, pos);
		Block downBlock = world.getBlockState(blockpos.below()).getBlock();
		if (downBlock == CORROCK_BLOCK_BLOCK || downBlock == EEBlocks.EUMUS.get()) {
			int i = 0;
			BlockPos.Mutable mutable = new BlockPos.Mutable();

			for (int j = 0; j < 32; ++j) {
				mutable.setWithOffset(blockpos, rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));
				BlockState state = CORROCK_STATE.get();
				if (world.isEmptyBlock(mutable) && state.canSurvive(world, mutable)) {
					if (world.getBlockState(mutable.below()).getBlock() == CORROCK_BLOCK_BLOCK || rand.nextFloat() < config.probability) {
						world.setBlock(mutable, state, 2);
						++i;
					}
				}
			}
			return i > 0;
		}
		return false;
	}

}