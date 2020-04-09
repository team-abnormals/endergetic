package endergeticexpansion.core.registry.other;

import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
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
	
}