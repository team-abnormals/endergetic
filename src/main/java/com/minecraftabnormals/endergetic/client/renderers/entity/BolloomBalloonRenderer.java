package com.minecraftabnormals.endergetic.client.renderers.entity;

import com.minecraftabnormals.endergetic.client.EERenderTypes;
import com.minecraftabnormals.endergetic.client.models.bolloom.BolloomBalloonModel;
import com.minecraftabnormals.endergetic.common.entities.bolloom.BolloomBalloonEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.CameraType;
import net.minecraft.resources.ResourceLocation;
import com.mojang.math.Vector3f;

public final class BolloomBalloonRenderer extends EntityRenderer<BolloomBalloonEntity> {
	private static final Minecraft MC = Minecraft.getInstance();
	private final BolloomBalloonModel<BolloomBalloonEntity> model;

	public BolloomBalloonRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.model = new BolloomBalloonModel<>(context.bakeLayer(BolloomBalloonModel.LOCATION));
	}

	@Override
	public void render(BolloomBalloonEntity balloon, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource bufferIn, int packedLightIn) {
		matrixStack.pushPose();
		matrixStack.translate(0.0F, 1.5F, 0.0F);
		matrixStack.mulPose(Vector3f.XP.rotationDegrees(180.0F));

		VertexConsumer ivertexbuilder = bufferIn.getBuffer(this.model.renderType(this.getTextureLocation(balloon)));
		this.model.setupAnim(balloon, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
		this.model.renderToBuffer(matrixStack, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		this.model.renderString(matrixStack, bufferIn.getBuffer(EERenderTypes.BOLLOOM_STRING), packedLightIn);

		matrixStack.popPose();
		super.render(balloon, entityYaw, partialTicks, matrixStack, bufferIn, packedLightIn);
	}

	@Override
	public boolean shouldRender(BolloomBalloonEntity balloon, Frustum camera, double camX, double camY, double camZ) {
		if (!balloon.shouldRender(camX, camY, camZ)) {
			return false;
		}
		LocalPlayer player = MC.player;
		return balloon.getAttachedEntity() != player || MC.options.getCameraType() != CameraType.FIRST_PERSON || player.getXRot() < -45.0F;
	}

	@Override
	public ResourceLocation getTextureLocation(BolloomBalloonEntity balloon) {
		return balloon.getColor().texture;
	}
}