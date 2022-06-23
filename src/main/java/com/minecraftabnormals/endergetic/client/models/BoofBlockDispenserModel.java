package com.minecraftabnormals.endergetic.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.geom.ModelPart;

/**
 * ModelBoofBlockDispenser - Endergized
 * Created using Tabula 7.0.0
 */
public class BoofBlockDispenserModel {
	public ModelPart base;

	public BoofBlockDispenserModel() {
		this.base = new ModelPart(64, 32, 0, 0);
		this.base.setPos(0.0F, 18.0F, 2.0F);
		this.base.addBox(-6.0F, -6.0F, -6.0F, 12, 12, 12, 0.0F);
	}

	public void renderAll(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn) {
		this.base.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
	}

	/**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	public void setRotateAngle(ModelPart RendererModel, float x, float y, float z) {
		RendererModel.xRot = x;
		RendererModel.yRot = y;
		RendererModel.zRot = z;
	}
}
