package com.teamabnormals.endergetic.core.registry.other;

import com.teamabnormals.endergetic.core.EndergeticExpansion;
import com.teamabnormals.blueprint.core.util.TagUtil;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

public final class EETags {

	public static class Items {
		@SuppressWarnings("unused")
		private static TagKey<Item> createTag(String name) {
			return TagUtil.itemTag(EndergeticExpansion.MOD_ID, name);
		}
	}

	public static class Blocks {
		public static final TagKey<Block> END_PLANTABLE = createTag("end_plantable");
		public static final TagKey<Block> POISE_PLANTABLE = createTag("poise_plantable");
		public static final TagKey<Block> ENDER_FIRE_BASE_BLOCKS = createTag("ender_fire_base_blocks");
		public static final TagKey<Block> END_CRYSTAL_PLACEABLE = createTag("end_crystal_placeable");
		public static final TagKey<Block> CHORUS_PLANTABLE = createTag("chorus_plantable");

		private static TagKey<Block> createTag(String name) {
			return TagUtil.blockTag(EndergeticExpansion.MOD_ID, name);
		}
	}

	public static class EntityTypes {
		public static final TagKey<EntityType<?>> BOOF_BLOCK_RESISTANT = createTag("boof_block_resistant");
		public static final TagKey<EntityType<?>> NOT_BALLOON_ATTACHABLE = createTag("not_balloon_attachable");

		private static TagKey<EntityType<?>> createTag(String name) {
			return TagUtil.entityTypeTag(EndergeticExpansion.MOD_ID, name);
		}
	}

	public static final class Biomes {
		public static final TagKey<Biome> HAS_EETLE_NEST = TagUtil.biomeTag(EndergeticExpansion.MOD_ID, "has_structure/eetle_nest");
	}

}