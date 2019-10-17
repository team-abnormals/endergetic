package endergeticexpansion.client.model.booflo;

import endergeticexpansion.api.EndergeticAPI.ClientInfo;
import endergeticexpansion.api.endimator.Endimator;
import endergeticexpansion.api.endimator.EndimatorEntityModel;
import endergeticexpansion.api.endimator.EndimatorRendererModel;
import endergeticexpansion.common.entities.booflo.EntityBoofloAdolescent;
import net.minecraft.util.math.MathHelper;

/**
 * ModelAdolescentBooflo - Endergized
 * Created using Tabula 7.0.0
 */
public class ModelAdolescentBooflo<E extends EntityBoofloAdolescent> extends EndimatorEntityModel<E> {
    public EndimatorRendererModel Head;
    public EndimatorRendererModel KneeLeft;
    public EndimatorRendererModel KneeRight;
    public EndimatorRendererModel ArmLeft;
    public EndimatorRendererModel ArmRight;
    public EndimatorRendererModel Tail;
    public EndimatorRendererModel Jaw;
    
    public Endimator endimator = new Endimator();

    public ModelAdolescentBooflo() {
        this.textureWidth = 64;
        this.textureHeight = 48;
        this.Tail = new EndimatorRendererModel(this, 32, 16);
        this.Tail.setRotationPoint(0.0F, -5.0F, 5.0F);
        this.Tail.addBox(0.0F, 0.0F, 0.0F, 0, 5, 7, 0.0F);
        this.KneeLeft = new EndimatorRendererModel(this, 0, 24);
        this.KneeLeft.setRotationPoint(2.5F, -4.5F, 2.5F);
        this.KneeLeft.addBox(-1.5F, -5.0F, -1.5F, 3, 5, 3, 0.0F);
        this.setRotateAngle(KneeLeft, 0.0F, 0.0F, 0.3490658503988659F);
        this.ArmLeft = new EndimatorRendererModel(this, 0, 16);
        this.ArmLeft.setRotationPoint(4.5F, -2.0F, -2.0F);
        this.ArmLeft.addBox(0.0F, -1.0F, -1.0F, 4, 2, 2, 0.0F);
        this.setRotateAngle(ArmLeft, 0.0F, 0.20944F, 0.3490658503988659F);
        this.ArmRight = new EndimatorRendererModel(this, 14, 16);
        this.ArmRight.setRotationPoint(-4.5F, -2.0F, -2.0F);
        this.ArmRight.addBox(-4.0F, -1.0F, -1.0F, 4, 2, 2, 0.0F);
        this.setRotateAngle(ArmRight, 0.0F, -0.20944F, -0.3490658503988659F);
        this.KneeRight = new EndimatorRendererModel(this, 14, 24);
        this.KneeRight.setRotationPoint(-2.5F, -4.5F, 2.5F);
        this.KneeRight.addBox(-1.5F, -5.0F, -1.5F, 3, 5, 3, 0.0F);
        this.setRotateAngle(KneeRight, 0.0F, 0.0F, -0.3490658503988659F);
        this.Jaw = new EndimatorRendererModel(this, 16, 28);
        this.Jaw.setRotationPoint(0.0F, 0.0F, 5.0F);
        this.Jaw.addBox(-6.0F, 0.0F, -11.0F, 12, 6, 12, 0.0F);
        this.Head = new EndimatorRendererModel(this, 0, 0);
        this.Head.setRotationPoint(0.0F, 18.0F, 0.0F);
        this.Head.addBox(-5.0F, -5.0F, -5.0F, 10, 5, 10, 0.0F);
        this.Head.addChild(this.Tail);
        this.Head.addChild(this.KneeLeft);
        this.Head.addChild(this.ArmLeft);
        this.Head.addChild(this.ArmRight);
        this.Head.addChild(this.KneeRight);
        this.Head.addChild(this.Jaw);
        
        this.createScaleController();
        
        this.setDefaultBoxValues();
    }

