package endergeticexpansion.client.render.entity;

import endergeticexpansion.client.model.ModelPoiseBlock;
import endergeticexpansion.common.entities.EntityPoiseCluster;
import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.util.ResourceLocation;

public class RenderPoiseCluster extends LivingRenderer<EntityPoiseCluster, ModelPoiseBlock<EntityPoiseCluster>> {
	
	public RenderPoiseCluster(EntityRendererManager renderManager) {
        super(renderManager, new ModelPoiseBlock<>(), 0.0F);
    }

	@Override
	protected ResourceLocation getEntityTexture(EntityPoiseCluster entity) {
		return new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/poise_cluster.png");
	}
	
	protected boolean canRenderName(EntityPoiseCluster entity) {
		return super.canRenderName(entity) && (entity.getAlwaysRenderNameTagForRender() || entity.hasCustomName() && entity == this.field_76990_c.pointedEntity);
	}

}
