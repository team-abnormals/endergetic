package endergeticexpansion.client.model;

import endergeticexpansion.common.entities.EntityBoofBlock;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;

/**
 * ModelBoofBlock - Endergized
 * Created using Tabula 7.0.0
 */
public class ModelBoofBlock<T extends EntityBoofBlock> extends EntityModel<T> {
    public RendererModel base;

    public ModelBoofBlock() {
        this.textureWidth = 130;
        this.textureHeight = 64;
        this.base = new RendererModel(this, 0, 0);
        this.base.setRotationPoint(-14.0F, 0.0F, -14.0F);
        this.base.addBox(0.0F, 0.0F, 0.0F, 28, 28, 28, 0.0F);
    }

    @Override
    public void render(EntityBoofBlock entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.base.render(f5);
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
