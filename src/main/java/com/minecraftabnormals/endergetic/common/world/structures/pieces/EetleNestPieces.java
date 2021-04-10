package com.minecraftabnormals.endergetic.common.world.structures.pieces;

import com.google.common.collect.Sets;
import com.minecraftabnormals.abnormals_core.core.util.MathUtil;
import com.minecraftabnormals.endergetic.common.blocks.CorrockCrownBlock;
import com.minecraftabnormals.endergetic.common.blocks.CorrockCrownStandingBlock;
import com.minecraftabnormals.endergetic.common.blocks.CorrockCrownWallBlock;
import com.minecraftabnormals.endergetic.common.blocks.EetleEggsBlock;
import com.minecraftabnormals.endergetic.common.entities.eetle.BroodEetleEntity;
import com.minecraftabnormals.endergetic.common.world.structures.EEStructures;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.minecraftabnormals.endergetic.core.registry.EEEntities;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.SpawnReason;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.OctavesNoiseGenerator;
import net.minecraft.world.gen.PerlinNoiseGenerator;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.*;
import java.util.stream.IntStream;

public final class EetleNestPieces {
	private static Set<MutableBoundingBox> GENERATING_BOUNDS = new HashSet<>();
	private static final Direction[] ATTACHMENT_DIRECTIONS = Direction.values();
	private static final Direction[] TUNNEL_SIDES = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
	private static final Direction[] EGG_DIRECTIONS = Direction.values();
	private static final Block CORROCK_BLOCK = EEBlocks.CORROCK_END_BLOCK.get();
	private static final Block EUMUS = EEBlocks.EUMUS.get();
	private static final Block CROWN_STANDING = EEBlocks.CORROCK_CROWN_END_STANDING.get();
	private static final Block CROWN_WALL = EEBlocks.CORROCK_CROWN_END_WALL.get();
	private static final Block EETLE_EGSS = EEBlocks.EETLE_EGGS.get();
	private static final Block CORROCK = EEBlocks.CORROCK_END.get();
	public static final Set<Block> CARVABLE_BLOCKS = Sets.newHashSet(Blocks.STONE, Blocks.END_STONE, CORROCK_BLOCK, CORROCK, CROWN_STANDING, CROWN_WALL, EETLE_EGSS, EUMUS, EEBlocks.POISMOSS.get(), EEBlocks.EUMUS_POISMOSS.get());
	private static final BlockState CORROCK_BLOCK_STATE = CORROCK_BLOCK.getDefaultState();
	private static final BlockState CORROCK_STATE = CORROCK.getDefaultState();
	private static final BlockState EUMUS_STATE = EUMUS.getDefaultState();
	private static final BlockState CROWN_WALL_STATE = CROWN_WALL.getDefaultState();
	private static final BlockState CROWN_STANDING_STATE = CROWN_STANDING.getDefaultState();
	private static final BlockState EETLE_EGGS_STATE = EETLE_EGSS.getDefaultState();
	private static final Map<Long, PerlinNoiseGenerator> SURFACE_NOISE = new HashMap<>();
	private static final Map<Long, OctavesNoiseGenerator> UNDERGROUND_NOISE = new HashMap<>();

	public static boolean isNotInsideGeneratingBounds(BlockPos pos) {
		for (MutableBoundingBox boundingBox : GENERATING_BOUNDS) {
			if (boundingBox.isVecInside(pos)) {
				return false;
			}
		}
		return true;
	}

	public static class EetleNestPiece extends StructurePiece {
		private static final ResourceLocation ARENA = new ResourceLocation(EndergeticExpansion.MOD_ID, "eetle_nest/arena");
		private final Template arena;
		private NestDesign nestDesign = null;

		public EetleNestPiece(TemplateManager manager, CompoundNBT compoundNBT) {
			super(EEStructures.PieceTypes.EETLE_NEST, compoundNBT);
			this.arena = manager.getTemplate(ARENA);
		}

		public EetleNestPiece(TemplateManager manager, BlockPos corner) {
			super(EEStructures.PieceTypes.EETLE_NEST, 0);
			this.boundingBox = new MutableBoundingBox(corner.add(-64, -48, -64), corner.add(64, 6, 64));
			this.arena = manager.getTemplate(ARENA);
		}

		private static void transformSurface(ISeedReader world, int x, int z, int maxDepth, int depthScale, double noise, MutableBoundingBox boundingBox, BlockState topState) {
			BlockPos.Mutable mutable = new BlockPos.Mutable(x, boundingBox.minY, z);
			if (boundingBox.isVecInside(mutable)) {
				int startingY = world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, x, z);
				int depth = (int) (Math.abs(noise) * depthScale + 14.0D);
				BlockState fillState = topState;
				for (int y = startingY, d = 0; y > maxDepth && d < depth; y--, d++) {
					mutable.setY(y);
					if (boundingBox.isVecInside(mutable)) {
						if (world.getBlockState(mutable).getBlock() == Blocks.END_STONE) {
							world.setBlockState(mutable, fillState, 2);
							fillState = EUMUS_STATE;
						} else if (world.isAirBlock(mutable)) {
							fillState = topState;
						}
					}
				}
			}
		}

