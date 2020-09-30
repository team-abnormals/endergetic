package com.minecraftabnormals.endergetic.client.renderers.entity.booflo;

import com.minecraftabnormals.endergetic.client.models.booflo.AdolescentBoofloModel;
import com.minecraftabnormals.endergetic.client.renderers.entity.layer.EmissiveLayerRenderer;
import com.minecraftabnormals.endergetic.client.renderers.entity.layer.LayerRendererBoofloAdolescentFruit;
import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloAdolescentEntity;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class BoofloAdolescentRenderer extends MobRenderer<BoofloAdolescentEntity, AdolescentBoofloModel<BoofloAdolescentEntity>> {

	public BoofloAdolescentRenderer(EntityRendererManager manager) {
		super(manager, new AdolescentBoofloModel<>(), 0.5F);
		this.addLayer(new EmissiveLayerRenderer<>(this, new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/booflo/booflo_adolescent_emissive.png")));
		this.addLayer(new LayerRendererBoofloAdolescentFruit(this));
	}

	@Override
	public ResourceLocation getEntityTexture(BoofloAdolescentEntity entity) {
		return new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/booflo/booflo_adolescent.png");
	}
	
}