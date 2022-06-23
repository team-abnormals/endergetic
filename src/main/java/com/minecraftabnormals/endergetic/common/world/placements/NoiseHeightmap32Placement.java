package com.minecraftabnormals.endergetic.common.world.placements;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.placement.DecorationContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoiseDependantDecoratorConfiguration;
import net.minecraft.world.level.levelgen.placement.FeatureDecorator;

import java.util.Objects;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class NoiseHeightmap32Placement extends FeatureDecorator<NoiseDependantDecoratorConfiguration> {

	public NoiseHeightmap32Placement(Codec<NoiseDependantDecoratorConfiguration> codec) {
		super(codec);
	}

	@Override
	public Stream<BlockPos> getPositions(DecorationContext helper, Random random, NoiseDependantDecoratorConfiguration config, BlockPos pos) {
		double d0 = Biome.BIOME_INFO_NOISE.getValue((double)pos.getX() / 200.0D, (double)pos.getZ() / 200.0D, false);
		int i = d0 < config.noiseLevel ? config.belowNoise : config.aboveNoise;
		return IntStream.range(0, i).mapToObj((p_227449_3_) -> {
			int x = random.nextInt(16) + pos.getX();
			int z = random.nextInt(16) + pos.getZ();
			int y = helper.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z) + 32;
			return y <= 0 ? null : new BlockPos(x, random.nextInt(y), z);
		}).filter(Objects::nonNull);
	}

}
