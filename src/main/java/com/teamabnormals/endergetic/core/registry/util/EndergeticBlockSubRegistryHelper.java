package com.teamabnormals.endergetic.core.registry.util;

import com.teamabnormals.blueprint.core.util.registry.BlockSubRegistryHelper;
import com.teamabnormals.blueprint.core.util.registry.RegistryHelper;
import com.teamabnormals.endergetic.common.item.CorrockCrownBlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public final class EndergeticBlockSubRegistryHelper extends BlockSubRegistryHelper {

	public EndergeticBlockSubRegistryHelper(RegistryHelper parent) {
		super(parent);
	}

	public <B extends Block> RegistryObject<B> createCorrockStandingBlock(String name, Supplier<? extends B> standingSupplier, Supplier<? extends B> wallSupplier, @Nullable CreativeModeTab group) {
		RegistryObject<B> standingBlock = this.deferredRegister.register(name, standingSupplier);
		this.itemRegister.register(name, () -> new CorrockCrownBlockItem(standingBlock.get(), wallSupplier::get, new Item.Properties().tab(group)));
		return standingBlock;
	}

}
