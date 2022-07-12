package com.teamabnormals.endergetic.client.renderers.tile;

import com.teamabnormals.endergetic.client.EERenderTypes;
import com.teamabnormals.endergetic.client.models.eetle.eggs.*;
import com.teamabnormals.endergetic.client.models.eetle.eggs.IEetleEggModel;
import com.teamabnormals.endergetic.client.models.eetle.eggs.MediumEetleEggModel;
import com.teamabnormals.endergetic.common.blocks.EetleEggBlock;
import com.teamabnormals.endergetic.common.tileentities.EetleEggTileEntity;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import com.teamabnormals.endergetic.core.registry.EEBlocks;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import com.mojang.math.Vector3f;
import net.minecraft.world.level.Level;

public class EetleEggTileEntityRenderer implements BlockEntityRenderer<EetleEggTileEntity> {
	public static final ResourceLocation[] TEXTURES = new ResourceLocation[] {
			new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/tile/eggs/small_eetle_egg.png"),
			new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/tile/eggs/medium_eetle_egg.png"),
			new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/tile/eggs/large_eetle_egg.png")
	};
	private static final BlockState DEFAULT_STATE = EEBlocks.EETLE_EGG.get().defaultBlockState();
	private static final RandomSource ROTATION_RANDOM = RandomSource.create();
	private final IEetleEggModel[] eggModels;

	public EetleEggTileEntityRenderer(BlockEntityRendererProvider.Context context) {
		this.eggModels = new IEetleEggModel[] {
				new SmallEetleEggModel(context.bakeLayer(SmallEetleEggModel.LOCATION)),
				new MediumEetleEggModel(context.bakeLayer(MediumEetleEggModel.LOCATION)),
				new LargeEetleEggModel(context.bakeLayer(LargeEetleEggModel.LOCATION))
		};
	}

	@Override
	public void render(EetleEggTileEntity eggs, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
		Level world = eggs.getLevel();
		BlockState state = DEFAULT_STATE;
		Direction randomDirection = Direction.NORTH;
		if (world != null) {
			state = eggs.getBlockState();
			ROTATION_RANDOM.setSeed(getPosSeed(eggs.getBlockPos()));
			randomDirection = Direction.Plane.HORIZONTAL.getRandomDirection(ROTATION_RANDOM);
		}
		matrixStack.pushPose();
		Direction facing = state.getValue(EetleEggBlock.FACING);
		switch (facing) {
			case UP:
				matrixStack.translate(0.5F, 1.5F, 0.5F);
				matrixStack.mulPose(Vector3f.XP.rotationDegrees(180.0F));
				break;
			case DOWN:
				matrixStack.translate(0.5F, -0.5F, 0.5F);
				break;
			default:
				matrixStack.translate(0.5F + facing.getStepX(), 0.5F, 0.5F + facing.getStepZ());
				matrixStack.mulPose((facing.getAxis() == Direction.Axis.X ? Vector3f.ZP : Vector3f.XN).rotationDegrees(90.0F * facing.getAxisDirection().getStep()));
				break;
		}
		matrixStack.mulPose(Vector3f.YP.rotationDegrees(randomDirection.toYRot()));
		int size = state.getValue(EetleEggBlock.SIZE);
		IEetleEggModel eggsModel = this.eggModels[size];
		eggsModel.render(matrixStack, buffer.getBuffer(RenderType.entityCutout(TEXTURES[size])), combinedLight, combinedOverlay, partialTicks, eggs.getSackGrowths());
		eggsModel.renderSilk(matrixStack, buffer.getBuffer(EERenderTypes.EETLE_EGG_SILK), combinedLight, combinedOverlay);
		matrixStack.popPose();
	}

	private static long getPosSeed(BlockPos pos) {
		return pos.getX() * 0x2FF20L ^ pos.getY() * 0x0FFFF ^ pos.getZ();
	}
}
