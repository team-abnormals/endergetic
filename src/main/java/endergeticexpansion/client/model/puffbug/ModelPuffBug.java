package endergeticexpansion.client.model.puffbug;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import endergeticexpansion.api.EndergeticAPI.ClientInfo;
import endergeticexpansion.api.endimator.EndimatorEntityModel;
import endergeticexpansion.api.endimator.EndimatorModelRenderer;
import endergeticexpansion.common.entities.booflo.EntityBooflo;
import endergeticexpansion.common.entities.puffbug.EntityPuffBug;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

/**
 * ModelPuffBugInflated - Endergized
 * Created using Tabula 7.0.0
 */
public class ModelPuffBug<E extends EntityPuffBug> extends EndimatorEntityModel<E> {
    public EndimatorModelRenderer Body;
    public EndimatorModelRenderer Stinger;
    public EndimatorModelRenderer Neck;
    public EndimatorModelRenderer Head;
    public EndimatorModelRenderer Sensor1;
    public EndimatorModelRenderer Sensor2;
    
    public EndimatorModelRenderer BodyDeflated;
    public EndimatorModelRenderer StingerDeflated;
    public EndimatorModelRenderer NeckDeflated;
    public EndimatorModelRenderer HeadDeflated;
    public EndimatorModelRenderer Sensor1Deflated;
    public EndimatorModelRenderer Sensor2Deflated;
    
    public EndimatorModelRenderer BodyDeflatedProjectile;
    public EndimatorModelRenderer StingerDeflatedProjectile;
    public EndimatorModelRenderer NeckDeflatedProjectile;
    public EndimatorModelRenderer HeadDeflatedProjectile;
    public EndimatorModelRenderer Sensor1DeflatedProjectile;
    public EndimatorModelRenderer Sensor2DeflatedProjectile;

