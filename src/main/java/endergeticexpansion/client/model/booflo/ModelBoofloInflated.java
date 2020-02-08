package endergeticexpansion.client.model.booflo;

import endergeticexpansion.api.endimator.EndimatorEntityModel;
import endergeticexpansion.api.endimator.EndimatorRendererModel;
import endergeticexpansion.common.entities.booflo.EntityBooflo;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * BoofloDefault - Endergized
 * Created using Tabula 7.0.0
 */
@OnlyIn(Dist.CLIENT)
public class ModelBoofloInflated<E extends EntityBooflo> extends EndimatorEntityModel<E> {
    public EndimatorRendererModel Head;
    public EndimatorRendererModel KneeLeft;
    public EndimatorRendererModel KneeRight;
    public EndimatorRendererModel LegLeft;
    public EndimatorRendererModel LegRight;
    public EndimatorRendererModel Jaw;
    public EndimatorRendererModel LegBackLeft;
    public EndimatorRendererModel LegBackRight;

    public ModelBoofloInflated() {
        this.textureWidth = 150;
        this.textureHeight = 150;
        this.LegRight = new EndimatorRendererModel(this, 28, 66);
        this.LegRight.setRotationPoint(-8.5F, 2.0F, -3.0F);
        this.LegRight.addBox(0.0F, -1.5F, -1.5F, 20, 3, 3, 0.0F);
        this.setRotateAngle(LegRight, 0.0F, 0.5235987755982988F, 3.141592653589794F);
        this.LegBackLeft = new EndimatorRendererModel(this, 0, 81);
        this.LegBackLeft.setRotationPoint(1.5F, -7.5F, -0.5F);
        this.LegBackLeft.addBox(0.0F, -1.5F, -1.5F, 25, 3, 3, 0.0F);
        this.setRotateAngle(LegBackLeft, 0.0F, -0.3490658503988659F, 0.3490658503988658F);
        this.Jaw = new EndimatorRendererModel(this, 0, 97);
        this.Jaw.setRotationPoint(0.0F, 4.0F, 8.0F);
        this.Jaw.addBox(-16.0F, 0.0F, -24.0F, 32, 16, 32, 0.0F);
        this.KneeRight = new EndimatorRendererModel(this, 0, 58);
        this.KneeRight.setRotationPoint(-5.5F, -2.0F, 5.5F);
        this.KneeRight.addBox(-2.5F, -10.0F, -2.5F, 5, 10, 5, 0.0F);
        this.setRotateAngle(KneeRight, 0.0F, 0.17453292519943295F, -0.5585053606381855F);
        this.LegLeft = new EndimatorRendererModel(this, 28, 59);
        this.LegLeft.setRotationPoint(8.5F, 2.0F, -3.0F);
        this.LegLeft.addBox(0.0F, -1.5F, -1.5F, 20, 3, 3, 0.0F);
        this.setRotateAngle(LegLeft, 0.0F, 0.5235987755982988F, -2.4802620430283604E-16F);
        this.Head = new EndimatorRendererModel(this, 0, 29);
        this.Head.setRotationPoint(0.0F, 3.9999999999999947F, 0.0F);
        this.Head.addBox(-9.0F, -4.0F, -9.0F, 18, 8, 18, 0.0F);
        this.setRotateAngle(Head, 3.1003275537854505E-17F, 0.0F, 0.0F);
        this.KneeLeft = new EndimatorRendererModel(this, 0, 58);
        this.KneeLeft.setRotationPoint(5.5F, -2.0F, 5.5F);
        this.KneeLeft.addBox(-2.5F, -10.0F, -2.5F, 5, 10, 5, 0.0F);
        this.setRotateAngle(KneeLeft, 0.0F, -0.17453292519943295F, 0.558505360638178F);
        this.LegBackRight = new EndimatorRendererModel(this, 0, 89);
        this.LegBackRight.setRotationPoint(-1.5F, -7.5F, 0.0F);
        this.LegBackRight.addBox(-25.0F, -1.5F, -1.5F, 25, 3, 3, 0.0F);
        this.setRotateAngle(LegBackRight, 0.0F, 0.3490658503988659F, -0.3490658503988659F);
        this.Head.addChild(this.LegRight);
        this.KneeLeft.addChild(this.LegBackLeft);
        this.Head.addChild(this.Jaw);
        this.Head.addChild(this.KneeRight);
        this.Head.addChild(this.LegLeft);
        this.Head.addChild(this.KneeLeft);
        this.KneeRight.addChild(this.LegBackRight);
        
        this.createScaleController();
        
        this.setDefaultBoxValues();
    }

    @Override
    public void render(E booflo, float f, float f1, float f2, float f3, float f4, float f5) {
    	this.animateModel(booflo, f, f1, f2, f3, f4, f5);
        this.Head.render(f5);
    }
    
