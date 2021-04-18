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
		this.textureWidth = 300;
		this.textureHeight = 100;
		this.rightFrontLeg = new EndimatorModelRenderer(this, 103, 79);
		this.rightFrontLeg.mirror = true;
		this.rightFrontLeg.setRotationPoint(-13.0F, 6.0F, 5.0F);
		this.rightFrontLeg.addBox(0.0F, 0.0F, -2.5F, 5, 14, 5, 0.0F);
		this.setRotateAngle(rightFrontLeg, 0.0F, -0.08726646259971647F, 0.6108652381980153F);
		this.rightBackFoot = new EndimatorModelRenderer(this, 77, 61);
		this.rightBackFoot.mirror = true;
		this.rightBackFoot.setRotationPoint(2.5F, 12.0F, 0.0F);
		this.rightBackFoot.addBox(-3.5F, 0.0F, -3.5F, 7, 14, 7, 0.0F);
		this.setRotateAngle(rightBackFoot, -0.13962634015954636F, 0.0F, -0.7853981633974483F);
		this.leftMandible = new EndimatorModelRenderer(this, 93, 60);
		this.leftMandible.setRotationPoint(0.0F, 3.0F, -14.0F);
		this.leftMandible.addBox(0.0F, 0.0F, -15.0F, 15, 0, 15, 0.0F);
		this.rightBackLeg = new EndimatorModelRenderer(this, 103, 79);
		this.rightBackLeg.mirror = true;
		this.rightBackLeg.setRotationPoint(-13.0F, 6.0F, 23.0F);
		this.rightBackLeg.addBox(0.0F, 0.0F, -2.5F, 5, 14, 5, 0.0F);
		this.setRotateAngle(rightBackLeg, 0.2617993877991494F, -0.08726646259971647F, 0.7853981633974483F);
		this.horn = new EndimatorModelRenderer(this, 0, 62);
		this.horn.setRotationPoint(0.0F, 0.0F, -11.5F);
		this.horn.addBox(-3.0F, -15.0F, -3.0F, 6, 15, 6, 0.0F);
		this.setRotateAngle(horn, 0.3490658503988659F, 0.0F, 0.0F);
		this.body = new EndimatorModelRenderer(this, 0, 59);
		this.body.setRotationPoint(0.0F, -8.0F, -15.0F);
		this.body.addBox(-12.0F, 0.0F, 0.0F, 24, 14, 27, 0.0F);
		this.setRotateAngle(body, -0.08726646259971647F, 0.0F, 0.0F);
		this.eggMouthBottom = new EndimatorModelRenderer(this, 125, 60);
		this.eggMouthBottom.setRotationPoint(-11.0F, 10.5F, 0.5F);
		this.eggMouthBottom.addBox(0.0F, 0.0F, 0.0F, 21, 21, 17, 0.0F);
		this.setRotateAngle(eggMouthBottom, 0.0F, 0.0F, 4.71238898038469F);
		this.eggSack = new EndimatorModelRenderer(this, 5, 27);
		this.eggSack.setRotationPoint(13.0F, 0.0F, 17.0F);
		this.eggSack.addBox(-8.0F, -8.0F, 0.0F, 16, 16, 11, 0.0F);
		this.head = new EndimatorModelRenderer(this, 0, 0);
		this.head.setRotationPoint(0.0F, 8.5F, 1.5F);
		this.head.addBox(-8.0F, -6.0F, -14.0F, 16, 12, 14, 0.0F);
		this.setRotateAngle(head, 0.2617993877991494F, 0.0F, 0.0F);
		this.rightMandible = new EndimatorModelRenderer(this, 93, 60);
		this.rightMandible.mirror = true;
		this.rightMandible.setRotationPoint(0.0F, 3.0F, -13.0F);
		this.rightMandible.addBox(-15.0F, 0.0F, -16.0F, 15, 0, 15, 0.0F);
		this.leftBackLeg = new EndimatorModelRenderer(this, 103, 79);
		this.leftBackLeg.setRotationPoint(13.0F, 6.0F, 23.0F);
		this.leftBackLeg.addBox(-5.0F, 0.0F, -2.5F, 5, 14, 5, 0.0F);
		this.setRotateAngle(leftBackLeg, 0.2617993877991494F, 0.08726646259971647F, -0.7853981633974483F);
		this.eggMouthLeft = new EndimatorModelRenderer(this, 125, 60);
		this.eggMouthLeft.setRotationPoint(11.0F, 10.5F, 0.5F);
		this.eggMouthLeft.addBox(0.0F, 0.0F, 0.0F, 21, 21, 17, 0.0F);
		this.setRotateAngle(eggMouthLeft, 0.0F, 0.0F, 3.141592653589793F);
		this.rightFrontFoot = new EndimatorModelRenderer(this, 77, 61);
		this.rightFrontFoot.mirror = true;
		this.rightFrontFoot.setRotationPoint(2.5F, 12.0F, 0.0F);
		this.rightFrontFoot.addBox(-3.5F, 0.0F, -3.5F, 7, 14, 7, 0.0F);
		this.setRotateAngle(rightFrontFoot, 0.03490658503988659F, 0.0F, -0.5759586531581287F);
		this.eggLayer = new EndimatorModelRenderer(this, 210, 56);
		this.eggLayer.setRotationPoint(-0.5F, -10.5F, -0.5F);
		this.eggLayer.addBox(0.0F, 0.0F, 0.0F, 27, 21, 18, 0.0F);
		this.wingLeft = new EndimatorModelRenderer(this, 120, 0);
		this.wingLeft.setRotationPoint(0.0F, -0.2F, 0.0F);
		this.wingLeft.addBox(0.0F, 0.0F, 0.0F, 12, 0, 40, 0.0F);
		this.setRotateAngle(wingLeft, 0.0F, 0.017453292519943295F, 0.0F);
		this.eggMouthRight = new EndimatorModelRenderer(this, 125, 60);
		this.eggMouthRight.setRotationPoint(-11.0F, -10.5F, 0.5F);
		this.eggMouthRight.addBox(0.0F, 0.0F, 0.0F, 21, 21, 17, 0.0F);
		this.setRotateAngle(eggMouthRight, 0.0F, 0.006108652381980153F, 0.0F);
		this.wingRight = new EndimatorModelRenderer(this, 120, 0);
		this.wingRight.mirror = true;
		this.wingRight.setRotationPoint(0.0F, -0.2F, 0.0F);
		this.wingRight.addBox(-12.0F, 0.0F, 0.0F, 12, 0, 40, 0.0F);
		this.setRotateAngle(wingRight, 0.0F, -0.017453292519943295F, 0.0F);
		this.leftFrontLeg = new EndimatorModelRenderer(this, 103, 79);
		this.leftFrontLeg.setRotationPoint(13.0F, 6.0F, 5.0F);
		this.leftFrontLeg.addBox(-5.0F, 0.0F, -2.5F, 5, 14, 5, 0.0F);
		this.setRotateAngle(leftFrontLeg, 0.0F, 0.08726646259971647F, -0.6108652381980153F);
		this.eggMouthTop = new EndimatorModelRenderer(this, 125, 60);
		this.eggMouthTop.setRotationPoint(11.0F, -10.5F, 0.5F);
		this.eggMouthTop.addBox(0.0F, 0.0F, 0.0F, 21, 21, 17, 0.0F);
		this.setRotateAngle(eggMouthTop, 0.0F, 0.0F, 1.5707963267948966F);
		this.egg = new EndimatorModelRenderer(this, 61, 0);
		this.egg.setRotationPoint(-13.0F, 6.0F, 24.0F);
		this.egg.addBox(0.0F, -10.0F, 0.0F, 26, 20, 17, 0.0F);
		this.setRotateAngle(egg, 0.40142572795869574F, 0.0F, 0.0F);
		this.leftShell = new EndimatorModelRenderer(this, 217, 0);
		this.leftShell.setRotationPoint(0.0F, -0.5F, -0.5F);
		this.leftShell.addBox(0.0F, 0.0F, 0.0F, 13, 12, 27, 0.0F);
		this.setRotateAngle(leftShell, 0.0F, 0.03490658503988659F, 0.0F);
		this.leftFrontFoot = new EndimatorModelRenderer(this, 77, 61);
		this.leftFrontFoot.setRotationPoint(-2.5F, 12.0F, 0.0F);
		this.leftFrontFoot.addBox(-3.5F, 0.0F, -3.5F, 7, 14, 7, 0.0F);
		this.setRotateAngle(leftFrontFoot, 0.03490658503988659F, 0.0F, 0.5759586531581287F);
		this.rightShell = new EndimatorModelRenderer(this, 217, 0);
		this.rightShell.mirror = true;
		this.rightShell.setRotationPoint(0.0F, -0.5F, -0.5F);
		this.rightShell.addBox(-13.0F, 0.0F, 0.0F, 13, 12, 27, 0.0F);
		this.setRotateAngle(rightShell, 0.0F, -0.03490658503988659F, 0.0F);
		this.hornTop = new EndimatorModelRenderer(this, 62, 38);
		this.hornTop.setRotationPoint(-13.0F, -30.0F, 0.0F);
		this.hornTop.addBox(0.0F, 0.0F, 0.0F, 26, 20, 0, 0.0F);
		this.leftBackFoot = new EndimatorModelRenderer(this, 77, 61);
		this.leftBackFoot.setRotationPoint(-2.5F, 12.0F, 0.0F);
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
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		super.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		this.wingLeft.showModel = this.wingLeft.rotateAngleY >= WING_SHOW_THRESHOLD;
		this.wingRight.showModel = this.wingRight.rotateAngleY <= -WING_SHOW_THRESHOLD;
		this.body.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public void setRotationAngles(BroodEetleEntity eetle, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setRotationAngles(eetle, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

		FlyingRotations flyingRotations = eetle.getFlyingRotations();
		this.body.rotateAngleX += MathHelper.clamp(flyingRotations.getRenderFlyPitch(), -30.0F, 20.0F) * ((float) Math.PI / 180.0F);

		if (eetle.isFlying()) {
			this.body.rotateAngleZ = flyingRotations.getRenderFlyRoll() * ((float) Math.PI / 180.0F);
		}

		float flyingProgress = eetle.getFlyingProgress();
		this.head.rotateAngleY = netHeadYaw * ((float) Math.PI / 180.0F);
		this.head.rotateAngleX += 0.35F * flyingProgress + (headPitch * ((float) Math.PI / 180.0F));

		float legFlying = 0.52F * flyingProgress;
		this.leftFrontLeg.rotateAngleX += legFlying;
		this.rightFrontLeg.rotateAngleX += legFlying;
		this.leftBackLeg.rotateAngleX += legFlying;
		this.rightBackLeg.rotateAngleX += legFlying;

		float scale = 1.0F + 0.05F * Math.abs(MathHelper.sin(0.05F * ageInTicks));
		this.eggSack.setScale(scale, scale, scale);

		float eggMouthAngle = 0.087F * Math.abs(MathHelper.cos(0.05F * ageInTicks)) + 0.79F * eetle.getEggMouthProgress();
		this.eggMouthTop.rotateAngleY -= eggMouthAngle;
		this.eggMouthBottom.rotateAngleY -= eggMouthAngle;
		this.eggMouthRight.rotateAngleY -= eggMouthAngle;
		this.eggMouthLeft.rotateAngleY -= eggMouthAngle;

		this.egg.rotateAngleX += computeSmoothCurve(eetle.getEggCannonProgress(), 0.0F, 0.0F, 0.91F) + computeSmoothCurve(eetle.getEggCannonFireProgress(), 0.01F, 0.02F, 0.3F) - computeSmoothCurve(eetle.getEggCannonFlyingProgress(), 0.0F, 0.0F, 0.8F);

		float takeOffProgress = eetle.getTakeoffProgress();
		float thirtyDegreeProgress = 0.52F * takeOffProgress;
		this.leftShell.rotateAngleZ -= thirtyDegreeProgress;
		this.rightShell.rotateAngleZ += thirtyDegreeProgress;
		this.wingLeft.rotateAngleY += thirtyDegreeProgress;
		this.wingRight.rotateAngleY -= thirtyDegreeProgress;

		float shellY = 0.87F * takeOffProgress;
		this.leftShell.rotateAngleY += shellY;
		this.rightShell.rotateAngleY -= shellY;

		if (eetle.isFlying()) {
			float wingX = 0.1F * MathHelper.sin(8.0F * eetle.getWingFlap()) + (0.26F * takeOffProgress);
			this.wingLeft.rotateAngleX += wingX;
			this.wingRight.rotateAngleX += wingX;

			float frontLegMoving = Math.abs(MathHelper.cos(limbSwing * 0.3F)) * 0.21F * limbSwingAmount;
			this.leftFrontLeg.rotateAngleX -= frontLegMoving;
			this.rightFrontLeg.rotateAngleX -= frontLegMoving;

			float backLegMoving = Math.abs(MathHelper.sin(limbSwing * 0.3F)) * 0.21F * limbSwingAmount;
			this.leftBackLeg.rotateAngleX += backLegMoving;
			this.rightBackLeg.rotateAngleX += backLegMoving;
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
			this.startKeyframe(5);
			this.scale(this.eggSack, 0.175F, 0.175F, 0.175F);
			this.rotate(this.head, 0.3F, 0.0F, 0.0F);
			this.rotate(this.horn, 0.52F, 0.0F, 0.0F);
			this.endKeyframe();

			this.resetKeyframe(10);
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
		}
	}

	/**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	public void setRotateAngle(EndimatorModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	/**
	 * Computes a value from 0 to a maximum value over an adjusted gradient curve.
	 */
	private static float computeSmoothCurve(float progress, float fromGradient, float toGradient, float max) {
		return (fromGradient - toGradient - 2.0F * max) * (progress * progress * progress) + (3.0F * max - 2.0F * fromGradient - toGradient) * (progress * progress) + fromGradient * progress;
	}
}
