package com.minecraftabnormals.endergetic.common.world.features.corrock.tower;

import com.minecraftabnormals.abnormals_core.core.util.GenerationPiece;
import com.minecraftabnormals.endergetic.common.world.features.corrock.AbstractCorrockFeature;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.ProbabilityConfig;

import javax.annotation.Nullable;
import java.util.Random;

public final class SmallCorrockTowerFeature extends AbstractCorrockFeature<ProbabilityConfig> {

	public SmallCorrockTowerFeature(Codec<ProbabilityConfig> configFactory) {
		super(configFactory);
	}

	@Override
	public boolean generate(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, ProbabilityConfig config) {
		if (world.isAirBlock(pos) && world.getBlockState(pos.down()).getBlock() == EEBlocks.CORROCK_END_BLOCK.get() && world.getBlockState(pos.down(2)).isSolid()) {
			GenerationPiece base = getBase(world, pos, rand);
			if (base != null) {
				float crownChance = config.probability;
				BlockState corrockBlockState = CORROCK_BLOCK_STATE.getValue();
				GenerationPiece topPiece = new GenerationPiece((w, p) -> w.isAirBlock(p.pos));
				for (int x = -1; x < 2; x++) {
					for (int z = -1; z < 2; z++) {
						BlockPos placingPos = pos.add(x, 1, z);
						topPiece.addBlockPiece(corrockBlockState, placingPos);
						if (rand.nextFloat() < crownChance) {
							topPiece.addBlockPiece(getCorrockCrownStanding(rand.nextInt(16)), placingPos.up());
						}
					}
				}

				float increasedCrownChance = crownChance + 0.05F;
				for (Direction horizontal : Direction.Plane.HORIZONTAL) {
					BlockPos sidePos = pos.up(2).offset(horizontal, 2);
					BlockPos rightPos = sidePos.offset(horizontal.rotateY());
					BlockPos leftPos = sidePos.offset(horizontal.rotateYCCW());

					topPiece.addBlockPiece(corrockBlockState, sidePos);
					if (rand.nextFloat() < increasedCrownChance) {
						topPiece.addBlockPiece(getCorrockCrownStanding(rand.nextInt(16)), sidePos.up());
					}

					topPiece.addBlockPiece(corrockBlockState, rightPos);
					if (rand.nextFloat() < increasedCrownChance) {
						topPiece.addBlockPiece(getCorrockCrownStanding(rand.nextInt(16)), rightPos.up());
					}

					topPiece.addBlockPiece(corrockBlockState, leftPos);
					if (rand.nextFloat() < increasedCrownChance) {
						topPiece.addBlockPiece(getCorrockCrownStanding(rand.nextInt(16)), leftPos.up());
					}
				}

				if (topPiece.canPlace(world)) {
					base.place(world);
					topPiece.place(world);
					world.setBlockState(pos, corrockBlockState, 2);
					return true;
				}
			}
		}
		return false;
	}

	@Nullable
	private static GenerationPiece getBase(IWorld world, BlockPos pos, Random rand) {
		int successfulSides = 0;
		GenerationPiece piece = new GenerationPiece((w, p) -> w.isAirBlock(p.pos) && Block.hasSolidSideOnTop(w, p.pos.down()));
		BlockState corrockBlockState = CORROCK_BLOCK_STATE.getValue();
		for (Direction horizontal : Direction.Plane.HORIZONTAL) {
			int length = 0;
			for (int i = 1; i < 3; i++) {
				BlockPos offset = pos.offset(horizontal, i);
				if (world.isAirBlock(offset) && Block.hasSolidSideOnTop(world, offset.down())) {
					length++;
				} else {
					break;
				}
			}
			if (length > 0) {
				for (int i2 = 1; i2 < length + 1; i2++) {
					piece.addBlockPiece(corrockBlockState, pos.offset(horizontal, i2));
				}
				successfulSides++;
			}
		}
		return successfulSides >= 2 ? piece : null;
	}

}
