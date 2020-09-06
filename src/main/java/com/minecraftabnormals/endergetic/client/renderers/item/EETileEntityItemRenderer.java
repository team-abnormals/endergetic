package com.minecraftabnormals.endergetic.client.renderers.item;

import java.util.function.Supplier;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EETileEntityItemRenderer<T extends TileEntity> extends ItemStackTileEntityRenderer {
	private final Supplier<T> te;

	public EETileEntityItemRenderer(Supplier<T> te) {
		this.te = te;
	}
	
	@Override
	public void func_239207_a_(ItemStack itemStack, TransformType type, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
		TileEntityRendererDispatcher.instance.renderItem(this.te.get(), matrixStack, buffer, combinedLight, combinedOverlay);
	}
}