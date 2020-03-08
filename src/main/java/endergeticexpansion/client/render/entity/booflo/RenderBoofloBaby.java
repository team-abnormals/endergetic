package endergeticexpansion.client.render.entity.booflo;

import endergeticexpansion.client.model.booflo.ModelBoofloBaby;
import endergeticexpansion.client.render.entity.layer.RenderLayerBoofloBabyGlow;
import endergeticexpansion.common.entities.booflo.EntityBoofloBaby;
import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class RenderBoofloBaby extends MobRenderer<EntityBoofloBaby, ModelBoofloBaby<EntityBoofloBaby>> {

	public RenderBoofloBaby(EntityRendererManager manager) {
		super(manager, new ModelBoofloBaby<>(), 0.3F);
		this.addLayer(new RenderLayerBoofloBabyGlow<>(this));
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityBoofloBaby entity) {
		return new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/booflo/booflo_baby.png");
	}
	
}
