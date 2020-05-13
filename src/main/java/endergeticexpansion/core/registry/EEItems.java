package endergeticexpansion.core.registry;

import com.teamabnormals.abnormals_core.core.utils.RegistryHelper;

import endergeticexpansion.common.items.ItemBolloomBalloon;
import endergeticexpansion.common.items.ItemBolloomFruit;
import endergeticexpansion.common.items.ItemBoofloVest;
import endergeticexpansion.common.items.ItemPuffBugBottle;
import endergeticexpansion.core.EndergeticExpansion;
import endergeticexpansion.core.registry.util.EndergeticRegistryHelper;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EndergeticExpansion.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EEItems {
	public static final EndergeticRegistryHelper HELPER = EndergeticExpansion.REGISTRY_HELPER;
	
	public static final RegistryObject<Item> EUMUS_BRICK = HELPER.createItem("eumus_brick", () -> new Item(RegistryHelper.createSimpleItemProperty(64, ItemGroup.MATERIALS)));
	
	/*
	 * Poise
	 */
	public static final RegistryObject<Item> POISE_BOAT                 = HELPER.createBoatItem("poise", EEBlocks.POISE_PLANKS);
	public static final RegistryObject<Item> BOLLOOM_FRUIT              = HELPER.createItem("bolloom_fruit", () -> new ItemBolloomFruit(new Item.Properties().group(ItemGroup.FOOD).food(EEFoods.BOLLOOM_FRUIT)));
	public static final RegistryObject<Item> BOOFLO_HIDE                = HELPER.createItem("booflo_hide", () -> new Item(RegistryHelper.createSimpleItemProperty(64, ItemGroup.MATERIALS)));
	public static final RegistryObject<Item> PUFFBUG_BOTTLE             = HELPER.createItem("puffbug_bottle", () -> new ItemPuffBugBottle(RegistryHelper.createSimpleItemProperty(1, ItemGroup.MISC)));
	public static final RegistryObject<Item> BOLLOOM_BALLOON            = HELPER.createItem("bolloom_balloon", () -> new ItemBolloomBalloon(RegistryHelper.createSimpleItemProperty(16, ItemGroup.TOOLS), null));
	public static final RegistryObject<Item> BOLLOOM_BALLOON_RED        = HELPER.createItem("bolloom_balloon_red", () -> new ItemBolloomBalloon(RegistryHelper.createSimpleItemProperty(16, ItemGroup.TOOLS), DyeColor.RED));
	public static final RegistryObject<Item> BOLLOOM_BALLOON_ORANGE     = HELPER.createItem("bolloom_balloon_orange", () -> new ItemBolloomBalloon(RegistryHelper.createSimpleItemProperty(16, ItemGroup.TOOLS), DyeColor.ORANGE));
	public static final RegistryObject<Item> BOLLOOM_BALLOON_YELLOW     = HELPER.createItem("bolloom_balloon_yellow", () -> new ItemBolloomBalloon(RegistryHelper.createSimpleItemProperty(16, ItemGroup.TOOLS), DyeColor.YELLOW));
	public static final RegistryObject<Item> BOLLOOM_BALLOON_LIME       = HELPER.createItem("bolloom_balloon_lime", () -> new ItemBolloomBalloon(RegistryHelper.createSimpleItemProperty(16, ItemGroup.TOOLS), DyeColor.LIME));
	public static final RegistryObject<Item> BOLLOOM_BALLOON_GREEN      = HELPER.createItem("bolloom_balloon_green", () -> new ItemBolloomBalloon(RegistryHelper.createSimpleItemProperty(16, ItemGroup.TOOLS), DyeColor.GREEN));
	public static final RegistryObject<Item> BOLLOOM_BALLOON_LIGHT_BLUE = HELPER.createItem("bolloom_balloon_light_blue", () -> new ItemBolloomBalloon(RegistryHelper.createSimpleItemProperty(16, ItemGroup.TOOLS), DyeColor.LIGHT_BLUE));
	public static final RegistryObject<Item> BOLLOOM_BALLOON_CYAN       = HELPER.createItem("bolloom_balloon_cyan", () -> new ItemBolloomBalloon(RegistryHelper.createSimpleItemProperty(16, ItemGroup.TOOLS), DyeColor.CYAN));
	public static final RegistryObject<Item> BOLLOOM_BALLOON_BLUE       = HELPER.createItem("bolloom_balloon_blue", () -> new ItemBolloomBalloon(RegistryHelper.createSimpleItemProperty(16, ItemGroup.TOOLS), DyeColor.BLUE));
	public static final RegistryObject<Item> BOLLOOM_BALLOON_PINK       = HELPER.createItem("bolloom_balloon_pink", () -> new ItemBolloomBalloon(RegistryHelper.createSimpleItemProperty(16, ItemGroup.TOOLS), DyeColor.PINK));
	public static final RegistryObject<Item> BOLLOOM_BALLOON_MAGENTA    = HELPER.createItem("bolloom_balloon_magenta", () -> new ItemBolloomBalloon(RegistryHelper.createSimpleItemProperty(16, ItemGroup.TOOLS), DyeColor.MAGENTA));
	public static final RegistryObject<Item> BOLLOOM_BALLOON_PURPLE     = HELPER.createItem("bolloom_balloon_purple", () -> new ItemBolloomBalloon(RegistryHelper.createSimpleItemProperty(16, ItemGroup.TOOLS), DyeColor.PURPLE));
	public static final RegistryObject<Item> BOLLOOM_BALLOON_BROWN      = HELPER.createItem("bolloom_balloon_brown", () -> new ItemBolloomBalloon(RegistryHelper.createSimpleItemProperty(16, ItemGroup.TOOLS), DyeColor.BROWN));
	public static final RegistryObject<Item> BOLLOOM_BALLOON_GRAY       = HELPER.createItem("bolloom_balloon_gray", () -> new ItemBolloomBalloon(RegistryHelper.createSimpleItemProperty(16, ItemGroup.TOOLS), DyeColor.GRAY));
	public static final RegistryObject<Item> BOLLOOM_BALLOON_LIGHT_GRAY = HELPER.createItem("bolloom_balloon_light_gray", () -> new ItemBolloomBalloon(RegistryHelper.createSimpleItemProperty(16, ItemGroup.TOOLS), DyeColor.LIGHT_GRAY));
	public static final RegistryObject<Item> BOLLOOM_BALLOON_WHITE      = HELPER.createItem("bolloom_balloon_white", () -> new ItemBolloomBalloon(RegistryHelper.createSimpleItemProperty(16, ItemGroup.TOOLS), DyeColor.WHITE));
	public static final RegistryObject<Item> BOLLOOM_BALLOON_BLACK      = HELPER.createItem("bolloom_balloon_black", () -> new ItemBolloomBalloon(RegistryHelper.createSimpleItemProperty(16, ItemGroup.TOOLS), DyeColor.BLACK));
	public static final RegistryObject<Item> BOOFLO_VEST                = HELPER.createItem("booflo_vest", () -> new ItemBoofloVest(new Item.Properties().group(ItemGroup.COMBAT)));

	/*
	 * Spawn Eggs
	 */
	public static final RegistryObject<Item> PUFF_BUG_SPAWN_EGG         = HELPER.createSpawnEggItem("puff_bug", () -> EEEntities.PUFF_BUG.get(), 15660724, 16610303);
	public static final RegistryObject<Item> BOOFLO_SPAWN_EGG           = HELPER.createSpawnEggItem("booflo", () -> EEEntities.BOOFLO.get(), 8143741, 16641190);
}