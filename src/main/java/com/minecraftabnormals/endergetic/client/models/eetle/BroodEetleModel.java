package com.minecraftabnormals.endergetic.client.models.eetle;

import com.minecraftabnormals.abnormals_core.core.endimator.entity.EndimatorEntityModel;
import com.minecraftabnormals.abnormals_core.core.endimator.entity.EndimatorModelRenderer;
import com.minecraftabnormals.endergetic.common.entities.eetle.BroodEetleEntity;
import com.minecraftabnormals.endergetic.common.entities.eetle.flying.FlyingRotations;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.util.math.MathHelper;

/**
 * @author Endergized
 * @author SmellyModder (Luke Tonon)
 * <p>
 * Created using Tabula 7.0.0
 */
public class BroodEetleModel extends EndimatorEntityModel<BroodEetleEntity> {
	private static final float WING_SHOW_THRESHOLD = (float) Math.toRadians(15.0F);
	public EndimatorModelRenderer body;
	public EndimatorModelRenderer leftShell;
	public EndimatorModelRenderer rightShell;
	public EndimatorModelRenderer wingLeft;
	public EndimatorModelRenderer wingRight;
	public EndimatorModelRenderer head;
	public EndimatorModelRenderer egg;
	public EndimatorModelRenderer leftFrontLeg;
	public EndimatorModelRenderer rightFrontLeg;
	public EndimatorModelRenderer leftBackLeg;
	public EndimatorModelRenderer rightBackLeg;
	public EndimatorModelRenderer horn;
	public EndimatorModelRenderer rightMandible;
	public EndimatorModelRenderer leftMandible;
	public EndimatorModelRenderer hornTop;
	public EndimatorModelRenderer eggLayer;
	public EndimatorModelRenderer eggSack;
	public EndimatorModelRenderer eggMouthTop;
	public EndimatorModelRenderer eggMouthBottom;
	public EndimatorModelRenderer eggMouthLeft;
	public EndimatorModelRenderer eggMouthRight;
	public EndimatorModelRenderer leftFrontFoot;
	public EndimatorModelRenderer rightFrontFoot;
	public EndimatorModelRenderer leftBackFoot;
	public EndimatorModelRenderer rightBackFoot;

