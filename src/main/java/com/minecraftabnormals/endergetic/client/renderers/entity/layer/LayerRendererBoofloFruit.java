package com.minecraftabnormals.endergetic.client.renderers.entity.layer;

import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity;
import com.minecraftabnormals.endergetic.core.registry.EEItems;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LayerRendererBoofloFruit extends LayerRenderer<BoofloEntity, EntityModel<BoofloEntity>> {

	public LayerRendererBoofloFruit(IEntityRenderer<BoofloEntity, EntityModel<BoofloEntity>> renderer) {
		super(renderer);
	}

	@Override
	public void render(MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int packedLightIn, BoofloEntity booflo, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (booflo.hasCaughtFruit() && !booflo.isBoofed() && booflo.isEndimationPlaying(BoofloEntity.EAT) && booflo.getAnimationTick() > 20) {
			matrixStack.push();

			Vector3d fruitPos = (new Vector3d(-1.25D, 0.0D, 0.0D)).rotateYaw(-netHeadYaw * ((float) Math.PI / 180F) - ((float) Math.PI / 2F));
			matrixStack.translate(fruitPos.getX(), fruitPos.getY() + 1.15F + this.getFruitPosOffset(booflo), fruitPos.getZ());

			matrixStack.rotate(Vector3f.XP.rotationDegrees(90.0F));

			matrixStack.scale(1.3F, 1.3F, 1.3F);

			Minecraft.getInstance().getFirstPersonRenderer().renderItemSide(booflo, new ItemStack(EEItems.BOLLOOM_FRUIT.get()), ItemCameraTransforms.TransformType.GROUND, false, matrixStack, bufferIn, packedLightIn);
			matrixStack.pop();
		}
	}

	private float getFruitPosOffset(BoofloEntity booflo) {
		return 0.22F * booflo.FRUIT_HOVER.getAnimationProgress();
	}

}