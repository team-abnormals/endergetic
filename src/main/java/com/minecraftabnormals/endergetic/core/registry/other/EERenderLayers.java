package com.minecraftabnormals.endergetic.core.registry.other;

import com.minecraftabnormals.endergetic.core.registry.EEBlocks;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;

public final class EERenderLayers {

	public static void setupRenderLayers() {
		setRenderLayer(EEBlocks.CORROCK_END.get(), RenderType.getCutout());
		setRenderLayer(EEBlocks.CORROCK_NETHER.get(), RenderType.getCutout());
		setRenderLayer(EEBlocks.CORROCK_OVERWORLD.get(), RenderType.getCutout());
		setRenderLayer(EEBlocks.PETRIFIED_CORROCK_END.get(), RenderType.getCutout());
		setRenderLayer(EEBlocks.PETRIFIED_CORROCK_NETHER.get(), RenderType.getCutout());
		setRenderLayer(EEBlocks.PETRIFIED_CORROCK_OVERWORLD.get(), RenderType.getCutout());

		setRenderLayer(EEBlocks.ENDER_FIRE.get(), RenderType.getCutout());
		setRenderLayer(EEBlocks.ENDER_TORCH.get(), RenderType.getCutout());
		setRenderLayer(EEBlocks.ENDER_WALL_TORCH.get(), RenderType.getCutout());
		setRenderLayer(EEBlocks.ENDER_LANTERN.get(), RenderType.getCutout());
		setRenderLayer(EEBlocks.ENDER_CAMPFIRE.get(), RenderType.getCutout());

		setRenderLayer(EEBlocks.POTTED_POISE_BUSH.get(), RenderType.getCutout());
		setRenderLayer(EEBlocks.POTTED_TALL_POISE_BUSH.get(), RenderType.getCutout());
		setRenderLayer(EEBlocks.POISE_DOOR.get(), RenderType.getCutout());
		setRenderLayer(EEBlocks.POISE_TRAPDOOR.get(), RenderType.getCutout());
		setRenderLayer(EEBlocks.POISE_LADDER.get(), RenderType.getCutout());
		setRenderLayer(EEBlocks.EUMUS_POISMOSS.get(), RenderType.getCutoutMipped());
		setRenderLayer(EEBlocks.POISMOSS.get(), RenderType.getCutoutMipped());
		setRenderLayer(EEBlocks.POISE_BUSH.get(), RenderType.getCutout());
		setRenderLayer(EEBlocks.TALL_POISE_BUSH.get(), RenderType.getCutout());
		setRenderLayer(EEBlocks.POISE_CLUSTER.get(), RenderType.getTranslucent());
		setRenderLayer(EEBlocks.HIVE_HANGER.get(), RenderType.getCutout());
	}

	private static void setRenderLayer(Block block, RenderType type) {
		RenderTypeLookup.setRenderLayer(block, type::equals);
	}

}