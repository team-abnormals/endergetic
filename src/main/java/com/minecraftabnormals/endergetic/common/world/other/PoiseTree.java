package com.minecraftabnormals.endergetic.common.world.other;

import java.util.Random;

import javax.annotation.Nullable;

import com.minecraftabnormals.endergetic.common.world.features.PoiseTreeFeature;

import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public final class PoiseTree extends EndergeticTree {

	@Override
	@Nullable
	protected Feature<NoneFeatureConfiguration> getTreeFeature(Random random) {
		return new PoiseTreeFeature(NoneFeatureConfiguration.CODEC);
	}

}
