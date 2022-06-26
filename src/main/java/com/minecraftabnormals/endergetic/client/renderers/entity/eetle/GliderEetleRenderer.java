package com.minecraftabnormals.endergetic.client.renderers.entity.eetle;

import com.minecraftabnormals.endergetic.client.models.eetle.GliderEetleModel;
import com.minecraftabnormals.endergetic.common.entities.eetle.GliderEetleEntity;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

public class GliderEetleRenderer extends AbstractEetleRenderer<GliderEetleEntity> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/eetle/glider_eetle.png");
	private static final ResourceLocation EMISSIVE_TEXTURE = new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/eetle/glider_eetle_emissive.png");

	public GliderEetleRenderer(EntityRendererProvider.Context context) {
		super(context, new GliderEetleModel(context.bakeLayer(GliderEetleModel.LOCATION)), EMISSIVE_TEXTURE, 0.75F);
	}

	@Nonnull
	@Override
	protected ResourceLocation getAdultTexture() {
		return TEXTURE;
	}
}
