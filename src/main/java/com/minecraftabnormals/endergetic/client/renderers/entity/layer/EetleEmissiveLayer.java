package com.minecraftabnormals.endergetic.client.renderers.entity.layer;

import com.minecraftabnormals.abnormals_core.client.ACRenderTypes;
import com.minecraftabnormals.endergetic.common.entities.eetle.AbstractEetleEntity;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;

public class EetleEmissiveLayer<E extends AbstractEetleEntity, M extends EntityModel<E>> extends LayerRenderer<E, M> {
	private static final ResourceLocation LEETLE_TEXTURE = new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/eetle/leetle_emissive.png");
	private final ResourceLocation adultTexture;

	public EetleEmissiveLayer(IEntityRenderer<E, M> entityRendererIn, ResourceLocation adultTexture) {
		super(entityRendererIn);
		this.adultTexture = adultTexture;
	}

	@Override
	public void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLightIn, E entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		M model = this.getParentModel();
		model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		model.renderToBuffer(matrixStack, buffer.getBuffer(ACRenderTypes.getEmissiveEntity(entity.isBaby() ? LEETLE_TEXTURE : this.adultTexture)), 240, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
	}
}
