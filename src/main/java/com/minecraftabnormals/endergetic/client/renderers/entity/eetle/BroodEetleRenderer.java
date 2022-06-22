package com.minecraftabnormals.endergetic.client.renderers.entity.eetle;

import com.minecraftabnormals.endergetic.client.models.eetle.BroodEetleModel;
import com.minecraftabnormals.endergetic.client.renderers.entity.layer.EmissiveLayerRenderer;
import com.minecraftabnormals.endergetic.common.entities.eetle.BroodEetleEntity;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

public class BroodEetleRenderer extends MobRenderer<BroodEetleEntity, BroodEetleModel> {
	private static final ResourceLocation[] TEXTURES = getStageTextures();
	private static final ResourceLocation EMISSIVE_TEXTURE = new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/eetle/brood/brood_eetle_emissive.png");

	public BroodEetleRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, new BroodEetleModel(), 1.0F);
		this.addLayer(new EmissiveLayerRenderer<>(this, EMISSIVE_TEXTURE));
	}

	@Override
	protected void setupRotations(BroodEetleEntity broodEetle, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
		matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(180.0F - rotationYaw));
		if (broodEetle.hasCustomName()) {
			String name = TextFormatting.stripFormatting(broodEetle.getName().getString());
			if (name.equals("Dinnerbone") || name.equals("Grum")) {
				matrixStackIn.translate(0.0D, broodEetle.getBbHeight() + 0.1D, 0.0D);
				matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
			}
		}
	}

	@Override
	public ResourceLocation getTextureLocation(BroodEetleEntity entity) {
		return TEXTURES[entity.getHealthStage().ordinal()];
	}

	private static ResourceLocation[] getStageTextures() {
		List<ResourceLocation> textures = new ArrayList<>();
		for (BroodEetleEntity.HealthStage stage : BroodEetleEntity.HealthStage.values()) {
			textures.add(new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/eetle/brood/brood_eetle_" + stage.ordinal() + ".png"));
		}
		return textures.toArray(new ResourceLocation[0]);
	}
}
