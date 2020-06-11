package endergeticexpansion.client.model.armor;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.teamabnormals.abnormals_core.client.ClientInfo;

import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;

/**
 * ModelBoofloVest - Endergized
 * Created using Tabula 7.0.0
 */
public class BoofloVestModel<T extends LivingEntity> extends BipedModel<T> {
    public ModelRenderer strap;
    public ModelRenderer boofer;
    private T entity;

    public BoofloVestModel(float modelSize) {
    	super(modelSize, 0.0F, 64, 64);
        this.strap = new ModelRenderer(this, 16, 16);
        this.strap.setRotationPoint(-4.0F, 0.0F, -2.0F);
        this.strap.addBox(0.0F, 0.0F, 0.0F, 8, 11, 4, 0.0F);
        this.boofer = new ModelRenderer(this, 0, 32);
        this.boofer.setRotationPoint(0.0F, 2.0F, -2.0F);
        this.boofer.addBox(0.0F, 0.0F, 0.0F, 8, 8, 8, 0.0F);
        this.strap.addChild(this.boofer);
    }
    
    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder vertexBuilder, int p_225598_3_, int p_225598_4_, float p_225598_5_, float p_225598_6_, float p_225598_7_, float f5) {
    	super.render(matrixStack, vertexBuilder, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, f5);
    	
    	matrixStack.push();
    	matrixStack.scale(1.25F, 1.25F, 1.25F);
    	
    	if(this.entity.isShiftKeyDown()) {
    		matrixStack.translate(-0.25F, -0.05F, -0.125F);
    	} else {
    		matrixStack.translate(-0.25F, -0.05F, -0.125F);
    	}
    	
    	
    	//Temporary get around for lighting bug with forge armor model
    	float partialTicks = ClientInfo.getPartialTicks();
    	int light = LightTexture.packLight(this.getBlockLight(this.entity, partialTicks), this.entity.world.getLightFor(LightType.SKY, new BlockPos(this.entity.getEyePosition(partialTicks))));
    	
    	this.strap.render(matrixStack, vertexBuilder, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStack.pop();
    }
    
    @Override
    public void setRotationAngles(T entity, float p_225597_2_, float p_225597_3_, float p_225597_4_, float p_225597_5_, float p_225597_6_) {
    	this.entity = entity;
    	super.setRotationAngles(entity, p_225597_2_, p_225597_3_, p_225597_4_, p_225597_5_, p_225597_6_);
    	
    	this.strap.copyModelAngles(this.bipedBody);
    }
    
    private int getBlockLight(T entityIn, float partialTicks) {
    	return entityIn.isBurning() ? 15 : entityIn.world.getLightFor(LightType.BLOCK, new BlockPos(entityIn.getEyePosition(partialTicks)));
    }
}