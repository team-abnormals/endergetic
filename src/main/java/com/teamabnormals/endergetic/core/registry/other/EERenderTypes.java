package com.teamabnormals.endergetic.core.registry.other;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.teamabnormals.blueprint.client.BlueprintRenderTypes;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public final class EERenderTypes extends RenderStateShard {
	public static final RenderType BOLLOOM_STRING = BlueprintRenderTypes.getUnshadedCutoutEntity(new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/balloon/bolloom_balloon.png"), true);
	public static final RenderType BOLLOOM_VINE = BlueprintRenderTypes.getUnshadedCutoutEntity(new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/bolloom_fruit.png"), true);
	public static final RenderType EETLE_EGG_SILK = BlueprintRenderTypes.getUnshadedCutoutEntity(new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/tile/eggs/eetle_egg_silk.png"), true);
	public static final RenderType PURPAZOID_SHIELDING_LINE = RenderType.create("balloon_string", DefaultVertexFormat.POSITION_COLOR_LIGHTMAP, VertexFormat.Mode.TRIANGLE_STRIP, 256, false, false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_LEASH_SHADER).setTextureState(NO_TEXTURE).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setCullState(NO_CULL).setLightmapState(LIGHTMAP).createCompositeState(false));

	private EERenderTypes(String nameIn, Runnable setupTaskIn, Runnable clearTaskIn) {
		super(nameIn, setupTaskIn, clearTaskIn);
	}
}
