package com.minecraftabnormals.endergetic.client.models.eetle.eggs;

import com.minecraftabnormals.abnormals_core.core.endimator.entity.EndimatorModelRenderer;
import com.minecraftabnormals.endergetic.common.tileentities.EetleEggTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.model.ModelRenderer;

/**
 * @author Endergized
 * @author SmellyModder (Luke Tonon)
 * <p>
 * Created using Tabula 7.0.0
 */
public class SmallEetleEggModel implements IEetleEggModel {
	public EndimatorModelRenderer egg;
	public EndimatorModelRenderer otherEgg;
	public EndimatorModelRenderer bigEgg;
	public EndimatorModelRenderer smallEgg;
	public ModelRenderer cross1;
	public ModelRenderer cross2;
	public ModelRenderer base;
	public ModelRenderer cross3;
	public ModelRenderer cross4;

	public SmallEetleEggModel() {
		int textureWidth = 64;
		int textureHeight = 64;
		this.egg = new EndimatorModelRenderer(textureWidth, textureHeight, 46, 0);
		this.egg.setPos(4.0F, 24.0F, 4.0F);
		this.egg.addBox(-2.0F, -4.0F, -2.0F, 4, 4, 4, 0.0F);
		this.smallEgg = new EndimatorModelRenderer(textureWidth, textureHeight, 33, 0);
		this.smallEgg.setPos(-4.5F, 24.0F, -5.5F);
		this.smallEgg.addBox(-1.5F, -3.0F, -1.5F, 3, 3, 3, 0.0F);
		this.base = new ModelRenderer(textureWidth, textureHeight, -16, 38);
		this.base.setPos(0.0F, 23.99F, 0.0F);
		this.base.addBox(-8.0F, 0.0F, -8.0F, 16, 0, 16, 0.0F);
		this.bigEgg = new EndimatorModelRenderer(textureWidth, textureHeight, 17, 2);
		this.bigEgg.setPos(1.5F, 24.0F, -1.5F);
		this.bigEgg.addBox(-2.5F, -5.0F, -2.5F, 5, 5, 5, 0.0F);
		this.cross2 = new ModelRenderer(textureWidth, textureHeight, 0, 29);
		this.cross2.setPos(-7.8F, 21.0F, -7.8F);
		this.cross2.addBox(0.0F, 0.0F, 0.0F, 11, 3, 0, 0.0F);
		this.setRotateAngle(cross2, 0.0F, -0.7853981633974483F, 0.0F);
		this.cross4 = new ModelRenderer(textureWidth, textureHeight, 0, 29);
		this.cross4.setPos(7.8F, 21.0F, -7.8F);
		this.cross4.addBox(0.0F, 0.0F, 0.0F, 11, 3, 0, 0.0F);
		this.setRotateAngle(cross4, 0.0F, -2.356194490192345F, 0.0F);
		this.otherEgg = new EndimatorModelRenderer(textureWidth, textureHeight, 0, 0);
		this.otherEgg.setPos(-4.0F, 24.0F, 2.0F);
		this.otherEgg.addBox(-2.0F, -4.0F, -2.0F, 4, 4, 4, 0.0F);
		this.cross1 = new ModelRenderer(textureWidth, textureHeight, 0, 29);
		this.cross1.setPos(-7.8F, 21.0F, 7.8F);
		this.cross1.addBox(0.0F, 0.0F, 0.0F, 11, 3, 0, 0.0F);
		this.setRotateAngle(cross1, 0.0F, 0.7853981633974483F, 0.0F);
		this.cross3 = new ModelRenderer(textureWidth, textureHeight, 0, 29);
		this.cross3.setPos(7.8F, 21.0F, 7.8F);
		this.cross3.addBox(0.0F, 0.0F, 0.0F, 11, 3, 0, 0.0F);
		this.setRotateAngle(cross3, 0.0F, 2.356194490192345F, 0.0F);
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder builder, int packedLight, int packedOverlay, float partialTicks, EetleEggTileEntity.SackGrowth[] sackGrowths) {
		float eggScale1 = sackGrowths[0].getGrowth(partialTicks);
		this.egg.setScale(eggScale1, eggScale1, eggScale1);
		this.egg.render(matrixStack, builder, 240, packedOverlay);

		float eggScale2 = sackGrowths[1].getGrowth(partialTicks);
		this.smallEgg.setScale(eggScale2, eggScale2, eggScale2);
		this.smallEgg.render(matrixStack, builder, 240, packedOverlay);

		float eggScale3 = sackGrowths[2].getGrowth(partialTicks);
		this.otherEgg.setScale(eggScale3, eggScale3, eggScale3);
		this.otherEgg.render(matrixStack, builder, 240, packedOverlay);

		float eggScale4 = sackGrowths[3].getGrowth(partialTicks);
		this.bigEgg.setScale(eggScale4, eggScale4, eggScale4);
		this.bigEgg.render(matrixStack, builder, 240, packedOverlay);
	}

	@Override
	public void renderSilk(MatrixStack matrixStack, IVertexBuilder silkBuilder, int packedLight, int packedOverlay) {
		this.base.render(matrixStack, silkBuilder, packedLight, packedOverlay);
		this.cross2.render(matrixStack, silkBuilder, packedLight, packedOverlay);
		this.cross4.render(matrixStack, silkBuilder, packedLight, packedOverlay);
		this.cross1.render(matrixStack, silkBuilder, packedLight, packedOverlay);
		this.cross3.render(matrixStack, silkBuilder, packedLight, packedOverlay);
	}

	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}
