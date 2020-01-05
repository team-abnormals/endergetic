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
public class RenderLayerBoofloGlow<E extends EntityBooflo, M extends EntityModel<E>> extends LayerRenderer<E, M> {
	public static final ResourceLocation BOOFLO_GLOW_LAYER = new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/booflo/booflo_glow_layer.png");
	public static final ResourceLocation BOOFLO_INFLATED_GLOW_LAYER = new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/booflo/booflo_inflated_glow_layer.png");
	
	public RenderLayerBoofloGlow(IEntityRenderer<E, M> entityRenderer) {
		super(entityRenderer);
	}
	
	@Override
	public void render(E booflo, float p_212842_2_, float p_212842_3_, float p_212842_4_, float p_212842_5_, float p_212842_6_, float p_212842_7_, float p_212842_8_) {
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
		return booflo.isBoofed() ? BOOFLO_INFLATED_GLOW_LAYER : BOOFLO_GLOW_LAYER;
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}
}