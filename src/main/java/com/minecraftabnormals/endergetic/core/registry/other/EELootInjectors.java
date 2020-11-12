package com.minecraftabnormals.endergetic.core.registry.other;

import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.minecraftabnormals.endergetic.core.registry.EEItems;
import com.teamabnormals.abnormals_core.core.registry.LootInjectionRegistry;
import com.teamabnormals.abnormals_core.core.utils.DataUtils;
import net.minecraft.loot.LootTables;

public final class EELootInjectors {

	public static void registerLootInjectors() {
		LootInjectionRegistry.LootInjector injector = new LootInjectionRegistry.LootInjector(EndergeticExpansion.MOD_ID);
		injector.registerLootInjection(injector.buildLootPool("end_city_treasure", 1, 0), LootTables.CHESTS_END_CITY_TREASURE);
	}

}
