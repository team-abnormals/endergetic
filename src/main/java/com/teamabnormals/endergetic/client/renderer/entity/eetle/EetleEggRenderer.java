package com.teamabnormals.endergetic.client.renderer.entity.eetle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.teamabnormals.endergetic.client.model.eetle.eggs.IEetleEggModel;
import com.teamabnormals.endergetic.client.model.eetle.eggs.LargeEetleEggModel;
import com.teamabnormals.endergetic.client.model.eetle.eggs.MediumEetleEggModel;
import com.teamabnormals.endergetic.client.model.eetle.eggs.SmallEetleEggModel;
import com.teamabnormals.endergetic.client.renderer.block.EetleEggTileEntityRenderer;
import com.teamabnormals.endergetic.common.entity.eetle.EetleEgg;
import com.teamabnormals.endergetic.core.registry.other.EERenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class EetleEggRenderer extends EntityRenderer<EetleEgg> {
	private final IEetleEggModel[] eggModels;

	public EetleEggRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.eggModels = new IEetleEggModel[]{
				new SmallEetleEggModel(context.bakeLayer(SmallEetleEggModel.LOCATION)),
				new MediumEetleEggModel(context.bakeLayer(MediumEetleEggModel.LOCATION)),
				new LargeEetleEggModel(context.bakeLayer(LargeEetleEggModel.LOCATION))
		};
	}

	@Override
	public void render(EetleEgg eggsEntity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLightIn) {
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
	public ResourceLocation getTextureLocation(EetleEgg entity) {
		return EetleEggTileEntityRenderer.TEXTURES[0];
	}
}
