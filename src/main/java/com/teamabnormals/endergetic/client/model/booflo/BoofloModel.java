package com.teamabnormals.endergetic.client.model.booflo;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamabnormals.blueprint.client.ClientInfo;
import com.teamabnormals.blueprint.core.endimator.Endimator;
import com.teamabnormals.blueprint.core.endimator.entity.EndimatorEntityModel;
import com.teamabnormals.endergetic.common.entity.booflo.Booflo;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
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
public class BoofloModel<E extends Booflo> extends EndimatorEntityModel<E> {
	public static final ModelLayerLocation LOCATION = new ModelLayerLocation(new ResourceLocation(EndergeticExpansion.MOD_ID, "booflo"), "main");

	public ModelPart Head;
	public ModelPart Jaw;
	public ModelPart KneeLeft;
	public ModelPart KneeRight;
	public ModelPart LegLeft;
	public ModelPart LegRight;
	public ModelPart LegBackLeft;
	public ModelPart LegBackRight;

	public ModelPart HeadInflated;
	public ModelPart KneeLeftInflated;
	public ModelPart KneeRightInflated;
	public ModelPart LegLeftInflated;
	public ModelPart LegRightInflated;
	public ModelPart JawInflated;
	public ModelPart LegBackLeftInflated;
	public ModelPart LegBackRightInflated;

	public BoofloModel(ModelPart root) {
		this.Head = root.getChild("Head");
		this.Jaw = this.Head.getChild("Jaw");
		this.KneeLeft = this.Head.getChild("KneeLeft");
		this.KneeRight = this.Head.getChild("KneeRight");
		this.LegLeft = this.Head.getChild("LegLeft");
		this.LegRight = this.Head.getChild("LegRight");
		this.LegBackLeft = this.KneeLeft.getChild("LegBackLeft");
		this.LegBackRight = this.KneeRight.getChild("LegBackRight");

		this.HeadInflated = root.getChild("HeadInflated");
		this.KneeLeftInflated = this.HeadInflated.getChild("KneeLeftInflated");
		this.KneeRightInflated = this.HeadInflated.getChild("KneeRightInflated");
		this.LegLeftInflated = this.HeadInflated.getChild("LegLeftInflated");
		this.LegRightInflated = this.HeadInflated.getChild("LegRightInflated");
		this.JawInflated = this.HeadInflated.getChild("JawInflated");
		this.LegBackLeftInflated = this.KneeLeftInflated.getChild("LegBackLeftInflated");
		this.LegBackRightInflated = this.KneeRightInflated.getChild("LegBackRightInflated");

		this.endimator = Endimator.compile(root);
	}

