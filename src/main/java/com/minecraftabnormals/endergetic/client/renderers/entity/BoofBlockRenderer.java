package com.minecraftabnormals.endergetic.client.renderers.entity;

import com.minecraftabnormals.endergetic.client.models.BoofBlockModel;
import com.minecraftabnormals.endergetic.common.entities.BoofBlockEntity;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import com.mojang.blaze3d.matrix.MatrixStack;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;

public class BoofBlockRenderer extends EntityRenderer<BoofBlockEntity> {
	private final BoofBlockModel<?> model = new BoofBlockModel<>();

	public BoofBlockRenderer(EntityRendererManager renderManager) {
		super(renderManager);
		this.shadowSize = 0.0F;
	}

	@Override
	public void render(BoofBlockEntity boof, float entityYaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer bufferIn, int packedLightIn) {
		stack.push();

		IVertexBuilder ivertexbuilder = bufferIn.getBuffer(this.model.getRenderType(this.getEntityTexture(boof)));
		this.model.render(stack, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

		stack.pop();
	}

	@Override
	public ResourceLocation getEntityTexture(BoofBlockEntity boof) {
		return new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/boof_block_inflated.png");
	}
}