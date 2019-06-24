package endergeticexpansion.client.render.entity;

import endergeticexpansion.client.model.ModelBoofBlock;
import endergeticexpansion.common.entities.EntityBoofBlock;
import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.util.ResourceLocation;

public class RenderBoofBlock extends LivingRenderer<EntityBoofBlock, ModelBoofBlock<EntityBoofBlock>>{

	public RenderBoofBlock(EntityRendererManager renderManager) {
        super(renderManager, new ModelBoofBlock<>(), 0.0F);
    }
	
	@Override
	public void doRender(EntityBoofBlock entity, double x, double y, double z, float entityYaw, float partialTicks) {
		entity.prevRenderYawOffset = 0;
		entity.renderYawOffset = 0;
		entity.rotationYaw = 0;
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityBoofBlock entity) {
		return new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/boof_block_inflated.png");
	}

	protected boolean canRenderName(EntityBoofBlock entity) {
		return false;
	}
	
}
