package com.minecraftabnormals.endergetic.client.renderers.entity.booflo;

import com.minecraftabnormals.endergetic.client.models.booflo.AdolescentBoofloModel;
import com.minecraftabnormals.endergetic.client.renderers.entity.layer.EmissiveLayerRenderer;
import com.minecraftabnormals.endergetic.client.renderers.entity.layer.LayerRendererBoofloAdolescentFruit;
import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloAdolescentEntity;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;

import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class BoofloAdolescentRenderer extends MobRenderer<BoofloAdolescentEntity, AdolescentBoofloModel<BoofloAdolescentEntity>> {

	public BoofloAdolescentRenderer(EntityRenderDispatcher manager) {
		super(manager, new AdolescentBoofloModel<>(), 0.5F);
		this.addLayer(new EmissiveLayerRenderer<>(this, new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/booflo/booflo_adolescent_emissive.png")));
		this.addLayer(new LayerRendererBoofloAdolescentFruit(this));
	}

	@Override
	public ResourceLocation getTextureLocation(BoofloAdolescentEntity entity) {
		return new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/booflo/booflo_adolescent.png");
	}

}