package com.teamabnormals.endergetic.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.teamabnormals.blueprint.client.BlueprintRenderTypes;
import com.teamabnormals.endergetic.client.model.purpoid.PurpModel;
import com.teamabnormals.endergetic.client.model.purpoid.PurpoidModel;
import com.teamabnormals.endergetic.client.renderer.entity.layers.PurpShielderLayer;
import com.teamabnormals.endergetic.client.renderer.entity.layers.PurpazoidStunLayer;
import com.teamabnormals.endergetic.client.renderer.entity.layers.PurpoidEmissiveLayer;
import com.teamabnormals.endergetic.client.renderer.entity.layers.PurpoidGelLayer;
import com.teamabnormals.endergetic.common.entity.purpoid.Purpoid;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import com.teamabnormals.endergetic.core.other.EEModelLayers;
import com.teamabnormals.endergetic.core.other.EEPlayableEndimations;
import com.teamabnormals.endergetic.core.other.EERenderTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import javax.annotation.Nullable;

public class PurpoidRenderer extends MobRenderer<Purpoid, PurpoidModel> {
	private static final ResourceLocation[] TEXTURES = new ResourceLocation[]{
			new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/purpoid/purpoid.png"),
			new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/purpoid/purp.png"),
			new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/purpoid/purpazoid.png"),
	};
	private final PurpoidModel[] models;

	public PurpoidRenderer(EntityRendererProvider.Context context) {
		super(context, new PurpoidModel(context.bakeLayer(EEModelLayers.PURPOID)), 0.6F);
		this.models = new PurpoidModel[]{
				this.getModel(),
				new PurpModel(context.bakeLayer(EEModelLayers.PURP)),
				new PurpModel(context.bakeLayer(EEModelLayers.PURPAZOID))
		};
		this.addLayer(new PurpoidEmissiveLayer(this));
		PurpoidGelLayer gelLayer = new PurpoidGelLayer(this, context.getModelSet());
		this.addLayer(gelLayer);
		this.addLayer(new PurpShielderLayer(this, this.models[2], gelLayer.getGelModel(2)));
		this.addLayer(new PurpazoidStunLayer(this, this.models[1], gelLayer.getGelModel(1)));
	}

	@Override
	public boolean shouldRender(Purpoid purpoid, Frustum frustum, double p_115470_, double p_115471_, double p_115472_) {
		return purpoid.getIDOfShieldedMommy() >= 0 || super.shouldRender(purpoid, frustum, p_115470_, p_115471_, p_115472_);
	}

