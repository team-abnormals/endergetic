package endergeticexpansion.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;

import endergeticexpansion.client.model.BoofBlockModel;
import endergeticexpansion.common.entities.BoofBlockEntity;
import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.util.ResourceLocation;

public class BoofBlockRenderer extends LivingRenderer<BoofBlockEntity, BoofBlockModel<BoofBlockEntity>>{

	public BoofBlockRenderer(EntityRendererManager renderManager) {
        super(renderManager, new BoofBlockModel<>(), 0.0F);
    }
	
	@Override
	public void render(BoofBlockEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		entity.prevRenderYawOffset = 0;
		entity.renderYawOffset = 0;
		entity.rotationYaw = 0;
		super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Override
	public ResourceLocation getEntityTexture(BoofBlockEntity entity) {
		return new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/boof_block_inflated.png");
	}

	protected boolean canRenderName(BoofBlockEntity entity) {
		return false;
	}
	
}