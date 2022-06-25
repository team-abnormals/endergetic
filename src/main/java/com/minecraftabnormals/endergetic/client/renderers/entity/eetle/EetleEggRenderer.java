package com.minecraftabnormals.endergetic.client.renderers.entity.eetle;

import com.minecraftabnormals.endergetic.client.EERenderTypes;
import com.minecraftabnormals.endergetic.client.models.eetle.eggs.*;
import com.minecraftabnormals.endergetic.client.renderers.tile.EetleEggTileEntityRenderer;
import com.minecraftabnormals.endergetic.common.entities.eetle.EetleEggEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import com.mojang.math.Vector3f;

public class EetleEggRenderer extends EntityRenderer<EetleEggEntity> {
	private final IEetleEggModel[] eggModels;

	public EetleEggRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.eggModels = new IEetleEggModel[] {
				new SmallEetleEggModel(context.bakeLayer(SmallEetleEggModel.LOCATION)),
				new MediumEetleEggModel(context.bakeLayer(MediumEetleEggModel.LOCATION)),
				new LargeEetleEggModel(context.bakeLayer(LargeEetleEggModel.LOCATION))
		};
	}

	@Override
	public void render(EetleEggEntity eggsEntity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLightIn) {
		matrixStack.pushPose();
		matrixStack.mulPose(Vector3f.XP.rotationDegrees(180.0F));
		matrixStack.translate(0.0F, -1.5F, 0.0F);
		int size = eggsEntity.getEggSize().ordinal();
		IEetleEggModel eggsModel = this.eggModels[size];
		eggsModel.render(matrixStack, buffer.getBuffer(RenderType.entityCutout(EetleEggTileEntityRenderer.TEXTURES[size])), packedLightIn, OverlayTexture.NO_OVERLAY, partialTicks, eggsEntity.getSackGrowths());
		eggsModel.renderSilk(matrixStack, buffer.getBuffer(EERenderTypes.EETLE_EGG_SILK), packedLightIn, OverlayTexture.NO_OVERLAY);
		matrixStack.popPose();
		super.render(eggsEntity, entityYaw, partialTicks, matrixStack, buffer, packedLightIn);
	}

	@Override
	public ResourceLocation getTextureLocation(EetleEggEntity entity) {
		return EetleEggTileEntityRenderer.TEXTURES[0];
	}
}
