package com.minecraftabnormals.endergetic.client.renderers.tile;

import com.minecraftabnormals.endergetic.client.EERenderTypes;
import com.minecraftabnormals.endergetic.client.models.eetle.eggs.*;
import com.minecraftabnormals.endergetic.client.models.eetle.eggs.IEetleEggModel;
import com.minecraftabnormals.endergetic.client.models.eetle.eggs.MediumEetleEggModel;
import com.minecraftabnormals.endergetic.common.blocks.EetleEggBlock;
import com.minecraftabnormals.endergetic.common.tileentities.EetleEggTileEntity;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;

import java.util.Random;

public class EetleEggTileEntityRenderer extends TileEntityRenderer<EetleEggTileEntity> {
	public static final ResourceLocation[] TEXTURES = new ResourceLocation[] {
			new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/tile/eggs/small_eetle_egg.png"),
			new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/tile/eggs/medium_eetle_egg.png"),
			new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/tile/eggs/large_eetle_egg.png")
	};
	private static final BlockState DEFAULT_STATE = EEBlocks.EETLE_EGG.get().getDefaultState();
	private static final Random ROTATION_RANDOM = new Random();
	private final IEetleEggModel[] eggModels = new IEetleEggModel[] {
			new SmallEetleEggModel(),
			new MediumEetleEggModel(),
			new LargeEetleEggModel()
	};

	public EetleEggTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
	}

	@Override
	public void render(EetleEggTileEntity eggs, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
		World world = eggs.getWorld();
		BlockState state = DEFAULT_STATE;
		Direction randomDirection = Direction.NORTH;
		if (world != null) {
			state = eggs.getBlockState();
			ROTATION_RANDOM.setSeed(getPosSeed(eggs.getPos()));
			randomDirection = Direction.Plane.HORIZONTAL.random(ROTATION_RANDOM);
		}
		matrixStack.push();
		Direction facing = state.get(EetleEggBlock.FACING);
		switch (facing) {
			case UP:
				matrixStack.translate(0.5F, 1.5F, 0.5F);
				matrixStack.rotate(Vector3f.XP.rotationDegrees(180.0F));
				break;
			case DOWN:
				matrixStack.translate(0.5F, -0.5F, 0.5F);
				break;
			default:
				matrixStack.translate(0.5F + facing.getXOffset(), 0.5F, 0.5F + facing.getZOffset());
				matrixStack.rotate((facing.getAxis() == Direction.Axis.X ? Vector3f.ZP : Vector3f.XN).rotationDegrees(90.0F * facing.getAxisDirection().getOffset()));
				break;
		}
		matrixStack.rotate(Vector3f.YP.rotationDegrees(randomDirection.getHorizontalAngle()));
		int size = state.get(EetleEggBlock.SIZE);
		IEetleEggModel eggsModel = this.eggModels[size];
		eggsModel.render(matrixStack, buffer.getBuffer(RenderType.getEntityCutout(TEXTURES[size])), combinedLight, combinedOverlay, partialTicks, eggs.getSackGrowths());
		eggsModel.renderSilk(matrixStack, buffer.getBuffer(EERenderTypes.EETLE_EGG_SILK), combinedLight, combinedOverlay);
		matrixStack.pop();
	}

	private static long getPosSeed(BlockPos pos) {
		return pos.getX() * 0x2FF20L ^ pos.getY() * 0x0FFFF ^ pos.getZ();
	}
}
