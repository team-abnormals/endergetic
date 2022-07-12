package com.teamabnormals.endergetic.client.renderers.entity.booflo;

import com.teamabnormals.endergetic.client.models.booflo.AdolescentBoofloModel;
import com.teamabnormals.endergetic.client.renderers.entity.layer.EmissiveLayerRenderer;
import com.teamabnormals.endergetic.client.renderers.entity.layer.LayerRendererBoofloAdolescentFruit;
import com.teamabnormals.endergetic.common.entities.booflo.BoofloAdolescentEntity;
import com.teamabnormals.endergetic.core.EndergeticExpansion;

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