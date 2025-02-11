package com.teamabnormals.endergetic.core.other;

import com.teamabnormals.endergetic.core.registry.EEBlocks;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.ChunkRenderTypeSet;

public class EEClientCompat {

	public static void registerClientCompat() {
		registerRenderLayers();
	}

	private static void registerRenderLayers() {
		setRenderLayer(EEBlocks.END_CORROCK.get(), RenderType.cutout());
		setRenderLayer(EEBlocks.NETHER_CORROCK.get(), RenderType.cutout());
		setRenderLayer(EEBlocks.OVERWORLD_CORROCK.get(), RenderType.cutout());
		setRenderLayer(EEBlocks.PETRIFIED_END_CORROCK.get(), RenderType.cutout());
		setRenderLayer(EEBlocks.PETRIFIED_NETHER_CORROCK.get(), RenderType.cutout());
		setRenderLayer(EEBlocks.PETRIFIED_OVERWORLD_CORROCK.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(EEBlocks.PORTAPLASM_BLOCK.get(), ChunkRenderTypeSet.of(RenderType.tripwire(), RenderType.translucent()));

		setRenderLayer(EEBlocks.ENDER_FIRE.get(), RenderType.cutout());
		setRenderLayer(EEBlocks.ENDER_TORCH.get(), RenderType.cutout());
		setRenderLayer(EEBlocks.ENDER_WALL_TORCH.get(), RenderType.cutout());
		setRenderLayer(EEBlocks.ENDER_LANTERN.get(), RenderType.cutout());
		setRenderLayer(EEBlocks.ENDER_CAMPFIRE.get(), RenderType.cutout());

		setRenderLayer(EEBlocks.POTTED_POISE_BUSH.get(), RenderType.cutout());
		setRenderLayer(EEBlocks.POTTED_TALL_POISE_BUSH.get(), RenderType.tripwire());
		setRenderLayer(EEBlocks.POISE_DOOR.get(), RenderType.cutout());
		setRenderLayer(EEBlocks.POISE_TRAPDOOR.get(), RenderType.cutout());
		setRenderLayer(EEBlocks.POISE_LADDER.get(), RenderType.cutout());
		setRenderLayer(EEBlocks.EUMUS_POISMOSS.get(), RenderType.cutoutMipped());
		setRenderLayer(EEBlocks.POISMOSS.get(), RenderType.cutoutMipped());
		setRenderLayer(EEBlocks.POISE_BUSH.get(), RenderType.cutout());
		setRenderLayer(EEBlocks.TALL_POISE_BUSH.get(), RenderType.tripwire());
		setRenderLayer(EEBlocks.POISE_CLUSTER.get(), RenderType.translucent());
		setRenderLayer(EEBlocks.HIVE_HANGER.get(), RenderType.cutout());
	}

	private static void setRenderLayer(Block block, RenderType type) {
		ItemBlockRenderTypes.setRenderLayer(block, type::equals);
	}

}