package endergeticexpansion.client.model.booflo;

import endergeticexpansion.common.entities.booflo.EntityBoofloBaby;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.util.math.MathHelper;

/**
 * ModelBoofloBaby - Endergized
 * Created using Tabula 7.0.0
 */
public class ModelBoofloBaby<E extends EntityBoofloBaby> extends EntityModel<E> {
    public RendererModel Head;
    public RendererModel Jaw;
    public RendererModel Tail;

    public ModelBoofloBaby() {
        this.textureWidth = 32;
        this.textureHeight = 16;
        this.Head = new RendererModel(this, 15, 10);
        this.Head.setRotationPoint(0.0F, 21.0F, 0.0F);
        this.Head.addBox(-2.0F, -2.0F, -2.0F, 4, 2, 4, 0.0F);
        this.Tail = new RendererModel(this, 0, 2);
        this.Tail.setRotationPoint(0.0F, 0.0F, 2.0F);
        this.Tail.addBox(0.0F, -2.0F, 0.0F, 0, 4, 8, 0.0F);
        this.Jaw = new RendererModel(this, 0, 0);
        this.Jaw.setRotationPoint(0.0F, 0.0F, 2.0F);
        this.Jaw.addBox(-3.0F, 0.0F, -5.0F, 6, 3, 6, 0.0F);
        this.Head.addChild(this.Tail);
        this.Head.addChild(this.Jaw);
    }

    @Override
    public void render(E entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.Head.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(RendererModel RendererModel, float x, float y, float z) {
        RendererModel.rotateAngleX = x;
        RendererModel.rotateAngleY = y;
        RendererModel.rotateAngleZ = z;
    }
    
    @Override
    public void setRotationAngles(E entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
    	float tailAnimation = entityIn.getTailAnimation(ageInTicks - entityIn.ticksExisted);
    	
    	this.Head.rotateAngleX = headPitch * (float) (Math.PI / 180F);
		this.Head.rotateAngleY = netHeadYaw * (float) (Math.PI / 180F);
		
		if(entityIn.isMoving()) {
			this.Head.rotateAngleY += -1.1F * 0.2F * MathHelper.sin(0.55F * ageInTicks);
		}
		this.Tail.rotateAngleY = MathHelper.sin(tailAnimation) * (float) Math.PI * 0.09F;
    }
}
