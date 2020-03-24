package endergeticexpansion.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import endergeticexpansion.client.model.ModelPuffBugHive;
import endergeticexpansion.common.tileentities.TileEntityPuffBugHive;
import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
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
	public void render(TileEntityPuffBugHive hive, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		matrixStack.push();
		matrixStack.translate(0.5D, 1.5D, 0.5D);
		
		IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getEntityCutout(TEXTURE));
		this.hiveModel.renderAll(matrixStack, ivertexbuilder, combinedLightIn, combinedOverlayIn);
		
		matrixStack.pop();
	}
}