		protected static void createEumusPatch(ISeedReader world, BlockPos origin, OctavesNoiseGenerator noiseGenerator, int radius, MutableBoundingBox bounds) {
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
							if (bounds.isVecInside(mutable) && world.getBlockState(mutable).getBlock() == CORROCK_BLOCK) {
								world.setBlockState(mutable, EUMUS_STATE, 2);
							}
						}
					}
				}
			}
		}

		@Override
		protected void readAdditional(CompoundNBT tagCompound) {
		}

		@SuppressWarnings("deprecation")
		@Override
		public boolean func_230383_a_(ISeedReader world, StructureManager structureManager, ChunkGenerator chunkGenerator, Random random, MutableBoundingBox bounds, ChunkPos chunkPos, BlockPos pos) {
			GENERATING_BOUNDS.add(this.boundingBox);
			int originX = pos.getX();
			int originZ = pos.getZ();
			pos = new BlockPos(originX, chunkGenerator.getNoiseHeight(originX, originZ, Heightmap.Type.WORLD_SURFACE_WG), originZ);

			PerlinNoiseGenerator surfaceNoise = SURFACE_NOISE.computeIfAbsent(world.getSeed(), seedL -> new PerlinNoiseGenerator(new SharedSeedRandom(seedL), IntStream.rangeClosed(-3, 0)));
			int radius = 32;
			for (int x = -radius; x < radius; x++) {
				for (int z = -radius; z < radius; z++) {
					double noise = surfaceNoise.noiseAt(x, z, true);
					double areaNoise = noise * 12.0D - 7.0D;
					double distanceSq = x * x + z * z + areaNoise * areaNoise;
					if (distanceSq <= (radius - 3) * (radius - 3)) {
						int posX = originX + x;
						int posZ = originZ + z;
						transformSurface(world, posX, posZ, world.getSeaLevel() / 2, 40, noise, bounds, CORROCK_BLOCK_STATE);
					} else if (distanceSq <= radius * radius) {
						int posX = originX + x;
						int posZ = originZ + z;
						transformSurface(world, posX, posZ, world.getSeaLevel() / 2, 24, noise, bounds, EUMUS_STATE);
					}
				}
			}

			BlockPos.Mutable mutable = new BlockPos.Mutable();
			BlockPos carvingPos = pos.down(24);
			int originY = carvingPos.getY();
			OctavesNoiseGenerator undergroundNoise = UNDERGROUND_NOISE.computeIfAbsent(world.getSeed(), seedL -> new OctavesNoiseGenerator(new SharedSeedRandom(seedL), IntStream.rangeClosed(-4, -3)));

			if (this.nestDesign == null) {
				this.nestDesign = new NestDesign(random);
			}

			NestDesign nestDesign = this.nestDesign;
			NestCore nestCore = nestDesign.core;
			if (!nestCore.stateMap.setup) {
				nestCore.setup(originX, originY, originZ, random, undergroundNoise);
			}
			nestCore.generate(world, bounds);

			for (EumusPatch eumusPatch : nestDesign.eumusPatches) {
				eumusPatch.generate(world, carvingPos, undergroundNoise, bounds);
			}

			nestDesign.arena.generate(world, carvingPos.up(2), random, this.arena, bounds);

			Map<Pair<Integer, Integer>, Integer> horizontalToMaxHeight = nestCore.horizontalToMaxHeight;
			StateMap stateMap = nestCore.stateMap;
			for (NestStalactite stalactite : nestDesign.stalactites) {
				int placingX = originX + stalactite.xOffset;
				int placingZ = originZ + stalactite.zOffset;
				mutable.setPos(placingX, originY, placingZ);
				mutable.setY(horizontalToMaxHeight.computeIfAbsent(Pair.of(placingX, placingZ), pair -> {
					for (int i = 0; i < 18; i++) {
						mutable.move(Direction.UP);
						if (stateMap.getBlockState(mutable).getBlock() == CORROCK_BLOCK) {
							return mutable.getY();
						}
					}
					return originY + 18;
				}));
				stalactite.generate(world, mutable, random, undergroundNoise, bounds);
			}

			List<CorrockShelf> corrockShelves = nestCore.corrockShelves;
			for (NestTunnel tunnel : nestDesign.tunnels) {
				if (tunnel.airPositions.isEmpty()) {
					Direction facing = tunnel.facing;
					mutable.setPos(carvingPos);
					for (int i = 0; i < 18; i++) {
						mutable.move(facing);
						if (stateMap.getBlockState(mutable).getBlock() == CORROCK_BLOCK) {
							break;
						}
					}
					tunnel.setup(mutable, chunkGenerator, undergroundNoise, random);
					BlockPos tunnelStart = tunnel.start;
					//Helps reduce the chance of shelves generating near tunnel entrances
					corrockShelves.removeIf(corrockShelf -> tunnelStart.distanceSq(corrockShelf.pos) <= 64);
				}
			}
			nestCore.generateCorrockShelfs(world, bounds);

			//Generate caves first to ensure they don't block other tunnels
			for (NestTunnel tunnel : nestDesign.tunnels) {
				tunnel.generateCave(world, undergroundNoise, bounds);
			}

			for (NestTunnel tunnel : nestDesign.tunnels) {
				tunnel.generate(world, random, bounds);
			}

			//Generate decorations last to prevent tunnels cutting them off
			for (NestTunnel tunnel : nestDesign.tunnels) {
				tunnel.generateDecorations(world, undergroundNoise, random, bounds);
			}

			nestCore.generateCorrockShelfDecorations(world, bounds);
			nestCore.generateCorrockPatches(world, random, bounds);
			return true;
		}

		/**
		 * Stores the 'design' of the nest
		 */
		private static class NestDesign {
			private final List<EumusPatch> eumusPatches = new ArrayList<>();
			private final NestArena arena;
			private final NestCore core = new NestCore();
			private final List<NestStalactite> stalactites = new ArrayList<>();
			private final List<NestTunnel> tunnels = new ArrayList<>();

			private NestDesign(Random random) {
				int eumusPatchCount = random.nextInt(3) + 3;
				for (int i = 0; i < eumusPatchCount; i++) {
					this.eumusPatches.add(new EumusPatch(EumusPatch.PatchType.CORE, random));
				}
				this.arena = new NestArena(random);
				int stalactiteCount = random.nextInt(3) + 3;
				for (int i = 0; i < stalactiteCount; i++) {
					this.stalactites.add(new NestStalactite(random));
				}
				EetleEggsBlock.shuffleDirections(TUNNEL_SIDES, random);
				List<NestTunnel> tunnels = this.tunnels;
				for (Direction side : TUNNEL_SIDES) {
					if (tunnels.size() < 3 || random.nextBoolean()) {
						tunnels.add(new NestTunnel(random, side));
					}
					if (random.nextBoolean()) {
						tunnels.add(new NestTunnel(random, side));
					}
				}
				for (int i = 0; i < 2; i++) {
					tunnels.get(random.nextInt(tunnels.size())).goesToSurface = true;
				}
				Set<Direction> sidesWithCaves = new HashSet<>();
				for (NestTunnel tunnel : this.tunnels) {
					if (!tunnel.goesToSurface && !sidesWithCaves.contains(tunnel.facing)) {
						tunnel.nestCave = new NestTunnel.NestCave(random);
						sidesWithCaves.add(tunnel.facing);
					}
				}
			}
		}

		private static class NestCore {
			private final StateMap stateMap = new StateMap();
			private final List<BlockPos> corrockPatchPositions = new ArrayList<>();
			private final List<CorrockShelf> corrockShelves = new ArrayList<>();
			private final Map<Pair<Integer, Integer>, Integer> horizontalToMaxHeight = new HashMap<>(5);

			private void setup(int originX, int originY, int originZ, Random random, OctavesNoiseGenerator undergroundNoise) {
				StateMap stateMap = this.stateMap;
				BlockPos.Mutable mutable = new BlockPos.Mutable();
				int horizontalRadius = 28;
				int verticalRadius = 18;
				for (int x = originX - horizontalRadius; x < originX + horizontalRadius; x++) {
					for (int z = originZ - horizontalRadius; z < originZ + horizontalRadius; z++) {
						for (int y = originY - verticalRadius; y < originY + verticalRadius; y++) {
							mutable.setPos(x, y, z);
							double localX = (x - originX) / (float) horizontalRadius;
							double localY = (y - originY) / (float) verticalRadius;
							double localZ = (z - originZ) / (float) horizontalRadius;
							double distanceSq = localX * localX + localY * localY + localZ * localZ;
							double frequency = 0.65F;
							double shapeNoise = undergroundNoise.func_205563_a(x * frequency, y * frequency, z * frequency) * 0.5F;
							if (distanceSq >= 0.7F + shapeNoise && distanceSq <= 1.2F + shapeNoise) {
								stateMap.setBlockState(mutable, CORROCK_BLOCK_STATE);
							} else if (distanceSq <= 0.9F + shapeNoise) {
								stateMap.setBlockState(mutable, Blocks.CAVE_AIR.getDefaultState());
							}
						}
					}
				}
				stateMap.setup = true;
				int cornerX = originX - 28;
				int cornerZ = originZ - 28;
				List<BlockPos> corrockPatchPositions = this.corrockPatchPositions;
				for (int i = 0; i < 64; i++) {
					corrockPatchPositions.add(new BlockPos(cornerX + random.nextInt(57), originY - random.nextInt(8), cornerZ + random.nextInt(57)));
				}

				List<CorrockShelf> corrockShelves = this.corrockShelves;
				int shelfs = random.nextInt(3) + 2;
				for (int i = 0; i < shelfs; i++) {
					Direction horizontal = Direction.Plane.HORIZONTAL.random(random);
					BlockPos.Mutable mutable1 = new BlockPos.Mutable(originX, originY + MathUtil.makeNegativeRandomly(random.nextInt(8), random), originZ);
					if (random.nextFloat() < 0.25F) {
						for (int j = 0; j < horizontalRadius; j++) {
							mutable1.move(horizontal);
							if (stateMap.getBlockState(mutable1).getBlock() == CORROCK_BLOCK) {
								corrockShelves.add(new CorrockShelf(mutable1.offset(horizontal.rotateY(), random.nextInt(4) - random.nextInt(4)), random));
							}
						}
					} else {
						int offset = random.nextInt(horizontalRadius + 1);
						mutable1.move(horizontal, offset);
						Direction leftOrRight = random.nextBoolean() ? horizontal.rotateY() : horizontal.rotateYCCW();
						for (int j = 0; j < horizontalRadius; j++) {
							mutable1.move(leftOrRight);
							if (stateMap.getBlockState(mutable1).getBlock() == CORROCK_BLOCK) {
								corrockShelves.add(new CorrockShelf(mutable1, random));
							}
						}
					}
				}
			}

			private void generate(ISeedReader world, MutableBoundingBox bounds) {
				this.stateMap.generate(world, bounds);
			}

			private void generateCorrockPatches(ISeedReader world, Random random, MutableBoundingBox bounds) {
				BlockPos.Mutable mutable = new BlockPos.Mutable();
				List<BlockPos> corrockPatchPositions = this.corrockPatchPositions;
				for (int i = 0; i < corrockPatchPositions.size(); i++) {
					BlockPos corrockPos = corrockPatchPositions.get(i);
					if (bounds.isVecInside(corrockPos)) {
						mutable.setPos(corrockPos);
						for (int y = 0; y < 18; y++) {
							Block block = world.getBlockState(mutable).getBlock();
							if (block == CORROCK_BLOCK) {
								break;
							}
							mutable.move(Direction.DOWN);
						}
						corrockPatchPositions.set(i, corrockPos = mutable.toImmutable());
					}
					for (int j = 0; j < 32; j++) {
						mutable.setAndOffset(corrockPos, random.nextInt(8) - random.nextInt(8), random.nextInt(4) - random.nextInt(4), random.nextInt(8) - random.nextInt(8));
						if (bounds.isVecInside(mutable)) {
							if (world.isAirBlock(mutable) && CORROCK_STATE.isValidPosition(world, mutable)) {
								Block below = world.getBlockState(mutable.down()).getBlock();
								if (below == CORROCK_BLOCK || random.nextFloat() < 0.4F) {
									world.setBlockState(mutable, CORROCK_STATE, 2);
								}
							}
						}
					}
				}
			}

			private void generateCorrockShelfs(ISeedReader world, MutableBoundingBox bounds) {
				for (CorrockShelf shelf : this.corrockShelves) {
					shelf.generate(world, bounds);
				}
			}

			private void generateCorrockShelfDecorations(ISeedReader world, MutableBoundingBox bounds) {
				for (CorrockShelf shelf : this.corrockShelves) {
					shelf.generateDecorations(world, bounds);
				}
			}
		}

		private static class EumusPatch {
			private final int radius;
			private final int xOffset;
			private final int yOffset;
			private final int zOffset;

			private EumusPatch(PatchType type, Random random) {
				this.radius = random.nextInt(type.boundRadius) + type.minRadius;
				int maxHorizontalOffset = type.maxHorizontalOffset;
				this.xOffset = random.nextInt(maxHorizontalOffset) - random.nextInt(maxHorizontalOffset);
				int halfMaxYOffset = type.halfMaxYOffset;
				this.yOffset = (int) MathUtil.makeNegativeRandomly(random.nextInt(halfMaxYOffset + 1) + halfMaxYOffset, random);
				this.zOffset = random.nextInt(maxHorizontalOffset) - random.nextInt(maxHorizontalOffset);
			}

			private void generate(ISeedReader world, BlockPos center, OctavesNoiseGenerator noiseGenerator, MutableBoundingBox bounds) {
				createEumusPatch(world, center.add(this.xOffset, this.yOffset, this.zOffset), noiseGenerator, this.radius, bounds);
			}

			enum PatchType {
				CORE(4, 3, 19, 8),
				SMALL(2, 2, 10, 3),
				MEDIUM(3, 2, 13, 5),
				LARGE(3, 3, 15, 6);

				private final int minRadius, boundRadius;
				private final int maxHorizontalOffset;
				private final int halfMaxYOffset;

				PatchType(int minRadius, int boundRadius, int maxHorizontalOffset, int halfMaxYOffset) {
					this.minRadius = minRadius;
					this.boundRadius = boundRadius;
					this.maxHorizontalOffset = maxHorizontalOffset;
					this.halfMaxYOffset = halfMaxYOffset;
				}
			}
		}

		private static class NestArena {
			private static final EnumMap<Rotation, BottomStepsRotationInfo> BOTTOM_STEPS_ROTATION_INFO = Util.make(new EnumMap<>(Rotation.class), map -> {
				map.put(
						Rotation.NONE,
						new BottomStepsRotationInfo(
								1, -1, Direction.NORTH,
								1, 4, 2, 5,
								-2, 3, -1, 4,
								-4, 1, 0, -3,
								Direction.NORTH, Direction.EAST
						)
				);
				map.put(
						Rotation.CLOCKWISE_90,
						new BottomStepsRotationInfo(
								1, 1, Direction.SOUTH,
								-5, 1, -4, 2,
								-3, -2, -2, -1,
								-1, -5, 4, -1,
								Direction.EAST, Direction.SOUTH
						)
				);
				map.put(
						Rotation.CLOCKWISE_180,
						new BottomStepsRotationInfo(
								-1, 1, Direction.SOUTH,
								-2, -4, -1, -3,
								1, -3, 2, -2,
								4, 1, 1, 4,
								Direction.NORTH, Direction.WEST
						)
				);
				map.put(
						Rotation.COUNTERCLOCKWISE_90,
						new BottomStepsRotationInfo(
								-1, -1, Direction.NORTH,
								3, -2, 4, -1,
								2, 1, 3, 2,
								0, 5, -4, 1,
								Direction.WEST, Direction.NORTH
						)
				);
			});
			private final Rotation rotation;
			private final Direction bottomDoorDirection;
			private final Rotation stepsRotation;
			private final Direction topDoorDirection;
			private final Direction topDoorLeftOrRight;
			private final StateMap smallSteps = new StateMap();
			private final StateMap mediumStep = new StateMap();
			private final StateMap largeStep = new StateMap();

			private NestArena(Random random) {
				this.rotation = Rotation.randomRotation(random);
				this.bottomDoorDirection = Direction.Plane.HORIZONTAL.random(random);
				this.stepsRotation = Rotation.randomRotation(random);
				this.topDoorDirection = Direction.Plane.HORIZONTAL.random(random);
				this.topDoorLeftOrRight = random.nextBoolean() ? this.topDoorDirection.rotateY() : this.topDoorDirection.rotateYCCW();
			}

			private static void searchAndPlaceTopGate(ISeedReader world, BlockPos start, Random random, Direction opposite, MutableBoundingBox bounds) {
				BlockPos.Mutable mutable = start.toMutable();
				for (int i = 0; i <= 7; i++) {
					if (bounds.isVecInside(mutable)) {
						if (world.getBlockState(mutable).getBlock() == CORROCK_BLOCK) {
							BlockPos up = mutable.up();
							world.setBlockState(up, CORROCK_BLOCK_STATE, 2);
							tryToPlaceCrownAroundPos(world, up, random, bounds);
							break;
						}
					}
					mutable.move(opposite);
				}
			}

			private static void createOuterStairPiece(ISeedReader world, BlockPos start, Random random, Direction opposite, MutableBoundingBox bounds) {
				BlockPos.Mutable mutable = start.toMutable();
				for (int i = 0; i <= 8; i++) {
					if (bounds.isVecInside(mutable)) {
						world.setBlockState(mutable, CORROCK_BLOCK_STATE, 2);
						mutable.move(opposite);
						if (world.getBlockState(mutable).getBlock() == CORROCK_BLOCK) {
							break;
						}
					}
				}
				if (random.nextBoolean()) {
					tryToPlaceCrownAroundPos(world, start, random, bounds);
				}
			}

			private static void createLedgePiece(ISeedReader world, BlockPos middle, Direction right, Direction left, Random random, int radius, boolean generateCrowns, MutableBoundingBox bounds) {
				BlockPos.Mutable leftMutable = middle.toMutable();
				BlockPos.Mutable rightMutable = middle.toMutable();
				for (int i = 0; i <= radius; i++) {
					if (bounds.isVecInside(leftMutable)) {
						world.setBlockState(leftMutable, CORROCK_BLOCK_STATE, 2);
					}
					if (bounds.isVecInside(rightMutable)) {
						world.setBlockState(rightMutable, CORROCK_BLOCK_STATE, 2);
					}
					if (generateCrowns && i == radius) {
						if (random.nextBoolean()) {
							tryToPlaceCrownAroundPos(world, leftMutable, random, bounds);
						}
						if (random.nextBoolean()) {
							tryToPlaceCrownAroundPos(world, rightMutable, random, bounds);
						}
					}
					leftMutable.move(left);
					rightMutable.move(right);
				}
			}

			private static int randomRange(int min, int max, Random random) {
				return random.nextInt(max - min + 1) + min;
			}

			private static void tryToPlaceCrownAroundPos(ISeedReader world, BlockPos pos, Random random, MutableBoundingBox bounds) {
				EetleEggsBlock.shuffleDirections(ATTACHMENT_DIRECTIONS, random);
				for (Direction direction : ATTACHMENT_DIRECTIONS) {
					if (direction.getAxis() == Direction.Axis.Y) {
						boolean upsideDown = direction == Direction.DOWN;
						BlockPos offset = upsideDown ? pos.down() : pos.up();
						if (bounds.isVecInside(offset)) {
							if (world.isAirBlock(offset)) {
								world.setBlockState(offset, CROWN_STANDING_STATE.with(CorrockCrownStandingBlock.UPSIDE_DOWN, upsideDown).with(CorrockCrownStandingBlock.ROTATION, random.nextInt(16)), 2);
								break;
							}
						}
					} else {
						BlockPos offset = pos.offset(direction);
						if (bounds.isVecInside(offset)) {
							if (world.isAirBlock(offset)) {
								world.setBlockState(offset, CROWN_WALL_STATE.with(CorrockCrownWallBlock.FACING, direction), 2);
								break;
							}
						}
					}
				}
			}

			private static void createHorizontalCrownedCluster(StateMap stateMap, BlockPos pos, Random random) {
				BlockPos up = pos.up(2);
				for (Direction horizontal : Direction.Plane.HORIZONTAL) {
					if (random.nextFloat() < 0.8F) {
						BlockPos offset = pos.offset(horizontal);
						stateMap.setBlockState(offset, CORROCK_BLOCK_STATE);
						BlockPos offsetUp = offset.up();
						if (random.nextFloat() < 0.15F) {
							stateMap.setBlockState(offsetUp, CROWN_STANDING_STATE.with(CorrockCrownStandingBlock.ROTATION, random.nextInt(16)));
						}
					}
					if (random.nextFloat() < 0.8F) {
						BlockPos offset = up.offset(horizontal);
						stateMap.setBlockState(offset, CORROCK_BLOCK_STATE);
						BlockPos offsetUp = offset.up();
						if (random.nextFloat() < 0.2F) {
							stateMap.setBlockState(offsetUp, CROWN_STANDING_STATE.with(CorrockCrownStandingBlock.ROTATION, random.nextInt(16)));
						}
					}
				}
			}

			private static void fillForward(ISeedReader world, BlockPos pos, Direction forward, MutableBoundingBox bounds) {
				BlockPos.Mutable mutable = pos.toMutable();
				for (int i = 0; i <= 4; i++) {
					if (bounds.isVecInside(mutable)) {
						if (world.isAirBlock(mutable)) {
							world.setBlockState(mutable, CORROCK_BLOCK_STATE, 2);
							mutable.move(forward);
						} else {
							break;
						}
					}
				}
			}

			private static void createLargeStepBottom(StateMap stateMap, BlockPos pos, Random random) {
				for (Direction horizontal : Direction.Plane.HORIZONTAL) {
					if (random.nextBoolean()) {
						BlockPos offset = pos.offset(horizontal);
						stateMap.setBlockState(offset, CORROCK_BLOCK_STATE);
						if (random.nextFloat() < 0.25F) {
							stateMap.setBlockState(offset.up(), CROWN_STANDING_STATE.with(CorrockCrownStandingBlock.ROTATION, random.nextInt(16)));
						}
					} else if (random.nextBoolean()) {
						BlockPos offset = pos.offset(horizontal);
						stateMap.setBlockState(offset, CORROCK_BLOCK_STATE);
						stateMap.setBlockState(offset.up(), CORROCK_BLOCK_STATE);
						if (random.nextFloat() < 0.25F) {
							stateMap.setBlockState(offset.up(2), CROWN_STANDING_STATE.with(CorrockCrownStandingBlock.ROTATION, random.nextInt(16)));
						}
						if (random.nextBoolean()) {
							BlockPos doubleOffset = pos.offset(horizontal, 2);
							stateMap.setBlockState(doubleOffset, CORROCK_BLOCK_STATE);
							if (random.nextFloat() < 0.25F) {
								stateMap.setBlockState(doubleOffset.up(), CROWN_STANDING_STATE.with(CorrockCrownStandingBlock.ROTATION, random.nextInt(16)));
							}
						}
					}
				}
			}

			private static void createLargeStepTop(StateMap stateMap, BlockPos pos, Random random) {
				for (Direction horizontal : Direction.Plane.HORIZONTAL) {
					BlockPos offset = pos.offset(horizontal);
					stateMap.setBlockState(offset, CORROCK_BLOCK_STATE);
					if (random.nextFloat() < 0.2F) {
						stateMap.setBlockState(offset.up(), CROWN_STANDING_STATE.with(CorrockCrownStandingBlock.ROTATION, random.nextInt(16)));
					}
					if (random.nextFloat() < 0.25F) {
						BlockPos otherOffset = offset.offset(Direction.Plane.HORIZONTAL.random(random));
						stateMap.setBlockState(otherOffset, CORROCK_BLOCK_STATE);
						if (random.nextFloat() < 0.2F) {
							stateMap.setBlockState(otherOffset.up(), CROWN_STANDING_STATE.with(CorrockCrownStandingBlock.ROTATION, random.nextInt(16)));
						}
						stateMap.setBlockState(offset.down(), CORROCK_BLOCK_STATE);
					}
				}
			}

			private static void carveForward(ISeedReader world, BlockPos pos, Direction forward, MutableBoundingBox bounds) {
				BlockPos.Mutable mutable = pos.toMutable();
				for (int i = 0; i <= 4; i++) {
					if (bounds.isVecInside(mutable)) {
						world.setBlockState(mutable, CAVE_AIR, 2);
						mutable.move(forward);
					}
				}
			}

			private static void carveForwardLimited(ISeedReader world, BlockPos pos, Direction forward, MutableBoundingBox bounds) {
				BlockPos.Mutable mutable = pos.toMutable();
				int carved = 0;
				for (int i = 0; i <= 4; i++) {
					if (bounds.isVecInside(mutable)) {
						if (world.getBlockState(mutable).getBlock() == CORROCK_BLOCK) {
							world.setBlockState(mutable, CAVE_AIR, 2);
							if (++carved == 2) {
								break;
							}
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

			private void createWayUp(ISeedReader world, BlockPos center, Direction horizontal, Direction leftOrRight, Random random, MutableBoundingBox bounds) {
				BlockPos.Mutable mutable = center.toMutable();
				mutable.move(horizontal, 7);

				carveForward(world, mutable, horizontal, bounds);
				carveForward(world, mutable.up(), horizontal, bounds);
				carveForward(world, mutable.up(2), horizontal, bounds);

				Direction cw = horizontal.rotateY();
				BlockPos rotateCW = mutable.offset(cw);
				carveForward(world, rotateCW, horizontal, bounds);
				carveForward(world, rotateCW.up(), horizontal, bounds);

				Direction ccw = horizontal.rotateYCCW();
				BlockPos rotateCCW = mutable.offset(ccw);
				carveForward(world, rotateCCW, horizontal, bounds);
				carveForward(world, rotateCCW.up(), horizontal, bounds);

				BlockPos outsideCenter = mutable.offset(horizontal, 2).down();

				for (int l = 0; l < 4; l++) {
					BlockPos offset = outsideCenter.offset(horizontal, l);
					createLedgePiece(world, offset, cw, ccw, random, 4 - l, true, bounds);
					createLedgePiece(world, offset.down(), cw, ccw, random, 2 - l, false, bounds);
					if (l == 3 && random.nextFloat() < 0.25F) {
						tryToPlaceCrownAroundPos(world, offset, random, bounds);
					}
				}

				BlockPos outsideThree = outsideCenter.offset(horizontal, 3);
				BlockPos firstStairEdge = outsideThree.offset(leftOrRight, 2);
				if (bounds.isVecInside(firstStairEdge)) {
					world.setBlockState(firstStairEdge, CORROCK_BLOCK_STATE, 2);
					tryToPlaceCrownAroundPos(world, firstStairEdge, random, bounds);
				}

				Direction opposite = horizontal.getOpposite();
				mutable = outsideThree.toMutable();
				mutable.move(leftOrRight, 2);
				int startY = mutable.getY();
				int term = 1;
				for (int n = 0; n < 6; n++) {
					term += (n % 2);
					mutable.setY(startY + term);
					createOuterStairPiece(world, mutable.move(leftOrRight), random, opposite, bounds);
				}
				createOuterStairPiece(world, mutable.move(leftOrRight).move(opposite), random, opposite, bounds);
				createOuterStairPiece(world, mutable.move(leftOrRight).move(opposite), random, opposite, bounds);
				Direction leftOrRightOpposite = leftOrRight.getOpposite();
				carveForwardLimited(world, mutable.move(Direction.UP).move(leftOrRightOpposite), opposite, bounds);
				carveForwardLimited(world, mutable.move(leftOrRightOpposite), opposite, bounds);
				searchAndPlaceTopGate(world, mutable.move(leftOrRight, 2), random, opposite, bounds);
				searchAndPlaceTopGate(world, mutable.move(leftOrRightOpposite, 3), random, opposite, bounds);
			}

			private void createCenterSteps(ISeedReader world, BlockPos center, Rotation rotation, Random random, MutableBoundingBox bounds) {
				StateMap smallStep = this.smallSteps;
				BottomStepsRotationInfo bottomRotationInfo = BOTTOM_STEPS_ROTATION_INFO.get(rotation);

				int xDirection = bottomRotationInfo.xDirection;
				int zDirection = bottomRotationInfo.zDirection;
				Direction ledgeFillDirection = bottomRotationInfo.ledgeFillDirection;
				fillForward(world, center.add(3 * xDirection, 0, 4 * zDirection), ledgeFillDirection, bounds);
				fillForward(world, center.add(4 * xDirection, 0, 3 * zDirection), ledgeFillDirection, bounds);
				fillForward(world, center.add(5 * xDirection, 0, 3 * zDirection), ledgeFillDirection, bounds);
				fillForward(world, center.add(6 * xDirection, 0, 3 * zDirection), ledgeFillDirection, bounds);

				center = center.down(4);

				if (!smallStep.setup) {
					int centerX = center.getX();
					int centerY = center.getY();
					int centerZ = center.getZ();
					StateMap.fillAreaWithBlockCube(smallStep, centerX + bottomRotationInfo.firstSmallStepMinX, centerY, centerZ + bottomRotationInfo.firstSmallStepMinZ, centerX + bottomRotationInfo.firstSmallStepMaxX, centerY, centerZ + bottomRotationInfo.firstSmallStepMaxZ, CORROCK_BLOCK_STATE);
					if (random.nextBoolean()) {
						smallStep.setBlockState(center.add(randomRange(bottomRotationInfo.firstSmallStepMinX, bottomRotationInfo.firstSmallStepMaxX, random), 1, randomRange(bottomRotationInfo.firstSmallStepMinZ, bottomRotationInfo.firstSmallStepMaxZ, random)), CROWN_STANDING_STATE.with(CorrockCrownStandingBlock.ROTATION, random.nextInt(16)));
					}

					StateMap.fillAreaWithBlockCube(smallStep, centerX + bottomRotationInfo.secondSmallStepMinX, centerY, centerZ + bottomRotationInfo.secondSmallStepMinZ, centerX + bottomRotationInfo.secondSmallStepMaxX, centerY + 1, centerZ + +bottomRotationInfo.secondSmallStepMaxZ, CORROCK_BLOCK_STATE);
					smallStep.setBlockState(center.add(randomRange(bottomRotationInfo.secondSmallStepMinX, bottomRotationInfo.secondSmallStepMaxX, random), 0, randomRange(bottomRotationInfo.secondSmallStepMinZ, bottomRotationInfo.secondSmallStepMaxZ, random)), CAVE_AIR);
					if (random.nextFloat() < 0.6F) {
						smallStep.setBlockState(center.add(randomRange(bottomRotationInfo.secondSmallStepMinX, bottomRotationInfo.secondSmallStepMaxX, random), 2, randomRange(bottomRotationInfo.secondSmallStepMinZ, bottomRotationInfo.secondSmallStepMaxZ, random)), CROWN_STANDING_STATE.with(CorrockCrownStandingBlock.ROTATION, random.nextInt(16)));
					}
					smallStep.setup = true;
				}
				smallStep.generate(world, bounds);

				this.createMediumStep(world, center.add(bottomRotationInfo.mediumStepOffsetX, 0, bottomRotationInfo.mediumStepOffsetZ), random, bottomRotationInfo.mediumStepDirection, bounds);
				this.createLargeStep(world, center.add(bottomRotationInfo.largeStepOffsetX, 0, bottomRotationInfo.largeStepOffsetZ), random, bottomRotationInfo.largeStepDirection, bounds);
			}

			private void createMediumStep(ISeedReader world, BlockPos pos, Random random, Direction direction, MutableBoundingBox bounds) {
				StateMap mediumStep = this.mediumStep;
				if (!mediumStep.setup) {
					BlockPos.Mutable mutable = pos.toMutable();
					int startY = mutable.getY();
					for (int y = 0; y < 3; y++) {
						mutable.setY(startY + y);
						mediumStep.setBlockState(mutable, CORROCK_BLOCK_STATE);
					}
					createHorizontalCrownedCluster(mediumStep, pos, random);
					BlockPos offset = pos.offset(direction);
					mutable = offset.toMutable();
					for (int y = 0; y < 3; y++) {
						mutable.setY(startY + y);
						mediumStep.setBlockState(mutable, CORROCK_BLOCK_STATE);
					}
					createHorizontalCrownedCluster(mediumStep, offset, random);
					mediumStep.setup = true;
				}
				mediumStep.stateMap.forEach((posEntry, state) -> {
					if (bounds.isVecInside(posEntry)) {
						if (state.getBlock() instanceof CorrockCrownBlock) {
							if (world.isAirBlock(posEntry)) {
								world.setBlockState(posEntry, state, 2);
							}
						} else {
							world.setBlockState(posEntry, state, 2);
						}
					}
				});
			}

			private void createLargeStep(ISeedReader world, BlockPos pos, Random random, Direction direction, MutableBoundingBox bounds) {
				StateMap largeStep = this.largeStep;
				if (!largeStep.setup) {
					BlockPos.Mutable mutable = pos.toMutable();
					int startY = mutable.getY();
					for (int y = 0; y < 4; y++) {
						mutable.setY(startY + y);
						largeStep.setBlockState(mutable, CORROCK_BLOCK_STATE);
					}
					createLargeStepBottom(largeStep, pos, random);
					createLargeStepTop(largeStep, mutable, random);
					BlockPos offset = pos.offset(direction);
					mutable = offset.toMutable();
					for (int y = 0; y < 4; y++) {
						mutable.setY(startY + y);
						largeStep.setBlockState(mutable, CORROCK_BLOCK_STATE);
					}
					createLargeStepBottom(largeStep, offset, random);
					createLargeStepTop(largeStep, mutable, random);
					largeStep.setup = true;
				}
				largeStep.stateMap.forEach((posEntry, state) -> {
					if (bounds.isVecInside(posEntry)) {
						if (state.getBlock() instanceof CorrockCrownBlock) {
							if (world.isAirBlock(posEntry)) {
								world.setBlockState(posEntry, state, 2);
							}
						} else {
							world.setBlockState(posEntry, state, 2);
						}
					}
				});
			}

			private void generate(ISeedReader world, BlockPos origin, Random random, Template arena, MutableBoundingBox bounds) {
				BlockPos min = origin.add(-11, -16, -11);
				BlockPos max = origin.add(11, 1, 11);
				arena.func_237146_a_(world, offsetPosForRotation(min, this.rotation), origin, new PlacementSettings().setRotation(this.rotation).setBoundingBox(bounds), random, 2);
				BlockPos.Mutable mutable = new BlockPos.Mutable();
				//Fill in bottom areas to prevent floating cases
				int extraFillBottomY = origin.getY() - 16;
				for (int x = min.getX(); x <= max.getX(); x++) {
					for (int z = min.getZ(); z <= max.getZ(); z++) {
						mutable.setPos(x, extraFillBottomY, z);
						if (bounds.isVecInside(mutable)) {
							if (world.getBlockState(mutable).getBlock() == Blocks.GOLD_BLOCK) {
								world.setBlockState(mutable, CORROCK_BLOCK_STATE, 2);
								for (int y = extraFillBottomY; y > extraFillBottomY - 3; y--) {
									mutable.setY(y);
									world.setBlockState(mutable, CORROCK_BLOCK_STATE, 2);
								}
							}
						}
					}
				}

				mutable = origin.down(3).toMutable();
				if (bounds.isVecInside(mutable)) {
					BroodEetleEntity broodEetle = EEEntities.BROOD_EETLE.get().create(world.getWorld());
					if (broodEetle != null) {
						broodEetle.setLocationAndAngles(mutable.getX() + 0.5D, mutable.getY(), mutable.getZ() + 0.5D, 0.0F, 0.0F);
						broodEetle.onInitialSpawn(world, world.getDifficultyForLocation(broodEetle.getPosition()), SpawnReason.STRUCTURE, null, null);
						world.func_242417_l(broodEetle);
					}
				}

				mutable.move(Direction.DOWN, 10);

				//Create bottom doorway
				Direction horizontal = this.bottomDoorDirection;
				mutable.move(horizontal, 6);
				carveForward(world, mutable, horizontal, bounds);
				carveForward(world, mutable.up(), horizontal, bounds);
				carveForward(world, mutable.up(2), horizontal, bounds);

				BlockPos rotateCW = mutable.offset(horizontal.rotateY());
				carveForward(world, rotateCW, horizontal, bounds);
				carveForward(world, rotateCW.up(), horizontal, bounds);

				BlockPos rotateCCW = mutable.offset(horizontal.rotateYCCW());
				carveForward(world, rotateCCW, horizontal, bounds);
				carveForward(world, rotateCCW.up(), horizontal, bounds);

				this.createCenterSteps(world, origin.down(9), this.stepsRotation, random, bounds);

				this.createWayUp(world, origin.down(7), this.topDoorDirection, this.topDoorLeftOrRight, random, bounds);

				int arenaTopY = origin.getY() - 3;
				//Adds crowns to top of arena
				for (int x = min.getX(); x <= max.getX(); x++) {
					for (int z = min.getZ(); z <= max.getZ(); z++) {
						mutable.setPos(x, arenaTopY, z);
						if (bounds.isVecInside(mutable)) {
							if (world.getBlockState(mutable).getBlock() == CORROCK_BLOCK && random.nextBoolean()) {
								EetleEggsBlock.shuffleDirections(ATTACHMENT_DIRECTIONS, random);
								for (Direction direction : ATTACHMENT_DIRECTIONS) {
									if (direction.getAxis() == Direction.Axis.Y) {
										direction = Direction.UP;
										BlockPos offset = mutable.offset(direction);
										if (bounds.isVecInside(offset) && world.isAirBlock(offset)) {
											world.setBlockState(offset, CROWN_STANDING_STATE.with(CorrockCrownStandingBlock.ROTATION, random.nextInt(16)), 2);
											break;
										}
									} else {
										BlockPos offset = mutable.offset(direction);
										if (bounds.isVecInside(offset) && world.isAirBlock(offset) && world.isAirBlock(offset.down())) {
											world.setBlockState(offset, CROWN_WALL_STATE.with(CorrockCrownWallBlock.FACING, direction), 2);
											break;
										}
									}
								}
							}
						}
					}
				}
			}

			static class BottomStepsRotationInfo {
				private final int xDirection;
				private final int zDirection;
				private final Direction ledgeFillDirection;
				private final int firstSmallStepMinX, firstSmallStepMinZ;
				private final int firstSmallStepMaxX, firstSmallStepMaxZ;
				private final int secondSmallStepMinX, secondSmallStepMinZ;
				private final int secondSmallStepMaxX, secondSmallStepMaxZ;
				private final int mediumStepOffsetX, mediumStepOffsetZ;
				private final int largeStepOffsetX, largeStepOffsetZ;
				private final Direction mediumStepDirection;
				private final Direction largeStepDirection;

				BottomStepsRotationInfo(int xDirection, int zDirection, Direction ledgeFillDirection, int firstSmallStepMinX, int firstSmallStepMinZ, int firstSmallStepMaxX, int firstSmallStepMaxZ, int secondSmallStepMinX, int secondSmallStepMinZ, int secondSmallStepMaxX, int secondSmallStepMaxZ, int mediumStepOffsetX, int mediumStepOffsetZ, int largeStepOffsetX, int largeStepOffsetZ, Direction mediumStepDirection, Direction largeStepDirection) {
					this.xDirection = xDirection;
					this.zDirection = zDirection;
					this.ledgeFillDirection = ledgeFillDirection;
					this.firstSmallStepMinX = firstSmallStepMinX;
					this.firstSmallStepMinZ = firstSmallStepMinZ;
					this.firstSmallStepMaxX = firstSmallStepMaxX;
					this.firstSmallStepMaxZ = firstSmallStepMaxZ;
					this.secondSmallStepMinX = secondSmallStepMinX;
					this.secondSmallStepMinZ = secondSmallStepMinZ;
					this.secondSmallStepMaxX = secondSmallStepMaxX;
					this.secondSmallStepMaxZ = secondSmallStepMaxZ;
					this.mediumStepOffsetX = mediumStepOffsetX;
					this.mediumStepOffsetZ = mediumStepOffsetZ;
					this.largeStepOffsetX = largeStepOffsetX;
					this.largeStepOffsetZ = largeStepOffsetZ;
					this.mediumStepDirection = mediumStepDirection;
					this.largeStepDirection = largeStepDirection;
				}
			}
		}

		/**
		 * Allows for storage of {@link BlockPos} -> {@link BlockState}
		 * <p>Useful for randomized 'pre-generating'</p>
		 */
		private static class StateMap {
			private final Map<BlockPos, BlockState> stateMap = new HashMap<>();
			private boolean setup;

			private static void fillAreaWithBlockCube(StateMap stateMap, int x1, int y1, int z1, int x2, int y2, int z2, BlockState block) {
				BlockPos.Mutable mutable = new BlockPos.Mutable();
				for (int yy = y1; yy <= y2; yy++) {
					for (int xx = x1; xx <= x2; xx++) {
						for (int zz = z1; zz <= z2; zz++) {
							mutable.setPos(xx, yy, zz);
							stateMap.setBlockState(mutable, block);
						}
					}
				}
			}

			private void generate(ISeedReader world, MutableBoundingBox bounds) {
				this.stateMap.forEach((pos, blockState) -> {
					if (bounds.isVecInside(pos)) {
						world.setBlockState(pos, blockState, 2);
					}
				});
			}

			private void setBlockState(BlockPos pos, BlockState state) {
				this.stateMap.put(pos.toImmutable(), state);
			}

			private BlockState getBlockState(BlockPos pos) {
				return this.stateMap.getOrDefault(pos, Blocks.AIR.getDefaultState());
			}
		}

		private static class NestStalactite {
			private final int radius;
			private final int length;
			private final int xOffset;
			private final int zOffset;

			private NestStalactite(Random random) {
				this.radius = random.nextInt(2) + 3;
				this.length = random.nextInt(3) + 5;
				this.xOffset = random.nextInt(15) - random.nextInt(15);
				this.zOffset = random.nextInt(15) - random.nextInt(15);
			}

			private void generate(ISeedReader world, BlockPos origin, Random random, OctavesNoiseGenerator noiseGenerator, MutableBoundingBox boundingBox) {
				List<BlockPos> possibleDecorationPositions = new ArrayList<>();
				BlockPos.Mutable mutable = new BlockPos.Mutable();
				int originX = origin.getX();
				int originY = origin.getY();
				int originZ = origin.getZ();
				int radius = this.radius;
				int length = this.length;
				float noiseMultiplier = radius * 0.25F;
				for (int x = originX - radius; x < originX + radius; x++) {
					for (int y = originY - length; y < originY; y++) {
						for (int z = originZ - radius; z < originZ + radius; z++) {
							mutable.setPos(x, y, z);
							if (boundingBox.isVecInside(mutable)) {
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
				}
				for (BlockPos pos : possibleDecorationPositions) {
					if (random.nextFloat() < 0.1F) {
						EetleEggsBlock.shuffleDirections(ATTACHMENT_DIRECTIONS, random);
						for (Direction direction : ATTACHMENT_DIRECTIONS) {
							BlockPos offset = pos.offset(direction);
							if (boundingBox.isVecInside(offset) && world.isAirBlock(offset)) {
								if (random.nextBoolean()) {
									world.setBlockState(offset, EETLE_EGGS_STATE.with(EetleEggsBlock.FACING, direction), 2);
								} else {
									if (direction.getAxis() == Direction.Axis.Y) {
										world.setBlockState(offset, CROWN_STANDING_STATE.with(CorrockCrownStandingBlock.UPSIDE_DOWN, direction == Direction.DOWN).with(CorrockCrownStandingBlock.ROTATION, random.nextInt(16)), 2);
									} else {
										world.setBlockState(offset, CROWN_WALL_STATE.with(CorrockCrownWallBlock.FACING, direction), 2);
									}
								}
							}
						}
					}
				}
			}
		}

		private static class NestTunnel {
			private static final Set<Block> CORROCK_REPLACEABLE = Sets.newHashSet(
					Blocks.END_STONE, Blocks.STONE, EUMUS, CORROCK_BLOCK
			);
			private static final Vector3d UP = new Vector3d(0.0D, 1.0D, 1.0D);
			private final Direction facing;
			private final int xOffset;
			private final int yOffset;
			private final int zOffset;
			private final List<BlockPos> airPositions = new ArrayList<>();
			private final List<BlockPos> corrockPositions = new ArrayList<>();
			private final List<TunnelDecoration> decorations = new ArrayList<>();
			private BlockPos start;
			private NestCave nestCave = null;
			private boolean goesToSurface;

			private NestTunnel(Random random, Direction facing) {
				this.facing = facing;
				this.yOffset = random.nextInt(6) - random.nextInt(6);
				Direction rotateY = facing.rotateY();
				int offset = random.nextInt(11) - random.nextInt(11);
				this.xOffset = rotateY.getXOffset() * offset;
				this.zOffset = rotateY.getZOffset() * offset;
			}

			private void setup(BlockPos startPos, ChunkGenerator chunkGenerator, OctavesNoiseGenerator noiseGenerator, Random random) {
				startPos = startPos.add(this.xOffset, this.yOffset, this.zOffset);
				this.start = startPos;
				List<Vector3d> points = new ArrayList<>();
				Vector3d startVec = Vector3d.copy(startPos);
				BlockPos end;
				Direction facing = this.facing;
				if (this.goesToSurface) {
					end = startPos.offset(facing, 16 + random.nextInt(9)).offset(facing.rotateY(), (int) MathUtil.makeNegativeRandomly(random.nextInt(7) + 6, random));
					int endX = end.getX();
					int endZ = end.getZ();
					int topY = chunkGenerator.getNoiseHeight(endX, endZ, Heightmap.Type.WORLD_SURFACE);
					end = new BlockPos(endX, topY, endZ);

					Vector3d endVec = Vector3d.copy(end);
					Vector3d difference = endVec.subtract(startVec);
					Vector3d normalizedDifference = difference.normalize();

					Vector3d anchorStart = startVec.subtract(normalizedDifference).add(-6 * facing.getXOffset(), 0, -6 * facing.getZOffset());
					Vector3d anchorEnd = endVec.add(normalizedDifference).add(0, 6, 0);

					points.add(anchorStart);
					points.add(startVec);

					Vector3d offset = UP.crossProduct(normalizedDifference);
					double offsetX = offset.x;
					double offsetZ = offset.z;
					int iterations = 6;
					for (int i = 0; i < iterations; i++) {
						points.add(startVec.add(difference.scale(i / (float) iterations)).add(offsetX * (random.nextDouble() - 0.5D) * 5.0D, (random.nextDouble() - 0.25D) * 5.0D, offsetZ * (random.nextDouble() - 0.5D) * 5.0D));
					}

					points.add(endVec);
					points.add(anchorEnd);
				} else {
					end = startPos.offset(facing, 24 + random.nextInt(9)).offset(facing.rotateY(), (int) MathUtil.makeNegativeRandomly(random.nextInt(7) + 6, random)).offset(Direction.UP, (int) MathUtil.makeNegativeRandomly(random.nextInt(7) + 6, random));
					Vector3d endVec = Vector3d.copy(end);
					Vector3d difference = endVec.subtract(startVec);
					Vector3d normalizedDifference = difference.normalize();

					float xOffset = facing.getXOffset();
					float zOffset = facing.getZOffset();
					Vector3d anchorStart = startVec.subtract(normalizedDifference).add(-6 * xOffset, 0, -6 * zOffset);
					Vector3d anchorEnd = endVec.add(normalizedDifference).add(-6 * xOffset, 0, -6 * zOffset);

					points.add(anchorStart);
					points.add(startVec);

					Vector3d offset = UP.crossProduct(normalizedDifference);
					double offsetX = offset.x;
					double offsetZ = offset.z;
					int iterations = 6;
					for (int i = 0; i < iterations; i++) {
						points.add(startVec.add(difference.scale(i / (float) iterations)).add(offsetX * (random.nextDouble() - 0.5D) * 4.0D, (random.nextDouble() - 0.25D) * 4.0D, offsetZ * (random.nextDouble() - 0.5D) * 4.0D));
					}

					points.add(endVec);
					points.add(anchorEnd);

					NestCave nestCave = this.nestCave;
					if (nestCave != null) {
						nestCave.setup(end, random, chunkGenerator, noiseGenerator);
					}
				}
				MathUtil.CatmullRomSpline spline = new MathUtil.CatmullRomSpline(points.toArray(new Vector3d[0]), MathUtil.CatmullRomSpline.SplineType.CHORDAL);
				int steps = (int) (10 + Math.sqrt(startPos.distanceSq(end)) * 3);
				BlockPos prevPos = null;
				List<BlockPos> airPositions = this.airPositions;
				List<BlockPos> corrockPositions = this.corrockPositions;
				List<TunnelDecoration> eumusPatches = new ArrayList<>();
				List<TunnelDecoration> eggsAndCorrock = new ArrayList<>();
				for (int i = 0; i < steps; i++) {
					float progress = i / (float) steps;
					BlockPos interpolatedPos = spline.interpolate(progress);
					if (interpolatedPos.equals(prevPos)) {
						continue;
					}
					prevPos = interpolatedPos;
					int originX = interpolatedPos.getX();
					int originY = interpolatedPos.getY();
					int originZ = interpolatedPos.getZ();
					int radius = 6 + random.nextInt(3);
					for (int x = -radius; x <= radius; x++) {
						for (int y = -radius; y <= radius; y++) {
							for (int z = -radius; z <= radius; z++) {
								int distanceSq = x * x + y * y + z * z;
								if (distanceSq <= radius) {
									BlockPos spherePos = new BlockPos(originX + x, originY + y, originZ + z);
									airPositions.add(spherePos);
									if (distanceSq >= radius - 2) {
										corrockPositions.add(spherePos);
									}
									if (random.nextFloat() < 0.0005F && eumusPatches.size() < 3) {
										eumusPatches.add(new EumusPatch(spherePos, random.nextInt(2) + 3));
									}
									if (random.nextFloat() < 0.01F && eggsAndCorrock.size() < 48) {
										if (random.nextFloat() < 0.25F) {
											eggsAndCorrock.add(new EetleEggsPatch(spherePos));
										} else {
											eggsAndCorrock.add(new CorrockPatch(spherePos));
										}
									}
								}
							}
						}
					}
				}
				List<TunnelDecoration> decorations = this.decorations;
				decorations.addAll(eumusPatches);
				decorations.addAll(eggsAndCorrock);
			}

			private void generateCave(ISeedReader world, OctavesNoiseGenerator noiseGenerator, MutableBoundingBox bounds) {
				NestCave nestCave = this.nestCave;
				if (nestCave != null) {
					nestCave.generate(world, noiseGenerator, bounds);
				}
			}

			private void generate(ISeedReader world, Random random, MutableBoundingBox bounds) {
				for (BlockPos pos : this.airPositions) {
					if (bounds.isVecInside(pos) && CARVABLE_BLOCKS.contains(world.getBlockState(pos).getBlock())) {
						world.setBlockState(pos, CAVE_AIR, 2);
					}
				}
				int radius = 1;
				BlockPos.Mutable mutable = new BlockPos.Mutable();
				for (BlockPos corrockPos : this.corrockPositions) {
					int originX = corrockPos.getX();
					int originY = corrockPos.getY();
					int originZ = corrockPos.getZ();
					for (int x = -radius; x <= radius; x++) {
						for (int y = -radius; y <= radius; y++) {
							for (int z = -radius; z <= radius; z++) {
								mutable.setPos(originX + x, originY + y, originZ + z);
								if (bounds.isVecInside(mutable) && CORROCK_REPLACEABLE.contains(world.getBlockState(mutable).getBlock())) {
									world.setBlockState(mutable, CORROCK_BLOCK_STATE, 2);
									if (random.nextBoolean()) {
										mutable.move(Direction.getRandomDirection(random));
										if (bounds.isVecInside(mutable) && CORROCK_REPLACEABLE.contains(world.getBlockState(mutable).getBlock())) {
											world.setBlockState(mutable, CORROCK_BLOCK_STATE, 2);
										}
									}
								}
							}
						}
					}
				}
			}

			private void generateDecorations(ISeedReader world, OctavesNoiseGenerator noiseGenerator, Random random, MutableBoundingBox bounds) {
				for (TunnelDecoration decoration : this.decorations) {
					if (decoration.isNotSetup()) {
						decoration.setup(random);
					}
					decoration.generate(world, noiseGenerator, bounds);
				}
			}

			interface TunnelDecoration {
				void setup(Random random);

				void generate(ISeedReader world, OctavesNoiseGenerator noiseGenerator, MutableBoundingBox bounds);

				boolean isNotSetup();
			}

			static class EumusPatch implements TunnelDecoration {
				private final BlockPos origin;
				private final int radius;

				EumusPatch(BlockPos origin, int radius) {
					this.origin = origin;
					this.radius = radius;
				}

				@Override
				public void setup(Random random) {
				}

				@Override
				public void generate(ISeedReader world, OctavesNoiseGenerator noiseGenerator, MutableBoundingBox bounds) {
					createEumusPatch(world, this.origin, noiseGenerator, this.radius, bounds);
				}

				@Override
				public boolean isNotSetup() {
					return false;
				}
			}

			static class EetleEggsPatch implements TunnelDecoration {
				private final StateMap stateMap = new StateMap();
				private final BlockPos origin;

				EetleEggsPatch(BlockPos origin) {
					this.origin = origin;
				}

				@Override
				public void setup(Random random) {
					StateMap stateMap = this.stateMap;
					BlockPos origin = this.origin;
					BlockPos.Mutable mutable = new BlockPos.Mutable();
					EetleEggsBlock.shuffleDirections(EGG_DIRECTIONS, random);
					for (int j = 0; j < 48; j++) {
						mutable.setAndOffset(origin, random.nextInt(9) - random.nextInt(9), random.nextInt(9) - random.nextInt(9), random.nextInt(9) - random.nextInt(9));
						if (random.nextFloat() < 0.4F) {
							for (Direction direction : EGG_DIRECTIONS) {
								BlockState state = EETLE_EGGS_STATE.with(EetleEggsBlock.FACING, direction.getOpposite());
								stateMap.setBlockState(mutable, state.with(EetleEggsBlock.SIZE, random.nextFloat() < 0.75F ? 0 : random.nextFloat() < 0.6F ? 1 : 2));
								break;
							}
						}
					}
					stateMap.setup = true;
				}

				public void generate(ISeedReader world, OctavesNoiseGenerator noiseGenerator, MutableBoundingBox bounds) {
					StateMap stateMap = this.stateMap;
					stateMap.stateMap.forEach((pos, state) -> {
						if (bounds.isVecInside(pos) && world.isAirBlock(pos)) {
							BlockPos offset = pos.offset(state.get(EetleEggsBlock.FACING).getOpposite());
							if (bounds.isVecInside(offset)) {
								Block opposite = world.getBlockState(offset).getBlock();
								if (opposite == CORROCK_BLOCK || opposite == EUMUS) {
									world.setBlockState(pos, state, 2);
								}
							}
						}
					});
				}

				@Override
				public boolean isNotSetup() {
					return !this.stateMap.setup;
				}
			}

			static class CorrockPatch implements TunnelDecoration {
				private final StateMap stateMap = new StateMap();
				private final BlockPos origin;

				CorrockPatch(BlockPos origin) {
					this.origin = origin;
				}

				@Override
				public void setup(Random random) {
					StateMap stateMap = this.stateMap;
					BlockPos origin = this.origin;
					BlockPos.Mutable mutable = new BlockPos.Mutable();
					for (int i = 0; i < 32; i++) {
						mutable.setAndOffset(origin, random.nextInt(8) - random.nextInt(8), random.nextInt(4) - random.nextInt(4), random.nextInt(8) - random.nextInt(8));
						if (random.nextBoolean()) {
							stateMap.setBlockState(mutable, CORROCK_STATE);
						}
					}
					stateMap.setup = true;
				}

				@Override
				public void generate(ISeedReader world, OctavesNoiseGenerator noiseGenerator, MutableBoundingBox bounds) {
					StateMap stateMap = this.stateMap;
					stateMap.stateMap.forEach((pos, state) -> {
						if (bounds.isVecInside(pos) && world.isAirBlock(pos)) {
							Block below = world.getBlockState(pos.down()).getBlock();
							if (below == CORROCK_BLOCK || below == EUMUS) {
								world.setBlockState(pos, state, 2);
							}
						}
					});
				}

				@Override
				public boolean isNotSetup() {
					return !this.stateMap.setup;
				}
			}

			static class NestCave {
				private final StateMap cave = new StateMap();
				private final NestCaveType type;
				private final List<EetleNestPiece.EumusPatch> eumusPatches = new ArrayList<>();
				private final List<EetleEggsPatch> eetleEggsPatches = new ArrayList<>();
				private final List<CorrockPatch> corrockPatches = new ArrayList<>();
				private BlockPos center;

				private NestCave(Random random) {
					this.type = NestCaveType.random(random);
				}

				private void setup(BlockPos end, Random random, ChunkGenerator chunkGenerator, OctavesNoiseGenerator noiseGenerator) {
					this.center = end;
					NestCaveType type = this.type;
					int horizontalRadius = type.horizontalRadius;
					int verticalRadius = type.verticalRadius;
					if (isAreaCarvable(end, horizontalRadius, verticalRadius, chunkGenerator)) {
						int endX = end.getX();
						int endY = end.getY();
						int endZ = end.getZ();
						BlockPos.Mutable mutable = new BlockPos.Mutable();
						StateMap cave = this.cave;
						for (int x = endX - horizontalRadius; x <= endX + horizontalRadius; x++) {
							for (int z = endZ - horizontalRadius; z <= endZ + horizontalRadius; z++) {
								for (int y = endY - verticalRadius; y <= endY + verticalRadius; y++) {
									mutable.setPos(x, y, z);

									double localX = (x - endX) / (float) horizontalRadius;
									double localY = (y - endY) / (float) verticalRadius;
									double localZ = (z - endZ) / (float) horizontalRadius;
									double distanceSq = localX * localX + localY * localY + localZ * localZ;
									double frequency = 0.65F;
									double shapeNoise = noiseGenerator.func_205563_a(x * frequency, y * frequency, z * frequency) * 0.5F;
									if (distanceSq >= 0.7F + shapeNoise && distanceSq <= 1.2F + shapeNoise) {
										cave.setBlockState(mutable, CORROCK_BLOCK_STATE);
									} else if (distanceSq <= 0.9F + shapeNoise) {
										cave.setBlockState(mutable, Blocks.CAVE_AIR.getDefaultState());
									}
								}
							}
						}
						List<EetleNestPiece.EumusPatch> eumusPatches = this.eumusPatches;
						int eumusPatchCount = random.nextInt(2) + 2;
						EetleNestPiece.EumusPatch.PatchType patchType = type.patchType;
						for (int i = 0; i < eumusPatchCount; i++) {
							eumusPatches.add(new EetleNestPiece.EumusPatch(patchType, random));
						}
						List<EetleEggsPatch> eetleEggsPatches = this.eetleEggsPatches;
						for (int i = 0; i < type.eetleEggPatches; i++) {
							eetleEggsPatches.add(new EetleEggsPatch(end.add(random.nextInt(horizontalRadius) - random.nextInt(horizontalRadius), random.nextInt(verticalRadius) - random.nextInt(verticalRadius), random.nextInt(horizontalRadius) - random.nextInt(horizontalRadius)), random));
						}
						List<CorrockPatch> corrockPatches = this.corrockPatches;
						for (int i = 0; i < type.corrockPatches; i++) {
							corrockPatches.add(new CorrockPatch(end.add(random.nextInt(horizontalRadius) - random.nextInt(horizontalRadius), -random.nextInt(verticalRadius), random.nextInt(horizontalRadius) - random.nextInt(horizontalRadius)), random));
						}
					}
				}

				private void generate(ISeedReader world, OctavesNoiseGenerator noiseGenerator, MutableBoundingBox bounds) {
					this.cave.generate(world, bounds);
					BlockPos center = this.center;
					for (EetleNestPiece.EumusPatch eumusPatch : this.eumusPatches) {
						eumusPatch.generate(world, center, noiseGenerator, bounds);
					}
					for (EetleEggsPatch eetleEggsPatch : this.eetleEggsPatches) {
						eetleEggsPatch.generate(world, bounds);
					}
					for (CorrockPatch corrockPatch : this.corrockPatches) {
						corrockPatch.generate(world, bounds);
					}
				}

				private static boolean isAreaCarvable(BlockPos center, int horizontalRadius, int verticalRadius, ChunkGenerator chunkGenerator) {
					BlockPos.Mutable mutable = new BlockPos.Mutable();
					int foundAirBlocks = 0;
					int centerX = center.getX();
					int centerY = center.getY();
					int centerZ = center.getZ();
					int maxAirBlocks = (int) (horizontalRadius * 2 * horizontalRadius * 2 * 0.25F);
					for (int x = centerX - horizontalRadius; x <= centerX + horizontalRadius; x++) {
						for (int z = centerZ - horizontalRadius; z <= centerZ + horizontalRadius; z++) {
							IBlockReader reader = chunkGenerator.func_230348_a_(x, z);
							for (int y = centerY - verticalRadius; y <= centerY + verticalRadius; y++) {
								Block block = reader.getBlockState(mutable.setPos(x, y, z)).getBlock();
								if (!EetleNestPieces.CARVABLE_BLOCKS.contains(block)) {
									if (block == Blocks.AIR) {
										if (foundAirBlocks++ >= maxAirBlocks) {
											return false;
										}
									} else {
										return false;
									}
								}
							}
						}
					}
					return true;
				}

				static class EetleEggsPatch {
					private final StateMap stateMap = new StateMap();

					EetleEggsPatch(BlockPos origin, Random random) {
						EetleEggsBlock.shuffleDirections(EGG_DIRECTIONS, random);
						BlockPos.Mutable mutable = new BlockPos.Mutable();
						StateMap stateMap = this.stateMap;
						for (int j = 0; j < 48; j++) {
							mutable.setAndOffset(origin, random.nextInt(9) - random.nextInt(9), random.nextInt(9) - random.nextInt(9), random.nextInt(9) - random.nextInt(9));
							if (random.nextFloat() < 0.4F) {
								for (Direction direction : EGG_DIRECTIONS) {
									BlockState state = EETLE_EGGS_STATE.with(EetleEggsBlock.FACING, direction.getOpposite());
									stateMap.setBlockState(mutable, state.with(EetleEggsBlock.SIZE, random.nextFloat() < 0.75F ? 0 : random.nextFloat() < 0.6F ? 1 : 2));
									break;
								}
							}
						}
					}

					private void generate(ISeedReader world, MutableBoundingBox bounds) {
						this.stateMap.stateMap.forEach((pos, state) -> {
							if (bounds.isVecInside(pos) && world.isAirBlock(pos)) {
								BlockPos offset = pos.offset(state.get(EetleEggsBlock.FACING).getOpposite());
								if (bounds.isVecInside(offset)) {
									Block opposite = world.getBlockState(offset).getBlock();
									if (opposite == CORROCK_BLOCK || opposite == EUMUS) {
										world.setBlockState(pos, state, 2);
									}
								}
							}
						});
					}
				}

				static class CorrockPatch {
					private final StateMap stateMap = new StateMap();

					CorrockPatch(BlockPos origin, Random random) {
						StateMap stateMap = this.stateMap;
						BlockPos.Mutable mutable = new BlockPos.Mutable();
						for (int i = 0; i < 32; i++) {
							mutable.setAndOffset(origin, random.nextInt(8) - random.nextInt(8), random.nextInt(4) - random.nextInt(4), random.nextInt(8) - random.nextInt(8));
							if (random.nextBoolean()) {
								stateMap.setBlockState(mutable, CORROCK_STATE);
							}
						}
					}

					private void generate(ISeedReader world, MutableBoundingBox bounds) {
						this.stateMap.stateMap.forEach((pos, state) -> {
							if (bounds.isVecInside(pos) && world.isAirBlock(pos)) {
								Block below = world.getBlockState(pos.down()).getBlock();
								if (below == CORROCK_BLOCK || below == EUMUS) {
									world.setBlockState(pos, state, 2);
								}
							}
						});
					}
				}

				enum NestCaveType {
					SMALL(9, 6, 27, 20, EetleNestPiece.EumusPatch.PatchType.SMALL),
					MEDIUM(12, 9, 48, 36, EetleNestPiece.EumusPatch.PatchType.MEDIUM),
					LARGE(14, 11, 64, 49, EetleNestPiece.EumusPatch.PatchType.LARGE);

					private static final NestCaveType[] VALUES = NestCaveType.values();
					private final int horizontalRadius;
					private final int verticalRadius;
					private final int eetleEggPatches;
					private final int corrockPatches;
					private final EetleNestPiece.EumusPatch.PatchType patchType;

					NestCaveType(int horizontalRadius, int verticalRadius, int eetleEggPatches, int corrockPatches, EetleNestPiece.EumusPatch.PatchType patchType) {
						this.horizontalRadius = horizontalRadius;
						this.verticalRadius = verticalRadius;
						this.eetleEggPatches = eetleEggPatches;
						this.corrockPatches = corrockPatches;
						this.patchType = patchType;
					}

					private static NestCaveType random(Random random) {
						return VALUES[random.nextInt(VALUES.length)];
					}
				}
			}
		}

		static class CorrockShelf {
			private static final Direction[] HORIZONTALS = Direction.Plane.HORIZONTAL.getDirectionValues().toArray(Direction[]::new);
			private final BlockPos pos;
			private final StateMap corrock = new StateMap();
			private final StateMap decorations = new StateMap();

			CorrockShelf(BlockPos pos, Random random) {
				this.pos = pos;
				int size = random.nextBoolean() ? 3 : 4;
				int edgeBias = 10;
				int min = -(size / edgeBias + size);
				int max = size / edgeBias + size;
				int originX = pos.getX();
				int originY = pos.getY();
				int originZ = pos.getZ();
				int underXDistance = random.nextInt(2) + 2;
				int underZDistance = random.nextInt(2) + 2;
				BlockPos.Mutable mutable = new BlockPos.Mutable();
				StateMap corrock = this.corrock;
				StateMap decorations = this.decorations;
				List<BlockPos> wallCrowns = new ArrayList<>();
				for (int x = min; x <= max; x++) {
					for (int z = min; z <= max; z++) {
						mutable.setPos(originX + x, originY, originZ + z);
						double radius = (Math.cos(4 * Math.atan2(z, x)) / edgeBias + 1) * size;
						int distance = x * x + z * z;
						if (distance < radius * radius) {
							corrock.setBlockState(mutable, CORROCK_BLOCK_STATE);
							if (x * x < (radius - underXDistance) * (radius - underXDistance) && z * z < (radius - underZDistance) * (radius - underZDistance)) {
								BlockPos down = mutable.down();
								corrock.setBlockState(down, CORROCK_BLOCK_STATE);
							}
							if (random.nextFloat() < 0.75F) {
								double radiusMinusOne = radius - 1.0F;
								if (distance > radiusMinusOne * radiusMinusOne) {
									if (random.nextFloat() < 0.25F) {
										decorations.setBlockState(mutable.up(), CROWN_STANDING_STATE.with(CorrockCrownStandingBlock.ROTATION, random.nextInt(16)));
									} else {
										wallCrowns.add(mutable.toImmutable());
									}
								}
							}
						}
					}
				}
				Map<BlockPos, BlockState> corrockStateMap = corrock.stateMap;
				for (BlockPos crownPos : wallCrowns) {
					int crownsPlaced = 0;
					EetleEggsBlock.shuffleDirections(HORIZONTALS, random);
					for (Direction direction : HORIZONTALS) {
						BlockPos offset = crownPos.offset(direction);
						if (!corrockStateMap.containsKey(offset)) {
							decorations.setBlockState(offset, CROWN_WALL_STATE.with(CorrockCrownWallBlock.FACING, direction));
							if (random.nextFloat() > 0.75F || crownsPlaced++ == 1) {
								break;
							}
						}
					}
				}
			}

			private void generate(ISeedReader world, MutableBoundingBox bounds) {
				this.corrock.stateMap.forEach((pos, state) -> {
					if (bounds.isVecInside(pos) && world.isAirBlock(pos)) {
						world.setBlockState(pos, state, 2);
					}
				});
			}

			private void generateDecorations(ISeedReader world, MutableBoundingBox bounds) {
				this.decorations.stateMap.forEach((pos, state) -> {
					if (bounds.isVecInside(pos) && world.isAirBlock(pos)) {
						if (state.getBlock() instanceof CorrockCrownStandingBlock) {
							if (world.getBlockState(pos.down()).getBlock() == CORROCK_BLOCK) {
								world.setBlockState(pos, state, 2);
							}
						} else {
							BlockPos offset = pos.offset(state.get(CorrockCrownWallBlock.FACING).getOpposite());
							if (bounds.isVecInside(offset)) {
								if (world.getBlockState(offset).getBlock() == CORROCK_BLOCK) {
									world.setBlockState(pos, state, 2);
								}
							}
						}
					}
				});
			}
		}
	}
}
