package com.minecraftabnormals.endergetic.common.world.features.corrock;

import java.util.Random;

import com.minecraftabnormals.endergetic.common.world.configs.EndergeticPatchConfig;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.mojang.serialization.Codec;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;

public class CorrockPatchFeature extends AbstractCorrockFeature<EndergeticPatchConfig> {

	public CorrockPatchFeature(Codec<EndergeticPatchConfig> config) {
		super(config);
	}

	public boolean place(WorldGenLevel world, ChunkGenerator generator, Random rand, BlockPos pos, EndergeticPatchConfig config) {
		BlockPos blockpos = EndergeticPatchConfig.getPos(world, pos, config.shouldSearchDown());
		Block downBlock = world.getBlockState(blockpos.below()).getBlock();
		if (downBlock == CORROCK_BLOCK_BLOCK || downBlock == EEBlocks.EUMUS.get()) {
			int i = 0;
			BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

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