	public BroodEetleModel() {
		this.texWidth = 300;
		this.texHeight = 100;
		this.rightFrontLeg = new EndimatorModelRenderer(this, 103, 79);
		this.rightFrontLeg.mirror = true;
		this.rightFrontLeg.setPos(-13.0F, 6.0F, 5.0F);
		this.rightFrontLeg.addBox(0.0F, 0.0F, -2.5F, 5, 14, 5, 0.0F);
		this.setRotateAngle(rightFrontLeg, 0.0F, -0.08726646259971647F, 0.6108652381980153F);
		this.rightBackFoot = new EndimatorModelRenderer(this, 77, 61);
		this.rightBackFoot.mirror = true;
		this.rightBackFoot.setPos(2.5F, 12.0F, 0.0F);
		this.rightBackFoot.addBox(-3.5F, 0.0F, -3.5F, 7, 14, 7, 0.0F);
		this.setRotateAngle(rightBackFoot, -0.13962634015954636F, 0.0F, -0.7853981633974483F);
		this.leftMandible = new EndimatorModelRenderer(this, 93, 60);
		this.leftMandible.setPos(0.0F, 3.0F, -14.0F);
		this.leftMandible.addBox(0.0F, 0.0F, -15.0F, 15, 0, 15, 0.0F);
		this.rightBackLeg = new EndimatorModelRenderer(this, 103, 79);
		this.rightBackLeg.mirror = true;
		this.rightBackLeg.setPos(-13.0F, 6.0F, 23.0F);
		this.rightBackLeg.addBox(0.0F, 0.0F, -2.5F, 5, 14, 5, 0.0F);
		this.setRotateAngle(rightBackLeg, 0.2617993877991494F, -0.08726646259971647F, 0.7853981633974483F);
		this.horn = new EndimatorModelRenderer(this, 0, 62);
		this.horn.setPos(0.0F, 0.0F, -11.5F);
		this.horn.addBox(-3.0F, -15.0F, -3.0F, 6, 15, 6, 0.0F);
		this.setRotateAngle(horn, 0.3490658503988659F, 0.0F, 0.0F);
		this.body = new EndimatorModelRenderer(this, 0, 59);
		this.body.setPos(0.0F, -8.0F, -15.0F);
		this.body.addBox(-12.0F, 0.0F, 0.0F, 24, 14, 27, 0.0F);
		this.setRotateAngle(body, -0.08726646259971647F, 0.0F, 0.0F);
		this.eggMouthBottom = new EndimatorModelRenderer(this, 125, 60);
		this.eggMouthBottom.setPos(-11.0F, 10.5F, 0.5F);
		this.eggMouthBottom.addBox(0.0F, 0.0F, 0.0F, 21, 21, 17, 0.0F);
		this.setRotateAngle(eggMouthBottom, 0.0F, 0.0F, 4.71238898038469F);
		this.eggSack = new EndimatorModelRenderer(this, 5, 27);
		this.eggSack.setPos(13.0F, 0.0F, 17.0F);
		this.eggSack.addBox(-8.0F, -8.0F, 0.0F, 16, 16, 11, 0.0F);
		this.head = new EndimatorModelRenderer(this, 0, 0);
		this.head.setPos(0.0F, 8.5F, 1.5F);
		this.head.addBox(-8.0F, -6.0F, -14.0F, 16, 12, 14, 0.0F);
		this.setRotateAngle(head, 0.2617993877991494F, 0.0F, 0.0F);
		this.rightMandible = new EndimatorModelRenderer(this, 93, 60);
		this.rightMandible.mirror = true;
		this.rightMandible.setPos(0.0F, 3.0F, -13.0F);
		this.rightMandible.addBox(-15.0F, 0.0F, -16.0F, 15, 0, 15, 0.0F);
		this.leftBackLeg = new EndimatorModelRenderer(this, 103, 79);
		this.leftBackLeg.setPos(13.0F, 6.0F, 23.0F);
		this.leftBackLeg.addBox(-5.0F, 0.0F, -2.5F, 5, 14, 5, 0.0F);
		this.setRotateAngle(leftBackLeg, 0.2617993877991494F, 0.08726646259971647F, -0.7853981633974483F);
		this.eggMouthLeft = new EndimatorModelRenderer(this, 125, 60);
		this.eggMouthLeft.setPos(11.0F, 10.5F, 0.5F);
		this.eggMouthLeft.addBox(0.0F, 0.0F, 0.0F, 21, 21, 17, 0.0F);
		this.setRotateAngle(eggMouthLeft, 0.0F, 0.0F, 3.141592653589793F);
		this.rightFrontFoot = new EndimatorModelRenderer(this, 77, 61);
		this.rightFrontFoot.mirror = true;
		this.rightFrontFoot.setPos(2.5F, 12.0F, 0.0F);
		this.rightFrontFoot.addBox(-3.5F, 0.0F, -3.5F, 7, 14, 7, 0.0F);
		this.setRotateAngle(rightFrontFoot, 0.03490658503988659F, 0.0F, -0.5759586531581287F);
		this.eggLayer = new EndimatorModelRenderer(this, 210, 56);
		this.eggLayer.setPos(-0.5F, -10.5F, -0.5F);
		this.eggLayer.addBox(0.0F, 0.0F, 0.0F, 27, 21, 18, 0.0F);
		this.wingLeft = new EndimatorModelRenderer(this, 120, 0);
		this.wingLeft.setPos(0.0F, -0.2F, 0.0F);
		this.wingLeft.addBox(0.0F, 0.0F, 0.0F, 12, 0, 40, 0.0F);
		this.setRotateAngle(wingLeft, 0.0F, 0.017453292519943295F, 0.0F);
		this.eggMouthRight = new EndimatorModelRenderer(this, 125, 60);
		this.eggMouthRight.setPos(-11.0F, -10.5F, 0.5F);
		this.eggMouthRight.addBox(0.0F, 0.0F, 0.0F, 21, 21, 17, 0.0F);
		this.setRotateAngle(eggMouthRight, 0.0F, 0.006108652381980153F, 0.0F);
		this.wingRight = new EndimatorModelRenderer(this, 120, 0);
		this.wingRight.mirror = true;
		this.wingRight.setPos(0.0F, -0.2F, 0.0F);
		this.wingRight.addBox(-12.0F, 0.0F, 0.0F, 12, 0, 40, 0.0F);
		this.setRotateAngle(wingRight, 0.0F, -0.017453292519943295F, 0.0F);
		this.leftFrontLeg = new EndimatorModelRenderer(this, 103, 79);
		this.leftFrontLeg.setPos(13.0F, 6.0F, 5.0F);
		this.leftFrontLeg.addBox(-5.0F, 0.0F, -2.5F, 5, 14, 5, 0.0F);
		this.setRotateAngle(leftFrontLeg, 0.0F, 0.08726646259971647F, -0.6108652381980153F);
		this.eggMouthTop = new EndimatorModelRenderer(this, 125, 60);
		this.eggMouthTop.setPos(11.0F, -10.5F, 0.5F);
		this.eggMouthTop.addBox(0.0F, 0.0F, 0.0F, 21, 21, 17, 0.0F);
		this.setRotateAngle(eggMouthTop, 0.0F, 0.0F, 1.5707963267948966F);
		this.egg = new EndimatorModelRenderer(this, 61, 0);
		this.egg.setPos(-13.0F, 6.0F, 24.0F);
		this.egg.addBox(0.0F, -10.0F, 0.0F, 26, 20, 17, 0.0F);
		this.setRotateAngle(egg, 0.40142572795869574F, 0.0F, 0.0F);
		this.leftShell = new EndimatorModelRenderer(this, 217, 0);
		this.leftShell.setPos(0.0F, -0.5F, -0.5F);
		this.leftShell.addBox(0.0F, 0.0F, 0.0F, 13, 12, 27, 0.0F);
		this.setRotateAngle(leftShell, 0.0F, 0.03490658503988659F, 0.0F);
		this.leftFrontFoot = new EndimatorModelRenderer(this, 77, 61);
		this.leftFrontFoot.setPos(-2.5F, 12.0F, 0.0F);
		this.leftFrontFoot.addBox(-3.5F, 0.0F, -3.5F, 7, 14, 7, 0.0F);
		this.setRotateAngle(leftFrontFoot, 0.03490658503988659F, 0.0F, 0.5759586531581287F);
		this.rightShell = new EndimatorModelRenderer(this, 217, 0);
		this.rightShell.mirror = true;
		this.rightShell.setPos(0.0F, -0.5F, -0.5F);
		this.rightShell.addBox(-13.0F, 0.0F, 0.0F, 13, 12, 27, 0.0F);
		this.setRotateAngle(rightShell, 0.0F, -0.03490658503988659F, 0.0F);
		this.hornTop = new EndimatorModelRenderer(this, 62, 38);
		this.hornTop.setPos(-13.0F, -30.0F, 0.0F);
		this.hornTop.addBox(0.0F, 0.0F, 0.0F, 26, 20, 0, 0.0F);
		this.leftBackFoot = new EndimatorModelRenderer(this, 77, 61);
		this.leftBackFoot.setPos(-2.5F, 12.0F, 0.0F);
		this.leftBackFoot.addBox(-3.5F, 0.0F, -3.5F, 7, 14, 7, 0.0F);
		this.setRotateAngle(leftBackFoot, -0.13962634015954636F, 0.0F, 0.7853981633974483F);
		this.body.addChild(this.rightFrontLeg);
		this.rightBackLeg.addChild(this.rightBackFoot);
		this.head.addChild(this.leftMandible);
		this.body.addChild(this.rightBackLeg);
		this.head.addChild(this.horn);
		this.eggSack.addChild(this.eggMouthBottom);
		this.egg.addChild(this.eggSack);
		this.body.addChild(this.head);
		this.head.addChild(this.rightMandible);
		this.body.addChild(this.leftBackLeg);
		this.eggSack.addChild(this.eggMouthLeft);
		this.rightFrontLeg.addChild(this.rightFrontFoot);
		this.egg.addChild(this.eggLayer);
		this.body.addChild(this.wingLeft);
		this.eggSack.addChild(this.eggMouthRight);
		this.body.addChild(this.wingRight);
		this.body.addChild(this.leftFrontLeg);
		this.eggSack.addChild(this.eggMouthTop);
		this.body.addChild(this.egg);
		this.body.addChild(this.leftShell);
		this.leftFrontLeg.addChild(this.leftFrontFoot);
		this.body.addChild(this.rightShell);
		this.horn.addChild(this.hornTop);
		this.leftBackLeg.addChild(this.leftBackFoot);

		this.eggSack.setShouldScaleChildren(false);
		this.setDefaultBoxValues();
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		super.renderToBuffer(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		this.wingLeft.visible = this.wingLeft.yRot >= WING_SHOW_THRESHOLD;
		this.wingRight.visible = this.wingRight.yRot <= -WING_SHOW_THRESHOLD;
		this.body.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public void setupAnim(BroodEetleEntity eetle, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setupAnim(eetle, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

		FlyingRotations flyingRotations = eetle.getFlyingRotations();
		this.body.xRot += MathHelper.clamp(flyingRotations.getRenderFlyPitch(), -30.0F, 20.0F) * ((float) Math.PI / 180.0F);

		if (eetle.isFlying()) {
			this.body.zRot = flyingRotations.getRenderFlyRoll() * ((float) Math.PI / 180.0F);
		}

		float flyingProgress = eetle.getFlyingProgress();
		float sleepingProgress = eetle.getSleepingProgress();
		this.head.yRot = netHeadYaw * ((float) Math.PI / 180.0F);
		this.head.xRot += 0.35F * flyingProgress + (headPitch * ((float) Math.PI / 180.0F)) + 0.35F * sleepingProgress + sleepingProgress * 0.06F * MathHelper.sin(0.08F * ageInTicks);

		this.body.y += sleepingProgress * 6.0F;

		float frontLegSleepingX = 0.7F * sleepingProgress;
		float legFlying = 0.52F * flyingProgress;
		this.leftFrontLeg.xRot += legFlying - frontLegSleepingX;
		this.rightFrontLeg.xRot += legFlying - frontLegSleepingX;
		float backLegSleepingX = 0.09F * sleepingProgress;
		this.leftBackLeg.xRot += legFlying + backLegSleepingX;
		this.rightBackLeg.xRot += legFlying + backLegSleepingX;

		float frontLegSleepingZ = 0.52F * sleepingProgress;
		this.leftFrontLeg.zRot -= frontLegSleepingZ;
		this.rightFrontLeg.zRot += frontLegSleepingZ;
		float backLegSleepingZ = 0.33F * sleepingProgress;
		this.leftBackLeg.zRot -= backLegSleepingZ;
		this.rightBackLeg.zRot += backLegSleepingZ;

		float backLegSleepingY = 0.54F * sleepingProgress;
		this.leftBackLeg.yRot += backLegSleepingY;
		this.rightBackLeg.yRot -= backLegSleepingY;

		float healPulseProgress = eetle.getHealPulseProgress();
		float scale = 1.0F + 0.05F * Math.abs(MathHelper.sin(0.05F * ageInTicks)) + 0.1F * MathHelper.sin((float) Math.PI * 0.5F * healPulseProgress);
		this.eggSack.setScale(scale, scale, scale);
		EndimatorModelRenderer egg = this.egg;
		float eggScale = 0.15F * healPulseProgress * healPulseProgress * healPulseProgress;
		egg.setScale(egg.scaleX + eggScale, egg.scaleY + eggScale, egg.scaleZ + eggScale);

		float eggMouthAngle = 0.087F * Math.abs(MathHelper.cos(0.05F * ageInTicks)) + 0.79F * eetle.getEggMouthProgress();
		this.eggMouthTop.yRot -= eggMouthAngle;
		this.eggMouthBottom.yRot -= eggMouthAngle;
		this.eggMouthRight.yRot -= eggMouthAngle;
		this.eggMouthLeft.yRot -= eggMouthAngle;

		egg.xRot += computeSmoothCurve(eetle.getEggCannonProgress(), 0.0F, 0.01F, 0.91F) - computeSmoothCurve(eetle.getEggCannonFlyingProgress(), 0.0F, 0.0F, 0.8F) + 0.07F * MathHelper.sin(0.09F * ageInTicks) - 0.44F * sleepingProgress;

		float takeOffProgress = eetle.getTakeoffProgress();
		float thirtyDegreeProgress = 0.52F * takeOffProgress;
		this.leftShell.zRot -= thirtyDegreeProgress;
		this.rightShell.zRot += thirtyDegreeProgress;
		this.wingLeft.yRot += thirtyDegreeProgress;
		this.wingRight.yRot -= thirtyDegreeProgress;

		float shellY = 0.87F * takeOffProgress;
		this.leftShell.yRot += shellY;
		this.rightShell.yRot -= shellY;

		if (eetle.isFlying()) {
			float wingX = 0.1F * MathHelper.sin(8.0F * eetle.getWingFlap()) + (0.26F * takeOffProgress);
			this.wingLeft.xRot += wingX;
			this.wingRight.xRot += wingX;

			float frontLegMoving = Math.abs(MathHelper.cos(limbSwing * 0.3F)) * 0.21F * limbSwingAmount;
			this.leftFrontLeg.xRot -= frontLegMoving;
			this.rightFrontLeg.xRot -= frontLegMoving;

			float backLegMoving = Math.abs(MathHelper.sin(limbSwing * 0.3F)) * 0.21F * limbSwingAmount;
			this.leftBackLeg.xRot += backLegMoving;
			this.rightBackLeg.xRot += backLegMoving;
		}
	}

	@Override
	public void animateModel(BroodEetleEntity endimatedEntity) {
		super.animateModel(endimatedEntity);
		if (this.tryToPlayEndimation(BroodEetleEntity.FLAP)) {
			this.startKeyframe(5);
			this.rotate(this.leftShell, 0.0F, 0.87F, -0.35F);
			this.rotate(this.rightShell, 0.0F, -0.87F, 0.35F);
			this.rotate(this.wingLeft, 0.12F, 0.61F, 0.0F);
			this.rotate(this.wingRight, 0.12F, -0.61F, 0.0F);
			this.endKeyframe();

			this.startKeyframe(2);
			this.rotate(this.leftShell, 0.0F, 0.813F, -0.35F);
			this.rotate(this.rightShell, 0.0F, -0.813F, 0.35F);
			this.rotate(this.wingLeft, 0.17F, 0.58F, 0.0F);
			this.rotate(this.wingRight, 0.17F, -0.58F, 0.0F);
			this.endKeyframe();

			this.startKeyframe(2);
			this.rotate(this.leftShell, 0.0F, 0.757F, -0.35F);
			this.rotate(this.rightShell, 0.0F, -0.757F, 0.35F);
			this.rotate(this.wingLeft, 0.05F, 0.55F, 0.0F);
			this.rotate(this.wingRight, 0.05F, -0.55F, 0.0F);
			this.endKeyframe();

			this.startKeyframe(2);
			this.rotate(this.leftShell, 0.0F, 0.7F, -0.35F);
			this.rotate(this.rightShell, 0.0F, -0.7F, 0.35F);
			this.rotate(this.wingLeft, 0.12F, 0.52F, 0.0F);
			this.rotate(this.wingRight, 0.12F, -0.52F, 0.0F);
			this.endKeyframe();

			this.startKeyframe(2);
			this.rotate(this.leftShell, 0.0F, 0.757F, -0.35F);
			this.rotate(this.rightShell, 0.0F, -0.757F, 0.35F);
			this.rotate(this.wingLeft, 0.05F, 0.61F, 0.0F);
			this.rotate(this.wingRight, 0.05F, -0.61F, 0.0F);
			this.endKeyframe();

			this.startKeyframe(2);
			this.rotate(this.leftShell, 0.0F, 0.813F, -0.35F);
			this.rotate(this.rightShell, 0.0F, -0.813F, 0.35F);
			this.rotate(this.wingLeft, 0.17F, 0.61F, 0.0F);
			this.rotate(this.wingRight, 0.17F, -0.61F, 0.0F);
			this.endKeyframe();

			this.startKeyframe(2);
			this.rotate(this.leftShell, 0.0F, 0.87F, -0.35F);
			this.rotate(this.rightShell, 0.0F, -0.87F, 0.35F);
			this.rotate(this.wingLeft, 0.12F, 0.61F, 0.0F);
			this.rotate(this.wingRight, 0.12F, -0.61F, 0.0F);
			this.endKeyframe();

			this.resetKeyframe(5);
		} else if (this.tryToPlayEndimation(BroodEetleEntity.MUNCH)) {
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
		} else if (this.tryToPlayEndimation(BroodEetleEntity.ATTACK)) {
			this.startKeyframe(4);
			this.rotate(this.head, 0.78F, 0.0F, 0.0F);
			this.endKeyframe();

			this.startKeyframe(3);
			this.rotate(this.head, -0.26F, 0.0F, 0.0F);
			this.rotate(this.horn, 0.78F, 0.0F, 0.0F);
			this.endKeyframe();

			this.resetKeyframe(5);
		} else if (this.tryToPlayEndimation(BroodEetleEntity.SLAM)) {
			this.startKeyframe(10);
			this.rotate(this.leftFrontLeg, -1.0875F, 0.0F, -1.571F);
			this.rotate(this.rightFrontLeg, -1.0875F, 0.0F, 1.571F);
			this.rotate(this.head, -0.4F, 0.0F, 0.0F);
			this.endKeyframe();

			this.startKeyframe(5);
			this.rotate(this.leftFrontLeg, -1.187F, 1.1F, -0.785F);
			this.rotate(this.rightFrontLeg, -1.187F, -1.1F, 0.785F);
			this.rotate(this.leftFrontFoot, -0.31F, 0.14F, 0.52F);
			this.rotate(this.rightFrontFoot, -0.31F, -0.14F, -0.52F);
			this.rotate(this.head, 0.4F, 0.0F, 0.0F);
			this.move(this.leftFrontLeg, 0.0F, 6.5F, 0.0F);
			this.move(this.rightFrontLeg, 0.0F, 6.5F, 0.0F);
			this.endKeyframe();

			this.resetKeyframe(5);
		} else if (this.tryToPlayEndimation(BroodEetleEntity.LAUNCH)) {
			float droppingFactor = endimatedEntity.isEggCannonFlyingAtMax() ? -1.2F : 1.0F;
			this.startKeyframe(5);
			this.scale(this.eggSack, 0.2F, 0.2F, 0.2F);
			this.rotate(this.head, 0.3F, 0.0F, 0.0F);
			this.rotate(this.horn, 0.52F, 0.0F, 0.0F);
			this.rotateAdditive(this.egg, droppingFactor * 0.27F, 0.0F, 0.0F);
			this.scale(this.egg, 0.1F, 0.1F, 0.1F);
			this.rotateAdditive(this.eggMouthTop, 0.0F, -0.17F, 0.0F);
			this.rotateAdditive(this.eggMouthBottom, 0.0F, -0.17F, 0.0F);
			this.rotateAdditive(this.eggMouthLeft, 0.0F, -0.17F, 0.0F);
			this.rotateAdditive(this.eggMouthRight, 0.0F, -0.17F, 0.0F);
			this.endKeyframe();

			this.startKeyframe(4);
			this.rotate(this.head, 0.075F, 0.0F, 0.0F);
			this.rotate(this.horn, 0.13F, 0.0F, 0.0F);
			this.scale(this.eggSack, 0.1375F, 0.1375F, 0.1375F);
			this.endKeyframe();

			this.startKeyframe(5);
			this.rotateAdditive(this.egg, droppingFactor * 0.13F, 0.0F, 0.0F);
			this.rotateAdditive(this.eggMouthTop, 0.0F, -0.07F, 0.0F);
			this.rotateAdditive(this.eggMouthBottom, 0.0F, -0.07F, 0.0F);
			this.rotateAdditive(this.eggMouthLeft, 0.0F, -0.07F, 0.0F);
			this.rotateAdditive(this.eggMouthRight, 0.0F, -0.07F, 0.0F);
			this.endKeyframe();

			this.resetKeyframe(4);
		} else if (this.tryToPlayEndimation(BroodEetleEntity.AIR_CHARGE)) {
			this.startKeyframe(10);
			this.rotate(this.leftFrontLeg, -1.0875F, 0.0F, -1.571F);
			this.rotate(this.rightFrontLeg, -1.0875F, 0.0F, 1.571F);
			this.rotate(this.head, -0.4F, 0.0F, 0.0F);
			this.endKeyframe();

			this.setStaticKeyframe(60);
			this.resetKeyframe(10);
		} else if (this.tryToPlayEndimation(BroodEetleEntity.AIR_SLAM)) {
			this.startKeyframe(1);
			this.rotate(this.leftFrontLeg, -1.0875F, 0.0F, -1.571F);
			this.rotate(this.rightFrontLeg, -1.0875F, 0.0F, 1.571F);
			this.rotate(this.head, -0.4F, 0.0F, 0.0F);
			this.endKeyframe();

			this.startKeyframe(5);
			this.rotate(this.leftFrontLeg, -1.187F, 1.1F, -0.785F);
			this.rotate(this.rightFrontLeg, -1.187F, -1.1F, 0.785F);
			this.rotate(this.leftFrontFoot, -0.31F, 0.14F, 0.52F);
			this.rotate(this.rightFrontFoot, -0.31F, -0.14F, -0.52F);
			this.rotate(this.head, 0.4F, 0.0F, 0.0F);
			this.move(this.leftFrontLeg, 0.0F, 6.5F, 0.0F);
			this.move(this.rightFrontLeg, 0.0F, 6.5F, 0.0F);
			this.endKeyframe();

			this.resetKeyframe(5);
		} else if (this.tryToPlayEndimation(BroodEetleEntity.DEATH)) {
			float headTiltAngle = endimatedEntity.headTiltDirection.angle;

			this.startKeyframe(20);
			this.move(this.body, 0.0F, 7.0F, 0.0F);
			this.rotate(this.head, 0.44F,0.0F, headTiltAngle);
			this.rotate(this.leftFrontLeg, -0.7F, 0.0F, -0.57F);
			this.rotate(this.rightFrontLeg, -0.7F, 0.0F, 0.57F);
			this.rotate(this.leftBackLeg, 0.18F, 0.54F, -0.33F);
			this.rotate(this.rightBackLeg, 0.18F, -0.54F, 0.33F);
			this.endKeyframe();

			this.setStaticKeyframe(35);

			this.startKeyframe(5);
			this.move(this.body, 0.0F, 7.0F, 0.0F);
			this.rotate(this.head, 0.44F,0.0F, headTiltAngle);
			this.rotate(this.leftFrontLeg, -0.7F, 0.0F, -0.57F);
			this.rotate(this.rightFrontLeg, -0.7F, 0.0F, 0.57F);
			this.rotate(this.leftBackLeg, 0.18F, 0.54F, -0.33F);
			this.rotate(this.rightBackLeg, 0.18F, -0.54F, 0.33F);
			this.scale(this.eggSack, 0.175F, 0.175F, 0.175F);
			this.endKeyframe();

			this.startKeyframe(5);
			this.move(this.body, 0.0F, 7.0F, 0.0F);
			this.rotate(this.head, 0.44F,0.0F, headTiltAngle);
			this.rotate(this.leftFrontLeg, -0.7F, 0.0F, -0.57F);
			this.rotate(this.rightFrontLeg, -0.7F, 0.0F, 0.57F);
			this.rotate(this.leftBackLeg, 0.18F, 0.54F, -0.33F);
			this.rotate(this.rightBackLeg, 0.18F, -0.54F, 0.33F);
			this.scale(this.eggSack, 0.0F, 0.0F, 0.0F);
			this.endKeyframe();

			this.startKeyframe(5);
			this.move(this.body, 0.0F, 7.0F, 0.0F);
			this.rotate(this.head, 0.44F,0.0F, headTiltAngle);
			this.rotate(this.leftFrontLeg, -0.7F, 0.0F, -0.57F);
			this.rotate(this.rightFrontLeg, -0.7F, 0.0F, 0.57F);
			this.rotate(this.leftBackLeg, 0.18F, 0.54F, -0.33F);
			this.rotate(this.rightBackLeg, 0.18F, -0.54F, 0.33F);
			this.scale(this.eggSack, 0.19F, 0.19F, 0.19F);
			this.endKeyframe();

			this.startKeyframe(5);
			this.move(this.body, 0.0F, 7.0F, 0.0F);
			this.rotate(this.head, 0.44F,0.0F, headTiltAngle);
			this.rotate(this.leftFrontLeg, -0.7F, 0.0F, -0.57F);
			this.rotate(this.rightFrontLeg, -0.7F, 0.0F, 0.57F);
			this.rotate(this.leftBackLeg, 0.18F, 0.54F, -0.33F);
			this.rotate(this.rightBackLeg, 0.18F, -0.54F, 0.33F);
			this.scale(this.eggSack, 0.0F, 0.0F, 0.0F);
			this.endKeyframe();

			this.startKeyframe(5);
			this.move(this.body, 0.0F, 7.0F, 0.0F);
			this.rotate(this.head, 0.44F,0.0F, headTiltAngle);
			this.rotate(this.leftFrontLeg, -0.7F, 0.0F, -0.57F);
			this.rotate(this.rightFrontLeg, -0.7F, 0.0F, 0.57F);
			this.rotate(this.leftBackLeg, 0.18F, 0.54F, -0.33F);
			this.rotate(this.rightBackLeg, 0.18F, -0.54F, 0.33F);
			this.scale(this.eggSack, 0.18F, 0.18F, 0.18F);
			this.endKeyframe();

			this.startKeyframe(5);
			this.move(this.body, 0.0F, 7.0F, 0.0F);
			this.rotate(this.head, 0.44F,0.0F, headTiltAngle);
			this.rotate(this.leftFrontLeg, -0.7F, 0.0F, -0.57F);
			this.rotate(this.rightFrontLeg, -0.7F, 0.0F, 0.57F);
			this.rotate(this.leftBackLeg, 0.18F, 0.54F, -0.33F);
			this.rotate(this.rightBackLeg, 0.18F, -0.54F, 0.33F);
			this.scale(this.eggSack, 0.0F, 0.0F, 0.0F);
			this.endKeyframe();

			this.startKeyframe(5);
			this.move(this.body, 0.0F, 7.0F, 0.0F);
			this.rotate(this.head, 0.44F,0.0F, headTiltAngle);
			this.rotate(this.leftFrontLeg, -0.7F, 0.0F, -0.57F);
			this.rotate(this.rightFrontLeg, -0.7F, 0.0F, 0.57F);
			this.rotate(this.leftBackLeg, 0.18F, 0.54F, -0.33F);
			this.rotate(this.rightBackLeg, 0.18F, -0.54F, 0.33F);
			this.scale(this.eggSack, 0.225F, 0.225F, 0.225F);
			this.endKeyframe();

			this.startKeyframe(5);
			this.move(this.body, 0.0F, 7.0F, 0.0F);
			this.rotate(this.head, 0.44F,0.0F, headTiltAngle);
			this.rotate(this.leftFrontLeg, -0.7F, 0.0F, -0.57F);
			this.rotate(this.rightFrontLeg, -0.7F, 0.0F, 0.57F);
			this.rotate(this.leftBackLeg, 0.18F, 0.54F, -0.33F);
			this.rotate(this.rightBackLeg, 0.18F, -0.54F, 0.33F);
			this.scale(this.eggSack, 0.0F, 0.0F, 0.0F);
			this.endKeyframe();

			this.startKeyframe(15);
			this.move(this.body, 0.0F, 7.0F, 0.0F);
			this.rotate(this.head, 0.44F,0.0F, headTiltAngle);
			this.rotate(this.leftFrontLeg, -0.7F, 0.0F, -0.57F);
			this.rotate(this.rightFrontLeg, -0.7F, 0.0F, 0.57F);
			this.rotate(this.leftBackLeg, 0.18F, 0.54F, -0.33F);
			this.rotate(this.rightBackLeg, 0.18F, -0.54F, 0.33F);
			this.scale(this.egg, 0.3F, 0.3F, 0.3F);
			this.scale(this.eggSack, 0.275F, 0.275F, 0.275F);
			this.endKeyframe();

			this.setStaticKeyframe(5);
		}
	}

	/**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	public void setRotateAngle(EndimatorModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}

	/**
	 * Computes a value from 0 to a maximum value over an adjusted gradient curve.
	 */
	private static float computeSmoothCurve(float progress, float fromGradient, float toGradient, float max) {
		return (fromGradient - toGradient - 2.0F * max) * (progress * progress * progress) + (3.0F * max - 2.0F * fromGradient - toGradient) * (progress * progress) + fromGradient * progress;
	}
}
