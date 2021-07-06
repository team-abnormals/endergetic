package com.minecraftabnormals.endergetic.client.models.purpoid;

import com.minecraftabnormals.abnormals_core.core.endimator.entity.EndimatorEntityModel;
import com.minecraftabnormals.abnormals_core.core.endimator.entity.EndimatorModelRenderer;
import com.minecraftabnormals.endergetic.common.entities.purpoid.PurpoidEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.util.math.MathHelper;

/**
 * @author Endergized
 * @author SmellyModder (Luke Tonon)
 * <p>
 * Created using Tabula 7.0.0
 */
public class PurpoidModel extends EndimatorEntityModel<PurpoidEntity> {
	public EndimatorModelRenderer head;
	public EndimatorModelRenderer rim1;
	public EndimatorModelRenderer rim2;
	public EndimatorModelRenderer rim3;
	public EndimatorModelRenderer rim4;
	public EndimatorModelRenderer tentacleSmall1;
	public EndimatorModelRenderer tentacleSmall2;
	public EndimatorModelRenderer tentacleSmall3;
	public EndimatorModelRenderer tentacleSmall4;
	public EndimatorModelRenderer tentacleLarge1;
	public EndimatorModelRenderer tentacleLarge2;
	public EndimatorModelRenderer tentacleLarge3;
	public EndimatorModelRenderer tentacleLarge4;

