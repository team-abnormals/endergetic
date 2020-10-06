package com.minecraftabnormals.endergetic.client.renderers.entity.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.teamabnormals.abnormals_core.client.ACRenderTypes;
import com.teamabnormals.abnormals_core.client.ClientInfo;
import com.minecraftabnormals.endergetic.common.entities.puffbug.PuffBugEntity;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;

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
	private static final ResourceLocation[] TEXTURES = new ResourceLocation[]{
			new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/puffbug/puffbug_inflated_overlay.png"),
			new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/puffbug/puffbug_inflated_overlay_grayscale.png"),
			new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/puffbug/puffbug_inflated_levitation_overlay.png"),
	};

	public LayerRendererPuffBugGlow(IEntityRenderer<T, M> entityRenderer) {
		super(entityRenderer);
	}

	@Override
	public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T puffbug, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (!puffbug.isInflated()) return;

		boolean colorFlag = puffbug.getColor() != -1 && !isLeviationOnlyEffect(puffbug);

		float[] rgb = new float[]{
				colorFlag ? (float) ((puffbug.getColor() >> 16 & 255) / 255.0D) : 1.0F,
				colorFlag ? (float) ((puffbug.getColor() >> 8 & 255) / 255.0D) : 1.0F,
				colorFlag ? (float) ((puffbug.getColor() & 255) / 255.0D) : 1.0F,
		};

		ClientInfo.MINECRAFT.getTextureManager().bindTexture(this.getTexture(puffbug));
		IVertexBuilder ivertexbuilder = bufferIn.getBuffer(ACRenderTypes.getEmissiveEntity(this.getTexture(puffbug)));

		this.getEntityModel().setRotationAngles(puffbug, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		this.getEntityModel().render(matrixStackIn, ivertexbuilder, 240, OverlayTexture.NO_OVERLAY, rgb[0], rgb[1], rgb[2], 1.0F);
	}

	private ResourceLocation getTexture(PuffBugEntity puffbug) {
		if (this.isLeviationOnlyEffect(puffbug)) {
			return TEXTURES[2];
		}
		return puffbug.getColor() != -1 ? TEXTURES[1] : TEXTURES[0];
	}

	private boolean isLeviationOnlyEffect(PuffBugEntity bug) {
		if (bug.getColor() == 13565951) {
			return true;
		}
		return false;
	}
}