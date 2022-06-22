package com.minecraftabnormals.endergetic.common.world.features;

import java.util.Random;
import java.util.function.Supplier;

import com.minecraftabnormals.endergetic.common.blocks.poise.PoiseTallBushBlock;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.mojang.serialization.Codec;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

/**
 * @author - SmellyModder(Luke Tonon)
 */
public class TallPoiseBushFeature extends Feature<NoFeatureConfig> {
	private static final Supplier<BlockState> TALL_POISE_BUSH = () -> EEBlocks.TALL_POISE_BUSH.get().defaultBlockState();

	public TallPoiseBushFeature(Codec<NoFeatureConfig> config) {
		super(config);
	}

	public boolean place(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config) {
		boolean flag = false;

		for (int i = 0; i < 64; ++i) {
			BlockPos blockpos = pos.offset(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));
			if (!isTouchingBolloomBud(world, blockpos) && world.isEmptyBlock(blockpos) && blockpos.getY() < world.getMaxBuildHeight() - 2 && TALL_POISE_BUSH.get().canSurvive(world, blockpos)) {
				((PoiseTallBushBlock) TALL_POISE_BUSH.get().getBlock()).placeAt(world, blockpos, 2);
				flag = true;
			}
		}

		return flag;
	}

	private boolean isTouchingBolloomBud(IWorld world, BlockPos pos) {
		BlockPos.Mutable mutable = pos.mutable();
		for (Direction direction : Direction.Plane.HORIZONTAL) {
			BlockPos offset = mutable.relative(direction);
			if (world.getBlockState(offset).getBlock() == EEBlocks.BOLLOOM_BUD.get() || world.getBlockState(offset.above()).getBlock() == EEBlocks.BOLLOOM_BUD.get()) {
				return true;
			}
		}
		return false;
	}
}
