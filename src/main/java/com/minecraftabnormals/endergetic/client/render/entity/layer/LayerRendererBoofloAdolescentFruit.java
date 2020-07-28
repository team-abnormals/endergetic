package com.minecraftabnormals.endergetic.client.render.entity.layer;

import com.minecraftabnormals.endergetic.client.model.booflo.AdolescentBoofloModel;
import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloAdolescentEntity;
import com.minecraftabnormals.endergetic.core.registry.EEItems;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LayerRendererBoofloAdolescentFruit extends LayerRenderer<BoofloAdolescentEntity, AdolescentBoofloModel<BoofloAdolescentEntity>> {
	
	public LayerRendererBoofloAdolescentFruit(IEntityRenderer<BoofloAdolescentEntity, AdolescentBoofloModel<BoofloAdolescentEntity>> renderer) {
		super(renderer);
	}
	
	@Override
	public void render(MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int packedLightIn, BoofloAdolescentEntity adolescent, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if(adolescent.hasFruit()) {
			matrixStack.push();
			matrixStack.translate(0.0F, 1.1F, -0.2F);
			
			matrixStack.rotate(Vector3f.XP.rotationDegrees(90.0F));
			
			Minecraft.getInstance().getFirstPersonRenderer().renderItemSide(adolescent, new ItemStack(EEItems.BOLLOOM_FRUIT.get()), ItemCameraTransforms.TransformType.GROUND, false, matrixStack, bufferIn, packedLightIn);
			matrixStack.pop();
		}
	}
	
}