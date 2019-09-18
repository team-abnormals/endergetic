package endergeticexpansion.client.model.bolloom;

import com.mojang.blaze3d.platform.GLX;

import endergeticexpansion.common.entities.bolloom.EntityBolloomBalloon;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;

/**
 * ModelBolloomBalloon - Endergized
 * Created using Tabula 7.0.0
 */
public class ModelBolloomBalloon<T extends EntityBolloomBalloon> extends EntityModel<T> {
    public RendererModel balloon;
    public RendererModel x_string;
    public RendererModel z_string;
    public RendererModel x_string_2;
    public RendererModel z_string_2;

    public ModelBolloomBalloon() {
    	this.textureHeight = 32;
    	this.textureWidth = 32;
    	this.z_string_2 = new RendererModel(this, 13, 10);
        this.z_string_2.setRotationPoint(3.0F, 0.0F, 0.0F);
        this.z_string_2.addBox(0.0F, 0.0F, 0.0F, 0, 16, 6, 0.0F);
        this.setRotateAngle(z_string_2, 0.0F, -1.5707963267948966F, 0.0F);
        this.z_string = new RendererModel(this, 0, 10);
        this.z_string.setRotationPoint(3.0F, 0.0F, 0.0F);
        this.z_string.addBox(0.0F, 0.0F, 0.0F, 0, 16, 6, 0.0F);
        this.setRotateAngle(z_string, 0.0F, -1.5707963267948966F, 0.0F);
        this.x_string = new RendererModel(this, 0, 10);
        this.x_string.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.x_string.addBox(0.0F, 0.0F, -3.0F, 0, 16, 6, 0.0F);
        this.balloon = new RendererModel(this, 0, 0);
        this.balloon.setRotationPoint(-4.0F, 16.0F, -4.0F);
        this.balloon.addBox(0.0F, 0.0F, 0.0F, 8, 8, 8, 0.0F);
        this.x_string_2 = new RendererModel(this, 13, 10);
        this.x_string_2.setRotationPoint(0.0F, 16.0F, 3.0F);
        this.x_string_2.addBox(0.0F, 0.0F, -3.0F, 0, 16, 6, 0.0F);
        this.x_string_2.addChild(this.z_string_2);
        this.x_string.addChild(this.z_string);
        this.z_string.addChild(this.x_string_2);
    }

    @Override
    public void render(T entity, float f, float f1, float f2, float f3, float f4, float f5) {
    	GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, 240.0F, 240.0F);
        this.balloon.render(f5);
        
        int i = entity.getBrightnessForRender();
        int j = i % 65536;
        int k = i / 65536;
        GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, j, k);
        this.x_string.render(f5);
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
