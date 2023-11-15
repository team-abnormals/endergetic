package com.teamabnormals.endergetic.client.renderer.entity.eetle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.teamabnormals.endergetic.client.model.eetle.BroodEetleModel;
import com.teamabnormals.endergetic.client.renderer.entity.layers.EmissiveLayerRenderer;
import com.teamabnormals.endergetic.common.entity.eetle.BroodEetle;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class BroodEetleRenderer extends MobRenderer<BroodEetle, BroodEetleModel> {
	private static final ResourceLocation[] TEXTURES = getStageTextures();
	private static final ResourceLocation EMISSIVE_TEXTURE = new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/eetle/brood/brood_eetle_emissive.png");

	public BroodEetleRenderer(EntityRendererProvider.Context context) {
		super(context, new BroodEetleModel(context.bakeLayer(BroodEetleModel.LOCATION)), 1.0F);
		this.addLayer(new EmissiveLayerRenderer<>(this, EMISSIVE_TEXTURE));
	}

	@Override
	protected void setupRotations(BroodEetle broodEetle, PoseStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
		matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(180.0F - rotationYaw));
		if (broodEetle.hasCustomName()) {
			String name = ChatFormatting.stripFormatting(broodEetle.getName().getString());
			if (name.equals("Dinnerbone") || name.equals("Grum")) {
				matrixStackIn.translate(0.0D, broodEetle.getBbHeight() + 0.1D, 0.0D);
				matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
			}
		}
	}

	@Override
	public ResourceLocation getTextureLocation(BroodEetle entity) {
		return TEXTURES[entity.getHealthStage().ordinal()];
	}

	private static ResourceLocation[] getStageTextures() {
		List<ResourceLocation> textures = new ArrayList<>();
		for (BroodEetle.HealthStage stage : BroodEetle.HealthStage.values()) {
			textures.add(new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/eetle/brood/brood_eetle_" + stage.ordinal() + ".png"));
		}
		return textures.toArray(new ResourceLocation[0]);
	}
}
