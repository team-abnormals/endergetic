package com.minecraftabnormals.endergetic.client.renderers.entity.eetle;

import com.minecraftabnormals.endergetic.client.models.eetle.BroodEetleModel;
import com.minecraftabnormals.endergetic.client.renderers.entity.layer.EmissiveLayerRenderer;
import com.minecraftabnormals.endergetic.common.entities.eetle.BroodEetleEntity;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class BroodEetleRenderer extends MobRenderer<BroodEetleEntity, BroodEetleModel> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/eetle/brood_eetle.png");
	private static final ResourceLocation EMISSIVE_TEXTURE = new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/eetle/brood_eetle_emissive.png");

	public BroodEetleRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, new BroodEetleModel(), 1.0F);
		this.addLayer(new EmissiveLayerRenderer<>(this, EMISSIVE_TEXTURE));
	}

	@Override
	public ResourceLocation getEntityTexture(BroodEetleEntity entity) {
		return TEXTURE;
	}
}
