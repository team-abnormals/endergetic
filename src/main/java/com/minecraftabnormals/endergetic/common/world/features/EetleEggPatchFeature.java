package com.minecraftabnormals.endergetic.common.world.features;

import com.minecraftabnormals.endergetic.common.blocks.EetleEggBlock;
import com.minecraftabnormals.endergetic.common.world.configs.EndergeticPatchConfig;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;

import java.util.Random;

public class EetleEggPatchFeature extends Feature<EndergeticPatchConfig> {
	private static final Direction[] DIRECTIONS = Direction.values();
	private static final BlockState BASE_STATE = EEBlocks.EETLE_EGG.get().defaultBlockState();
	private static final BlockState INFESTED_STATE = EEBlocks.INFESTED_CORROCK.get().defaultBlockState();

	public EetleEggPatchFeature(Codec<EndergeticPatchConfig> codec) {
		super(codec);
	}

	@Override
	public boolean place(WorldGenLevel world, ChunkGenerator generator, Random rand, BlockPos pos, EndergeticPatchConfig config) {
		int i = 0;
		pos = EndergeticPatchConfig.getPos(world, pos, config.shouldSearchDown());
		EetleEggBlock.shuffleDirections(DIRECTIONS, rand);
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
		float chance = config.getFrequency();
		Block corrockBlock = EEBlocks.CORROCK_END_BLOCK.get();
		Block eumus = EEBlocks.EUMUS.get();
		for (int j = 0; j < 45; j++) {
			mutable.setWithOffset(pos, rand.nextInt(9) - rand.nextInt(9), rand.nextInt(9) - rand.nextInt(9), rand.nextInt(9) - rand.nextInt(9));
			if (world.isEmptyBlock(mutable) && rand.nextFloat() < chance) {
				for (Direction direction : DIRECTIONS) {
					BlockPos offsetPos = mutable.relative(direction);
					Block offsetBlock = world.getBlockState(offsetPos).getBlock();
					if (offsetBlock == corrockBlock || offsetBlock == eumus) {
						BlockState state = BASE_STATE.setValue(EetleEggBlock.FACING, direction.getOpposite());
						if (state.canSurvive(world, mutable)) {
							world.setBlock(mutable, state.setValue(EetleEggBlock.SIZE, rand.nextFloat() < 0.75F ? 0 : rand.nextFloat() < 0.6F ? 1 : 2), 2);
							spreadInfestedCorrockAtPos(world, offsetPos, rand, corrockBlock);
							i++;
							break;
						}
					}
				}
			}
		}
		return i > 0;
	}

	private static void spreadInfestedCorrockAtPos(WorldGenLevel world, BlockPos pos, Random random, Block corrockBlock) {
		int radius = 1;
		world.setBlock(pos, INFESTED_STATE, 2);
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
		for (int x = -radius; x <= radius; x++) {
			for (int y = -radius; y <= radius; y++) {
				for (int z = -radius; z <= radius; z++) {
					if (world.getBlockState(mutable.setWithOffset(pos, x, y, z)).getBlock() == corrockBlock && random.nextFloat() <= 0.25F) {
						world.setBlock(mutable, INFESTED_STATE, 2);
					}
				}
			}
		}
	}
}
