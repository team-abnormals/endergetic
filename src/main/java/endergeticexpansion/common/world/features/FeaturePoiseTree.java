package endergeticexpansion.common.world.features;

import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

import com.mojang.datafixers.Dynamic;

import endergeticexpansion.api.util.GenerationUtils;
import endergeticexpansion.common.blocks.poise.BlockGlowingPoiseLog;
import endergeticexpansion.core.registry.EEBlocks;
import endergeticexpansion.core.registry.other.EETags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

/**
 * @author - SmellyModder(Luke Tonon)
 */
public class FeaturePoiseTree extends Feature<NoFeatureConfig> {
	private final Supplier<BlockState> POISMOSS_EUMUS = () -> EEBlocks.POISMOSS_EUMUS.get().getDefaultState();
	private final Supplier<BlockState> POISE_LOG = () -> EEBlocks.POISE_LOG.get().getDefaultState();
	private final Supplier<BlockState> GLOWING_POISE_LOG = () -> EEBlocks.POISE_LOG_GLOWING.get().getDefaultState();
	
	public FeaturePoiseTree(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactoryIn) {
		super(configFactoryIn);
	}
	
	@Override
	public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config) {
		int treeHeight = rand.nextInt(19) + 13;
		int size = 0;
		//Random tree top size; small(45%), medium(40%), large(15%)
		float rng = rand.nextFloat();
		if(rng <= 0.45F) {
			size = 0;
		} else {
			if(rng >= 0.85F) {
				size = 2;
			} else {
				size = 1;	
			}
		}
		if(this.isValidGround(world, pos.down()) && this.isAreaOpen(world, pos)) {
			boolean[] isSutableForSizes = new boolean[] {
				GenerationUtils.isAreaReplacable(world, pos.north(3).west(3).up(treeHeight).getX(), pos.north(3).west(3).up(treeHeight).getY(), pos.north(3).west(3).up(treeHeight).getZ(), pos.south(3).east(3).up(treeHeight + 7).getX(), pos.south(3).east(3).up(treeHeight + 7).getY(), pos.south(3).east(3).up(treeHeight + 7).getZ()),
				GenerationUtils.isAreaReplacable(world, pos.north(4).west(4).up(treeHeight).getX(), pos.north(4).west(4).up(treeHeight).getY(), pos.north(4).west(4).up(treeHeight).getZ(), pos.south(4).east(4).up(treeHeight + 9).getX(), pos.south(4).east(4).up(treeHeight + 9).getY(), pos.south(4).east(4).up(treeHeight + 9).getZ()),
				GenerationUtils.isAreaReplacable(world, pos.north(6).west(6).up(treeHeight).getX(), pos.north(6).west(6).up(treeHeight).getY(), pos.north(6).west(6).up(treeHeight).getZ(), pos.south(6).east(6).up(treeHeight + 13).getX(), pos.south(6).east(6).up(treeHeight + 13).getY(), pos.south(6).east(6).up(treeHeight + 13).getZ())
			};
			if(size == 0) {
				if(GenerationUtils.isAreaReplacable(world, pos.north().west().getX(), pos.north().west().getY(), pos.north().west().getZ(), pos.south().east().up(treeHeight).getX(), pos.south().east().up(treeHeight).getY(), pos.south().east().up(treeHeight).getZ())) {
					if(isSutableForSizes[0]) {
						this.buildTreeBase(world, pos, rand);
						this.buildStem(world, pos, rand, treeHeight);
						this.buildTreeTop(world, pos, rand, treeHeight, size);
						
						if(rand.nextFloat() <= 0.80F) {
							this.tryToBuildBranch(world, pos, rand, treeHeight);
						}
						
						this.buildPoismossCircle(world, world, rand, pos);
						return true;
					}
					return false;
				} else {
					return false;
				}
			} else if(size == 1) {
				if(GenerationUtils.isAreaReplacable(world, pos.north().west().getX(), pos.north().west().getY(), pos.north().west().getZ(), pos.south().east().up(treeHeight).getX(), pos.south().east().up(treeHeight).getY(), pos.south().east().up(treeHeight).getZ())) {
					if(isSutableForSizes[1]) {
						this.buildTreeBase(world, pos, rand);
						this.buildStem(world, pos, rand, treeHeight);
						this.buildTreeTop(world, pos, rand, treeHeight, size);
						
						if(rand.nextFloat() <= 0.80F) {
							this.tryToBuildBranch(world, pos, rand, treeHeight);
						}
					
						this.buildPoismossCircle(world, world, rand, pos);
						return true;
					} else {
						if(isSutableForSizes[0]) {
							this.buildTreeBase(world, pos, rand);
							this.buildStem(world, pos, rand, treeHeight);
							this.buildTreeTop(world, pos, rand, treeHeight, 0);
							
							if(rand.nextFloat() <= 0.80F) {
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
			} else if(size == 2) {
				if(GenerationUtils.isAreaReplacable(world, pos.north().west().getX(), pos.north().west().getY(), pos.north().west().getZ(), pos.south().east().up(treeHeight).getX(), pos.south().east().up(treeHeight).getY(), pos.south().east().up(treeHeight).getZ())) {
					if(isSutableForSizes[2]) {
						this.buildTreeBase(world, pos, rand);
						this.buildStem(world, pos, rand, treeHeight);
						this.buildTreeTop(world, pos, rand, treeHeight, size);
						
						if(rand.nextFloat() <= 0.80F) {
							this.tryToBuildBranch(world, pos, rand, treeHeight);
						}
					
						this.buildPoismossCircle(world, world, rand, pos);
						return true;
					} else {
						if(isSutableForSizes[1]) {
							this.buildTreeBase(world, pos, rand);
							this.buildStem(world, pos, rand, treeHeight);
							this.buildTreeTop(world, pos, rand, treeHeight, 1);
							
							if(rand.nextFloat() <= 0.80F) {
								this.tryToBuildBranch(world, pos, rand, treeHeight);
							}
						
							this.buildPoismossCircle(world, world, rand, pos);
							return true;
						} else {
							if(isSutableForSizes[0]) {
								this.buildTreeBase(world, pos, rand);
								this.buildStem(world, pos, rand, treeHeight);
								this.buildTreeTop(world, pos, rand, treeHeight, 0);
								
								if(rand.nextFloat() <= 0.80F) {
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
		int[] sideRandValues = new int[] {
			rand.nextInt(8) + 2,
			rand.nextInt(8) + 2,
			rand.nextInt(8) + 2,
			rand.nextInt(8) + 2
		};
		for(int x = pos.getX() - 1; x < pos.getX() + 2; x++) {
			for(int z = pos.getZ() - 1; z < pos.getZ() + 2; z++) {
				this.setPoiseLog(world, new BlockPos(x, pos.getY(), z), rand, true, false);
			}
		}
		for(int xN = 1; xN < sideRandValues[0] + 1; xN++) {
			this.setPoiseLog(world, pos.north().up(xN), rand, true, false);
		}
		for(int xE = 1; xE < sideRandValues[1] + 1; xE++) {
			this.setPoiseLog(world, pos.east().up(xE), rand, true, false);
		}
		for(int xS = 1; xS < sideRandValues[2] + 1; xS++) {
			this.setPoiseLog(world, pos.south().up(xS), rand, true, false);
		}
		for(int xW = 1; xW < sideRandValues[3] + 1; xW++) {
			this.setPoiseLog(world, pos.west().up(xW), rand, true, false);
		}
		
		BlockPos downPos = pos.down(2);
		
		if(GenerationUtils.isAreaCompletelySolid(world, downPos.getX() - 1, downPos.getY(), downPos.getZ() - 1, downPos.getX() + 1, downPos.getY(), downPos.getZ() + 1)) {
			GenerationUtils.fillAreaWithBlockCube(world, downPos.getX() - 1, downPos.getY(), downPos.getZ() - 1, downPos.getX() + 1, downPos.getY() + 1, downPos.getZ() + 1, POISE_LOG.get());
		}
	}
	
	private void buildStem(IWorld world, BlockPos pos, Random rand, int height) {
		for(int y = 1; y < height; y++) {
			boolean doBubbles = y <= height - 2 ? false : true;
			this.setPoiseLog(world, pos.up(y), rand, false, doBubbles);
		}
	}
	
	private void buildTreeTop(IWorld world, BlockPos pos, Random rand, int arrivedPos, int size) {
		if(size == 0) {
			for(int x = pos.getX() - 1; x <= pos.getX() + 1; x++) {
				for(int z = pos.getZ() - 1; z <= pos.getZ() + 1; z++) {
					this.setPoiseLog(world, new BlockPos(x, pos.up(arrivedPos).getY(), z), rand, false, true);
				}
			}
			for(int x = pos.getX() - 1; x <= pos.getX() + 1; x++) {
				for(int z = pos.getZ() - 1; z <= pos.getZ() + 1; z++) {
					this.setBlockState(world, new BlockPos(x, pos.up(arrivedPos + 1).getY(), z), POISMOSS_EUMUS.get());
				}
			}
			this.setPoiseLog(world, pos.up(arrivedPos).north(2), rand, false, true);
			this.setPoiseLog(world, pos.up(arrivedPos).east(2), rand, false, true);
			this.setPoiseLog(world, pos.up(arrivedPos).south(2), rand, false, true);
			this.setPoiseLog(world, pos.up(arrivedPos).west(2), rand, false, true);
			
			this.setPoiseLog(world, pos.up(arrivedPos).north(3).up(), rand, false, true);
			this.setPoiseLog(world, pos.up(arrivedPos).east(3).up(), rand, false, true);
			this.setPoiseLog(world, pos.up(arrivedPos).south(3).up(), rand, false, true);
			this.setPoiseLog(world, pos.up(arrivedPos).west(3).up(), rand, false, true);
			
			this.setPoiseLog(world, pos.up(arrivedPos).north(2).up().east(), rand, false, true);
			this.setPoiseLog(world, pos.up(arrivedPos).north(2).up().west(), rand, false, true);
			this.setPoiseLog(world, pos.up(arrivedPos).east(2).up().north(), rand, false, true);
			this.setPoiseLog(world, pos.up(arrivedPos).east(2).up().south(), rand, false, true);
			this.setPoiseLog(world, pos.up(arrivedPos).south(2).up().east(), rand, false, true);
			this.setPoiseLog(world, pos.up(arrivedPos).south(2).up().west(), rand, false, true);
			this.setPoiseLog(world, pos.up(arrivedPos).west(2).up().north(), rand, false, true);
			this.setPoiseLog(world, pos.up(arrivedPos).west(2).up().south(), rand, false, true);
			
			this.setBlockState(world, pos.up(arrivedPos).north(2).up(), POISMOSS_EUMUS.get());
			this.setBlockState(world, pos.up(arrivedPos).east(2).up(), POISMOSS_EUMUS.get());
			this.setBlockState(world, pos.up(arrivedPos).south(2).up(), POISMOSS_EUMUS.get());
			this.setBlockState(world, pos.up(arrivedPos).west(2).up(), POISMOSS_EUMUS.get());
			
			this.placeInnerDomeFeatures(world, rand, pos.up(arrivedPos).west(2).up());
			
			int[] sideRandValues = new int[] {
				rand.nextInt(3) + 1,
				rand.nextInt(3) + 1,
				rand.nextInt(3) + 1,
				rand.nextInt(3) + 1
			};
			for(int yn = 1; yn <= sideRandValues[0]; yn++) {
				this.setPoiseLog(world, pos.up(arrivedPos).north(3).up().up(yn), rand, false, true);
				if(yn == 1 && rand.nextFloat() <= 0.25F) {
					if(rand.nextBoolean()) {
						this.setPoiseLog(world, pos.up(arrivedPos).north(3).east().up().up(yn), rand, false, true);
					} else {
						this.setPoiseLog(world, pos.up(arrivedPos).north(3).west().up().up(yn), rand, false, true);
					}
				}
			}
			for(int ye = 1; ye <= sideRandValues[1]; ye++) {
				this.setPoiseLog(world, pos.up(arrivedPos).east(3).up().up(ye), rand, false, true);
				if(ye == 1 && rand.nextFloat() <= 0.25F) {
					if(rand.nextBoolean()) {
						this.setPoiseLog(world, pos.up(arrivedPos).east(3).south().up().up(ye), rand, false, true);
					} else {
						this.setPoiseLog(world, pos.up(arrivedPos).east(3).north().up().up(ye), rand, false, true);
					}
				}
			}
			for(int ys = 1; ys <= sideRandValues[2]; ys++) {
				this.setPoiseLog(world, pos.up(arrivedPos).south(3).up().up(ys), rand, false, true);
				if(ys == 1 && rand.nextFloat() <= 0.25F) {
					if(rand.nextBoolean()) {
						this.setPoiseLog(world, pos.up(arrivedPos).south(3).west().up().up(ys), rand, false, true);
					} else {
						this.setPoiseLog(world, pos.up(arrivedPos).south(3).east().up().up(ys), rand, false, true);
					}
				}
			}
			for(int yw = 1; yw <= sideRandValues[3]; yw++) {
				this.setPoiseLog(world, pos.up(arrivedPos).west(3).up().up(yw), rand, false, true);
				if(yw == 1 && rand.nextFloat() <= 0.25F) {
					if(rand.nextBoolean()) {
						this.setPoiseLog(world, pos.up(arrivedPos).west(3).north().up().up(yw), rand, false, true);
					} else {
						this.setPoiseLog(world, pos.up(arrivedPos).west(3).south().up().up(yw), rand, false, true);
					}
				}
			}
			this.addTreeDomeTop(world, pos, rand, arrivedPos, 0);
		} else if(size == 1) {
			BlockPos origin = pos.up(arrivedPos);
			this.setPoiseLog(world, origin, rand, false, true);
			this.setPoiseLog(world, origin.north(), rand, false, true);
			this.setPoiseLog(world, origin.east(), rand, false, true);
			this.setPoiseLog(world, origin.south(), rand, false, true);
			this.setPoiseLog(world, origin.west(), rand, false, true);
			
			for(int x = origin.getX() - 2; x <= origin.getX() + 2; x++) {
				for(int z = origin.getZ() - 1; z <= origin.getZ() + 1; z++) {
					this.setPoiseLog(world, new BlockPos(x, origin.up().getY(), z), rand, false, true);
				}
			}
			for(int x = origin.getX() - 1; x <= origin.getX() + 1; x++) {
				for(int z = origin.getZ() - 2; z <= origin.getZ() + 2; z++) {
					this.setPoiseLog(world, new BlockPos(x, origin.up().getY(), z), rand, false, true);
				}
			}
			
			this.setPoiseLog(world, origin.up(2).north(3), rand, false, true);
			this.setPoiseLog(world, origin.up(2).north(3).west(), rand, false, true);
			this.setPoiseLog(world, origin.up(2).north(3).east(), rand, false, true);
			
			this.setPoiseLog(world, origin.up(2).east(3), rand, false, true);
			this.setPoiseLog(world, origin.up(2).east(3).north(), rand, false, true);
			this.setPoiseLog(world, origin.up(2).east(3).south(), rand, false, true);
			
			this.setPoiseLog(world, origin.up(2).south(3), rand, false, true);
			this.setPoiseLog(world, origin.up(2).south(3).west(), rand, false, true);
			this.setPoiseLog(world, origin.up(2).south(3).east(), rand, false, true);
			
			this.setPoiseLog(world, origin.up(2).west(3), rand, false, true);
			this.setPoiseLog(world, origin.up(2).west(3).north(), rand, false, true);
			this.setPoiseLog(world, origin.up(2).west(3).south(), rand, false, true);
			
			this.setPoiseLog(world, origin.up(2).north(2).west(2), rand, false, true);
			this.setPoiseLog(world, origin.up(2).north(2).east(2), rand, false, true);
			this.setPoiseLog(world, origin.up(2).south(2).west(2), rand, false, true);
			this.setPoiseLog(world, origin.up(2).south(2).east(2), rand, false, true);
			
			for(int x = origin.getX() - 2; x <= origin.getX() + 2; x++) {
				for(int z = origin.getZ() - 1; z <= origin.getZ() + 1; z++) {
					world.setBlockState(new BlockPos(x, origin.up(2).getY(), z), POISMOSS_EUMUS.get(), 2);
				}
			}
			
			for(int x = origin.getX() - 1; x <= origin.getX() + 1; x++) {
				for(int z = origin.getZ() - 2; z <= origin.getZ() + 2; z++) {
					world.setBlockState(new BlockPos(x, origin.up(2).getY(), z), POISMOSS_EUMUS.get(), 2);
				}
			}
			
			this.placeInnerDomeFeatures(world, rand, origin.up(2));
			
			int[] sideRandValues = new int[] {
				rand.nextInt(3) + 1,
				rand.nextInt(3) + 1,
				rand.nextInt(3) + 1,
				rand.nextInt(3) + 1
			};
			for(int yn = 1; yn <= sideRandValues[0]; yn++) {
				this.setPoiseLog(world, pos.up(arrivedPos).north(4).up(2).up(yn), rand, false, true);
				if(yn == 1 && rand.nextFloat() <= 0.25F) {
					if(rand.nextBoolean()) {
						this.setPoiseLog(world, pos.up(arrivedPos).north(4).east().up(2).up(yn), rand, false, true);
					} else {
						this.setPoiseLog(world, pos.up(arrivedPos).north(4).west().up(2).up(yn), rand, false, true);
					}
				}
			}
			for(int yn = 1; yn <= sideRandValues[1]; yn++) {
				this.setPoiseLog(world, pos.up(arrivedPos).east(4).up(2).up(yn), rand, false, true);
				if(yn == 1 && rand.nextFloat() <= 0.25F) {
					if(rand.nextBoolean()) {
						this.setPoiseLog(world, pos.up(arrivedPos).east(4).north().up(2).up(yn), rand, false, true);
					} else {
						this.setPoiseLog(world, pos.up(arrivedPos).east(4).south().up(2).up(yn), rand, false, true);
					}
				}
			}
			for(int yn = 1; yn <= sideRandValues[2]; yn++) {
				this.setPoiseLog(world, pos.up(arrivedPos).south(4).up(2).up(yn), rand, false, true);
				if(yn == 1 && rand.nextFloat() <= 0.25F) {
					if(rand.nextBoolean()) {
						this.setPoiseLog(world, pos.up(arrivedPos).south(4).east().up(2).up(yn), rand, false, true);
					} else {
						this.setPoiseLog(world, pos.up(arrivedPos).south(4).west().up(2).up(yn), rand, false, true);
					}
				}
			}
			for(int yn = 1; yn <= sideRandValues[3]; yn++) {
				this.setPoiseLog(world, pos.up(arrivedPos).west(4).up(2).up(yn), rand, false, true);
				if(yn == 1 && rand.nextFloat() <= 0.25F) {
					if(rand.nextBoolean()) {
						this.setPoiseLog(world, pos.up(arrivedPos).west(4).north().up(2).up(yn), rand, false, true);
					} else {
						this.setPoiseLog(world, pos.up(arrivedPos).west(4).south().up(2).up(yn), rand, false, true);
					}
				}
			}
			//Corner bits
			boolean[] doCornerFeature = new boolean[] {
				rand.nextInt(4) == 0 ? true : false,
				rand.nextInt(4) == 0 ? true : false,
				rand.nextInt(4) == 0 ? true : false,
				rand.nextInt(4) == 0 ? true : false,
			};
			if(doCornerFeature[0]) {
				BlockPos cornerOrigin = origin.up(3).north(3).west(2);
				if(rand.nextBoolean()) {
					this.setPoiseLog(world, cornerOrigin, rand, false, true);
					this.setPoiseLog(world, cornerOrigin.west(), rand, false, true);
					this.setPoiseLog(world, cornerOrigin.west().up(), rand, false, true);
					this.setPoiseLog(world, cornerOrigin.south().west(), rand, false, true);
				} else {
					this.setPoiseLog(world, cornerOrigin, rand, false, true);
					this.setPoiseLog(world, cornerOrigin.west().up(), rand, false, true);
					if(rand.nextBoolean()) this.setPoiseLog(world, cornerOrigin.west().up(2), rand, false, true);
				}
			}
			if(doCornerFeature[1]) {
				BlockPos cornerOrigin = origin.up(3).east(3).north(2);
				if(rand.nextBoolean()) {
					this.setPoiseLog(world, cornerOrigin, rand, false, true);
					this.setPoiseLog(world, cornerOrigin.north(), rand, false, true);
					this.setPoiseLog(world, cornerOrigin.north().up(), rand, false, true);
					this.setPoiseLog(world, cornerOrigin.west().north(), rand, false, true);
				} else {
					this.setPoiseLog(world, cornerOrigin, rand, false, true);
					this.setPoiseLog(world, cornerOrigin.north().up(), rand, false, true);
					if(rand.nextBoolean()) this.setPoiseLog(world, cornerOrigin.up(2).north(), rand, false, true);
				}
			}
			if(doCornerFeature[2]) {
				BlockPos cornerOrigin = origin.up(3).south(3).east(2);
				if(rand.nextBoolean()) {
					this.setPoiseLog(world, cornerOrigin, rand, false, true);
					this.setPoiseLog(world, cornerOrigin.east(), rand, false, true);
					this.setPoiseLog(world, cornerOrigin.east().up(), rand, false, true);
					this.setPoiseLog(world, cornerOrigin.north().east(), rand, false, true);
				} else {
					this.setPoiseLog(world, cornerOrigin, rand, false, true);
					this.setPoiseLog(world, cornerOrigin.east().up(), rand, false, true);
					if(rand.nextBoolean()) this.setPoiseLog(world, cornerOrigin.up(2).east(), rand, false, true);
				}
			}
			if(doCornerFeature[3]) {
				BlockPos cornerOrigin = origin.up(3).west(3).south(2);
				if(rand.nextBoolean()) {
					this.setPoiseLog(world, cornerOrigin, rand, false, true);
					this.setPoiseLog(world, cornerOrigin.south(), rand, false, true);
					this.setPoiseLog(world, cornerOrigin.south().up(), rand, false, true);
					this.setPoiseLog(world, cornerOrigin.east().south(), rand, false, true);
				} else {
					this.setPoiseLog(world, cornerOrigin, rand, false, true);
					this.setPoiseLog(world, cornerOrigin.up().south(), rand, false, true);
					if(rand.nextBoolean()) this.setPoiseLog(world, cornerOrigin.up(2).south(), rand, false, true);
				}
			}
			this.addTreeDomeTop(world, pos, rand, arrivedPos, 1);
		} else {
			BlockPos origin = pos.up(arrivedPos);
			this.setPoiseLog(world, origin, rand, false, true);
			if(rand.nextFloat() <= 0.75F) this.setPoiseLog(world, origin.north(), rand, false, true);
			if(rand.nextFloat() <= 0.75F) this.setPoiseLog(world, origin.east(), rand, false, true);
			if(rand.nextFloat() <= 0.75F) this.setPoiseLog(world, origin.south(), rand, false, true);
			if(rand.nextFloat() <= 0.75F) this.setPoiseLog(world, origin.west(), rand, false, true);
			
			GenerationUtils.fillWithRandomTwoBlocksCube(world, origin.up().north().west().getX(), origin.up().north().west().getY(), origin.up().north().west().getZ(), origin.up(2).east().south().getX(), origin.up(2).east().south().getY(), origin.up(2).east().south().getZ(), rand, POISE_LOG.get(), GLOWING_POISE_LOG.get(), 0.10F);
		
			for(int x = origin.getX() - 1; x <= origin.getX() + 1; x++) {
				for(int z = origin.getZ() - 3; z <= origin.getZ() + 3; z++) {
					this.setPoiseLog(world, new BlockPos(x, origin.up(3).getY(), z), rand, false, true);
				}
			}
			for(int x = origin.getX() - 3; x <= origin.getX() + 3; x++) {
				for(int z = origin.getZ() - 1; z <= origin.getZ() + 1; z++) {
					this.setPoiseLog(world, new BlockPos(x, origin.up(3).getY(), z), rand, false, true);
				}
			}
			this.setPoiseLog(world, origin.up(3).north(2).east(2), rand, false, true);
			this.setPoiseLog(world, origin.up(3).north(2).west(2), rand, false, true);
			this.setPoiseLog(world, origin.up(3).south(2).east(2), rand, false, true);
			this.setPoiseLog(world, origin.up(3).south(2).west(2), rand, false, true);
			
			for(int x = origin.getX() - 1; x <= origin.getX() + 1; x++) {
				for(int z = origin.getZ() - 5; z <= origin.getZ() + 5; z++) {
					this.setPoiseLog(world, new BlockPos(x, origin.up(4).getY(), z), rand, false, true);
				}
			}
			for(int x = origin.getX() - 5; x <= origin.getX() + 5; x++) {
				for(int z = origin.getZ() - 1; z <= origin.getZ() + 1; z++) {
					this.setPoiseLog(world, new BlockPos(x, origin.up(4).getY(), z), rand, false, true);
				}
			}
			this.setPoiseLog(world, origin.up(4).north(3).east(2), rand, false, true);
			this.setPoiseLog(world, origin.up(4).north(4).east(2), rand, false, true);
			this.setPoiseLog(world, origin.up(4).north(3).east(3), rand, false, true);
			this.setPoiseLog(world, origin.up(4).north(2).east(3), rand, false, true);
			this.setPoiseLog(world, origin.up(4).north(2).east(4), rand, false, true);
			
			world.setBlockState(origin.up(4).north(2).east(2), POISMOSS_EUMUS.get(), 2);
			
			this.setPoiseLog(world, origin.up(4).east(3).south(2), rand, false, true);
			this.setPoiseLog(world, origin.up(4).east(4).south(2), rand, false, true);
			this.setPoiseLog(world, origin.up(4).east(3).south(3), rand, false, true);
			this.setPoiseLog(world, origin.up(4).east(2).south(3), rand, false, true);
			this.setPoiseLog(world, origin.up(4).east(2).south(4), rand, false, true);
			
			world.setBlockState(origin.up(4).east(2).south(2), POISMOSS_EUMUS.get(), 2);
			
			this.setPoiseLog(world, origin.up(4).south(3).west(2), rand, false, true);
			this.setPoiseLog(world, origin.up(4).south(4).west(2), rand, false, true);
			this.setPoiseLog(world, origin.up(4).south(3).west(3), rand, false, true);
			this.setPoiseLog(world, origin.up(4).south(2).west(3), rand, false, true);
			this.setPoiseLog(world, origin.up(4).south(2).west(4), rand, false, true);
			
			world.setBlockState(origin.up(4).south(2).west(2), POISMOSS_EUMUS.get(), 2);
			
			this.setPoiseLog(world, origin.up(4).west(3).north(2), rand, false, true);
			this.setPoiseLog(world, origin.up(4).west(4).north(2), rand, false, true);
			this.setPoiseLog(world, origin.up(4).west(3).north(3), rand, false, true);
			this.setPoiseLog(world, origin.up(4).west(2).north(3), rand, false, true);
			this.setPoiseLog(world, origin.up(4).west(2).north(4), rand, false, true);
			
			world.setBlockState(origin.up(4).west(2).north(2), POISMOSS_EUMUS.get(), 2);
			
			for(int x = origin.getX() - 1; x <= origin.getX() + 1; x++) {
				for(int z = origin.getZ() - 3; z <= origin.getZ() + 3; z++) {
					world.setBlockState(new BlockPos(x, origin.up(4).getY(), z), POISMOSS_EUMUS.get(), 2);
				}
			}
			
			for(int x = origin.getX() - 3; x <= origin.getX() + 3; x++) {
				for(int z = origin.getZ() - 1; z <= origin.getZ() + 1; z++) {
					world.setBlockState(new BlockPos(x, origin.up(4).getY(), z), POISMOSS_EUMUS.get(), 2);
				}
			}
			
			this.placeInnerDomeFeatures(world, rand, origin.up(4));
			
			this.setPoiseLog(world, origin.up(5).north(5), rand, false, true);
			this.setPoiseLog(world, origin.up(5).north(5).east(), rand, false, true);
			this.setPoiseLog(world, origin.up(5).north(5).east(2), rand, false, true);
			this.setPoiseLog(world, origin.up(5).north(5).west(), rand, false, true);
			this.setPoiseLog(world, origin.up(5).north(5).west(2), rand, false, true);
			
			this.setPoiseLog(world, origin.up(5).east(5), rand, false, true);
			this.setPoiseLog(world, origin.up(5).east(5).north(), rand, false, true);
			this.setPoiseLog(world, origin.up(5).east(5).north(2), rand, false, true);
			this.setPoiseLog(world, origin.up(5).east(5).south(), rand, false, true);
			this.setPoiseLog(world, origin.up(5).east(5).south(2), rand, false, true);
			
			this.setPoiseLog(world, origin.up(5).south(5), rand, false, true);
			this.setPoiseLog(world, origin.up(5).south(5).west(), rand, false, true);
			this.setPoiseLog(world, origin.up(5).south(5).west(2), rand, false, true);
			this.setPoiseLog(world, origin.up(5).south(5).east(), rand, false, true);
			this.setPoiseLog(world, origin.up(5).south(5).east(2), rand, false, true);
			
			this.setPoiseLog(world, origin.up(5).west(5), rand, false, true);
			this.setPoiseLog(world, origin.up(5).west(5).south(), rand, false, true);
			this.setPoiseLog(world, origin.up(5).west(5).south(2), rand, false, true);
			this.setPoiseLog(world, origin.up(5).west(5).north(), rand, false, true);
			this.setPoiseLog(world, origin.up(5).west(5).north(2), rand, false, true);
			
			this.setPoiseLog(world, origin.up(5).north(4).west(3), rand, false, true);
			this.setPoiseLog(world, origin.up(5).north(3).west(4), rand, false, true);
			this.setPoiseLog(world, origin.up(5).north(4).east(3), rand, false, true);
			this.setPoiseLog(world, origin.up(5).north(3).east(4), rand, false, true);
			this.setPoiseLog(world, origin.up(5).south(4).west(3), rand, false, true);
			this.setPoiseLog(world, origin.up(5).south(3).west(4), rand, false, true);
			this.setPoiseLog(world, origin.up(5).south(4).east(3), rand, false, true);
			this.setPoiseLog(world, origin.up(5).south(3).east(4), rand, false, true);
			
			int[] sideRandValues = new int[] {
				rand.nextInt(3) + 1,
				rand.nextInt(3) + 1,
				rand.nextInt(3) + 1,
				rand.nextInt(3) + 1
			};
			for(int yn = 1; yn <= sideRandValues[0]; yn++) {
				this.setPoiseLog(world, pos.up(arrivedPos).north(6).up(5).up(yn), rand, false, true);
				if(yn == 1 && rand.nextFloat() <= 0.25F) {
					if(rand.nextBoolean()) {
						this.setPoiseLog(world, pos.up(arrivedPos).north(6).west().up(5).up(yn), rand, false, true);
					} else {
						this.setPoiseLog(world, pos.up(arrivedPos).north(6).east().up(5).up(yn), rand, false, true);
					}
				}
			}
			for(int yn = 1; yn <= sideRandValues[1]; yn++) {
				this.setPoiseLog(world, pos.up(arrivedPos).east(6).up(5).up(yn), rand, false, true);
				if(yn == 1 && rand.nextFloat() <= 0.25F) {
					if(rand.nextBoolean()) {
						this.setPoiseLog(world, pos.up(arrivedPos).east(6).north().up(5).up(yn), rand, false, true);
					} else {
						this.setPoiseLog(world, pos.up(arrivedPos).east(6).south().up(5).up(yn), rand, false, true);
					}
				}
			}
			for(int yn = 1; yn <= sideRandValues[2]; yn++) {
				this.setPoiseLog(world, pos.up(arrivedPos).south(6).up(5).up(yn), rand, false, true);
				if(yn == 1 && rand.nextFloat() <= 0.25F) {
					if(rand.nextBoolean()) {
						this.setPoiseLog(world, pos.up(arrivedPos).south(6).west().up(5).up(yn), rand, false, true);
					} else {
						this.setPoiseLog(world, pos.up(arrivedPos).south(6).east().up(5).up(yn), rand, false, true);
					}
				}
			}
			for(int yn = 1; yn <= sideRandValues[3]; yn++) {
				this.setPoiseLog(world, pos.up(arrivedPos).west(6).up(5).up(yn), rand, false, true);
				if(yn == 1 && rand.nextFloat() <= 0.25F) {
					if(rand.nextBoolean()) {
						this.setPoiseLog(world, pos.up(arrivedPos).west(6).south().up(5).up(yn), rand, false, true);
					} else {
						this.setPoiseLog(world, pos.up(arrivedPos).west(6).north().up(5).up(yn), rand, false, true);
					}
				}
			}
			
			int[] cornerRandValues = new int[] {
				rand.nextFloat() <= 0.75 ? rand.nextInt(3) + 1 : 0,
				rand.nextFloat() <= 0.75 ? rand.nextInt(3) + 1 : 0,
				rand.nextFloat() <= 0.75 ? rand.nextInt(3) + 1 : 0,
				rand.nextFloat() <= 0.75 ? rand.nextInt(3) + 1 : 0
			};
			for(int yn = 1; yn <= cornerRandValues[0]; yn++) {
				this.setPoiseLog(world, pos.up(arrivedPos).north(4).east(4).up(5).up(yn), rand, false, true);
			}
			for(int yn = 1; yn <= cornerRandValues[1]; yn++) {
				this.setPoiseLog(world, pos.up(arrivedPos).east(4).south(4).up(5).up(yn), rand, false, true);
			}
			for(int yn = 1; yn <= cornerRandValues[2]; yn++) {
				this.setPoiseLog(world, pos.up(arrivedPos).south(4).west(4).up(5).up(yn), rand, false, true);
			}
			for(int yn = 1; yn <= cornerRandValues[3]; yn++) {
				this.setPoiseLog(world, pos.up(arrivedPos).west(4).north(4).up(5).up(yn), rand, false, true);
			}
			this.addTreeDomeTop(world, pos, rand, arrivedPos, 2);
		}
	}
	
	private void addTreeDomeTop(IWorld world, BlockPos pos, Random rand, int arrivedPos, int size) {
		BlockPos origin = pos.up(arrivedPos);
		if(size == 0) {
			//North
			GenerationUtils.fillAreaWithBlockCube(world, origin.north(3).up(2).west().getX(), origin.north(3).up(2).west().getY(), origin.north(3).up(2).east().getZ(), origin.north(3).up(2).east().getX(), origin.north(3).up(6).east().getY(), origin.north(3).up(2).east().getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.north(3).up(3).west(2).getX(), origin.north(3).up(3).west(2).getY(), origin.north(3).up(3).west(2).getZ(), origin.north(3).up(3).west(2).getX(), origin.north(3).up(5).west(2).getY(), origin.north(3).up(3).west(2).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.north(3).up(3).east(2).getX(), origin.north(3).up(3).east(2).getY(), origin.north(3).up(3).east(2).getZ(), origin.north(3).up(3).east(2).getX(), origin.north(3).up(5).east(2).getY(), origin.north(3).up(3).east(2).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		
			//East
			GenerationUtils.fillAreaWithBlockCube(world, origin.east(3).up(2).north().getX(), origin.east(3).up(2).north().getY(), origin.east(3).up(2).north().getZ(), origin.east(3).up(2).south().getX(), origin.east(3).up(6).south().getY(), origin.east(3).up(2).south().getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.east(3).up(3).north(2).getX(), origin.east(3).up(3).north(2).getY(), origin.east(3).up(3).north(2).getZ(), origin.east(3).up(3).north(2).getX(), origin.east(3).up(5).north(2).getY(), origin.east(3).up(3).north(2).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.east(3).up(3).south(2).getX(), origin.east(3).up(3).south(2).getY(), origin.east(3).up(3).south(2).getZ(), origin.east(3).up(3).south(2).getX(), origin.east(3).up(5).south(2).getY(), origin.east(3).up(3).south(2).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());

			//South
			GenerationUtils.fillAreaWithBlockCube(world, origin.south(3).up(2).west().getX(), origin.south(3).up(2).west().getY(), origin.south(3).up(2).west().getZ(), origin.south(3).up(2).east().getX(), origin.south(3).up(6).east().getY(), origin.south(3).up(2).east().getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.south(3).up(3).west(2).getX(), origin.south(3).up(3).west(2).getY(), origin.south(3).up(3).west(2).getZ(), origin.south(3).up(3).west(2).getX(), origin.south(3).up(5).west(2).getY(), origin.south(3).up(3).west(2).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.south(3).up(3).east(2).getX(), origin.south(3).up(3).east(2).getY(), origin.south(3).up(3).east(2).getZ(), origin.south(3).up(3).east(2).getX(), origin.south(3).up(5).east(2).getY(), origin.south(3).up(3).east(2).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
			
			//West
			GenerationUtils.fillAreaWithBlockCube(world, origin.west(3).up(2).north().getX(), origin.west(3).up(2).north().getY(), origin.west(3).up(2).north().getZ(), origin.west(3).up(2).south().getX(), origin.west(3).up(6).south().getY(), origin.west(3).up(2).south().getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.west(3).up(3).north(2).getX(), origin.west(3).up(3).north(2).getY(), origin.west(3).up(3).north(2).getZ(), origin.west(3).up(3).north(2).getX(), origin.west(3).up(5).north(2).getY(), origin.west(3).up(3).north(2).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.west(3).up(3).south(2).getX(), origin.west(3).up(3).south(2).getY(), origin.west(3).up(3).south(2).getZ(), origin.west(3).up(3).south(2).getX(), origin.west(3).up(5).south(2).getY(), origin.west(3).up(3).south(2).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
			
			//Corners
			this.setPoiseCluster(world, origin.north(2).up(2).west(2));
			this.setPoiseCluster(world, origin.north(2).up(2).east(2));
			this.setPoiseCluster(world, origin.north(2).up(6).west(2));
			this.setPoiseCluster(world, origin.north(2).up(6).east(2));
			
			this.setPoiseCluster(world, origin.east(2).up(2).north(2));
			this.setPoiseCluster(world, origin.east(2).up(2).south(2));
			this.setPoiseCluster(world, origin.east(2).up(6).north(2));
			this.setPoiseCluster(world, origin.east(2).up(6).south(2));
			
			this.setPoiseCluster(world, origin.south(2).up(2).east(2));
			this.setPoiseCluster(world, origin.south(2).up(2).west(2));
			this.setPoiseCluster(world, origin.south(2).up(6).east(2));
			this.setPoiseCluster(world, origin.south(2).up(6).west(2));
			
			this.setPoiseCluster(world, origin.west(2).up(2).south(2));
			this.setPoiseCluster(world, origin.west(2).up(2).north(2));
			this.setPoiseCluster(world, origin.west(2).up(6).south(2));
			this.setPoiseCluster(world, origin.west(2).up(6).north(2));
			
			//Top
			GenerationUtils.fillAreaWithBlockCube(world, origin.north().west(2).up(7).getX(), origin.north().west(2).up(7).getY(), origin.north().west(2).up(7).getZ(), origin.south().east(2).up(7).getX(), origin.south().east(2).up(7).getY(), origin.south().east(2).up(7).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
			this.setPoiseCluster(world, origin.north(2).west().up(7));
			this.setPoiseCluster(world, origin.north(2).up(7));
			this.setPoiseCluster(world, origin.north(2).east().up(7));
			this.setPoiseCluster(world, origin.south(2).west().up(7));
			this.setPoiseCluster(world, origin.south(2).up(7));
			this.setPoiseCluster(world, origin.south(2).east().up(7));
		} else if(size == 1) {
			//North
			GenerationUtils.fillAreaWithBlockCube(world, origin.north(4).up(3).west().getX(), origin.north(4).up(3).west().getY(), origin.north(4).up(3).east().getZ(), origin.north(4).up(3).east().getX(), origin.north(4).up(7).east().getY(), origin.north(4).up(3).east().getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.north(4).up(4).west(2).getX(), origin.north(4).up(4).west(2).getY(), origin.north(4).up(4).west(2).getZ(), origin.north(4).up(4).west(2).getX(), origin.north(4).up(6).west(2).getY(), origin.north(4).up(4).west(2).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.north(4).up(4).east(2).getX(), origin.north(4).up(4).east(2).getY(), origin.north(4).up(4).east(2).getZ(), origin.north(4).up(4).east(2).getX(), origin.north(4).up(6).east(2).getY(), origin.north(4).up(4).east(2).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		
			//East
			GenerationUtils.fillAreaWithBlockCube(world, origin.east(4).up(3).north().getX(), origin.east(4).up(3).north().getY(), origin.east(4).up(3).north().getZ(), origin.east(4).up(3).south().getX(), origin.east(4).up(7).south().getY(), origin.east(4).up(3).south().getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.east(4).up(4).north(2).getX(), origin.east(4).up(4).north(2).getY(), origin.east(4).up(4).north(2).getZ(), origin.east(4).up(4).north(2).getX(), origin.east(4).up(6).north(2).getY(), origin.east(4).up(4).north(2).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.east(4).up(4).south(2).getX(), origin.east(4).up(4).south(2).getY(), origin.east(4).up(4).south(2).getZ(), origin.east(4).up(4).south(2).getX(), origin.east(4).up(6).south(2).getY(), origin.east(4).up(4).south(2).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());

			//South
			GenerationUtils.fillAreaWithBlockCube(world, origin.south(4).up(3).west().getX(), origin.south(4).up(3).west().getY(), origin.south(4).up(3).west().getZ(), origin.south(4).up(3).east().getX(), origin.south(4).up(7).east().getY(), origin.south(4).up(3).east().getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.south(4).up(4).west(2).getX(), origin.south(4).up(4).west(2).getY(), origin.south(4).up(4).west(2).getZ(), origin.south(4).up(4).west(2).getX(), origin.south(4).up(6).west(2).getY(), origin.south(4).up(4).west(2).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.south(4).up(4).east(2).getX(), origin.south(4).up(4).east(2).getY(), origin.south(4).up(4).east(2).getZ(), origin.south(4).up(4).east(2).getX(), origin.south(4).up(6).east(2).getY(), origin.south(4).up(4).east(2).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
			
			//West
			GenerationUtils.fillAreaWithBlockCube(world, origin.west(4).up(3).north().getX(), origin.west(4).up(3).north().getY(), origin.west(4).up(3).north().getZ(), origin.west(4).up(3).south().getX(), origin.west(4).up(7).south().getY(), origin.west(4).up(3).south().getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.west(4).up(4).north(2).getX(), origin.west(4).up(4).north(2).getY(), origin.west(4).up(4).north(2).getZ(), origin.west(4).up(4).north(2).getX(), origin.west(4).up(6).north(2).getY(), origin.west(4).up(4).north(2).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.west(4).up(4).south(2).getX(), origin.west(4).up(4).south(2).getY(), origin.west(4).up(4).south(2).getZ(), origin.west(4).up(4).south(2).getX(), origin.west(4).up(6).south(2).getY(), origin.west(4).up(4).south(2).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		
			//Corners
			this.setPoiseCluster(world, origin.up(3).north(3).west(2));
			this.setPoiseCluster(world, origin.up(3).north(3).west(3));
			this.setPoiseCluster(world, origin.up(3).north(2).west(3));
			this.setPoiseCluster(world, origin.up(4).north(3).west(3));
			this.setPoiseCluster(world, origin.up(5).north(3).west(3));
			this.setPoiseCluster(world, origin.up(6).north(3).west(3));
			this.setPoiseCluster(world, origin.up(7).north(3).west(2));
			this.setPoiseCluster(world, origin.up(7).north(3).west(3));
			this.setPoiseCluster(world, origin.up(7).north(2).west(3));
			
			this.setPoiseCluster(world, origin.up(3).east(3).north(2));
			this.setPoiseCluster(world, origin.up(3).east(3).north(3));
			this.setPoiseCluster(world, origin.up(3).east(2).north(3));
			this.setPoiseCluster(world, origin.up(4).east(3).north(3));
			this.setPoiseCluster(world, origin.up(5).east(3).north(3));
			this.setPoiseCluster(world, origin.up(6).east(3).north(3));
			this.setPoiseCluster(world, origin.up(7).east(3).north(2));
			this.setPoiseCluster(world, origin.up(7).east(3).north(3));
			this.setPoiseCluster(world, origin.up(7).east(2).north(3));
			
			this.setPoiseCluster(world, origin.up(3).south(3).east(2));
			this.setPoiseCluster(world, origin.up(3).south(3).east(3));
			this.setPoiseCluster(world, origin.up(3).south(2).east(3));
			this.setPoiseCluster(world, origin.up(4).south(3).east(3));
			this.setPoiseCluster(world, origin.up(5).south(3).east(3));
			this.setPoiseCluster(world, origin.up(6).south(3).east(3));
			this.setPoiseCluster(world, origin.up(7).south(3).east(2));
			this.setPoiseCluster(world, origin.up(7).south(3).east(3));
			this.setPoiseCluster(world, origin.up(7).south(2).east(3));
			
			this.setPoiseCluster(world, origin.up(3).west(3).south(2));
			this.setPoiseCluster(world, origin.up(3).west(3).south(3));
			this.setPoiseCluster(world, origin.up(3).west(2).south(3));
			this.setPoiseCluster(world, origin.up(4).west(3).south(3));
			this.setPoiseCluster(world, origin.up(5).west(3).south(3));
			this.setPoiseCluster(world, origin.up(6).west(3).south(3));
			this.setPoiseCluster(world, origin.up(7).west(3).south(2));
			this.setPoiseCluster(world, origin.up(7).west(3).south(3));
			this.setPoiseCluster(world, origin.up(7).west(2).south(3));
			
			//Top
			this.setPoiseCluster(world, origin.up(8).west(2).south(2));
			this.setPoiseCluster(world, origin.up(8).south(2).east(2));
			this.setPoiseCluster(world, origin.up(8).east(2).north(2));
			this.setPoiseCluster(world, origin.up(8).north(2).west(2));
			
			this.setPoiseCluster(world, origin.up(8).north(3).west());
			this.setPoiseCluster(world, origin.up(8).north(3));
			this.setPoiseCluster(world, origin.up(8).north(3).east());
			
			this.setPoiseCluster(world, origin.up(8).east(3).north());
			this.setPoiseCluster(world, origin.up(8).east(3));
			this.setPoiseCluster(world, origin.up(8).east(3).south());
			
			this.setPoiseCluster(world, origin.up(8).south(3).east());
			this.setPoiseCluster(world, origin.up(8).south(3));
			this.setPoiseCluster(world, origin.up(8).south(3).west());
			
			this.setPoiseCluster(world, origin.up(8).west(3).south());
			this.setPoiseCluster(world, origin.up(8).west(3));
			this.setPoiseCluster(world, origin.up(8).west(3).north());
			
			GenerationUtils.fillAreaWithBlockCube(world, origin.north().west(2).up(9).getX(), origin.north().west(2).up(9).getY(), origin.north().west(2).up(9).getZ(), origin.south().east(2).up(9).getX(), origin.south().east(2).up(9).getY(), origin.south().east(2).up(9).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
			this.setPoiseCluster(world, origin.north(2).west().up(9));
			this.setPoiseCluster(world, origin.north(2).up(9));
			this.setPoiseCluster(world, origin.north(2).east().up(9));
			this.setPoiseCluster(world, origin.south(2).west().up(9));
			this.setPoiseCluster(world, origin.south(2).up(9));
			this.setPoiseCluster(world, origin.south(2).east().up(9));
		} else {
			//North
			GenerationUtils.fillAreaWithBlockCube(world, origin.north(6).up(6).west().getX(), origin.north(6).up(6).west().getY(), origin.north(6).up(6).west().getZ(), origin.north(6).up(8).east().getX(), origin.north(6).up(8).west().getY(), origin.north(6).up(8).west().getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.north(5).up(6).west(3).getX(), origin.north(5).up(6).west(3).getY(), origin.north(5).up(6).west(3).getZ(), origin.north(5).up(8).west(2).getX(), origin.north(5).up(8).west(2).getY(), origin.north(5).up(8).west(2).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.north(5).up(6).east(2).getX(), origin.north(5).up(6).east(2).getY(), origin.north(5).up(6).east(2).getZ(), origin.north(5).up(8).east(3).getX(), origin.north(5).up(8).east(3).getY(), origin.north(5).up(8).east(3).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.north(5).up(9).west(2).getX(), origin.north(5).up(9).west(2).getY(), origin.north(5).up(9).west(2).getZ(), origin.north(5).up(9).east(2).getX(), origin.north(5).up(9).east(2).getY(), origin.north(5).up(9).east(2).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.north(5).up(10).west().getX(), origin.north(5).up(10).west().getY(), origin.north(5).up(10).west().getZ(), origin.north(5).up(10).east().getX(), origin.north(5).up(10).east().getY(), origin.north(5).up(10).east().getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
			
			//East
			GenerationUtils.fillAreaWithBlockCube(world, origin.east(6).up(6).north().getX(), origin.east(6).up(6).north().getY(), origin.east(6).up(6).north().getZ(), origin.east(6).up(8).south().getX(), origin.east(6).up(8).south().getY(), origin.east(6).up(8).south().getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.east(5).up(6).north(3).getX(), origin.east(5).up(6).north(3).getY(), origin.east(5).up(6).north(3).getZ(), origin.east(5).up(8).north(2).getX(), origin.east(5).up(8).north(2).getY(), origin.east(5).up(8).north(2).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.east(5).up(6).south(2).getX(), origin.east(5).up(6).south(2).getY(), origin.east(5).up(6).south(2).getZ(), origin.east(5).up(8).south(3).getX(), origin.east(5).up(8).south(3).getY(), origin.east(5).up(8).south(3).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.east(5).up(9).south(2).getX(), origin.east(5).up(9).north(2).getY(), origin.east(5).up(9).north(2).getZ(), origin.east(5).up(9).south(2).getX(), origin.east(5).up(9).south(2).getY(), origin.east(5).up(9).south(2).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.east(5).up(10).north().getX(), origin.east(5).up(10).north().getY(), origin.east(5).up(10).north().getZ(), origin.east(5).up(10).south().getX(), origin.east(5).up(10).south().getY(), origin.east(5).up(10).south().getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
			
			//South
			GenerationUtils.fillAreaWithBlockCube(world, origin.south(6).up(6).west().getX(), origin.south(6).up(6).west().getY(), origin.south(6).up(6).west().getZ(), origin.south(6).up(8).east().getX(), origin.south(6).up(8).east().getY(), origin.south(6).up(8).east().getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.south(5).up(6).west(3).getX(), origin.south(5).up(6).west(3).getY(), origin.south(5).up(6).west(3).getZ(), origin.south(5).up(8).west(2).getX(), origin.south(5).up(8).north(2).getY(), origin.south(5).up(8).west(2).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.south(5).up(6).east(2).getX(), origin.south(5).up(6).east(2).getY(), origin.south(5).up(6).east(2).getZ(), origin.south(5).up(8).east(3).getX(), origin.south(5).up(8).east(3).getY(), origin.south(5).up(8).east(3).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.south(5).up(9).west(2).getX(), origin.south(5).up(9).west(2).getY(), origin.south(5).up(9).west(2).getZ(), origin.south(5).up(9).east(2).getX(), origin.south(5).up(9).east(2).getY(), origin.south(5).up(9).east(2).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.south(5).up(10).west().getX(), origin.south(5).up(10).west().getY(), origin.south(5).up(10).west().getZ(), origin.south(5).up(10).east().getX(), origin.south(5).up(10).east().getY(), origin.south(5).up(10).east().getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
			
			//West
			GenerationUtils.fillAreaWithBlockCube(world, origin.west(6).up(6).north().getX(), origin.west(6).up(6).north().getY(), origin.west(6).up(6).north().getZ(), origin.west(6).up(8).south().getX(), origin.west(6).up(8).south().getY(), origin.west(6).up(8).south().getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.west(5).up(6).north(3).getX(), origin.west(5).up(6).north(3).getY(), origin.west(5).up(6).north(3).getZ(), origin.west(5).up(8).north(2).getX(), origin.west(5).up(8).north(2).getY(), origin.west(5).up(8).north(2).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.west(5).up(6).south(2).getX(), origin.west(5).up(6).south(2).getY(), origin.west(5).up(6).south(2).getZ(), origin.west(5).up(8).south(3).getX(), origin.west(5).up(8).south(3).getY(), origin.west(5).up(8).south(3).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.west(5).up(9).north(2).getX(), origin.west(5).up(9).north(2).getY(), origin.west(5).up(9).north(2).getZ(), origin.west(5).up(9).south(2).getX(), origin.west(5).up(9).south(2).getY(), origin.west(5).up(9).south(2).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.west(5).up(10).north().getX(), origin.west(5).up(10).north().getY(), origin.west(5).up(10).north().getZ(), origin.west(5).up(10).south().getX(), origin.west(5).up(10).south().getY(), origin.west(5).up(10).south().getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
			
			
			//Corners
			this.setPoiseCluster(world, origin.up(6).north(4).west(4));
			this.setPoiseCluster(world, origin.up(7).north(4).west(4));
			this.setPoiseCluster(world, origin.up(8).north(4).west(4));
			this.setPoiseCluster(world, origin.up(9).north(4).west(4));
			this.setPoiseCluster(world, origin.up(9).north(4).west(3));
			this.setPoiseCluster(world, origin.up(10).north(4).west(3));
			this.setPoiseCluster(world, origin.up(10).north(4).west(2));
			this.setPoiseCluster(world, origin.up(9).north(3).west(4));
			this.setPoiseCluster(world, origin.up(10).north(3).west(4));
			this.setPoiseCluster(world, origin.up(10).north(2).west(4));
			
			this.setPoiseCluster(world, origin.up(6).east(4).north(4));
			this.setPoiseCluster(world, origin.up(7).east(4).north(4));
			this.setPoiseCluster(world, origin.up(8).east(4).north(4));
			this.setPoiseCluster(world, origin.up(9).east(4).north(4));
			this.setPoiseCluster(world, origin.up(9).east(4).north(3));
			this.setPoiseCluster(world, origin.up(10).east(4).north(3));
			this.setPoiseCluster(world, origin.up(10).east(4).north(2));
			this.setPoiseCluster(world, origin.up(9).east(3).north(4));
			this.setPoiseCluster(world, origin.up(10).east(3).north(4));
			this.setPoiseCluster(world, origin.up(10).east(2).north(4));
			
			this.setPoiseCluster(world, origin.up(6).south(4).east(4));
			this.setPoiseCluster(world, origin.up(7).south(4).east(4));
			this.setPoiseCluster(world, origin.up(8).south(4).east(4));
			this.setPoiseCluster(world, origin.up(9).south(4).east(4));
			this.setPoiseCluster(world, origin.up(9).south(4).east(3));
			this.setPoiseCluster(world, origin.up(10).south(4).east(3));
			this.setPoiseCluster(world, origin.up(10).south(4).east(2));
			this.setPoiseCluster(world, origin.up(9).south(3).east(4));
			this.setPoiseCluster(world, origin.up(10).south(3).east(4));
			this.setPoiseCluster(world, origin.up(10).south(2).east(4));
			
			this.setPoiseCluster(world, origin.up(6).west(4).south(4));
			this.setPoiseCluster(world, origin.up(7).west(4).south(4));
			this.setPoiseCluster(world, origin.up(8).west(4).south(4));
			this.setPoiseCluster(world, origin.up(9).west(4).south(4));
			this.setPoiseCluster(world, origin.up(9).west(4).south(3));
			this.setPoiseCluster(world, origin.up(10).west(4).south(3));
			this.setPoiseCluster(world, origin.up(10).west(4).south(2));
			this.setPoiseCluster(world, origin.up(9).west(3).south(4));
			this.setPoiseCluster(world, origin.up(10).west(3).south(4));
			this.setPoiseCluster(world, origin.up(10).west(2).south(4));
			
			//Top
			this.setPoiseCluster(world, origin.up(11).north(3).west(3));
			this.setPoiseCluster(world, origin.up(11).north(2).west(3));
			this.setPoiseCluster(world, origin.up(11).north(2).west(4));
			this.setPoiseCluster(world, origin.up(11).north(3).west(2));
			this.setPoiseCluster(world, origin.up(11).north(4).west(2));
			
			this.setPoiseCluster(world, origin.up(11).north(4).west());
			this.setPoiseCluster(world, origin.up(11).north(4));
			this.setPoiseCluster(world, origin.up(11).north(4).east());
			
			this.setPoiseCluster(world, origin.up(11).east(3).north(3));
			this.setPoiseCluster(world, origin.up(11).east(2).north(3));
			this.setPoiseCluster(world, origin.up(11).east(2).north(4));
			this.setPoiseCluster(world, origin.up(11).east(3).north(2));
			this.setPoiseCluster(world, origin.up(11).east(4).north(2));
			
			this.setPoiseCluster(world, origin.up(11).east(4).north());
			this.setPoiseCluster(world, origin.up(11).east(4));
			this.setPoiseCluster(world, origin.up(11).east(4).south());
			
			this.setPoiseCluster(world, origin.up(11).south(3).east(3));
			this.setPoiseCluster(world, origin.up(11).south(2).east(3));
			this.setPoiseCluster(world, origin.up(11).south(2).east(4));
			this.setPoiseCluster(world, origin.up(11).south(3).east(2));
			this.setPoiseCluster(world, origin.up(11).south(4).east(2));
			
			this.setPoiseCluster(world, origin.up(11).south(4).east());
			this.setPoiseCluster(world, origin.up(11).south(4));
			this.setPoiseCluster(world, origin.up(11).south(4).west());
			
			this.setPoiseCluster(world, origin.up(11).west(3).south(3));
			this.setPoiseCluster(world, origin.up(11).west(2).south(3));
			this.setPoiseCluster(world, origin.up(11).west(2).south(4));
			this.setPoiseCluster(world, origin.up(11).west(3).south(2));
			this.setPoiseCluster(world, origin.up(11).west(4).south(2));
			
			this.setPoiseCluster(world, origin.up(11).west(4).south());
			this.setPoiseCluster(world, origin.up(11).west(4));
			this.setPoiseCluster(world, origin.up(11).west(4).north());
			
			this.setPoiseCluster(world, origin.up(12).north(3).west());
			this.setPoiseCluster(world, origin.up(12).north(3));
			this.setPoiseCluster(world, origin.up(12).north(3).east());
			this.setPoiseCluster(world, origin.up(12).east(3).north());
			this.setPoiseCluster(world, origin.up(12).east(3));
			this.setPoiseCluster(world, origin.up(12).east(3).south());
			this.setPoiseCluster(world, origin.up(12).south(3).east());
			this.setPoiseCluster(world, origin.up(12).south(3));
			this.setPoiseCluster(world, origin.up(12).south(3).west());
			this.setPoiseCluster(world, origin.up(12).west(3).north());
			this.setPoiseCluster(world, origin.up(12).west(3));
			this.setPoiseCluster(world, origin.up(12).west(3).south());
			
			for(int x = origin.getX() - 1; x <= origin.getX() + 1; x++) {
				for(int z = origin.getZ() - 1; z <= origin.getZ() + 1; z++) {
					this.setPoiseCluster(world, new BlockPos(x, origin.up(13).getY(), z));
				}
			}
			for(int x = origin.getX() - 2; x <= origin.getX() + 2; x++) {
				for(int z = origin.getZ() - 2; z <= origin.getZ() + 2; z++) {
					if(x == origin.getX() + 2 || z == origin.getZ() + 2 || x == origin.getX() - 2 || z == origin.getZ() - 2) {
						this.setPoiseCluster(world, new BlockPos(x, origin.up(12).getY(), z));
					}
				}
			}
		}
	}
	
	private void tryToBuildBranch(IWorld world, BlockPos pos, Random rand, int treeHeight) {
		// First array - Values for the amout of blocks in the facing direction, Second Array - Values for the amount of blocks up
		int[] branchLengths = new int[] {
			rand.nextInt(3) + 1,
			rand.nextInt(3) + 1,
			1,
			1
		};
		int[] branchHeights = new int[] {
			1,
			1,
			rand.nextInt(4) + 1,
			rand.nextInt(4) + 4
		};
		Direction randDir = Direction.byIndex(rand.nextInt(4) + 2);
		int pickedHeight = rand.nextInt((int) Math.ceil(treeHeight / 3)) + 6;
		BlockPos pickedPosition = pos.offset(randDir).up(pickedHeight);
		if(world.getBlockState(pickedPosition).getMaterial().isReplaceable() && treeHeight > 15) {
			if(this.isViableBranchArea(world, pickedPosition, randDir, 2 + branchLengths[0] + branchLengths[1], pickedPosition.offset(randDir, branchLengths[0] + branchLengths[1] + branchLengths[2] + branchLengths[3]).up(3 + branchHeights[2]).up(branchHeights[3]))) {
				this.setPoiseLog(world, pickedPosition, rand, false, true);
				this.setPoiseLog(world, pickedPosition.up(), rand, false, true);
				this.setPoiseLog(world, pickedPosition.down(), rand, false, true);
				
				for(int dir = 0; dir <= branchLengths[0]; dir++) {
					this.setPoiseLogWithDirection(world, pickedPosition.offset(randDir, dir), rand, randDir);
					if(dir == branchLengths[0]) {
						this.setPoiseLog(world, pickedPosition.offset(randDir, dir).up(), rand, false, true);
					}
				}
				for(int dir = 1; dir <= branchLengths[1]; dir++) {
					this.setPoiseLogWithDirection(world, pickedPosition.offset(randDir, dir + branchLengths[0]).up(), rand, randDir);
					if(dir == branchLengths[1]) {
						this.setPoiseLog(world, pickedPosition.offset(randDir, branchLengths[0] + branchLengths[1]).up(2), rand, false, true);
					}
				}
				for(int dir = 1; dir <= branchLengths[2]; dir++) {
					this.setPoiseLogWithDirection(world, pickedPosition.offset(randDir, dir + branchLengths[0] + branchLengths[1]).up(2), rand, randDir);
					if(dir == branchLengths[2]) {
						for(int y = 0; y < branchHeights[2]; y++) {
							this.setPoiseLog(world, pickedPosition.offset(randDir, branchLengths[0] + branchLengths[1] + branchLengths[2]).up(3).up(y), rand, false, false);
						}
					}
				}
				for(int dir = 1; dir <= branchLengths[3]; dir++) {
					this.setPoiseLogWithDirection(world, pickedPosition.offset(randDir, dir + branchLengths[0] + branchLengths[1] + branchLengths[2]).up(2 + branchHeights[2]), rand, randDir);
					if(dir == branchLengths[3]) {
						for(int y = 0; y < branchHeights[3]; y++) {
							this.setPoiseLog(world, pickedPosition.offset(randDir, branchLengths[0] + branchLengths[1] + branchLengths[2] + branchLengths[3]).up(3 + branchHeights[2]).up(y), rand, false, false);
						}
					}
				}
				this.buildTreeTop(world, pickedPosition.offset(randDir, branchLengths[0] + branchLengths[1] + branchLengths[2] + branchLengths[3]).up(3 + branchHeights[2]).up(branchHeights[3]), rand, 0, 0);
			}
		}
	}
	
	private void buildSideBubble(IWorld world, BlockPos pos, Random rand) {
		int variant = rand.nextInt(100);
		if(variant >= 49) {
			Direction randDir = Direction.byIndex(rand.nextInt(4) + 2);
			this.setPoiseCluster(world, pos.offset(randDir));
		} else if(variant <= 15) {
			world.setBlockState(pos.up(), GLOWING_POISE_LOG.get(), 2);
			this.setPoiseCluster(world, pos.north());
			this.setPoiseCluster(world, pos.east());
			this.setPoiseCluster(world, pos.south());
			this.setPoiseCluster(world, pos.west());
			this.setPoiseCluster(world, pos.north().up());
			this.setPoiseCluster(world, pos.east().up());
			this.setPoiseCluster(world, pos.south().up());
			this.setPoiseCluster(world, pos.west().up());
		} else if(variant >= 16) {
			Direction randDir = Direction.byIndex(rand.nextInt(4) + 2);
			if(randDir == Direction.NORTH) {
				world.setBlockState(pos.up(), GLOWING_POISE_LOG.get(), 2);
				this.setPoiseCluster(world, pos.north());
				this.setPoiseCluster(world, pos.north().east());
				this.setPoiseCluster(world, pos.east());
				this.setPoiseCluster(world, pos.north().up());
				this.setPoiseCluster(world, pos.north().east().up());
				this.setPoiseCluster(world, pos.east().up());
			} else if(randDir == Direction.EAST) {
				world.setBlockState(pos.up(), GLOWING_POISE_LOG.get(), 2);
				this.setPoiseCluster(world, pos.east());
				this.setPoiseCluster(world, pos.east().south());
				this.setPoiseCluster(world, pos.south());
				this.setPoiseCluster(world, pos.east().up());
				this.setPoiseCluster(world, pos.east().south().up());
				this.setPoiseCluster(world, pos.south().up());
			} else if(randDir == Direction.SOUTH) {
				world.setBlockState(pos.up(), GLOWING_POISE_LOG.get(), 2);
				this.setPoiseCluster(world, pos.south());
				this.setPoiseCluster(world, pos.south().west());
				this.setPoiseCluster(world, pos.west());
				this.setPoiseCluster(world, pos.south().up());
				this.setPoiseCluster(world, pos.south().west().up());
				this.setPoiseCluster(world, pos.west().up());
			} else if(randDir == Direction.WEST) {
				world.setBlockState(pos.up(), GLOWING_POISE_LOG.get(), 2);
				this.setPoiseCluster(world, pos.west());
				this.setPoiseCluster(world, pos.west().north());
				this.setPoiseCluster(world, pos.north());
				this.setPoiseCluster(world, pos.west().up());
				this.setPoiseCluster(world, pos.west().north().up());
				this.setPoiseCluster(world, pos.north().up());
			}
		} else if(variant >= 30) {
			Direction randDir = Direction.byIndex(rand.nextInt(4) + 2);
			if(randDir == Direction.NORTH) {
				world.setBlockState(pos.up(), GLOWING_POISE_LOG.get(), 2);
				this.setPoiseCluster(world, pos.north());
				this.setPoiseCluster(world, pos.north().west());
				this.setPoiseCluster(world, pos.north(2).west());
				this.setPoiseCluster(world, pos.north().west().up());
				this.setPoiseCluster(world, pos.north().west().down());
				this.setPoiseCluster(world, pos.north().west(2));
				this.setPoiseCluster(world, pos.west());
			} else if(randDir == Direction.EAST) {
				world.setBlockState(pos.up(), GLOWING_POISE_LOG.get(), 2);
				this.setPoiseCluster(world, pos.east());
				this.setPoiseCluster(world, pos.east().north());
				this.setPoiseCluster(world, pos.east(2).north());
				this.setPoiseCluster(world, pos.east().north().up());
				this.setPoiseCluster(world, pos.east().north().down());
				this.setPoiseCluster(world, pos.east().north(2));
				this.setPoiseCluster(world, pos.north());
			} else if(randDir == Direction.SOUTH) {
				world.setBlockState(pos.up(), GLOWING_POISE_LOG.get(), 2);
				this.setPoiseCluster(world, pos.south());
				this.setPoiseCluster(world, pos.south().east());
				this.setPoiseCluster(world, pos.south(2).east());
				this.setPoiseCluster(world, pos.south().east().up());
				this.setPoiseCluster(world, pos.south().east().down());
				this.setPoiseCluster(world, pos.south().east(2));
				this.setPoiseCluster(world, pos.east());
			} else if(randDir == Direction.WEST) {
				world.setBlockState(pos.up(), GLOWING_POISE_LOG.get(), 2);
				this.setPoiseCluster(world, pos.west());
				this.setPoiseCluster(world, pos.west().south());
				this.setPoiseCluster(world, pos.west(2).south());
				this.setPoiseCluster(world, pos.west().south().up());
				this.setPoiseCluster(world, pos.west().south().down());
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

		for(int i = 0; i < 5; ++i) {
			int j = random.nextInt(64);
			int k = j % 8;
			int l = j / 8;
			if (k == 0 || k == 7 || l == 0 || l == 7) {
				this.placePoismossCircle(world, reader, pos.add(-3 + k, 0, -3 + l));
			}
		}
	}
	
	private void placePoismossCircle(IWorld world, IWorldGenerationReader reader, BlockPos center) {
		for(int i = -2; i <= 2; ++i) {
			for(int j = -2; j <= 2; ++j) {
				if (Math.abs(i) != 2 || Math.abs(j) != 2) {
					this.placePoismossAt(world, reader, center.add(i, 0, j));
				}
			}
		}
	}
	
	private void placePoismossAt(IWorld world, IWorldGenerationReader reader, BlockPos pos) {
		for(int i = 2; i >= -3; --i) {
			BlockPos blockpos = pos.up(i);
			if(world.getBlockState(blockpos).getBlock() == EEBlocks.EUMUS.get() || world.getBlockState(blockpos).getBlock() == Blocks.END_STONE) {
				BlockState newGround = world.getBlockState(blockpos).getBlock() == EEBlocks.EUMUS.get() ? POISMOSS_EUMUS.get() : EEBlocks.POISE_GRASS_BLOCK.get().getDefaultState();
				this.setBlockState(reader, blockpos, newGround);
				break;
			}

			if(!GenerationUtils.isAir(reader, blockpos) && i < 0) {
				break;
			}
		}
	}
	
	private void placeInnerDomeFeatures(IWorld world, Random rand, BlockPos pos) {
		if(rand.nextFloat() > 0.75) return;
		
		for(int x = 0; x < 128; x++) {
			BlockPos blockpos = pos.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));
			if(world.isAirBlock(blockpos) && EEBlocks.POISE_GRASS.get().getDefaultState().isValidPosition(world, blockpos)) {
				world.setBlockState(blockpos, EEBlocks.POISE_GRASS.get().getDefaultState(), 2);
			}
		}
		
		for(int x = 0; x < 128; x++) {
			BlockPos blockpos = pos.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));
			if(rand.nextFloat() <= 0.45F && world.isAirBlock(blockpos) && world.isAirBlock(blockpos.up()) && EEBlocks.POISE_GRASS_TALL.get().getDefaultState().isValidPosition(world, blockpos)) {
				EEBlocks.POISE_GRASS_TALL.get().placeAt(world, blockpos, 2);
			}
		}
		
		for(int x = 0; x < 4; x++) {
			BlockPos randPos = pos.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));
			if((world.isAirBlock(randPos) || world.getBlockState(randPos).getBlock() == EEBlocks.POISE_GRASS.get()) && EEBlocks.BOLLOOM_BUD.get().getDefaultState().isValidPosition(world, randPos)) {
				world.setBlockState(randPos, EEBlocks.BOLLOOM_BUD.get().getDefaultState(), 2);
			}	
		}
	}
	
	public boolean canFitBud(IWorld world, BlockPos pos) {
		for(int y = pos.getY(); y < pos.getY() + 5; y++) {
			for(int x = pos.getX() - 1; x < pos.getX() + 1; x++) {
				for(int z = pos.getZ() - 1; z < pos.getZ() + 1; z++) {
					BlockPos newPos = new BlockPos(x, y, z);
					if(newPos != pos && !world.isAirBlock(newPos)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	private void setPoiseLog(IWorld world, BlockPos pos, Random rand, boolean isTreeBase, boolean noBubbles) {
		BlockState logState = rand.nextFloat() <= 0.11F ? GLOWING_POISE_LOG.get() : POISE_LOG.get();
		if(world.getBlockState(pos).getMaterial().isReplaceable() || world.getBlockState(pos).getBlock() == EEBlocks.POISE_CLUSTER.get()) {
			world.setBlockState(pos, logState, 2);
			if(!noBubbles && logState == GLOWING_POISE_LOG.get()) {
				if(!isTreeBase) {
					boolean willCollide = world.getBlockState(pos.down()).getBlock() == EEBlocks.POISE_LOG_GLOWING.get() || world.getBlockState(pos.down(2)).getBlock() == EEBlocks.POISE_LOG_GLOWING.get() || world.getBlockState(pos.down(3)).getBlock() == EEBlocks.POISE_LOG_GLOWING.get() 
						|| world.getBlockState(pos.up()).getBlock() == EEBlocks.POISE_LOG_GLOWING.get() || world.getBlockState(pos.up(2)).getBlock() == EEBlocks.POISE_LOG_GLOWING.get() || world.getBlockState(pos.up(3)).getBlock() == EEBlocks.POISE_LOG_GLOWING.get();
					if(rand.nextFloat() <= 0.70F && !willCollide
						&& world.getBlockState(pos.north()).getMaterial().isReplaceable() && world.getBlockState(pos.east()).getMaterial().isReplaceable()
						&& world.getBlockState(pos.south()).getMaterial().isReplaceable() && world.getBlockState(pos.west()).getMaterial().isReplaceable()) 
							this.buildSideBubble(world, pos, rand);
				} else {
					boolean willCollide = world.getBlockState(pos.down()).getBlock() == EEBlocks.POISE_LOG_GLOWING.get() || world.getBlockState(pos.down(2)).getBlock() == EEBlocks.POISE_LOG_GLOWING.get() || world.getBlockState(pos.down(3)).getBlock() == EEBlocks.POISE_LOG_GLOWING.get() 
						|| world.getBlockState(pos.up()).getBlock() == EEBlocks.POISE_LOG_GLOWING.get() || world.getBlockState(pos.up(2)).getBlock() == EEBlocks.POISE_LOG_GLOWING.get() || world.getBlockState(pos.up(3)).getBlock() == EEBlocks.POISE_LOG_GLOWING.get();
					if(rand.nextFloat() <= 0.40F && !willCollide) {
						this.buildSideBubble(world, pos, rand);
					}
				}
			}
		}
	}
	
	private void setPoiseLogWithDirection(IWorld world, BlockPos pos, Random rand, Direction direction) {
		BlockState logState = rand.nextFloat() <= 0.90F ? POISE_LOG.get().with(BlockGlowingPoiseLog.AXIS, direction.getAxis()) : GLOWING_POISE_LOG.get().with(BlockGlowingPoiseLog.AXIS, direction.getAxis());
		if(world.getBlockState(pos).getMaterial().isReplaceable()) {
			world.setBlockState(pos, logState, 2);
		}
	}
	
	private void setPoiseCluster(IWorld world, BlockPos pos) {
		if(world.getBlockState(pos).getMaterial().isReplaceable()) {
			world.setBlockState(pos, EEBlocks.POISE_CLUSTER.get().getDefaultState(), 2);
		}
	}
	
	private boolean isViableBranchArea(IWorld world, BlockPos pos, Direction direction, int directionLength, BlockPos topPosition) {
		int y = topPosition.getY() - pos.getY();
		if(direction == Direction.NORTH) {
			if(GenerationUtils.isAreaReplacable(world, pos.down().west().north(directionLength).getX(), pos.down().west().north(directionLength).getY(), pos.down().west().north(directionLength).getZ(), pos.up(y).east().getX(), pos.up(y).east().getY(), pos.up(y).east().getZ())) {
				if(GenerationUtils.isAreaReplacable(world, topPosition.north(3).west(3).getX(), topPosition.north(3).west(3).getY(), topPosition.north(3).west(3).getZ(), topPosition.south(3).east(3).up(7).getX(), topPosition.south(3).east(3).up(7).getY(), topPosition.south(3).east(3).up(7).getZ())) {
					return true;
				}
			}
		} else if(direction == Direction.EAST) {
			if(GenerationUtils.isAreaReplacable(world, pos.down().north().east(directionLength).getX(), pos.down().north().east(directionLength).getY(), pos.down().north().east(directionLength).getZ(), pos.up(y).west().getX(), pos.up(y).west().getY(), pos.up(y).west().getZ())) {
				if(GenerationUtils.isAreaReplacable(world, topPosition.north(3).west(3).getX(), topPosition.north(3).west(3).getY(), topPosition.north(3).west(3).getZ(), topPosition.south(3).east(3).up(7).getX(), topPosition.south(3).east(3).up(7).getY(), topPosition.south(3).east(3).up(7).getZ())) {
					return true;
				}
			}
		} else if(direction == Direction.SOUTH) {
			if(GenerationUtils.isAreaReplacable(world, pos.down().west().getX(), pos.down().west().getY(), pos.down().west().getZ(), pos.up(y).east().south(directionLength).getX(), pos.up(y).east().south(directionLength).getY(), pos.up(y).east().south(directionLength).getZ())) {
				if(GenerationUtils.isAreaReplacable(world, topPosition.north(3).west(3).getX(), topPosition.north(3).west(3).getY(), topPosition.north(3).west(3).getZ(), topPosition.south(3).east(3).up(7).getX(), topPosition.south(3).east(3).up(7).getY(), topPosition.south(3).east(3).up(7).getZ())) {
					return true;
				}
			}
		} else {
			if(GenerationUtils.isAreaReplacable(world, pos.down().north().getX(), pos.down().north().getY(), pos.down().north().getZ(), pos.up(y).south().west(directionLength).getX(), pos.up(y).south().west(directionLength).getY(), pos.up(y).south().west(directionLength).getZ())) {
				if(GenerationUtils.isAreaReplacable(world, topPosition.north(3).west(3).getX(), topPosition.north(3).west(3).getY(), topPosition.north(3).west(3).getZ(), topPosition.south(3).east(3).up(7).getX(), topPosition.south(3).east(3).up(7).getY(), topPosition.south(3).east(3).up(7).getZ())) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isAreaOpen(IWorld world, BlockPos pos) {
		for(int y = pos.getY(); y < pos.getY() + 1; y++) {
			for(int x = pos.getX() - 1; x < pos.getX() + 2; x++) {
				for(int z = pos.getZ() - 1; z < pos.getZ() + 2; z++) {
					if(!world.getBlockState(new BlockPos(x, y, z)).getMaterial().isReplaceable()) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	private boolean isValidGround(IWorld world, BlockPos pos) {
		Block block = world.getBlockState(pos).getBlock();
		return block == Blocks.END_STONE.getBlock() || block.isIn(EETags.Blocks.END_PLANTABLE) || block.isIn(EETags.Blocks.POISE_PLANTABLE);
	}
}