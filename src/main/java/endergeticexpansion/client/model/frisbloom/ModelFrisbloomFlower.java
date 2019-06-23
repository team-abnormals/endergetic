package endergeticexpansion.client.model.frisbloom;

import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.Model;

/**
 * ModelFrisbloomFlower - Endergized & SmellyModder
 * Created using Tabula 7.0.0
 */
public class ModelFrisbloomFlower extends Model {
    public RendererModel shapez;
    public RendererModel shapex;
    public RendererModel shapex_g;
    public RendererModel shapez_g;

    public ModelFrisbloomFlower() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.shapez_g = new RendererModel(this, 52, 0);
        this.shapez_g.setRotationPoint(0.0F, 8.0F, -3.0F);
        this.shapez_g.addBox(0.0F, 0.0F, 0.0F, 0, 16, 6, 0.0F);
        this.shapez = new RendererModel(this, 13, 0);
        this.shapez.setRotationPoint(0.0F, 8.0F, -3.0F);
        this.shapez.addBox(0.0F, 0.0F, 0.0F, 0, 16, 6, 0.0F);
        this.shapex_g = new RendererModel(this, 39, 0);
        this.shapex_g.setRotationPoint(3.0F, 8.0F, 0.0F);
        this.shapex_g.addBox(0.0F, 0.0F, 0.0F, 0, 16, 6, 0.0F);
        this.setRotateAngle(shapex_g, 0.0F, -1.5707963267948966F, 0.0F);
        this.shapex = new RendererModel(this, 0, 0);
        this.shapex.setRotationPoint(3.0F, 8.0F, 0.0F);
        this.shapex.addBox(0.0F, 0.0F, 0.0F, 0, 16, 6, 0.0F);
        this.setRotateAngle(shapex, 0.0F, -1.5707963267948966F, 0.0F);
    }

    public void renderAll(boolean emissive) { 
        if(emissive) {
        	this.shapez_g.render(0.0625F);
        	this.shapex_g.render(0.0625F);
        } else {
        	this.shapex.render(0.0625F);
        	this.shapez.render(0.0625F);
        }
    }

    public void setRotateAngle(RendererModel RendererModel, float x, float y, float z) {
        RendererModel.rotateAngleX = x;
        RendererModel.rotateAngleY = y;
        RendererModel.rotateAngleZ = z;
    }
}
