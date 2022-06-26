package com.minecraftabnormals.endergetic.client.models.bolloom;

import com.minecraftabnormals.endergetic.common.entities.bolloom.BolloomBalloonEntity;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import com.teamabnormals.blueprint.client.ClientInfo;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

/**
 * ModelBolloomBalloon - Endergized
 * Created using Tabula 7.0.0
 */
public class BolloomBalloonModel<T extends BolloomBalloonEntity> extends EntityModel<T> {
	public static final ModelLayerLocation LOCATION = new ModelLayerLocation(new ResourceLocation(EndergeticExpansion.MOD_ID, "bolloom_balloon"), "main");
	public ModelPart balloon;
	public ModelPart x_string;
	public ModelPart z_string;
	public ModelPart x_string_2;
	public ModelPart z_string_2;

	public BolloomBalloonModel(ModelPart root) {
		this.balloon = root.getChild("balloon");
		this.x_string = root.getChild("x_string");
		this.z_string = this.x_string.getChild("z_string");
		this.x_string_2 = this.z_string.getChild("x_string_2");
		this.z_string_2 = this.x_string_2.getChild("z_string_2");
	}

	//Layer Definition
	public static LayerDefinition createLayerDefinition() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition root = meshdefinition.getRoot();
		PartDefinition balloon = root.addOrReplaceChild("balloon", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 8.0F, 8.0F, 8.0F, false), PartPose.offsetAndRotation(-4.0F, 16.0F, -4.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition x_string = root.addOrReplaceChild("x_string", CubeListBuilder.create().texOffs(0, 10).addBox(0.0F, 0.0F, -3.0F, 0.0F, 16.0F, 6.0F, false), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition z_string = x_string.addOrReplaceChild("z_string", CubeListBuilder.create().texOffs(0, 10).addBox(0.0F, 0.0F, 0.0F, 0.0F, 16.0F, 6.0F, false), PartPose.offsetAndRotation(3.0F, 0.0F, 0.0F, 0.0F, -1.5707964F, 0.0F));
		PartDefinition x_string_2 = z_string.addOrReplaceChild("x_string_2", CubeListBuilder.create().texOffs(13, 10).addBox(0.0F, 0.0F, -3.0F, 0.0F, 16.0F, 6.0F, false), PartPose.offsetAndRotation(0.0F, 16.0F, 3.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition z_string_2 = x_string_2.addOrReplaceChild("z_string_2", CubeListBuilder.create().texOffs(13, 10).addBox(0.0F, 0.0F, 0.0F, 0.0F, 16.0F, 6.0F, false), PartPose.offsetAndRotation(3.0F, 0.0F, 0.0F, 0.0F, -1.5707964F, 0.0F));
		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void renderToBuffer(PoseStack matrix, VertexConsumer arg1, int f, int f1, float f2, float f3, float f4, float f5) {
		this.balloon.render(matrix, arg1, 240, f1, f5, f5, f5, f5);
	}

	public void renderString(PoseStack matrix, VertexConsumer vertexBuilder, int light) {
		this.x_string.render(matrix, vertexBuilder, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public void setupAnim(T balloon, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float[] angles = balloon.getVineAnimation(ClientInfo.getPartialTicks());
		this.x_string.xRot = angles[0];
		this.x_string.yRot = angles[1];
	}

	/**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	public void setRotateAngle(ModelPart ModelRenderer, float x, float y, float z) {
		ModelRenderer.xRot = x;
		ModelRenderer.yRot = y;
		ModelRenderer.zRot = z;
	}
}