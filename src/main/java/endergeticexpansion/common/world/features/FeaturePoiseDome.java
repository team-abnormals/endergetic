package endergeticexpansion.common.world.features;

import java.util.Random;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import endergeticexpansion.api.util.GenerationUtils;
import endergeticexpansion.core.registry.EEBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class FeaturePoiseDome extends Feature<NoFeatureConfig> {

	public FeaturePoiseDome(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactory) {
		super(configFactory);
	}

	@Override
	public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config) {
		if(world.getBlockState(pos).getBlock() == Blocks.END_STONE) {
			this.buildDomeBase(world, pos.up(10), rand);
			this.buildDome(world, pos.up(10), rand);
		}
		return false;
	}
	
	private void buildDomeBase(IWorld world, BlockPos pos, Random rand) {
		BlockPos origin = pos;
		for(int x = origin.getX() - 13; x <= origin.getX() + 13; x++) {
			for(int z = origin.getZ() - 2; z <= origin.getZ() + 2; z++) {
				if(x == origin.getX() - 13 || x == origin.getX() + 13) {
					this.setPoiseLog(world, new BlockPos(x, origin.getY(), z), rand);
				} else {
					this.setPoismoss(world, new BlockPos(x, origin.getY(), z));
				}
			}
		}
		for(int x = origin.getX() - 12; x <= origin.getX() + 12; x++) {
			for(int z = origin.getZ() - 5; z <= origin.getZ() + 5; z++) {
				if(x == origin.getX() - 12 || x == origin.getX() + 12) {
					this.setPoiseLog(world, new BlockPos(x, origin.getY(), z), rand);
				} else {
					this.setPoismoss(world, new BlockPos(x, origin.getY(), z));
				}
			}
		}
		for(int x = origin.getX() - 11; x <= origin.getX() + 11; x++) {
			for(int z = origin.getZ() - 7; z <= origin.getZ() + 7; z++) {
				if(x == origin.getX() - 11 || x == origin.getX() + 11) {
					this.setPoiseLog(world, new BlockPos(x, origin.getY(), z), rand);
				} else {
					this.setPoismoss(world, new BlockPos(x, origin.getY(), z));
				}
			}
		}
		for(int x = origin.getX() - 10; x <= origin.getX() + 10; x++) {
			for(int z = origin.getZ() - 8; z <= origin.getZ() + 8; z++) {
				if(x == origin.getX() - 10 || x == origin.getX() + 10) {
					this.setPoiseLog(world, new BlockPos(x, origin.getY(), z), rand);
				} else {
					this.setPoismoss(world, new BlockPos(x, origin.getY(), z));
				}
			}
		}
		for(int x = origin.getX() - 9; x <= origin.getX() + 9; x++) {
			for(int z = origin.getZ() - 9; z <= origin.getZ() + 9; z++) {
				if(x == origin.getX() - 9 || x == origin.getX() + 9) {
					this.setPoiseLog(world, new BlockPos(x, origin.getY(), z), rand);
				} else {
					this.setPoismoss(world, new BlockPos(x, origin.getY(), z));
				}
			}
		}
		for(int x = origin.getX() - 8; x <= origin.getX() + 8; x++) {
			for(int z = origin.getZ() - 10; z <= origin.getZ() + 10; z++) {
				if(x == origin.getX() - 8 || x == origin.getX() + 8) {
					this.setPoiseLog(world, new BlockPos(x, origin.getY(), z), rand);
				} else {
					this.setPoismoss(world, new BlockPos(x, origin.getY(), z));
				}
			}
		}
		for(int x = origin.getX() - 7; x <= origin.getX() + 7; x++) {
			for(int z = origin.getZ() - 11; z <= origin.getZ() + 11; z++) {
				if(x == origin.getX() - 7 || x == origin.getX() + 7 || x == origin.getX() - 6 || x == origin.getX() + 6) {
					this.setPoiseLog(world, new BlockPos(x, origin.getY(), z), rand);
				} else {
					this.setPoismoss(world, new BlockPos(x, origin.getY(), z));
				}
			}
		}
		for(int x = origin.getX() - 5; x <= origin.getX() + 5; x++) {
			for(int z = origin.getZ() - 12; z <= origin.getZ() + 12; z++) {
				if(x <= origin.getX() - 3 || x >= origin.getX() + 3) {
					this.setPoiseLog(world, new BlockPos(x, origin.getY(), z), rand);
				} else {
					this.setPoismoss(world, new BlockPos(x, origin.getY(), z));
				}
			}
		}
		for(int x = 0; x <= 4; x++) {
			this.setPoiseLog(world, origin.north(13).east(2).west(x), rand);
			this.setPoiseLog(world, origin.south(13).east(2).west(x), rand);
		}
		
		//Doors
		for(int y = 1; y <= 3; y++) {
			this.setPoiseLog(world, origin.north(12).west(3).up(y), rand);
		}
		this.setPoiseLog(world, origin.north(12).west(2).up(4), rand);
		for(int y = 1; y <= 3; y++) {
			this.setPoiseLog(world, origin.north(12).east(3).up(y), rand);
		}
		this.setPoiseLog(world, origin.north(12).east(2).up(4), rand);
		
		this.setPoiseLog(world, origin.north(12).west().up(5), rand);
		this.setPoiseLog(world, origin.north(12).up(5), rand);
		this.setPoiseLog(world, origin.north(12).east().up(5), rand);
		if(rand.nextFloat() <= 0.25F) {
			if(rand.nextInt(3) == 0) {
				this.setPoiseLog(world, origin.north(12).up(6), rand);
			} else {
				if(rand.nextInt(2) == 0) {
					this.setPoiseLog(world, origin.north(12).west().up(6), rand);
				} else {
					this.setPoiseLog(world, origin.north(12).east().up(6), rand);
				}
			}
		}
		
		for(int y = 1; y <= 3; y++) {
			this.setPoiseLog(world, origin.east(12).north(3).up(y), rand);
		}
		this.setPoiseLog(world, origin.east(12).north(2).up(4), rand);
		for(int y = 1; y <= 3; y++) {
			this.setPoiseLog(world, origin.east(12).south(3).up(y), rand);
		}
		this.setPoiseLog(world, origin.east(12).south(2).up(4), rand);
		
		this.setPoiseLog(world, origin.east(12).north().up(5), rand);
		this.setPoiseLog(world, origin.east(12).up(5), rand);
		this.setPoiseLog(world, origin.east(12).south().up(5), rand);
		if(rand.nextFloat() <= 0.25F) {
			if(rand.nextInt(3) == 0) {
				this.setPoiseLog(world, origin.east(12).up(6), rand);
			} else {
				if(rand.nextInt(2) == 0) {
					this.setPoiseLog(world, origin.east(12).north().up(6), rand);
				} else {
					this.setPoiseLog(world, origin.east(12).south().up(6), rand);
				}
			}
		}
		
		for(int y = 1; y <= 3; y++) {
			this.setPoiseLog(world, origin.south(12).east(3).up(y), rand);
		}
		this.setPoiseLog(world, origin.south(12).east(2).up(4), rand);
		for(int y = 1; y <= 3; y++) {
			this.setPoiseLog(world, origin.south(12).west(3).up(y), rand);
		}
		this.setPoiseLog(world, origin.south(12).west(2).up(4), rand);
		
		this.setPoiseLog(world, origin.south(12).east().up(5), rand);
		this.setPoiseLog(world, origin.south(12).up(5), rand);
		this.setPoiseLog(world, origin.south(12).west().up(5), rand);
		if(rand.nextFloat() <= 0.25F) {
			if(rand.nextInt(3) == 0) {
				this.setPoiseLog(world, origin.south(12).up(6), rand);
			} else {
				if(rand.nextInt(2) == 0) {
					this.setPoiseLog(world, origin.south(12).east().up(6), rand);
				} else {
					this.setPoiseLog(world, origin.south(12).west().up(6), rand);
				}
			}
		}
		
		for(int y = 1; y <= 3; y++) {
			this.setPoiseLog(world, origin.west(12).south(3).up(y), rand);
		}
		this.setPoiseLog(world, origin.west(12).south(2).up(4), rand);
		for(int y = 1; y <= 3; y++) {
			this.setPoiseLog(world, origin.west(12).north(3).up(y), rand);
		}
		this.setPoiseLog(world, origin.west(12).north(2).up(4), rand);
		
		this.setPoiseLog(world, origin.west(12).south().up(5), rand);
		this.setPoiseLog(world, origin.west(12).up(5), rand);
		this.setPoiseLog(world, origin.west(12).north().up(5), rand);
		if(rand.nextFloat() <= 0.25F) {
			if(rand.nextInt(3) == 0) {
				this.setPoiseLog(world, origin.west(12).up(6), rand);
			} else {
				if(rand.nextInt(2) == 0) {
					this.setPoiseLog(world, origin.west(12).south().up(6), rand);
				} else {
					this.setPoiseLog(world, origin.west(12).north().up(6), rand);
				}
			}
		}
		this.buildPoismossCircle(world, world, rand, origin);
	}
	
	private void buildDome(IWorld world, BlockPos origin, Random rand) {
		//North
		{
		GenerationUtils.fillAreaWithBlockCube(world, origin.up().north(12).west(5).getX(), origin.up().north(12).west(5).getY(), origin.up().north(12).west(5).getZ(), origin.up(4).north(12).west(3).getX(), origin.up(4).north(12).west(3).getY(), origin.up(4).north(12).west(3).getZ(), EEBlocks.POISE_CLUSTER.getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up().north(12).east(3).getX(), origin.up().north(12).east(3).getY(), origin.up().north(12).east(3).getZ(), origin.up(4).north(12).east(5).getX(), origin.up(4).north(12).east(5).getY(), origin.up(4).north(12).east(5).getZ(), EEBlocks.POISE_CLUSTER.getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(5).north(12).west(4).getX(), origin.up(5).north(12).west(4).getY(), origin.up(5).north(12).west(4).getZ(), origin.up(6).north(12).east(4).getX(), origin.up(6).north(12).east(4).getY(), origin.up(6).north(12).east(4).getZ(), EEBlocks.POISE_CLUSTER.getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(7).north(12).west(2).getX(), origin.up(7).north(12).west(2).getY(), origin.up(7).north(12).west(2).getZ(), origin.up(7).north(12).east(2).getX(), origin.up(7).north(12).east(2).getY(), origin.up(7).north(12).east(2).getZ(), EEBlocks.POISE_CLUSTER.getDefaultState());
		}
		
		{
		GenerationUtils.fillAreaWithBlockCube(world, origin.up().north(11).west(7).getX(), origin.up().north(11).west(7).getY(), origin.up().north(11).west(7).getZ(), origin.up(5).north(11).west(6).getX(), origin.up(5).north(11).west(6).getY(), origin.up(5).north(11).west(6).getZ(), EEBlocks.POISE_CLUSTER.getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(5).north(11).west(6).getX(), origin.up(5).north(11).west(6).getY(), origin.up(5).north(11).west(6).getZ(), origin.up(7).north(11).west(5).getX(), origin.up(7).north(11).west(5).getY(), origin.up(7).north(11).west(5).getZ(), EEBlocks.POISE_CLUSTER.getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(7).north(11).west(5).getX(), origin.up(7).north(11).west(5).getY(), origin.up(7).north(11).west(5).getZ(), origin.up(8).north(11).west(3).getX(), origin.up(8).north(11).west(3).getY(), origin.up(8).north(11).west(3).getZ(), EEBlocks.POISE_CLUSTER.getDefaultState());
		
		GenerationUtils.fillAreaWithBlockCube(world, origin.up().north(11).east(6).getX(), origin.up().north(11).east(6).getY(), origin.up().north(11).east(6).getZ(), origin.up(5).north(11).east(7).getX(), origin.up(5).north(11).east(7).getY(), origin.up(5).north(11).east(7).getZ(), EEBlocks.POISE_CLUSTER.getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(5).north(11).east(5).getX(), origin.up(5).north(11).east(5).getY(), origin.up(5).north(11).east(5).getZ(), origin.up(7).north(11).east(6).getX(), origin.up(7).north(11).east(6).getY(), origin.up(7).north(11).east(6).getZ(), EEBlocks.POISE_CLUSTER.getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(7).north(11).east(3).getX(), origin.up(7).north(11).east(3).getY(), origin.up(7).north(11).east(3).getZ(), origin.up(8).north(11).east(5).getX(), origin.up(8).north(11).east(5).getY(), origin.up(8).north(11).east(5).getZ(), EEBlocks.POISE_CLUSTER.getDefaultState());
		
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(8).north(11).west(3).getX(), origin.up(8).north(11).west(3).getY(), origin.up(8).north(11).west(3).getZ(), origin.up(9).north(11).east(3).getX(), origin.up(9).north(11).east(3).getY(), origin.up(9).north(11).east(3).getZ(), EEBlocks.POISE_CLUSTER.getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(10).north(11).west().getX(), origin.up(10).north(11).west().getY(), origin.up(10).north(11).west().getZ(), origin.up(11).north(10).east().getX(), origin.up(10).north(11).east().getY(), origin.up(10).north(11).east().getZ(), EEBlocks.POISE_CLUSTER.getDefaultState());
		}
		
		{
		GenerationUtils.fillAreaWithBlockCube(world, origin.up().north(10).west(8).getX(), origin.up().north(10).west(8).getY(), origin.up().north(10).west(8).getZ(), origin.up(6).north(10).west(8).getX(), origin.up(6).north(10).west(8).getY(), origin.up(6).north(10).west(8).getZ(), EEBlocks.POISE_CLUSTER.getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(6).north(10).west(7).getX(), origin.up(6).north(10).west(7).getY(), origin.up(6).north(10).west(7).getZ(), origin.up(8).north(10).west(7).getX(), origin.up(8).north(10).west(7).getY(), origin.up(8).north(10).west(7).getZ(), EEBlocks.POISE_CLUSTER.getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(8).north(10).west(6).getX(), origin.up(8).north(10).west(6).getY(), origin.up(8).north(10).west(6).getZ(), origin.up(9).north(10).west(6).getX(), origin.up(9).north(10).west(6).getY(), origin.up(9).north(10).west(6).getZ(), EEBlocks.POISE_CLUSTER.getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(9).north(10).west(6).getX(), origin.up(9).north(10).west(6).getY(), origin.up(9).north(10).west(6).getZ(), origin.up(9).north(10).west(4).getX(), origin.up(9).north(10).west(4).getY(), origin.up(9).north(10).west(4).getZ(), EEBlocks.POISE_CLUSTER.getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(10).north(10).west(4).getX(), origin.up(10).north(10).west(4).getY(), origin.up(10).north(10).west(4).getZ(), origin.up(10).north(10).west(2).getX(), origin.up(10).north(10).west(2).getY(), origin.up(10).north(10).west(2).getZ(), EEBlocks.POISE_CLUSTER.getDefaultState());
		
		GenerationUtils.fillAreaWithBlockCube(world, origin.up().north(10).east(8).getX(), origin.up().north(10).east(8).getY(), origin.up().north(10).east(8).getZ(), origin.up(6).north(10).east(8).getX(), origin.up(6).north(10).east(8).getY(), origin.up(6).north(10).east(8).getZ(), EEBlocks.POISE_CLUSTER.getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(6).north(10).east(7).getX(), origin.up(6).north(10).east(7).getY(), origin.up(6).north(10).east(7).getZ(), origin.up(8).north(10).east(7).getX(), origin.up(8).north(10).east(7).getY(), origin.up(8).north(10).east(7).getZ(), EEBlocks.POISE_CLUSTER.getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(8).north(10).east(6).getX(), origin.up(8).north(10).east(6).getY(), origin.up(8).north(10).east(6).getZ(), origin.up(9).north(10).east(6).getX(), origin.up(9).north(10).east(6).getY(), origin.up(9).north(10).east(6).getZ(), EEBlocks.POISE_CLUSTER.getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(9).north(10).east(4).getX(), origin.up(9).north(10).east(4).getY(), origin.up(9).north(10).east(4).getZ(), origin.up(9).north(10).east(6).getX(), origin.up(9).north(10).east(6).getY(), origin.up(9).north(10).east(6).getZ(), EEBlocks.POISE_CLUSTER.getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(10).north(10).east(2).getX(), origin.up(10).north(10).east(2).getY(), origin.up(10).north(10).east(2).getZ(), origin.up(10).north(10).east(4).getX(), origin.up(10).north(10).east(4).getY(), origin.up(10).north(10).east(4).getZ(), EEBlocks.POISE_CLUSTER.getDefaultState());
		
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(11).north(10).west(2).getX(), origin.up(11).north(10).west(2).getY(), origin.up(11).north(10).west(2).getZ(), origin.up(11).north(10).east(2).getX(), origin.up(11).north(10).east(2).getY(), origin.up(11).north(10).east(2).getZ(), EEBlocks.POISE_CLUSTER.getDefaultState());
		}
		
	}
	
	private void buildPoismossCircle(IWorld world, IWorldGenerationReader reader, Random random, BlockPos pos) {
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
		BlockPos blockpos = pos.up();
		if(world.getBlockState(blockpos).getBlock() == Blocks.AIR) {
			BlockState newGround = EEBlocks.POISE_GRASS_BLOCK.getDefaultState();
			this.setBlockState(reader, blockpos, newGround);
		}
	}
	
	private void setPoiseCluster(IWorld world, BlockPos pos, Random rand) {
		if(world.getBlockState(pos).getMaterial().isReplaceable()) {
			world.setBlockState(pos, EEBlocks.POISE_CLUSTER.getDefaultState(), 2);
		}
	}
	
	private void setPoiseLog(IWorld world, BlockPos pos, Random rand) {
		BlockState logState = rand.nextFloat() <= 0.90F ? EEBlocks.POISE_LOG.getDefaultState() : EEBlocks.POISE_LOG_GLOWING.getDefaultState();
		if(world.getBlockState(pos).getMaterial().isReplaceable()) {
			world.setBlockState(pos, logState, 2);
		}
	}
	
	private void setPoismoss(IWorld world, BlockPos pos) {
		world.setBlockState(pos, EEBlocks.POISE_GRASS_BLOCK.getDefaultState(), 2);
	}

}
