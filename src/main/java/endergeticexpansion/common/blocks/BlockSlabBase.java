package endergeticexpansion.common.blocks;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.storage.loot.LootContext.Builder;

public class BlockSlabBase extends SlabBlock {
	
	public BlockSlabBase(Properties properties) {
		super(properties);
		this.setDefaultState(this.getDefaultState().with(TYPE, SlabType.BOTTOM).with(WATERLOGGED, Boolean.valueOf(false)));
	}
	
	@Override
	public int getFlammability(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
		return 60;
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState p_220076_1_, Builder p_220076_2_) {
		ArrayList<ItemStack> dropList = new ArrayList<ItemStack>();
		dropList.add(new ItemStack(this));
		return dropList;
	}
	
}
