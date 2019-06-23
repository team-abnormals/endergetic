package endergeticexpansion.client.model;

import endergeticexpansion.common.entities.EntityPoiseCluster;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;

/**
 * ModelPoiseBlock - Endergized
 * Created using Tabula 7.0.0
 */
public class ModelPoiseBlock<T extends EntityPoiseCluster> extends EntityModel<T> {
    public RendererModel cube;

    public ModelPoiseBlock() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.cube = new RendererModel(this, 0, 0);
        this.cube.setRotationPoint(0.0F, 16.0F, 0.0F);
        this.cube.addBox(-8.0F, -8.0F, -8.0F, 16, 16, 16, 0.0F);
    }
    
    @Override
    public void render(EntityPoiseCluster entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.cube.render(f5);
    }
    
    public void setRotateAngle(RendererModel RendererModel, float x, float y, float z) {
        RendererModel.rotateAngleX = x;
        RendererModel.rotateAngleY = y;
        RendererModel.rotateAngleZ = z;
    }
}
