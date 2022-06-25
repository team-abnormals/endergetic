package com.minecraftabnormals.endergetic.client.models.eetle.eggs;

import com.minecraftabnormals.endergetic.common.tileentities.EetleEggTileEntity;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
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

import static com.minecraftabnormals.endergetic.api.util.ModelUtil.setScale;

/**
 * @author Endergized
 * @author SmellyModder (Luke Tonon)
 * <p>
 * Created using Tabula 7.0.0
 */
public class SmallEetleEggModel implements IEetleEggModel {
	public static final ModelLayerLocation LOCATION = new ModelLayerLocation(new ResourceLocation(EndergeticExpansion.MOD_ID, "small_eetle_egg"), "main");
	public ModelPart egg;
	public ModelPart otherEgg;
	public ModelPart bigEgg;
	public ModelPart smallEgg;
	public ModelPart cross1;
	public ModelPart cross2;
	public ModelPart base;
	public ModelPart cross3;
	public ModelPart cross4;

	public SmallEetleEggModel(ModelPart root) {
		this.egg = root.getChild("egg");
		this.otherEgg = root.getChild("otherEgg");
		this.bigEgg = root.getChild("bigEgg");
		this.smallEgg = root.getChild("smallEgg");
		this.cross1 = root.getChild("cross1");
		this.cross2 = root.getChild("cross2");
		this.base = root.getChild("base");
		this.cross3 = root.getChild("cross3");
		this.cross4 = root.getChild("cross4");
	}

	public static LayerDefinition createLayerDefinition() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition root = meshdefinition.getRoot();
		PartDefinition egg = root.addOrReplaceChild("egg", CubeListBuilder.create(), PartPose.offsetAndRotation(4.0F, 24.0F, 4.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition otherEgg = root.addOrReplaceChild("otherEgg", CubeListBuilder.create(), PartPose.offsetAndRotation(-4.0F, 24.0F, 2.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition bigEgg = root.addOrReplaceChild("bigEgg", CubeListBuilder.create(), PartPose.offsetAndRotation(1.5F, 24.0F, -1.5F, 0.0F, 0.0F, 0.0F));
		PartDefinition smallEgg = root.addOrReplaceChild("smallEgg", CubeListBuilder.create(), PartPose.offsetAndRotation(-4.5F, 24.0F, -5.5F, 0.0F, 0.0F, 0.0F));
		PartDefinition cross1 = root.addOrReplaceChild("cross1", CubeListBuilder.create().texOffs(0, 29).addBox(0.0F, 0.0F, 0.0F, 11.0F, 3.0F, 0.0F, false), PartPose.offsetAndRotation(-7.8F, 21.0F, 7.8F, 0.0F, 0.7853982F, 0.0F));
		PartDefinition cross2 = root.addOrReplaceChild("cross2", CubeListBuilder.create().texOffs(0, 29).addBox(0.0F, 0.0F, 0.0F, 11.0F, 3.0F, 0.0F, false), PartPose.offsetAndRotation(-7.8F, 21.0F, -7.8F, 0.0F, -0.7853982F, 0.0F));
		PartDefinition base = root.addOrReplaceChild("base", CubeListBuilder.create().texOffs(-16, 38).addBox(-8.0F, 0.0F, -8.0F, 16.0F, 0.0F, 16.0F, false), PartPose.offsetAndRotation(0.0F, 23.99F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition cross3 = root.addOrReplaceChild("cross3", CubeListBuilder.create().texOffs(0, 29).addBox(0.0F, 0.0F, 0.0F, 11.0F, 3.0F, 0.0F, false), PartPose.offsetAndRotation(7.8F, 21.0F, 7.8F, 0.0F, 2.3561945F, 0.0F));
		PartDefinition cross4 = root.addOrReplaceChild("cross4", CubeListBuilder.create().texOffs(0, 29).addBox(0.0F, 0.0F, 0.0F, 11.0F, 3.0F, 0.0F, false), PartPose.offsetAndRotation(7.8F, 21.0F, -7.8F, 0.0F, -2.3561945F, 0.0F));
		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void render(PoseStack matrixStack, VertexConsumer builder, int packedLight, int packedOverlay, float partialTicks, EetleEggTileEntity.SackGrowth[] sackGrowths) {
		float eggScale1 = sackGrowths[0].getGrowth(partialTicks);
		setScale(this.egg, eggScale1, eggScale1, eggScale1);
		this.egg.render(matrixStack, builder, 240, packedOverlay);

		float eggScale2 = sackGrowths[1].getGrowth(partialTicks);
		setScale(this.smallEgg, eggScale2, eggScale2, eggScale2);
		this.smallEgg.render(matrixStack, builder, 240, packedOverlay);

		float eggScale3 = sackGrowths[2].getGrowth(partialTicks);
		setScale(this.otherEgg, eggScale3, eggScale3, eggScale3);
		this.otherEgg.render(matrixStack, builder, 240, packedOverlay);

		float eggScale4 = sackGrowths[3].getGrowth(partialTicks);
		setScale(this.bigEgg, eggScale4, eggScale4, eggScale4);
		this.bigEgg.render(matrixStack, builder, 240, packedOverlay);
	}

	@Override
	public void renderSilk(PoseStack matrixStack, VertexConsumer silkBuilder, int packedLight, int packedOverlay) {
		this.base.render(matrixStack, silkBuilder, packedLight, packedOverlay);
		this.cross2.render(matrixStack, silkBuilder, packedLight, packedOverlay);
		this.cross4.render(matrixStack, silkBuilder, packedLight, packedOverlay);
		this.cross1.render(matrixStack, silkBuilder, packedLight, packedOverlay);
		this.cross3.render(matrixStack, silkBuilder, packedLight, packedOverlay);
	}

	public void setRotateAngle(ModelPart modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}
