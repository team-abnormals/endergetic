package com.minecraftabnormals.endergetic.core.registry.other;

import com.minecraftabnormals.abnormals_core.core.utils.DataUtils;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;

public class EEBlockRegistrars {
	public static void registerFireInfo() {
		DataUtils.registerFlammable(EEBlocks.POISE_PLANKS.get(), 5, 20);
		DataUtils.registerFlammable(EEBlocks.POISE_SLAB.get(), 5, 20);
		DataUtils.registerFlammable(EEBlocks.POISE_STAIRS.get(), 5, 20);
		DataUtils.registerFlammable(EEBlocks.POISE_FENCE.get(), 5, 20);
		DataUtils.registerFlammable(EEBlocks.POISE_FENCE_GATE.get(), 5, 20);
		DataUtils.registerFlammable(EEBlocks.POISE_VERTICAL_PLANKS.get(), 5, 20);
		DataUtils.registerFlammable(EEBlocks.POISE_VERTICAL_SLAB.get(), 5, 20);
		DataUtils.registerFlammable(EEBlocks.POISE_BOOKSHELF.get(), 5, 20);
	}
	
	public static void setupRenderLayers() {
		setRenderLayer(EEBlocks.CORROCK_END.get(), RenderType.getCutout());
		setRenderLayer(EEBlocks.CORROCK_NETHER.get(), RenderType.getCutout());
		setRenderLayer(EEBlocks.CORROCK_OVERWORLD.get(), RenderType.getCutout());
		setRenderLayer(EEBlocks.PETRIFIED_CORROCK_END.get(), RenderType.getCutout());
		setRenderLayer(EEBlocks.PETRIFIED_CORROCK_NETHER.get(), RenderType.getCutout());
		setRenderLayer(EEBlocks.PETRIFIED_CORROCK_OVERWORLD.get(), RenderType.getCutout());
		setRenderLayer(EEBlocks.ENDER_FIRE.get(), RenderType.getCutout());
		
		setRenderLayer(EEBlocks.POISE_BUSH_POT.get(), RenderType.getCutout());
		setRenderLayer(EEBlocks.POISE_DOOR.get(), RenderType.getCutout());
		setRenderLayer(EEBlocks.POISE_TRAPDOOR.get(), RenderType.getCutout());
		setRenderLayer(EEBlocks.POISE_LADDER.get(), RenderType.getCutout());
		setRenderLayer(EEBlocks.POISMOSS_EUMUS.get(), RenderType.getCutoutMipped());
		setRenderLayer(EEBlocks.POISMOSS.get(), RenderType.getCutoutMipped());
		setRenderLayer(EEBlocks.POISE_BUSH.get(), RenderType.getCutout());
		setRenderLayer(EEBlocks.POISE_BUSH_TALL.get(), RenderType.getCutout());
		setRenderLayer(EEBlocks.POISE_CLUSTER.get(), RenderType.getTranslucent());
		setRenderLayer(EEBlocks.HIVE_HANGER.get(), RenderType.getCutout());
	}
	
	private static synchronized void setRenderLayer(Block block, RenderType type) {
		RenderTypeLookup.setRenderLayer(block, type::equals);
	}
}