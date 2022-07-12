package com.teamabnormals.endergetic.client.models.eetle;

import com.teamabnormals.endergetic.common.entities.eetle.AbstractEetleEntity;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamabnormals.blueprint.core.endimator.Endimator;
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
public class LeetleModel<E extends AbstractEetleEntity> extends EndimatorEntityModel<E> {
	public static final ModelLayerLocation LOCATION = new ModelLayerLocation(new ResourceLocation(EndergeticExpansion.MOD_ID, "leetle"), "main");
	public ModelPart body;
	public ModelPart head;
	public ModelPart body2;
	public ModelPart booty;
	public ModelPart leftLeg1;
	public ModelPart rightLeg1;
	public ModelPart leftLeg2;
	public ModelPart rightLeg2;

	public LeetleModel(ModelPart root) {
		this.body = root.getChild("body");
		this.head = this.body.getChild("head");
		this.body2 = this.body.getChild("body2");
		this.booty = this.body2.getChild("booty");
		this.leftLeg1 = this.body.getChild("leftLeg1");
		this.rightLeg1 = this.body.getChild("rightLeg1");
		this.leftLeg2 = this.body2.getChild("leftLeg2");
		this.rightLeg2 = this.body2.getChild("rightLeg2");

		this.endimator = Endimator.compile(root);
	}
	
	public static LayerDefinition createLayerDefinition() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition root = meshdefinition.getRoot();
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(19, 0).addBox(-3.5F, -3.0F, 0.0F, 7.0F, 6.0F, 5.0F, false), PartPose.offsetAndRotation(0.0F, 20.0F, -3.5F, 0.0F, 0.0F, 0.0F));
		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -2.0F, -4.0F, 5.0F, 4.0F, 4.0F, false), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition body2 = body.addOrReplaceChild("body2", CubeListBuilder.create().texOffs(0, 10).addBox(-3.0F, -2.5F, 0.0F, 6.0F, 5.0F, 4.0F, false), PartPose.offsetAndRotation(0.0F, 0.5F, 5.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition booty = body2.addOrReplaceChild("booty", CubeListBuilder.create().texOffs(22, 12).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 2.0F, false), PartPose.offsetAndRotation(0.0F, 0.5F, 4.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition leftLeg1 = body.addOrReplaceChild("leftLeg1", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, -0.5F, 0.0F, 2.0F, 1.0F, false), PartPose.offsetAndRotation(3.5F, 2.5F, 2.5F, 0.0F, 0.0F, -0.7853982F));
		PartDefinition rightLeg1 = body.addOrReplaceChild("rightLeg1", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, -0.5F, 0.0F, 2.0F, 1.0F, false), PartPose.offsetAndRotation(-3.5F, 2.5F, 2.5F, 0.0F, 0.0F, 0.7853982F));
		PartDefinition leftLeg2 = body2.addOrReplaceChild("leftLeg2", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, -0.5F, 0.0F, 2.0F, 1.0F, false), PartPose.offsetAndRotation(3.0F, 2.0F, 2.5F, 0.0F, 0.0F, -0.7853982F));
		PartDefinition rightLeg2 = body2.addOrReplaceChild("rightLeg2", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, -0.5F, 0.0F, 2.0F, 1.0F, false), PartPose.offsetAndRotation(-3.0F, 2.0F, 2.5F, 0.0F, 0.0F, 0.7853982F));
		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	public void setupAnim(E entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		this.head.yRot = netHeadYaw * ((float) Math.PI / 180.0F);
		this.head.xRot = headPitch * ((float) Math.PI / 180.0F);

		//Child mobs triple their limb swing
		limbSwing /= 3.0F;

		this.leftLeg1.xRot = this.rightLeg1.xRot = this.leftLeg2.xRot = this.rightLeg2.xRot = 0.0F;
		float frontLegAnglesX = Mth.sin(limbSwing * 1.5F + ((float) Math.PI * 1.5F)) * limbSwingAmount;
		this.leftLeg1.xRot = frontLegAnglesX;
		this.rightLeg1.xRot = frontLegAnglesX;

		float frontLegAnglesZ = Math.abs(Mth.sin(limbSwing * 0.75F + (float) Math.PI) * 1.5F) * limbSwingAmount;
		this.leftLeg1.zRot = -0.785F - frontLegAnglesZ;
		this.rightLeg1.zRot = 0.785F + frontLegAnglesZ;

		float backLegAnglesX = Mth.sin(limbSwing * 1.5F - ((float) Math.PI * 1.5F)) * limbSwingAmount;
		this.leftLeg2.xRot = backLegAnglesX;
		this.rightLeg2.xRot = backLegAnglesX;

		float backLegAnglesZ = Math.abs(Mth.sin(limbSwing * 0.75F + (float) Math.PI) * 1.5F) * limbSwingAmount;
		this.leftLeg2.zRot = -0.785F - backLegAnglesZ;
		this.rightLeg2.zRot = 0.785F + backLegAnglesZ;
	}

	@Override
	public void renderToBuffer(PoseStack matrixStack, VertexConsumer vertexBuilder, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.body.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
