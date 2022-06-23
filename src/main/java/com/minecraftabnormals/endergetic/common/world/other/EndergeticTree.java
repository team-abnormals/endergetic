package com.minecraftabnormals.endergetic.common.world.other;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.server.level.ServerLevel;

public abstract class EndergeticTree {
	@Nullable
	protected abstract Feature<NoneFeatureConfiguration> getTreeFeature(Random random);

	public boolean spawn(ServerLevel world, ChunkGenerator chunkGenerator, BlockPos pos, BlockState blockUnder, Random random) {
		Feature<NoneFeatureConfiguration> treefeature = this.getTreeFeature(random);
		if (treefeature == null) {
			return false;
		} else {
			if (treefeature.place(world, chunkGenerator, random, pos, FeatureConfiguration.NONE)) {
				return true;
			} else {
				return false;
			}
		}
	}
}