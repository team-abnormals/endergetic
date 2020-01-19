package endergeticexpansion.client.render.entity.layer;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;

import endergeticexpansion.common.entities.booflo.EntityBooflo;
import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderLayerBoofloBracelets<E extends EntityBooflo, M extends EntityModel<E>> extends LayerRenderer<E, M> {
	public RenderLayerBoofloBracelets(IEntityRenderer<E, M> entityRenderer) {
		super(entityRenderer);
	}
	
	@Override
	public void render(E booflo, float p_212842_2_, float p_212842_3_, float p_212842_4_, float p_212842_5_, float p_212842_6_, float p_212842_7_, float p_212842_8_) {
		if(!booflo.isTamed()) return;
		
		this.bindTexture(this.getTexture(booflo));
		
		GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, 240.0F, 240.0F);
		
		GlStateManager.disableLighting();
        
		this.getEntityModel().render(booflo, p_212842_2_, p_212842_3_, p_212842_5_, p_212842_6_, p_212842_7_, p_212842_8_);
		
		GlStateManager.enableLighting();
		
		int i = booflo.getBrightnessForRender();
		GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, i % 65536, i / 65536);
		this.func_215334_a(booflo);
	}
	
	public ResourceLocation getTexture(E booflo) {
		return booflo.isBoofed() ? new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/booflo/bracelets/booflo_ring_" + booflo.getBraceletsColor().getTranslationKey() + "_inflated.png") : new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/booflo/bracelets/booflo_ring_" + booflo.getBraceletsColor().getTranslationKey() + ".png");
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}
}