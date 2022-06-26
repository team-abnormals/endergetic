package com.minecraftabnormals.endergetic.client.renderers.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import com.teamabnormals.blueprint.client.BlueprintRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;

public class EmissiveLayerRenderer<E extends Entity, M extends EntityModel<E>> extends RenderLayer<E, M> {
	private final ResourceLocation texture;

	public EmissiveLayerRenderer(RenderLayerParent<E, M> entityRendererIn, ResourceLocation texture) {
		super(entityRendererIn);
		this.texture = texture;
	}

	@Override
	public void render(PoseStack matrixStack, MultiBufferSource buffer, int packedLight, E entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		M model = this.getParentModel();
		model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		VertexConsumer ivertexbuilder = buffer.getBuffer(BlueprintRenderTypes.getUnshadedCutoutEntity(this.texture, true));
		model.renderToBuffer(matrixStack, ivertexbuilder, 240, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
	}
}