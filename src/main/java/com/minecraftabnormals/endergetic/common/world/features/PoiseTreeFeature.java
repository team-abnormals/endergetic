package com.minecraftabnormals.endergetic.common.world.features;

import java.util.Random;
import java.util.function.Supplier;

import com.minecraftabnormals.endergetic.api.util.GenerationUtils;
import com.minecraftabnormals.endergetic.common.blocks.poise.GlowingPoiseStemBlock;
import com.minecraftabnormals.endergetic.common.blocks.poise.PoiseTallBushBlock;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.minecraftabnormals.endergetic.core.registry.other.EETags;
import com.mojang.serialization.Codec;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

/**
 * @author - SmellyModder(Luke Tonon)
 */
public class PoiseTreeFeature extends Feature<NoFeatureConfig> {
	private final Supplier<BlockState> POISMOSS_EUMUS = () -> EEBlocks.EUMUS_POISMOSS.get().defaultBlockState();
	private final Supplier<BlockState> POISE_STEM = () -> EEBlocks.POISE_STEM.get().defaultBlockState();
	private final Supplier<BlockState> GLOWING_POISE_STEM = () -> EEBlocks.GLOWING_POISE_STEM.get().defaultBlockState();

	public PoiseTreeFeature(Codec<NoFeatureConfig> configFactoryIn) {
		super(configFactoryIn);
	}

	@Override
	public boolean place(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config) {
		int treeHeight = rand.nextInt(19) + 13;
		int size = 0;
		//Random tree top size; small(45%), medium(40%), large(15%)
		float rng = rand.nextFloat();
		if (rng <= 0.45F) {
			size = 0;
		} else {
			if (rng >= 0.85F) {
				size = 2;
			} else {
				size = 1;
			}
		}
		if (this.isValidGround(world, pos.below()) && this.isAreaOpen(world, pos)) {
			boolean[] isSutableForSizes = new boolean[]{
					GenerationUtils.isAreaReplacable(world, pos.north(3).west(3).above(treeHeight).getX(), pos.north(3).west(3).above(treeHeight).getY(), pos.north(3).west(3).above(treeHeight).getZ(), pos.south(3).east(3).above(treeHeight + 7).getX(), pos.south(3).east(3).above(treeHeight + 7).getY(), pos.south(3).east(3).above(treeHeight + 7).getZ()),
					GenerationUtils.isAreaReplacable(world, pos.north(4).west(4).above(treeHeight).getX(), pos.north(4).west(4).above(treeHeight).getY(), pos.north(4).west(4).above(treeHeight).getZ(), pos.south(4).east(4).above(treeHeight + 9).getX(), pos.south(4).east(4).above(treeHeight + 9).getY(), pos.south(4).east(4).above(treeHeight + 9).getZ()),
					GenerationUtils.isAreaReplacable(world, pos.north(6).west(6).above(treeHeight).getX(), pos.north(6).west(6).above(treeHeight).getY(), pos.north(6).west(6).above(treeHeight).getZ(), pos.south(6).east(6).above(treeHeight + 13).getX(), pos.south(6).east(6).above(treeHeight + 13).getY(), pos.south(6).east(6).above(treeHeight + 13).getZ())
			};
			if (size == 0) {
				if (GenerationUtils.isAreaReplacable(world, pos.north().west().getX(), pos.north().west().getY(), pos.north().west().getZ(), pos.south().east().above(treeHeight).getX(), pos.south().east().above(treeHeight).getY(), pos.south().east().above(treeHeight).getZ())) {
					if (isSutableForSizes[0]) {
						this.buildTreeBase(world, pos, rand);
						this.buildStem(world, pos, rand, treeHeight);
						this.buildTreeTop(world, pos, rand, treeHeight, size);

						if (rand.nextFloat() <= 0.80F) {
							this.tryToBuildBranch(world, pos, rand, treeHeight);
						}

						this.buildPoismossCircle(world, world, rand, pos);
						return true;
					}
					return false;
				} else {
					return false;
				}
			} else if (size == 1) {
				if (GenerationUtils.isAreaReplacable(world, pos.north().west().getX(), pos.north().west().getY(), pos.north().west().getZ(), pos.south().east().above(treeHeight).getX(), pos.south().east().above(treeHeight).getY(), pos.south().east().above(treeHeight).getZ())) {
					if (isSutableForSizes[1]) {
						this.buildTreeBase(world, pos, rand);
						this.buildStem(world, pos, rand, treeHeight);
						this.buildTreeTop(world, pos, rand, treeHeight, size);

						if (rand.nextFloat() <= 0.80F) {
							this.tryToBuildBranch(world, pos, rand, treeHeight);
						}

						this.buildPoismossCircle(world, world, rand, pos);
						return true;
					} else {
						if (isSutableForSizes[0]) {
							this.buildTreeBase(world, pos, rand);
							this.buildStem(world, pos, rand, treeHeight);
							this.buildTreeTop(world, pos, rand, treeHeight, 0);

							if (rand.nextFloat() <= 0.80F) {
								this.tryToBuildBranch(world, pos, rand, treeHeight);
							}

							this.buildPoismossCircle(world, world, rand, pos);
							return true;
						}
					}
					return false;
				} else {
					return false;
				}
			} else {
				if (GenerationUtils.isAreaReplacable(world, pos.north().west().getX(), pos.north().west().getY(), pos.north().west().getZ(), pos.south().east().above(treeHeight).getX(), pos.south().east().above(treeHeight).getY(), pos.south().east().above(treeHeight).getZ())) {
					if (isSutableForSizes[2]) {
						this.buildTreeBase(world, pos, rand);
						this.buildStem(world, pos, rand, treeHeight);
						this.buildTreeTop(world, pos, rand, treeHeight, size);

						if (rand.nextFloat() <= 0.80F) {
							this.tryToBuildBranch(world, pos, rand, treeHeight);
						}

						this.buildPoismossCircle(world, world, rand, pos);
						return true;
					} else {
						if (isSutableForSizes[1]) {
							this.buildTreeBase(world, pos, rand);
							this.buildStem(world, pos, rand, treeHeight);
							this.buildTreeTop(world, pos, rand, treeHeight, 1);

							if (rand.nextFloat() <= 0.80F) {
								this.tryToBuildBranch(world, pos, rand, treeHeight);
							}

							this.buildPoismossCircle(world, world, rand, pos);
							return true;
						} else {
							if (isSutableForSizes[0]) {
								this.buildTreeBase(world, pos, rand);
								this.buildStem(world, pos, rand, treeHeight);
								this.buildTreeTop(world, pos, rand, treeHeight, 0);

								if (rand.nextFloat() <= 0.80F) {
									this.tryToBuildBranch(world, pos, rand, treeHeight);
								}

								this.buildPoismossCircle(world, world, rand, pos);
								return true;
							}
						}
					}
					return false;
				} else {
					return false;
				}
			}
		}
		return false;
	}

	private void buildTreeBase(IWorld world, BlockPos pos, Random rand) {
		int[] sideRandValues = new int[]{
				rand.nextInt(8) + 2,
				rand.nextInt(8) + 2,
				rand.nextInt(8) + 2,
				rand.nextInt(8) + 2
		};
		for (int x = pos.getX() - 1; x < pos.getX() + 2; x++) {
			for (int z = pos.getZ() - 1; z < pos.getZ() + 2; z++) {
				this.setPoiseLog(world, new BlockPos(x, pos.getY(), z), rand, true, false);
			}
		}
		for (int xN = 1; xN < sideRandValues[0] + 1; xN++) {
			this.setPoiseLog(world, pos.north().above(xN), rand, true, false);
		}
		for (int xE = 1; xE < sideRandValues[1] + 1; xE++) {
			this.setPoiseLog(world, pos.east().above(xE), rand, true, false);
		}
		for (int xS = 1; xS < sideRandValues[2] + 1; xS++) {
			this.setPoiseLog(world, pos.south().above(xS), rand, true, false);
		}
		for (int xW = 1; xW < sideRandValues[3] + 1; xW++) {
			this.setPoiseLog(world, pos.west().above(xW), rand, true, false);
		}

		BlockPos downPos = pos.below(2);

		if (GenerationUtils.isAreaCompletelySolid(world, downPos.getX() - 1, downPos.getY(), downPos.getZ() - 1, downPos.getX() + 1, downPos.getY(), downPos.getZ() + 1)) {
			GenerationUtils.fillAreaWithBlockCube(world, downPos.getX() - 1, downPos.getY(), downPos.getZ() - 1, downPos.getX() + 1, downPos.getY() + 1, downPos.getZ() + 1, POISE_STEM.get());
		}
	}

	private void buildStem(IWorld world, BlockPos pos, Random rand, int height) {
		for (int y = 1; y < height; y++) {
			boolean doBubbles = y <= height - 2 ? false : true;
			this.setPoiseLog(world, pos.above(y), rand, false, doBubbles);
		}
	}

