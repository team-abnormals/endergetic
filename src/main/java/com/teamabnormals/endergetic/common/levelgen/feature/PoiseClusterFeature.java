package com.teamabnormals.endergetic.common.levelgen.feature;

import com.mojang.serialization.Codec;
import com.teamabnormals.endergetic.core.registry.EEBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * @author - SmellyModder(Luke Tonon)
 */
public class PoiseClusterFeature extends Feature<NoneFeatureConfiguration> {
	private static final BlockState GLOWING_POISE_LOG = EEBlocks.GLOWING_POISE_WOOD.get().defaultBlockState();
	private static final BlockState POISE_CLUSTER = EEBlocks.POISE_CLUSTER.get().defaultBlockState();

	public PoiseClusterFeature(Codec<NoneFeatureConfiguration> configFactoryIn) {
		super(configFactoryIn);
	}

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
		WorldGenLevel level = context.level();
		BlockPos pos = context.origin();
		if (level.getBlockState(pos).getMaterial().isReplaceable() && level.getBlockState(pos.below()).getBlock() == EEBlocks.POISMOSS.get()) {
			RandomSource rand = context.random();
			this.createGlob(rand.nextInt(12), level, pos, rand);
			return true;
		}
		return false;
	}

	private void createGlob(int variation, LevelAccessor level, BlockPos pos, RandomSource rand) {
		this.setBlockIfReplacable(level, pos, GLOWING_POISE_LOG);
		this.setBlockIfReplacable(level, pos.north(), POISE_CLUSTER);
		this.setBlockIfReplacable(level, pos.east(), POISE_CLUSTER);
		this.setBlockIfReplacable(level, pos.south(), POISE_CLUSTER);
		this.setBlockIfReplacable(level, pos.west(), POISE_CLUSTER);
		this.setBlockIfReplacable(level, pos.above(), POISE_CLUSTER);

		this.setBlockIfReplacable(level, pos.north().above(), POISE_CLUSTER);
		this.setBlockIfReplacable(level, pos.east().above(), POISE_CLUSTER);
		this.setBlockIfReplacable(level, pos.south().above(), POISE_CLUSTER);
		this.setBlockIfReplacable(level, pos.west().above(), POISE_CLUSTER);

		this.setBlockIfReplacable(level, pos.north(2), POISE_CLUSTER);
		this.setBlockIfReplacable(level, pos.east(2), POISE_CLUSTER);
		this.setBlockIfReplacable(level, pos.south(2), POISE_CLUSTER);
		this.setBlockIfReplacable(level, pos.west(2), POISE_CLUSTER);

		this.setBlockIfReplacable(level, pos.north().east(), POISE_CLUSTER);
		this.setBlockIfReplacable(level, pos.north().west(), POISE_CLUSTER);
		this.setBlockIfReplacable(level, pos.south().east(), POISE_CLUSTER);
		this.setBlockIfReplacable(level, pos.south().west(), POISE_CLUSTER);

		if (variation <= 2) {
			this.setBlockIfReplacable(level, pos.above().above(), POISE_CLUSTER);
		} else if (variation <= 7) {
			if (rand.nextInt(2) == 1) this.setBlockIfReplacable(level, pos.north().west().above(), POISE_CLUSTER);
			if (rand.nextInt(2) == 1) this.setBlockIfReplacable(level, pos.north().east().above(), POISE_CLUSTER);
			if (rand.nextInt(2) == 1) this.setBlockIfReplacable(level, pos.south().west().above(), POISE_CLUSTER);
			if (rand.nextInt(2) == 1) this.setBlockIfReplacable(level, pos.south().east().above(), POISE_CLUSTER);

			if (rand.nextInt(2) == 1) this.setBlockIfReplacable(level, pos.north(2).west(), POISE_CLUSTER);
			if (rand.nextInt(2) == 1) this.setBlockIfReplacable(level, pos.north(2).east(), POISE_CLUSTER);
			if (rand.nextInt(2) == 1) this.setBlockIfReplacable(level, pos.south(2).west(), POISE_CLUSTER);
			if (rand.nextInt(2) == 1) this.setBlockIfReplacable(level, pos.west(2).east(), POISE_CLUSTER);
		} else if (variation <= 10) {
			int i = rand.nextInt(4);
			if (i == 0) {
				this.setBlockIfReplacable(level, pos.north().above().east(), POISE_CLUSTER);
				this.setBlockIfReplacable(level, pos.north().east(2), POISE_CLUSTER);
				this.setBlockIfReplacable(level, pos.north(2).east(), POISE_CLUSTER);
				this.setBlockIfReplacable(level, pos.north(2).east(2), POISE_CLUSTER);
			} else if (i == 1) {
				this.setBlockIfReplacable(level, pos.east().above().south(), POISE_CLUSTER);
				this.setBlockIfReplacable(level, pos.east().south(2), POISE_CLUSTER);
				this.setBlockIfReplacable(level, pos.east(2).south(), POISE_CLUSTER);
				this.setBlockIfReplacable(level, pos.east(2).south(2), POISE_CLUSTER);
			} else if (i == 2) {
				this.setBlockIfReplacable(level, pos.south().above().west(), POISE_CLUSTER);
				this.setBlockIfReplacable(level, pos.south().west(2), POISE_CLUSTER);
				this.setBlockIfReplacable(level, pos.south(2).west(), POISE_CLUSTER);
				this.setBlockIfReplacable(level, pos.south(2).west(2), POISE_CLUSTER);
			} else if (i == 3) {
				this.setBlockIfReplacable(level, pos.west().above().north(), POISE_CLUSTER);
				this.setBlockIfReplacable(level, pos.west().north(2), POISE_CLUSTER);
				this.setBlockIfReplacable(level, pos.west(2).north(), POISE_CLUSTER);
				this.setBlockIfReplacable(level, pos.west(2).north(2), POISE_CLUSTER);
			}
		}
	}

	private void setBlockIfReplacable(LevelAccessor world, BlockPos pos, BlockState newState) {
		if (world.getBlockState(pos).getMaterial().isReplaceable() && world.getBlockState(pos.above()).getBlock() != EEBlocks.TALL_POISE_BUSH.get() && world.getBlockState(pos.below()).getBlock() != EEBlocks.TALL_POISE_BUSH.get()) {
			world.setBlock(pos, newState, 2);
		}
	}
}
