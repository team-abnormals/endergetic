package endergeticexpansion.common.blocks;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.BlockState;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.LootContext.Builder;
import net.minecraftforge.common.ToolType;

public class BlockPressurePlateBase extends PressurePlateBlock {

	public BlockPressurePlateBase(Sensitivity sensitivity, Properties props) {
		super(sensitivity, props);
	}

	@Override
	public ToolType getHarvestTool(BlockState state) {
		return ToolType.AXE;
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState p_220076_1_, Builder p_220076_2_) {
		ArrayList<ItemStack> dropList = new ArrayList<ItemStack>();
		dropList.add(new ItemStack(this));
		return dropList;
	}
	
}
