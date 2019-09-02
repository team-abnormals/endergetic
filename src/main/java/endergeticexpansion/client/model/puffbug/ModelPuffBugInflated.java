package endergeticexpansion.client.model.puffbug;

import endergeticexpansion.common.entities.EntityPuffBug;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;

/**
 * ModelPuffBugInflated - Endergized
 * Created using Tabula 7.0.0
 */
public class ModelPuffBugInflated<E extends EntityPuffBug> extends EntityModel<E> {
    public RendererModel Body;
    public RendererModel Stinger;
    public RendererModel Neck;
    public RendererModel Head;
    public RendererModel Sensor1;
    public RendererModel Sensor2;

    public ModelPuffBugInflated() {
        this.textureWidth = 32;
        this.textureHeight = 16;
        this.Head = new RendererModel(this, 0, 0);
        this.Head.setRotationPoint(0.0F, 3.7F, 0.0F);
        this.Head.addBox(-2.0F, 0.0F, -1.5F, 4, 3, 3, 0.0F);
        this.setRotateAngle(Head, -0.136659280431156F, 0.0F, 0.0F);
        this.Stinger = new RendererModel(this, 26, 0);
        this.Stinger.setRotationPoint(-0.5F, -3.5F, 0.0F);
        this.Stinger.addBox(0.0F, -4.0F, 0.0F, 1, 4, 0, 0.0F);
        this.Body = new RendererModel(this, 8, 3);
        this.Body.setRotationPoint(0.0F, 12.0F, 0.0F);
        this.Body.addBox(-3.0F, -3.5F, -3.0F, 6, 7, 6, 0.0F);
        this.setRotateAngle(Body, 0.0F, 0.045553093477052F, 0.0F);
        this.Sensor1 = new RendererModel(this, 30, 0);
        this.Sensor1.setRotationPoint(-2.0F, 3.0F, 0.5F);
        this.Sensor1.addBox(0.0F, 0.0F, 0.0F, 1, 4, 0, 0.0F);
        this.setRotateAngle(Sensor1, 0.0F, 1.5707963267948966F, 0.7330382858376184F);
        this.Neck = new RendererModel(this, 0, 6);
        this.Neck.setRotationPoint(0.0F, 2.5F, 0.0F);
        this.Neck.addBox(-1.0F, 0.0F, -1.0F, 2, 4, 2, 0.0F);
        this.setRotateAngle(Neck, -0.136659280431156F, 0.0F, 0.0F);
        this.Sensor2 = new RendererModel(this, 30, 0);
        this.Sensor2.setRotationPoint(2.0F, 3.0F, 0.5F);
        this.Sensor2.addBox(0.0F, 0.0F, 0.0F, 1, 4, 0, 0.0F);
        this.setRotateAngle(Sensor2, 0.7330382858376184F, 1.5707963267948966F, 0.0F);
        this.Neck.addChild(this.Head);
        this.Body.addChild(this.Stinger);
        this.Head.addChild(this.Sensor1);
        this.Body.addChild(this.Neck);
        this.Head.addChild(this.Sensor2);
    }

    @Override
    public void render(E entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.Body.render(f5);
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
