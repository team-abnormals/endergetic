package endergeticexpansion.client.model.armor;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.entity.LivingEntity;

/**
 * ModelBoofloVest - Endergized
 * Created using Tabula 7.0.0
 */
public class ModelBoofloVest<T extends LivingEntity> extends BipedModel<T> {
    public RendererModel strap;
    public RendererModel boofer;

    public ModelBoofloVest(float modelSize) {
    	super(modelSize, 0.0F, 64, 64);
        this.strap = new RendererModel(this, 16, 16);
        this.strap.setRotationPoint(-4.0F, 0.0F, -2.0F);
        this.strap.addBox(0.0F, 0.0F, 0.0F, 8, 11, 4, 0.0F);
        this.boofer = new RendererModel(this, 0, 32);
        this.boofer.setRotationPoint(0.0F, 2.0F, -2.0F);
        this.boofer.addBox(0.0F, 0.0F, 0.0F, 8, 8, 8, 0.0F);
        this.strap.addChild(this.boofer);
    }

	@Override
    public void render(T entity, float f, float f1, float f2, float f3, float f4, float f5) {
    	super.render(entity, f, f1, f2, f3, f4, f5);
    	GlStateManager.pushMatrix();
    	GlStateManager.scalef(1.25F, 1.25F, 1.25F);
    	if(entity.isSneaking()) {
    		GlStateManager.translatef(-0.25F, 0.18F, -0.175F);
    	} else {
    		GlStateManager.translatef(-0.25F, -0.05F, -0.125F);
    	}
        this.strap.render(f5);
    	GlStateManager.popMatrix();
    }
	
	@Override
	public void setRotationAngles(T entity, float p_212844_2_, float p_212844_3_, float p_212844_4_,float p_212844_5_, float p_212844_6_, float p_212844_7_) {
		super.setRotationAngles(entity, p_212844_2_, p_212844_3_, p_212844_4_, p_212844_5_, p_212844_6_, p_212844_7_);
		this.strap.func_217177_a(this.field_78115_e);
	}

}
