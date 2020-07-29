package com.minecraftabnormals.endergetic.client.model.booflo;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.teamabnormals.abnormals_core.client.ClientInfo;
import com.teamabnormals.abnormals_core.core.library.endimator.EndimatorEntityModel;
import com.teamabnormals.abnormals_core.core.library.endimator.EndimatorModelRenderer;
import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloAdolescentEntity;

import net.minecraft.util.math.MathHelper;

/**
 * ModelAdolescentBooflo - Endergized
 * Created using Tabula 7.0.0
 */
public class AdolescentBoofloModel<E extends BoofloAdolescentEntity> extends EndimatorEntityModel<E> {
    public EndimatorModelRenderer Head;
    public EndimatorModelRenderer KneeLeft;
    public EndimatorModelRenderer KneeRight;
    public EndimatorModelRenderer ArmLeft;
    public EndimatorModelRenderer ArmRight;
    public EndimatorModelRenderer Tail;
    public EndimatorModelRenderer Jaw;
    
    public AdolescentBoofloModel() {
        this.textureWidth = 64;
        this.textureHeight = 48;
        this.ArmRight = new EndimatorModelRenderer(this, 14, 16);
        this.ArmRight.setRotationPoint(-4.5F, -2.0F, -7.0F);
        this.ArmRight.addBox(-4.0F, -1.0F, -1.0F, 4, 2, 2, 0.0F);
        this.setRotateAngle(ArmRight, 0.0F, -0.5585053606381855F, -0.3490658503988659F);
        this.Head = new EndimatorModelRenderer(this, 0, 0);
        this.Head.setRotationPoint(0.0F, 18.0F, 5.0F);
        this.Head.addBox(-5.0F, -5.0F, -10.0F, 10, 5, 10, 0.0F);
        this.KneeLeft = new EndimatorModelRenderer(this, 0, 24);
        this.KneeLeft.setRotationPoint(2.5F, -4.5F, -3.5F);
        this.KneeLeft.addBox(-1.5F, -5.0F, -1.5F, 3, 5, 3, 0.0F);
        this.setRotateAngle(KneeLeft, 0.0F, 0.0F, 0.3490658503988659F);
        this.KneeRight = new EndimatorModelRenderer(this, 14, 24);
        this.KneeRight.setRotationPoint(-2.5F, -4.5F, -3.5F);
        this.KneeRight.addBox(-1.5F, -5.0F, -1.5F, 3, 5, 3, 0.0F);
        this.setRotateAngle(KneeRight, 0.0F, 0.0F, -0.3490658503988659F);
        this.Jaw = new EndimatorModelRenderer(this, 16, 28);
        this.Jaw.setRotationPoint(0.0F, 18.0F, 5.0F);
        this.Jaw.addBox(-6.0F, 0.0F, -11.0F, 12, 6, 12, 0.0F);
        this.Tail = new EndimatorModelRenderer(this, 32, 16);
        this.Tail.setRotationPoint(0.0F, -5.0F, 0.0F);
        this.Tail.addBox(0.0F, 0.0F, 0.0F, 0, 5, 7, 0.0F);
        this.ArmLeft = new EndimatorModelRenderer(this, 0, 16);
        this.ArmLeft.setRotationPoint(4.5F, -2.0F, -7.0F);
        this.ArmLeft.addBox(0.0F, -1.0F, -1.0F, 4, 2, 2, 0.0F);
        this.setRotateAngle(ArmLeft, 0.0F, 0.5585053606381855F, 0.3490658503988659F);
        this.Head.addChild(this.ArmRight);
        this.Head.addChild(this.KneeLeft);
        this.Head.addChild(this.KneeRight);
        this.Head.addChild(this.Tail);
        this.Head.addChild(this.ArmLeft);
        
        this.setDefaultBoxValues();
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
    	this.animateModel(this.entity);
    	
    	this.Head.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    	this.Jaw.render(matrixStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(EndimatorModelRenderer EndimatorModelRenderer, float x, float y, float z) {
        EndimatorModelRenderer.rotateAngleX = x;
        EndimatorModelRenderer.rotateAngleY = y;
        EndimatorModelRenderer.rotateAngleZ = z;
    }
    
    @Override
    public void setRotationAngles(E entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    	super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    	
    	this.revertBoxesToDefaultValues();
    	
    	this.Tail.rotateAngleY = 0.1F * MathHelper.sin(entityIn.getTailAnimation(0.3F * ClientInfo.getPartialTicks())) * (float) Math.PI;
    	
    	if(!entityIn.isEndimationPlaying(BoofloAdolescentEntity.BOOF_ANIMATION) && !entityIn.isInWater()) {
    		if(entityIn.getRidingEntity() == null && !entityIn.isEating()) {
    			this.Head.rotationPointY += 0.5F * MathHelper.sin(0.4F * ageInTicks);
    			this.Jaw.rotationPointY += 0.5F * MathHelper.sin(0.4F * ageInTicks);
    		}
    		if(!entityIn.isEating()) {
    			this.KneeLeft.rotateAngleZ += 0.1F * -MathHelper.sin(0.6F * entityIn.getSwimmingAnimation(ClientInfo.getPartialTicks()));
    			this.KneeRight.rotateAngleZ += 0.1F * MathHelper.sin(0.6F * entityIn.getSwimmingAnimation(ClientInfo.getPartialTicks()));
    			this.ArmLeft.rotateAngleZ += 0.3F * -MathHelper.sin(0.6F * entityIn.getSwimmingAnimation(ClientInfo.getPartialTicks())) - 0.17F;
    			this.ArmRight.rotateAngleZ += 0.3F * MathHelper.sin(0.6F * entityIn.getSwimmingAnimation(ClientInfo.getPartialTicks())) + 0.17F;
    		}
    	} else if(!entityIn.isEndimationPlaying(BoofloAdolescentEntity.BOOF_ANIMATION) && entityIn.isInWater()) {
    		this.Head.rotationPointY += 0.5F * MathHelper.sin(0.4F * ageInTicks);
    		this.Jaw.rotationPointY += 0.5F * MathHelper.sin(0.4F * ageInTicks);
    		this.KneeLeft.rotateAngleZ += 0.1F * -MathHelper.sin(0.6F * ageInTicks);
    		this.KneeRight.rotateAngleZ += 0.1F * MathHelper.sin(0.6F * ageInTicks);
    		this.ArmLeft.rotateAngleZ += 0.3F * -MathHelper.sin(0.6F * ageInTicks) - 0.17F;
    		this.ArmRight.rotateAngleZ += 0.3F * MathHelper.sin(0.6F * ageInTicks) + 0.17F;
    	}
    	
    	if(entityIn.isAggressive()) {
    		this.Jaw.rotateAngleX += 0.2F * MathHelper.sin(0.3F * ageInTicks) + 0.4F;
    	}
    }
    
    @Override
    public void animateModel(E booflo) {
    	super.animateModel(booflo);
    	
    	if(booflo.isEndimationPlaying(BoofloAdolescentEntity.BOOF_ANIMATION)) {
    		this.setEndimationToPlay(BoofloAdolescentEntity.BOOF_ANIMATION);
    		
    		this.startKeyframe(3);
    		this.scale(this.Head, 0.5F, -0.2F, 0.5F);
    		this.scale(this.Jaw, 0.5F, -0.2F, 0.5F);
    		this.move(Head, 0.0F, -0.2F, 0.0F);
    		this.move(Jaw, 0.0F, -0.2F, 0.0F);
    		this.rotate(ArmLeft, 0.0F, 0.0F, -0.4F);
    		this.rotate(ArmRight, 0.0F, 0.0F, 0.4F);
    		this.rotate(KneeLeft, 0.0F, 0.0F, 0.2F);
    		this.rotate(KneeRight, 0.0F, 0.0F, -0.2F);
    		this.endKeyframe();
    		
    		this.setStaticKeyframe(3);
    		
    		this.startKeyframe(4);
    		this.scale(this.Head, -0.0F, 0.0F, -0.0F);
    		this.scale(this.Jaw, -0.0F, 0.0F, -0.0F);
    		this.move(Head, 0.0F, 0F, 0.0F);
    		this.move(Jaw, 0.0F, 0F, 0.0F);
    		this.rotate(ArmLeft, 0.0F, 0.0F, 0.6F);
    		this.rotate(ArmRight, 0.0F, 0.0F, -0.6F);
    		this.rotate(KneeLeft, 0.0F, 0.0F, -0.35F);
    		this.rotate(KneeRight, 0.0F, 0.0F, 0.35F);
    		this.endKeyframe();
    		
    		this.setStaticKeyframe(4);
    		
    		this.startKeyframe(4);
    		this.rotate(ArmLeft, 0.0F, 0.0F, -0.2F);
    		this.rotate(ArmRight, 0.0F, 0.0F, 0.2F);
    		this.rotate(KneeLeft, 0.0F, 0.0F, 0.15F);
    		this.rotate(KneeRight, 0.0F, 0.0F, -0.15F);
    		this.endKeyframe();
    	} else if(booflo.isEndimationPlaying(BoofloAdolescentEntity.EATING_ANIMATION)) {
    		this.setEndimationToPlay(BoofloAdolescentEntity.EATING_ANIMATION);
    		
    		this.startKeyframe(5);
    		this.rotate(ArmLeft, 0.0F, -0.42F, 0.0F);
    		this.rotate(ArmRight, 0.0F, 0.42F, 0.0F);
    		this.rotate(Head, -0.6F, 0.0F, 0.0F);
    		this.endKeyframe();
    		
    		this.startKeyframe(5);
    		this.rotate(ArmLeft, 0.0F, -0.0F, 0.0F);
    		this.rotate(ArmRight, 0.0F, 0.0F, 0.0F);
    		this.rotate(Head, -0.0F, 0.0F, 0.0F);
    		this.endKeyframe();
    	}
    }
}