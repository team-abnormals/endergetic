package com.minecraftabnormals.endergetic.common.world.features.corrock;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.minecraftabnormals.abnormals_core.core.util.GenerationPiece;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.minecraftabnormals.endergetic.api.util.GenerationUtils;
import com.minecraftabnormals.endergetic.common.blocks.CorrockCrownWallBlock;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;

import net.minecraft.block.Block;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.ProbabilityConfig;

public class CorrockTowerFeature extends AbstractCorrockFeature {

	public CorrockTowerFeature(Codec<ProbabilityConfig> config) {
		super(config);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean generate(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, ProbabilityConfig config) {
		Block belowBlock = world.getBlockState(pos.down()).getBlock();
		if (world.isAirBlock(pos) && belowBlock == EEBlocks.CORROCK_END_BLOCK.get()) {
			float chance = rand.nextFloat();
			if (chance > 0.5F) {
				GenerationPiece base = new GenerationPiece((w, p) -> w.isAirBlock(p.pos));
				this.fillUp(base, pos, 3);

				if (!base.canPlace(world)) return false;

				BlockPos downPos = pos.down(2);
				if (GenerationUtils.isAreaCompletelySolid(world, downPos.getX() - 1, downPos.getY(), downPos.getZ() - 1, downPos.getX() + 1, downPos.getY(), downPos.getZ() + 1)) {
					for (int x = downPos.getX() - 1; x <= downPos.getX() + 1; x++) {
						for (int y = downPos.getY(); y <= downPos.getY() + 1; y++) {
							for (int z = downPos.getZ() - 1; z <= downPos.getZ() + 1; z++) {
								BlockPos currentPos = new BlockPos(x, y, z);
								if (world.isAirBlock(currentPos)) {
									base.addBlockPiece(CORROCK_BLOCK.get(), currentPos);
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
					this.fillUp(base, offset, 3);

					if (rand.nextFloat() < 0.5F) {
						base.addBlockPiece(CORROCK_BLOCK.get(), offset.offset(horizontal.rotateY()));
					}

					if (rand.nextFloat() < 0.5F) {
						base.addBlockPiece(CORROCK_BLOCK.get(), offset.offset(horizontal.rotateYCCW()));
					}

					if (rand.nextFloat() < 0.5F) {
						base.addBlockPiece(CORROCK_BLOCK.get(), offset.up(2).offset(horizontal.rotateY()));
					}

					if (rand.nextFloat() < 0.5F) {
						base.addBlockPiece(CORROCK_BLOCK.get(), offset.up(2).offset(horizontal.rotateYCCW()));
					}

					if (rand.nextFloat() < 0.5F) {
						base.addBlockPiece(CORROCK_BLOCK.get(), doubleOffset);
					}

					if (rand.nextFloat() < 0.5F) {
						base.addBlockPiece(CORROCK_BLOCK.get(), doubleOffset.up(2));
					}
				}

				boolean xOrz = rand.nextBoolean();
				int x = xOrz ? rand.nextInt(2) : 0;
				int z = !xOrz ? rand.nextInt(2) : 0;
				Pair<GenerationPiece, List<ChorusPlantPart>> topPiece = this.getMediumTop(world, pos.add(x, 0, z).up(4), rand);

				if (base.canPlace(world) && topPiece.getFirst().canPlace(world)) {
					base.place(world);
					topPiece.getFirst().place(world);
					topPiece.getSecond().forEach((growth) -> {
						growth.placeGrowth(world, rand);
					});
					return true;
				}
			} else if (chance < 0.5F) {
				GenerationPiece base = this.getSmallBase(world, pos, rand);
				if (base != null) {
					GenerationPiece topPiece = new GenerationPiece((w, p) -> w.isAirBlock(p.pos));
					for (int x = -1; x < 2; x++) {
						for (int z = -1; z < 2; z++) {
							BlockPos placingPos = pos.add(x, 1, z);
							topPiece.addBlockPiece(CORROCK_BLOCK.get(), placingPos);
							if (rand.nextFloat() < 0.25F) {
								topPiece.addBlockPiece(CORROCK_CROWN(false).get().rotate(Rotation.randomRotation(rand)), placingPos.up());
							}
						}
					}

					for (Direction horizontal : Direction.Plane.HORIZONTAL) {
						BlockPos sidePos = pos.up(2).offset(horizontal, 2);
						BlockPos rightPos = sidePos.offset(horizontal.rotateY());
						BlockPos leftPos = sidePos.offset(horizontal.rotateYCCW());

						topPiece.addBlockPiece(CORROCK_BLOCK.get(), sidePos);
						if (rand.nextFloat() < 0.3F) {
							topPiece.addBlockPiece(CORROCK_CROWN(false).get().rotate(Rotation.randomRotation(rand)), sidePos.up());
						}

						topPiece.addBlockPiece(CORROCK_BLOCK.get(), rightPos);
						if (rand.nextFloat() < 0.3F) {
							topPiece.addBlockPiece(CORROCK_CROWN(false).get().rotate(Rotation.randomRotation(rand)), rightPos.up());
						}

						topPiece.addBlockPiece(CORROCK_BLOCK.get(), leftPos);
						if (rand.nextFloat() < 0.3F) {
							topPiece.addBlockPiece(CORROCK_CROWN(false).get().rotate(Rotation.randomRotation(rand)), leftPos.up());
						}
					}

					if (topPiece.canPlace(world)) {
						base.place(world);
						topPiece.place(world);
						world.setBlockState(pos, CORROCK_BLOCK.get(), 2);
						return true;
					}
				}
			}
		}
		return false;
	}

	@Nullable
	private GenerationPiece getSmallBase(IWorld world, BlockPos pos, Random rand) {
		int successfulSides = 0;
		GenerationPiece piece = new GenerationPiece((w, p) -> w.isAirBlock(p.pos) && Block.hasSolidSideOnTop(w, p.pos.down()));
		for (Direction horizontal : Direction.Plane.HORIZONTAL) {
			int length = 0;
			for (int i = 1; i < 3; i++) {
				BlockPos offset = pos.offset(horizontal, i);
				if (world.isAirBlock(offset) && Block.hasSolidSideOnTop(world, offset.down())) {
					length++;
				} else {
					break;
				}
			}
			if (length > 0) {
				for (int i2 = 1; i2 < length + 1; i2++) {
					piece.addBlockPiece(CORROCK_BLOCK.get(), pos.offset(horizontal, i2));
				}
				successfulSides++;
			}
		}
		return successfulSides >= 2 ? piece : null;
	}

	@SuppressWarnings("deprecation")
	private Pair<GenerationPiece, List<ChorusPlantPart>> getMediumTop(IWorld world, BlockPos pos, Random rand) {
		GenerationPiece top = new GenerationPiece((w, p) -> w.isAirBlock(p.pos));
		List<ChorusPlantPart> growths = Lists.newArrayList();
		List<BlockPos> corners = Lists.newArrayList();
		int variant = rand.nextInt(4);

		BlockPos startNPos = pos.offset(Direction.NORTH, 4).add(-2, 0, 0);
		for (int i = 0; i < 4; i++) {
			BlockPos placePos = startNPos.add(i, 0, 0);
			top.addBlockPiece(CORROCK_BLOCK.get(), placePos);
			if (rand.nextFloat() < 0.5F) {
				if (rand.nextBoolean()) {
					top.addBlockPiece(CORROCK_CROWN(true).get().with(CorrockCrownWallBlock.FACING, Direction.NORTH), placePos.offset(Direction.NORTH));
				} else {
					top.addBlockPiece(CORROCK_CROWN(false).get().rotate(Rotation.randomRotation(rand)), placePos.up());
				}
			}
		}

		BlockPos startEPos = pos.offset(Direction.EAST, 3).add(0, 0, 1);
		for (int i = 0; i < 4; i++) {
			BlockPos placePos = startEPos.add(0, 0, -i);
			top.addBlockPiece(CORROCK_BLOCK.get(), placePos);
			if (rand.nextFloat() < 0.5F) {
				if (rand.nextBoolean()) {
					top.addBlockPiece(CORROCK_CROWN(true).get().with(CorrockCrownWallBlock.FACING, Direction.EAST), placePos.offset(Direction.EAST));
				} else {
					top.addBlockPiece(CORROCK_CROWN(false).get().rotate(Rotation.randomRotation(rand)), placePos.up());
				}
			}
		}

		BlockPos startSPos = pos.offset(Direction.SOUTH, 3).add(1, 0, 0);
		for (int i = 0; i < 4; i++) {
			BlockPos placePos = startSPos.add(-i, 0, 0);
			top.addBlockPiece(CORROCK_BLOCK.get(), placePos);
			if (rand.nextFloat() < 0.5F) {
				if (rand.nextBoolean()) {
					top.addBlockPiece(CORROCK_CROWN(true).get().with(CorrockCrownWallBlock.FACING, Direction.SOUTH), placePos.offset(Direction.SOUTH));
				} else {
					top.addBlockPiece(CORROCK_CROWN(false).get().rotate(Rotation.randomRotation(rand)), placePos.up());
				}
			}
		}

		BlockPos startWPos = pos.offset(Direction.WEST, 4).add(0, 0, 1);
		for (int i = 0; i < 4; i++) {
			BlockPos placePos = startWPos.add(0, 0, -i);
			top.addBlockPiece(CORROCK_BLOCK.get(), placePos);
			if (rand.nextFloat() < 0.5F) {
				if (rand.nextBoolean()) {
					top.addBlockPiece(CORROCK_CROWN(true).get().with(CorrockCrownWallBlock.FACING, Direction.WEST), placePos.offset(Direction.WEST));
				} else {
					top.addBlockPiece(CORROCK_CROWN(false).get().rotate(Rotation.randomRotation(rand)), placePos.up());
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
				top.addBlockPiece(CORROCK_CROWN(false).get().rotate(Rotation.randomRotation(rand)), cornerNW.up());
			} else {
				if (rand.nextFloat() < 0.5F) {
					top.addBlockPiece(CORROCK_CROWN(true).get().with(CorrockCrownWallBlock.FACING, Direction.NORTH), cornerNW.offset(Direction.NORTH));
				}

				if (rand.nextFloat() < 0.5F) {
					top.addBlockPiece(CORROCK_CROWN(true).get().with(CorrockCrownWallBlock.FACING, Direction.WEST), cornerNW.offset(Direction.WEST));
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
				top.addBlockPiece(CORROCK_CROWN(false).get().rotate(Rotation.randomRotation(rand)), cornerNE.up());
			} else {
				if (rand.nextFloat() < 0.5F) {
					top.addBlockPiece(CORROCK_CROWN(true).get().with(CorrockCrownWallBlock.FACING, Direction.NORTH), cornerNE.offset(Direction.NORTH));
				}

				if (rand.nextFloat() < 0.5F) {
					top.addBlockPiece(CORROCK_CROWN(true).get().with(CorrockCrownWallBlock.FACING, Direction.EAST), cornerNE.offset(Direction.EAST));
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
				top.addBlockPiece(CORROCK_CROWN(false).get().rotate(Rotation.randomRotation(rand)), cornerSE.up());
			} else {
				if (rand.nextFloat() < 0.5F) {
					top.addBlockPiece(CORROCK_CROWN(true).get().with(CorrockCrownWallBlock.FACING, Direction.SOUTH), cornerSE.offset(Direction.SOUTH));
				}

				if (rand.nextFloat() < 0.5F) {
					top.addBlockPiece(CORROCK_CROWN(true).get().with(CorrockCrownWallBlock.FACING, Direction.EAST), cornerSE.offset(Direction.EAST));
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
				top.addBlockPiece(CORROCK_CROWN(false).get().rotate(Rotation.randomRotation(rand)), cornerSW.up());
			} else {
				if (rand.nextFloat() < 0.5F) {
					top.addBlockPiece(CORROCK_CROWN(true).get().with(CorrockCrownWallBlock.FACING, Direction.SOUTH), cornerSW.offset(Direction.SOUTH));
				}

				if (rand.nextFloat() < 0.5F) {
					top.addBlockPiece(CORROCK_CROWN(true).get().with(CorrockCrownWallBlock.FACING, Direction.WEST), cornerSW.offset(Direction.WEST));
				}
			}
		}
		corners.add(cornerSW);

		for (int x = cornerNW.getX(); x <= cornerSE.getX(); x++) {
			for (int z = cornerNW.getZ(); z <= cornerSE.getZ(); z++) {
				BlockPos placingPos = new BlockPos(x, pos.getY(), z);
				if (!corners.contains(placingPos)) {
					if (!this.isCloseToAnotherGrowth(growths, placingPos.down()) && rand.nextFloat() < 0.1F && world.isAirBlock(placingPos) && world.isAirBlock(placingPos.up())) {
						growths.add(new ChorusPlantPart(placingPos.down()));

						for (Direction direction : Direction.values()) {
							if (direction != Direction.UP) {
								top.addBlockPiece(CORROCK_BLOCK.get(), placingPos.down().offset(direction));
							}
						}
					} else {
						top.addBlockPiece(CORROCK_BLOCK.get(), placingPos.down());
					}
				}
			}
		}

		for (BlockPos positions : corners) {
			top.addBlockPiece(CORROCK_BLOCK.get(), positions);
		}

		return Pair.of(top, growths);
	}

	private void fillUp(GenerationPiece piece, BlockPos pos, int height) {
		for (int i = 0; i < height; i++) {
			piece.addBlockPiece(CORROCK_BLOCK.get(), pos.up(i));
		}
	}

	private boolean isCloseToAnotherGrowth(List<ChorusPlantPart> growths, BlockPos pos) {
		for (ChorusPlantPart part : growths) {
			if (MathHelper.sqrt(part.pos.distanceSq(pos)) < 2.0F) {
				return true;
			}
		}
		return false;
	}

}