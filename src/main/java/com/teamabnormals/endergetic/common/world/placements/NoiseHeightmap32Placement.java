package com.teamabnormals.endergetic.common.world.placements;

import com.teamabnormals.endergetic.core.registry.EEPlacementModifierTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class NoiseHeightmap32Placement extends PlacementModifier {
	public static final Codec<NoiseHeightmap32Placement> CODEC = RecordCodecBuilder.create((p_191761_) -> {
		return p_191761_.group(Codec.DOUBLE.fieldOf("noise_level").forGetter((p_191771_) -> {
			return p_191771_.noiseLevel;
		}), Codec.INT.fieldOf("below_noise").forGetter((p_191769_) -> {
			return p_191769_.belowNoise;
		}), Codec.INT.fieldOf("above_noise").forGetter((p_191763_) -> {
			return p_191763_.aboveNoise;
		})).apply(p_191761_, NoiseHeightmap32Placement::new);
	});
	private final double noiseLevel;
	private final int belowNoise;
	private final int aboveNoise;

	public NoiseHeightmap32Placement(double noiseLevel, int belowNoise, int aboveNoise) {
		this.noiseLevel = noiseLevel;
		this.belowNoise = belowNoise;
		this.aboveNoise = aboveNoise;
	}

	@Override
	public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos pos) {
		double d0 = Biome.BIOME_INFO_NOISE.getValue((double)pos.getX() / 200.0D, (double)pos.getZ() / 200.0D, false);
		int i = d0 < this.noiseLevel ? this.belowNoise : this.aboveNoise;
		return IntStream.range(0, i).mapToObj((p_227449_3_) -> {
			int x = random.nextInt(16) + pos.getX();
			int z = random.nextInt(16) + pos.getZ();
			int y = context.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z) + 32;
			return y <= 0 ? null : new BlockPos(x, random.nextInt(y), z);
		}).filter(Objects::nonNull);
	}

	@Override
	public PlacementModifierType<?> type() {
		return EEPlacementModifierTypes.NOISE_HEIGHTMAP_32.get();
	}
}
