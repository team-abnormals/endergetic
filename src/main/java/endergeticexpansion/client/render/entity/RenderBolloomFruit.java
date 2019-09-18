package endergeticexpansion.client.render.entity;

import com.mojang.blaze3d.platform.GlStateManager;

import endergeticexpansion.client.model.bolloom.ModelBolloomFruit;
import endergeticexpansion.common.entities.bolloom.EntityBolloomFruit;
import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderBolloomFruit extends EntityRenderer<EntityBolloomFruit> {
	public ModelBolloomFruit<EntityBolloomFruit> model;
	
	public RenderBolloomFruit(EntityRendererManager renderManager) {
		super(renderManager);
		this.model = new ModelBolloomFruit<EntityBolloomFruit>();
    }

	@Override
	protected ResourceLocation getEntityTexture(EntityBolloomFruit entity) {
		return new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/bolloom_fruit.png");
	}
	
	@Override
	public void doRender(EntityBolloomFruit entity, double x, double y, double z, float entityYaw, float partialTicks) {
		float[] angles = entity.getVineAnimation(partialTicks);
		this.model.vine_x.rotateAngleX = angles[0];
		this.model.vine_x.rotateAngleY = angles[1];
		GlStateManager.pushMatrix();
		this.bindTexture(this.getEntityTexture(entity));
		GlStateManager.translated(x, y + 1.5F, z);
		GlStateManager.rotatef(180.0F, 1.0F, 0.0F, 0.0F);
		model.render(entity, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
		GlStateManager.popMatrix();
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}
}
