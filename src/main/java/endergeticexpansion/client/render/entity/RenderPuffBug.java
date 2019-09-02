package endergeticexpansion.client.render.entity;

import endergeticexpansion.client.model.puffbug.ModelPuffBugDeflated;
import endergeticexpansion.client.model.puffbug.ModelPuffBugInflatedMedium;
import endergeticexpansion.client.render.entity.layer.RenderLayerPuffBugGlow;
import endergeticexpansion.client.model.puffbug.ModelPuffBugInflated;
import endergeticexpansion.common.entities.EntityPuffBug;
import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;

public class RenderPuffBug extends LivingRenderer<EntityPuffBug, EntityModel<EntityPuffBug>> {
	private final ResourceLocation[] TEXTURES = new ResourceLocation[] {
		new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/puffbug/puffbug_deflated.png"),
		new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/puffbug/puffbug_medium_inflated.png"),
		new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/puffbug/puffbug_inflated.png")
	};
	private final ModelPuffBugDeflated<EntityPuffBug> DEFLATED_MODEL = new ModelPuffBugDeflated<>();
	private final ModelPuffBugInflatedMedium<EntityPuffBug> MEDIUM_INFLATED_MODEL = new ModelPuffBugInflatedMedium<>();
	private final ModelPuffBugInflated<EntityPuffBug> INFLATED_MODEL = new ModelPuffBugInflated<>();
	
	public RenderPuffBug(EntityRendererManager manager) {
		super(manager, new ModelPuffBugInflatedMedium<>(), 0.3F);
		this.addLayer(new RenderLayerPuffBugGlow<>(this));
	}
	
	@Override
	public void doRender(EntityPuffBug entity, double x, double y, double z, float entityYaw, float partialTicks) {
		this.entityModel = entity.getPuffState() == 0 ? DEFLATED_MODEL : entity.getPuffState() == 1 ? MEDIUM_INFLATED_MODEL : INFLATED_MODEL;
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityPuffBug entity) {
		return TEXTURES[entity.getPuffState()];
	}
	
	@Override
	protected boolean canRenderName(EntityPuffBug entity) {
		return entity.hasCustomName() ? super.canRenderName(entity) : false;
	}
}
