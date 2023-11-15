package com.teamabnormals.endergetic.client.model.bolloom;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamabnormals.blueprint.client.ClientInfo;
import com.teamabnormals.endergetic.common.entity.bolloom.BolloomFruit;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
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
 * ModelBolloomFruit - Endergized
 * Created using Tabula 7.0.0
 */
public class BolloomFruitModel<T extends BolloomFruit> extends EntityModel<T> {
	public static final ModelLayerLocation LOCATION = new ModelLayerLocation(new ResourceLocation(EndergeticExpansion.MOD_ID, "bolloom_fruit"), "main");
	public ModelPart vine_x;
	public ModelPart fruit;
	public ModelPart vine_z;
	public ModelPart vine_x_1;
	public ModelPart vine_z_1;
	public ModelPart vine_x_2;
	public ModelPart vine_z_2;
	public ModelPart vine_x_3;
	public ModelPart vine_z_3;
	public ModelPart vine_x_4;
	public ModelPart vine_z_4;
	public ModelPart vine_x_5;
	public ModelPart vine_z_5;
	public ModelPart vine_x_6;
	public ModelPart vine_z_6;
	public ModelPart flap;

	public BolloomFruitModel(ModelPart root) {
		this.vine_x = root.getChild("vine_x");
		this.vine_z = this.vine_x.getChild("vine_z");
		this.vine_x_1 = this.vine_z.getChild("vine_x_1");
		this.vine_z_1 = this.vine_x_1.getChild("vine_z_1");
		this.vine_x_2 = this.vine_z_1.getChild("vine_x_2");
		this.vine_z_2 = this.vine_x_2.getChild("vine_z_2");
		this.vine_x_3 = this.vine_z_2.getChild("vine_x_3");
		this.vine_z_3 = this.vine_x_3.getChild("vine_z_3");
		this.vine_x_4 = this.vine_z_3.getChild("vine_x_4");
		this.vine_z_4 = this.vine_x_4.getChild("vine_z_4");
		this.vine_x_5 = this.vine_z_4.getChild("vine_x_5");
		this.vine_z_5 = this.vine_x_5.getChild("vine_z_5");
		this.vine_x_6 = this.vine_z_5.getChild("vine_x_6");
		this.vine_z_6 = this.vine_x_6.getChild("vine_z_6");
		this.fruit = root.getChild("fruit");
		this.flap = this.fruit.getChild("flap");
	}

