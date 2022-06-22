package com.minecraftabnormals.endergetic.client.models.bolloom;

import com.minecraftabnormals.endergetic.common.tileentities.BolloomBudTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.model.ModelRenderer;

/**
 * ModelBolloomBudOpen - Endergized
 * Created using Tabula 7.0.0
 */
public class BolloomBudModel {
	public ModelRenderer Center;
	public ModelRenderer Pedal;
	public ModelRenderer Pedal_1;
	public ModelRenderer Pedal_2;
	public ModelRenderer Pedal_3;
	public ModelRenderer Pedal_open;
	public ModelRenderer Pedal_1_open;
	public ModelRenderer Pedal_2_open;
	public ModelRenderer Pedal_3_open;

	public BolloomBudModel() {
		int[] textureSize = {64, 32};

		this.Pedal = new ModelRenderer(textureSize[0], textureSize[1], 0, 18);
		this.Pedal.setPos(-6.0F, 21.0F, 7.0F);
		this.Pedal.addBox(0.0F, 0.0F, 0.0F, 12, 2, 12, 0.0F);
		this.setRotateAngle(Pedal, 1.780235837034216F, 0.0F, 0.0F);
		this.Pedal_3 = new ModelRenderer(textureSize[0], textureSize[1], 0, 18);
		this.Pedal_3.setPos(-7.0F, 21.0F, -6.0F);
		this.Pedal_3.addBox(0.0F, 0.0F, 0.0F, 12, 2, 12, 0.0F);
		this.setRotateAngle(Pedal_3, 1.780235837034216F, -1.5707963267948966F, 0.0F);
		this.Pedal_1 = new ModelRenderer(textureSize[0], textureSize[1], 0, 18);
		this.Pedal_1.setPos(7.0F, 21.0F, 6.0F);
		this.Pedal_1.addBox(0.0F, 0.0F, 0.0F, 12, 2, 12, 0.0F);
		this.setRotateAngle(Pedal_1, 1.780235837034216F, 1.5707963267948966F, 0.0F);
		this.Pedal_2 = new ModelRenderer(textureSize[0], textureSize[1], 0, 18);
		this.Pedal_2.setPos(6.0F, 21.0F, -7.0F);
		this.Pedal_2.addBox(0.0F, 0.0F, 0.0F, 12, 2, 12, 0.0F);
		this.setRotateAngle(Pedal_2, 1.780235837034216F, 3.141592653589793F, 0.0F);
		this.Center = new ModelRenderer(textureSize[0], textureSize[1], 0, 0);
		this.Center.setPos(-7.0F, 21.0F, -7.0F);
		this.Center.addBox(0.0F, 0.0F, 0.0F, 14, 3, 14, 0.0F);
		this.Pedal_open = new ModelRenderer(textureSize[0], textureSize[1], 0, 18);
		this.Pedal_open.setPos(-6.0F, 21.0F, 7.0F);
		this.Pedal_open.addBox(0.0F, 0.0F, 0.0F, 12, 2, 12, 0.0F);
		this.setRotateAngle(Pedal_open, 0F, 0.0F, 0.0F);
		this.Pedal_3_open = new ModelRenderer(textureSize[0], textureSize[1], 0, 18);
		this.Pedal_3_open.setPos(-7.0F, 21.0F, -6.0F);
		this.Pedal_3_open.addBox(0.0F, 0.0F, 0.0F, 12, 2, 12, 0.0F);
		this.setRotateAngle(Pedal_3_open, 0F, -1.5707963267948966F, 0.0F);
		this.Pedal_1_open = new ModelRenderer(textureSize[0], textureSize[1], 0, 18);
		this.Pedal_1_open.setPos(7.0F, 21.0F, 6.0F);
		this.Pedal_1_open.addBox(0.0F, 0.0F, 0.0F, 12, 2, 12, 0.0F);
		this.setRotateAngle(Pedal_1_open, 0, 1.5707963267948966F, 0.0F);
		this.Pedal_2_open = new ModelRenderer(textureSize[0], textureSize[1], 0, 18);
		this.Pedal_2_open.setPos(6.0F, 21.0F, -7.0F);
		this.Pedal_2_open.addBox(0.0F, 0.0F, 0.0F, 12, 2, 12, 0.0F);
		this.setRotateAngle(Pedal_2_open, 0, 3.141592653589793F, 0.0F);
	}

	public void renderAll(BolloomBudTileEntity bud, MatrixStack matrix, IVertexBuilder builder, int light, int overlay) {
		float angle = 1.78F * bud.pedalAnimation.getAnimationProgress();

		this.Pedal.xRot = angle;
		this.Pedal_1.xRot = angle;
		this.Pedal_2.xRot = angle;
		this.Pedal_3.xRot = angle;

		this.Pedal.render(matrix, builder, light, overlay);
		this.Pedal_1.render(matrix, builder, light, overlay);
		this.Pedal_2.render(matrix, builder, light, overlay);
		this.Pedal_3.render(matrix, builder, light, overlay);

		this.Pedal.xRot = 0.0F;
		this.Pedal_1.xRot = 0.0F;
		this.Pedal_2.xRot = 0.0F;
		this.Pedal_3.xRot = 0.0F;

		this.Center.render(matrix, builder, light, overlay);
	}

	public void setRotateAngle(ModelRenderer ModelRenderer, float x, float y, float z) {
		ModelRenderer.xRot = x;
		ModelRenderer.yRot = y;
		ModelRenderer.zRot = z;
	}
}