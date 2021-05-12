package com.minecraftabnormals.endergetic.client;

import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public final class EERenderTypes extends RenderState {
	public static final RenderType BOLLOOM_STRING = createUnshadedEntity(new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/balloon/bolloom_balloon.png"));
	public static final RenderType BOLLOOM_VINE = createUnshadedEntity(new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/bolloom_fruit.png"));
	public static final RenderType EETLE_EGG_SILK = createUnshadedEntity(new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/tile/eggs/eetle_egg_silk.png"));

	private EERenderTypes(String nameIn, Runnable setupTaskIn, Runnable clearTaskIn) {
		super(nameIn, setupTaskIn, clearTaskIn);
	}

	private static RenderType createUnshadedEntity(ResourceLocation texture) {
		RenderType.State state = RenderType.State.getBuilder().texture(new RenderState.TextureState(texture, false, false)).transparency(NO_TRANSPARENCY).diffuseLighting(DIFFUSE_LIGHTING_DISABLED).alpha(DEFAULT_ALPHA).cull(CULL_DISABLED).lightmap(LIGHTMAP_ENABLED).overlay(OVERLAY_ENABLED).build(false);
		return RenderType.makeType("endergetic:unshaded_entity", DefaultVertexFormats.ENTITY, 7, 256, true, false, state);
	}
}
