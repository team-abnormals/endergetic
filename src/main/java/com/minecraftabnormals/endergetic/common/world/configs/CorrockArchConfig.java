package com.minecraftabnormals.endergetic.common.world.configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.gen.feature.IFeatureConfig;

public final class CorrockArchConfig implements IFeatureConfig {
	public static final Codec<CorrockArchConfig> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				Codec.FLOAT.fieldOf("crown_chance").forGetter(config -> config.crownChance),
				Codec.FLOAT.fieldOf("plant_chance").forGetter(config -> config.plantChance),
				Codec.floatRange(1.0F, 32.0F).fieldOf("min_distance").forGetter(config -> config.minDistance),
				Codec.floatRange(1.0F, 32.0F).fieldOf("max_distance").forGetter(config -> config.maxDistance),
				Codec.floatRange(1.0F, 18.0F).fieldOf("max_arch_height").forGetter(config -> config.maxArchHeight)
		).apply(instance, CorrockArchConfig::new);
	});
	private final float crownChance;
	private final float plantChance;
	private final float minDistance;
	private final float maxDistance;
	private final float maxArchHeight;

	public CorrockArchConfig(float crownChance, float plantChance, float minDistance, float maxDistance, float maxArchHeight) {
		this.crownChance = crownChance;
		this.plantChance = plantChance;
		this.minDistance = minDistance;
		this.maxDistance = maxDistance;
		this.maxArchHeight = maxArchHeight;
	}

	public float getCrownChance() {
		return this.crownChance;
	}

	public float getPlantChance() {
		return this.plantChance;
	}

	public float getMinDistance() {
		return this.minDistance;
	}

	public float getMaxDistance() {
		return this.maxDistance;
	}

	public float getMaxArchHeight() {
		return this.maxArchHeight;
	}
}
