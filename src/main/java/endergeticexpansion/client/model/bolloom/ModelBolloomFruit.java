package endergeticexpansion.client.model.bolloom;

import com.mojang.blaze3d.platform.GLX;

import endergeticexpansion.common.entities.bolloom.EntityBolloomFruit;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;

/**
 * ModelBolloomFruit - Endergized
 * Created using Tabula 7.0.0
 */
public class ModelBolloomFruit<T extends EntityBolloomFruit> extends EntityModel<T> {
    public RendererModel vine_x;
    public RendererModel fruit;
    public RendererModel vine_z;
    public RendererModel vine_x_1;
    public RendererModel vine_z_1;
    public RendererModel vine_x_2;
    public RendererModel vine_z_2;
    public RendererModel vine_x_3;
    public RendererModel vine_z_3;
    public RendererModel vine_x_4;
    public RendererModel vine_z_4;
    public RendererModel vine_x_5;
    public RendererModel vine_z_5;
    public RendererModel vine_x_6;
    public RendererModel vine_z_6;
    public RendererModel flap;

    public ModelBolloomFruit() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.vine_z = new RendererModel(this, 0, 10);
        this.vine_z.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.vine_z.addBox(0.0F, 0.0F, -3.0F, 0, 16, 6, 0.0F);
        this.setRotateAngle(vine_z, 0.0F, -1.5707963267948966F, 0.0F);
        this.vine_z_3 = new RendererModel(this, 0, 10);
        this.vine_z_3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.vine_z_3.addBox(0.0F, 0.0F, -3.0F, 0, 16, 6, 0.0F);
        this.setRotateAngle(vine_z_3, 0.0F, -1.5707963267948966F, 0.0F);
        this.vine_z_5 = new RendererModel(this, 0, 10);
        this.vine_z_5.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.vine_z_5.addBox(0.0F, 0.0F, -3.0F, 0, 16, 6, 0.0F);
        this.setRotateAngle(vine_z_5, 0.0F, -1.5707963267948966F, 0.0F);
        this.fruit = new RendererModel(this, 0, 0);
        this.fruit.setRotationPoint(-4.0F, 16.0F, -4.0F);
        this.fruit.addBox(0.0F, 0.0F, 0.0F, 8, 8, 8, 0.0F);
        this.vine_z_1 = new RendererModel(this, 0, 10);
        this.vine_z_1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.vine_z_1.addBox(0.0F, 0.0F, -3.0F, 0, 16, 6, 0.0F);
        this.setRotateAngle(vine_z_1, 0.0F, -1.5707963267948966F, 0.0F);
        this.vine_x_2 = new RendererModel(this, 13, 10);
        this.vine_x_2.setRotationPoint(0.0F, 16.0F, 0.0F);
        this.vine_x_2.addBox(0.0F, 0.0F, -3.0F, 0, 16, 6, 0.0F);
        this.flap = new RendererModel(this, 20, 4);
        this.flap.setRotationPoint(-2.0F, 8.1F, -2.0F);
        this.flap.addBox(0.0F, 0.0F, 0.0F, 12, 0, 12, 0.0F);
        this.vine_x_4 = new RendererModel(this, 13, 10);
        this.vine_x_4.setRotationPoint(0.0F, 16.0F, 0.0F);
        this.vine_x_4.addBox(0.0F, 0.0F, -3.0F, 0, 16, 6, 0.0F);
        this.vine_x_1 = new RendererModel(this, 13, 10);
        this.vine_x_1.setRotationPoint(0.0F, 16.0F, 0.0F);
        this.vine_x_1.addBox(0.0F, 0.0F, -3.0F, 0, 16, 6, 0.0F);
        this.vine_z_2 = new RendererModel(this, 0, 10);
        this.vine_z_2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.vine_z_2.addBox(0.0F, 0.0F, -3.0F, 0, 16, 6, 0.0F);
        this.setRotateAngle(vine_z_2, 0.0F, -1.5707963267948966F, 0.0F);
        this.vine_z_6 = new RendererModel(this, 0, 10);
        this.vine_z_6.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.vine_z_6.addBox(0.0F, 0.0F, -3.0F, 0, 16, 6, 0.0F);
        this.setRotateAngle(vine_z_6, 0.0F, -1.5707963267948966F, 0.0F);
        this.vine_x_3 = new RendererModel(this, 13, 10);
        this.vine_x_3.setRotationPoint(0.0F, 16.0F, 0.0F);
        this.vine_x_3.addBox(0.0F, 0.0F, -3.0F, 0, 16, 6, 0.0F);
        this.vine_z_4 = new RendererModel(this, 0, 10);
        this.vine_z_4.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.vine_z_4.addBox(0.0F, 0.0F, -3.0F, 0, 16, 6, 0.0F);
        this.setRotateAngle(vine_z_4, 0.0F, -1.5707963267948966F, 0.0F);
        this.vine_x = new RendererModel(this, 13, 10);
        this.vine_x.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.vine_x.addBox(0.0F, 0.0F, -3.0F, 0, 16, 6, 0.0F);
        this.vine_x_6 = new RendererModel(this, 13, 10);
        this.vine_x_6.setRotationPoint(0.0F, 16.0F, 0.0F);
        this.vine_x_6.addBox(0.0F, 0.0F, -3.0F, 0, 16, 6, 0.0F);
        this.vine_x_5 = new RendererModel(this, 13, 10);
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
    public void render(EntityBolloomFruit entity, float f, float f1, float f2, float f3, float f4, float f5) {
        int height = entity.getVineHeight();
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
		this.vine_x.showModel = true;
		this.vine_z.showModel = true;
		this.fruit.showModel = true;
		this.flap.showModel = true;
        switch(height) {
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
        if(!entity.isGrown()) {
        	this.vine_x.showModel = false;
        	this.fruit.showModel = false;
        	this.flap.showModel = false;
        }
        GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, 240.0F, 240.0F);
        this.fruit.render(f5);
        
        int i = entity.getBrightnessForRender();
        int j = i % 65536;
        int k = i / 65536;
        GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, j, k);
        this.vine_x.render(f5);
    }
    
    @Override
    public void setLivingAnimations(T entity, float p_212843_2_, float p_212843_3_, float partialTicks) {
    	float angle1Old = entity.prevVineAngle;
        float angle2Old = entity.prevAngle;
        float angle1New = entity.getVineAngle();
        float angle2New = entity.getAngle();
        float angle1 = angle1Old * (1 - partialTicks) + angle1New * partialTicks;
        float angle2 = angle2Old * (1 - partialTicks) + angle2New * partialTicks;
        this.vine_x.rotateAngleX = angle1;
        this.vine_x.rotateAngleY = angle2;
    	super.setLivingAnimations(entity, p_212843_2_, p_212843_3_, partialTicks);
    }

    public void setRotateAngle(RendererModel rendererModel, float x, float y, float z) {
        rendererModel.rotateAngleX = x;
        rendererModel.rotateAngleY = y;
        rendererModel.rotateAngleZ = z;
    }
}
