package com.teamabnormals.endergetic.core.registry.other;

import com.teamabnormals.endergetic.core.registry.EEBlocks;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;

public class EEClientCompat {

	public static void registerClientCompat() {
		registerRenderLayers();
	}

	private static void registerRenderLayers() {
		setRenderLayer(EEBlocks.CORROCK_END.get(), RenderType.cutout());
		setRenderLayer(EEBlocks.CORROCK_NETHER.get(), RenderType.cutout());
		setRenderLayer(EEBlocks.CORROCK_OVERWORLD.get(), RenderType.cutout());
		setRenderLayer(EEBlocks.PETRIFIED_CORROCK_END.get(), RenderType.cutout());
		setRenderLayer(EEBlocks.PETRIFIED_CORROCK_NETHER.get(), RenderType.cutout());
		setRenderLayer(EEBlocks.PETRIFIED_CORROCK_OVERWORLD.get(), RenderType.cutout());

		setRenderLayer(EEBlocks.ENDER_FIRE.get(), RenderType.cutout());
		setRenderLayer(EEBlocks.ENDER_TORCH.get(), RenderType.cutout());
		setRenderLayer(EEBlocks.ENDER_WALL_TORCH.get(), RenderType.cutout());
		setRenderLayer(EEBlocks.ENDER_LANTERN.get(), RenderType.cutout());
		setRenderLayer(EEBlocks.ENDER_CAMPFIRE.get(), RenderType.cutout());

		setRenderLayer(EEBlocks.POTTED_POISE_BUSH.get(), RenderType.cutout());
		setRenderLayer(EEBlocks.POTTED_TALL_POISE_BUSH.get(), RenderType.translucent());
		setRenderLayer(EEBlocks.POISE_DOOR.get(), RenderType.cutout());
		setRenderLayer(EEBlocks.POISE_TRAPDOOR.get(), RenderType.cutout());
		setRenderLayer(EEBlocks.POISE_LADDER.get(), RenderType.cutout());
		setRenderLayer(EEBlocks.POISE_POST.get(), RenderType.cutout());
		setRenderLayer(EEBlocks.STRIPPED_POISE_POST.get(), RenderType.cutout());
		setRenderLayer(EEBlocks.EUMUS_POISMOSS.get(), RenderType.cutoutMipped());
		setRenderLayer(EEBlocks.POISMOSS.get(), RenderType.cutoutMipped());
		setRenderLayer(EEBlocks.POISE_BUSH.get(), RenderType.cutout());
		setRenderLayer(EEBlocks.TALL_POISE_BUSH.get(), RenderType.translucent());
		setRenderLayer(EEBlocks.POISE_CLUSTER.get(), RenderType.translucent());
		setRenderLayer(EEBlocks.HIVE_HANGER.get(), RenderType.cutout());
	}

	private static void setRenderLayer(Block block, RenderType type) {
		ItemBlockRenderTypes.setRenderLayer(block, type::equals);
	}

}