package endergeticexpansion.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import endergeticexpansion.client.model.bolloom.BolloomFruitModel;
import endergeticexpansion.common.entities.bolloom.BolloomFruitEntity;
import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;

public class BolloomFruitRenderer extends EntityRenderer<BolloomFruitEntity> {
	public BolloomFruitModel<BolloomFruitEntity> model;
	
	public BolloomFruitRenderer(EntityRendererManager renderManager) {
		super(renderManager);
		this.model = new BolloomFruitModel<BolloomFruitEntity>();
    }

	@Override
	public ResourceLocation getEntityTexture(BolloomFruitEntity entity) {
		return new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/bolloom_fruit.png");
	}
	
	@Override
	public void render(BolloomFruitEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int packedLightIn) {
		float[] angles = entity.getVineAnimation(partialTicks);
		this.model.vine_x.rotateAngleX = angles[0];
		this.model.vine_x.rotateAngleY = angles[1];
		this.model.setRotationAngles(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
		matrixStack.push();
		matrixStack.translate(0.0F, 1.5F, 0.0F);
		matrixStack.rotate(Vector3f.XP.rotationDegrees(180.0F));
		IVertexBuilder ivertexbuilder = bufferIn.getBuffer(this.model.getRenderType(this.getEntityTexture(entity)));
    	this.model.render(matrixStack, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		matrixStack.pop();
		super.render(entity, entityYaw, partialTicks, matrixStack, bufferIn, packedLightIn);
	}
}