package com.minecraftabnormals.endergetic.core.registry.other;

import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.minecraftabnormals.endergetic.core.registry.EEItems;
import com.teamabnormals.abnormals_core.core.utils.DataUtils;

/**
 * @author SmellyModder (Luke Tonon)
 */
public final class EECompostables {

	public static void registerCompostables() {
		DataUtils.registerCompostable(EEBlocks.POISE_BUSH.get(), 0.3F);
		DataUtils.registerCompostable(EEBlocks.TALL_POISE_BUSH.get(), 0.5F);
		DataUtils.registerCompostable(EEBlocks.POISE_CLUSTER.get(), 0.85F);
		DataUtils.registerCompostable(EEBlocks.BOLLOOM_BUD.get(), 1.0F);
		DataUtils.registerCompostable(EEItems.BOLLOOM_FRUIT.get(), 0.65F);
		DataUtils.registerCompostable(EEBlocks.BOLLOOM_CRATE.get(), 1.0F);
	}

}
