package com.minecraftabnormals.endergetic.client.models;

import com.minecraftabnormals.endergetic.common.entities.PoiseClusterEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

/**
 * ModelPoiseBlock - Endergized
 * Created using Tabula 7.0.0
 */
public class PoiseClusterModel<T extends PoiseClusterEntity> extends EntityModel<T> {
	public ModelRenderer cube;

	public PoiseClusterModel() {
		this.texWidth = 64;
		this.texHeight = 32;
		this.cube = new ModelRenderer(this, 0, 0);
		this.cube.setPos(0.0F, 16.0F, 0.0F);
		this.cube.addBox(-8.0F, -8.0F, -8.0F, 16, 16, 16, 0.0F);
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		this.cube.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, 0.3F);
	}

	@Override
	public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}

	public void setRotateAngle(ModelRenderer ModelRenderer, float x, float y, float z) {
		ModelRenderer.xRot = x;
		ModelRenderer.yRot = y;
		ModelRenderer.zRot = z;
	}
}
