package com.minecraftabnormals.endergetic.client.models.eetle;

import com.minecraftabnormals.endergetic.common.entities.eetle.GliderEetleEntity;
import com.minecraftabnormals.endergetic.common.entities.eetle.flying.FlyingRotations;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamabnormals.blueprint.core.endimator.Endimator;
import com.teamabnormals.blueprint.core.endimator.entity.EndimatorEntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

/**
 * @author Endergized
 * @author SmellyModder (Luke Tonon)
 * Created using Tabula 7.0.0
 */
public class GliderEetleModel extends EndimatorEntityModel<GliderEetleEntity> {
	public static final ModelLayerLocation LOCATION = new ModelLayerLocation(new ResourceLocation(EndergeticExpansion.MOD_ID, "glider_eetle"), "main");
	private static final float WING_SHOW_THRESHOLD = (float) Math.toRadians(9.0F);
	public ModelPart body;
	public ModelPart rightElytron;
	public ModelPart leftElytron;
	public ModelPart leftWing;
	public ModelPart rightWing;
	public ModelPart head;
	public ModelPart leftBackLeg;
	public ModelPart rightBackLeg;
	public ModelPart rightFrontLeg;
	public ModelPart leftFrontLeg;
	public ModelPart leftMandible;
	public ModelPart rightMandible;

	//Constructor
	public GliderEetleModel(ModelPart root) {
		this.body = root.getChild("body");
		this.rightElytron = this.body.getChild("rightElytron");
		this.leftElytron = this.body.getChild("leftElytron");
		this.leftWing = this.body.getChild("leftWing");
		this.rightWing = this.body.getChild("rightWing");
		this.head = this.body.getChild("head");
		this.leftBackLeg = this.body.getChild("leftBackLeg");
		this.rightBackLeg = this.body.getChild("rightBackLeg");
		this.rightFrontLeg = this.body.getChild("rightFrontLeg");
		this.leftFrontLeg = this.body.getChild("leftFrontLeg");
		this.leftMandible = this.head.getChild("leftMandible");
		this.rightMandible = this.head.getChild("rightMandible");

		this.endimator = Endimator.compile(root);
	}

