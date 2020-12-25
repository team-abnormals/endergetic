package com.minecraftabnormals.endergetic.common.world.features.corrock.tower;

import com.google.common.collect.Lists;
import com.minecraftabnormals.abnormals_core.core.util.GenerationPiece;
import com.minecraftabnormals.endergetic.api.util.GenerationUtils;
import com.minecraftabnormals.endergetic.common.world.features.corrock.AbstractCorrockFeature;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.ProbabilityConfig;

import java.util.List;
import java.util.Random;

public final class MediumCorrockTowerFeature extends AbstractCorrockFeature<ProbabilityConfig> {

	public MediumCorrockTowerFeature(Codec<ProbabilityConfig> configFactory) {
		super(configFactory);
	}

	//TODO: Move to using a custom config
	@Override
	public boolean generate(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, ProbabilityConfig config) {
		Block belowBlock = world.getBlockState(pos.down()).getBlock();
		if (world.isAirBlock(pos) && belowBlock == EEBlocks.CORROCK_END_BLOCK.get()) {
			BlockState corrockBlockState = CORROCK_BLOCK_STATE.getValue();
			GenerationPiece base = new GenerationPiece((w, p) -> w.isAirBlock(p.pos));
			fillUpThree(base, corrockBlockState, pos);

			if (!base.canPlace(world)) return false;

			BlockPos downPos = pos.down(2);
			if (GenerationUtils.isAreaCompletelySolid(world, downPos.getX() - 1, downPos.getY(), downPos.getZ() - 1, downPos.getX() + 1, downPos.getY(), downPos.getZ() + 1)) {
				for (int x = downPos.getX() - 1; x <= downPos.getX() + 1; x++) {
					for (int y = downPos.getY(); y <= downPos.getY() + 1; y++) {
						for (int z = downPos.getZ() - 1; z <= downPos.getZ() + 1; z++) {
							BlockPos currentPos = new BlockPos(x, y, z);
							if (world.isAirBlock(currentPos)) {
								base.addBlockPiece(corrockBlockState, currentPos);
							}
						}
					}
				}
			} else {
				return false;
			}

			for (Direction horizontal : Direction.Plane.HORIZONTAL) {
				BlockPos offset = pos.offset(horizontal);
				BlockPos doubleOffset = pos.offset(horizontal, 2);
				fillUpThree(base, corrockBlockState, offset);

				if (rand.nextFloat() < 0.5F) {
					base.addBlockPiece(corrockBlockState, offset.offset(horizontal.rotateY()));
				}

				if (rand.nextFloat() < 0.5F) {
					base.addBlockPiece(corrockBlockState, offset.offset(horizontal.rotateYCCW()));
				}

				if (rand.nextFloat() < 0.5F) {
					base.addBlockPiece(corrockBlockState, offset.up(2).offset(horizontal.rotateY()));
				}

				if (rand.nextFloat() < 0.5F) {
					base.addBlockPiece(corrockBlockState, offset.up(2).offset(horizontal.rotateYCCW()));
				}

				if (rand.nextFloat() < 0.5F) {
					base.addBlockPiece(corrockBlockState, doubleOffset);
				}

				if (rand.nextFloat() < 0.5F) {
					base.addBlockPiece(corrockBlockState, doubleOffset.up(2));
				}
			}

			boolean xOrz = rand.nextBoolean();
			int x = xOrz ? rand.nextInt(2) : 0;
			int z = !xOrz ? rand.nextInt(2) : 0;
			Pair<GenerationPiece, List<ChorusPlantPart>> topPiece = getTop(world, pos.add(x, 0, z).up(4), rand);

			if (base.canPlace(world) && topPiece.getFirst().canPlace(world)) {
				base.place(world);
				topPiece.getFirst().place(world);
				topPiece.getSecond().forEach((growth) -> growth.placeGrowth(world, rand));
				return true;
			}
		}
		return false;
	}

	private static void fillUpThree(GenerationPiece piece, BlockState state, BlockPos pos) {
		for (int i = 0; i < 3; i++) {
			piece.addBlockPiece(state, pos.up(i));
		}
	}

