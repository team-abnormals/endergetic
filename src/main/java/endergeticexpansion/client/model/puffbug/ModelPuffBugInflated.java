package endergeticexpansion.client.model.puffbug;

import endergeticexpansion.api.EndergeticAPI.ClientInfo;
import endergeticexpansion.api.endimator.EndimatorEntityModel;
import endergeticexpansion.api.endimator.EndimatorRendererModel;
import endergeticexpansion.common.entities.puffbug.EntityPuffBug;
import net.minecraft.util.math.MathHelper;

/**
 * ModelPuffBugInflated - Endergized
 * Created using Tabula 7.0.0
 */
public class ModelPuffBugInflated<E extends EntityPuffBug> extends EndimatorEntityModel<E> {
    public EndimatorRendererModel Body;
    public EndimatorRendererModel Stinger;
    public EndimatorRendererModel Neck;
    public EndimatorRendererModel Head;
    public EndimatorRendererModel Sensor1;
    public EndimatorRendererModel Sensor2;

    public ModelPuffBugInflated() {
        this.textureWidth = 32;
        this.textureHeight = 16;
        this.Head = new EndimatorRendererModel(this, 0, 0);
        this.Head.setRotationPoint(0.0F, 3.7F, 0.0F);
        this.Head.addBox(-2.0F, 0.0F, -1.5F, 4, 3, 3, 0.0F);
        this.setRotateAngle(Head, -0.136659280431156F, 0.0F, 0.0F);
        this.Stinger = new EndimatorRendererModel(this, 26, 0);
        this.Stinger.setRotationPoint(-0.5F, -3.5F, 0.0F);
        this.Stinger.addBox(0.0F, -4.0F, 0.0F, 1, 4, 0, 0.0F);
        this.Body = new EndimatorRendererModel(this, 8, 3);
        this.Body.setRotationPoint(0.0F, 12.0F, 0.0F);
        this.Body.addBox(-3.0F, -3.5F, -3.0F, 6, 7, 6, 0.0F);
        this.setRotateAngle(Body, 0.0F, 0.045553093477052F, 0.0F);
        this.Sensor1 = new EndimatorRendererModel(this, 30, 0);
        this.Sensor1.setRotationPoint(-2.0F, 3.0F, 0.5F);
        this.Sensor1.addBox(0.0F, 0.0F, 0.0F, 1, 4, 0, 0.0F);
        this.setRotateAngle(Sensor1, 0.0F, 1.5707963267948966F, 0.7330382858376184F);
        this.Neck = new EndimatorRendererModel(this, 0, 6);
        this.Neck.setRotationPoint(0.0F, 2.5F, 0.0F);
        this.Neck.addBox(-1.0F, 0.0F, -1.0F, 2, 4, 2, 0.0F);
        this.setRotateAngle(Neck, -0.136659280431156F, 0.0F, 0.0F);
        this.Sensor2 = new EndimatorRendererModel(this, 30, 0);
        this.Sensor2.setRotationPoint(2.0F, 3.0F, 0.5F);
        this.Sensor2.addBox(0.0F, 0.0F, 0.0F, 1, 4, 0, 0.0F);
        this.setRotateAngle(Sensor2, 0.7330382858376184F, 1.5707963267948966F, 0.0F);
        this.Neck.addChild(this.Head);
        this.Body.addChild(this.Stinger);
        this.Head.addChild(this.Sensor1);
        this.Body.addChild(this.Neck);
        this.Head.addChild(this.Sensor2);
        
        this.createScaleController();
        
        this.setDefaultBoxValues();
    }

    @Override
    public void render(E entity, float f, float f1, float f2, float f3, float f4, float f5) {
    	this.animateModel(entity, f, f1, f2, f3, f4, f5);
        this.Body.render(f5);
    }
    
    @Override
    public void setRotationAngles(E puffBug, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
    	this.Body.setShouldScaleChildren(false);
    	
    	this.revertBoxesToDefaultValues();
    	
    	if(!puffBug.isEndimationPlaying(EntityPuffBug.PUFF_ANIMATION) && !puffBug.isEndimationPlaying(EntityPuffBug.POLLINATE_ANIMATION)) {
    		this.Sensor1.rotateAngleZ += 0.1F * MathHelper.sin(0.25F * ageInTicks);
    		this.Sensor2.rotateAngleX += 0.1F * MathHelper.sin(0.25F * ageInTicks);
    	}
    	
    	this.Head.rotateAngleX += 0.075F * MathHelper.sin(0.1F * ageInTicks);
    	
    	this.Body.rotateAngleY = puffBug.getRotationController().getRotations(ClientInfo.getPartialTicks())[0] * (float) (Math.PI / 180F);
    	this.Body.rotateAngleX = puffBug.getRotationController().getRotations(ClientInfo.getPartialTicks())[1] * (float) (Math.PI / 180F);
    }
    
