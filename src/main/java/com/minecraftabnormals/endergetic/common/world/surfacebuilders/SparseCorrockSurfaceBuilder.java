package com.minecraftabnormals.endergetic.common.world.surfacebuilders;

import java.util.Random;

import com.mojang.serialization.Codec;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.surfacebuilders.DefaultSurfaceBuilder;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderBaseConfiguration;

public class SparseCorrockSurfaceBuilder extends DefaultSurfaceBuilder {
	private static final double SPECKLED_THRESHOLD = 1.275F;

	public SparseCorrockSurfaceBuilder(Codec<SurfaceBuilderBaseConfiguration> config) {
		super(config);
	}

	public void apply(Random random, ChunkAccess chunkIn, Biome biomeIn, int x, int z, int startHeight, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, long seed, SurfaceBuilderBaseConfiguration config) {
		if (noise > 1.9F) {
			if (noise <= 2.1F) {
				SurfaceBuilder.DEFAULT.apply(random, chunkIn, biomeIn, x, z, startHeight, noise, defaultBlock, defaultFluid, seaLevel, seed, random.nextFloat() < 0.35F ? EESurfaceBuilders.Configs.SPECKLED_END_STONE_CONFIG.get() : EESurfaceBuilders.Configs.CORROCK_CONFIG.get());
			} else {
				SurfaceBuilder.DEFAULT.apply(random, chunkIn, biomeIn, x, z, startHeight, noise, defaultBlock, defaultFluid, seaLevel, seed, EESurfaceBuilders.Configs.CORROCK_CONFIG.get());
			}
		} else if (noise > SPECKLED_THRESHOLD && random.nextFloat() <= Math.abs(noise - SPECKLED_THRESHOLD)) {
			SurfaceBuilder.DEFAULT.apply(random, chunkIn, biomeIn, x, z, startHeight, noise, defaultBlock, defaultFluid, seaLevel, seed, EESurfaceBuilders.Configs.SPECKLED_END_STONE_CONFIG.get());
		} else {
			SurfaceBuilder.DEFAULT.apply(random, chunkIn, biomeIn, x, z, startHeight, noise, defaultBlock, defaultFluid, seaLevel, seed, EESurfaceBuilders.Configs.END_STONE_CONFIG.get());
		}
	}
}