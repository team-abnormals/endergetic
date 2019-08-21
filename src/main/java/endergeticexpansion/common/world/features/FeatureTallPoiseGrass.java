package endergeticexpansion.common.world.features;

import java.util.Random;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import endergeticexpansion.common.blocks.poise.BlockPoiseGrassPlantTall;
import endergeticexpansion.core.registry.EEBlocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.DoublePlantConfig;
import net.minecraft.world.gen.feature.Feature;

/**
 * @author - SmellyModder(Luke Tonon)
 */
public class FeatureTallPoiseGrass extends Feature<DoublePlantConfig> {
	
	public FeatureTallPoiseGrass(Function<Dynamic<?>, ? extends DoublePlantConfig> config) {
		super(config);
	}

	public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, DoublePlantConfig config) {
		boolean flag = false;

		for(int i = 0; i < 64; ++i) {
			BlockPos blockpos = pos.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));
			if (!isNearBolloomBud(worldIn, blockpos) && worldIn.isAirBlock(blockpos) && blockpos.getY() < worldIn.getWorld().getDimension().getHeight() - 2 && config.state.isValidPosition(worldIn, blockpos)) {
				((BlockPoiseGrassPlantTall)config.state.getBlock()).placeAt(worldIn, blockpos, 2);
				flag = true;
			}
		}

		return flag;
	}
	
	protected boolean isNearBolloomBud(IWorld world, BlockPos pos) {
		if(world.getBlockState(pos.north()).getBlock() == EEBlocks.BOLLOOM_BUD || world.getBlockState(pos.east()).getBlock() == EEBlocks.BOLLOOM_BUD || world.getBlockState(pos.south()).getBlock() == EEBlocks.BOLLOOM_BUD || world.getBlockState(pos.west()).getBlock() == EEBlocks.BOLLOOM_BUD
			|| world.getBlockState(pos.north().up()).getBlock() == EEBlocks.BOLLOOM_BUD || world.getBlockState(pos.east().up()).getBlock() == EEBlocks.BOLLOOM_BUD || world.getBlockState(pos.south().up()).getBlock() == EEBlocks.BOLLOOM_BUD || world.getBlockState(pos.west().up()).getBlock() == EEBlocks.BOLLOOM_BUD) {
			return true;
		}
		return false;
	}

}
