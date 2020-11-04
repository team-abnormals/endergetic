package com.minecraftabnormals.endergetic.client.renderers.entity.layer;

import com.minecraftabnormals.endergetic.common.entities.puffbug.PuffBugEntity;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.teamabnormals.abnormals_core.client.ACRenderTypes;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LayerRendererPuffBugGlow<T extends PuffBugEntity, M extends EntityModel<T>> extends LayerRenderer<T, M> {
	private static final ResourceLocation GRAYSCALE_TEXTURE = new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/puffbug/puffbug_inflated_overlay_grayscale.png");
	private static final ResourceLocation POLLINATED_TEXTURE = new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/puffbug/puffbug_inflated_levitation_overlay.png");
	private static final ResourceLocation DEFAULT_TEXTURE = new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/puffbug/puffbug_inflated_overlay.png");

	public LayerRendererPuffBugGlow(IEntityRenderer<T, M> entityRenderer) {
		super(entityRenderer);
	}

	@Override
	public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T puffbug, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (!puffbug.isInflated()) return;

		M model = this.getEntityModel();
		model.setRotationAngles(puffbug, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		int color = puffbug.getColor();
		switch (color) {
			case -1:
				model.render(matrixStackIn, bufferIn.getBuffer(ACRenderTypes.getEmissiveEntity(DEFAULT_TEXTURE)), 240, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
				break;
			case 13565951:
				model.render(matrixStackIn, bufferIn.getBuffer(ACRenderTypes.getEmissiveEntity(POLLINATED_TEXTURE)), 240, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
				break;
			default:
				model.render(matrixStackIn, bufferIn.getBuffer(ACRenderTypes.getEmissiveEntity(GRAYSCALE_TEXTURE)), 240, OverlayTexture.NO_OVERLAY, (color >> 16 & 255) / 255.0F, (color >> 8 & 255) / 255.0F, (color & 255) / 255.0F, 1.0F);
				break;
		}
	}
}