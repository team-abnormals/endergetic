package com.minecraftabnormals.endergetic.common.world.features.corrock;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.teamabnormals.abnormals_core.core.library.GenerationPiece;
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
import net.minecraft.world.gen.feature.ProbabilityConfig;
import net.minecraft.world.gen.feature.SphereReplaceConfig;
import net.minecraft.world.gen.feature.structure.StructureManager;

public class CorrockBranchFeature extends AbstractCorrockFeature {

	public CorrockBranchFeature(Codec<ProbabilityConfig> configFactory) {
		super(configFactory);
	}

	@Override
	public boolean func_230362_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random rand, BlockPos pos, ProbabilityConfig config) {
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
					
					BlockPos downPos = pos.down();
					BlockPos groundModifierPos = new BlockPos(downPos.getX() + (rand.nextInt(3) - rand.nextInt(3)), downPos.getY(), downPos.getZ() + (rand.nextInt(3) - rand.nextInt(3)));
					EEFeatures.GROUND_PATCH.get().func_230362_a_(world, manager, generator, rand, groundModifierPos, new SphereReplaceConfig(CORROCK_BLOCK.get(), 3, 3, Lists.newArrayList(Blocks.END_STONE.getDefaultState())));
					
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
	
	private List<Pair<GenerationPiece, ChorusPlantPart>> createBranches(IWorld world, BlockPos pos, Random rand, int count, int height) {
		List<Pair<GenerationPiece, ChorusPlantPart>> pieces = Lists.newArrayList();
		for (int i = 0; i < count; i++) {
			pieces.add(this.createBranch(world, pos, rand, height));
		}
		return pieces;
	}
	
	private Pair<GenerationPiece, ChorusPlantPart> createBranch(IWorld world, BlockPos pos, Random rand, int height) {
		GenerationPiece basePiece = new GenerationPiece((iworld, part) -> world.isAirBlock(part.pos));
		ChorusPlantPart chorusPlantPart = null;
		BlockPos startPos = pos.up(height - 1);
		Direction horizontalStep = this.randomHorizontalDirection(rand);
		boolean shouldStep = rand.nextBoolean();
		
		if (shouldStep) {
			for (int y = 0; y < 3; y++) {
				basePiece.addBlockPiece(CORROCK_BLOCK.get(), startPos.offset(horizontalStep).up(y));
			}

			int branchHeight = rand.nextInt(3) + 4;
			for (int y = 0; y < branchHeight; y++) {
				basePiece.addBlockPiece(CORROCK_BLOCK.get(), startPos.offset(horizontalStep, 2).up(2).up(y));

				if (y == branchHeight - 1 && rand.nextFloat() < 0.85F) {
					this.createCrownOrbit(basePiece, world, startPos.offset(horizontalStep, 2).up(2).up(y), rand);
				}
			}

			Direction sideStep = this.randomHorizontalDirection(rand);
			int sideYPos = branchHeight / 2;

			for (int offset = 0; offset < 2; offset++) {
				basePiece.addBlockPiece(CORROCK_BLOCK.get(), startPos.offset(horizontalStep, 2).offset(sideStep, offset).up(2).up(sideYPos));
			}

			int lastBranchHeight = rand.nextInt(3) + 4;
			for (int y = 0; y < lastBranchHeight; y++) {
				basePiece.addBlockPiece(CORROCK_BLOCK.get(), startPos.offset(horizontalStep, 2).offset(sideStep, 2).up(2).up(sideYPos).up(y));

				if (y == lastBranchHeight - 1 && rand.nextFloat() < 0.85F) {
					BlockPos crownOrigin = startPos.offset(horizontalStep, 2).offset(sideStep, 2).up(2).up(sideYPos).up(y);
					this.createCrownOrbit(basePiece, world, crownOrigin, rand);
					ChorusPlantPart chorusPart = this.tryToCreateChorusPlantPart(world, crownOrigin, rand);
					if (chorusPart != null) {
						chorusPlantPart = chorusPart;
					}
				}
			}
		} else {
			ChorusPlantPart stepBranch = this.createStepBranch(world, startPos, rand, basePiece, horizontalStep, 1);
			if (stepBranch != null) {
				chorusPlantPart = stepBranch;
			}
		}
		return new Pair<>(basePiece, chorusPlantPart);
	}

	/**
	 * This is a new more extensible stepping branch algorithm.
	 * Only used for 1 branched forms currently.
	 * TODO: Combine logic of double stepped form into this to support maxIndex > 1
	 */
	@Nullable
	private ChorusPlantPart createStepBranch(IWorld world, BlockPos pos, Random rand, GenerationPiece basePiece, Direction horizontalStep, int maxIndex) {
		ChorusPlantPart chorusPlantPart = null;
		int branched = 0;
		int branchHeight = rand.nextInt(3) + 4;
		BlockPos offset = pos.offset(horizontalStep);
		for (int y = 0; y < branchHeight; y++) {
			basePiece.addBlockPiece(CORROCK_BLOCK.get(), offset.up(y));
			if (y == branchHeight - 1) {
				boolean lastBranched = branched == maxIndex;
				if (rand.nextBoolean()) {
					BlockPos crownOrigin = offset.up(y);
					this.createCrownOrbit(basePiece, world, crownOrigin, rand);
					if (lastBranched) {
						ChorusPlantPart chorusPart = this.tryToCreateChorusPlantPart(world, crownOrigin, rand);
						if (chorusPart != null) {
							chorusPlantPart = chorusPart;
						}
						break;
					}
				} else if (lastBranched) {
					break;
				}

				if (rand.nextFloat() > 0.6F) {
					horizontalStep = rand.nextBoolean() ? horizontalStep.rotateY() : horizontalStep.rotateYCCW();
				}

				boolean beforeLastBranched = branched == maxIndex - 1;
				if (beforeLastBranched) {
					int middle = branchHeight / 2;
					y -= branchHeight - middle;
					branchHeight = middle;
					basePiece.addBlockPiece(CORROCK_BLOCK.get(), offset.up(branchHeight).offset(horizontalStep));
					offset = offset.offset(horizontalStep, 2);
				} else {
					offset = offset.offset(horizontalStep);
				}

				branchHeight += rand.nextInt(3) + 4;
				branched++;
			}
		}
		return chorusPlantPart;
	}
	
	private void createCrownOrbit(GenerationPiece branch, IWorld world, BlockPos pos, Random rand) {
		for (Direction horizontal : Direction.Plane.HORIZONTAL) {
			BlockPos placingPos = pos.offset(horizontal);
			if (rand.nextFloat() < 0.35F && world.isAirBlock(placingPos)) {
				branch.addBlockPiece(CORROCK_CROWN(true).get().with(CorrockCrownWallBlock.FACING, horizontal), placingPos);
			}
		}
		if (rand.nextBoolean()) {
			branch.addBlockPiece(this.randomStandingCorrockCrown(rand), pos.up());
		}
	}

	@Nullable
	private ChorusPlantPart tryToCreateChorusPlantPart(IWorld world, BlockPos pos, Random rand) {
		for (Direction horizontal : Direction.Plane.HORIZONTAL) {
			BlockPos placingPos = pos.offset(horizontal);
			if (rand.nextFloat() < 0.3F && world.isAirBlock(placingPos)) {
				return new ChorusPlantPart(placingPos, horizontal);
			}
		}
		return null;
	}
	
	private Direction randomHorizontalDirection(Random rand) {
		return Direction.byHorizontalIndex(rand.nextInt(6));
	}
	
	private BlockState randomStandingCorrockCrown(Random rand) {
		return CORROCK_CROWN(false).get().with(CorrockCrownStandingBlock.ROTATION, rand.nextInt(16));
	}
}