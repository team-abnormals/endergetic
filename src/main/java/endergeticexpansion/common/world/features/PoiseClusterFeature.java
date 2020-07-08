package endergeticexpansion.common.world.features;

import java.util.Random;

import com.mojang.serialization.Codec;

import endergeticexpansion.core.registry.EEBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.StructureManager;

/**
 * @author - SmellyModder(Luke Tonon)
 */
public class PoiseClusterFeature extends Feature<NoFeatureConfig> {
	protected static final BlockState GLOWING_POISE_LOG = EEBlocks.POISE_WOOD_GLOWING.get().getDefaultState();
	protected static final BlockState POISE_CLUSTER = EEBlocks.POISE_CLUSTER.get().getDefaultState();

	public PoiseClusterFeature(Codec<NoFeatureConfig> configFactoryIn) {
		super(configFactoryIn);
	}

	@Override
	public boolean func_230362_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config) {
		if(world.getBlockState(pos).getMaterial().isReplaceable() && world.getBlockState(pos.down()).getBlock() == EEBlocks.POISMOSS.get()) {
			this.createGlob(rand.nextInt(12), world, pos, rand);
			return true;
		}
		return false;
	}
	
	private void createGlob(int variation, IWorld world, BlockPos pos, Random rand) {
		this.setBlockIfReplacable(world, pos, GLOWING_POISE_LOG);
		this.setBlockIfReplacable(world, pos.north(), POISE_CLUSTER);
		this.setBlockIfReplacable(world, pos.east(), POISE_CLUSTER);
		this.setBlockIfReplacable(world, pos.south(), POISE_CLUSTER);
		this.setBlockIfReplacable(world, pos.west(), POISE_CLUSTER);
		this.setBlockIfReplacable(world, pos.up(), POISE_CLUSTER);
		
		this.setBlockIfReplacable(world, pos.north().up(), POISE_CLUSTER);
		this.setBlockIfReplacable(world, pos.east().up(), POISE_CLUSTER);
		this.setBlockIfReplacable(world, pos.south().up(), POISE_CLUSTER);
		this.setBlockIfReplacable(world, pos.west().up(), POISE_CLUSTER);
		
		this.setBlockIfReplacable(world, pos.north(2), POISE_CLUSTER);
		this.setBlockIfReplacable(world, pos.east(2), POISE_CLUSTER);
		this.setBlockIfReplacable(world, pos.south(2), POISE_CLUSTER);
		this.setBlockIfReplacable(world, pos.west(2), POISE_CLUSTER);
		
		this.setBlockIfReplacable(world, pos.north().east(), POISE_CLUSTER);
		this.setBlockIfReplacable(world, pos.north().west(), POISE_CLUSTER);
		this.setBlockIfReplacable(world, pos.south().east(), POISE_CLUSTER);
		this.setBlockIfReplacable(world, pos.south().west(), POISE_CLUSTER);
		
		if(variation <= 2) {
			this.setBlockIfReplacable(world, pos.up().up(), POISE_CLUSTER);
		} else if(variation >= 3 && variation <= 7) {
			if(rand.nextInt(2) == 1) this.setBlockIfReplacable(world, pos.north().west().up(), POISE_CLUSTER);
			if(rand.nextInt(2) == 1) this.setBlockIfReplacable(world, pos.north().east().up(), POISE_CLUSTER);
			if(rand.nextInt(2) == 1) this.setBlockIfReplacable(world, pos.south().west().up(), POISE_CLUSTER);
			if(rand.nextInt(2) == 1) this.setBlockIfReplacable(world, pos.south().east().up(), POISE_CLUSTER);
			
			if(rand.nextInt(2) == 1) this.setBlockIfReplacable(world, pos.north(2).west(), POISE_CLUSTER);
			if(rand.nextInt(2) == 1) this.setBlockIfReplacable(world, pos.north(2).east(), POISE_CLUSTER);
			if(rand.nextInt(2) == 1) this.setBlockIfReplacable(world, pos.south(2).west(), POISE_CLUSTER);
			if(rand.nextInt(2) == 1) this.setBlockIfReplacable(world, pos.west(2).east(), POISE_CLUSTER);
		} else if(variation >= 8 && variation <= 10) {
			int i = rand.nextInt(4);
			if(i == 0) {
				this.setBlockIfReplacable(world, pos.north().up().east(), POISE_CLUSTER);
				this.setBlockIfReplacable(world, pos.north().east(2), POISE_CLUSTER);
				this.setBlockIfReplacable(world, pos.north(2).east(), POISE_CLUSTER);
				this.setBlockIfReplacable(world, pos.north(2).east(2), POISE_CLUSTER);
			} else if(i == 1) {
				this.setBlockIfReplacable(world, pos.east().up().south(), POISE_CLUSTER);
				this.setBlockIfReplacable(world, pos.east().south(2), POISE_CLUSTER);
				this.setBlockIfReplacable(world, pos.east(2).south(), POISE_CLUSTER);
				this.setBlockIfReplacable(world, pos.east(2).south(2), POISE_CLUSTER);
			} else if(i == 2) {
				this.setBlockIfReplacable(world, pos.south().up().west(), POISE_CLUSTER);
				this.setBlockIfReplacable(world, pos.south().west(2), POISE_CLUSTER);
				this.setBlockIfReplacable(world, pos.south(2).west(), POISE_CLUSTER);
				this.setBlockIfReplacable(world, pos.south(2).west(2), POISE_CLUSTER);
			} else if(i == 3) {
				this.setBlockIfReplacable(world, pos.west().up().north(), POISE_CLUSTER);
				this.setBlockIfReplacable(world, pos.west().north(2), POISE_CLUSTER);
				this.setBlockIfReplacable(world, pos.west(2).north(), POISE_CLUSTER);
				this.setBlockIfReplacable(world, pos.west(2).north(2), POISE_CLUSTER);
			}
		}
	}
	
	private void setBlockIfReplacable(IWorld world, BlockPos pos, BlockState newState) {
		if(world.getBlockState(pos).getMaterial().isReplaceable()) {
			if(world.getBlockState(pos.up()).getBlock() != EEBlocks.POISE_BUSH_TALL.get() && world.getBlockState(pos.down()).getBlock() != EEBlocks.POISE_BUSH_TALL.get()) {
				world.setBlockState(pos, newState, 2);
			}
		}
	}

}
