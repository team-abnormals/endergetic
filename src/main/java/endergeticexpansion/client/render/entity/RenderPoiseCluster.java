package endergeticexpansion.client.render.entity;

import endergeticexpansion.client.model.ModelPoiseBlock;
import endergeticexpansion.common.entities.EntityPoiseCluster;
import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.util.ResourceLocation;

public class RenderPoiseCluster extends LivingRenderer<EntityPoiseCluster, ModelPoiseBlock<EntityPoiseCluster>> {
	
	public RenderPoiseCluster(EntityRendererManager renderManager) {
        super(renderManager, new ModelPoiseBlock<>(), 0.0F);
    }
	
	@Override
	protected RenderType func_230042_a_(EntityPoiseCluster cluster, boolean p_230042_2_, boolean p_230042_3_) {
		return RenderType.getEntityTranslucent(this.getEntityTexture(cluster));
	}

	@Override
	public ResourceLocation getEntityTexture(EntityPoiseCluster entity) {
		return new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/poise_cluster.png");
	}
	
	protected boolean canRenderName(EntityPoiseCluster entity) {
		return false;
	}

}