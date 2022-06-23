package com.minecraftabnormals.endergetic.common.world.structures;

import com.google.common.collect.Lists;
import com.minecraftabnormals.endergetic.common.world.structures.pieces.EetleNestPieces;
import com.minecraftabnormals.endergetic.core.registry.EEEntities;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

import java.util.List;

import net.minecraft.world.level.levelgen.feature.StructureFeature.StructureStartFactory;

public class EetleNestStructure extends StructureFeature<NoneFeatureConfiguration> {

	public EetleNestStructure(Codec<NoneFeatureConfiguration> codec) {
		super(codec);
	}

	private static int getYPosForStructure(int chunkX, int chunkZ, ChunkGenerator generator) {
		int x = (chunkX << 4) + 7;
		int z = (chunkZ << 4) + 7;
		int center = generator.getFirstOccupiedHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG);
		int centerZOffset = generator.getFirstOccupiedHeight(x, z + 5, Heightmap.Types.WORLD_SURFACE_WG);
		int centerXOffset = generator.getFirstOccupiedHeight(x + 5, z, Heightmap.Types.WORLD_SURFACE_WG);
		int centerXZOffset = generator.getFirstOccupiedHeight(x + 5, z + 5, Heightmap.Types.WORLD_SURFACE_WG);
		return Math.min(Math.min(center, centerZOffset), Math.min(centerXOffset, centerXZOffset));
	}

	private static boolean isAreaCarvable(int chunkX, int chunkZ, int y, ChunkGenerator generator) {
		int x = (chunkX << 4);
		int z = (chunkZ << 4);
		int foundAirBlocks = 0;
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
		for (int posX = x - 24; posX < x + 24; posX++) {
			for (int posZ = z - 24; posZ < z + 24; posZ++) {
				BlockGetter reader = generator.getBaseColumn(posX, posZ);
				for (int posY = y - 40; posY < y - 8; posY++) {
					Block block = reader.getBlockState(mutable.set(posX, posY, posZ)).getBlock();
					if (!EetleNestPieces.CARVABLE_BLOCKS.contains(block)) {
						if (block == Blocks.AIR) {
							if (foundAirBlocks++ >= 576) {
								return false;
							}
						} else {
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	@Override
	protected boolean isFeatureChunk(ChunkGenerator generator, BiomeSource provider, long seed, WorldgenRandom sharedSeedRandom, int chunkX, int chunkZ, Biome biome, ChunkPos chunkPos, NoneFeatureConfiguration config) {
		int yPos = getYPosForStructure(chunkX, chunkZ, generator);
		return yPos >= 60 && isAreaCarvable(chunkX, chunkZ, yPos, generator);
	}

	@Override
	public StructureStartFactory<NoneFeatureConfiguration> getStartFactory() {
		return Start::new;
	}

	@Override
	public GenerationStep.Decoration step() {
		return GenerationStep.Decoration.RAW_GENERATION;
	}

	@Override
	public List<MobSpawnSettings.SpawnerData> getDefaultSpawnList() {
		return Lists.newArrayList(
				new MobSpawnSettings.SpawnerData(EEEntities.CHARGER_EETLE.get(), 12, 3, 7),
				new MobSpawnSettings.SpawnerData(EEEntities.GLIDER_EETLE.get(), 8, 3, 6)
		);
	}

	private static class Start extends StructureStart<NoneFeatureConfiguration> {

		public Start(StructureFeature<NoneFeatureConfiguration> structure, int chunkX, int chunkZ, BoundingBox boundingBox, int references, long seed) {
			super(structure, chunkX, chunkZ, boundingBox, references, seed);
		}

		@Override
		public void generatePieces(RegistryAccess dynamicRegistries, ChunkGenerator generator, StructureManager templateManager, int chunkX, int chunkZ, Biome biome, NoneFeatureConfiguration config) {
			int yPos = getYPosForStructure(chunkX, chunkZ, generator);
			if (yPos >= 60) {
				BlockPos corner = new BlockPos(chunkX * 16, yPos, chunkZ * 16);
				this.pieces.add(new EetleNestPieces.EetleNestPiece(templateManager, corner));
				this.calculateBoundingBox();
			}
		}

	}

}
