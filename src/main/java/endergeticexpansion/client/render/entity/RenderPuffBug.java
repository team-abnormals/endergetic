package endergeticexpansion.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;

import endergeticexpansion.client.model.puffbug.ModelPuffBug;
import endergeticexpansion.client.render.entity.layer.RenderLayerPuffBugGlow;
import endergeticexpansion.common.entities.puffbug.EntityPuffBug;
import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;

public class RenderPuffBug extends MobRenderer<EntityPuffBug, EntityModel<EntityPuffBug>> {
	private static final ResourceLocation DEFLATED = new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/puffbug/puffbug_deflated.png");
	private static final ResourceLocation INFLATED = new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/puffbug/puffbug_inflated.png");
	
	public RenderPuffBug(EntityRendererManager manager) {
		super(manager, new ModelPuffBug<>(), 0.3F);
		this.addLayer(new RenderLayerPuffBugGlow<>(this));
	}
	
	@Override
	public void render(EntityPuffBug puffbug, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		if(puffbug.isChild()) {
			this.shadowSize *= 0.5F;
		}
		super.render(puffbug, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}
	
	@Override
	public ResourceLocation getEntityTexture(EntityPuffBug puffbug) {
		return puffbug.isInflated() ? INFLATED : DEFLATED;
	}
	
	@Override
	protected void preRenderCallback(EntityPuffBug puffbug, MatrixStack matrixStack, float partialTickTime) {
		matrixStack.scale(1.0F, 1.0F, 1.0F);
		if(puffbug.isChild()) {
			matrixStack.scale(0.5F, 0.5F, 0.5F);
		}
	}
}