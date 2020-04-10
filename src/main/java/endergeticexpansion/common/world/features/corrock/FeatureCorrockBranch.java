package endergeticexpansion.common.world.features.corrock;

import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.mojang.datafixers.Dynamic;

import endergeticexpansion.api.generation.GenerationPiece;
import endergeticexpansion.api.generation.IAddToBiomes;
import endergeticexpansion.common.blocks.BlockCorrockCrownStanding;
import endergeticexpansion.common.blocks.BlockCorrockCrownWall;
import endergeticexpansion.common.world.features.EEFeatures;
import endergeticexpansion.core.registry.EEBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChorusFlowerBlock;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.PooledMutable;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.ProbabilityConfig;
import net.minecraft.world.gen.feature.SphereReplaceConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidWithNoiseConfig;

public class FeatureCorrockBranch extends AbstractCorrockFeature {

	public FeatureCorrockBranch(Function<Dynamic<?>, ? extends ProbabilityConfig> configFactory) {
		super(configFactory);
	}

	@Override
	public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, ProbabilityConfig config) {
		int baseHeight = rand.nextInt(4) + 4;
		if(rand.nextFloat() > config.probability) return false;
		
		List<ChorusPlantPart> chorusGrowths = Lists.newArrayList();
		
		if(world.getBlockState(pos.down()).getBlock() == Blocks.END_STONE) {
			GenerationPiece basePiece = new GenerationPiece((iworld, part) -> world.isAirBlock(part.pos));
			this.createBase(basePiece, world, pos, rand, baseHeight);
			if(basePiece.canPlace(world)) {
				int branchCount = rand.nextBoolean() ? 1 : rand.nextInt(3) + 1;
				GenerationPiece[] branchPieces = this.createBranches(chorusGrowths, world, pos, rand, branchCount, baseHeight);
				if(branchPieces[0].canPlace(world)) {
					basePiece.place(world);
					branchPieces[0].place(world);
					
					for(ChorusPlantPart chorusParts : this.getAllChorusPartsMatchingPiece(chorusGrowths, branchPieces[0])) {
						chorusParts.placeGrowth(world, rand);
					}
					
					if(branchCount > 1) {
						if(branchPieces[1].canPlace(world)) {
							branchPieces[1].place(world);
							
							for(ChorusPlantPart chorusParts : this.getAllChorusPartsMatchingPiece(chorusGrowths, branchPieces[1])) {
								chorusParts.placeGrowth(world, rand);
							}
						}
						if(branchCount > 2) {
							if(branchPieces[2].canPlace(world)) {
								branchPieces[2].place(world);
								
								for(ChorusPlantPart chorusParts : this.getAllChorusPartsMatchingPiece(chorusGrowths, branchPieces[2])) {
									chorusParts.placeGrowth(world, rand);
								}
							}
						}
					}
					
					BlockPos downPos = pos.down();
					BlockPos groundModifierPos = new BlockPos(downPos.getX() + (rand.nextInt(3) - rand.nextInt(3)), downPos.getY(), downPos.getZ() + (rand.nextInt(3) - rand.nextInt(3)));
					EEFeatures.CORROCK_GROUND_PATCH.get().place(world, generator, rand, groundModifierPos, new SphereReplaceConfig(CORROCK_BLOCK.get(), 3, 3, Lists.newArrayList(Blocks.END_STONE.getDefaultState())));
					
					PooledMutable corrockPlantPos = PooledMutable.retain();
					for(int x = pos.getX() - 4; x < pos.getX() + 4; x++) {
						for(int y = pos.getY(); y < pos.getY() + baseHeight + 10; y++) {
							for(int z = pos.getZ() - 4; z < pos.getZ() + 4; z++) {
								corrockPlantPos.setPos(x, y, z);
								boolean isCorrockBelow = world.getBlockState(corrockPlantPos.down()).getBlock() == EEBlocks.CORROCK_END_BLOCK.get();
								if((isCorrockBelow && rand.nextFloat() < 0.5F || !isCorrockBelow && rand.nextFloat() < 0.025F) && world.isAirBlock(corrockPlantPos) && CORROCK.get().isValidPosition(world, corrockPlantPos)) {
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
	
	private GenerationPiece[] createBranches(List<ChorusPlantPart> chorusGrowths, IWorld world, BlockPos pos, Random rand, int count, int height) {
		GenerationPiece[] pieces = new GenerationPiece[count];
		for(int i = 0; i < pieces.length; i++) {
			pieces[i] = this.createBranch(chorusGrowths, world, pos, rand, height);
		}
		return pieces;
	}
	
	private GenerationPiece createBranch(List<ChorusPlantPart> chorusGrowths, IWorld world, BlockPos pos, Random rand, int height) {
		GenerationPiece basePiece = new GenerationPiece((iworld, part) -> world.isAirBlock(part.pos));
		BlockPos startPos = pos.up(height - 1);
		Direction horizontalStep = this.randomHorizontalDirection(rand);
		boolean shouldStep = rand.nextBoolean();
		
		if(shouldStep) {
			for(int y = 0; y < 3; y++) {
				basePiece.addBlockPiece(CORROCK_BLOCK.get(), startPos.offset(horizontalStep).up(y));
			}
			
			int branchHeight = rand.nextInt(3) + 4;
			int sideYPos = branchHeight / 2;
			for(int y = 0; y < branchHeight; y++) {
				basePiece.addBlockPiece(CORROCK_BLOCK.get(), startPos.offset(horizontalStep, 2).up(2).up(y));
				
				if(y == branchHeight - 1 && rand.nextFloat() < 0.85F) {
					this.createCrownOrbit(null, basePiece, world, startPos.offset(horizontalStep, 2).up(2).up(y), rand);
				}
			}
			
			Direction sideStep = this.randomHorizontalDirection(rand);
			
			for(int offset = 0; offset < 2; offset++) {
				basePiece.addBlockPiece(CORROCK_BLOCK.get(), startPos.offset(horizontalStep, 2).offset(sideStep, offset).up(2).up(sideYPos));
			}
			
			int lastBranchHeight = rand.nextInt(3) + 4;
			for(int y = 0; y < lastBranchHeight; y++) {
				basePiece.addBlockPiece(CORROCK_BLOCK.get(), startPos.offset(horizontalStep, 2).offset(sideStep, 2).up(2).up(sideYPos).up(y));
			
				if(y == lastBranchHeight - 1 && rand.nextFloat() < 0.85F) {
					this.createCrownOrbit(chorusGrowths, basePiece, world, startPos.offset(horizontalStep, 2).offset(sideStep, 2).up(2).up(sideYPos).up(y), rand);
				}
			}
		} else {
			int firstBranchHeight = rand.nextInt(3) + 4;
			int secondBranchYPos = firstBranchHeight / 2;
			
			for(int y = 0; y < firstBranchHeight; y++) {
				basePiece.addBlockPiece(CORROCK_BLOCK.get(), startPos.offset(horizontalStep).up(y));
				
				if(y == firstBranchHeight - 1 && rand.nextBoolean()) {
					this.createCrownOrbit(null, basePiece, world, startPos.offset(horizontalStep).up(y), rand);
				}
			}
			
			Direction sideStep = rand.nextFloat() < 0.6F ? horizontalStep : rand.nextBoolean() ? horizontalStep.rotateY() : horizontalStep.rotateYCCW();
			
			for(int offset = 0; offset < 2; offset++) {
				basePiece.addBlockPiece(CORROCK_BLOCK.get(), startPos.up(secondBranchYPos).offset(horizontalStep).offset(sideStep, offset));
			}
			
			int lastBranchHeight = rand.nextInt(3) + 4;
			for(int y = 0; y < lastBranchHeight; y++) {
				basePiece.addBlockPiece(CORROCK_BLOCK.get(), startPos.up(secondBranchYPos).offset(horizontalStep).offset(sideStep, 2).up(y));
			
				if(y == lastBranchHeight - 1 && rand.nextFloat() < 0.85F) {
					this.createCrownOrbit(chorusGrowths, basePiece, world, startPos.up(secondBranchYPos).offset(horizontalStep).offset(sideStep, 2).up(y), rand);
				}
			}
		}
		
		return basePiece;
	}
	
	private void createBase(GenerationPiece piece, IWorld world, BlockPos pos, Random rand, int height) {
		for(int y = 0; y < height; y++) {
			piece.addBlockPiece(CORROCK_BLOCK.get(), pos.up(y));
			
			if(y == height - 1 && rand.nextFloat() < 0.85F) {
				piece.addBlockPiece(this.randomStandingCorrockCrown(rand), pos.up(height));
			}
		}
		/*
		 * Creates base cluster
		 */
		for(int x = pos.getX() - 1; x < pos.getX() + 2; x++) {
			for(int z = pos.getZ() - 1; z < pos.getZ() + 2; z++) {
				if(rand.nextFloat() < 0.4F) {
					BlockPos currentPos = new BlockPos(x, pos.getY(), z);
					if(this.tryToMakeAreaBelowPlacableOn(piece, world, currentPos)) {
						int randSideHeight = rand.nextInt(height - 1) + 1;
						for(int y = 0; y < randSideHeight; y++) {
							piece.addBlockPiece(CORROCK_BLOCK.get(), currentPos.up(y));
						}
					}
				}
			}
		}
	}
	
	private void createCrownOrbit(@Nullable List<ChorusPlantPart> chorusGrowths, GenerationPiece branch, IWorld world, BlockPos pos, Random rand) {
		boolean addedChorusGrowth = false;
		
		for(Direction horizontals : Direction.Plane.HORIZONTAL) {
			BlockPos placingPos = pos.offset(horizontals);
			if(rand.nextFloat() < 0.35F && world.isAirBlock(placingPos)) {
				branch.addBlockPiece(CORROCK_CROWN(true).get().with(BlockCorrockCrownWall.FACING, horizontals), placingPos);
			} else if(chorusGrowths != null && world.isAirBlock(placingPos)) {
				if(rand.nextFloat() < 0.3F && !addedChorusGrowth) {
					chorusGrowths.add(new ChorusPlantPart(branch, placingPos, horizontals));
					addedChorusGrowth = true;
				}
			}
		}
		if(rand.nextBoolean()) {
			branch.addBlockPiece(this.randomStandingCorrockCrown(rand), pos.up());
		}
	}
	
	private boolean tryToMakeAreaBelowPlacableOn(GenerationPiece piece, IWorld world, BlockPos pos) {
		boolean[] flags = { world.isAirBlock(pos.down()), world.isAirBlock(pos.down(2)), world.isAirBlock(pos.down(3))};
		if(flags[0]) {
			if(flags[2]) return false;
			piece.addBlockPiece(CORROCK_BLOCK.get(), pos.down());
			if(flags[1]) {
				piece.addBlockPiece(CORROCK_BLOCK.get(), pos.down(2));
			}
		}
		return true;
	}
	
	public List<ChorusPlantPart> getAllChorusPartsMatchingPiece(List<ChorusPlantPart> parts, GenerationPiece piece) {
		List<ChorusPlantPart> newParts = parts;
		newParts.removeIf(part -> part.piece != piece);
		return newParts;
	}
	
	private Direction randomHorizontalDirection(Random rand) {
		return Direction.byIndex(rand.nextInt(4) + 2);
	}
	
	private BlockState randomStandingCorrockCrown(Random rand) {
		return CORROCK_CROWN(false).get().with(BlockCorrockCrownStanding.ROTATION, rand.nextInt(16));
	}

	@Override
	public Consumer<Biome> processBiomeAddition() {
		return biome -> {
			if(IAddToBiomes.isInChorusBiome(biome)) {
				biome.addFeature(Decoration.SURFACE_STRUCTURES, EEFeatures.CORROCK_BRANCH.get().withConfiguration(new ProbabilityConfig(0.25F)).withPlacement(Placement.TOP_SOLID_HEIGHTMAP_NOISE_BIASED.configure(new TopSolidWithNoiseConfig(2, 5.0D, 0.0D, Heightmap.Type.WORLD_SURFACE_WG))));
			}
		};
	}
	
	class ChorusPlantPart {
		public final GenerationPiece piece;
		private final Direction facing;
		public final BlockPos pos;
		
		public ChorusPlantPart(GenerationPiece piece, BlockPos pos, Direction facing) {
			this.piece = piece;
			this.pos = pos;
			this.facing = facing;
		}
		
		public void placeGrowth(IWorld world, Random rand) {
			world.setBlockState(this.pos, Blocks.END_STONE.getDefaultState(), 2);
			world.setBlockState(this.pos.offset(facing), EEBlocks.ENDSTONE_COVER.get().getDefaultState().with(HorizontalBlock.HORIZONTAL_FACING, this.facing.getOpposite()), 2);
			ChorusFlowerBlock.generatePlant(world, this.pos.up(), rand, 8);
		}
	}

}