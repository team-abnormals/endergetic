package com.teamabnormals.endergetic.client.renderer.entity.eetle;

import com.teamabnormals.endergetic.client.model.eetle.GliderEetleModel;
import com.teamabnormals.endergetic.common.entity.eetle.GliderEetle;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

public class GliderEetleRenderer extends AbstractEetleRenderer<GliderEetle> {
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
