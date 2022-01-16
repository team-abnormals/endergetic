package com.minecraftabnormals.endergetic.client;

import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public final class EERenderTypes extends RenderState {
	public static final RenderType BOLLOOM_STRING = createPlantEntity(new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/balloon/bolloom_balloon.png"));
	public static final RenderType BOLLOOM_VINE = createPlantEntity(new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/bolloom_fruit.png"));

	private EERenderTypes(String nameIn, Runnable setupTaskIn, Runnable clearTaskIn) {
		super(nameIn, setupTaskIn, clearTaskIn);
	}

	private static RenderType createPlantEntity(ResourceLocation texture) {
		RenderType.State state = RenderType.State.builder().setTextureState(new RenderState.TextureState(texture, false, false)).setTransparencyState(NO_TRANSPARENCY).setDiffuseLightingState(NO_DIFFUSE_LIGHTING).setAlphaState(DEFAULT_ALPHA).setCullState(NO_CULL).setLightmapState(LIGHTMAP).setOverlayState(OVERLAY).createCompositeState(false);
		return RenderType.create("endergetic:plant_entity", DefaultVertexFormats.NEW_ENTITY, 7, 256, true, false, state);
	}
}
