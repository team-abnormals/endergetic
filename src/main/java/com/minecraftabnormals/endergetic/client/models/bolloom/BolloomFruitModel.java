package com.minecraftabnormals.endergetic.client.models.bolloom;

import com.minecraftabnormals.abnormals_core.client.ClientInfo;
import com.minecraftabnormals.endergetic.common.entities.bolloom.BolloomFruitEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;

/**
 * ModelBolloomFruit - Endergized
 * Created using Tabula 7.0.0
 */
public class BolloomFruitModel<T extends BolloomFruitEntity> extends EntityModel<T> {
	public ModelRenderer vine_x;
	public ModelRenderer fruit;
	public ModelRenderer vine_z;
	public ModelRenderer vine_x_1;
	public ModelRenderer vine_z_1;
	public ModelRenderer vine_x_2;
	public ModelRenderer vine_z_2;
	public ModelRenderer vine_x_3;
	public ModelRenderer vine_z_3;
	public ModelRenderer vine_x_4;
	public ModelRenderer vine_z_4;
	public ModelRenderer vine_x_5;
	public ModelRenderer vine_z_5;
	public ModelRenderer vine_x_6;
	public ModelRenderer vine_z_6;
	public ModelRenderer flap;

	public BolloomFruitModel() {
		this.texWidth = 64;
		this.texHeight = 32;
		this.vine_z = new ModelRenderer(this, 0, 10);
		this.vine_z.setPos(0.0F, 0.0F, 0.0F);
		this.vine_z.addBox(0.0F, 0.0F, -3.0F, 0, 16, 6, 0.0F);
		this.setRotateAngle(vine_z, 0.0F, -1.5707963267948966F, 0.0F);
		this.vine_z_3 = new ModelRenderer(this, 0, 10);
		this.vine_z_3.setPos(0.0F, 0.0F, 0.0F);
		this.vine_z_3.addBox(0.0F, 0.0F, -3.0F, 0, 16, 6, 0.0F);
		this.setRotateAngle(vine_z_3, 0.0F, -1.5707963267948966F, 0.0F);
		this.vine_z_5 = new ModelRenderer(this, 0, 10);
		this.vine_z_5.setPos(0.0F, 0.0F, 0.0F);
		this.vine_z_5.addBox(0.0F, 0.0F, -3.0F, 0, 16, 6, 0.0F);
		this.setRotateAngle(vine_z_5, 0.0F, -1.5707963267948966F, 0.0F);
		this.fruit = new ModelRenderer(this, 0, 0);
		this.fruit.setPos(-4.0F, 16.0F, -4.0F);
		this.fruit.addBox(0.0F, 0.0F, 0.0F, 8, 8, 8, 0.0F);
		this.vine_z_1 = new ModelRenderer(this, 0, 10);
		this.vine_z_1.setPos(0.0F, 0.0F, 0.0F);
		this.vine_z_1.addBox(0.0F, 0.0F, -3.0F, 0, 16, 6, 0.0F);
		this.setRotateAngle(vine_z_1, 0.0F, -1.5707963267948966F, 0.0F);
		this.vine_x_2 = new ModelRenderer(this, 13, 10);
		this.vine_x_2.setPos(0.0F, 16.0F, 0.0F);
		this.vine_x_2.addBox(0.0F, 0.0F, -3.0F, 0, 16, 6, 0.0F);
		this.flap = new ModelRenderer(this, 20, 4);
		this.flap.setPos(-2.0F, 8.1F, -2.0F);
		this.flap.addBox(0.0F, 0.0F, 0.0F, 12, 0, 12, 0.0F);
		this.vine_x_4 = new ModelRenderer(this, 13, 10);
		this.vine_x_4.setPos(0.0F, 16.0F, 0.0F);
		this.vine_x_4.addBox(0.0F, 0.0F, -3.0F, 0, 16, 6, 0.0F);
		this.vine_x_1 = new ModelRenderer(this, 13, 10);
		this.vine_x_1.setPos(0.0F, 16.0F, 0.0F);
		this.vine_x_1.addBox(0.0F, 0.0F, -3.0F, 0, 16, 6, 0.0F);
		this.vine_z_2 = new ModelRenderer(this, 0, 10);
		this.vine_z_2.setPos(0.0F, 0.0F, 0.0F);
		this.vine_z_2.addBox(0.0F, 0.0F, -3.0F, 0, 16, 6, 0.0F);
		this.setRotateAngle(vine_z_2, 0.0F, -1.5707963267948966F, 0.0F);
		this.vine_z_6 = new ModelRenderer(this, 0, 10);
		this.vine_z_6.setPos(0.0F, 0.0F, 0.0F);
		this.vine_z_6.addBox(0.0F, 0.0F, -3.0F, 0, 16, 6, 0.0F);
		this.setRotateAngle(vine_z_6, 0.0F, -1.5707963267948966F, 0.0F);
		this.vine_x_3 = new ModelRenderer(this, 13, 10);
		this.vine_x_3.setPos(0.0F, 16.0F, 0.0F);
		this.vine_x_3.addBox(0.0F, 0.0F, -3.0F, 0, 16, 6, 0.0F);
		this.vine_z_4 = new ModelRenderer(this, 0, 10);
		this.vine_z_4.setPos(0.0F, 0.0F, 0.0F);
		this.vine_z_4.addBox(0.0F, 0.0F, -3.0F, 0, 16, 6, 0.0F);
		this.setRotateAngle(vine_z_4, 0.0F, -1.5707963267948966F, 0.0F);
		this.vine_x = new ModelRenderer(this, 13, 10);
		this.vine_x.setPos(0.0F, 24.0F, 0.0F);
		this.vine_x.addBox(0.0F, 0.0F, -3.0F, 0, 16, 6, 0.0F);
		this.vine_x_6 = new ModelRenderer(this, 13, 10);
		this.vine_x_6.setPos(0.0F, 16.0F, 0.0F);
		this.vine_x_6.addBox(0.0F, 0.0F, -3.0F, 0, 16, 6, 0.0F);
		this.vine_x_5 = new ModelRenderer(this, 13, 10);
		this.vine_x_5.setPos(0.0F, 16.0F, 0.0F);
		this.vine_x_5.addBox(0.0F, 0.0F, -3.0F, 0, 16, 6, 0.0F);
		this.vine_x.addChild(this.vine_z);
		this.vine_x_3.addChild(this.vine_z_3);
		this.vine_x_5.addChild(this.vine_z_5);
		this.vine_x_1.addChild(this.vine_z_1);
		this.vine_z_1.addChild(this.vine_x_2);
		this.fruit.addChild(this.flap);
		this.vine_z_3.addChild(this.vine_x_4);
		this.vine_z.addChild(this.vine_x_1);
		this.vine_x_2.addChild(this.vine_z_2);
		this.vine_x_6.addChild(this.vine_z_6);
		this.vine_z_2.addChild(this.vine_x_3);
		this.vine_x_4.addChild(this.vine_z_4);
		this.vine_z_5.addChild(this.vine_x_6);
		this.vine_z_4.addChild(this.vine_x_5);
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		this.fruit.render(matrixStackIn, bufferIn, 240, packedOverlayIn, red, green, blue, alpha);
	}

