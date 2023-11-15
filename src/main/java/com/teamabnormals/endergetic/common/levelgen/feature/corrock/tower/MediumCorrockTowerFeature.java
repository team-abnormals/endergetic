package com.teamabnormals.endergetic.common.levelgen.feature.corrock.tower;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.teamabnormals.blueprint.core.util.GenerationPiece;
import com.teamabnormals.endergetic.api.util.GenerationUtils;
import com.teamabnormals.endergetic.common.levelgen.configs.CorrockTowerConfig;
import com.teamabnormals.endergetic.common.levelgen.feature.corrock.AbstractCorrockFeature;
import com.teamabnormals.endergetic.core.registry.EEBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.List;

public final class MediumCorrockTowerFeature extends AbstractCorrockFeature<CorrockTowerConfig> {

	public MediumCorrockTowerFeature(Codec<CorrockTowerConfig> configFactory) {
		super(configFactory);
	}

	@Override
	public boolean place(FeaturePlaceContext<CorrockTowerConfig> context) {
		WorldGenLevel world = context.level();
		BlockPos pos = context.origin();
		if (world.isEmptyBlock(pos) && world.getBlockState(pos.below()).getBlock() == EEBlocks.CORROCK_END_BLOCK.get() && world.getBlockState(pos.below(2)).canOcclude()) {
			BlockState corrockBlockState = CORROCK_BLOCK_STATE.get();
			GenerationPiece base = new GenerationPiece((w, p) -> w.isEmptyBlock(p.pos));
			RandomSource rand = context.random();
			CorrockTowerConfig config = context.config();
			int height = rand.nextInt(config.getMaxHeight() - config.getMinHeight() + 1) + config.getMinHeight();
			fillUp(base, corrockBlockState, pos, height);

			if (!base.canPlace(world)) return false;

			BlockPos downPos = pos.below(2);
			BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
			if (GenerationUtils.isAreaCompletelySolid(world, downPos.getX() - 1, downPos.getY(), downPos.getZ() - 1, downPos.getX() + 1, downPos.getY(), downPos.getZ() + 1)) {
				for (int x = downPos.getX() - 1; x <= downPos.getX() + 1; x++) {
					for (int y = downPos.getY(); y <= downPos.getY() + 1; y++) {
						for (int z = downPos.getZ() - 1; z <= downPos.getZ() + 1; z++) {
							mutable.set(x, y, z);
							if (world.isEmptyBlock(mutable)) {
								base.addBlockPiece(corrockBlockState, mutable.immutable());
							}
						}
					}
				}
			} else {
				return false;
			}

			int heightMinusOne = height - 1;
			for (Direction horizontal : Direction.Plane.HORIZONTAL) {
				BlockPos offset = pos.relative(horizontal);
				BlockPos doubleOffset = pos.relative(horizontal, 2);
				fillUp(base, corrockBlockState, offset, height);

				if (rand.nextBoolean()) {
					base.addBlockPiece(corrockBlockState, offset.relative(horizontal.getClockWise()));
				}

				if (rand.nextBoolean()) {
					base.addBlockPiece(corrockBlockState, offset.relative(horizontal.getCounterClockWise()));
				}

				if (rand.nextBoolean()) {
					base.addBlockPiece(corrockBlockState, offset.above(heightMinusOne).relative(horizontal.getClockWise()));
				}

				if (rand.nextBoolean()) {
					base.addBlockPiece(corrockBlockState, offset.above(heightMinusOne).relative(horizontal.getCounterClockWise()));
				}

				if (rand.nextBoolean()) {
					base.addBlockPiece(corrockBlockState, doubleOffset);
				}

				if (rand.nextBoolean()) {
					base.addBlockPiece(corrockBlockState, doubleOffset.above(heightMinusOne));
				}
			}

			int x = 0;
			int z = 0;
			if (rand.nextBoolean()) {
				x = rand.nextInt(2);
			} else {
				z = rand.nextInt(2);
			}

			Pair<GenerationPiece, List<ChorusPlantPart>> topPiece = getTop(world, pos.offset(x, height + 1, z), rand, config.getCrownChance(), config.getChorusChance());
			if (base.canPlace(world) && topPiece.getFirst().canPlace(world)) {
				base.place(world);
				topPiece.getFirst().place(world);
				topPiece.getSecond().forEach((growth) -> growth.placeGrowth(world, rand));
				BlockPos topMiddle = pos.above(height + 1);
				BlockState corrockPlantState = CORROCK_STATE.get();
				for (int i = 0; i < 16; i++) {
					if (rand.nextFloat() < 0.6F && world.isEmptyBlock(mutable.setWithOffset(topMiddle, rand.nextInt(6) - rand.nextInt(6), rand.nextInt(2) - rand.nextInt(2), rand.nextInt(6) - rand.nextInt(6)))) {
						if (world.getBlockState(mutable.below()).getBlock() == CORROCK_BLOCK_BLOCK) {
							world.setBlock(mutable, corrockPlantState, 2);
						}
					}
				}
				return true;
			}
		}
		return false;
	}

