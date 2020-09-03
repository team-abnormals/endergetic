package com.minecraftabnormals.endergetic.client.render.entity;

import com.minecraftabnormals.endergetic.client.model.bolloom.BolloomBalloonModel;
import com.minecraftabnormals.endergetic.common.entities.bolloom.BolloomBalloonEntity;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

public final class BolloomBalloonRenderer extends EntityRenderer<BolloomBalloonEntity> {
	private static final Minecraft MC = Minecraft.getInstance();
	public static final ResourceLocation DEFAULT_TEXTURE = new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/balloon/bolloom_balloon.png");

	public BolloomBalloonModel<BolloomBalloonEntity> model;
	
	public BolloomBalloonRenderer(EntityRendererManager p_i46179_1_) {
		super(p_i46179_1_);
		this.model = new BolloomBalloonModel<>();
	}
	
	@Override
	public void render(BolloomBalloonEntity balloon, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int packedLightIn) {
		float[] angles = balloon.getVineAnimation(partialTicks);
		this.model.x_string.rotateAngleX = angles[0];
		this.model.x_string.rotateAngleY = angles[1];
		
		matrixStack.push();
		matrixStack.translate(0.0F, 1.5F, 0.0F);
		matrixStack.rotate(Vector3f.XP.rotationDegrees(180.0F));
		
		IVertexBuilder ivertexbuilder = bufferIn.getBuffer(this.model.getRenderType(this.getEntityTexture(balloon)));
    	this.model.render(matrixStack, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		
    	matrixStack.pop();
		super.render(balloon, entityYaw, partialTicks, matrixStack, bufferIn, packedLightIn);
	}
	
	@Override
	public boolean shouldRender(BolloomBalloonEntity balloon, ClippingHelper camera, double camX, double camY, double camZ) {
		ClientPlayerEntity player = MC.player;
		return balloon.getAttachedEntity() != player || MC.gameSettings.thirdPersonView != 0 || player.rotationPitch < -45.0F;
	}
	
	@Override
	public ResourceLocation getEntityTexture(BolloomBalloonEntity balloon) {
		return balloon.getColor().texture;
	}
}