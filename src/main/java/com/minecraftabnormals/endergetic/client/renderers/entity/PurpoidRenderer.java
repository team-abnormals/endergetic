package com.minecraftabnormals.endergetic.client.renderers.entity;

import com.minecraftabnormals.endergetic.client.EERenderTypes;
import com.minecraftabnormals.endergetic.client.models.purpoid.PurpoidModel;
import com.minecraftabnormals.endergetic.client.renderers.entity.layer.EmissiveLayerRenderer;
import com.minecraftabnormals.endergetic.client.renderers.entity.layer.PurpoidGelLayer;
import com.minecraftabnormals.endergetic.common.entities.purpoid.PurpoidEntity;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

import javax.annotation.Nullable;

public class PurpoidRenderer extends MobRenderer<PurpoidEntity, PurpoidModel> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/purpoid/purpoid.png");
	public static final ResourceLocation EMISSIVE_TEXTURE = new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/purpoid/purpoid_emissive.png");

	public PurpoidRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, new PurpoidModel(), 0.6F);
		this.addLayer(new EmissiveLayerRenderer<>(this, EMISSIVE_TEXTURE));
		this.addLayer(new PurpoidGelLayer(this));
	}

	@Override
	public void render(PurpoidEntity purpoid, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		this.shadowSize = 0.6F * purpoid.getSize().getScale();
		super.render(purpoid, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Override
	protected void preRenderCallback(PurpoidEntity purpoid, MatrixStack matrixStack, float partialTickTime) {
		float scale = purpoid.getSize().getScale();
		matrixStack.scale(scale, scale, scale);
	}

	@Override
	protected void applyRotations(PurpoidEntity purpoid, MatrixStack matrixStack, float ageInTicks, float rotationYaw, float partialTicks) {
		Vector3d pull = purpoid.getPull(partialTicks);

		double pulledX = MathHelper.lerp(partialTicks, purpoid.prevPosX, purpoid.getPosX()) - pull.getX();
		double pulledY = MathHelper.lerp(partialTicks, purpoid.prevPosY, purpoid.getPosY()) - pull.getY();
		double pulledZ = MathHelper.lerp(partialTicks, purpoid.prevPosZ, purpoid.getPosZ()) - pull.getZ();

		float rotationOffset = 0.5F * purpoid.getSize().getScale();
		float yRot = (float) MathHelper.atan2(pulledZ, pulledX);
		matrixStack.translate(0.0F, rotationOffset, 0.0F);
		matrixStack.rotate(Vector3f.YP.rotation(-yRot));
		matrixStack.rotate(Vector3f.ZP.rotation((float) ((MathHelper.atan2(MathHelper.sqrt(pulledX * pulledX + pulledZ * pulledZ), -pulledY)) - Math.PI)));
		matrixStack.rotate(Vector3f.YP.rotation(yRot));
		matrixStack.translate(0.0F, -rotationOffset, 0.0F);
	}

	@Nullable
	@Override
	protected RenderType func_230496_a_(PurpoidEntity purpoidEntity, boolean p_230496_2_, boolean p_230496_3_, boolean p_230496_4_) {
		ResourceLocation texture = this.getEntityTexture(purpoidEntity);
		if (p_230496_3_) {
			return RenderType.getItemEntityTranslucentCull(texture);
		} else if (p_230496_2_) {
			return EERenderTypes.createUnshadedEntity(texture);
		} else {
			return p_230496_4_ ? RenderType.getOutline(texture) : null;
		}
	}

	@Override
	public ResourceLocation getEntityTexture(PurpoidEntity entity) {
		return TEXTURE;
	}
}
