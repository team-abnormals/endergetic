package com.minecraftabnormals.endergetic.client.renderers.entity.layer;

import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.teamabnormals.abnormals_core.client.ACRenderTypes;
import com.teamabnormals.abnormals_core.client.ClientInfo;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;

public final class BoofloEmissiveLayer<B extends BoofloEntity, M extends EntityModel<B>> extends LayerRenderer<B, M> {

	public BoofloEmissiveLayer(IEntityRenderer<B, M> entityRenderer) {
		super(entityRenderer);
	}

	@Override
	public void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight, B entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		ResourceLocation emissive = this.getEmissiveTexture(entity);
		ClientInfo.MINECRAFT.getTextureManager().bindTexture(emissive);
		IVertexBuilder ivertexbuilder = buffer.getBuffer(ACRenderTypes.getEmissiveEntity(emissive));

		this.getEntityModel().setRotationAngles(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		this.getEntityModel().render(matrixStack, ivertexbuilder, 240, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
	}

	private ResourceLocation getEmissiveTexture(B booflo) {
		return new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/booflo/booflo" + booflo.getNameSuffix() + "_glow_layer.png");
	}

}
