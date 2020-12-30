package com.minecraftabnormals.endergetic.client.models.eetle.eggs;

import com.minecraftabnormals.endergetic.common.tileentities.EetleEggsTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

public interface IEetleEggsModel {
	void render(MatrixStack matrixStack, IVertexBuilder builder, int packedLight, int packedOverlay, float partialTicks, EetleEggsTileEntity.SackGrowth[] sackGrowths);
}
