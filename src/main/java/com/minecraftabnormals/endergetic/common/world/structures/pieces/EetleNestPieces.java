package com.minecraftabnormals.endergetic.common.world.structures.pieces;

import com.google.common.collect.Sets;
import com.minecraftabnormals.abnormals_core.core.util.MathUtil;
import com.minecraftabnormals.endergetic.api.util.GenerationUtils;
import com.minecraftabnormals.endergetic.common.blocks.CorrockCrownBlock;
import com.minecraftabnormals.endergetic.common.blocks.CorrockCrownStandingBlock;
import com.minecraftabnormals.endergetic.common.blocks.CorrockCrownWallBlock;
import com.minecraftabnormals.endergetic.common.blocks.EetleEggsBlock;
import com.minecraftabnormals.endergetic.common.world.structures.EEStructures;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.OctavesNoiseGenerator;
import net.minecraft.world.gen.PerlinNoiseGenerator;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.*;
import java.util.stream.IntStream;

public final class EetleNestPieces {
	private static final Map<Long, PerlinNoiseGenerator> SURFACE_NOISE = new HashMap<>();
	private static final Map<Long, OctavesNoiseGenerator> UNDERGROUND_NOISE = new HashMap<>();

	private abstract static class EetleNestPiece extends StructurePiece {
		protected static final Direction[] ATTACHMENT_DIRECTIONS = Direction.values();
		protected static final Block CORROCK_BLOCK = EEBlocks.CORROCK_END_BLOCK.get();
		protected static final Block EUMUS = EEBlocks.EUMUS.get();
		protected static final Block CROWN_STANDING = EEBlocks.CORROCK_CROWN_END_STANDING.get();
		protected static final Block EETLE_EGSS = EEBlocks.EETLE_EGGS.get();
		protected static final BlockState CORROCK_BLOCK_STATE = CORROCK_BLOCK.getDefaultState();
		protected static final BlockState EUMUS_STATE = EUMUS.getDefaultState();
		protected static final BlockState CROWN_WALL = EEBlocks.CORROCK_CROWN_END_WALL.get().getDefaultState();
		protected static final BlockState CROWN_STANDING_STATE = CROWN_STANDING.getDefaultState();
		protected static final BlockState EETLE_EGGS_STATE = EETLE_EGSS.getDefaultState();

		protected EetleNestPiece(IStructurePieceType pieceType, int componentType) {
			super(pieceType, componentType);
			this.setCoordBaseMode(null);
		}

		protected EetleNestPiece(IStructurePieceType pieceType, CompoundNBT nbt) {
			super(pieceType, nbt);
		}

		@Override
		protected void readAdditional(CompoundNBT compoundNBT) {
		}
	}

	public static class EetleNestParentPiece extends EetleNestPiece {
		private final List<EetleNestPiece> childPieces = new ArrayList<>();

		public EetleNestParentPiece(TemplateManager manager, CompoundNBT compoundNBT) {
			super(EEStructures.PieceTypes.EETLE_NEST_PARENT, compoundNBT);
			this.addChildPieces(manager, this.boundingBox);
		}

		public EetleNestParentPiece(TemplateManager manager, BlockPos corner) {
			super(EEStructures.PieceTypes.EETLE_NEST_PARENT, 0);
			this.boundingBox = new MutableBoundingBox(corner.down(32), corner.add(64, 0, 64));
			this.addChildPieces(manager, this.boundingBox);
		}

		private static void transformSurface(ISeedReader world, int x, int z, int startingY, int maxDepth, int depthScale, double noise, BlockState topState) {
			BlockPos.Mutable mutable = new BlockPos.Mutable(x, startingY, z);
			int depth = (int) (Math.abs(noise) * depthScale + 14.0D);
			BlockState fillState = topState;
			for (int y = startingY, d = 0; y > maxDepth && d < depth; y--, d++) {
				mutable.setY(y);
				if (world.getBlockState(mutable).getBlock() == Blocks.END_STONE) {
					world.setBlockState(mutable, fillState, 2);
					fillState = EUMUS_STATE;
				} else if (world.isAirBlock(mutable)) {
					fillState = topState;
				}
			}
		}

		private void addChildPieces(TemplateManager templateManager, MutableBoundingBox boundingBox) {
			this.childPieces.add(new EetleNestCorePiece(templateManager, boundingBox));
		}