    public ModelPuffBug() {
        this.textureWidth = 32;
        this.textureHeight = 16;
        this.Head = new EndimatorModelRenderer(this, 0, 0);
        this.Head.setRotationPoint(0.0F, 3.7F, 0.0F);
        this.Head.addBox(-2.0F, 0.0F, -1.5F, 4, 3, 3, 0.0F);
        this.setRotateAngle(Head, -0.136659280431156F, 0.0F, 0.0F);
        this.Stinger = new EndimatorModelRenderer(this, 26, 0);
        this.Stinger.setRotationPoint(-0.5F, -3.5F, 0.0F);
        this.Stinger.addBox(0.0F, -4.0F, 0.0F, 1, 4, 0, 0.0F);
        this.Body = new EndimatorModelRenderer(this, 8, 3);
        this.Body.setRotationPoint(0.0F, 12.0F, 0.0F);
        this.Body.addBox(-3.0F, -3.5F, -3.0F, 6, 7, 6, 0.0F);
        this.setRotateAngle(Body, 0.0F, 0.045553093477052F, 0.0F);
        this.Sensor1 = new EndimatorModelRenderer(this, 30, 0);
        this.Sensor1.setRotationPoint(-2.0F, 3.0F, 0.5F);
        this.Sensor1.addBox(0.0F, 0.0F, 0.0F, 1, 4, 0, 0.0F);
        this.setRotateAngle(Sensor1, 0.0F, 1.5707963267948966F, 0.7330382858376184F);
        this.Neck = new EndimatorModelRenderer(this, 0, 6);
        this.Neck.setRotationPoint(0.0F, 2.5F, 0.0F);
        this.Neck.addBox(-1.0F, 0.0F, -1.0F, 2, 4, 2, 0.0F);
        this.setRotateAngle(Neck, -0.136659280431156F, 0.0F, 0.0F);
        this.Sensor2 = new EndimatorModelRenderer(this, 30, 0);
        this.Sensor2.setRotationPoint(2.0F, 3.0F, 0.5F);
        this.Sensor2.addBox(0.0F, 0.0F, 0.0F, 1, 4, 0, 0.0F);
        this.setRotateAngle(Sensor2, 0.7330382858376184F, 1.5707963267948966F, 0.0F);
        this.Neck.addChild(this.Head);
        this.Body.addChild(this.Stinger);
        this.Head.addChild(this.Sensor1);
        this.Body.addChild(this.Neck);
        this.Head.addChild(this.Sensor2);
        
        this.BodyDeflated = new EndimatorModelRenderer(this, 10, 7);
        this.BodyDeflated.setRotationPoint(0.0F, 11.0F, 0.0F);
        this.BodyDeflated.addBox(-1.0F, -3.0F, -1.0F, 2, 6, 2, 0.0F);
        this.Sensor1Deflated = new EndimatorModelRenderer(this, 18, 1);
        this.Sensor1Deflated.setRotationPoint(-2.0F, 3.0F, 0.5F);
        this.Sensor1Deflated.addBox(0.0F, 0.0F, 0.0F, 1, 4, 0, 0.0F);
        this.setRotateAngle(Sensor1Deflated, 0.0F, 1.5707963267948966F, 0.7330382858376184F);
        this.HeadDeflated = new EndimatorModelRenderer(this, 0, 0);
        this.HeadDeflated.setRotationPoint(0.0F, 3.7F, 0.0F);
        this.HeadDeflated.addBox(-2.0F, 0.0F, -1.5F, 4, 3, 3, 0.0F);
        this.setRotateAngle(HeadDeflated, -0.13962634015954636F, 0.0F, 0.0F);
        this.Sensor2Deflated = new EndimatorModelRenderer(this, 18, 1);
        this.Sensor2Deflated.setRotationPoint(2.0F, 3.0F, 0.5F);
        this.Sensor2Deflated.addBox(-0.1F, 0.0F, 0.0F, 1, 4, 0, 0.0F);
        this.setRotateAngle(Sensor2Deflated, 0.7330382858376184F, 1.5707963267948966F, 0.0F);
        this.StingerDeflated = new EndimatorModelRenderer(this, 15, 1);
        this.StingerDeflated.setRotationPoint(0.0F, -3.0F, 0.0F);
        this.StingerDeflated.addBox(-0.5F, -4.0F, 0.0F, 1, 4, 0, 0.0F);
        this.NeckDeflated = new EndimatorModelRenderer(this, 0, 6);
        this.NeckDeflated.setRotationPoint(0.0F, 3.0F, 0.0F);
        this.NeckDeflated.addBox(-1.0F, 0.0F, -1.0F, 2, 4, 2, 0.0F);
        this.setRotateAngle(NeckDeflated, -0.13962634015954636F, 0.0F, 0.0F);
        this.HeadDeflated.addChild(this.Sensor1Deflated);
        this.NeckDeflated.addChild(this.HeadDeflated);
        this.HeadDeflated.addChild(this.Sensor2Deflated);
        this.BodyDeflated.addChild(this.StingerDeflated);
        this.BodyDeflated.addChild(this.NeckDeflated);
        
        this.NeckDeflatedProjectile = new EndimatorModelRenderer(this, 0, 6);
        this.NeckDeflatedProjectile.setRotationPoint(0.0F, 6.5F, 0.0F);
        this.NeckDeflatedProjectile.addBox(-1.0F, 0.0F, -1.0F, 2, 4, 2, 0.0F);
        this.setRotateAngle(NeckDeflatedProjectile, -0.136659280431156F, 0.0F, 0.0F);
        this.BodyDeflatedProjectile = new EndimatorModelRenderer(this, 10, 7);
        this.BodyDeflatedProjectile.setRotationPoint(0.0F, 23.0F, 0.0F);
        this.BodyDeflatedProjectile.addBox(-1.0F, 0.5F, -1.0F, 2, 6, 2, 0.0F);
        this.HeadDeflatedProjectile = new EndimatorModelRenderer(this, 0, 0);
        this.HeadDeflatedProjectile.setRotationPoint(0.0F, 3.7F, 0.0F);
        this.HeadDeflatedProjectile.addBox(-2.0F, 0.0F, -1.5F, 4, 3, 3, 0.0F);
        this.setRotateAngle(HeadDeflatedProjectile, -0.13962634015954636F, 0.0F, 0.0F);
        this.Sensor2DeflatedProjectile = new EndimatorModelRenderer(this, 18, 1);
        this.Sensor2DeflatedProjectile.setRotationPoint(2.0F, 3.0F, 0.5F);
        this.Sensor2DeflatedProjectile.addBox(-0.1F, 0.0F, 0.0F, 1, 4, 0, 0.0F);
        this.setRotateAngle(Sensor2DeflatedProjectile, 0.7330382858376184F, 1.5707963267948966F, 0.08726646259971647F);
        this.StingerDeflatedProjectile = new EndimatorModelRenderer(this, 15, 1);
        this.StingerDeflatedProjectile.setRotationPoint(0.0F, 0.5F, 0.0F);
        this.StingerDeflatedProjectile.addBox(-0.5F, -4.0F, 0.0F, 1, 4, 0, 0.0F);
        this.Sensor1DeflatedProjectile = new EndimatorModelRenderer(this, 18, 1);
        this.Sensor1DeflatedProjectile.setRotationPoint(-2.0F, 3.0F, 0.5F);
        this.Sensor1DeflatedProjectile.addBox(0.0F, 0.0F, 0.0F, 1, 4, 0, 0.0F);
        this.setRotateAngle(Sensor1DeflatedProjectile, 0.0F, 1.5707963267948966F, 0.7330382858376184F);
        this.BodyDeflatedProjectile.addChild(this.NeckDeflatedProjectile);
        this.NeckDeflatedProjectile.addChild(this.HeadDeflatedProjectile);
        this.HeadDeflatedProjectile.addChild(this.Sensor2DeflatedProjectile);
        this.BodyDeflatedProjectile.addChild(this.StingerDeflatedProjectile);
        this.HeadDeflatedProjectile.addChild(this.Sensor1DeflatedProjectile);
        
        this.createScaleController();
        
        this.setDefaultBoxValues();
    }
    
    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
    	Entity ridingEntity = this.entity.getRidingEntity();
    	if(ridingEntity instanceof EntityBooflo && !(((EntityBooflo) ridingEntity).isEndimationPlaying(EntityBooflo.EAT) && ((EntityBooflo) ridingEntity).getAnimationTick() >= 20)) {
    		return;
    	}
    	
