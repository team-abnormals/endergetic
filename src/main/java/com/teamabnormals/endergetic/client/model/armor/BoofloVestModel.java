package com.teamabnormals.endergetic.client.model.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;

/**
 * ModelBoofloVest - Endergized
 * Created using Tabula 7.0.0
 */
public class BoofloVestModel extends HumanoidModel<LivingEntity> {
	public static final BoofloVestModel INSTANCE = new BoofloVestModel(createLayerDefinition().bakeRoot());
	public ModelPart strap;
	public ModelPart boofer;

	public BoofloVestModel(ModelPart root) {
		super(root);
		this.strap = root.getChild("strap");
		this.boofer = this.strap.getChild("boofer");
	}

	public static LayerDefinition createLayerDefinition() {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition root = meshdefinition.getRoot();
		PartDefinition strap = root.addOrReplaceChild("strap", CubeListBuilder.create().texOffs(16, 16).addBox(0.0F, 0.0F, 0.0F, 8.0F, 11.0F, 4.0F, false), PartPose.offsetAndRotation(-4.0F, 0.0F, -2.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition boofer = strap.addOrReplaceChild("boofer", CubeListBuilder.create().texOffs(0, 32).addBox(0.0F, 0.0F, 0.0F, 8.0F, 8.0F, 8.0F, false), PartPose.offsetAndRotation(0.0F, 2.0F, -2.0F, 0.0F, 0.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void renderToBuffer(PoseStack matrixStack, VertexConsumer vertexBuilder, int light, int p_225598_4_, float p_225598_5_, float p_225598_6_, float p_225598_7_, float f5) {
		this.strap.copyFrom(this.body);

		matrixStack.pushPose();
		matrixStack.scale(1.25F, 1.25F, 1.25F);
		matrixStack.translate(-0.25F, -0.05F, -0.125F);

		this.strap.render(matrixStack, vertexBuilder, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		matrixStack.popPose();
	}
}