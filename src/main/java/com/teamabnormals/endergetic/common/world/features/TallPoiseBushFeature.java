package com.teamabnormals.endergetic.common.world.features;

import java.util.function.Supplier;

import com.teamabnormals.endergetic.common.blocks.poise.PoiseTallBushBlock;
import com.teamabnormals.endergetic.core.registry.EEBlocks;
import com.mojang.serialization.Codec;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * @author - SmellyModder(Luke Tonon)
 */
public class TallPoiseBushFeature extends Feature<NoneFeatureConfiguration> {
	private static final Supplier<BlockState> TALL_POISE_BUSH = () -> EEBlocks.TALL_POISE_BUSH.get().defaultBlockState();

	public TallPoiseBushFeature(Codec<NoneFeatureConfiguration> config) {
		super(config);
	}

	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
		boolean flag = false;

		BlockPos pos = context.origin();
		RandomSource rand = context.random();
		WorldGenLevel level = context.level();
		BlockState state = TALL_POISE_BUSH.get();
		PoiseTallBushBlock poiseTallBushBlock = ((PoiseTallBushBlock) state.getBlock());
		for (int i = 0; i < 64; ++i) {
			BlockPos blockpos = pos.offset(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));
			if (!isTouchingBolloomBud(level, blockpos) && level.isEmptyBlock(blockpos) && blockpos.getY() < level.getMaxBuildHeight() - 2 && state.canSurvive(level, blockpos)) {
				poiseTallBushBlock.placeAt(level, blockpos, 2);
				flag = true;
			}
		}

		return flag;
	}

	private boolean isTouchingBolloomBud(LevelAccessor level, BlockPos pos) {
		BlockPos.MutableBlockPos mutable = pos.mutable();
		for (Direction direction : Direction.Plane.HORIZONTAL) {
			BlockPos offset = mutable.relative(direction);
			if (level.getBlockState(offset).getBlock() == EEBlocks.BOLLOOM_BUD.get() || level.getBlockState(offset.above()).getBlock() == EEBlocks.BOLLOOM_BUD.get()) {
				return true;
			}
		}
		return false;
	}
}
