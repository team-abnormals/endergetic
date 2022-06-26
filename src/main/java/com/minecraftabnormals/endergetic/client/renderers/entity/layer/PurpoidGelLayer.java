package com.minecraftabnormals.endergetic.client.renderers.entity.layer;

import com.minecraftabnormals.endergetic.client.models.purpoid.PurpoidGelModel;
import com.minecraftabnormals.endergetic.client.models.purpoid.PurpoidModel;
import com.minecraftabnormals.endergetic.client.renderers.entity.PurpoidRenderer;
import com.minecraftabnormals.endergetic.common.entities.purpoid.PurpoidEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.teamabnormals.blueprint.client.BlueprintRenderTypes;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;

public class PurpoidGelLayer extends RenderLayer<PurpoidEntity, PurpoidModel> {
	private final PurpoidGelModel gelModel;

	public PurpoidGelLayer(RenderLayerParent<PurpoidEntity, PurpoidModel> parent, EntityModelSet entityModelSet) {
		super(parent);
		this.gelModel = new PurpoidGelModel(entityModelSet.bakeLayer(PurpoidGelModel.LOCATION));
	}

	@Override
	public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, PurpoidEntity purpoidEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		PurpoidGelModel gelModel = this.gelModel;
		gelModel.setupAnim(purpoidEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		gelModel.parentToHead(this.getParentModel().head);
		int overlay = LivingEntityRenderer.getOverlayCoords(purpoidEntity, 0.0F);
		gelModel.animateModel(purpoidEntity, partialTicks);
		gelModel.renderToBuffer(matrixStackIn, bufferIn.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(purpoidEntity))), packedLightIn, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
		gelModel.renderToBuffer(matrixStackIn, bufferIn.getBuffer(BlueprintRenderTypes.getUnshadedCutoutEntity(PurpoidRenderer.EMISSIVE_TEXTURE, true)), 240, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
	}
}
