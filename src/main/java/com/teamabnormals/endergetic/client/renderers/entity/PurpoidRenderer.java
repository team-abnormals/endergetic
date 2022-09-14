package com.teamabnormals.endergetic.client.renderers.entity;

import com.teamabnormals.endergetic.client.models.purpoid.PurpModel;
import com.teamabnormals.endergetic.client.models.purpoid.PurpazoidModel;
import com.teamabnormals.endergetic.client.models.purpoid.PurpoidModel;
import com.teamabnormals.endergetic.client.renderers.entity.layer.PurpoidEmissiveLayer;
import com.teamabnormals.endergetic.client.renderers.entity.layer.PurpoidGelLayer;
import com.teamabnormals.endergetic.common.entities.purpoid.PurpoidEntity;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import com.mojang.blaze3d.vertex.PoseStack;
import com.teamabnormals.blueprint.client.BlueprintRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import com.mojang.math.Vector3f;
import net.minecraft.ChatFormatting;

import javax.annotation.Nullable;

public class PurpoidRenderer extends MobRenderer<PurpoidEntity, PurpoidModel> {
	private static final ResourceLocation[] TEXTURES = new ResourceLocation[]{
			new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/purpoid/purpoid.png"),
			new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/purpoid/purp.png"),
			new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/purpoid/purpazoid.png"),
	};
	private final PurpoidModel[] models;

	public PurpoidRenderer(EntityRendererProvider.Context context) {
		super(context, new PurpoidModel(context.bakeLayer(PurpoidModel.LOCATION)), 0.6F);
		this.addLayer(new PurpoidEmissiveLayer(this));
		this.addLayer(new PurpoidGelLayer(this, context.getModelSet()));
		this.models = new PurpoidModel[] {
				this.getModel(),
				new PurpModel(context.bakeLayer(PurpModel.LOCATION)),
				new PurpModel(context.bakeLayer(PurpazoidModel.LOCATION))
		};
	}

	@Override
	public void render(PurpoidEntity purpoid, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn) {
		this.model = this.models[purpoid.getSize().ordinal()];
		this.shadowRadius = 0.6F * purpoid.getSize().getScale();
		super.render(purpoid, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
	}

	@Override
	protected void setupRotations(PurpoidEntity purpoid, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTicks) {
		Entity ridingEntity = purpoid.getVehicle();
		if (ridingEntity != null) {
			if (ridingEntity instanceof LivingEntity livingEntity) {
				poseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F - Mth.rotLerp(partialTicks, livingEntity.yHeadRotO, livingEntity.yHeadRot)));
			} else {
				poseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F - rotationYaw));
			}
		} else {
			Vec3 pull = purpoid.getPull(partialTicks);

			double pulledX = Mth.lerp(partialTicks, purpoid.xo, purpoid.getX()) - pull.x();
			double pulledY = Mth.lerp(partialTicks, purpoid.yo, purpoid.getY()) - pull.y();
			double pulledZ = Mth.lerp(partialTicks, purpoid.zo, purpoid.getZ()) - pull.z();

			float rotationOffset = 0.5F * purpoid.getSize().getScale();
			float yRot = (float) Mth.atan2(pulledZ, pulledX);
			poseStack.translate(0.0F, rotationOffset, 0.0F);
			poseStack.mulPose(Vector3f.YP.rotation(-yRot));
			poseStack.mulPose(Vector3f.ZP.rotation((float) ((Mth.atan2(Mth.sqrt((float) (pulledX * pulledX + pulledZ * pulledZ)), -pulledY)) - Math.PI)));
			poseStack.mulPose(Vector3f.YP.rotation(yRot));
			poseStack.translate(0.0F, -rotationOffset, 0.0F);
		}
		if (purpoid.hasCustomName()) {
			String name = ChatFormatting.stripFormatting(purpoid.getName().getString());
			if (name.equals("Dinnerbone") || name.equals("Grumm")) {
				poseStack.translate(0.0D, purpoid.getBbHeight() + 0.1D, 0.0D);
				poseStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
			}
		}
	}

	@Nullable
	@Override
	protected RenderType getRenderType(PurpoidEntity purpoidEntity, boolean p_230496_2_, boolean p_230496_3_, boolean p_230496_4_) {
		ResourceLocation texture = this.getTextureLocation(purpoidEntity);
		if (p_230496_3_) {
			return RenderType.itemEntityTranslucentCull(texture);
		} else if (p_230496_2_) {
			return BlueprintRenderTypes.getUnshadedCutoutEntity(texture, true);
		} else {
			return p_230496_4_ ? RenderType.outline(texture) : null;
		}
	}

	@Override
	public ResourceLocation getTextureLocation(PurpoidEntity entity) {
		return TEXTURES[entity.getSize().ordinal()];
	}
}
