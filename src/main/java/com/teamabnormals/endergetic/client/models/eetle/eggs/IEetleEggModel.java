package com.teamabnormals.endergetic.client.models.eetle.eggs;

import com.teamabnormals.endergetic.common.tileentities.EetleEggTileEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public interface IEetleEggModel {
	void render(PoseStack matrixStack, VertexConsumer builder, int packedLight, int packedOverlay, float partialTicks, EetleEggTileEntity.SackGrowth[] sackGrowths);

	void renderSilk(PoseStack matrixStack, VertexConsumer silkBuilder, int packedLight, int packedOverlay);
}