		@SuppressWarnings("deprecation")
		@Override
		public boolean func_230383_a_(ISeedReader world, StructureManager structureManager, ChunkGenerator chunkGenerator, Random random, MutableBoundingBox mutableBoundingBox, ChunkPos chunkPos, BlockPos pos) {
			PerlinNoiseGenerator noiseGenerator = SURFACE_NOISE.computeIfAbsent(world.getSeed(), seedL -> new PerlinNoiseGenerator(new SharedSeedRandom(seedL), IntStream.rangeClosed(-3, 0)));
			int originX = pos.getX();
			int originZ = pos.getZ();
			int radius = 32;
			for (int x = -radius; x < radius; x++) {
				for (int z = -radius; z < radius; z++) {
					double noise = noiseGenerator.noiseAt(x, z, true);
					double areaNoise = noise * 12.0D - 7.0D;
					double distanceSq = x * x + z * z + areaNoise * areaNoise;
					if (distanceSq <= (radius - 3) * (radius - 3)) {
						int posX = originX + x;
						int posZ = originZ + z;
						transformSurface(world, posX, posZ, world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, posX, posZ), world.getSeaLevel() / 2, 40, noise, CORROCK_BLOCK_STATE);
					} else if (distanceSq <= radius * radius) {
						int posX = originX + x;
						int posZ = originZ + z;
						transformSurface(world, posX, posZ, world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, posX, posZ), world.getSeaLevel() / 2, 24, noise, EUMUS_STATE);
					}
				}
			}
			pos = world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, pos);
			for (EetleNestPiece piece : this.childPieces) {
				piece.func_230383_a_(world, structureManager, chunkGenerator, random, mutableBoundingBox, chunkPos, pos);
			}
			return true;
		}
	}

	public static class EetleNestCorePiece extends EetleNestPiece {
		private static final Set<Block> CARVABLE_BLOCKS = Sets.newHashSet(Blocks.STONE, Blocks.END_STONE, CORROCK_BLOCK, EUMUS);
		private static final ResourceLocation ARENA = new ResourceLocation(EndergeticExpansion.MOD_ID, "eetle_nest/arena");
		private final Template arena;

		protected EetleNestCorePiece(TemplateManager templateManager, MutableBoundingBox boundingBox) {
			super(EEStructures.PieceTypes.EETLE_NEST_CORE, 1);
			this.boundingBox = boundingBox;
			this.arena = templateManager.getTemplate(ARENA);
		}

		public EetleNestCorePiece(TemplateManager templateManager, CompoundNBT nbt) {
			super(EEStructures.PieceTypes.EETLE_NEST_CORE, nbt);
			this.arena = templateManager.getTemplate(ARENA);
		}

		private static void createEumusPatch(ISeedReader world, BlockPos origin, OctavesNoiseGenerator noiseGenerator, int radius) {
			int originX = origin.getX();
			int originY = origin.getY();
			int originZ = origin.getZ();
			BlockPos.Mutable mutable = new BlockPos.Mutable();
			for (int x = originX - radius; x < originX + radius; x++) {
				for (int y = originY - radius; y < originY + radius; y++) {
					for (int z = originZ - radius; z < originZ + radius; z++) {
						double localX = (x - originX) / (float) radius;
						double localY = (y - originY) / ((float) radius);
						double localZ = (z - originZ) / (float) radius;
						double distanceSq = localX * localX + localY * localY + localZ * localZ;
						double frequency = 0.75F;
						double shapeNoise = noiseGenerator.func_205563_a(x * frequency, y * frequency, z * frequency) * 0.5F;
						if (distanceSq <= 1.0F + shapeNoise) {
							mutable.setPos(x, y, z);
							if (world.getBlockState(mutable).getBlock() == CORROCK_BLOCK) {
								world.setBlockState(mutable, EUMUS_STATE, 2);
							}
						}
					}
				}
			}
		}

		private static void createStalactite(ISeedReader world, BlockPos origin, Random random, OctavesNoiseGenerator noiseGenerator, int radius, int length) {
			List<BlockPos> possibleDecorationPositions = new ArrayList<>();
			BlockPos.Mutable mutable = new BlockPos.Mutable();
			int originX = origin.getX();
			int originY = origin.getY();
			int originZ = origin.getZ();
			float noiseMultiplier = radius * 0.25F;
			for (int x = originX - radius; x < originX + radius; x++) {
				for (int y = originY - length; y < originY; y++) {
					for (int z = originZ - radius; z < originZ + radius; z++) {
						mutable.setPos(x, y, z);
						double localX = (x - originX) / (float) radius;
						double localY = (y - originY) / ((float) length - 1);
						double localZ = (z - originZ) / (float) radius;
						double distanceSq = localX * localX + localY * localY + localZ * localZ;
						double frequency = 2.0F;
						double shapeNoise = noiseGenerator.func_205563_a(x * frequency, y * frequency, z * frequency) * noiseMultiplier;
						if (distanceSq <= 1.0F + shapeNoise) {
							for (int yU = 0; yU <= length; yU++) {
								Block block = world.getBlockState(mutable).getBlock();
								if (world.isAirBlock(mutable) || block == EETLE_EGSS || block instanceof CorrockCrownBlock) {
									possibleDecorationPositions.add(mutable.toImmutable());
									world.setBlockState(mutable, CORROCK_BLOCK_STATE, 2);
								} else {
									break;
								}
								mutable.setY(mutable.getY() + 1);
							}
						}
					}
				}
			}
			for (BlockPos pos : possibleDecorationPositions) {
				if (random.nextFloat() < 0.1F) {
					EetleEggsBlock.shuffleDirections(ATTACHMENT_DIRECTIONS, random);
					for (Direction direction : ATTACHMENT_DIRECTIONS) {
						BlockPos offset = pos.offset(direction);
						if (world.isAirBlock(offset)) {
							if (random.nextBoolean()) {
								world.setBlockState(offset, EETLE_EGGS_STATE.with(EetleEggsBlock.FACING, direction), 2);
							} else {
								if (direction.getAxis() == Direction.Axis.Y) {
									world.setBlockState(offset, CROWN_STANDING_STATE.with(CorrockCrownStandingBlock.UPSIDE_DOWN, direction == Direction.DOWN).with(CorrockCrownStandingBlock.ROTATION, random.nextInt(16)), 2);
								} else {
									world.setBlockState(offset, CROWN_WALL.with(CorrockCrownWallBlock.FACING, direction), 2);
								}
							}
						}
					}
				}
			}
		}

		private static void createArena(ISeedReader world, BlockPos origin, Random random, Template arena) {
			BlockPos min = origin.add(-11, -16, -11);
			BlockPos max = origin.add(11, 1, 11);
			Rotation rotation = Rotation.randomRotation(random);
			arena.func_237146_a_(world, offsetPosForRotation(min, rotation), origin, new PlacementSettings().setRotation(rotation).setBoundingBox(new MutableBoundingBox(min, max)), random, 2);
			BlockPos.Mutable mutable = new BlockPos.Mutable();
			//Fill in bottom areas to prevent floating cases
			int extraFillBottomY = origin.getY() - 16;
			for (int x = min.getX(); x <= max.getX(); x++) {
				for (int z = min.getZ(); z <= max.getZ(); z++) {
					mutable.setPos(x, extraFillBottomY, z);
					if (world.getBlockState(mutable).getBlock() == Blocks.GOLD_BLOCK) {
						world.setBlockState(mutable, CORROCK_BLOCK_STATE, 2);
						for (int y = extraFillBottomY; y > extraFillBottomY - 3; y--) {
							mutable.setY(y);
							world.setBlockState(mutable, CORROCK_BLOCK_STATE, 2);
						}
					}
				}
			}
			mutable = origin.down(13).toMutable();
			//Create bottom doorway
			Direction horizontal = Direction.Plane.HORIZONTAL.random(random);
			for (int i = 0; i <= 8; i++) {
				mutable.move(horizontal);
				if (world.getBlockState(mutable).getBlock() == CORROCK_BLOCK) {
					mutable.move(horizontal.getOpposite(), 2);
					carveForward(world, mutable, horizontal);
					carveForward(world, mutable.up(), horizontal);
					carveForward(world, mutable.up(2), horizontal);

					BlockPos rotateCW = mutable.offset(horizontal.rotateY());
					carveForward(world, rotateCW, horizontal);
					carveForward(world, rotateCW.up(), horizontal);

					BlockPos rotateCCW = mutable.offset(horizontal.rotateYCCW());
					carveForward(world, rotateCCW, horizontal);
					carveForward(world, rotateCCW.up(), horizontal);
					break;
				}
			}
			createCenterSteps(world, origin.down(9), random);
			createWayUp(world, origin.down(7), random);

			int arenaTopY = origin.getY() - 3;
			//Adds crowns to top of arena
			for (int x = min.getX(); x <= max.getX(); x++) {
				for (int z = min.getZ(); z <= max.getZ(); z++) {
					mutable.setPos(x, arenaTopY, z);
					if (world.getBlockState(mutable).getBlock() == CORROCK_BLOCK && random.nextBoolean()) {
						EetleEggsBlock.shuffleDirections(ATTACHMENT_DIRECTIONS, random);
						for (Direction direction : ATTACHMENT_DIRECTIONS) {
							if (direction.getAxis() == Direction.Axis.Y) {
								direction = Direction.UP;
								BlockPos offset = mutable.offset(direction);
								if (world.isAirBlock(offset)) {
									world.setBlockState(offset, CROWN_STANDING_STATE.with(CorrockCrownStandingBlock.ROTATION, random.nextInt(16)), 2);
									break;
								}
							} else {
								BlockPos offset = mutable.offset(direction);
								if (world.isAirBlock(offset) && world.isAirBlock(offset.down())) {
									world.setBlockState(offset, CROWN_WALL.with(CorrockCrownWallBlock.FACING, direction), 2);
									break;
								}
							}
						}
					}
				}
			}
		}

		private static void createWayUp(ISeedReader world, BlockPos center, Random random) {
			Direction horizontal = Direction.Plane.HORIZONTAL.random(random);
			BlockPos.Mutable mutable = center.toMutable();
			for (int i = 0; i < 10; i++) {
				mutable.move(horizontal);
				if (world.getBlockState(mutable).getBlock() == CORROCK_BLOCK) {
					Direction opposite = horizontal.getOpposite();
					mutable.move(opposite, 2);

					carveForward(world, mutable, horizontal);
					carveForward(world, mutable.up(), horizontal);
					carveForward(world, mutable.up(2), horizontal);

					Direction cw = horizontal.rotateY();
					BlockPos rotateCW = mutable.offset(cw);
					carveForward(world, rotateCW, horizontal);
					carveForward(world, rotateCW.up(), horizontal);

					Direction ccw = horizontal.rotateYCCW();
					BlockPos rotateCCW = mutable.offset(horizontal.rotateYCCW());
					carveForward(world, rotateCCW, horizontal);
					carveForward(world, rotateCCW.up(), horizontal);

					BlockPos outsideCenter = findOutsidePos(world, mutable, horizontal).down();
					for (int l = 0; l < 4; l++) {
						BlockPos offset = outsideCenter.offset(horizontal, l);
						createLedgePiece(world, offset, cw, ccw, random, 4 - l, true);
						createLedgePiece(world, offset.down(), cw, ccw, random, 2 - l, false);
						if (l == 3 && random.nextFloat() < 0.25F) {
							tryToPlaceCrownAroundPos(world, offset, random);
						}
					}

					Direction leftOrRight = random.nextBoolean() ? ccw : cw;
					BlockPos outsideThree = outsideCenter.offset(horizontal, 3);
					BlockPos firstStairEdge = outsideThree.offset(leftOrRight, 2);
					world.setBlockState(firstStairEdge, CORROCK_BLOCK_STATE, 2);
					tryToPlaceCrownAroundPos(world, firstStairEdge, random);
					mutable = outsideThree.toMutable();
					mutable.move(leftOrRight, 2);
					int startY = mutable.getY();
					int term = 1;
					for (int n = 0; n < 6; n++) {
						term += (n % 2);
						mutable.setY(startY + term);
						createOuterStairPiece(world, mutable.move(leftOrRight), random, opposite);
					}
					createOuterStairPiece(world, mutable.move(leftOrRight).move(opposite), random, opposite);
					createOuterStairPiece(world, mutable.move(leftOrRight).move(opposite), random, opposite);
					Direction leftOrRightOpposite = leftOrRight.getOpposite();
					carveForward(world, mutable.move(Direction.UP).move(leftOrRightOpposite), opposite, 1);
					carveForward(world, mutable.move(leftOrRightOpposite), opposite, 2);
					searchAndPlaceTopGate(world, mutable.move(leftOrRight, 2), random, opposite);
					searchAndPlaceTopGate(world, mutable.move(leftOrRightOpposite, 3), random, opposite);
					break;
				}
			}
		}

		private static void searchAndPlaceTopGate(ISeedReader world, BlockPos start, Random random, Direction opposite) {
			BlockPos.Mutable mutable = start.toMutable();
			for (int i = 0; i <= 7; i++) {
				if (world.getBlockState(mutable).getBlock() == CORROCK_BLOCK) {
					BlockPos up = mutable.up();
					world.setBlockState(up, CORROCK_BLOCK_STATE, 2);
					tryToPlaceCrownAroundPos(world, up, random);
					break;
				}
				mutable.move(opposite);
			}
		}

		private static void createOuterStairPiece(ISeedReader world, BlockPos start, Random random, Direction opposite) {
			BlockPos.Mutable mutable = start.toMutable();
			for (int i = 0; i <= 8; i++) {
				world.setBlockState(mutable, CORROCK_BLOCK_STATE, 2);
				mutable.move(opposite);
				if (world.getBlockState(mutable).getBlock() == CORROCK_BLOCK) {
					break;
				}
			}
			if (random.nextBoolean()) {
				tryToPlaceCrownAroundPos(world, start, random);
			}
		}

		private static void createLedgePiece(ISeedReader world, BlockPos middle, Direction right, Direction left, Random random, int radius, boolean generateCrowns) {
			BlockPos.Mutable leftMutable = middle.toMutable();
			BlockPos.Mutable rightMutable = middle.toMutable();
			for (int i = 0; i <= radius; i++) {
				world.setBlockState(leftMutable, CORROCK_BLOCK_STATE, 2);
				world.setBlockState(rightMutable, CORROCK_BLOCK_STATE, 2);
				if (generateCrowns && i == radius) {
					if (random.nextBoolean()) {
						tryToPlaceCrownAroundPos(world, leftMutable, random);
					}
					if (random.nextBoolean()) {
						tryToPlaceCrownAroundPos(world, rightMutable, random);
					}
				}
				leftMutable.move(left);
				rightMutable.move(right);
			}
		}

		private static void tryToPlaceCrownAroundPos(ISeedReader world, BlockPos pos, Random random) {
			EetleEggsBlock.shuffleDirections(ATTACHMENT_DIRECTIONS, random);
			for (Direction direction : ATTACHMENT_DIRECTIONS) {
				if (direction.getAxis() == Direction.Axis.Y) {
					boolean upsideDown = direction == Direction.DOWN;
					BlockPos offset = upsideDown ? pos.down() : pos.up();
					if (world.isAirBlock(offset)) {
						world.setBlockState(offset, CROWN_STANDING_STATE.with(CorrockCrownStandingBlock.UPSIDE_DOWN, upsideDown).with(CorrockCrownStandingBlock.ROTATION, random.nextInt(16)), 2);
						break;
					}
				} else {
					BlockPos offset = pos.offset(direction);
					if (world.isAirBlock(offset)) {
						world.setBlockState(offset, CROWN_WALL.with(CorrockCrownWallBlock.FACING, direction), 2);
						break;
					}
				}
			}
		}

		private static BlockPos findOutsidePos(ISeedReader world, BlockPos center, Direction direction) {
			boolean wasLastCorrock = false;
			BlockPos.Mutable mutable = center.toMutable();
			for (int x = 0; x < 4; x++) {
				mutable.move(direction);
				if (world.isAirBlock(mutable) && wasLastCorrock) {
					return mutable;
				}
				wasLastCorrock = world.getBlockState(mutable.down()).getBlock() == CORROCK_BLOCK;
			}
			return mutable;
		}

		private static void createCenterSteps(ISeedReader world, BlockPos center, Random random) {
			Rotation rotation = Rotation.randomRotation(random);
			switch (rotation) {
				case NONE:
					fillForward(world, center.add(3, 0, -4), Direction.NORTH);
					fillForward(world, center.add(4, 0, -3), Direction.NORTH);
					fillForward(world, center.add(5, 0, -3), Direction.NORTH);
					fillForward(world, center.add(6, 0, -3), Direction.NORTH);

					center = center.down(4);
					int centerX = center.getX();
					int centerY = center.getY();
					int centerZ = center.getZ();
					GenerationUtils.fillAreaWithBlockCube(world, centerX + 1, centerY, centerZ + 4, centerX + 2, centerY, centerZ + 5, CORROCK_BLOCK_STATE);
					if (random.nextBoolean()) {
						world.setBlockState(center.add(random.nextInt(2) + 1, 1, random.nextInt(2) + 4), CROWN_STANDING_STATE.with(CorrockCrownStandingBlock.ROTATION, random.nextInt(16)), 2);
					}

					GenerationUtils.fillAreaWithBlockCube(world, centerX - 2, centerY, centerZ + 3, centerX - 1, centerY + 1, centerZ + 4, CORROCK_BLOCK_STATE);
					world.setBlockState(center.add(-random.nextInt(2) - 1, 0, random.nextInt(2) + 3), CAVE_AIR, 2);
					if (random.nextFloat() < 0.6F) {
						world.setBlockState(center.add(-random.nextInt(2) - 1, 2, random.nextInt(2) + 3), CROWN_STANDING_STATE.with(CorrockCrownStandingBlock.ROTATION, random.nextInt(16)), 2);
					}

					createMediumStep(world, center.add(-4, 0, 1), random, Direction.NORTH);
					createLargeStep(world, center.add(0, 0, -3), random, Direction.EAST);
					break;
				case CLOCKWISE_90:
					fillForward(world, center.add(3, 0, 4), Direction.SOUTH);
					fillForward(world, center.add(4, 0, 3), Direction.SOUTH);
					fillForward(world, center.add(5, 0, 3), Direction.SOUTH);
					fillForward(world, center.add(6, 0, 3), Direction.SOUTH);

					center = center.down(4);
					centerX = center.getX();
					centerY = center.getY();
					centerZ = center.getZ();
					GenerationUtils.fillAreaWithBlockCube(world, centerX - 5, centerY, centerZ + 1, centerX - 4, centerY, centerZ + 2, CORROCK_BLOCK_STATE);
					if (random.nextBoolean()) {
						world.setBlockState(center.add(-random.nextInt(2) - 4, 1, random.nextInt(2) + 1), CROWN_STANDING_STATE.with(CorrockCrownStandingBlock.ROTATION, random.nextInt(16)), 2);
					}

					GenerationUtils.fillAreaWithBlockCube(world, centerX - 3, centerY, centerZ - 2, centerX - 2, centerY + 1, centerZ - 1, CORROCK_BLOCK_STATE);
					world.setBlockState(center.add(-random.nextInt(2) - 2, 0, -random.nextInt(2) - 1), CAVE_AIR, 2);
					if (random.nextFloat() < 0.6F) {
						world.setBlockState(center.add(-random.nextInt(2) - 2, 2, -random.nextInt(2) - 1), CROWN_STANDING_STATE.with(CorrockCrownStandingBlock.ROTATION, random.nextInt(16)), 2);
					}

					createMediumStep(world, center.add(-1, 0, -5), random, Direction.EAST);
					createLargeStep(world, center.add(4, 0, -1), random, Direction.SOUTH);
					break;
				case CLOCKWISE_180:
					fillForward(world, center.add(-3, 0, 4), Direction.SOUTH);
					fillForward(world, center.add(-4, 0, 3), Direction.SOUTH);
					fillForward(world, center.add(-5, 0, 3), Direction.SOUTH);
					fillForward(world, center.add(-6, 0, 3), Direction.SOUTH);

					center = center.down(4);
					centerX = center.getX();
					centerY = center.getY();
					centerZ = center.getZ();
					GenerationUtils.fillAreaWithBlockCube(world, centerX - 2, centerY, centerZ - 4, centerX - 1, centerY, centerZ - 3, CORROCK_BLOCK_STATE);
					if (random.nextBoolean()) {
						world.setBlockState(center.add(-(random.nextInt(2) + 1), 1, -(random.nextInt(2) + 3)), CROWN_STANDING_STATE.with(CorrockCrownStandingBlock.ROTATION, random.nextInt(16)), 2);
					}

					GenerationUtils.fillAreaWithBlockCube(world, centerX + 1, centerY, centerZ - 3, centerX + 2, centerY + 1, centerZ - 2, CORROCK_BLOCK_STATE);
					world.setBlockState(center.add(random.nextInt(2) + 1, 0, -(random.nextInt(2) + 2)), CAVE_AIR, 2);
					if (random.nextFloat() < 0.6F) {
						world.setBlockState(center.add(random.nextInt(2) + 1, 2, -(random.nextInt(2) + 2)), CROWN_STANDING_STATE.with(CorrockCrownStandingBlock.ROTATION, random.nextInt(16)), 2);
					}

					createMediumStep(world, center.add(4, 0, 1), random, Direction.NORTH);
					createLargeStep(world, center.add(1, 0, 4), random, Direction.WEST);
					break;
				case COUNTERCLOCKWISE_90:
					fillForward(world, center.add(-3, 0, -4), Direction.NORTH);
					fillForward(world, center.add(-4, 0, -3), Direction.NORTH);
					fillForward(world, center.add(-5, 0, -3), Direction.NORTH);
					fillForward(world, center.add(-6, 0, -3), Direction.NORTH);

					center = center.down(4);
					centerX = center.getX();
					centerY = center.getY();
					centerZ = center.getZ();
					GenerationUtils.fillAreaWithBlockCube(world, centerX + 3, centerY, centerZ - 2, centerX + 4, centerY, centerZ - 1, CORROCK_BLOCK_STATE);
					if (random.nextBoolean()) {
						world.setBlockState(center.add(random.nextInt(2) + 3, 1, -(random.nextInt(2) + 1)), CROWN_STANDING_STATE.with(CorrockCrownStandingBlock.ROTATION, random.nextInt(16)), 2);
					}

					GenerationUtils.fillAreaWithBlockCube(world, centerX + 2, centerY, centerZ + 1, centerX + 3, centerY + 1, centerZ + 2, CORROCK_BLOCK_STATE);
					world.setBlockState(center.add(random.nextInt(2) + 1, 0, random.nextInt(2) + 1), CAVE_AIR, 2);
					if (random.nextFloat() < 0.6F) {
						world.setBlockState(center.add(random.nextInt(2) + 1, 2, random.nextInt(2) + 1), CROWN_STANDING_STATE.with(CorrockCrownStandingBlock.ROTATION, random.nextInt(16)), 2);
					}

					createMediumStep(world, center.add(0, 0, 4), random, Direction.WEST);
					createLargeStep(world, center.add(-4, 0, 1), random, Direction.NORTH);
					break;
			}
		}

		private static void createMediumStep(ISeedReader world, BlockPos pos, Random random, Direction direction) {
			BlockPos.Mutable mutable = pos.toMutable();
			int startY = mutable.getY();
			for (int y = 0; y < 3; y++) {
				mutable.setY(startY + y);
				world.setBlockState(mutable, CORROCK_BLOCK_STATE, 2);
			}
			createHorizontalCrownedCluster(world, pos, random);
			BlockPos offset = pos.offset(direction);
			mutable = offset.toMutable();
			for (int y = 0; y < 3; y++) {
				mutable.setY(startY + y);
				world.setBlockState(mutable, CORROCK_BLOCK_STATE, 2);
			}
			createHorizontalCrownedCluster(world, offset, random);
		}

		private static void createLargeStep(ISeedReader world, BlockPos pos, Random random, Direction direction) {
			BlockPos.Mutable mutable = pos.toMutable();
			int startY = mutable.getY();
			for (int y = 0; y < 4; y++) {
				mutable.setY(startY + y);
				world.setBlockState(mutable, CORROCK_BLOCK_STATE, 2);
			}
			createLargeStepBottom(world, pos, random);
			createLargeStepTop(world, mutable, random);
			BlockPos offset = pos.offset(direction);
			mutable = offset.toMutable();
			for (int y = 0; y < 4; y++) {
				mutable.setY(startY + y);
				world.setBlockState(mutable, CORROCK_BLOCK_STATE, 2);
			}
			createLargeStepBottom(world, offset, random);
			createLargeStepTop(world, mutable, random);
		}

		private static void createLargeStepBottom(ISeedReader world, BlockPos pos, Random random) {
			for (Direction horizontal : Direction.Plane.HORIZONTAL) {
				if (random.nextBoolean()) {
					BlockPos offset = pos.offset(horizontal);
					world.setBlockState(offset, CORROCK_BLOCK_STATE, 2);
					if (random.nextFloat() < 0.25F) {
						BlockPos offsetUp = offset.up();
						if (world.isAirBlock(offsetUp)) {
							world.setBlockState(offset.up(), CROWN_STANDING_STATE.with(CorrockCrownStandingBlock.ROTATION, random.nextInt(16)), 2);
						}
					}
				} else if (random.nextBoolean()) {
					BlockPos offset = pos.offset(horizontal);
					world.setBlockState(offset, CORROCK_BLOCK_STATE, 2);
					world.setBlockState(offset.up(), CORROCK_BLOCK_STATE, 2);
					if (random.nextFloat() < 0.25F) {
						BlockPos offsetUp2 = offset.up(2);
						if (world.isAirBlock(offsetUp2)) {
							world.setBlockState(offsetUp2, CROWN_STANDING_STATE.with(CorrockCrownStandingBlock.ROTATION, random.nextInt(16)), 2);
						}
					}
					if (random.nextBoolean()) {
						BlockPos doubleOffset = pos.offset(horizontal, 2);
						world.setBlockState(doubleOffset, CORROCK_BLOCK_STATE, 2);
						if (random.nextFloat() < 0.25F) {
							BlockPos doubleOffsetUp = doubleOffset.up();
							if (world.isAirBlock(doubleOffsetUp)) {
								world.setBlockState(doubleOffsetUp, CROWN_STANDING_STATE.with(CorrockCrownStandingBlock.ROTATION, random.nextInt(16)), 2);
							}
						}
					}
				}
			}
		}

		private static void createLargeStepTop(ISeedReader world, BlockPos pos, Random random) {
			for (Direction horizontal : Direction.Plane.HORIZONTAL) {
				BlockPos offset = pos.offset(horizontal);
				world.setBlockState(offset, CORROCK_BLOCK_STATE, 2);
				if (random.nextFloat() < 0.2F) {
					BlockPos up = offset.up();
					if (world.isAirBlock(up)) {
						world.setBlockState(up, CROWN_STANDING_STATE.with(CorrockCrownStandingBlock.ROTATION, random.nextInt(16)), 2);
					}
				}
				if (random.nextFloat() < 0.25F) {
					BlockPos otherOffset = offset.offset(Direction.Plane.HORIZONTAL.random(random));
					world.setBlockState(otherOffset, CORROCK_BLOCK_STATE, 2);
					if (random.nextFloat() < 0.2F) {
						BlockPos up = otherOffset.up();
						if (world.isAirBlock(up)) {
							world.setBlockState(up, CROWN_STANDING_STATE.with(CorrockCrownStandingBlock.ROTATION, random.nextInt(16)), 2);
						}
					}
					world.setBlockState(offset.down(), CORROCK_BLOCK_STATE, 2);
				}
			}
		}

		private static void createHorizontalCrownedCluster(ISeedReader world, BlockPos pos, Random random) {
			BlockPos up = pos.up(2);
			for (Direction horizontal : Direction.Plane.HORIZONTAL) {
				if (random.nextFloat() < 0.8F) {
					BlockPos offset = pos.offset(horizontal);
					world.setBlockState(offset, CORROCK_BLOCK_STATE, 2);
					BlockPos offsetUp = offset.up();
					if (world.isAirBlock(offset) && random.nextFloat() < 0.15F) {
						world.setBlockState(offsetUp, CROWN_STANDING_STATE.with(CorrockCrownStandingBlock.ROTATION, random.nextInt(16)), 2);
					}
				}
				if (random.nextFloat() < 0.8F) {
					BlockPos offset = up.offset(horizontal);
					world.setBlockState(offset, CORROCK_BLOCK_STATE, 2);
					BlockPos offsetUp = offset.up();
					if (world.isAirBlock(offsetUp) && random.nextFloat() < 0.2F) {
						world.setBlockState(offsetUp, CROWN_STANDING_STATE.with(CorrockCrownStandingBlock.ROTATION, random.nextInt(16)), 2);
					}
				}
			}
		}

		private static void fillForward(ISeedReader world, BlockPos pos, Direction forward) {
			BlockPos.Mutable mutable = pos.toMutable();
			for (int i = 0; i <= 4; i++) {
				if (world.isAirBlock(mutable)) {
					world.setBlockState(mutable, CORROCK_BLOCK_STATE, 2);
					mutable.move(forward);
				} else {
					break;
				}
			}
		}

		private static void carveForward(ISeedReader world, BlockPos pos, Direction forward) {
			BlockPos.Mutable mutable = pos.toMutable();
			for (int i = 0; i <= 4; i++) {
				world.setBlockState(mutable, CAVE_AIR, 2);
				mutable.move(forward);
			}
		}

		private static void carveForward(ISeedReader world, BlockPos pos, Direction forward, int limit) {
			BlockPos.Mutable mutable = pos.toMutable();
			int carved = 0;
			for (int i = 0; i <= 4; i++) {
				if (world.getBlockState(mutable).getBlock() == CORROCK_BLOCK) {
					world.setBlockState(mutable, CAVE_AIR, 2);
					if (++carved == 2) {
						break;
					}
				}
				mutable.move(forward);
			}
		}

		private static BlockPos offsetPosForRotation(BlockPos pos, Rotation rotation) {
			switch (rotation) {
				default:
				case NONE:
					return pos;
				case CLOCKWISE_90:
					return pos.add(22, 0, 0);
				case CLOCKWISE_180:
					return pos.add(22, 0, 22);
				case COUNTERCLOCKWISE_90:
					return pos.add(0, 0, 22);
			}
		}

		//TODO: Ease-up conditions to lessen the chance for no center cave
		private static boolean isAreaCarvable(ISeedReader world, BlockPos origin) {
			int foundAirBlocks = 0;
			BlockPos.Mutable mutable = new BlockPos.Mutable();
			int posX = origin.getX();
			int posY = origin.getY();
			int posZ = origin.getZ();
			for (int x = posX - 24; x < posX + 24; x++) {
				for (int y = posY - 32; y < posY; y++) {
					for (int z = posZ - 24; z < posZ + 24; z++) {
						Block block = world.getBlockState(mutable.setPos(x, y, z)).getBlock();
						if (!CARVABLE_BLOCKS.contains(block)) {
							if (block == Blocks.AIR) {
								if (foundAirBlocks++ >= 307) {
									return false;
								}
								continue;
							}
							return false;
						}
					}
				}
			}
			return true;
		}

		@Override
		public boolean func_230383_a_(ISeedReader world, StructureManager structureManager, ChunkGenerator chunkGenerator, Random random, MutableBoundingBox boundingBox, ChunkPos chunkPos, BlockPos pos) {
			BlockPos originCarvingPos = pos.down(random.nextInt(4) + 5);
			BlockPos carvingPos = originCarvingPos;
			//TODO: If can't carve out cave then make some tunnels and chambers
			if (isAreaCarvable(world, carvingPos) || isAreaCarvable(world, carvingPos = originCarvingPos.add(16, 0, 16)) || isAreaCarvable(world, carvingPos = originCarvingPos.add(-16, 0, 16)) || isAreaCarvable(world, carvingPos = originCarvingPos.add(16, 0, -16)) || isAreaCarvable(world, carvingPos = originCarvingPos.add(-16, 0, -16))) {
				OctavesNoiseGenerator noiseGenerator = UNDERGROUND_NOISE.computeIfAbsent(world.getSeed(), seedL -> new OctavesNoiseGenerator(new SharedSeedRandom(seedL), IntStream.rangeClosed(-4, -3)));
				BlockPos.Mutable mutable = new BlockPos.Mutable();
				carvingPos = carvingPos.down(16);
				int originX = carvingPos.getX();
				int originY = carvingPos.getY();
				int originZ = carvingPos.getZ();
				for (int x = originX - 28; x < originX + 28; x++) {
					for (int y = originY - 18; y < originY + 18; y++) {
						for (int z = originZ - 28; z < originZ + 28; z++) {
							mutable.setPos(x, y, z);
							double localX = (x - originX) / 28.0F;
							double localY = (y - originY) / 18.0F;
							double localZ = (z - originZ) / 28.0F;
							double distanceSq = localX * localX + localY * localY + localZ * localZ;
							double frequency = 0.65F;
							double shapeNoise = noiseGenerator.func_205563_a(x * frequency, y * frequency, z * frequency) * 0.5F;
							if (distanceSq >= 0.7F + shapeNoise && distanceSq <= 1.2F + shapeNoise) {
								world.setBlockState(mutable, CORROCK_BLOCK_STATE, 2);
							} else if (distanceSq <= 0.9F + shapeNoise) {
								world.setBlockState(mutable, Blocks.CAVE_AIR.getDefaultState(), 2);
							}
						}
					}
				}
				int eumusPatches = random.nextInt(3) + 3;
				for (int i = 0; i < eumusPatches; i++) {
					mutable.setPos(originX + (random.nextInt(19) - random.nextInt(19)), originY + MathUtil.makeNegativeRandomly(random.nextInt(9) + 8, random), originZ + (random.nextInt(19) - random.nextInt(19)));
					createEumusPatch(world, mutable, noiseGenerator, random.nextInt(4) + 4);
				}

				createArena(world, carvingPos.up(2), random, this.arena);

				int stalactites = random.nextInt(3) + 3;
				for (int i = 0; i < stalactites; i++) {
					mutable.setPos(originX + (random.nextInt(15) - random.nextInt(15)), originY, originZ + (random.nextInt(15) - random.nextInt(15)));
					int startY = mutable.getY();
					for (int y = 0; y <= 18; y++) {
						mutable.setY(startY + y);
						if (world.getBlockState(mutable).getBlock() == CORROCK_BLOCK) {
							createStalactite(world, mutable, random, noiseGenerator, random.nextInt(2) + 3, random.nextInt(3) + 5);
							break;
						}
					}
				}
			}
			return true;
		}

	}
}
