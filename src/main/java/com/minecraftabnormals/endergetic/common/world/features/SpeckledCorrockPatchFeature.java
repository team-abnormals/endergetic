package com.minecraftabnormals.endergetic.common.world.features;

import com.google.common.collect.Sets;
import com.minecraftabnormals.endergetic.common.world.configs.EndergeticPatchConfig;
import com.minecraftabnormals.endergetic.common.world.configs.MultiPatchConfig;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class SpeckledCorrockPatchFeature extends Feature<MultiPatchConfig> {
	private static final Block CORROCK_BLOCK = EEBlocks.CORROCK_END_BLOCK.get();
	private static final Set<Block> TRANSFORMABLE_BLOCKS = Sets.newHashSet(Blocks.END_STONE, CORROCK_BLOCK, EEBlocks.SPECKLED_END_CORROCK.get(), EEBlocks.EUMUS.get());
	private static final BlockState END_STONE = Blocks.END_STONE.getDefaultState();
	private static final BlockState SPECKLED_CORROCK = EEBlocks.SPECKLED_END_CORROCK.get().getDefaultState();

	public SpeckledCorrockPatchFeature(Codec<MultiPatchConfig> codec) {
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
				generatePatch(world, down.add(rand.nextInt(3) - rand.nextInt(3) , 0, rand.nextInt(3) - rand.nextInt(3)), rand, maxExtraRadius);
			}
			return true;
		}
		return false;
	}

	private static void generatePatch(ISeedReader world, BlockPos origin, Random rand, int maxExtraRadius) {
		int radius = 2 + rand.nextInt(maxExtraRadius);
		int radiusSquared = radius * radius;
		int originX = origin.getX();
		int originY = origin.getY();
		int originZ = origin.getZ();
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		List<BlockPos> positions = new ArrayList<>();
		for (int x = -radius; x <= radius; x++) {
			for (int z = -radius; z <= radius; z++) {
				int distanceSq = x * x + z * z - rand.nextInt(2);
				if (distanceSq <= radiusSquared) {
					BlockPos pos = world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, mutable.setPos(originX + x, originY, originZ + z)).down();
					int distanceY = Math.abs(originY - pos.getY());
					if (distanceY <= 1) {
						if (TRANSFORMABLE_BLOCKS.contains(world.getBlockState(pos).getBlock())) {
							positions.add(pos);
							world.setBlockState(pos, END_STONE, 2);
						}
					}
				}
			}
		}
		positions.forEach(pos -> {
			int neighborCorrocks = 0;
			for (Direction horizontal : Direction.Plane.HORIZONTAL) {
				BlockPos offset = pos.offset(horizontal);
				if (world.getBlockState(offset).getBlock() == CORROCK_BLOCK) {
					neighborCorrocks++;
				}
			}
			if (neighborCorrocks > 0 && rand.nextFloat() >= neighborCorrocks * 0.25F) {
				world.setBlockState(pos, SPECKLED_CORROCK, 2);
			}
		});
	}
}
