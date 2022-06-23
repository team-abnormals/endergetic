package com.minecraftabnormals.endergetic.client.renderers.entity.eetle;

import com.minecraftabnormals.endergetic.common.entities.eetle.BroodEggSackEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;

public class BroodEggSackRenderer extends EntityRenderer<BroodEggSackEntity> {

	public BroodEggSackRenderer(EntityRenderDispatcher entityRendererManager) {
		super(entityRendererManager);
	}

	@Override
	public void render(BroodEggSackEntity eggSackEntity, float p_225623_2_, float p_225623_3_, PoseStack stack, MultiBufferSource renderTypeBuffer, int p_225623_6_) {
	}

	@SuppressWarnings("deprecation")
	@Override
	public ResourceLocation getTextureLocation(BroodEggSackEntity entity) {
		return TextureAtlas.LOCATION_BLOCKS;
	}

}
