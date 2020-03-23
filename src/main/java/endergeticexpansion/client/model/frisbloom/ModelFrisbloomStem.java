package endergeticexpansion.client.model.frisbloom;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.model.ModelRenderer;

/**
 * ModelFrisbloomStem - Endergized
 * Created using Tabula 7.0.0
 */
public class ModelFrisbloomStem {
	public ModelRenderer frisbloom_stem_x;
    public ModelRenderer frisbloom_stem_y;
    public ModelRenderer frisbloom_small_top;
    public ModelRenderer frisbloom_small_bottom;
    public ModelRenderer frisbloom_medium_top;
    public ModelRenderer frisbloom_medium_bottom;
    public ModelRenderer frisbloom_large_top;
    public ModelRenderer frisbloom_large_bottom;

    public ModelFrisbloomStem() {
    	int[] textureSize = {64, 32};
    	
        this.frisbloom_medium_bottom = new ModelRenderer(textureSize[0], textureSize[1], 16, 16);
        this.frisbloom_medium_bottom.setRotationPoint(-12.0F, 16.11F, -12.0F);
        this.frisbloom_medium_bottom.addBox(0.0F, 0.0F, 0.0F, 16, 0, 16, 0.0F);
        this.frisbloom_stem_x = new ModelRenderer(textureSize[0], textureSize[1], 13, -6);
        this.frisbloom_stem_x.setRotationPoint(0.0F, 8.0F, -3.0F);
        this.frisbloom_stem_x.addBox(0.0F, 0.0F, 0.0F, 0, 16, 6, 0.0F);
        this.frisbloom_small_top = new ModelRenderer(textureSize[0], textureSize[1], -16, 16);
        this.frisbloom_small_top.setRotationPoint(-8.0F, 16.0F, -8.0F);
        this.frisbloom_small_top.addBox(0.0F, 0.0F, 0.0F, 16, 0, 16, 0.0F);
        this.frisbloom_large_bottom = new ModelRenderer(textureSize[0], textureSize[1], 16, 16);
        this.frisbloom_large_bottom.setRotationPoint(-16.0F, 16.11F, -16.0F);
        this.frisbloom_large_bottom.addBox(0.0F, 0.0F, 0.0F, 16, 0, 16, 0.0F);
        this.frisbloom_large_top = new ModelRenderer(textureSize[0], textureSize[1], -16, 16);
        this.frisbloom_large_top.setRotationPoint(-16.0F, 16.0F, -16.0F);
        this.frisbloom_large_top.addBox(0.0F, 0.0F, 0.0F, 16, 0, 16, 0.0F);
        this.frisbloom_small_bottom = new ModelRenderer(textureSize[0], textureSize[1], 16, 16);
        this.frisbloom_small_bottom.setRotationPoint(-8.0F, 16.11F, -8.0F);
        this.frisbloom_small_bottom.addBox(0.0F, 0.0F, 0.0F, 16, 0, 16, 0.0F);
        this.frisbloom_stem_y = new ModelRenderer(textureSize[0], textureSize[1], 0, -6);
        this.frisbloom_stem_y.setRotationPoint(3.0F, 8.0F, 0.0F);
        this.frisbloom_stem_y.addBox(0.0F, 0.0F, 0.0F, 0, 16, 6, 0.0F);
        this.setRotateAngle(frisbloom_stem_y, 0.0F, -1.5707963267948966F, 0.0F);
        this.frisbloom_medium_top = new ModelRenderer(textureSize[0], textureSize[1], -16, 16);
        this.frisbloom_medium_top.setRotationPoint(-12.0F, 16.0F, -12.0F);
        this.frisbloom_medium_top.addBox(0.0F, 0.0F, 0.0F, 16, 0, 16, 0.0F);
    }

    public void renderStem(MatrixStack matrix, IVertexBuilder builder, int light, int overlay) { 
        this.frisbloom_stem_y.render(matrix, builder, light, overlay);
        this.frisbloom_stem_x.render(matrix, builder, light, overlay);
    }
    
