package com.minecraftabnormals.endergetic.client;

import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.resources.ResourceLocation;

public final class EERenderTypes extends RenderStateShard {
	public static final RenderType BOLLOOM_STRING = createUnshadedEntity(new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/balloon/bolloom_balloon.png"));
	public static final RenderType BOLLOOM_VINE = createUnshadedEntity(new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/bolloom_fruit.png"));
	public static final RenderType EETLE_EGG_SILK = createUnshadedEntity(new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/tile/eggs/eetle_egg_silk.png"));

	private EERenderTypes(String nameIn, Runnable setupTaskIn, Runnable clearTaskIn) {
		super(nameIn, setupTaskIn, clearTaskIn);
	}

	public static RenderType createUnshadedEntity(ResourceLocation texture) {
		RenderType.CompositeState state = RenderType.CompositeState.builder().setTextureState(new RenderStateShard.TextureStateShard(texture, false, false)).setTransparencyState(NO_TRANSPARENCY).setDiffuseLightingState(NO_DIFFUSE_LIGHTING).setAlphaState(DEFAULT_ALPHA).setCullState(NO_CULL).setLightmapState(LIGHTMAP).setOverlayState(OVERLAY).createCompositeState(false);
		return RenderType.create("endergetic:unshaded_entity", DefaultVertexFormat.NEW_ENTITY, 7, 256, true, false, state);
	}
}
