package endergeticexpansion.client.render.item;

import com.mojang.blaze3d.matrix.MatrixStack;

import endergeticexpansion.common.blocks.poise.BlockBolloomBud;
import endergeticexpansion.common.blocks.poise.hive.BlockPuffBugHive;
import endergeticexpansion.common.tileentities.TileEntityBolloomBud;
import endergeticexpansion.common.tileentities.TileEntityPuffBugHive;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;

public class EETileEntityItemRenderer extends ItemStackTileEntityRenderer {
	private static final TileEntityBolloomBud BOLLOOM_BUD = new TileEntityBolloomBud();
	private static final TileEntityPuffBugHive PUFFBUG_HIVE = new TileEntityPuffBugHive();
	
	@Override
	public void render(ItemStack itemStackIn, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		Block block = Block.getBlockFromItem(itemStackIn.getItem());
		if(block instanceof BlockBolloomBud) {
			TileEntityRendererDispatcher.instance.renderItem(BOLLOOM_BUD, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        } else if(block instanceof BlockPuffBugHive) {
        	TileEntityRendererDispatcher.instance.renderItem(PUFFBUG_HIVE,  matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        }
	}
}