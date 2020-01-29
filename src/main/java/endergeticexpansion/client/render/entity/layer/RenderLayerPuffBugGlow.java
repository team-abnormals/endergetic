package endergeticexpansion.client.render.entity.layer;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;

import endergeticexpansion.common.entities.EntityPuffBug;
import endergeticexpansion.common.entities.EntityPuffBug.PuffState;
import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderLayerPuffBugGlow <T extends EntityPuffBug, M extends EntityModel<T>> extends LayerRenderer<T, M> { 
	private final ResourceLocation[] TEXTURES = new ResourceLocation[] {
		new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/puffbug/puffbug_medium_inflated_overlay.png"),
		new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/puffbug/puffbug_inflated_overlay.png"),
		new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/puffbug/puffbug_medium_inflated_overlay_grayscale.png"),
		new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/puffbug/puffbug_inflated_overlay_grayscale.png"),
		new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/puffbug/puffbug_medium_inflated_levitation_overlay.png"),
		new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/puffbug/puffbug_inflated_levitation_overlay.png"),
	};
	
	public RenderLayerPuffBugGlow(IEntityRenderer<T, M> entityRenderer) {
		super(entityRenderer);
	}
	
	@Override
	public void render(T puffbug, float p_212842_2_, float p_212842_3_, float p_212842_4_, float p_212842_5_, float p_212842_6_, float p_212842_7_, float p_212842_8_) {
		if(puffbug.getPuffState() == PuffState.DEFLATED) return;
		
		this.bindTexture(this.getTexture(puffbug));
		
		GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, 240.0F, 240.0F);
		
		float[] rgb = new float[] {
			(float) ((puffbug.getColor() >> 16 & 255) / 255.0D),
			(float) ((puffbug.getColor() >> 8 & 255) / 255.0D),
			(float) ((puffbug.getColor() & 255) / 255.0D),
		};
		
		if(puffbug.getColor() != -1 && !isLeviationOnlyEffect(puffbug)) GlStateManager.color3f(rgb[0] * 1.5F, rgb[1] * 1.5F, rgb[2] * 1.5F);
		
		GlStateManager.disableLighting();
        
		this.getEntityModel().render(puffbug, p_212842_2_, p_212842_3_, p_212842_5_, p_212842_6_, p_212842_7_, p_212842_8_);
		
		GlStateManager.enableLighting();
		
		int i = puffbug.getBrightnessForRender();
		GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, i % 65536, i / 65536);
		this.func_215334_a(puffbug);
	}
	
	private ResourceLocation getTexture(EntityPuffBug puffbug) {
		if(this.isLeviationOnlyEffect(puffbug)) {
			return TEXTURES[puffbug.getPuffStateId() + 3];
		}
		return puffbug.getColor() != -1 ? TEXTURES[puffbug.getPuffStateId() + 1] : TEXTURES[puffbug.getPuffStateId() - 1];
	}
	
	private boolean isLeviationOnlyEffect(EntityPuffBug bug) {
		if(bug.getColor() == 13565951) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean shouldCombineTextures() {
		return false;
	}
}
