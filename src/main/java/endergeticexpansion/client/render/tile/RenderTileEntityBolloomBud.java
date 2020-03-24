package endergeticexpansion.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import endergeticexpansion.client.model.bolloom.ModelBolloomBud;
import endergeticexpansion.common.tileentities.TileEntityBolloomBud;
import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
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
	public void render(TileEntityBolloomBud bud, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		matrixStack.push();
		matrixStack.translate(0.5D, 1.5D, 0.5D);
		matrixStack.scale(1.0F, -1.0F, -1.0F);
		
		IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getEntityCutoutNoCull(TEXTURE));
		this.budModel.renderAll(bud, matrixStack, ivertexbuilder, combinedLightIn, combinedOverlayIn);
		
		matrixStack.pop();
	}
}