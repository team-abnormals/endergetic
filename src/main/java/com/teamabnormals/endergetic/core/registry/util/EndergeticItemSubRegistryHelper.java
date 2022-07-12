package com.teamabnormals.endergetic.core.registry.util;

import com.teamabnormals.endergetic.common.items.EetleSpawnEggItem;
import com.teamabnormals.blueprint.core.util.registry.ItemSubRegistryHelper;
import com.teamabnormals.blueprint.core.util.registry.RegistryHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.registries.RegistryObject;

public final class EndergeticItemSubRegistryHelper extends ItemSubRegistryHelper {

	public EndergeticItemSubRegistryHelper(RegistryHelper parent) {
		super(parent, parent.getItemSubHelper().getDeferredRegister());
	}

	public RegistryObject<EetleSpawnEggItem> createEetleSpawnEgg() {
		return this.deferredRegister.register("eetle_spawn_egg", () -> new EetleSpawnEggItem(7964867, 3943508, new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
	}

}