	private void buildTreeTop(IWorld world, BlockPos pos, Random rand, int arrivedPos, int size) {
		if (size == 0) {
			for (int x = pos.getX() - 1; x <= pos.getX() + 1; x++) {
				for (int z = pos.getZ() - 1; z <= pos.getZ() + 1; z++) {
					this.setPoiseLog(world, new BlockPos(x, pos.above(arrivedPos).getY(), z), rand, false, true);
				}
			}
			for (int x = pos.getX() - 1; x <= pos.getX() + 1; x++) {
				for (int z = pos.getZ() - 1; z <= pos.getZ() + 1; z++) {
					this.setBlockState(world, new BlockPos(x, pos.above(arrivedPos + 1).getY(), z), POISMOSS_EUMUS.get());
				}
			}
			this.setPoiseLog(world, pos.above(arrivedPos).north(2), rand, false, true);
			this.setPoiseLog(world, pos.above(arrivedPos).east(2), rand, false, true);
			this.setPoiseLog(world, pos.above(arrivedPos).south(2), rand, false, true);
			this.setPoiseLog(world, pos.above(arrivedPos).west(2), rand, false, true);

			this.setPoiseLog(world, pos.above(arrivedPos).north(3).above(), rand, false, true);
			this.setPoiseLog(world, pos.above(arrivedPos).east(3).above(), rand, false, true);
			this.setPoiseLog(world, pos.above(arrivedPos).south(3).above(), rand, false, true);
			this.setPoiseLog(world, pos.above(arrivedPos).west(3).above(), rand, false, true);

			this.setPoiseLog(world, pos.above(arrivedPos).north(2).above().east(), rand, false, true);
			this.setPoiseLog(world, pos.above(arrivedPos).north(2).above().west(), rand, false, true);
			this.setPoiseLog(world, pos.above(arrivedPos).east(2).above().north(), rand, false, true);
			this.setPoiseLog(world, pos.above(arrivedPos).east(2).above().south(), rand, false, true);
			this.setPoiseLog(world, pos.above(arrivedPos).south(2).above().east(), rand, false, true);
			this.setPoiseLog(world, pos.above(arrivedPos).south(2).above().west(), rand, false, true);
			this.setPoiseLog(world, pos.above(arrivedPos).west(2).above().north(), rand, false, true);
			this.setPoiseLog(world, pos.above(arrivedPos).west(2).above().south(), rand, false, true);

			this.setBlockState(world, pos.above(arrivedPos).north(2).above(), POISMOSS_EUMUS.get());
			this.setBlockState(world, pos.above(arrivedPos).east(2).above(), POISMOSS_EUMUS.get());
			this.setBlockState(world, pos.above(arrivedPos).south(2).above(), POISMOSS_EUMUS.get());
			this.setBlockState(world, pos.above(arrivedPos).west(2).above(), POISMOSS_EUMUS.get());

			this.placeInnerDomeFeatures(world, rand, pos.above(arrivedPos).west(2).above());

			int[] sideRandValues = new int[]{
					rand.nextInt(3) + 1,
					rand.nextInt(3) + 1,
					rand.nextInt(3) + 1,
					rand.nextInt(3) + 1
			};
			for (int yn = 1; yn <= sideRandValues[0]; yn++) {
				this.setPoiseLog(world, pos.above(arrivedPos).north(3).above().above(yn), rand, false, true);
				if (yn == 1 && rand.nextFloat() <= 0.25F) {
					if (rand.nextBoolean()) {
						this.setPoiseLog(world, pos.above(arrivedPos).north(3).east().above().above(yn), rand, false, true);
					} else {
						this.setPoiseLog(world, pos.above(arrivedPos).north(3).west().above().above(yn), rand, false, true);
					}
				}
			}
			for (int ye = 1; ye <= sideRandValues[1]; ye++) {
				this.setPoiseLog(world, pos.above(arrivedPos).east(3).above().above(ye), rand, false, true);
				if (ye == 1 && rand.nextFloat() <= 0.25F) {
					if (rand.nextBoolean()) {
						this.setPoiseLog(world, pos.above(arrivedPos).east(3).south().above().above(ye), rand, false, true);
					} else {
						this.setPoiseLog(world, pos.above(arrivedPos).east(3).north().above().above(ye), rand, false, true);
					}
				}
			}
			for (int ys = 1; ys <= sideRandValues[2]; ys++) {
				this.setPoiseLog(world, pos.above(arrivedPos).south(3).above().above(ys), rand, false, true);
				if (ys == 1 && rand.nextFloat() <= 0.25F) {
					if (rand.nextBoolean()) {
						this.setPoiseLog(world, pos.above(arrivedPos).south(3).west().above().above(ys), rand, false, true);
					} else {
						this.setPoiseLog(world, pos.above(arrivedPos).south(3).east().above().above(ys), rand, false, true);
					}
				}
			}
			for (int yw = 1; yw <= sideRandValues[3]; yw++) {
				this.setPoiseLog(world, pos.above(arrivedPos).west(3).above().above(yw), rand, false, true);
				if (yw == 1 && rand.nextFloat() <= 0.25F) {
					if (rand.nextBoolean()) {
						this.setPoiseLog(world, pos.above(arrivedPos).west(3).north().above().above(yw), rand, false, true);
					} else {
						this.setPoiseLog(world, pos.above(arrivedPos).west(3).south().above().above(yw), rand, false, true);
					}
				}
			}
			this.addTreeDomeTop(world, pos, rand, arrivedPos, 0);
		} else if (size == 1) {
			BlockPos origin = pos.above(arrivedPos);
			this.setPoiseLog(world, origin, rand, false, true);
			this.setPoiseLog(world, origin.north(), rand, false, true);
			this.setPoiseLog(world, origin.east(), rand, false, true);
			this.setPoiseLog(world, origin.south(), rand, false, true);
			this.setPoiseLog(world, origin.west(), rand, false, true);

			for (int x = origin.getX() - 2; x <= origin.getX() + 2; x++) {
				for (int z = origin.getZ() - 1; z <= origin.getZ() + 1; z++) {
					this.setPoiseLog(world, new BlockPos(x, origin.above().getY(), z), rand, false, true);
				}
			}
			for (int x = origin.getX() - 1; x <= origin.getX() + 1; x++) {
				for (int z = origin.getZ() - 2; z <= origin.getZ() + 2; z++) {
					this.setPoiseLog(world, new BlockPos(x, origin.above().getY(), z), rand, false, true);
				}
			}

			this.setPoiseLog(world, origin.above(2).north(3), rand, false, true);
			this.setPoiseLog(world, origin.above(2).north(3).west(), rand, false, true);
			this.setPoiseLog(world, origin.above(2).north(3).east(), rand, false, true);

			this.setPoiseLog(world, origin.above(2).east(3), rand, false, true);
			this.setPoiseLog(world, origin.above(2).east(3).north(), rand, false, true);
			this.setPoiseLog(world, origin.above(2).east(3).south(), rand, false, true);

			this.setPoiseLog(world, origin.above(2).south(3), rand, false, true);
			this.setPoiseLog(world, origin.above(2).south(3).west(), rand, false, true);
			this.setPoiseLog(world, origin.above(2).south(3).east(), rand, false, true);

			this.setPoiseLog(world, origin.above(2).west(3), rand, false, true);
			this.setPoiseLog(world, origin.above(2).west(3).north(), rand, false, true);
			this.setPoiseLog(world, origin.above(2).west(3).south(), rand, false, true);

			this.setPoiseLog(world, origin.above(2).north(2).west(2), rand, false, true);
			this.setPoiseLog(world, origin.above(2).north(2).east(2), rand, false, true);
			this.setPoiseLog(world, origin.above(2).south(2).west(2), rand, false, true);
			this.setPoiseLog(world, origin.above(2).south(2).east(2), rand, false, true);

			for (int x = origin.getX() - 2; x <= origin.getX() + 2; x++) {
				for (int z = origin.getZ() - 1; z <= origin.getZ() + 1; z++) {
					world.setBlock(new BlockPos(x, origin.above(2).getY(), z), POISMOSS_EUMUS.get(), 2);
				}
			}

			for (int x = origin.getX() - 1; x <= origin.getX() + 1; x++) {
				for (int z = origin.getZ() - 2; z <= origin.getZ() + 2; z++) {
					world.setBlock(new BlockPos(x, origin.above(2).getY(), z), POISMOSS_EUMUS.get(), 2);
				}
			}

			this.placeInnerDomeFeatures(world, rand, origin.above(2));

			int[] sideRandValues = new int[]{
					rand.nextInt(3) + 1,
					rand.nextInt(3) + 1,
					rand.nextInt(3) + 1,
					rand.nextInt(3) + 1
			};
			for (int yn = 1; yn <= sideRandValues[0]; yn++) {
				this.setPoiseLog(world, pos.above(arrivedPos).north(4).above(2).above(yn), rand, false, true);
				if (yn == 1 && rand.nextFloat() <= 0.25F) {
					if (rand.nextBoolean()) {
						this.setPoiseLog(world, pos.above(arrivedPos).north(4).east().above(2).above(yn), rand, false, true);
					} else {
						this.setPoiseLog(world, pos.above(arrivedPos).north(4).west().above(2).above(yn), rand, false, true);
					}
				}
			}
			for (int yn = 1; yn <= sideRandValues[1]; yn++) {
				this.setPoiseLog(world, pos.above(arrivedPos).east(4).above(2).above(yn), rand, false, true);
				if (yn == 1 && rand.nextFloat() <= 0.25F) {
					if (rand.nextBoolean()) {
						this.setPoiseLog(world, pos.above(arrivedPos).east(4).north().above(2).above(yn), rand, false, true);
					} else {
						this.setPoiseLog(world, pos.above(arrivedPos).east(4).south().above(2).above(yn), rand, false, true);
					}
				}
			}
			for (int yn = 1; yn <= sideRandValues[2]; yn++) {
				this.setPoiseLog(world, pos.above(arrivedPos).south(4).above(2).above(yn), rand, false, true);
				if (yn == 1 && rand.nextFloat() <= 0.25F) {
					if (rand.nextBoolean()) {
						this.setPoiseLog(world, pos.above(arrivedPos).south(4).east().above(2).above(yn), rand, false, true);
					} else {
						this.setPoiseLog(world, pos.above(arrivedPos).south(4).west().above(2).above(yn), rand, false, true);
					}
				}
			}
			for (int yn = 1; yn <= sideRandValues[3]; yn++) {
				this.setPoiseLog(world, pos.above(arrivedPos).west(4).above(2).above(yn), rand, false, true);
				if (yn == 1 && rand.nextFloat() <= 0.25F) {
					if (rand.nextBoolean()) {
						this.setPoiseLog(world, pos.above(arrivedPos).west(4).north().above(2).above(yn), rand, false, true);
					} else {
						this.setPoiseLog(world, pos.above(arrivedPos).west(4).south().above(2).above(yn), rand, false, true);
					}
				}
			}
			//Corner bits
			boolean[] doCornerFeature = new boolean[]{
					rand.nextInt(4) == 0,
					rand.nextInt(4) == 0,
					rand.nextInt(4) == 0,
					rand.nextInt(4) == 0,
			};
			if (doCornerFeature[0]) {
				BlockPos cornerOrigin = origin.above(3).north(3).west(2);
				if (rand.nextBoolean()) {
					this.setPoiseLog(world, cornerOrigin, rand, false, true);
					this.setPoiseLog(world, cornerOrigin.west(), rand, false, true);
					this.setPoiseLog(world, cornerOrigin.west().above(), rand, false, true);
					this.setPoiseLog(world, cornerOrigin.south().west(), rand, false, true);
				} else {
					this.setPoiseLog(world, cornerOrigin, rand, false, true);
					this.setPoiseLog(world, cornerOrigin.west().above(), rand, false, true);
					if (rand.nextBoolean()) this.setPoiseLog(world, cornerOrigin.west().above(2), rand, false, true);
				}
			}
			if (doCornerFeature[1]) {
				BlockPos cornerOrigin = origin.above(3).east(3).north(2);
				if (rand.nextBoolean()) {
					this.setPoiseLog(world, cornerOrigin, rand, false, true);
					this.setPoiseLog(world, cornerOrigin.north(), rand, false, true);
					this.setPoiseLog(world, cornerOrigin.north().above(), rand, false, true);
					this.setPoiseLog(world, cornerOrigin.west().north(), rand, false, true);
				} else {
					this.setPoiseLog(world, cornerOrigin, rand, false, true);
					this.setPoiseLog(world, cornerOrigin.north().above(), rand, false, true);
					if (rand.nextBoolean()) this.setPoiseLog(world, cornerOrigin.above(2).north(), rand, false, true);
				}
			}
			if (doCornerFeature[2]) {
				BlockPos cornerOrigin = origin.above(3).south(3).east(2);
				if (rand.nextBoolean()) {
					this.setPoiseLog(world, cornerOrigin, rand, false, true);
					this.setPoiseLog(world, cornerOrigin.east(), rand, false, true);
					this.setPoiseLog(world, cornerOrigin.east().above(), rand, false, true);
					this.setPoiseLog(world, cornerOrigin.north().east(), rand, false, true);
				} else {
					this.setPoiseLog(world, cornerOrigin, rand, false, true);
					this.setPoiseLog(world, cornerOrigin.east().above(), rand, false, true);
					if (rand.nextBoolean()) this.setPoiseLog(world, cornerOrigin.above(2).east(), rand, false, true);
				}
			}
			if (doCornerFeature[3]) {
				BlockPos cornerOrigin = origin.above(3).west(3).south(2);
				if (rand.nextBoolean()) {
					this.setPoiseLog(world, cornerOrigin, rand, false, true);
					this.setPoiseLog(world, cornerOrigin.south(), rand, false, true);
					this.setPoiseLog(world, cornerOrigin.south().above(), rand, false, true);
					this.setPoiseLog(world, cornerOrigin.east().south(), rand, false, true);
				} else {
					this.setPoiseLog(world, cornerOrigin, rand, false, true);
					this.setPoiseLog(world, cornerOrigin.above().south(), rand, false, true);
					if (rand.nextBoolean()) this.setPoiseLog(world, cornerOrigin.above(2).south(), rand, false, true);
				}
			}
			this.addTreeDomeTop(world, pos, rand, arrivedPos, 1);
		} else {
			BlockPos origin = pos.above(arrivedPos);
			this.setPoiseLog(world, origin, rand, false, true);
			if (rand.nextFloat() <= 0.75F) this.setPoiseLog(world, origin.north(), rand, false, true);
			if (rand.nextFloat() <= 0.75F) this.setPoiseLog(world, origin.east(), rand, false, true);
			if (rand.nextFloat() <= 0.75F) this.setPoiseLog(world, origin.south(), rand, false, true);
			if (rand.nextFloat() <= 0.75F) this.setPoiseLog(world, origin.west(), rand, false, true);

			GenerationUtils.fillWithRandomTwoBlocksCube(world, origin.above().north().west().getX(), origin.above().north().west().getY(), origin.above().north().west().getZ(), origin.above(2).east().south().getX(), origin.above(2).east().south().getY(), origin.above(2).east().south().getZ(), rand, POISE_STEM.get(), GLOWING_POISE_STEM.get(), 0.10F);

			for (int x = origin.getX() - 1; x <= origin.getX() + 1; x++) {
				for (int z = origin.getZ() - 3; z <= origin.getZ() + 3; z++) {
					this.setPoiseLog(world, new BlockPos(x, origin.above(3).getY(), z), rand, false, true);
				}
			}
			for (int x = origin.getX() - 3; x <= origin.getX() + 3; x++) {
				for (int z = origin.getZ() - 1; z <= origin.getZ() + 1; z++) {
					this.setPoiseLog(world, new BlockPos(x, origin.above(3).getY(), z), rand, false, true);
				}
			}
			this.setPoiseLog(world, origin.above(3).north(2).east(2), rand, false, true);
			this.setPoiseLog(world, origin.above(3).north(2).west(2), rand, false, true);
			this.setPoiseLog(world, origin.above(3).south(2).east(2), rand, false, true);
			this.setPoiseLog(world, origin.above(3).south(2).west(2), rand, false, true);

			for (int x = origin.getX() - 1; x <= origin.getX() + 1; x++) {
				for (int z = origin.getZ() - 5; z <= origin.getZ() + 5; z++) {
					this.setPoiseLog(world, new BlockPos(x, origin.above(4).getY(), z), rand, false, true);
				}
			}
			for (int x = origin.getX() - 5; x <= origin.getX() + 5; x++) {
				for (int z = origin.getZ() - 1; z <= origin.getZ() + 1; z++) {
					this.setPoiseLog(world, new BlockPos(x, origin.above(4).getY(), z), rand, false, true);
				}
			}
			this.setPoiseLog(world, origin.above(4).north(3).east(2), rand, false, true);
			this.setPoiseLog(world, origin.above(4).north(4).east(2), rand, false, true);
			this.setPoiseLog(world, origin.above(4).north(3).east(3), rand, false, true);
			this.setPoiseLog(world, origin.above(4).north(2).east(3), rand, false, true);
			this.setPoiseLog(world, origin.above(4).north(2).east(4), rand, false, true);

			world.setBlock(origin.above(4).north(2).east(2), POISMOSS_EUMUS.get(), 2);

			this.setPoiseLog(world, origin.above(4).east(3).south(2), rand, false, true);
			this.setPoiseLog(world, origin.above(4).east(4).south(2), rand, false, true);
			this.setPoiseLog(world, origin.above(4).east(3).south(3), rand, false, true);
			this.setPoiseLog(world, origin.above(4).east(2).south(3), rand, false, true);
			this.setPoiseLog(world, origin.above(4).east(2).south(4), rand, false, true);

			world.setBlock(origin.above(4).east(2).south(2), POISMOSS_EUMUS.get(), 2);

			this.setPoiseLog(world, origin.above(4).south(3).west(2), rand, false, true);
			this.setPoiseLog(world, origin.above(4).south(4).west(2), rand, false, true);
			this.setPoiseLog(world, origin.above(4).south(3).west(3), rand, false, true);
			this.setPoiseLog(world, origin.above(4).south(2).west(3), rand, false, true);
			this.setPoiseLog(world, origin.above(4).south(2).west(4), rand, false, true);

			world.setBlock(origin.above(4).south(2).west(2), POISMOSS_EUMUS.get(), 2);

			this.setPoiseLog(world, origin.above(4).west(3).north(2), rand, false, true);
			this.setPoiseLog(world, origin.above(4).west(4).north(2), rand, false, true);
			this.setPoiseLog(world, origin.above(4).west(3).north(3), rand, false, true);
			this.setPoiseLog(world, origin.above(4).west(2).north(3), rand, false, true);
			this.setPoiseLog(world, origin.above(4).west(2).north(4), rand, false, true);

			world.setBlock(origin.above(4).west(2).north(2), POISMOSS_EUMUS.get(), 2);

			for (int x = origin.getX() - 1; x <= origin.getX() + 1; x++) {
				for (int z = origin.getZ() - 3; z <= origin.getZ() + 3; z++) {
					world.setBlock(new BlockPos(x, origin.above(4).getY(), z), POISMOSS_EUMUS.get(), 2);
				}
			}

			for (int x = origin.getX() - 3; x <= origin.getX() + 3; x++) {
				for (int z = origin.getZ() - 1; z <= origin.getZ() + 1; z++) {
					world.setBlock(new BlockPos(x, origin.above(4).getY(), z), POISMOSS_EUMUS.get(), 2);
				}
			}

			this.placeInnerDomeFeatures(world, rand, origin.above(4));

			this.setPoiseLog(world, origin.above(5).north(5), rand, false, true);
			this.setPoiseLog(world, origin.above(5).north(5).east(), rand, false, true);
			this.setPoiseLog(world, origin.above(5).north(5).east(2), rand, false, true);
			this.setPoiseLog(world, origin.above(5).north(5).west(), rand, false, true);
			this.setPoiseLog(world, origin.above(5).north(5).west(2), rand, false, true);

			this.setPoiseLog(world, origin.above(5).east(5), rand, false, true);
			this.setPoiseLog(world, origin.above(5).east(5).north(), rand, false, true);
			this.setPoiseLog(world, origin.above(5).east(5).north(2), rand, false, true);
			this.setPoiseLog(world, origin.above(5).east(5).south(), rand, false, true);
			this.setPoiseLog(world, origin.above(5).east(5).south(2), rand, false, true);

			this.setPoiseLog(world, origin.above(5).south(5), rand, false, true);
			this.setPoiseLog(world, origin.above(5).south(5).west(), rand, false, true);
			this.setPoiseLog(world, origin.above(5).south(5).west(2), rand, false, true);
			this.setPoiseLog(world, origin.above(5).south(5).east(), rand, false, true);
			this.setPoiseLog(world, origin.above(5).south(5).east(2), rand, false, true);

			this.setPoiseLog(world, origin.above(5).west(5), rand, false, true);
			this.setPoiseLog(world, origin.above(5).west(5).south(), rand, false, true);
			this.setPoiseLog(world, origin.above(5).west(5).south(2), rand, false, true);
			this.setPoiseLog(world, origin.above(5).west(5).north(), rand, false, true);
			this.setPoiseLog(world, origin.above(5).west(5).north(2), rand, false, true);

			this.setPoiseLog(world, origin.above(5).north(4).west(3), rand, false, true);
			this.setPoiseLog(world, origin.above(5).north(3).west(4), rand, false, true);
			this.setPoiseLog(world, origin.above(5).north(4).east(3), rand, false, true);
			this.setPoiseLog(world, origin.above(5).north(3).east(4), rand, false, true);
			this.setPoiseLog(world, origin.above(5).south(4).west(3), rand, false, true);
			this.setPoiseLog(world, origin.above(5).south(3).west(4), rand, false, true);
			this.setPoiseLog(world, origin.above(5).south(4).east(3), rand, false, true);
			this.setPoiseLog(world, origin.above(5).south(3).east(4), rand, false, true);

			int[] sideRandValues = new int[]{
					rand.nextInt(3) + 1,
					rand.nextInt(3) + 1,
					rand.nextInt(3) + 1,
					rand.nextInt(3) + 1
			};
			for (int yn = 1; yn <= sideRandValues[0]; yn++) {
				this.setPoiseLog(world, pos.above(arrivedPos).north(6).above(5).above(yn), rand, false, true);
				if (yn == 1 && rand.nextFloat() <= 0.25F) {
					if (rand.nextBoolean()) {
						this.setPoiseLog(world, pos.above(arrivedPos).north(6).west().above(5).above(yn), rand, false, true);
					} else {
						this.setPoiseLog(world, pos.above(arrivedPos).north(6).east().above(5).above(yn), rand, false, true);
					}
				}
			}
			for (int yn = 1; yn <= sideRandValues[1]; yn++) {
				this.setPoiseLog(world, pos.above(arrivedPos).east(6).above(5).above(yn), rand, false, true);
				if (yn == 1 && rand.nextFloat() <= 0.25F) {
					if (rand.nextBoolean()) {
						this.setPoiseLog(world, pos.above(arrivedPos).east(6).north().above(5).above(yn), rand, false, true);
					} else {
						this.setPoiseLog(world, pos.above(arrivedPos).east(6).south().above(5).above(yn), rand, false, true);
					}
				}
			}
			for (int yn = 1; yn <= sideRandValues[2]; yn++) {
				this.setPoiseLog(world, pos.above(arrivedPos).south(6).above(5).above(yn), rand, false, true);
				if (yn == 1 && rand.nextFloat() <= 0.25F) {
					if (rand.nextBoolean()) {
						this.setPoiseLog(world, pos.above(arrivedPos).south(6).west().above(5).above(yn), rand, false, true);
					} else {
						this.setPoiseLog(world, pos.above(arrivedPos).south(6).east().above(5).above(yn), rand, false, true);
					}
				}
			}
			for (int yn = 1; yn <= sideRandValues[3]; yn++) {
				this.setPoiseLog(world, pos.above(arrivedPos).west(6).above(5).above(yn), rand, false, true);
				if (yn == 1 && rand.nextFloat() <= 0.25F) {
					if (rand.nextBoolean()) {
						this.setPoiseLog(world, pos.above(arrivedPos).west(6).south().above(5).above(yn), rand, false, true);
					} else {
						this.setPoiseLog(world, pos.above(arrivedPos).west(6).north().above(5).above(yn), rand, false, true);
					}
				}
			}

			int[] cornerRandValues = new int[]{
					rand.nextFloat() <= 0.75 ? rand.nextInt(3) + 1 : 0,
					rand.nextFloat() <= 0.75 ? rand.nextInt(3) + 1 : 0,
					rand.nextFloat() <= 0.75 ? rand.nextInt(3) + 1 : 0,
					rand.nextFloat() <= 0.75 ? rand.nextInt(3) + 1 : 0
			};
			for (int yn = 1; yn <= cornerRandValues[0]; yn++) {
				this.setPoiseLog(world, pos.above(arrivedPos).north(4).east(4).above(5).above(yn), rand, false, true);
			}
			for (int yn = 1; yn <= cornerRandValues[1]; yn++) {
				this.setPoiseLog(world, pos.above(arrivedPos).east(4).south(4).above(5).above(yn), rand, false, true);
			}
			for (int yn = 1; yn <= cornerRandValues[2]; yn++) {
				this.setPoiseLog(world, pos.above(arrivedPos).south(4).west(4).above(5).above(yn), rand, false, true);
			}
			for (int yn = 1; yn <= cornerRandValues[3]; yn++) {
				this.setPoiseLog(world, pos.above(arrivedPos).west(4).north(4).above(5).above(yn), rand, false, true);
			}
			this.addTreeDomeTop(world, pos, rand, arrivedPos, 2);
		}
	}

