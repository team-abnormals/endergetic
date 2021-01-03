package com.minecraftabnormals.endergetic.client.renderers.entity.eetle;

import com.minecraftabnormals.endergetic.client.models.eetle.LeetleModel;
import com.minecraftabnormals.endergetic.client.renderers.entity.layer.EetleEmissiveLayer;
import com.minecraftabnormals.endergetic.common.entities.eetle.AbstractEetleEntity;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public abstract class AbstractEetleRenderer<E extends AbstractEetleEntity> extends MobRenderer<E, EntityModel<E>> {
	private static final ResourceLocation LEETLE_TEXTURE = new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/eetle/leetle.png");
	private final LeetleModel<E> leetleModel = new LeetleModel<>();
	private final EntityModel<E> adultModel;
	private final float adultShadowSize;

	public AbstractEetleRenderer(EntityRendererManager renderManager, EntityModel<E> adultModel, ResourceLocation emissiveAdultTexture, float adultShadowSize) {
		super(renderManager, adultModel, adultShadowSize);
		this.addLayer(new EetleEmissiveLayer<>(this, emissiveAdultTexture));
		this.adultModel = adultModel;
		this.adultShadowSize = adultShadowSize;
	}

	@Override
	public void render(E entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		this.shadowSize = this.adultShadowSize;
		this.entityModel = this.adultModel;
		if (entity.isChild()) {
			this.shadowSize = 0.7F;
			this.entityModel = this.leetleModel;
		}
		super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Override
	public ResourceLocation getEntityTexture(E entity) {
		if (entity.isChild()) {
			return LEETLE_TEXTURE;
		}
		return this.getAdultTexture();
	}

	@Nonnull
	protected abstract ResourceLocation getAdultTexture();
}
