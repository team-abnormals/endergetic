package endergeticexpansion.client.render.entity.booflo;

import endergeticexpansion.client.model.booflo.ModelAdolescentBooflo;
import endergeticexpansion.client.render.entity.layer.RenderLayerAdolescentGlow;
import endergeticexpansion.client.render.entity.layer.RenderLayerBoofloAdolescentFruit;
import endergeticexpansion.common.entities.booflo.EntityBoofloAdolescent;
import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class RenderBoofloAdolescent extends MobRenderer<EntityBoofloAdolescent, ModelAdolescentBooflo<EntityBoofloAdolescent>> {

	public RenderBoofloAdolescent(EntityRendererManager manager) {
		super(manager, new ModelAdolescentBooflo<>(), 0.5F);
		this.addLayer(new RenderLayerAdolescentGlow<>(this));
		this.addLayer(new RenderLayerBoofloAdolescentFruit(this));
	}

	@Override
	public ResourceLocation getEntityTexture(EntityBoofloAdolescent entity) {
		return new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/booflo/booflo_adolescent.png");
	}
	
}