package com.minecraftabnormals.endergetic.client.models.frisbloom;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.geom.ModelPart;

/**
 * ModelFrisbloomFlower - Endergized & SmellyModder
 * Created using Tabula 7.0.0
 */
public class FrisbloomFlowerModel {
	public ModelPart shapez;
	public ModelPart shapex;
	public ModelPart shapex_g;
	public ModelPart shapez_g;

	public FrisbloomFlowerModel() {
		int[] textureSizes = {64, 32};

		this.shapez_g = new ModelPart(textureSizes[0], textureSizes[1], 52, 0);
		this.shapez_g.setPos(0.0F, 8.0F, -3.0F);
		this.shapez_g.addBox(0.0F, 0.0F, 0.0F, 0, 16, 6, 0.0F);
		this.shapez = new ModelPart(textureSizes[0], textureSizes[1], 13, 0);
		this.shapez.setPos(0.0F, 8.0F, -3.0F);
		this.shapez.addBox(0.0F, 0.0F, 0.0F, 0, 16, 6, 0.0F);
		this.shapex_g = new ModelPart(textureSizes[0], textureSizes[1], 39, 0);
		this.shapex_g.setPos(3.0F, 8.0F, 0.0F);
		this.shapex_g.addBox(0.0F, 0.0F, 0.0F, 0, 16, 6, 0.0F);
		this.setRotateAngle(shapex_g, 0.0F, -1.5707963267948966F, 0.0F);
		this.shapex = new ModelPart(textureSizes[0], textureSizes[1], 0, 0);
		this.shapex.setPos(3.0F, 8.0F, 0.0F);
		this.shapex.addBox(0.0F, 0.0F, 0.0F, 0, 16, 6, 0.0F);
		this.setRotateAngle(shapex, 0.0F, -1.5707963267948966F, 0.0F);
	}

	public void renderAll(PoseStack matrix, VertexConsumer builder, int light, int overlay) {
		this.shapez_g.render(matrix, builder, 15728640, overlay);
		this.shapex_g.render(matrix, builder, 15728640, overlay);

		this.shapex.render(matrix, builder, light, overlay);
		this.shapez.render(matrix, builder, light, overlay);
	}

	public void setRotateAngle(ModelPart ModelRenderer, float x, float y, float z) {
		ModelRenderer.xRot = x;
		ModelRenderer.yRot = y;
		ModelRenderer.zRot = z;
	}
}