    	this.animateModel(this.entity, 0, 0, 0, 0, 0, 0);
    	if(this.entity.isInflated()) {
    		this.Body.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    	} else {
    		if(this.entity.isProjectile()) {
    			this.BodyDeflatedProjectile.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    		} else {
    			this.BodyDeflated.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    		}
    	}
    }
    
    @Override
    public void setRotationAngles(E puffBug, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    	super.setRotationAngles(puffBug, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    	this.Body.setShouldScaleChildren(false);
    	
    	this.revertBoxesToDefaultValues();
    	
    	if(!puffBug.isEndimationPlaying(EntityPuffBug.PUFF_ANIMATION) && !puffBug.isEndimationPlaying(EntityPuffBug.POLLINATE_ANIMATION)) {
    		float angle = 0.1F * MathHelper.sin(0.25F * ageInTicks);
    		this.Sensor1.rotateAngleZ += angle;
    		this.Sensor2.rotateAngleX += angle;
    	}
    	
    	if(!puffBug.isEndimationPlaying(EntityPuffBug.FLY_ANIMATION)) {
    		this.Head.rotateAngleX += 0.075F * MathHelper.sin(0.1F * ageInTicks);
    		this.HeadDeflated.rotateAngleX = this.Head.rotateAngleX;
    	}
    	
    	float[] rotations = puffBug.getRotationController().getRotations(ClientInfo.getPartialTicks());
    	
    	this.Body.rotateAngleY = rotations[0] * (float) (Math.PI / 180F);
    	this.Body.rotateAngleX = rotations[1] * (float) (Math.PI / 180F);
    	
    	if(puffBug.isPassenger()) {
    		this.Body.rotateAngleZ = 1.57F;
    	}
    	
    	this.BodyDeflated.rotateAngleY = this.Body.rotateAngleY;
    	this.BodyDeflated.rotateAngleX = this.Body.rotateAngleX;
    	this.BodyDeflatedProjectile.rotateAngleY = this.Body.rotateAngleY;
    	this.BodyDeflatedProjectile.rotateAngleX = this.Body.rotateAngleX;
    	
    	this.NeckDeflated.rotateAngleX += -0.56F * puffBug.HIVE_LANDING.getAnimationProgress();
    	this.HeadDeflated.rotateAngleX += -0.42F * puffBug.HIVE_LANDING.getAnimationProgress();
    	this.Sensor1Deflated.rotateAngleZ += 1.7F * puffBug.HIVE_LANDING.getAnimationProgress();
    	this.Sensor2Deflated.rotateAngleX += 1.7F * puffBug.HIVE_LANDING.getAnimationProgress();
    	
    	this.NeckDeflated.rotateAngleX += 0.3F * puffBug.HIVE_SLEEP.getAnimationProgress();
    	this.HeadDeflated.rotateAngleX += 0.25F * puffBug.HIVE_SLEEP.getAnimationProgress();
    		
    	this.Neck.rotateAngleX = this.HeadDeflated.rotateAngleX;
    	this.Head.rotateAngleX = this.HeadDeflated.rotateAngleX;
    	this.Sensor1.rotateAngleZ = this.Sensor1Deflated.rotateAngleZ;
    	this.Sensor2.rotateAngleX = this.Sensor2Deflated.rotateAngleX;
    	
    	if(puffBug.isProjectile()) {
    		this.Neck.rotateAngleY = 0.0F;
    		this.NeckDeflated.rotateAngleY = 0.0F;
    		
    		this.Sensor1Deflated.rotateAngleX = 0.7F;
    		this.Sensor1Deflated.rotateAngleY = 0.7F;
    		this.Sensor1Deflated.rotateAngleZ = 0.09F;
    		
    		this.Sensor2Deflated.rotateAngleX = 0.32F;
    		this.Sensor2Deflated.rotateAngleY = -2.64F;
    		this.Sensor2Deflated.rotateAngleZ = 0.09F;
    		
    		float spin = MathHelper.lerp(ClientInfo.getPartialTicks(), puffBug.prevSpin, puffBug.spin);
    		
    		this.NeckDeflatedProjectile.rotateAngleY += Math.toRadians(spin);
    		this.StingerDeflatedProjectile.rotateAngleY += Math.toRadians(spin);
    	}
    }
    
    @Override
    public void animateModel(E puffbug, float f, float f1, float f2, float f3, float f4, float f5) {
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
    	} else if(puffbug.isEndimationPlaying(EntityPuffBug.MAKE_ITEM_ANIMATION)) {
    		this.endimator.setAnimationToPlay(EntityPuffBug.MAKE_ITEM_ANIMATION);
    		
    		this.endimator.startKeyframe(10);
    		this.endimator.move(this.getScaleController(), 0.2F, 0.2F, 0.2F);
    		this.endimator.rotate(this.Sensor1, 0.0F, 0.0F, 0.5F);
    		this.endimator.rotate(this.Sensor2, 0.5F, 0.0F, 0.0F);
    		this.endimator.endKeyframe();
    		
    		this.endimator.resetKeyframe(10);
    		
    		this.endimator.startKeyframe(10);
    		this.endimator.move(this.getScaleController(), 0.2F, 0.2F, 0.2F);
    		this.endimator.rotate(this.Sensor1, 0.0F, 0.0F, 0.5F);
    		this.endimator.rotate(this.Sensor2, 0.5F, 0.0F, 0.0F);
    		this.endimator.endKeyframe();
    		
    		this.endimator.resetKeyframe(10);
    		
    		this.endimator.startKeyframe(10);
    		this.endimator.move(this.getScaleController(), 0.2F, 0.2F, 0.2F);
    		this.endimator.rotate(this.Sensor1, 0.0F, 0.0F, 0.5F);
    		this.endimator.rotate(this.Sensor2, 0.5F, 0.0F, 0.0F);
    		this.endimator.endKeyframe();
    		
    		this.endimator.resetKeyframe(10);
    		
    		this.endimator.startKeyframe(10);
    		this.endimator.move(this.getScaleController(), 0.2F, 0.2F, 0.2F);
    		this.endimator.rotate(this.Sensor1, 0.0F, 0.0F, 0.5F);
    		this.endimator.rotate(this.Sensor2, 0.5F, 0.0F, 0.0F);
    		this.endimator.endKeyframe();
    		
    		this.endimator.resetKeyframe(10);
    		
    		this.endimator.startKeyframe(10);
    		this.endimator.move(this.getScaleController(), 0.4F, 0.4F, 0.4F);
    		this.endimator.rotate(this.Sensor1, 0.0F, 0.0F, 0.6F);
    		this.endimator.rotate(this.Sensor2, 0.6F, 0.0F, 0.0F);
    		this.endimator.rotate(this.Head, -0.35F, 0.0F, 0.0F);
    		this.endimator.rotate(this.Neck, -0.25F, 0.0F, 0.0F);
    		this.endimator.endKeyframe();
    		
    		this.endimator.resetKeyframe(10);
    	} else if(puffbug.isEndimationPlaying(EntityPuffBug.LAND_ANIMATION)) {
    		this.endimator.setAnimationToPlay(EntityPuffBug.LAND_ANIMATION);
    		
    		this.endimator.setStaticKeyframe(3);
    		
    		this.endimator.startKeyframe(3);
    		this.endimator.rotate(this.Sensor1DeflatedProjectile, 0.0F, 0.0F, 2.05F);
    		this.endimator.rotate(this.Sensor2DeflatedProjectile, 1.6F, 0.0F, -0.44F);
    		
    		this.endimator.rotate(this.NeckDeflatedProjectile, -0.5F, 0.0F, 0.0F);
    		this.endimator.rotate(this.HeadDeflatedProjectile, -0.7F, 0.0F, 0.0F);
    		this.endimator.endKeyframe();
    		
    		this.endimator.startKeyframe(3);
    		this.endimator.rotate(this.Sensor1DeflatedProjectile, 0.0F, 0.0F, 2.05F);
    		this.endimator.rotate(this.Sensor2DeflatedProjectile, 1.6F, 0.0F, -0.44F);
    		
    		this.endimator.rotate(this.NeckDeflatedProjectile, 0.5F, 0.0F, 0.0F);
    		this.endimator.rotate(this.HeadDeflatedProjectile, 0.7F, 0.0F, 0.0F);
    		this.endimator.endKeyframe();
    		
    		this.endimator.startKeyframe(3);
    		this.endimator.rotate(this.Sensor1DeflatedProjectile, 0.0F, 0.0F, 0.0F);
    		this.endimator.rotate(this.Sensor2DeflatedProjectile, 0.0F, 0.0F, 0.0F);
    		
    		this.endimator.rotate(this.NeckDeflatedProjectile, -0.44F, 0.0F, 0.0F);
    		this.endimator.rotate(this.HeadDeflatedProjectile, -0.35F, 0.0F, 0.0F);
    		this.endimator.endKeyframe();
    		
    		this.endimator.startKeyframe(3);
    		this.endimator.rotate(this.Sensor1DeflatedProjectile, 0.0F, 0.0F, 0.0F);
    		this.endimator.rotate(this.Sensor2DeflatedProjectile, 0.0F, 0.0F, 0.0F);
    		
    		this.endimator.rotate(this.NeckDeflatedProjectile, 0.15F, 0.0F, 0.0F);
    		this.endimator.rotate(this.HeadDeflatedProjectile, 0.26F, 0.0F, 0.0F);
    		this.endimator.endKeyframe();
    		
    		this.endimator.startKeyframe(3);
    		this.endimator.rotate(this.HeadDeflatedProjectile, 0.0F, 0.0F, 0.0F);
    		
    		this.endimator.rotate(this.NeckDeflatedProjectile, -0.15F, 0.0F, 0.0F);
    		this.endimator.endKeyframe();
    		
    		this.endimator.resetKeyframe(2);
    	} else if(puffbug.isEndimationPlaying(EntityPuffBug.PULL_ANIMATION)) {
    		this.endimator.setAnimationToPlay(EntityPuffBug.PULL_ANIMATION);
    		
    		this.endimator.startKeyframe(5);
    		this.endimator.rotate(this.NeckDeflatedProjectile, 0.65F, 0.0F, 0.0F);
    		this.endimator.rotate(this.HeadDeflatedProjectile, 0.4F, 0.0F, 0.0F);
    		
    		this.endimator.move(this.BodyDeflatedProjectile, 0.0F, -1.0F, 0.0F);
    		this.endimator.endKeyframe();
    		
    		this.endimator.startKeyframe(10);
    		this.endimator.rotate(this.NeckDeflatedProjectile, 0.23F, 0.0F, 0.0F);
    		this.endimator.rotate(this.HeadDeflatedProjectile, 0.1F, 0.0F, 0.0F);
    		
    		this.endimator.move(this.BodyDeflatedProjectile, 0.0F, -0.5F, 0.0F);
    		this.endimator.endKeyframe();
    		
    		this.endimator.resetKeyframe(10);
    	}
    	
    	this.Body.setScale(this.getScaleController().rotationPointX, this.getScaleController().rotationPointY, this.getScaleController().rotationPointZ);
    }
    
    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(EndimatorModelRenderer EndimatorModelRenderer, float x, float y, float z) {
        EndimatorModelRenderer.rotateAngleX = x;
        EndimatorModelRenderer.rotateAngleY = y;
        EndimatorModelRenderer.rotateAngleZ = z;
    }
}