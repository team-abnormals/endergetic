package com.teamabnormals.endergetic.client.renderers.entity.eetle;

import com.teamabnormals.endergetic.client.models.eetle.ChargerEetleModel;
import com.teamabnormals.endergetic.common.entities.eetle.ChargerEetleEntity;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

public class ChargerEetleRenderer extends AbstractEetleRenderer<ChargerEetleEntity> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/eetle/charger_eetle.png");
	private static final ResourceLocation EMISSIVE_TEXTURE = new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/eetle/charger_eetle_emissive.png");

	public ChargerEetleRenderer(EntityRendererProvider.Context context) {
		super(context, new ChargerEetleModel(context.bakeLayer(ChargerEetleModel.LOCATION)), EMISSIVE_TEXTURE, 0.8F);
	}

	@Nonnull
	@Override
	protected ResourceLocation getAdultTexture() {
		return TEXTURE;
	}
}
