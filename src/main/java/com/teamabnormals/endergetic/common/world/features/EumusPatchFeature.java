package com.teamabnormals.endergetic.common.world.features;

import com.google.common.collect.Sets;
import com.teamabnormals.endergetic.common.world.configs.EndergeticPatchConfig;
import com.teamabnormals.endergetic.common.world.configs.MultiPatchConfig;
import com.teamabnormals.endergetic.core.registry.EEBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.Set;

public class EumusPatchFeature extends Feature<MultiPatchConfig> {
	private static final Set<Block> TRANSFORMABLE_BLOCKS = Sets.newHashSet(Blocks.END_STONE, EEBlocks.CORROCK_END_BLOCK.get(), EEBlocks.SPECKLED_END_CORROCK.get());
	private static final BlockState EUMUS = EEBlocks.EUMUS.get().defaultBlockState();

	public EumusPatchFeature(Codec<MultiPatchConfig> codec) {
		super(codec);
	}

	@Override
	public boolean place(FeaturePlaceContext<MultiPatchConfig> context) {
		WorldGenLevel level = context.level();
		BlockPos down = EndergeticPatchConfig.getPos(level, context.origin(), false).below();
		Block downBlock = level.getBlockState(down).getBlock();
		if (downBlock == EEBlocks.CORROCK_END_BLOCK.get()) {
			RandomSource rand = context.random();
			MultiPatchConfig config = context.config();
			int extraPatches = 1 + rand.nextInt(config.getMaxExtraPatches() + 1);
			int maxExtraRadius = config.getMaxExtraRadius();
			generatePatch(level, down, rand, maxExtraRadius);
			for (int i = 0; i < extraPatches; i++) {
				generatePatch(level, down.offset(rand.nextInt(2) - rand.nextInt(2) , 0, rand.nextInt(2) - rand.nextInt(2)), rand, maxExtraRadius);
			}
			return true;
		}
		return false;
	}

	private static void generatePatch(WorldGenLevel world, BlockPos origin, RandomSource rand, int maxExtraRadius) {
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
