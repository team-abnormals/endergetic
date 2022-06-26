package com.minecraftabnormals.endergetic.client.models.booflo;

import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity;

import com.teamabnormals.blueprint.client.ClientInfo;
import com.teamabnormals.blueprint.core.endimator.entity.EndimatorEntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * BoofloDefault - Endergized
 * Created using Tabula 7.0.0
 */
@OnlyIn(Dist.CLIENT)
public class BoofloModel<E extends BoofloEntity> extends EndimatorEntityModel<E> {
	public static final ModelLayerLocation LOCATION = new ModelLayerLocation(new ResourceLocation(EndergeticExpansion.MOD_ID, "booflo"), "main");

	public ModelPart Head;
	public ModelPart Jaw;
	public ModelPart KneeLeft;
	public ModelPart KneeRight;
	public ModelPart LegLeft;
	public ModelPart LegRight;
	public ModelPart LegBackLeft;
	public ModelPart LegBackRight;
	public ModelPart FruitPos;

	public ModelPart HeadInflated;
	public ModelPart KneeLeftInflated;
	public ModelPart KneeRightInflated;
	public ModelPart LegLeftInflated;
	public ModelPart LegRightInflated;
	public ModelPart JawInflated;
	public ModelPart LegBackLeftInflated;
	public ModelPart LegBackRightInflated;

	//Constructor
	public BoofloModel(ModelPart root) {
		this.Head = root.getChild("Head");
		this.Jaw = root.getChild("Jaw");
		this.KneeLeft = root.getChild("KneeLeft");
		this.KneeRight = root.getChild("KneeRight");
		this.LegLeft = root.getChild("LegLeft");
		this.LegRight = root.getChild("LegRight");
		this.LegBackLeft = root.getChild("LegBackLeft");
		this.LegBackRight = root.getChild("LegBackRight");
		this.FruitPos = root.getChild("FruitPos");
		this.HeadInflated = root.getChild("HeadInflated");
		this.KneeLeftInflated = root.getChild("KneeLeftInflated");
		this.KneeRightInflated = root.getChild("KneeRightInflated");
		this.LegLeftInflated = root.getChild("LegLeftInflated");
		this.LegRightInflated = root.getChild("LegRightInflated");
		this.JawInflated = root.getChild("JawInflated");
		this.LegBackLeftInflated = root.getChild("LegBackLeftInflated");
		this.LegBackRightInflated = root.getChild("LegBackRightInflated");
	}

