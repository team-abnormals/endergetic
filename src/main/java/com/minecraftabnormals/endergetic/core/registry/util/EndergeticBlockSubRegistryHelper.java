package com.minecraftabnormals.endergetic.core.registry.util;

import com.minecraftabnormals.abnormals_core.core.util.registry.BlockSubRegistryHelper;
import com.minecraftabnormals.abnormals_core.core.util.registry.RegistryHelper;
import com.minecraftabnormals.endergetic.common.items.blockitems.CorrockCrownSBlockItem;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public final class EndergeticBlockSubRegistryHelper extends BlockSubRegistryHelper {

	public EndergeticBlockSubRegistryHelper(RegistryHelper parent) {
		super(parent);
	}

	public <B extends Block> RegistryObject<B> createCorrockStandingBlock(String name, Supplier<? extends B> standingSupplier, Supplier<? extends B> wallSupplier, @Nullable ItemGroup group) {
		RegistryObject<B> standingBlock = this.deferredRegister.register(name, standingSupplier);
		this.itemRegister.register(name, () -> new CorrockCrownSBlockItem(standingBlock.get(), wallSupplier::get, new Item.Properties().group(group)));
		return standingBlock;
	}

}
