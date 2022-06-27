package com.minecraftabnormals.endergetic.common.world.features.corrock.tower;

import com.minecraftabnormals.endergetic.common.world.features.corrock.AbstractCorrockFeature;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.mojang.serialization.Codec;
import com.teamabnormals.blueprint.core.util.GenerationPiece;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;

import javax.annotation.Nullable;

public final class SmallCorrockTowerFeature extends AbstractCorrockFeature<ProbabilityFeatureConfiguration> {

	public SmallCorrockTowerFeature(Codec<ProbabilityFeatureConfiguration> configFactory) {
		super(configFactory);
	}

	@Override
	public boolean place(FeaturePlaceContext<ProbabilityFeatureConfiguration> context) {
		WorldGenLevel level = context.level();
		BlockPos pos = context.origin();
		if (level.isEmptyBlock(pos) && level.getBlockState(pos.below()).getBlock() == EEBlocks.CORROCK_END_BLOCK.get() && level.getBlockState(pos.below(2)).canOcclude()) {
			GenerationPiece base = getBase(level, pos);
			if (base != null) {
				BlockState corrockBlockState = CORROCK_BLOCK_STATE.get();
				GenerationPiece topPiece = new GenerationPiece((w, p) -> w.isEmptyBlock(p.pos));
				RandomSource rand = context.random();
				float crownChance = context.config().probability;
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

				if (topPiece.canPlace(level)) {
					base.place(level);
					topPiece.place(level);
					level.setBlock(pos, corrockBlockState, 2);
					return true;
				}
			}
		}
		return false;
	}

	@Nullable
	private static GenerationPiece getBase(LevelAccessor level, BlockPos pos) {
		int successfulSides = 0;
		GenerationPiece piece = new GenerationPiece((w, p) -> w.isEmptyBlock(p.pos) && Block.canSupportRigidBlock(w, p.pos.below()));
		BlockState corrockBlockState = CORROCK_BLOCK_STATE.get();
		for (Direction horizontal : Direction.Plane.HORIZONTAL) {
			int length = 0;
			for (int i = 1; i < 3; i++) {
				BlockPos offset = pos.relative(horizontal, i);
				if (level.isEmptyBlock(offset) && Block.canSupportRigidBlock(level, offset.below())) {
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
