package com.minecraftabnormals.endergetic.common.world.other;

import com.minecraftabnormals.endergetic.common.world.features.PoiseTreeFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import javax.annotation.Nullable;
import java.util.Random;

public final class PoiseTree extends EndergeticTree {

	@Override
	@Nullable
	protected Feature<NoFeatureConfig> getTreeFeature(Random random) {
		return new PoiseTreeFeature(NoFeatureConfig.CODEC);
	}

}
