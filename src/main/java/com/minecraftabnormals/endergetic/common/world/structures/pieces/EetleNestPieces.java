package com.minecraftabnormals.endergetic.common.world.structures.pieces;

import com.minecraftabnormals.endergetic.common.world.structures.EEStructures;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.PerlinNoiseGenerator;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.IntStream;

public final class EetleNestPieces {

	private abstract static class EetleNestPiece extends StructurePiece {
		protected static final BlockState CORROCK_BLOCK = EEBlocks.CORROCK_END_BLOCK.get().getDefaultState();
		protected static final BlockState EUMUS = EEBlocks.EUMUS.get().getDefaultState();

		protected EetleNestPiece(IStructurePieceType pieceType, int componentType) {
			super(pieceType, componentType);
		}

		protected EetleNestPiece(IStructurePieceType pieceType, CompoundNBT nbt) {
			super(pieceType, nbt);
		}

		@Override
		protected void readAdditional(CompoundNBT compoundNBT) {
		}
	}

	public static class EetleNestParentPiece extends EetleNestPiece {
		private static final Map<Long, PerlinNoiseGenerator> SURFACE_NOISE = new HashMap<>();

		public EetleNestParentPiece(TemplateManager manager, CompoundNBT compoundNBT) {
			super(EEStructures.PieceTypes.EETLE_NEST_PARENT, compoundNBT);
		}

		public EetleNestParentPiece(BlockPos corner) {
			super(EEStructures.PieceTypes.EETLE_NEST_PARENT, 0);
			this.boundingBox = new MutableBoundingBox(corner.down(32), corner.add(64, 0, 64));
		}

		@SuppressWarnings("deprecation")
		@Override
		public boolean func_230383_a_(ISeedReader world, StructureManager structureManager, ChunkGenerator chunkGenerator, Random random, MutableBoundingBox mutableBoundingBox, ChunkPos chunkPos, BlockPos pos) {
			PerlinNoiseGenerator noiseGenerator = SURFACE_NOISE.computeIfAbsent(world.getSeed(), seedL -> new PerlinNoiseGenerator(new SharedSeedRandom(seedL), IntStream.rangeClosed(-3, 0)));
			int originX = pos.getX();
			int originZ = pos.getZ();
			int radius = 32;
			for (int x = -radius; x < radius; x++) {
				for (int z = -radius; z < radius; z++) {
					double noise = noiseGenerator.noiseAt(x, z, true);
					double areaNoise = noise * 12.0D - 7.0D;
					double distanceSq = x * x + z * z + areaNoise * areaNoise;
					if (distanceSq <= (radius - 3) * (radius - 3)) {
						int posX = originX + x;
						int posZ = originZ + z;
						transformSurface(world, posX, posZ, world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, posX, posZ), world.getSeaLevel() / 2, 40, noise, CORROCK_BLOCK);
					} else if (distanceSq <= radius * radius) {
						int posX = originX + x;
						int posZ = originZ + z;
						transformSurface(world, posX, posZ, world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, posX, posZ), world.getSeaLevel() / 2, 24, noise, EUMUS);
					}
				}
			}
			return true;
		}

		private static void transformSurface(ISeedReader world, int x, int z, int startingY, int maxDepth, int depthScale, double noise, BlockState topState) {
			BlockPos.Mutable mutable = new BlockPos.Mutable(x, startingY, z);
			int depth = (int) (Math.abs(noise) * depthScale + 14.0D);
			BlockState fillState = topState;
			for (int y = startingY, d = 0; y > maxDepth && d < depth; y--, d++) {
				mutable.setY(y);
				if (world.getBlockState(mutable).getBlock() == Blocks.END_STONE) {
					world.setBlockState(mutable, fillState, 2);
					fillState = EUMUS;
				} else if (world.isAirBlock(mutable)) {
					fillState = topState;
				}
			}
		}
	}

}
