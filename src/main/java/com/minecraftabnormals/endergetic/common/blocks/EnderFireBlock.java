package com.minecraftabnormals.endergetic.common.blocks;

import com.minecraftabnormals.endergetic.core.registry.other.EETags;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;

public class EnderFireBlock extends AbstractFireBlock {

	public EnderFireBlock(Properties builder) {
		super(builder, 3.0F);
	}

	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		return this.canSurvive(stateIn, worldIn, currentPos) ? this.defaultBlockState() : Blocks.AIR.defaultBlockState();
	}

	public boolean canSurvive(BlockState state, IWorldReader worldIn, BlockPos pos) {
		return isEnderFireBase(worldIn.getBlockState(pos.below()).getBlock());
	}

	public static boolean isEnderFireBase(Block block) {
		return block.is(EETags.Blocks.ENDER_FIRE_BASE_BLOCKS);
	}

	protected boolean canBurn(BlockState stateIn) {
		return true;
	}

}