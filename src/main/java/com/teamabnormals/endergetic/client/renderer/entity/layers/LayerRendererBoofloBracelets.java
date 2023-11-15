package com.teamabnormals.endergetic.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamabnormals.blueprint.client.BlueprintRenderTypes;
import com.teamabnormals.endergetic.common.entity.booflo.Booflo;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.stream.Stream;

@OnlyIn(Dist.CLIENT)
public class LayerRendererBoofloBracelets<E extends Booflo, M extends EntityModel<E>> extends RenderLayer<E, M> {
	private static final RenderType[] BOOFLO_BRACELETS = Stream.of(DyeColor.values()).map(dyeColor -> {
		return BlueprintRenderTypes.getUnshadedCutoutEntity(new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/booflo/bracelets/booflo_ring_" + dyeColor.getName() + ".png"), true);
	}).toArray(RenderType[]::new);

	public LayerRendererBoofloBracelets(RenderLayerParent<E, M> entityRenderer) {
		super(entityRenderer);
	}

	@Override
	public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, E booflo, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (!booflo.isTamed()) return;
		M model = this.getParentModel();
		model.setupAnim(booflo, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		model.renderToBuffer(matrixStackIn, bufferIn.getBuffer(BOOFLO_BRACELETS[booflo.getBraceletsColor().ordinal()]), 240, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
	}
}