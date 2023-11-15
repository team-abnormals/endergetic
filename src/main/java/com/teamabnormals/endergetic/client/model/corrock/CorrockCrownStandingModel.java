package com.teamabnormals.endergetic.client.model.corrock;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

/**
 * ModelCorrockCrownStanding - Endergized
 * Created using Tabula 7.0.0
 */
public class CorrockCrownStandingModel {
	public static final ModelLayerLocation LOCATION = new ModelLayerLocation(new ResourceLocation(EndergeticExpansion.MOD_ID, "corrock_crown_standing"), "main");
	public ModelPart shape1;
	public ModelPart shape1_1;

	public CorrockCrownStandingModel(ModelPart root) {
		this.shape1 = root.getChild("shape1");
		this.shape1_1 = root.getChild("shape1_1");
	}

	public static LayerDefinition createLayerDefinition() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition root = meshdefinition.getRoot();
		PartDefinition shape1 = root.addOrReplaceChild("shape1", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -16.0F, 0.0F, 16.0F, 16.0F, 0.0F, true), PartPose.offsetAndRotation(-8.0F, 24.0F, 0.0F, 0.17453292F, 0.0F, 0.0F));
		PartDefinition shape1_1 = root.addOrReplaceChild("shape1_1", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -16.0F, 0.0F, 16.0F, 16.0F, 0.0F, false), PartPose.offsetAndRotation(-8.0F, 24.0F, 0.0F, -0.17453292F, 0.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	public void renderAll(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn) {
		this.shape1.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
		this.shape1_1.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
	}

	public void setRotateAngle(ModelPart ModelRenderer, float x, float y, float z) {
		ModelRenderer.xRot = x;
		ModelRenderer.yRot = y;
		ModelRenderer.zRot = z;
	}
}
