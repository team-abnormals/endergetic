package com.teamabnormals.endergetic.client.model.eetle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamabnormals.blueprint.core.endimator.Endimator;
import com.teamabnormals.blueprint.core.endimator.EndimatorModelPart;
import com.teamabnormals.blueprint.core.endimator.entity.EndimatorEntityModel;
import com.teamabnormals.blueprint.core.endimator.model.EndimatorLayerDefinition;
import com.teamabnormals.blueprint.core.endimator.model.EndimatorPartDefinition;
import com.teamabnormals.endergetic.common.entity.eetle.BroodEetle;
import com.teamabnormals.endergetic.common.entity.eetle.flying.FlyingRotations;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

/**
 * @author Endergized
 * @author SmellyModder (Luke Tonon)
 * <p>
 * Created using Tabula 7.0.0
 */
public class BroodEetleModel extends EndimatorEntityModel<BroodEetle> {
	public static final ModelLayerLocation LOCATION = new ModelLayerLocation(new ResourceLocation(EndergeticExpansion.MOD_ID, "brood_eetle"), "main");
	private static final float WING_SHOW_THRESHOLD = (float) Math.toRadians(15.0F);
	public EndimatorModelPart body;
	public EndimatorModelPart leftShell;
	public EndimatorModelPart rightShell;
	public EndimatorModelPart wingLeft;
	public EndimatorModelPart wingRight;
	public EndimatorModelPart head;
	public EndimatorModelPart egg;
	public EndimatorModelPart leftFrontLeg;
	public EndimatorModelPart rightFrontLeg;
	public EndimatorModelPart leftBackLeg;
	public EndimatorModelPart rightBackLeg;
	public EndimatorModelPart horn;
	public EndimatorModelPart rightMandible;
	public EndimatorModelPart leftMandible;
	public EndimatorModelPart hornTop;
	public EndimatorModelPart eggLayer;
	public EndimatorModelPart eggSack;
	public EndimatorModelPart eggMouthTop;
	public EndimatorModelPart eggMouthBottom;
	public EndimatorModelPart eggMouthLeft;
	public EndimatorModelPart eggMouthRight;
	public EndimatorModelPart leftFrontFoot;
	public EndimatorModelPart rightFrontFoot;
	public EndimatorModelPart leftBackFoot;
	public EndimatorModelPart rightBackFoot;

	@SuppressWarnings("all")
	public BroodEetleModel(ModelPart root) {
		this.body = (EndimatorModelPart) root.getChild("body");
		this.leftShell = (EndimatorModelPart) this.body.getChild("leftShell");
		this.rightShell = (EndimatorModelPart) this.body.getChild("rightShell");
		this.wingLeft = (EndimatorModelPart) this.body.getChild("wingLeft");
		this.wingRight = (EndimatorModelPart) this.body.getChild("wingRight");
		this.head = (EndimatorModelPart) this.body.getChild("head");
		this.egg = (EndimatorModelPart) this.body.getChild("egg");
		this.leftFrontLeg = (EndimatorModelPart) this.body.getChild("leftFrontLeg");
		this.rightFrontLeg = (EndimatorModelPart) this.body.getChild("rightFrontLeg");
		this.leftBackLeg = (EndimatorModelPart) this.body.getChild("leftBackLeg");
		this.rightBackLeg = (EndimatorModelPart) this.body.getChild("rightBackLeg");
		this.horn = (EndimatorModelPart) this.head.getChild("horn");
		this.rightMandible = (EndimatorModelPart) this.head.getChild("rightMandible");
		this.leftMandible = (EndimatorModelPart) this.head.getChild("leftMandible");
		this.hornTop = (EndimatorModelPart) this.horn.getChild("hornTop");
		this.eggLayer = (EndimatorModelPart) this.egg.getChild("eggLayer");
		this.eggSack = (EndimatorModelPart) this.egg.getChild("eggSack");
		this.eggMouthTop = (EndimatorModelPart) this.eggSack.getChild("eggMouthTop");
		this.eggMouthBottom = (EndimatorModelPart) this.eggSack.getChild("eggMouthBottom");
		this.eggMouthLeft = (EndimatorModelPart) this.eggSack.getChild("eggMouthLeft");
		this.eggMouthRight = (EndimatorModelPart) this.eggSack.getChild("eggMouthRight");
		this.leftFrontFoot = (EndimatorModelPart) this.leftFrontLeg.getChild("leftFrontFoot");
		this.rightFrontFoot = (EndimatorModelPart) this.rightFrontLeg.getChild("rightFrontFoot");
		this.leftBackFoot = (EndimatorModelPart) this.leftBackLeg.getChild("leftBackFoot");
		this.rightBackFoot = (EndimatorModelPart) this.rightBackLeg.getChild("rightBackFoot");

		this.endimator = Endimator.compile(root);
		this.eggSack.setShouldScaleChildren(false);
	}

