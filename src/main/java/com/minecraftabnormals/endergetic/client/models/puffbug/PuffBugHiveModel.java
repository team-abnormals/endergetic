package com.minecraftabnormals.endergetic.client.models.puffbug;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.model.ModelRenderer;

/**
 * ModelPuffBugHive - Endergized
 * Created using Tabula 7.0.0
 */
public class PuffBugHiveModel {
	public ModelRenderer HiveBase;
	public ModelRenderer HiveBottom;

	public PuffBugHiveModel() {
		int[] textureSize = {100, 64};
		this.HiveBottom = new ModelRenderer(textureSize[0], textureSize[1], 0, 30);
		this.HiveBottom.setPos(-7.0F, 21.0F, -7.0F);
		this.HiveBottom.addBox(0.0F, 0.0F, 0.0F, 14, 3, 14, 0.0F);
		this.HiveBase = new ModelRenderer(textureSize[0], textureSize[1], 0, 1);
		this.HiveBase.setPos(-8.0F, 8.0F, -8.0F);
		this.HiveBase.addBox(0.0F, 0.0F, 0.0F, 16, 13, 16, 0.0F);
	}

	public void renderAll(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn) {
		this.HiveBottom.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
		this.HiveBase.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
	}

	public void setRotateAngle(ModelRenderer ModelRenderer, float x, float y, float z) {
		ModelRenderer.xRot = x;
		ModelRenderer.yRot = y;
		ModelRenderer.zRot = z;
	}
}
