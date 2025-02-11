package com.teamabnormals.endergetic.core.registry;

import com.mojang.datafixers.util.Pair;
import com.teamabnormals.blueprint.common.item.BlueprintRecordItem;
import com.teamabnormals.blueprint.core.util.item.CreativeModeTabContentsPopulator;
import com.teamabnormals.endergetic.common.entity.bolloom.BalloonColor;
import com.teamabnormals.endergetic.common.item.*;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import com.teamabnormals.endergetic.core.other.EEConstants;
import com.teamabnormals.endergetic.core.registry.util.EndergeticItemSubRegistryHelper;
import com.teamabnormals.endergetic.integration.boatload.EEBoatTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.RegistryObject;

import static com.teamabnormals.blueprint.core.util.item.ItemStackUtil.is;
import static com.teamabnormals.endergetic.core.registry.EEBlocks.*;
import static net.minecraft.world.item.CreativeModeTabs.*;
import static net.minecraft.world.item.crafting.Ingredient.of;

@EventBusSubscriber(modid = EndergeticExpansion.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class EEItems {
	private static final EndergeticItemSubRegistryHelper HELPER = EndergeticExpansion.REGISTRY_HELPER.getItemSubHelper();

	public static final RegistryObject<Item> EUMUS_BRICK = HELPER.createItem("eumus_brick", () -> new Item(new Item.Properties()));

	public static final Pair<RegistryObject<Item>, RegistryObject<Item>> POISE_BOAT = HELPER.createBoatAndChestBoatItem("poise", EEBlocks.POISE_PLANKS);
	public static final RegistryObject<Item> POISE_FURNACE_BOAT = HELPER.createItem("poise_furnace_boat", ModList.get().isLoaded("boatload") ? EEBoatTypes.POISE_FURNACE_BOAT : () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> LARGE_POISE_BOAT = HELPER.createItem("large_poise_boat", ModList.get().isLoaded("boatload") ? EEBoatTypes.LARGE_POISE_BOAT : () -> new Item(new Item.Properties()));

	public static final RegistryObject<Item> BOLLOOM_FRUIT = HELPER.createItem("bolloom_fruit", () -> new BolloomFruitItem(new Item.Properties().food(EEFoods.BOLLOOM_FRUIT)));
	public static final RegistryObject<Item> BOOFLO_HIDE = HELPER.createItem("booflo_hide", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> BOOFLO_VEST = HELPER.createItem("booflo_vest", () -> new BoofloVestItem(new Item.Properties()));
	public static final RegistryObject<Item> PUFF_BUG_BOTTLE = HELPER.createItem("puffbug_bottle", () -> new PuffBugBottleItem(new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> MUSIC_DISC_KILOBYTE = HELPER.createItem("music_disc_kilobyte", () -> new BlueprintRecordItem(14, EESoundEvents.KILOBYTE, new Item.Properties().stacksTo(1).rarity(Rarity.RARE), 163));
	public static final RegistryObject<Item> COOKED_EETLE_EGG = HELPER.createItem("cooked_eetle_egg", () -> new Item(new Item.Properties().food(EEFoods.COOKED_EETLE_EGG)));
	public static final RegistryObject<Item> PORTAPLASM = HELPER.createItem("portaplasm", () -> new Item(new Item.Properties()));

	public static final RegistryObject<Item> BOLLOOM_BALLOON = HELPER.createItem("bolloom_balloon", () -> new BolloomBalloonItem(new Item.Properties(), BalloonColor.DEFAULT));
	public static final RegistryObject<Item> RED_BOLLOOM_BALLOON = HELPER.createItem("red_bolloom_balloon", () -> new BolloomBalloonItem(new Item.Properties(), BalloonColor.RED));
	public static final RegistryObject<Item> ORANGE_BOLLOOM_BALLOON = HELPER.createItem("orange_bolloom_balloon", () -> new BolloomBalloonItem(new Item.Properties(), BalloonColor.ORANGE));
	public static final RegistryObject<Item> YELLOW_BOLLOOM_BALLOON = HELPER.createItem("yellow_bolloom_balloon", () -> new BolloomBalloonItem(new Item.Properties(), BalloonColor.YELLOW));
	public static final RegistryObject<Item> LIME_BOLLOOM_BALLOON = HELPER.createItem("lime_bolloom_balloon", () -> new BolloomBalloonItem(new Item.Properties(), BalloonColor.LIME));
	public static final RegistryObject<Item> GREEN_BOLLOOM_BALLOON = HELPER.createItem("green_bolloom_balloon", () -> new BolloomBalloonItem(new Item.Properties(), BalloonColor.GREEN));
	public static final RegistryObject<Item> LIGHT_BLUE_BOLLOOM_BALLOON = HELPER.createItem("light_blue_bolloom_balloon", () -> new BolloomBalloonItem(new Item.Properties(), BalloonColor.LIGHT_BLUE));
	public static final RegistryObject<Item> CYAN_BOLLOOM_BALLOON = HELPER.createItem("cyan_bolloom_balloon", () -> new BolloomBalloonItem(new Item.Properties(), BalloonColor.CYAN));
	public static final RegistryObject<Item> BLUE_BOLLOOM_BALLOON = HELPER.createItem("blue_bolloom_balloon", () -> new BolloomBalloonItem(new Item.Properties(), BalloonColor.BLUE));
	public static final RegistryObject<Item> PINK_BOLLOOM_BALLOON = HELPER.createItem("pink_bolloom_balloon", () -> new BolloomBalloonItem(new Item.Properties(), BalloonColor.PINK));
	public static final RegistryObject<Item> MAGENTA_BOLLOOM_BALLOON = HELPER.createItem("magenta_bolloom_balloon", () -> new BolloomBalloonItem(new Item.Properties(), BalloonColor.MAGENTA));
	public static final RegistryObject<Item> PURPLE_BOLLOOM_BALLOON = HELPER.createItem("purple_bolloom_balloon", () -> new BolloomBalloonItem(new Item.Properties(), BalloonColor.PURPLE));
	public static final RegistryObject<Item> BROWN_BOLLOOM_BALLOON = HELPER.createItem("brown_bolloom_balloon", () -> new BolloomBalloonItem(new Item.Properties(), BalloonColor.BROWN));
	public static final RegistryObject<Item> GRAY_BOLLOOM_BALLOON = HELPER.createItem("gray_bolloom_balloon", () -> new BolloomBalloonItem(new Item.Properties(), BalloonColor.GRAY));
	public static final RegistryObject<Item> LIGHT_GRAY_BOLLOOM_BALLOON = HELPER.createItem("light_gray_bolloom_balloon", () -> new BolloomBalloonItem(new Item.Properties(), BalloonColor.LIGHT_GRAY));
	public static final RegistryObject<Item> WHITE_BOLLOOM_BALLOON = HELPER.createItem("white_bolloom_balloon", () -> new BolloomBalloonItem(new Item.Properties(), BalloonColor.WHITE));
	public static final RegistryObject<Item> BLACK_BOLLOOM_BALLOON = HELPER.createItem("black_bolloom_balloon", () -> new BolloomBalloonItem(new Item.Properties(), BalloonColor.BLACK));

	public static final RegistryObject<ForgeSpawnEggItem> PUFF_BUG_SPAWN_EGG = HELPER.createSpawnEggItem("puff_bug", EEEntityTypes.PUFF_BUG::get, 15660724, 16610303);
	public static final RegistryObject<ForgeSpawnEggItem> BOOFLO_SPAWN_EGG = HELPER.createSpawnEggItem("booflo", EEEntityTypes.BOOFLO::get, 8143741, 16641190);
	public static final RegistryObject<EetleSpawnEggItem> EETLE_SPAWN_EGG = HELPER.createEetleSpawnEgg();
	public static final RegistryObject<ForgeSpawnEggItem> BROOD_EETLE_SPAWN_EGG = HELPER.createSpawnEggItem("brood_eetle", EEEntityTypes.BROOD_EETLE::get, 0x6e7ebe, 0xE78FFF);
	public static final RegistryObject<ForgeSpawnEggItem> PURPOID_SPAWN_EGG = HELPER.createSpawnEggItem("purpoid", EEEntityTypes.PURPOID::get, 6240129, 11179503);

	public static void setupTabEditors() {
		CreativeModeTabContentsPopulator.mod(EndergeticExpansion.MOD_ID)
				.tab(FOOD_AND_DRINKS)
				.addItemsAfter(of(Items.CHORUS_FRUIT), BOLLOOM_FRUIT)
				.tab(TOOLS_AND_UTILITIES)
				.addItemsAfter(of(Items.MUSIC_DISC_RELIC), MUSIC_DISC_KILOBYTE)
				.addItemsAfter(ofID(EEConstants.LARGE_WARPED_BOAT), POISE_FURNACE_BOAT, LARGE_POISE_BOAT)
				.addItemsAfter(ofID(EEConstants.LARGE_WARPED_BOAT, Items.BAMBOO_CHEST_RAFT), POISE_BOAT.getFirst(), POISE_BOAT.getSecond())
				.tab(INGREDIENTS)
				.addItemsAfter(of(Items.SHULKER_SHELL), BOOFLO_HIDE)
				.addItemsAfter(of(Items.NETHER_BRICK), EUMUS_BRICK)
				.tab(TOOLS_AND_UTILITIES)
				.addItemsBefore(of(Items.FISHING_ROD), PUFF_BUG_BOTTLE)
				.addItemsAfter(of(Items.LEAD), BOLLOOM_BALLOON)
				.tab(COLORED_BLOCKS)
				.addItems(BOLLOOM_BALLOON, WHITE_BOLLOOM_BALLOON, LIGHT_GRAY_BOLLOOM_BALLOON, GRAY_BOLLOOM_BALLOON, BLACK_BOLLOOM_BALLOON, BROWN_BOLLOOM_BALLOON, RED_BOLLOOM_BALLOON, ORANGE_BOLLOOM_BALLOON, YELLOW_BOLLOOM_BALLOON, LIME_BOLLOOM_BALLOON, GREEN_BOLLOOM_BALLOON, CYAN_BOLLOOM_BALLOON, LIGHT_BLUE_BOLLOOM_BALLOON, BLUE_BOLLOOM_BALLOON, PURPLE_BOLLOOM_BALLOON, MAGENTA_BOLLOOM_BALLOON, PINK_BOLLOOM_BALLOON)
				.tab(COMBAT)
				.addItemsAfter(of(Items.TURTLE_HELMET), BOOFLO_VEST)
				.tab(SPAWN_EGGS)
				.addItemsAlphabetically(is(SpawnEggItem.class), BOOFLO_SPAWN_EGG, PUFF_BUG_SPAWN_EGG);
	}
	
	public static class EEFoods {
		public static final FoodProperties BOLLOOM_FRUIT = new FoodProperties.Builder().nutrition(2).saturationMod(0.3F).effect(() -> new MobEffectInstance(MobEffects.LEVITATION, 125, 0), 1.0F).alwaysEat().build();
		//TODO: Subject to change
		public static final FoodProperties COOKED_EETLE_EGG = new FoodProperties.Builder().nutrition(8).saturationMod(0.8F).build();
	}
}
