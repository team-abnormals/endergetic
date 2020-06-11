package endergeticexpansion.client.model.frisbloom;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.model.ModelRenderer;

/**
 * ModelFrisbloomFlower - Endergized & SmellyModder
 * Created using Tabula 7.0.0
 */
public class FrisbloomFlowerModel {
    public ModelRenderer shapez;
    public ModelRenderer shapex;
    public ModelRenderer shapex_g;
    public ModelRenderer shapez_g;

    public FrisbloomFlowerModel() {
        int[] textureSizes = {64, 32};
    	
        this.shapez_g = new ModelRenderer(textureSizes[0], textureSizes[1], 52, 0);
        this.shapez_g.setRotationPoint(0.0F, 8.0F, -3.0F);
        this.shapez_g.addBox(0.0F, 0.0F, 0.0F, 0, 16, 6, 0.0F);
        this.shapez = new ModelRenderer(textureSizes[0], textureSizes[1], 13, 0);
        this.shapez.setRotationPoint(0.0F, 8.0F, -3.0F);
        this.shapez.addBox(0.0F, 0.0F, 0.0F, 0, 16, 6, 0.0F);
        this.shapex_g = new ModelRenderer(textureSizes[0], textureSizes[1], 39, 0);
        this.shapex_g.setRotationPoint(3.0F, 8.0F, 0.0F);
        this.shapex_g.addBox(0.0F, 0.0F, 0.0F, 0, 16, 6, 0.0F);
        this.setRotateAngle(shapex_g, 0.0F, -1.5707963267948966F, 0.0F);
        this.shapex = new ModelRenderer(textureSizes[0], textureSizes[1], 0, 0);
        this.shapex.setRotationPoint(3.0F, 8.0F, 0.0F);
        this.shapex.addBox(0.0F, 0.0F, 0.0F, 0, 16, 6, 0.0F);
        this.setRotateAngle(shapex, 0.0F, -1.5707963267948966F, 0.0F);
    }

    public void renderAll(MatrixStack matrix, IVertexBuilder builder, int light, int overlay) { 
        this.shapez_g.render(matrix, builder, 15728640, overlay);
        this.shapex_g.render(matrix, builder, 15728640, overlay);
       
        this.shapex.render(matrix, builder, light, overlay);
        this.shapez.render(matrix, builder, light, overlay);
    }

    public void setRotateAngle(ModelRenderer ModelRenderer, float x, float y, float z) {
        ModelRenderer.rotateAngleX = x;
        ModelRenderer.rotateAngleY = y;
        ModelRenderer.rotateAngleZ = z;
    }
}