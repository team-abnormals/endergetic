package endergeticexpansion.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraftforge.common.ToolType;

public class BlockEumus extends Block {

	public BlockEumus(Properties properties) {
		super(properties);
	}

	@Override
	public ToolType getHarvestTool(BlockState state) {
		return ToolType.SHOVEL;
	}
	
}