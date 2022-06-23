package com.minecraftabnormals.endergetic.core.registry.other;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.Tag;
import net.minecraft.tags.ItemTags;

public final class EETags {

	public static class Items {
		@SuppressWarnings("unused")
		private static Tag.Named<Item> createTag(String name) {
			return ItemTags.bind("endergetic:" + name);
		}
	}

	public static class Blocks {
		public static final Tag.Named<Block> END_PLANTABLE = createTag("end_plantable");
		public static final Tag.Named<Block> POISE_PLANTABLE = createTag("poise_plantable");
		public static final Tag.Named<Block> ENDER_FIRE_BASE_BLOCKS = createTag("ender_fire_base_blocks");
		public static final Tag.Named<Block> END_CRYSTAL_PLACEABLE = createTag("end_crystal_placeable");
		public static final Tag.Named<Block> CHORUS_PLANTABLE = createTag("chorus_plantable");

		private static Tag.Named<Block> createTag(String name) {
			return BlockTags.bind("endergetic:" + name);
		}
	}

	public static class EntityTypes {
		public static final Tag.Named<EntityType<?>> BOOF_BLOCK_RESISTANT = createTag("boof_block_resistant");
		public static final Tag.Named<EntityType<?>> NOT_BALLOON_ATTACHABLE = createTag("not_balloon_attachable");

		private static Tag.Named<EntityType<?>> createTag(String name) {
			return EntityTypeTags.bind("endergetic:" + name);
		}
	}

}