	public static LayerDefinition createLayerDefinition() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition root = meshdefinition.getRoot();
		PartDefinition Head = root.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 29).addBox(-9.0F, -4.0F, -9.0F, 18.0F, 8.0F, 18.0F, false), PartPose.offsetAndRotation(0.0F, 9.0F, 0.0F, -0.091106184F, 0.0F, 0.0F));
		PartDefinition Jaw = root.addOrReplaceChild("Jaw", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, 0.0F, -16.0F, 16.0F, 6.0F, 16.0F, false), PartPose.offsetAndRotation(0.0F, 4.0F, 8.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition KneeLeft = root.addOrReplaceChild("KneeLeft", CubeListBuilder.create().texOffs(0, 58).addBox(-2.5F, -10.0F, -2.5F, 5.0F, 10.0F, 5.0F, false), PartPose.offsetAndRotation(5.5F, -2.0F, 5.5F, 0.0F, -0.17453292F, 0.7853982F));
		PartDefinition KneeRight = root.addOrReplaceChild("KneeRight", CubeListBuilder.create().texOffs(0, 58).addBox(-2.5F, -10.0F, -2.5F, 5.0F, 10.0F, 5.0F, false), PartPose.offsetAndRotation(-5.5F, -2.0F, 5.5F, 0.0F, 0.17453292F, -0.7853982F));
		PartDefinition LegLeft = root.addOrReplaceChild("LegLeft", CubeListBuilder.create().texOffs(28, 59).addBox(0.0F, -1.5F, -1.5F, 20.0F, 3.0F, 3.0F, false), PartPose.offsetAndRotation(8.5F, 2.0F, -3.0F, 0.0F, 0.5235988F, 1.0471976F));
		PartDefinition LegRight = root.addOrReplaceChild("LegRight", CubeListBuilder.create().texOffs(28, 59).addBox(0.0F, -1.5F, -1.5F, 20.0F, 3.0F, 3.0F, false), PartPose.offsetAndRotation(-8.5F, 2.0F, -3.0F, 0.0F, 0.5235988F, 2.0943952F));
		PartDefinition LegBackLeft = root.addOrReplaceChild("LegBackLeft", CubeListBuilder.create().texOffs(0, 81).addBox(0.0F, -1.5F, -1.5F, 25.0F, 3.0F, 3.0F, false), PartPose.offsetAndRotation(1.5F, -7.5F, -0.5F, 0.0F, -0.34906584F, 0.34906584F));
		PartDefinition LegBackRight = root.addOrReplaceChild("LegBackRight", CubeListBuilder.create().texOffs(0, 89).addBox(-25.0F, -1.5F, -1.5F, 25.0F, 3.0F, 3.0F, false), PartPose.offsetAndRotation(-1.5F, -7.5F, 0.0F, 0.0F, 0.34906584F, -0.34906584F));
		PartDefinition FruitPos = root.addOrReplaceChild("FruitPos", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, false), PartPose.offsetAndRotation(20.0F, -4.15F, -0.5F, -0.7853982F, -0.2617994F, -1.3089969F));
		PartDefinition HeadInflated = root.addOrReplaceChild("HeadInflated", CubeListBuilder.create().texOffs(0, 29).addBox(-9.0F, -4.0F, -9.0F, 18.0F, 8.0F, 18.0F, false), PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 3.1003275E-17F, 0.0F, 0.0F));
		PartDefinition KneeLeftInflated = root.addOrReplaceChild("KneeLeftInflated", CubeListBuilder.create().texOffs(0, 58).addBox(-2.5F, -10.0F, -2.5F, 5.0F, 10.0F, 5.0F, false), PartPose.offsetAndRotation(5.5F, -2.0F, 5.5F, 0.0F, -0.17453292F, 0.55850536F));
		PartDefinition KneeRightInflated = root.addOrReplaceChild("KneeRightInflated", CubeListBuilder.create().texOffs(0, 58).addBox(-2.5F, -10.0F, -2.5F, 5.0F, 10.0F, 5.0F, false), PartPose.offsetAndRotation(-5.5F, -2.0F, 5.5F, 0.0F, 0.17453292F, -0.55850536F));
		PartDefinition LegLeftInflated = root.addOrReplaceChild("LegLeftInflated", CubeListBuilder.create().texOffs(28, 59).addBox(0.0F, -1.5F, -1.5F, 20.0F, 3.0F, 3.0F, false), PartPose.offsetAndRotation(8.5F, 2.0F, -3.0F, 0.0F, 0.5235988F, -2.480262E-16F));
		PartDefinition LegRightInflated = root.addOrReplaceChild("LegRightInflated", CubeListBuilder.create().texOffs(28, 66).addBox(0.0F, -1.5F, -1.5F, 20.0F, 3.0F, 3.0F, false), PartPose.offsetAndRotation(-8.5F, 2.0F, -3.0F, 0.0F, 0.5235988F, 3.1415927F));
		PartDefinition JawInflated = root.addOrReplaceChild("JawInflated", CubeListBuilder.create().texOffs(0, 97).addBox(-16.0F, 0.0F, -24.0F, 32.0F, 16.0F, 32.0F, false), PartPose.offsetAndRotation(0.0F, 4.0F, 8.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition LegBackLeftInflated = root.addOrReplaceChild("LegBackLeftInflated", CubeListBuilder.create().texOffs(0, 81).addBox(0.0F, -1.5F, -1.5F, 25.0F, 3.0F, 3.0F, false), PartPose.offsetAndRotation(1.5F, -7.5F, -0.5F, 0.0F, -0.34906584F, 0.34906584F));
		PartDefinition LegBackRightInflated = root.addOrReplaceChild("LegBackRightInflated", CubeListBuilder.create().texOffs(0, 89).addBox(-25.0F, -1.5F, -1.5F, 25.0F, 3.0F, 3.0F, false), PartPose.offsetAndRotation(-1.5F, -7.5F, 0.0F, 0.0F, 0.34906584F, -0.34906584F));
		return LayerDefinition.create(meshdefinition, 150, 150);
	}

	@Override
	public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		if (this.entity.isBoofed()) {
			this.HeadInflated.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		} else {
			this.Head.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		}
	}

	@Override
	public void setupAnim(E booflo, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setupAnim(booflo, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

		if (booflo.isBoofed()) {
			this.HeadInflated.yRot = netHeadYaw * (float) (Math.PI / 180F);
			this.HeadInflated.xRot = headPitch * (float) (Math.PI / 180F);

			this.JawInflated.xRot += 0.4F * booflo.OPEN_JAW.getProgress(ClientInfo.getPartialTicks());
		}
	}
}