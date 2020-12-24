package com.minecraftabnormals.endergetic.common.world.surfacebuilders;

import java.util.Random;

import com.mojang.serialization.Codec;

import net.minecraft.block.BlockState;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.surfacebuilders.DefaultSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

public class SparseCorrockSurfaceBuilder extends DefaultSurfaceBuilder {

	public SparseCorrockSurfaceBuilder(Codec<SurfaceBuilderConfig> config) {
		super(config);
	}

	public void buildSurface(Random random, IChunk chunkIn, Biome biomeIn, int x, int z, int startHeight, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, long seed, SurfaceBuilderConfig config) {
		if (noise > 3.5D) {
			SurfaceBuilder.DEFAULT.buildSurface(random, chunkIn, biomeIn, x, z, startHeight, noise, defaultBlock, defaultFluid, seaLevel, seed, EESurfaceBuilders.Configs.CORROCK_CONFIG.get());
		} else if (noise > 2.85D) {
			SurfaceBuilder.DEFAULT.buildSurface(random, chunkIn, biomeIn, x, z, startHeight, noise, defaultBlock, defaultFluid, seaLevel, seed, EESurfaceBuilders.Configs.EUMUS_CONFIG.get());
		} else {
			SurfaceBuilder.DEFAULT.buildSurface(random, chunkIn, biomeIn, x, z, startHeight, noise, defaultBlock, defaultFluid, seaLevel, seed, EESurfaceBuilders.Configs.END_STONE_CONFIG.get());
		}
	}

}