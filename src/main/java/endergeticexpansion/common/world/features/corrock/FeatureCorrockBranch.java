package endergeticexpansion.common.world.features.corrock;

import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import endergeticexpansion.api.generation.GenerationPiece;
import endergeticexpansion.api.generation.IAddToBiomes;
import endergeticexpansion.common.world.features.EEFeatures;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.ProbabilityConfig;
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
		
		if(world.getBlockState(pos.down()).getBlock() == Blocks.END_STONE) {
			GenerationPiece basePiece = new GenerationPiece((iworld, part) -> world.isAirBlock(part.pos));
			this.createBase(basePiece, world, pos, rand, baseHeight);
			if(basePiece.canPlace(world)) {
				int branchCount = rand.nextBoolean() ? 1 : rand.nextInt(3) + 1;
				GenerationPiece[] branchPieces = this.createBranches(world, pos, rand, branchCount, baseHeight);
				if(branchPieces[0].canPlace(world)) {
					basePiece.place(world);
					branchPieces[0].place(world);
					
					if(branchCount > 1) {
						if(branchPieces[1].canPlace(world)) {
							branchPieces[1].place(world);
						}
						if(branchCount > 2) {
							if(branchPieces[2].canPlace(world)) {
								branchPieces[2].place(world);
							}
						}
					}
					return true;
				}
			}
		}
		
		return false;
	}
	
	private GenerationPiece[] createBranches(IWorld world, BlockPos pos, Random rand, int count, int height) {
		GenerationPiece[] pieces = new GenerationPiece[count];
		for(int i = 0; i < pieces.length; i++) {
			pieces[i] = this.createBranch(world, pos, rand, height);
		}
		return pieces;
	}
	
	private GenerationPiece createBranch(IWorld world, BlockPos pos, Random rand, int height) {
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
			}
			
			Direction sideStep = this.randomHorizontalDirection(rand);
			
			for(int offset = 0; offset < 2; offset++) {
				basePiece.addBlockPiece(CORROCK_BLOCK.get(), startPos.offset(horizontalStep, 2).offset(sideStep, offset).up(2).up(sideYPos));
			}
			
			int lastBranchHeight = rand.nextInt(3) + 4;
			for(int y = 0; y < lastBranchHeight; y++) {
				basePiece.addBlockPiece(CORROCK_BLOCK.get(), startPos.offset(horizontalStep, 2).offset(sideStep, 2).up(2).up(sideYPos).up(y));
			}
		} else {
			int firstBranchHeight = rand.nextInt(3) + 4;
			int secondBranchYPos = firstBranchHeight / 2;
			
			for(int y = 0; y < firstBranchHeight; y++) {
				basePiece.addBlockPiece(CORROCK_BLOCK.get(), startPos.offset(horizontalStep).up(y));
			}
			
			Direction sideStep = rand.nextFloat() < 0.6F ? horizontalStep : rand.nextBoolean() ? horizontalStep.rotateY() : horizontalStep.rotateYCCW();
			
			for(int offset = 0; offset < 2; offset++) {
				basePiece.addBlockPiece(CORROCK_BLOCK.get(), startPos.up(secondBranchYPos).offset(horizontalStep).offset(sideStep, offset));
			}
			
			int lastBranchHeight = rand.nextInt(3) + 4;
			for(int y = 0; y < lastBranchHeight; y++) {
				basePiece.addBlockPiece(CORROCK_BLOCK.get(), startPos.up(secondBranchYPos).offset(horizontalStep).offset(sideStep, 2).up(y));
			}
		}
		
		return basePiece;
	}
	
	private void createBase(GenerationPiece piece, IWorld world, BlockPos pos, Random rand, int height) {
		for(int y = 0; y < height; y++) {
			piece.addBlockPiece(CORROCK_BLOCK.get(), pos.up(y));
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
	
	private Direction randomHorizontalDirection(Random rand) {
		return Direction.byIndex(rand.nextInt(4) + 2);
	}

	@Override
	public Consumer<Biome> processBiomeAddition() {
		return biome -> {
			if(IAddToBiomes.isInChorusBiome(biome)) {
				biome.addFeature(Decoration.SURFACE_STRUCTURES, EEFeatures.CORROCK_BRANCH.get().withConfiguration(new ProbabilityConfig(0.25F)).withPlacement(Placement.TOP_SOLID_HEIGHTMAP_NOISE_BIASED.configure(new TopSolidWithNoiseConfig(2, 5.0D, 0.0D, Heightmap.Type.WORLD_SURFACE_WG))));
			}
		};
	}

}