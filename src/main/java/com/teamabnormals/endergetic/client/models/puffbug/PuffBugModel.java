package com.teamabnormals.endergetic.client.models.puffbug;

import com.teamabnormals.endergetic.core.EndergeticExpansion;
import com.teamabnormals.endergetic.core.registry.other.EEPlayableEndimations;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamabnormals.endergetic.common.entities.booflo.BoofloEntity;
import com.teamabnormals.endergetic.common.entities.puffbug.PuffBugEntity;

import com.teamabnormals.blueprint.client.ClientInfo;
import com.teamabnormals.blueprint.core.endimator.Endimator;
import com.teamabnormals.blueprint.core.endimator.EndimatorModelPart;
import com.teamabnormals.blueprint.core.endimator.entity.EndimatorEntityModel;
import com.teamabnormals.blueprint.core.endimator.model.EndimatorLayerDefinition;
import com.teamabnormals.blueprint.core.endimator.model.EndimatorPartDefinition;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LightLayer;

/**
 * ModelPuffBugInflated - Endergized
 * Created using Tabula 7.0.0
 */
public class PuffBugModel<E extends PuffBugEntity> extends EndimatorEntityModel<E> {
	public static final ModelLayerLocation LOCATION = new ModelLayerLocation(new ResourceLocation(EndergeticExpansion.MOD_ID, "puff_bug"), "main");
	public EndimatorModelPart Body;
	public EndimatorModelPart Stinger;
	public EndimatorModelPart Neck;
	public EndimatorModelPart Head;
	public EndimatorModelPart Sensor1;
	public EndimatorModelPart Sensor2;

	public EndimatorModelPart BodyDeflated;
	public EndimatorModelPart StingerDeflated;
	public EndimatorModelPart NeckDeflated;
	public EndimatorModelPart HeadDeflated;
	public EndimatorModelPart Sensor1Deflated;
	public EndimatorModelPart Sensor2Deflated;

	public EndimatorModelPart BodyDeflatedProjectile;
	public EndimatorModelPart StingerDeflatedProjectile;
	public EndimatorModelPart NeckDeflatedProjectile;
	public EndimatorModelPart HeadDeflatedProjectile;
	public EndimatorModelPart Sensor1DeflatedProjectile;
	public EndimatorModelPart Sensor2DeflatedProjectile;

	@SuppressWarnings("all")
	public PuffBugModel(ModelPart root) {
		this.Body = (EndimatorModelPart) root.getChild("Body");
		this.Stinger = (EndimatorModelPart) this.Body.getChild("Stinger");
		this.Neck = (EndimatorModelPart) this.Body.getChild("Neck");
		this.Head = (EndimatorModelPart) this.Neck.getChild("Head");
		this.Sensor1 = (EndimatorModelPart) this.Head.getChild("Sensor1");
		this.Sensor2 = (EndimatorModelPart) this.Head.getChild("Sensor2");

		this.BodyDeflated = (EndimatorModelPart) root.getChild("BodyDeflated");
		this.StingerDeflated = (EndimatorModelPart) this.BodyDeflated.getChild("StingerDeflated");
		this.NeckDeflated = (EndimatorModelPart) this.BodyDeflated.getChild("NeckDeflated");
		this.HeadDeflated = (EndimatorModelPart) this.NeckDeflated.getChild("HeadDeflated");
		this.Sensor1Deflated = (EndimatorModelPart) this.HeadDeflated.getChild("Sensor1Deflated");
		this.Sensor2Deflated = (EndimatorModelPart) this.HeadDeflated.getChild("Sensor2Deflated");

		this.BodyDeflatedProjectile = (EndimatorModelPart) root.getChild("BodyDeflatedProjectile");
		this.StingerDeflatedProjectile = (EndimatorModelPart) this.BodyDeflatedProjectile.getChild("StingerDeflatedProjectile");
		this.NeckDeflatedProjectile = (EndimatorModelPart) this.BodyDeflatedProjectile.getChild("NeckDeflatedProjectile");
		this.HeadDeflatedProjectile = (EndimatorModelPart) this.NeckDeflatedProjectile.getChild("HeadDeflatedProjectile");
		this.Sensor1DeflatedProjectile = (EndimatorModelPart) this.HeadDeflatedProjectile.getChild("Sensor1DeflatedProjectile");
		this.Sensor2DeflatedProjectile = (EndimatorModelPart) this.HeadDeflatedProjectile.getChild("Sensor2DeflatedProjectile");

		this.endimator = Endimator.compile(root);
	}

