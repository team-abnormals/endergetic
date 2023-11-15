package com.teamabnormals.endergetic.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamabnormals.endergetic.client.model.bolloom.BolloomKnotModel;
import com.teamabnormals.endergetic.common.entity.bolloom.BolloomKnot;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class BolloomKnotRenderer extends EntityRenderer<BolloomKnot> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/bolloom_knot.png");
	public BolloomKnotModel<BolloomKnot> model;

	public BolloomKnotRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.model = new BolloomKnotModel<>(context.bakeLayer(BolloomKnotModel.LOCATION));
	}

	@Override
	public void render(BolloomKnot entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource bufferIn, int packedLightIn) {
		matrixStack.pushPose();
		matrixStack.translate(0.0F, -1.31F, 0.0F);
		VertexConsumer ivertexbuilder = bufferIn.getBuffer(this.model.renderType(this.getTextureLocation(entity)));
		this.model.renderToBuffer(matrixStack, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		matrixStack.popPose();
		super.render(entity, entityYaw, partialTicks, matrixStack, bufferIn, packedLightIn);
	}

	@Override
	public ResourceLocation getTextureLocation(BolloomKnot arg0) {
		return TEXTURE;
	}
}