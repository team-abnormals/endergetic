package com.minecraftabnormals.endergetic.common.blocks.poise;

import com.minecraftabnormals.endergetic.core.registry.EEBlocks;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.tags.FluidTags;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.lighting.LayerLightEngine;
import net.minecraft.server.level.ServerLevel;

@SuppressWarnings("deprecation")
public class PoismossEumusBlock extends Block implements BonemealableBlock {

	public PoismossEumusBlock(Properties properties) {
		super(properties);
	}

	@Override
	public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource random) {
		if (!worldIn.isClientSide) {
			if (!worldIn.isAreaLoaded(pos, 3)) return;

			if (!isLightCoveringPos(state, worldIn, pos)) {
				worldIn.setBlockAndUpdate(pos, EEBlocks.EUMUS.get().defaultBlockState());
			} else {
				if (worldIn.getMaxLocalRawBrightness(pos.above()) >= 9) {
					BlockState blockstate = this.defaultBlockState();

					for (int i = 0; i < 4; ++i) {
						BlockPos blockpos = pos.offset(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
						if (worldIn.getBlockState(blockpos).getBlock() == EEBlocks.EUMUS.get() && shouldGrowOnto(blockstate, worldIn, blockpos)) {
							worldIn.setBlockAndUpdate(blockpos, EEBlocks.EUMUS_POISMOSS.get().defaultBlockState());
						}
					}
				}
			}
		}
	}

	private static boolean isLightCoveringPos(BlockState p_220257_0_, LevelReader p_220257_1_, BlockPos p_220257_2_) {
		BlockPos blockpos = p_220257_2_.above();
		BlockState blockstate = p_220257_1_.getBlockState(blockpos);
		int i = LayerLightEngine.getLightBlockInto(p_220257_1_, p_220257_0_, p_220257_2_, blockstate, blockpos, Direction.UP, blockstate.getLightBlock(p_220257_1_, blockpos));
		return i < p_220257_1_.getMaxLightLevel();
	}

	private static boolean shouldGrowOnto(BlockState p_220256_0_, LevelReader p_220256_1_, BlockPos p_220256_2_) {
		BlockPos blockpos = p_220256_2_.above();
		return isLightCoveringPos(p_220256_0_, p_220256_1_, p_220256_2_) && !p_220256_1_.getFluidState(blockpos).is(FluidTags.WATER);
	}

	@Override
	public boolean isValidBonemealTarget(BlockGetter worldIn, BlockPos pos, BlockState state, boolean isClient) {
		return worldIn.getBlockState(pos.above()).isAir();
	}

	@Override
	public boolean isBonemealSuccess(Level p_220878_, RandomSource p_220879_, BlockPos p_220880_, BlockState p_220881_) {
		return true;
	}

	@Override
	public void performBonemeal(ServerLevel level, RandomSource rand, BlockPos pos, BlockState state) {
		BlockPos blockpos = pos.above();
		BlockState blockstate = EEBlocks.POISE_BUSH.get().defaultBlockState();

		for (int i = 0; i < 128; ++i) {
			BlockPos blockpos1 = blockpos;
			int j = 0;

			while (true) {
				if (j >= i / 16) {
					BlockState blockstate2 = level.getBlockState(blockpos1);
					if (blockstate2.getBlock() == blockstate.getBlock() && rand.nextInt(10) == 0) {
						((BonemealableBlock) blockstate.getBlock()).performBonemeal(level, rand, blockpos1, blockstate2);
					}

					if (!blockstate2.isAir()) {
						break;
					}

					BlockState blockstate1;
					blockstate1 = blockstate;

					if (blockstate1.canSurvive(level, blockpos1)) {
						level.setBlock(blockpos1, blockstate1, 3);
					}
					break;
				}

				blockpos1 = blockpos1.offset(rand.nextInt(3) - 1, (rand.nextInt(3) - 1) * rand.nextInt(3) / 2, rand.nextInt(3) - 1);
				if (level.getBlockState(blockpos1.below()).getBlock() != this || level.getBlockState(blockpos1).isSolidRender(level, blockpos1)) {
					break;
				}

				j++;
			}
		}
	}

}