    @Override
    public void animateModel(E puffbug, float f, float f1, float f2, float f3, float f4, float f5) {
    	super.animateModel(puffbug, f, f1, f2, f3, f4, f5);
    	
    	this.endimator.updateAnimations(puffbug);
    	
    	if(puffbug.isEndimationPlaying(EntityPuffBug.CLAIM_HIVE_ANIMATION)) {
    		this.endimator.setAnimationToPlay(EntityPuffBug.CLAIM_HIVE_ANIMATION);
    		
    		this.endimator.startKeyframe(5);
    		this.endimator.rotate(this.Sensor1, 0.0F, 0.0F, 0.45F);
    		this.endimator.rotate(this.Sensor2, 0.45F, 0.0F, 0.0F);
    		this.endimator.endKeyframe();
    		
    		this.endimator.resetKeyframe(5);
    		
    		this.endimator.startKeyframe(5);
    		this.endimator.rotate(this.Sensor1, 0.0F, 0.0F, 0.45F);
    		this.endimator.rotate(this.Sensor2, 0.45F, 0.0F, 0.0F);
    		this.endimator.endKeyframe();
    		
    		this.endimator.resetKeyframe(5);
    	} else if(puffbug.isEndimationPlaying(EntityPuffBug.PUFF_ANIMATION)) {
    		this.endimator.setAnimationToPlay(EntityPuffBug.PUFF_ANIMATION);
    		
    		this.endimator.startKeyframe(10);
    		this.endimator.rotate(this.Neck, 0.4F, 0.0F, 0.0F);
    		this.endimator.rotate(this.Head, 0.55F, 0.0F, 0.0F);
    		
    		this.endimator.rotate(this.Sensor1, 0.0F, 0.0F, -0.5F);
    		this.endimator.rotate(this.Sensor2, -0.5F, 0.0F, 0.0F);
    		
    		this.endimator.rotate(this.Body, 0.25F, 0.0F, 0.0F);
    		
    		this.endimator.move(this.getScaleController(), 0.4F, 0.4F, 0.4F);
    		this.endimator.endKeyframe();
    		
    		this.endimator.resetKeyframe(10);
    	} else if(puffbug.isEndimationPlaying(EntityPuffBug.TELEPORT_TO_ANIMATION)) {
    		this.Body.setShouldScaleChildren(true);
    		
    		this.endimator.setAnimationToPlay(EntityPuffBug.TELEPORT_TO_ANIMATION);
    		
    		this.endimator.startKeyframe(5);
    		this.endimator.move(this.getScaleController(), 1.3F, 1.3F, 1.3F);
    		this.endimator.endKeyframe();
    		
    		this.endimator.startKeyframe(3);
    		this.endimator.move(this.getScaleController(), -1.0F, -1.0F, -1.0F);
    		this.endimator.endKeyframe();
    		
    		this.endimator.setStaticKeyframe(7);
    	} else if(puffbug.isEndimationPlaying(EntityPuffBug.TELEPORT_FROM_ANIMATION)) {
    		this.Body.setShouldScaleChildren(true);
    		
    		this.endimator.setAnimationToPlay(EntityPuffBug.TELEPORT_FROM_ANIMATION);
    		
    		this.endimator.startKeyframe(5);
    		this.endimator.move(this.getScaleController(), 1.3F, 1.3F, 1.3F);
    		this.endimator.endKeyframe();
    		
    		this.endimator.resetKeyframe(5);
    	} else if(puffbug.isEndimationPlaying(EntityPuffBug.ROTATE_ANIMATION)) {
    		this.endimator.setAnimationToPlay(EntityPuffBug.ROTATE_ANIMATION);
    		
    		this.endimator.startKeyframe(10);
    		this.endimator.rotate(this.Head, -0.5F, 0.0F, 0.0F);
    		this.endimator.rotate(this.Neck, -0.5F, 0.0F, 0.0F);
    		this.endimator.rotate(this.Stinger, 0.4F, 0.0F, 0.0F);
    		this.endimator.endKeyframe();
    		
    		this.endimator.resetKeyframe(10);
    	} else if(puffbug.isEndimationPlaying(EntityPuffBug.POLLINATE_ANIMATION)) {
    		this.endimator.setAnimationToPlay(EntityPuffBug.POLLINATE_ANIMATION);
    		
    		this.endimator.startKeyframe(5);
    		this.endimator.rotate(this.Neck, 0.0F, 0.0F, 0.17F);
    		this.endimator.rotate(this.Head, 0.0F, 0.0F, 0.35F);
    		this.endimator.rotate(this.Sensor1, 0.0F, 0.0F, -0.65F);
    		this.endimator.rotate(this.Sensor2, 0.0F, 0.0F, 0.5F);
    		this.endimator.endKeyframe();
    		
    		this.endimator.resetKeyframe(5);
    		
    		this.endimator.startKeyframe(5);
    		this.endimator.rotate(this.Neck, 0.0F, 0.0F, -0.17F);
    		this.endimator.rotate(this.Head, 0.0F, 0.0F, -0.35F);
    		this.endimator.rotate(this.Sensor1, 0.0F, 0.0F, -0.65F);
    		this.endimator.rotate(this.Sensor2, 0.0F, 0.0F, 0.5F);
    		this.endimator.endKeyframe();
    		
    		this.endimator.resetKeyframe(5);
    		
    		this.endimator.startKeyframe(5);
    		this.endimator.rotate(this.Neck, 0.0F, 0.0F, 0.17F);
    		this.endimator.rotate(this.Head, 0.0F, 0.0F, 0.35F);
    		this.endimator.rotate(this.Sensor1, 0.0F, 0.0F, -0.65F);
    		this.endimator.rotate(this.Sensor2, 0.0F, 0.0F, 0.5F);
    		this.endimator.endKeyframe();
    		
    		this.endimator.resetKeyframe(5);
    		
    		this.endimator.startKeyframe(5);
    		this.endimator.rotate(this.Neck, 0.0F, 0.0F, -0.17F);
    		this.endimator.rotate(this.Head, 0.0F, 0.0F, -0.35F);
    		this.endimator.rotate(this.Sensor1, 0.0F, 0.0F, -0.65F);
    		this.endimator.rotate(this.Sensor2, 0.0F, 0.0F, 0.5F);
    		this.endimator.endKeyframe();
    		
    		this.endimator.resetKeyframe(5);
    		
    		this.endimator.startKeyframe(5);
    		this.endimator.rotate(this.Neck, 0.0F, 0.0F, 0.17F);
    		this.endimator.rotate(this.Head, 0.0F, 0.0F, 0.35F);
    		this.endimator.rotate(this.Sensor1, 0.0F, 0.0F, -0.65F);
    		this.endimator.rotate(this.Sensor2, 0.0F, 0.0F, 0.5F);
    		this.endimator.endKeyframe();
    		
    		this.endimator.resetKeyframe(5);
    		
    		this.endimator.startKeyframe(5);
    		this.endimator.rotate(this.Neck, 0.0F, 0.0F, -0.17F);
    		this.endimator.rotate(this.Head, 0.0F, 0.0F, -0.35F);
    		this.endimator.rotate(this.Sensor1, 0.0F, 0.0F, -0.65F);
    		this.endimator.rotate(this.Sensor2, 0.0F, 0.0F, 0.5F);
    		this.endimator.endKeyframe();
    		
    		this.endimator.resetKeyframe(5);
    		
    		this.endimator.startKeyframe(5);
    		this.endimator.rotate(this.Neck, 0.0F, 0.0F, 0.17F);
    		this.endimator.rotate(this.Head, 0.0F, 0.0F, 0.35F);
    		this.endimator.rotate(this.Sensor1, 0.0F, 0.0F, -0.65F);
    		this.endimator.rotate(this.Sensor2, 0.0F, 0.0F, 0.5F);
    		this.endimator.endKeyframe();
    		
    		this.endimator.resetKeyframe(5);
    		
    		this.endimator.startKeyframe(5);
    		this.endimator.rotate(this.Neck, 0.0F, 0.0F, -0.17F);
    		this.endimator.rotate(this.Head, 0.0F, 0.0F, -0.35F);
    		this.endimator.rotate(this.Sensor1, 0.0F, 0.0F, -0.65F);
    		this.endimator.rotate(this.Sensor2, 0.0F, 0.0F, 0.5F);
    		this.endimator.endKeyframe();
    		
    		this.endimator.resetKeyframe(5);
    		
    		this.endimator.startKeyframe(5);
    		this.endimator.rotate(this.Neck, 0.0F, 0.0F, 0.17F);
    		this.endimator.rotate(this.Head, 0.0F, 0.0F, 0.35F);
    		this.endimator.rotate(this.Sensor1, 0.0F, 0.0F, -0.65F);
    		this.endimator.rotate(this.Sensor2, 0.0F, 0.0F, 0.5F);
    		this.endimator.endKeyframe();
    		
    		this.endimator.resetKeyframe(5);
    		
    		this.endimator.startKeyframe(5);
    		this.endimator.rotate(this.Neck, 0.0F, 0.0F, -0.17F);
    		this.endimator.rotate(this.Head, 0.0F, 0.0F, -0.35F);
    		this.endimator.rotate(this.Sensor1, 0.0F, 0.0F, -0.65F);
    		this.endimator.rotate(this.Sensor2, 0.0F, 0.0F, 0.5F);
    		this.endimator.endKeyframe();
    		
    		this.endimator.resetKeyframe(5);
    		
    		this.endimator.startKeyframe(10);
    		this.endimator.move(this.getScaleController(), 0.4F, 0.4F, 0.4F);
    		this.endimator.endKeyframe();
    		
    		this.endimator.resetKeyframe(10);
    	}
    	
    	this.Body.setScale(this.getScaleController().rotationPointX, this.getScaleController().rotationPointY, this.getScaleController().rotationPointZ);
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