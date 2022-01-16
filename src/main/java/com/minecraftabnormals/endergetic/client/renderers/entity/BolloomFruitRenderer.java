package com.minecraftabnormals.endergetic.client.renderers.entity;

import com.minecraftabnormals.endergetic.client.EERenderTypes;
import com.minecraftabnormals.endergetic.client.models.bolloom.BolloomFruitModel;
import com.minecraftabnormals.endergetic.common.entities.bolloom.BolloomFruitEntity;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

public class BolloomFruitRenderer extends EntityRenderer<BolloomFruitEntity> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/bolloom_fruit.png");
	private final BolloomFruitModel<BolloomFruitEntity> model;

	public BolloomFruitRenderer(EntityRendererManager renderManager) {
		super(renderManager);
		this.model = new BolloomFruitModel<BolloomFruitEntity>();
	}

	@Override
	public void render(BolloomFruitEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int packedLightIn) {
		matrixStack.pushPose();
		matrixStack.translate(0.0F, 1.5F, 0.0F);
		matrixStack.mulPose(Vector3f.XP.rotationDegrees(180.0F));

		IVertexBuilder ivertexbuilder = bufferIn.getBuffer(this.model.renderType(this.getTextureLocation(entity)));
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