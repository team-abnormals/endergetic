package com.minecraftabnormals.endergetic.core.registry.other;

import com.minecraftabnormals.abnormals_core.core.util.DataUtil;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;

public final class EEFlammables {

	public static void registerFlammables() {
		DataUtil.registerFlammable(EEBlocks.POISE_STEM.get(), 5, 5);
		DataUtil.registerFlammable(EEBlocks.POISE_WOOD.get(), 5, 5);
		DataUtil.registerFlammable(EEBlocks.GLOWING_POISE_STEM.get(), 5, 5);
		DataUtil.registerFlammable(EEBlocks.GLOWING_POISE_WOOD.get(), 5, 5);
		DataUtil.registerFlammable(EEBlocks.STRIPPED_POISE_STEM.get(), 5, 5);
		DataUtil.registerFlammable(EEBlocks.STRIPPED_POISE_WOOD.get(), 5, 5);
		DataUtil.registerFlammable(EEBlocks.POISE_PLANKS.get(), 5, 20);
		DataUtil.registerFlammable(EEBlocks.POISE_SLAB.get(), 5, 20);
		DataUtil.registerFlammable(EEBlocks.POISE_STAIRS.get(), 5, 20);
		DataUtil.registerFlammable(EEBlocks.POISE_FENCE.get(), 5, 20);
		DataUtil.registerFlammable(EEBlocks.POISE_FENCE_GATE.get(), 5, 20);
		DataUtil.registerFlammable(EEBlocks.POISE_VERTICAL_PLANKS.get(), 5, 20);
		DataUtil.registerFlammable(EEBlocks.POISE_VERTICAL_SLAB.get(), 5, 20);
		DataUtil.registerFlammable(EEBlocks.POISE_BOOKSHELF.get(), 30, 20);
		DataUtil.registerFlammable(EEBlocks.POISE_BEEHIVE.get(), 5, 20);
		DataUtil.registerFlammable(EEBlocks.BOLLOOM_CRATE.get(), 5, 20);
		DataUtil.registerFlammable(EEBlocks.POISE_POST.get(), 5, 20);
		DataUtil.registerFlammable(EEBlocks.STRIPPED_POISE_POST.get(), 5, 20);
	}

}