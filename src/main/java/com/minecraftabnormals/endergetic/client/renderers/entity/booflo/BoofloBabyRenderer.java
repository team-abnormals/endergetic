package com.minecraftabnormals.endergetic.client.renderers.entity.booflo;

import com.minecraftabnormals.endergetic.client.models.booflo.BoofloBabyModel;
import com.minecraftabnormals.endergetic.client.renderers.entity.layer.EmissiveLayerRenderer;
import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloBabyEntity;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class BoofloBabyRenderer extends MobRenderer<BoofloBabyEntity, BoofloBabyModel<BoofloBabyEntity>> {

	public BoofloBabyRenderer(EntityRendererProvider.Context context) {
		super(context, new BoofloBabyModel<>(context.bakeLayer(BoofloBabyModel.LOCATION)), 0.3F);
		this.addLayer(new EmissiveLayerRenderer<>(this, new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/booflo/booflo_baby_emissive.png")));
	}

	@Override
	public ResourceLocation getTextureLocation(BoofloBabyEntity entity) {
		return new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/booflo/booflo_baby.png");
	}

}