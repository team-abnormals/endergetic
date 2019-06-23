package endergeticexpansion.client.model;

import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.Model;

/**
 * ModelPuffBugHive - Endergized
 * Created using Tabula 7.0.0
 */
public class ModelPuffBugHive extends Model {
    public RendererModel HiveBase;
    public RendererModel HiveBottom;

    public ModelPuffBugHive() {
        this.textureWidth = 100;
        this.textureHeight = 64;
        this.HiveBottom = new RendererModel(this, 0, 30);
        this.HiveBottom.setRotationPoint(-7.0F, 21.0F, -7.0F);
        this.HiveBottom.addBox(0.0F, 0.0F, 0.0F, 14, 3, 14, 0.0F);
        this.HiveBase = new RendererModel(this, 0, 1);
        this.HiveBase.setRotationPoint(-8.0F, 8.0F, -8.0F);
        this.HiveBase.addBox(0.0F, 0.0F, 0.0F, 16, 13, 16, 0.0F);
    }

    public void renderAll() { 
        this.HiveBottom.render(0.0625F);
        this.HiveBase.render(0.0625F);
    }

    public void setRotateAngle(RendererModel RendererModel, float x, float y, float z) {
        RendererModel.rotateAngleX = x;
        RendererModel.rotateAngleY = y;
        RendererModel.rotateAngleZ = z;
    }
}
