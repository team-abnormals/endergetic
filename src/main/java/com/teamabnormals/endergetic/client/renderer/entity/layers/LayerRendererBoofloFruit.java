package com.teamabnormals.endergetic.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.teamabnormals.blueprint.client.ClientInfo;
import com.teamabnormals.endergetic.common.entity.booflo.Booflo;
import com.teamabnormals.endergetic.core.registry.EEItems;
import com.teamabnormals.endergetic.core.registry.other.EEPlayableEndimations;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LayerRendererBoofloFruit extends RenderLayer<Booflo, EntityModel<Booflo>> {
	private final ItemInHandRenderer itemInHandRenderer;

	public LayerRendererBoofloFruit(RenderLayerParent<Booflo, EntityModel<Booflo>> renderer, ItemInHandRenderer itemInHandRenderer) {
		super(renderer);
		this.itemInHandRenderer = itemInHandRenderer;
	}

	@Override
	public void render(PoseStack matrixStack, MultiBufferSource bufferIn, int packedLightIn, Booflo booflo, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (booflo.hasCaughtFruit() && !booflo.isBoofed() && booflo.isEndimationPlaying(EEPlayableEndimations.BOOFLO_EAT) && booflo.getAnimationTick() > 20) {
			matrixStack.pushPose();

			Vec3 fruitPos = (new Vec3(-1.25D, 0.0D, 0.0D)).yRot(-netHeadYaw * ((float) Math.PI / 180F) - ((float) Math.PI / 2F));
			matrixStack.translate(fruitPos.x(), fruitPos.y() + 1.15F + this.getFruitPosOffset(booflo), fruitPos.z());

			matrixStack.mulPose(Vector3f.XP.rotationDegrees(90.0F));

			matrixStack.scale(1.3F, 1.3F, 1.3F);

			this.itemInHandRenderer.renderItem(booflo, new ItemStack(EEItems.BOLLOOM_FRUIT.get()), ItemTransforms.TransformType.GROUND, false, matrixStack, bufferIn, packedLightIn);
			matrixStack.popPose();
		}
	}

	private float getFruitPosOffset(Booflo booflo) {
		return 0.22F * Booflo.getEatingOffsetProgress(booflo.getAnimationTick() + ClientInfo.getPartialTicks());
	}
}