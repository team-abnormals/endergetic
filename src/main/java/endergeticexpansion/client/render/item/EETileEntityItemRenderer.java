package endergeticexpansion.client.render.item;

import endergeticexpansion.common.blocks.poise.BlockBolloomBud;
import endergeticexpansion.common.blocks.poise.hive.BlockPuffBugHive;
import endergeticexpansion.common.tileentities.TileEntityBolloomBud;
import endergeticexpansion.common.tileentities.TileEntityPuffBugHive;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class EETileEntityItemRenderer extends ItemStackTileEntityRenderer {
	
	private static final TileEntityBolloomBud BOLLOOM_BUD = new TileEntityBolloomBud();
	private static final TileEntityPuffBugHive PUFFBUG_HIVE = new TileEntityPuffBugHive();
	
	@Override
	public void renderByItem(ItemStack itemStackIn) {
		Item item = itemStackIn.getItem();
		
		if (Block.getBlockFromItem(item) instanceof BlockBolloomBud) {
			TileEntityRendererDispatcher.instance.renderAsItem(BOLLOOM_BUD);
        } else if(Block.getBlockFromItem(item) instanceof BlockPuffBugHive) {
        	TileEntityRendererDispatcher.instance.renderAsItem(PUFFBUG_HIVE);
        }
		
	}
	
}
