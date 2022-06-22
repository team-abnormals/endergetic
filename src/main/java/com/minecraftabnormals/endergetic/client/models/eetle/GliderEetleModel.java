package com.minecraftabnormals.endergetic.client.models.eetle;

import com.minecraftabnormals.abnormals_core.core.endimator.entity.EndimatorEntityModel;
import com.minecraftabnormals.abnormals_core.core.endimator.entity.EndimatorModelRenderer;
import com.minecraftabnormals.endergetic.common.entities.eetle.GliderEetleEntity;
import com.minecraftabnormals.endergetic.common.entities.eetle.flying.FlyingRotations;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.util.math.MathHelper;

/**
 * @author Endergized
 * @author SmellyModder (Luke Tonon)
 * Created using Tabula 7.0.0
 */
public class GliderEetleModel extends EndimatorEntityModel<GliderEetleEntity> {
	private static final float WING_SHOW_THRESHOLD = (float) Math.toRadians(9.0F);
	public EndimatorModelRenderer body;
	public EndimatorModelRenderer rightElytron;
	public EndimatorModelRenderer leftElytron;
	public EndimatorModelRenderer leftWing;
	public EndimatorModelRenderer rightWing;
	public EndimatorModelRenderer head;
	public EndimatorModelRenderer leftBackLeg;
	public EndimatorModelRenderer rightBackLeg;
	public EndimatorModelRenderer rightFrontLeg;
	public EndimatorModelRenderer leftFrontLeg;
	public EndimatorModelRenderer leftMandible;
	public EndimatorModelRenderer rightMandible;

	public GliderEetleModel() {
		this.texWidth = 64;
		this.texHeight = 32;
		this.leftElytron = new EndimatorModelRenderer(this, 0, 0);
		this.leftElytron.mirror = true;
		this.leftElytron.setPos(0.0F, -3.0F, 0.0F);
		this.leftElytron.addBox(0.0F, -1.0F, -1.0F, 5, 6, 12, 0.0F);
		this.setRotateAngle(leftElytron, 0.0F, 0.03490658503988659F, 0.0F);
		this.rightFrontLeg = new EndimatorModelRenderer(this, 52, 0);
		this.rightFrontLeg.mirror = true;
		this.rightFrontLeg.setPos(-2.0F, 2.5F, 2.0F);
		this.rightFrontLeg.addBox(-3.0F, 0.0F, -1.5F, 3, 12, 3, 0.0F);
		this.setRotateAngle(rightFrontLeg, -0.2617993877991494F, 0.0F, 0.7853981633974483F);
		this.leftWing = new EndimatorModelRenderer(this, 22, 0);
		this.leftWing.setPos(0.0F, -3.0F, 0.0F);
		this.leftWing.addBox(0.0F, 0.0F, 0.0F, 7, 0, 16, 0.0F);
		this.setRotateAngle(this.leftWing, 0.05F, 0.0F, 0.0F);
		this.leftFrontLeg = new EndimatorModelRenderer(this, 52, 0);
		this.leftFrontLeg.mirror = true;
		this.leftFrontLeg.setPos(2.0F, 2.5F, 2.0F);
		this.leftFrontLeg.addBox(0.0F, 0.0F, -1.5F, 3, 12, 3, 0.0F);
		this.setRotateAngle(leftFrontLeg, -0.2617993877991494F, 0.0F, -0.7853981633974483F);
		this.head = new EndimatorModelRenderer(this, 0, 23);
		this.head.setPos(0.0F, 1.0F, 0.0F);
		this.head.addBox(-3.0F, -2.0F, -5.0F, 6, 4, 5, 0.0F);
		this.setRotateAngle(head, 0.3490658503988659F, 0.0F, 0.0F);
		this.body = new EndimatorModelRenderer(this, 28, 16);
		this.body.setPos(0.0F, 13.3F, -4.0F);
		this.body.addBox(-4.0F, -3.0F, 0.0F, 8, 6, 10, 0.0F);
		this.setRotateAngle(body, -0.17453292519943295F, 0.0F, 0.0F);
		this.rightWing = new EndimatorModelRenderer(this, 22, 0);
		this.rightWing.mirror = true;
		this.rightWing.setPos(0.0F, -3.0F, 0.0F);
		this.rightWing.addBox(-7.0F, 0.0F, 0.0F, 7, 0, 16, 0.0F);
		this.setRotateAngle(this.rightWing, 0.05F, 0.0F, 0.0F);
		this.rightBackLeg = new EndimatorModelRenderer(this, 52, 0);
		this.rightBackLeg.mirror = true;
		this.rightBackLeg.setPos(-1.9F, 1.0F, 8.0F);
		this.rightBackLeg.addBox(-3.0F, 0.0F, -1.5F, 3, 12, 3, 0.0F);
		this.setRotateAngle(rightBackLeg, 0.6108652381980153F, 0.0F, 0.7853981633974483F);
		this.leftBackLeg = new EndimatorModelRenderer(this, 52, 0);
		this.leftBackLeg.mirror = true;
		this.leftBackLeg.setPos(1.9F, 1.0F, 8.0F);
		this.leftBackLeg.addBox(0.0F, 0.0F, -1.5F, 3, 12, 3, 0.0F);
		this.setRotateAngle(leftBackLeg, 0.6108652381980153F, 0.0F, -0.7853981633974483F);
		this.leftMandible = new EndimatorModelRenderer(this, 16, 0);
		this.leftMandible.setPos(0.0F, 0.0F, -5.0F);
		this.leftMandible.addBox(0.0F, 0.0F, -6.0F, 5, 0, 6, 0.0F);
		this.rightElytron = new EndimatorModelRenderer(this, 0, 0);
		this.rightElytron.setPos(0.0F, -3.0F, 0.0F);
		this.rightElytron.addBox(-5.0F, -1.0F, -1.0F, 5, 6, 12, 0.0F);
		this.setRotateAngle(rightElytron, 0.0F, -0.03490658503988659F, 0.0F);
		this.rightMandible = new EndimatorModelRenderer(this, 16, 0);
		this.rightMandible.mirror = true;
		this.rightMandible.setPos(0.0F, 0.0F, -5.0F);
		this.rightMandible.addBox(-5.0F, 0.0F, -6.0F, 5, 0, 6, 0.0F);
		this.body.addChild(this.leftElytron);
		this.body.addChild(this.rightFrontLeg);
		this.body.addChild(this.leftWing);
		this.body.addChild(this.leftFrontLeg);
		this.body.addChild(this.head);
		this.body.addChild(this.rightWing);
		this.body.addChild(this.rightBackLeg);
		this.body.addChild(this.leftBackLeg);
		this.head.addChild(this.leftMandible);
		this.body.addChild(this.rightElytron);
		this.head.addChild(this.rightMandible);

		this.setDefaultBoxValues();
	}

