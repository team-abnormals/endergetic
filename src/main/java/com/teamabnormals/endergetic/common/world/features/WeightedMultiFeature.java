package com.teamabnormals.endergetic.common.world.features;

import com.teamabnormals.endergetic.common.world.configs.WeightedFeatureConfig;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public final class WeightedMultiFeature extends Feature<WeightedFeatureConfig> {

	public WeightedMultiFeature(Codec<WeightedFeatureConfig> codec) {
		super(codec);
	}

	@Override
	public boolean place(FeaturePlaceContext<WeightedFeatureConfig> context) {
		return context.config().getRandomFeature(context.random()).place(context.level(), context.chunkGenerator(), context.random(), context.origin());
	}

}
