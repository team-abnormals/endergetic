package endergeticexpansion.common.world.other;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public abstract class EndergeticTree {
	@Nullable
	protected abstract Feature<NoFeatureConfig> getTreeFeature(Random random);

	public boolean spawn(IWorld worldIn, BlockPos pos, BlockState blockUnder, Random random) {
		Feature<NoFeatureConfig> treefeature = this.getTreeFeature(random);
		if (treefeature == null) {
			return false;
		} else {
			if (treefeature.place(worldIn, worldIn.getChunkProvider().getChunkGenerator(), random, pos, IFeatureConfig.NO_FEATURE_CONFIG)) {
				return true;
			} else {
				return false;
			}
		}
	}
}
