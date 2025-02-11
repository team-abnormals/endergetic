package com.teamabnormals.endergetic.common.levelgen.structure.structures;

import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import com.teamabnormals.blueprint.core.util.MathUtil;
import com.teamabnormals.endergetic.common.block.CorrockCrownBlock;
import com.teamabnormals.endergetic.common.block.CorrockCrownStandingBlock;
import com.teamabnormals.endergetic.common.block.CorrockCrownWallBlock;
import com.teamabnormals.endergetic.common.block.EetleEggBlock;
import com.teamabnormals.endergetic.common.entity.eetle.BroodEetle;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import com.teamabnormals.endergetic.core.registry.EEBlocks;
import com.teamabnormals.endergetic.core.registry.EEEntityTypes;
import com.teamabnormals.endergetic.core.registry.EEStructureTypes.EEStructurePieceTypes;
import com.teamabnormals.endergetic.core.registry.builtin.EENoises;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraft.world.level.levelgen.synth.PerlinNoise;
import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;
import net.minecraft.world.phys.Vec3;

import java.util.*;
import java.util.stream.IntStream;

public final class EetleNestPieces {
	private static final Set<BoundingBox> GENERATING_BOUNDS = new HashSet<>();
	private static final Direction[] ATTACHMENT_DIRECTIONS = Direction.values();
	private static final Direction[] TUNNEL_SIDES = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
	private static final Direction[] EGG_DIRECTIONS = Direction.values();
	private static final Block CORROCK_BLOCK = EEBlocks.END_CORROCK_BLOCK.get();
	private static final Block EUMUS = EEBlocks.EUMUS.get();
	private static final Block CROWN_STANDING = EEBlocks.END_CORROCK_CROWN.get();
	private static final Block CROWN_WALL = EEBlocks.END_WALL_CORROCK_CROWN.get();
	private static final Block EETLE_EGSS = EEBlocks.EETLE_EGG.get();
	private static final Block CORROCK = EEBlocks.END_CORROCK.get();
	private static final Block SPECKLED_CORROCK = EEBlocks.SPECKLED_END_CORROCK.get();
	public static final Set<Block> CARVABLE_BLOCKS = Sets.newHashSet(Blocks.STONE, Blocks.END_STONE, CORROCK_BLOCK, CORROCK, CROWN_STANDING, CROWN_WALL, EETLE_EGSS, EUMUS, SPECKLED_CORROCK, EEBlocks.INFESTED_CORROCK.get(), EEBlocks.POISMOSS.get(), EEBlocks.EUMUS_POISMOSS.get());
	private static final BlockState CORROCK_BLOCK_STATE = CORROCK_BLOCK.defaultBlockState();
	private static final BlockState CORROCK_STATE = CORROCK.defaultBlockState();
	private static final BlockState EUMUS_STATE = EUMUS.defaultBlockState();
	private static final BlockState CROWN_WALL_STATE = CROWN_WALL.defaultBlockState();
	private static final BlockState CROWN_STANDING_STATE = CROWN_STANDING.defaultBlockState();
	private static final BlockState SPECKLED_CORROCK_STATE = SPECKLED_CORROCK.defaultBlockState();
	private static final BlockState END_STONE = Blocks.END_STONE.defaultBlockState();
	private static final BlockState EETLE_EGGS_STATE = EETLE_EGSS.defaultBlockState();
	private static final BlockState INFESTED_STATE = EEBlocks.INFESTED_CORROCK.get().defaultBlockState();
	private static final Map<Long, PerlinSimplexNoise> SURFACE_NOISE = new HashMap<>();
	private static final Map<Long, PerlinNoise> UNDERGROUND_NOISE = new HashMap<>();

	public static boolean isNotInsideGeneratingBounds(BlockPos pos) {
		for (BoundingBox boundingBox : GENERATING_BOUNDS) {
			if (boundingBox.isInside(pos)) {
				return false;
			}
		}
		return true;
	}

	public static class EetleNestPiece extends StructurePiece {
		private static final ResourceLocation ARENA = new ResourceLocation(EndergeticExpansion.MOD_ID, "eetle_nest/arena");
		private final StructureTemplate arena;
		private NestDesign nestDesign = null;

		public EetleNestPiece(StructureTemplateManager manager, CompoundTag compoundNBT) {
			super(EEStructurePieceTypes.EETLE_NEST.get(), compoundNBT);
			this.arena = manager.get(ARENA).orElseThrow(() -> new RuntimeException("Failed to get Eetle Nest Arena Template!"));
		}

		public EetleNestPiece(StructureTemplateManager manager, BlockPos corner) {
			super(EEStructurePieceTypes.EETLE_NEST.get(), 0, new BoundingBox(corner.getX() - 64, corner.getY() - 48, corner.getZ() - 64, corner.getX() + 64, corner.getY() + 6, corner.getZ() + 64));
			this.arena = manager.get(ARENA).orElseThrow(() -> new RuntimeException("Failed to get Eetle Nest Arena Template!"));
		}

		private static void transformSurface(WorldGenLevel world, int x, int z, int maxDepth, int depthScale, double noise, BoundingBox boundingBox, BlockState topState, BlockState under) {
			BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos(x, boundingBox.minY(), z);
			if (boundingBox.isInside(mutable)) {
				int startingY = world.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x, z);
				int depth = (int) (Math.abs(noise) * depthScale + 14.0D);
				BlockState fillState = topState;
				for (int y = startingY, d = 0; y > maxDepth && d < depth; y--, d++) {
					mutable.setY(y);
					if (boundingBox.isInside(mutable)) {
						Block block = world.getBlockState(mutable).getBlock();
						if (block == Blocks.END_STONE || block == SPECKLED_CORROCK) {
							world.setBlock(mutable, fillState, 2);
							fillState = under;
						} else if (world.isEmptyBlock(mutable)) {
							fillState = topState;
						}
					}
				}
			}
		}

		protected static void createEumusPatch(WorldGenLevel world, BlockPos origin, PerlinNoise noiseGenerator, int radius, BoundingBox bounds) {
			int originX = origin.getX();
			int originY = origin.getY();
			int originZ = origin.getZ();
			BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
			for (int x = originX - radius; x < originX + radius; x++) {
				for (int y = originY - radius; y < originY + radius; y++) {
					for (int z = originZ - radius; z < originZ + radius; z++) {
						double localX = (x - originX) / (float) radius;
						double localY = (y - originY) / ((float) radius);
						double localZ = (z - originZ) / (float) radius;
						double distanceSq = localX * localX + localY * localY + localZ * localZ;
						double frequency = 0.75F;
						double shapeNoise = noiseGenerator.getValue(x * frequency, y * frequency, z * frequency) * 0.5F;
						if (distanceSq <= 1.0F + shapeNoise) {
							mutable.set(x, y, z);
							if (bounds.isInside(mutable) && world.getBlockState(mutable).getBlock() == CORROCK_BLOCK) {
								world.setBlock(mutable, EUMUS_STATE, 2);
							}
						}
					}
				}
			}
		}

