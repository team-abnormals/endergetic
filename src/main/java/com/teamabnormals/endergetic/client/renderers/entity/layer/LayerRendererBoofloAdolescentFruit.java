package com.teamabnormals.endergetic.client.renderers.entity.layer;

import com.teamabnormals.endergetic.client.models.booflo.AdolescentBoofloModel;
import com.teamabnormals.endergetic.common.entities.booflo.BoofloAdolescentEntity;
import com.teamabnormals.endergetic.core.registry.EEItems;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.item.ItemStack;
import com.mojang.math.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LayerRendererBoofloAdolescentFruit extends RenderLayer<BoofloAdolescentEntity, AdolescentBoofloModel<BoofloAdolescentEntity>> {
	private final ItemInHandRenderer itemInHandRenderer;

	public LayerRendererBoofloAdolescentFruit(RenderLayerParent<BoofloAdolescentEntity, AdolescentBoofloModel<BoofloAdolescentEntity>> renderer, ItemInHandRenderer itemInHandRenderer) {
		super(renderer);
		this.itemInHandRenderer = itemInHandRenderer;
	}

	@Override
	public void render(PoseStack matrixStack, MultiBufferSource bufferIn, int packedLightIn, BoofloAdolescentEntity adolescent, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (adolescent.hasFruit()) {
			matrixStack.pushPose();
			matrixStack.translate(0.0F, 1.1F, -0.2F);

			matrixStack.mulPose(Vector3f.XP.rotationDegrees(90.0F));

			this.itemInHandRenderer.renderItem(adolescent, new ItemStack(EEItems.BOLLOOM_FRUIT.get()), ItemTransforms.TransformType.GROUND, false, matrixStack, bufferIn, packedLightIn);
			matrixStack.popPose();
		}
	}
}