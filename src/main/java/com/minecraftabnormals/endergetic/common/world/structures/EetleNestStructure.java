package com.minecraftabnormals.endergetic.common.world.structures;

import com.google.common.collect.Lists;
import com.minecraftabnormals.endergetic.common.world.structures.pieces.EetleNestPieces;
import com.minecraftabnormals.endergetic.core.registry.EEEntities;
import com.mojang.serialization.Codec;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.List;

public class EetleNestStructure extends Structure<NoFeatureConfig> {

	public EetleNestStructure(Codec<NoFeatureConfig> codec) {
		super(codec);
	}

	@Override
	protected boolean func_230363_a_(ChunkGenerator generator, BiomeProvider provider, long seed, SharedSeedRandom sharedSeedRandom, int chunkX, int chunkZ, Biome biome, ChunkPos chunkPos, NoFeatureConfig config) {
		return getYPosForStructure(chunkX, chunkZ, generator) >= 60;
	}

	@Override
	public IStartFactory<NoFeatureConfig> getStartFactory() {
		return Start::new;
	}

	@Override
	public GenerationStage.Decoration getDecorationStage() {
		return GenerationStage.Decoration.SURFACE_STRUCTURES;
	}

	@Override
	public List<MobSpawnInfo.Spawners> getDefaultSpawnList() {
		return Lists.newArrayList(
				new MobSpawnInfo.Spawners(EEEntities.CHARGER_EETLE.get(), 12, 3, 7),
				new MobSpawnInfo.Spawners(EEEntities.GLIDER_EETLE.get(), 8, 3, 6)
		);
	}

	private static int getYPosForStructure(int chunkX, int chunkZ, ChunkGenerator generator) {
		int x = (chunkX << 4) + 7;
		int z = (chunkZ << 4) + 7;
		int center = generator.getNoiseHeightMinusOne(x, z, Heightmap.Type.WORLD_SURFACE_WG);
		int centerZOffset = generator.getNoiseHeightMinusOne(x, z + 5, Heightmap.Type.WORLD_SURFACE_WG);
		int centerXOffset = generator.getNoiseHeightMinusOne(x + 5, z, Heightmap.Type.WORLD_SURFACE_WG);
		int centerXZOffset = generator.getNoiseHeightMinusOne(x + 5, z + 5, Heightmap.Type.WORLD_SURFACE_WG);
		return Math.min(Math.min(center, centerZOffset), Math.min(centerXOffset, centerXZOffset));
	}

	private static class Start extends StructureStart<NoFeatureConfig> {

		public Start(Structure<NoFeatureConfig> structure, int chunkX, int chunkZ, MutableBoundingBox boundingBox, int references, long seed) {
			super(structure, chunkX, chunkZ, boundingBox, references, seed);
		}

		@Override
		public void func_230364_a_(DynamicRegistries dynamicRegistries, ChunkGenerator generator, TemplateManager templateManager, int chunkX, int chunkZ, Biome biome, NoFeatureConfig config) {
			int yPos = getYPosForStructure(chunkX, chunkZ, generator);
			if (yPos >= 60) {
				BlockPos corner = new BlockPos(chunkX * 16, yPos, chunkZ * 16);
				this.components.add(new EetleNestPieces.EetleNestParentPiece(templateManager, corner));
				this.recalculateStructureSize();
			}
		}

	}

}
