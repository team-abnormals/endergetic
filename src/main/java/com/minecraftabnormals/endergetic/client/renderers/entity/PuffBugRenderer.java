package com.minecraftabnormals.endergetic.client.renderers.entity;

import com.minecraftabnormals.endergetic.client.models.puffbug.PuffBugModel;
import com.minecraftabnormals.endergetic.client.renderers.entity.layer.LayerRendererPuffBugGlow;
import com.minecraftabnormals.endergetic.common.entities.puffbug.PuffBugEntity;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;

public class PuffBugRenderer extends MobRenderer<PuffBugEntity, EntityModel<PuffBugEntity>> {
	private static final ResourceLocation DEFLATED = new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/puffbug/puffbug_deflated.png");
	private static final ResourceLocation INFLATED = new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/puffbug/puffbug_inflated.png");

	public PuffBugRenderer(EntityRendererManager manager) {
		super(manager, new PuffBugModel<>(), 0.3F);
		this.addLayer(new LayerRendererPuffBugGlow<>(this));
	}

	@Override
	public void render(PuffBugEntity puffbug, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		if (puffbug.isChild()) {
			this.shadowSize *= 0.5F;
		}
		super.render(puffbug, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Override
	public ResourceLocation getEntityTexture(PuffBugEntity puffbug) {
		return puffbug.isInflated() ? INFLATED : DEFLATED;
	}

	@Override
	protected void preRenderCallback(PuffBugEntity puffbug, MatrixStack matrixStack, float partialTickTime) {
		matrixStack.scale(1.0F, 1.0F, 1.0F);
		if (puffbug.isChild()) {
			matrixStack.scale(0.5F, 0.5F, 0.5F);
		}
	}
}