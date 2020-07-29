package com.minecraftabnormals.endergetic.client.render.entity.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.teamabnormals.abnormals_core.client.ACRenderTypes;
import com.teamabnormals.abnormals_core.client.ClientInfo;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class EmissiveLayerRenderer<E extends Entity, M extends EntityModel<E>> extends LayerRenderer<E, M> {
	private final ResourceLocation texture;
	
	public EmissiveLayerRenderer(IEntityRenderer<E, M> entityRendererIn, ResourceLocation texture) {
		super(entityRendererIn);
		this.texture = texture;
	}
	
	@Override
	public void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight, E entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		ClientInfo.MINECRAFT.getTextureManager().bindTexture(this.texture);
		IVertexBuilder ivertexbuilder = buffer.getBuffer(ACRenderTypes.getEmissiveEntity(this.texture));
		
		this.getEntityModel().setRotationAngles(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		this.getEntityModel().render(matrixStack, ivertexbuilder, 240, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
	}
}