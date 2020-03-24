package endergeticexpansion.client.render.entity.layer;

import endergeticexpansion.common.entities.booflo.EntityBoofloAdolescent;
import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.AbstractEyesLayer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderLayerAdolescentGlow<E extends EntityBoofloAdolescent, M extends EntityModel<E>> extends AbstractEyesLayer<E, M> {
	public static final ResourceLocation ADOLESCENT_GLOW_LAYER = new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/booflo/booflo_adolescent_glow_layer.png");
	
	public RenderLayerAdolescentGlow(IEntityRenderer<E, M> entityRenderer) {
		super(entityRenderer);
	}

	@Override
	public RenderType getRenderType() {
		return RenderType.getEyes(ADOLESCENT_GLOW_LAYER);
	}
}