	private static Pair<GenerationPiece, List<ChorusPlantPart>> getTop(IWorld world, BlockPos pos, Random rand) {
		GenerationPiece top = new GenerationPiece((w, p) -> w.isAirBlock(p.pos));
		List<ChorusPlantPart> growths = Lists.newArrayList();
		List<BlockPos> corners = Lists.newArrayList();
		int variant = rand.nextInt(4);
		BlockPos startNPos = pos.offset(Direction.NORTH, 4).add(-2, 0, 0);
		BlockState corrockBlockState = CORROCK_BLOCK_STATE.getValue();
		for (int i = 0; i < 4; i++) {
			BlockPos placePos = startNPos.add(i, 0, 0);
			top.addBlockPiece(corrockBlockState, placePos);
			if (rand.nextFloat() < 0.5F) {
				if (rand.nextBoolean()) {
					top.addBlockPiece(getCorrockCrownWall(Direction.NORTH), placePos.offset(Direction.NORTH));
				} else {
					top.addBlockPiece(getCorrockCrownStanding(rand.nextInt(16)), placePos.up());
				}
			}
		}

		BlockPos startEPos = pos.offset(Direction.EAST, 3).add(0, 0, 1);
		for (int i = 0; i < 4; i++) {
			BlockPos placePos = startEPos.add(0, 0, -i);
			top.addBlockPiece(corrockBlockState, placePos);
			if (rand.nextFloat() < 0.5F) {
				if (rand.nextBoolean()) {
					top.addBlockPiece(getCorrockCrownWall(Direction.EAST), placePos.offset(Direction.EAST));
				} else {
					top.addBlockPiece(getCorrockCrownStanding(rand.nextInt(16)), placePos.up());
				}
			}
		}

		BlockPos startSPos = pos.offset(Direction.SOUTH, 3).add(1, 0, 0);
		for (int i = 0; i < 4; i++) {
			BlockPos placePos = startSPos.add(-i, 0, 0);
			top.addBlockPiece(corrockBlockState, placePos);
			if (rand.nextFloat() < 0.5F) {
				if (rand.nextBoolean()) {
					top.addBlockPiece(getCorrockCrownWall(Direction.SOUTH), placePos.offset(Direction.SOUTH));
				} else {
					top.addBlockPiece(getCorrockCrownStanding(rand.nextInt(16)), placePos.up());
				}
			}
		}

		BlockPos startWPos = pos.offset(Direction.WEST, 4).add(0, 0, 1);
		for (int i = 0; i < 4; i++) {
			BlockPos placePos = startWPos.add(0, 0, -i);
			top.addBlockPiece(corrockBlockState, placePos);
			if (rand.nextFloat() < 0.5F) {
				if (rand.nextBoolean()) {
					top.addBlockPiece(getCorrockCrownWall(Direction.WEST), placePos.offset(Direction.WEST));
				} else {
					top.addBlockPiece(getCorrockCrownStanding(rand.nextInt(16)), placePos.up());
				}
			}
		}

		BlockPos cornerNW = pos.add(-3, 0, -3);
		if (variant != 0) {
			corners.add(cornerNW.offset(Direction.SOUTH));
			corners.add(cornerNW.offset(Direction.EAST));
		}
		if (rand.nextFloat() < 0.45F) {
			if (rand.nextBoolean()) {
				top.addBlockPiece(getCorrockCrownStanding(rand.nextInt(16)), cornerNW.up());
			} else {
				if (rand.nextFloat() < 0.5F) {
					top.addBlockPiece(getCorrockCrownWall(Direction.NORTH), cornerNW.offset(Direction.NORTH));
				}

				if (rand.nextFloat() < 0.5F) {
					top.addBlockPiece(getCorrockCrownWall(Direction.WEST), cornerNW.offset(Direction.WEST));
				}
			}
		}
		corners.add(cornerNW);

		BlockPos cornerNE = pos.add(2, 0, -3);
		if (variant != 1) {
			corners.add(cornerNE.offset(Direction.SOUTH));
			corners.add(cornerNE.offset(Direction.WEST));
		}
		if (rand.nextFloat() < 0.45F) {
			if (rand.nextBoolean()) {
				top.addBlockPiece(getCorrockCrownStanding(rand.nextInt(16)), cornerNE.up());
			} else {
				if (rand.nextFloat() < 0.5F) {
					top.addBlockPiece(getCorrockCrownWall(Direction.NORTH), cornerNE.offset(Direction.NORTH));
				}

				if (rand.nextFloat() < 0.5F) {
					top.addBlockPiece(getCorrockCrownWall(Direction.EAST), cornerNE.offset(Direction.EAST));
				}
			}
		}
		corners.add(cornerNE);

		BlockPos cornerSE = pos.add(2, 0, 2);
		if (variant != 2) {
			corners.add(cornerSE.offset(Direction.NORTH));
			corners.add(cornerSE.offset(Direction.WEST));
		}
		if (rand.nextFloat() < 0.45F) {
			if (rand.nextBoolean()) {
				top.addBlockPiece(getCorrockCrownStanding(rand.nextInt(16)), cornerSE.up());
			} else {
				if (rand.nextFloat() < 0.5F) {
					top.addBlockPiece(getCorrockCrownWall(Direction.SOUTH), cornerSE.offset(Direction.SOUTH));
				}

				if (rand.nextFloat() < 0.5F) {
					top.addBlockPiece(getCorrockCrownWall(Direction.EAST), cornerSE.offset(Direction.EAST));
				}
			}
		}
		corners.add(cornerSE);

		BlockPos cornerSW = pos.add(-3, 0, 2);
		if (variant != 3) {
			corners.add(cornerSW.offset(Direction.NORTH));
			corners.add(cornerSW.offset(Direction.EAST));
		}
		if (rand.nextFloat() < 0.45F) {
			if (rand.nextBoolean()) {
				top.addBlockPiece(getCorrockCrownStanding(rand.nextInt(16)), cornerSW.up());
			} else {
				if (rand.nextFloat() < 0.5F) {
					top.addBlockPiece(getCorrockCrownWall(Direction.SOUTH), cornerSW.offset(Direction.SOUTH));
				}

				if (rand.nextFloat() < 0.5F) {
					top.addBlockPiece(getCorrockCrownWall(Direction.WEST), cornerSW.offset(Direction.WEST));
				}
			}
		}
		corners.add(cornerSW);

		for (int x = cornerNW.getX(); x <= cornerSE.getX(); x++) {
			for (int z = cornerNW.getZ(); z <= cornerSE.getZ(); z++) {
				BlockPos placingPos = new BlockPos(x, pos.getY(), z);
				if (!corners.contains(placingPos)) {
					if (isNotCloseToAnotherGrowth(growths, placingPos.down()) && rand.nextFloat() < 0.1F && world.isAirBlock(placingPos) && world.isAirBlock(placingPos.up())) {
						growths.add(new ChorusPlantPart(placingPos.down()));
						for (Direction direction : Direction.values()) {
							if (direction != Direction.UP) {
								top.addBlockPiece(corrockBlockState, placingPos.down().offset(direction));
							}
						}
					} else {
						top.addBlockPiece(corrockBlockState, placingPos.down());
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
