package com.minecraftabnormals.endergetic.common.world.features.corrock.tower;

import com.minecraftabnormals.abnormals_core.core.util.GenerationPiece;
import com.minecraftabnormals.endergetic.api.util.GenerationUtils;
import com.minecraftabnormals.endergetic.common.world.configs.CorrockTowerConfig;
import com.minecraftabnormals.endergetic.common.world.features.corrock.AbstractCorrockFeature;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class LargeCorrockTowerFeature extends AbstractCorrockFeature<CorrockTowerConfig> {

	public LargeCorrockTowerFeature(Codec<CorrockTowerConfig> configFactory) {
		super(configFactory);
	}

	@Override
	public boolean generate(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, CorrockTowerConfig config) {
		Block belowBlock = world.getBlockState(pos.down()).getBlock();
		if (world.isAirBlock(pos) && belowBlock == EEBlocks.CORROCK_END_BLOCK.get()) {
			BlockState corrockBlockState = CORROCK_BLOCK_STATE.getValue();
			List<BlockPos> corrockPositions = new ArrayList<>();
			if (tryToMakeGroundSuitable(world, corrockPositions, pos)) {
				BlockPos.Mutable mutable = new BlockPos.Mutable();
				int startX = pos.getX();
				int startY = pos.getY();
				int startZ = pos.getZ();
				for (int y = 0; y < 3; y++) {
					for (int x = 0; x < 4; x++) {
						for (int z = 0; z < 4; z++) {
							mutable.setPos(startX + x, startY + y, startZ + z);
							if (world.isAirBlock(mutable)) {
								corrockPositions.add(mutable.toImmutable());
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
				GenerationPiece corrockCrowns = tryToMakeMiddle(world, rand, corrockPositions, pos, height, crownChance);
				if (corrockCrowns != null) {
					Pair<GenerationPiece, List<ChorusPlantPart>> top = tryToMakeLargeTop(world, rand, corrockPositions, pos.up(3 + height), crownChance, config.getChorusChance());
					if (top != null) {
						corrockPositions.forEach(corrockPos -> world.setBlockState(corrockPos, corrockBlockState, 2));
						corrockCrowns.place(world);
						top.getFirst().place(world);
						top.getSecond().forEach(chorusPlantPart -> chorusPlantPart.placeGrowth(world, rand));
						return true;
					}
				}
			}
			return false;
		}
		return false;
	}

	private static boolean tryToMakeGroundSuitable(ISeedReader world, List<BlockPos> positions, BlockPos origin) {
		BlockPos down = origin.down(2);
		int startX = down.getX();
		int startY = down.getY();
		int startZ = down.getZ();
		boolean isSolidBelow = GenerationUtils.isAreaCompletelySolid(world, startX - 2, startY, startZ - 2, startX + 2, startY, startZ + 2);
		if (isSolidBelow) {
			BlockPos.Mutable mutable = new BlockPos.Mutable();
			for (int y = 0; y < 2; y++) {
				for (int x = 0; x < 4; x++) {
					for (int z = 0; z < 4; z++) {
						mutable.setPos(startX + x, startY + y, startZ + z);
						BlockPos immutable = mutable.toImmutable();
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

	private static void tryToMakeSide(ISeedReader world, Random rand, List<BlockPos> positions, BlockPos origin, Direction facing) {
		List<BlockPos> toAdd = new ArrayList<>();
		if (world.isAirBlock(origin)) {
			tryToMakeSidePillar(world, rand, 0, rand.nextInt(2) + 2, origin, toAdd, facing);
		}
		positions.addAll(toAdd);
	}

	private static void tryToMakeSidePillar(ISeedReader world, Random rand, int index, int height, BlockPos origin, List<BlockPos> positions, Direction facing) {
		BlockPos.Mutable mutable = origin.toMutable();
		int startX = origin.getX();
		int startY = origin.getY();
		int startZ = origin.getZ();
		for (int y = 0; y < height; y++) {
			mutable.setPos(startX, startY + y, startZ);
			if (world.isAirBlock(mutable)) {
				positions.add(mutable.toImmutable());
			} else {
				positions.clear();
				return;
			}
			if (!tryToBuildPillarDownwards(world, positions, origin)) {
				positions.clear();
				return;
			}
			if (index < 1) {
				BlockPos offset = origin.offset(rand.nextBoolean() ? facing.rotateY() : facing.rotateYCCW());
				if (world.isAirBlock(offset)) {
					//If to the right or left of the pillar there is space, attempt to create another pillar
					tryToMakeSidePillar(world, rand, index + 1, height - 1, offset, positions, facing);
				}
			}
		}
	}

	private static boolean tryToBuildPillarDownwards(ISeedReader world, List<BlockPos> positions, BlockPos origin) {
		boolean foundGround = false;
		BlockPos.Mutable mutable = origin.toMutable();
		for (int y = 0; y < 4; y++) {
			mutable.move(0, -1, 0);
			if (!world.getBlockState(mutable).getMaterial().isReplaceable()) {
				foundGround = true;
			} else {
				positions.add(mutable.toImmutable());
			}
		}
		return foundGround;
	}

	@Nullable
	private static GenerationPiece tryToMakeMiddle(ISeedReader world, Random rand, List<BlockPos> positions, BlockPos origin, int height, float crownChance) {
		int startX = origin.getX() + 1;
		int startY = origin.getY() + 3;
		int startZ = origin.getZ() + 1;
		BlockPos.Mutable innerMutable = origin.toMutable();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < 2; x++) {
				for (int z = 0; z < 2; z++) {
					innerMutable.setPos(startX + x, startY + y, startZ + z);
					if (world.isAirBlock(innerMutable)) {
						positions.add(innerMutable.toImmutable());
					} else {
						return null;
					}
				}
			}
		}
		//Round trip around square to generate side pillars with corrock crowns and 0-2 height upward corners
		GenerationPiece corrockCrowns = new GenerationPiece((iWorld, blockPart) -> true);
		BlockPos.Mutable roundTripMutable = origin.toMutable();
		Direction currentDirection = Direction.NORTH;
		crownChance /= height;
		for (int i = 0; i < 12; i++) {
			if (i % 3 != 0) {
				roundTripMutable.move(currentDirection);
				Direction ccwDirection = currentDirection.rotateYCCW();
				BlockState crownState = getCorrockCrownWall(ccwDirection);
				for (int y = 0; y < height; y++) {
					roundTripMutable.setY(startY + y);
					if (world.isAirBlock(roundTripMutable)) {
						positions.add(roundTripMutable.toImmutable());
						if (rand.nextFloat() < crownChance) {
							BlockPos crownOffset = roundTripMutable.offset(ccwDirection);
							if (world.isAirBlock(crownOffset)) {
								corrockCrowns.addBlockPiece(crownState, crownOffset);
							}
						}
					} else {
						return null;
					}
				}
			} else {
				if (i != 0) {
					roundTripMutable.move(currentDirection);
				}
				currentDirection = currentDirection.rotateY();
				if (rand.nextFloat() < 0.8F) {
					int cornerTop = startY + height;
					roundTripMutable.setY(cornerTop - 1);
					if (world.isAirBlock(roundTripMutable)) {
						positions.add(roundTripMutable.toImmutable());
						if (height > 2 && rand.nextFloat() < 0.25F) {
							roundTripMutable.setY(cornerTop - 2);
							if (world.isAirBlock(roundTripMutable)) {
								positions.add(roundTripMutable.toImmutable());
							}
						}
					}
				}
			}
		}
		return corrockCrowns;
	}

	/**
	 * Returns a pair of corrock crowns and chorus plant parts, or null if there wasn't space for the top.
	 */
	@Nullable
	private static Pair<GenerationPiece, List<ChorusPlantPart>> tryToMakeLargeTop(ISeedReader world, Random rand, List<BlockPos> positions, BlockPos origin, float crownChance, float chorusChance) {
		int startX = origin.getX();
		int startY = origin.getY();
		int startZ = origin.getZ();
		if (tryToFillWithCorrockBlock(world, startX, startY, startZ, startX + 3, startY, startZ + 3, positions)) {
			if (tryToFillWithCorrockBlock(world, startX + 1, startY, startZ - 1, startX + 2, startY, startZ - 1, positions) && tryToFillWithCorrockBlock(world, startX - 1, startY, startZ + 1, startX - 1, startY, startZ + 2, positions) && tryToFillWithCorrockBlock(world, startX + 4, startY, startZ + 1, startX + 4, startY, startZ + 2, positions) && tryToFillWithCorrockBlock(world, startX + 1, startY, startZ + 4, startX + 2, startY, startZ + 4, positions)) {
				startY++;
				if (tryToFillWithCorrockBlock(world, startX - 1, startY, startZ, startX + 4, startY, startZ + 3, positions) && tryToFillWithCorrockBlock(world, startX, startY, startZ - 1, startX + 3, startY, startZ - 1, positions) && tryToFillWithCorrockBlock(world, startX, startY, startZ + 4, startX + 3, startY, startZ + 4, positions)) {
					BlockPos.Mutable mutable = new BlockPos.Mutable();
					if (tryToPlaceCorrockBlock(world, mutable.setPos(startX + rand.nextInt(2) + 1, startY, startZ - 2), positions) && tryToPlaceCorrockBlock(world, mutable.setPos(startX - 2, startY, startZ + rand.nextInt(2) + 1), positions) && tryToPlaceCorrockBlock(world, mutable.setPos(startX + rand.nextInt(2) + 1, startY, startZ + 5), positions) && tryToPlaceCorrockBlock(world, mutable.setPos(startX + 5, startY, startZ + rand.nextInt(2) + 1), positions)) {
						startY++;
						GenerationPiece crowns = new GenerationPiece((iWorld, blockPart) -> true);
						List<BlockPos> corners = new ArrayList<>();
						if (tryToPlaceCorrockBlockWithCrown(world, rand, mutable.setPos(startX - 1, startY, startZ - 1), positions, rand.nextBoolean() ? Direction.WEST : Direction.NORTH, crowns, corners, crownChance) && tryToPlaceCorrockBlockWithCrown(world, rand, mutable.setPos(startX + 4, startY, startZ - 1), positions, rand.nextBoolean() ? Direction.EAST : Direction.NORTH, crowns, corners, crownChance) && tryToPlaceCorrockBlockWithCrown(world, rand, mutable.setPos(startX + 4, startY, startZ + 4), positions, rand.nextBoolean() ? Direction.EAST : Direction.SOUTH, crowns, corners, crownChance) && tryToPlaceCorrockBlockWithCrown(world, rand, mutable.setPos(startX - 1, startY, startZ + 4), positions, rand.nextBoolean() ? Direction.WEST : Direction.SOUTH, crowns, corners, crownChance)) {
							if (tryToPlaceCrownedCorrockSquare(world, rand, startY, startX, startZ - 2, startX + 3, startZ - 2, positions, Direction.NORTH, crowns, crownChance) && tryToPlaceCrownedCorrockSquare(world, rand, startY, startX - 2, startZ, startX - 2, startZ + 3, positions, Direction.WEST, crowns, crownChance) && tryToPlaceCrownedCorrockSquare(world, rand, startY, startX + 5, startZ, startX + 5, startZ + 3, positions, Direction.EAST, crowns, crownChance) && tryToPlaceCrownedCorrockSquare(world, rand, startY, startX, startZ + 5, startX + 3, startZ + 5, positions, Direction.SOUTH, crowns, crownChance)) {
								if (rand.nextBoolean()) {
									if (tryToPlaceCorrockBlock(world, mutable.setPos(startX - 1, startY, startZ), positions)) {
										corners.add(mutable.toImmutable());
									}
									if (tryToPlaceCorrockBlock(world, mutable.setPos(startX, startY, startZ - 1), positions)) {
										corners.add(mutable.toImmutable());
									}
								}

								if (rand.nextBoolean()) {
									if (tryToPlaceCorrockBlock(world, mutable.setPos(startX + 4, startY, startZ), positions)) {
										corners.add(mutable.toImmutable());
									}
									if (tryToPlaceCorrockBlock(world, mutable.setPos(startX + 3, startY, startZ - 1), positions)) {
										corners.add(mutable.toImmutable());
									}
								}

								if (rand.nextBoolean()) {
									if (tryToPlaceCorrockBlock(world, mutable.setPos(startX + 4, startY, startZ + 3), positions)) {
										corners.add(mutable.toImmutable());
									}
									if (tryToPlaceCorrockBlock(world, mutable.setPos(startX + 3, startY, startZ + 4), positions)) {
										corners.add(mutable.toImmutable());
									}
								}

								if (rand.nextBoolean()) {
									if (tryToPlaceCorrockBlock(world, mutable.setPos(startX, startY, startZ + 4), positions)) {
										corners.add(mutable.toImmutable());
									}
									if (tryToPlaceCorrockBlock(world, mutable.setPos(startX - 1, startY, startZ + 3), positions)) {
										corners.add(mutable.toImmutable());
									}
								}

								List<ChorusPlantPart> chorusPlantParts = new ArrayList<>();
								int y = startY - 1;
								int cornerX = startX - 1;
								int cornerZ = startZ - 1;
								for (int x = 0; x < 5; x++) {
									for (int z = 0; z < 5; z++) {
										mutable.setPos(cornerX + x, y, cornerZ + z);
										if (rand.nextFloat() < chorusChance) {
											BlockPos up = mutable.up();
											if (world.isAirBlock(up) && !corners.contains(up) && isNotCloseToAnotherGrowth(chorusPlantParts, mutable)) {
												chorusPlantParts.add(new ChorusPlantPart(mutable.toImmutable()));
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
