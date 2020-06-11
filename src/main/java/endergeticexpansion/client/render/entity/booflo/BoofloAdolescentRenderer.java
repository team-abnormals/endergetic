package endergeticexpansion.client.render.entity.booflo;

import endergeticexpansion.client.model.booflo.AdolescentBoofloModel;
import endergeticexpansion.client.render.entity.layer.EmissiveLayerRenderer;
import endergeticexpansion.client.render.entity.layer.LayerRendererBoofloAdolescentFruit;
import endergeticexpansion.common.entities.booflo.BoofloAdolescentEntity;
import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class BoofloAdolescentRenderer extends MobRenderer<BoofloAdolescentEntity, AdolescentBoofloModel<BoofloAdolescentEntity>> {

	public BoofloAdolescentRenderer(EntityRendererManager manager) {
		super(manager, new AdolescentBoofloModel<>(), 0.5F);
		this.addLayer(new EmissiveLayerRenderer<>(this, new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/booflo/booflo_adolescent_glow_layer.png")));
		this.addLayer(new LayerRendererBoofloAdolescentFruit(this));
	}

	@Override
	public ResourceLocation getEntityTexture(BoofloAdolescentEntity entity) {
		return new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/booflo/booflo_adolescent.png");
	}
	
}