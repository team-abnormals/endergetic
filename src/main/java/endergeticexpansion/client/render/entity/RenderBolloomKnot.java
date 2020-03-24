package endergeticexpansion.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import endergeticexpansion.client.model.bolloom.ModelBolloomKnot;
import endergeticexpansion.common.entities.bolloom.EntityBolloomKnot;
import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;

public class RenderBolloomKnot extends EntityRenderer<EntityBolloomKnot> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/bolloom_knot.png");
	public ModelBolloomKnot<EntityBolloomKnot> model;
	
	public RenderBolloomKnot(EntityRendererManager manager) {
		super(manager);
		this.model = new ModelBolloomKnot<EntityBolloomKnot>();
	}
	
	@Override
	public void render(EntityBolloomKnot entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int packedLightIn) {
		matrixStack.push();
		matrixStack.translate(0.0F, -1.31F, 0.0F);
		IVertexBuilder ivertexbuilder = bufferIn.getBuffer(this.model.getRenderType(this.getEntityTexture(entity)));
    	this.model.render(matrixStack, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		matrixStack.pop();
		super.render(entity, entityYaw, partialTicks, matrixStack, bufferIn, packedLightIn);
	}
	
	@Override
	public ResourceLocation getEntityTexture(EntityBolloomKnot arg0) {
		return TEXTURE;
	}
}