	public static EndimatorLayerDefinition createLayerDefinition() {
		EndimatorPartDefinition root = EndimatorPartDefinition.root();
		EndimatorPartDefinition Body = root.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(8, 3).addBox(-3.0F, -3.5F, -3.0F, 6.0F, 7.0F, 6.0F, false), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.0F, 0.045553092F, 0.0F));
		EndimatorPartDefinition Stinger = Body.addOrReplaceChild("Stinger", CubeListBuilder.create().texOffs(26, 0).addBox(0.0F, -4.0F, 0.0F, 1.0F, 4.0F, 0.0F, false), PartPose.offsetAndRotation(-0.5F, -3.5F, 0.0F, 0.0F, 0.0F, 0.0F));
		EndimatorPartDefinition Neck = Body.addOrReplaceChild("Neck", CubeListBuilder.create().texOffs(0, 6).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, false), PartPose.offsetAndRotation(0.0F, 2.5F, 0.0F, -0.13665928F, 0.0F, 0.0F));
		EndimatorPartDefinition Head = Neck.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, 0.0F, -1.5F, 4.0F, 3.0F, 3.0F, false), PartPose.offsetAndRotation(0.0F, 3.7F, 0.0F, -0.13665928F, 0.0F, 0.0F));
		EndimatorPartDefinition Sensor1 = Head.addOrReplaceChild("Sensor1", CubeListBuilder.create().texOffs(30, 0).addBox(0.0F, 0.0F, 0.0F, 1.0F, 4.0F, 0.0F, false), PartPose.offsetAndRotation(-2.0F, 3.0F, 0.5F, 0.0F, 1.5707964F, 0.7330383F));
		EndimatorPartDefinition Sensor2 = Head.addOrReplaceChild("Sensor2", CubeListBuilder.create().texOffs(30, 0).addBox(0.0F, 0.0F, 0.0F, 1.0F, 4.0F, 0.0F, false), PartPose.offsetAndRotation(2.0F, 3.0F, 0.5F, 0.7330383F, 1.5707964F, 0.0F));

		EndimatorPartDefinition BodyDeflated = root.addOrReplaceChild("BodyDeflated", CubeListBuilder.create().texOffs(10, 7).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 6.0F, 2.0F, false), PartPose.offsetAndRotation(0.0F, 11.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		EndimatorPartDefinition StingerDeflated = BodyDeflated.addOrReplaceChild("StingerDeflated", CubeListBuilder.create().texOffs(15, 1).addBox(-0.5F, -4.0F, 0.0F, 1.0F, 4.0F, 0.0F, false), PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		EndimatorPartDefinition NeckDeflated = BodyDeflated.addOrReplaceChild("NeckDeflated", CubeListBuilder.create().texOffs(0, 6).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, false), PartPose.offsetAndRotation(0.0F, 3.0F, 0.0F, -0.13962634F, 0.0F, 0.0F));
		EndimatorPartDefinition HeadDeflated = NeckDeflated.addOrReplaceChild("HeadDeflated", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, 0.0F, -1.5F, 4.0F, 3.0F, 3.0F, false), PartPose.offsetAndRotation(0.0F, 3.7F, 0.0F, -0.13962634F, 0.0F, 0.0F));
		EndimatorPartDefinition Sensor1Deflated = HeadDeflated.addOrReplaceChild("Sensor1Deflated", CubeListBuilder.create().texOffs(18, 1).addBox(0.0F, 0.0F, 0.0F, 1.0F, 4.0F, 0.0F, false), PartPose.offsetAndRotation(-2.0F, 3.0F, 0.5F, 0.0F, 1.5707964F, 0.7330383F));
		EndimatorPartDefinition Sensor2Deflated = HeadDeflated.addOrReplaceChild("Sensor2Deflated", CubeListBuilder.create().texOffs(18, 1).addBox(-0.1F, 0.0F, 0.0F, 1.0F, 4.0F, 0.0F, false), PartPose.offsetAndRotation(2.0F, 3.0F, 0.5F, 0.7330383F, 1.5707964F, 0.0F));

		EndimatorPartDefinition BodyDeflatedProjectile = root.addOrReplaceChild("BodyDeflatedProjectile", CubeListBuilder.create().texOffs(10, 7).addBox(-1.0F, 0.5F, -1.0F, 2.0F, 6.0F, 2.0F, false), PartPose.offsetAndRotation(0.0F, 23.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		EndimatorPartDefinition StingerDeflatedProjectile = BodyDeflatedProjectile.addOrReplaceChild("StingerDeflatedProjectile", CubeListBuilder.create().texOffs(15, 1).addBox(-0.5F, -4.0F, 0.0F, 1.0F, 4.0F, 0.0F, false), PartPose.offsetAndRotation(0.0F, 0.5F, 0.0F, 0.0F, 0.0F, 0.0F));
		EndimatorPartDefinition NeckDeflatedProjectile = BodyDeflatedProjectile.addOrReplaceChild("NeckDeflatedProjectile", CubeListBuilder.create().texOffs(0, 6).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, false), PartPose.offsetAndRotation(0.0F, 6.5F, 0.0F, -0.13665928F, 0.0F, 0.0F));
		EndimatorPartDefinition HeadDeflatedProjectile = NeckDeflatedProjectile.addOrReplaceChild("HeadDeflatedProjectile", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, 0.0F, -1.5F, 4.0F, 3.0F, 3.0F, false), PartPose.offsetAndRotation(0.0F, 3.7F, 0.0F, -0.13962634F, 0.0F, 0.0F));
		EndimatorPartDefinition Sensor1DeflatedProjectile = HeadDeflatedProjectile.addOrReplaceChild("Sensor1DeflatedProjectile", CubeListBuilder.create().texOffs(18, 1).addBox(0.0F, 0.0F, 0.0F, 1.0F, 4.0F, 0.0F, false), PartPose.offsetAndRotation(-2.0F, 3.0F, 0.5F, 0.0F, 1.5707964F, 0.7330383F));
		EndimatorPartDefinition Sensor2DeflatedProjectile = HeadDeflatedProjectile.addOrReplaceChild("Sensor2DeflatedProjectile", CubeListBuilder.create().texOffs(18, 1).addBox(-0.1F, 0.0F, 0.0F, 1.0F, 4.0F, 0.0F, false), PartPose.offsetAndRotation(2.0F, 3.0F, 0.5F, 0.7330383F, 1.5707964F, 0.08726646F));
		return new EndimatorLayerDefinition(root, 32, 16);
	}

	@Override
	public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		Entity ridingEntity = this.entity.getVehicle();
		if (ridingEntity instanceof BoofloEntity && !(((BoofloEntity) ridingEntity).isEndimationPlaying(EEPlayableEndimations.BOOFLO_EAT) && ((BoofloEntity) ridingEntity).getAnimationTick() >= 20)) {
			return;
		}

		if (!this.entity.isInflated() && this.entity.stuckInBlock) {
			packedLightIn = this.getPackedLightForStuck(this.entity);
		}

		if (this.entity.isInflated()) {
			this.Body.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		} else {
			if (this.entity.isProjectile()) {
				this.BodyDeflatedProjectile.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
			} else {
				this.BodyDeflated.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
			}
		}
	}

	private int getPackedLightForStuck(PuffBugEntity puffbug) {
		float partialTicks = ClientInfo.getPartialTicks();
		return LightTexture.pack(puffbug.isOnFire() ? 15 : puffbug.level.getBrightness(LightLayer.BLOCK, this.getStuckLightPos(puffbug, partialTicks)), this.entity.level.getBrightness(LightLayer.SKY, this.getStuckLightPos(puffbug, partialTicks)));
	}

	private BlockPos getStuckLightPos(PuffBugEntity puffbug, float partialTicks) {
		BlockPos blockpos = new BlockPos(puffbug.getX(), puffbug.getY() + (double) puffbug.getEyeHeight(), puffbug.getX());
		boolean rotationFlag = true;
		float[] rotations = puffbug.getRotationController().getRotations(partialTicks);
		Direction horizontalOffset = Direction.fromYRot(rotations[0]).getOpposite();
		Direction verticalOffset = (rotations[1] <= 180.0F && rotations[1] > 100.0F) ? Direction.UP : Direction.DOWN;

		if (rotations[1] >= 80.0F && rotations[1] <= 100.0F) {
			rotationFlag = false;
		}

		return rotationFlag ? blockpos.relative(horizontalOffset).relative(verticalOffset) : blockpos.relative(horizontalOffset);
	}

	@Override
	public void setupAnim(E puffBug, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setupAnim(puffBug, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

		this.Body.setShouldScaleChildren(puffBug.isEndimationPlaying(EEPlayableEndimations.PUFF_BUG_TELEPORT_TO) || puffBug.isEndimationPlaying(EEPlayableEndimations.PUFF_BUG_TELEPORT_FROM));

		if (!puffBug.isEndimationPlaying(EEPlayableEndimations.PUFF_BUG_PUFF) && !puffBug.isEndimationPlaying(EEPlayableEndimations.PUFF_BUG_POLLINATE)) {
			float angle = 0.1F * Mth.sin(0.25F * ageInTicks);
			this.Sensor1.zRot += angle;
			this.Sensor2.xRot += angle;
		}

		if (!puffBug.isEndimationPlaying(EEPlayableEndimations.PUFF_BUG_FLY)) {
			this.Head.xRot += 0.075F * Mth.sin(0.1F * ageInTicks);
			this.HeadDeflated.xRot = this.Head.xRot;
		}

		float partialTicks = ClientInfo.getPartialTicks();
		float[] rotations = puffBug.getRotationController().getRotations(partialTicks);

		this.Body.yRot = rotations[0] * (float) (Math.PI / 180F);
		this.Body.xRot = rotations[1] * (float) (Math.PI / 180F);

		if (puffBug.isPassenger()) {
			this.Body.zRot = 1.57F;
		}

		this.BodyDeflated.yRot = this.Body.yRot;
		this.BodyDeflated.xRot = this.Body.xRot;
		this.BodyDeflatedProjectile.yRot = this.Body.yRot;
		this.BodyDeflatedProjectile.xRot = this.Body.xRot;

		this.NeckDeflated.xRot += -0.56F * puffBug.HIVE_LANDING.getProgress(partialTicks);
		this.HeadDeflated.xRot += -0.42F * puffBug.HIVE_LANDING.getProgress(partialTicks);
		this.Sensor1Deflated.zRot += 1.7F * puffBug.HIVE_LANDING.getProgress(partialTicks);
		this.Sensor2Deflated.xRot += 1.7F * puffBug.HIVE_LANDING.getProgress(partialTicks);

		this.NeckDeflated.xRot += 0.3F * puffBug.HIVE_SLEEP.getProgress(partialTicks);
		this.HeadDeflated.xRot += 0.25F * puffBug.HIVE_SLEEP.getProgress(partialTicks);

		this.Neck.xRot = this.HeadDeflated.xRot;
		this.Head.xRot = this.HeadDeflated.xRot;
		this.Sensor1.zRot = this.Sensor1Deflated.zRot;
		this.Sensor2.xRot = this.Sensor2Deflated.xRot;

		if (puffBug.isProjectile()) {
			this.Neck.yRot = 0.0F;
			this.NeckDeflated.yRot = 0.0F;

			this.Sensor1Deflated.xRot = 0.7F;
			this.Sensor1Deflated.yRot = 0.7F;
			this.Sensor1Deflated.zRot = 0.09F;

			this.Sensor2Deflated.xRot = 0.32F;
			this.Sensor2Deflated.yRot = -2.64F;
			this.Sensor2Deflated.zRot = 0.09F;

			float spin = Mth.lerp(ClientInfo.getPartialTicks(), puffBug.prevSpin, puffBug.spin);

			this.NeckDeflatedProjectile.yRot += Math.toRadians(spin);
			this.StingerDeflatedProjectile.yRot += Math.toRadians(spin);
		}
	}
}