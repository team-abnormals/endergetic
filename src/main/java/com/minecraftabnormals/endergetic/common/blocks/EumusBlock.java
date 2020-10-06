package com.minecraftabnormals.endergetic.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraftforge.common.ToolType;

public class EumusBlock extends Block {

	public EumusBlock(Properties properties) {
		super(properties);
	}

	@Override
	public ToolType getHarvestTool(BlockState state) {
		return ToolType.SHOVEL;
	}

}