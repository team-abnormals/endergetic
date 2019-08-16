package endergeticexpansion.core.registry;

import endergeticexpansion.common.entities.EntityEndergeticBoat.Type;
import endergeticexpansion.common.items.ItemBolloomBalloon;
import endergeticexpansion.common.items.ItemBolloomFruit;
import endergeticexpansion.common.items.ItemBoofloVest;
import endergeticexpansion.common.items.ItemEndergeticBoat;
import endergeticexpansion.core.EndergeticExpansion;
import endergeticexpansion.core.registry.util.RegistryUtils;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = EndergeticExpansion.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EEItems {
	
	public static Item EUMUS_BRICK                = new Item(RegistryUtils.createSimpleItemProperty(64, ItemGroup.MATERIALS)).setRegistryName(EndergeticExpansion.MOD_ID, "eumus_brick");
	/*
	 * Poise
	 */
	public static Item POISE_BOAT                 = new ItemEndergeticBoat(Type.POISE, RegistryUtils.createSimpleItemProperty(64, ItemGroup.TRANSPORTATION)).setRegistryName(EndergeticExpansion.MOD_ID, "poise_boat");
	public static Item BOLLOOM_FRUIT              = new ItemBolloomFruit(new Item.Properties().group(ItemGroup.FOOD).food(EEFoods.BOLLOOM_FRUIT)).setRegistryName(EndergeticExpansion.MOD_ID, "bolloom_fruit");
	public static Item BOOFLO_HIDE                = new Item(RegistryUtils.createSimpleItemProperty(64, ItemGroup.MATERIALS)).setRegistryName(EndergeticExpansion.MOD_ID, "booflo_hide");
	public static Item BOLLOOM_BALLOON            = new ItemBolloomBalloon(RegistryUtils.createSimpleItemProperty(16, ItemGroup.TOOLS), null).setRegistryName(EndergeticExpansion.MOD_ID, "bolloom_balloon");
	public static Item BOLLOOM_BALLOON_RED        = new ItemBolloomBalloon(RegistryUtils.createSimpleItemProperty(16, ItemGroup.TOOLS), DyeColor.RED).setRegistryName(EndergeticExpansion.MOD_ID, "bolloom_balloon_red");
	public static Item BOLLOOM_BALLOON_ORANGE     = new ItemBolloomBalloon(RegistryUtils.createSimpleItemProperty(16, ItemGroup.TOOLS), DyeColor.ORANGE).setRegistryName(EndergeticExpansion.MOD_ID, "bolloom_balloon_orange");
	public static Item BOLLOOM_BALLOON_YELLOW     = new ItemBolloomBalloon(RegistryUtils.createSimpleItemProperty(16, ItemGroup.TOOLS), DyeColor.YELLOW).setRegistryName(EndergeticExpansion.MOD_ID, "bolloom_balloon_yellow");
	public static Item BOLLOOM_BALLOON_LIME       = new ItemBolloomBalloon(RegistryUtils.createSimpleItemProperty(16, ItemGroup.TOOLS), DyeColor.LIME).setRegistryName(EndergeticExpansion.MOD_ID, "bolloom_balloon_lime");
	public static Item BOLLOOM_BALLOON_GREEN      = new ItemBolloomBalloon(RegistryUtils.createSimpleItemProperty(16, ItemGroup.TOOLS), DyeColor.GREEN).setRegistryName(EndergeticExpansion.MOD_ID, "bolloom_balloon_green");
	public static Item BOLLOOM_BALLOON_LIGHT_BLUE = new ItemBolloomBalloon(RegistryUtils.createSimpleItemProperty(16, ItemGroup.TOOLS), DyeColor.LIGHT_BLUE).setRegistryName(EndergeticExpansion.MOD_ID, "bolloom_balloon_light_blue");
	public static Item BOLLOOM_BALLOON_CYAN       = new ItemBolloomBalloon(RegistryUtils.createSimpleItemProperty(16, ItemGroup.TOOLS), DyeColor.CYAN).setRegistryName(EndergeticExpansion.MOD_ID, "bolloom_balloon_cyan");
	public static Item BOLLOOM_BALLOON_BLUE       = new ItemBolloomBalloon(RegistryUtils.createSimpleItemProperty(16, ItemGroup.TOOLS), DyeColor.BLUE).setRegistryName(EndergeticExpansion.MOD_ID, "bolloom_balloon_blue");
	public static Item BOLLOOM_BALLOON_PINK       = new ItemBolloomBalloon(RegistryUtils.createSimpleItemProperty(16, ItemGroup.TOOLS), DyeColor.PINK).setRegistryName(EndergeticExpansion.MOD_ID, "bolloom_balloon_pink");
	public static Item BOLLOOM_BALLOON_MAGENTA    = new ItemBolloomBalloon(RegistryUtils.createSimpleItemProperty(16, ItemGroup.TOOLS), DyeColor.MAGENTA).setRegistryName(EndergeticExpansion.MOD_ID, "bolloom_balloon_magenta");
	public static Item BOLLOOM_BALLOON_PURPLE     = new ItemBolloomBalloon(RegistryUtils.createSimpleItemProperty(16, ItemGroup.TOOLS), DyeColor.PURPLE).setRegistryName(EndergeticExpansion.MOD_ID, "bolloom_balloon_purple");
	public static Item BOLLOOM_BALLOON_BROWN      = new ItemBolloomBalloon(RegistryUtils.createSimpleItemProperty(16, ItemGroup.TOOLS), DyeColor.BROWN).setRegistryName(EndergeticExpansion.MOD_ID, "bolloom_balloon_brown");
	public static Item BOLLOOM_BALLOON_GRAY       = new ItemBolloomBalloon(RegistryUtils.createSimpleItemProperty(16, ItemGroup.TOOLS), DyeColor.GRAY).setRegistryName(EndergeticExpansion.MOD_ID, "bolloom_balloon_gray");
	public static Item BOLLOOM_BALLOON_LIGHT_GRAY = new ItemBolloomBalloon(RegistryUtils.createSimpleItemProperty(16, ItemGroup.TOOLS), DyeColor.LIGHT_GRAY).setRegistryName(EndergeticExpansion.MOD_ID, "bolloom_balloon_light_gray");
	public static Item BOLLOOM_BALLOON_BLACK      = new ItemBolloomBalloon(RegistryUtils.createSimpleItemProperty(16, ItemGroup.TOOLS), DyeColor.BLACK).setRegistryName(EndergeticExpansion.MOD_ID, "bolloom_balloon_black");
	public static Item BOOFLO_VEST                = new ItemBoofloVest(RegistryUtils.createSimpleItemProperty(1, ItemGroup.COMBAT)).setRegistryName(EndergeticExpansion.MOD_ID, "booflo_vest");
	
	@SubscribeEvent
	public static void onRegisterItems(RegistryEvent.Register<Item> event) {
		final IForgeRegistry<Item> registry = event.getRegistry();
		final Item[] items = {
			EUMUS_BRICK, POISE_BOAT, BOLLOOM_FRUIT, BOOFLO_HIDE, BOLLOOM_BALLOON, BOLLOOM_BALLOON_RED, BOLLOOM_BALLOON_ORANGE, BOLLOOM_BALLOON_YELLOW, BOLLOOM_BALLOON_LIME, BOLLOOM_BALLOON_GREEN, BOLLOOM_BALLOON_LIGHT_BLUE, BOLLOOM_BALLOON_CYAN, 
			BOLLOOM_BALLOON_BLUE, BOLLOOM_BALLOON_PINK, BOLLOOM_BALLOON_MAGENTA, BOLLOOM_BALLOON_PURPLE, BOLLOOM_BALLOON_BROWN, BOLLOOM_BALLOON_GRAY, BOLLOOM_BALLOON_LIGHT_GRAY, BOLLOOM_BALLOON_BLACK, BOOFLO_VEST
		};
		registry.registerAll(items);
	}
	
}
