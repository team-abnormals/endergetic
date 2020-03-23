package endergeticexpansion.client.model.armor;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;

/**
 * ModelBoofloVest - Endergized
 * Created using Tabula 7.0.0
 */
public class ModelBoofloVest<T extends LivingEntity> extends BipedModel<T> {
    public ModelRenderer strap;
    public ModelRenderer boofer;

    public ModelBoofloVest(float modelSize) {
    	super(modelSize, 0.0F, 64, 64);
        this.strap = new ModelRenderer(this, 16, 16);
        this.strap.setRotationPoint(-4.0F, 0.0F, -2.0F);
        this.strap.addBox(0.0F, 0.0F, 0.0F, 8, 11, 4, 0.0F);
        this.boofer = new ModelRenderer(this, 0, 32);
        this.boofer.setRotationPoint(0.0F, 2.0F, -2.0F);
        this.boofer.addBox(0.0F, 0.0F, 0.0F, 8, 8, 8, 0.0F);
        this.strap.addChild(this.boofer);
    }
    
    @Override
    public void render(MatrixStack p_225598_1_, IVertexBuilder p_225598_2_, int p_225598_3_, int p_225598_4_, float p_225598_5_, float p_225598_6_, float p_225598_7_, float f5) {
    	super.render(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, f5);
    	RenderSystem.pushMatrix();
    	RenderSystem.scalef(1.25F, 1.25F, 1.25F);
    	
        this.strap.render(p_225598_1_, p_225598_2_, p_225598_4_, p_225598_4_, f5, f5, f5, f5);
    	RenderSystem.popMatrix();
    }
    
    @Override
    public void setRotationAngles(T p_225597_1_, float p_225597_2_, float p_225597_3_, float p_225597_4_, float p_225597_5_, float p_225597_6_) {
    	super.setRotationAngles(p_225597_1_, p_225597_2_, p_225597_3_, p_225597_4_, p_225597_5_, p_225597_6_);
    	this.strap.copyModelAngles(this.bipedBody);
    }
}