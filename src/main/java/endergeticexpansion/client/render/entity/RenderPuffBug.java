package endergeticexpansion.client.render.entity;

import com.mojang.blaze3d.platform.GlStateManager;

import endergeticexpansion.client.model.puffbug.ModelPuffBugInflatedMedium;
import endergeticexpansion.client.render.entity.layer.RenderLayerPuffBugGlow;
import endergeticexpansion.common.entities.EntityPuffBug;
import endergeticexpansion.common.entities.EntityPuffBug.PuffState;
import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;

public class RenderPuffBug extends LivingRenderer<EntityPuffBug, EntityModel<EntityPuffBug>> {
	private final ResourceLocation[] TEXTURES = new ResourceLocation[] {
		new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/puffbug/puffbug_deflated.png"),
		new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/puffbug/puffbug_medium_inflated.png"),
		new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/puffbug/puffbug_inflated.png")
	};
	
	public RenderPuffBug(EntityRendererManager manager) {
		super(manager, new ModelPuffBugInflatedMedium<>(), 0.3F);
		this.addLayer(new RenderLayerPuffBugGlow<>(this));
	}
	
	@Override
	public void doRender(EntityPuffBug entity, double x, double y, double z, float entityYaw, float partialTicks) {
		if(entity.isChild()) {
			this.shadowSize *= 0.5F;
		}
		
		this.entityModel = PuffState.getModel(entity.getPuffState());
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityPuffBug entity) {
		return TEXTURES[entity.getPuffStateId()];
	}
	
	@Override
	protected boolean canRenderName(EntityPuffBug entity) {
		return entity.hasCustomName() ? super.canRenderName(entity) : false;
	}
	
	@Override
	protected void preRenderCallback(EntityPuffBug puffbug, float partialTickTime) {
		GlStateManager.scalef(1.0F, 1.0F, 1.0F);
		if(puffbug.isChild()) {
			GlStateManager.scalef(0.5F, 0.5F, 0.5F);
		}
	}
}
