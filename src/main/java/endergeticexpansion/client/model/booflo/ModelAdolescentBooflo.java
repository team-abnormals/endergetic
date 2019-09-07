package endergeticexpansion.client.model.booflo;

import endergeticexpansion.api.client.ClientInfo;
import endergeticexpansion.api.client.animation.EndimatorEntityModel;
import endergeticexpansion.api.client.animation.EndimatorRendererModel;
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
        
        this.setDefaultBoxValues();
    }

    @Override
    public void render(E entity, float f, float f1, float f2, float f3, float f4, float f5) {
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
    	this.ArmLeft.rotateAngleZ = 0.275F * MathHelper.sin(0.65F * (ClientInfo.getPartialTicks() + entityIn.ticksExisted)) + 0.17F;
    	this.ArmRight.rotateAngleZ = -0.275F * MathHelper.sin(0.65F * ageInTicks) - 0.17F;
    	
    	this.KneeLeft.rotateAngleZ = -0.1F * MathHelper.sin(0.65F * ageInTicks) + 0.349F;
    	this.KneeRight.rotateAngleZ = -0.1F * -MathHelper.sin(0.65F * ageInTicks) - 0.349F;
    }
}
