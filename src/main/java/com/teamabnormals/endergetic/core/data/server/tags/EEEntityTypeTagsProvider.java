package com.teamabnormals.endergetic.core.data.server.tags;

import com.teamabnormals.endergetic.core.EndergeticExpansion;
import com.teamabnormals.endergetic.core.other.tags.EEEntityTypeTags;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

import static com.teamabnormals.endergetic.core.registry.EEEntityTypes.*;

public final class EEEntityTypeTagsProvider extends EntityTypeTagsProvider {

	public EEEntityTypeTagsProvider(PackOutput output, CompletableFuture<Provider> lookupProvider, ExistingFileHelper helper) {
		super(output, lookupProvider, EndergeticExpansion.MOD_ID, helper);
	}

	@Override
	protected void addTags(Provider provider) {
		this.tag(EEEntityTypeTags.EETLES).add(BROOD_EETLE.get(), CHARGER_EETLE.get(), GLIDER_EETLE.get());

		this.tag(EEEntityTypeTags.BOOF_BLOCK_RESISTANT).add(
				EntityType.SHULKER, EntityType.PAINTING, EntityType.VEX, EntityType.ALLAY, EntityType.ITEM_FRAME, EntityType.GLOW_ITEM_FRAME,
				BOOF_BLOCK.get(), POISE_CLUSTER.get(), BOLLOOM_BALLOON.get(), BOLLOOM_FRUIT.get()
		).addOptional(new ResourceLocation("quark", "glass_item_frame")).addOptional(new ResourceLocation("quark", "dyed_item_frame"));

		this.tag(EEEntityTypeTags.NOT_BALLOON_ATTACHABLE).add(
				BOOF_BLOCK.get(), POISE_CLUSTER.get(), PUFF_BUG.get(), BOOFLO.get(), BOOFLO_ADOLESCENT.get(), BOOFLO_BABY.get(), BROOD_EETLE.get(),
				EntityType.SHULKER, EntityType.ARMOR_STAND, EntityType.PHANTOM, EntityType.GHAST, EntityType.WITHER, EntityType.ENDER_DRAGON
		);

		this.tag(EEEntityTypeTags.TELEPORT_IMMUNE).add(
				EntityType.SHULKER, EntityType.ENDER_DRAGON, PURPOID.get(), BROOD_EETLE.get()
		);

		this.tag(EEEntityTypeTags.PORTABLE).add(
				EntityType.ITEM, EntityType.TNT, EntityType.FALLING_BLOCK
		);
	}
}
