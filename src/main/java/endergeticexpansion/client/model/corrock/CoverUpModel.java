package endergeticexpansion.client.model.corrock;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.model.ModelRenderer;

/**
 * CoverUpModel - SmellyModder
 * Created using Tabula 7.0.0
 */
public class CoverUpModel {
    public ModelRenderer block;

    public CoverUpModel() {
        this.block = new ModelRenderer(64, 32, 0, 0);
        this.block.setRotationPoint(-8.0F, 8.0F, -8.0F);
        this.block.addBox(0.0F, 0.0F, 0.0F, 16, 16, 16, 0.0F);
    }
    
    public void render(MatrixStack matrix, IVertexBuilder builder, int light, int overlay) { 
    	this.block.render(matrix, builder, light, overlay);
    }
}