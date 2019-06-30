package endergeticexpansion.client.render.entity;

import endergeticexpansion.client.model.bolloom.ModelBolloomFruit;
import endergeticexpansion.common.entities.bolloom.EntityBolloomFruit;
import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.util.ResourceLocation;

public class RenderBolloomFruit extends LivingRenderer<EntityBolloomFruit, ModelBolloomFruit<EntityBolloomFruit>> {
	
	public RenderBolloomFruit(EntityRendererManager renderManager) {
        super(renderManager, new ModelBolloomFruit<>(), 0.0F);
    }

	@Override
	protected ResourceLocation getEntityTexture(EntityBolloomFruit entity) {
		return new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/bolloom_fruit.png");
	}
	
	@Override
	public void doRender(EntityBolloomFruit entity, double x, double y, double z, float entityYaw, float partialTicks) {
		entity.prevRenderYawOffset = 0;
		entity.renderYawOffset = 0;
		entity.rotationYaw = 0;
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}
	
	protected boolean canRenderName(EntityBolloomFruit entity) {
		return false;
	}
	
}
