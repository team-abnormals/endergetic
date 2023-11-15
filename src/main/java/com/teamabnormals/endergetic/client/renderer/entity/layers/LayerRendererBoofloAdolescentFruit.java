package com.teamabnormals.endergetic.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.teamabnormals.endergetic.client.model.booflo.AdolescentBoofloModel;
import com.teamabnormals.endergetic.common.entity.booflo.BoofloAdolescent;
import com.teamabnormals.endergetic.core.registry.EEItems;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LayerRendererBoofloAdolescentFruit extends RenderLayer<BoofloAdolescent, AdolescentBoofloModel<BoofloAdolescent>> {
	private final ItemInHandRenderer itemInHandRenderer;

	public LayerRendererBoofloAdolescentFruit(RenderLayerParent<BoofloAdolescent, AdolescentBoofloModel<BoofloAdolescent>> renderer, ItemInHandRenderer itemInHandRenderer) {
		super(renderer);
		this.itemInHandRenderer = itemInHandRenderer;
	}

	@Override
	public void render(PoseStack matrixStack, MultiBufferSource bufferIn, int packedLightIn, BoofloAdolescent adolescent, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (adolescent.hasFruit()) {
			matrixStack.pushPose();
			matrixStack.translate(0.0F, 1.1F, -0.2F);

			matrixStack.mulPose(Vector3f.XP.rotationDegrees(90.0F));

			this.itemInHandRenderer.renderItem(adolescent, new ItemStack(EEItems.BOLLOOM_FRUIT.get()), ItemTransforms.TransformType.GROUND, false, matrixStack, bufferIn, packedLightIn);
			matrixStack.popPose();
		}
	}
}