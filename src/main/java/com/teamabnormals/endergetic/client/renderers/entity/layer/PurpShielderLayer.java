package com.teamabnormals.endergetic.client.renderers.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamabnormals.endergetic.client.models.purpoid.PurpoidGelModel;
import com.teamabnormals.endergetic.client.models.purpoid.PurpoidModel;
import com.teamabnormals.endergetic.client.renderers.entity.PurpoidRenderer;
import com.teamabnormals.endergetic.common.entities.purpoid.PurpoidEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;

public class PurpShielderLayer extends RenderLayer<PurpoidEntity, PurpoidModel> {
	private final PurpoidModel purpazoidModel;
	private final PurpoidGelModel gelModel;

	public PurpShielderLayer(RenderLayerParent<PurpoidEntity, PurpoidModel> parent, PurpoidModel purpazoidModel, PurpoidGelModel gelModel) {
		super(parent);
		this.purpazoidModel = purpazoidModel;
		this.gelModel = gelModel;
	}

	@Override
	public void render(PoseStack stack, MultiBufferSource bufferIn, int packedLightIn, PurpoidEntity purpoid, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (!purpoid.isBaby() || purpoid.getIDOfShieldedMommy() < 0) return;
		PurpoidModel model = this.purpazoidModel;
		model.setupAnim(purpoid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		PurpoidGelModel gelModel = this.gelModel;
		gelModel.parentToHead(model.head);
		gelModel.setupAnim(purpoid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		stack.pushPose();
		stack.translate(0.0F, 0.8125F, 0.0F);
		float inAndOut = 0.05F * Mth.abs(Mth.sin(0.15707963267F * ageInTicks));
		float scale = 0.5F + inAndOut;
		stack.scale(scale, scale, scale);
		float alpha = 0.15F + inAndOut;
		VertexConsumer nonEmissiveBuffer = bufferIn.getBuffer(RenderType.entityTranslucent(PurpoidRenderer.getTexture(2)));
		model.renderToBuffer(stack, nonEmissiveBuffer, 240, OverlayTexture.pack(0, 10), 1.0F, 1.0F, 1.0F, alpha);
		gelModel.renderToBuffer(stack, nonEmissiveBuffer, 240, OverlayTexture.pack(0, 10), 1.0F, 1.0F, 1.0F, alpha);
		gelModel.renderToBuffer(stack, bufferIn.getBuffer(RenderType.entityTranslucent(PurpoidEmissiveLayer.TEXTURES[2])), 240, OverlayTexture.pack(0, 10), 1.0F, 1.0F, 1.0F, alpha);
		stack.popPose();
	}
}
