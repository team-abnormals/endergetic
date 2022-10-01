package com.teamabnormals.endergetic.client.models.purpoid;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamabnormals.endergetic.common.entities.purpoid.PurpoidEntity;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class PurpazoidModel extends PurpoidModel {
	public static final ModelLayerLocation LOCATION = new ModelLayerLocation(new ResourceLocation(EndergeticExpansion.MOD_ID, "purpazoid"), "main");

	public PurpazoidModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition createLayerDefinition() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-14.0F, -28.0F, -14.0F, 28.0F, 28.0F, 28.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition rim1 = head.addOrReplaceChild("rim1", CubeListBuilder.create().texOffs(25, 56).addBox(-17.0F, 0.0F, 0.0F, 34.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -14.0F, -0.7854F, 0.0F, 0.0F));
		PartDefinition rim2 = head.addOrReplaceChild("rim2", CubeListBuilder.create().texOffs(25, 56).addBox(-17.0F, 0.0F, 0.0F, 34.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 14.0F, 0.7854F, 0.0F, 0.0F));
		PartDefinition rim3 = head.addOrReplaceChild("rim3", CubeListBuilder.create().texOffs(25, 56).addBox(-17.0F, 0.0F, 0.0F, 34.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(14.0F, 0.0F, 0.0F, 0.7854F, 1.5708F, 0.0F));
		PartDefinition rim4 = head.addOrReplaceChild("rim4", CubeListBuilder.create().texOffs(25, 56).mirror().addBox(-17.0F, 0.0F, 0.0F, 34.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-14.0F, 0.0F, 0.0F, 0.7854F, -1.5708F, 0.0F));
		PartDefinition ear1 = head.addOrReplaceChild("ear1", CubeListBuilder.create().texOffs(84, 0).addBox(0.0F, -22.0F, 0.0F, 15.0F, 28.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(14.0F, -13.0F, 0.0F, 0.0F, -0.4363F, 0.2618F));
		PartDefinition ear2 = head.addOrReplaceChild("ear2", CubeListBuilder.create().texOffs(84, 0).mirror().addBox(-15.0F, -22.0F, 0.0F, 15.0F, 28.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-14.0F, -13.0F, 0.0F, 0.0F, 0.4363F, -0.2618F));
		PartDefinition tentacleSmall4 = head.addOrReplaceChild("tentacleSmall4", CubeListBuilder.create().texOffs(0, 56).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 60.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-12.0F, 0.0F, -12.0F, 0.0F, 0.7854F, 0.0F));
		PartDefinition tentacleSmall3 = head.addOrReplaceChild("tentacleSmall3", CubeListBuilder.create().texOffs(0, 56).mirror().addBox(-4.0F, 0.0F, 0.0F, 8.0F, 60.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(12.0F, 0.0F, -12.0F, 0.0F, -0.7854F, 0.0F));
		PartDefinition tentacleSmall2 = head.addOrReplaceChild("tentacleSmall2", CubeListBuilder.create().texOffs(0, 56).mirror().addBox(-4.0F, 0.0F, 0.0F, 8.0F, 60.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(12.0F, 0.0F, 12.0F, 0.0F, 0.7854F, 0.0F));
		PartDefinition tentacleSmall1 = head.addOrReplaceChild("tentacleSmall1", CubeListBuilder.create().texOffs(0, 56).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 60.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-12.0F, 0.0F, 12.0F, 0.0F, -0.7854F, 0.0F));
		PartDefinition tentacleLarge4 = head.addOrReplaceChild("tentacleLarge4", CubeListBuilder.create().texOffs(95, 56).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 72.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -14.0F));
		PartDefinition tentacleLarge3 = head.addOrReplaceChild("tentacleLarge3", CubeListBuilder.create().texOffs(95, 56).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 72.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 14.0F));
		PartDefinition tentacleLarge1 = head.addOrReplaceChild("tentacleLarge1", CubeListBuilder.create().texOffs(95, 56).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 72.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-14.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));
		PartDefinition tentacleLarge2 = head.addOrReplaceChild("tentacleLarge2", CubeListBuilder.create().texOffs(95, 56).mirror().addBox(-4.0F, 0.0F, 0.0F, 8.0F, 72.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(14.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));
		return LayerDefinition.create(meshdefinition, 256, 128);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.head.render(poseStack, buffer, packedLight, packedOverlay);
	}

	@Override
	protected void applyRestingAnimation(PurpoidEntity purpoid) {}
}