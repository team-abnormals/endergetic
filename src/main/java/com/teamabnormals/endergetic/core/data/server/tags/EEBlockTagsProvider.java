package com.teamabnormals.endergetic.core.data.server.tags;

import com.teamabnormals.endergetic.core.EndergeticExpansion;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public final class EEBlockTagsProvider extends BlockTagsProvider {

	public EEBlockTagsProvider(DataGenerator generator, ExistingFileHelper helper) {
		super(generator, EndergeticExpansion.MOD_ID, helper);
	}

	@Override
	protected void addTags() {
	}
}
