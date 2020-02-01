package endergeticexpansion.client.model.bolloom;

import endergeticexpansion.common.tileentities.TileEntityBolloomBud;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.Model;

/**
 * ModelBolloomBudOpen - Endergized
 * Created using Tabula 7.0.0
 */
public class ModelBolloomBud extends Model {
    public RendererModel Center;
    public RendererModel Pedal;
    public RendererModel Pedal_1;
    public RendererModel Pedal_2;
    public RendererModel Pedal_3;
    public RendererModel Pedal_open;
    public RendererModel Pedal_1_open;
    public RendererModel Pedal_2_open;
    public RendererModel Pedal_3_open;

    public ModelBolloomBud() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.Pedal = new RendererModel(this, 0, 18);
        this.Pedal.setRotationPoint(-6.0F, 21.0F, 7.0F);
        this.Pedal.addBox(0.0F, 0.0F, 0.0F, 12, 2, 12, 0.0F);
        this.setRotateAngle(Pedal, 1.780235837034216F, 0.0F, 0.0F);
        this.Pedal_3 = new RendererModel(this, 0, 18);
        this.Pedal_3.setRotationPoint(-7.0F, 21.0F, -6.0F);
        this.Pedal_3.addBox(0.0F, 0.0F, 0.0F, 12, 2, 12, 0.0F);
        this.setRotateAngle(Pedal_3, 1.780235837034216F, -1.5707963267948966F, 0.0F);
        this.Pedal_1 = new RendererModel(this, 0, 18);
        this.Pedal_1.setRotationPoint(7.0F, 21.0F, 6.0F);
        this.Pedal_1.addBox(0.0F, 0.0F, 0.0F, 12, 2, 12, 0.0F);
        this.setRotateAngle(Pedal_1, 1.780235837034216F, 1.5707963267948966F, 0.0F);
        this.Pedal_2 = new RendererModel(this, 0, 18);
        this.Pedal_2.setRotationPoint(6.0F, 21.0F, -7.0F);
        this.Pedal_2.addBox(0.0F, 0.0F, 0.0F, 12, 2, 12, 0.0F);
        this.setRotateAngle(Pedal_2, 1.780235837034216F, 3.141592653589793F, 0.0F);
        this.Center = new RendererModel(this, 0, 0);
        this.Center.setRotationPoint(-7.0F, 21.0F, -7.0F);
        this.Center.addBox(0.0F, 0.0F, 0.0F, 14, 3, 14, 0.0F);
        this.Pedal_open = new RendererModel(this, 0, 18);
        this.Pedal_open.setRotationPoint(-6.0F, 21.0F, 7.0F);
        this.Pedal_open.addBox(0.0F, 0.0F, 0.0F, 12, 2, 12, 0.0F);
        this.setRotateAngle(Pedal_open, 0F, 0.0F, 0.0F);
        this.Pedal_3_open = new RendererModel(this, 0, 18);
        this.Pedal_3_open.setRotationPoint(-7.0F, 21.0F, -6.0F);
        this.Pedal_3_open.addBox(0.0F, 0.0F, 0.0F, 12, 2, 12, 0.0F);
        this.setRotateAngle(Pedal_3_open, 0F, -1.5707963267948966F, 0.0F);
        this.Pedal_1_open = new RendererModel(this, 0, 18);
        this.Pedal_1_open.setRotationPoint(7.0F, 21.0F, 6.0F);
        this.Pedal_1_open.addBox(0.0F, 0.0F, 0.0F, 12, 2, 12, 0.0F);
        this.setRotateAngle(Pedal_1_open, 0, 1.5707963267948966F, 0.0F);
        this.Pedal_2_open = new RendererModel(this, 0, 18);
        this.Pedal_2_open.setRotationPoint(6.0F, 21.0F, -7.0F);
        this.Pedal_2_open.addBox(0.0F, 0.0F, 0.0F, 12, 2, 12, 0.0F);
        this.setRotateAngle(Pedal_2_open, 0, 3.141592653589793F, 0.0F);
    }

    public void renderAllClosed() { 
        this.Pedal.render(0.0625F);
        this.Pedal_3.render(0.0625F);
        this.Pedal_1.render(0.0625F);
        this.Pedal_2.render(0.0625F);
        this.Center.render(0.0625F);
    }
    
    public void renderAllOpen() { 
        this.Pedal_open.render(0.0625F);
        this.Pedal_3_open.render(0.0625F);
        this.Pedal_1_open.render(0.0625F);
        this.Pedal_2_open.render(0.0625F);
        this.Center.render(0.0625F);
    }
    
    public void renderAll(TileEntityBolloomBud bud) {
    	float angle = 1.78F * bud.PEDAL_PROGRESS.getAnimationProgress();
    	
    	this.Pedal.rotateAngleX = angle;
    	this.Pedal_1.rotateAngleX = angle;
    	this.Pedal_2.rotateAngleX = angle;
    	this.Pedal_3.rotateAngleX = angle;
    	
    	this.Pedal.render(0.0625F);
    	this.Pedal_1.render(0.0625F);
    	this.Pedal_2.render(0.0625F);
    	this.Pedal_3.render(0.0625F);
    	
    	this.Pedal.rotateAngleX = 0.0F;
    	this.Pedal_1.rotateAngleX = 0.0F;
    	this.Pedal_2.rotateAngleX = 0.0F;
    	this.Pedal_3.rotateAngleX = 0.0F;
        
        this.Center.render(0.0625F);
    }

    public void setRotateAngle(RendererModel RendererModel, float x, float y, float z) {
        RendererModel.rotateAngleX = x;
        RendererModel.rotateAngleY = y;
        RendererModel.rotateAngleZ = z;
    }
    
}
