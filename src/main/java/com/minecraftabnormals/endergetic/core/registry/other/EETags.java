package com.minecraftabnormals.endergetic.core.registry.other;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;

public final class EETags {

	public static class Items {
		@SuppressWarnings("unused")
		private static ITag.INamedTag<Item> createTag(String name) {
			return ItemTags.bind("endergetic:" + name);
		}
	}

	public static class Blocks {
		public static final ITag.INamedTag<Block> END_PLANTABLE = createTag("end_plantable");
		public static final ITag.INamedTag<Block> POISE_PLANTABLE = createTag("poise_plantable");
		public static final ITag.INamedTag<Block> ENDER_FIRE_BASE_BLOCKS = createTag("ender_fire_base_blocks");
		public static final ITag.INamedTag<Block> END_CRYSTAL_PLACEABLE = createTag("end_crystal_placeable");
		public static final ITag.INamedTag<Block> CHORUS_PLANTABLE = createTag("chorus_plantable");

		private static ITag.INamedTag<Block> createTag(String name) {
			return BlockTags.bind("endergetic:" + name);
		}
	}

	public static class EntityTypes {
		public static final ITag.INamedTag<EntityType<?>> BOOF_BLOCK_RESISTANT = createTag("boof_block_resistant");
		public static final ITag.INamedTag<EntityType<?>> NOT_BALLOON_ATTACHABLE = createTag("not_balloon_attachable");

		private static ITag.INamedTag<EntityType<?>> createTag(String name) {
			return EntityTypeTags.bind("endergetic:" + name);
		}
	}

}