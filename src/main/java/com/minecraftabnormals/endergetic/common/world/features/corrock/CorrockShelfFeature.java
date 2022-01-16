package com.minecraftabnormals.endergetic.common.world.features.corrock;

import com.minecraftabnormals.endergetic.common.blocks.CorrockCrownBlock;
import com.minecraftabnormals.endergetic.common.blocks.CorrockPlantBlock;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.ProbabilityConfig;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CorrockShelfFeature extends AbstractCorrockFeature<ProbabilityConfig> {
	private static final Direction[] DIRECTIONS = getDirections(false);
	private static final Direction[] DIRECTIONS_REVERSED = getDirections(true);

	public CorrockShelfFeature(Codec<ProbabilityConfig> configFactory) {
		super(configFactory);
	}

	@Override
	public boolean place(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, ProbabilityConfig config) {
		if (world.isEmptyBlock(pos) && world.getBlockState(pos.above()).getBlock() != CORROCK_BLOCK_BLOCK && isTouchingWall(world, pos)) {
			int size = rand.nextBoolean() ? 3 : 4;
			//Constantly 10, but could be changed in the future to allow for more variations
			int edgeBias = 10;
			generateShelf(world, rand, pos.getX(), pos.getY(), pos.getZ(), size, edgeBias, rand.nextInt(2) + 2, rand.nextInt(2) + 2, config.probability);
			BlockPos.Mutable mutable = new BlockPos.Mutable();
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

	private static boolean isTouchingWall(ISeedReader world, BlockPos origin) {
		for (Direction direction : DIRECTIONS) {
			if (searchForWall(world, origin.mutable(), direction) && searchForWall(world, origin.mutable().move(direction.getClockWise()), direction) && searchForWall(world, origin.mutable().move(direction.getCounterClockWise()), direction)) {
				return true;
			}
		}
		return false;
	}

	private static boolean searchForWall(ISeedReader world, BlockPos.Mutable mutable, Direction facing) {
		for (int i = 0; i < 2; i++) {
			Block block = world.getBlockState(mutable.move(facing)).getBlock();
			if (block == Blocks.END_STONE || block == CORROCK_BLOCK_BLOCK || block == EEBlocks.EUMUS.get()) {
				return true;
			}
		}
		return false;
	}

	private static void generateShelf(ISeedReader world, Random rand, int originX, int originY, int originZ, int size, int edgeBias, float underXDistance, float underZDistance, float crownChance) {
		int min = -(size / edgeBias + size);
		int max = size / edgeBias + size;
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		BlockState corrockBlockState = CORROCK_BLOCK_STATE.get();
		List<BlockPos> wallCrowns = new ArrayList<>();
		for (int x = min; x <= max; x++) {
			for (int z = min; z <= max; z++) {
				mutable.set(originX + x, originY, originZ + z);
				if (canReplace(world, mutable)) {
					double radius = (Math.cos(4 * Math.atan2(z, x)) / edgeBias + 1) * size;
					int distance = x * x + z * z;
					if (distance < radius * radius) {
						world.setBlock(mutable, corrockBlockState, 2);
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
	}

	private static boolean canReplace(ISeedReader world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		return state.getMaterial().isReplaceable() || state.getBlock() instanceof CorrockPlantBlock || state.getBlock() instanceof CorrockCrownBlock;
	}
}
