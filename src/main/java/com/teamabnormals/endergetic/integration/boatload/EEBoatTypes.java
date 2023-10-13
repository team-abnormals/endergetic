package com.teamabnormals.endergetic.integration.boatload;

import com.teamabnormals.boatload.common.item.FurnaceBoatItem;
import com.teamabnormals.boatload.common.item.LargeBoatItem;
import com.teamabnormals.boatload.core.api.BoatloadBoatType;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import com.teamabnormals.endergetic.core.registry.EEBlocks;
import com.teamabnormals.endergetic.core.registry.EEItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class EEBoatTypes {
	public static final BoatloadBoatType POISE = BoatloadBoatType.register(BoatloadBoatType.create(new ResourceLocation(EndergeticExpansion.MOD_ID, "poise"), () -> EEBlocks.POISE_PLANKS.get().asItem(), () -> EEItems.POISE_BOAT.getFirst().get(), () -> EEItems.POISE_BOAT.getSecond().get(), () -> EEItems.POISE_FURNACE_BOAT.get(), () -> EEItems.LARGE_POISE_BOAT.get()));

	public static final Supplier<Item> POISE_FURNACE_BOAT = () -> new FurnaceBoatItem(POISE);
	public static final Supplier<Item> LARGE_POISE_BOAT = () -> new LargeBoatItem(POISE);
}