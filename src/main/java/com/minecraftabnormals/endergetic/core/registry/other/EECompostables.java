package com.minecraftabnormals.endergetic.core.registry.other;

import com.minecraftabnormals.abnormals_core.core.util.DataUtil;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.minecraftabnormals.endergetic.core.registry.EEItems;

/**
 * @author SmellyModder (Luke Tonon)
 */
public final class EECompostables {

	public static void registerCompostables() {
		DataUtil.registerCompostable(EEBlocks.POISE_BUSH.get(), 0.3F);
		DataUtil.registerCompostable(EEBlocks.TALL_POISE_BUSH.get(), 0.5F);
		DataUtil.registerCompostable(EEBlocks.POISE_CLUSTER.get(), 0.85F);
		DataUtil.registerCompostable(EEBlocks.BOLLOOM_BUD.get(), 1.0F);
		DataUtil.registerCompostable(EEItems.BOLLOOM_FRUIT.get(), 0.65F);
		DataUtil.registerCompostable(EEBlocks.BOLLOOM_CRATE.get(), 1.0F);
	}

}
