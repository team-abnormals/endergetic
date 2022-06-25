package com.minecraftabnormals.endergetic.client.models.bolloom;

import com.minecraftabnormals.endergetic.common.tileentities.BolloomBudTileEntity;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import com.teamabnormals.blueprint.client.ClientInfo;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

/**
 * ModelBolloomBudOpen - Endergized
 * Created using Tabula 7.0.0
 */
public class BolloomBudModel {
	public static final ModelLayerLocation LOCATION = new ModelLayerLocation(new ResourceLocation(EndergeticExpansion.MOD_ID, "bolloom_bud"), "main");
	public ModelPart Center;
	public ModelPart Pedal;
	public ModelPart Pedal_1;
	public ModelPart Pedal_2;
	public ModelPart Pedal_3;
	public ModelPart Pedal_open;
	public ModelPart Pedal_1_open;
	public ModelPart Pedal_2_open;
	public ModelPart Pedal_3_open;

	public BolloomBudModel(ModelPart root) {
		this.Center = root.getChild("Center");
		this.Pedal = root.getChild("Pedal");
		this.Pedal_1 = root.getChild("Pedal_1");
		this.Pedal_2 = root.getChild("Pedal_2");
		this.Pedal_3 = root.getChild("Pedal_3");
		this.Pedal_open = root.getChild("Pedal_open");
		this.Pedal_1_open = root.getChild("Pedal_1_open");
		this.Pedal_2_open = root.getChild("Pedal_2_open");
		this.Pedal_3_open = root.getChild("Pedal_3_open");
	}

	public static LayerDefinition createLayerDefinition() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition root = meshdefinition.getRoot();
		PartDefinition Center = root.addOrReplaceChild("Center", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 14.0F, 3.0F, 14.0F, false), PartPose.offsetAndRotation(-7.0F, 21.0F, -7.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition Pedal = root.addOrReplaceChild("Pedal", CubeListBuilder.create().texOffs(0, 18).addBox(0.0F, 0.0F, 0.0F, 12.0F, 2.0F, 12.0F, false), PartPose.offsetAndRotation(-6.0F, 21.0F, 7.0F, 1.7802359F, 0.0F, 0.0F));
		PartDefinition Pedal_1 = root.addOrReplaceChild("Pedal_1", CubeListBuilder.create().texOffs(0, 18).addBox(0.0F, 0.0F, 0.0F, 12.0F, 2.0F, 12.0F, false), PartPose.offsetAndRotation(7.0F, 21.0F, 6.0F, 1.7802359F, 1.5707964F, 0.0F));
		PartDefinition Pedal_2 = root.addOrReplaceChild("Pedal_2", CubeListBuilder.create().texOffs(0, 18).addBox(0.0F, 0.0F, 0.0F, 12.0F, 2.0F, 12.0F, false), PartPose.offsetAndRotation(6.0F, 21.0F, -7.0F, 1.7802359F, 3.1415927F, 0.0F));
		PartDefinition Pedal_3 = root.addOrReplaceChild("Pedal_3", CubeListBuilder.create().texOffs(0, 18).addBox(0.0F, 0.0F, 0.0F, 12.0F, 2.0F, 12.0F, false), PartPose.offsetAndRotation(-7.0F, 21.0F, -6.0F, 1.7802359F, -1.5707964F, 0.0F));
		PartDefinition Pedal_open = root.addOrReplaceChild("Pedal_open", CubeListBuilder.create().texOffs(0, 18).addBox(0.0F, 0.0F, 0.0F, 12.0F, 2.0F, 12.0F, false), PartPose.offsetAndRotation(-6.0F, 21.0F, 7.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition Pedal_1_open = root.addOrReplaceChild("Pedal_1_open", CubeListBuilder.create().texOffs(0, 18).addBox(0.0F, 0.0F, 0.0F, 12.0F, 2.0F, 12.0F, false), PartPose.offsetAndRotation(7.0F, 21.0F, 6.0F, 0.0F, 1.5707964F, 0.0F));
		PartDefinition Pedal_2_open = root.addOrReplaceChild("Pedal_2_open", CubeListBuilder.create().texOffs(0, 18).addBox(0.0F, 0.0F, 0.0F, 12.0F, 2.0F, 12.0F, false), PartPose.offsetAndRotation(6.0F, 21.0F, -7.0F, 0.0F, 3.1415927F, 0.0F));
		PartDefinition Pedal_3_open = root.addOrReplaceChild("Pedal_3_open", CubeListBuilder.create().texOffs(0, 18).addBox(0.0F, 0.0F, 0.0F, 12.0F, 2.0F, 12.0F, false), PartPose.offsetAndRotation(-7.0F, 21.0F, -6.0F, 0.0F, -1.5707964F, 0.0F));
		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	public void renderAll(BolloomBudTileEntity bud, PoseStack matrix, VertexConsumer builder, int light, int overlay) {
		float angle = bud.getLevel() != null ? 1.78F * bud.pedalAnimation.getProgress(ClientInfo.getPartialTicks()) : 1.78F;

		this.Pedal.xRot = angle;
		this.Pedal_1.xRot = angle;
		this.Pedal_2.xRot = angle;
		this.Pedal_3.xRot = angle;

		this.Pedal.render(matrix, builder, light, overlay);
		this.Pedal_1.render(matrix, builder, light, overlay);
		this.Pedal_2.render(matrix, builder, light, overlay);
		this.Pedal_3.render(matrix, builder, light, overlay);

		this.Pedal.xRot = 0.0F;
		this.Pedal_1.xRot = 0.0F;
		this.Pedal_2.xRot = 0.0F;
		this.Pedal_3.xRot = 0.0F;

		this.Center.render(matrix, builder, light, overlay);
	}

	public void setRotateAngle(ModelPart ModelRenderer, float x, float y, float z) {
		ModelRenderer.xRot = x;
		ModelRenderer.yRot = y;
		ModelRenderer.zRot = z;
	}
}