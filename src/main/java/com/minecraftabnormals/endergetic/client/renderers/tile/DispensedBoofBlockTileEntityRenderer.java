package com.minecraftabnormals.endergetic.client.renderers.tile;

import com.minecraftabnormals.endergetic.client.models.BoofBlockDispenserModel;
import com.minecraftabnormals.endergetic.common.blocks.poise.boof.DispensedBoofBlock;
import com.minecraftabnormals.endergetic.common.tileentities.boof.DispensedBlockBoofTileEntity;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

public class DispensedBoofBlockTileEntityRenderer extends TileEntityRenderer<DispensedBlockBoofTileEntity> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/block/boof_block_dispensed.png");
	private final BoofBlockDispenserModel model;

	public DispensedBoofBlockTileEntityRenderer(TileEntityRendererDispatcher renderDispatcher) {
		super(renderDispatcher);
		this.model = new BoofBlockDispenserModel();
	}

	@Override
	public void render(DispensedBlockBoofTileEntity boof, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn) {
		Direction facing = boof.hasWorld() ? boof.getBlockState().get(DispensedBoofBlock.FACING) : Direction.NORTH;

		matrixStack.push();
		matrixStack.translate(0.5F, 1.5F, 0.5F);

		if (facing.getAxis().isVertical()) {
			float offset = -facing.getAxisDirection().getOffset();
			matrixStack.rotate(Vector3f.XP.rotationDegrees(90.0F * offset));
			matrixStack.translate(0.0F, 1.125F, 1.0F * offset);
		} else {
			matrixStack.rotate(Vector3f.YP.rotationDegrees(-facing.getHorizontalAngle()));
		}

		matrixStack.scale(1.0F, -1.0F, -1.0F);

		IVertexBuilder ivertexbuilder = buffer.getBuffer(RenderType.getEntityCutout(TEXTURE));
		this.model.renderAll(matrixStack, ivertexbuilder, combinedLightIn, combinedOverlayIn);

		matrixStack.pop();
	}
}