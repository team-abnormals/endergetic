package com.minecraftabnormals.endergetic.common.world.features;

import java.util.Random;
import java.util.function.Supplier;

import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.mojang.serialization.Codec;

import net.minecraft.block.BlockState;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

/**
 * @author - SmellyModder(Luke Tonon)
 */
@SuppressWarnings("deprecation")
public class PoiseBushFeature extends Feature<NoFeatureConfig> {
	private static final Supplier<BlockState> POISE_BUSH = () -> EEBlocks.POISE_BUSH.get().defaultBlockState();

	public PoiseBushFeature(Codec<NoFeatureConfig> config) {
		super(config);
	}

	public boolean place(ISeedReader worldIn, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config) {
		for (BlockState blockstate = worldIn.getBlockState(pos); (blockstate.isAir() || blockstate.is(BlockTags.LEAVES)) && pos.getY() > 0; blockstate = worldIn.getBlockState(pos)) {
			pos = pos.below();
		}

		int i = 0;
		for (int j = 0; j < 128; ++j) {
			BlockPos blockpos = pos.offset(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));
			if (!this.isNearBolloomBud(worldIn, blockpos) && worldIn.isEmptyBlock(blockpos) && POISE_BUSH.get().canSurvive(worldIn, blockpos)) {
				worldIn.setBlock(blockpos, POISE_BUSH.get(), 2);
				++i;
			}
		}

		return i > 0;
	}

	protected boolean isNearBolloomBud(IWorld world, BlockPos pos) {
		return world.getBlockState(pos.north()).getBlock() == EEBlocks.BOLLOOM_BUD.get() || world.getBlockState(pos.east()).getBlock() == EEBlocks.BOLLOOM_BUD.get() || world.getBlockState(pos.south()).getBlock() == EEBlocks.BOLLOOM_BUD.get() || world.getBlockState(pos.west()).getBlock() == EEBlocks.BOLLOOM_BUD.get();
	}
}
