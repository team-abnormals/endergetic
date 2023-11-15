package com.teamabnormals.endergetic.common.levelgen.configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public final class CorrockTowerConfig implements FeatureConfiguration {
	public static Codec<CorrockTowerConfig> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				Codec.intRange(1, 128).optionalFieldOf("min_height", 2).forGetter(config -> config.minHeight),
				Codec.intRange(2, 128).optionalFieldOf("max_height", 4).forGetter(config -> config.maxHeight),
				Codec.FLOAT.fieldOf("crown_chance").forGetter(config -> config.crownChance),
				Codec.FLOAT.fieldOf("chorus_chance").forGetter(config -> config.chorusChance)
		).apply(instance, CorrockTowerConfig::new);
	});
	private final int minHeight;
	private final int maxHeight;
	private final float crownChance;
	private final float chorusChance;

	public CorrockTowerConfig(int minHeight, int maxHeight, float crownChance, float chorusChance) {
		this.minHeight = minHeight;
		this.maxHeight = maxHeight;
		this.crownChance = crownChance;
		this.chorusChance = chorusChance;
	}

	public int getMinHeight() {
		return this.minHeight;
	}

	public int getMaxHeight() {
		return this.maxHeight;
	}

	public float getCrownChance() {
		return this.crownChance;
	}

	public float getChorusChance() {
		return this.chorusChance;
	}
}
