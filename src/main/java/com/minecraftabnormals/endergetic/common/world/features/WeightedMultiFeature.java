package com.minecraftabnormals.endergetic.common.world.features;

import com.minecraftabnormals.endergetic.common.world.configs.WeightedFeatureConfig;
import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public final class WeightedMultiFeature extends Feature<WeightedFeatureConfig> {

	public WeightedMultiFeature(Codec<WeightedFeatureConfig> codec) {
		super(codec);
	}

	@Override
	public boolean generate(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, WeightedFeatureConfig config) {
		return config.getRandomFeature(rand).generate(world, generator, rand, pos);
	}

}
