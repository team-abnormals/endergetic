package com.minecraftabnormals.endergetic.client.models.bolloom;

import com.minecraftabnormals.abnormals_core.client.ClientInfo;
import com.minecraftabnormals.endergetic.common.entities.bolloom.BolloomBalloonEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.texture.OverlayTexture;

/**
 * ModelBolloomBalloon - Endergized
 * Created using Tabula 7.0.0
 */
public class BolloomBalloonModel<T extends BolloomBalloonEntity> extends EntityModel<T> {
	public ModelPart balloon;
	public ModelPart x_string;
	public ModelPart z_string;
	public ModelPart x_string_2;
	public ModelPart z_string_2;

	public BolloomBalloonModel() {
		this.texHeight = 32;
		this.texWidth = 32;
		this.z_string_2 = new ModelPart(this, 13, 10);
		this.z_string_2.setPos(3.0F, 0.0F, 0.0F);
		this.z_string_2.addBox(0.0F, 0.0F, 0.0F, 0, 16, 6, 0.0F);
		this.setRotateAngle(z_string_2, 0.0F, -1.5707963267948966F, 0.0F);
		this.z_string = new ModelPart(this, 0, 10);
		this.z_string.setPos(3.0F, 0.0F, 0.0F);
		this.z_string.addBox(0.0F, 0.0F, 0.0F, 0, 16, 6, 0.0F);
		this.setRotateAngle(z_string, 0.0F, -1.5707963267948966F, 0.0F);
		this.x_string = new ModelPart(this, 0, 10);
		this.x_string.setPos(0.0F, 24.0F, 0.0F);
		this.x_string.addBox(0.0F, 0.0F, -3.0F, 0, 16, 6, 0.0F);
		this.balloon = new ModelPart(this, 0, 0);
		this.balloon.setPos(-4.0F, 16.0F, -4.0F);
		this.balloon.addBox(0.0F, 0.0F, 0.0F, 8, 8, 8, 0.0F);
		this.x_string_2 = new ModelPart(this, 13, 10);
		this.x_string_2.setPos(0.0F, 16.0F, 3.0F);
		this.x_string_2.addBox(0.0F, 0.0F, -3.0F, 0, 16, 6, 0.0F);
		this.x_string_2.addChild(this.z_string_2);
		this.x_string.addChild(this.z_string);
		this.z_string.addChild(this.x_string_2);
	}

	@Override
	public void renderToBuffer(PoseStack matrix, VertexConsumer arg1, int f, int f1, float f2, float f3, float f4, float f5) {
		this.balloon.render(matrix, arg1, 240, f1, f5, f5, f5, f5);
	}

	public void renderString(PoseStack matrix, VertexConsumer vertexBuilder, int light) {
		this.x_string.render(matrix, vertexBuilder, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public void setupAnim(T balloon, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float[] angles = balloon.getVineAnimation(ClientInfo.getPartialTicks());
		this.x_string.xRot = angles[0];
		this.x_string.yRot = angles[1];
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