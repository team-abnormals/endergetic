package com.teamabnormals.endergetic.common.levelgen.feature.corrock;

import com.mojang.serialization.Codec;
import com.teamabnormals.endergetic.common.levelgen.configs.EndergeticPatchConfig;
import com.teamabnormals.endergetic.core.registry.EEBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class CorrockPatchFeature extends AbstractCorrockFeature<EndergeticPatchConfig> {

	public CorrockPatchFeature(Codec<EndergeticPatchConfig> config) {
		super(config);
	}

	public boolean place(FeaturePlaceContext<EndergeticPatchConfig> context) {
		WorldGenLevel level = context.level();
		BlockPos pos = context.origin();
		EndergeticPatchConfig config = context.config();
		BlockPos blockpos = EndergeticPatchConfig.getPos(level, pos, config.shouldSearchDown());
		Block downBlock = level.getBlockState(blockpos.below()).getBlock();
		if (downBlock == CORROCK_BLOCK_BLOCK || downBlock == EEBlocks.EUMUS.get()) {
			int i = 0;
			BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

			float plantFrequency = config.getFrequency();
			RandomSource rand = context.random();
			for (int j = 0; j < 32; ++j) {
				mutable.setWithOffset(blockpos, rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));
				BlockState state = CORROCK_STATE.get();
				if (level.isEmptyBlock(mutable) && state.canSurvive(level, mutable)) {
					if (level.getBlockState(mutable.below()).getBlock() == CORROCK_BLOCK_BLOCK || rand.nextFloat() < plantFrequency) {
						level.setBlock(mutable, state, 2);
						++i;
					}
				}
			}
			return i > 0;
		}
		return false;
	}

}