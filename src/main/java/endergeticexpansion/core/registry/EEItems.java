package endergeticexpansion.core.registry;

import endergeticexpansion.common.entities.EntityEndergeticBoat.Type;
import endergeticexpansion.common.items.*;
import endergeticexpansion.core.EndergeticExpansion;
import endergeticexpansion.core.registry.util.RegistryUtils;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EEItems {
	public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, EndergeticExpansion.MOD_ID);
	
	public static final RegistryObject<Item> EUMUS_BRICK = RegistryUtils.createItem("eumus_brick", () -> new Item(RegistryUtils.createSimpleItemProperty(64, ItemGroup.MATERIALS)));
	
	/*
	 * Poise
	 */
	public static final RegistryObject<Item> POISE_BOAT                 = RegistryUtils.createItem("poise_boat", () -> new ItemEndergeticBoat(Type.POISE, RegistryUtils.createSimpleItemProperty(1, ItemGroup.TRANSPORTATION)));
	public static final RegistryObject<Item> BOLLOOM_FRUIT              = RegistryUtils.createItem("bolloom_fruit", () -> new ItemBolloomFruit(new Item.Properties().group(ItemGroup.FOOD).food(EEFoods.BOLLOOM_FRUIT)));
	public static final RegistryObject<Item> BOOFLO_HIDE                = RegistryUtils.createItem("booflo_hide", () -> new Item(RegistryUtils.createSimpleItemProperty(16, ItemGroup.MATERIALS)));
	public static final RegistryObject<Item> PUFFBUG_BOTTLE             = RegistryUtils.createItem("puffbug_bottle", () -> new ItemPuffBugBottle(RegistryUtils.createSimpleItemProperty(1, ItemGroup.MISC)));
	public static final RegistryObject<Item> BOLLOOM_BALLOON            = RegistryUtils.createItem("bolloom_balloon", () -> new ItemBolloomBalloon(RegistryUtils.createSimpleItemProperty(16, ItemGroup.TOOLS), null));
	public static final RegistryObject<Item> BOLLOOM_BALLOON_RED        = RegistryUtils.createItem("bolloom_balloon_red", () -> new ItemBolloomBalloon(RegistryUtils.createSimpleItemProperty(16, ItemGroup.TOOLS), DyeColor.RED));
	public static final RegistryObject<Item> BOLLOOM_BALLOON_ORANGE     = RegistryUtils.createItem("bolloom_balloon_orange", () -> new ItemBolloomBalloon(RegistryUtils.createSimpleItemProperty(16, ItemGroup.TOOLS), DyeColor.ORANGE));
	public static final RegistryObject<Item> BOLLOOM_BALLOON_YELLOW     = RegistryUtils.createItem("bolloom_balloon_yellow", () -> new ItemBolloomBalloon(RegistryUtils.createSimpleItemProperty(16, ItemGroup.TOOLS), DyeColor.YELLOW));
	public static final RegistryObject<Item> BOLLOOM_BALLOON_LIME       = RegistryUtils.createItem("bolloom_balloon_lime", () -> new ItemBolloomBalloon(RegistryUtils.createSimpleItemProperty(16, ItemGroup.TOOLS), DyeColor.LIME));
	public static final RegistryObject<Item> BOLLOOM_BALLOON_GREEN      = RegistryUtils.createItem("bolloom_balloon_green", () -> new ItemBolloomBalloon(RegistryUtils.createSimpleItemProperty(16, ItemGroup.TOOLS), DyeColor.RED));
	public static final RegistryObject<Item> BOLLOOM_BALLOON_LIGHT_BLUE = RegistryUtils.createItem("bolloom_balloon_light_blue", () -> new ItemBolloomBalloon(RegistryUtils.createSimpleItemProperty(16, ItemGroup.TOOLS), DyeColor.RED));
	public static final RegistryObject<Item> BOLLOOM_BALLOON_CYAN       = RegistryUtils.createItem("bolloom_balloon_cyan", () -> new ItemBolloomBalloon(RegistryUtils.createSimpleItemProperty(16, ItemGroup.TOOLS), DyeColor.RED));
	public static final RegistryObject<Item> BOLLOOM_BALLOON_BLUE       = RegistryUtils.createItem("bolloom_balloon_blue", () -> new ItemBolloomBalloon(RegistryUtils.createSimpleItemProperty(16, ItemGroup.TOOLS), DyeColor.RED));
	public static final RegistryObject<Item> BOLLOOM_BALLOON_PINK       = RegistryUtils.createItem("bolloom_balloon_pink", () -> new ItemBolloomBalloon(RegistryUtils.createSimpleItemProperty(16, ItemGroup.TOOLS), DyeColor.RED));
	public static final RegistryObject<Item> BOLLOOM_BALLOON_MAGENTA    = RegistryUtils.createItem("bolloom_balloon_magenta", () -> new ItemBolloomBalloon(RegistryUtils.createSimpleItemProperty(16, ItemGroup.TOOLS), DyeColor.RED));
	public static final RegistryObject<Item> BOLLOOM_BALLOON_PURPLE     = RegistryUtils.createItem("bolloom_balloon_purple", () -> new ItemBolloomBalloon(RegistryUtils.createSimpleItemProperty(16, ItemGroup.TOOLS), DyeColor.RED));
	public static final RegistryObject<Item> BOLLOOM_BALLOON_BROWN      = RegistryUtils.createItem("bolloom_balloon_brown", () -> new ItemBolloomBalloon(RegistryUtils.createSimpleItemProperty(16, ItemGroup.TOOLS), DyeColor.RED));
	public static final RegistryObject<Item> BOLLOOM_BALLOON_GRAY       = RegistryUtils.createItem("bolloom_balloon_gray", () -> new ItemBolloomBalloon(RegistryUtils.createSimpleItemProperty(16, ItemGroup.TOOLS), DyeColor.RED));
	public static final RegistryObject<Item> BOLLOOM_BALLOON_LIGHT_GRAY = RegistryUtils.createItem("bolloom_balloon_light_gray", () -> new ItemBolloomBalloon(RegistryUtils.createSimpleItemProperty(16, ItemGroup.TOOLS), DyeColor.LIGHT_GRAY));
	public static final RegistryObject<Item> BOLLOOM_BALLOON_WHITE      = RegistryUtils.createItem("bolloom_balloon_white", () -> new ItemBolloomBalloon(RegistryUtils.createSimpleItemProperty(16, ItemGroup.TOOLS), DyeColor.WHITE));
	public static final RegistryObject<Item> BOLLOOM_BALLOON_BLACK      = RegistryUtils.createItem("bolloom_balloon_black", () -> new ItemBolloomBalloon(RegistryUtils.createSimpleItemProperty(16, ItemGroup.TOOLS), DyeColor.BLACK));
	public static final RegistryObject<Item> BOOFLO_VEST                = RegistryUtils.createItem("booflo_vest", () -> new ItemBoofloVest(RegistryUtils.createSimpleItemProperty(1, ItemGroup.COMBAT)));

	/*
	 * Spawn Eggs
	 */
	public static final RegistryObject<Item> PUFF_BUG_SPAWN_EGG         = RegistryUtils.createSpawnEggItem("puff_bug", () -> EEEntities.PUFF_BUG.get(), 15660724, 16610303);
}