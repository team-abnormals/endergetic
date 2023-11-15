package com.teamabnormals.endergetic.client.model.booflo;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamabnormals.endergetic.common.entity.booflo.BoofloBaby;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import net.minecraft.client.model.EntityModel;
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
 * ModelBoofloBaby - Endergized
 * Created using Tabula 7.0.0
 */
public class BoofloBabyModel<E extends BoofloBaby> extends EntityModel<E> {
	public static final ModelLayerLocation LOCATION = new ModelLayerLocation(new ResourceLocation(EndergeticExpansion.MOD_ID, "booflo_baby"), "main");
	public ModelPart Head;
	public ModelPart Jaw;
	public ModelPart Tail;

	public BoofloBabyModel(ModelPart root) {
		this.Head = root.getChild("Head");
		this.Tail = this.Head.getChild("Tail");
		this.Jaw = this.Head.getChild("Jaw");
	}

	public static LayerDefinition createLayerDefinition() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition root = meshdefinition.getRoot();
		PartDefinition Head = root.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(15, 10).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 2.0F, 4.0F, false), PartPose.offsetAndRotation(0.0F, 21.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition Tail = Head.addOrReplaceChild("Tail", CubeListBuilder.create().texOffs(0, 2).addBox(0.0F, -2.0F, 0.0F, 0.0F, 4.0F, 8.0F, false), PartPose.offsetAndRotation(0.0F, 0.0F, 2.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition Jaw = Head.addOrReplaceChild("Jaw", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, 0.0F, -5.0F, 6.0F, 3.0F, 6.0F, false), PartPose.offsetAndRotation(0.0F, 0.0F, 2.0F, 0.0F, 0.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 32, 16);
	}

	@Override
	public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		this.Head.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}

	@Override
	public void setupAnim(E entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float tailAnimation = entityIn.getTailAnimation(ageInTicks - entityIn.tickCount);

		this.Head.yRot = netHeadYaw * (float) (Math.PI / 180F);

		this.Head.xRot = entityIn.isBeingBorn() ? (float) Math.PI : headPitch * (float) (Math.PI / 180F);

		if (entityIn.isMoving()) {
			this.Head.yRot += -1.1F * 0.2F * Mth.sin(0.55F * ageInTicks);
		}

		this.Tail.yRot = Mth.sin(tailAnimation) * (float) Math.PI * 0.09F;
	}
}