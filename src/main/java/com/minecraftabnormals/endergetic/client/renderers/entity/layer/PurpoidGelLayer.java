package com.minecraftabnormals.endergetic.client.renderers.entity.layer;

import com.minecraftabnormals.abnormals_core.client.ACRenderTypes;
import com.minecraftabnormals.endergetic.client.models.purpoid.PurpoidGelModel;
import com.minecraftabnormals.endergetic.client.models.purpoid.PurpoidModel;
import com.minecraftabnormals.endergetic.client.renderers.entity.PurpoidRenderer;
import com.minecraftabnormals.endergetic.common.entities.purpoid.PurpoidEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;

public class PurpoidGelLayer extends LayerRenderer<PurpoidEntity, PurpoidModel> {
	private final PurpoidGelModel gelModel = new PurpoidGelModel();

	public PurpoidGelLayer(IEntityRenderer<PurpoidEntity, PurpoidModel> entityRendererIn) {
		super(entityRendererIn);
	}

	@Override
	public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, PurpoidEntity purpoidEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		PurpoidGelModel gelModel = this.gelModel;
		gelModel.setLivingAnimations(purpoidEntity, limbSwing, limbSwingAmount, partialTicks);
		gelModel.setRotationAngles(purpoidEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		gelModel.parentToHead(this.getEntityModel().head);
		int overlay = LivingRenderer.getPackedOverlay(purpoidEntity, 0.0F);
		gelModel.animateModel(purpoidEntity);
		gelModel.render(matrixStackIn, bufferIn.getBuffer(RenderType.getEntityTranslucent(this.getEntityTexture(purpoidEntity))), packedLightIn, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
		gelModel.render(matrixStackIn, bufferIn.getBuffer(ACRenderTypes.getEmissiveEntity(PurpoidRenderer.EMISSIVE_TEXTURE)), 240, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
	}
}
