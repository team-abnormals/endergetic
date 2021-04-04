package com.minecraftabnormals.endergetic.common.world.features.corrock;

import com.minecraftabnormals.endergetic.common.blocks.CorrockCrownBlock;
import com.minecraftabnormals.endergetic.common.blocks.CorrockPlantBlock;
import com.minecraftabnormals.endergetic.common.world.structures.pieces.EetleNestPieces;
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
	public boolean generate(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, ProbabilityConfig config) {
		//Dirty trick to fix Shelfs attaching to not yet generated chunks in Eetle Nests
		if (EetleNestPieces.isNotInsideGeneratingBounds(pos) && world.isAirBlock(pos) && world.getBlockState(pos.up()).getBlock() != CORROCK_BLOCK_BLOCK && isTouchingWall(world, pos)) {
			int size = rand.nextBoolean() ? 3 : 4;
			generateShelf(world, rand, pos.getX(), pos.getY(), pos.getZ(), size, 10, rand.nextInt(2) + 2, rand.nextInt(2) + 2, config.probability);
			BlockPos.Mutable mutable = new BlockPos.Mutable();
			BlockState corrockState = CORROCK_STATE.getValue();
			for (int i = 0; i < 16; i++) {
				if (rand.nextFloat() < 0.4F && world.isAirBlock(mutable.setAndOffset(pos, rand.nextInt(size) - rand.nextInt(size), 1, rand.nextInt(size) - rand.nextInt(size)))) {
					if (world.getBlockState(mutable.down()).getBlock() == CORROCK_BLOCK_BLOCK) {
						world.setBlockState(mutable, corrockState, 2);
					}
				}
			}
			return true;
		}
		return false;
	}

	private static Direction[] getDirections(boolean reversed) {
		Direction[] directions = Direction.Plane.HORIZONTAL.getDirectionValues().toArray(Direction[]::new);
		if (reversed) ArrayUtils.reverse(directions);
		return directions;
	}

	private static boolean isTouchingWall(ISeedReader world, BlockPos origin) {
		for (Direction direction : DIRECTIONS) {
			if (searchForWall(world, origin.toMutable(), direction) && searchForWall(world, origin.toMutable().move(direction.rotateY()), direction) && searchForWall(world, origin.toMutable().move(direction.rotateYCCW()), direction)) {
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
		BlockState corrockBlockState = CORROCK_BLOCK_STATE.getValue();
		List<BlockPos> wallCrowns = new ArrayList<>();
		for (int x = min; x <= max; x++) {
			for (int z = min; z <= max; z++) {
				mutable.setPos(originX + x, originY, originZ + z);
				if (canReplace(world, mutable)) {
					double radius = (Math.cos(4 * Math.atan2(z, x)) / edgeBias + 1) * size;
					int distance = x * x + z * z;
					if (distance < radius * radius) {
						world.setBlockState(mutable, corrockBlockState, 2);
						if (x * x < (radius - underXDistance) * (radius - underXDistance) && z * z < (radius - underZDistance) * (radius - underZDistance)) {
							BlockPos down = mutable.down();
							if (canReplace(world, down)) {
								world.setBlockState(down, corrockBlockState, 2);
							}
						}
						if (rand.nextFloat() < crownChance) {
							double radiusMinusOne = radius - 1.0F;
							if (distance > radiusMinusOne * radiusMinusOne) {
								if (rand.nextBoolean()) {
									BlockPos up = mutable.up();
									if (canReplace(world, up)) {
										world.setBlockState(up, getCorrockCrownStanding(rand.nextInt(16)), 2);
									}
								} else {
									wallCrowns.add(mutable.toImmutable());
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
				BlockPos offset = pos.offset(direction);
				if (canReplace(world, offset)) {
					world.setBlockState(offset, getCorrockCrownWall(direction), 2);
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
