package com.minecraftabnormals.endergetic.core.registry.other;

import com.minecraftabnormals.abnormals_core.core.registry.LootInjectionRegistry;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import net.minecraft.loot.LootTables;

public final class EELootInjectors {

	public static void registerLootInjectors() {
		LootInjectionRegistry.LootInjector injector = new LootInjectionRegistry.LootInjector(EndergeticExpansion.MOD_ID);
		injector.addLootInjection(injector.buildLootPool("end_city_treasure", 1, 0), LootTables.CHESTS_END_CITY_TREASURE);
	}

}
