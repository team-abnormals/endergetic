package com.minecraftabnormals.endergetic.client.models.eetle;

import com.minecraftabnormals.abnormals_core.core.endimator.entity.EndimatorEntityModel;
import com.minecraftabnormals.abnormals_core.core.endimator.entity.EndimatorModelRenderer;
import com.minecraftabnormals.endergetic.common.entities.eetle.ChargerEetleEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.util.math.MathHelper;

/**
 * @author Endergized
 * @author SmellyModder (Luke Tonon)
 * <p>
 * Created using Tabula 7.0.0
 */
public class ChargerEetleModel extends EndimatorEntityModel<ChargerEetleEntity> {
	public EndimatorModelRenderer body;
	public EndimatorModelRenderer frontLeftLeg;
	public EndimatorModelRenderer frontRightLeg;
	public EndimatorModelRenderer backLeftLeg;
	public EndimatorModelRenderer backRightLeg;
	public EndimatorModelRenderer rightWing;
	public EndimatorModelRenderer leftWing;
	public EndimatorModelRenderer head;
	public EndimatorModelRenderer mouth;
	public EndimatorModelRenderer claw;

	public ChargerEetleModel() {
		this.textureWidth = 64;
		this.textureHeight = 32;
		this.head = new EndimatorModelRenderer(this, 0, 23);
		this.head.setRotationPoint(0.0F, 0.5F, 0.0F);
		this.head.addBox(-3.0F, -2.0F, -5.0F, 6, 4, 5, 0.0F);
		this.setRotateAngle(head, 0.35F, 0.0F, 0.0F);
		this.backLeftLeg = new EndimatorModelRenderer(this, 52, 0);
		this.backLeftLeg.mirror = true;
		this.backLeftLeg.setRotationPoint(2.0F, 16.0F, 4.5F);
		this.backLeftLeg.addBox(0.0F, 0.0F, -1.5F, 3, 12, 3, 0.0F);
		this.setRotateAngle(backLeftLeg, 0.4363323129985824F, 0.15707963267948966F, -0.7853981633974483F);
		this.frontLeftLeg = new EndimatorModelRenderer(this, 52, 0);
		this.frontLeftLeg.mirror = true;
		this.frontLeftLeg.setRotationPoint(2.0F, 16.0F, -1.5F);
		this.frontLeftLeg.addBox(0.0F, 0.0F, -1.5F, 3, 12, 3, 0.0F);
		this.setRotateAngle(frontLeftLeg, -0.488692191F, 0.0F, -0.7853981633974483F);
		this.frontRightLeg = new EndimatorModelRenderer(this, 52, 0);
		this.frontRightLeg.setRotationPoint(-2.0F, 16.0F, -1.5F);
		this.frontRightLeg.addBox(-3.0F, 0.0F, -1.5F, 3, 12, 3, 0.0F);
		this.setRotateAngle(frontRightLeg, -0.488692191F, 0.0F, 0.7853981633974483F);
		this.backRightLeg = new EndimatorModelRenderer(this, 52, 0);
		this.backRightLeg.setRotationPoint(-2.0F, 16.0F, 4.5F);
		this.backRightLeg.addBox(-3.0F, 0.0F, -1.5F, 3, 12, 3, 0.0F);
		this.setRotateAngle(backRightLeg, 0.4363323129985824F, 0.0F, 0.7853981633974483F);
		this.mouth = new EndimatorModelRenderer(this, 22, 0);
		this.mouth.setRotationPoint(0.0F, 2.0F, -5.0F);
		this.mouth.addBox(-2.0F, -8.0F, 0.0F, 4, 8, 3, 0.0F);
		this.setRotateAngle(mouth, 0.5235987755982988F, 0.0F, 0.0F);
		this.claw = new EndimatorModelRenderer(this, 20, 18);
		this.claw.setRotationPoint(0.0F, -7.0F, 1.0F);
		this.claw.addBox(-3.0F, -5.0F, -1.5F, 6, 5, 3, 0.0F);
		this.setRotateAngle(claw, -0.4363323129985824F, 0.0F, 0.0F);
		this.rightWing = new EndimatorModelRenderer(this, 0, 0);
		this.rightWing.setRotationPoint(0.0F, -3.0F, 0.0F);
		this.rightWing.addBox(-5.0F, -1.0F, -1.0F, 5, 6, 12, 0.0F);
		this.setRotateAngle(rightWing, -0.08726646259971647F, -0.03490658503988659F, 0.0F);
		this.body = new EndimatorModelRenderer(this, 28, 16);
		this.body.setRotationPoint(0.0F, 13.5F, -3.5F);
		this.body.addBox(-4.0F, -3.0F, 0.0F, 8, 6, 10, 0.0F);
		this.setRotateAngle(body, -0.17453292519943295F, 0.0F, 0.0F);
		this.leftWing = new EndimatorModelRenderer(this, 0, 0);
		this.leftWing.mirror = true;
		this.leftWing.setRotationPoint(0.0F, -3.0F, 0.0F);
		this.leftWing.addBox(0.0F, -1.0F, -1.0F, 5, 6, 12, 0.0F);
		this.setRotateAngle(leftWing, -0.08726646259971647F, 0.03490658503988659F, 0.0F);
		this.body.addChild(this.head);
		this.head.addChild(this.mouth);
		this.mouth.addChild(this.claw);
		this.body.addChild(this.rightWing);
		this.body.addChild(this.leftWing);

		this.setDefaultBoxValues();
	}

