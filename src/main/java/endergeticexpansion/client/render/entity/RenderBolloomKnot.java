package endergeticexpansion.client.render.entity;

import com.mojang.blaze3d.platform.GlStateManager;

import endergeticexpansion.client.model.bolloom.ModelBolloomKnot;
import endergeticexpansion.common.entities.EntityBolloomKnot;
import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderBolloomKnot extends EntityRenderer<EntityBolloomKnot> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/bolloom_knot.png");
	public ModelBolloomKnot<EntityBolloomKnot> model;
	
	public RenderBolloomKnot(EntityRendererManager p_i46179_1_) {
		super(p_i46179_1_);
		model = new ModelBolloomKnot<EntityBolloomKnot>();
	}
	
	@Override
	public void doRender(EntityBolloomKnot entity, double x, double y, double z, float entityYaw, float partialTicks) {
		GlStateManager.pushMatrix();
		this.bindTexture(TEXTURE);
		GlStateManager.translated(x, y - 1.31F, z);
		model.render(entity, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
		GlStateManager.popMatrix();
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityBolloomKnot arg0) {
		return TEXTURE;
	}

}
