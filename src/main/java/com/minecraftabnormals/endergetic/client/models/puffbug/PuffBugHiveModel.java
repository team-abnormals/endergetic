package com.minecraftabnormals.endergetic.client.models.puffbug;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.geom.ModelPart;

/**
 * ModelPuffBugHive - Endergized
 * Created using Tabula 7.0.0
 */
public class PuffBugHiveModel {
	public ModelPart HiveBase;
	public ModelPart HiveBottom;

	public PuffBugHiveModel() {
		int[] textureSize = {100, 64};
		this.HiveBottom = new ModelPart(textureSize[0], textureSize[1], 0, 30);
		this.HiveBottom.setPos(-7.0F, 21.0F, -7.0F);
		this.HiveBottom.addBox(0.0F, 0.0F, 0.0F, 14, 3, 14, 0.0F);
		this.HiveBase = new ModelPart(textureSize[0], textureSize[1], 0, 1);
		this.HiveBase.setPos(-8.0F, 8.0F, -8.0F);
		this.HiveBase.addBox(0.0F, 0.0F, 0.0F, 16, 13, 16, 0.0F);
	}

	public void renderAll(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn) {
		this.HiveBottom.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
		this.HiveBase.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
	}

	public void setRotateAngle(ModelPart ModelRenderer, float x, float y, float z) {
		ModelRenderer.xRot = x;
		ModelRenderer.yRot = y;
		ModelRenderer.zRot = z;
	}
}
