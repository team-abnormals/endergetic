package com.teamabnormals.endergetic.common.levelgen.configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public final class MultiPatchConfig implements FeatureConfiguration {
	public static final Codec<MultiPatchConfig> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				Codec.INT.fieldOf("max_extra_patches").forGetter(config -> config.maxExtraPatches),
				Codec.INT.fieldOf("max_extra_radius").forGetter(config -> config.maxExtraRadius)
		).apply(instance, MultiPatchConfig::new);
	});
	private final int maxExtraPatches;
	private final int maxExtraRadius;

	public MultiPatchConfig(int maxExtraPatches, int maxExtraRadius) {
		this.maxExtraPatches = maxExtraPatches;
		this.maxExtraRadius = maxExtraRadius;
	}

	public int getMaxExtraPatches() {
		return this.maxExtraPatches;
	}

	public int getMaxExtraRadius() {
		return this.maxExtraRadius;
	}
}
