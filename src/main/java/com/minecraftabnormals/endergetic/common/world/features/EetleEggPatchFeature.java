package com.minecraftabnormals.endergetic.common.world.features;

import com.minecraftabnormals.endergetic.common.blocks.EetleEggBlock;
import com.minecraftabnormals.endergetic.common.world.configs.EndergeticPatchConfig;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class EetleEggPatchFeature extends Feature<EndergeticPatchConfig> {
	private static final Direction[] DIRECTIONS = Direction.values();
	private static final BlockState BASE_STATE = EEBlocks.EETLE_EGG.get().getDefaultState();
	private static final BlockState INFESTED_STATE = EEBlocks.INFESTED_CORROCK.get().getDefaultState();

	public EetleEggPatchFeature(Codec<EndergeticPatchConfig> codec) {
		super(codec);
	}

	@Override
	public boolean generate(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, EndergeticPatchConfig config) {
		int i = 0;
		pos = EndergeticPatchConfig.getPos(world, pos, config.shouldSearchDown());
		EetleEggBlock.shuffleDirections(DIRECTIONS, rand);
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		float chance = config.getFrequency();
		Block corrockBlock = EEBlocks.CORROCK_END_BLOCK.get();
		Block eumus = EEBlocks.EUMUS.get();
		for (int j = 0; j < 45; j++) {
			mutable.setAndOffset(pos, rand.nextInt(9) - rand.nextInt(9), rand.nextInt(9) - rand.nextInt(9), rand.nextInt(9) - rand.nextInt(9));
			if (world.isAirBlock(mutable) && rand.nextFloat() < chance) {
				for (Direction direction : DIRECTIONS) {
					BlockPos offsetPos = mutable.offset(direction);
					Block offsetBlock = world.getBlockState(offsetPos).getBlock();
					if (offsetBlock == corrockBlock || offsetBlock == eumus) {
						BlockState state = BASE_STATE.with(EetleEggBlock.FACING, direction.getOpposite());
						if (state.isValidPosition(world, mutable)) {
							world.setBlockState(mutable, state.with(EetleEggBlock.SIZE, rand.nextFloat() < 0.75F ? 0 : rand.nextFloat() < 0.6F ? 1 : 2), 2);
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

	private static void spreadInfestedCorrockAtPos(ISeedReader world, BlockPos pos, Random random, Block corrockBlock) {
		int radius = 1;
		world.setBlockState(pos, INFESTED_STATE, 2);
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		for (int x = -radius; x <= radius; x++) {
			for (int y = -radius; y <= radius; y++) {
				for (int z = -radius; z <= radius; z++) {
					if (world.getBlockState(mutable.setAndOffset(pos, x, y, z)).getBlock() == corrockBlock && random.nextFloat() <= 0.25F) {
						world.setBlockState(mutable, INFESTED_STATE, 2);
					}
				}
			}
		}
	}
}
