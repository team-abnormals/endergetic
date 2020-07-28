package com.minecraftabnormals.endergetic.client.render.entity.booflo;

import com.minecraftabnormals.endergetic.client.model.booflo.BoofloModel;
import com.minecraftabnormals.endergetic.client.render.entity.layer.EmissiveLayerRenderer;
import com.minecraftabnormals.endergetic.client.render.entity.layer.LayerRendererBoofloBracelets;
import com.minecraftabnormals.endergetic.client.render.entity.layer.LayerRendererBoofloFruit;
import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BoofloRenderer extends MobRenderer<BoofloEntity, EntityModel<BoofloEntity>> {
	
	public BoofloRenderer(EntityRendererManager manager) {
		super(manager, new BoofloModel<>(), 1.25F);
		this.addLayer(new EmissiveLayerRenderer<>(this, new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/booflo/booflo_glow_layer.png")));
		this.addLayer(new LayerRendererBoofloBracelets<>(this));
		this.addLayer(new LayerRendererBoofloFruit(this));
	}
	
	@Override
	public void render(BoofloEntity booflo, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		this.shadowSize = booflo.isBoofed() ? 2.0F : 1.25F;
		
		super.render(booflo, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Override
	public ResourceLocation getEntityTexture(BoofloEntity booflo) {
		return new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/booflo/booflo" + booflo.getNameSuffix() + ".png");
	}
	
}