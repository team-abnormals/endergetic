package com.teamabnormals.endergetic.client.model.purpoid;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamabnormals.blueprint.client.ClientInfo;
import com.teamabnormals.endergetic.common.entity.purpoid.Purpoid;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class PurpModel extends PurpoidModel {
	public static final ModelLayerLocation LOCATION = new ModelLayerLocation(new ResourceLocation(EndergeticExpansion.MOD_ID, "purp"), "main");

	public PurpModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition createLayerDefinition() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition root = meshdefinition.getRoot();
		PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 23.3F, 0.0F));
		PartDefinition rim4 = head.addOrReplaceChild("rim2", CubeListBuilder.create().texOffs(7, 16).addBox(-5.0F, 0.0F, 0.0F, 10.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 4.0F, 0.5236F, 0.0F, 0.0F));
		PartDefinition rim2 = head.addOrReplaceChild("rim4", CubeListBuilder.create().texOffs(7, 16).addBox(-5.0F, 0.0F, 0.0F, 10.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 0.0F, 0.0F, -0.5236F, 1.5708F, 0.0F));
		PartDefinition tentacleLarge4 = head.addOrReplaceChild("tentacleLarge4", CubeListBuilder.create().texOffs(32, 14).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -4.0F));
		PartDefinition tentacleSmall2 = head.addOrReplaceChild("tentacleSmall2", CubeListBuilder.create().texOffs(38, 14).mirror().addBox(-1.5F, 0.0F, 0.0F, 3.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(3.5F, 0.0F, 3.5F, 0.0F, 0.7854F, 0.0F));
		PartDefinition rim3 = head.addOrReplaceChild("rim3", CubeListBuilder.create().texOffs(7, 16).addBox(-5.0F, 0.0F, 0.0F, 10.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 0.0F, 0.0F, 0.5236F, 1.5708F, 0.0F));
		PartDefinition tentacleSmall4 = head.addOrReplaceChild("tentacleSmall4", CubeListBuilder.create().texOffs(38, 14).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.5F, 0.0F, -3.5F, 0.0F, 0.7854F, 0.0F));
		PartDefinition tentacleLarge3 = head.addOrReplaceChild("tentacleLarge3", CubeListBuilder.create().texOffs(32, 14).mirror().addBox(-1.5F, 0.0F, 0.0F, 3.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 4.0F));
		PartDefinition tentacleSmall1 = head.addOrReplaceChild("tentacleSmall1", CubeListBuilder.create().texOffs(38, 14).mirror().addBox(-1.5F, 0.0F, 0.0F, 3.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.5F, 0.0F, 3.5F, 0.0F, -0.7854F, 0.0F));
		PartDefinition rim1 = head.addOrReplaceChild("rim1", CubeListBuilder.create().texOffs(7, 16).addBox(-5.0F, 0.0F, 0.0F, 10.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -4.0F, -0.5236F, 0.0F, 0.0F));
		PartDefinition tentacleLarge2 = head.addOrReplaceChild("tentacleLarge2", CubeListBuilder.create().texOffs(32, 14).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));
		PartDefinition tentacleLarge1 = head.addOrReplaceChild("tentacleLarge1", CubeListBuilder.create().texOffs(32, 14).mirror().addBox(-1.5F, 0.0F, 0.0F, 3.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));
		PartDefinition tentacleSmall3 = head.addOrReplaceChild("tentacleSmall3", CubeListBuilder.create().texOffs(38, 14).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.5F, 0.0F, -3.5F, 0.0F, -0.7854F, 0.0F));
		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	protected void applyRestingAnimation(Purpoid purpoid) {
		float restingTentacleAngle = 1.65806F * purpoid.getRestOntoAnimationProgress(ClientInfo.getPartialTicks());
		if (restingTentacleAngle > 0.0F) {
			this.tentacleLarge1.xRot -= restingTentacleAngle;
			this.tentacleLarge2.xRot += restingTentacleAngle;
			this.tentacleLarge3.xRot += restingTentacleAngle;
			this.tentacleLarge4.xRot -= restingTentacleAngle;
			this.tentacleSmall1.xRot += restingTentacleAngle;
			this.tentacleSmall2.xRot += restingTentacleAngle;
			this.tentacleSmall3.xRot -= restingTentacleAngle;
			this.tentacleSmall4.xRot -= restingTentacleAngle;
		}
	}
}