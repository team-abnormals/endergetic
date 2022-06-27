package com.minecraftabnormals.endergetic.common.world.features;

import java.util.function.Supplier;

import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.mojang.serialization.Codec;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.tags.BlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * @author - SmellyModder(Luke Tonon)
 */
public class PoiseBushFeature extends Feature<NoneFeatureConfiguration> {
	private static final Supplier<BlockState> POISE_BUSH = () -> EEBlocks.POISE_BUSH.get().defaultBlockState();

	public PoiseBushFeature(Codec<NoneFeatureConfiguration> config) {
		super(config);
	}

	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
		WorldGenLevel worldIn = context.level();
		BlockPos pos = context.origin();
		for (BlockState blockstate = worldIn.getBlockState(pos); (blockstate.isAir() || blockstate.is(BlockTags.LEAVES)) && pos.getY() > 0; blockstate = worldIn.getBlockState(pos)) {
			pos = pos.below();
		}

		int i = 0;
		RandomSource rand = context.random();
		for (int j = 0; j < 128; ++j) {
			BlockPos blockpos = pos.offset(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));
			if (!this.isNearBolloomBud(worldIn, blockpos) && worldIn.isEmptyBlock(blockpos) && POISE_BUSH.get().canSurvive(worldIn, blockpos)) {
				worldIn.setBlock(blockpos, POISE_BUSH.get(), 2);
				++i;
			}
		}

		return i > 0;
	}

	protected boolean isNearBolloomBud(LevelAccessor world, BlockPos pos) {
		return world.getBlockState(pos.north()).getBlock() == EEBlocks.BOLLOOM_BUD.get() || world.getBlockState(pos.east()).getBlock() == EEBlocks.BOLLOOM_BUD.get() || world.getBlockState(pos.south()).getBlock() == EEBlocks.BOLLOOM_BUD.get() || world.getBlockState(pos.west()).getBlock() == EEBlocks.BOLLOOM_BUD.get();
	}
}
