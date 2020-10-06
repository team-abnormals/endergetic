package com.minecraftabnormals.endergetic.client.models;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.model.ModelRenderer;

/**
 * ModelBoofBlockDispenser - Endergized
 * Created using Tabula 7.0.0
 */
public class BoofBlockDispenserModel {
	public ModelRenderer base;

	public BoofBlockDispenserModel() {
		this.base = new ModelRenderer(64, 32, 0, 0);
		this.base.setRotationPoint(0.0F, 18.0F, 2.0F);
		this.base.addBox(-6.0F, -6.0F, -6.0F, 12, 12, 12, 0.0F);
	}

	public void renderAll(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn) {
		this.base.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
	}

	/**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	public void setRotateAngle(ModelRenderer RendererModel, float x, float y, float z) {
		RendererModel.rotateAngleX = x;
		RendererModel.rotateAngleY = y;
		RendererModel.rotateAngleZ = z;
	}
}
