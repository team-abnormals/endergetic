package endergeticexpansion.core.registry.util;

import endergeticexpansion.common.items.itemblocks.ItemBlockCorrockCrown;
import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.TallBlockItem;
import net.minecraft.item.WallOrFloorItem;

public class RegistryUtils {

	public static BlockItem createSimpleBlockItem(Block blockForInput, ItemGroup itemGroup) {
		return (BlockItem) new BlockItem(blockForInput, new Item.Properties().group(itemGroup)).setRegistryName(blockForInput.getRegistryName());
	}
	
	public static BlockItem createWallOrFloorItem(Block floorBlock, Block wallBlock, ItemGroup itemGroup) {
		return (BlockItem) new WallOrFloorItem(floorBlock, wallBlock, new Item.Properties().group(itemGroup)).setRegistryName(floorBlock.getRegistryName());
	}
	
	public static BlockItem createTallItemBlock(Block blockForInput, ItemGroup itemGroup) {
		return (BlockItem) new TallBlockItem(blockForInput, new Item.Properties().group(itemGroup)).setRegistryName(blockForInput.getRegistryName());
	}
	
	public static BlockItem createWallOrFloorItemUpsideDownAllowed(Block floorBlock, Block wallBlock, ItemGroup itemGroup) {
		return (BlockItem) new ItemBlockCorrockCrown(floorBlock, wallBlock, new Item.Properties().group(itemGroup)).setRegistryName(floorBlock.getRegistryName());
	}
	
	public static Item createSimpleItem(String name, ItemGroup itemGroup) {
		return new Item(new Item.Properties().group(itemGroup)).setRegistryName(EndergeticExpansion.MOD_ID, name);
	}
	
	public static Item createSpawnEggForEntity(@SuppressWarnings("rawtypes") EntityType entityType, int eggColor1, int eggColor2, ItemGroup itemGroup) {
		return new SpawnEggItem(entityType, eggColor1, eggColor2, new Item.Properties().group(itemGroup)).setRegistryName(entityType.getRegistryName() + "_spawn_egg");
	}
	
	public static Item.Properties createSimpleItemProperty(int stackSize, ItemGroup itemGroup){
		return new Item.Properties().group(itemGroup).maxStackSize(stackSize);
	}
	
}