	public void renderFrisbloom(MatrixStack matrix, IVertexBuilder builder, int light, int overlay, int size, boolean isBottom) {
//    	if(isBottom) {
//    		if(size == 3) {
//    			this.frisbloom_small_bottom.render(matrix, builder, light, overlay);
//    		} else if(size == 2) {
//    			GlStateManager.pushMatrix();
//    			GlStateManager.translatef(this.frisbloom_medium_bottom.offsetX, this.frisbloom_medium_bottom.offsetY, this.frisbloom_medium_bottom.offsetZ);
//    			GlStateManager.translatef(this.frisbloom_medium_bottom.rotationPointX * 0.0625F, this.frisbloom_medium_bottom.rotationPointY * 0.0625F, this.frisbloom_medium_bottom.rotationPointZ * 0.0625F);
//    			GlStateManager.scaled(1.5D, 1.5D, 1.5D);
//    			GlStateManager.translatef(-this.frisbloom_medium_bottom.offsetX, -this.frisbloom_medium_bottom.offsetY, -this.frisbloom_medium_bottom.offsetZ);
//    			GlStateManager.translatef(-this.frisbloom_medium_bottom.rotationPointX * 0.0625F, -this.frisbloom_medium_bottom.rotationPointY * 0.0625F, -this.frisbloom_medium_bottom.rotationPointZ * 0.0625F);
//    			this.frisbloom_medium_bottom.render(matrix, builder, light, overlay);
//    			GlStateManager.popMatrix();
//    		} else {
//    			GlStateManager.pushMatrix();
//    			GlStateManager.translatef(this.frisbloom_large_bottom.offsetX, this.frisbloom_large_bottom.offsetY, this.frisbloom_large_bottom.offsetZ);
//    			GlStateManager.translatef(this.frisbloom_large_bottom.rotationPointX * 0.0625F, this.frisbloom_large_bottom.rotationPointY * 0.0625F, this.frisbloom_large_bottom.rotationPointZ * 0.0625F);
//    			GlStateManager.scaled(2.0D, 2.0D, 2.0D);
//    			GlStateManager.translatef(-this.frisbloom_large_bottom.offsetX, -this.frisbloom_large_bottom.offsetY, -this.frisbloom_large_bottom.offsetZ);
//    			GlStateManager.translatef(-this.frisbloom_large_bottom.rotationPointX * 0.0625F, -this.frisbloom_large_bottom.rotationPointY * 0.0625F, -this.frisbloom_large_bottom.rotationPointZ * 0.0625F);
//    			this.frisbloom_large_bottom.render(matrix, builder, light, overlay);
//    			GlStateManager.popMatrix();
//    		}
//    	} else {
//    		if(size == 3) {
//    			this.frisbloom_small_top.render(matrix, builder, light, overlay);
//    		} else if(size == 2) {
//    			GlStateManager.pushMatrix();
//    			GlStateManager.translatef(this.frisbloom_medium_top.offsetX, this.frisbloom_medium_top.offsetY, this.frisbloom_medium_top.offsetZ);
//    			GlStateManager.translatef(this.frisbloom_medium_top.rotationPointX * 0.0625F, this.frisbloom_medium_top.rotationPointY * 0.0625F, this.frisbloom_medium_top.rotationPointZ * 0.0625F);
//    			GlStateManager.scaled(1.5D, 1.5D, 1.5D);
//    			GlStateManager.translatef(-this.frisbloom_medium_top.offsetX, -this.frisbloom_medium_top.offsetY, -this.frisbloom_medium_top.offsetZ);
//    			GlStateManager.translatef(-this.frisbloom_medium_top.rotationPointX * 0.0625F, -this.frisbloom_medium_top.rotationPointY * 0.0625F, -this.frisbloom_medium_top.rotationPointZ * 0.0625F);
//    			this.frisbloom_medium_top.render(matrix, builder, light, overlay);
//    			GlStateManager.popMatrix();
//    		} else {
//    			GlStateManager.pushMatrix();
//    			GlStateManager.translatef(this.frisbloom_large_top.offsetX, this.frisbloom_large_top.offsetY, this.frisbloom_large_top.offsetZ);
//    			GlStateManager.translatef(this.frisbloom_large_top.rotationPointX * 0.0625F, this.frisbloom_large_top.rotationPointY * 0.0625F, this.frisbloom_large_top.rotationPointZ * 0.0625F);
//    			GlStateManager.scaled(2.0D, 2.0D, 2.0D);
//    			GlStateManager.translatef(-this.frisbloom_large_top.offsetX, -this.frisbloom_large_top.offsetY, -this.frisbloom_large_top.offsetZ);
//    			GlStateManager.translatef(-this.frisbloom_large_top.rotationPointX * 0.0625F, -this.frisbloom_large_top.rotationPointY * 0.0625F, -this.frisbloom_large_top.rotationPointZ * 0.0625F);
//    			this.frisbloom_large_top.render(matrix, builder, light, overlay);
//    			GlStateManager.popMatrix();
//    		}
//    	}
    }

    public void setRotateAngle(ModelRenderer ModelRenderer, float x, float y, float z) {
        ModelRenderer.rotateAngleX = x;
        ModelRenderer.rotateAngleY = y;
        ModelRenderer.rotateAngleZ = z;
    }
}
