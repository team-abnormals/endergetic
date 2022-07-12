package com.teamabnormals.endergetic.client.models.eetle.eggs;

import com.teamabnormals.endergetic.common.tileentities.EetleEggTileEntity;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

import static com.teamabnormals.endergetic.api.util.ModelUtil.setScale;

/**
 * @author Endergized
 * @author SmellyModder (Luke Tonon)
 * <p>
 * Created using Tabula 7.0.0
 */
public class MediumEetleEggModel implements IEetleEggModel {
	public static final ModelLayerLocation LOCATION = new ModelLayerLocation(new ResourceLocation(EndergeticExpansion.MOD_ID, "medium_eetle_egg"), "main");
	public ModelPart smallEgg1;
	public ModelPart smallEgg2;
	public ModelPart BigEgg;
	public ModelPart Egg;
	public ModelPart base;
	public ModelPart cross1;
	public ModelPart cross2;
	public ModelPart cross3;
	public ModelPart cross4;

	public MediumEetleEggModel(ModelPart root) {
		this.smallEgg1 = root.getChild("smallEgg1");
		this.smallEgg2 = root.getChild("smallEgg2");
		this.BigEgg = root.getChild("BigEgg");
		this.Egg = root.getChild("Egg");
		this.base = root.getChild("base");
		this.cross1 = root.getChild("cross1");
		this.cross2 = root.getChild("cross2");
		this.cross3 = root.getChild("cross3");
		this.cross4 = root.getChild("cross4");
	}

	public static LayerDefinition createLayerDefinition() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition root = meshdefinition.getRoot();
		PartDefinition smallEgg1 = root.addOrReplaceChild("smallEgg1", CubeListBuilder.create().texOffs(46, 0).addBox(-2.0F, -4.0F, -2.0F, 4.0F, 4.0F, 4.0F, false), PartPose.offsetAndRotation(4.0F, 24.0F, 4.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition smallEgg2 = root.addOrReplaceChild("smallEgg2", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -5.0F, -2.5F, 5.0F, 5.0F, 5.0F, false), PartPose.offsetAndRotation(-3.5F, 24.0F, 3.5F, 0.0F, 0.0F, 0.0F));
		PartDefinition BigEgg = root.addOrReplaceChild("BigEgg", CubeListBuilder.create().texOffs(28, 47).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, false), PartPose.offsetAndRotation(1.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition Egg = root.addOrReplaceChild("Egg", CubeListBuilder.create().texOffs(28, 0).addBox(-2.0F, -4.0F, -2.0F, 4.0F, 4.0F, 4.0F, false), PartPose.offsetAndRotation(-3.0F, 24.0F, -4.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition base = root.addOrReplaceChild("base", CubeListBuilder.create().texOffs(-16, 38).addBox(-8.0F, 0.0F, -8.0F, 16.0F, 0.0F, 16.0F, false), PartPose.offsetAndRotation(0.0F, 23.99F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition cross1 = root.addOrReplaceChild("cross1", CubeListBuilder.create().texOffs(0, 29).addBox(0.0F, 0.0F, 0.0F, 11.0F, 3.0F, 0.0F, false), PartPose.offsetAndRotation(-7.8F, 21.0F, 7.8F, 0.0F, 0.7853982F, 0.0F));
		PartDefinition cross2 = root.addOrReplaceChild("cross2", CubeListBuilder.create().texOffs(0, 29).addBox(0.0F, 0.0F, 0.0F, 11.0F, 3.0F, 0.0F, false), PartPose.offsetAndRotation(7.8F, 21.0F, -7.8F, 0.0F, -2.3561945F, 0.0F));
		PartDefinition cross3 = root.addOrReplaceChild("cross3", CubeListBuilder.create().texOffs(0, 29).addBox(0.0F, 0.0F, 0.0F, 11.0F, 3.0F, 0.0F, false), PartPose.offsetAndRotation(-7.8F, 21.0F, -7.8F, 0.0F, -0.7853982F, 0.0F));
		PartDefinition cross4 = root.addOrReplaceChild("cross4", CubeListBuilder.create().texOffs(0, 29).addBox(0.0F, 0.0F, 0.0F, 11.0F, 3.0F, 0.0F, false), PartPose.offsetAndRotation(7.8F, 21.0F, 7.8F, 0.0F, 2.3561945F, 0.0F));
		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void render(PoseStack matrixStack, VertexConsumer builder, int packedLight, int packedOverlay, float partialTicks, EetleEggTileEntity.SackGrowth[] sackGrowths) {
		float eggScale1 = sackGrowths[0].getGrowth(partialTicks);
		setScale(this.Egg, eggScale1, eggScale1, eggScale1);
		this.Egg.render(matrixStack, builder, 240, packedOverlay);

		float eggScale2 = sackGrowths[1].getGrowth(partialTicks);
		setScale(this.smallEgg2, eggScale2, eggScale2, eggScale2);
		this.smallEgg2.render(matrixStack, builder, 240, packedOverlay);

		float eggScale3 = sackGrowths[2].getGrowth(partialTicks);
		setScale(this.smallEgg1, eggScale3, eggScale3, eggScale3);
		this.smallEgg1.render(matrixStack, builder, 240, packedOverlay);

		float eggScale4 = sackGrowths[3].getGrowthMultiplied(partialTicks, 0.85F);
		setScale(this.BigEgg, eggScale4, eggScale4, eggScale4);
		this.BigEgg.render(matrixStack, builder, 240, packedOverlay);
	}

	@Override
	public void renderSilk(PoseStack matrixStack, VertexConsumer silkBuilder, int packedLight, int packedOverlay) {
		this.base.render(matrixStack, silkBuilder, packedLight, packedOverlay);
		this.cross1.render(matrixStack, silkBuilder, packedLight, packedOverlay);
		this.cross2.render(matrixStack, silkBuilder, packedLight, packedOverlay);
		this.cross3.render(matrixStack, silkBuilder, packedLight, packedOverlay);
		this.cross4.render(matrixStack, silkBuilder, packedLight, packedOverlay);
	}

	public void setRotateAngle(ModelPart modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}
