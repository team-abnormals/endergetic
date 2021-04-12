package com.minecraftabnormals.endergetic.client.renderers.entity.eetle;

import com.minecraftabnormals.endergetic.common.entities.eetle.BroodEggSackEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;

public class BroodEggSackRenderer extends EntityRenderer<BroodEggSackEntity> {

	public BroodEggSackRenderer(EntityRendererManager entityRendererManager) {
		super(entityRendererManager);
	}

	@Override
	public void render(BroodEggSackEntity eggSackEntity, float p_225623_2_, float p_225623_3_, MatrixStack stack, IRenderTypeBuffer renderTypeBuffer, int p_225623_6_) {
	}

	@SuppressWarnings("deprecation")
	@Override
	public ResourceLocation getEntityTexture(BroodEggSackEntity entity) {
		return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
	}

}
