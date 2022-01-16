package com.minecraftabnormals.endergetic.common.world.other;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Random;

public abstract class EndergeticTree {
	@Nullable
	protected abstract Feature<NoFeatureConfig> getTreeFeature(Random random);

	public boolean spawn(ServerWorld world, ChunkGenerator chunkGenerator, BlockPos pos, BlockState blockUnder, Random random) {
		Feature<NoFeatureConfig> treefeature = this.getTreeFeature(random);
		if (treefeature == null) {
			return false;
		} else {
			if (treefeature.place(world, chunkGenerator, random, pos, IFeatureConfig.NONE)) {
				return true;
			} else {
				return false;
			}
		}
	}
}