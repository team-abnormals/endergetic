package com.minecraftabnormals.endergetic.common.world.features.corrock;

import java.util.Random;

import com.minecraftabnormals.endergetic.common.world.configs.EndergeticPatchConfig;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.mojang.serialization.Codec;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;

public class CorrockPatchFeature extends AbstractCorrockFeature<EndergeticPatchConfig> {

	public CorrockPatchFeature(Codec<EndergeticPatchConfig> config) {
		super(config);
	}

	public boolean place(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, EndergeticPatchConfig config) {
		BlockPos blockpos = EndergeticPatchConfig.getPos(world, pos, config.shouldSearchDown());
		Block downBlock = world.getBlockState(blockpos.below()).getBlock();
		if (downBlock == CORROCK_BLOCK_BLOCK || downBlock == EEBlocks.EUMUS.get()) {
			int i = 0;
			BlockPos.Mutable mutable = new BlockPos.Mutable();

			float plantFrequency = config.getFrequency();
			for (int j = 0; j < 32; ++j) {
				mutable.setWithOffset(blockpos, rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));
				BlockState state = CORROCK_STATE.get();
				if (world.isEmptyBlock(mutable) && state.canSurvive(world, mutable)) {
					if (world.getBlockState(mutable.below()).getBlock() == CORROCK_BLOCK_BLOCK || rand.nextFloat() < plantFrequency) {
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