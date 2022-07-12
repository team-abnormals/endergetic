package com.teamabnormals.endergetic.client.renderers.entity.layer;

import com.teamabnormals.endergetic.common.entities.eetle.AbstractEetleEntity;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import com.mojang.blaze3d.vertex.PoseStack;
import com.teamabnormals.blueprint.client.BlueprintRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class EetleEmissiveLayer<E extends AbstractEetleEntity, M extends EntityModel<E>> extends RenderLayer<E, M> {
	private static final ResourceLocation LEETLE_TEXTURE = new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/eetle/leetle_emissive.png");
	private final ResourceLocation adultTexture;

	public EetleEmissiveLayer(RenderLayerParent<E, M> entityRendererIn, ResourceLocation adultTexture) {
		super(entityRendererIn);
		this.adultTexture = adultTexture;
	}

	@Override
	public void render(PoseStack matrixStack, MultiBufferSource buffer, int packedLightIn, E entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		M model = this.getParentModel();
		model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		model.renderToBuffer(matrixStack, buffer.getBuffer(BlueprintRenderTypes.getUnshadedCutoutEntity(entity.isBaby() ? LEETLE_TEXTURE : this.adultTexture, true)), 240, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
	}
}
