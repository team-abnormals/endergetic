package endergeticexpansion.client.model;

import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.Model;

/**
 * ModelBoofBlockDispenser - Endergized
 * Created using Tabula 7.0.0
 */
public class ModelBoofBlockDispenser extends Model {
    public RendererModel base;

    public ModelBoofBlockDispenser() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.base = new RendererModel(this, 0, 0);
        this.base.setRotationPoint(0.0F, 18.0F, 2.0F);
        this.base.addBox(-6.0F, -6.0F, -6.0F, 12, 12, 12, 0.0F);
    }

    public void renderAll() { 
        this.base.render(0.0625F);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(RendererModel RendererModel, float x, float y, float z) {
        RendererModel.rotateAngleX = x;
        RendererModel.rotateAngleY = y;
        RendererModel.rotateAngleZ = z;
    }
}
