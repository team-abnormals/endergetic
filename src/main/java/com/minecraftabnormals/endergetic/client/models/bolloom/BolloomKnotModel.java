package com.minecraftabnormals.endergetic.client.models.bolloom;

import com.minecraftabnormals.endergetic.common.entities.bolloom.BolloomKnotEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;

/**
 * ModelBolloomKnot - Endergized
 * Created using Tabula 7.0.0
 */
public class BolloomKnotModel<T extends BolloomKnotEntity> extends EntityModel<T> {
	public ModelPart knot;

	public BolloomKnotModel() {
		this.texWidth = 32;
		this.texHeight = 16;
		this.knot = new ModelPart(this, 0, 0);
		this.knot.setPos(-3.0F, 21.0F, -3.0F);
		this.knot.addBox(0.0F, 0.0F, 0.0F, 6, 3, 6, 0.0F);
	}

	@Override
	public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		this.knot.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}

	@Override
	public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}

	public void setRotateAngle(ModelPart RendererModel, float x, float y, float z) {
		RendererModel.xRot = x;
		RendererModel.yRot = y;
		RendererModel.zRot = z;
	}
}