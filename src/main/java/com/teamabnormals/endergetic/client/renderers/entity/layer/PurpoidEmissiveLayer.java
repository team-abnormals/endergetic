package com.teamabnormals.endergetic.client.renderers.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamabnormals.blueprint.client.BlueprintRenderTypes;
import com.teamabnormals.endergetic.client.models.purpoid.PurpoidModel;
import com.teamabnormals.endergetic.common.entities.purpoid.PurpoidEntity;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class PurpoidEmissiveLayer extends RenderLayer<PurpoidEntity, PurpoidModel> {
	public static final ResourceLocation[] TEXTURES = new ResourceLocation[]{
			new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/purpoid/purpoid_emissive.png"),
			new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/purpoid/purp_emissive.png"),
			new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/purpoid/purpazoid_emissive.png"),
	};

	public PurpoidEmissiveLayer(RenderLayerParent<PurpoidEntity, PurpoidModel> entityRendererIn) {
		super(entityRendererIn);
	}

	@Override
	public void render(PoseStack matrixStack, MultiBufferSource buffer, int packedLight, PurpoidEntity purpoid, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		PurpoidModel model = this.getParentModel();
		model.setupAnim(purpoid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		VertexConsumer ivertexbuilder = buffer.getBuffer(BlueprintRenderTypes.getUnshadedTranslucentEntity(TEXTURES[purpoid.getSize().ordinal()], true));
		float alpha = 1.0F;
		int stunTimer = purpoid.getStunTimer();
		if (stunTimer > 0) {
			float stunTimerHalfCycles = stunTimer / 20.0F;
			float progress = 1.0F - 2.0F * Mth.abs(stunTimerHalfCycles - Mth.floor(stunTimerHalfCycles + 0.5F));
			alpha = (progress * progress);
		}
		model.renderToBuffer(matrixStack, ivertexbuilder, 240, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, alpha);
	}
}
