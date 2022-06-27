package com.minecraftabnormals.endergetic.common.world.features.corrock;

import com.mojang.serialization.Codec;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;

public class CorrockGroundPatchFeature extends Feature<DiskConfiguration> {

	public CorrockGroundPatchFeature(Codec<DiskConfiguration> config) {
		super(config);
	}

	public boolean place(FeaturePlaceContext<DiskConfiguration> context) {
		int i = 0;
		RandomSource rand = context.random();
		DiskConfiguration config = context.config();
		int radius = rand.nextInt(config.radius().sample(rand));
		BlockPos pos = context.origin();
		int halfHeight = config.halfHeight();
		WorldGenLevel level = context.level();
		for (int x = pos.getX() - radius; x <= pos.getX() + radius; x++) {
			for (int z = pos.getZ() - radius; z <= pos.getZ() + radius; z++) {
				int radiusXDistance = x - pos.getX();
				int radiusZDistance = z - pos.getZ();
				int distance = radiusXDistance * radiusXDistance + radiusZDistance * radiusZDistance;
				if (distance <= radius * radius) {
					for (int y = pos.getY() - halfHeight; y <= pos.getY() + halfHeight; y++) {
						BlockPos blockpos = new BlockPos(x, y, z);
						if (config.target().test(level, blockpos) && (distance != radius * radius || rand.nextFloat() < 0.5F)) {
							level.setBlock(blockpos, config.stateProvider().getState(level, rand, blockpos), 2);
							i++;
						}
					}
				}
			}
		}
		return i > 0;
	}

}