	public void renderVine(MatrixStack matrix, IVertexBuilder vertexBuilder, int light) {
		this.vine_x.render(matrix, vertexBuilder, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public void setupAnim(T fruit, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float[] angles = fruit.getVineAnimation(ClientInfo.getPartialTicks());
		this.vine_x.xRot = angles[0];
		this.vine_x.yRot = angles[1];

		int height = fruit.getVineHeight();

		this.vine_x.visible = true;
		this.vine_z.visible = true;
		this.vine_x_1.visible = true;
		this.vine_z_1.visible = true;
		this.vine_x_2.visible = true;
		this.vine_z_2.visible = true;
		this.vine_x_3.visible = true;
		this.vine_z_3.visible = true;
		this.vine_x_4.visible = true;
		this.vine_z_4.visible = true;
		this.vine_x_5.visible = true;
		this.vine_z_5.visible = true;
		this.vine_x_6.visible = true;
		this.vine_z_6.visible = true;

		switch (height) {
			case 1:
				this.vine_x_1.visible = false;
				this.vine_z_1.visible = false;
				break;
			case 2:
				this.vine_x_2.visible = false;
				this.vine_z_2.visible = false;
				break;
			case 3:
				this.vine_x_3.visible = false;
				this.vine_z_3.visible = false;
				break;
			case 4:
				this.vine_x_4.visible = false;
				this.vine_z_4.visible = false;
				break;
			case 5:
				this.vine_x_5.visible = false;
				this.vine_z_5.visible = false;
				break;
			case 6:
				this.vine_x_6.visible = false;
				this.vine_z_6.visible = false;
				break;
		}
	}

	public void setRotateAngle(ModelRenderer ModelRenderer, float x, float y, float z) {
		ModelRenderer.xRot = x;
		ModelRenderer.yRot = y;
		ModelRenderer.zRot = z;
	}
}
