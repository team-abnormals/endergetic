package endergeticexpansion.client.render.entity.booflo;

import com.mojang.blaze3d.matrix.MatrixStack;

import endergeticexpansion.client.model.booflo.ModelBooflo;
import endergeticexpansion.client.render.entity.layer.RenderLayerBoofloBracelets;
import endergeticexpansion.client.render.entity.layer.RenderLayerBoofloFruit;
import endergeticexpansion.client.render.entity.layer.RenderLayerBoofloGlow;
import endergeticexpansion.common.entities.booflo.EntityBooflo;
import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderBooflo extends MobRenderer<EntityBooflo, EntityModel<EntityBooflo>> {
	
	public RenderBooflo(EntityRendererManager manager) {
		super(manager, new ModelBooflo<>(), 1.25F);
		this.addLayer(new RenderLayerBoofloGlow<>(this));
		this.addLayer(new RenderLayerBoofloBracelets<>(this));
		this.addLayer(new RenderLayerBoofloFruit(this));
	}
	
	@Override
	public void render(EntityBooflo booflo, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		this.shadowSize = booflo.isBoofed() ? 2.0F : 1.25F;
		
		super.render(booflo, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Override
	public ResourceLocation getEntityTexture(EntityBooflo booflo) {
		return new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/booflo/booflo" + booflo.getNameSuffix() + ".png");
	}
	
}