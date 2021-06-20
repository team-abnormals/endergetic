package com.minecraftabnormals.endergetic.client.models.eetle.eggs;

import com.minecraftabnormals.endergetic.common.tileentities.EetleEggTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

public interface IEetleEggModel {
	void render(MatrixStack matrixStack, IVertexBuilder builder, int packedLight, int packedOverlay, float partialTicks, EetleEggTileEntity.SackGrowth[] sackGrowths);

	void renderSilk(MatrixStack matrixStack, IVertexBuilder silkBuilder, int packedLight, int packedOverlay);
}
