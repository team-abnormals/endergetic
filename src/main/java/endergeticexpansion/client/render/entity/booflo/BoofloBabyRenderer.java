package endergeticexpansion.client.render.entity.booflo;

import endergeticexpansion.client.model.booflo.BoofloBabyModel;
import endergeticexpansion.client.render.entity.layer.EmissiveLayerRenderer;
import endergeticexpansion.common.entities.booflo.BoofloBabyEntity;
import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class BoofloBabyRenderer extends MobRenderer<BoofloBabyEntity, BoofloBabyModel<BoofloBabyEntity>> {

	public BoofloBabyRenderer(EntityRendererManager manager) {
		super(manager, new BoofloBabyModel<>(), 0.3F);
		this.addLayer(new EmissiveLayerRenderer<>(this, new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/booflo/booflo_baby_glow_layer.png")));
	}

	@Override
	public ResourceLocation getEntityTexture(BoofloBabyEntity entity) {
		return new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/booflo/booflo_baby.png");
	}
	
}