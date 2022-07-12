package com.teamabnormals.endergetic.common.world.features.corrock;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.teamabnormals.endergetic.common.world.configs.CorrockBranchConfig;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.teamabnormals.endergetic.core.registry.EEFeatures;
import com.teamabnormals.endergetic.core.registry.EEBlocks;

import com.teamabnormals.blueprint.core.util.GenerationPiece;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

/**
 * @author SmellyModder (Luke Tonon)
 */
public class CorrockBranchFeature extends AbstractCorrockFeature<CorrockBranchConfig> {

	public CorrockBranchFeature(Codec<CorrockBranchConfig> configFactory) {
		super(configFactory);
	}

	@Override
	public boolean place(FeaturePlaceContext<CorrockBranchConfig> context) {
		WorldGenLevel world = context.level();
		BlockPos pos = context.origin();
		CorrockBranchConfig config = context.config();
		BlockState belowState = world.getBlockState(pos.below());
		if (config.isValidGround(belowState) && world.getBlockState(pos.below(2)).canOcclude()) {
			RandomSource rand = context.random();
			int baseHeight = rand.nextInt(4) + 4;
			GenerationPiece basePiece = this.createBase(world, pos, rand, baseHeight);
			if (basePiece.canPlace(world)) {
				int branchCount = rand.nextBoolean() ? 1 : rand.nextInt(3) + 1;
				List<Pair<GenerationPiece, ChorusPlantPart>> branches = this.createBranches(world, pos, rand, branchCount, baseHeight, config.getCrownChance(), config.getDecoratedBranchChance());
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
					EEFeatures.Configured.DISK_CORROCK.get().place(world, context.chunkGenerator(), rand, groundModifierPos);

					BlockPos.MutableBlockPos corrockPlantPos = new BlockPos.MutableBlockPos();
					for (int x = pos.getX() - 4; x < pos.getX() + 4; x++) {
						for (int y = pos.getY(); y < pos.getY() + baseHeight + 10; y++) {
							for (int z = pos.getZ() - 4; z < pos.getZ() + 4; z++) {
								corrockPlantPos.set(x, y, z);
								boolean isCorrockBelow = world.getBlockState(corrockPlantPos.below()).getBlock() == EEBlocks.CORROCK_END_BLOCK.get();
								if ((isCorrockBelow && rand.nextFloat() < 0.25F || !isCorrockBelow && rand.nextFloat() < 0.025F) && world.isEmptyBlock(corrockPlantPos) && CORROCK_STATE.get().canSurvive(world, corrockPlantPos)) {
									world.setBlock(corrockPlantPos, CORROCK_STATE.get(), 2);
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
	private GenerationPiece createBase(LevelAccessor world, BlockPos pos, RandomSource rand, int height) {
		GenerationPiece piece = new GenerationPiece((iworld, part) -> iworld.isEmptyBlock(part.pos));
		BlockState corrockState = CORROCK_BLOCK_STATE.get();
		int heightMinusOne = height - 1;
		BlockPos topPos = pos.above(height);
		for (int y = 0; y < height; y++) {
			piece.addBlockPiece(corrockState, pos.above(y));

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
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
		for (int x = startX; x <= endX; x++) {
			for (int z = startZ; z <= endZ; z++) {
				if (rand.nextFloat() < 0.4F) {
					mutable.set(x, posY, z);
					if (this.tryToMakeAreaBelowPlacableOn(piece, world, mutable)) {
						int randSideHeight = rand.nextInt(heightMinusOne) + 1;
						for (int y = 0; y < randSideHeight; y++) {
							piece.addBlockPiece(corrockState, mutable.above(y));
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
	private boolean tryToMakeAreaBelowPlacableOn(GenerationPiece piece, LevelAccessor world, BlockPos pos) {
		BlockState corrockState = CORROCK_BLOCK_STATE.get();
		BlockPos down = pos.below();
		if (world.isEmptyBlock(down) && !world.isEmptyBlock(pos.below(3))) {
			piece.addBlockPiece(corrockState, down);
			BlockPos doubleDown = pos.below(2);
			if (world.isEmptyBlock(doubleDown)) {
				piece.addBlockPiece(corrockState, doubleDown);
			}
			return true;
		}
		return false;
	}

	/**
	 * Tries to create an amount (count) of branches. Each branch has a {@link GenerationPiece} for its formation and a {@link ChorusPlantPart} if it has a chorus growth at the top.
	 */
	private List<Pair<GenerationPiece, ChorusPlantPart>> createBranches(LevelAccessor world, BlockPos pos, RandomSource rand, int count, int height, float crownChance, float decorationChance) {
		List<Pair<GenerationPiece, ChorusPlantPart>> pieces = Lists.newArrayList();
		BlockPos branchStart = pos.above(height - 1);
		for (int i = 0; i < count; i++) {
			GenerationPiece basePiece = new GenerationPiece((iworld, part) -> world.isEmptyBlock(part.pos));
			pieces.add(new Pair<>(basePiece, this.createBranch(world, branchStart, rand, basePiece, this.randomHorizontalDirection(rand), rand.nextInt(2) + 1, crownChance, decorationChance)));
		}
		return pieces;
	}

	/**
	 * Creates a branch starting from a position and a direction with a max amount of sub-branches.
	 */
	@Nullable
	private ChorusPlantPart createBranch(LevelAccessor world, BlockPos pos, RandomSource rand, GenerationPiece basePiece, Direction horizontalStep, int subBranches, float crownChance, float decorationChance) {
		ChorusPlantPart chorusPlantPart = null;
		int branched = 0;
		int prevBranchHeight = 0;
		int branchHeight = rand.nextInt(3) + 4;
		BlockPos offset = pos.relative(horizontalStep);
		BlockState corrockState = CORROCK_BLOCK_STATE.get();
		for (int y = 0; y < branchHeight; y++) {
			basePiece.addBlockPiece(corrockState, offset.above(y));
			if (y == branchHeight - 1) {
				boolean lastBranched = branched == subBranches;
				if (rand.nextFloat() < decorationChance) {
					BlockPos crownOrigin = offset.above(y);
					this.createCrownOrbit(basePiece, world, crownOrigin, rand, crownChance);
					if (lastBranched) {
						chorusPlantPart = new ChorusPlantPart(crownOrigin);
						break;
					}
				} else if (lastBranched) {
					break;
				}

				if (rand.nextFloat() > 0.6F) {
					horizontalStep = rand.nextBoolean() ? horizontalStep.getClockWise() : horizontalStep.getCounterClockWise();
				}

				boolean beforeLastBranched = branched == subBranches - 1;
				if (beforeLastBranched) {
					int middle = prevBranchHeight + (branchHeight - prevBranchHeight) / 2;
					y -= branchHeight - middle;
					branchHeight = middle;
					basePiece.addBlockPiece(corrockState, offset.above(branchHeight).relative(horizontalStep));
					offset = offset.relative(horizontalStep, 2);
				} else {
					offset = offset.relative(horizontalStep);
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
	private void createCrownOrbit(GenerationPiece branch, LevelAccessor world, BlockPos pos, RandomSource rand, float crownChance) {
		for (Direction horizontal : Direction.Plane.HORIZONTAL) {
			BlockPos placingPos = pos.relative(horizontal);
			if (rand.nextFloat() < crownChance && world.isEmptyBlock(placingPos)) {
				branch.addBlockPiece(getCorrockCrownWall(horizontal), placingPos);
			}
		}
		if (rand.nextFloat() < crownChance && world.isEmptyBlock(pos.above())) {
			branch.addBlockPiece(this.randomStandingCorrockCrown(rand), pos.above());
		}
	}

	private Direction randomHorizontalDirection(RandomSource rand) {
		return Direction.from2DDataValue(rand.nextInt(6));
	}

	private BlockState randomStandingCorrockCrown(RandomSource rand) {
		return getCorrockCrownStanding(rand.nextInt(16));
	}
}