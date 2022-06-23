package com.minecraftabnormals.endergetic.client.renderers.entity;

import com.minecraftabnormals.endergetic.client.models.puffbug.PuffBugModel;
import com.minecraftabnormals.endergetic.client.renderers.entity.layer.LayerRendererPuffBugGlow;
import com.minecraftabnormals.endergetic.common.entities.puffbug.PuffBugEntity;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.resources.ResourceLocation;

public class PuffBugRenderer extends MobRenderer<PuffBugEntity, EntityModel<PuffBugEntity>> {
	private static final ResourceLocation DEFLATED = new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/puffbug/puffbug_deflated.png");
	private static final ResourceLocation INFLATED = new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/puffbug/puffbug_inflated.png");

	public PuffBugRenderer(EntityRenderDispatcher manager) {
		super(manager, new PuffBugModel<>(), 0.3F);
		this.addLayer(new LayerRendererPuffBugGlow<>(this));
	}

	@Override
	public void render(PuffBugEntity puffbug, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
		if (puffbug.isBaby()) {
			this.shadowRadius *= 0.5F;
		}
		super.render(puffbug, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Override
	public ResourceLocation getTextureLocation(PuffBugEntity puffbug) {
		return puffbug.isInflated() ? INFLATED : DEFLATED;
	}

	@Override
	protected void scale(PuffBugEntity puffbug, PoseStack matrixStack, float partialTickTime) {
		matrixStack.scale(1.0F, 1.0F, 1.0F);
		if (puffbug.isBaby()) {
			matrixStack.scale(0.5F, 0.5F, 0.5F);
		}
	}
}