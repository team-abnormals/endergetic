package com.minecraftabnormals.endergetic.common.world.features;

import com.google.common.collect.Sets;
import com.minecraftabnormals.endergetic.common.world.configs.EndergeticPatchConfig;
import com.minecraftabnormals.endergetic.common.world.configs.MultiPatchConfig;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SpeckledCorrockPatchFeature extends Feature<MultiPatchConfig> {
	private static final Block CORROCK_BLOCK = EEBlocks.CORROCK_END_BLOCK.get();
	private static final Set<Block> TRANSFORMABLE_BLOCKS = Sets.newHashSet(Blocks.END_STONE, CORROCK_BLOCK, EEBlocks.SPECKLED_END_CORROCK.get(), EEBlocks.EUMUS.get());
	private static final BlockState END_STONE = Blocks.END_STONE.defaultBlockState();
	private static final BlockState SPECKLED_CORROCK = EEBlocks.SPECKLED_END_CORROCK.get().defaultBlockState();

	public SpeckledCorrockPatchFeature(Codec<MultiPatchConfig> codec) {
		super(codec);
	}

	@Override
	public boolean place(FeaturePlaceContext<MultiPatchConfig> context) {
		WorldGenLevel level = context.level();
		BlockPos pos = context.origin();
		BlockPos down = EndergeticPatchConfig.getPos(level, pos, false).below();
		Block downBlock = level.getBlockState(down).getBlock();
		if (downBlock == EEBlocks.CORROCK_END_BLOCK.get()) {
			RandomSource rand = context.random();
			MultiPatchConfig config = context.config();
			int extraPatches = 1 + rand.nextInt(config.getMaxExtraPatches() + 1);
			int maxExtraRadius = config.getMaxExtraRadius();
			generatePatch(level, down, rand, maxExtraRadius);
			for (int i = 0; i < extraPatches; i++) {
				generatePatch(level, down.offset(rand.nextInt(3) - rand.nextInt(3) , 0, rand.nextInt(3) - rand.nextInt(3)), rand, maxExtraRadius);
			}
			return true;
		}
		return false;
	}

	private static void generatePatch(WorldGenLevel level, BlockPos origin, RandomSource rand, int maxExtraRadius) {
		int radius = 2 + rand.nextInt(maxExtraRadius);
		int radiusSquared = radius * radius;
		int originX = origin.getX();
		int originY = origin.getY();
		int originZ = origin.getZ();
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
		List<BlockPos> positions = new ArrayList<>();
		for (int x = -radius; x <= radius; x++) {
			for (int z = -radius; z <= radius; z++) {
				int distanceSq = x * x + z * z - rand.nextInt(2);
				if (distanceSq <= radiusSquared) {
					BlockPos pos = level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, mutable.set(originX + x, originY, originZ + z)).below();
					int distanceY = Math.abs(originY - pos.getY());
					if (distanceY <= 1) {
						if (TRANSFORMABLE_BLOCKS.contains(level.getBlockState(pos).getBlock())) {
							positions.add(pos);
							level.setBlock(pos, END_STONE, 2);
						}
					}
				}
			}
		}
		positions.forEach(pos -> {
			int neighborCorrocks = 0;
			for (Direction horizontal : Direction.Plane.HORIZONTAL) {
				BlockPos offset = pos.relative(horizontal);
				if (level.getBlockState(offset).getBlock() == CORROCK_BLOCK) {
					neighborCorrocks++;
				}
			}
			if (neighborCorrocks > 0 && rand.nextFloat() >= neighborCorrocks * 0.25F) {
				level.setBlock(pos, SPECKLED_CORROCK, 2);
			}
		});
	}
}
