package com.minecraftabnormals.endergetic.client.renderers.entity.layer;

import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity;
import com.minecraftabnormals.endergetic.core.registry.EEItems;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import com.mojang.math.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LayerRendererBoofloFruit extends RenderLayer<BoofloEntity, EntityModel<BoofloEntity>> {

	public LayerRendererBoofloFruit(RenderLayerParent<BoofloEntity, EntityModel<BoofloEntity>> renderer) {
		super(renderer);
	}

	@Override
	public void render(PoseStack matrixStack, MultiBufferSource bufferIn, int packedLightIn, BoofloEntity booflo, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (booflo.hasCaughtFruit() && !booflo.isBoofed() && booflo.isEndimationPlaying(BoofloEntity.EAT) && booflo.getAnimationTick() > 20) {
			matrixStack.pushPose();

			Vec3 fruitPos = (new Vec3(-1.25D, 0.0D, 0.0D)).yRot(-netHeadYaw * ((float) Math.PI / 180F) - ((float) Math.PI / 2F));
			matrixStack.translate(fruitPos.x(), fruitPos.y() + 1.15F + this.getFruitPosOffset(booflo), fruitPos.z());

			matrixStack.mulPose(Vector3f.XP.rotationDegrees(90.0F));

			matrixStack.scale(1.3F, 1.3F, 1.3F);

			Minecraft.getInstance().getItemInHandRenderer().renderItem(booflo, new ItemStack(EEItems.BOLLOOM_FRUIT.get()), ItemTransforms.TransformType.GROUND, false, matrixStack, bufferIn, packedLightIn);
			matrixStack.popPose();
		}
	}

	private float getFruitPosOffset(BoofloEntity booflo) {
		return 0.22F * booflo.FRUIT_HOVER.getAnimationProgress();
	}

}