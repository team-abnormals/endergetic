package com.teamabnormals.endergetic.common.levelgen.feature;

import com.mojang.serialization.Codec;
import com.teamabnormals.endergetic.common.block.EetleEggBlock;
import com.teamabnormals.endergetic.common.levelgen.configs.EndergeticPatchConfig;
import com.teamabnormals.endergetic.core.registry.EEBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class EetleEggPatchFeature extends Feature<EndergeticPatchConfig> {
	private static final Direction[] DIRECTIONS = Direction.values();
	private static final BlockState BASE_STATE = EEBlocks.EETLE_EGG.get().defaultBlockState();
	private static final BlockState INFESTED_STATE = EEBlocks.INFESTED_CORROCK.get().defaultBlockState();

	public EetleEggPatchFeature(Codec<EndergeticPatchConfig> codec) {
		super(codec);
	}

	@Override
	public boolean place(FeaturePlaceContext<EndergeticPatchConfig> context) {
		int i = 0;
		WorldGenLevel world = context.level();
		EndergeticPatchConfig config = context.config();
		BlockPos pos = EndergeticPatchConfig.getPos(world, context.origin(), config.shouldSearchDown());
		RandomSource rand = context.random();
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

	private static void spreadInfestedCorrockAtPos(WorldGenLevel level, BlockPos pos, RandomSource random, Block corrockBlock) {
		int radius = 1;
		level.setBlock(pos, INFESTED_STATE, 2);
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
		for (int x = -radius; x <= radius; x++) {
			for (int y = -radius; y <= radius; y++) {
				for (int z = -radius; z <= radius; z++) {
					if (level.getBlockState(mutable.setWithOffset(pos, x, y, z)).getBlock() == corrockBlock && random.nextFloat() <= 0.25F) {
						level.setBlock(mutable, INFESTED_STATE, 2);
					}
				}
			}
		}
	}
}
