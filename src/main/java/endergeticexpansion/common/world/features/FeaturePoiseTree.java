package endergeticexpansion.common.world.features;

import java.util.Random;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import endergeticexpansion.api.util.GenerationUtils;
import endergeticexpansion.core.registry.EEBlocks;
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

	public FeaturePoiseTree(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactoryIn) {
		super(configFactoryIn);
	}
	
	@Override
	public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config) {
		int treeHeight = rand.nextInt(19) + 13;
		if(world.getBlockState(pos.down()) == EEBlocks.POISE_GRASS_BLOCK.getDefaultState() && this.isAreaOpen(world, pos) || world.getBlockState(pos.down()) == Blocks.END_STONE.getDefaultState() && this.isAreaOpen(world, pos)) {
			this.buildTreeBase(world, pos, rand);
			this.buildStem(world, pos, rand, treeHeight);
			
			this.buildPoismossCircle(world, world, rand, pos);
			return true;
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
	}
	
	private void buildStem(IWorld world, BlockPos pos, Random rand, int height) {
		for(int y = 1; y < height; y++) {
			boolean doBubbles = y <= height - 2 ? false : true;
			this.setPoiseLog(world, pos.up(y), rand, false, doBubbles);
		}
	}
	
	private void buildSideBubble(IWorld world, BlockPos pos, Random rand) {
		int variant = rand.nextInt(100);
		if(variant >= 49) {
			Direction randDir = Direction.byIndex(rand.nextInt(4) + 2);
			this.setPoiseCluster(world, pos.offset(randDir));
		} else if(variant <= 15) {
			world.setBlockState(pos.up(), EEBlocks.POISE_LOG_GLOWING.getDefaultState(), 2);
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
				world.setBlockState(pos.up(), EEBlocks.POISE_LOG_GLOWING.getDefaultState(), 2);
				this.setPoiseCluster(world, pos.north());
				this.setPoiseCluster(world, pos.north().east());
				this.setPoiseCluster(world, pos.east());
				this.setPoiseCluster(world, pos.north().up());
				this.setPoiseCluster(world, pos.north().east().up());
				this.setPoiseCluster(world, pos.east().up());
			} else if(randDir == Direction.EAST) {
				world.setBlockState(pos.up(), EEBlocks.POISE_LOG_GLOWING.getDefaultState(), 2);
				this.setPoiseCluster(world, pos.east());
				this.setPoiseCluster(world, pos.east().south());
				this.setPoiseCluster(world, pos.south());
				this.setPoiseCluster(world, pos.east().up());
				this.setPoiseCluster(world, pos.east().south().up());
				this.setPoiseCluster(world, pos.south().up());
			} else if(randDir == Direction.SOUTH) {
				world.setBlockState(pos.up(), EEBlocks.POISE_LOG_GLOWING.getDefaultState(), 2);
				this.setPoiseCluster(world, pos.south());
				this.setPoiseCluster(world, pos.south().west());
				this.setPoiseCluster(world, pos.west());
				this.setPoiseCluster(world, pos.south().up());
				this.setPoiseCluster(world, pos.south().west().up());
				this.setPoiseCluster(world, pos.west().up());
			} else if(randDir == Direction.WEST) {
				world.setBlockState(pos.up(), EEBlocks.POISE_LOG_GLOWING.getDefaultState(), 2);
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
				world.setBlockState(pos.up(), EEBlocks.POISE_LOG_GLOWING.getDefaultState(), 2);
				this.setPoiseCluster(world, pos.north());
				this.setPoiseCluster(world, pos.north().west());
				this.setPoiseCluster(world, pos.north(2).west());
				this.setPoiseCluster(world, pos.north().west().up());
				this.setPoiseCluster(world, pos.north().west().down());
				this.setPoiseCluster(world, pos.north().west(2));
				this.setPoiseCluster(world, pos.west());
			} else if(randDir == Direction.EAST) {
				world.setBlockState(pos.up(), EEBlocks.POISE_LOG_GLOWING.getDefaultState(), 2);
				this.setPoiseCluster(world, pos.east());
				this.setPoiseCluster(world, pos.east().north());
				this.setPoiseCluster(world, pos.east(2).north());
				this.setPoiseCluster(world, pos.east().north().up());
				this.setPoiseCluster(world, pos.east().north().down());
				this.setPoiseCluster(world, pos.east().north(2));
				this.setPoiseCluster(world, pos.north());
			} else if(randDir == Direction.SOUTH) {
				world.setBlockState(pos.up(), EEBlocks.POISE_LOG_GLOWING.getDefaultState(), 2);
				this.setPoiseCluster(world, pos.south());
				this.setPoiseCluster(world, pos.south().east());
				this.setPoiseCluster(world, pos.south(2).east());
				this.setPoiseCluster(world, pos.south().east().up());
				this.setPoiseCluster(world, pos.south().east().down());
				this.setPoiseCluster(world, pos.south().east(2));
				this.setPoiseCluster(world, pos.east());
			} else if(randDir == Direction.WEST) {
				world.setBlockState(pos.up(), EEBlocks.POISE_LOG_GLOWING.getDefaultState(), 2);
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
			if(world.getBlockState(blockpos).getBlock() == EEBlocks.EUMUS || world.getBlockState(blockpos).getBlock() == Blocks.END_STONE) {
				BlockState newGround = world.getBlockState(blockpos).getBlock() == EEBlocks.EUMUS ? EEBlocks.POISMOSS_EUMUS.getDefaultState() : EEBlocks.POISE_GRASS_BLOCK.getDefaultState();
				this.setBlockState(reader, blockpos, newGround);
				break;
			}

			if(!GenerationUtils.isAir(reader, blockpos) && i < 0) {
				break;
			}
		}
	}

	private void setPoiseLog(IWorld world, BlockPos pos, Random rand, boolean isTreeBase, boolean noBubbles) {
		BlockState logState = rand.nextFloat() <= 0.11F ? EEBlocks.POISE_LOG_GLOWING.getDefaultState() : EEBlocks.POISE_LOG.getDefaultState();
		if(world.getBlockState(pos).getMaterial().isReplaceable() || world.getBlockState(pos).getBlock() == EEBlocks.POISE_CLUSTER) {
			world.setBlockState(pos, logState, 2);
			if(!noBubbles && logState == EEBlocks.POISE_LOG_GLOWING.getDefaultState()) {
				if(!isTreeBase) {
					boolean willCollide = world.getBlockState(pos.down()).getBlock() == EEBlocks.POISE_LOG_GLOWING || world.getBlockState(pos.down(2)).getBlock() == EEBlocks.POISE_LOG_GLOWING || world.getBlockState(pos.down(3)).getBlock() == EEBlocks.POISE_LOG_GLOWING 
						|| world.getBlockState(pos.up()).getBlock() == EEBlocks.POISE_LOG_GLOWING || world.getBlockState(pos.up(2)).getBlock() == EEBlocks.POISE_LOG_GLOWING || world.getBlockState(pos.up(3)).getBlock() == EEBlocks.POISE_LOG_GLOWING;
					if(rand.nextFloat() <= 0.70F && !willCollide
						&& world.getBlockState(pos.north()).getMaterial().isReplaceable() && world.getBlockState(pos.east()).getMaterial().isReplaceable()
						&& world.getBlockState(pos.south()).getMaterial().isReplaceable() && world.getBlockState(pos.west()).getMaterial().isReplaceable()) 
							this.buildSideBubble(world, pos, rand);
				} else {
					boolean willCollide = world.getBlockState(pos.down()).getBlock() == EEBlocks.POISE_LOG_GLOWING || world.getBlockState(pos.down(2)).getBlock() == EEBlocks.POISE_LOG_GLOWING || world.getBlockState(pos.down(3)).getBlock() == EEBlocks.POISE_LOG_GLOWING 
						|| world.getBlockState(pos.up()).getBlock() == EEBlocks.POISE_LOG_GLOWING || world.getBlockState(pos.up(2)).getBlock() == EEBlocks.POISE_LOG_GLOWING || world.getBlockState(pos.up(3)).getBlock() == EEBlocks.POISE_LOG_GLOWING;
					if(rand.nextFloat() <= 0.40F && !willCollide) {
						this.buildSideBubble(world, pos, rand);
					}
				}
			}
		}
	}
	
	private void setPoiseCluster(IWorld world, BlockPos pos) {
		if(world.getBlockState(pos).getMaterial().isReplaceable()) {
			world.setBlockState(pos, EEBlocks.POISE_CLUSTER.getDefaultState(), 2);
		}
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
	
}