	public static LayerDefinition createLayerDefinition() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition root = meshdefinition.getRoot();
		PartDefinition vine_x = root.addOrReplaceChild("vine_x", CubeListBuilder.create().texOffs(13, 10).addBox(0.0F, 0.0F, -3.0F, 0.0F, 16.0F, 6.0F, false), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition vine_z = vine_x.addOrReplaceChild("vine_z", CubeListBuilder.create().texOffs(0, 10).addBox(0.0F, 0.0F, -3.0F, 0.0F, 16.0F, 6.0F, false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5707964F, 0.0F));
		PartDefinition vine_x_1 = vine_z.addOrReplaceChild("vine_x_1", CubeListBuilder.create().texOffs(13, 10).addBox(0.0F, 0.0F, -3.0F, 0.0F, 16.0F, 6.0F, false), PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition vine_z_1 = vine_x_1.addOrReplaceChild("vine_z_1", CubeListBuilder.create().texOffs(0, 10).addBox(0.0F, 0.0F, -3.0F, 0.0F, 16.0F, 6.0F, false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5707964F, 0.0F));
		PartDefinition vine_x_2 = vine_z_1.addOrReplaceChild("vine_x_2", CubeListBuilder.create().texOffs(13, 10).addBox(0.0F, 0.0F, -3.0F, 0.0F, 16.0F, 6.0F, false), PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition vine_z_2 = vine_x_2.addOrReplaceChild("vine_z_2", CubeListBuilder.create().texOffs(0, 10).addBox(0.0F, 0.0F, -3.0F, 0.0F, 16.0F, 6.0F, false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5707964F, 0.0F));
		PartDefinition vine_x_3 = vine_z_2.addOrReplaceChild("vine_x_3", CubeListBuilder.create().texOffs(13, 10).addBox(0.0F, 0.0F, -3.0F, 0.0F, 16.0F, 6.0F, false), PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition vine_z_3 = vine_x_3.addOrReplaceChild("vine_z_3", CubeListBuilder.create().texOffs(0, 10).addBox(0.0F, 0.0F, -3.0F, 0.0F, 16.0F, 6.0F, false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5707964F, 0.0F));
		PartDefinition vine_x_4 = vine_z_3.addOrReplaceChild("vine_x_4", CubeListBuilder.create().texOffs(13, 10).addBox(0.0F, 0.0F, -3.0F, 0.0F, 16.0F, 6.0F, false), PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition vine_z_4 = vine_x_4.addOrReplaceChild("vine_z_4", CubeListBuilder.create().texOffs(0, 10).addBox(0.0F, 0.0F, -3.0F, 0.0F, 16.0F, 6.0F, false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5707964F, 0.0F));
		PartDefinition vine_x_5 = vine_z_4.addOrReplaceChild("vine_x_5", CubeListBuilder.create().texOffs(13, 10).addBox(0.0F, 0.0F, -3.0F, 0.0F, 16.0F, 6.0F, false), PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition vine_z_5 = vine_x_5.addOrReplaceChild("vine_z_5", CubeListBuilder.create().texOffs(0, 10).addBox(0.0F, 0.0F, -3.0F, 0.0F, 16.0F, 6.0F, false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5707964F, 0.0F));
		PartDefinition vine_x_6 = vine_z_5.addOrReplaceChild("vine_x_6", CubeListBuilder.create().texOffs(13, 10).addBox(0.0F, 0.0F, -3.0F, 0.0F, 16.0F, 6.0F, false), PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition vine_z_6 = vine_x_6.addOrReplaceChild("vine_z_6", CubeListBuilder.create().texOffs(0, 10).addBox(0.0F, 0.0F, -3.0F, 0.0F, 16.0F, 6.0F, false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5707964F, 0.0F));
		PartDefinition fruit = root.addOrReplaceChild("fruit", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 8.0F, 8.0F, 8.0F, false), PartPose.offsetAndRotation(-4.0F, 16.0F, -4.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition flap = fruit.addOrReplaceChild("flap", CubeListBuilder.create().texOffs(20, 4).addBox(0.0F, 0.0F, 0.0F, 12.0F, 0.0F, 12.0F, false), PartPose.offsetAndRotation(-2.0F, 8.1F, -2.0F, 0.0F, 0.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		this.fruit.render(matrixStackIn, bufferIn, 240, packedOverlayIn, red, green, blue, alpha);
	}

	public void renderVine(PoseStack matrix, VertexConsumer vertexBuilder, int light) {
		this.vine_x.render(matrix, vertexBuilder, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public void setupAnim(T fruit, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float[] angles = fruit.getVineAnimation(ClientInfo.getPartialTicks());
		this.vine_x.xRot = angles[0];
		this.vine_x.yRot = angles[1];

		int height = fruit.getVineHeight();

		this.vine_x.visible = true;
		this.vine_z.visible = true;
		this.vine_x_1.visible = true;
		this.vine_z_1.visible = true;
		this.vine_x_2.visible = true;
		this.vine_z_2.visible = true;
		this.vine_x_3.visible = true;
		this.vine_z_3.visible = true;
		this.vine_x_4.visible = true;
		this.vine_z_4.visible = true;
		this.vine_x_5.visible = true;
		this.vine_z_5.visible = true;
		this.vine_x_6.visible = true;
		this.vine_z_6.visible = true;

		switch (height) {
			case 1:
				this.vine_x_1.visible = false;
				this.vine_z_1.visible = false;
				break;
			case 2:
				this.vine_x_2.visible = false;
				this.vine_z_2.visible = false;
				break;
			case 3:
				this.vine_x_3.visible = false;
				this.vine_z_3.visible = false;
				break;
			case 4:
				this.vine_x_4.visible = false;
				this.vine_z_4.visible = false;
				break;
			case 5:
				this.vine_x_5.visible = false;
				this.vine_z_5.visible = false;
				break;
			case 6:
				this.vine_x_6.visible = false;
				this.vine_z_6.visible = false;
				break;
		}
	}

	public void setRotateAngle(ModelPart ModelRenderer, float x, float y, float z) {
		ModelRenderer.xRot = x;
		ModelRenderer.yRot = y;
		ModelRenderer.zRot = z;
	}
}
