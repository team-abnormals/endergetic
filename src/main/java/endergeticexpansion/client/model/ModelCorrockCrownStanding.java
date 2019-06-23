package endergeticexpansion.client.model;

import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.Model;

/**
 * ModelCorrockCrownStanding - Endergized
 * Created using Tabula 7.0.0
 */
public class ModelCorrockCrownStanding extends Model {
    public RendererModel shape1;
    public RendererModel shape1_1;

    public ModelCorrockCrownStanding() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.shape1 = new RendererModel(this, 0, 0);
        this.shape1.mirror = true;
        this.shape1.setRotationPoint(-8.0F, 24.0F, 0.0F);
        this.shape1.addBox(0.0F, -16.0F, 0.0F, 16, 16, 0, 0.0F);
        this.setRotateAngle(shape1, 0.17453292519943295F, 0.0F, 0.0F);
        this.shape1_1 = new RendererModel(this, 0, 0);
        this.shape1_1.setRotationPoint(-8.0F, 24.0F, 0.0F);
        this.shape1_1.addBox(0.0F, -16.0F, 0.0F, 16, 16, 0, 0.0F);
        this.setRotateAngle(shape1_1, -0.17453292519943295F, 0.0F, 0.0F);
    }

    public void renderAll() { 
        this.shape1.render(0.0625F);
        this.shape1_1.render(0.0625F);
    }

    public void setRotateAngle(RendererModel RendererModel, float x, float y, float z) {
        RendererModel.rotateAngleX = x;
        RendererModel.rotateAngleY = y;
        RendererModel.rotateAngleZ = z;
    }
}
