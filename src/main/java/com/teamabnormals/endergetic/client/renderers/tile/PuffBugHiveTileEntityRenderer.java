package com.teamabnormals.endergetic.client.renderers.tile;

import com.teamabnormals.endergetic.client.models.puffbug.PuffBugHiveModel;
import com.teamabnormals.endergetic.common.tileentities.PuffBugHiveTileEntity;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class PuffBugHiveTileEntityRenderer implements BlockEntityRenderer<PuffBugHiveTileEntity> {
	private final PuffBugHiveModel hiveModel;
	private static final ResourceLocation TEXTURE = new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/tile/puffbug_hive.png");

	public PuffBugHiveTileEntityRenderer(BlockEntityRendererProvider.Context context) {
		this.hiveModel = new PuffBugHiveModel(context.bakeLayer(PuffBugHiveModel.LOCATION));
	}

	@Override
	public void render(PuffBugHiveTileEntity hive, float partialTicks, PoseStack matrixStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
		matrixStack.pushPose();
		matrixStack.translate(0.5D, 1.5D, 0.5D);
		matrixStack.scale(1.0F, -1.0F, -1.0F);

		VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.entityCutout(TEXTURE));
		this.hiveModel.renderAll(matrixStack, ivertexbuilder, combinedLightIn, combinedOverlayIn);

		matrixStack.popPose();
	}
}