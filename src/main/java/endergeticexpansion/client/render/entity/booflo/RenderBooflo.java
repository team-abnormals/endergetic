package endergeticexpansion.client.render.entity.booflo;

import endergeticexpansion.client.model.booflo.ModelBooflo;
import endergeticexpansion.client.render.entity.layer.RenderLayerBoofloGlow;
import endergeticexpansion.common.entities.booflo.EntityBooflo;
import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderBooflo extends LivingRenderer<EntityBooflo, ModelBooflo<EntityBooflo>> {

	public RenderBooflo(EntityRendererManager manager) {
		super(manager, new ModelBooflo<>(), 1.25F);
		this.addLayer(new RenderLayerBoofloGlow<>(this));
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityBooflo entity) {
		return new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/booflo/booflo.png");
	}
	
	@Override
	protected boolean canRenderName(EntityBooflo entity) {
		return entity.hasCustomName() ? super.canRenderName(entity) : false;
	}

}