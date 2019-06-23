package endergeticexpansion.common.blocks;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.world.storage.loot.LootContext.Builder;
import net.minecraftforge.common.ToolType;

public class BlockEumus extends Block {

	public BlockEumus(Properties properties) {
		super(properties);
	}

	@Override
	public ToolType getHarvestTool(BlockState state) {
		return ToolType.SHOVEL;
	}
	
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState p_220076_1_, Builder p_220076_2_) {
		ArrayList<ItemStack> dropList = new ArrayList<ItemStack>();
		dropList.add(new ItemStack(this));
		return dropList;
	}
	
}
