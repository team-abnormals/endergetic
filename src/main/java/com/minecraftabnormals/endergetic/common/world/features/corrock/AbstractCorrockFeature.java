package com.minecraftabnormals.endergetic.common.world.features.corrock;

import java.util.List;

import com.minecraftabnormals.endergetic.common.blocks.CorrockCrownStandingBlock;
import com.mojang.serialization.Codec;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;

import com.teamabnormals.blueprint.core.util.GenerationPiece;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.ChorusFlowerBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.core.Direction;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import javax.annotation.Nullable;

@SuppressWarnings("deprecation")
public abstract class AbstractCorrockFeature<FC extends FeatureConfiguration> extends Feature<FC> {
	protected static final Block CORROCK_BLOCK_BLOCK = EEBlocks.CORROCK_END_BLOCK.get();
	protected static final LazyLoadedValue<BlockState> CORROCK_STATE = new LazyLoadedValue<>(() -> EEBlocks.CORROCK_END.get().defaultBlockState());
	protected static final LazyLoadedValue<BlockState> CORROCK_BLOCK_STATE = new LazyLoadedValue<>(() -> EEBlocks.CORROCK_END_BLOCK.get().defaultBlockState());

	protected static BlockState getCorrockCrownWall(Direction facing) {
		return EEBlocks.CORROCK_CROWN_END_WALL.get().defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, facing);
	}

	protected static BlockState getCorrockCrownStanding(int rotation) {
		return EEBlocks.CORROCK_CROWN_END_STANDING.get().defaultBlockState().setValue(CorrockCrownStandingBlock.ROTATION, rotation);
	}

	public AbstractCorrockFeature(Codec<FC> configFactory) {
		super(configFactory);
	}

	protected static boolean isNotCloseToAnotherGrowth(List<ChorusPlantPart> growths, BlockPos pos) {
		for (ChorusPlantPart part : growths) {
			if (Mth.sqrt((float) part.pos.distSqr(pos)) < 2.0F) {
				return false;
			}
		}
		return true;
	}

	protected static boolean tryToFillWithCorrockBlock(WorldGenLevel level, int x1, int y1, int z1, int x2, int y2, int z2, List<BlockPos> positions) {
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
		for (int yy = y1; yy <= y2; yy++) {
			for (int xx = x1; xx <= x2; xx++) {
				for (int zz = z1; zz <= z2; zz++) {
					if (level.isEmptyBlock(mutable.set(xx, yy, zz))) {
						positions.add(mutable.immutable());
					} else {
						return false;
					}
				}
			}
		}
		return true;
	}

	protected static boolean tryToPlaceCorrockBlock(WorldGenLevel level, BlockPos pos, List<BlockPos> positions) {
		if (level.isEmptyBlock(pos)) {
			positions.add(pos.immutable());
			return true;
		}
		return false;
	}

	protected static boolean tryToPlaceCorrockBlockWithCrown(WorldGenLevel level, RandomSource rand, BlockPos pos, List<BlockPos> positions, Direction direction, GenerationPiece crowns, @Nullable List<BlockPos> corners, float crownChance) {
		if (level.isEmptyBlock(pos)) {
			BlockPos immutable = pos.immutable();
			positions.add(immutable);
			if (corners != null) {
				corners.add(immutable);
			}
			if (rand.nextFloat() < crownChance) {
				if (rand.nextBoolean()) {
					BlockPos up = pos.above();
					if (level.isEmptyBlock(up)) {
						crowns.addBlockPiece(getCorrockCrownStanding(rand.nextInt(16)), up);
					}
				} else {
					BlockPos offset = pos.relative(direction);
					if (level.isEmptyBlock(offset)) {
						crowns.addBlockPiece(getCorrockCrownWall(direction), offset);
					}
				}
			}
			return true;
		}
		return false;
	}

	protected static boolean tryToPlaceCrownedCorrockSquare(WorldGenLevel level, RandomSource rand, int y, int x1, int z1, int x2, int z2, List<BlockPos> positions, Direction direction, GenerationPiece crowns, float crownChance) {
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
		for (int x = x1; x <= x2; x++) {
			for (int z = z1; z <= z2; z++) {
				if (!tryToPlaceCorrockBlockWithCrown(level, rand, mutable.set(x, y, z), positions, direction, crowns, null, crownChance)) {
					return false;
				}
			}
		}
		return true;
	}

	protected static class ChorusPlantPart {
		public final BlockPos pos;

		public ChorusPlantPart(BlockPos pos) {
			this.pos = pos;
		}

		public void placeGrowth(LevelAccessor world, RandomSource rand) {
			world.setBlock(this.pos, CORROCK_BLOCK_STATE.get(), 2);
			ChorusFlowerBlock.generatePlant(world, this.pos.above(), rand, 8);
		}
	}
}