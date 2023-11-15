package com.teamabnormals.endergetic.core.registry.other.tags;

import com.teamabnormals.blueprint.core.util.TagUtil;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class EEEntityTypeTags {
	public static final TagKey<EntityType<?>> BOOF_BLOCK_RESISTANT = entityTypeTag("boof_block_resistant");
	public static final TagKey<EntityType<?>> NOT_BALLOON_ATTACHABLE = entityTypeTag("not_balloon_attachable");

	private static TagKey<EntityType<?>> entityTypeTag(String name) {
		return TagUtil.entityTypeTag(EndergeticExpansion.MOD_ID, name);
	}
}
