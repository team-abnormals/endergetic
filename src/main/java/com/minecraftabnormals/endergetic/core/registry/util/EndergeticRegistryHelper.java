package com.minecraftabnormals.endergetic.core.registry.util;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.minecraftabnormals.abnormals_core.core.utils.RegistryHelper;
import com.minecraftabnormals.endergetic.common.items.blockitems.CorrockCrownSBlockItem;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;

public class EndergeticRegistryHelper extends RegistryHelper {

	public EndergeticRegistryHelper(String modId) {
		super(modId);
	}
	
	public <B extends Block> RegistryObject<B> createCorrockStandingBlock(String name, Supplier<? extends B> standingSupplier, Supplier<? extends B> wallSupplier, @Nullable ItemGroup group) {
		RegistryObject<B> standingBlock = this.getDeferredBlockRegister().register(name, standingSupplier);
		this.getDeferredItemRegister().register(name, () -> new CorrockCrownSBlockItem(standingBlock.get(), () -> wallSupplier.get(), new Item.Properties().group(group)));
		return standingBlock;
	}

}