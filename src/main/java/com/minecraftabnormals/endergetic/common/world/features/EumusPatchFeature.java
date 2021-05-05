package com.minecraftabnormals.endergetic.common.world.features;

import com.google.common.collect.Sets;
import com.minecraftabnormals.endergetic.common.world.configs.EndergeticPatchConfig;
import com.minecraftabnormals.endergetic.common.world.configs.MultiPatchConfig;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;
import java.util.Set;

public class EumusPatchFeature extends Feature<MultiPatchConfig> {
	private static final Set<Block> TRANSFORMABLE_BLOCKS = Sets.newHashSet(Blocks.END_STONE, EEBlocks.CORROCK_END_BLOCK.get(), EEBlocks.SPECKLED_END_CORROCK.get());
	private static final BlockState EUMUS = EEBlocks.EUMUS.get().getDefaultState();

	public EumusPatchFeature(Codec<MultiPatchConfig> codec) {
		super(codec);
	}

	@Override
	public boolean generate(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, MultiPatchConfig config) {
		BlockPos down = EndergeticPatchConfig.getPos(world, pos, false).down();
		Block downBlock = world.getBlockState(down).getBlock();
		if (downBlock == EEBlocks.CORROCK_END_BLOCK.get()) {
			int extraPatches = 1 + rand.nextInt(config.getMaxExtraPatches() + 1);
			int maxExtraRadius = config.getMaxExtraRadius();
			generatePatch(world, down, rand, maxExtraRadius);
			for (int i = 0; i < extraPatches; i++) {
				generatePatch(world, down.add(rand.nextInt(2) - rand.nextInt(2) , 0, rand.nextInt(2) - rand.nextInt(2)), rand, maxExtraRadius);
			}
			return true;
		}
		return false;
	}

	private static void generatePatch(ISeedReader world, BlockPos origin, Random rand, int maxExtraRadius) {
		int radius = 1 + rand.nextInt(maxExtraRadius);
		int radiusSquared = radius * radius;
		int originX = origin.getX();
		int originY = origin.getY();
		int originZ = origin.getZ();
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		for (int x = -radius; x <= radius; x++) {
			for (int z = -radius; z <= radius; z++) {
				int distanceSq = x * x + z * z - rand.nextInt(2);
				if (distanceSq <= radiusSquared) {
					BlockPos pos = world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, mutable.setPos(originX + x, originY, originZ + z)).down();
					int distanceY = Math.abs(originY - pos.getY());
					if (distanceY <= 1) {
						if (TRANSFORMABLE_BLOCKS.contains(world.getBlockState(pos).getBlock())) {
							world.setBlockState(pos, EUMUS, 2);
						}
					}
				}
			}
		}
	}
}
