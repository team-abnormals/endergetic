package com.minecraftabnormals.endergetic.common.world.configs;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.WeightedList;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.IFeatureConfig;

import java.util.Random;

public final class WeightedFeatureConfig implements IFeatureConfig {
	public static Codec<WeightedFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				WeightedList.func_234002_a_(ConfiguredFeature.field_242763_a).fieldOf("features").forGetter(config -> config.weightedFeatures)
		).apply(instance, WeightedFeatureConfig::new);
	});
	private final WeightedList<ConfiguredFeature<?, ?>> weightedFeatures;

	public WeightedFeatureConfig(WeightedList<ConfiguredFeature<?, ?>> weightedFeatures) {
		this.weightedFeatures = weightedFeatures;
	}

	@SafeVarargs
	public static WeightedFeatureConfig createFromPairs(Pair<ConfiguredFeature<?, ?>, Integer>... pairs) {
		WeightedList<ConfiguredFeature<?, ?>> weightedList = new WeightedList<>();
		for (Pair<ConfiguredFeature<?, ?>, Integer> pair : pairs) {
			weightedList.func_226313_a_(pair.getFirst(), pair.getSecond());
		}
		return new WeightedFeatureConfig(weightedList);
	}

	public ConfiguredFeature<?, ?> getRandomFeature(Random random) {
		return this.weightedFeatures.func_226318_b_(random);
	}

	public WeightedList<ConfiguredFeature<?, ?>> getWeightedFeatures() {
		return this.weightedFeatures;
	}
}
