package com.minecraftabnormals.endergetic.client.models.corrock;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.geom.ModelPart;

/**
 * ModelCorrockCrownWall - Endergized & SmellyModder
 * Created using Tabula 7.0.0
 */
public class CorrockCrownWallModel {
	public ModelPart shape1;
	public ModelPart shape1_1;

	public CorrockCrownWallModel() {
		int[] textureSizes = {64, 32};

		this.shape1_1 = new ModelPart(textureSizes[0], textureSizes[1], 0, 0);
		this.shape1_1.setPos(-8.0F, 16.0F, -7.0F);
		this.shape1_1.addBox(0.0F, -16.0F, 0.0F, 16, 16, 0, 0.0F);
		this.setRotateAngle(shape1_1, -0.3490658503988659F, 0.0F, 0.0F);
		this.shape1 = new ModelPart(textureSizes[0], textureSizes[1], 0, 0);
		this.shape1.mirror = true;
		this.shape1.setPos(-8.0F, 16.0F, -7.0F);
		this.shape1.addBox(0.0F, -16.0F, 0.0F, 16, 16, 0, 0.0F);
		this.setRotateAngle(shape1, -0.6981317007977318F, 0.0F, 0.0F);
	}

	public void renderAll(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn) {
		this.shape1_1.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
		this.shape1.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
	}

	public void setRotateAngle(ModelPart ModelRenderer, float x, float y, float z) {
		ModelRenderer.xRot = x;
		ModelRenderer.yRot = y;
		ModelRenderer.zRot = z;
	}
}
