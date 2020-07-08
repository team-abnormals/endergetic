package endergeticexpansion.client.render.entity;

import endergeticexpansion.client.model.PoiseClusterModel;
import endergeticexpansion.common.entities.PoiseClusterEntity;
import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.util.ResourceLocation;

public class PoiseClusterRender extends LivingRenderer<PoiseClusterEntity, PoiseClusterModel<PoiseClusterEntity>> {
	
	public PoiseClusterRender(EntityRendererManager renderManager) {
        super(renderManager, new PoiseClusterModel<>(), 0.0F);
    }
	
	@Override
	protected RenderType func_230496_a_(PoiseClusterEntity cluster, boolean p_230496_2_, boolean p_230496_3_, boolean p_230496_4_) {
		return RenderType.getEntityTranslucent(this.getEntityTexture(cluster));
	}

	@Override
	public ResourceLocation getEntityTexture(PoiseClusterEntity entity) {
		return new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/poise_cluster.png");
	}
	
	protected boolean canRenderName(PoiseClusterEntity entity) {
		return false;
	}

}