package com.minecraftabnormals.endergetic.client.models.frisbloom;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.geom.ModelPart;

/**
 * ModelFrisbloomStem - Endergized
 * Created using Tabula 7.0.0
 */
public class FrisbloomStemModel {
	public ModelPart frisbloom_stem_x;
	public ModelPart frisbloom_stem_y;
	public ModelPart frisbloom_small_top;
	public ModelPart frisbloom_small_bottom;
	public ModelPart frisbloom_medium_top;
	public ModelPart frisbloom_medium_bottom;
	public ModelPart frisbloom_large_top;
	public ModelPart frisbloom_large_bottom;

	public FrisbloomStemModel() {
		int[] textureSize = {64, 32};

		this.frisbloom_medium_bottom = new ModelPart(textureSize[0], textureSize[1], 16, 16);
		this.frisbloom_medium_bottom.setPos(-12.0F, 16.11F, -12.0F);
		this.frisbloom_medium_bottom.addBox(0.0F, 0.0F, 0.0F, 16, 0, 16, 0.0F);
		this.frisbloom_stem_x = new ModelPart(textureSize[0], textureSize[1], 13, -6);
		this.frisbloom_stem_x.setPos(0.0F, 8.0F, -3.0F);
		this.frisbloom_stem_x.addBox(0.0F, 0.0F, 0.0F, 0, 16, 6, 0.0F);
		this.frisbloom_small_top = new ModelPart(textureSize[0], textureSize[1], -16, 16);
		this.frisbloom_small_top.setPos(-8.0F, 16.0F, -8.0F);
		this.frisbloom_small_top.addBox(0.0F, 0.0F, 0.0F, 16, 0, 16, 0.0F);
		this.frisbloom_large_bottom = new ModelPart(textureSize[0], textureSize[1], 16, 16);
		this.frisbloom_large_bottom.setPos(-16.0F, 16.11F, -16.0F);
		this.frisbloom_large_bottom.addBox(0.0F, 0.0F, 0.0F, 16, 0, 16, 0.0F);
		this.frisbloom_large_top = new ModelPart(textureSize[0], textureSize[1], -16, 16);
		this.frisbloom_large_top.setPos(-16.0F, 16.0F, -16.0F);
		this.frisbloom_large_top.addBox(0.0F, 0.0F, 0.0F, 16, 0, 16, 0.0F);
		this.frisbloom_small_bottom = new ModelPart(textureSize[0], textureSize[1], 16, 16);
		this.frisbloom_small_bottom.setPos(-8.0F, 16.11F, -8.0F);
		this.frisbloom_small_bottom.addBox(0.0F, 0.0F, 0.0F, 16, 0, 16, 0.0F);
		this.frisbloom_stem_y = new ModelPart(textureSize[0], textureSize[1], 0, -6);
		this.frisbloom_stem_y.setPos(3.0F, 8.0F, 0.0F);
		this.frisbloom_stem_y.addBox(0.0F, 0.0F, 0.0F, 0, 16, 6, 0.0F);
		this.setRotateAngle(frisbloom_stem_y, 0.0F, -1.5707963267948966F, 0.0F);
		this.frisbloom_medium_top = new ModelPart(textureSize[0], textureSize[1], -16, 16);
		this.frisbloom_medium_top.setPos(-12.0F, 16.0F, -12.0F);
		this.frisbloom_medium_top.addBox(0.0F, 0.0F, 0.0F, 16, 0, 16, 0.0F);
	}

	public void renderStem(PoseStack matrix, VertexConsumer builder, int light, int overlay) {
		this.frisbloom_stem_y.render(matrix, builder, light, overlay);
		this.frisbloom_stem_x.render(matrix, builder, light, overlay);
	}

