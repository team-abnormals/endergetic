package com.minecraftabnormals.endergetic.common.blocks;

import com.minecraftabnormals.endergetic.core.registry.other.EETags;

import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class EnderFireBlock extends BaseFireBlock {

	public EnderFireBlock(Properties builder) {
		super(builder, 3.0F);
	}

	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
		return this.canSurvive(stateIn, worldIn, currentPos) ? this.defaultBlockState() : Blocks.AIR.defaultBlockState();
	}

	public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
		return isEnderFireBase(worldIn.getBlockState(pos.below()).getBlock());
	}

	public static boolean isEnderFireBase(Block block) {
		return block.is(EETags.Blocks.ENDER_FIRE_BASE_BLOCKS);
	}

	protected boolean canBurn(BlockState stateIn) {
		return true;
	}

}