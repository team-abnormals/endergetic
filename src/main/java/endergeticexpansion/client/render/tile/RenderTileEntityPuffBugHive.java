package endergeticexpansion.client.render.tile;

import com.mojang.blaze3d.platform.GlStateManager;

import endergeticexpansion.client.model.ModelPuffBugHive;
import endergeticexpansion.common.tileentities.TileEntityPuffBugHive;
import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;

public class RenderTileEntityPuffBugHive extends TileEntityRenderer<TileEntityPuffBugHive> {
	private ModelPuffBugHive hiveModel;
	private static final ResourceLocation TEXTURE = new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/tile/puffbug_hive.png");
	
	public RenderTileEntityPuffBugHive(TileEntityRendererDispatcher renderDispatcher) {
		super(renderDispatcher);
		hiveModel = new ModelPuffBugHive();
	}
	
	@Override
	public void render(TileEntityPuffBugHive te, double x, double y, double z, float partialTicks, int destroyStage) {
		GlStateManager.enableDepthTest();
		GlStateManager.depthFunc(515);
		GlStateManager.depthMask(true);
		
		if (destroyStage >= 0) {
			this.bindTexture(DESTROY_STAGES[destroyStage]);
			GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scalef(4.0F, 4.0F, 2.0F);
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
		
		hiveModel.renderAll();
		
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
