package com.minecraftabnormals.endergetic.client.models.purpoid;

import com.minecraftabnormals.endergetic.api.util.ModelUtil;
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

public class PurpoidGelModel extends EndimatorEntityModel<PurpoidEntity> {
	public static final ModelLayerLocation LOCATION = new ModelLayerLocation(new ResourceLocation(EndergeticExpansion.MOD_ID, "purpoid_gel"), "main");
	public ModelPart gelLayer;

	public PurpoidGelModel(ModelPart root) {
		this.gelLayer = root.getChild("gelLayer");
	}

	public static LayerDefinition createLayerDefinition() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition root = meshdefinition.getRoot();
		PartDefinition gelLayer = root.addOrReplaceChild("gelLayer", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 16.0F, 16.0F, false), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 64, 96);
	}

	public void parentToHead(ModelPart head) {
		ModelPart gelLayer = this.gelLayer;
		gelLayer.xRot = head.xRot;
		gelLayer.yRot = head.yRot;
		gelLayer.zRot = head.zRot;
		gelLayer.x = head.x;
		gelLayer.y = head.y + 1.0F;
		gelLayer.z = head.z;
	}

	@Override
	public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.gelLayer.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public void setupAnim(PurpoidEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		if (entity.isNoEndimationPlaying()) {
			float scaleOffset = Mth.sin(limbSwing * 0.6F) * Math.min(0.2F, limbSwingAmount);
			float horizontalScaleOffset = Math.max(-0.05F, scaleOffset);
			ModelUtil.setScale(this.gelLayer, 1.0F + horizontalScaleOffset, 1.0F - scaleOffset * 0.5F, 1.0F + horizontalScaleOffset);
		}
	}
}
