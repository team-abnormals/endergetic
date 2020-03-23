package endergeticexpansion.client.model.bolloom;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import endergeticexpansion.common.tileentities.TileEntityBolloomBud;
import net.minecraft.client.renderer.model.ModelRenderer;

/**
 * ModelBolloomBudOpen - Endergized
 * Created using Tabula 7.0.0
 */
public class ModelBolloomBud {
    public ModelRenderer Center;
    public ModelRenderer Pedal;
    public ModelRenderer Pedal_1;
    public ModelRenderer Pedal_2;
    public ModelRenderer Pedal_3;
    public ModelRenderer Pedal_open;
    public ModelRenderer Pedal_1_open;
    public ModelRenderer Pedal_2_open;
    public ModelRenderer Pedal_3_open;

    public ModelBolloomBud() {
    	int[] textureSize = { 64, 32 };
    	
        this.Pedal = new ModelRenderer(textureSize[0], textureSize[1], 0, 18);
        this.Pedal.setRotationPoint(-6.0F, 21.0F, 7.0F);
        this.Pedal.addBox(0.0F, 0.0F, 0.0F, 12, 2, 12, 0.0F);
        this.setRotateAngle(Pedal, 1.780235837034216F, 0.0F, 0.0F);
        this.Pedal_3 = new ModelRenderer(textureSize[0], textureSize[1], 0, 18);
        this.Pedal_3.setRotationPoint(-7.0F, 21.0F, -6.0F);
        this.Pedal_3.addBox(0.0F, 0.0F, 0.0F, 12, 2, 12, 0.0F);
        this.setRotateAngle(Pedal_3, 1.780235837034216F, -1.5707963267948966F, 0.0F);
        this.Pedal_1 = new ModelRenderer(textureSize[0], textureSize[1], 0, 18);
        this.Pedal_1.setRotationPoint(7.0F, 21.0F, 6.0F);
        this.Pedal_1.addBox(0.0F, 0.0F, 0.0F, 12, 2, 12, 0.0F);
        this.setRotateAngle(Pedal_1, 1.780235837034216F, 1.5707963267948966F, 0.0F);
        this.Pedal_2 = new ModelRenderer(textureSize[0], textureSize[1], 0, 18);
        this.Pedal_2.setRotationPoint(6.0F, 21.0F, -7.0F);
        this.Pedal_2.addBox(0.0F, 0.0F, 0.0F, 12, 2, 12, 0.0F);
        this.setRotateAngle(Pedal_2, 1.780235837034216F, 3.141592653589793F, 0.0F);
        this.Center = new ModelRenderer(textureSize[0], textureSize[1], 0, 0);
        this.Center.setRotationPoint(-7.0F, 21.0F, -7.0F);
        this.Center.addBox(0.0F, 0.0F, 0.0F, 14, 3, 14, 0.0F);
        this.Pedal_open = new ModelRenderer(textureSize[0], textureSize[1], 0, 18);
        this.Pedal_open.setRotationPoint(-6.0F, 21.0F, 7.0F);
        this.Pedal_open.addBox(0.0F, 0.0F, 0.0F, 12, 2, 12, 0.0F);
        this.setRotateAngle(Pedal_open, 0F, 0.0F, 0.0F);
        this.Pedal_3_open = new ModelRenderer(textureSize[0], textureSize[1], 0, 18);
        this.Pedal_3_open.setRotationPoint(-7.0F, 21.0F, -6.0F);
        this.Pedal_3_open.addBox(0.0F, 0.0F, 0.0F, 12, 2, 12, 0.0F);
        this.setRotateAngle(Pedal_3_open, 0F, -1.5707963267948966F, 0.0F);
        this.Pedal_1_open = new ModelRenderer(textureSize[0], textureSize[1], 0, 18);
        this.Pedal_1_open.setRotationPoint(7.0F, 21.0F, 6.0F);
        this.Pedal_1_open.addBox(0.0F, 0.0F, 0.0F, 12, 2, 12, 0.0F);
        this.setRotateAngle(Pedal_1_open, 0, 1.5707963267948966F, 0.0F);
        this.Pedal_2_open = new ModelRenderer(textureSize[0], textureSize[1], 0, 18);
        this.Pedal_2_open.setRotationPoint(6.0F, 21.0F, -7.0F);
        this.Pedal_2_open.addBox(0.0F, 0.0F, 0.0F, 12, 2, 12, 0.0F);
        this.setRotateAngle(Pedal_2_open, 0, 3.141592653589793F, 0.0F);
    }

    public void renderAllClosed(MatrixStack matrix, IVertexBuilder builder, int light, int overlay) { 
        this.Pedal.render(matrix, builder, light, overlay);
        this.Pedal_3.render(matrix, builder, light, overlay);
        this.Pedal_1.render(matrix, builder, light, overlay);
        this.Pedal_2.render(matrix, builder, light, overlay);
        this.Center.render(matrix, builder, light, overlay);
    }
    
    public void renderAll(TileEntityBolloomBud bud, MatrixStack matrix, IVertexBuilder builder, int light, int overlay) {
    	float angle = 1.78F * bud.PEDAL_PROGRESS.getAnimationProgress();
    	
    	this.Pedal.rotateAngleX = angle;
    	this.Pedal_1.rotateAngleX = angle;
    	this.Pedal_2.rotateAngleX = angle;
    	this.Pedal_3.rotateAngleX = angle;
    	
    	this.Pedal.render(matrix, builder, light, overlay);
    	this.Pedal_1.render(matrix, builder, light, overlay);
    	this.Pedal_2.render(matrix, builder, light, overlay);
    	this.Pedal_3.render(matrix, builder, light, overlay);
    	
    	this.Pedal.rotateAngleX = 0.0F;
    	this.Pedal_1.rotateAngleX = 0.0F;
    	this.Pedal_2.rotateAngleX = 0.0F;
    	this.Pedal_3.rotateAngleX = 0.0F;
        
        this.Center.render(matrix, builder, light, overlay);
    }

    public void setRotateAngle(ModelRenderer ModelRenderer, float x, float y, float z) {
        ModelRenderer.rotateAngleX = x;
        ModelRenderer.rotateAngleY = y;
        ModelRenderer.rotateAngleZ = z;
    }
    
}