	public static LayerDefinition createLayerDefinition() {
		EndimatorPartDefinition root = EndimatorPartDefinition.root();
		EndimatorPartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 59).addBox(-12.0F, 0.0F, 0.0F, 24.0F, 14.0F, 27.0F, false), PartPose.offsetAndRotation(0.0F, -8.0F, -15.0F, -0.08726646F, 0.0F, 0.0F));
		EndimatorPartDefinition leftShell = body.addOrReplaceChild("leftShell", CubeListBuilder.create().texOffs(217, 0).addBox(0.0F, 0.0F, 0.0F, 13.0F, 12.0F, 27.0F, false), PartPose.offsetAndRotation(0.0F, -0.5F, -0.5F, 0.0F, 0.034906585F, 0.0F));
		EndimatorPartDefinition rightShell = body.addOrReplaceChild("rightShell", CubeListBuilder.create().texOffs(217, 0).addBox(-13.0F, 0.0F, 0.0F, 13.0F, 12.0F, 27.0F, true), PartPose.offsetAndRotation(0.0F, -0.5F, -0.5F, 0.0F, -0.034906585F, 0.0F));
		EndimatorPartDefinition wingLeft = body.addOrReplaceChild("wingLeft", CubeListBuilder.create().texOffs(120, 0).addBox(0.0F, 0.0F, 0.0F, 12.0F, 0.0F, 40.0F, false), PartPose.offsetAndRotation(0.0F, -0.2F, 0.0F, 0.0F, 0.017453292F, 0.0F));
		EndimatorPartDefinition wingRight = body.addOrReplaceChild("wingRight", CubeListBuilder.create().texOffs(120, 0).addBox(-12.0F, 0.0F, 0.0F, 12.0F, 0.0F, 40.0F, true), PartPose.offsetAndRotation(0.0F, -0.2F, 0.0F, 0.0F, -0.017453292F, 0.0F));
		EndimatorPartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -6.0F, -14.0F, 16.0F, 12.0F, 14.0F, false), PartPose.offsetAndRotation(0.0F, 8.5F, 1.5F, 0.2617994F, 0.0F, 0.0F));
		EndimatorPartDefinition egg = body.addOrReplaceChild("egg", CubeListBuilder.create().texOffs(61, 0).addBox(-13.0F, -10.0F, 0.0F, 26.0F, 20.0F, 17.0F, false), PartPose.offsetAndRotation(0.0F, 6.0F, 24.0F, 0.40142572F, 0.0F, 0.0F));
		EndimatorPartDefinition leftFrontLeg = body.addOrReplaceChild("leftFrontLeg", CubeListBuilder.create().texOffs(103, 79).addBox(-5.0F, 0.0F, -2.5F, 5.0F, 14.0F, 5.0F, false), PartPose.offsetAndRotation(13.0F, 6.0F, 5.0F, 0.0F, 0.08726646F, -0.61086524F));
		EndimatorPartDefinition rightFrontLeg = body.addOrReplaceChild("rightFrontLeg", CubeListBuilder.create().texOffs(103, 79).addBox(0.0F, 0.0F, -2.5F, 5.0F, 14.0F, 5.0F, true), PartPose.offsetAndRotation(-13.0F, 6.0F, 5.0F, 0.0F, -0.08726646F, 0.61086524F));
		EndimatorPartDefinition leftBackLeg = body.addOrReplaceChild("leftBackLeg", CubeListBuilder.create().texOffs(103, 79).addBox(-5.0F, 0.0F, -2.5F, 5.0F, 14.0F, 5.0F, false), PartPose.offsetAndRotation(13.0F, 6.0F, 23.0F, 0.2617994F, 0.08726646F, -0.7853982F));
		EndimatorPartDefinition rightBackLeg = body.addOrReplaceChild("rightBackLeg", CubeListBuilder.create().texOffs(103, 79).addBox(0.0F, 0.0F, -2.5F, 5.0F, 14.0F, 5.0F, true), PartPose.offsetAndRotation(-13.0F, 6.0F, 23.0F, 0.2617994F, -0.08726646F, 0.7853982F));
		EndimatorPartDefinition horn = head.addOrReplaceChild("horn", CubeListBuilder.create().texOffs(0, 62).addBox(-3.0F, -15.0F, -3.0F, 6.0F, 15.0F, 6.0F, false), PartPose.offsetAndRotation(0.0F, 0.0F, -11.5F, 0.34906584F, 0.0F, 0.0F));
		EndimatorPartDefinition rightMandible = head.addOrReplaceChild("rightMandible", CubeListBuilder.create().texOffs(93, 60).addBox(-15.0F, 0.0F, -16.0F, 15.0F, 0.0F, 15.0F, true), PartPose.offsetAndRotation(0.0F, 3.0F, -13.0F, 0.0F, 0.0F, 0.0F));
		EndimatorPartDefinition leftMandible = head.addOrReplaceChild("leftMandible", CubeListBuilder.create().texOffs(93, 60).addBox(0.0F, 0.0F, -15.0F, 15.0F, 0.0F, 15.0F, false), PartPose.offsetAndRotation(0.0F, 3.0F, -14.0F, 0.0F, 0.0F, 0.0F));
		EndimatorPartDefinition hornTop = horn.addOrReplaceChild("hornTop", CubeListBuilder.create().texOffs(62, 38).addBox(0.0F, 0.0F, 0.0F, 26.0F, 20.0F, 0.0F, false), PartPose.offsetAndRotation(-13.0F, -30.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		EndimatorPartDefinition eggLayer = egg.addOrReplaceChild("eggLayer", CubeListBuilder.create().texOffs(210, 56).addBox(-13.0F, 0.0F, 0.0F, 27.0F, 21.0F, 18.0F, false), PartPose.offsetAndRotation(-0.5F, -10.5F, -0.5F, 0.0F, 0.0F, 0.0F));
		EndimatorPartDefinition eggSack = egg.addOrReplaceChild("eggSack", CubeListBuilder.create().texOffs(5, 27).addBox(-21.0F, -8.0F, 0.0F, 16.0F, 16.0F, 11.0F, false), PartPose.offsetAndRotation(13.0F, 0.0F, 17.0F, 0.0F, 0.0F, 0.0F));
		EndimatorPartDefinition eggMouthTop = eggSack.addOrReplaceChild("eggMouthTop", CubeListBuilder.create().texOffs(125, 60).addBox(0.0F, 13.0F, 0.0F, 21.0F, 21.0F, 17.0F, false), PartPose.offsetAndRotation(11.0F, -10.5F, 0.5F, 0.0F, 0.0F, 1.5707964F));
		EndimatorPartDefinition eggMouthBottom = eggSack.addOrReplaceChild("eggMouthBottom", CubeListBuilder.create().texOffs(125, 60).addBox(0.0F, -13.0F, 0.0F, 21.0F, 21.0F, 17.0F, false), PartPose.offsetAndRotation(-11.0F, 10.5F, 0.5F, 0.0F, 0.0F, 4.712389F));
		EndimatorPartDefinition eggMouthLeft = eggSack.addOrReplaceChild("eggMouthLeft", CubeListBuilder.create().texOffs(125, 60).addBox(0.0F, 0.0F, 0.0F, 21.0F, 21.0F, 17.0F, false), PartPose.offsetAndRotation(-2.0F, 10.5F, 0.5F, 0.0F, 0.0F, 3.1415927F));
		EndimatorPartDefinition eggMouthRight = eggSack.addOrReplaceChild("eggMouthRight", CubeListBuilder.create().texOffs(125, 60).addBox(0.0F, 0.0F, 0.0F, 21.0F, 21.0F, 17.0F, false), PartPose.offsetAndRotation(-24.0F, -10.5F, 0.5F, 0.0F, 0.0061086523F, 0.0F));
		EndimatorPartDefinition leftFrontFoot = leftFrontLeg.addOrReplaceChild("leftFrontFoot", CubeListBuilder.create().texOffs(77, 61).addBox(-3.5F, 0.0F, -3.5F, 7.0F, 14.0F, 7.0F, false), PartPose.offsetAndRotation(-2.5F, 12.0F, 0.0F, 0.034906585F, 0.0F, 0.57595867F));
		EndimatorPartDefinition rightFrontFoot = rightFrontLeg.addOrReplaceChild("rightFrontFoot", CubeListBuilder.create().texOffs(77, 61).addBox(-3.5F, 0.0F, -3.5F, 7.0F, 14.0F, 7.0F, true), PartPose.offsetAndRotation(2.5F, 12.0F, 0.0F, 0.034906585F, 0.0F, -0.57595867F));
		EndimatorPartDefinition leftBackFoot = leftBackLeg.addOrReplaceChild("leftBackFoot", CubeListBuilder.create().texOffs(77, 61).addBox(-3.5F, 0.0F, -3.5F, 7.0F, 14.0F, 7.0F, false), PartPose.offsetAndRotation(-2.5F, 12.0F, 0.0F, -0.13962634F, 0.0F, 0.7853982F));
		EndimatorPartDefinition rightBackFoot = rightBackLeg.addOrReplaceChild("rightBackFoot", CubeListBuilder.create().texOffs(77, 61).addBox(-3.5F, 0.0F, -3.5F, 7.0F, 14.0F, 7.0F, true), PartPose.offsetAndRotation(2.5F, 12.0F, 0.0F, -0.13962634F, 0.0F, -0.7853982F));
		return new EndimatorLayerDefinition(root, 300, 100);
	}

	@Override
	public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.wingLeft.visible = this.wingLeft.yRot >= WING_SHOW_THRESHOLD;
		this.wingRight.visible = this.wingRight.yRot <= -WING_SHOW_THRESHOLD;
		this.body.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public void setupAnim(BroodEetle eetle, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setupAnim(eetle, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

		FlyingRotations flyingRotations = eetle.getFlyingRotations();
		this.body.xRot += Mth.clamp(flyingRotations.getRenderFlyPitch(), -30.0F, 20.0F) * ((float) Math.PI / 180.0F);

		if (eetle.isFlying()) {
			this.body.zRot = flyingRotations.getRenderFlyRoll() * ((float) Math.PI / 180.0F);
		}

		float flyingProgress = eetle.getFlyingProgress();
		float sleepingProgress = eetle.getSleepingProgress();
		this.head.yRot = netHeadYaw * ((float) Math.PI / 180.0F);
		this.head.xRot += 0.35F * flyingProgress + (headPitch * ((float) Math.PI / 180.0F)) + 0.35F * sleepingProgress + sleepingProgress * 0.06F * Mth.sin(0.08F * ageInTicks);

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
		float scale = 1.0F + 0.05F * Math.abs(Mth.sin(0.05F * ageInTicks)) + 0.1F * Mth.sin((float) Math.PI * 0.5F * healPulseProgress);
		this.eggSack.setScale(scale, scale, scale);
		EndimatorModelPart egg = this.egg;
		float eggScale = 0.15F * healPulseProgress * healPulseProgress * healPulseProgress;
		egg.setScale(egg.xScale + eggScale, egg.yScale + eggScale, egg.zScale + eggScale);

		float eggMouthAngle = 0.087F * Math.abs(Mth.cos(0.05F * ageInTicks)) + 0.79F * eetle.getEggMouthProgress();
		this.eggMouthTop.yRot -= eggMouthAngle;
		this.eggMouthBottom.yRot -= eggMouthAngle;
		this.eggMouthRight.yRot -= eggMouthAngle;
		this.eggMouthLeft.yRot -= eggMouthAngle;

		egg.xRot += computeSmoothCurve(eetle.getEggCannonProgress(), 0.0F, 0.01F, 0.91F) - computeSmoothCurve(eetle.getEggCannonFlyingProgress(), 0.0F, 0.0F, 0.8F) + 0.07F * Mth.sin(0.09F * ageInTicks) - 0.44F * sleepingProgress;

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
			float wingX = 0.1F * Mth.sin(8.0F * eetle.getWingFlap()) + (0.26F * takeOffProgress);
			this.wingLeft.xRot += wingX;
			this.wingRight.xRot += wingX;

			float frontLegMoving = Math.abs(Mth.cos(limbSwing * 0.3F)) * 0.21F * limbSwingAmount;
			this.leftFrontLeg.xRot -= frontLegMoving;
			this.rightFrontLeg.xRot -= frontLegMoving;

			float backLegMoving = Math.abs(Mth.sin(limbSwing * 0.3F)) * 0.21F * limbSwingAmount;
			this.leftBackLeg.xRot += backLegMoving;
			this.rightBackLeg.xRot += backLegMoving;
		}
	}

	/**
	 * Computes a value from 0 to a maximum value over an adjusted gradient curve.
	 */
	private static float computeSmoothCurve(float progress, float fromGradient, float toGradient, float max) {
		return (fromGradient - toGradient - 2.0F * max) * (progress * progress * progress) + (3.0F * max - 2.0F * fromGradient - toGradient) * (progress * progress) + fromGradient * progress;
	}
}