	@Override
	public void render(Purpoid purpoid, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn) {
		this.model = this.models[purpoid.getSize().ordinal()];
		this.shadowRadius = 0.6F * purpoid.getSize().getScale();
		super.render(purpoid, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
		if (isInvisibleWhileTeleporting(purpoid)) return;
		Entity shieldedMommy = purpoid.level().getEntity(purpoid.getIDOfShieldedMommy());
		if (shieldedMommy instanceof Purpoid purpoidMommy && !isInvisibleWhileTeleporting(purpoidMommy)) {
			renderShieldingLine(purpoid, partialTicks, poseStack, bufferIn, purpoidMommy.getPosition(partialTicks), purpoidMommy.getStunTimer());
		}
	}

	@Override
	protected void setupRotations(Purpoid purpoid, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTicks) {
		Entity ridingEntity = purpoid.getVehicle();
		if (ridingEntity != null) {
			if (ridingEntity instanceof LivingEntity livingEntity) {
				poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - Mth.rotLerp(partialTicks, livingEntity.yHeadRotO, livingEntity.yHeadRot)));
			} else {
				poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - rotationYaw));
			}
		} else {
			Vec3 pull = purpoid.getPull(partialTicks);
			double pulledX = Mth.lerp(partialTicks, purpoid.xo, purpoid.getX()) - pull.x();
			double pulledY = Mth.lerp(partialTicks, purpoid.yo, purpoid.getY()) - pull.y();
			double pulledZ = Mth.lerp(partialTicks, purpoid.zo, purpoid.getZ()) - pull.z();

			Vector3f lookDir = new Vector3f((float) pulledX, (float) pulledY, (float) pulledZ);
			if (lookDir.lengthSquared() < 1e-6F) {
				lookDir.set(0.0F, 0.0F, 1.0F);
			} else {
				lookDir.normalize();
			}

			Vector3f defaultForward = new Vector3f(0.0F, 0.0F, -1.0F);
			Quaternionf rotation = new Quaternionf().rotationTo(defaultForward, lookDir);
			Quaternionf correction = new Quaternionf().rotateX(-Mth.HALF_PI);
			rotation.mul(correction);

			float rotationOffset = 0.5F * purpoid.getSize().getScale();
			poseStack.translate(0.0F, rotationOffset, 0.0F);
			poseStack.mulPose(rotation);
			poseStack.translate(0.0F, -rotationOffset, 0.0F);
		}
		if (purpoid.hasCustomName()) {
			String name = ChatFormatting.stripFormatting(purpoid.getName().getString());
			if (name.equals("Dinnerbone") || name.equals("Grumm")) {
				poseStack.translate(0.0D, purpoid.getBbHeight() + 0.1D, 0.0D);
				poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
			}
		}
	}

	@Nullable
	@Override
	protected RenderType getRenderType(Purpoid purpoid, boolean p_230496_2_, boolean p_230496_3_, boolean p_230496_4_) {
		ResourceLocation texture = this.getTextureLocation(purpoid);
		if (p_230496_3_) {
			return RenderType.itemEntityTranslucentCull(texture);
		} else if (p_230496_2_) {
			return BlueprintRenderTypes.getUnshadedCutoutEntity(texture, true);
		} else {
			return p_230496_4_ ? RenderType.outline(texture) : null;
		}
	}

	@Override
	public ResourceLocation getTextureLocation(Purpoid entity) {
		return getTexture(entity.getSize().ordinal());
	}

	public static ResourceLocation getTexture(int index) {
		return TEXTURES[index];
	}

	private static boolean isInvisibleWhileTeleporting(Purpoid purpoid) {
		return (purpoid.isEndimationPlaying(EEPlayableEndimations.PURPOID_TELEPORT_TO) || purpoid.isEndimationPlaying(EEPlayableEndimations.PURPOID_FAST_TELEPORT_TO)) && purpoid.getAnimationTick() >= 10;
	}

	private static void renderShieldingLine(Purpoid purpoid, float partialTicks, PoseStack stack, MultiBufferSource bufferSource, Vec3 mommyPos, int stunTimer) {
		stack.pushPose();
		double interpolatedPurpoidX = Mth.lerp(partialTicks, purpoid.xo, purpoid.getX());
		double interpolatedPurpoidY = Mth.lerp(partialTicks, purpoid.yo, purpoid.getY()) + 0.25F;
		double interpolatedPurpoidZ = Mth.lerp(partialTicks, purpoid.zo, purpoid.getZ());
		stack.translate(0.0F, 0.25F, 0.0F);
		float xDifference = (float) (mommyPos.x - interpolatedPurpoidX);
		float yDifference = (float) (mommyPos.y + 1.0F - interpolatedPurpoidY);
		float zDifference = (float) (mommyPos.z - interpolatedPurpoidZ);
		VertexConsumer vertexconsumer = bufferSource.getBuffer(EERenderTypes.PURPAZOID_SHIELDING_LINE);
		Matrix4f matrix4f = stack.last().pose();
		float width = 0.1F;
		float widthProportionalToDistance = (float) Mth.fastInvSqrt(xDifference * xDifference + zDifference * zDifference) * width / 2.0F;
		float zGap = zDifference * widthProportionalToDistance;
		float xGap = xDifference * widthProportionalToDistance;

		float stunTimerHalfCycles = Math.max(stunTimer - partialTicks, 0.0F) / 20.0F;
		float alpha = 0.1F + 0.4F * (1.0F - 2.0F * Mth.abs(stunTimerHalfCycles - Mth.floor(stunTimerHalfCycles + 0.5F)));

		float baseR = 0.643F;
		float baseG = 0.482F;
		float baseB = 0.918F;
		if (purpoid.hurtTime > 0 || purpoid.deathTime > 0) {
			baseR = baseR * 0.5F + 0.5F;
			baseG *= 0.5F;
			baseB *= 0.5F;
		}

		float waveOffset = Mth.PI * (purpoid.tickCount + partialTicks) / 20.0F;
		for (int i = 0; i <= 48; ++i) {
			addShieldingLineVertexPair(vertexconsumer, matrix4f, alpha, waveOffset, baseR, baseG, baseB, xDifference, yDifference, zDifference, width, width, zGap, xGap, i, false);
		}

		for (int i = 48; i >= 0; --i) {
			addShieldingLineVertexPair(vertexconsumer, matrix4f, alpha, waveOffset, baseR, baseG, baseB, xDifference, yDifference, zDifference, width, 0.0F, zGap, xGap, i, true);
		}

		stack.popPose();
	}

	private static void addShieldingLineVertexPair(VertexConsumer vertexConsumer, Matrix4f matrix4f, float alpha, float ageInTicks, float baseR, float baseG, float baseB, float xDifference, float yDifference, float zDifference, float width, float secondaryWidth, float zGap, float xGap, int index, boolean otherLine) {
		float progress = (float) index / 48.0F;
		float colorBrightness = index % 2 == (otherLine ? 1 : 0) ? 0.5F : 1.0F;
		float r = baseR * colorBrightness;
		float g = baseG * colorBrightness;
		float b = baseB * colorBrightness;
		float progressedXDifference = xDifference * progress;
		float yProgress;
		if (progress < 0.1) {
			float smoothInto = progress / 0.1F;
			yProgress = (1.0F - 0.05F * smoothInto) * ((-(Mth.cos(Mth.PI * progress) - 1.0F) / 2.0F)) + 0.05F * smoothInto * Mth.sin(10.0F * Mth.PI * progress + ageInTicks);
		} else if (progress < 0.9F) {
			yProgress = 0.95F * ((-(Mth.cos(Mth.PI * progress) - 1.0F) / 2.0F)) + 0.05F * Mth.sin(10.0F * Mth.PI * progress + ageInTicks);
		} else {
			float smoothInto = (progress - 0.9F) / 0.1F;
			yProgress = (0.95F + 0.05F * smoothInto) * ((-(Mth.cos(Mth.PI * progress) - 1.0F) / 2.0F)) + (0.05F - 0.05F * smoothInto) * Mth.sin(10.0F * Mth.PI * progress + ageInTicks);
		}
		float progressedYDifference = yDifference > 0.0F ? yDifference * yProgress : yDifference - yDifference * (1.0F - yProgress);
		float progressedZDifference = zDifference * progress;
		vertexConsumer.vertex(matrix4f, progressedXDifference - zGap, progressedYDifference + secondaryWidth, progressedZDifference + xGap).color(r, g, b, alpha).uv2(15728880).endVertex();
		vertexConsumer.vertex(matrix4f, progressedXDifference + zGap, progressedYDifference + width - secondaryWidth, progressedZDifference - xGap).color(r, g, b, alpha).uv2(15728880).endVertex();
	}
}
