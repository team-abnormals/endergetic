package com.minecraftabnormals.endergetic.common.world.features.corrock;

import java.util.Random;

import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.mojang.serialization.Codec;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;

public class CorrockGroundPatchFeature extends Feature<DiskConfiguration> {

	public CorrockGroundPatchFeature(Codec<DiskConfiguration> config) {
		super(config);
	}

	public boolean place(WorldGenLevel world, ChunkGenerator generator, Random rand, BlockPos pos, DiskConfiguration config) {
		int i = 0;
		int radius = rand.nextInt(config.radius.sample(rand));

		if (config.targets.contains(Blocks.END_STONE.defaultBlockState())) {
			if (config.state == EEBlocks.EUMUS.get().defaultBlockState() && rand.nextFloat() < 0.75F) {
				return false;
			} else if (config.state != EEBlocks.EUMUS.get().defaultBlockState() && rand.nextFloat() < 0.5F) {
				return false;
			}
		} else if (config.targets.contains(EEBlocks.CORROCK_END_BLOCK.get().defaultBlockState()) && rand.nextFloat() < 0.75F) {
			return false;
		}

		for (int x = pos.getX() - radius; x <= pos.getX() + radius; x++) {
			for (int z = pos.getZ() - radius; z <= pos.getZ() + radius; z++) {
				int radiusXDistance = x - pos.getX();
				int radiusZDistance = z - pos.getZ();
				int distance = radiusXDistance * radiusXDistance + radiusZDistance * radiusZDistance;
				if (distance <= radius * radius) {
					for (int y = pos.getY() - config.halfHeight; y <= pos.getY() + config.halfHeight; y++) {
						BlockPos blockpos = new BlockPos(x, y, z);
						BlockState blockstate = world.getBlockState(blockpos);

						for (BlockState blockstate1 : config.targets) {
							if (blockstate1.getBlock() == blockstate.getBlock() && (distance != radius * radius || rand.nextFloat() < 0.5F)) {
								world.setBlock(blockpos, config.state, 2);
								i++;
								break;
							}
						}
					}
				}
			}
		}
		return i > 0;
	}

}