package com.minecraftabnormals.endergetic.common.world.features.corrock;

import com.minecraftabnormals.endergetic.common.blocks.CorrockCrownBlock;
import com.minecraftabnormals.endergetic.common.blocks.CorrockPlantBlock;
import com.minecraftabnormals.endergetic.common.world.structures.pieces.EetleNestPieces;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CorrockShelfFeature extends AbstractCorrockFeature<ProbabilityFeatureConfiguration> {
	private static final Direction[] DIRECTIONS = getDirections(false);
	private static final Direction[] DIRECTIONS_REVERSED = getDirections(true);
	private static final BlockState SPECKLED_CORROCK = EEBlocks.SPECKLED_END_CORROCK.get().defaultBlockState();

	public CorrockShelfFeature(Codec<ProbabilityFeatureConfiguration> configFactory) {
		super(configFactory);
	}

	@Override
	public boolean place(WorldGenLevel world, ChunkGenerator generator, Random rand, BlockPos pos, ProbabilityFeatureConfiguration config) {
		//Dirty trick to fix Shelfs attaching to not yet generated chunks in Eetle Nests
		if (EetleNestPieces.isNotInsideGeneratingBounds(pos) && world.isEmptyBlock(pos) && world.getBlockState(pos.above()).getBlock() != CORROCK_BLOCK_BLOCK && isTouchingWall(world, pos)) {
			int size = rand.nextBoolean() ? 3 : 4;
			generateShelf(world, rand, pos.getX(), pos.getY(), pos.getZ(), size, 10, rand.nextInt(2) + 2, rand.nextInt(2) + 2, config.probability);
			BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
			BlockState corrockState = CORROCK_STATE.get();
			for (int i = 0; i < 16; i++) {
				if (rand.nextFloat() < 0.4F && world.isEmptyBlock(mutable.setWithOffset(pos, rand.nextInt(size) - rand.nextInt(size), 1, rand.nextInt(size) - rand.nextInt(size)))) {
					if (world.getBlockState(mutable.below()).getBlock() == CORROCK_BLOCK_BLOCK) {
						world.setBlock(mutable, corrockState, 2);
					}
				}
			}
			return true;
		}
		return false;
	}

	private static Direction[] getDirections(boolean reversed) {
		Direction[] directions = Direction.Plane.HORIZONTAL.stream().toArray(Direction[]::new);
		if (reversed) ArrayUtils.reverse(directions);
		return directions;
	}

	private static boolean isTouchingWall(WorldGenLevel world, BlockPos origin) {
		for (Direction direction : DIRECTIONS) {
			if (searchForWall(world, origin.mutable(), direction) && searchForWall(world, origin.mutable().move(direction.getClockWise()), direction) && searchForWall(world, origin.mutable().move(direction.getCounterClockWise()), direction)) {
				return true;
			}
		}
		return false;
	}

	private static boolean searchForWall(WorldGenLevel world, BlockPos.MutableBlockPos mutable, Direction facing) {
		for (int i = 0; i < 2; i++) {
			Block block = world.getBlockState(mutable.move(facing)).getBlock();
			if (block == Blocks.END_STONE || block == CORROCK_BLOCK_BLOCK || block == EEBlocks.EUMUS.get()) {
				return true;
			}
		}
		return false;
	}

	private static void generateShelf(WorldGenLevel world, Random rand, int originX, int originY, int originZ, int size, int edgeBias, float underXDistance, float underZDistance, float crownChance) {
		int min = -(size / edgeBias + size);
		int max = size / edgeBias + size;
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
		BlockState corrockBlockState = CORROCK_BLOCK_STATE.get();
		List<BlockPos> wallCrowns = new ArrayList<>();
		List<BlockPos> speckledPatchPositions = new ArrayList<>();
		for (int x = min; x <= max; x++) {
			for (int z = min; z <= max; z++) {
				mutable.set(originX + x, originY, originZ + z);
				if (canReplace(world, mutable)) {
					double radius = (Math.cos(4 * Math.atan2(z, x)) / edgeBias + 1) * size;
					int distance = x * x + z * z;
					if (distance < radius * radius) {
						world.setBlock(mutable, corrockBlockState, 2);
						if (rand.nextFloat() < 0.25F) {
							speckledPatchPositions.add(mutable.immutable());
						}
						if (x * x < (radius - underXDistance) * (radius - underXDistance) && z * z < (radius - underZDistance) * (radius - underZDistance)) {
							BlockPos down = mutable.below();
							if (canReplace(world, down)) {
								world.setBlock(down, corrockBlockState, 2);
							}
						}
						if (rand.nextFloat() < crownChance) {
							double radiusMinusOne = radius - 1.0F;
							if (distance > radiusMinusOne * radiusMinusOne) {
								if (rand.nextBoolean()) {
									BlockPos up = mutable.above();
									if (canReplace(world, up)) {
										world.setBlock(up, getCorrockCrownStanding(rand.nextInt(16)), 2);
									}
								} else {
									wallCrowns.add(mutable.immutable());
								}
							}
						}
					}
				}
			}
		}
		for (BlockPos pos : wallCrowns) {
			int crownsPlaced = 0;
			//Reduces bias of directions
			Direction[] directions = rand.nextBoolean() ? DIRECTIONS : DIRECTIONS_REVERSED;
			for (Direction direction : directions) {
				BlockPos offset = pos.relative(direction);
				if (canReplace(world, offset)) {
					world.setBlock(offset, getCorrockCrownWall(direction), 2);
					if (rand.nextFloat() > crownChance || crownsPlaced++ == 1) {
						break;
					}
				}
			}
		}
		for (BlockPos pos : speckledPatchPositions) {
			int radius = 1;
			for (int x = -radius; x <= radius; x++) {
				for (int y = -radius; y <= radius; y++) {
					for (int z = -radius; z <= radius; z++) {
						if (rand.nextFloat() <= 0.6F) {
							if (rand.nextBoolean()) {
								if (rand.nextBoolean()) {
									x += (rand.nextInt(3) - rand.nextInt(3));
								} else {
									z += (rand.nextInt(3) - rand.nextInt(3));
								}
							} else {
								y += (rand.nextInt(3) - rand.nextInt(3));
							}
						}
						if (world.getBlockState(mutable.setWithOffset(pos, x, y, z)).getBlock() == Blocks.END_STONE && rand.nextFloat() < 0.6F) {
							world.setBlock(mutable, SPECKLED_CORROCK, 2);
						}
					}
				}
			}
		}
	}

	private static boolean canReplace(WorldGenLevel world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		return state.getMaterial().isReplaceable() || state.getBlock() instanceof CorrockPlantBlock || state.getBlock() instanceof CorrockCrownBlock;
	}
}
