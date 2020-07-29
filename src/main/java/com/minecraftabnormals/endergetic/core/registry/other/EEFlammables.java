package com.minecraftabnormals.endergetic.core.registry.other;

import com.teamabnormals.abnormals_core.core.utils.DataUtils;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;

public class EEFlammables {

	public static void registerFlammables() {
		DataUtils.registerFlammable(EEBlocks.POISE_LOG.get(), 5, 5);
		DataUtils.registerFlammable(EEBlocks.POISE_WOOD.get(), 5, 5);
		DataUtils.registerFlammable(EEBlocks.POISE_LOG_STRIPPED.get(), 5, 5);
		DataUtils.registerFlammable(EEBlocks.POISE_WOOD_STRIPPED.get(), 5, 5);
		DataUtils.registerFlammable(EEBlocks.POISE_PLANKS.get(), 5, 20);
		DataUtils.registerFlammable(EEBlocks.POISE_SLAB.get(), 5, 20);
		DataUtils.registerFlammable(EEBlocks.POISE_STAIRS.get(), 5, 20);
		DataUtils.registerFlammable(EEBlocks.POISE_FENCE.get(), 5, 20);
		DataUtils.registerFlammable(EEBlocks.POISE_FENCE_GATE.get(), 5, 20);
		DataUtils.registerFlammable(EEBlocks.POISE_VERTICAL_PLANKS.get(), 5, 20);
		DataUtils.registerFlammable(EEBlocks.POISE_VERTICAL_SLAB.get(), 5, 20);
	}
	
}