    @Override
    public void render(E entity, float f, float f1, float f2, float f3, float f4, float f5) {
    	this.animateModel(entity, f, f1, f2, f3, f4, f5);
    	this.Head.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(EndimatorRendererModel EndimatorRendererModel, float x, float y, float z) {
        EndimatorRendererModel.rotateAngleX = x;
        EndimatorRendererModel.rotateAngleY = y;
        EndimatorRendererModel.rotateAngleZ = z;
    }
    
    @Override
    public void setRotationAngles(E entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
    	this.revertBoxesToDefaultValues();
    	
    	this.Tail.rotateAngleY = 0.1F * MathHelper.sin(entityIn.getTailAnimation(0.3F * ClientInfo.getPartialTicks())) * (float) Math.PI;
    	
    	if(!entityIn.isAnimationPlaying(EntityBoofloAdolescent.BOOF_ANIMATION) && !entityIn.isInWater()) {
    		this.Head.rotationPointY += 0.5F * MathHelper.sin(0.4F * ageInTicks);
    		this.KneeLeft.rotateAngleZ += 0.1F * -MathHelper.sin(0.6F * entityIn.getSwimmingAnimation(ClientInfo.getPartialTicks()));
    		this.KneeRight.rotateAngleZ += 0.1F * MathHelper.sin(0.6F * entityIn.getSwimmingAnimation(ClientInfo.getPartialTicks()));
    		this.ArmLeft.rotateAngleZ += 0.3F * -MathHelper.sin(0.6F * entityIn.getSwimmingAnimation(ClientInfo.getPartialTicks())) - 0.17F;
    		this.ArmRight.rotateAngleZ += 0.3F * MathHelper.sin(0.6F * entityIn.getSwimmingAnimation(ClientInfo.getPartialTicks())) + 0.17F;
    	} else if(!entityIn.isAnimationPlaying(EntityBoofloAdolescent.BOOF_ANIMATION) && entityIn.isInWater()) {
    		this.Head.rotationPointY += 0.5F * MathHelper.sin(0.4F * ageInTicks);
    		this.KneeLeft.rotateAngleZ += 0.1F * -MathHelper.sin(0.6F * ageInTicks);
    		this.KneeRight.rotateAngleZ += 0.1F * MathHelper.sin(0.6F * ageInTicks);
    		this.ArmLeft.rotateAngleZ += 0.3F * -MathHelper.sin(0.6F * ageInTicks) - 0.17F;
    		this.ArmRight.rotateAngleZ += 0.3F * MathHelper.sin(0.6F * ageInTicks) + 0.17F;
    	}
    }
    
    @Override
    public void animateModel(E booflo, float f, float f1, float f2, float f3, float f4, float f5) {
    	super.animateModel(booflo, f, f1, f2, f3, f4, f5);
    	this.endimator.updateAnimations(booflo);
    	
    	if(booflo.isAnimationPlaying(EntityBoofloAdolescent.BOOF_ANIMATION)) {
    		this.endimator.setAnimationToPlay(EntityBoofloAdolescent.BOOF_ANIMATION);
    		this.endimator.startKeyframe(3);
    		this.endimator.move(this.getScaleController(), 0.5F, -0.2F, 0.5F);
    		this.endimator.move(Head, 0.0F, -0.2F, 0.0F);
    		this.endimator.rotate(ArmLeft, 0.0F, 0.0F, -0.4F);
    		this.endimator.rotate(ArmRight, 0.0F, 0.0F, 0.4F);
    		this.endimator.rotate(KneeLeft, 0.0F, 0.0F, 0.2F);
    		this.endimator.rotate(KneeRight, 0.0F, 0.0F, -0.2F);
    		this.endimator.endKeyframe();
    		this.endimator.setStaticKeyframe(3);
    		this.endimator.startKeyframe(4);
    		this.endimator.move(this.getScaleController(), -0.0F, 0.0F, -0.0F);
    		this.endimator.move(Head, 0.0F, 0F, 0.0F);
    		this.endimator.rotate(ArmLeft, 0.0F, 0.0F, 0.6F);
    		this.endimator.rotate(ArmRight, 0.0F, 0.0F, -0.6F);
    		this.endimator.rotate(KneeLeft, 0.0F, 0.0F, -0.35F);
    		this.endimator.rotate(KneeRight, 0.0F, 0.0F, 0.35F);
    		this.endimator.endKeyframe();
    		this.endimator.setStaticKeyframe(4);
    		this.endimator.startKeyframe(4);
    		this.endimator.rotate(ArmLeft, 0.0F, 0.0F, -0.2F);
    		this.endimator.rotate(ArmRight, 0.0F, 0.0F, 0.2F);
    		this.endimator.rotate(KneeLeft, 0.0F, 0.0F, 0.15F);
    		this.endimator.rotate(KneeRight, 0.0F, 0.0F, -0.15F);
    		this.endimator.endKeyframe();
    	}
    	
    	this.Head.setScale(this.getScaleController().rotationPointX, this.getScaleController().rotationPointY, this.getScaleController().rotationPointZ);
    }
}
