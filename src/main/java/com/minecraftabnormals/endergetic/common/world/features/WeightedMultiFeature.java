package com.minecraftabnormals.endergetic.common.world.features;

import com.minecraftabnormals.endergetic.common.world.configs.WeightedFeatureConfig;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;

import java.util.Random;

public final class WeightedMultiFeature extends Feature<WeightedFeatureConfig> {

	public WeightedMultiFeature(Codec<WeightedFeatureConfig> codec) {
		super(codec);
	}

	@Override
	public boolean place(WorldGenLevel world, ChunkGenerator generator, Random rand, BlockPos pos, WeightedFeatureConfig config) {
		return config.getRandomFeature(rand).place(world, generator, rand, pos);
	}

}
