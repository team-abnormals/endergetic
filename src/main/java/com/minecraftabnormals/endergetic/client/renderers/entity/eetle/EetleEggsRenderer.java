package com.minecraftabnormals.endergetic.client.renderers.entity.eetle;

import com.minecraftabnormals.endergetic.client.EERenderTypes;
import com.minecraftabnormals.endergetic.client.models.eetle.eggs.*;
import com.minecraftabnormals.endergetic.client.renderers.tile.EetleEggsTileEntityRenderer;
import com.minecraftabnormals.endergetic.common.entities.eetle.EetleEggsEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

public class EetleEggsRenderer extends EntityRenderer<EetleEggsEntity> {
	private final IEetleEggsModel[] eggModels = new IEetleEggsModel[] {
			new SmallEetleEggsModel(),
			new MediumEetleEggsModel(),
			new LargeEetleEggsModel()
	};

	public EetleEggsRenderer(EntityRendererManager renderManager) {
		super(renderManager);
	}

	@Override
	public void render(EetleEggsEntity eggsEntity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLightIn) {
		matrixStack.push();
		matrixStack.rotate(Vector3f.XP.rotationDegrees(180.0F));
		matrixStack.translate(0.0F, -1.5F, 0.0F);
		int size = eggsEntity.getEggSize().ordinal();
		IEetleEggsModel eggsModel = this.eggModels[size];
		eggsModel.render(matrixStack, buffer.getBuffer(RenderType.getEntityCutout(EetleEggsTileEntityRenderer.TEXTURES[size])), packedLightIn, OverlayTexture.NO_OVERLAY, partialTicks, eggsEntity.getSackGrowths());
		eggsModel.renderSilk(matrixStack, buffer.getBuffer(EERenderTypes.EETLE_EGG_SILK), packedLightIn, OverlayTexture.NO_OVERLAY);
		matrixStack.pop();
		super.render(eggsEntity, entityYaw, partialTicks, matrixStack, buffer, packedLightIn);
	}

	@Override
	public ResourceLocation getEntityTexture(EetleEggsEntity entity) {
		return EetleEggsTileEntityRenderer.TEXTURES[0];
	}
}
