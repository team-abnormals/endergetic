package com.minecraftabnormals.endergetic.common.world.placements;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.WorldDecoratingHelper;
import net.minecraft.world.gen.placement.NoiseDependant;
import net.minecraft.world.gen.placement.Placement;

import java.util.Objects;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class NoiseHeightmap32Placement extends Placement<NoiseDependant> {

	public NoiseHeightmap32Placement(Codec<NoiseDependant> codec) {
		super(codec);
	}

	@Override
	public Stream<BlockPos> getPositions(WorldDecoratingHelper helper, Random random, NoiseDependant config, BlockPos pos) {
		double d0 = Biome.INFO_NOISE.noiseAt((double)pos.getX() / 200.0D, (double)pos.getZ() / 200.0D, false);
		int i = d0 < config.noiseLevel ? config.belowNoise : config.aboveNoise;
		return IntStream.range(0, i).mapToObj((p_227449_3_) -> {
			int x = random.nextInt(16) + pos.getX();
			int z = random.nextInt(16) + pos.getZ();
			int y = helper.func_242893_a(Heightmap.Type.MOTION_BLOCKING, x, z) + 32;
			return y <= 0 ? null : new BlockPos(x, random.nextInt(y), z);
		}).filter(Objects::nonNull);
	}

}