	private static void fillUp(GenerationPiece piece, BlockState state, BlockPos pos, int height) {
		for (int i = 0; i < height; i++) {
			piece.addBlockPiece(state, pos.above(i));
		}
	}

	private static Pair<GenerationPiece, List<ChorusPlantPart>> getTop(LevelAccessor world, BlockPos pos, RandomSource rand, float crownChance, float chorusChance) {
		GenerationPiece top = new GenerationPiece((w, p) -> w.isEmptyBlock(p.pos));
		List<ChorusPlantPart> growths = Lists.newArrayList();
		List<BlockPos> corners = Lists.newArrayList();
		int variant = rand.nextInt(4);
		BlockPos startNPos = pos.relative(Direction.NORTH, 4).offset(-2, 0, 0);
		BlockState corrockBlockState = CORROCK_BLOCK_STATE.get();
		for (int i = 0; i < 4; i++) {
			BlockPos placePos = startNPos.offset(i, 0, 0);
			top.addBlockPiece(corrockBlockState, placePos);
			if (rand.nextFloat() < crownChance) {
				if (rand.nextBoolean()) {
					top.addBlockPiece(getCorrockCrownWall(Direction.NORTH), placePos.relative(Direction.NORTH));
				} else {
					top.addBlockPiece(getCorrockCrownStanding(rand.nextInt(16)), placePos.above());
				}
			}
		}

		BlockPos startEPos = pos.relative(Direction.EAST, 3).offset(0, 0, 1);
		for (int i = 0; i < 4; i++) {
			BlockPos placePos = startEPos.offset(0, 0, -i);
			top.addBlockPiece(corrockBlockState, placePos);
			if (rand.nextFloat() < crownChance) {
				if (rand.nextBoolean()) {
					top.addBlockPiece(getCorrockCrownWall(Direction.EAST), placePos.relative(Direction.EAST));
				} else {
					top.addBlockPiece(getCorrockCrownStanding(rand.nextInt(16)), placePos.above());
				}
			}
		}

		BlockPos startSPos = pos.relative(Direction.SOUTH, 3).offset(1, 0, 0);
		for (int i = 0; i < 4; i++) {
			BlockPos placePos = startSPos.offset(-i, 0, 0);
			top.addBlockPiece(corrockBlockState, placePos);
			if (rand.nextFloat() < crownChance) {
				if (rand.nextBoolean()) {
					top.addBlockPiece(getCorrockCrownWall(Direction.SOUTH), placePos.relative(Direction.SOUTH));
				} else {
					top.addBlockPiece(getCorrockCrownStanding(rand.nextInt(16)), placePos.above());
				}
			}
		}

		BlockPos startWPos = pos.relative(Direction.WEST, 4).offset(0, 0, 1);
		for (int i = 0; i < 4; i++) {
			BlockPos placePos = startWPos.offset(0, 0, -i);
			top.addBlockPiece(corrockBlockState, placePos);
			if (rand.nextFloat() < crownChance) {
				if (rand.nextBoolean()) {
					top.addBlockPiece(getCorrockCrownWall(Direction.WEST), placePos.relative(Direction.WEST));
				} else {
					top.addBlockPiece(getCorrockCrownStanding(rand.nextInt(16)), placePos.above());
				}
			}
		}

		BlockPos cornerNW = pos.offset(-3, 0, -3);
		if (variant != 0) {
			corners.add(cornerNW.relative(Direction.SOUTH));
			corners.add(cornerNW.relative(Direction.EAST));
		}
		if (rand.nextFloat() < crownChance) {
			if (rand.nextBoolean()) {
				top.addBlockPiece(getCorrockCrownStanding(rand.nextInt(16)), cornerNW.above());
			} else {
				if (rand.nextFloat() < crownChance) {
					top.addBlockPiece(getCorrockCrownWall(Direction.NORTH), cornerNW.relative(Direction.NORTH));
				}

				if (rand.nextFloat() < crownChance) {
					top.addBlockPiece(getCorrockCrownWall(Direction.WEST), cornerNW.relative(Direction.WEST));
				}
			}
		}
		corners.add(cornerNW);

		BlockPos cornerNE = pos.offset(2, 0, -3);
		if (variant != 1) {
			corners.add(cornerNE.relative(Direction.SOUTH));
			corners.add(cornerNE.relative(Direction.WEST));
		}
		if (rand.nextFloat() < crownChance) {
			if (rand.nextBoolean()) {
				top.addBlockPiece(getCorrockCrownStanding(rand.nextInt(16)), cornerNE.above());
			} else {
				if (rand.nextFloat() < crownChance) {
					top.addBlockPiece(getCorrockCrownWall(Direction.NORTH), cornerNE.relative(Direction.NORTH));
				}

				if (rand.nextFloat() < crownChance) {
					top.addBlockPiece(getCorrockCrownWall(Direction.EAST), cornerNE.relative(Direction.EAST));
				}
			}
		}
		corners.add(cornerNE);

		BlockPos cornerSE = pos.offset(2, 0, 2);
		if (variant != 2) {
			corners.add(cornerSE.relative(Direction.NORTH));
			corners.add(cornerSE.relative(Direction.WEST));
		}
		if (rand.nextFloat() < crownChance) {
			if (rand.nextBoolean()) {
				top.addBlockPiece(getCorrockCrownStanding(rand.nextInt(16)), cornerSE.above());
			} else {
				if (rand.nextFloat() < crownChance) {
					top.addBlockPiece(getCorrockCrownWall(Direction.SOUTH), cornerSE.relative(Direction.SOUTH));
				}

				if (rand.nextFloat() < crownChance) {
					top.addBlockPiece(getCorrockCrownWall(Direction.EAST), cornerSE.relative(Direction.EAST));
				}
			}
		}
		corners.add(cornerSE);

		BlockPos cornerSW = pos.offset(-3, 0, 2);
		if (variant != 3) {
			corners.add(cornerSW.relative(Direction.NORTH));
			corners.add(cornerSW.relative(Direction.EAST));
		}
		if (rand.nextFloat() < crownChance) {
			if (rand.nextBoolean()) {
				top.addBlockPiece(getCorrockCrownStanding(rand.nextInt(16)), cornerSW.above());
			} else {
				if (rand.nextFloat() < crownChance) {
					top.addBlockPiece(getCorrockCrownWall(Direction.SOUTH), cornerSW.relative(Direction.SOUTH));
				}

				if (rand.nextFloat() < crownChance) {
					top.addBlockPiece(getCorrockCrownWall(Direction.WEST), cornerSW.relative(Direction.WEST));
				}
			}
		}
		corners.add(cornerSW);

		for (int x = cornerNW.getX(); x <= cornerSE.getX(); x++) {
			for (int z = cornerNW.getZ(); z <= cornerSE.getZ(); z++) {
				BlockPos placingPos = new BlockPos(x, pos.getY(), z);
				if (!corners.contains(placingPos)) {
					if (isNotCloseToAnotherGrowth(growths, placingPos.below()) && rand.nextFloat() < chorusChance && world.isEmptyBlock(placingPos) && world.isEmptyBlock(placingPos.above())) {
						growths.add(new ChorusPlantPart(placingPos.below()));
						for (Direction direction : Direction.values()) {
							if (direction != Direction.UP) {
								top.addBlockPiece(corrockBlockState, placingPos.below().relative(direction));
							}
						}
					} else {
						top.addBlockPiece(corrockBlockState, placingPos.below());
					}
				}
			}
		}

		for (BlockPos positions : corners) {
			top.addBlockPiece(corrockBlockState, positions);
		}

		return Pair.of(top, growths);
	}

}
