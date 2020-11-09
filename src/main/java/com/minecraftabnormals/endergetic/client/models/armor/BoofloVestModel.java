package com.minecraftabnormals.endergetic.client.models.armor;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;

/**
 * ModelBoofloVest - Endergized
 * Created using Tabula 7.0.0
 */
public class BoofloVestModel extends BipedModel<LivingEntity> {
	public static final BoofloVestModel INSTANCE = new BoofloVestModel(1.0F);
	public ModelRenderer strap;
	public ModelRenderer boofer;

	public BoofloVestModel(float modelSize) {
		super(modelSize, 0.0F, 64, 64);
		this.strap = new ModelRenderer(this, 16, 16);
		this.strap.setRotationPoint(-4.0F, 0.0F, -2.0F);
		this.strap.addBox(0.0F, 0.0F, 0.0F, 8, 11, 4, 0.0F);
		this.boofer = new ModelRenderer(this, 0, 32);
		this.boofer.setRotationPoint(0.0F, 2.0F, -2.0F);
		this.boofer.addBox(0.0F, 0.0F, 0.0F, 8, 8, 8, 0.0F);
		this.strap.addChild(this.boofer);
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder vertexBuilder, int light, int p_225598_4_, float p_225598_5_, float p_225598_6_, float p_225598_7_, float f5) {
		this.strap.copyModelAngles(this.bipedBody);

		matrixStack.push();
		matrixStack.scale(1.25F, 1.25F, 1.25F);
		matrixStack.translate(-0.25F, -0.05F, -0.125F);

		this.strap.render(matrixStack, vertexBuilder, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		matrixStack.pop();
	}
}