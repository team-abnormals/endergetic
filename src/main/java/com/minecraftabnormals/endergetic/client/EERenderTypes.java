package com.minecraftabnormals.endergetic.client;

import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import com.teamabnormals.blueprint.client.BlueprintRenderTypes;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public final class EERenderTypes extends RenderStateShard {
	public static final RenderType BOLLOOM_STRING = BlueprintRenderTypes.getUnshadedCutoutEntity(new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/balloon/bolloom_balloon.png"), true);
	public static final RenderType BOLLOOM_VINE = BlueprintRenderTypes.getUnshadedCutoutEntity(new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/bolloom_fruit.png"), true);
	public static final RenderType EETLE_EGG_SILK = BlueprintRenderTypes.getUnshadedCutoutEntity(new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/tile/eggs/eetle_egg_silk.png"), true);

	private EERenderTypes(String nameIn, Runnable setupTaskIn, Runnable clearTaskIn) {
		super(nameIn, setupTaskIn, clearTaskIn);
	}
}
