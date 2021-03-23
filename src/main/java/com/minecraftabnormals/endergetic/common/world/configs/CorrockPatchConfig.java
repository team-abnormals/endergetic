package com.minecraftabnormals.endergetic.common.world.configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.gen.feature.IFeatureConfig;

public final class CorrockPatchConfig implements IFeatureConfig {
	public static final Codec<CorrockPatchConfig> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				Codec.FLOAT.fieldOf("plant_frequency").forGetter(config -> config.plantFrequency),
				Codec.BOOL.fieldOf("search_down").forGetter(config -> config.searchDown)
		).apply(instance, CorrockPatchConfig::new);
	});
	private final float plantFrequency;
	private final boolean searchDown;

	public CorrockPatchConfig(float plantFrequency, boolean searchDown) {
		this.plantFrequency = plantFrequency;
		this.searchDown = searchDown;
	}

	public float getPlantFrequency() {
		return this.plantFrequency;
	}

	public boolean shouldSearchDown() {
		return this.searchDown;
	}
}
