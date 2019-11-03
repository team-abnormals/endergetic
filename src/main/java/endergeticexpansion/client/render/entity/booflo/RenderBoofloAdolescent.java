package endergeticexpansion.client.render.entity.booflo;

import endergeticexpansion.client.model.booflo.ModelAdolescentBooflo;
import endergeticexpansion.client.render.entity.layer.RenderLayerBoofloAdolescentFruit;
import endergeticexpansion.common.entities.booflo.EntityBoofloAdolescent;
import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.util.ResourceLocation;

public class RenderBoofloAdolescent extends LivingRenderer<EntityBoofloAdolescent, ModelAdolescentBooflo<EntityBoofloAdolescent>> {

	public RenderBoofloAdolescent(EntityRendererManager manager) {
		super(manager, new ModelAdolescentBooflo<>(), 0.5F);
		this.addLayer(new RenderLayerBoofloAdolescentFruit(this));
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityBoofloAdolescent entity) {
		return new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/booflo/booflo_adolescent.png");
	}
	
	@Override
	protected boolean canRenderName(EntityBoofloAdolescent entity) {
		return entity.hasCustomName() ? super.canRenderName(entity) : false;
	}

}
