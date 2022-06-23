package com.minecraftabnormals.endergetic.client.models;

import com.minecraftabnormals.endergetic.common.entities.BoofBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;

/**
 * ModelBoofBlock - Endergized
 * Created using Tabula 7.0.0
 */
public class BoofBlockModel<T extends BoofBlockEntity> extends EntityModel<T> {
	public ModelPart base;

	public BoofBlockModel() {
		this.texWidth = 130;
		this.texHeight = 64;
		this.base = new ModelPart(this, 0, 0);
		this.base.setPos(-14.0F, 0.0F, -14.0F);
		this.base.addBox(0.0F, 0.0F, 0.0F, 28, 28, 28, 0.0F);
	}

	@Override
	public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		this.base.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}

	@Override
	public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}

	/**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	public void setRotateAngle(ModelPart ModelRenderer, float x, float y, float z) {
		ModelRenderer.xRot = x;
		ModelRenderer.yRot = y;
		ModelRenderer.zRot = z;
	}
}
