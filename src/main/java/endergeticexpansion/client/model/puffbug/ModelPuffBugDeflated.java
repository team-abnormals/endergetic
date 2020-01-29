package endergeticexpansion.client.model.puffbug;

import endergeticexpansion.api.endimator.EndimatorEntityModel;
import endergeticexpansion.api.endimator.EndimatorRendererModel;
import endergeticexpansion.common.entities.EntityPuffBug;
import net.minecraft.util.math.MathHelper;

/**
 * ModelPuffBugDeflated - Endergized
 * Created using Tabula 7.0.0
 */
public class ModelPuffBugDeflated<E extends EntityPuffBug> extends EndimatorEntityModel<E> {
    public EndimatorRendererModel Body;
    public EndimatorRendererModel Stinger;
    public EndimatorRendererModel Neck;
    public EndimatorRendererModel Head;
    public EndimatorRendererModel Sensor1;
    public EndimatorRendererModel Sensor2;

    public ModelPuffBugDeflated() {
        this.textureWidth = 32;
        this.textureHeight = 16;
        this.Body = new EndimatorRendererModel(this, 10, 7);
        this.Body.setRotationPoint(0.0F, 11.0F, 0.0F);
        this.Body.addBox(-1.0F, -3.0F, -1.0F, 2, 6, 2, 0.0F);
        this.Sensor1 = new EndimatorRendererModel(this, 18, 1);
        this.Sensor1.setRotationPoint(-2.0F, 3.0F, 0.5F);
        this.Sensor1.addBox(0.0F, 0.0F, 0.0F, 1, 4, 0, 0.0F);
        this.setRotateAngle(Sensor1, 0.0F, 1.5707963267948966F, 0.7330382858376184F);
        this.Head = new EndimatorRendererModel(this, 0, 0);
        this.Head.setRotationPoint(0.0F, 3.7F, 0.0F);
        this.Head.addBox(-2.0F, 0.0F, -1.5F, 4, 3, 3, 0.0F);
        this.setRotateAngle(Head, -0.13962634015954636F, 0.0F, 0.0F);
        this.Sensor2 = new EndimatorRendererModel(this, 18, 1);
        this.Sensor2.setRotationPoint(2.0F, 3.0F, 0.5F);
        this.Sensor2.addBox(-0.1F, 0.0F, 0.0F, 1, 4, 0, 0.0F);
        this.setRotateAngle(Sensor2, 0.7330382858376184F, 1.5707963267948966F, 0.0F);
        this.Stinger = new EndimatorRendererModel(this, 15, 1);
        this.Stinger.setRotationPoint(-0.5F, -3.0F, 0.0F);
        this.Stinger.addBox(0.0F, -4.0F, 0.0F, 1, 4, 0, 0.0F);
        this.Neck = new EndimatorRendererModel(this, 0, 6);
        this.Neck.setRotationPoint(0.0F, 3.0F, 0.0F);
        this.Neck.addBox(-1.0F, 0.0F, -1.0F, 2, 4, 2, 0.0F);
        this.setRotateAngle(Neck, -0.13962634015954636F, 0.0F, 0.0F);
        this.Head.addChild(this.Sensor1);
        this.Neck.addChild(this.Head);
        this.Head.addChild(this.Sensor2);
        this.Body.addChild(this.Stinger);
        this.Body.addChild(this.Neck);
        
        this.setDefaultBoxValues();
    }

    @Override
    public void render(E entity, float f, float f1, float f2, float f3, float f4, float f5) {
    	this.animateModel(entity, f, f1, f2, f3, f4, f5);
        this.Body.render(f5);
    }
    
    @Override
    public void setRotationAngles(E entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
    	this.revertBoxesToDefaultValues();
    	
    	this.Sensor1.rotateAngleZ += 0.1F * MathHelper.sin(0.25F * ageInTicks);
    	this.Sensor2.rotateAngleX += 0.1F * MathHelper.sin(0.25F * ageInTicks);
    }

    public void setRotateAngle(EndimatorRendererModel EndimatorRendererModel, float x, float y, float z) {
        EndimatorRendererModel.rotateAngleX = x;
        EndimatorRendererModel.rotateAngleY = y;
        EndimatorRendererModel.rotateAngleZ = z;
    }
}