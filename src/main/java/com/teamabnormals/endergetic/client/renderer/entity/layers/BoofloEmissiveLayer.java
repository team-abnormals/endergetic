package com.teamabnormals.endergetic.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamabnormals.blueprint.client.BlueprintRenderTypes;
import com.teamabnormals.blueprint.client.EntitySkinHelper;
import com.teamabnormals.endergetic.common.entity.booflo.Booflo;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public final class BoofloEmissiveLayer<B extends Booflo, M extends EntityModel<B>> extends RenderLayer<B, M> {
	private static final ResourceLocation DEFAULT = new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/booflo/booflo_emissive.png");
	private static final EntitySkinHelper<Booflo> SKIN_HELPER = EntitySkinHelper.create(EndergeticExpansion.MOD_ID, "textures/entity/booflo/", "booflo_emissive", (skinHelper) -> {
		skinHelper.putSkins("snake", "snake", "snakeblock", "theforsakenone");
		skinHelper.putSkins("cam", "cameron", "cam");
	});

	public BoofloEmissiveLayer(RenderLayerParent<B, M> entityRenderer) {
		super(entityRenderer);
	}

	@Override
	public void render(PoseStack matrixStack, MultiBufferSource buffer, int packedLight, B entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		VertexConsumer ivertexbuilder = buffer.getBuffer(BlueprintRenderTypes.getUnshadedCutoutEntity(this.getEmissiveTexture(entity), true));
		M model = this.getParentModel();
		model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		model.renderToBuffer(matrixStack, ivertexbuilder, 240, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
	}

	private ResourceLocation getEmissiveTexture(B booflo) {
		return SKIN_HELPER.getSkinForEntityOrElse(booflo, DEFAULT);
	}
}
