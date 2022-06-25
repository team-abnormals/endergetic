package com.minecraftabnormals.endergetic.client.renderers.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.minecraftabnormals.endergetic.client.models.corrock.CorrockCrownStandingModel;
import com.minecraftabnormals.endergetic.client.models.corrock.CorrockCrownWallModel;
import com.minecraftabnormals.endergetic.common.blocks.CorrockCrownStandingBlock;
import com.minecraftabnormals.endergetic.common.tileentities.CorrockCrownTileEntity;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;

import com.teamabnormals.blueprint.client.BlueprintRenderTypes;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import com.mojang.math.Vector3f;

public class CorrockCrownTileEntityRenderer implements BlockEntityRenderer<CorrockCrownTileEntity> {
	public CorrockCrownStandingModel standingModel;
	public CorrockCrownWallModel wallModel;
	private static final ResourceLocation[] TEXTURES = {
			new ResourceLocation(EndergeticExpansion.MOD_ID + ":textures/tile/end_corrock_crown.png"),
			new ResourceLocation(EndergeticExpansion.MOD_ID + ":textures/tile/nether_corrock_crown.png"),
			new ResourceLocation(EndergeticExpansion.MOD_ID + ":textures/tile/overworld_corrock_crown.png")
	};

	public CorrockCrownTileEntityRenderer(BlockEntityRendererProvider.Context context) {
		this.standingModel = new CorrockCrownStandingModel(context.bakeLayer(CorrockCrownStandingModel.LOCATION));
		this.wallModel = new CorrockCrownWallModel(context.bakeLayer(CorrockCrownWallModel.LOCATION));
	}

	@Override
	public void render(CorrockCrownTileEntity te, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
		BlockState state = te.getBlockState();
		boolean isStanding = state.getBlock() instanceof CorrockCrownStandingBlock;

		matrixStack.pushPose();

		if (isStanding) {
			matrixStack.translate(0.5F, 1.5F, 0.5F);
			float angle = -((float) (state.getValue(StandingSignBlock.ROTATION) * 360) / 16.0F);
			matrixStack.mulPose(Vector3f.YP.rotationDegrees(angle));
		} else {
			matrixStack.translate(0.5F, 1.5F, 0.5F);
			float angle = -state.getValue(WallSignBlock.FACING).getOpposite().toYRot();
			matrixStack.mulPose(Vector3f.YP.rotationDegrees(angle));

			matrixStack.translate(0.0F, -0.4F, 0.05F);
		}

		if (isStanding && state.getValue(CorrockCrownStandingBlock.UPSIDE_DOWN)) {
			matrixStack.mulPose(Vector3f.XP.rotationDegrees(180.0F));
			matrixStack.translate(0.0F, 2.0F, 0.0F);
		}
		matrixStack.scale(1.0F, -1.0F, -1.0F);

		VertexConsumer ivertexbuilder = buffer.getBuffer(BlueprintRenderTypes.getUnshadedCutoutEntity(TEXTURES[this.getTexture(te)], true));
		if (isStanding) {
			this.standingModel.renderAll(matrixStack, ivertexbuilder, 240, combinedOverlay);
		} else {
			this.wallModel.renderAll(matrixStack, ivertexbuilder, 240, combinedOverlay);
		}

		matrixStack.popPose();
	}

	public int getTexture(CorrockCrownTileEntity te) {
		Block block = te.getBlockState().getBlock();
		if (block == EEBlocks.CORROCK_CROWN_END_STANDING.get() || block == EEBlocks.CORROCK_CROWN_END_WALL.get() || block == EEBlocks.PETRIFIED_CORROCK_CROWN_END_STANDING.get() || block == EEBlocks.PETRIFIED_CORROCK_CROWN_END_WALL.get()) {
			return 0;
		} else if (block == EEBlocks.CORROCK_CROWN_NETHER_STANDING.get() || block == EEBlocks.CORROCK_CROWN_NETHER_WALL.get() || block == EEBlocks.PETRIFIED_CORROCK_CROWN_NETHER_STANDING.get() || block == EEBlocks.PETRIFIED_CORROCK_CROWN_NETHER_WALL.get()) {
			return 1;
		} else {
			return 2;
		}
	}
}