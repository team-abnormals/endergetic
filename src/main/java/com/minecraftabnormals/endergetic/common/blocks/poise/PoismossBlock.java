package com.minecraftabnormals.endergetic.common.blocks.poise;

import java.util.Random;

import com.minecraftabnormals.endergetic.core.registry.EEBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.lighting.LightEngine;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;

import net.minecraft.block.AbstractBlock.Properties;

public class PoismossBlock extends Block implements IGrowable {

	public PoismossBlock(Properties properties) {
		super(properties);
	}

	@Override
	public ToolType getHarvestTool(BlockState state) {
		return ToolType.PICKAXE;
	}

	@Override
	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
		if (!worldIn.isClientSide) {
			if (!worldIn.isAreaLoaded(pos, 3)) return;
			if (!canBeGrass(state, worldIn, pos)) {
				worldIn.setBlockAndUpdate(pos, Blocks.END_STONE.defaultBlockState());
			}
		}
	}

	private static boolean canBeGrass(BlockState p_220257_0_, IWorldReader p_220257_1_, BlockPos p_220257_2_) {
		BlockPos blockpos = p_220257_2_.above();
		BlockState blockstate = p_220257_1_.getBlockState(blockpos);
		int i = LightEngine.getLightBlockInto(p_220257_1_, p_220257_0_, p_220257_2_, blockstate, blockpos, Direction.UP, blockstate.getLightBlock(p_220257_1_, blockpos));
		return i < p_220257_1_.getMaxLightLevel();
	}

	@SuppressWarnings("deprecation")
	public boolean isValidBonemealTarget(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient) {
		return worldIn.getBlockState(pos.above()).isAir();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void performBonemeal(ServerWorld worldIn, Random rand, BlockPos pos, BlockState state) {
		BlockPos blockpos = pos.above();
		BlockState blockstate = EEBlocks.POISE_BUSH.get().defaultBlockState();

		for (int i = 0; i < 128; ++i) {
			BlockPos blockpos1 = blockpos;
			int j = 0;

			while (true) {
				if (j >= i / 16) {
					BlockState blockstate2 = worldIn.getBlockState(blockpos1);
					if (blockstate2.getBlock() == blockstate.getBlock() && rand.nextInt(10) == 0) {
						((IGrowable) blockstate.getBlock()).performBonemeal(worldIn, rand, blockpos1, blockstate2);
					}

					if (!blockstate2.isAir()) {
						break;
					}

					BlockState blockstate1;
					blockstate1 = blockstate;

					if (blockstate1.canSurvive(worldIn, blockpos1)) {
						worldIn.setBlock(blockpos1, blockstate1, 3);
					}
					break;
				}

				blockpos1 = blockpos1.offset(rand.nextInt(3) - 1, (rand.nextInt(3) - 1) * rand.nextInt(3) / 2, rand.nextInt(3) - 1);
				if (worldIn.getBlockState(blockpos1.below()).getBlock() != this || worldIn.getBlockState(blockpos1).isSolidRender(worldIn, blockpos1)) {
					break;
				}

				j++;
			}
		}
	}

	public boolean isBonemealSuccess(World worldIn, Random rand, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public int getHarvestLevel(BlockState state) {
		return 2;
	}

	@Override
	public SoundType getSoundType(BlockState p_220072_1_) {
		return SoundType.STONE;
	}

}