		@Override
		protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag compoundTag) {
		}

		@SuppressWarnings("deprecation")
		@Override
		public void postProcess(WorldGenLevel world, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomSource random, BoundingBox bounds, ChunkPos chunkPos, BlockPos pos) {
			GENERATING_BOUNDS.add(this.boundingBox);
			int originX = pos.getX();
			int originZ = pos.getZ();
			RandomState randomState = world.getLevel().getChunkSource().randomState();
			pos = new BlockPos(originX, chunkGenerator.getFirstFreeHeight(originX, originZ, Heightmap.Types.WORLD_SURFACE_WG, world, randomState), originZ);

			PerlinSimplexNoise surfaceNoise = SURFACE_NOISE.computeIfAbsent(world.getSeed(), seedL -> new PerlinSimplexNoise(new WorldgenRandom(RandomSource.create(seedL)), List.of(-3, 0)));
			NormalNoise tendrilNoise = randomState.getOrCreateNoise(EENoises.CORROCK_TENDRILS);
			int range = 60;
			int radius = 28;
			int radiusSquared = radius * radius;
			for (int x = -range; x < range; x++) {
				for (int z = -range; z < range; z++) {
					double noise = surfaceNoise.getValue(x, z, true);
					double areaNoise = noise * 12.0D - 7.0D;
					double distanceSq = x * x + z * z + areaNoise * areaNoise;
					int posX = originX + x;
					int posZ = originZ + z;
					if (distanceSq <= radiusSquared) {
						transformSurface(world, posX, posZ, world.getSeaLevel() / 2, 40, noise, bounds, CORROCK_BLOCK_STATE, EUMUS_STATE);
					} else {
						double tendrilNoiseValue = Math.abs(tendrilNoise.getValue(posX, 0.0D, posZ));
						double distanceDampening = distanceSq / radiusSquared;
						double tendrilProgress = 0.6D - 0.15D * distanceDampening - tendrilNoiseValue;
						if (tendrilProgress >= 0.0D) {
							transformSurface(world, posX, posZ, world.getSeaLevel() / 2, 40, noise, bounds, CORROCK_BLOCK_STATE, EUMUS_STATE);
						} else if (tendrilProgress >= -0.05D || (tendrilProgress >= -0.1D && random.nextFloat() <= -5.0D * tendrilProgress)) {
							transformSurface(world, posX, posZ, world.getSeaLevel() / 2, 24, noise, bounds, SPECKLED_CORROCK_STATE, END_STONE);
						}
					}
				}
			}

			BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
			BlockPos carvingPos = pos.below(24);
			int originY = carvingPos.getY();
			PerlinNoise undergroundNoise = UNDERGROUND_NOISE.computeIfAbsent(world.getSeed(), seedL -> PerlinNoise.create(new WorldgenRandom(RandomSource.create(seedL)), IntStream.rangeClosed(-4, -3)));

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

			nestDesign.arena.generate(world, carvingPos.above(2), random, this.arena, bounds);

			Map<Pair<Integer, Integer>, Integer> horizontalToMaxHeight = nestCore.horizontalToMaxHeight;
			StateMap stateMap = nestCore.stateMap;
			for (NestStalactite stalactite : nestDesign.stalactites) {
				int placingX = originX + stalactite.xOffset;
				int placingZ = originZ + stalactite.zOffset;
				mutable.set(placingX, originY, placingZ);
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
					mutable.set(carvingPos);
					for (int i = 0; i < 18; i++) {
						mutable.move(facing);
						if (stateMap.getBlockState(mutable).getBlock() == CORROCK_BLOCK) {
							break;
						}
					}
					tunnel.setup(mutable, chunkGenerator, world, randomState, undergroundNoise, random);
					BlockPos tunnelStart = tunnel.start;
					//Helps reduce the chance of shelves generating near tunnel entrances
					corrockShelves.removeIf(corrockShelf -> tunnelStart.distSqr(corrockShelf.pos) <= 64);
				}
			}
			nestCore.generateCorrockShelfs(world, bounds);

			//Generate caves first to ensure they don't block other tunnels
			for (NestTunnel tunnel : nestDesign.tunnels) {
				tunnel.generateCave(world, undergroundNoise, random, bounds);
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

			private NestDesign(RandomSource random) {
				int eumusPatchCount = random.nextInt(3) + 3;
				for (int i = 0; i < eumusPatchCount; i++) {
					this.eumusPatches.add(new EumusPatch(EumusPatch.PatchType.CORE, random));
				}
				this.arena = new NestArena(random);
				int stalactiteCount = random.nextInt(3) + 3;
				for (int i = 0; i < stalactiteCount; i++) {
					this.stalactites.add(new NestStalactite(random));
				}
				EetleEggBlock.shuffleDirections(TUNNEL_SIDES, random);
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

			private void setup(int originX, int originY, int originZ, RandomSource random, PerlinNoise undergroundNoise) {
				StateMap stateMap = this.stateMap;
				BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
				int horizontalRadius = 28;
				int verticalRadius = 18;
				for (int x = originX - horizontalRadius; x < originX + horizontalRadius; x++) {
					for (int z = originZ - horizontalRadius; z < originZ + horizontalRadius; z++) {
						for (int y = originY - verticalRadius; y < originY + verticalRadius; y++) {
							mutable.set(x, y, z);
							double localX = (x - originX) / (float) horizontalRadius;
							double localY = (y - originY) / (float) verticalRadius;
							double localZ = (z - originZ) / (float) horizontalRadius;
							double distanceSq = localX * localX + localY * localY + localZ * localZ;
							double frequency = 0.65F;
							double shapeNoise = undergroundNoise.getValue(x * frequency, y * frequency, z * frequency) * 0.5F;
							if (distanceSq >= 0.7F + shapeNoise && distanceSq <= 1.2F + shapeNoise) {
								stateMap.setBlockState(mutable, CORROCK_BLOCK_STATE);
							} else if (distanceSq <= 0.9F + shapeNoise) {
								stateMap.setBlockState(mutable, Blocks.CAVE_AIR.defaultBlockState());
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
					Direction horizontal = Direction.Plane.HORIZONTAL.getRandomDirection(random);
					BlockPos.MutableBlockPos mutable1 = new BlockPos.MutableBlockPos(originX, originY + MathUtil.makeNegativeRandomly(random.nextInt(8), random), originZ);
					if (random.nextFloat() < 0.25F) {
						for (int j = 0; j < horizontalRadius; j++) {
							mutable1.move(horizontal);
							if (stateMap.getBlockState(mutable1).getBlock() == CORROCK_BLOCK) {
								corrockShelves.add(new CorrockShelf(mutable1.relative(horizontal.getClockWise(), random.nextInt(4) - random.nextInt(4)), random));
							}
						}
					} else {
						int offset = random.nextInt(horizontalRadius + 1);
						mutable1.move(horizontal, offset);
						Direction leftOrRight = random.nextBoolean() ? horizontal.getClockWise() : horizontal.getCounterClockWise();
						for (int j = 0; j < horizontalRadius; j++) {
							mutable1.move(leftOrRight);
							if (stateMap.getBlockState(mutable1).getBlock() == CORROCK_BLOCK) {
								corrockShelves.add(new CorrockShelf(mutable1, random));
							}
						}
					}
				}
			}

			private void generate(WorldGenLevel world, BoundingBox bounds) {
				this.stateMap.generate(world, bounds);
			}

			private void generateCorrockPatches(WorldGenLevel world, RandomSource random, BoundingBox bounds) {
				BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
				List<BlockPos> corrockPatchPositions = this.corrockPatchPositions;
				for (int i = 0; i < corrockPatchPositions.size(); i++) {
					BlockPos corrockPos = corrockPatchPositions.get(i);
					if (bounds.isInside(corrockPos)) {
						mutable.set(corrockPos);
						for (int y = 0; y < 18; y++) {
							Block block = world.getBlockState(mutable).getBlock();
							if (block == CORROCK_BLOCK) {
								break;
							}
							mutable.move(Direction.DOWN);
						}
						corrockPatchPositions.set(i, corrockPos = mutable.immutable());
					}
					for (int j = 0; j < 32; j++) {
						mutable.setWithOffset(corrockPos, random.nextInt(8) - random.nextInt(8), random.nextInt(4) - random.nextInt(4), random.nextInt(8) - random.nextInt(8));
						if (bounds.isInside(mutable)) {
							if (world.isEmptyBlock(mutable) && CORROCK_STATE.canSurvive(world, mutable)) {
								Block below = world.getBlockState(mutable.below()).getBlock();
								if (below == CORROCK_BLOCK || random.nextFloat() < 0.4F) {
									world.setBlock(mutable, CORROCK_STATE, 2);
								}
							}
						}
					}
				}
			}

			private void generateCorrockShelfs(WorldGenLevel world, BoundingBox bounds) {
				for (CorrockShelf shelf : this.corrockShelves) {
					shelf.generate(world, bounds);
				}
			}

			private void generateCorrockShelfDecorations(WorldGenLevel world, BoundingBox bounds) {
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

			private EumusPatch(PatchType type, RandomSource random) {
				this.radius = random.nextInt(type.boundRadius) + type.minRadius;
				int maxHorizontalOffset = type.maxHorizontalOffset;
				this.xOffset = random.nextInt(maxHorizontalOffset) - random.nextInt(maxHorizontalOffset);
				int halfMaxYOffset = type.halfMaxYOffset;
				this.yOffset = (int) MathUtil.makeNegativeRandomly(random.nextInt(halfMaxYOffset + 1) + halfMaxYOffset, random);
				this.zOffset = random.nextInt(maxHorizontalOffset) - random.nextInt(maxHorizontalOffset);
			}

			private void generate(WorldGenLevel world, BlockPos center, PerlinNoise noiseGenerator, BoundingBox bounds) {
				createEumusPatch(world, center.offset(this.xOffset, this.yOffset, this.zOffset), noiseGenerator, this.radius, bounds);
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

			private NestArena(RandomSource random) {
				this.rotation = Rotation.getRandom(random);
				switch (this.bottomDoorDirection = Direction.Plane.HORIZONTAL.getRandomDirection(random)) {
					default -> this.stepsRotation = Rotation.COUNTERCLOCKWISE_90;
					case EAST -> this.stepsRotation = Rotation.NONE;
					case SOUTH -> this.stepsRotation = Rotation.CLOCKWISE_90;
					case WEST -> this.stepsRotation = Rotation.CLOCKWISE_180;
				}
				this.topDoorDirection = Direction.Plane.HORIZONTAL.getRandomDirection(random);
				this.topDoorLeftOrRight = random.nextBoolean() ? this.topDoorDirection.getClockWise() : this.topDoorDirection.getCounterClockWise();
			}

			private static void searchAndPlaceTopGate(WorldGenLevel world, BlockPos start, RandomSource random, Direction opposite, BoundingBox bounds) {
				BlockPos.MutableBlockPos mutable = start.mutable();
				for (int i = 0; i <= 7; i++) {
					if (bounds.isInside(mutable)) {
						if (world.getBlockState(mutable).getBlock() == CORROCK_BLOCK) {
							BlockPos up = mutable.above();
							world.setBlock(up, CORROCK_BLOCK_STATE, 2);
							tryToPlaceCrownAroundPos(world, up, random, bounds);
							break;
						}
					}
					mutable.move(opposite);
				}
			}

			private static void createOuterStairPiece(WorldGenLevel world, BlockPos start, RandomSource random, Direction opposite, BoundingBox bounds, int length) {
				BlockPos.MutableBlockPos mutable = start.mutable();
				for (int i = 0; i < length; i++) {
					if (bounds.isInside(mutable)) {
						world.setBlock(mutable, CORROCK_BLOCK_STATE, 2);
					}
					mutable.move(opposite);
					if (bounds.isInside(mutable) && world.getBlockState(mutable).getBlock() == CORROCK_BLOCK) {
						break;
					}
				}
				if (random.nextBoolean()) {
					tryToPlaceCrownAroundPos(world, start, random, bounds);
				}
			}

			private static void createLedgePiece(WorldGenLevel world, BlockPos middle, Direction right, Direction left, RandomSource random, int radius, boolean generateCrowns, BoundingBox bounds) {
				BlockPos.MutableBlockPos leftMutable = middle.mutable();
				BlockPos.MutableBlockPos rightMutable = middle.mutable();
				for (int i = 0; i <= radius; i++) {
					if (bounds.isInside(leftMutable)) {
						world.setBlock(leftMutable, CORROCK_BLOCK_STATE, 2);
					}
					if (bounds.isInside(rightMutable)) {
						world.setBlock(rightMutable, CORROCK_BLOCK_STATE, 2);
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

			private static int randomRange(int min, int max, RandomSource random) {
				return random.nextInt(max - min + 1) + min;
			}

			private static void tryToPlaceCrownAroundPos(WorldGenLevel world, BlockPos pos, RandomSource random, BoundingBox bounds) {
				EetleEggBlock.shuffleDirections(ATTACHMENT_DIRECTIONS, random);
				for (Direction direction : ATTACHMENT_DIRECTIONS) {
					if (direction.getAxis() == Direction.Axis.Y) {
						boolean upsideDown = direction == Direction.DOWN;
						BlockPos offset = upsideDown ? pos.below() : pos.above();
						if (bounds.isInside(offset)) {
							if (world.isEmptyBlock(offset)) {
								world.setBlock(offset, CROWN_STANDING_STATE.setValue(CorrockCrownStandingBlock.UPSIDE_DOWN, upsideDown).setValue(CorrockCrownStandingBlock.ROTATION, random.nextInt(16)), 2);
								break;
							}
						}
					} else {
						BlockPos offset = pos.relative(direction);
						if (bounds.isInside(offset)) {
							if (world.isEmptyBlock(offset)) {
								world.setBlock(offset, CROWN_WALL_STATE.setValue(CorrockCrownWallBlock.FACING, direction), 2);
								break;
							}
						}
					}
				}
			}

			private static void createHorizontalCrownedCluster(StateMap stateMap, BlockPos pos, RandomSource random) {
				BlockPos up = pos.above(2);
				for (Direction horizontal : Direction.Plane.HORIZONTAL) {
					if (random.nextFloat() < 0.8F) {
						BlockPos offset = pos.relative(horizontal);
						stateMap.setBlockState(offset, CORROCK_BLOCK_STATE);
						BlockPos offsetUp = offset.above();
						if (random.nextFloat() < 0.15F) {
							stateMap.setBlockState(offsetUp, CROWN_STANDING_STATE.setValue(CorrockCrownStandingBlock.ROTATION, random.nextInt(16)));
						}
					}
					if (random.nextFloat() < 0.8F) {
						BlockPos offset = up.relative(horizontal);
						stateMap.setBlockState(offset, CORROCK_BLOCK_STATE);
						BlockPos offsetUp = offset.above();
						if (random.nextFloat() < 0.2F) {
							stateMap.setBlockState(offsetUp, CROWN_STANDING_STATE.setValue(CorrockCrownStandingBlock.ROTATION, random.nextInt(16)));
						}
					}
				}
			}

			private static void fillForward(WorldGenLevel world, BlockPos pos, Direction forward, BoundingBox bounds) {
				BlockPos.MutableBlockPos mutable = pos.mutable();
				for (int i = 0; i <= 4; i++) {
					if (bounds.isInside(mutable)) {
						if (world.isEmptyBlock(mutable)) {
							world.setBlock(mutable, CORROCK_BLOCK_STATE, 2);
							mutable.move(forward);
						} else {
							break;
						}
					}
				}
			}

			private static void createLargeStepBottom(StateMap stateMap, BlockPos pos, RandomSource random) {
				for (Direction horizontal : Direction.Plane.HORIZONTAL) {
					if (random.nextBoolean()) {
						BlockPos offset = pos.relative(horizontal);
						stateMap.setBlockState(offset, CORROCK_BLOCK_STATE);
						if (random.nextFloat() < 0.25F) {
							stateMap.setBlockState(offset.above(), CROWN_STANDING_STATE.setValue(CorrockCrownStandingBlock.ROTATION, random.nextInt(16)));
						}
					} else if (random.nextBoolean()) {
						BlockPos offset = pos.relative(horizontal);
						stateMap.setBlockState(offset, CORROCK_BLOCK_STATE);
						stateMap.setBlockState(offset.above(), CORROCK_BLOCK_STATE);
						if (random.nextFloat() < 0.25F) {
							stateMap.setBlockState(offset.above(2), CROWN_STANDING_STATE.setValue(CorrockCrownStandingBlock.ROTATION, random.nextInt(16)));
						}
						if (random.nextBoolean()) {
							BlockPos doubleOffset = pos.relative(horizontal, 2);
							stateMap.setBlockState(doubleOffset, CORROCK_BLOCK_STATE);
							if (random.nextFloat() < 0.25F) {
								stateMap.setBlockState(doubleOffset.above(), CROWN_STANDING_STATE.setValue(CorrockCrownStandingBlock.ROTATION, random.nextInt(16)));
							}
						}
					}
				}
			}

			private static void createLargeStepTop(StateMap stateMap, BlockPos pos, RandomSource random) {
				for (Direction horizontal : Direction.Plane.HORIZONTAL) {
					BlockPos offset = pos.relative(horizontal);
					stateMap.setBlockState(offset, CORROCK_BLOCK_STATE);
					if (random.nextFloat() < 0.2F) {
						stateMap.setBlockState(offset.above(), CROWN_STANDING_STATE.setValue(CorrockCrownStandingBlock.ROTATION, random.nextInt(16)));
					}
					if (random.nextFloat() < 0.25F) {
						BlockPos otherOffset = offset.relative(Direction.Plane.HORIZONTAL.getRandomDirection(random));
						stateMap.setBlockState(otherOffset, CORROCK_BLOCK_STATE);
						if (random.nextFloat() < 0.2F) {
							stateMap.setBlockState(otherOffset.above(), CROWN_STANDING_STATE.setValue(CorrockCrownStandingBlock.ROTATION, random.nextInt(16)));
						}
						stateMap.setBlockState(offset.below(), CORROCK_BLOCK_STATE);
					}
				}
			}

			private static void carveForward(WorldGenLevel world, BlockPos pos, Direction forward, BoundingBox bounds) {
				BlockPos.MutableBlockPos mutable = pos.mutable();
				for (int i = 0; i <= 4; i++) {
					if (bounds.isInside(mutable)) {
						world.setBlock(mutable, CAVE_AIR, 2);
					}
					mutable.move(forward);
				}
			}

			private static void carveForwardLimited(WorldGenLevel world, BlockPos pos, Direction forward, BoundingBox bounds) {
				BlockPos.MutableBlockPos mutable = pos.mutable();
				int carved = 0;
				for (int i = 0; i <= 4; i++) {
					if (bounds.isInside(mutable)) {
						if (world.getBlockState(mutable).getBlock() == CORROCK_BLOCK) {
							world.setBlock(mutable, CAVE_AIR, 2);
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
						return pos.offset(22, 0, 0);
					case CLOCKWISE_180:
						return pos.offset(22, 0, 22);
					case COUNTERCLOCKWISE_90:
						return pos.offset(0, 0, 22);
				}
			}

			private void createWayUp(WorldGenLevel world, BlockPos center, Direction horizontal, Direction leftOrRight, RandomSource random, BoundingBox bounds) {
				BlockPos.MutableBlockPos mutable = center.mutable();
				mutable.move(horizontal, 7);

				carveForward(world, mutable, horizontal, bounds);
				carveForward(world, mutable.above(), horizontal, bounds);
				carveForward(world, mutable.above(2), horizontal, bounds);

				Direction cw = horizontal.getClockWise();
				BlockPos rotateCW = mutable.relative(cw);
				carveForward(world, rotateCW, horizontal, bounds);
				carveForward(world, rotateCW.above(), horizontal, bounds);

				Direction ccw = horizontal.getCounterClockWise();
				BlockPos rotateCCW = mutable.relative(ccw);
				carveForward(world, rotateCCW, horizontal, bounds);
				carveForward(world, rotateCCW.above(), horizontal, bounds);

				BlockPos outsideCenter = mutable.relative(horizontal, 2).below();

				for (int l = 0; l < 4; l++) {
					BlockPos offset = outsideCenter.relative(horizontal, l);
					createLedgePiece(world, offset, cw, ccw, random, 4 - l, true, bounds);
					createLedgePiece(world, offset.below(), cw, ccw, random, 2 - l, false, bounds);
					if (l == 3 && random.nextFloat() < 0.25F) {
						tryToPlaceCrownAroundPos(world, offset, random, bounds);
					}
				}

				BlockPos outsideThree = outsideCenter.relative(horizontal, 3);
				BlockPos firstStairEdge = outsideThree.relative(leftOrRight, 2);
				if (bounds.isInside(firstStairEdge)) {
					world.setBlock(firstStairEdge, CORROCK_BLOCK_STATE, 2);
					tryToPlaceCrownAroundPos(world, firstStairEdge, random, bounds);
				}

				Direction opposite = horizontal.getOpposite();
				mutable = outsideThree.mutable();
				mutable.move(leftOrRight, 2);
				int startY = mutable.getY();
				int term = 1;
				for (int n = 0; n < 6; n++) {
					term += (n % 2);
					mutable.setY(startY + term);
					createOuterStairPiece(world, mutable.move(leftOrRight), random, opposite, bounds, 5);
				}
				createOuterStairPiece(world, mutable.move(leftOrRight).move(opposite), random, opposite, bounds, 7);
				createOuterStairPiece(world, mutable.move(leftOrRight).move(opposite), random, opposite, bounds, 7);
				Direction leftOrRightOpposite = leftOrRight.getOpposite();
				carveForwardLimited(world, mutable.move(Direction.UP).move(leftOrRightOpposite), opposite, bounds);
				carveForwardLimited(world, mutable.move(leftOrRightOpposite), opposite, bounds);
				searchAndPlaceTopGate(world, mutable.move(leftOrRight, 2), random, opposite, bounds);
				searchAndPlaceTopGate(world, mutable.move(leftOrRightOpposite, 3), random, opposite, bounds);
			}

			private void createCenterSteps(WorldGenLevel world, BlockPos center, Rotation rotation, RandomSource random, BoundingBox bounds) {
				StateMap smallStep = this.smallSteps;
				BottomStepsRotationInfo bottomRotationInfo = BOTTOM_STEPS_ROTATION_INFO.get(rotation);

				int xDirection = bottomRotationInfo.xDirection;
				int zDirection = bottomRotationInfo.zDirection;
				Direction ledgeFillDirection = bottomRotationInfo.ledgeFillDirection;
				fillForward(world, center.offset(3 * xDirection, 0, 4 * zDirection), ledgeFillDirection, bounds);
				fillForward(world, center.offset(4 * xDirection, 0, 3 * zDirection), ledgeFillDirection, bounds);
				fillForward(world, center.offset(5 * xDirection, 0, 3 * zDirection), ledgeFillDirection, bounds);
				fillForward(world, center.offset(6 * xDirection, 0, 3 * zDirection), ledgeFillDirection, bounds);

				center = center.below(4);

				if (!smallStep.setup) {
					int centerX = center.getX();
					int centerY = center.getY();
					int centerZ = center.getZ();
					StateMap.fillAreaWithBlockCube(smallStep, centerX + bottomRotationInfo.firstSmallStepMinX, centerY, centerZ + bottomRotationInfo.firstSmallStepMinZ, centerX + bottomRotationInfo.firstSmallStepMaxX, centerY, centerZ + bottomRotationInfo.firstSmallStepMaxZ, CORROCK_BLOCK_STATE);
					if (random.nextBoolean()) {
						smallStep.setBlockState(center.offset(randomRange(bottomRotationInfo.firstSmallStepMinX, bottomRotationInfo.firstSmallStepMaxX, random), 1, randomRange(bottomRotationInfo.firstSmallStepMinZ, bottomRotationInfo.firstSmallStepMaxZ, random)), CROWN_STANDING_STATE.setValue(CorrockCrownStandingBlock.ROTATION, random.nextInt(16)));
					}

					StateMap.fillAreaWithBlockCube(smallStep, centerX + bottomRotationInfo.secondSmallStepMinX, centerY, centerZ + bottomRotationInfo.secondSmallStepMinZ, centerX + bottomRotationInfo.secondSmallStepMaxX, centerY + 1, centerZ + +bottomRotationInfo.secondSmallStepMaxZ, CORROCK_BLOCK_STATE);
					smallStep.setBlockState(center.offset(randomRange(bottomRotationInfo.secondSmallStepMinX, bottomRotationInfo.secondSmallStepMaxX, random), 0, randomRange(bottomRotationInfo.secondSmallStepMinZ, bottomRotationInfo.secondSmallStepMaxZ, random)), CAVE_AIR);
					if (random.nextFloat() < 0.6F) {
						smallStep.setBlockState(center.offset(randomRange(bottomRotationInfo.secondSmallStepMinX, bottomRotationInfo.secondSmallStepMaxX, random), 2, randomRange(bottomRotationInfo.secondSmallStepMinZ, bottomRotationInfo.secondSmallStepMaxZ, random)), CROWN_STANDING_STATE.setValue(CorrockCrownStandingBlock.ROTATION, random.nextInt(16)));
					}
					smallStep.setup = true;
				}
				smallStep.generate(world, bounds);

				this.createMediumStep(world, center.offset(bottomRotationInfo.mediumStepOffsetX, 0, bottomRotationInfo.mediumStepOffsetZ), random, bottomRotationInfo.mediumStepDirection, bounds);
				this.createLargeStep(world, center.offset(bottomRotationInfo.largeStepOffsetX, 0, bottomRotationInfo.largeStepOffsetZ), random, bottomRotationInfo.largeStepDirection, bounds);
			}

			private void createMediumStep(WorldGenLevel world, BlockPos pos, RandomSource random, Direction direction, BoundingBox bounds) {
				StateMap mediumStep = this.mediumStep;
				if (!mediumStep.setup) {
					BlockPos.MutableBlockPos mutable = pos.mutable();
					int startY = mutable.getY();
					for (int y = 0; y < 3; y++) {
						mutable.setY(startY + y);
						mediumStep.setBlockState(mutable, CORROCK_BLOCK_STATE);
					}
					createHorizontalCrownedCluster(mediumStep, pos, random);
					BlockPos offset = pos.relative(direction);
					mutable = offset.mutable();
					for (int y = 0; y < 3; y++) {
						mutable.setY(startY + y);
						mediumStep.setBlockState(mutable, CORROCK_BLOCK_STATE);
					}
					createHorizontalCrownedCluster(mediumStep, offset, random);
					mediumStep.setup = true;
				}
				mediumStep.stateMap.forEach((posEntry, state) -> {
					if (bounds.isInside(posEntry)) {
						if (state.getBlock() instanceof CorrockCrownBlock) {
							if (world.isEmptyBlock(posEntry)) {
								world.setBlock(posEntry, state, 2);
							}
						} else {
							world.setBlock(posEntry, state, 2);
						}
					}
				});
			}

			private void createLargeStep(WorldGenLevel world, BlockPos pos, RandomSource random, Direction direction, BoundingBox bounds) {
				StateMap largeStep = this.largeStep;
				if (!largeStep.setup) {
					BlockPos.MutableBlockPos mutable = pos.mutable();
					int startY = mutable.getY();
					for (int y = 0; y < 4; y++) {
						mutable.setY(startY + y);
						largeStep.setBlockState(mutable, CORROCK_BLOCK_STATE);
					}
					createLargeStepBottom(largeStep, pos, random);
					createLargeStepTop(largeStep, mutable, random);
					BlockPos offset = pos.relative(direction);
					mutable = offset.mutable();
					for (int y = 0; y < 4; y++) {
						mutable.setY(startY + y);
						largeStep.setBlockState(mutable, CORROCK_BLOCK_STATE);
					}
					createLargeStepBottom(largeStep, offset, random);
					createLargeStepTop(largeStep, mutable, random);
					largeStep.setup = true;
				}
				largeStep.stateMap.forEach((posEntry, state) -> {
					if (bounds.isInside(posEntry)) {
						if (state.getBlock() instanceof CorrockCrownBlock) {
							if (world.isEmptyBlock(posEntry)) {
								world.setBlock(posEntry, state, 2);
							}
						} else {
							world.setBlock(posEntry, state, 2);
						}
					}
				});
			}

			private void generate(WorldGenLevel world, BlockPos origin, RandomSource random, StructureTemplate arena, BoundingBox bounds) {
				BlockPos min = origin.offset(-11, -16, -11);
				BlockPos max = origin.offset(11, 1, 11);
				arena.placeInWorld(world, offsetPosForRotation(min, this.rotation), origin, new StructurePlaceSettings().setRotation(this.rotation).setBoundingBox(bounds), random, 2);
				BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
				//Fill in bottom areas to prevent floating cases
				int extraFillBottomY = origin.getY() - 16;
				for (int x = min.getX(); x <= max.getX(); x++) {
					for (int z = min.getZ(); z <= max.getZ(); z++) {
						mutable.set(x, extraFillBottomY, z);
						if (bounds.isInside(mutable)) {
							if (world.getBlockState(mutable).getBlock() == Blocks.GOLD_BLOCK) {
								world.setBlock(mutable, CORROCK_BLOCK_STATE, 2);
								for (int y = extraFillBottomY; y > extraFillBottomY - 3; y--) {
									mutable.setY(y);
									world.setBlock(mutable, CORROCK_BLOCK_STATE, 2);
								}
							}
						}
					}
				}

				mutable = origin.below(3).mutable();
				if (bounds.isInside(mutable)) {
					BroodEetle broodEetle = EEEntityTypes.BROOD_EETLE.get().create(world.getLevel());
					if (broodEetle != null) {
						broodEetle.setSleeping(true);
						broodEetle.moveTo(mutable.getX() + 0.5D, mutable.getY(), mutable.getZ() + 0.5D, 0.0F, 0.0F);
						broodEetle.finalizeSpawn(world, world.getCurrentDifficultyAt(broodEetle.blockPosition()), MobSpawnType.STRUCTURE, null, null);
						world.addFreshEntityWithPassengers(broodEetle);
					}
				}

				mutable.move(Direction.DOWN, 10);

				//Create bottom doorway
				Direction horizontal = this.bottomDoorDirection;
				mutable.move(horizontal, 6);
				carveForward(world, mutable, horizontal, bounds);
				carveForward(world, mutable.above(), horizontal, bounds);
				carveForward(world, mutable.above(2), horizontal, bounds);

				BlockPos rotateCW = mutable.relative(horizontal.getClockWise());
				carveForward(world, rotateCW, horizontal, bounds);
				carveForward(world, rotateCW.above(), horizontal, bounds);

				BlockPos rotateCCW = mutable.relative(horizontal.getCounterClockWise());
				carveForward(world, rotateCCW, horizontal, bounds);
				carveForward(world, rotateCCW.above(), horizontal, bounds);

				this.createCenterSteps(world, origin.below(9), this.stepsRotation, random, bounds);

				this.createWayUp(world, origin.below(7), this.topDoorDirection, this.topDoorLeftOrRight, random, bounds);

				int arenaTopY = origin.getY() - 3;
				//Adds crowns to top of arena
				for (int x = min.getX(); x <= max.getX(); x++) {
					for (int z = min.getZ(); z <= max.getZ(); z++) {
						mutable.set(x, arenaTopY, z);
						if (bounds.isInside(mutable)) {
							if (world.getBlockState(mutable).getBlock() == CORROCK_BLOCK && random.nextBoolean()) {
								EetleEggBlock.shuffleDirections(ATTACHMENT_DIRECTIONS, random);
								for (Direction direction : ATTACHMENT_DIRECTIONS) {
									if (direction.getAxis() == Direction.Axis.Y) {
										direction = Direction.UP;
										BlockPos offset = mutable.relative(direction);
										if (bounds.isInside(offset) && world.isEmptyBlock(offset)) {
											world.setBlock(offset, CROWN_STANDING_STATE.setValue(CorrockCrownStandingBlock.ROTATION, random.nextInt(16)), 2);
											break;
										}
									} else {
										BlockPos offset = mutable.relative(direction);
										if (bounds.isInside(offset) && world.isEmptyBlock(offset) && world.isEmptyBlock(offset.below())) {
											world.setBlock(offset, CROWN_WALL_STATE.setValue(CorrockCrownWallBlock.FACING, direction), 2);
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
				BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
				for (int yy = y1; yy <= y2; yy++) {
					for (int xx = x1; xx <= x2; xx++) {
						for (int zz = z1; zz <= z2; zz++) {
							mutable.set(xx, yy, zz);
							stateMap.setBlockState(mutable, block);
						}
					}
				}
			}

			private void generate(WorldGenLevel world, BoundingBox bounds) {
				this.stateMap.forEach((pos, blockState) -> {
					if (bounds.isInside(pos)) {
						world.setBlock(pos, blockState, 2);
					}
				});
			}

			private void setBlockState(BlockPos pos, BlockState state) {
				this.stateMap.put(pos.immutable(), state);
			}

			private BlockState getBlockState(BlockPos pos) {
				return this.stateMap.getOrDefault(pos, Blocks.AIR.defaultBlockState());
			}
		}

		private static class NestStalactite {
			private final int radius;
			private final int length;
			private final int xOffset;
			private final int zOffset;

			private NestStalactite(RandomSource random) {
				this.radius = random.nextInt(2) + 3;
				this.length = random.nextInt(3) + 5;
				this.xOffset = random.nextInt(15) - random.nextInt(15);
				this.zOffset = random.nextInt(15) - random.nextInt(15);
			}

			private void generate(WorldGenLevel world, BlockPos origin, RandomSource random, PerlinNoise noiseGenerator, BoundingBox boundingBox) {
				List<BlockPos> possibleDecorationPositions = new ArrayList<>();
				BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
				int originX = origin.getX();
				int originY = origin.getY();
				int originZ = origin.getZ();
				int radius = this.radius;
				int length = this.length;
				float noiseMultiplier = radius * 0.25F;
				for (int x = originX - radius; x < originX + radius; x++) {
					for (int y = originY - length; y < originY; y++) {
						for (int z = originZ - radius; z < originZ + radius; z++) {
							mutable.set(x, y, z);
							if (boundingBox.isInside(mutable)) {
								double localX = (x - originX) / (float) radius;
								double localY = (y - originY) / ((float) length - 1);
								double localZ = (z - originZ) / (float) radius;
								double distanceSq = localX * localX + localY * localY + localZ * localZ;
								double frequency = 2.0F;
								double shapeNoise = noiseGenerator.getValue(x * frequency, y * frequency, z * frequency) * noiseMultiplier;
								if (distanceSq <= 1.0F + shapeNoise) {
									for (int yU = 0; yU <= length; yU++) {
										Block block = world.getBlockState(mutable).getBlock();
										if (world.isEmptyBlock(mutable) || block == EETLE_EGSS || block instanceof CorrockCrownBlock) {
											possibleDecorationPositions.add(mutable.immutable());
											world.setBlock(mutable, CORROCK_BLOCK_STATE, 2);
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
						EetleEggBlock.shuffleDirections(ATTACHMENT_DIRECTIONS, random);
						for (Direction direction : ATTACHMENT_DIRECTIONS) {
							BlockPos offset = pos.relative(direction);
							if (boundingBox.isInside(offset) && world.isEmptyBlock(offset)) {
								if (random.nextBoolean()) {
									world.setBlock(offset, EETLE_EGGS_STATE.setValue(EetleEggBlock.FACING, direction), 2);
								} else {
									if (direction.getAxis() == Direction.Axis.Y) {
										world.setBlock(offset, CROWN_STANDING_STATE.setValue(CorrockCrownStandingBlock.UPSIDE_DOWN, direction == Direction.DOWN).setValue(CorrockCrownStandingBlock.ROTATION, random.nextInt(16)), 2);
									} else {
										world.setBlock(offset, CROWN_WALL_STATE.setValue(CorrockCrownWallBlock.FACING, direction), 2);
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
			private static final Vec3 UP = new Vec3(0.0D, 1.0D, 1.0D);
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

			private NestTunnel(RandomSource random, Direction facing) {
				this.facing = facing;
				this.yOffset = random.nextInt(6) - random.nextInt(6);
				Direction rotateY = facing.getClockWise();
				int offset = random.nextInt(11) - random.nextInt(11);
				this.xOffset = rotateY.getStepX() * offset;
				this.zOffset = rotateY.getStepZ() * offset;
			}

			private void setup(BlockPos startPos, ChunkGenerator chunkGenerator, LevelHeightAccessor heightAccessor, RandomState randomState, PerlinNoise noiseGenerator, RandomSource random) {
				startPos = startPos.offset(this.xOffset, this.yOffset, this.zOffset);
				this.start = startPos;
				List<Vec3> points = new ArrayList<>();
				Vec3 startVec = Vec3.atLowerCornerOf(startPos);
				BlockPos end;
				Direction facing = this.facing;
				if (this.goesToSurface) {
					end = startPos.relative(facing, 16 + random.nextInt(9)).relative(facing.getClockWise(), (int) MathUtil.makeNegativeRandomly(random.nextInt(7) + 6, random));
					int endX = end.getX();
					int endZ = end.getZ();
					int topY = chunkGenerator.getFirstFreeHeight(endX, endZ, Heightmap.Types.WORLD_SURFACE, heightAccessor, randomState);
					end = new BlockPos(endX, topY, endZ);

					Vec3 endVec = Vec3.atLowerCornerOf(end);
					Vec3 difference = endVec.subtract(startVec);
					Vec3 normalizedDifference = difference.normalize();

					Vec3 anchorStart = startVec.subtract(normalizedDifference).add(-6 * facing.getStepX(), 0, -6 * facing.getStepZ());
					Vec3 anchorEnd = endVec.add(normalizedDifference).add(0, 6, 0);

					points.add(anchorStart);
					points.add(startVec);

					Vec3 offset = UP.cross(normalizedDifference);
					double offsetX = offset.x;
					double offsetZ = offset.z;
					int iterations = 6;
					for (int i = 0; i < iterations; i++) {
						points.add(startVec.add(difference.scale(i / (float) iterations)).add(offsetX * (random.nextDouble() - 0.5D) * 5.0D, (random.nextDouble() - 0.25D) * 5.0D, offsetZ * (random.nextDouble() - 0.5D) * 5.0D));
					}

					points.add(endVec);
					points.add(anchorEnd);
				} else {
					end = startPos.relative(facing, 24 + random.nextInt(9)).relative(facing.getClockWise(), (int) MathUtil.makeNegativeRandomly(random.nextInt(7) + 6, random)).relative(Direction.UP, (int) MathUtil.makeNegativeRandomly(random.nextInt(7) + 6, random));
					Vec3 endVec = Vec3.atLowerCornerOf(end);
					Vec3 difference = endVec.subtract(startVec);
					Vec3 normalizedDifference = difference.normalize();

					float xOffset = facing.getStepX();
					float zOffset = facing.getStepZ();
					Vec3 anchorStart = startVec.subtract(normalizedDifference).add(-6 * xOffset, 0, -6 * zOffset);
					Vec3 anchorEnd = endVec.add(normalizedDifference).add(-6 * xOffset, 0, -6 * zOffset);

					points.add(anchorStart);
					points.add(startVec);

					Vec3 offset = UP.cross(normalizedDifference);
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
						nestCave.setup(end, random, chunkGenerator, heightAccessor, randomState, noiseGenerator);
					}
				}
				MathUtil.CatmullRomSpline spline = new MathUtil.CatmullRomSpline(points.toArray(new Vec3[0]), MathUtil.CatmullRomSpline.SplineType.CHORDAL);
				int steps = (int) (10 + Math.sqrt(startPos.distSqr(end)) * 3);
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
											eggsAndCorrock.add(new EetleEggPatch(spherePos));
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

			private void generateCave(WorldGenLevel world, PerlinNoise noiseGenerator, RandomSource random, BoundingBox bounds) {
				NestCave nestCave = this.nestCave;
				if (nestCave != null) {
					nestCave.generate(world, noiseGenerator, random, bounds);
				}
			}

			private void generate(WorldGenLevel world, RandomSource random, BoundingBox bounds) {
				for (BlockPos pos : this.airPositions) {
					if (bounds.isInside(pos) && CARVABLE_BLOCKS.contains(world.getBlockState(pos).getBlock())) {
						world.setBlock(pos, CAVE_AIR, 2);
					}
				}
				int radius = 1;
				BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
				for (BlockPos corrockPos : this.corrockPositions) {
					int originX = corrockPos.getX();
					int originY = corrockPos.getY();
					int originZ = corrockPos.getZ();
					for (int x = -radius; x <= radius; x++) {
						for (int y = -radius; y <= radius; y++) {
							for (int z = -radius; z <= radius; z++) {
								mutable.set(originX + x, originY + y, originZ + z);
								if (bounds.isInside(mutable) && CORROCK_REPLACEABLE.contains(world.getBlockState(mutable).getBlock())) {
									world.setBlock(mutable, CORROCK_BLOCK_STATE, 2);
									if (random.nextBoolean()) {
										mutable.move(Direction.getRandom(random));
										if (bounds.isInside(mutable) && CORROCK_REPLACEABLE.contains(world.getBlockState(mutable).getBlock())) {
											world.setBlock(mutable, CORROCK_BLOCK_STATE, 2);
										}
									}
								}
							}
						}
					}
				}
			}

			private void generateDecorations(WorldGenLevel world, PerlinNoise noiseGenerator, RandomSource random, BoundingBox bounds) {
				for (TunnelDecoration decoration : this.decorations) {
					if (decoration.isNotSetup()) {
						decoration.setup(random);
					}
					decoration.generate(world, noiseGenerator, random, bounds);
				}
			}

			interface TunnelDecoration {
				void setup(RandomSource random);

				void generate(WorldGenLevel world, PerlinNoise noiseGenerator, RandomSource random, BoundingBox bounds);

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
				public void setup(RandomSource random) {
				}

				@Override
				public void generate(WorldGenLevel world, PerlinNoise noiseGenerator, RandomSource random, BoundingBox bounds) {
					createEumusPatch(world, this.origin, noiseGenerator, this.radius, bounds);
				}

				@Override
				public boolean isNotSetup() {
					return false;
				}
			}

			static class EetleEggPatch implements TunnelDecoration {
				private final StateMap stateMap = new StateMap();
				private final BlockPos origin;

				EetleEggPatch(BlockPos origin) {
					this.origin = origin;
				}

				@Override
				public void setup(RandomSource random) {
					StateMap stateMap = this.stateMap;
					BlockPos origin = this.origin;
					BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
					EetleEggBlock.shuffleDirections(EGG_DIRECTIONS, random);
					for (int j = 0; j < 48; j++) {
						mutable.setWithOffset(origin, random.nextInt(9) - random.nextInt(9), random.nextInt(9) - random.nextInt(9), random.nextInt(9) - random.nextInt(9));
						if (random.nextFloat() < 0.4F) {
							for (Direction direction : EGG_DIRECTIONS) {
								BlockState state = EETLE_EGGS_STATE.setValue(EetleEggBlock.FACING, direction.getOpposite());
								stateMap.setBlockState(mutable, state.setValue(EetleEggBlock.SIZE, random.nextFloat() < 0.75F ? 0 : random.nextFloat() < 0.6F ? 1 : 2));
								break;
							}
						}
					}
					stateMap.setup = true;
				}

				public void generate(WorldGenLevel world, PerlinNoise noiseGenerator, RandomSource random, BoundingBox bounds) {
					StateMap stateMap = this.stateMap;
					stateMap.stateMap.forEach((pos, state) -> {
						if (bounds.isInside(pos) && world.isEmptyBlock(pos)) {
							BlockPos offset = pos.relative(state.getValue(EetleEggBlock.FACING).getOpposite());
							if (bounds.isInside(offset)) {
								Block opposite = world.getBlockState(offset).getBlock();
								if (opposite == CORROCK_BLOCK || opposite == EUMUS) {
									world.setBlock(pos, state, 2);
									NestCave.EetleEggPatch.spreadInfestedCorrockAtPos(world, offset, random, bounds);
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
				public void setup(RandomSource random) {
					StateMap stateMap = this.stateMap;
					BlockPos origin = this.origin;
					BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
					for (int i = 0; i < 32; i++) {
						mutable.setWithOffset(origin, random.nextInt(8) - random.nextInt(8), random.nextInt(4) - random.nextInt(4), random.nextInt(8) - random.nextInt(8));
						if (random.nextBoolean()) {
							stateMap.setBlockState(mutable, CORROCK_STATE);
						}
					}
					stateMap.setup = true;
				}

				@Override
				public void generate(WorldGenLevel world, PerlinNoise noiseGenerator, RandomSource random, BoundingBox bounds) {
					StateMap stateMap = this.stateMap;
					stateMap.stateMap.forEach((pos, state) -> {
						if (bounds.isInside(pos) && world.isEmptyBlock(pos)) {
							Block below = world.getBlockState(pos.below()).getBlock();
							if (below == CORROCK_BLOCK || below == EUMUS) {
								world.setBlock(pos, state, 2);
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
				private final List<EetleEggPatch> eetleEggPatches = new ArrayList<>();
				private final List<CorrockPatch> corrockPatches = new ArrayList<>();
				private BlockPos center;

				private NestCave(RandomSource random) {
					this.type = NestCaveType.random(random);
				}

				private void setup(BlockPos end, RandomSource random, ChunkGenerator chunkGenerator, LevelHeightAccessor heightAccessor, RandomState randomState, PerlinNoise noiseGenerator) {
					this.center = end;
					NestCaveType type = this.type;
					int horizontalRadius = type.horizontalRadius;
					int verticalRadius = type.verticalRadius;
					if (isAreaCarvable(end, horizontalRadius, verticalRadius, chunkGenerator, heightAccessor, randomState)) {
						int endX = end.getX();
						int endY = end.getY();
						int endZ = end.getZ();
						BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
						StateMap cave = this.cave;
						for (int x = endX - horizontalRadius; x <= endX + horizontalRadius; x++) {
							for (int z = endZ - horizontalRadius; z <= endZ + horizontalRadius; z++) {
								for (int y = endY - verticalRadius; y <= endY + verticalRadius; y++) {
									mutable.set(x, y, z);

									double localX = (x - endX) / (float) horizontalRadius;
									double localY = (y - endY) / (float) verticalRadius;
									double localZ = (z - endZ) / (float) horizontalRadius;
									double distanceSq = localX * localX + localY * localY + localZ * localZ;
									double frequency = 0.65F;
									double shapeNoise = noiseGenerator.getValue(x * frequency, y * frequency, z * frequency) * 0.5F;
									if (distanceSq >= 0.7F + shapeNoise && distanceSq <= 1.2F + shapeNoise) {
										cave.setBlockState(mutable, CORROCK_BLOCK_STATE);
									} else if (distanceSq <= 0.9F + shapeNoise) {
										cave.setBlockState(mutable, Blocks.CAVE_AIR.defaultBlockState());
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
						List<EetleEggPatch> eetleEggPatches = this.eetleEggPatches;
						for (int i = 0; i < type.eetleEggPatches; i++) {
							eetleEggPatches.add(new EetleEggPatch(end.offset(random.nextInt(horizontalRadius) - random.nextInt(horizontalRadius), random.nextInt(verticalRadius) - random.nextInt(verticalRadius), random.nextInt(horizontalRadius) - random.nextInt(horizontalRadius)), random));
						}
						List<CorrockPatch> corrockPatches = this.corrockPatches;
						for (int i = 0; i < type.corrockPatches; i++) {
							corrockPatches.add(new CorrockPatch(end.offset(random.nextInt(horizontalRadius) - random.nextInt(horizontalRadius), -random.nextInt(verticalRadius), random.nextInt(horizontalRadius) - random.nextInt(horizontalRadius)), random));
						}
					}
				}

				private void generate(WorldGenLevel world, PerlinNoise noiseGenerator, RandomSource random, BoundingBox bounds) {
					this.cave.generate(world, bounds);
					BlockPos center = this.center;
					for (EetleNestPiece.EumusPatch eumusPatch : this.eumusPatches) {
						eumusPatch.generate(world, center, noiseGenerator, bounds);
					}
					for (EetleEggPatch eetleEggPatch : this.eetleEggPatches) {
						eetleEggPatch.generate(world, random, bounds);
					}
					for (CorrockPatch corrockPatch : this.corrockPatches) {
						corrockPatch.generate(world, bounds);
					}
				}

				private static boolean isAreaCarvable(BlockPos center, int horizontalRadius, int verticalRadius, ChunkGenerator chunkGenerator, LevelHeightAccessor heightAccessor, RandomState randomState) {
					int foundAirBlocks = 0;
					int centerX = center.getX();
					int centerY = center.getY();
					int centerZ = center.getZ();
					int maxAirBlocks = (int) (horizontalRadius * 2 * horizontalRadius * 2 * 0.25F);
					for (int x = centerX - horizontalRadius; x <= centerX + horizontalRadius; x++) {
						for (int z = centerZ - horizontalRadius; z <= centerZ + horizontalRadius; z++) {
							NoiseColumn column = chunkGenerator.getBaseColumn(x, z, heightAccessor, randomState);
							for (int y = centerY - verticalRadius; y <= centerY + verticalRadius; y++) {
								Block block = column.getBlock(y).getBlock();
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

				static class EetleEggPatch {
					private final StateMap stateMap = new StateMap();

					EetleEggPatch(BlockPos origin, RandomSource random) {
						EetleEggBlock.shuffleDirections(EGG_DIRECTIONS, random);
						BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
						StateMap stateMap = this.stateMap;
						for (int j = 0; j < 48; j++) {
							mutable.setWithOffset(origin, random.nextInt(9) - random.nextInt(9), random.nextInt(9) - random.nextInt(9), random.nextInt(9) - random.nextInt(9));
							if (random.nextFloat() < 0.4F) {
								for (Direction direction : EGG_DIRECTIONS) {
									BlockState state = EETLE_EGGS_STATE.setValue(EetleEggBlock.FACING, direction.getOpposite());
									stateMap.setBlockState(mutable, state.setValue(EetleEggBlock.SIZE, random.nextFloat() < 0.75F ? 0 : random.nextFloat() < 0.6F ? 1 : 2));
									break;
								}
							}
						}
					}

					private void generate(WorldGenLevel world, RandomSource random, BoundingBox bounds) {
						this.stateMap.stateMap.forEach((pos, state) -> {
							if (bounds.isInside(pos) && world.isEmptyBlock(pos)) {
								BlockPos offset = pos.relative(state.getValue(EetleEggBlock.FACING).getOpposite());
								if (bounds.isInside(offset)) {
									Block opposite = world.getBlockState(offset).getBlock();
									if (opposite == CORROCK_BLOCK || opposite == EUMUS) {
										world.setBlock(pos, state, 2);
										spreadInfestedCorrockAtPos(world, offset, random, bounds);
									}
								}
							}
						});
					}

					private static void spreadInfestedCorrockAtPos(WorldGenLevel world, BlockPos pos, RandomSource random, BoundingBox bounds) {
						int radius = 1;
						if (bounds.isInside(pos)) {
							world.setBlock(pos, INFESTED_STATE, 2);
						}
						BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
						for (int x = -radius; x <= radius; x++) {
							for (int y = -radius; y <= radius; y++) {
								for (int z = -radius; z <= radius; z++) {
									if (bounds.isInside(mutable.setWithOffset(pos, x, y, z))) {
										if (world.getBlockState(mutable).getBlock() == CORROCK_BLOCK && random.nextFloat() <= 0.25F) {
											world.setBlock(mutable, INFESTED_STATE, 2);
										}
									}
								}
							}
						}
					}
				}

				static class CorrockPatch {
					private final StateMap stateMap = new StateMap();

					CorrockPatch(BlockPos origin, RandomSource random) {
						StateMap stateMap = this.stateMap;
						BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
						for (int i = 0; i < 32; i++) {
							mutable.setWithOffset(origin, random.nextInt(8) - random.nextInt(8), random.nextInt(4) - random.nextInt(4), random.nextInt(8) - random.nextInt(8));
							if (random.nextBoolean()) {
								stateMap.setBlockState(mutable, CORROCK_STATE);
							}
						}
					}

					private void generate(WorldGenLevel world, BoundingBox bounds) {
						this.stateMap.stateMap.forEach((pos, state) -> {
							if (bounds.isInside(pos) && world.isEmptyBlock(pos)) {
								Block below = world.getBlockState(pos.below()).getBlock();
								if (below == CORROCK_BLOCK || below == EUMUS) {
									world.setBlock(pos, state, 2);
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

					private static NestCaveType random(RandomSource random) {
						return VALUES[random.nextInt(VALUES.length)];
					}
				}
			}
		}

		static class CorrockShelf {
			private static final Direction[] HORIZONTALS = Direction.Plane.HORIZONTAL.stream().toArray(Direction[]::new);
			private final BlockPos pos;
			private final StateMap corrock = new StateMap();
			private final StateMap decorations = new StateMap();

			CorrockShelf(BlockPos pos, RandomSource random) {
				this.pos = pos;
				int size = random.nextBoolean() ? 3 : 4;
				int edgeBias = 10;
				int min = -size;
				int originX = pos.getX();
				int originY = pos.getY();
				int originZ = pos.getZ();
				int underXDistance = random.nextInt(2) + 2;
				int underZDistance = random.nextInt(2) + 2;
				BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
				StateMap corrock = this.corrock;
				StateMap decorations = this.decorations;
				List<BlockPos> wallCrowns = new ArrayList<>();
				for (int x = min; x <= size; x++) {
					for (int z = min; z <= size; z++) {
						mutable.set(originX + x, originY, originZ + z);
						double radius = (Math.cos(4 * Math.atan2(z, x)) / edgeBias + 1) * size;
						int distance = x * x + z * z;
						if (distance < radius * radius) {
							corrock.setBlockState(mutable, CORROCK_BLOCK_STATE);
							if (x * x < (radius - underXDistance) * (radius - underXDistance) && z * z < (radius - underZDistance) * (radius - underZDistance)) {
								BlockPos down = mutable.below();
								corrock.setBlockState(down, CORROCK_BLOCK_STATE);
							}
							if (random.nextFloat() < 0.75F) {
								double radiusMinusOne = radius - 1.0F;
								if (distance > radiusMinusOne * radiusMinusOne) {
									if (random.nextFloat() < 0.25F) {
										decorations.setBlockState(mutable.above(), CROWN_STANDING_STATE.setValue(CorrockCrownStandingBlock.ROTATION, random.nextInt(16)));
									} else {
										wallCrowns.add(mutable.immutable());
									}
								}
							}
						}
					}
				}
				Map<BlockPos, BlockState> corrockStateMap = corrock.stateMap;
				for (BlockPos crownPos : wallCrowns) {
					int crownsPlaced = 0;
					EetleEggBlock.shuffleDirections(HORIZONTALS, random);
					for (Direction direction : HORIZONTALS) {
						BlockPos offset = crownPos.relative(direction);
						if (!corrockStateMap.containsKey(offset)) {
							decorations.setBlockState(offset, CROWN_WALL_STATE.setValue(CorrockCrownWallBlock.FACING, direction));
							if (random.nextFloat() > 0.75F || crownsPlaced++ == 1) {
								break;
							}
						}
					}
				}
			}

			private void generate(WorldGenLevel world, BoundingBox bounds) {
				this.corrock.stateMap.forEach((pos, state) -> {
					if (bounds.isInside(pos) && world.isEmptyBlock(pos)) {
						world.setBlock(pos, state, 2);
					}
				});
			}

			private void generateDecorations(WorldGenLevel world, BoundingBox bounds) {
				this.decorations.stateMap.forEach((pos, state) -> {
					if (bounds.isInside(pos) && world.isEmptyBlock(pos)) {
						if (state.getBlock() instanceof CorrockCrownStandingBlock) {
							if (world.getBlockState(pos.below()).getBlock() == CORROCK_BLOCK) {
								world.setBlock(pos, state, 2);
							}
						} else {
							BlockPos offset = pos.relative(state.getValue(CorrockCrownWallBlock.FACING).getOpposite());
							if (bounds.isInside(offset)) {
								if (world.getBlockState(offset).getBlock() == CORROCK_BLOCK) {
									world.setBlock(pos, state, 2);
								}
							}
						}
					}
				});
			}
		}
	}
}