	@Override
	public void setupAnim(GliderEetleEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

		FlyingRotations flyingRotations = entity.getFlyingRotations();
		float flyingProgress = entity.getFlyingProgress();
		this.body.xRot += flyingRotations.getRenderFlyPitch() * ((float) Math.PI / 180.0F);
		this.head.xRot += 0.52F * flyingProgress;

		if (entity.isFlying()) {
			this.body.zRot = flyingRotations.getRenderFlyRoll() * ((float) Math.PI / 180.0F);
		}

		float frontLegFlying = 0.87F * flyingProgress;
		this.leftFrontLeg.xRot += frontLegFlying;
		this.rightFrontLeg.xRot += frontLegFlying;

		float backLegFlying = 0.7F * flyingProgress;
		this.leftBackLeg.xRot += backLegFlying;
		this.rightBackLeg.xRot += backLegFlying;

		this.head.yRot = netHeadYaw * ((float) Math.PI / 180.0F);
		this.head.xRot += (headPitch * ((float) Math.PI / 180.0F));

		float takeOffProgress = entity.getTakeoffProgress();

		float elytronZ = 0.35F * takeOffProgress;
		this.leftElytron.zRot += -elytronZ;
		this.rightElytron.zRot += elytronZ;

		float elytronY = 1.22F * takeOffProgress;
		this.leftElytron.yRot += elytronY;
		this.rightElytron.yRot += -elytronY;

		float wingY = 0.52F * takeOffProgress;
		this.leftWing.yRot += wingY;
		this.rightWing.yRot += -wingY;

		if (entity.isFlying()) {
			float wingX = 0.1F * MathHelper.sin(8.0F * entity.getWingFlap()) + 0.15F;

			this.leftWing.xRot += wingX;
			this.rightWing.xRot += wingX;

			float frontLegMoving = Math.abs(MathHelper.cos(limbSwing * 0.3F)) * 0.28F * limbSwingAmount;
			this.leftFrontLeg.xRot -= frontLegMoving;
			this.rightFrontLeg.xRot -= frontLegMoving;

			float backLegMoving = Math.abs(MathHelper.sin(limbSwing * 0.3F)) * 0.28F * limbSwingAmount;
			this.leftBackLeg.xRot += backLegMoving;
			this.rightBackLeg.xRot += backLegMoving;
		} else {
			float angleX = MathHelper.cos(limbSwing * 1.34F + ((float) Math.PI * 1.5F)) * limbSwingAmount;
			float backAngleX = MathHelper.cos(limbSwing * 1.34F) * limbSwingAmount;
			this.leftFrontLeg.xRot -= angleX;
			this.rightFrontLeg.xRot += angleX;
			this.leftBackLeg.xRot += backAngleX;
			this.rightBackLeg.xRot -= backAngleX;

			float angleZ = Math.abs(MathHelper.sin(limbSwing * 0.67F + ((float) Math.PI * 1.5F))) * 0.5F * limbSwingAmount;
			float backAngleZ = Math.abs(MathHelper.sin(limbSwing * 0.67F)) * 0.5F * limbSwingAmount;
			this.leftFrontLeg.zRot -= angleZ;
			this.rightFrontLeg.zRot += angleZ;
			this.leftBackLeg.zRot -= backAngleZ;
			this.rightBackLeg.zRot += backAngleZ;
		}
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		super.renderToBuffer(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		this.leftWing.visible = this.leftWing.yRot >= WING_SHOW_THRESHOLD;
		this.rightWing.visible = this.rightWing.yRot <= -WING_SHOW_THRESHOLD;
		this.body.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public void animateModel(GliderEetleEntity glider) {
		super.animateModel(glider);
		if (this.tryToPlayEndimation(GliderEetleEntity.FLAP)) {
			this.startKeyframe(5);
			this.rotate(this.leftElytron, 0.0F, 0.87F, -0.35F);
			this.rotate(this.rightElytron, 0.0F, -0.87F, 0.35F);
			this.rotate(this.leftWing, 0.12F, 0.35F, 0.0F);
			this.rotate(this.rightWing, 0.12F, -0.35F, 0.0F);
			this.endKeyframe();

			this.startKeyframe(2);
			this.rotate(this.leftElytron, 0.0F, 0.813F, -0.35F);
			this.rotate(this.rightElytron, 0.0F, -0.813F, 0.35F);
			this.rotate(this.leftWing, 0.17F, 0.32F, 0.0F);
			this.rotate(this.rightWing, 0.17F, -0.32F, 0.0F);
			this.endKeyframe();

			this.startKeyframe(2);
			this.rotate(this.leftElytron, 0.0F, 0.757F, -0.35F);
			this.rotate(this.rightElytron, 0.0F, -0.757F, 0.35F);
			this.rotate(this.leftWing, 0.05F, 0.29F, 0.0F);
			this.rotate(this.rightWing, 0.05F, -0.29F, 0.0F);
			this.endKeyframe();

			this.startKeyframe(2);
			this.rotate(this.leftElytron, 0.0F, 0.7F, -0.35F);
			this.rotate(this.rightElytron, 0.0F, -0.7F, 0.35F);
			this.rotate(this.leftWing, 0.12F, 0.26F, 0.0F);
			this.rotate(this.rightWing, 0.12F, -0.26F, 0.0F);
			this.endKeyframe();

			//2nd flap

			this.startKeyframe(2);
			this.rotate(this.leftElytron, 0.0F, 0.757F, -0.35F);
			this.rotate(this.rightElytron, 0.0F, -0.757F, 0.35F);
			this.rotate(this.leftWing, 0.05F, 0.35F, 0.0F);
			this.rotate(this.rightWing, 0.05F, -0.35F, 0.0F);
			this.endKeyframe();

			this.startKeyframe(2);
			this.rotate(this.leftElytron, 0.0F, 0.813F, -0.35F);
			this.rotate(this.rightElytron, 0.0F, -0.813F, 0.35F);
			this.rotate(this.leftWing, 0.17F, 0.35F, 0.0F);
			this.rotate(this.rightWing, 0.17F, -0.35F, 0.0F);
			this.endKeyframe();

			this.startKeyframe(2);
			this.rotate(this.leftElytron, 0.0F, 0.87F, -0.35F);
			this.rotate(this.rightElytron, 0.0F, -0.87F, 0.35F);
			this.rotate(this.leftWing, 0.12F, 0.35F, 0.0F);
			this.rotate(this.rightWing, 0.12F, -0.35F, 0.0F);
			this.endKeyframe();

			this.resetKeyframe(5);
		} else if (this.tryToPlayEndimation(GliderEetleEntity.MUNCH)) {
			this.startKeyframe(5);
			this.rotate(this.leftMandible, 0.0F, -0.35F, 0.0F);
			this.rotate(this.rightMandible, 0.0F, 0.35F, 0.0F);
			this.endKeyframe();

			this.startKeyframe(5);
			this.rotate(this.leftMandible, 0.0F, 0.17F, 0.0F);
			this.rotate(this.rightMandible, 0.0F, -0.17F, 0.0F);
			this.endKeyframe();

			this.startKeyframe(5);
			this.rotate(this.leftMandible, 0.0F, -0.35F, 0.0F);
			this.rotate(this.rightMandible, 0.0F, 0.35F, 0.0F);
			this.endKeyframe();

			this.startKeyframe(5);
			this.rotate(this.leftMandible, 0.0F, 0.17F, 0.0F);
			this.rotate(this.rightMandible, 0.0F, -0.17F, 0.0F);
			this.endKeyframe();

			this.resetKeyframe(5);
		}
	}

	public void setRotateAngle(EndimatorModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}