	public void renderFrisbloom(PoseStack matrix, VertexConsumer builder, int light, int overlay, int size, boolean isBottom) {
//    	if(isBottom) {
//    		if(size == 3) {
//    			this.frisbloom_small_bottom.render(matrix, builder, light, overlay);
//    		} else if(size == 2) {
//    			GlStateManager.pushMatrix();
//    			GlStateManager.translatef(this.frisbloom_medium_bottom.offsetX, this.frisbloom_medium_bottom.offsetY, this.frisbloom_medium_bottom.offsetZ);
//    			GlStateManager.translatef(this.frisbloom_medium_bottom.rotationPointX * 0.0625F, this.frisbloom_medium_bottom.rotationPointY * 0.0625F, this.frisbloom_medium_bottom.rotationPointZ * 0.0625F);
//    			GlStateManager.scaled(1.5D, 1.5D, 1.5D);
//    			GlStateManager.translatef(-this.frisbloom_medium_bottom.offsetX, -this.frisbloom_medium_bottom.offsetY, -this.frisbloom_medium_bottom.offsetZ);
//    			GlStateManager.translatef(-this.frisbloom_medium_bottom.rotationPointX * 0.0625F, -this.frisbloom_medium_bottom.rotationPointY * 0.0625F, -this.frisbloom_medium_bottom.rotationPointZ * 0.0625F);
//    			this.frisbloom_medium_bottom.render(matrix, builder, light, overlay);
//    			GlStateManager.popMatrix();
//    		} else {
//    			GlStateManager.pushMatrix();
//    			GlStateManager.translatef(this.frisbloom_large_bottom.offsetX, this.frisbloom_large_bottom.offsetY, this.frisbloom_large_bottom.offsetZ);
//    			GlStateManager.translatef(this.frisbloom_large_bottom.rotationPointX * 0.0625F, this.frisbloom_large_bottom.rotationPointY * 0.0625F, this.frisbloom_large_bottom.rotationPointZ * 0.0625F);
//    			GlStateManager.scaled(2.0D, 2.0D, 2.0D);
//    			GlStateManager.translatef(-this.frisbloom_large_bottom.offsetX, -this.frisbloom_large_bottom.offsetY, -this.frisbloom_large_bottom.offsetZ);
//    			GlStateManager.translatef(-this.frisbloom_large_bottom.rotationPointX * 0.0625F, -this.frisbloom_large_bottom.rotationPointY * 0.0625F, -this.frisbloom_large_bottom.rotationPointZ * 0.0625F);
//    			this.frisbloom_large_bottom.render(matrix, builder, light, overlay);
//    			GlStateManager.popMatrix();
//    		}
//    	} else {
//    		if(size == 3) {
//    			this.frisbloom_small_top.render(matrix, builder, light, overlay);
//    		} else if(size == 2) {
//    			GlStateManager.pushMatrix();
//    			GlStateManager.translatef(this.frisbloom_medium_top.offsetX, this.frisbloom_medium_top.offsetY, this.frisbloom_medium_top.offsetZ);
//    			GlStateManager.translatef(this.frisbloom_medium_top.rotationPointX * 0.0625F, this.frisbloom_medium_top.rotationPointY * 0.0625F, this.frisbloom_medium_top.rotationPointZ * 0.0625F);
//    			GlStateManager.scaled(1.5D, 1.5D, 1.5D);
//    			GlStateManager.translatef(-this.frisbloom_medium_top.offsetX, -this.frisbloom_medium_top.offsetY, -this.frisbloom_medium_top.offsetZ);
//    			GlStateManager.translatef(-this.frisbloom_medium_top.rotationPointX * 0.0625F, -this.frisbloom_medium_top.rotationPointY * 0.0625F, -this.frisbloom_medium_top.rotationPointZ * 0.0625F);
//    			this.frisbloom_medium_top.render(matrix, builder, light, overlay);
//    			GlStateManager.popMatrix();
//    		} else {
//    			GlStateManager.pushMatrix();
//    			GlStateManager.translatef(this.frisbloom_large_top.offsetX, this.frisbloom_large_top.offsetY, this.frisbloom_large_top.offsetZ);
//    			GlStateManager.translatef(this.frisbloom_large_top.rotationPointX * 0.0625F, this.frisbloom_large_top.rotationPointY * 0.0625F, this.frisbloom_large_top.rotationPointZ * 0.0625F);
//    			GlStateManager.scaled(2.0D, 2.0D, 2.0D);
//    			GlStateManager.translatef(-this.frisbloom_large_top.offsetX, -this.frisbloom_large_top.offsetY, -this.frisbloom_large_top.offsetZ);
//    			GlStateManager.translatef(-this.frisbloom_large_top.rotationPointX * 0.0625F, -this.frisbloom_large_top.rotationPointY * 0.0625F, -this.frisbloom_large_top.rotationPointZ * 0.0625F);
//    			this.frisbloom_large_top.render(matrix, builder, light, overlay);
//    			GlStateManager.popMatrix();
//    		}
//    	}
	}

	public void setRotateAngle(ModelPart ModelRenderer, float x, float y, float z) {
		ModelRenderer.xRot = x;
		ModelRenderer.yRot = y;
		ModelRenderer.zRot = z;
	}
}
