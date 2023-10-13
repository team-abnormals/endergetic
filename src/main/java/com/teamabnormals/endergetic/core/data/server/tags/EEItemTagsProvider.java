package com.teamabnormals.endergetic.core.data.server.tags;

import com.teamabnormals.blueprint.core.other.tags.BlueprintItemTags;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.data.ExistingFileHelper;

import static com.teamabnormals.endergetic.core.registry.EEItems.*;

public final class EEItemTagsProvider extends ItemTagsProvider {

	public EEItemTagsProvider(DataGenerator generator, BlockTagsProvider blockTags, ExistingFileHelper helper) {
		super(generator, blockTags, EndergeticExpansion.MOD_ID, helper);
	}

	@Override
	protected void addTags() {
		this.tag(ItemTags.BOATS).add(POISE_BOAT.getFirst().get());
		this.tag(ItemTags.CHEST_BOATS).add(POISE_BOAT.getSecond().get());
		this.tag(BlueprintItemTags.FURNACE_BOATS).add(POISE_FURNACE_BOAT.get());
		this.tag(BlueprintItemTags.LARGE_BOATS).add(LARGE_POISE_BOAT.get());
	}
}
