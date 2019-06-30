package endergeticexpansion.client.model.bolloom;

import endergeticexpansion.common.entities.bolloom.EntityBolloomKnot;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;

/**
 * ModelBolloomKnot - Endergized
 * Created using Tabula 7.0.0
 */
public class ModelBolloomKnot<T extends EntityBolloomKnot> extends EntityModel<T> {
    public RendererModel knot;

    public ModelBolloomKnot() {
        this.textureWidth = 32;
        this.textureHeight = 16;
        this.knot = new RendererModel(this, 0, 0);
        this.knot.setRotationPoint(-3.0F, 21.0F, -3.0F);
        this.knot.addBox(0.0F, 0.0F, 0.0F, 6, 3, 6, 0.0F);
    }

    @Override
    public void render(T entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.knot.render(f5);
    }

    public void setRotateAngle(RendererModel RendererModel, float x, float y, float z) {
        RendererModel.rotateAngleX = x;
        RendererModel.rotateAngleY = y;
        RendererModel.rotateAngleZ = z;
    }
}
