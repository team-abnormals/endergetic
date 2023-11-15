package com.teamabnormals.endergetic.client.model.booflo;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamabnormals.blueprint.client.ClientInfo;
import com.teamabnormals.blueprint.core.endimator.Endimator;
import com.teamabnormals.blueprint.core.endimator.entity.EndimatorEntityModel;
import com.teamabnormals.endergetic.common.entity.booflo.BoofloAdolescent;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import com.teamabnormals.endergetic.core.registry.other.EEPlayableEndimations;
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
 * ModelAdolescentBooflo - Endergized
 * Created using Tabula 7.0.0
 */
public class AdolescentBoofloModel<E extends BoofloAdolescent> extends EndimatorEntityModel<E> {
	public static final ModelLayerLocation LOCATION = new ModelLayerLocation(new ResourceLocation(EndergeticExpansion.MOD_ID, "adolescent_booflo"), "main");
	public ModelPart Head;
	public ModelPart KneeLeft;
	public ModelPart KneeRight;
	public ModelPart ArmLeft;
	public ModelPart ArmRight;
	public ModelPart Tail;
	public ModelPart Jaw;

	public AdolescentBoofloModel(ModelPart root) {
		this.Head = root.getChild("Head");
		this.KneeLeft = this.Head.getChild("KneeLeft");
		this.KneeRight = this.Head.getChild("KneeRight");
		this.ArmLeft = this.Head.getChild("ArmLeft");
		this.ArmRight = this.Head.getChild("ArmRight");
		this.Tail = this.Head.getChild("Tail");
		this.Jaw = root.getChild("Jaw");

		this.endimator = Endimator.compile(root);
	}

	public static LayerDefinition createLayerDefinition() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition root = meshdefinition.getRoot();
		PartDefinition Head = root.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -5.0F, -10.0F, 10.0F, 5.0F, 10.0F, false), PartPose.offsetAndRotation(0.0F, 18.0F, 5.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition KneeLeft = Head.addOrReplaceChild("KneeLeft", CubeListBuilder.create().texOffs(0, 24).addBox(-1.5F, -5.0F, -1.5F, 3.0F, 5.0F, 3.0F, false), PartPose.offsetAndRotation(2.5F, -4.5F, -3.5F, 0.0F, 0.0F, 0.34906584F));
		PartDefinition KneeRight = Head.addOrReplaceChild("KneeRight", CubeListBuilder.create().texOffs(14, 24).addBox(-1.5F, -5.0F, -1.5F, 3.0F, 5.0F, 3.0F, false), PartPose.offsetAndRotation(-2.5F, -4.5F, -3.5F, 0.0F, 0.0F, -0.34906584F));
		PartDefinition ArmLeft = Head.addOrReplaceChild("ArmLeft", CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, -1.0F, -1.0F, 4.0F, 2.0F, 2.0F, false), PartPose.offsetAndRotation(4.5F, -2.0F, -7.0F, 0.0F, 0.55850536F, 0.34906584F));
		PartDefinition ArmRight = Head.addOrReplaceChild("ArmRight", CubeListBuilder.create().texOffs(14, 16).addBox(-4.0F, -1.0F, -1.0F, 4.0F, 2.0F, 2.0F, false), PartPose.offsetAndRotation(-4.5F, -2.0F, -7.0F, 0.0F, -0.55850536F, -0.34906584F));
		PartDefinition Tail = Head.addOrReplaceChild("Tail", CubeListBuilder.create().texOffs(32, 16).addBox(0.0F, 0.0F, 0.0F, 0.0F, 5.0F, 7.0F, false), PartPose.offsetAndRotation(0.0F, -5.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition Jaw = root.addOrReplaceChild("Jaw", CubeListBuilder.create().texOffs(16, 28).addBox(-6.0F, 0.0F, -11.0F, 12.0F, 6.0F, 12.0F, false), PartPose.offsetAndRotation(0.0F, 18.0F, 5.0F, 0.0F, 0.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 64, 48);
	}

	@Override
	public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		this.Head.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		this.Jaw.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}

	@Override
	public void setupAnim(E entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

		float partialTicks = ClientInfo.getPartialTicks();
		this.Tail.yRot = 0.1F * Mth.sin(entityIn.getTailAnimation(0.3F * partialTicks)) * (float) Math.PI;

		if (!entityIn.isEndimationPlaying(EEPlayableEndimations.ADOLESCENT_BOOFLO_BOOF)) {
			if (entityIn.isInWater()) {
				this.Head.y += 0.5F * Mth.sin(0.4F * ageInTicks);
				this.Jaw.y += 0.5F * Mth.sin(0.4F * ageInTicks);
				this.KneeLeft.zRot += 0.1F * -Mth.sin(0.6F * ageInTicks);
				this.KneeRight.zRot += 0.1F * Mth.sin(0.6F * ageInTicks);
				this.ArmLeft.zRot += 0.3F * -Mth.sin(0.6F * ageInTicks) - 0.17F;
				this.ArmRight.zRot += 0.3F * Mth.sin(0.6F * ageInTicks) + 0.17F;
			} else {
				if (!entityIn.isEating()) {
					if (entityIn.getVehicle() == null) {
						this.Head.y += 0.5F * Mth.sin(0.4F * ageInTicks);
						this.Jaw.y += 0.5F * Mth.sin(0.4F * ageInTicks);
					}
					this.KneeLeft.zRot += 0.1F * -Mth.sin(0.6F * entityIn.getSwimmingAnimation(partialTicks));
					this.KneeRight.zRot += 0.1F * Mth.sin(0.6F * entityIn.getSwimmingAnimation(partialTicks));
					this.ArmLeft.zRot += 0.3F * -Mth.sin(0.6F * entityIn.getSwimmingAnimation(partialTicks)) - 0.17F;
					this.ArmRight.zRot += 0.3F * Mth.sin(0.6F * entityIn.getSwimmingAnimation(partialTicks)) + 0.17F;
				}
			}
		}

		if (entityIn.isAggressive()) {
			this.Jaw.xRot += 0.2F * Mth.sin(0.3F * ageInTicks) + 0.4F;
		}
	}
}