package com.minecraftabnormals.endergetic.client.models.bolloom;

import com.minecraftabnormals.endergetic.common.entities.bolloom.BolloomBalloonEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import com.teamabnormals.abnormals_core.client.ClientInfo;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;

/**
 * ModelBolloomBalloon - Endergized
 * Created using Tabula 7.0.0
 */
public class BolloomBalloonModel<T extends BolloomBalloonEntity> extends EntityModel<T> {
	public ModelRenderer balloon;
	public ModelRenderer x_string;
	public ModelRenderer z_string;
	public ModelRenderer x_string_2;
	public ModelRenderer z_string_2;

	public BolloomBalloonModel() {
		this.textureHeight = 32;
		this.textureWidth = 32;
		this.z_string_2 = new ModelRenderer(this, 13, 10);
		this.z_string_2.setRotationPoint(3.0F, 0.0F, 0.0F);
		this.z_string_2.addBox(0.0F, 0.0F, 0.0F, 0, 16, 6, 0.0F);
		this.setRotateAngle(z_string_2, 0.0F, -1.5707963267948966F, 0.0F);
		this.z_string = new ModelRenderer(this, 0, 10);
		this.z_string.setRotationPoint(3.0F, 0.0F, 0.0F);
		this.z_string.addBox(0.0F, 0.0F, 0.0F, 0, 16, 6, 0.0F);
		this.setRotateAngle(z_string, 0.0F, -1.5707963267948966F, 0.0F);
		this.x_string = new ModelRenderer(this, 0, 10);
		this.x_string.setRotationPoint(0.0F, 24.0F, 0.0F);
		this.x_string.addBox(0.0F, 0.0F, -3.0F, 0, 16, 6, 0.0F);
		this.balloon = new ModelRenderer(this, 0, 0);
		this.balloon.setRotationPoint(-4.0F, 16.0F, -4.0F);
		this.balloon.addBox(0.0F, 0.0F, 0.0F, 8, 8, 8, 0.0F);
		this.x_string_2 = new ModelRenderer(this, 13, 10);
		this.x_string_2.setRotationPoint(0.0F, 16.0F, 3.0F);
		this.x_string_2.addBox(0.0F, 0.0F, -3.0F, 0, 16, 6, 0.0F);
		this.x_string_2.addChild(this.z_string_2);
		this.x_string.addChild(this.z_string);
		this.z_string.addChild(this.x_string_2);
	}

	@Override
	public void render(MatrixStack matrix, IVertexBuilder arg1, int f, int f1, float f2, float f3, float f4, float f5) {
		this.balloon.render(matrix, arg1, 240, f1, f5, f5, f5, f5);
	}

	public void renderString(MatrixStack matrix, IVertexBuilder vertexBuilder, int light) {
		this.x_string.render(matrix, vertexBuilder, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public void setRotationAngles(T balloon, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float[] angles = balloon.getVineAnimation(ClientInfo.getPartialTicks());
		this.x_string.rotateAngleX = angles[0];
		this.x_string.rotateAngleY = angles[1];
	}

	/**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	public void setRotateAngle(ModelRenderer ModelRenderer, float x, float y, float z) {
		ModelRenderer.rotateAngleX = x;
		ModelRenderer.rotateAngleY = y;
		ModelRenderer.rotateAngleZ = z;
	}
}