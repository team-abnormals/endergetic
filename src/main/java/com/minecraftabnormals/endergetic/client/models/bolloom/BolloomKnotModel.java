package com.minecraftabnormals.endergetic.client.models.bolloom;

import com.minecraftabnormals.endergetic.common.entities.bolloom.BolloomKnotEntity;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

/**
 * ModelBolloomKnot - Endergized
 * Created using Tabula 7.0.0
 */
public class BolloomKnotModel<T extends BolloomKnotEntity> extends EntityModel<T> {
	public static final ModelLayerLocation LOCATION = new ModelLayerLocation(new ResourceLocation(EndergeticExpansion.MOD_ID, "bolloom_knot"), "main");
	public ModelPart knot;

	public BolloomKnotModel(ModelPart root) {
		this.knot = root.getChild("knot");
	}

	public static LayerDefinition createLayerDefinition() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition root = meshdefinition.getRoot();
		PartDefinition knot = root.addOrReplaceChild("knot", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 6.0F, 3.0F, 6.0F, false), PartPose.offsetAndRotation(-3.0F, 21.0F, -3.0F, 0.0F, 0.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 32, 16);
	}

	@Override
	public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		this.knot.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}

	@Override
	public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}

	public void setRotateAngle(ModelPart RendererModel, float x, float y, float z) {
		RendererModel.xRot = x;
		RendererModel.yRot = y;
		RendererModel.zRot = z;
	}
}