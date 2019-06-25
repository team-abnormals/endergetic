package endergeticexpansion.client.render.tile;

import com.mojang.blaze3d.platform.GlStateManager;

import endergeticexpansion.client.model.ModelBoofBlockDispenser;
import endergeticexpansion.common.blocks.poise.boof.BlockDispensedBoof;
import endergeticexpansion.common.tileentities.boof.TileEntityDispensedBoof;
import endergeticexpansion.core.EndergeticExpansion;
import endergeticexpansion.core.registry.EEBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;

public class RenderTileEntityBoofBlockDispensed extends TileEntityRenderer<TileEntityDispensedBoof> {
	public ModelBoofBlockDispenser model;
	private static final ResourceLocation TEXTURE = new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/block/boof_block_dispensed.png");
	
	public RenderTileEntityBoofBlockDispensed() {
		this.model = new ModelBoofBlockDispenser();
	}
	
	@Override
	public void render(TileEntityDispensedBoof te, double x, double y, double z, float partialTicks, int destroyStage) {
		BlockState state = te.hasWorld() ? te.getBlockState() : (BlockState) EEBlocks.BOOF_DISPENSED_BLOCK.getDefaultState();
		GlStateManager.pushMatrix();
		
		this.bindTexture(TEXTURE);
		GlStateManager.translatef((float)x + 0.5F, (float)y + 1.5F, (float)z + 0.5F);
		if(state.get(BlockDispensedBoof.FACING) == Direction.NORTH) {
			GlStateManager.rotatef(180.0F, 0, 1F, 0);
		} else if(state.get(BlockDispensedBoof.FACING) == Direction.EAST) {
			GlStateManager.rotatef(90.0F, 0, 1F, 0);
		} else if(state.get(BlockDispensedBoof.FACING) == Direction.WEST) {
			GlStateManager.rotatef(-90.0F, 0, 1F, 0);
		} else if(state.get(BlockDispensedBoof.FACING) == Direction.UP) {
			GlStateManager.rotatef(-90.0F, 1F, 0, 0);
			GlStateManager.translatef(0.0F, 1.125F, -1.0F);
		} else if(state.get(BlockDispensedBoof.FACING) == Direction.DOWN) {
			GlStateManager.rotatef(90.0F, 1F, 0, 0);
			GlStateManager.translatef(0.0F, 1.125F, 1.0F);
		}
		
		GlStateManager.enableRescaleNormal();
		
		GlStateManager.pushMatrix();
		GlStateManager.scalef(1.0F, -1.0F, -1.0F);
		model.renderAll();
		
		GlStateManager.popMatrix();
		GlStateManager.popMatrix();
	}
	
}
