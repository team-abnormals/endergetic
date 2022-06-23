package com.minecraftabnormals.endergetic.common.world.features.corrock.tower;

import com.minecraftabnormals.abnormals_core.core.util.GenerationPiece;
import com.minecraftabnormals.endergetic.api.util.GenerationUtils;
import com.minecraftabnormals.endergetic.common.world.configs.CorrockTowerConfig;
import com.minecraftabnormals.endergetic.common.world.features.corrock.AbstractCorrockFeature;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.minecraftabnormals.endergetic.common.world.features.corrock.AbstractCorrockFeature.ChorusPlantPart;

public final class LargeCorrockTowerFeature extends AbstractCorrockFeature<CorrockTowerConfig> {

	public LargeCorrockTowerFeature(Codec<CorrockTowerConfig> configFactory) {
		super(configFactory);
	}

	@Override
	public boolean place(WorldGenLevel world, ChunkGenerator generator, Random rand, BlockPos pos, CorrockTowerConfig config) {
		if (world.isEmptyBlock(pos) && world.getBlockState(pos.below()).getBlock() == EEBlocks.CORROCK_END_BLOCK.get() && world.getBlockState(pos.below(2)).canOcclude()) {
			BlockState corrockBlockState = CORROCK_BLOCK_STATE.get();
			List<BlockPos> corrockPositions = new ArrayList<>();
			if (tryToMakeGroundSuitable(world, corrockPositions, pos)) {
				BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
				int startX = pos.getX();
				int startY = pos.getY();
				int startZ = pos.getZ();
				for (int y = 0; y < 3; y++) {
					for (int x = 0; x < 4; x++) {
						for (int z = 0; z < 4; z++) {
							mutable.set(startX + x, startY + y, startZ + z);
							if (world.isEmptyBlock(mutable)) {
								corrockPositions.add(mutable.immutable());
							} else {
								return false;
							}
						}
					}
				}

				tryToMakeSide(world, rand, corrockPositions, pos.north().east(rand.nextInt(2) + 1), Direction.NORTH);
				tryToMakeSide(world, rand, corrockPositions, pos.west().south(rand.nextInt(2) + 1), Direction.WEST);
				tryToMakeSide(world, rand, corrockPositions, pos.south(4).east(rand.nextInt(2) + 1), Direction.SOUTH);
				tryToMakeSide(world, rand, corrockPositions, pos.east(4).south(rand.nextInt(2) + 1), Direction.EAST);

				int height = rand.nextInt(config.getMaxHeight() - config.getMinHeight() + 1) + config.getMinHeight();
				float crownChance = config.getCrownChance();
				if (tryToMakeMiddle(world, rand, corrockPositions, pos, height)) {
					Pair<GenerationPiece, List<ChorusPlantPart>> top = tryToMakeLargeTop(world, rand, corrockPositions, pos.above(3 + height), crownChance, config.getChorusChance());
					if (top != null) {
						corrockPositions.forEach(corrockPos -> world.setBlock(corrockPos, corrockBlockState, 2));
						top.getFirst().place(world);
						top.getSecond().forEach(chorusPlantPart -> chorusPlantPart.placeGrowth(world, rand));
						BlockPos topMiddle = pos.above(4 + height);
						BlockState corrockPlantState = CORROCK_STATE.get();
						for (int i = 0; i < 16; i++) {
							if (rand.nextFloat() < 0.6F && world.isEmptyBlock(mutable.setWithOffset(topMiddle, rand.nextInt(7) - rand.nextInt(7), rand.nextInt(2) - rand.nextInt(2), rand.nextInt(7) - rand.nextInt(7)))) {
								if (world.getBlockState(mutable.below()).getBlock() == CORROCK_BLOCK_BLOCK) {
									world.setBlock(mutable, corrockPlantState, 2);
								}
							}
						}
						return true;
					}
				}
			}
			return false;
		}
		return false;
	}

	private static boolean tryToMakeGroundSuitable(WorldGenLevel world, List<BlockPos> positions, BlockPos origin) {
		BlockPos down = origin.below(2);
		int startX = down.getX();
		int startY = down.getY();
		int startZ = down.getZ();
		boolean isSolidBelow = GenerationUtils.isAreaCompletelySolid(world, startX - 2, startY, startZ - 2, startX + 2, startY, startZ + 2);
		if (isSolidBelow) {
			BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
			for (int y = 0; y < 2; y++) {
				for (int x = 0; x < 4; x++) {
					for (int z = 0; z < 4; z++) {
						mutable.set(startX + x, startY + y, startZ + z);
						BlockPos immutable = mutable.immutable();
						if (world.getBlockState(immutable).getMaterial().isReplaceable()) {
							positions.add(immutable);
						}
					}
				}
			}
			return true;
		}
		return false;
	}