	public static LayerDefinition createLayerDefinition() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition root = meshdefinition.getRoot();
		PartDefinition Head = root.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 29).addBox(-9.0F, -4.0F, -9.0F, 18.0F, 8.0F, 18.0F, false), PartPose.offsetAndRotation(0.0F, 9.0F, 0.0F, -0.091106184F, 0.0F, 0.0F));
		PartDefinition Jaw = Head.addOrReplaceChild("Jaw", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, 0.0F, -16.0F, 16.0F, 6.0F, 16.0F, false), PartPose.offsetAndRotation(0.0F, 4.0F, 8.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition KneeLeft = Head.addOrReplaceChild("KneeLeft", CubeListBuilder.create().texOffs(0, 58).addBox(-2.5F, -10.0F, -2.5F, 5.0F, 10.0F, 5.0F, false), PartPose.offsetAndRotation(5.5F, -2.0F, 5.5F, 0.0F, -0.17453292F, 0.7853982F));
		PartDefinition KneeRight = Head.addOrReplaceChild("KneeRight", CubeListBuilder.create().texOffs(0, 58).addBox(-2.5F, -10.0F, -2.5F, 5.0F, 10.0F, 5.0F, false), PartPose.offsetAndRotation(-5.5F, -2.0F, 5.5F, 0.0F, 0.17453292F, -0.7853982F));
		PartDefinition LegLeft = Head.addOrReplaceChild("LegLeft", CubeListBuilder.create().texOffs(28, 59).addBox(0.0F, -1.5F, -1.5F, 20.0F, 3.0F, 3.0F, false), PartPose.offsetAndRotation(8.5F, 2.0F, -3.0F, 0.0F, 0.5235988F, 1.0471976F));
		PartDefinition LegRight = Head.addOrReplaceChild("LegRight", CubeListBuilder.create().texOffs(28, 59).addBox(0.0F, -1.5F, -1.5F, 20.0F, 3.0F, 3.0F, false), PartPose.offsetAndRotation(-8.5F, 2.0F, -3.0F, 0.0F, 0.5235988F, 2.0943952F));
		PartDefinition LegBackLeft = KneeLeft.addOrReplaceChild("LegBackLeft", CubeListBuilder.create().texOffs(0, 81).addBox(0.0F, -1.5F, -1.5F, 25.0F, 3.0F, 3.0F, false), PartPose.offsetAndRotation(1.5F, -7.5F, -0.5F, 0.0F, -0.34906584F, 0.34906584F));
		PartDefinition LegBackRight = KneeRight.addOrReplaceChild("LegBackRight", CubeListBuilder.create().texOffs(0, 89).addBox(-25.0F, -1.5F, -1.5F, 25.0F, 3.0F, 3.0F, false), PartPose.offsetAndRotation(-1.5F, -7.5F, 0.0F, 0.0F, 0.34906584F, -0.34906584F));

		PartDefinition HeadInflated = root.addOrReplaceChild("HeadInflated", CubeListBuilder.create().texOffs(0, 29).addBox(-9.0F, -4.0F, -9.0F, 18.0F, 8.0F, 18.0F, false), PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 3.1003275E-17F, 0.0F, 0.0F));
		PartDefinition KneeLeftInflated = HeadInflated.addOrReplaceChild("KneeLeftInflated", CubeListBuilder.create().texOffs(0, 58).addBox(-2.5F, -10.0F, -2.5F, 5.0F, 10.0F, 5.0F, false), PartPose.offsetAndRotation(5.5F, -2.0F, 5.5F, 0.0F, -0.17453292F, 0.55850536F));
		PartDefinition KneeRightInflated = HeadInflated.addOrReplaceChild("KneeRightInflated", CubeListBuilder.create().texOffs(0, 58).addBox(-2.5F, -10.0F, -2.5F, 5.0F, 10.0F, 5.0F, false), PartPose.offsetAndRotation(-5.5F, -2.0F, 5.5F, 0.0F, 0.17453292F, -0.55850536F));
		PartDefinition LegLeftInflated = HeadInflated.addOrReplaceChild("LegLeftInflated", CubeListBuilder.create().texOffs(28, 59).addBox(0.0F, -1.5F, -1.5F, 20.0F, 3.0F, 3.0F, false), PartPose.offsetAndRotation(8.5F, 2.0F, -3.0F, 0.0F, 0.5235988F, -2.480262E-16F));
		PartDefinition LegRightInflated = HeadInflated.addOrReplaceChild("LegRightInflated", CubeListBuilder.create().texOffs(28, 66).addBox(0.0F, -1.5F, -1.5F, 20.0F, 3.0F, 3.0F, false), PartPose.offsetAndRotation(-8.5F, 2.0F, -3.0F, 0.0F, 0.5235988F, 3.1415927F));
		PartDefinition JawInflated = HeadInflated.addOrReplaceChild("JawInflated", CubeListBuilder.create().texOffs(0, 97).addBox(-16.0F, 0.0F, -24.0F, 32.0F, 16.0F, 32.0F, false), PartPose.offsetAndRotation(0.0F, 4.0F, 8.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition LegBackLeftInflated = KneeLeftInflated.addOrReplaceChild("LegBackLeftInflated", CubeListBuilder.create().texOffs(0, 81).addBox(0.0F, -1.5F, -1.5F, 25.0F, 3.0F, 3.0F, false), PartPose.offsetAndRotation(1.5F, -7.5F, -0.5F, 0.0F, -0.34906584F, 0.34906584F));
		PartDefinition LegBackRightInflated = KneeRightInflated.addOrReplaceChild("LegBackRightInflated", CubeListBuilder.create().texOffs(0, 89).addBox(-25.0F, -1.5F, -1.5F, 25.0F, 3.0F, 3.0F, false), PartPose.offsetAndRotation(-1.5F, -7.5F, 0.0F, 0.0F, 0.34906584F, -0.34906584F));
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