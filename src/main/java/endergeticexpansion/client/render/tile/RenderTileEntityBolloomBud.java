package endergeticexpansion.client.render.tile;

import com.mojang.blaze3d.platform.GlStateManager;

import endergeticexpansion.client.model.bolloom.ModelBolloomBud;
import endergeticexpansion.common.tileentities.TileEntityBolloomBud;
import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;

public class RenderTileEntityBolloomBud extends TileEntityRenderer<TileEntityBolloomBud> {
	private ModelBolloomBud budModel;
	private static final ResourceLocation TEXTURE = new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/tile/bolloom_bud.png");
	
	public RenderTileEntityBolloomBud(TileEntityRendererDispatcher renderDispatcher) {
		super(renderDispatcher);
		this.budModel = new ModelBolloomBud();
	}
	
	@Override
	public void render(TileEntityBolloomBud tileEntityIn, double x, double y, double z, float partialTicks, int destroyStage) {
		GlStateManager.enableDepthTest();
		GlStateManager.depthFunc(515);
		GlStateManager.depthMask(true);
		
		if (destroyStage >= 0) {
			this.bindTexture(DESTROY_STAGES[destroyStage]);
			GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scalef(3.0F, 2.0F, 6.0F);
            GlStateManager.translatef(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
		} else {
			GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.bindTexture(TEXTURE);
		}
		
		GlStateManager.pushMatrix();
		
		GlStateManager.translatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
		GlStateManager.enableRescaleNormal();
		GlStateManager.scalef(1.0F, -1.0F, -1.0F);
		
		this.budModel.renderAll(tileEntityIn); 
		
		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
		
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		
        if (destroyStage >= 0) {
           GlStateManager.matrixMode(5890);
           GlStateManager.popMatrix();
           GlStateManager.matrixMode(5888);
        }
	}
}