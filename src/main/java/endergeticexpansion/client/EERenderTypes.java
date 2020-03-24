package endergeticexpansion.client;

import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class EERenderTypes extends RenderState {

	public EERenderTypes(String nameIn, Runnable setupTaskIn, Runnable clearTaskIn) {
		super(nameIn, setupTaskIn, clearTaskIn);
	}

	public static RenderType getEmissiveEntity(ResourceLocation texture) {
		RenderType.State state = RenderType.State.getBuilder().texture(new RenderState.TextureState(texture, false, false)).transparency(NO_TRANSPARENCY).diffuseLighting(DIFFUSE_LIGHTING_DISABLED).alpha(DEFAULT_ALPHA).cull(CULL_DISABLED).lightmap(LIGHTMAP_ENABLED).overlay(OVERLAY_ENABLED).build(true);
		return RenderType.makeType("entity_emissive_cutout", DefaultVertexFormats.ENTITY, 7, 256, true, false, state);
	}
	
}