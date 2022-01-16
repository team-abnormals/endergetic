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
	public boolean place(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, ProbabilityConfig config) {
		Block belowBlock = world.getBlockState(pos.below()).getBlock();
		if (world.isEmptyBlock(pos) && belowBlock == EEBlocks.CORROCK_END_BLOCK.get()) {
			GenerationPiece base = getBase(world, pos, rand);
			if (base != null) {
				float crownChance = config.probability;
				BlockState corrockBlockState = CORROCK_BLOCK_STATE.get();
				GenerationPiece topPiece = new GenerationPiece((w, p) -> w.isEmptyBlock(p.pos));
				for (int x = -1; x < 2; x++) {
					for (int z = -1; z < 2; z++) {
						BlockPos placingPos = pos.offset(x, 1, z);
						topPiece.addBlockPiece(corrockBlockState, placingPos);
						if (rand.nextFloat() < crownChance) {
							topPiece.addBlockPiece(getCorrockCrownStanding(rand.nextInt(16)), placingPos.above());
						}
					}
				}

				float increasedCrownChance = crownChance + 0.05F;
				for (Direction horizontal : Direction.Plane.HORIZONTAL) {
					BlockPos sidePos = pos.above(2).relative(horizontal, 2);
					BlockPos rightPos = sidePos.relative(horizontal.getClockWise());
					BlockPos leftPos = sidePos.relative(horizontal.getCounterClockWise());

					topPiece.addBlockPiece(corrockBlockState, sidePos);
					if (rand.nextFloat() < increasedCrownChance) {
						topPiece.addBlockPiece(getCorrockCrownStanding(rand.nextInt(16)), sidePos.above());
					}

					topPiece.addBlockPiece(corrockBlockState, rightPos);
					if (rand.nextFloat() < increasedCrownChance) {
						topPiece.addBlockPiece(getCorrockCrownStanding(rand.nextInt(16)), rightPos.above());
					}

					topPiece.addBlockPiece(corrockBlockState, leftPos);
					if (rand.nextFloat() < increasedCrownChance) {
						topPiece.addBlockPiece(getCorrockCrownStanding(rand.nextInt(16)), leftPos.above());
					}
				}

				if (topPiece.canPlace(world)) {
					base.place(world);
					topPiece.place(world);
					world.setBlock(pos, corrockBlockState, 2);
					return true;
				}
			}
		}
		return false;
	}

	@Nullable
	private static GenerationPiece getBase(IWorld world, BlockPos pos, Random rand) {
		int successfulSides = 0;
		GenerationPiece piece = new GenerationPiece((w, p) -> w.isEmptyBlock(p.pos) && Block.canSupportRigidBlock(w, p.pos.below()));
		BlockState corrockBlockState = CORROCK_BLOCK_STATE.get();
		for (Direction horizontal : Direction.Plane.HORIZONTAL) {
			int length = 0;
			for (int i = 1; i < 3; i++) {
				BlockPos offset = pos.relative(horizontal, i);
				if (world.isEmptyBlock(offset) && Block.canSupportRigidBlock(world, offset.below())) {
					length++;
				} else {
					break;
				}
			}
			if (length > 0) {
				for (int i2 = 1; i2 < length + 1; i2++) {
					piece.addBlockPiece(corrockBlockState, pos.relative(horizontal, i2));
				}
				successfulSides++;
			}
		}
		return successfulSides >= 2 ? piece : null;
	}

}
