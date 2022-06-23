package com.minecraftabnormals.endergetic.client.models.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;

/**
 * ModelBoofloVest - Endergized
 * Created using Tabula 7.0.0
 */
public class BoofloVestModel extends HumanoidModel<LivingEntity> {
	public static final BoofloVestModel INSTANCE = new BoofloVestModel(1.0F);
	public ModelPart strap;
	public ModelPart boofer;

	public BoofloVestModel(float modelSize) {
		super(modelSize, 0.0F, 64, 64);
		this.strap = new ModelPart(this, 16, 16);
		this.strap.setPos(-4.0F, 0.0F, -2.0F);
		this.strap.addBox(0.0F, 0.0F, 0.0F, 8, 11, 4, 0.0F);
		this.boofer = new ModelPart(this, 0, 32);
		this.boofer.setPos(0.0F, 2.0F, -2.0F);
		this.boofer.addBox(0.0F, 0.0F, 0.0F, 8, 8, 8, 0.0F);
		this.strap.addChild(this.boofer);
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