package com.minecraftabnormals.endergetic.core.registry.other;

import com.minecraftabnormals.abnormals_core.core.util.registry.ItemSubRegistryHelper;
import com.minecraftabnormals.endergetic.common.items.EndergeticEnderCrystalItem;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

//TODO: Move to Mixin
@Mod.EventBusSubscriber(modid = EndergeticExpansion.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class EERegistryReplacements {

	@ObjectHolder("minecraft:end_crystal")
	public static Item END_CRYSTAL;

	@SubscribeEvent
	public static void registerItemReplacements(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(
				new EndergeticEnderCrystalItem(ItemSubRegistryHelper.createSimpleItemProperty(64, ItemGroup.DECORATIONS)).setRegistryName("minecraft:end_crystal")
		);
	}

}