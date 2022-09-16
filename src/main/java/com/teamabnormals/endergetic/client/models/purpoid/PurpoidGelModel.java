package com.teamabnormals.endergetic.client.models.purpoid;

import com.teamabnormals.endergetic.api.util.ModelUtil;
import com.teamabnormals.endergetic.common.entities.purpoid.PurpoidEntity;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamabnormals.blueprint.client.ClientInfo;
import com.teamabnormals.blueprint.core.endimator.Endimation;
import com.teamabnormals.blueprint.core.endimator.Endimator;
import com.teamabnormals.blueprint.core.endimator.PlayableEndimation;
import com.teamabnormals.blueprint.core.endimator.entity.EndimatorEntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class PurpoidGelModel extends EndimatorEntityModel<PurpoidEntity> {
	public static final ModelLayerLocation PURPOID_LOCATION = new ModelLayerLocation(new ResourceLocation(EndergeticExpansion.MOD_ID, "purpoid_gel"), "main");
	public static final ModelLayerLocation PURP_LOCATION = new ModelLayerLocation(new ResourceLocation(EndergeticExpansion.MOD_ID, "purp_gel"), "main");
	public static final ModelLayerLocation PURPAZOID_LOCATION = new ModelLayerLocation(new ResourceLocation(EndergeticExpansion.MOD_ID, "purpazoid_gel"), "main");
	public ModelPart gelLayer;
	private float headScale;

	public PurpoidGelModel(ModelPart root) {
		this.gelLayer = root.getChild("gelLayer");
		this.endimator = Endimator.compile(root);
	}

	public static LayerDefinition createPurpoidLayerDefinition() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition root = meshdefinition.getRoot();
		PartDefinition gelLayer = root.addOrReplaceChild("gelLayer", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 16.0F, 16.0F, false), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 64, 96);
	}

	public static LayerDefinition createPurpLayerDefinition() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition root = meshdefinition.getRoot();
		PartDefinition gelLayer = root.addOrReplaceChild("gelLayer", CubeListBuilder.create().texOffs(0, 20).addBox(-5.0F, -10.0F, -5.0F, 10.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	public static LayerDefinition createPurpazoidLayerDefinition() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition root = meshdefinition.getRoot();
		PartDefinition gelLayer = root.addOrReplaceChild("gelLayer", CubeListBuilder.create().texOffs(112, 0).addBox(-16.0F, -32.0F, -16.0F, 32.0F, 32.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 256, 128);
	}

	public void parentToHead(ModelPart head) {
		ModelPart gelLayer = this.gelLayer;
		gelLayer.xRot = head.xRot;
		gelLayer.yRot = head.yRot;
		gelLayer.zRot = head.zRot;
		gelLayer.x = head.x;
		gelLayer.y = head.y + 1.0F;
		gelLayer.z = head.z;
		this.headScale = head.yScale;
	}

	@Override
	public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.gelLayer.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public void setupAnim(PurpoidEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		ModelPart gelLayer = this.gelLayer;
		gelLayer.xScale = gelLayer.yScale = gelLayer.zScale = 1.0F;
		PlayableEndimation playingEndimation = entity.getPlayingEndimation();
		Endimation endimation = playingEndimation.asEndimation();
		if (endimation != null) {
			float time = (entity.getAnimationTick() + ClientInfo.getPartialTicks()) * 0.05F;
			float length = endimation.getLength();
			if (time > length) {
				time = length;
			}
			this.endimator.apply(endimation, time, Endimator.ResetMode.RESET);
			entity.getEffectHandler().update(endimation, time);
		}
		if (playingEndimation == PlayableEndimation.BLANK) {
			float scaleOffset = Mth.sin((entity.isBaby() ? limbSwing / 3.0F : limbSwing) * 0.6F) * Math.min(0.17F, limbSwingAmount * 0.85F);
			float horizontalScaleOffset = Math.max(-0.05F, scaleOffset);
			float baseScale = entity.getRestOntoFlowerAnimationProgress(ClientInfo.getPartialTicks()) > 0.0F ? this.headScale : 1.0F;
			ModelUtil.setScale(this.gelLayer, baseScale + horizontalScaleOffset, baseScale - scaleOffset * 0.5F, baseScale + horizontalScaleOffset);
		}
	}
}
