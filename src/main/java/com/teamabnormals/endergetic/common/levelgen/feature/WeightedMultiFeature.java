package com.teamabnormals.endergetic.common.levelgen.feature;

import com.mojang.serialization.Codec;
import com.teamabnormals.endergetic.common.levelgen.configs.WeightedFeatureConfig;
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
