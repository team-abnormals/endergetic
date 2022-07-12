package com.teamabnormals.endergetic.core.data.server;

import com.teamabnormals.endergetic.core.EndergeticExpansion;
import com.teamabnormals.endergetic.core.registry.EEItems;
import com.teamabnormals.blueprint.common.loot.modification.LootModifierProvider;
import com.teamabnormals.blueprint.common.loot.modification.modifiers.LootPoolsModifier;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;

import java.util.List;

public final class EELootModifierProvider extends LootModifierProvider {

	public EELootModifierProvider(DataGenerator dataGenerator) {
		super(dataGenerator, EndergeticExpansion.MOD_ID);
	}

	@Override
	protected void registerEntries() {
		LootPool kilobyte = LootPool.lootPool()
				.name(EndergeticExpansion.MOD_ID + ":" + "kilobyte")
				.add(LootItem.lootTableItem(EEItems.MUSIC_DISC_KILOBYTE.get()).apply(SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(1, 0.08F))))
				.build();
		this.entry("chests/end_city_treasure")
				.selects(BuiltInLootTables.END_CITY_TREASURE)
				.addModifier(new LootPoolsModifier(List.of(kilobyte), false));
	}

}
