package com.minecraftabnormals.endergetic.client.models.bolloom;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.teamabnormals.abnormals_core.client.ClientInfo;
import com.minecraftabnormals.endergetic.common.entities.bolloom.BolloomFruitEntity;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

/**
 * ModelBolloomFruit - Endergized
 * Created using Tabula 7.0.0
 */
public class BolloomFruitModel<T extends BolloomFruitEntity> extends EntityModel<T> {
	private T fruitEntity;
	
    public ModelRenderer vine_x;
    public ModelRenderer fruit;
    public ModelRenderer vine_z;
    public ModelRenderer vine_x_1;
    public ModelRenderer vine_z_1;
    public ModelRenderer vine_x_2;
    public ModelRenderer vine_z_2;
    public ModelRenderer vine_x_3;
    public ModelRenderer vine_z_3;
    public ModelRenderer vine_x_4;
    public ModelRenderer vine_z_4;
    public ModelRenderer vine_x_5;
    public ModelRenderer vine_z_5;
    public ModelRenderer vine_x_6;
    public ModelRenderer vine_z_6;
    public ModelRenderer flap;

    public BolloomFruitModel() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.vine_z = new ModelRenderer(this, 0, 10);
        this.vine_z.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.vine_z.addBox(0.0F, 0.0F, -3.0F, 0, 16, 6, 0.0F);
        this.setRotateAngle(vine_z, 0.0F, -1.5707963267948966F, 0.0F);
        this.vine_z_3 = new ModelRenderer(this, 0, 10);
        this.vine_z_3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.vine_z_3.addBox(0.0F, 0.0F, -3.0F, 0, 16, 6, 0.0F);
        this.setRotateAngle(vine_z_3, 0.0F, -1.5707963267948966F, 0.0F);
        this.vine_z_5 = new ModelRenderer(this, 0, 10);
        this.vine_z_5.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.vine_z_5.addBox(0.0F, 0.0F, -3.0F, 0, 16, 6, 0.0F);
        this.setRotateAngle(vine_z_5, 0.0F, -1.5707963267948966F, 0.0F);
        this.fruit = new ModelRenderer(this, 0, 0);
        this.fruit.setRotationPoint(-4.0F, 16.0F, -4.0F);
        this.fruit.addBox(0.0F, 0.0F, 0.0F, 8, 8, 8, 0.0F);
        this.vine_z_1 = new ModelRenderer(this, 0, 10);
        this.vine_z_1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.vine_z_1.addBox(0.0F, 0.0F, -3.0F, 0, 16, 6, 0.0F);
        this.setRotateAngle(vine_z_1, 0.0F, -1.5707963267948966F, 0.0F);
        this.vine_x_2 = new ModelRenderer(this, 13, 10);
        this.vine_x_2.setRotationPoint(0.0F, 16.0F, 0.0F);
        this.vine_x_2.addBox(0.0F, 0.0F, -3.0F, 0, 16, 6, 0.0F);
        this.flap = new ModelRenderer(this, 20, 4);
        this.flap.setRotationPoint(-2.0F, 8.1F, -2.0F);
        this.flap.addBox(0.0F, 0.0F, 0.0F, 12, 0, 12, 0.0F);
        this.vine_x_4 = new ModelRenderer(this, 13, 10);
        this.vine_x_4.setRotationPoint(0.0F, 16.0F, 0.0F);
        this.vine_x_4.addBox(0.0F, 0.0F, -3.0F, 0, 16, 6, 0.0F);
        this.vine_x_1 = new ModelRenderer(this, 13, 10);
        this.vine_x_1.setRotationPoint(0.0F, 16.0F, 0.0F);
        this.vine_x_1.addBox(0.0F, 0.0F, -3.0F, 0, 16, 6, 0.0F);
        this.vine_z_2 = new ModelRenderer(this, 0, 10);
        this.vine_z_2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.vine_z_2.addBox(0.0F, 0.0F, -3.0F, 0, 16, 6, 0.0F);
        this.setRotateAngle(vine_z_2, 0.0F, -1.5707963267948966F, 0.0F);
        this.vine_z_6 = new ModelRenderer(this, 0, 10);
        this.vine_z_6.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.vine_z_6.addBox(0.0F, 0.0F, -3.0F, 0, 16, 6, 0.0F);
        this.setRotateAngle(vine_z_6, 0.0F, -1.5707963267948966F, 0.0F);
        this.vine_x_3 = new ModelRenderer(this, 13, 10);
        this.vine_x_3.setRotationPoint(0.0F, 16.0F, 0.0F);
        this.vine_x_3.addBox(0.0F, 0.0F, -3.0F, 0, 16, 6, 0.0F);
        this.vine_z_4 = new ModelRenderer(this, 0, 10);
        this.vine_z_4.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.vine_z_4.addBox(0.0F, 0.0F, -3.0F, 0, 16, 6, 0.0F);
        this.setRotateAngle(vine_z_4, 0.0F, -1.5707963267948966F, 0.0F);
        this.vine_x = new ModelRenderer(this, 13, 10);
        this.vine_x.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.vine_x.addBox(0.0F, 0.0F, -3.0F, 0, 16, 6, 0.0F);
        this.vine_x_6 = new ModelRenderer(this, 13, 10);
        this.vine_x_6.setRotationPoint(0.0F, 16.0F, 0.0F);
        this.vine_x_6.addBox(0.0F, 0.0F, -3.0F, 0, 16, 6, 0.0F);
        this.vine_x_5 = new ModelRenderer(this, 13, 10);
        this.vine_x_5.setRotationPoint(0.0F, 16.0F, 0.0F);
        this.vine_x_5.addBox(0.0F, 0.0F, -3.0F, 0, 16, 6, 0.0F);
        this.vine_x.addChild(this.vine_z);
        this.vine_x_3.addChild(this.vine_z_3);
        this.vine_x_5.addChild(this.vine_z_5);
        this.vine_x_1.addChild(this.vine_z_1);
        this.vine_z_1.addChild(this.vine_x_2);
        this.fruit.addChild(this.flap);
        this.vine_z_3.addChild(this.vine_x_4);
        this.vine_z.addChild(this.vine_x_1);
        this.vine_x_2.addChild(this.vine_z_2);
        this.vine_x_6.addChild(this.vine_z_6);
        this.vine_z_2.addChild(this.vine_x_3);
        this.vine_x_4.addChild(this.vine_z_4);
        this.vine_z_5.addChild(this.vine_x_6);
        this.vine_z_4.addChild(this.vine_x_5);
    }
    
    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
    	int height = this.fruitEntity.getVineHeight();
    	
    	this.vine_x.showModel = true;
    	this.vine_z.showModel = true;
    	this.vine_x_1.showModel = true;
    	this.vine_z_1.showModel = true;
    	this.vine_x_2.showModel = true;
    	this.vine_z_2.showModel = true;
    	this.vine_x_3.showModel = true;
    	this.vine_z_3.showModel = true;
    	this.vine_x_4.showModel = true;
    	this.vine_z_4.showModel = true;
    	this.vine_x_5.showModel = true;
    	this.vine_z_5.showModel = true;
    	this.vine_x_6.showModel = true;
    	this.vine_z_6.showModel = true;
        
        switch (height) {
        	case 1:
        		this.vine_x_1.showModel = false;
        		this.vine_z_1.showModel = false;
        		break;
        	case 2:
        		this.vine_x_2.showModel = false;
        		this.vine_z_2.showModel = false;
        		break;
        	case 3:
        		this.vine_x_3.showModel = false;
        		this.vine_z_3.showModel = false;
        		break;
        	case 4:
        		this.vine_x_4.showModel = false;
        		this.vine_z_4.showModel = false;
        		break;
        	case 5:
        		this.vine_x_5.showModel = false;
        		this.vine_z_5.showModel = false;
        		break;
        	case 6:
        		this.vine_x_6.showModel = false;
        		this.vine_z_6.showModel = false;
        		break;
        }
        this.fruit.render(matrixStackIn, bufferIn, 240, packedOverlayIn, red, green, blue, alpha);
        this.vine_x.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
    
    @Override
    public void setRotationAngles(T fruit, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    	this.fruitEntity = fruit;
    	
    	float[] angles = fruit.getVineAnimation(ClientInfo.getPartialTicks());
        this.vine_x.rotateAngleX = angles[0];
        this.vine_x.rotateAngleY = angles[1];
    }
    
    public void setRotateAngle(ModelRenderer ModelRenderer, float x, float y, float z) {
        ModelRenderer.rotateAngleX = x;
        ModelRenderer.rotateAngleY = y;
        ModelRenderer.rotateAngleZ = z;
    }
}
