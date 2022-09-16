package com.teamabnormals.endergetic.core.registry;

import com.teamabnormals.endergetic.common.entities.bolloom.BalloonColor;
import com.teamabnormals.endergetic.common.items.*;
import com.teamabnormals.endergetic.core.EndergeticExpansion;

import com.teamabnormals.endergetic.core.registry.util.EndergeticItemSubRegistryHelper;
import com.mojang.datafixers.util.Pair;
import com.teamabnormals.blueprint.common.item.BlueprintRecordItem;
import com.teamabnormals.blueprint.core.util.registry.ItemSubRegistryHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = EndergeticExpansion.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class EEItems {
	private static final EndergeticItemSubRegistryHelper HELPER = EndergeticExpansion.REGISTRY_HELPER.getItemSubHelper();

	public static final RegistryObject<Item> EUMUS_BRICK = HELPER.createItem("eumus_brick", () -> new Item(ItemSubRegistryHelper.createSimpleItemProperty(64, CreativeModeTab.TAB_MATERIALS)));

	public static final Pair<RegistryObject<Item>, RegistryObject<Item>> POISE_BOAT = HELPER.createBoatAndChestBoatItem("poise", EEBlocks.POISE_PLANKS);
	public static final RegistryObject<Item> BOLLOOM_FRUIT = HELPER.createItem("bolloom_fruit", () -> new BolloomFruitItem(new Item.Properties().tab(CreativeModeTab.TAB_FOOD).food(EEFoods.BOLLOOM_FRUIT)));
	public static final RegistryObject<Item> BOOFLO_HIDE = HELPER.createItem("booflo_hide", () -> new Item(ItemSubRegistryHelper.createSimpleItemProperty(64, CreativeModeTab.TAB_MATERIALS)));
	public static final RegistryObject<Item> BOOFLO_VEST = HELPER.createItem("booflo_vest", () -> new BoofloVestItem(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
	public static final RegistryObject<Item> PUFFBUG_BOTTLE = HELPER.createItem("puffbug_bottle", () -> new PuffBugBottleItem(ItemSubRegistryHelper.createSimpleItemProperty(1, CreativeModeTab.TAB_MISC)));
	public static final RegistryObject<Item> MUSIC_DISC_KILOBYTE = HELPER.createItem("music_disc_kilobyte", () -> new BlueprintRecordItem(14, EESounds.KILOBYTE, new Item.Properties().stacksTo(1).tab(CreativeModeTab.TAB_MISC).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> COOKED_EETLE_EGG = HELPER.createItem("cooked_eetle_egg", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_FOOD).food(EEFoods.COOKED_EETLE_EGG)));
	public static final RegistryObject<Item> PORTAPLASM = HELPER.createItem("portaplasm", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));

	public static final RegistryObject<Item> BOLLOOM_BALLOON = HELPER.createItem("bolloom_balloon", () -> new BolloomBalloonItem(ItemSubRegistryHelper.createSimpleItemProperty(16, CreativeModeTab.TAB_TOOLS), BalloonColor.DEFAULT));
	public static final RegistryObject<Item> BOLLOOM_BALLOON_RED = HELPER.createItem("red_bolloom_balloon", () -> new BolloomBalloonItem(ItemSubRegistryHelper.createSimpleItemProperty(16, CreativeModeTab.TAB_TOOLS), BalloonColor.RED));
	public static final RegistryObject<Item> BOLLOOM_BALLOON_ORANGE = HELPER.createItem("orange_bolloom_balloon", () -> new BolloomBalloonItem(ItemSubRegistryHelper.createSimpleItemProperty(16, CreativeModeTab.TAB_TOOLS), BalloonColor.ORANGE));
	public static final RegistryObject<Item> BOLLOOM_BALLOON_YELLOW = HELPER.createItem("yellow_bolloom_balloon", () -> new BolloomBalloonItem(ItemSubRegistryHelper.createSimpleItemProperty(16, CreativeModeTab.TAB_TOOLS), BalloonColor.YELLOW));
	public static final RegistryObject<Item> BOLLOOM_BALLOON_LIME = HELPER.createItem("lime_bolloom_balloon", () -> new BolloomBalloonItem(ItemSubRegistryHelper.createSimpleItemProperty(16, CreativeModeTab.TAB_TOOLS), BalloonColor.LIME));
	public static final RegistryObject<Item> BOLLOOM_BALLOON_GREEN = HELPER.createItem("green_bolloom_balloon", () -> new BolloomBalloonItem(ItemSubRegistryHelper.createSimpleItemProperty(16, CreativeModeTab.TAB_TOOLS), BalloonColor.GREEN));
	public static final RegistryObject<Item> BOLLOOM_BALLOON_LIGHT_BLUE = HELPER.createItem("light_blue_bolloom_balloon", () -> new BolloomBalloonItem(ItemSubRegistryHelper.createSimpleItemProperty(16, CreativeModeTab.TAB_TOOLS), BalloonColor.LIGHT_BLUE));
	public static final RegistryObject<Item> BOLLOOM_BALLOON_CYAN = HELPER.createItem("cyan_bolloom_balloon", () -> new BolloomBalloonItem(ItemSubRegistryHelper.createSimpleItemProperty(16, CreativeModeTab.TAB_TOOLS), BalloonColor.CYAN));
	public static final RegistryObject<Item> BOLLOOM_BALLOON_BLUE = HELPER.createItem("blue_bolloom_balloon", () -> new BolloomBalloonItem(ItemSubRegistryHelper.createSimpleItemProperty(16, CreativeModeTab.TAB_TOOLS), BalloonColor.BLUE));
	public static final RegistryObject<Item> BOLLOOM_BALLOON_PINK = HELPER.createItem("pink_bolloom_balloon", () -> new BolloomBalloonItem(ItemSubRegistryHelper.createSimpleItemProperty(16, CreativeModeTab.TAB_TOOLS), BalloonColor.PINK));
	public static final RegistryObject<Item> BOLLOOM_BALLOON_MAGENTA = HELPER.createItem("magenta_bolloom_balloon", () -> new BolloomBalloonItem(ItemSubRegistryHelper.createSimpleItemProperty(16, CreativeModeTab.TAB_TOOLS), BalloonColor.MAGENTA));
	public static final RegistryObject<Item> BOLLOOM_BALLOON_PURPLE = HELPER.createItem("purple_bolloom_balloon", () -> new BolloomBalloonItem(ItemSubRegistryHelper.createSimpleItemProperty(16, CreativeModeTab.TAB_TOOLS), BalloonColor.PURPLE));
	public static final RegistryObject<Item> BOLLOOM_BALLOON_BROWN = HELPER.createItem("brown_bolloom_balloon", () -> new BolloomBalloonItem(ItemSubRegistryHelper.createSimpleItemProperty(16, CreativeModeTab.TAB_TOOLS), BalloonColor.BROWN));
	public static final RegistryObject<Item> BOLLOOM_BALLOON_GRAY = HELPER.createItem("gray_bolloom_balloon", () -> new BolloomBalloonItem(ItemSubRegistryHelper.createSimpleItemProperty(16, CreativeModeTab.TAB_TOOLS), BalloonColor.GRAY));
	public static final RegistryObject<Item> BOLLOOM_BALLOON_LIGHT_GRAY = HELPER.createItem("light_gray_bolloom_balloon", () -> new BolloomBalloonItem(ItemSubRegistryHelper.createSimpleItemProperty(16, CreativeModeTab.TAB_TOOLS), BalloonColor.LIGHT_GRAY));
	public static final RegistryObject<Item> BOLLOOM_BALLOON_WHITE = HELPER.createItem("white_bolloom_balloon", () -> new BolloomBalloonItem(ItemSubRegistryHelper.createSimpleItemProperty(16, CreativeModeTab.TAB_TOOLS), BalloonColor.WHITE));
	public static final RegistryObject<Item> BOLLOOM_BALLOON_BLACK = HELPER.createItem("black_bolloom_balloon", () -> new BolloomBalloonItem(ItemSubRegistryHelper.createSimpleItemProperty(16, CreativeModeTab.TAB_TOOLS), BalloonColor.BLACK));

	public static final RegistryObject<ForgeSpawnEggItem> PUFF_BUG_SPAWN_EGG = HELPER.createSpawnEggItem("puff_bug", EEEntities.PUFF_BUG::get, 15660724, 16610303);
	public static final RegistryObject<ForgeSpawnEggItem> BOOFLO_SPAWN_EGG = HELPER.createSpawnEggItem("booflo", EEEntities.BOOFLO::get, 8143741, 16641190);
	public static final RegistryObject<EetleSpawnEggItem> EETLE_SPAWN_EGG = HELPER.createEetleSpawnEgg();
	public static final RegistryObject<ForgeSpawnEggItem> PURPOID_SPAWN_EGG = HELPER.createSpawnEggItem("purpoid", EEEntities.PURPOID::get, 6240129, 11179503);
}