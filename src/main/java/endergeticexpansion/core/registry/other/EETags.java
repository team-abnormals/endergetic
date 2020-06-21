package endergeticexpansion.core.registry.other;

import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

public class EETags {
	
	public static class Items {
		@SuppressWarnings("unused")
		private static Tag<Item> createTag(String name) {
			return new ItemTags.Wrapper(new ResourceLocation(EndergeticExpansion.MOD_ID, name));
		}
	}
	
	public static class Blocks {
		public static final Tag<Block> END_PLANTABLE = createTag("end_plantable");
		public static final Tag<Block> CHORUS_PLANTABLE = createTag("chorus_plantable");
		public static final Tag<Block> POISE_PLANTABLE = createTag("poise_plantable");
		
		private static Tag<Block> createTag(String name) {
			return new BlockTags.Wrapper(new ResourceLocation(EndergeticExpansion.MOD_ID, name));
		}
	}
	
	public static class EntityTypes {
		public static final Tag<EntityType<?>> BOOF_BLOCK_RESISTANT = createTag("boof_block_resistant");
		
		private static Tag<EntityType<?>> createTag(String name) {
			return new EntityTypeTags.Wrapper(new ResourceLocation(EndergeticExpansion.MOD_ID, name));
		}
	}
	
}