	public static LayerDefinition createLayerDefinition() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition root = meshdefinition.getRoot();
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(28, 16).addBox(-4.0F, -3.0F, 0.0F, 8.0F, 6.0F, 10.0F, false), PartPose.offsetAndRotation(0.0F, 13.3F, -4.0F, -0.17453292F, 0.0F, 0.0F));
		PartDefinition rightElytron = body.addOrReplaceChild("rightElytron", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -1.0F, -1.0F, 5.0F, 6.0F, 12.0F, false), PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, 0.0F, -0.034906585F, 0.0F));
		PartDefinition leftElytron = body.addOrReplaceChild("leftElytron", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -1.0F, -1.0F, 5.0F, 6.0F, 12.0F, true), PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, 0.0F, 0.034906585F, 0.0F));
		PartDefinition leftWing = body.addOrReplaceChild("leftWing", CubeListBuilder.create().texOffs(22, 0).addBox(0.0F, 0.0F, 0.0F, 7.0F, 0.0F, 16.0F, false), PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, 0.05F, 0.0F, 0.0F));
		PartDefinition rightWing = body.addOrReplaceChild("rightWing", CubeListBuilder.create().texOffs(22, 0).addBox(-7.0F, 0.0F, 0.0F, 7.0F, 0.0F, 16.0F, true), PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, 0.05F, 0.0F, 0.0F));
		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 23).addBox(-3.0F, -2.0F, -5.0F, 6.0F, 4.0F, 5.0F, false), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.34906584F, 0.0F, 0.0F));
		PartDefinition leftBackLeg = body.addOrReplaceChild("leftBackLeg", CubeListBuilder.create().texOffs(52, 0).addBox(0.0F, 0.0F, -1.5F, 3.0F, 12.0F, 3.0F, true), PartPose.offsetAndRotation(1.9F, 1.0F, 8.0F, 0.61086524F, 0.0F, -0.7853982F));
		PartDefinition rightBackLeg = body.addOrReplaceChild("rightBackLeg", CubeListBuilder.create().texOffs(52, 0).addBox(-3.0F, 0.0F, -1.5F, 3.0F, 12.0F, 3.0F, true), PartPose.offsetAndRotation(-1.9F, 1.0F, 8.0F, 0.61086524F, 0.0F, 0.7853982F));
		PartDefinition rightFrontLeg = body.addOrReplaceChild("rightFrontLeg", CubeListBuilder.create().texOffs(52, 0).addBox(-3.0F, 0.0F, -1.5F, 3.0F, 12.0F, 3.0F, true), PartPose.offsetAndRotation(-2.0F, 2.5F, 2.0F, -0.2617994F, 0.0F, 0.7853982F));
		PartDefinition leftFrontLeg = body.addOrReplaceChild("leftFrontLeg", CubeListBuilder.create().texOffs(52, 0).addBox(0.0F, 0.0F, -1.5F, 3.0F, 12.0F, 3.0F, true), PartPose.offsetAndRotation(2.0F, 2.5F, 2.0F, -0.2617994F, 0.0F, -0.7853982F));
		PartDefinition leftMandible = head.addOrReplaceChild("leftMandible", CubeListBuilder.create().texOffs(16, 0).addBox(0.0F, 0.0F, -6.0F, 5.0F, 0.0F, 6.0F, false), PartPose.offsetAndRotation(0.0F, 0.0F, -5.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition rightMandible = head.addOrReplaceChild("rightMandible", CubeListBuilder.create().texOffs(16, 0).addBox(-5.0F, 0.0F, -6.0F, 5.0F, 0.0F, 6.0F, true), PartPose.offsetAndRotation(0.0F, 0.0F, -5.0F, 0.0F, 0.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 64, 32);
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
			float wingX = 0.1F * Mth.sin(8.0F * entity.getWingFlap()) + 0.15F;

			this.leftWing.xRot += wingX;
			this.rightWing.xRot += wingX;

			float frontLegMoving = Math.abs(Mth.cos(limbSwing * 0.3F)) * 0.28F * limbSwingAmount;
			this.leftFrontLeg.xRot -= frontLegMoving;
			this.rightFrontLeg.xRot -= frontLegMoving;

			float backLegMoving = Math.abs(Mth.sin(limbSwing * 0.3F)) * 0.28F * limbSwingAmount;
			this.leftBackLeg.xRot += backLegMoving;
			this.rightBackLeg.xRot += backLegMoving;
		} else {
			float angleX = Mth.cos(limbSwing * 1.34F + ((float) Math.PI * 1.5F)) * limbSwingAmount;
			float backAngleX = Mth.cos(limbSwing * 1.34F) * limbSwingAmount;
			this.leftFrontLeg.xRot -= angleX;
			this.rightFrontLeg.xRot += angleX;
			this.leftBackLeg.xRot += backAngleX;
			this.rightBackLeg.xRot -= backAngleX;

			float angleZ = Math.abs(Mth.sin(limbSwing * 0.67F + ((float) Math.PI * 1.5F))) * 0.5F * limbSwingAmount;
			float backAngleZ = Math.abs(Mth.sin(limbSwing * 0.67F)) * 0.5F * limbSwingAmount;
			this.leftFrontLeg.zRot -= angleZ;
			this.rightFrontLeg.zRot += angleZ;
			this.leftBackLeg.zRot -= backAngleZ;
			this.rightBackLeg.zRot += backAngleZ;
		}
	}

	@Override
	public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.leftWing.visible = this.leftWing.yRot >= WING_SHOW_THRESHOLD;
		this.rightWing.visible = this.rightWing.yRot <= -WING_SHOW_THRESHOLD;
		this.body.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
