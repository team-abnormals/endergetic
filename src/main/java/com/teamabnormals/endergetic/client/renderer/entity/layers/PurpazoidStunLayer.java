package com.teamabnormals.endergetic.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.teamabnormals.endergetic.client.model.purpoid.PurpoidGelModel;
import com.teamabnormals.endergetic.client.model.purpoid.PurpoidModel;
import com.teamabnormals.endergetic.client.renderer.entity.PurpoidRenderer;
import com.teamabnormals.endergetic.common.entity.purpoid.Purpoid;
import com.teamabnormals.endergetic.common.entity.purpoid.PurpoidSize;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;

public class PurpazoidStunLayer extends RenderLayer<Purpoid, PurpoidModel> {
	private final static Vec2[] PURP_VERTEXES = new Vec2[]{
			new Vec2(0.0F, -14.4F),
			new Vec2(-14.4F, 9.6F),
			new Vec2(14.4F, 9.6F),
	};
	private final PurpoidModel purpModel;
	private final PurpoidGelModel gelModel;

	public PurpazoidStunLayer(RenderLayerParent<Purpoid, PurpoidModel> parent, PurpoidModel purpModel, PurpoidGelModel gelModel) {
		super(parent);
		this.purpModel = purpModel;
		this.gelModel = gelModel;
	}

	@Override
	public void render(PoseStack stack, MultiBufferSource bufferSource, int packedLightIn, Purpoid purpoid, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		float stunTimerHalfCycles = (purpoid.getStunTimer() - partialTicks) / 20.0F;
		float stunTimerMultiplier = Math.min(stunTimerHalfCycles, 1.0F);
		if (stunTimerMultiplier <= 0.0F || purpoid.getSize() != PurpoidSize.PURPAZOID) return;

		//Prepare models
		PurpoidModel model = this.purpModel;
		model.reset();
		PurpoidGelModel gelModel = this.gelModel;
		gelModel.reset();
		gelModel.parentToHead(model.head);

		stack.pushPose();
		stack.translate(0.0F, -2.75F, 0.0F);
		stack.mulPose(Vector3f.YP.rotation(0.20943951023F * ageInTicks));

		ModelPart gelLayer = gelModel.gelLayer;
		ModelPart head = model.head;
		float originalHeadZ = head.z;
		float originalHeadX = head.x;
		int overlay = OverlayTexture.pack(0, 10);
		float inAndOutProgress = 1.0F - 2.0F * Mth.abs(stunTimerHalfCycles - Mth.floor(stunTimerHalfCycles + 0.5F));
		float alpha = stunTimerMultiplier * (0.1F + 0.3F * (inAndOutProgress * inAndOutProgress));
		float rotation = 0.10471975512F * ageInTicks;
		RenderType nonEmissiveRenderType = RenderType.entityTranslucent(PurpoidRenderer.getTexture(1));
		RenderType emissiveRenderType = RenderType.entityTranslucent(PurpoidEmissiveLayer.TEXTURES[1]);
		for (int i = 0; i < PURP_VERTEXES.length; i++) {
			Vec2 vertex = PURP_VERTEXES[i];
			gelLayer.z = head.z = originalHeadZ + vertex.y;
			gelLayer.x = head.x = originalHeadX + vertex.x;
			gelLayer.yRot = head.yRot = rotation - i * 2.0944F;
			VertexConsumer nonEmissiveBuffer = bufferSource.getBuffer(nonEmissiveRenderType);
			model.renderToBuffer(stack, nonEmissiveBuffer, 240, overlay, 1.0F, 1.0F, 1.0F, alpha);
			gelModel.renderToBuffer(stack, nonEmissiveBuffer, 240, overlay, 1.0F, 1.0F, 1.0F, alpha);
			gelModel.renderToBuffer(stack, bufferSource.getBuffer(emissiveRenderType), 240, overlay, 1.0F, 1.0F, 1.0F, alpha);
		}

		stack.popPose();
	}
}
