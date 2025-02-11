package com.teamabnormals.endergetic.common.levelgen.placement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamabnormals.endergetic.core.registry.EEPlacementModifierTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public class NoiseRarityFilter extends PlacementFilter {
	public static final Codec<NoiseRarityFilter> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(
			NormalNoise.NoiseParameters.CODEC.fieldOf("noise").forGetter(placement -> placement.noiseParameters),
			Codec.DOUBLE.fieldOf("noise_level").forGetter(filter -> filter.noiseLevel),
			Codec.DOUBLE.fieldOf("below_noise").forGetter(filter -> filter.belowNoise),
			Codec.DOUBLE.fieldOf("above_noise").forGetter(filter -> filter.aboveNoise)
		).apply(instance, NoiseRarityFilter::new);
	});
	private final Holder<NormalNoise.NoiseParameters> noiseParameters;
	private NormalNoise noise;
	private volatile boolean initialized;
	private final double noiseLevel;
	private final double belowNoise;
	private final double aboveNoise;

	public NoiseRarityFilter(Holder<NormalNoise.NoiseParameters> noiseParameters, double noiseLevel, double belowNoise, double aboveNoise) {
		this.noiseParameters = noiseParameters;
		this.noiseLevel = noiseLevel;
		this.belowNoise = belowNoise;
		this.aboveNoise = aboveNoise;
	}

	@Override
	protected boolean shouldPlace(PlacementContext context, RandomSource random, BlockPos pos) {
		if (!this.initialized) {
			synchronized (this) {
				if (!this.initialized) {
					this.noise = NormalNoise.create(WorldgenRandom.Algorithm.LEGACY.newInstance(context.getLevel().getSeed()).forkPositional().fromHashOf(this.noiseParameters.unwrapKey().orElseThrow().location()), this.noiseParameters.value());
					this.initialized = true;
				}
			}
		}
		return random.nextFloat() < (this.noise.getValue(pos.getX(), 0.0D, pos.getZ()) < this.noiseLevel ? this.belowNoise : this.aboveNoise);
	}

	public PlacementModifierType<?> type() {
		return EEPlacementModifierTypes.NOISE_RARITY_FILTER.get();
	}
}