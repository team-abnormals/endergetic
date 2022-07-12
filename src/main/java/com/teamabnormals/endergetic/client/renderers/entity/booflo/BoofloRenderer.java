package com.teamabnormals.endergetic.client.renderers.entity.booflo;

import com.teamabnormals.endergetic.client.models.booflo.BoofloModel;
import com.teamabnormals.endergetic.client.renderers.entity.layer.BoofloEmissiveLayer;
import com.teamabnormals.endergetic.client.renderers.entity.layer.LayerRendererBoofloBracelets;
import com.teamabnormals.endergetic.client.renderers.entity.layer.LayerRendererBoofloFruit;
import com.teamabnormals.endergetic.common.entities.booflo.BoofloEntity;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import com.mojang.blaze3d.vertex.PoseStack;

import com.teamabnormals.blueprint.client.EntitySkinHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BoofloRenderer extends MobRenderer<BoofloEntity, EntityModel<BoofloEntity>> {
	private static final ResourceLocation DEFAULT = new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/booflo/booflo.png");
	private static final EntitySkinHelper<BoofloEntity> SKIN_HELPER = EntitySkinHelper.create(EndergeticExpansion.MOD_ID, "textures/entity/booflo/", "booflo", (skinHelper) -> {
		skinHelper.putSkins("snake", "snake", "snake block", "the forsaken one");
		skinHelper.putSkins("cam", "cameron", "cam");
	});

	public BoofloRenderer(EntityRendererProvider.Context context) {
		super(context, new BoofloModel<>(context.bakeLayer(BoofloModel.LOCATION)), 1.25F);
		this.addLayer(new BoofloEmissiveLayer<>(this));
		this.addLayer(new LayerRendererBoofloBracelets<>(this));
		this.addLayer(new LayerRendererBoofloFruit(this, context.getItemInHandRenderer()));
	}

	@Override
	public void render(BoofloEntity booflo, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
		this.shadowRadius = booflo.isBoofed() ? 2.0F : 1.25F;
		super.render(booflo, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Override
	public ResourceLocation getTextureLocation(BoofloEntity booflo) {
		return SKIN_HELPER.getSkinForEntityOrElse(booflo, DEFAULT);
	}
}