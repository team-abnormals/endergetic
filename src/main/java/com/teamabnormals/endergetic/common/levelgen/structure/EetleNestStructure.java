package com.teamabnormals.endergetic.common.levelgen.structure;

import com.mojang.serialization.Codec;
import com.teamabnormals.endergetic.common.levelgen.structure.structures.EetleNestPieces;
import com.teamabnormals.endergetic.core.registry.EEStructures;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

import java.util.Optional;

public class EetleNestStructure extends Structure {
	public static final Codec<EetleNestStructure> CODEC = simpleCodec(EetleNestStructure::new);

	public EetleNestStructure(Structure.StructureSettings settings) {
		super(settings);
	}

	private static boolean isAreaCarvable(int x, int z, int y, GenerationContext context) {
		LevelHeightAccessor levelHeightAccessor = context.heightAccessor();
		RandomState randomState = context.randomState();
		ChunkGenerator chunkGenerator = context.chunkGenerator();
		int foundAirBlocks = 0;
		for (int posX = x - 24; posX <= x + 24; posX++) {
			for (int posZ = z - 24; posZ <= z + 24; posZ++) {
				NoiseColumn column = chunkGenerator.getBaseColumn(posX, posZ, levelHeightAccessor, randomState);
				for (int posY = y - 40; posY < y - 8; posY++) {
					Block block = column.getBlock(posY).getBlock();
					if (!EetleNestPieces.CARVABLE_BLOCKS.contains(block)) {
						if (block != Blocks.AIR || ++foundAirBlocks >= 576) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	@Override
	@SuppressWarnings("deprecation")
	public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
		BlockPos blockpos = this.getLowestYIn5by5BoxOffset7Blocks(context, Rotation.NONE);
		int yPos = blockpos.getY();
		if (yPos >= 60 && isAreaCarvable(blockpos.getX(), blockpos.getY(), yPos, context)) {
			return Optional.of(new Structure.GenerationStub(blockpos, (builder) -> {
				builder.addPiece(new EetleNestPieces.EetleNestPiece(context.structureTemplateManager(), blockpos));
			}));
		}
		return Optional.empty();
	}

	@Override
	public StructureType<?> type() {
		return EEStructures.EETLE_NEST_TYPE.get();
	}
}
