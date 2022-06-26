package com.minecraftabnormals.endergetic.client.models.purpoid;

import com.minecraftabnormals.endergetic.common.entities.purpoid.PurpoidEntity;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
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
public class PurpoidModel extends EndimatorEntityModel<PurpoidEntity> {
	public static final ModelLayerLocation LOCATION = new ModelLayerLocation(new ResourceLocation(EndergeticExpansion.MOD_ID, "purpoid"), "main");
	public ModelPart head;
	public ModelPart rim1;
	public ModelPart rim2;
	public ModelPart rim3;
	public ModelPart rim4;
	public ModelPart tentacleSmall1;
	public ModelPart tentacleSmall2;
	public ModelPart tentacleSmall3;
	public ModelPart tentacleSmall4;
	public ModelPart tentacleLarge1;
	public ModelPart tentacleLarge2;
	public ModelPart tentacleLarge3;
	public ModelPart tentacleLarge4;

	public PurpoidModel(ModelPart root) {
		this.head = root.getChild("head");
		this.rim1 = root.getChild("rim1");
		this.rim2 = root.getChild("rim2");
		this.rim3 = root.getChild("rim3");
		this.rim4 = root.getChild("rim4");
		this.tentacleSmall1 = root.getChild("tentacleSmall1");
		this.tentacleSmall2 = root.getChild("tentacleSmall2");
		this.tentacleSmall3 = root.getChild("tentacleSmall3");
		this.tentacleSmall4 = root.getChild("tentacleSmall4");
		this.tentacleLarge1 = root.getChild("tentacleLarge1");
		this.tentacleLarge2 = root.getChild("tentacleLarge2");
		this.tentacleLarge3 = root.getChild("tentacleLarge3");
		this.tentacleLarge4 = root.getChild("tentacleLarge4");
	}

	public static LayerDefinition createLayerDefinition() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition root = meshdefinition.getRoot();
		PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 32).addBox(-7.0F, -13.0F, -7.0F, 14.0F, 13.0F, 14.0F, false), PartPose.offsetAndRotation(0.0F, 23.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition rim1 = root.addOrReplaceChild("rim1", CubeListBuilder.create().texOffs(12, 59).addBox(-9.0F, 0.0F, 0.0F, 18.0F, 6.0F, 0.0F, false), PartPose.offsetAndRotation(0.0F, 0.0F, -7.0F, -0.7853982F, 0.0F, 0.0F));
		PartDefinition rim2 = root.addOrReplaceChild("rim2", CubeListBuilder.create().texOffs(12, 59).addBox(-9.0F, 0.0F, 0.0F, 18.0F, 6.0F, 0.0F, false), PartPose.offsetAndRotation(0.0F, 0.0F, 7.0F, 0.7853982F, 0.0F, 0.0F));
		PartDefinition rim3 = root.addOrReplaceChild("rim3", CubeListBuilder.create().texOffs(12, 41).addBox(0.0F, 0.0F, -9.0F, 0.0F, 6.0F, 18.0F, false), PartPose.offsetAndRotation(7.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.7853982F));
		PartDefinition rim4 = root.addOrReplaceChild("rim4", CubeListBuilder.create().texOffs(12, 41).addBox(0.0F, 0.0F, -9.0F, 0.0F, 6.0F, 18.0F, false), PartPose.offsetAndRotation(-7.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7853982F));
		PartDefinition tentacleSmall1 = root.addOrReplaceChild("tentacleSmall1", CubeListBuilder.create().texOffs(0, 59).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 32.0F, 0.0F, false), PartPose.offsetAndRotation(-5.5F, 0.0F, 6.0F, 0.0F, -0.7853982F, 0.0F));
		PartDefinition tentacleSmall2 = root.addOrReplaceChild("tentacleSmall2", CubeListBuilder.create().texOffs(0, 59).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 32.0F, 0.0F, false), PartPose.offsetAndRotation(5.5F, 0.0F, 6.0F, 0.0F, 0.7853982F, 0.0F));
		PartDefinition tentacleSmall3 = root.addOrReplaceChild("tentacleSmall3", CubeListBuilder.create().texOffs(0, 59).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 32.0F, 0.0F, true), PartPose.offsetAndRotation(5.5F, 0.0F, -6.0F, 0.0F, -0.7853982F, 0.0F));
		PartDefinition tentacleSmall4 = root.addOrReplaceChild("tentacleSmall4", CubeListBuilder.create().texOffs(0, 59).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 32.0F, 0.0F, false), PartPose.offsetAndRotation(-5.5F, 0.0F, -6.0F, 0.0F, 0.7853982F, 0.0F));
		PartDefinition tentacleLarge1 = root.addOrReplaceChild("tentacleLarge1", CubeListBuilder.create().texOffs(6, 59).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 37.0F, 0.0F, true), PartPose.offsetAndRotation(-7.0F, 0.0F, 0.0F, 0.0F, 1.5707964F, 0.0F));
		PartDefinition tentacleLarge2 = root.addOrReplaceChild("tentacleLarge2", CubeListBuilder.create().texOffs(6, 59).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 37.0F, 0.0F, false), PartPose.offsetAndRotation(7.0F, 0.0F, 0.0F, 0.0F, 1.5707964F, 0.0F));
		PartDefinition tentacleLarge3 = root.addOrReplaceChild("tentacleLarge3", CubeListBuilder.create().texOffs(6, 59).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 37.0F, 0.0F, true), PartPose.offsetAndRotation(0.0F, 0.0F, 7.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition tentacleLarge4 = root.addOrReplaceChild("tentacleLarge4", CubeListBuilder.create().texOffs(6, 59).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 37.0F, 0.0F, false), PartPose.offsetAndRotation(0.0F, 0.0F, -7.0F, 0.0F, 0.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 64, 96);
	}

	@Override
	public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.head.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public void setupAnim(PurpoidEntity purpoid, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setupAnim(purpoid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		if (purpoid.isBaby()) limbSwing /= 3.0F;
		float rimAngle = 0.17F * Mth.sin(0.1F * ageInTicks) + Mth.cos(limbSwing * 0.8F) * limbSwingAmount * 1.16F;
		this.rim1.xRot -= rimAngle;
		this.rim2.xRot += rimAngle;
		this.rim3.zRot -= rimAngle;
        this.rim4.zRot += rimAngle;
        float tentacleAngle = 0.09F * Mth.cos(0.1F * ageInTicks + 1.0F) + Mth.sin(limbSwing * 0.6F) * Math.min(0.3F, limbSwingAmount) * 0.5F;
        this.tentacleLarge1.xRot -= tentacleAngle;
		this.tentacleLarge2.xRot += tentacleAngle;
		this.tentacleLarge3.xRot += tentacleAngle;
		this.tentacleLarge4.xRot -= tentacleAngle;
		this.tentacleSmall1.xRot += tentacleAngle;
		this.tentacleSmall2.xRot += tentacleAngle;
		this.tentacleSmall3.xRot -= tentacleAngle;
		this.tentacleSmall4.xRot -= tentacleAngle;
	}
}
