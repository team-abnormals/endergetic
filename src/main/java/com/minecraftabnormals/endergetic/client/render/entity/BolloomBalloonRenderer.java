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

public class BolloomBalloonRenderer extends EntityRenderer<BolloomBalloonEntity> {
	public static final ResourceLocation DEFAULT_TEXTURE = new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/balloon/bolloom_balloon.png");
	public static final ResourceLocation[] COLORS = new ResourceLocation[] {
		new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/balloon/white_bolloom_balloon.png"),
		new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/balloon/orange_bolloom_balloon.png"),
		new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/balloon/magenta_bolloom_balloon.png"),
		new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/balloon/light_blue_bolloom_balloon.png"),
		new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/balloon/yellow_bolloom_balloon.png"),
		new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/balloon/lime_bolloom_balloon.png"),
		new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/balloon/pink_bolloom_balloon.png"),
		new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/balloon/gray_bolloom_balloon.png"),
		new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/balloon/light_gray_bolloom_balloon.png"),
		new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/balloon/cyan_bolloom_balloon.png"),
		new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/balloon/purple_bolloom_balloon.png"),
		new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/balloon/blue_bolloom_balloon.png"),
		new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/balloon/brown_bolloom_balloon.png"),
		new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/balloon/green_bolloom_balloon.png"),
		new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/balloon/red_bolloom_balloon.png"),
		new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/balloon/black_bolloom_balloon.png"),
	};

	public BolloomBalloonModel<BolloomBalloonEntity> model;
	
	public BolloomBalloonRenderer(EntityRendererManager p_i46179_1_) {
		super(p_i46179_1_);
		this.model = new BolloomBalloonModel<BolloomBalloonEntity>();
	}
	
	@Override
	public void render(BolloomBalloonEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int packedLightIn) {
		float[] angles = entity.getVineAnimation(partialTicks);
		this.model.x_string.rotateAngleX = angles[0];
		this.model.x_string.rotateAngleY = angles[1];
		
		matrixStack.push();
		matrixStack.translate(0.0F, 1.5F, 0.0F);
		matrixStack.rotate(Vector3f.XP.rotationDegrees(180.0F));
		
		IVertexBuilder ivertexbuilder = bufferIn.getBuffer(this.model.getRenderType(this.getEntityTexture(entity)));
    	this.model.render(matrixStack, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		
    	matrixStack.pop();
		super.render(entity, entityYaw, partialTicks, matrixStack, bufferIn, packedLightIn);
	}
	
	@Override
	public boolean shouldRender(BolloomBalloonEntity balloon, ClippingHelper camera, double camX, double camY, double camZ) {
		if (balloon.getHideTime() > 0) return false;
		ClientPlayerEntity player = Minecraft.getInstance().player;
		return balloon.getRidingEntity() == player && Minecraft.getInstance().gameSettings.thirdPersonView == 0 ? player.rotationPitch < -45.0F : true;
	}
	
	@Override
	public ResourceLocation getEntityTexture(BolloomBalloonEntity balloon) {
		return balloon.getColor() == null ? DEFAULT_TEXTURE : COLORS[balloon.getColor().getId()];
	}
}