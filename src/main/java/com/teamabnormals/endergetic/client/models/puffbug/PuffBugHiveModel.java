package com.teamabnormals.endergetic.client.models.puffbug;

import com.teamabnormals.endergetic.core.EndergeticExpansion;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

/**
 * ModelPuffBugHive - Endergized
 * Created using Tabula 7.0.0
 */
public class PuffBugHiveModel {
	public static final ModelLayerLocation LOCATION = new ModelLayerLocation(new ResourceLocation(EndergeticExpansion.MOD_ID, "puff_bug_hive"), "main");
	public ModelPart HiveBase;
	public ModelPart HiveBottom;

	public PuffBugHiveModel(ModelPart root) {
		this.HiveBase = root.getChild("HiveBase");
		this.HiveBottom = root.getChild("HiveBottom");
	}

	public static LayerDefinition createLayerDefinition() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition root = meshdefinition.getRoot();
		PartDefinition HiveBase = root.addOrReplaceChild("HiveBase", CubeListBuilder.create().texOffs(0, 1).addBox(0.0F, 0.0F, 0.0F, 16.0F, 13.0F, 16.0F, false), PartPose.offsetAndRotation(-8.0F, 8.0F, -8.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition HiveBottom = root.addOrReplaceChild("HiveBottom", CubeListBuilder.create().texOffs(0, 15).addBox(0.0F, 0.0F, 0.0F, 14.0F, 3.0F, 14.0F, false), PartPose.offsetAndRotation(-7.0F, 21.0F, -7.0F, 0.0F, 0.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 100, 64);
	}

	public void renderAll(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn) {
		this.HiveBottom.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
		this.HiveBase.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
	}

	public void setRotateAngle(ModelPart ModelRenderer, float x, float y, float z) {
		ModelRenderer.xRot = x;
		ModelRenderer.yRot = y;
		ModelRenderer.zRot = z;
	}
}