	private void addTreeDomeTop(IWorld world, BlockPos pos, Random rand, int arrivedPos, int size) {
		BlockPos origin = pos.above(arrivedPos);
		if (size == 0) {
			//North
			GenerationUtils.fillAreaWithBlockCube(world, origin.north(3).above(2).west().getX(), origin.north(3).above(2).west().getY(), origin.north(3).above(2).east().getZ(), origin.north(3).above(2).east().getX(), origin.north(3).above(6).east().getY(), origin.north(3).above(2).east().getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.north(3).above(3).west(2).getX(), origin.north(3).above(3).west(2).getY(), origin.north(3).above(3).west(2).getZ(), origin.north(3).above(3).west(2).getX(), origin.north(3).above(5).west(2).getY(), origin.north(3).above(3).west(2).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.north(3).above(3).east(2).getX(), origin.north(3).above(3).east(2).getY(), origin.north(3).above(3).east(2).getZ(), origin.north(3).above(3).east(2).getX(), origin.north(3).above(5).east(2).getY(), origin.north(3).above(3).east(2).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			//East
			GenerationUtils.fillAreaWithBlockCube(world, origin.east(3).above(2).north().getX(), origin.east(3).above(2).north().getY(), origin.east(3).above(2).north().getZ(), origin.east(3).above(2).south().getX(), origin.east(3).above(6).south().getY(), origin.east(3).above(2).south().getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.east(3).above(3).north(2).getX(), origin.east(3).above(3).north(2).getY(), origin.east(3).above(3).north(2).getZ(), origin.east(3).above(3).north(2).getX(), origin.east(3).above(5).north(2).getY(), origin.east(3).above(3).north(2).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.east(3).above(3).south(2).getX(), origin.east(3).above(3).south(2).getY(), origin.east(3).above(3).south(2).getZ(), origin.east(3).above(3).south(2).getX(), origin.east(3).above(5).south(2).getY(), origin.east(3).above(3).south(2).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			//South
			GenerationUtils.fillAreaWithBlockCube(world, origin.south(3).above(2).west().getX(), origin.south(3).above(2).west().getY(), origin.south(3).above(2).west().getZ(), origin.south(3).above(2).east().getX(), origin.south(3).above(6).east().getY(), origin.south(3).above(2).east().getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.south(3).above(3).west(2).getX(), origin.south(3).above(3).west(2).getY(), origin.south(3).above(3).west(2).getZ(), origin.south(3).above(3).west(2).getX(), origin.south(3).above(5).west(2).getY(), origin.south(3).above(3).west(2).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.south(3).above(3).east(2).getX(), origin.south(3).above(3).east(2).getY(), origin.south(3).above(3).east(2).getZ(), origin.south(3).above(3).east(2).getX(), origin.south(3).above(5).east(2).getY(), origin.south(3).above(3).east(2).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			//West
			GenerationUtils.fillAreaWithBlockCube(world, origin.west(3).above(2).north().getX(), origin.west(3).above(2).north().getY(), origin.west(3).above(2).north().getZ(), origin.west(3).above(2).south().getX(), origin.west(3).above(6).south().getY(), origin.west(3).above(2).south().getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.west(3).above(3).north(2).getX(), origin.west(3).above(3).north(2).getY(), origin.west(3).above(3).north(2).getZ(), origin.west(3).above(3).north(2).getX(), origin.west(3).above(5).north(2).getY(), origin.west(3).above(3).north(2).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.west(3).above(3).south(2).getX(), origin.west(3).above(3).south(2).getY(), origin.west(3).above(3).south(2).getZ(), origin.west(3).above(3).south(2).getX(), origin.west(3).above(5).south(2).getY(), origin.west(3).above(3).south(2).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			//Corners
			this.setPoiseCluster(world, origin.north(2).above(2).west(2));
			this.setPoiseCluster(world, origin.north(2).above(2).east(2));
			this.setPoiseCluster(world, origin.north(2).above(6).west(2));
			this.setPoiseCluster(world, origin.north(2).above(6).east(2));

			this.setPoiseCluster(world, origin.east(2).above(2).north(2));
			this.setPoiseCluster(world, origin.east(2).above(2).south(2));
			this.setPoiseCluster(world, origin.east(2).above(6).north(2));
			this.setPoiseCluster(world, origin.east(2).above(6).south(2));

			this.setPoiseCluster(world, origin.south(2).above(2).east(2));
			this.setPoiseCluster(world, origin.south(2).above(2).west(2));
			this.setPoiseCluster(world, origin.south(2).above(6).east(2));
			this.setPoiseCluster(world, origin.south(2).above(6).west(2));

			this.setPoiseCluster(world, origin.west(2).above(2).south(2));
			this.setPoiseCluster(world, origin.west(2).above(2).north(2));
			this.setPoiseCluster(world, origin.west(2).above(6).south(2));
			this.setPoiseCluster(world, origin.west(2).above(6).north(2));

			//Top
			GenerationUtils.fillAreaWithBlockCube(world, origin.north().west(2).above(7).getX(), origin.north().west(2).above(7).getY(), origin.north().west(2).above(7).getZ(), origin.south().east(2).above(7).getX(), origin.south().east(2).above(7).getY(), origin.south().east(2).above(7).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			this.setPoiseCluster(world, origin.north(2).west().above(7));
			this.setPoiseCluster(world, origin.north(2).above(7));
			this.setPoiseCluster(world, origin.north(2).east().above(7));
			this.setPoiseCluster(world, origin.south(2).west().above(7));
			this.setPoiseCluster(world, origin.south(2).above(7));
			this.setPoiseCluster(world, origin.south(2).east().above(7));
		} else if (size == 1) {
			//North
			GenerationUtils.fillAreaWithBlockCube(world, origin.north(4).above(3).west().getX(), origin.north(4).above(3).west().getY(), origin.north(4).above(3).east().getZ(), origin.north(4).above(3).east().getX(), origin.north(4).above(7).east().getY(), origin.north(4).above(3).east().getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.north(4).above(4).west(2).getX(), origin.north(4).above(4).west(2).getY(), origin.north(4).above(4).west(2).getZ(), origin.north(4).above(4).west(2).getX(), origin.north(4).above(6).west(2).getY(), origin.north(4).above(4).west(2).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.north(4).above(4).east(2).getX(), origin.north(4).above(4).east(2).getY(), origin.north(4).above(4).east(2).getZ(), origin.north(4).above(4).east(2).getX(), origin.north(4).above(6).east(2).getY(), origin.north(4).above(4).east(2).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			//East
			GenerationUtils.fillAreaWithBlockCube(world, origin.east(4).above(3).north().getX(), origin.east(4).above(3).north().getY(), origin.east(4).above(3).north().getZ(), origin.east(4).above(3).south().getX(), origin.east(4).above(7).south().getY(), origin.east(4).above(3).south().getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.east(4).above(4).north(2).getX(), origin.east(4).above(4).north(2).getY(), origin.east(4).above(4).north(2).getZ(), origin.east(4).above(4).north(2).getX(), origin.east(4).above(6).north(2).getY(), origin.east(4).above(4).north(2).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.east(4).above(4).south(2).getX(), origin.east(4).above(4).south(2).getY(), origin.east(4).above(4).south(2).getZ(), origin.east(4).above(4).south(2).getX(), origin.east(4).above(6).south(2).getY(), origin.east(4).above(4).south(2).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			//South
			GenerationUtils.fillAreaWithBlockCube(world, origin.south(4).above(3).west().getX(), origin.south(4).above(3).west().getY(), origin.south(4).above(3).west().getZ(), origin.south(4).above(3).east().getX(), origin.south(4).above(7).east().getY(), origin.south(4).above(3).east().getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.south(4).above(4).west(2).getX(), origin.south(4).above(4).west(2).getY(), origin.south(4).above(4).west(2).getZ(), origin.south(4).above(4).west(2).getX(), origin.south(4).above(6).west(2).getY(), origin.south(4).above(4).west(2).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.south(4).above(4).east(2).getX(), origin.south(4).above(4).east(2).getY(), origin.south(4).above(4).east(2).getZ(), origin.south(4).above(4).east(2).getX(), origin.south(4).above(6).east(2).getY(), origin.south(4).above(4).east(2).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			//West
			GenerationUtils.fillAreaWithBlockCube(world, origin.west(4).above(3).north().getX(), origin.west(4).above(3).north().getY(), origin.west(4).above(3).north().getZ(), origin.west(4).above(3).south().getX(), origin.west(4).above(7).south().getY(), origin.west(4).above(3).south().getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.west(4).above(4).north(2).getX(), origin.west(4).above(4).north(2).getY(), origin.west(4).above(4).north(2).getZ(), origin.west(4).above(4).north(2).getX(), origin.west(4).above(6).north(2).getY(), origin.west(4).above(4).north(2).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.west(4).above(4).south(2).getX(), origin.west(4).above(4).south(2).getY(), origin.west(4).above(4).south(2).getZ(), origin.west(4).above(4).south(2).getX(), origin.west(4).above(6).south(2).getY(), origin.west(4).above(4).south(2).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			//Corners
			this.setPoiseCluster(world, origin.above(3).north(3).west(2));
			this.setPoiseCluster(world, origin.above(3).north(3).west(3));
			this.setPoiseCluster(world, origin.above(3).north(2).west(3));
			this.setPoiseCluster(world, origin.above(4).north(3).west(3));
			this.setPoiseCluster(world, origin.above(5).north(3).west(3));
			this.setPoiseCluster(world, origin.above(6).north(3).west(3));
			this.setPoiseCluster(world, origin.above(7).north(3).west(2));
			this.setPoiseCluster(world, origin.above(7).north(3).west(3));
			this.setPoiseCluster(world, origin.above(7).north(2).west(3));

			this.setPoiseCluster(world, origin.above(3).east(3).north(2));
			this.setPoiseCluster(world, origin.above(3).east(3).north(3));
			this.setPoiseCluster(world, origin.above(3).east(2).north(3));
			this.setPoiseCluster(world, origin.above(4).east(3).north(3));
			this.setPoiseCluster(world, origin.above(5).east(3).north(3));
			this.setPoiseCluster(world, origin.above(6).east(3).north(3));
			this.setPoiseCluster(world, origin.above(7).east(3).north(2));
			this.setPoiseCluster(world, origin.above(7).east(3).north(3));
			this.setPoiseCluster(world, origin.above(7).east(2).north(3));

			this.setPoiseCluster(world, origin.above(3).south(3).east(2));
			this.setPoiseCluster(world, origin.above(3).south(3).east(3));
			this.setPoiseCluster(world, origin.above(3).south(2).east(3));
			this.setPoiseCluster(world, origin.above(4).south(3).east(3));
			this.setPoiseCluster(world, origin.above(5).south(3).east(3));
			this.setPoiseCluster(world, origin.above(6).south(3).east(3));
			this.setPoiseCluster(world, origin.above(7).south(3).east(2));
			this.setPoiseCluster(world, origin.above(7).south(3).east(3));
			this.setPoiseCluster(world, origin.above(7).south(2).east(3));

			this.setPoiseCluster(world, origin.above(3).west(3).south(2));
			this.setPoiseCluster(world, origin.above(3).west(3).south(3));
			this.setPoiseCluster(world, origin.above(3).west(2).south(3));
			this.setPoiseCluster(world, origin.above(4).west(3).south(3));
			this.setPoiseCluster(world, origin.above(5).west(3).south(3));
			this.setPoiseCluster(world, origin.above(6).west(3).south(3));
			this.setPoiseCluster(world, origin.above(7).west(3).south(2));
			this.setPoiseCluster(world, origin.above(7).west(3).south(3));
			this.setPoiseCluster(world, origin.above(7).west(2).south(3));

			//Top
			this.setPoiseCluster(world, origin.above(8).west(2).south(2));
			this.setPoiseCluster(world, origin.above(8).south(2).east(2));
			this.setPoiseCluster(world, origin.above(8).east(2).north(2));
			this.setPoiseCluster(world, origin.above(8).north(2).west(2));

			this.setPoiseCluster(world, origin.above(8).north(3).west());
			this.setPoiseCluster(world, origin.above(8).north(3));
			this.setPoiseCluster(world, origin.above(8).north(3).east());

			this.setPoiseCluster(world, origin.above(8).east(3).north());
			this.setPoiseCluster(world, origin.above(8).east(3));
			this.setPoiseCluster(world, origin.above(8).east(3).south());

			this.setPoiseCluster(world, origin.above(8).south(3).east());
			this.setPoiseCluster(world, origin.above(8).south(3));
			this.setPoiseCluster(world, origin.above(8).south(3).west());

			this.setPoiseCluster(world, origin.above(8).west(3).south());
			this.setPoiseCluster(world, origin.above(8).west(3));
			this.setPoiseCluster(world, origin.above(8).west(3).north());

			GenerationUtils.fillAreaWithBlockCube(world, origin.north().west(2).above(9).getX(), origin.north().west(2).above(9).getY(), origin.north().west(2).above(9).getZ(), origin.south().east(2).above(9).getX(), origin.south().east(2).above(9).getY(), origin.south().east(2).above(9).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			this.setPoiseCluster(world, origin.north(2).west().above(9));
			this.setPoiseCluster(world, origin.north(2).above(9));
			this.setPoiseCluster(world, origin.north(2).east().above(9));
			this.setPoiseCluster(world, origin.south(2).west().above(9));
			this.setPoiseCluster(world, origin.south(2).above(9));
			this.setPoiseCluster(world, origin.south(2).east().above(9));
		} else {
			//North
			GenerationUtils.fillAreaWithBlockCube(world, origin.north(6).above(6).west().getX(), origin.north(6).above(6).west().getY(), origin.north(6).above(6).west().getZ(), origin.north(6).above(8).east().getX(), origin.north(6).above(8).west().getY(), origin.north(6).above(8).west().getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.north(5).above(6).west(3).getX(), origin.north(5).above(6).west(3).getY(), origin.north(5).above(6).west(3).getZ(), origin.north(5).above(8).west(2).getX(), origin.north(5).above(8).west(2).getY(), origin.north(5).above(8).west(2).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.north(5).above(6).east(2).getX(), origin.north(5).above(6).east(2).getY(), origin.north(5).above(6).east(2).getZ(), origin.north(5).above(8).east(3).getX(), origin.north(5).above(8).east(3).getY(), origin.north(5).above(8).east(3).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.north(5).above(9).west(2).getX(), origin.north(5).above(9).west(2).getY(), origin.north(5).above(9).west(2).getZ(), origin.north(5).above(9).east(2).getX(), origin.north(5).above(9).east(2).getY(), origin.north(5).above(9).east(2).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.north(5).above(10).west().getX(), origin.north(5).above(10).west().getY(), origin.north(5).above(10).west().getZ(), origin.north(5).above(10).east().getX(), origin.north(5).above(10).east().getY(), origin.north(5).above(10).east().getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			//East
			GenerationUtils.fillAreaWithBlockCube(world, origin.east(6).above(6).north().getX(), origin.east(6).above(6).north().getY(), origin.east(6).above(6).north().getZ(), origin.east(6).above(8).south().getX(), origin.east(6).above(8).south().getY(), origin.east(6).above(8).south().getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.east(5).above(6).north(3).getX(), origin.east(5).above(6).north(3).getY(), origin.east(5).above(6).north(3).getZ(), origin.east(5).above(8).north(2).getX(), origin.east(5).above(8).north(2).getY(), origin.east(5).above(8).north(2).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.east(5).above(6).south(2).getX(), origin.east(5).above(6).south(2).getY(), origin.east(5).above(6).south(2).getZ(), origin.east(5).above(8).south(3).getX(), origin.east(5).above(8).south(3).getY(), origin.east(5).above(8).south(3).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.east(5).above(9).south(2).getX(), origin.east(5).above(9).north(2).getY(), origin.east(5).above(9).north(2).getZ(), origin.east(5).above(9).south(2).getX(), origin.east(5).above(9).south(2).getY(), origin.east(5).above(9).south(2).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.east(5).above(10).north().getX(), origin.east(5).above(10).north().getY(), origin.east(5).above(10).north().getZ(), origin.east(5).above(10).south().getX(), origin.east(5).above(10).south().getY(), origin.east(5).above(10).south().getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			//South
			GenerationUtils.fillAreaWithBlockCube(world, origin.south(6).above(6).west().getX(), origin.south(6).above(6).west().getY(), origin.south(6).above(6).west().getZ(), origin.south(6).above(8).east().getX(), origin.south(6).above(8).east().getY(), origin.south(6).above(8).east().getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.south(5).above(6).west(3).getX(), origin.south(5).above(6).west(3).getY(), origin.south(5).above(6).west(3).getZ(), origin.south(5).above(8).west(2).getX(), origin.south(5).above(8).north(2).getY(), origin.south(5).above(8).west(2).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.south(5).above(6).east(2).getX(), origin.south(5).above(6).east(2).getY(), origin.south(5).above(6).east(2).getZ(), origin.south(5).above(8).east(3).getX(), origin.south(5).above(8).east(3).getY(), origin.south(5).above(8).east(3).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.south(5).above(9).west(2).getX(), origin.south(5).above(9).west(2).getY(), origin.south(5).above(9).west(2).getZ(), origin.south(5).above(9).east(2).getX(), origin.south(5).above(9).east(2).getY(), origin.south(5).above(9).east(2).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.south(5).above(10).west().getX(), origin.south(5).above(10).west().getY(), origin.south(5).above(10).west().getZ(), origin.south(5).above(10).east().getX(), origin.south(5).above(10).east().getY(), origin.south(5).above(10).east().getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			//West
			GenerationUtils.fillAreaWithBlockCube(world, origin.west(6).above(6).north().getX(), origin.west(6).above(6).north().getY(), origin.west(6).above(6).north().getZ(), origin.west(6).above(8).south().getX(), origin.west(6).above(8).south().getY(), origin.west(6).above(8).south().getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.west(5).above(6).north(3).getX(), origin.west(5).above(6).north(3).getY(), origin.west(5).above(6).north(3).getZ(), origin.west(5).above(8).north(2).getX(), origin.west(5).above(8).north(2).getY(), origin.west(5).above(8).north(2).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.west(5).above(6).south(2).getX(), origin.west(5).above(6).south(2).getY(), origin.west(5).above(6).south(2).getZ(), origin.west(5).above(8).south(3).getX(), origin.west(5).above(8).south(3).getY(), origin.west(5).above(8).south(3).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.west(5).above(9).north(2).getX(), origin.west(5).above(9).north(2).getY(), origin.west(5).above(9).north(2).getZ(), origin.west(5).above(9).south(2).getX(), origin.west(5).above(9).south(2).getY(), origin.west(5).above(9).south(2).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.west(5).above(10).north().getX(), origin.west(5).above(10).north().getY(), origin.west(5).above(10).north().getZ(), origin.west(5).above(10).south().getX(), origin.west(5).above(10).south().getY(), origin.west(5).above(10).south().getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());


			//Corners
			this.setPoiseCluster(world, origin.above(6).north(4).west(4));
			this.setPoiseCluster(world, origin.above(7).north(4).west(4));
			this.setPoiseCluster(world, origin.above(8).north(4).west(4));
			this.setPoiseCluster(world, origin.above(9).north(4).west(4));
			this.setPoiseCluster(world, origin.above(9).north(4).west(3));
			this.setPoiseCluster(world, origin.above(10).north(4).west(3));
			this.setPoiseCluster(world, origin.above(10).north(4).west(2));
			this.setPoiseCluster(world, origin.above(9).north(3).west(4));
			this.setPoiseCluster(world, origin.above(10).north(3).west(4));
			this.setPoiseCluster(world, origin.above(10).north(2).west(4));

			this.setPoiseCluster(world, origin.above(6).east(4).north(4));
			this.setPoiseCluster(world, origin.above(7).east(4).north(4));
			this.setPoiseCluster(world, origin.above(8).east(4).north(4));
			this.setPoiseCluster(world, origin.above(9).east(4).north(4));
			this.setPoiseCluster(world, origin.above(9).east(4).north(3));
			this.setPoiseCluster(world, origin.above(10).east(4).north(3));
			this.setPoiseCluster(world, origin.above(10).east(4).north(2));
			this.setPoiseCluster(world, origin.above(9).east(3).north(4));
			this.setPoiseCluster(world, origin.above(10).east(3).north(4));
			this.setPoiseCluster(world, origin.above(10).east(2).north(4));

			this.setPoiseCluster(world, origin.above(6).south(4).east(4));
			this.setPoiseCluster(world, origin.above(7).south(4).east(4));
			this.setPoiseCluster(world, origin.above(8).south(4).east(4));
			this.setPoiseCluster(world, origin.above(9).south(4).east(4));
			this.setPoiseCluster(world, origin.above(9).south(4).east(3));
			this.setPoiseCluster(world, origin.above(10).south(4).east(3));
			this.setPoiseCluster(world, origin.above(10).south(4).east(2));
			this.setPoiseCluster(world, origin.above(9).south(3).east(4));
			this.setPoiseCluster(world, origin.above(10).south(3).east(4));
			this.setPoiseCluster(world, origin.above(10).south(2).east(4));

			this.setPoiseCluster(world, origin.above(6).west(4).south(4));
			this.setPoiseCluster(world, origin.above(7).west(4).south(4));
			this.setPoiseCluster(world, origin.above(8).west(4).south(4));
			this.setPoiseCluster(world, origin.above(9).west(4).south(4));
			this.setPoiseCluster(world, origin.above(9).west(4).south(3));
			this.setPoiseCluster(world, origin.above(10).west(4).south(3));
			this.setPoiseCluster(world, origin.above(10).west(4).south(2));
			this.setPoiseCluster(world, origin.above(9).west(3).south(4));
			this.setPoiseCluster(world, origin.above(10).west(3).south(4));
			this.setPoiseCluster(world, origin.above(10).west(2).south(4));

			//Top
			this.setPoiseCluster(world, origin.above(11).north(3).west(3));
			this.setPoiseCluster(world, origin.above(11).north(2).west(3));
			this.setPoiseCluster(world, origin.above(11).north(2).west(4));
			this.setPoiseCluster(world, origin.above(11).north(3).west(2));
			this.setPoiseCluster(world, origin.above(11).north(4).west(2));

			this.setPoiseCluster(world, origin.above(11).north(4).west());
			this.setPoiseCluster(world, origin.above(11).north(4));
			this.setPoiseCluster(world, origin.above(11).north(4).east());

			this.setPoiseCluster(world, origin.above(11).east(3).north(3));
			this.setPoiseCluster(world, origin.above(11).east(2).north(3));
			this.setPoiseCluster(world, origin.above(11).east(2).north(4));
			this.setPoiseCluster(world, origin.above(11).east(3).north(2));
			this.setPoiseCluster(world, origin.above(11).east(4).north(2));

			this.setPoiseCluster(world, origin.above(11).east(4).north());
			this.setPoiseCluster(world, origin.above(11).east(4));
			this.setPoiseCluster(world, origin.above(11).east(4).south());

			this.setPoiseCluster(world, origin.above(11).south(3).east(3));
			this.setPoiseCluster(world, origin.above(11).south(2).east(3));
			this.setPoiseCluster(world, origin.above(11).south(2).east(4));
			this.setPoiseCluster(world, origin.above(11).south(3).east(2));
			this.setPoiseCluster(world, origin.above(11).south(4).east(2));

			this.setPoiseCluster(world, origin.above(11).south(4).east());
			this.setPoiseCluster(world, origin.above(11).south(4));
			this.setPoiseCluster(world, origin.above(11).south(4).west());

			this.setPoiseCluster(world, origin.above(11).west(3).south(3));
			this.setPoiseCluster(world, origin.above(11).west(2).south(3));
			this.setPoiseCluster(world, origin.above(11).west(2).south(4));
			this.setPoiseCluster(world, origin.above(11).west(3).south(2));
			this.setPoiseCluster(world, origin.above(11).west(4).south(2));

			this.setPoiseCluster(world, origin.above(11).west(4).south());
			this.setPoiseCluster(world, origin.above(11).west(4));
			this.setPoiseCluster(world, origin.above(11).west(4).north());

			this.setPoiseCluster(world, origin.above(12).north(3).west());
			this.setPoiseCluster(world, origin.above(12).north(3));
			this.setPoiseCluster(world, origin.above(12).north(3).east());
			this.setPoiseCluster(world, origin.above(12).east(3).north());
			this.setPoiseCluster(world, origin.above(12).east(3));
			this.setPoiseCluster(world, origin.above(12).east(3).south());
			this.setPoiseCluster(world, origin.above(12).south(3).east());
			this.setPoiseCluster(world, origin.above(12).south(3));
			this.setPoiseCluster(world, origin.above(12).south(3).west());
			this.setPoiseCluster(world, origin.above(12).west(3).north());
			this.setPoiseCluster(world, origin.above(12).west(3));
			this.setPoiseCluster(world, origin.above(12).west(3).south());

			for (int x = origin.getX() - 1; x <= origin.getX() + 1; x++) {
				for (int z = origin.getZ() - 1; z <= origin.getZ() + 1; z++) {
					this.setPoiseCluster(world, new BlockPos(x, origin.above(13).getY(), z));
				}
			}
			for (int x = origin.getX() - 2; x <= origin.getX() + 2; x++) {
				for (int z = origin.getZ() - 2; z <= origin.getZ() + 2; z++) {
					if (x == origin.getX() + 2 || z == origin.getZ() + 2 || x == origin.getX() - 2 || z == origin.getZ() - 2) {
						this.setPoiseCluster(world, new BlockPos(x, origin.above(12).getY(), z));
					}
				}
			}
		}
	}

	private void tryToBuildBranch(IWorld world, BlockPos pos, Random rand, int treeHeight) {
		// First array - Values for the amout of blocks in the facing direction, Second Array - Values for the amount of blocks up
		int[] branchLengths = new int[]{
				rand.nextInt(3) + 1,
				rand.nextInt(3) + 1,
				1,
				1
		};
		int[] branchHeights = new int[]{
				1,
				1,
				rand.nextInt(4) + 1,
				rand.nextInt(4) + 4
		};
		Direction randDir = Direction.from3DDataValue(rand.nextInt(4) + 2);
		int pickedHeight = rand.nextInt((int) Math.ceil(treeHeight / 3)) + 6;
		BlockPos pickedPosition = pos.relative(randDir).above(pickedHeight);
		if (world.getBlockState(pickedPosition).getMaterial().isReplaceable() && treeHeight > 15) {
			if (this.isViableBranchArea(world, pickedPosition, randDir, 2 + branchLengths[0] + branchLengths[1], pickedPosition.relative(randDir, branchLengths[0] + branchLengths[1] + branchLengths[2] + branchLengths[3]).above(3 + branchHeights[2]).above(branchHeights[3]))) {
				this.setPoiseLog(world, pickedPosition, rand, false, true);
				this.setPoiseLog(world, pickedPosition.above(), rand, false, true);
				this.setPoiseLog(world, pickedPosition.below(), rand, false, true);

				for (int dir = 0; dir <= branchLengths[0]; dir++) {
					this.setPoiseLogWithDirection(world, pickedPosition.relative(randDir, dir), rand, randDir);
					if (dir == branchLengths[0]) {
						this.setPoiseLog(world, pickedPosition.relative(randDir, dir).above(), rand, false, true);
					}
				}
				for (int dir = 1; dir <= branchLengths[1]; dir++) {
					this.setPoiseLogWithDirection(world, pickedPosition.relative(randDir, dir + branchLengths[0]).above(), rand, randDir);
					if (dir == branchLengths[1]) {
						this.setPoiseLog(world, pickedPosition.relative(randDir, branchLengths[0] + branchLengths[1]).above(2), rand, false, true);
					}
				}
				for (int dir = 1; dir <= branchLengths[2]; dir++) {
					this.setPoiseLogWithDirection(world, pickedPosition.relative(randDir, dir + branchLengths[0] + branchLengths[1]).above(2), rand, randDir);
					if (dir == branchLengths[2]) {
						for (int y = 0; y < branchHeights[2]; y++) {
							this.setPoiseLog(world, pickedPosition.relative(randDir, branchLengths[0] + branchLengths[1] + branchLengths[2]).above(3).above(y), rand, false, false);
						}
					}
				}
				for (int dir = 1; dir <= branchLengths[3]; dir++) {
					this.setPoiseLogWithDirection(world, pickedPosition.relative(randDir, dir + branchLengths[0] + branchLengths[1] + branchLengths[2]).above(2 + branchHeights[2]), rand, randDir);
					if (dir == branchLengths[3]) {
						for (int y = 0; y < branchHeights[3]; y++) {
							this.setPoiseLog(world, pickedPosition.relative(randDir, branchLengths[0] + branchLengths[1] + branchLengths[2] + branchLengths[3]).above(3 + branchHeights[2]).above(y), rand, false, false);
						}
					}
				}
				this.buildTreeTop(world, pickedPosition.relative(randDir, branchLengths[0] + branchLengths[1] + branchLengths[2] + branchLengths[3]).above(3 + branchHeights[2]).above(branchHeights[3]), rand, 0, 0);
			}
		}
	}

	private void buildSideBubble(IWorld world, BlockPos pos, Random rand) {
		int variant = rand.nextInt(100);
		if (variant >= 49) {
			Direction randDir = Direction.from3DDataValue(rand.nextInt(4) + 2);
			this.setPoiseCluster(world, pos.relative(randDir));
		} else if (variant <= 15) {
			world.setBlock(pos.above(), GLOWING_POISE_STEM.get(), 2);
			this.setPoiseCluster(world, pos.north());
			this.setPoiseCluster(world, pos.east());
			this.setPoiseCluster(world, pos.south());
			this.setPoiseCluster(world, pos.west());
			this.setPoiseCluster(world, pos.north().above());
			this.setPoiseCluster(world, pos.east().above());
			this.setPoiseCluster(world, pos.south().above());
			this.setPoiseCluster(world, pos.west().above());
		} else if (variant >= 16) {
			Direction randDir = Direction.from3DDataValue(rand.nextInt(4) + 2);
			if (randDir == Direction.NORTH) {
				world.setBlock(pos.above(), GLOWING_POISE_STEM.get(), 2);
				this.setPoiseCluster(world, pos.north());
				this.setPoiseCluster(world, pos.north().east());
				this.setPoiseCluster(world, pos.east());
				this.setPoiseCluster(world, pos.north().above());
				this.setPoiseCluster(world, pos.north().east().above());
				this.setPoiseCluster(world, pos.east().above());
			} else if (randDir == Direction.EAST) {
				world.setBlock(pos.above(), GLOWING_POISE_STEM.get(), 2);
				this.setPoiseCluster(world, pos.east());
				this.setPoiseCluster(world, pos.east().south());
				this.setPoiseCluster(world, pos.south());
				this.setPoiseCluster(world, pos.east().above());
				this.setPoiseCluster(world, pos.east().south().above());
				this.setPoiseCluster(world, pos.south().above());
			} else if (randDir == Direction.SOUTH) {
				world.setBlock(pos.above(), GLOWING_POISE_STEM.get(), 2);
				this.setPoiseCluster(world, pos.south());
				this.setPoiseCluster(world, pos.south().west());
				this.setPoiseCluster(world, pos.west());
				this.setPoiseCluster(world, pos.south().above());
				this.setPoiseCluster(world, pos.south().west().above());
				this.setPoiseCluster(world, pos.west().above());
			} else if (randDir == Direction.WEST) {
				world.setBlock(pos.above(), GLOWING_POISE_STEM.get(), 2);
				this.setPoiseCluster(world, pos.west());
				this.setPoiseCluster(world, pos.west().north());
				this.setPoiseCluster(world, pos.north());
				this.setPoiseCluster(world, pos.west().above());
				this.setPoiseCluster(world, pos.west().north().above());
				this.setPoiseCluster(world, pos.north().above());
			}
		} else if (variant >= 30) {
			Direction randDir = Direction.from3DDataValue(rand.nextInt(4) + 2);
			if (randDir == Direction.NORTH) {
				world.setBlock(pos.above(), GLOWING_POISE_STEM.get(), 2);
				this.setPoiseCluster(world, pos.north());
				this.setPoiseCluster(world, pos.north().west());
				this.setPoiseCluster(world, pos.north(2).west());
				this.setPoiseCluster(world, pos.north().west().above());
				this.setPoiseCluster(world, pos.north().west().below());
				this.setPoiseCluster(world, pos.north().west(2));
				this.setPoiseCluster(world, pos.west());
			} else if (randDir == Direction.EAST) {
				world.setBlock(pos.above(), GLOWING_POISE_STEM.get(), 2);
				this.setPoiseCluster(world, pos.east());
				this.setPoiseCluster(world, pos.east().north());
				this.setPoiseCluster(world, pos.east(2).north());
				this.setPoiseCluster(world, pos.east().north().above());
				this.setPoiseCluster(world, pos.east().north().below());
				this.setPoiseCluster(world, pos.east().north(2));
				this.setPoiseCluster(world, pos.north());
			} else if (randDir == Direction.SOUTH) {
				world.setBlock(pos.above(), GLOWING_POISE_STEM.get(), 2);
				this.setPoiseCluster(world, pos.south());
				this.setPoiseCluster(world, pos.south().east());
				this.setPoiseCluster(world, pos.south(2).east());
				this.setPoiseCluster(world, pos.south().east().above());
				this.setPoiseCluster(world, pos.south().east().below());
				this.setPoiseCluster(world, pos.south().east(2));
				this.setPoiseCluster(world, pos.east());
			} else if (randDir == Direction.WEST) {
				world.setBlock(pos.above(), GLOWING_POISE_STEM.get(), 2);
				this.setPoiseCluster(world, pos.west());
				this.setPoiseCluster(world, pos.west().south());
				this.setPoiseCluster(world, pos.west(2).south());
				this.setPoiseCluster(world, pos.west().south().above());
				this.setPoiseCluster(world, pos.west().south().below());
				this.setPoiseCluster(world, pos.west().south(2));
				this.setPoiseCluster(world, pos.south());
			}
		}
	}

	public void buildPoismossCircle(IWorld world, IWorldGenerationReader reader, Random random, BlockPos pos) {
		this.placePoismossCircle(world, reader, pos.west().north());
		this.placePoismossCircle(world, reader, pos.east(2).north());
		this.placePoismossCircle(world, reader, pos.west().south(2));
		this.placePoismossCircle(world, reader, pos.east(2).south(2));

		for (int i = 0; i < 5; ++i) {
			int j = random.nextInt(64);
			int k = j % 8;
			int l = j / 8;
			if (k == 0 || k == 7 || l == 0 || l == 7) {
				this.placePoismossCircle(world, reader, pos.offset(-3 + k, 0, -3 + l));
			}
		}
	}

	private void placePoismossCircle(IWorld world, IWorldGenerationReader reader, BlockPos center) {
		for (int i = -2; i <= 2; ++i) {
			for (int j = -2; j <= 2; ++j) {
				if (Math.abs(i) != 2 || Math.abs(j) != 2) {
					this.placePoismossAt(world, reader, center.offset(i, 0, j));
				}
			}
		}
	}

	private void placePoismossAt(IWorld world, IWorldGenerationReader reader, BlockPos pos) {
		for (int i = 2; i >= -3; --i) {
			BlockPos blockpos = pos.above(i);
			if (world.getBlockState(blockpos).getBlock() == EEBlocks.EUMUS.get() || world.getBlockState(blockpos).getBlock() == Blocks.END_STONE) {
				BlockState newGround = world.getBlockState(blockpos).getBlock() == EEBlocks.EUMUS.get() ? POISMOSS_EUMUS.get() : EEBlocks.POISMOSS.get().defaultBlockState();
				this.setBlockState(world, blockpos, newGround);
				break;
			}

			if (!GenerationUtils.isAir(reader, blockpos) && i < 0) {
				break;
			}
		}
	}

	private void placeInnerDomeFeatures(IWorld world, Random rand, BlockPos pos) {
		if (rand.nextFloat() > 0.75) return;

		for (int x = 0; x < 128; x++) {
			BlockPos blockpos = pos.offset(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));
			if (world.isEmptyBlock(blockpos) && EEBlocks.POISE_BUSH.get().defaultBlockState().canSurvive(world, blockpos)) {
				world.setBlock(blockpos, EEBlocks.POISE_BUSH.get().defaultBlockState(), 2);
			}
		}

		for (int x = 0; x < 128; x++) {
			BlockPos blockpos = pos.offset(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));
			if (rand.nextFloat() <= 0.45F && world.isEmptyBlock(blockpos) && world.isEmptyBlock(blockpos.above()) && EEBlocks.TALL_POISE_BUSH.get().defaultBlockState().canSurvive(world, blockpos)) {
				((PoiseTallBushBlock) EEBlocks.TALL_POISE_BUSH.get()).placeAt(world, blockpos, 2);
			}
		}

		for (int x = 0; x < 4; x++) {
			BlockPos randPos = pos.offset(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));
			if ((world.isEmptyBlock(randPos) || world.getBlockState(randPos).getBlock() == EEBlocks.POISE_BUSH.get()) && EEBlocks.BOLLOOM_BUD.get().defaultBlockState().canSurvive(world, randPos)) {
				world.setBlock(randPos, EEBlocks.BOLLOOM_BUD.get().defaultBlockState(), 2);
			}
		}
	}

	public boolean canFitBud(IWorld world, BlockPos pos) {
		for (int y = pos.getY(); y < pos.getY() + 5; y++) {
			for (int x = pos.getX() - 1; x < pos.getX() + 1; x++) {
				for (int z = pos.getZ() - 1; z < pos.getZ() + 1; z++) {
					BlockPos newPos = new BlockPos(x, y, z);
					if (newPos != pos && !world.isEmptyBlock(newPos)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	private void setPoiseLog(IWorld world, BlockPos pos, Random rand, boolean isTreeBase, boolean noBubbles) {
		BlockState logState = rand.nextFloat() <= 0.11F ? GLOWING_POISE_STEM.get() : POISE_STEM.get();
		if (world.getBlockState(pos).getMaterial().isReplaceable() || world.getBlockState(pos).getBlock() == EEBlocks.POISE_CLUSTER.get()) {
			world.setBlock(pos, logState, 2);
			if (!noBubbles && logState == GLOWING_POISE_STEM.get()) {
				if (!isTreeBase) {
					boolean willCollide = world.getBlockState(pos.below()).getBlock() == EEBlocks.GLOWING_POISE_STEM.get() || world.getBlockState(pos.below(2)).getBlock() == EEBlocks.GLOWING_POISE_STEM.get() || world.getBlockState(pos.below(3)).getBlock() == EEBlocks.GLOWING_POISE_STEM.get()
							|| world.getBlockState(pos.above()).getBlock() == EEBlocks.GLOWING_POISE_STEM.get() || world.getBlockState(pos.above(2)).getBlock() == EEBlocks.GLOWING_POISE_STEM.get() || world.getBlockState(pos.above(3)).getBlock() == EEBlocks.GLOWING_POISE_STEM.get();
					if (rand.nextFloat() <= 0.70F && !willCollide
							&& world.getBlockState(pos.north()).getMaterial().isReplaceable() && world.getBlockState(pos.east()).getMaterial().isReplaceable()
							&& world.getBlockState(pos.south()).getMaterial().isReplaceable() && world.getBlockState(pos.west()).getMaterial().isReplaceable())
						this.buildSideBubble(world, pos, rand);
				} else {
					boolean willCollide = world.getBlockState(pos.below()).getBlock() == EEBlocks.GLOWING_POISE_STEM.get() || world.getBlockState(pos.below(2)).getBlock() == EEBlocks.GLOWING_POISE_STEM.get() || world.getBlockState(pos.below(3)).getBlock() == EEBlocks.GLOWING_POISE_STEM.get()
							|| world.getBlockState(pos.above()).getBlock() == EEBlocks.GLOWING_POISE_STEM.get() || world.getBlockState(pos.above(2)).getBlock() == EEBlocks.GLOWING_POISE_STEM.get() || world.getBlockState(pos.above(3)).getBlock() == EEBlocks.GLOWING_POISE_STEM.get();
					if (rand.nextFloat() <= 0.40F && !willCollide) {
						this.buildSideBubble(world, pos, rand);
					}
				}
			}
		}
	}

	private void setPoiseLogWithDirection(IWorld world, BlockPos pos, Random rand, Direction direction) {
		BlockState logState = rand.nextFloat() <= 0.90F ? POISE_STEM.get().setValue(GlowingPoiseStemBlock.AXIS, direction.getAxis()) : GLOWING_POISE_STEM.get().setValue(GlowingPoiseStemBlock.AXIS, direction.getAxis());
		if (world.getBlockState(pos).getMaterial().isReplaceable()) {
			world.setBlock(pos, logState, 2);
		}
	}

	private void setPoiseCluster(IWorld world, BlockPos pos) {
		if (world.getBlockState(pos).getMaterial().isReplaceable()) {
			world.setBlock(pos, EEBlocks.POISE_CLUSTER.get().defaultBlockState(), 2);
		}
	}

	private void setBlockState(IWorld world, BlockPos pos, BlockState state) {
		world.setBlock(pos, state, 2);
	}

	private boolean isViableBranchArea(IWorld world, BlockPos pos, Direction direction, int directionLength, BlockPos topPosition) {
		int y = topPosition.getY() - pos.getY();
		if (direction == Direction.NORTH) {
			if (GenerationUtils.isAreaReplacable(world, pos.below().west().north(directionLength).getX(), pos.below().west().north(directionLength).getY(), pos.below().west().north(directionLength).getZ(), pos.above(y).east().getX(), pos.above(y).east().getY(), pos.above(y).east().getZ())) {
				if (GenerationUtils.isAreaReplacable(world, topPosition.north(3).west(3).getX(), topPosition.north(3).west(3).getY(), topPosition.north(3).west(3).getZ(), topPosition.south(3).east(3).above(7).getX(), topPosition.south(3).east(3).above(7).getY(), topPosition.south(3).east(3).above(7).getZ())) {
					return true;
				}
			}
		} else if (direction == Direction.EAST) {
			if (GenerationUtils.isAreaReplacable(world, pos.below().north().east(directionLength).getX(), pos.below().north().east(directionLength).getY(), pos.below().north().east(directionLength).getZ(), pos.above(y).west().getX(), pos.above(y).west().getY(), pos.above(y).west().getZ())) {
				if (GenerationUtils.isAreaReplacable(world, topPosition.north(3).west(3).getX(), topPosition.north(3).west(3).getY(), topPosition.north(3).west(3).getZ(), topPosition.south(3).east(3).above(7).getX(), topPosition.south(3).east(3).above(7).getY(), topPosition.south(3).east(3).above(7).getZ())) {
					return true;
				}
			}
		} else if (direction == Direction.SOUTH) {
			if (GenerationUtils.isAreaReplacable(world, pos.below().west().getX(), pos.below().west().getY(), pos.below().west().getZ(), pos.above(y).east().south(directionLength).getX(), pos.above(y).east().south(directionLength).getY(), pos.above(y).east().south(directionLength).getZ())) {
				if (GenerationUtils.isAreaReplacable(world, topPosition.north(3).west(3).getX(), topPosition.north(3).west(3).getY(), topPosition.north(3).west(3).getZ(), topPosition.south(3).east(3).above(7).getX(), topPosition.south(3).east(3).above(7).getY(), topPosition.south(3).east(3).above(7).getZ())) {
					return true;
				}
			}
		} else {
			if (GenerationUtils.isAreaReplacable(world, pos.below().north().getX(), pos.below().north().getY(), pos.below().north().getZ(), pos.above(y).south().west(directionLength).getX(), pos.above(y).south().west(directionLength).getY(), pos.above(y).south().west(directionLength).getZ())) {
				if (GenerationUtils.isAreaReplacable(world, topPosition.north(3).west(3).getX(), topPosition.north(3).west(3).getY(), topPosition.north(3).west(3).getZ(), topPosition.south(3).east(3).above(7).getX(), topPosition.south(3).east(3).above(7).getY(), topPosition.south(3).east(3).above(7).getZ())) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isAreaOpen(IWorld world, BlockPos pos) {
		for (int y = pos.getY(); y < pos.getY() + 1; y++) {
			for (int x = pos.getX() - 1; x < pos.getX() + 2; x++) {
				for (int z = pos.getZ() - 1; z < pos.getZ() + 2; z++) {
					if (!world.getBlockState(new BlockPos(x, y, z)).getMaterial().isReplaceable()) {
						return false;
					}
				}
			}
		}
		return true;
	}

	private boolean isValidGround(IWorld world, BlockPos pos) {
		Block block = world.getBlockState(pos).getBlock();
		return block == Blocks.END_STONE.getBlock() || block.is(EETags.Blocks.END_PLANTABLE) || block.is(EETags.Blocks.POISE_PLANTABLE);
	}
}