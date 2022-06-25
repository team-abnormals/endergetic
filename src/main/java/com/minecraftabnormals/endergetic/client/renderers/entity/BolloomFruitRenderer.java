package com.minecraftabnormals.endergetic.client.renderers.entity;

import com.minecraftabnormals.endergetic.client.EERenderTypes;
import com.minecraftabnormals.endergetic.client.models.bolloom.BolloomFruitModel;
import com.minecraftabnormals.endergetic.common.entities.bolloom.BolloomFruitEntity;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import com.mojang.math.Vector3f;

public class BolloomFruitRenderer extends EntityRenderer<BolloomFruitEntity> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/bolloom_fruit.png");
	private final BolloomFruitModel<BolloomFruitEntity> model;

	public BolloomFruitRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.model = new BolloomFruitModel<>(context.bakeLayer(BolloomFruitModel.LOCATION));
	}

	@Override
	public void render(BolloomFruitEntity entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource bufferIn, int packedLightIn) {
		matrixStack.pushPose();
		matrixStack.translate(0.0F, 1.5F, 0.0F);
		matrixStack.mulPose(Vector3f.XP.rotationDegrees(180.0F));

		VertexConsumer ivertexbuilder = bufferIn.getBuffer(this.model.renderType(this.getTextureLocation(entity)));
		this.model.setupAnim(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
		this.model.renderToBuffer(matrixStack, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		this.model.renderVine(matrixStack, bufferIn.getBuffer(EERenderTypes.BOLLOOM_VINE), packedLightIn);

		matrixStack.popPose();
		super.render(entity, entityYaw, partialTicks, matrixStack, bufferIn, packedLightIn);
	}

	@Override
	public ResourceLocation getTextureLocation(BolloomFruitEntity entity) {
		return TEXTURE;
	}
}