	private static void tryToMakeSide(WorldGenLevel world, Random rand, List<BlockPos> positions, BlockPos origin, Direction facing) {
		List<BlockPos> toAdd = new ArrayList<>();
		if (world.isEmptyBlock(origin)) {
			tryToMakeSidePillar(world, rand, 0, rand.nextInt(2) + 2, origin, toAdd, facing);
		}
		positions.addAll(toAdd);
	}

	private static void tryToMakeSidePillar(WorldGenLevel world, Random rand, int index, int height, BlockPos origin, List<BlockPos> positions, Direction facing) {
		BlockPos.MutableBlockPos mutable = origin.mutable();
		int startX = origin.getX();
		int startY = origin.getY();
		int startZ = origin.getZ();
		for (int y = 0; y < height; y++) {
			mutable.set(startX, startY + y, startZ);
			if (world.isEmptyBlock(mutable)) {
				positions.add(mutable.immutable());
			} else {
				positions.clear();
				return;
			}
			if (!tryToBuildPillarDownwards(world, positions, origin)) {
				positions.clear();
				return;
			}
			if (index < 1) {
				BlockPos offset = origin.relative(rand.nextBoolean() ? facing.getClockWise() : facing.getCounterClockWise());
				if (world.isEmptyBlock(offset)) {
					//If to the right or left of the pillar there is space, attempt to create another pillar
					tryToMakeSidePillar(world, rand, index + 1, height - 1, offset, positions, facing);
				}
			}
		}
	}

	private static boolean tryToBuildPillarDownwards(WorldGenLevel world, List<BlockPos> positions, BlockPos origin) {
		boolean foundGround = false;
		BlockPos.MutableBlockPos mutable = origin.mutable();
		for (int y = 0; y < 4; y++) {
			mutable.move(0, -1, 0);
			if (!world.getBlockState(mutable).getMaterial().isReplaceable()) {
				foundGround = true;
			} else {
				positions.add(mutable.immutable());
			}
		}
		return foundGround;
	}