    @Override
    public void setRotationAngles(E booflo, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
    	this.revertBoxesToDefaultValues();
    	
    	this.Head.rotateAngleY = netHeadYaw * (float) (Math.PI / 180F);
    	this.Head.rotateAngleX = headPitch * (float) (Math.PI / 180F);
    	
    	this.Jaw.rotateAngleX += 0.4F * booflo.OPEN_JAW.getAnimationProgress();
    }
    
    
    @Override
    public void animateModel(E booflo, float f, float f1, float f2, float f3, float f4, float f5) {
    	super.animateModel(booflo, f, f1, f2, f3, f4, f5);
    	
    	this.endimator.updateAnimations(booflo);
    	
    	if(booflo.isEndimationPlaying(EntityBooflo.INFLATE)) {
    		this.endimator.setAnimationToPlay(EntityBooflo.INFLATE);
    		
    		this.endimator.startKeyframe(5);
    		this.endimator.move(this.getScaleController(), 0.7F, 0.0F, 0.7F);
    		
    		this.endimator.rotate(this.LegRight, 0.0F, 0.0F, 0.6F);
    		this.endimator.rotate(this.LegLeft, 0.0F, 0.0F, -0.6F);
    		
    		this.endimator.rotate(this.KneeRight, 0.0F, 0.0F, 0.4F);
    		this.endimator.rotate(this.LegBackRight, 0.0F, 0.0F, -0.4F);
    		
    		this.endimator.rotate(this.KneeRight, 0.0F, 0.0F, 0.2F);
    		this.endimator.rotate(this.KneeLeft, 0.0F, 0.0F, -0.2F);
    		this.endimator.endKeyframe();
    		
    		this.endimator.startKeyframe(5);
    		this.endimator.move(this.getScaleController(), 0.0F, 0.0F, 0.0F);
    		this.endimator.endKeyframe();
    	} else if(booflo.isEndimationPlaying(EntityBooflo.SWIM)) {
    		this.endimator.setAnimationToPlay(EntityBooflo.SWIM);
    		
    		this.endimator.startKeyframe(10);
    		this.endimator.rotate(this.LegRight, 0.0F, -1.6F, 0.0F);
    		this.endimator.rotate(this.LegLeft, 0.0F, -1.6F, 0.0F);
    		
    		this.endimator.rotate(this.KneeRight, -0.895F, 0.0F, 0.0F);
    		this.endimator.rotate(this.KneeLeft, -0.895F, 0.0F, 0.0F);
    		
    		this.endimator.rotate(this.LegBackRight, 0.8F, 0.8F, 1.6F);
    		this.endimator.rotate(this.LegBackLeft, 0.8F, -0.8F, -1.6F);
    		
    		this.endimator.rotate(this.Jaw, 0.2F, 0.0F, 0.0F);
    		
    		this.endimator.rotate(this.Head, -0.105F, 0.0F, 0.0F);
    		this.endimator.move(this.Head, 0.0F, -0.3F, 0.0F);
    		this.endimator.endKeyframe();
    		
    		/*
    		 * Returns to defaults
    		 */
    		this.endimator.startKeyframe(10);
    		this.endimator.endKeyframe();
    	} else if(booflo.isEndimationPlaying(EntityBooflo.CHARGE)) {
    		this.endimator.setAnimationToPlay(EntityBooflo.CHARGE);
    		
    		this.endimator.startKeyframe(15);
    		this.endimator.rotate(this.LegRight, 0.0F, 0.0F, 0.5F);
    		this.endimator.rotate(this.LegLeft, 0.0F, 0.0F, -0.5F);
    		
    		this.endimator.rotate(this.KneeRight, 0.0F, 0.0F, 0.4F);
    		this.endimator.rotate(this.KneeLeft, 0.0F, 0.0F, -0.4F);
    		
    		this.endimator.rotate(this.LegBackRight, 0.0F, 0.0F, 0.5F);
    		this.endimator.rotate(this.LegBackLeft, 0.0F, 0.0F, -0.5F);
    		this.endimator.endKeyframe();
    		
    		this.endimator.setStaticKeyframe(60);
    	} else if(booflo.isEndimationPlaying(EntityBooflo.SLAM)) {
    		this.endimator.setAnimationToPlay(EntityBooflo.SLAM);
    		
    		this.endimator.startKeyframe(4);
    		this.endimator.move(this.Head, 0.0F, 9.9F, 0.0F);
    		this.endimator.move(this.getScaleController(), -0.2F, -0.5F, -0.2F);
    		
    		this.endimator.rotate(this.LegRight, 0.0F, 0.0F, -0.5F);
    		this.endimator.rotate(this.LegLeft, 0.0F, 0.0F, 0.5F);
    		
    		this.endimator.rotate(this.KneeRight, 0.0F, 0.0F, -0.5F);
    		this.endimator.rotate(this.KneeLeft, 0.0F, 0.0F, 0.5F);
    		
    		this.endimator.rotate(this.LegBackRight, 0.0F, 0.0F, -0.2F);
    		this.endimator.rotate(this.LegBackLeft, 0.0F, 0.0F, 0.2F);
    		this.endimator.endKeyframe();
    		
    		this.endimator.startKeyframe(3);
    		this.endimator.move(this.Head, 0.0F, 1.9F, 0.0F);
    		this.endimator.move(this.getScaleController(), 0.8F, 1.5F, 0.8F);
    		
    		this.endimator.rotate(this.LegRight, 0.0F, 0.0F, 0.5F);
    		this.endimator.rotate(this.LegLeft, 0.0F, 0.0F, -0.5F);
    		
    		this.endimator.rotate(this.KneeRight, 0.0F, 0.0F, 0.7F);
    		this.endimator.rotate(this.KneeLeft, 0.0F, 0.0F, -0.7F);
    		
    		this.endimator.rotate(this.LegBackRight, 0.0F, 0.0F, 0.1F);
    		this.endimator.rotate(this.LegBackLeft, 0.0F, 0.0F, -0.1F);
    		this.endimator.endKeyframe();
    		
    		this.endimator.resetKeyframe(3);
    	}
    	
    	this.Head.setScale(this.getScaleController().rotationPointX, this.getScaleController().rotationPointY, this.getScaleController().rotationPointZ);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(EndimatorRendererModel EndimatorRendererModel, float x, float y, float z) {
        EndimatorRendererModel.rotateAngleX = x;
        EndimatorRendererModel.rotateAngleY = y;
        EndimatorRendererModel.rotateAngleZ = z;
    }
}