package com.minecraftabnormals.endergetic.common.world.features.corrock;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.minecraftabnormals.abnormals_core.core.util.GenerationPiece;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.minecraftabnormals.endergetic.common.blocks.CorrockCrownStandingBlock;
import com.minecraftabnormals.endergetic.common.blocks.CorrockCrownWallBlock;
import com.minecraftabnormals.endergetic.common.world.features.EEFeatures;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.FeatureSpread;
import net.minecraft.world.gen.feature.ProbabilityConfig;
import net.minecraft.world.gen.feature.SphereReplaceConfig;

/**
 * @author SmellyModder (Luke Tonon)
 */
public class CorrockBranchFeature extends AbstractCorrockFeature {
	private static final SphereReplaceConfig SPHERE_CONFIG = new SphereReplaceConfig(CORROCK_BLOCK.get(), FeatureSpread.func_242252_a(3), 3, Lists.newArrayList(Blocks.END_STONE.getDefaultState()));

	public CorrockBranchFeature(Codec<ProbabilityConfig> configFactory) {
		super(configFactory);
	}

	@Override
	public boolean generate(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, ProbabilityConfig config) {
		if (rand.nextFloat() > config.probability) return false;

		Block belowBlock = world.getBlockState(pos.down()).getBlock();

		if (belowBlock == EEBlocks.CORROCK_END_BLOCK.get() || belowBlock == Blocks.END_STONE) {
			int baseHeight = rand.nextInt(4) + 4;
			GenerationPiece basePiece = this.createBase(world, pos, rand, baseHeight);
			if (basePiece.canPlace(world)) {
				int branchCount = rand.nextBoolean() ? 1 : rand.nextInt(3) + 1;
				List<Pair<GenerationPiece, ChorusPlantPart>> branches = this.createBranches(world, pos, rand, branchCount, baseHeight);
				Pair<GenerationPiece, ChorusPlantPart> firstBranch = branches.get(0);
				GenerationPiece firstBranchPiece = firstBranch.getFirst();
				if (firstBranchPiece.canPlace(world)) {
					basePiece.place(world);
					firstBranchPiece.place(world);

					ChorusPlantPart firstChorusPart = firstBranch.getSecond();
					if (firstChorusPart != null) {
						firstChorusPart.placeGrowth(world, rand);
					}

					for (int i = 1; i < branchCount; i++) {
						Pair<GenerationPiece, ChorusPlantPart> branch = branches.get(i);
						GenerationPiece branchPiece = branch.getFirst();

						if (branchPiece.canPlace(world)) {
							branchPiece.place(world);

							ChorusPlantPart chorusPlantPart = branch.getSecond();
							if (chorusPlantPart != null) {
								chorusPlantPart.placeGrowth(world, rand);
							}
						}
					}

					BlockPos groundModifierPos = new BlockPos(pos.getX() - 1 + (rand.nextInt(3) - rand.nextInt(3)), pos.getY() - 1, pos.getZ() - 1 + (rand.nextInt(3) - rand.nextInt(3)));
					EEFeatures.GROUND_PATCH.get().generate(world, generator, rand, groundModifierPos, SPHERE_CONFIG);

					BlockPos.Mutable corrockPlantPos = new BlockPos.Mutable();
					for (int x = pos.getX() - 4; x < pos.getX() + 4; x++) {
						for (int y = pos.getY(); y < pos.getY() + baseHeight + 10; y++) {
							for (int z = pos.getZ() - 4; z < pos.getZ() + 4; z++) {
								corrockPlantPos.setPos(x, y, z);
								boolean isCorrockBelow = world.getBlockState(corrockPlantPos.down()).getBlock() == EEBlocks.CORROCK_END_BLOCK.get();
								if ((isCorrockBelow && rand.nextFloat() < 0.5F || !isCorrockBelow && rand.nextFloat() < 0.025F) && world.isAirBlock(corrockPlantPos) && CORROCK.get().isValidPosition(world, corrockPlantPos)) {
									world.setBlockState(corrockPlantPos, CORROCK.get(), 2);
								}
							}
						}
					}
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Creates the base of the branch feature, this includes the middle pillar where the branches will start from and the cluster around the origin.
	 */
	private GenerationPiece createBase(IWorld world, BlockPos pos, Random rand, int height) {
		GenerationPiece piece = new GenerationPiece((iworld, part) -> iworld.isAirBlock(part.pos));
		int heightMinusOne = height - 1;
		BlockPos topPos = pos.up(height);
		for (int y = 0; y < height; y++) {
			piece.addBlockPiece(CORROCK_BLOCK.get(), pos.up(y));

			if (y == heightMinusOne && rand.nextFloat() < 0.85F) {
				piece.addBlockPiece(this.randomStandingCorrockCrown(rand), topPos);
			}
		}
		/*
		 * Creates cluster around origin
		 */
		int posX = pos.getX();
		int posY = pos.getY();
		int posZ = pos.getZ();
		int startX = posX - 1;
		int startZ = posZ - 1;
		int endX = posX + 1;
		int endZ = posZ + 1;
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		for (int x = startX; x <= endX; x++) {
			for (int z = startZ; z <= endZ; z++) {
				if (rand.nextFloat() < 0.4F) {
					mutable.setPos(x, posY, z);
					if (this.tryToMakeAreaBelowPlacableOn(piece, world, mutable)) {
						int randSideHeight = rand.nextInt(heightMinusOne) + 1;
						for (int y = 0; y < randSideHeight; y++) {
							piece.addBlockPiece(CORROCK_BLOCK.get(), mutable.up(y));
						}
					}
				}
			}
		}
		return piece;
	}

	/**
	 * Tries to make the area below the cluster around the origin have no air spaces.
	 */
	private boolean tryToMakeAreaBelowPlacableOn(GenerationPiece piece, IWorld world, BlockPos pos) {
		BlockPos down = pos.down();
		if (world.isAirBlock(down) && !world.isAirBlock(pos.down(3))) {
			piece.addBlockPiece(CORROCK_BLOCK.get(), down);
			BlockPos doubleDown = pos.down(2);
			if (world.isAirBlock(doubleDown)) {
				piece.addBlockPiece(CORROCK_BLOCK.get(), doubleDown);
			}
			return true;
		}
		return false;
	}

	/**
	 * Tries to create an amount (count) of branches. Each branch has a {@link GenerationPiece} for its formation and a {@link ChorusPlantPart} if it has a chorus growth at the top.
	 */
	private List<Pair<GenerationPiece, ChorusPlantPart>> createBranches(IWorld world, BlockPos pos, Random rand, int count, int height) {
		List<Pair<GenerationPiece, ChorusPlantPart>> pieces = Lists.newArrayList();
		BlockPos branchStart = pos.up(height - 1);
		for (int i = 0; i < count; i++) {
			GenerationPiece basePiece = new GenerationPiece((iworld, part) -> world.isAirBlock(part.pos));
			pieces.add(new Pair<>(basePiece, this.createBranch(world, branchStart, rand, basePiece, this.randomHorizontalDirection(rand), rand.nextInt(2) + 1)));
		}
		return pieces;
	}

	/**
	 * Creates a branch starting from a position and a direction with a max amount of sub-branches.
	 */
	@Nullable
	private ChorusPlantPart createBranch(IWorld world, BlockPos pos, Random rand, GenerationPiece basePiece, Direction horizontalStep, int subBranches) {
		ChorusPlantPart chorusPlantPart = null;
		int branched = 0;
		int prevBranchHeight = 0;
		int branchHeight = rand.nextInt(3) + 4;
		BlockPos offset = pos.offset(horizontalStep);
		for (int y = 0; y < branchHeight; y++) {
			basePiece.addBlockPiece(CORROCK_BLOCK.get(), offset.up(y));
			if (y == branchHeight - 1) {
				boolean lastBranched = branched == subBranches;
				if (rand.nextBoolean()) {
					BlockPos crownOrigin = offset.up(y);
					this.createCrownOrbit(basePiece, world, crownOrigin, rand);
					if (lastBranched) {
						chorusPlantPart = new ChorusPlantPart(crownOrigin);
						break;
					}
				} else if (lastBranched) {
					break;
				}

				if (rand.nextFloat() > 0.6F) {
					horizontalStep = rand.nextBoolean() ? horizontalStep.rotateY() : horizontalStep.rotateYCCW();
				}

				boolean beforeLastBranched = branched == subBranches - 1;
				if (beforeLastBranched) {
					int middle = prevBranchHeight + (branchHeight - prevBranchHeight) / 2;
					y -= branchHeight - middle;
					branchHeight = middle;
					basePiece.addBlockPiece(CORROCK_BLOCK.get(), offset.up(branchHeight).offset(horizontalStep));
					offset = offset.offset(horizontalStep, 2);
				} else {
					offset = offset.offset(horizontalStep);
					y--;
					branchHeight--;
				}

				prevBranchHeight = branchHeight;
				branchHeight += rand.nextInt(3) + 4;
				branched++;
			}
		}
		return chorusPlantPart;
	}

	/**
	 * Creates corrock crowns 'orbiting' (i.e. attached to all open sides) a position.
	 */
	private void createCrownOrbit(GenerationPiece branch, IWorld world, BlockPos pos, Random rand) {
		for (Direction horizontal : Direction.Plane.HORIZONTAL) {
			BlockPos placingPos = pos.offset(horizontal);
			if (rand.nextFloat() < 0.35F && world.isAirBlock(placingPos)) {
				branch.addBlockPiece(CORROCK_CROWN(true).get().with(CorrockCrownWallBlock.FACING, horizontal), placingPos);
			}
		}
		if (rand.nextBoolean() && world.isAirBlock(pos.up())) {
			branch.addBlockPiece(this.randomStandingCorrockCrown(rand), pos.up());
		}
	}

	private Direction randomHorizontalDirection(Random rand) {
		return Direction.byHorizontalIndex(rand.nextInt(6));
	}

	private BlockState randomStandingCorrockCrown(Random rand) {
		return CORROCK_CROWN(false).get().with(CorrockCrownStandingBlock.ROTATION, rand.nextInt(16));
	}
}