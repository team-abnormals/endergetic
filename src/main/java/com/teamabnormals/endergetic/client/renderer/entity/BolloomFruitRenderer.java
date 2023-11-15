package com.teamabnormals.endergetic.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.teamabnormals.endergetic.client.model.bolloom.BolloomFruitModel;
import com.teamabnormals.endergetic.common.entity.bolloom.BolloomFruit;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import com.teamabnormals.endergetic.core.registry.other.EERenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class BolloomFruitRenderer extends EntityRenderer<BolloomFruit> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/bolloom_fruit.png");
	private final BolloomFruitModel<BolloomFruit> model;

	public BolloomFruitRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.model = new BolloomFruitModel<>(context.bakeLayer(BolloomFruitModel.LOCATION));
	}

	@Override
	public void render(BolloomFruit entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource bufferIn, int packedLightIn) {
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
	public ResourceLocation getTextureLocation(BolloomFruit entity) {
		return TEXTURE;
	}
}