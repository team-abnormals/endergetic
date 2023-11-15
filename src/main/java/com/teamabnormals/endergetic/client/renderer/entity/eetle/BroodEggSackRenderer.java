package com.teamabnormals.endergetic.client.renderer.entity.eetle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamabnormals.endergetic.common.entity.eetle.BroodEggSack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;

public class BroodEggSackRenderer extends EntityRenderer<BroodEggSack> {

	public BroodEggSackRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(BroodEggSack eggSackEntity, float p_225623_2_, float p_225623_3_, PoseStack stack, MultiBufferSource renderTypeBuffer, int p_225623_6_) {
	}

	@SuppressWarnings("deprecation")
	@Override
	public ResourceLocation getTextureLocation(BroodEggSack entity) {
		return TextureAtlas.LOCATION_BLOCKS;
	}

}
