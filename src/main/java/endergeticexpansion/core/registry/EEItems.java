package endergeticexpansion.core.registry;

import endergeticexpansion.common.items.ItemBolloomBalloon;
import endergeticexpansion.common.items.ItemBolloomFruit;
import endergeticexpansion.common.items.ItemBoofloVest;
import endergeticexpansion.core.EndergeticExpansion;
import endergeticexpansion.core.registry.util.RegistryUtils;
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
	public static Item BOLLOOM_FRUIT   = new ItemBolloomFruit(new Item.Properties().group(ItemGroup.FOOD).food(EEFoods.BOLLOOM_FRUIT)).setRegistryName(EndergeticExpansion.MOD_ID, "bolloom_fruit");
	public static Item BOOFLO_HIDE     = new Item(RegistryUtils.createSimpleItemProperty(64, ItemGroup.MATERIALS)).setRegistryName(EndergeticExpansion.MOD_ID, "booflo_hide");
	public static Item BOLLOOM_BALLOON = new ItemBolloomBalloon(RegistryUtils.createSimpleItemProperty(16, ItemGroup.TOOLS)).setRegistryName(EndergeticExpansion.MOD_ID, "bolloom_balloon");
	public static Item BOOFLO_VEST     = new ItemBoofloVest(RegistryUtils.createSimpleItemProperty(1, ItemGroup.COMBAT)).setRegistryName(EndergeticExpansion.MOD_ID, "booflo_vest");
	
	@SubscribeEvent
	public static void onRegisterItems(RegistryEvent.Register<Item> event) {
		final IForgeRegistry<Item> registry = event.getRegistry();
		final Item[] items = {
			BOLLOOM_FRUIT, BOOFLO_HIDE, BOLLOOM_BALLOON, BOOFLO_VEST
		};
		registry.registerAll(items);
	}
	
}
