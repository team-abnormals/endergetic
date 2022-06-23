package com.minecraftabnormals.endergetic.common.world.features;

import com.google.common.collect.Sets;
import com.minecraftabnormals.endergetic.common.world.configs.EndergeticPatchConfig;
import com.minecraftabnormals.endergetic.common.world.configs.MultiPatchConfig;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
	public boolean place(WorldGenLevel world, ChunkGenerator generator, Random rand, BlockPos pos, MultiPatchConfig config) {
		BlockPos down = EndergeticPatchConfig.getPos(world, pos, false).below();
		Block downBlock = world.getBlockState(down).getBlock();
		if (downBlock == EEBlocks.CORROCK_END_BLOCK.get()) {
			int extraPatches = 1 + rand.nextInt(config.getMaxExtraPatches() + 1);
			int maxExtraRadius = config.getMaxExtraRadius();
			generatePatch(world, down, rand, maxExtraRadius);
			for (int i = 0; i < extraPatches; i++) {
				generatePatch(world, down.offset(rand.nextInt(3) - rand.nextInt(3) , 0, rand.nextInt(3) - rand.nextInt(3)), rand, maxExtraRadius);
			}
			return true;
		}
		return false;
	}

	private static void generatePatch(WorldGenLevel world, BlockPos origin, Random rand, int maxExtraRadius) {
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
					BlockPos pos = world.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, mutable.set(originX + x, originY, originZ + z)).below();
					int distanceY = Math.abs(originY - pos.getY());
					if (distanceY <= 1) {
						if (TRANSFORMABLE_BLOCKS.contains(world.getBlockState(pos).getBlock())) {
							positions.add(pos);
							world.setBlock(pos, END_STONE, 2);
						}
					}
				}
			}
		}
		positions.forEach(pos -> {
			int neighborCorrocks = 0;
			for (Direction horizontal : Direction.Plane.HORIZONTAL) {
				BlockPos offset = pos.relative(horizontal);
				if (world.getBlockState(offset).getBlock() == CORROCK_BLOCK) {
					neighborCorrocks++;
				}
			}
			if (neighborCorrocks > 0 && rand.nextFloat() >= neighborCorrocks * 0.25F) {
				world.setBlock(pos, SPECKLED_CORROCK, 2);
			}
		});
	}
}