	private static boolean tryToMakeMiddle(WorldGenLevel world, Random rand, List<BlockPos> positions, BlockPos origin, int height) {
		int startX = origin.getX() + 1;
		int startY = origin.getY() + 3;
		int startZ = origin.getZ() + 1;
		BlockPos.MutableBlockPos innerMutable = origin.mutable();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < 2; x++) {
				for (int z = 0; z < 2; z++) {
					innerMutable.set(startX + x, startY + y, startZ + z);
					if (world.isEmptyBlock(innerMutable)) {
						positions.add(innerMutable.immutable());
					} else {
						return false;
					}
				}
			}
		}
		//Round trip around square to generate side pillars and 0-2 height upward corners
		BlockPos.MutableBlockPos roundTripMutable = origin.mutable();
		Direction currentDirection = Direction.NORTH;
		for (int i = 0; i < 12; i++) {
			if (i % 3 != 0) {
				roundTripMutable.move(currentDirection);
				for (int y = 0; y < height; y++) {
					roundTripMutable.setY(startY + y);
					if (world.isEmptyBlock(roundTripMutable)) {
						positions.add(roundTripMutable.immutable());
					} else {
						return false;
					}
				}
			} else {
				if (i != 0) {
					roundTripMutable.move(currentDirection);
				}
				currentDirection = currentDirection.getClockWise();
				if (rand.nextFloat() < 0.8F) {
					int cornerTop = startY + height;
					roundTripMutable.setY(cornerTop - 1);
					if (world.isEmptyBlock(roundTripMutable)) {
						positions.add(roundTripMutable.immutable());
						if (height > 2 && rand.nextFloat() < 0.25F) {
							roundTripMutable.setY(cornerTop - 2);
							if (world.isEmptyBlock(roundTripMutable)) {
								positions.add(roundTripMutable.immutable());
							}
						}
					}
				}
			}
		}
		return true;
	}

	/**
	 * Returns a pair of corrock crowns and chorus plant parts, or null if there wasn't space for the top.
	 */
	@Nullable
	private static Pair<GenerationPiece, List<ChorusPlantPart>> tryToMakeLargeTop(WorldGenLevel world, Random rand, List<BlockPos> positions, BlockPos origin, float crownChance, float chorusChance) {
		int startX = origin.getX();
		int startY = origin.getY();
		int startZ = origin.getZ();
		if (tryToFillWithCorrockBlock(world, startX, startY, startZ, startX + 3, startY, startZ + 3, positions)) {
			if (tryToFillWithCorrockBlock(world, startX + 1, startY, startZ - 1, startX + 2, startY, startZ - 1, positions) && tryToFillWithCorrockBlock(world, startX - 1, startY, startZ + 1, startX - 1, startY, startZ + 2, positions) && tryToFillWithCorrockBlock(world, startX + 4, startY, startZ + 1, startX + 4, startY, startZ + 2, positions) && tryToFillWithCorrockBlock(world, startX + 1, startY, startZ + 4, startX + 2, startY, startZ + 4, positions)) {
				startY++;
				if (tryToFillWithCorrockBlock(world, startX - 1, startY, startZ, startX + 4, startY, startZ + 3, positions) && tryToFillWithCorrockBlock(world, startX, startY, startZ - 1, startX + 3, startY, startZ - 1, positions) && tryToFillWithCorrockBlock(world, startX, startY, startZ + 4, startX + 3, startY, startZ + 4, positions)) {
					BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
					if (tryToPlaceCorrockBlock(world, mutable.set(startX + rand.nextInt(2) + 1, startY, startZ - 2), positions) && tryToPlaceCorrockBlock(world, mutable.set(startX - 2, startY, startZ + rand.nextInt(2) + 1), positions) && tryToPlaceCorrockBlock(world, mutable.set(startX + rand.nextInt(2) + 1, startY, startZ + 5), positions) && tryToPlaceCorrockBlock(world, mutable.set(startX + 5, startY, startZ + rand.nextInt(2) + 1), positions)) {
						startY++;
						GenerationPiece crowns = new GenerationPiece((iWorld, blockPart) -> true);
						List<BlockPos> corners = new ArrayList<>();
						if (tryToPlaceCorrockBlockWithCrown(world, rand, mutable.set(startX - 1, startY, startZ - 1), positions, rand.nextBoolean() ? Direction.WEST : Direction.NORTH, crowns, corners, crownChance) && tryToPlaceCorrockBlockWithCrown(world, rand, mutable.set(startX + 4, startY, startZ - 1), positions, rand.nextBoolean() ? Direction.EAST : Direction.NORTH, crowns, corners, crownChance) && tryToPlaceCorrockBlockWithCrown(world, rand, mutable.set(startX + 4, startY, startZ + 4), positions, rand.nextBoolean() ? Direction.EAST : Direction.SOUTH, crowns, corners, crownChance) && tryToPlaceCorrockBlockWithCrown(world, rand, mutable.set(startX - 1, startY, startZ + 4), positions, rand.nextBoolean() ? Direction.WEST : Direction.SOUTH, crowns, corners, crownChance)) {
							if (tryToPlaceCrownedCorrockSquare(world, rand, startY, startX, startZ - 2, startX + 3, startZ - 2, positions, Direction.NORTH, crowns, crownChance) && tryToPlaceCrownedCorrockSquare(world, rand, startY, startX - 2, startZ, startX - 2, startZ + 3, positions, Direction.WEST, crowns, crownChance) && tryToPlaceCrownedCorrockSquare(world, rand, startY, startX + 5, startZ, startX + 5, startZ + 3, positions, Direction.EAST, crowns, crownChance) && tryToPlaceCrownedCorrockSquare(world, rand, startY, startX, startZ + 5, startX + 3, startZ + 5, positions, Direction.SOUTH, crowns, crownChance)) {
								if (rand.nextBoolean()) {
									if (tryToPlaceCorrockBlock(world, mutable.set(startX - 1, startY, startZ), positions)) {
										corners.add(mutable.immutable());
									}
									if (tryToPlaceCorrockBlock(world, mutable.set(startX, startY, startZ - 1), positions)) {
										corners.add(mutable.immutable());
									}
								}

								if (rand.nextBoolean()) {
									if (tryToPlaceCorrockBlock(world, mutable.set(startX + 4, startY, startZ), positions)) {
										corners.add(mutable.immutable());
									}
									if (tryToPlaceCorrockBlock(world, mutable.set(startX + 3, startY, startZ - 1), positions)) {
										corners.add(mutable.immutable());
									}
								}

								if (rand.nextBoolean()) {
									if (tryToPlaceCorrockBlock(world, mutable.set(startX + 4, startY, startZ + 3), positions)) {
										corners.add(mutable.immutable());
									}
									if (tryToPlaceCorrockBlock(world, mutable.set(startX + 3, startY, startZ + 4), positions)) {
										corners.add(mutable.immutable());
									}
								}

								if (rand.nextBoolean()) {
									if (tryToPlaceCorrockBlock(world, mutable.set(startX, startY, startZ + 4), positions)) {
										corners.add(mutable.immutable());
									}
									if (tryToPlaceCorrockBlock(world, mutable.set(startX - 1, startY, startZ + 3), positions)) {
										corners.add(mutable.immutable());
									}
								}

								List<ChorusPlantPart> chorusPlantParts = new ArrayList<>();
								int y = startY - 1;
								int cornerX = startX - 1;
								int cornerZ = startZ - 1;
								for (int x = 0; x < 5; x++) {
									for (int z = 0; z < 5; z++) {
										mutable.set(cornerX + x, y, cornerZ + z);
										if (rand.nextFloat() < chorusChance) {
											BlockPos up = mutable.above();
											if (world.isEmptyBlock(up) && !corners.contains(up) && isNotCloseToAnotherGrowth(chorusPlantParts, mutable)) {
												chorusPlantParts.add(new ChorusPlantPart(mutable.immutable()));
											}
										}
									}
								}
								return Pair.of(crowns, chorusPlantParts);
							}
						}
					}
				}
			}
		}
		return null;
	}

}
