package endergeticexpansion.client.render;

import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.util.math.MathHelper;

public class ModelRenderHelper {

	public void bob(RendererModel box, float speed, float degree, boolean invert, float offset, float weight, float limbSwing, float limbSwingAmount) {
        box.rotationPointY = this.computeAnimation(speed, degree, invert, offset, weight, limbSwing, limbSwingAmount);
    }

    public void walk(RendererModel box, float speed, float degree, boolean invert, float offset, float weight, float limbSwing, float limbSwingAmount) {
        box.rotateAngleX = this.computeAnimation(speed, degree, invert, offset, weight, limbSwing, limbSwingAmount);
    }

    public void swing(RendererModel box, float speed, float degree, boolean invert, float offset, float weight, float limbSwing, float limbSwingAmount) {
        box.rotateAngleY = this.computeAnimation(speed, degree, invert, offset, weight, limbSwing, limbSwingAmount);
    }

    public void flap(RendererModel box, float speed, float degree, boolean invert, float offset, float weight, float limbSwing, float limbSwingAmount) {
        box.rotateAngleZ = this.computeAnimation(speed, degree, invert, offset, weight, limbSwing, limbSwingAmount);
    }
    
    public float computeAnimation(float speed, float degree, boolean invert, float offset, float weight, float limbSwing, float limbSwingAmount) {
        float theta = limbSwing * speed + offset;
        float scaledWeight = weight * limbSwingAmount;
        float rotation = (MathHelper.cos(theta) * degree * limbSwingAmount) + scaledWeight;
        return invert ? -rotation : rotation;
    }
    
}
