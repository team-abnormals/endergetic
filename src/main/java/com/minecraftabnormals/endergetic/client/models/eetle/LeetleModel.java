package com.minecraftabnormals.endergetic.client.models.eetle;

import com.minecraftabnormals.abnormals_core.core.endimator.entity.EndimatorEntityModel;
import com.minecraftabnormals.abnormals_core.core.endimator.entity.EndimatorModelRenderer;
import com.minecraftabnormals.endergetic.common.entities.eetle.AbstractEetleEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.util.math.MathHelper;

/**
 * @author Endergized
 * @author SmellyModder (Luke Tonon)
 * <p>
 * Created using Tabula 7.0.0
 */
public class LeetleModel<E extends AbstractEetleEntity> extends EndimatorEntityModel<E> {
	public EndimatorModelRenderer body;
	public EndimatorModelRenderer head;
	public EndimatorModelRenderer body2;
	public EndimatorModelRenderer booty;
	public EndimatorModelRenderer leftLeg1;
	public EndimatorModelRenderer rightLeg1;
	public EndimatorModelRenderer leftLeg2;
	public EndimatorModelRenderer rightLeg2;

	public LeetleModel() {
		this.texWidth = 64;
		this.texHeight = 32;
		this.body2 = new EndimatorModelRenderer(this, 0, 10);
		this.body2.setPos(0.0F, 0.5F, 5.0F);
		this.body2.addBox(-3.0F, -2.5F, 0.0F, 6, 5, 4, 0.0F);
		this.head = new EndimatorModelRenderer(this, 0, 0);
		this.head.setPos(0.0F, 1.0F, 0.0F);
		this.head.addBox(-2.5F, -2.0F, -4.0F, 5, 4, 4, 0.0F);
		this.rightLeg2 = new EndimatorModelRenderer(this, 0, 0);
		this.rightLeg2.setPos(-3.0F, 2.0F, 2.5F);
		this.rightLeg2.addBox(0.0F, 0.0F, -0.5F, 0, 2, 1, 0.0F);
		this.setRotateAngle(rightLeg2, 0.0F, 0.0F, 0.7853981633974483F);
		this.leftLeg1 = new EndimatorModelRenderer(this, 0, 0);
		this.leftLeg1.setPos(3.5F, 2.5F, 2.5F);
		this.leftLeg1.addBox(0.0F, 0.0F, -0.5F, 0, 2, 1, 0.0F);
		this.setRotateAngle(leftLeg1, 0.0F, 0.0F, -0.7853981633974483F);
		this.leftLeg2 = new EndimatorModelRenderer(this, 0, 0);
		this.leftLeg2.setPos(3.0F, 2.0F, 2.5F);
		this.leftLeg2.addBox(0.0F, 0.0F, -0.5F, 0, 2, 1, 0.0F);
		this.setRotateAngle(leftLeg2, 0.0F, 0.0F, -0.7853981633974483F);
		this.booty = new EndimatorModelRenderer(this, 22, 12);
		this.booty.setPos(0.0F, 0.5F, 4.0F);
		this.booty.addBox(-2.0F, -2.0F, 0.0F, 4, 4, 2, 0.0F);
		this.rightLeg1 = new EndimatorModelRenderer(this, 0, 0);
		this.rightLeg1.setPos(-3.5F, 2.5F, 2.5F);
		this.rightLeg1.addBox(0.0F, 0.0F, -0.5F, 0, 2, 1, 0.0F);
		this.setRotateAngle(rightLeg1, 0.0F, 0.0F, 0.7853981633974483F);
		this.body = new EndimatorModelRenderer(this, 19, 0);
		this.body.setPos(0.0F, 20.0F, -3.5F);
		this.body.addBox(-3.5F, -3.0F, 0.0F, 7, 6, 5, 0.0F);
		this.body.addChild(this.body2);
		this.body.addChild(this.head);
		this.body2.addChild(this.rightLeg2);
		this.body.addChild(this.leftLeg1);
		this.body2.addChild(this.leftLeg2);
		this.body2.addChild(this.booty);
		this.body.addChild(this.rightLeg1);

		this.setDefaultBoxValues();
	}

	@Override
	public void setupAnim(E entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		this.head.yRot = netHeadYaw * ((float) Math.PI / 180.0F);
		this.head.xRot = headPitch * ((float) Math.PI / 180.0F);

		//Child mobs triple their limb swing
		limbSwing /= 3.0F;

		this.leftLeg1.xRot = this.rightLeg1.xRot = this.leftLeg2.xRot = this.rightLeg2.xRot = 0.0F;
		float frontLegAnglesX = MathHelper.sin(limbSwing * 1.5F + ((float) Math.PI * 1.5F)) * limbSwingAmount;
		this.leftLeg1.xRot = frontLegAnglesX;
		this.rightLeg1.xRot = frontLegAnglesX;

		float frontLegAnglesZ = Math.abs(MathHelper.sin(limbSwing * 0.75F + (float) Math.PI) * 1.5F) * limbSwingAmount;
		this.leftLeg1.zRot = -0.785F - frontLegAnglesZ;
		this.rightLeg1.zRot = 0.785F + frontLegAnglesZ;

		float backLegAnglesX = MathHelper.sin(limbSwing * 1.5F - ((float) Math.PI * 1.5F)) * limbSwingAmount;
		this.leftLeg2.xRot = backLegAnglesX;
		this.rightLeg2.xRot = backLegAnglesX;

		float backLegAnglesZ = Math.abs(MathHelper.sin(limbSwing * 0.75F + (float) Math.PI) * 1.5F) * limbSwingAmount;
		this.leftLeg2.zRot = -0.785F - backLegAnglesZ;
		this.rightLeg2.zRot = 0.785F + backLegAnglesZ;
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder vertexBuilder, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		super.renderToBuffer(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green, blue, alpha);
		this.body.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public void animateModel(E endimatedEntity) {
		super.animateModel(endimatedEntity);
		if (this.tryToPlayEndimation(AbstractEetleEntity.GROW_UP)) {
			this.startKeyframe(5);
			this.scale(this.body, 0.2F, 0.2F, 0.2F);
			this.endKeyframe();

			this.resetKeyframe(5);

			this.startKeyframe(5);
			this.scale(this.body, 0.25F, 0.25F, 0.25F);
			this.endKeyframe();

			this.resetKeyframe(5);

			this.startKeyframe(10);
			this.scale(this.body, 0.5F, 0.5F, 0.5F);
			this.endKeyframe();
		}
	}

	public void setRotateAngle(EndimatorModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}
