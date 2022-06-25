package com.minecraftabnormals.endergetic.client.renderers.tile;

import com.minecraftabnormals.endergetic.client.models.BoofBlockDispenserModel;
import com.minecraftabnormals.endergetic.common.blocks.poise.boof.DispensedBoofBlock;
import com.minecraftabnormals.endergetic.common.tileentities.boof.DispensedBlockBoofTileEntity;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import com.mojang.math.Vector3f;

public class DispensedBoofBlockTileEntityRenderer implements BlockEntityRenderer<DispensedBlockBoofTileEntity> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/block/boof_block_dispensed.png");
	private final BoofBlockDispenserModel model;

	public DispensedBoofBlockTileEntityRenderer(BlockEntityRendererProvider.Context context) {
		this.model = new BoofBlockDispenserModel(context.bakeLayer(BoofBlockDispenserModel.LOCATION));
	}

	@Override
	public void render(DispensedBlockBoofTileEntity boof, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLightIn, int combinedOverlayIn) {
		Direction facing = boof.hasLevel() ? boof.getBlockState().getValue(DispensedBoofBlock.FACING) : Direction.NORTH;

		matrixStack.pushPose();
		matrixStack.translate(0.5F, 1.5F, 0.5F);

		if (facing.getAxis().isVertical()) {
			float offset = -facing.getAxisDirection().getStep();
			matrixStack.mulPose(Vector3f.XP.rotationDegrees(90.0F * offset));
			matrixStack.translate(0.0F, 1.125F, 1.0F * offset);
		} else {
			matrixStack.mulPose(Vector3f.YP.rotationDegrees(-facing.toYRot()));
		}

		matrixStack.scale(1.0F, -1.0F, -1.0F);

		VertexConsumer ivertexbuilder = buffer.getBuffer(RenderType.entityCutout(TEXTURE));
		this.model.renderAll(matrixStack, ivertexbuilder, combinedLightIn, combinedOverlayIn);

		matrixStack.popPose();
	}
}