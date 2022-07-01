package com.minecraftabnormals.endergetic.client.models.eetle;

import com.minecraftabnormals.endergetic.common.entities.eetle.ChargerEetleEntity;
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
 * <p>
 * Created using Tabula 7.0.0
 */
public class ChargerEetleModel extends EndimatorEntityModel<ChargerEetleEntity> {
	public static final ModelLayerLocation LOCATION = new ModelLayerLocation(new ResourceLocation(EndergeticExpansion.MOD_ID, "charger_eetle"), "main");

	public ModelPart body;
	public ModelPart frontLeftLeg;
	public ModelPart frontRightLeg;
	public ModelPart backLeftLeg;
	public ModelPart backRightLeg;
	public ModelPart rightWing;
	public ModelPart leftWing;
	public ModelPart head;
	public ModelPart mouth;
	public ModelPart claw;

	public ChargerEetleModel(ModelPart root) {
		this.body = root.getChild("body");
		this.frontLeftLeg = root.getChild("frontLeftLeg");
		this.frontRightLeg = root.getChild("frontRightLeg");
		this.backLeftLeg = root.getChild("backLeftLeg");
		this.backRightLeg = root.getChild("backRightLeg");
		this.rightWing = this.body.getChild("rightWing");
		this.leftWing = this.body.getChild("leftWing");
		this.head = this.body.getChild("head");
		this.mouth = this.head.getChild("mouth");
		this.claw = this.mouth.getChild("claw");
		this.endimator = Endimator.compile(root);
	}

	public static LayerDefinition createLayerDefinition() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition root = meshdefinition.getRoot();
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(28, 16).addBox(-4.0F, -3.0F, 0.0F, 8.0F, 6.0F, 10.0F, false), PartPose.offsetAndRotation(0.0F, 13.5F, -3.5F, -0.17453292F, 0.0F, 0.0F));
		PartDefinition frontLeftLeg = root.addOrReplaceChild("frontLeftLeg", CubeListBuilder.create().texOffs(52, 0).addBox(0.0F, 0.0F, -1.5F, 3.0F, 12.0F, 3.0F, true), PartPose.offsetAndRotation(2.0F, 16.0F, -1.5F, -0.4886922F, 0.0F, -0.7853982F));
		PartDefinition frontRightLeg = root.addOrReplaceChild("frontRightLeg", CubeListBuilder.create().texOffs(52, 0).addBox(-3.0F, 0.0F, -1.5F, 3.0F, 12.0F, 3.0F, false), PartPose.offsetAndRotation(-2.0F, 16.0F, -1.5F, -0.4886922F, 0.0F, 0.7853982F));
		PartDefinition backLeftLeg = root.addOrReplaceChild("backLeftLeg", CubeListBuilder.create().texOffs(52, 0).addBox(0.0F, 0.0F, -1.5F, 3.0F, 12.0F, 3.0F, true), PartPose.offsetAndRotation(2.0F, 16.0F, 4.5F, 0.43633232F, 0.15707964F, -0.7853982F));
		PartDefinition backRightLeg = root.addOrReplaceChild("backRightLeg", CubeListBuilder.create().texOffs(52, 0).addBox(-3.0F, 0.0F, -1.5F, 3.0F, 12.0F, 3.0F, false), PartPose.offsetAndRotation(-2.0F, 16.0F, 4.5F, 0.43633232F, 0.0F, 0.7853982F));
		PartDefinition rightWing = body.addOrReplaceChild("rightWing", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -1.0F, -1.0F, 5.0F, 6.0F, 12.0F, false), PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, -0.08726646F, -0.034906585F, 0.0F));
		PartDefinition leftWing = body.addOrReplaceChild("leftWing", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -1.0F, -1.0F, 5.0F, 6.0F, 12.0F, true), PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, -0.08726646F, 0.034906585F, 0.0F));
		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 23).addBox(-3.0F, -2.0F, -5.0F, 6.0F, 4.0F, 5.0F, false), PartPose.offsetAndRotation(0.0F, 0.5F, 0.0F, 0.35F, 0.0F, 0.0F));
		PartDefinition mouth = head.addOrReplaceChild("mouth", CubeListBuilder.create().texOffs(22, 0).addBox(-2.0F, -8.0F, 0.0F, 4.0F, 8.0F, 3.0F, false), PartPose.offsetAndRotation(0.0F, 2.0F, -5.0F, 0.5235988F, 0.0F, 0.0F));
		PartDefinition claw = mouth.addOrReplaceChild("claw", CubeListBuilder.create().texOffs(20, 18).addBox(-3.0F, -5.0F, -1.5F, 6.0F, 5.0F, 3.0F, false), PartPose.offsetAndRotation(0.0F, -7.0F, 1.0F, -0.43633232F, 0.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	public void setupAnim(ChargerEetleEntity eetle, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setupAnim(eetle, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

		this.head.yRot = netHeadYaw * ((float) Math.PI / 180.0F);
		this.head.xRot += (headPitch * ((float) Math.PI / 180.0F));

		float angleX = Mth.cos(limbSwing * 1.34F + ((float) Math.PI * 1.5F)) * limbSwingAmount;
		float backAngleX = Mth.cos(limbSwing * 1.34F) * limbSwingAmount;
		this.frontLeftLeg.xRot -= angleX;
		this.frontRightLeg.xRot += angleX;
		this.backLeftLeg.xRot += backAngleX;
		this.backRightLeg.xRot -= backAngleX;

		float angleZ = Math.abs(Mth.sin(limbSwing * 0.67F + ((float) Math.PI * 1.5F))) * 0.5F * limbSwingAmount;
		float backAngleZ = Math.abs(Mth.sin(limbSwing * 0.67F)) * 0.5F * limbSwingAmount;
		this.frontLeftLeg.zRot -= angleZ;
		this.frontRightLeg.zRot += angleZ;
		this.backLeftLeg.zRot -= backAngleZ;
		this.backRightLeg.zRot += backAngleZ;
	}

	@Override
	public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		this.body.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		this.frontLeftLeg.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		this.frontRightLeg.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		this.backLeftLeg.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		this.backRightLeg.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}
}