	public PurpoidModel() {
		this.textureWidth = 64;
		this.textureHeight = 96;
		this.rim2 = new EndimatorModelRenderer(this, 12, 59);
		this.rim2.setRotationPoint(0.0F, 0.0F, 7.0F);
		this.rim2.addBox(-9.0F, 0.0F, 0.0F, 18, 6, 0, 0.0F);
		this.setRotateAngle(rim2, 0.7853981633974483F, 0.0F, 0.0F);
		this.head = new EndimatorModelRenderer(this, 0, 32);
		this.head.setRotationPoint(0.0F, 23.0F, 0.0F);
		this.head.addBox(-7.0F, -13.0F, -7.0F, 14, 13, 14, 0.0F);
		this.rim3 = new EndimatorModelRenderer(this, 12, 41);
		this.rim3.setRotationPoint(7.0F, 0.0F, 0.0F);
		this.rim3.addBox(0.0F, 0.0F, -9.0F, 0, 6, 18, 0.0F);
		this.setRotateAngle(rim3, 0.0F, 0.0F, -0.7853981633974483F);
		this.tentacleSmall3 = new EndimatorModelRenderer(this, 0, 59);
		this.tentacleSmall3.mirror = true;
		this.tentacleSmall3.setRotationPoint(5.5F, 0.0F, -6.0F);
		this.tentacleSmall3.addBox(-1.5F, 0.0F, 0.0F, 3, 32, 0, 0.0F);
		this.setRotateAngle(tentacleSmall3, 0.0F, -0.7853981633974483F, 0.0F);
		this.tentacleLarge4 = new EndimatorModelRenderer(this, 6, 59);
		this.tentacleLarge4.setRotationPoint(0.0F, 0.0F, -7.0F);
		this.tentacleLarge4.addBox(-1.5F, 0.0F, 0.0F, 3, 37, 0, 0.0F);
		this.tentacleLarge3 = new EndimatorModelRenderer(this, 6, 59);
		this.tentacleLarge3.mirror = true;
		this.tentacleLarge3.setRotationPoint(0.0F, 0.0F, 7.0F);
		this.tentacleLarge3.addBox(-1.5F, 0.0F, 0.0F, 3, 37, 0, 0.0F);
		this.tentacleSmall4 = new EndimatorModelRenderer(this, 0, 59);
		this.tentacleSmall4.setRotationPoint(-5.5F, 0.0F, -6.0F);
		this.tentacleSmall4.addBox(-1.5F, 0.0F, 0.0F, 3, 32, 0, 0.0F);
		this.setRotateAngle(tentacleSmall4, 0.0F, 0.7853981633974483F, 0.0F);
		this.tentacleLarge1 = new EndimatorModelRenderer(this, 6, 59);
		this.tentacleLarge1.mirror = true;
		this.tentacleLarge1.setRotationPoint(-7.0F, 0.0F, 0.0F);
		this.tentacleLarge1.addBox(-1.5F, 0.0F, 0.0F, 3, 37, 0, 0.0F);
		this.setRotateAngle(tentacleLarge1, 0.0F, 1.5707963267948966F, 0.0F);
		this.rim1 = new EndimatorModelRenderer(this, 12, 59);
		this.rim1.setRotationPoint(0.0F, 0.0F, -7.0F);
		this.rim1.addBox(-9.0F, 0.0F, 0.0F, 18, 6, 0, 0.0F);
		this.setRotateAngle(rim1, -0.7853981633974483F, 0.0F, 0.0F);
		this.rim4 = new EndimatorModelRenderer(this, 12, 41);
		this.rim4.setRotationPoint(-7.0F, 0.0F, 0.0F);
		this.rim4.addBox(0.0F, 0.0F, -9.0F, 0, 6, 18, 0.0F);
		this.setRotateAngle(rim4, 0.0F, 0.0F, 0.7853981633974483F);
		this.tentacleSmall1 = new EndimatorModelRenderer(this, 0, 59);
		this.tentacleSmall1.setRotationPoint(-5.5F, 0.0F, 6.0F);
		this.tentacleSmall1.addBox(-1.5F, 0.0F, 0.0F, 3, 32, 0, 0.0F);
		this.setRotateAngle(tentacleSmall1, 0.0F, -0.7853981633974483F, 0.0F);
		this.tentacleLarge2 = new EndimatorModelRenderer(this, 6, 59);
		this.tentacleLarge2.setRotationPoint(7.0F, 0.0F, 0.0F);
		this.tentacleLarge2.addBox(-1.5F, 0.0F, 0.0F, 3, 37, 0, 0.0F);
		this.setRotateAngle(tentacleLarge2, 0.0F, 1.5707963267948966F, 0.0F);
		this.tentacleSmall2 = new EndimatorModelRenderer(this, 0, 59);
		this.tentacleSmall2.setRotationPoint(5.5F, 0.0F, 6.0F);
		this.tentacleSmall2.addBox(-1.5F, 0.0F, 0.0F, 3, 32, 0, 0.0F);
		this.setRotateAngle(tentacleSmall2, 0.0F, 0.7853981633974483F, 0.0F);
		this.head.addChild(this.rim2);
		this.head.addChild(this.rim3);
		this.head.addChild(this.tentacleSmall3);
		this.head.addChild(this.tentacleLarge4);
		this.head.addChild(this.tentacleLarge3);
		this.head.addChild(this.tentacleSmall4);
		this.head.addChild(this.tentacleLarge1);
		this.head.addChild(this.rim1);
		this.head.addChild(this.rim4);
		this.head.addChild(this.tentacleSmall1);
		this.head.addChild(this.tentacleLarge2);
		this.head.addChild(this.tentacleSmall2);

		this.setDefaultBoxValues();
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		super.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		this.head.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public void setRotationAngles(PurpoidEntity purpoid, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setRotationAngles(purpoid, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		float rimAngle = 0.17F * MathHelper.sin(0.1F * ageInTicks) + MathHelper.cos(limbSwing * 0.8F) * limbSwingAmount * 1.16F;
		this.rim1.rotateAngleX -= rimAngle;
		this.rim2.rotateAngleX += rimAngle;
		this.rim3.rotateAngleZ -= rimAngle;
        this.rim4.rotateAngleZ += rimAngle;
        float tentacleAngle = 0.09F * MathHelper.cos(0.1F * ageInTicks + 1.0F) + MathHelper.sin(limbSwing * 0.6F) * Math.min(0.3F, limbSwingAmount) * 0.5F;
        this.tentacleLarge1.rotateAngleX -= tentacleAngle;
		this.tentacleLarge2.rotateAngleX += tentacleAngle;
		this.tentacleLarge3.rotateAngleX += tentacleAngle;
		this.tentacleLarge4.rotateAngleX -= tentacleAngle;
		this.tentacleSmall1.rotateAngleX += tentacleAngle;
		this.tentacleSmall2.rotateAngleX += tentacleAngle;
		this.tentacleSmall3.rotateAngleX -= tentacleAngle;
		this.tentacleSmall4.rotateAngleX -= tentacleAngle;
	}

	@Override
	public void animateModel(PurpoidEntity purpoid) {
		super.animateModel(purpoid);
		if (this.tryToPlayEndimation(PurpoidEntity.TELEPORT_TO_ANIMATION)) {
			this.startKeyframe(5);
			this.scale(this.head, 1.3F, 1.3F, 1.3F);
			this.rotate(this.tentacleLarge1, -0.26F, 0.0F, 0.0F);
			this.rotate(this.tentacleLarge2, 0.26F, 0.0F, 0.0F);
			this.rotate(this.tentacleLarge3, 0.26F, 0.0F, 0.0F);
			this.rotate(this.tentacleLarge4, -0.26F, 0.0F, 0.0F);
			this.rotate(this.tentacleSmall1, 0.26F, 0.0F, 0.0F);
			this.rotate(this.tentacleSmall2, 0.26F, 0.0F, 0.0F);
			this.rotate(this.tentacleSmall3, -0.26F, 0.0F, 0.0F);
			this.rotate(this.tentacleSmall4, -0.26F, 0.0F, 0.0F);
			this.endKeyframe();

			this.startKeyframe(5);
			this.scale(this.head, -1.0F, -1.0F, -1.0F);
			this.rotate(this.tentacleLarge1, 0.26F, 0.0F, 0.0F);
			this.rotate(this.tentacleLarge2, -0.26F, 0.0F, 0.0F);
			this.rotate(this.tentacleLarge3, -0.26F, 0.0F, 0.0F);
			this.rotate(this.tentacleLarge4, 0.26F, 0.0F, 0.0F);
			this.rotate(this.tentacleSmall1, -0.26F, 0.0F, 0.0F);
			this.rotate(this.tentacleSmall2, -0.26F, 0.0F, 0.0F);
			this.rotate(this.tentacleSmall3, 0.26F, 0.0F, 0.0F);
			this.rotate(this.tentacleSmall4, 0.26F, 0.0F, 0.0F);
			this.endKeyframe();

			this.setStaticKeyframe(8);
		} else if (this.tryToPlayEndimation(PurpoidEntity.TELEPORT_FROM_ANIMATION)) {
			this.startKeyframe(5);
			this.scale(this.head, 1.3F, 1.3F, 1.3F);
			this.rotate(this.tentacleLarge1, -0.26F, 0.0F, 0.0F);
			this.rotate(this.tentacleLarge2, 0.26F, 0.0F, 0.0F);
			this.rotate(this.tentacleLarge3, 0.26F, 0.0F, 0.0F);
			this.rotate(this.tentacleLarge4, -0.26F, 0.0F, 0.0F);
			this.rotate(this.tentacleSmall1, 0.26F, 0.0F, 0.0F);
			this.rotate(this.tentacleSmall2, 0.26F, 0.0F, 0.0F);
			this.rotate(this.tentacleSmall3, -0.26F, 0.0F, 0.0F);
			this.rotate(this.tentacleSmall4, -0.26F, 0.0F, 0.0F);
			this.endKeyframe();

			this.resetKeyframe(5);
		}
	}

	/**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	public void setRotateAngle(EndimatorModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
