package endergeticexpansion.client.render.entity.booflo;

import endergeticexpansion.client.model.booflo.ModelBooflo;
import endergeticexpansion.client.model.booflo.ModelBoofloInflated;
import endergeticexpansion.client.render.entity.layer.RenderLayerBoofloFruit;
import endergeticexpansion.client.render.entity.layer.RenderLayerBoofloGlow;
import endergeticexpansion.common.entities.booflo.EntityBooflo;
import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderBooflo extends LivingRenderer<EntityBooflo, EntityModel<EntityBooflo>> {
	
	public RenderBooflo(EntityRendererManager manager) {
		super(manager, new ModelBooflo<>(), 1.25F);
		this.addLayer(new RenderLayerBoofloGlow<>(this));
		this.addLayer(new RenderLayerBoofloFruit(this));
	}
	
	public void doRender(EntityBooflo booflo, double x, double y, double z, float entityYaw, float partialTicks) {
		this.shadowSize = booflo.isBoofed() ? 2.0F : 1.25F;
		this.entityModel = booflo.isBoofed() ? new ModelBoofloInflated<>() : new ModelBooflo<>();
		super.doRender(booflo, x, y, z, entityYaw, partialTicks);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityBooflo booflo) {
		return booflo.isBoofed() ? new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/booflo/booflo_inflated.png") : new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/booflo/booflo.png");
	}
	
	@Override
	protected boolean canRenderName(EntityBooflo entity) {
		return entity.hasCustomName() ? super.canRenderName(entity) : false;
	}

}