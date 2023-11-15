package com.teamabnormals.endergetic.client.renderer.entity.booflo;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamabnormals.blueprint.client.EntitySkinHelper;
import com.teamabnormals.endergetic.client.model.booflo.BoofloModel;
import com.teamabnormals.endergetic.client.renderer.entity.layers.BoofloEmissiveLayer;
import com.teamabnormals.endergetic.client.renderer.entity.layers.LayerRendererBoofloBracelets;
import com.teamabnormals.endergetic.client.renderer.entity.layers.LayerRendererBoofloFruit;
import com.teamabnormals.endergetic.common.entity.booflo.Booflo;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BoofloRenderer extends MobRenderer<Booflo, EntityModel<Booflo>> {
	private static final ResourceLocation DEFAULT = new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/booflo/booflo.png");
	private static final EntitySkinHelper<Booflo> SKIN_HELPER = EntitySkinHelper.create(EndergeticExpansion.MOD_ID, "textures/entity/booflo/", "booflo", (skinHelper) -> {
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
	public void render(Booflo booflo, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
		this.shadowRadius = booflo.isBoofed() ? 2.0F : 1.25F;
		super.render(booflo, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Override
	public ResourceLocation getTextureLocation(Booflo booflo) {
		return SKIN_HELPER.getSkinForEntityOrElse(booflo, DEFAULT);
	}
}