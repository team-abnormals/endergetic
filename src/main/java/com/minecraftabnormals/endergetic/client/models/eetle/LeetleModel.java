package com.minecraftabnormals.endergetic.client.models.eetle;

import com.minecraftabnormals.endergetic.common.entities.eetle.AbstractEetleEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

/**
 * @author Endergized
 * @author SmellyModder (Luke Tonon)
 * <p>
 * Created using Tabula 7.0.0
 */
public class LeetleModel<E extends AbstractEetleEntity> extends EntityModel<E> {
	public ModelRenderer body;
	public ModelRenderer head;
	public ModelRenderer body2;
	public ModelRenderer booty;
	public ModelRenderer leftLeg1;
	public ModelRenderer rightLeg1;
	public ModelRenderer leftLeg2;
	public ModelRenderer rightLeg2;

	public LeetleModel() {
		this.textureWidth = 64;
		this.textureHeight = 32;
		this.body2 = new ModelRenderer(this, 0, 10);
		this.body2.setRotationPoint(0.0F, 0.5F, 5.0F);
		this.body2.addBox(-3.0F, -2.5F, 0.0F, 6, 5, 4, 0.0F);
		this.head = new ModelRenderer(this, 0, 0);
		this.head.setRotationPoint(0.0F, 1.0F, 0.0F);
		this.head.addBox(-2.5F, -2.0F, -4.0F, 5, 4, 4, 0.0F);
		this.rightLeg2 = new ModelRenderer(this, 0, 0);
		this.rightLeg2.setRotationPoint(-3.0F, 2.0F, 2.5F);
		this.rightLeg2.addBox(0.0F, 0.0F, -0.5F, 0, 2, 1, 0.0F);
		this.setRotateAngle(rightLeg2, 0.0F, 0.0F, 0.7853981633974483F);
		this.leftLeg1 = new ModelRenderer(this, 0, 0);
		this.leftLeg1.setRotationPoint(3.5F, 2.5F, 2.5F);
		this.leftLeg1.addBox(0.0F, 0.0F, -0.5F, 0, 2, 1, 0.0F);
		this.setRotateAngle(leftLeg1, 0.0F, 0.0F, -0.7853981633974483F);
		this.leftLeg2 = new ModelRenderer(this, 0, 0);
		this.leftLeg2.setRotationPoint(3.0F, 2.0F, 2.5F);
		this.leftLeg2.addBox(0.0F, 0.0F, -0.5F, 0, 2, 1, 0.0F);
		this.setRotateAngle(leftLeg2, 0.0F, 0.0F, -0.7853981633974483F);
		this.booty = new ModelRenderer(this, 22, 12);
		this.booty.setRotationPoint(0.0F, 0.5F, 4.0F);
		this.booty.addBox(-2.0F, -2.0F, 0.0F, 4, 4, 2, 0.0F);
		this.rightLeg1 = new ModelRenderer(this, 0, 0);
		this.rightLeg1.setRotationPoint(-3.5F, 2.5F, 2.5F);
		this.rightLeg1.addBox(0.0F, 0.0F, -0.5F, 0, 2, 1, 0.0F);
		this.setRotateAngle(rightLeg1, 0.0F, 0.0F, 0.7853981633974483F);
		this.body = new ModelRenderer(this, 19, 0);
		this.body.setRotationPoint(0.0F, 20.0F, -3.5F);
		this.body.addBox(-3.5F, -3.0F, 0.0F, 7, 6, 5, 0.0F);
		this.body.addChild(this.body2);
		this.body.addChild(this.head);
		this.body2.addChild(this.rightLeg2);
		this.body.addChild(this.leftLeg1);
		this.body2.addChild(this.leftLeg2);
		this.body2.addChild(this.booty);
		this.body.addChild(this.rightLeg1);
	}

	@Override
	public void setRotationAngles(E entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.rotateAngleY = netHeadYaw * ((float) Math.PI / 180.0F);
		this.head.rotateAngleX = headPitch * ((float) Math.PI / 180.0F);

		//Child mobs triple their limb swing
		limbSwing /= 3.0F;

		this.leftLeg1.rotateAngleX = this.rightLeg1.rotateAngleX = this.leftLeg2.rotateAngleX = this.rightLeg2.rotateAngleX = 0.0F;
		float frontLegAnglesX = MathHelper.sin(limbSwing * 1.5F + ((float) Math.PI * 1.5F)) * limbSwingAmount;
		this.leftLeg1.rotateAngleX = frontLegAnglesX;
		this.rightLeg1.rotateAngleX = frontLegAnglesX;

		float frontLegAnglesZ = Math.abs(MathHelper.sin(limbSwing * 0.75F + (float) Math.PI) * 1.5F) * limbSwingAmount;
		this.leftLeg1.rotateAngleZ = -0.785F - frontLegAnglesZ;
		this.rightLeg1.rotateAngleZ = 0.785F + frontLegAnglesZ;

		float backLegAnglesX = MathHelper.sin(limbSwing * 1.5F - ((float) Math.PI * 1.5F)) * limbSwingAmount;
		this.leftLeg2.rotateAngleX = backLegAnglesX;
		this.rightLeg2.rotateAngleX = backLegAnglesX;

		float backLegAnglesZ = Math.abs(MathHelper.sin(limbSwing * 0.75F + (float) Math.PI) * 1.5F) * limbSwingAmount;
		this.leftLeg2.rotateAngleZ = -0.785F - backLegAnglesZ;
		this.rightLeg2.rotateAngleZ = 0.785F + backLegAnglesZ;
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder vertexBuilder, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.body.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
