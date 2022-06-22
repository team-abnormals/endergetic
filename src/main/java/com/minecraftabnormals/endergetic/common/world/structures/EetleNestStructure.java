package com.minecraftabnormals.endergetic.common.world.structures;

import com.google.common.collect.Lists;
import com.minecraftabnormals.endergetic.common.world.structures.pieces.EetleNestPieces;
import com.minecraftabnormals.endergetic.core.registry.EEEntities;
import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.IBlockReader;
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

import net.minecraft.world.gen.feature.structure.Structure.IStartFactory;

public class EetleNestStructure extends Structure<NoFeatureConfig> {

	public EetleNestStructure(Codec<NoFeatureConfig> codec) {
		super(codec);
	}

	private static int getYPosForStructure(int chunkX, int chunkZ, ChunkGenerator generator) {
		int x = (chunkX << 4) + 7;
		int z = (chunkZ << 4) + 7;
		int center = generator.getFirstOccupiedHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG);
		int centerZOffset = generator.getFirstOccupiedHeight(x, z + 5, Heightmap.Type.WORLD_SURFACE_WG);
		int centerXOffset = generator.getFirstOccupiedHeight(x + 5, z, Heightmap.Type.WORLD_SURFACE_WG);
		int centerXZOffset = generator.getFirstOccupiedHeight(x + 5, z + 5, Heightmap.Type.WORLD_SURFACE_WG);
		return Math.min(Math.min(center, centerZOffset), Math.min(centerXOffset, centerXZOffset));
	}

	private static boolean isAreaCarvable(int chunkX, int chunkZ, int y, ChunkGenerator generator) {
		int x = (chunkX << 4);
		int z = (chunkZ << 4);
		int foundAirBlocks = 0;
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		for (int posX = x - 24; posX < x + 24; posX++) {
			for (int posZ = z - 24; posZ < z + 24; posZ++) {
				IBlockReader reader = generator.getBaseColumn(posX, posZ);
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
	protected boolean isFeatureChunk(ChunkGenerator generator, BiomeProvider provider, long seed, SharedSeedRandom sharedSeedRandom, int chunkX, int chunkZ, Biome biome, ChunkPos chunkPos, NoFeatureConfig config) {
		int yPos = getYPosForStructure(chunkX, chunkZ, generator);
		return yPos >= 60 && isAreaCarvable(chunkX, chunkZ, yPos, generator);
	}

	@Override
	public IStartFactory<NoFeatureConfig> getStartFactory() {
		return Start::new;
	}

	@Override
	public GenerationStage.Decoration step() {
		return GenerationStage.Decoration.RAW_GENERATION;
	}

	@Override
	public List<MobSpawnInfo.Spawners> getDefaultSpawnList() {
		return Lists.newArrayList(
				new MobSpawnInfo.Spawners(EEEntities.CHARGER_EETLE.get(), 12, 3, 7),
				new MobSpawnInfo.Spawners(EEEntities.GLIDER_EETLE.get(), 8, 3, 6)
		);
	}

	private static class Start extends StructureStart<NoFeatureConfig> {

		public Start(Structure<NoFeatureConfig> structure, int chunkX, int chunkZ, MutableBoundingBox boundingBox, int references, long seed) {
			super(structure, chunkX, chunkZ, boundingBox, references, seed);
		}

		@Override
		public void generatePieces(DynamicRegistries dynamicRegistries, ChunkGenerator generator, TemplateManager templateManager, int chunkX, int chunkZ, Biome biome, NoFeatureConfig config) {
			int yPos = getYPosForStructure(chunkX, chunkZ, generator);
			if (yPos >= 60) {
				BlockPos corner = new BlockPos(chunkX * 16, yPos, chunkZ * 16);
				this.pieces.add(new EetleNestPieces.EetleNestPiece(templateManager, corner));
				this.calculateBoundingBox();
			}
		}

	}

}
