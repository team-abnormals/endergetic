package com.minecraftabnormals.endergetic.common.world.features;

import com.google.common.collect.Sets;
import com.minecraftabnormals.endergetic.common.world.configs.EndergeticPatchConfig;
import com.minecraftabnormals.endergetic.common.world.configs.MultiPatchConfig;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;

import java.util.Random;
import java.util.Set;

public class EumusPatchFeature extends Feature<MultiPatchConfig> {
	private static final Set<Block> TRANSFORMABLE_BLOCKS = Sets.newHashSet(Blocks.END_STONE, EEBlocks.CORROCK_END_BLOCK.get(), EEBlocks.SPECKLED_END_CORROCK.get());
	private static final BlockState EUMUS = EEBlocks.EUMUS.get().defaultBlockState();

	public EumusPatchFeature(Codec<MultiPatchConfig> codec) {
		super(codec);
	}

	@Override
	public boolean place(WorldGenLevel world, ChunkGenerator generator, Random rand, BlockPos pos, MultiPatchConfig config) {
		BlockPos down = EndergeticPatchConfig.getPos(world, pos, false).below();
		Block downBlock = world.getBlockState(down).getBlock();
		if (downBlock == EEBlocks.CORROCK_END_BLOCK.get()) {
			int extraPatches = 1 + rand.nextInt(config.getMaxExtraPatches() + 1);
			int maxExtraRadius = config.getMaxExtraRadius();
			generatePatch(world, down, rand, maxExtraRadius);
			for (int i = 0; i < extraPatches; i++) {
				generatePatch(world, down.offset(rand.nextInt(2) - rand.nextInt(2) , 0, rand.nextInt(2) - rand.nextInt(2)), rand, maxExtraRadius);
			}
			return true;
		}
		return false;
	}

	private static void generatePatch(WorldGenLevel world, BlockPos origin, Random rand, int maxExtraRadius) {
		int radius = 1 + rand.nextInt(maxExtraRadius);
		int radiusSquared = radius * radius;
		int originX = origin.getX();
		int originY = origin.getY();
		int originZ = origin.getZ();
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
		for (int x = -radius; x <= radius; x++) {
			for (int z = -radius; z <= radius; z++) {
				int distanceSq = x * x + z * z - rand.nextInt(2);
				if (distanceSq <= radiusSquared) {
					BlockPos pos = world.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, mutable.set(originX + x, originY, originZ + z)).below();
					int distanceY = Math.abs(originY - pos.getY());
					if (distanceY <= 1) {
						if (TRANSFORMABLE_BLOCKS.contains(world.getBlockState(pos).getBlock())) {
							world.setBlock(pos, EUMUS, 2);
						}
					}
				}
			}
		}
	}
}
