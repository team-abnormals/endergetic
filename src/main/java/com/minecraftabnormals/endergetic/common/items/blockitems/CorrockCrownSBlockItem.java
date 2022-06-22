package com.minecraftabnormals.endergetic.common.items.blockitems;

import java.util.Map;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.world.IWorldReader;

public class CorrockCrownSBlockItem extends BlockItem {
	protected final Supplier<Block> wallBlock;

	public CorrockCrownSBlockItem(Block floorBlock, Supplier<Block> wallBlockIn, Item.Properties p_i48462_3_) {
		super(floorBlock, p_i48462_3_);
		this.wallBlock = wallBlockIn;
	}

	@Nullable
	protected BlockState getPlacementState(BlockItemUseContext context) {
		BlockState iblockstate = this.wallBlock.get().getStateForPlacement(context);
		BlockState iblockstate1 = null;
		IWorldReader iworldreaderbase = context.getLevel();
		BlockPos blockpos = context.getClickedPos();

		for (Direction enumfacing : context.getNearestLookingDirections()) {
			BlockState iblockstate2 = enumfacing == Direction.UP || enumfacing == Direction.DOWN ? this.getBlock().getStateForPlacement(context) : iblockstate;
			if (iblockstate2 != null && iblockstate2.canSurvive(iworldreaderbase, blockpos)) {
				iblockstate1 = iblockstate2;
				break;
			}
		}

		return iblockstate1 != null && iworldreaderbase.isUnobstructed(iblockstate1, blockpos, ISelectionContext.empty()) ? iblockstate1 : null;
	}

	@Override
	public void registerBlocks(Map<Block, Item> blockToItemMap, Item itemIn) {
		super.registerBlocks(blockToItemMap, itemIn);
		blockToItemMap.put(this.wallBlock.get(), itemIn);
	}
}