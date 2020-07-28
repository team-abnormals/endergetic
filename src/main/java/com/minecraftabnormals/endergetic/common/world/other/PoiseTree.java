package com.minecraftabnormals.endergetic.common.world.other;

import java.util.Random;

import javax.annotation.Nullable;

import com.minecraftabnormals.endergetic.common.world.features.PoiseTreeFeature;

import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class PoiseTree extends EndergeticTree {

	@Override
	@Nullable
	protected Feature<NoFeatureConfig> getTreeFeature(Random random) {
		return new PoiseTreeFeature(NoFeatureConfig.field_236558_a_);
	}

}
