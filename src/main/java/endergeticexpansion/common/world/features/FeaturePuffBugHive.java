package endergeticexpansion.common.world.features;

import java.util.Random;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import endergeticexpansion.core.registry.EEBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class FeaturePuffBugHive extends Feature<NoFeatureConfig> {
	private BlockState HIVE_STATE(boolean hanger) {
		return hanger ? EEBlocks.HIVE_HANGER.getDefaultState() : EEBlocks.PUFFBUG_HIVE.getDefaultState();
	}

	public FeaturePuffBugHive(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactoryIn) {
		super(configFactoryIn);
	}
	
	@Override
	public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config) {
		if(world.getBlockState(pos.up()).getBlock() == EEBlocks.POISE_LOG || world.getBlockState(pos.up()).getBlock() == EEBlocks.POISE_LOG_GLOWING) {
			if(world.getBlockState(pos).getMaterial().isReplaceable() && world.getBlockState(pos).getMaterial().isReplaceable()) {
				world.setBlockState(pos, this.HIVE_STATE(true), 2);
				world.setBlockState(pos.down(), this.HIVE_STATE(false), 2);
				return true;
			}
		} else {
			if(world.getBlockState(pos.up()).getBlock() == EEBlocks.POISE_CLUSTER && world.getHeight() > 90) {
				if(world.getBlockState(pos).getMaterial().isReplaceable() && world.getBlockState(pos).getMaterial().isReplaceable()) {
					world.setBlockState(pos, this.HIVE_STATE(true), 2);
					world.setBlockState(pos.down(), this.HIVE_STATE(false), 2);
					return true;
				}
			}
		}
		return false;
	}

}
