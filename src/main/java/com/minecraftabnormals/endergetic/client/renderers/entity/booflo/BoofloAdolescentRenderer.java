package com.minecraftabnormals.endergetic.client.renderers.entity.booflo;

import com.minecraftabnormals.endergetic.client.models.booflo.AdolescentBoofloModel;
import com.minecraftabnormals.endergetic.client.renderers.entity.layer.EmissiveLayerRenderer;
import com.minecraftabnormals.endergetic.client.renderers.entity.layer.LayerRendererBoofloAdolescentFruit;
import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloAdolescentEntity;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class BoofloAdolescentRenderer extends MobRenderer<BoofloAdolescentEntity, AdolescentBoofloModel<BoofloAdolescentEntity>> {

	public BoofloAdolescentRenderer(EntityRendererProvider.Context context) {
		super(context, new AdolescentBoofloModel<>(context.bakeLayer(AdolescentBoofloModel.LOCATION)), 0.5F);
		this.addLayer(new EmissiveLayerRenderer<>(this, new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/booflo/booflo_adolescent_emissive.png")));
		this.addLayer(new LayerRendererBoofloAdolescentFruit(this, context.getItemInHandRenderer()));
	}

	@Override
	public ResourceLocation getTextureLocation(BoofloAdolescentEntity entity) {
		return new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/booflo/booflo_adolescent.png");
	}

}