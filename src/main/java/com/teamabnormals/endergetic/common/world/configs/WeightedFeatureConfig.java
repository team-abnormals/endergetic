package com.teamabnormals.endergetic.common.world.configs;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.stream.Stream;

public final class WeightedFeatureConfig implements FeatureConfiguration {
	private static final Codec<WeightedEntry.Wrapper<Holder<ConfiguredFeature<?, ?>>>> ENTRY_CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(
				ConfiguredFeature.CODEC.fieldOf("feature").forGetter(WeightedEntry.Wrapper::getData),
				Codec.intRange(0, Integer.MAX_VALUE).fieldOf("weight").forGetter(wrapper -> wrapper.getWeight().asInt())
		).apply(instance, WeightedEntry::wrap);
	});
	public static final Codec<WeightedFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				WeightedRandomList.codec(ENTRY_CODEC).fieldOf("features").forGetter(config -> config.weightedFeatures)
		).apply(instance, WeightedFeatureConfig::new);
	});
	private final WeightedRandomList<WeightedEntry.Wrapper<Holder<ConfiguredFeature<?, ?>>>> weightedFeatures;

	public WeightedFeatureConfig(WeightedRandomList<WeightedEntry.Wrapper<Holder<ConfiguredFeature<?, ?>>>> weightedFeatures) {
		this.weightedFeatures = weightedFeatures;
	}

	@SafeVarargs
	@SuppressWarnings("unchecked")
	public static WeightedFeatureConfig createFromPairs(Pair<Holder<? extends ConfiguredFeature<?, ?>>, Integer>... pairs) {
		return new WeightedFeatureConfig(WeightedRandomList.create(Stream.of(pairs).map(pair -> WeightedEntry.wrap(pair.getFirst(), pair.getSecond())).toArray(WeightedEntry.Wrapper[]::new)));
	}

	public ConfiguredFeature<?, ?> getRandomFeature(RandomSource random) {
		return this.weightedFeatures.getRandom(random).get().getData().value();
	}

	public WeightedRandomList<WeightedEntry.Wrapper<Holder<ConfiguredFeature<?, ?>>>> getWeightedFeatures() {
		return this.weightedFeatures;
	}
}
