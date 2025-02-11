package com.teamabnormals.endergetic.core.other.tags;

import com.teamabnormals.blueprint.core.util.TagUtil;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class EEEntityTypeTags {
	public static final TagKey<EntityType<?>> BOOF_BLOCK_RESISTANT = entityTypeTag("boof_block_resistant");
	public static final TagKey<EntityType<?>> NOT_BALLOON_ATTACHABLE = entityTypeTag("not_balloon_attachable");
	public static final TagKey<EntityType<?>> EETLES = entityTypeTag("eetles");
	public static final TagKey<EntityType<?>> TELEPORT_IMMUNE = entityTypeTag("teleport_immune");
	public static final TagKey<EntityType<?>> PORTABLE = entityTypeTag("portable");

	private static TagKey<EntityType<?>> entityTypeTag(String name) {
		return TagUtil.entityTypeTag(EndergeticExpansion.MOD_ID, name);
	}
}