	@Override
	public void setRotationAngles(ChargerEetleEntity eetle, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setRotationAngles(eetle, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

		this.head.rotateAngleY = netHeadYaw * ((float) Math.PI / 180.0F);
		this.head.rotateAngleX += (headPitch * ((float) Math.PI / 180.0F));

		float angleX = MathHelper.cos(limbSwing * 1.34F + ((float) Math.PI * 1.5F)) * limbSwingAmount;
		float backAngleX = MathHelper.cos(limbSwing * 1.34F) * limbSwingAmount;
		this.frontLeftLeg.rotateAngleX -= angleX;
		this.frontRightLeg.rotateAngleX += angleX;
		this.backLeftLeg.rotateAngleX += backAngleX;
		this.backRightLeg.rotateAngleX -= backAngleX;

		float angleZ = Math.abs(MathHelper.sin(limbSwing * 0.67F + ((float) Math.PI * 1.5F))) * 0.5F * limbSwingAmount;
		float backAngleZ = Math.abs(MathHelper.sin(limbSwing * 0.67F)) * 0.5F * limbSwingAmount;
		this.frontLeftLeg.rotateAngleZ -= angleZ;
		this.frontRightLeg.rotateAngleZ += angleZ;
		this.backLeftLeg.rotateAngleZ -= backAngleZ;
		this.backRightLeg.rotateAngleZ += backAngleZ;
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		super.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		this.body.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		this.frontLeftLeg.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		this.frontRightLeg.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		this.backLeftLeg.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		this.backRightLeg.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}

	@Override
	public void animateModel(ChargerEetleEntity eetle) {
		super.animateModel(eetle);
		if (this.tryToPlayEndimation(ChargerEetleEntity.ATTACK)) {
			this.startKeyframe(2);
			this.rotate(this.head, 0.25F, 0.0F, 0.0F);
			this.endKeyframe();

			this.startKeyframe(3);
			this.rotate(this.head, -0.75F, 0.0F, 0.0F);
			this.rotate(this.mouth, -0.09F, 0.0F, 0.0F);
			this.rotate(this.claw, -0.05F, 0.0F, 0.0F);
			this.endKeyframe();

			this.resetKeyframe(5);
		} else if (this.tryToPlayEndimation(ChargerEetleEntity.FLAP)) {
			this.startKeyframe(5);
			this.rotate(this.leftWing, 0.26F, 0.26F, 0.0F);
			this.rotate(this.rightWing, 0.26F, -0.26F, 0.0F);
			this.endKeyframe();

			this.startKeyframe(5);
			this.rotate(this.leftWing, 0.13F, 0.13F, 0.0F);
			this.rotate(this.rightWing, 0.13F, -0.13F, 0.0F);
			this.endKeyframe();

			this.startKeyframe(5);
			this.rotate(this.leftWing, 0.26F, 0.26F, 0.0F);
			this.rotate(this.rightWing, 0.26F, -0.26F, 0.0F);
			this.endKeyframe();

			this.resetKeyframe(5);
		}
	}

	public void setRotateAngle(EndimatorModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
