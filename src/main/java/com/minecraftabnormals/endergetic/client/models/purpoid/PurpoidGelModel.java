package com.minecraftabnormals.endergetic.client.models.purpoid;

import com.minecraftabnormals.abnormals_core.core.endimator.entity.EndimatorEntityModel;
import com.minecraftabnormals.abnormals_core.core.endimator.entity.EndimatorModelRenderer;
import com.minecraftabnormals.endergetic.common.entities.purpoid.PurpoidEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.util.math.MathHelper;

public class PurpoidGelModel extends EndimatorEntityModel<PurpoidEntity> {
	public EndimatorModelRenderer gelLayer;

	public PurpoidGelModel() {
		this.texWidth = 64;
		this.texHeight = 96;
		this.gelLayer = new EndimatorModelRenderer(this, 0, 0);
		this.gelLayer.setPos(0.0F, 1.0F, 0.0F);
		this.gelLayer.addBox(-8.0F, -16.0F, -8.0F, 16, 16, 16, 0.0F);
		this.setDefaultBoxValues();
	}

	public void parentToHead(EndimatorModelRenderer head) {
		EndimatorModelRenderer gelLayer = this.gelLayer;
		gelLayer.copyFrom(head);
		gelLayer.y += 1.0F;
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.gelLayer.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public void setupAnim(PurpoidEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		if (entity.isNoEndimationPlaying()) {
			float scaleOffset = MathHelper.sin(limbSwing * 0.6F) * Math.min(0.2F, limbSwingAmount);
			float horizontalScaleOffset = Math.max(-0.05F, scaleOffset);
			this.gelLayer.setScale(1.0F + horizontalScaleOffset, 1.0F - scaleOffset * 0.5F, 1.0F + horizontalScaleOffset);
		}
	}

	@Override
	public void animateModel(PurpoidEntity endimatedEntity) {
		super.animateModel(endimatedEntity);

		if (this.tryToPlayEndimation(PurpoidEntity.TELEPORT_TO_ANIMATION) || this.tryToPlayEndimation(PurpoidEntity.FAST_TELEPORT_TO_ANIMATION)) {
			this.startKeyframe(5);
			this.scale(this.gelLayer, 1.3F, 1.3F, 1.3F);
			this.endKeyframe();

			this.startKeyframe(5);
			this.scale(this.gelLayer, -1.0F, -1.0F, -1.0F);
			this.endKeyframe();

			this.setStaticKeyframe(8);
		} else if (this.tryToPlayEndimation(PurpoidEntity.TELEPORT_FROM_ANIMATION)) {
			this.startKeyframe(5);
			this.scale(this.gelLayer, 1.3F, 1.3F, 1.3F);
			this.endKeyframe();

			this.resetKeyframe(5);
		} else if (this.tryToPlayEndimation(PurpoidEntity.DEATH_ANIMATION)) {
			this.startKeyframe(5);
			this.scale(this.gelLayer, -0.4F, -0.4F, -0.4F);
			this.endKeyframe();

			this.startKeyframe(5);
			this.scale(this.gelLayer, 1.15F, 1.0F, 1.15F);
			this.endKeyframe();

			this.setStaticKeyframe(10);
		} else if (this.tryToPlayEndimation(PurpoidEntity.TELEFRAG_ANIMATION)) {
			this.startKeyframe(5);
			this.scale(this.gelLayer, 1.0F, 1.0F, 1.0F);
			this.endKeyframe();

			this.resetKeyframe(5);
		}
	}
}
