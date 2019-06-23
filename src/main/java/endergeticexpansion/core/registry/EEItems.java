package endergeticexpansion.core.registry;

import endergeticexpansion.common.items.ItemBolloomFruit;
import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = EndergeticExpansion.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EEItems {
	
	/*
	 * Poise
	 */
	public static Item BOLLOOM_FRUIT = new ItemBolloomFruit(new Item.Properties().group(ItemGroup.FOOD).func_221540_a(EEFoods.BOLLOOM_FRUIT)).setRegistryName(EndergeticExpansion.MOD_ID, "bolloom_fruit");
	
	@SubscribeEvent
	public static void onRegisterItems(RegistryEvent.Register<Item> event) {
		final IForgeRegistry<Item> registry = event.getRegistry();
		Item[] items = {
			BOLLOOM_FRUIT
		};
		registry.registerAll(items);
	}
	
}
